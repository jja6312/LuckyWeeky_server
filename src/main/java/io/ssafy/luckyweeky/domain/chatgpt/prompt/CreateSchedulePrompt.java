package io.ssafy.luckyweeky.domain.chatgpt.prompt;

import io.ssafy.luckyweeky.dispatcher.dto.AnalyticalData;

public class CreateSchedulePrompt {
    private String role;
    private String content;

    public CreateSchedulePrompt(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String toJson() {
        return String.format("{\"role\": \"%s\", \"content\": \"%s\"}", role, content);
    }

    public String getRole() {
        return role;
    }

    public String getContent() {
        return content;
    }

    /**
     * AnalyticalData 데이터를 기반으로 프롬프트를 생성하는 메서드
     */
    public static CreateSchedulePrompt createPrompt(AnalyticalData data) {
        String content = String.format(
                "다음 데이터를 기반으로 기간 내 일정을 생성해주세요:\n\n" +
                        "시작 날짜: %s\n" +
                        "종료 날짜: %s\n" +
                        "목표(할 일): %s\n" +
                        "투자가능한 시간: %s\n" +
                        "추가 요청사항: %s\n\n" +
                        "일정은 MainSchedule과 SubSchedule로 나뉩니다:\n" +
                        "- MainSchedule: 주요 작업 또는 프로젝트.\n" +
                        "- SubSchedule: MainSchedule에서 파생된 세부 작업.\n\n" +
                        "각 일정은 다음 정보를 포함해야 합니다:\n" +
                        "- MainSchedule: 제목, 시작 시간, 종료 시간.\n" +
                        "- SubSchedule: 제목, 설명, 시작 시간, 종료 시간, 완료 여부.\n\n" +
                        "일정을 생성할 때 가능한 시간과 추가 요청사항을 반영해주세요.",
                data.getStartDate(),
                data.getEndDate(),
                data.getTask(),
                data.getAvailableTime(),
                data.getAdditionalRequest() != null ? data.getAdditionalRequest() : "없음"
        );

        return new CreateSchedulePrompt("user", content);
    }
}
