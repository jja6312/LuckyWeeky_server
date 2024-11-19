package io.ssafy.luckyweeky.application.user;

import com.google.gson.JsonObject;
import io.ssafy.luckyweeky.application.Controller;
import io.ssafy.luckyweeky.domain.user.service.UserService;
import io.ssafy.luckyweeky.infrastructure.config.bean.XmlBeanFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ChangePasswordController implements Controller {
	private final UserService userService;

	public ChangePasswordController() {
		this.userService = (UserService) XmlBeanFactory.getBean("userService");
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response,
						JsonObject respJson) throws ServletException, IOException {
		respJson.addProperty("msg","ChangePasswordController");
	}

}
