package org.egov.bpa.masters.repository;

import org.egov.bpa.application.entity.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long> {
    
    

}
