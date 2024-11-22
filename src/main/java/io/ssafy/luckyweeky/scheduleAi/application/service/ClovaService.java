package io.ssafy.luckyweeky.scheduleAi.application.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.github.cdimascio.dotenv.Dotenv;
import io.ssafy.luckyweeky.common.DispatcherServlet;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ClovaService {
    private final String CLOVA_API_URL;
    private final String CLOVA_ACCESS_KEY; // 발급받은 Access Key
    private final String CLOVA_SECRET_KEY; // 발급받은 Secret Key

    public ClovaService() {
        // 환경 변수 로드
        Dotenv dotenv = Dotenv.configure()
                .directory(DispatcherServlet.getWebInfPath() + File.separator)
                .filename(".env")
                .load();

        this.CLOVA_API_URL = dotenv.get("CLOVA_API_URL");
        this.CLOVA_ACCESS_KEY = dotenv.get("CLOVA_ACCESS_KEY");
        this.CLOVA_SECRET_KEY = dotenv.get("CLOVA_SECRET_KEY");
    }

    // Clova Speech API 호출
    public String callClovaSTT(InputStream audioStream) throws IOException {

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            // HTTP 연결 설정
            URL url = new URL(CLOVA_API_URL + "?lang=Kor"); // 언어 설정 추가
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/octet-stream");
            connection.setRequestProperty("X-NCP-APIGW-API-KEY-ID", CLOVA_ACCESS_KEY);
            connection.setRequestProperty("X-NCP-APIGW-API-KEY", CLOVA_SECRET_KEY);
            connection.setDoOutput(true);

            // 오디오 데이터 전송
            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = audioStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            // 응답 처리
            int responseCode = connection.getResponseCode();
            System.out.println("Clova API Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
                System.out.println("Clova API Response: " + responseBuilder.toString());

                // JSON 응답에서 텍스트 추출
                JsonObject jsonResponse = new Gson().fromJson(responseBuilder.toString(), JsonObject.class);
                return jsonResponse.get("text").getAsString();
            } else {
                InputStream errorStream = connection.getErrorStream();
                if (errorStream != null) {
                    BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
                    StringBuilder errorBuilder = new StringBuilder();
                    String errorLine;
                    while ((errorLine = errorReader.readLine()) != null) {
                        errorBuilder.append(errorLine);
                    }
                    System.err.println("Clova API Error Response: " + errorBuilder.toString());
                }
                throw new IOException("Clova Speech API 호출 실패: 응답 코드 " + responseCode);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

}