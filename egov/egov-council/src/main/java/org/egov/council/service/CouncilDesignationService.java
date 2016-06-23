package org.egov.council.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.council.entity.CouncilDesignation;
import org.egov.council.repository.CouncilDesignationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CouncilDesignationService {

    private final CouncilDesignationRepository councilDesignationRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public CouncilDesignationService(final CouncilDesignationRepository councilDesignationRepository) {
        this.councilDesignationRepository = councilDesignationRepository;
    }

    @Transactional
    public CouncilDesignation create(final CouncilDesignation councilDesignation) {
        return councilDesignationRepository.save(councilDesignation);
    }

    @Transactional
    public CouncilDesignation update(final CouncilDesignation councilDesignation) {
        return councilDesignationRepository.save(councilDesignation);
    }

    public List<CouncilDesignation> findAll() {
        return councilDesignationRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public CouncilDesignation findByName(String name) {
        return councilDesignationRepository.findByName(name);
    }

    public CouncilDesignation findOne(Long id) {
        return councilDesignationRepository.findOne(id);
    }

    public List<CouncilDesignation> search(CouncilDesignation councilDesignation) {
        return councilDesignationRepository.findAll();
    }
}