package com.capstone.smutaxi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController {

    @GetMapping("/demoImageURL")
    public String getDemoImageUrl(@RequestParam String imageUrl) {
        // 받은 imageUrl을 그대로 반환합니다.
        return imageUrl;
    }
}
