package com.capstone.smutaxi.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class FileNameGenerator {
    public static String generateFileName() {
        // 현재 시간을 기반으로 파일 이름 생성
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = dateFormat.format(new Date());

        // 랜덤한 숫자 생성
        Random random = new Random();
        int randomNumber = random.nextInt(10000);

        // 파일 이름 생성
        String fileName = "file_" + timestamp + "_" + randomNumber + ".jpg";

        return fileName;
    }
}