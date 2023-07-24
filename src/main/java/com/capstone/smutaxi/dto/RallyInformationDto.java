package com.capstone.smutaxi.dto;

import com.capstone.smutaxi.entity.RallyDetail;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;


@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class RallyInformationDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;

    private List<RallyDetailsDto> rallyDetailsDtoList;

    //dto 이름 수정 필요
    @Getter
    @Setter
    public static class RallyDetailsDto {
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime startTime;
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime endTime;
        private String location;
        private String rallyScale;
        private String jurisdiction;

        public RallyDetail toRallyDetail() {
            RallyDetail rallyDetail = new RallyDetail();
            rallyDetail.setStartTime(this.startTime);
            rallyDetail.setEndTime(this.endTime);
            rallyDetail.setLocation(this.location);
            rallyDetail.setRallyScale(this.rallyScale);
            rallyDetail.setJurisdiction(this.jurisdiction);
            return rallyDetail;
        }
    }

}
