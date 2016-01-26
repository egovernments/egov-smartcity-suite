package org.egov.repository;


import org.egov.commons.CGeneralLedgerDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface CGeneralLedgerDetailRepository extends JpaRepository<CGeneralLedgerDetail,Long> {

//CGeneralLedgerDetail findByName(String name);

}
