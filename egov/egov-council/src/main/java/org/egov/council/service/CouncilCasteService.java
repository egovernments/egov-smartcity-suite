package org.egov.council.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.council.entity.CouncilCaste;
import org.egov.council.repository.CouncilCasteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CouncilCasteService {

    private final CouncilCasteRepository councilCasteRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public CouncilCasteService(final CouncilCasteRepository councilCasteRepository) {
        this.councilCasteRepository = councilCasteRepository;
    }

    @Transactional
    public CouncilCaste create(final CouncilCaste councilCaste) {
        return councilCasteRepository.save(councilCaste);
    }

    @Transactional
    public CouncilCaste update(final CouncilCaste councilCaste) {
        return councilCasteRepository.save(councilCaste);
    }

    public List<CouncilCaste> findAll() {
        return councilCasteRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public CouncilCaste findByName(String name) {
        return councilCasteRepository.findByName(name);
    }

    public CouncilCaste findOne(Long id) {
        return councilCasteRepository.findOne(id);
    }

    public List<CouncilCaste> search(CouncilCaste councilCaste) {
        return councilCasteRepository.findAll();
    }
}