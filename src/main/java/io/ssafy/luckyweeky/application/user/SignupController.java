package io.ssafy.luckyweeky.application.user;

import com.google.gson.JsonObject;
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
    public void service(HttpServletRequest request, HttpServletResponse response, JsonObject reqjson,
                        JsonObject respJson) throws Exception {
        // JSON 데이터 파싱
        Part jsonPart = request.getPart("json");
        JsonObject jsonObject = RequestJsonParser.getInstance().parse(jsonPart);

        // 파일 데이터 처리
        Part filePart = (request.getPart("file")!=null&&request.getPart("file").getSize()>0)?request.getPart("file"):null;

        // 파일 MIME 타입, 사이즈, 정상 이미지 검증 -> 검증 실패시 에러 코드 작성
        FileValidator.getInstance().isValid(filePart);


        String profileImageUrl = "profile-images/default.png";
        if(filePart != null){
            String fileName = filePart.getSubmittedFileName();
            int lastDotIndex = fileName.lastIndexOf(".");
            if (lastDotIndex != -1) {
                profileImageUrl="profile-images/"+UUID.randomUUID()+fileName.substring(lastDotIndex);
            }
        }
        GeneralSignupUser user = new GeneralSignupUser(
                jsonObject.get("email").getAsString(),
                jsonObject.get("password").getAsString(),
                jsonObject.get("username").getAsString(),
                jsonObject.get("birthDate").getAsString(),
                profileImageUrl
        );

        if(userService.generalRegister(user,filePart)){
            respJson.addProperty("email", user.getEmail());
        }else{
            throw new Exception("회원가입 실패 코드 작성");
        }
    }
}
