package com.capstone.smutaxi.repository;

import com.capstone.smutaxi.entity.RallyInformation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class RallyInfoRepository {
    private final EntityManager em;

    public RallyInformation save(RallyInformation rallyInformation) {
        em.persist(rallyInformation);
        em.flush();
        return rallyInformation;
    }

    @Transactional(readOnly = true)
    public RallyInformation getRecentRallyInfo() {
        return em.createQuery("SELECT r FROM RallyInformation r ORDER BY r.id DESC", RallyInformation.class)
                .setMaxResults(1)
                .getSingleResult();
    }

}
