package com.capstone.smutaxi.service;

import com.capstone.smutaxi.dto.responses.rally.RallyInformationDto;
import com.capstone.smutaxi.entity.RallyDetail;
import com.capstone.smutaxi.entity.RallyInformation;
import com.capstone.smutaxi.repository.RallyInformationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RallyInformationService {

    private final RallyInformationRepository rallyInformationRepository;

    //집회 정보 저장
    @Transactional
    public RallyInformation createRallyInfo(RallyInformationDto rallyInformationDto) {
        //RallyInfoDto를 담을 RallyInformation 생성
        RallyInformation rallyInformation = new RallyInformation();
        rallyInformation.setDate(rallyInformationDto.getDate());
        rallyInformation.setComment(rallyInformationDto.getComment());

        //RallyInfoDto에서rallyDetailsDtoList를 뽑아낸뒤 rallyDetailList로 변경
        List<RallyInformationDto.RallyDetailsDto> rallyDetailsDtoList = rallyInformationDto.getRallyDetailsDtoList();
        List<RallyDetail> rallyDetailList = new ArrayList<>();
        for (RallyInformationDto.RallyDetailsDto rallyDetailsDto : rallyDetailsDtoList) {
            RallyDetail rallyDetail = rallyDetailsDto.toRallyDetail();
            //위에서 담을 RallyInformation을 set 해준다.
            rallyDetail.setRallyInformation(rallyInformation);
            rallyDetailList.add(rallyDetail);
        }
        //RallyInformation에도 rallyDetailList를 넣어준다.
        rallyInformation.getRallyDetailList().addAll(rallyDetailList);

        // RallyInformation과 RallyDetail 한 번에 저장
        //cascade = CascadeType.ALL 옵션을 설정했기 때문에, RallyInformation을 저장할 때 연관된 RallyDetail도 함께 저장된다.
        rallyInformationRepository.save(rallyInformation);

        return rallyInformation;

    }

    //가장 최근의 집회를 하나 가져옴
    public RallyInformation getRecentRallyInfo() {
        return rallyInformationRepository.findTopByOrderByIdDesc();
    }
}
