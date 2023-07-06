package com.capstone.smutaxi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RallyInfoController {

    /**
     * @RALLY_INFO_IMGURL
     * 나중에 서버에 올렸을 때 admin 할 수 있는 방법 강구 필요!
     * 지금은 전역 상수로 이미지 지정
     */
    private static final String RALLY_INFO_IMGURL = "https://www.smpa.go.kr/common/attachfile/attachfileView.do?attachNo=00230200&imgType=L";
    @GetMapping("/api/rally-info")
    public String getDemoImageUrl() {
        // 받은 imageUrl을 그대로 반환합니다.
        return RALLY_INFO_IMGURL;
    }
}
