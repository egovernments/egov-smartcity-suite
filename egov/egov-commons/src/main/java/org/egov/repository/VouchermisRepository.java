package org.egov.repository;


import org.egov.commons.Vouchermis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface VouchermisRepository extends JpaRepository<Vouchermis,Long> {

//Vouchermis findByName(String name);

}
