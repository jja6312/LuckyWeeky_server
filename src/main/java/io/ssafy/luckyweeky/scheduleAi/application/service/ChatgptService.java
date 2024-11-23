package io.ssafy.luckyweeky.scheduleAi.application.service;

import io.github.cdimascio.dotenv.Dotenv;
import io.ssafy.luckyweeky.common.DispatcherServlet;
import io.ssafy.luckyweeky.scheduleAi.domain.prompt.AIPromptGenerator;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ChatgptService {
    private final String OPENAI_API_KEY;
    private final String GPT_API_ENDPOINT;
    private final OkHttpClient client;

    public ChatgptService() {
        // 환경 변수 로드
        Dotenv dotenv = Dotenv.configure()
                .directory(DispatcherServlet.getWebInfPath() + File.separator)
                .filename(".env")
                .load();

        this.OPENAI_API_KEY = dotenv.get("OPENAI_API_KEY");
        this.GPT_API_ENDPOINT = dotenv.get("GPT_API_ENDPOINT");

        // OkHttpClient 초기화
        this.client = new OkHttpClient.Builder()
                .connectTimeout(260, TimeUnit.SECONDS)
                .readTimeout(260, TimeUnit.SECONDS)
                .writeTimeout(260, TimeUnit.SECONDS)
                .build();
    }

    // ChatGPT API 호출
    public String createChat(String prompt) throws IOException {
        if (OPENAI_API_KEY == null || OPENAI_API_KEY.isEmpty()) {
            throw new IllegalStateException("API key is not set.");
        }

        // JSON 요청 생성
        JSONObject json = new JSONObject();
        json.put("model", "gpt-4"); // 변경된 모델 이름
        json.put("messages", new JSONArray()
                .put(new JSONObject()
                        .put("role", "system")
                        .put("content", AIPromptGenerator.INITIAL_PROMPT_TEMPLATE))
                .put(new JSONObject()
                        .put("role", "user")
                        .put("content", prompt))
        );
        // 입력 토큰 수 추정값
        int estimatedInputTokens = prompt.length() / 4 + AIPromptGenerator.INITIAL_PROMPT_TEMPLATE.length() / 4;

        // max_tokens 설정
        json.put("max_tokens", Math.min(8000 - estimatedInputTokens, 4000)); // 안전 범위 내 설정
        json.put("temperature", 0.6);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"),
                json.toString()
        );

        // HTTP 요청 생성 및 실행
        Request request = new Request.Builder()
                .url(GPT_API_ENDPOINT)
                .addHeader("Authorization", "Bearer " + OPENAI_API_KEY.trim())
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response.code());
            }

            // 응답에서 메시지 추출
            return extractMessageFromChatResponse(new String(response.body().bytes(), "UTF-8"));
        }
    }

    private String extractMessageFromChatResponse(String responseBody) {
        JSONObject jsonResponse = new JSONObject(responseBody);
        JSONArray choices = jsonResponse.optJSONArray("choices");

        if (choices != null && choices.length() > 0) {
            JSONObject messageObject = choices.getJSONObject(0).optJSONObject("message");
            if (messageObject != null) {
                return messageObject.optString("content", "No content available");
            }
        }

        return "No content available";
    }
}
