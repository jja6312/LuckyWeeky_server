package io.ssafy.luckyweeky.scheduleAi.application.service;

import io.github.cdimascio.dotenv.Dotenv;
import io.ssafy.luckyweeky.common.DispatcherServlet;

import java.io.File;

public class ChatgptService {
    private static final ChatgptService instance = new ChatgptService();
    private final String CHAT_GPT_API_KEY;

    private ChatgptService() {
        Dotenv dotenv = Dotenv.configure()
                .directory(DispatcherServlet.getWebInfPath()+ File.separatorChar)
                .filename(".env") // 파일 이름 지정 (기본값: ".env")
                .load();
        CHAT_GPT_API_KEY = dotenv.get("CHAT_GPT_API_KEY");
    }

    public static ChatgptService getInstance() {
        return instance;
    }

    public String getChatResponse(String prompt) {
        return callChatGPTAPI(prompt);
    }

    private String callChatGPTAPI(String prompt) {

        return null;
    }
}
