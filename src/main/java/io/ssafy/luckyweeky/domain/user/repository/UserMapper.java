package io.ssafy.luckyweeky.domain.user.repository;

import io.ssafy.luckyweeky.domain.user.model.UserEntity;
import io.ssafy.luckyweeky.domain.user.model.UserSaltEntity;

public interface UserMapper {
    UserEntity findById(long userId);
    UserEntity findByEmail(String email);
    void insertUser(UserEntity user);
    void insertUserSalt(UserSaltEntity userSaltEntity);
    String findSaltByEmail(String email);
}
