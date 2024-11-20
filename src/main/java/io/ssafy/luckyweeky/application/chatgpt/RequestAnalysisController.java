package io.ssafy.luckyweeky.application.chatgpt;

import com.google.gson.JsonObject;
import io.ssafy.luckyweeky.application.Controller;
import io.ssafy.luckyweeky.dispatcher.dto.AnalyticalData;
import io.ssafy.luckyweeky.dispatcher.validator.AnalyticalDataValidator;
import io.ssafy.luckyweeky.domain.chatgpt.prompt.CreateSchedulePrompt;
import io.ssafy.luckyweeky.domain.chatgpt.service.ChatgptService;
import io.ssafy.luckyweeky.infrastructure.config.bean.XmlBeanFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RequestAnalysisController implements Controller {
    private final ChatgptService chatgptService;

    public RequestAnalysisController() {
        this.chatgptService = (ChatgptService) XmlBeanFactory.getBean("chatgptService");
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response, JsonObject respJson) throws Exception {
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String task = request.getParameter("task");
        String availableTime = request.getParameter("availableTime");
        String additionalRequest = request.getParameter("additionalRequest")==null?"":request.getParameter("additionalRequest");

        // 유효성 검사
        AnalyticalDataValidator.validate(startDate, endDate, task, availableTime, additionalRequest);

        // AnalyticalData 객체 생성
        AnalyticalData analyticalData = new AnalyticalData(startDate, endDate, task, availableTime, additionalRequest);

        // ChatGPT 서비스 호출 예제
        String chatResponse = chatgptService.getChatResponse(CreateSchedulePrompt.createPrompt(analyticalData).toJson());

        // 응답 설정
        respJson.addProperty("data", chatResponse);
    }

}
