package io.ssafy.luckyweeky.application;

import java.io.IOException;

import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Controller {
	public void service(
			HttpServletRequest request,
			HttpServletResponse response, 
			JsonObject respJson
			) throws Exception;
}
