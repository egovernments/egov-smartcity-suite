/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.model.repository;

import org.egov.model.budget.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetDefinitionRepository extends JpaRepository<Budget, java.lang.Long> {

    List<Budget> findByFinancialYearIdOrderByFinancialYearIdAscNameAsc(Long id);

    List<Budget> findByIsbereIsOrderByFinancialYearIdAscNameAsc(String isBere);

    List<Budget> findByIsbereIsAndFinancialYearIdIsOrderByFinancialYearIdAscNameAsc(String bere, Long id);

    @Query("from Budget be where isActiveBudget=true and isbere =:isbere and financialYear.id=:financialYearId and id not in :rbIds")
    List<Budget> findReferenceBudget(@Param("isbere") final String isbere,
            @Param("financialYearId") final Long financialYearId, @Param("rbIds") final List<Long> rbIds);

    List<Budget> findByIsActiveBudgetTrueAndIsbereIsAndFinancialYearIdIs(String isbere, Long financialYearId);

    List<Budget> findByIsbereIsAndFinancialYearIdIsAndIdNotIn(String isbere, Long financialYearId, List<Long> rbId);

    List<Budget> findByIsbereIsAndFinancialYearIdIsAndIsPrimaryBudgetTrueAndParentIsNull(String isbere,
            Long financialYearId);

    List<Budget> findByFinancialYearIdIsOrderByFinancialYearIdAscNameAsc(Long financialYearId);

    @Query("select count(b) from Budget b where b.status.id =:statusId")
    Long countBudget(Integer statusId);

    Long countByIdNotInAndStatusIdInAndFinancialYearIdIsAndIsbereIs(List<Long> budgetId, Integer statusId, Long financialYearId,
            String bere);

    Long countByStatusIdInAndFinancialYearIdIsAndIsbereIsAndIdIn(Integer statusId, Long financialYearId, String bere,
            List<Long> budgetId);

    Long countByIdNotInAndFinancialYearIdIs(List<Long> budgetId, Long financialYearId);

    @Query("select count(b) from Budget b where parent is null")
    Long getRootBudgetsCount();

    @Query("select count(b) from Budget b where parent=:parent")
    Long getChildBudgetsCount(@Param("parent") Budget parent);

    @Query("select bd.referenceBudget.id from Budget bd where bd.referenceBudget.id is not null and "
            + "bd.financialYear.id=:financialYearId")
    List<Long> getReferenceBudgetIds(@Param("financialYearId") Long financialYearId);

    Long countByIdNotInAndFinancialYearIdIsAndIsbereIs(List<Long> budgetId, Long financialYearId, String bere);

    @Query("select distinct bg.parent.id from Budget bg, Budget bd where  bg.parent=bd.id and (bg.parent is not null)")
    List<Long> findParentBudget();

    Budget findByReferenceBudgetId(final Long budgetId);

    Long countByStatusIdNotInAndFinancialYearIdIsAndIsbereIsAndIdNotIn(Integer statusId, Long financialYearId, String bere,
            List<Long> budgetId);

    @Query("select count(b) from Budget b  where b.materializedPath like :path||'%' and b.status.code not in ('Approved')")
    Long countNotApprovedBudgetByMaterializedPath(@Param("path") String path);

    Budget findByMaterializedPath(final String path);
}
