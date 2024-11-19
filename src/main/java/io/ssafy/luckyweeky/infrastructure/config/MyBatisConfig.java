package io.ssafy.luckyweeky.infrastructure.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.Reader;

public class MyBatisConfig {
    private static SqlSessionFactory sqlSessionFactory;

    public static SqlSessionFactory getSqlSessionFactory() {
        if (sqlSessionFactory == null) { // 객체가 없을 경우 초기화
            synchronized (MyBatisConfig.class) { // 멀티스레드 환경을 대비해 동기화 블록 사용
                if (sqlSessionFactory == null) {
                    try {
                        // .env 파일 로드
                        Dotenv dotenv = Dotenv.load();
                        String dbUrl = dotenv.get("DB_URL");
                        String dbUsername = dotenv.get("DB_USERNAME");
                        String dbPassword = dotenv.get("DB_PASSWORD");

                        // MyBatis 설정 파일 로드
                        Reader reader = Resources.getResourceAsReader("mybatis-config.xml");

                        // DataSource 생성
                        PooledDataSource dataSource = new PooledDataSource();
                        dataSource.setDriver("com.mysql.cj.jdbc.Driver");
                        dataSource.setUrl(dbUrl);
                        dataSource.setUsername(dbUsername);
                        dataSource.setPassword(dbPassword);

                        // SqlSessionFactory 생성
                        sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
                        sqlSessionFactory.getConfiguration().setEnvironment(
                                new org.apache.ibatis.mapping.Environment(
                                        "development",
                                        new org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory(),
                                        dataSource
                                )
                        );
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to create SqlSessionFactory", e);
                    }
                }
            }
        }
        return sqlSessionFactory;
    }
}
