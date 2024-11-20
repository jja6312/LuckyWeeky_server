package io.ssafy.luckyweeky.scheduleAi.domain;

import io.ssafy.luckyweeky.scheduleAi.application.dto.AnalyticalData;

public class AIPromptGenerator {

    private static final String INITIAL_PROMPT_TEMPLATE = "다음 데이터를 기반으로 기간 내 일정을 생성해주세요:\n\n"
            + "시작 날짜: %s\n"
            + "종료 날짜: %s\n"
            + "목표(할 일): %s\n"
            + "투자가능한 시간: %s\n"
            + "추가 요청사항: %s\n\n"
            + "일정은 MainSchedule과 SubSchedule로 나뉩니다:\n"
            + "- MainSchedule: 주요 작업 또는 프로젝트.\n"
            + "- SubSchedule: MainSchedule에서 파생된 세부 작업.\n\n"
            + "각 일정은 다음 정보를 포함해야 합니다:\n"
            + "- MainSchedule: 제목, 시작 시간, 종료 시간.\n"
            + "- SubSchedule: 제목, 설명, 시작 시간, 종료 시간, 완료 여부.\n\n"
            + "일정을 생성할 때 가능한 시간과 추가 요청사항을 반영해주세요.";

    private static final String FOLLOW_UP_PROMPT_TEMPLATE = "이전 일정을 기반으로 추가 정보를 반영하여 일정을 수정해주세요:\n\n"
            + "추가 요청사항: %s\n\n"
            + "수정된 일정을 생성할 때 가능한 시간과 추가 요청사항을 반영해주세요.";

    /**
     * AnalyticalData 데이터를 기반으로 첫 요청 프롬프트를 생성합니다.
     *
     * @param data 프롬프트 생성에 필요한 데이터
     * @return 생성된 프롬프트 문자열
     * @throws IllegalArgumentException 데이터가 유효하지 않을 경우
     */
    public static String generateInitialPrompt(AnalyticalData data) {
        validateData(data);
        return String.format(
                INITIAL_PROMPT_TEMPLATE,
                data.getStartDate(),
                data.getEndDate(),
                data.getTask(),
                data.getAvailableTime(),
                data.getAdditionalRequest() != null ? data.getAdditionalRequest() : "없음"
        );
    }

    /**
     * AnalyticalData 데이터를 기반으로 재요청 프롬프트를 생성합니다.
     *
     * @param data 프롬프트 생성에 필요한 데이터
     * @return 생성된 프롬프트 문자열
     * @throws IllegalArgumentException 데이터가 유효하지 않을 경우
     */
    public static String generateFollowUpPrompt(AnalyticalData data) {
        validateData(data);
        return String.format(
                FOLLOW_UP_PROMPT_TEMPLATE,
                data.getAdditionalRequest() != null ? data.getAdditionalRequest() : "없음"
        );
    }

    private static void validateData(AnalyticalData data) {
        if (data == null) {
            throw new IllegalArgumentException("AnalyticalData cannot be null");
        }
        // 추가적인 검증 로직이 필요하다면 여기에 추가
    }
}
