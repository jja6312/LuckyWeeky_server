package io.ssafy.luckyweeky.common.infrastructure.persistence;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MyBatisSqlSessionFactory {
    private static SqlSessionFactory sqlSessionFactory;

    static {
        try {
            // 드라이버 명시적 로드
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 1. 환경 변수에서 값을 가져옴
            String url = System.getenv("DB_URL");
            String username = System.getenv("DB_USERNAME");
            String password = System.getenv("DB_PASSWORD");

            // 2. 유효성 검사
            if (url == null || username == null || password == null) {
                throw new IllegalStateException("Required environment variables are missing. Check DB_URL, DB_USERNAME, DB_PASSWORD.");
            }

            // 3. Properties 객체에 환경 변수 추가
            Properties props = new Properties();
            props.setProperty("url", url);
            props.setProperty("username", username);
            props.setProperty("password", password);

            // 4. MyBatis 설정 파일 읽기
            String resource = "mybatis-config.xml"; // 설정 파일 경로
            InputStream inputStream = Resources.getResourceAsStream(resource);

            // 5. SqlSessionFactory 생성
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, props);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize SqlSessionFactory due to IO error.", e);
        } catch (IllegalStateException e) {
            throw new RuntimeException("Failed to initialize SqlSessionFactory due to missing environment variables.", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver not found. Ensure that the MySQL Connector/J is included in the classpath.", e);
        }
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }
}
