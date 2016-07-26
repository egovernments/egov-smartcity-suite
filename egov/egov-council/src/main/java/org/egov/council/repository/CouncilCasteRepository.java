package org.egov.council.repository;

import java.util.List;

import org.egov.council.entity.CouncilCaste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouncilCasteRepository extends JpaRepository<CouncilCaste, java.lang.Long> {

    CouncilCaste findByName(String name);
    
    public List<CouncilCaste> findByisActive(Boolean isActive);

}