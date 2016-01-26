package org.egov.repository;


import org.egov.commons.CVoucherHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface CVoucherHeaderRepository extends JpaRepository<CVoucherHeader,Long> {

CVoucherHeader findByName(String name);

}
