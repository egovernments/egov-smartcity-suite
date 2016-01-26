package org.egov.repository;


import org.egov.commons.Fund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface FundRepository extends JpaRepository<Fund,Long> {

Fund findByName(String name);

Fund findByCode(String code);

}
