package io.ssafy.luckyweeky.application.user;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.ssafy.luckyweeky.application.Controller;
import io.ssafy.luckyweeky.dispatcher.dto.GeneralSignupUser;
import io.ssafy.luckyweeky.dispatcher.validator.FileValidator;
import io.ssafy.luckyweeky.domain.user.service.UserService;
import io.ssafy.luckyweeky.infrastructure.config.bean.XmlBeanFactory;
import io.ssafy.luckyweeky.infrastructure.util.RequestJsonParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.util.UUID;

public class SignupController implements Controller {
    private final UserService userService;

    public SignupController() {
        this.userService = (UserService) XmlBeanFactory.getBean("userService");
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response,
                        JsonObject respJson) throws Exception {
        // 1. JSON 데이터 파싱
        Part jsonPart = request.getPart("user");
        if (jsonPart == null || jsonPart.getSize() == 0) {
            throw new IllegalArgumentException("회원가입 필수 데이터 누락 에러코드 작성");
        }

        // 2. JSON 데이터 파싱
        JsonObject jsonObject = RequestJsonParser.getInstance().parse(jsonPart);

        // 기본 프로필 이미지 경로 설정
        String profileImageKey = "profile-images/default.png";

        // 2. 멀티파트 요청에서 파일 데이터 처리
        Part filePart = null;
        if (request.getContentType() != null && request.getContentType().startsWith("multipart/form-data")) {
            // 파일 파트 가져오기
            filePart = request.getPart("file");
            if (filePart != null && filePart.getSize() > 0) {
                // 파일 MIME 타입, 사이즈, 정상 이미지 검증
                FileValidator.getInstance().isValid(filePart);

                // 파일 이름과 확장자 추출
                String fileName = filePart.getSubmittedFileName();
                String fileExtension = "";
                int lastDotIndex = fileName.lastIndexOf(".");
                if (lastDotIndex != -1) {
                    fileExtension = fileName.substring(lastDotIndex);
                }

                // 고유한 파일 이름 생성
                profileImageKey = "profile-images/" + UUID.randomUUID() + fileExtension;
            }
        }

        // 3. GeneralSignupUser 객체 생성
        GeneralSignupUser user = new GeneralSignupUser(
                jsonObject.get("email").getAsString(),
                jsonObject.get("password").getAsString(),
                jsonObject.get("username").getAsString(),
                jsonObject.get("birthDate").getAsString(),
                profileImageKey
        );

        // 4. 회원 가입 서비스 호출
        if (userService.generalRegister(user, filePart)) {
            respJson.addProperty("email", user.getEmail());
        } else {
            throw new Exception("회원가입 실패 코드 작성");
        }
    }

}
