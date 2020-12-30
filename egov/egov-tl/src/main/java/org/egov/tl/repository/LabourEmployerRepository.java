package org.egov.tl.repository;

import org.egov.tl.entity.LabourEmployer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabourEmployerRepository extends JpaRepository<LabourEmployer, Long> {
	
	LabourEmployer findByName(String name);
}
