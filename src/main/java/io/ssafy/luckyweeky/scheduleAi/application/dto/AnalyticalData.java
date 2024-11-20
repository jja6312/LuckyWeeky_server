package io.ssafy.luckyweeky.scheduleAi.application.dto;

import java.time.LocalDate;

public class AnalyticalData {
    private LocalDate startDate;
    private LocalDate endDate;
    private String task;
    private String availableTime;
    private String additionalRequest;

    public AnalyticalData(String startDate, String endDate, String task, String availableTime, String additionalRequest) {
        this.startDate = LocalDate.parse(startDate);
        this.endDate = LocalDate.parse(endDate);;
        this.task = task;
        this.availableTime = availableTime;
        this.additionalRequest = additionalRequest;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getTask() {
        return task;
    }

    public String getAvailableTime() {
        return availableTime;
    }

    public String getAdditionalRequest() {
        return additionalRequest;
    }
}
