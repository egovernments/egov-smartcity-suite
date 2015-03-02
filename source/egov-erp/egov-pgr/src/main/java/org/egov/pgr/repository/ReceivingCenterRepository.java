package org.egov.pgr.repository;

import org.egov.pgr.entity.ReceivingCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceivingCenterRepository extends JpaRepository<ReceivingCenter,Long>{
}
