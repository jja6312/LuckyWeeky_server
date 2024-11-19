package io.ssafy.luckyweeky.infrastructure.storage;

import io.github.cdimascio.dotenv.Dotenv;
import io.ssafy.luckyweeky.dispatcher.DispatcherServlet;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.File;
import java.io.InputStream;

public class S3Fileloader {
    private static S3Fileloader instance;
    private static String BUCKET_NAME;
    private static final Region REGION = Region.AP_NORTHEAST_2;

    private final S3Client s3Client;

    private S3Fileloader(String accessKey, String secretKey) {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
        this.s3Client = S3Client.builder()
                .region(REGION)
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }
    public static synchronized S3Fileloader getInstance() {
        if (instance == null) {
            Dotenv dotenv = Dotenv.configure()
                    .directory(DispatcherServlet.getWebInfPath()+ File.separatorChar)
                    .filename(".env")
                    .load();
            String accessKey = dotenv.get("AWS_ACCESS_KEY");
            String secretKey = dotenv.get("AWS_SECRET_KEY");
            BUCKET_NAME = dotenv.get("BUCKET_NAME");
            instance = new S3Fileloader(accessKey, secretKey);
        }
        return instance;
    }

    public String upload(File file, String keyName) throws Exception {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(keyName)
                    .build();
            s3Client.putObject(putObjectRequest, file.toPath());
            return "https://" + BUCKET_NAME + ".s3." + REGION + ".amazonaws.com/" + keyName;
        } catch (S3Exception e) {
            throw new Exception("S3이미지업로드에러코드작성");
        }
    }

    public InputStream download(String keyName) throws Exception {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(keyName)
                    .build();

            return s3Client.getObject(getObjectRequest);
        } catch (S3Exception e) {
            e.printStackTrace();
            throw new Exception("S3이미지 파일 not found에러");
        }
    }

    public synchronized void close() {
        if (s3Client != null) {
            s3Client.close();
        }
    }
}
