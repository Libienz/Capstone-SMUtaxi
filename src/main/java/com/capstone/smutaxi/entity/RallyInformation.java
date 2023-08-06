package com.capstone.smutaxi.entity;


import com.capstone.smutaxi.dto.responses.rally.RallyInformationDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "rally_informations")
public class RallyInformation {
    @Id
    @Column(name = "rally_information_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime date;

    @OneToMany(mappedBy = "rallyInformation", cascade = CascadeType.ALL)
    private List<RallyDetail> rallyDetailList = new ArrayList<>();

    public RallyInformationDto toRallyInformationDto() {
        RallyInformationDto rallyInformationDto = new RallyInformationDto();
        rallyInformationDto.setDate(this.date);

        List<RallyInformationDto.RallyDetailsDto> rallyDetailsDtoList = new ArrayList<>();
        for (RallyDetail rallyDetail : this.rallyDetailList) {
            rallyDetailsDtoList.add(rallyDetail.toRallyDetailsDto());
        }
        rallyInformationDto.setRallyDetailsDtoList(rallyDetailsDtoList);

        return rallyInformationDto;
    }
}
