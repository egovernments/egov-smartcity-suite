package org.egov.works.mb.repository;


import org.egov.works.mb.entity.MBMeasurementSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface MBMeasurementSheetRepository extends JpaRepository<MBMeasurementSheet,java.lang.Long> {

}