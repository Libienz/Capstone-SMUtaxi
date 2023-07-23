package com.capstone.smutaxi.service;

import com.capstone.smutaxi.dto.RallyInfoDto;
import com.capstone.smutaxi.entity.RallyDetail;
import com.capstone.smutaxi.entity.RallyInformation;
import com.capstone.smutaxi.repository.RallyDetailsRepository;
import com.capstone.smutaxi.repository.RallyInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RallyInfoService {
    @Autowired
    private final RallyInfoRepository rallyInfoRepository;
    @Autowired
    private final RallyDetailsRepository rallyDetailsRepository;

    @Transactional
    public RallyInformation createRallyInfo(RallyInfoDto rallyInfoDto) {
        RallyInformation rallyInformation = new RallyInformation();
        rallyInformation.setDate(rallyInfoDto.getDate());

        RallyInformation savedRallyInformation = rallyInfoRepository.save(rallyInformation);

        for (RallyInfoDto.RallyDetailsDto rallyDetailsDto : rallyInfoDto.getRallyDetailsDtoList()) {
            RallyDetail rallyDetail = new RallyDetail();
            rallyDetail.setStartTime(rallyDetailsDto.getStartTime());
            rallyDetail.setEndTime(rallyDetailsDto.getEndTime());
            rallyDetail.setLocation(rallyDetailsDto.getLocation());
            rallyDetail.setRallyScale(rallyDetailsDto.getRallyAttendance());
            rallyDetail.setJurisdiction(rallyDetailsDto.getPoliceStation());
            rallyDetail.setRallyInformation(savedRallyInformation);

            rallyDetailsRepository.save(rallyDetail);
            rallyDetailsRepository.flush();

        }

        // RallyInformation 엔티티의 rallyDetailsList를 업데이트
        savedRallyInformation.getRallyDetailList().addAll(rallyDetailsRepository.findByRallyInformation(savedRallyInformation));

        return savedRallyInformation;
    }

}
