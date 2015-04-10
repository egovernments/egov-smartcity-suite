package org.egov.infra.citizen.repository;

import org.egov.infra.citizen.entity.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitizenRepository extends JpaRepository<Citizen, Long> {
    
    Citizen findByEmailId(String emailId);
    Citizen findByUsername(String userName);
}