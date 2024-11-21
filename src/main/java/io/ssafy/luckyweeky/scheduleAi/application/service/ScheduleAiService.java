package io.ssafy.luckyweeky.scheduleAi.application.service;

import io.ssafy.luckyweeky.common.config.XmlBeanFactory;
import io.ssafy.luckyweeky.scheduleAi.application.dto.AnalyticalData;
import io.ssafy.luckyweeky.scheduleAi.domain.prompt.AIPromptGenerator;

import java.io.IOException;

public class ScheduleAiService {
    private final ChatgptService chatgptService;

    public ScheduleAiService() {
        this.chatgptService = (ChatgptService) XmlBeanFactory.getBean("chatgptService");

    }
    public String generateSchedule(AnalyticalData analyticalData) throws IOException {
        // 프롬프트 생성
        String prompt = AIPromptGenerator.generateInitialPrompt(analyticalData);

        // ChatGPT 호출
        return chatgptService.createChat(prompt);
    }
}
