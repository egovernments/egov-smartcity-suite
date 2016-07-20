package org.egov.works.abstractestimate.repository;


import org.egov.works.abstractestimate.entity.MeasurementSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface MeasurementSheetRepository extends JpaRepository<MeasurementSheet,java.lang.Long> {

}