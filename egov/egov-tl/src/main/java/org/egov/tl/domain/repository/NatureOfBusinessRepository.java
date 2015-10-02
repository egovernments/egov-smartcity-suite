package org.egov.tl.domain.repository;


import org.egov.tl.domain.entity.NatureOfBusiness;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface NatureOfBusinessRepository extends JpaRepository<NatureOfBusiness,Long> {

NatureOfBusiness findByName(String name);

}