package io.ssafy.luckyweeky.dispatcher.validator;

import io.ssafy.luckyweeky.dispatcher.dto.LoginUser;

import java.util.regex.Pattern;

public class LoginUserValidator {
    private String errorCode;

    // 유효성 검사
    public boolean isValid(LoginUser request) {
        if (request.getEmail() == null || !isValidEmail(request.getEmail())) {
            errorCode = "U01";
            return false;
        }
        if (request.getPassword() == null || request.getPassword().length() < 10) {
            errorCode = "U02";
            return false;
        }
        return true;
    }

    // 이메일 유효성 검사
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.matches(emailRegex, email);
    }

    // 에러 메시지 반환
    public String getErrorCode() {
        return errorCode;
    }
}