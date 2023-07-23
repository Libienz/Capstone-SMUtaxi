package com.capstone.smutaxi.entity;

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

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;

    @OneToMany(mappedBy = "rallyInformation", cascade = CascadeType.ALL)
    private List<RallyDetail> rallyDetailList = new ArrayList<>();

}
