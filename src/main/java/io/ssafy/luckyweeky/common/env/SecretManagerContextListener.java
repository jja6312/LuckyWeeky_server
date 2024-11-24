package io.ssafy.luckyweeky.common.env;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.json.JSONObject;

public class SecretManagerContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            String secretJson = System.getenv("LUCKYWEEKY_ENV_VARS");
            if (secretJson == null) {
                throw new IllegalStateException("환경 변수 'LUCKYWEEKY_ENV_VARS'가 설정되지 않았습니다.");
            }

            JSONObject secrets = new JSONObject(secretJson);
            secrets.keySet().forEach(key -> {
                String value = secrets.getString(key);
                System.setProperty(key, value);
                System.out.println("환경 변수 '" + key + "'가 설정되었습니다.");
            });

        } catch (Exception e) {
            throw new RuntimeException("환경 변수 초기화에 실패했습니다.", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 필요 시 리소스 정리
    }
}
