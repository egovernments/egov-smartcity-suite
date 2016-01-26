package org.egov.repository;


import java.util.List;

import org.egov.commons.CChartOfAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface CChartOfAccountsRepository extends JpaRepository<CChartOfAccounts,Long> {

CChartOfAccounts findByName(String name);

List<CChartOfAccounts> findByType(Character type);

List<CChartOfAccounts> findByMajorCode(String majorCode);

List<CChartOfAccounts> findByMajorCodeAndClassification(String majorCode, Long classification);

List<CChartOfAccounts> findByGlcodeLikeIgnoreCaseAndClassification(String glcode,
		Long classification);

List<CChartOfAccounts> findByGlcodeLikeIgnoreCaseAndClassificationAndMajorCode(String glcode,
		Long classification, String majorCode);
}
