package io.ssafy.luckyweeky.user.presentation;

import com.google.gson.JsonObject;
import io.ssafy.luckyweeky.common.config.XmlBeanFactory;
import io.ssafy.luckyweeky.common.implement.Controller;
import io.ssafy.luckyweeky.common.util.parser.RequestJsonParser;
import io.ssafy.luckyweeky.common.util.url.RequestUrlPath;
import io.ssafy.luckyweeky.user.application.dto.GeneralSignupUserDto;
import io.ssafy.luckyweeky.user.application.service.UserService;
import io.ssafy.luckyweeky.user.application.validator.FileValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.UUID;

public class UserController implements Controller {
    private static final String USER_PART = "user";
    private static final String FILE_PART = "file";
    private static final String DEFAULT_PROFILE_IMAGE = "profile-images/default.png";
    private static final String MULTIPART_FORM_DATA = "multipart/form-data";

    private final UserService userService;

    public UserController() {
        this.userService = (UserService) XmlBeanFactory.getBean("userService");
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response, JsonObject respJson) throws ServletException, IOException {
        String action = RequestUrlPath.getURI(request.getRequestURI())[1];

        switch (action){
            case "RClmJ":{//signup
                userSignUp(request, response,respJson);
                break;
            }
        }
    }

    public void userSignUp(HttpServletRequest request, HttpServletResponse response, JsonObject respJson) throws ServletException, IOException {
        JsonObject jsonObject = RequestJsonParser.getInstance().parse(request.getPart(USER_PART));
        Part filePart = getFilePart(request);
        String profileImageKey = processFilePart(filePart);

        GeneralSignupUserDto user = new GeneralSignupUserDto(
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
            throw new IllegalArgumentException("회원가입 실패 코드 작성");
        }
    }

    private Part getFilePart(HttpServletRequest request){
        try {
            if (request.getContentType() != null && request.getContentType().startsWith(MULTIPART_FORM_DATA)) {
                return request.getPart(FILE_PART);
            }
            return null;
        }catch (Exception e){
            return null;
        }
    }

    private String processFilePart(Part filePart){
        if (filePart != null && filePart.getSize() > 0) {
            FileValidator.getInstance().isValid(filePart);
            String uniqueFileName = UUID.randomUUID() + getExtension(filePart);
            return "profile-images/" + uniqueFileName;
        }
        return DEFAULT_PROFILE_IMAGE;
    }
    private String getExtension(Part filePart) {
        String fileName = filePart.getSubmittedFileName();
        String fileExtension = "";
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex != -1) {
            fileExtension = fileName.substring(lastDotIndex);
        }
        return fileExtension;
    }
}
