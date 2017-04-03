package org.egov.bpa.masters.repository;

import org.egov.bpa.application.entity.StakeHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StakeHolderRepository extends JpaRepository<StakeHolder, Long> {

}
