package org.egov.lcms.masters.repository;

import org.egov.lcms.masters.entity.PetitiontypeMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetitiontypeMasterRepository extends JpaRepository<PetitiontypeMaster, java.lang.Long> {

    PetitiontypeMaster findByCode(String code);

}