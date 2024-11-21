package io.ssafy.luckyweeky.schedule.application.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ScheduleDto {
    private Long userId;
    private String mainTitle;
    private String color;
    private LocalDateTime startTime; // 시작 시간
    private LocalDateTime endTime; // 종
    private List<SubScheduleDto> subSchedules;

    public ScheduleDto(Long userId,String mainTitle, String color, String startTime, String endTime, List<SubScheduleDto> subSchedules) {
        this.userId = userId;
        this.mainTitle = mainTitle;
        this.color = color;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.startTime = LocalDateTime.parse(startTime,formatter);
        this.endTime = LocalDateTime.parse(endTime,formatter);
        this.subSchedules = subSchedules;
    }

    public Long getUserId() {
        return userId;
    }

    public String getMainTitle() {
        return mainTitle;
    }

    public String getColor() {
        return color;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public List<SubScheduleDto> getSubSchedules() {
        return subSchedules;
    }
}
