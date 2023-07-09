package com.capstone.smutaxi.service;

import com.capstone.smutaxi.dto.RallyInfoDto;
import com.capstone.smutaxi.entity.ChatRoom;
import com.capstone.smutaxi.entity.RallyDetails;
import com.capstone.smutaxi.entity.RallyInfo;
import com.capstone.smutaxi.repository.RallyDetailsRepository;
import com.capstone.smutaxi.repository.RallyInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RallyInfoService {
    @Autowired
    private final RallyInfoRepository rallyInfoRepository;
    @Autowired
    private final RallyDetailsRepository rallyDetailsRepository;

    @Transactional
    public RallyInfo createRallyInfo(RallyInfoDto rallyInfoDto) {
        RallyInfo rallyInfo = new RallyInfo();
        rallyInfo.setDate(rallyInfoDto.getDate());

        RallyInfo savedRallyInfo = rallyInfoRepository.save(rallyInfo);

        System.out.println("잘 되고있니");
        for (RallyInfoDto.RallyDetailsDto rallyDetailsDto : rallyInfoDto.getRallyDetailsDtoList()) {
            RallyDetails rallyDetails = new RallyDetails();
            rallyDetails.setStartTime(rallyDetailsDto.getStartTime());
            rallyDetails.setEndTime(rallyDetailsDto.getEndTime());
            rallyDetails.setLocation(rallyDetailsDto.getLocation());
            rallyDetails.setRallyInfo(savedRallyInfo);

            rallyDetailsRepository.save(rallyDetails);
            rallyDetailsRepository.flush();
            System.out.println("디테일 = " + rallyDetails);

        }

        // RallyInfo 엔티티의 rallyDetailsList를 업데이트
        savedRallyInfo.getRallyDetailsList().addAll(rallyDetailsRepository.findByRallyInfo(savedRallyInfo));

        return savedRallyInfo;
    }

}
