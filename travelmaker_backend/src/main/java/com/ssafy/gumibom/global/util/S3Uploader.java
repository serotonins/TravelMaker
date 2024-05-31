package com.ssafy.gumibom.global.util;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
@Service
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 멀티파트 파일을 파일로 변환 후 S3로 업로드
    public String uploadFileToS3(MultipartFile multipartFile, String filePath) throws IOException {
        // 1. MultipartFile -> File 변환
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패,,,"));

        return upload(uploadFile, filePath);
    }

    // 파일을 바로 S3로 업로드
    public String uploadFileToS3(File file, String filePath) throws IOException {
        return upload(file, filePath);
    }

    private String upload(File uploadFile, String filePath) {
        // S3에 저장될 파일 이름
        String fileName = filePath + "/" + uploadFile.getName();

        // S3에 업로드 후 로컬 파일 삭제
        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    // S3로 File 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(
                CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // S3에 있는 파일 삭제
    public void deleteS3(String filePath) throws Exception {
        try {
            String key = filePath.substring(56); // 폴더, 파일 확장자

            try {
                amazonS3Client.deleteObject(bucket, key);
            } catch (AmazonS3Exception e) {
                log.info(e.getErrorMessage());
            }
        } catch (Exception exception) {
            log.info(exception.getMessage());
        }
        log.info("[S3 Uploader]: S3에 있는 파일 삭제");
    }

    // 로컬에 파일 업로드 및 변환(MultipartFile -> File)
    private Optional<File> convert(MultipartFile multipartFile) throws IOException {

        // 로컬에서 저장할 파일 경로: user.dir (현재 디렉토리 기준)
        String dirPath = System.getProperty("user.dir") + "/" + multipartFile.getOriginalFilename();
        File convertFile = new File(dirPath);

        if(convertFile.createNewFile()) {
            // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장
            try(FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(multipartFile.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }

    // 로컬에 저장된 파일 삭제
    private void removeNewFile(File targetFile) {
        if(targetFile.delete()) {
            log.info("[파일 업로드]: 파일 삭제 성공");
            return;
        }
        log.info("[파일 업로드]: 파일 삭제 실패");
    }
}
