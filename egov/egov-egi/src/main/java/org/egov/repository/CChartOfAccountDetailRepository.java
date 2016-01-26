package org.egov.repository;


import org.egov.commons.CChartOfAccountDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface CChartOfAccountDetailRepository extends JpaRepository<CChartOfAccountDetail,Long> {

//CChartOfAccountDetail findByName(String name);

}
