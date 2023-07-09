package com.capstone.smutaxi.controller;

import com.capstone.smutaxi.dto.RallyInfoDto;
import com.capstone.smutaxi.entity.RallyInfo;
import com.capstone.smutaxi.repository.RallyInfoRepository;
import com.capstone.smutaxi.service.RallyInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rally-info")
@RequiredArgsConstructor
public class RallyInfoController {

    private final RallyInfoService rallyInfoService;
    private final RallyInfoRepository rallyInfoRepository;

    /**
     * @RALLY_INFO_IMGURL
     * 나중에 서버에 올렸을 때 admin 할 수 있는 방법 강구 필요!
     * 지금은 전역 상수로 이미지 지정
     */

    @PostMapping("/create")
    public ResponseEntity<RallyInfo> createRallyInfo(@RequestBody RallyInfoDto rallyInfoDto) {
        // 집회정보를 반환
        RallyInfo rallyInfo = rallyInfoService.createRallyInfo(rallyInfoDto);
        return ResponseEntity.ok(rallyInfo);
    }

    @GetMapping
    public ResponseEntity<RallyInfo> getRallyInfo(){
        RallyInfo recentRallyInfo = rallyInfoRepository.getRecentRallyInfo();
        return ResponseEntity.ok(recentRallyInfo);
    }
}
