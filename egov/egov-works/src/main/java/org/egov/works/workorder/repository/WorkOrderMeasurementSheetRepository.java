package org.egov.works.workorder.repository;


import org.egov.works.workorder.entity.WorkOrderMeasurementSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface WorkOrderMeasurementSheetRepository extends JpaRepository<WorkOrderMeasurementSheet,java.lang.Long> {

}