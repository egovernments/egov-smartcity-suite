package org.egov.repository;


import org.egov.commons.EgfRecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface EgfRecordStatusRepository extends JpaRepository<EgfRecordStatus,Long> {

//EgfRecordStatus findByName(String name);

}
