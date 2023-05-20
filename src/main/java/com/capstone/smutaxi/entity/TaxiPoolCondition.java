package com.capstone.smutaxi.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Getter @Setter
@Embeddable
public class TaxiPoolCondition {
    private boolean heteroSexPossible; //동승자 이성가능
    private int minimumNumberOfPool; //최소 인원
}
