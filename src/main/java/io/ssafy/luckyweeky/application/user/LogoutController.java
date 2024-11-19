package io.ssafy.luckyweeky.application.user;

import java.io.IOException;

import com.google.gson.JsonObject;

import io.ssafy.luckyweeky.application.Controller;
import io.ssafy.luckyweeky.domain.user.service.UserService;
import io.ssafy.luckyweeky.infrastructure.config.bean.XmlBeanFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LogoutController implements Controller {
	private final UserService userService;

	public LogoutController() {
		this.userService = (UserService) XmlBeanFactory.getBean("userService");
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response, JsonObject reqjson,
			JsonObject respJson) throws ServletException, IOException {
		respJson.addProperty("msg","LoginController");
	}

}
