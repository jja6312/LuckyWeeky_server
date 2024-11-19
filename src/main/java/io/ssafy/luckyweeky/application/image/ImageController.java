package io.ssafy.luckyweeky.application.image;

import com.google.gson.JsonObject;
import io.ssafy.luckyweeky.application.Controller;
import io.ssafy.luckyweeky.infrastructure.storage.S3Fileloader;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.InputStream;
import java.io.OutputStream;

public class ImageController implements Controller {
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response, JsonObject reqJson, JsonObject respJson) throws Exception{
        // URL 경로에서 keyName 추출
        String keyName = request.getPathInfo().substring(1); // "/image/{keyName}"에서 {keyName} 추출

        // S3에서 파일 스트림 가져오기
        InputStream inputStream = S3Fileloader.getInstance().download(keyName);

        // 응답 헤더 설정
        response.setContentType("image/jpeg"); // JPEG 이미지를 기본으로 설정

        // 클라이언트로 스트리밍
        try (OutputStream outputStream = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }catch (Exception e){
            throw new Exception("Error downloading image 에러 코드 작성");
        }
    }
}
