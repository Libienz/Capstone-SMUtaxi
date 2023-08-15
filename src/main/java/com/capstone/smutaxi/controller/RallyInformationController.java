package com.capstone.smutaxi.controller;

import com.capstone.smutaxi.dto.responses.rally.RallyInformationDto;
import com.capstone.smutaxi.dto.responses.rally.RallyResponse;
import com.capstone.smutaxi.dto.responses.ResponseFactory;
import com.capstone.smutaxi.entity.RallyInformation;
import com.capstone.smutaxi.service.RallyInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/rally-information")
@RequiredArgsConstructor
public class RallyInformationController {

    private final RallyInformationService rallyInformationService;

    /**
     * @RALLY_INFO_IMGURL
     * 나중에 서버에 올렸을 때 admin 할 수 있는 방법 강구 필요!
     * 지금은 전역 상수로 이미지 지정
     */

    //집회 정보 생성 API
    @PostMapping("")
    public ResponseEntity<RallyResponse> createRallyInfo(HttpServletRequest request,@RequestBody RallyInformationDto rallyInformationDto) {

        //받은 정보를 저장
        RallyInformation rallyInformation = rallyInformationService.createRallyInfo(rallyInformationDto);

        //Response Dto 생성 -> {success, message, RallyInfoDto}
        RallyResponse rallyResponse = ResponseFactory.createRallyResponse(true, null, rallyInformationDto);
        return ResponseEntity.ok().body(rallyResponse);
    }

    //가장 최근 집회정보 1개 GET API
    @GetMapping("")
    public ResponseEntity<RallyResponse> getRallyInfo(){

        //가장 최근 집회정보 1개 (RallyInformation)
        RallyInformation recentRallyInformation = rallyInformationService.getRecentRallyInfo();

        //Dto로 변환
        RallyInformationDto rallyResponseRallyInformationDto = recentRallyInformation.toRallyInformationDto();

        //Response Dto 생성 -> {success, message, RallyInfoDto}
        RallyResponse rallyResponse = ResponseFactory.createRallyResponse(true, null, rallyResponseRallyInformationDto);
        return ResponseEntity.ok().body(rallyResponse);
    }
}
