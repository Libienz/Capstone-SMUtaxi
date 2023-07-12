package com.capstone.smutaxi.repository;

import com.capstone.smutaxi.entity.RallyDetails;
import com.capstone.smutaxi.entity.RallyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RallyDetailsRepository extends JpaRepository<RallyDetails, Long> {
    List<RallyDetails> findByRallyInfo(RallyInfo rallyInfo);
}
