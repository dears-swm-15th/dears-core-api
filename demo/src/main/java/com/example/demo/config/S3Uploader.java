package com.example.demo.config;


import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Component
public class S3Uploader {

    @Autowired
    private S3Config s3Config;

    @Value("${cloud.aws.bucket}")
    private String bucket;

    @Value("${cloud.aws.cloudfront.path}")
    private String cloudfrontPath;

    private final int PRESIGNED_URL_EXPIRATION = 60 * 1000; // 1분


    public String getPresignedUrl(String fileName) {
        return generatePresignedUrl(fileName).toString();
    }


    // Upload file to S3 using presigned url
    private GeneratePresignedUrlRequest getGeneratePresignedUrlRequest(String fileName) {
        return new GeneratePresignedUrlRequest(bucket, "test/" + fileName)
                .withMethod(HttpMethod.PUT)
                .withExpiration(setExpiration());
    }

    private URL generatePresignedUrl(String fileName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePresignedUrlRequest(fileName);
        URL url = s3Config.amazonS3().generatePresignedUrl(generatePresignedUrlRequest);
        return url;
    }

    private Date setExpiration(){
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += PRESIGNED_URL_EXPIRATION;
        expiration.setTime(expTimeMillis);
        return expiration;
    }

    private String getUniqueFilename(String fileName) {
        String uuid = UUID.randomUUID().toString();
        return "test/" + fileName + uuid;
    }

    //get file from cloudfront not using presigned url
    private String getCloudfrontFilePath(String fileName) {
        return cloudfrontPath + fileName;
    }
}
