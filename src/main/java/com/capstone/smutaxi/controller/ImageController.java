package com.capstone.smutaxi.controller;


import com.capstone.smutaxi.dto.responses.UploadImageResponse;
import com.capstone.smutaxi.entity.User;
import com.capstone.smutaxi.repository.UserRepository;
import com.capstone.smutaxi.service.auth.UserService;
import io.jsonwebtoken.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.http.Part;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Value("${server.baseUrl}") // 서버의 기본 URL을 설정한 프로퍼티 값으로 가져옴
    private String serverBaseUrl;

    @PostMapping("profile-image/upload")
    public ResponseEntity<UploadImageResponse> uploadFile(@Part("imagePart") MultipartFile imagePart) {

        try {
            String imageUrl = userService.updateUserProfileImage(imagePart);
            UploadImageResponse uploadImageResponse = UploadImageResponse.builder()
                    .success(Boolean.TRUE)
                    .message("Image Uploaded")
                    .imageUrl(imageUrl)
                    .build();
            return ResponseEntity.ok(uploadImageResponse);
        } catch (Exception e) {
            UploadImageResponse uploadImageResponse = UploadImageResponse.builder()
                    .success(Boolean.TRUE)
                    .message("Fail to upload image")
                    .build();

            return ResponseEntity.ok(uploadImageResponse);
        }
    }

    @GetMapping("profile-image/{fileName}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileName) {
        try {

            Resource profileImage = userService.getProfileImage(fileName);
            MediaType imageMediaType = userService.getImageMediaType(fileName);
            // 응답에 이미지 파일을 포함하여 반환
            return ResponseEntity.ok()
                    .contentType(imageMediaType) // 이미지 파일 타입에 맞게 설정
                    .body(profileImage);
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
