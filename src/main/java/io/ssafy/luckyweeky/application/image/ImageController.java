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
    public void service(HttpServletRequest request, HttpServletResponse response, JsonObject respJson) throws Exception {
        String prefix = "/api/v1/qweSJqwo/";
        String keyName;

        try {
            // URL 경로에서 keyName 추출
            keyName = request.getRequestURI().substring(prefix.length());

            // 확장자 추출 및 MIME 타입 설정
            String extension = keyName.substring(keyName.lastIndexOf(".") + 1).toLowerCase();
            String contentType = getMimeType(extension);
            response.setContentType(contentType);

            // S3에서 파일 스트림 가져오기
            try (InputStream inputStream = S3Fileloader.getInstance().download(keyName)) {
                if (inputStream == null) {
                    throw new Exception("이미지를 찾을 수 없습니다.");
                }

                // 클라이언트로 스트리밍
                try (OutputStream outputStream = response.getOutputStream()) {
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    outputStream.flush();
                }
            }
        } catch (StringIndexOutOfBoundsException e) {
            throw new StringIndexOutOfBoundsException("잘못된 이미지 요청 경로에러코드");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("이미지 처리 중 오류 에러코드");
        }
    }

    // 확장자에 따른 MIME 타입 반환
    private String getMimeType(String extension) {
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "bmp":
                return "image/bmp";
            case "webp":
                return "image/webp";
            default:
                return "application/octet-stream"; // 기본 MIME 타입
        }
    }
}
