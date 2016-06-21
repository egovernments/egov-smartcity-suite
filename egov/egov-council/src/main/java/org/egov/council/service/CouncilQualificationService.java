package org.egov.council.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.council.entity.CouncilQualification;
import org.egov.council.repository.CouncilQualificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CouncilQualificationService {

    private final CouncilQualificationRepository councilQualificationRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public CouncilQualificationService(final CouncilQualificationRepository councilQualificationRepository) {
        this.councilQualificationRepository = councilQualificationRepository;
    }

    @Transactional
    public CouncilQualification create(final CouncilQualification councilQualification) {
        return councilQualificationRepository.save(councilQualification);
    }

    @Transactional
    public CouncilQualification update(final CouncilQualification councilQualification) {
        return councilQualificationRepository.save(councilQualification);
    }

    public List<CouncilQualification> findAll() {
        return councilQualificationRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public CouncilQualification findByName(String name) {
        return councilQualificationRepository.findByName(name);
    }

    public CouncilQualification findOne(Long id) {
        return councilQualificationRepository.findOne(id);
    }

    public List<CouncilQualification> search(CouncilQualification councilQualification) {
        return councilQualificationRepository.findAll();
    }
}