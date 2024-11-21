package io.ssafy.luckyweeky.schedule.application.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubScheduleDto {
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public SubScheduleDto(String title, String description, String startTime, String endTime) {
        this.title = title;
        this.description = description;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.startTime = LocalDateTime.parse(startTime,formatter);
        this.endTime = LocalDateTime.parse(endTime,formatter);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}
