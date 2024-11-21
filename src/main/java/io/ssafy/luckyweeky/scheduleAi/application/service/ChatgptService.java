package io.ssafy.luckyweeky.scheduleAi.application.service;

import io.github.cdimascio.dotenv.Dotenv;
import io.ssafy.luckyweeky.common.DispatcherServlet;
import io.ssafy.luckyweeky.scheduleAi.domain.prompt.AIPromptGenerator;
import io.ssafy.luckyweeky.scheduleAi.application.dto.AnalyticalData;
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

        System.out.println("OPENAI_API_KEY: " + OPENAI_API_KEY);
        System.out.println("GPT_API_ENDPOINT: " + GPT_API_ENDPOINT);

        // OkHttpClient 초기화
        this.client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    // ChatGPT API 호출
    public String createChat(String prompt) throws IOException {
        if (OPENAI_API_KEY == null || OPENAI_API_KEY.isEmpty()) {
            throw new IllegalStateException("API key is not set.");
        }

        // JSON 요청 생성
        JSONObject json = new JSONObject();
        json.put("model", "gpt-3.5-turbo");
        json.put("messages", new JSONArray()
                .put(new JSONObject()
                        .put("role", "system")
                        .put("content", AIPromptGenerator.INITIAL_PROMPT_TEMPLATE))
                .put(new JSONObject()
                        .put("role", "user")
                        .put("content", prompt))
        );
        json.put("max_tokens", 300);
        json.put("temperature", 0.7);

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
            return extractMessageFromChatResponse(response.body().string());
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
