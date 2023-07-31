package com.capstone.smutaxi.controller;


import com.capstone.smutaxi.dto.responses.ResponseFactory;
import com.capstone.smutaxi.dto.responses.user.UploadImageResponse;
import com.capstone.smutaxi.repository.UserRepository;
import com.capstone.smutaxi.service.user.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.http.Part;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final UserRepository userRepository;
    private final ImageService imageService;


    //프로필 이미지 업로드 (재설정 포함) API
    @PostMapping("/profile-image/upload")
    public ResponseEntity<UploadImageResponse> uploadFile(@Part("imagePart") MultipartFile imagePart) throws IOException {

        String imageUrl = imageService.uploadUserProfileImage(imagePart);
        UploadImageResponse uploadImageResponse = ResponseFactory.createUploadImageResponse(Boolean.TRUE, null, imageUrl);
        return ResponseEntity.ok(uploadImageResponse);

    }

    //프로필 이미지 GET API
    @GetMapping("/profile-image/{fileName}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileName) throws MalformedURLException, FileNotFoundException {
        Resource profileImage = imageService.getProfileImage(fileName);
        MediaType imageMediaType = imageService.getImageMediaType(fileName);
        // 응답에 이미지 파일을 포함하여 반환
        return ResponseEntity.ok()
                .contentType(imageMediaType) // 이미지 파일 타입에 맞게 설정
                .body(profileImage);

    }

}
