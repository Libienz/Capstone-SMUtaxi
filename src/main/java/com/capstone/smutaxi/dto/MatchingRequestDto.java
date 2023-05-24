package com.capstone.smutaxi.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class MatchingRequestDto {
    @NotNull
    private double latitude;
    @NotNull
    private double longitude;
    @NotNull
    private boolean allowSameGender;
}
