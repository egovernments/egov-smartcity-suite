package org.egov.tl.repository;

import org.egov.tl.entity.LabourClassification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabourClassificationRepository extends JpaRepository<LabourClassification, Long>{

}
