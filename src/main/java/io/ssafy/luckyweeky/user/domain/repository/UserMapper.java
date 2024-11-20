package io.ssafy.luckyweeky.user.domain.repository;

import io.ssafy.luckyweeky.user.domain.model.UserEntity;
import io.ssafy.luckyweeky.user.domain.model.UserSaltEntity;

public interface UserMapper {
    UserEntity findById(long userId);
    UserEntity findByEmail(String email);
    void insertUser(UserEntity user);
    void insertUserSalt(UserSaltEntity userSaltEntity);
    String findSaltByEmail(String email);
}
