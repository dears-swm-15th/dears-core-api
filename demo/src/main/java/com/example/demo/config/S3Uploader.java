package com.example.demo.config;


import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@Profile("!test")
public class S3Uploader {

    @Autowired
    private S3Config s3Config;

    @Value("${cloud.aws.bucket}")
    private String bucket;

    @Value("${cloud.aws.cloudfront.path}")
    private String cloudfrontPath;

    private final int PRESIGNED_URL_EXPIRATION = 60 * 1000 * 10; // 10분
    


    // Upload file to S3 using presigned url
    private GeneratePresignedUrlRequest getGeneratePresignedUrlRequest(String fileName) {
        return new GeneratePresignedUrlRequest(bucket, fileName)
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

    public String makeUniqueFileName(String type, Long id, String fileName) {
        return type + "/" + id +"/" + UUID.randomUUID() + "." + getFileExtension(fileName);
    }

    public static String getFileExtension(String fileName) throws RuntimeException{
        if (fileName == null || fileName.lastIndexOf('.') == -1) {
            throw new RuntimeException("파일 확장자가 없습니다.");
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    public void deleteFile(String fileName){
        if (fileName == null) {
            return;
        }
        if (checkFileExists(fileName)){
            s3Config.amazonS3().deleteObject(new DeleteObjectRequest(bucket, fileName));
        }
    }


    public String getPresignedUrl(String fileName){
        return generatePresignedUrl(fileName).toString();
    }

    public List<String> getPresignedUrls(List<String> fileNameList){
        List<String> presignedUrlList = new ArrayList<>();
        for (String fileName : fileNameList) {
            presignedUrlList.add(generatePresignedUrl(fileName).toString());
        }
        return presignedUrlList;
    }

    private boolean checkFileExists(String fileName) {
        // cloudfrontUrl 부분을 앞에서부터 제거
        String cleanedFileName = fileName;
        if (fileName.startsWith(cloudfrontPath)) {
            cleanedFileName = fileName.substring(cloudfrontPath.length());
        }
        return s3Config.amazonS3().doesObjectExist(bucket, cleanedFileName);
    }
}

