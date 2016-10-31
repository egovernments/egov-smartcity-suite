package org.egov.model.repository;

import java.util.List;

import org.egov.commons.CChartOfAccounts;
import org.egov.model.budget.BudgetGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetingGroupRepository extends JpaRepository<BudgetGroup, Long> {

    public BudgetGroup findByMajorCode_Id(Long id);

    public BudgetGroup findByMajorCode_IdAndIdNotIn(Long majorCodeId, Long id);

    public List<BudgetGroup> findByMinCodeGlcodeLessThanEqualAndMaxCodeGlcodeGreaterThanEqual(String minCode,
            String minCode1);

    public List<BudgetGroup> findByMinCodeGlcodeLessThanEqualAndMaxCodeGlcodeGreaterThanEqualAndIdNotIn(String minCode,
            String minCode1, Long id);

    @Query("from BudgetGroup  where  upper(name) like '%'||upper(:name)||'%' order by id")
    List<BudgetGroup> findBudgetGroupByNameLike(@Param("name") String name);

    @Query("from BudgetGroup where substr(minCode.glcode,1,:length)<=:majorCode and substr(maxCode.glcode,1,:length)>=:majorCode")
    public List<BudgetGroup> getBudgetGroupForMappedMajorCode(@Param("length") Integer length,
            @Param("majorCode") String majorCode);

    @Query("from BudgetGroup where majorCode.glcode<=:minCode and majorCode.glcode>=:minCode")
    public BudgetGroup getBudgetGroupForMinorCodesMajorCode(@Param("minCode") String minCode);

    @Query("from CChartOfAccounts  where length(glcode)=:length order by glcode")
    List<CChartOfAccounts> findCOAByLength(@Param("length") Integer length);

    public List<BudgetGroup> findByAccountTypeIs(String accountType);

    public List<BudgetGroup> findBybudgetingTypeIs(String budgetingType);

    public List<BudgetGroup> findByIsActiveTrue();

}