package io.ssafy.luckyweeky.dispatcher.dto;

import java.time.LocalDate;

public class GoogleSignupUser {
    private String email;                // 이메일
    private String password;             // 비밀번호
    private String username;             // 사용자 이름
    private LocalDate birthDate;         // 생년월일
    private String profileImageKey;      // 프로필 이미지 URL
    private String oauthId;              // Google OAuth에서 제공한 고유 ID
    private String provider;             // OAuth 제공자 정보 (예: "google")

    public GoogleSignupUser(String email, String password, String username, String birthDate, String profileImageKey, String oauthId, String provider) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.birthDate = LocalDate.parse(birthDate);
        this.profileImageKey = profileImageKey;
        this.oauthId = oauthId;
        this.provider = provider;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getProfileImageKey() {
        return profileImageKey;
    }

    public String getOauthId() {
        return oauthId;
    }

    public String getProvider() {
        return provider;
    }
}
