package io.ssafy.luckyweeky.common.env;

import org.json.JSONObject;

public class SecretManagerInit {
    public SecretManagerInit() {
        // 1. ECS에서 전달된 환경 변수 LUCKYWEEKY_ENV_VARS 읽기
        String secretJson = System.getenv("LUCKYWEEKY_ENV_VARS");
        if (secretJson == null) {
            throw new IllegalStateException("환경 변수 'LUCKYWEEKY_ENV_VARS'가 설정되지 않았습니다.");
        }

        // 2. JSON 파싱
        JSONObject secrets = new JSONObject(secretJson);

        // 3. JVM 환경 변수처럼 설정
        secrets.keySet().forEach(key -> {
            String value = secrets.getString(key);
            System.setProperty(key, value); // JVM 시스템 속성에 추가
        });

        // 4. 설정 확인 및 사용
        System.out.println("DB_URL: " + System.getProperty("DB_URL"));
        System.out.println("DB_USERNAME: " + System.getProperty("DB_USERNAME"));
        System.out.println("DB_PASSWORD: " + System.getProperty("DB_PASSWORD"));
    }
}