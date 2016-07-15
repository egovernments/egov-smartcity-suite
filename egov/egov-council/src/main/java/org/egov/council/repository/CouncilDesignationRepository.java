package org.egov.council.repository;

import java.util.List;

import org.egov.council.entity.CouncilDesignation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouncilDesignationRepository extends JpaRepository<CouncilDesignation, java.lang.Long> {

    CouncilDesignation findByName(String name);
    
    public List<CouncilDesignation> findByisActive(Boolean isActive);

}