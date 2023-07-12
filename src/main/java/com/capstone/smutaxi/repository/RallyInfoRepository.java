package com.capstone.smutaxi.repository;

import com.capstone.smutaxi.entity.RallyInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class RallyInfoRepository {
    private final EntityManager em;

    public RallyInfo save(RallyInfo rallyInfo) {
        em.persist(rallyInfo);
        em.flush();
        return rallyInfo;
    }

    @Transactional(readOnly = true)
    public RallyInfo getRecentRallyInfo() {
        return em.createQuery("SELECT r FROM RallyInfo r ORDER BY r.id DESC", RallyInfo.class)
                .setMaxResults(1)
                .getSingleResult();
    }

}
