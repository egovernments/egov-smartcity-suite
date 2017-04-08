package org.egov.bpa.masters.repository;

import org.egov.bpa.application.entity.VillageName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VillageNameRepository  extends JpaRepository<VillageName, Long> {

}
