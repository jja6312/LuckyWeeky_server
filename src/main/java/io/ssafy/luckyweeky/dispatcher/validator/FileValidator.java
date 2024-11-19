package io.ssafy.luckyweeky.dispatcher.validator;

import jakarta.servlet.http.Part;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.file.Files;
import javax.imageio.ImageIO;
import jakarta.servlet.http.Part;

public class FileValidator {
    private static FileValidator instance;
    private final long maxFileSizeInBytes;
    private final String[] allowedMimeTypes;

    // 생성자를 private으로 제한해 외부에서 객체 생성 금지
    private FileValidator(long maxFileSizeInBytes, String[] allowedMimeTypes) {
        this.maxFileSizeInBytes = maxFileSizeInBytes;
        this.allowedMimeTypes = allowedMimeTypes;
    }

    // 싱글톤 인스턴스를 반환하는 메서드
    public static FileValidator getInstance() {
        if (instance == null) {
            instance = new FileValidator(
                    1 * 1024 * 1024, // 1MB
                    new String[] {"image/jpeg", "image/png", "image/gif"} // 허용된 MIME 타입
            );
        }
        return instance;
    }

    /**
     * Part 데이터를 기반으로 파일 유효성 검사
     *
     * @param part 업로드된 파일의 Part 객체
     * @return 파일이 유효한지 여부
     */
    public boolean isValid(Part part) throws Exception{
        return part==null||(isValidSize(part) && isValidMimeType(part) && isValidImageContent(part));
    }

    private boolean isValidSize(Part part) throws Exception{
        if(part.getSize() > maxFileSizeInBytes){
            throw new Exception("파일크기에러코드작성");
        }
        return true;
    }

    private boolean isValidMimeType(Part part) throws Exception{
        try {
            String mimeType = part.getContentType(); // Part에서 MIME 타입 직접 가져오기
            if (mimeType == null) {
                return false;
            }
            for (String allowedMimeType : allowedMimeTypes) {
                if (mimeType.equals(allowedMimeType)) {
                    return true;
                }
            }
        } catch (Exception e) {
            throw new Exception("마인타입에라코드작성");
        }
        return false;
    }

    private boolean isValidImageContent(Part part) throws Exception{
        try (InputStream inputStream = part.getInputStream()) {
            BufferedImage image = ImageIO.read(inputStream);
            return image != null; // 이미지 데이터를 정상적으로 읽을 수 있는지 확인
        } catch (Exception e) {
            throw new Exception("비정상적이미지데이터에러코드작성");
        }
    }
}
