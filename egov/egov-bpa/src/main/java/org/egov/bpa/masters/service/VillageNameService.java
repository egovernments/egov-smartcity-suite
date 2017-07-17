package org.egov.bpa.masters.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.bpa.application.entity.VillageName;
import org.egov.bpa.masters.repository.VillageNameRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class VillageNameService {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private VillageNameRepository villageNameRepository;
    
    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public List<VillageName> findAll() {
        return villageNameRepository.findAll();
    }

}
