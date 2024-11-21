package io.ssafy.luckyweeky.scheduleAi.presentation;

import com.google.gson.JsonObject;
import io.ssafy.luckyweeky.common.config.XmlBeanFactory;
import io.ssafy.luckyweeky.common.implement.Controller;
import io.ssafy.luckyweeky.common.util.parser.RequestJsonParser;
import io.ssafy.luckyweeky.common.util.url.RequestUrlPath;
import io.ssafy.luckyweeky.scheduleAi.application.dto.AnalyticalData;
import io.ssafy.luckyweeky.scheduleAi.application.service.ScheduleAiService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;

public class ScheduleAiController implements Controller {

    private final ScheduleAiService scheduleAiService;

    public ScheduleAiController() {
        this.scheduleAiService = (ScheduleAiService) XmlBeanFactory.getBean("scheduleAiService");
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response, JsonObject respJson) throws ServletException, IOException {
        String action = RequestUrlPath.getURI(request.getRequestURI())[1]; // URI에서 액션 추출

        try {
            switch (action) {
                case "SmdBid": { // 일정 생성 요청
                    generateSchedule(request, response, respJson);
                    break;
                }
                case "LdslbEd": { // 다른 액션 처리
                    // 추가 액션 처리 로직
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Unsupported action: " + action);
                }
            }
        } catch (Exception e) {
            respJson.addProperty("result", "false");
            respJson.addProperty("error", e.getMessage());
        }
    }

    private void generateSchedule(HttpServletRequest request, HttpServletResponse response, JsonObject respJson) throws IOException, ServletException {
        // JSON 데이터 파싱
        JsonObject requestData = null;
        try (BufferedReader reader = request.getReader()) {
            requestData = RequestJsonParser.getInstance().parseFromBody(reader);
        };

        // DTO 생성
        AnalyticalData analyticalData = new AnalyticalData(
                LocalDateTime.parse(requestData.get("startDate").getAsString()), // LocalDateTime으로 변환
                LocalDateTime.parse(requestData.get("endDate").getAsString()),   // LocalDateTime으로 변환
                requestData.get("task").getAsString(),
                requestData.get("availableTime").getAsString(),
                requestData.has("additionalRequest") ? requestData.get("additionalRequest").getAsString() : null
        );


        // 서비스 호출
        String aiGeneratedResult = scheduleAiService.generateSchedule(analyticalData);

        // 응답 데이터 구성
        respJson.addProperty("result", "true");
        respJson.addProperty("schedule", aiGeneratedResult);
    }

}
