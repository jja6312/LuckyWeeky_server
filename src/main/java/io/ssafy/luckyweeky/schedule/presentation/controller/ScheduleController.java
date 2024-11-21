package io.ssafy.luckyweeky.schedule.presentation.controller;

import com.google.gson.JsonObject;
import io.ssafy.luckyweeky.common.config.XmlBeanFactory;
import io.ssafy.luckyweeky.common.implement.Controller;
import io.ssafy.luckyweeky.common.util.parser.RequestJsonParser;
import io.ssafy.luckyweeky.common.util.url.RequestUrlPath;
import io.ssafy.luckyweeky.schedule.application.dto.ScheduleDto;
import io.ssafy.luckyweeky.schedule.application.service.ScheduleService;
import io.ssafy.luckyweeky.schedule.presentation.converter.JsonObjectToScheduleDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ScheduleController implements Controller {
    private final ScheduleService scheduleService;

    public ScheduleController() {
        this.scheduleService =(ScheduleService) XmlBeanFactory.getBean("scheduleService");
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response, JsonObject respJson) throws ServletException, IOException {
        String action = RequestUrlPath.getURI(request.getRequestURI())[1];

        switch (action){
            case "OqwSjA":{
                addSchedule(request, response,respJson);
                break;
            }
        }
    }

    private void addSchedule(HttpServletRequest request, HttpServletResponse response, JsonObject respJson) throws ServletException, IOException {
        // 요청 본문에서 JSON 데이터를 파싱
        JsonObject jsonObject = RequestJsonParser.getInstance().parseFromBody(request.getReader());
        ScheduleDto scheduleDto = JsonObjectToScheduleDto.getInstance().convert(jsonObject);
        if(scheduleDto==null){
            throw new IllegalArgumentException("request body is invalid");
        }
        if(!scheduleService.addSchedule(scheduleDto)){
            throw new IllegalArgumentException("schedule register failed");
        }
    }
}
