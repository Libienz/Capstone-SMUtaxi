package com.capstone.smutaxi.repository;

import com.capstone.smutaxi.entity.RallyInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RallyInformationRepository extends JpaRepository<RallyInformation, Long> {
    RallyInformation findTopByOrderByIdDesc();
}
