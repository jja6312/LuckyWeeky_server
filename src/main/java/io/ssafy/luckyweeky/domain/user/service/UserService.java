package io.ssafy.luckyweeky.domain.user.service;

import io.ssafy.luckyweeky.dispatcher.dto.GeneralSignupUser;
import io.ssafy.luckyweeky.dispatcher.dto.LoginUser;
import io.ssafy.luckyweeky.domain.user.model.UserEntity;
import io.ssafy.luckyweeky.domain.user.model.UserSaltEntity;
import io.ssafy.luckyweeky.domain.user.repository.UserRepository;
import io.ssafy.luckyweeky.infrastructure.config.bean.XmlBeanFactory;
import io.ssafy.luckyweeky.infrastructure.storage.S3Fileloader;
import io.ssafy.luckyweeky.infrastructure.util.OpenCrypt;
import io.ssafy.luckyweeky.infrastructure.util.SnowflakeIdGenerator;
import jakarta.servlet.http.Part;

import java.io.File;

public class UserService {
    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = (UserRepository) XmlBeanFactory.getBean("userRepository");
    }

    /**
     * 이메일 존재 여부 체크
     *
     * @param email 확인할 이메일
     * @return 이메일 존재 시 true, 존재하지 않으면 false
     */
    public boolean isEmailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    /**
     * 로그인 처리
     *
     * @param loginUser 사용자 정보(email,password)
     * @return 로그인 성공 시 User 객체 반환, 실패 시 null 반환
     */
    public UserEntity login(LoginUser loginUser) {
        String salt = userRepository.getUserSalt(loginUser.getEmail());
        UserEntity user = userRepository.findByEmail(loginUser.getEmail());
        if (salt != null && user != null && user.getPasswordHash().equals(OpenCrypt.getEncryptPassword(loginUser.getPassword(), salt))) {
            return user; // 로그인 성공
        }
        return null; // 로그인 실패
    }

    /**
     * 회원가입 처리
     *
     * @param generalSignupUser 사용자 회원가입 정보
     * @param filePart          사용자 프로필 이미지정보
     * @return 회원가입 성공 시 true, 실패 시 false
     */
    public boolean generalRegister(GeneralSignupUser generalSignupUser, Part filePart) throws Exception {
        // 이미 이메일이 존재하면 회원가입 실패
        if (isEmailExists(generalSignupUser.getEmail())) {
            return false;
        }
        if (filePart != null) {
            File tempFile = null;
            try {
                int lastDotIndex = generalSignupUser.getProfileImageKey().lastIndexOf(".");
                String extension = (lastDotIndex != -1) ? generalSignupUser.getProfileImageKey().substring(lastDotIndex) : ".jpg";
                // 임시 파일 생성
                tempFile = File.createTempFile("upload-", extension);
                // 파일 쓰기
                filePart.write(tempFile.getAbsolutePath());
                // S3에 파일 등록
                S3Fileloader.getInstance().upload(tempFile, generalSignupUser.getProfileImageKey());
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            } finally {
                if (tempFile != null && tempFile.exists()) {
                    // 임시 파일 삭제
                    tempFile.delete();
                }
            }
        }

        String salt = OpenCrypt.createEncryptSalt();
        UserEntity userEntity = new UserEntity.Builder()
                .userId(SnowflakeIdGenerator.getInstance().nextId())
                .passwordHash(OpenCrypt.getEncryptPassword(generalSignupUser.getPassword(),salt))
                .username(generalSignupUser.getUsername())
                .email(generalSignupUser.getEmail())
                .birthDate(generalSignupUser.getBirthDate())
                .profileImageKey(generalSignupUser.getProfileImageKey())
                .build();
        UserSaltEntity userSaltEntity = new UserSaltEntity(userEntity.getUserId(), salt);
        return userRepository.insertUser(userEntity, userSaltEntity);
    }
}
