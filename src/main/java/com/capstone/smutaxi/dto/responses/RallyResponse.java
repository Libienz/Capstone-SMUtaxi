package com.capstone.smutaxi.dto.responses;

import com.capstone.smutaxi.dto.RallyInformationDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RallyResponse {
    private Boolean success;
    private String message;
    private RallyInformationDto rallyInformationDto;
}
