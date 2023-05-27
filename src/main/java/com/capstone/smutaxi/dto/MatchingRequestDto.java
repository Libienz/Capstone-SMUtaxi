package com.capstone.smutaxi.dto;

import com.capstone.smutaxi.chat.domain.GenderRestriction;
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
    private GenderRestriction genderRestriction;

}
