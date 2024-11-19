package io.ssafy.luckyweeky.infrastructure.persistence;

import io.github.cdimascio.dotenv.Dotenv;
import io.ssafy.luckyweeky.dispatcher.DispatcherServlet;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MyBatisSqlSessionFactory {
    private static SqlSessionFactory sqlSessionFactory;

    static {
        try {
            // 1. .env 파일 로드
            Dotenv dotenv = Dotenv.configure()
                    .directory(DispatcherServlet.getWebInfPath()+ File.separatorChar)
                    .filename(".env") // 파일 이름 지정 (기본값: ".env")
                    .load();

            // 2. 환경 변수 가져오기
            String url = dotenv.get("DB_URL");
            String username = dotenv.get("DB_USERNAME");
            String password = dotenv.get("DB_PASSWORD");

            // 3. Properties 객체에 환경 변수 추가
            Properties props = new Properties();
            props.setProperty("url", url);
            props.setProperty("username", username);
            props.setProperty("password", password);
            // 4. MyBatis 설정 파일 읽기
            String resource = "mybatis-config.xml"; // 설정 파일 경로
            InputStream inputStream = Resources.getResourceAsStream(resource);

            // 5. SqlSessionFactory 생성 시 Properties 전달
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, props);
        } catch (IOException e) {
            throw new RuntimeException("Failed to init SqlSessionFactory.");
        }
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }
}
