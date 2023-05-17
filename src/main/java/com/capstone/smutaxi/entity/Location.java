package com.capstone.smutaxi.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Getter @Setter
@Embeddable
public class Location {

    private double latitude; //위도
    private double longitude; //경도

}
