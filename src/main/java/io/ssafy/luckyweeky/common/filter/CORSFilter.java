package io.ssafy.luckyweeky.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CORSFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // CORS 헤더 설정
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST");
        httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        // 다음 필터나 서블릿으로 요청 전달
        chain.doFilter(request, response);
    }
}
