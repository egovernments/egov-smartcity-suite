package org.egov.council.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.council.entity.CouncilMember;
import org.egov.council.repository.CouncilMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CouncilMemberService {

    private final CouncilMemberRepository councilMemberRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public CouncilMemberService(final CouncilMemberRepository councilMemberRepository) {
        this.councilMemberRepository = councilMemberRepository;
    }

    @Transactional
    public CouncilMember create(final CouncilMember councilMember) {
        return councilMemberRepository.save(councilMember);
    }

    @Transactional
    public CouncilMember update(final CouncilMember councilMember) {
        return councilMemberRepository.save(councilMember);
    }

    public List<CouncilMember> findAll() {
        return councilMemberRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public CouncilMember findByName(String name) {
        return councilMemberRepository.findByName(name);
    }

    public CouncilMember findOne(Long id) {
        return councilMemberRepository.findById(id);
    }

    public List<CouncilMember> search(CouncilMember councilMember) {
        return councilMemberRepository.findAll();
    }
    
    
}