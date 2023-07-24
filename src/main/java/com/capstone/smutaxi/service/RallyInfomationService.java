package com.capstone.smutaxi.service;

import com.capstone.smutaxi.dto.RallyInfoDto;
import com.capstone.smutaxi.entity.RallyDetail;
import com.capstone.smutaxi.entity.RallyInformation;
import com.capstone.smutaxi.repository.RallyDetailsRepository;
import com.capstone.smutaxi.repository.RallyInfomationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RallyInfomationService {

    private final RallyInfomationRepository rallyInfomationRepository;
    private final RallyDetailsRepository rallyDetailsRepository;

    @Transactional
    public RallyInformation createRallyInfo(RallyInfoDto rallyInfoDto) {
        RallyInformation rallyInformation = new RallyInformation();
        rallyInformation.setDate(rallyInfoDto.getDate());

        RallyInformation savedRallyInformation = rallyInfomationRepository.save(rallyInformation);

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

    public RallyInformation getRecentRallyInfo() {
        return rallyInfomationRepository.findRecentRallyInfo();
    }
}
