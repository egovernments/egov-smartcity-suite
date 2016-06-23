package org.egov.council.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.council.entity.CouncilParty;
import org.egov.council.repository.CouncilPartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CouncilPartyService {

    private final CouncilPartyRepository councilPartyRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public CouncilPartyService(final CouncilPartyRepository councilPartyRepository) {
        this.councilPartyRepository = councilPartyRepository;
    }

    @Transactional
    public CouncilParty create(final CouncilParty councilParty) {
        return councilPartyRepository.save(councilParty);
    }

    @Transactional
    public CouncilParty update(final CouncilParty councilParty) {
        return councilPartyRepository.save(councilParty);
    }

    public List<CouncilParty> findAll() {
        return councilPartyRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public CouncilParty findByName(String name) {
        return councilPartyRepository.findByName(name);
    }

    public CouncilParty findOne(Long id) {
        return councilPartyRepository.findOne(id);
    }

    public List<CouncilParty> search(CouncilParty councilParty) {
        return councilPartyRepository.findAll();
    }
}