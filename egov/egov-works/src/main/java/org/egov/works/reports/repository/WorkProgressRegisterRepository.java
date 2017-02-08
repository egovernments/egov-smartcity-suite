package org.egov.works.reports.repository;

import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.reports.entity.WorkProgressRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkProgressRegisterRepository extends JpaRepository<WorkProgressRegister, Long> {

    WorkProgressRegister findByLineEstimateDetails(LineEstimateDetails led);

    WorkProgressRegister findByAbstractEstimate(AbstractEstimate abstractEstimate);
}