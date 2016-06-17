package org.egov.lcms.masters.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.lcms.masters.entity.PetitiontypeMaster;
import org.egov.lcms.masters.repository.PetitiontypeMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PetitiontypeMasterService {

    private final PetitiontypeMasterRepository petitiontypeMasterRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public PetitiontypeMasterService(final PetitiontypeMasterRepository petitiontypeMasterRepository) {
        this.petitiontypeMasterRepository = petitiontypeMasterRepository;
    }

    @Transactional
    public PetitiontypeMaster create(final PetitiontypeMaster petitiontypeMaster) {
        return petitiontypeMasterRepository.save(petitiontypeMaster);
    }

    @Transactional
    public PetitiontypeMaster update(final PetitiontypeMaster petitiontypeMaster) {
        return petitiontypeMasterRepository.save(petitiontypeMaster);
    }

    public List<PetitiontypeMaster> findAll() {
        return petitiontypeMasterRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public PetitiontypeMaster findByCode(final String code) {
        return petitiontypeMasterRepository.findByCode(code);
    }

    public PetitiontypeMaster findOne(final Long id) {
        return petitiontypeMasterRepository.findOne(id);
    }

    public List<PetitiontypeMaster> search(final PetitiontypeMaster petitiontypeMaster) {
        return petitiontypeMasterRepository.findAll();
    }
}