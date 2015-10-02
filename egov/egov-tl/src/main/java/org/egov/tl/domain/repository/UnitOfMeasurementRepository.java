package org.egov.tl.domain.repository;


import org.egov.tl.domain.entity.UnitOfMeasurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface UnitOfMeasurementRepository extends JpaRepository<UnitOfMeasurement,Long> {

UnitOfMeasurement findByName(String name);

}