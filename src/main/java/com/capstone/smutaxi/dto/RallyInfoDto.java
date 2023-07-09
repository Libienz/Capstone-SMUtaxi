package com.capstone.smutaxi.dto;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
public class RallyInfoDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;

    private List<RallyDetailsDto> rallyDetailsDtoList;

    public RallyInfoDto() {
        this.rallyDetailsDtoList = new ArrayList<>();
    }

    @Getter
    public static class RallyDetailsDto {
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime startTime;
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime endTime;
        private String location;
    }

}
