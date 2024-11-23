package io.ssafy.luckyweeky.user.application.service;

import io.ssafy.luckyweeky.common.DispatcherServlet;
import io.ssafy.luckyweeky.common.config.XmlBeanFactory;
import io.ssafy.luckyweeky.user.application.converter.GeneralSignupUserDtoToUserEntityWithSaltConverter;
import io.ssafy.luckyweeky.user.application.converter.UserEntityWithSalt;
import io.ssafy.luckyweeky.common.infrastructure.s3.S3Fileloader;
import io.ssafy.luckyweeky.common.util.security.OpenCrypt;
import io.ssafy.luckyweeky.user.application.dto.GeneralSignupUserDto;
import io.ssafy.luckyweeky.user.application.dto.LoginUserDto;
import io.ssafy.luckyweeky.user.domain.model.UserEntity;
import io.ssafy.luckyweeky.user.domain.model.UserSaltEntity;
import io.ssafy.luckyweeky.user.infrastructure.repository.UserRepository;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;

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
    public UserEntity login(LoginUserDto loginUser) {
        String salt = userRepository.getUserSalt(loginUser.getEmail());
        UserEntity user = userRepository.findByEmail(loginUser.getEmail());
        if (salt != null && user != null && user.getPasswordHash().equals(OpenCrypt.getEncryptPassword(loginUser.getPassword(), salt))) {
            return user; // 로그인 성공
        }
        return null; // 로그인 실패
    }

    /**
     * 회원가입 처리
     * @param generalSignupUser 사용자 회원가입 정보
     * @param filePart          사용자 프로필 이미지정보
     * @return 회원가입 성공 시 true, 실패 시 false
     */
    public boolean generalRegister(GeneralSignupUserDto generalSignupUser, Part filePart) throws IOException {
        // 이미 이메일이 존재하면 회원가입 실패
        if (isEmailExists(generalSignupUser.getEmail())) {
            return false;
        }

        if (filePart != null) {
            File tempFile = null;
            try {
                // 파일 확장자 추출
                int lastDotIndex = generalSignupUser.getProfileImageKey().lastIndexOf(".");
                String extension = (lastDotIndex != -1) ? generalSignupUser.getProfileImageKey().substring(lastDotIndex) : ".jpg";

                // 프로젝트 디렉토리 기준으로 임시 파일 저장 경로 설정
                String tempDirPath = DispatcherServlet.getWebInfPath()+"/temp";
                File tempDir = new File(tempDirPath);
                if (!tempDir.exists()) {
                    tempDir.mkdirs(); // 디렉토리가 없으면 생성
                }

                // 임시 파일 생성
                tempFile = File.createTempFile("upload-", extension, tempDir);

                // 파일 쓰기
                filePart.write(tempFile.getAbsolutePath());

                // S3에 파일 등록
                S3Fileloader.getInstance().upload(tempFile, generalSignupUser.getProfileImageKey());
            } catch (IOException e) {
                throw new IOException("파일 업로드 에러 코드 작성");
            } finally {
                if (tempFile != null && tempFile.exists()) {
                    // 임시 파일 삭제
                    tempFile.delete();
                }
            }
        }
        UserEntityWithSalt result = GeneralSignupUserDtoToUserEntityWithSaltConverter.getInstance().convert(generalSignupUser);
        String salt = result.getSalt();
        UserEntity userEntity = result.getUserEntity();
        UserSaltEntity userSaltEntity = new UserSaltEntity(userEntity.getUserId(), salt);
        return userRepository.insertUser(userEntity, userSaltEntity);
    }
}
