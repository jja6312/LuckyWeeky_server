package io.ssafy.luckyweeky.common.infrastructure.persistence;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MyBatisSqlSessionFactory {
    private static volatile SqlSessionFactory sqlSessionFactory;

    public static SqlSessionFactory getSqlSessionFactory() {
        if (sqlSessionFactory == null) {
            synchronized (MyBatisSqlSessionFactory.class) {
                if (sqlSessionFactory == null) {
                    sqlSessionFactory = initializeFactory();
                }
            }
        }
        return sqlSessionFactory;
    }

    private static SqlSessionFactory initializeFactory() {
        try {
            // 드라이버 명시적 로드
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 1. 환경 변수에서 값을 가져옴
            String DB_URL = System.getenv("DB_URL");
            String DB_USERNAME = System.getenv("DB_USERNAME");
            String DB_PASSWORD = System.getenv("DB_PASSWORD");

            // 2. 유효성 검사
            if (DB_URL == null || DB_USERNAME == null || DB_PASSWORD == null) {
                throw new IllegalStateException("Required environment variables are missing. Check DB_URL, DB_USERNAME, DB_PASSWORD.");
            }

            // 3. Properties 객체에 환경 변수 추가
            Properties props = new Properties();
            props.setProperty("DB_URL", DB_URL);
            props.setProperty("DB_USERNAME", DB_USERNAME);
            props.setProperty("DB_PASSWORD", DB_PASSWORD);

            // 4. MyBatis 설정 파일 읽기
            String resource = "mybatis-config.xml"; // 설정 파일 경로
            try (InputStream inputStream = Resources.getResourceAsStream(resource)) {
                // 5. SqlSessionFactory 생성
                return new SqlSessionFactoryBuilder().build(inputStream, props);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize SqlSessionFactory due to IO error.", e);
        } catch (IllegalStateException e) {
            throw new RuntimeException("Failed to initialize SqlSessionFactory due to missing environment variables.", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver not found. Ensure that the MySQL Connector/J is included in the classpath.", e);
        }
    }
}
