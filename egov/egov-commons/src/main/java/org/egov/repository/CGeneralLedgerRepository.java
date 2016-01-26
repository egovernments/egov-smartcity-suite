package org.egov.repository;


import org.egov.commons.CGeneralLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface CGeneralLedgerRepository extends JpaRepository<CGeneralLedger,Long> {

//CGeneralLedger findByName(String name);

}
