package com.capstone.smutaxi.repository;

import com.capstone.smutaxi.entity.RallyDetail;
import com.capstone.smutaxi.entity.RallyInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RallyDetailsRepository extends JpaRepository<RallyDetail, Long> {
    List<RallyDetail> findByRallyInformation(RallyInformation rallyInformation);
}
