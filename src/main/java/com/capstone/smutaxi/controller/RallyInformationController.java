package com.capstone.smutaxi.controller;

import com.capstone.smutaxi.config.jwt.JwtTokenProvider;
import com.capstone.smutaxi.dto.RallyInformationDto;
import com.capstone.smutaxi.dto.responses.RallyResponse;
import com.capstone.smutaxi.entity.RallyInformation;
import com.capstone.smutaxi.repository.RallyInformationRepository;
import com.capstone.smutaxi.service.RallyInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rally-info")
@RequiredArgsConstructor
public class RallyInformationController {

    private final JwtTokenProvider jwtTokenProvider;
    private final RallyInformationService rallyInformationService;
    private final RallyInformationRepository rallyInformationRepository;

    /**
     * @RALLY_INFO_IMGURL
     * 나중에 서버에 올렸을 때 admin 할 수 있는 방법 강구 필요!
     * 지금은 전역 상수로 이미지 지정
     */

    //집회 정보 생성
    @PostMapping("/create")
    public ResponseEntity<RallyResponse> createRallyInfo(HttpServletRequest request,@RequestBody RallyInformationDto rallyInformationDto) {
        // 집회정보를 반환
//        String token = jwtTokenProvider.resolveToken(request);
//        String userRole = jwtTokenProvider.getUserRole(token);
//        System.out.println("userRole = " + userRole);
//
//        if (!"ADMIN".equals(userRole)) {
//            // userRole이 ADMIN이 아닌 경우 접근 거부
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
        try {
            //받은 정보를 저장
            RallyInformation rallyInformation = rallyInformationService.createRallyInfo(rallyInformationDto);

            //Dto로 변환
            RallyInformationDto rallyResponseRallyInformationDto = RallyInformationDto.builder()
                    .date(rallyInformation.getDate())
                    .rallyDetailsDtoList(rallyInformation.getRallyDetailList().stream()
                            .map(rallyDetails -> {
                                RallyInformationDto.RallyDetailsDto rallyDetailDto = new RallyInformationDto.RallyDetailsDto();
                                rallyDetailDto.setStartTime(rallyDetails.getStartTime());
                                rallyDetailDto.setEndTime(rallyDetails.getEndTime());
                                rallyDetailDto.setLocation(rallyDetails.getLocation());
                                rallyDetailDto.setRallyScale(rallyDetails.getRallyScale());
                                rallyDetailDto.setJurisdiction(rallyDetails.getJurisdiction());
                                return rallyDetailDto;
                            }).collect(Collectors.toList()))
                    .build();

            //Response Dto 생성 -> {success, message, RallyInfoDto}
            RallyResponse rallyResponse = RallyResponse.builder()
                                                                .success(Boolean.TRUE)
                                                                .message(null)
                                                                .rallyInformationDto(rallyResponseRallyInformationDto)
                                                                .build();

            return ResponseEntity.ok().body(rallyResponse);
        }catch (IllegalArgumentException e){
            RallyResponse rallyResponse = RallyResponse.builder()
                    .success(Boolean.FALSE)
                    .message(e.toString())
                    .build();
            return ResponseEntity.ok().body(rallyResponse);
        }

    }

    //가장 최근 집회정보 1개 GET
    @GetMapping
    public ResponseEntity<RallyResponse> getRallyInfo(){
        try{
            //가장 최근 집회정보 1개 (RallyInformation)
            RallyInformation recentRallyInformation = rallyInformationService.getRecentRallyInfo();

            //Dto로 변환
            RallyInformationDto rallyResponseRallyInformationDto = RallyInformationDto.builder()
                    .date(recentRallyInformation.getDate())
                    .rallyDetailsDtoList(recentRallyInformation.getRallyDetailList().stream()
                            .map(rallyDetails -> {
                                RallyInformationDto.RallyDetailsDto rallyDetailDto = new RallyInformationDto.RallyDetailsDto();
                                rallyDetailDto.setStartTime(rallyDetails.getStartTime());
                                rallyDetailDto.setEndTime(rallyDetails.getEndTime());
                                rallyDetailDto.setLocation(rallyDetails.getLocation());
                                rallyDetailDto.setRallyScale(rallyDetails.getRallyScale());
                                rallyDetailDto.setJurisdiction(rallyDetails.getJurisdiction());
                                return rallyDetailDto;
                            }).collect(Collectors.toList()))
                    .build();

            //Response Dto 생성 -> {success, message, RallyInfoDto}
            RallyResponse rallyResponse = RallyResponse.builder()
                    .success(Boolean.TRUE)
                    .message(null)
                    .rallyInformationDto(rallyResponseRallyInformationDto)
                    .build();

            return ResponseEntity.ok().body(rallyResponse);
        }catch (IllegalArgumentException e){
            RallyResponse rallyResponse = RallyResponse.builder()
                    .success(Boolean.FALSE)
                    .message(e.toString())
                    .build();
            return ResponseEntity.ok().body(rallyResponse);
        }

    }
}
