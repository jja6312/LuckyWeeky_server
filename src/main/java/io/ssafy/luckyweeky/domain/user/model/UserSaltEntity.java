package io.ssafy.luckyweeky.domain.user.model;

public class UserSaltEntity {
    private long userId;
    private String salt;

    public UserSaltEntity(long userId, String salt) {
        this.userId = userId;
        this.salt = salt;
    }

    // Getters and Setters
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
