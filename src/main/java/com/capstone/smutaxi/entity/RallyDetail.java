package com.capstone.smutaxi.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "rally_details")
public class RallyDetail {

    @Id
    @Column(name = "rally_detail_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;

    private String location;

    //집회 규모
    private String rallyScale;

    //집회 관할군
    private String jurisdiction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rally_information_id" )
    private RallyInformation rallyInformation;
}
