package org.egov.repository;


import org.egov.commons.FinancingInstitution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface FinancingInstitutionRepository extends JpaRepository<FinancingInstitution,Long> {

FinancingInstitution findByName(String name);

}
