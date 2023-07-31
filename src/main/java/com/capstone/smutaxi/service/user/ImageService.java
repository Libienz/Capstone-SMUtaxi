package com.capstone.smutaxi.service.user;

import com.capstone.smutaxi.utils.FileNameGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageService {


    @Value("${server.upload.directory}")
    private String uploadDirectory;
    @Value("${server.baseUrl}")
    private String serverDomain;

    //유저 프로필사진 업로드 후 img url 반환
    public String uploadUserProfileImage(MultipartFile file) throws IOException {
        // 저장할 파일 이름 생성 (시간 기반)
        String fileName = FileNameGenerator.generateFileName();

        // 파일 저장 경로 생성
        String filePath = uploadDirectory + "/" + fileName;

        // 파일 저장
        File dest = new File(filePath);
        file.transferTo(dest);

        // URL 생성
        String imageUrl = serverDomain + "/api/images/" + fileName;
        return imageUrl;
    }



    //프로필 이미지 리소스 가져오기
    public Resource getProfileImage(String fileName) throws MalformedURLException, FileNotFoundException {
        String imagePath = uploadDirectory + "/" + fileName;

        // 이미지 파일을 읽어옴
        Resource imageResource = new UrlResource("file:" + imagePath);

        // 파일이 존재하지 않는 경우
        if (!imageResource.exists()) {
            throw new FileNotFoundException("이미지를 찾을 수 없습니다.");
        }
        return imageResource;
    }

    //프로필 이미지 리소스의 미디어 타입 확인
    public MediaType getImageMediaType(String fileName) {
        // 이미지 파일 확장자에 따라 적절한 콘텐츠 타입 설정
        String fileExtension = FilenameUtils.getExtension(fileName);
        if (fileExtension.equalsIgnoreCase("png")) {
            return MediaType.IMAGE_PNG;
        } else if (fileExtension.equalsIgnoreCase("jpeg") || fileExtension.equalsIgnoreCase("jpg")) {
            return MediaType.IMAGE_JPEG;
        } else if (fileExtension.equalsIgnoreCase("gif")) {
            return MediaType.IMAGE_GIF;
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
