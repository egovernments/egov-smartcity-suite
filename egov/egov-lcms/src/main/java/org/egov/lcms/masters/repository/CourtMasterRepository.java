package org.egov.lcms.masters.repository;

import org.egov.lcms.masters.entity.CourtMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourtMasterRepository extends JpaRepository<CourtMaster, java.lang.Long> {

    CourtMaster findByName(String name);

}