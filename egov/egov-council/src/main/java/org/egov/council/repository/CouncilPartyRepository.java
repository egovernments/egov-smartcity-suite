package org.egov.council.repository;

import java.util.List;

import org.egov.council.entity.CouncilParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface CouncilPartyRepository extends JpaRepository<CouncilParty, java.lang.Long> {

    CouncilParty findByName(String name);

    public List<CouncilParty> findByisActive(Boolean isActive); 
}