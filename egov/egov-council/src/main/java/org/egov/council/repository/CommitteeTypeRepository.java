package org.egov.council.repository;

import java.util.List;

import org.egov.council.entity.CommitteeType;
import org.egov.council.entity.CouncilCaste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitteeTypeRepository extends JpaRepository<CommitteeType, java.lang.Long> {

    CommitteeType findByName(String name);
    
    public List<CommitteeType> findByisActive(Boolean isActive);

}