package org.egov.repository;


import org.egov.commons.CChartOfAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface CChartOfAccountsRepository extends JpaRepository<CChartOfAccounts,Long> {

CChartOfAccounts findByName(String name);

}