package io.ssafy.luckyweeky.schedule.presentation.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

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
            case "WJsdDo":{
                getThisWeekSchedules(request, response,respJson);
                break;
            }
            case "lCSZB":{
                getSchedulesByDate(request,response,respJson);
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

    private void getThisWeekSchedules(HttpServletRequest request, HttpServletResponse response, JsonObject respJson) throws ServletException, IOException {
        LocalDateTime now = LocalDateTime.now();
        Map<String ,Object> params = new HashMap<>(
                Map.of(
                        "userId", request.getAttribute("userId"),
                        "startDate", now.with(DayOfWeek.MONDAY).withHour(0).withMinute(0).withSecond(0),
                        "endDate", now.with(DayOfWeek.SUNDAY).withHour(23).withMinute(59).withSecond(59)
                )
        );
        respJson.add("schedules", JsonParser.parseString(scheduleService.getSchedulesByDateRange(params).toString()).getAsJsonArray());
    }

    private void getSchedulesByDate(HttpServletRequest request, HttpServletResponse response, JsonObject respJson) throws IOException {
        JsonObject jsonObject = RequestJsonParser.getInstance().parseFromBody(request.getReader());
        LocalDateTime date = LocalDateTime.parse(jsonObject.get("date").getAsString(),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String ,Object> params = new HashMap<>(
                Map.of(
                        "userId", request.getAttribute("userId"),
                        "startDate", date.with(DayOfWeek.MONDAY).withHour(0).withMinute(0).withSecond(0),
                        "endDate", date.with(DayOfWeek.SUNDAY).withHour(23).withMinute(59).withSecond(59)
                )
        );
        respJson.add("schedules", JsonParser.parseString(scheduleService.getSchedulesByDateRange(params).toString()).getAsJsonArray());
    }
}
