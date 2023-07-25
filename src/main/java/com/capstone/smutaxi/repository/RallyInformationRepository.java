package com.capstone.smutaxi.repository;

import com.capstone.smutaxi.entity.RallyInformation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class RallyInformationRepository {
    private final EntityManager em;

    public RallyInformation save(RallyInformation rallyInformation) {
        em.persist(rallyInformation);
        em.flush();
        return rallyInformation;
    }

    public RallyInformation findRecentRallyInfo() {
        return em.createQuery("SELECT r FROM RallyInformation r ORDER BY r.id DESC", RallyInformation.class)
                .setMaxResults(1)
                .getSingleResult();
    }

}
