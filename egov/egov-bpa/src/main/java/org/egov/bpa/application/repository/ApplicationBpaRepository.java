package org.egov.bpa.application.repository;

import org.egov.bpa.application.entity.BpaApplication;
import org.egov.demand.model.EgDemand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationBpaRepository extends JpaRepository<BpaApplication, Long> {

    @Query("select app from org.egov.bpa.application.entity.BpaApplication app where app.demand=:demand")
    BpaApplication findByDemand(@Param("demand") EgDemand demand);

    BpaApplication findByApplicationNumber(String applicationNumber);

}
