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
package org.egov.model.service;

import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.service.CFinancialYearService;
import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.repository.BudgetDefinitionRepository;
import org.egov.services.budget.BudgetDetailService;
import org.egov.utils.FinancialConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BudgetDefinitionService {
    private static final String RE = "RE";
    private final BudgetDefinitionRepository budgetDefinitionRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource messageSource;
    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernate;
    @Autowired
    private CFinancialYearService cFinancialYearService;
    @Autowired
    private BudgetDetailService budgetDetailService;

    @Autowired
    public BudgetDefinitionService(final BudgetDefinitionRepository budgetDefinitionRepository) {
        this.budgetDefinitionRepository = budgetDefinitionRepository;
    }

    @Transactional
    public Budget create(final Budget budget) {
        budget.setMaterializedPath(generateMaterializedPath(budget));
        return budgetDefinitionRepository.save(budget);
    }

    private String generateMaterializedPath(final Budget budget) {
        return budget.getParent() == null ? String.valueOf(budgetDefinitionRepository.getRootBudgetsCount() + 1)
                : budget.getParent().getMaterializedPath()
                        .concat(".")
                        .concat(String.valueOf(budgetDefinitionRepository.getChildBudgetsCount(budget.getParent()) + 1));
    }

    @Transactional
    public Budget update(final Budget budget) {

        return budgetDefinitionRepository.save(budget);
    }

    public List<Budget> findAll() {
        return budgetDefinitionRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public Budget findOne(final Long id) {
        return budgetDefinitionRepository.findOne(id);
    }

    public List<Budget> search(final Budget budget) {
        if (budget.getFinancialYear() != null && budget.getSearchBere() != null)
            return budgetDefinitionRepository.findByIsbereIsAndFinancialYearIdIsOrderByFinancialYearIdAscNameAsc(
                    budget.getSearchBere(),
                    budget.getFinancialYear().getId());
        if (budget.getFinancialYear() != null && budget.getSearchBere() == null)
            return budgetDefinitionRepository
                    .findByFinancialYearIdIsOrderByFinancialYearIdAscNameAsc(budget.getFinancialYear().getId());
        if (budget.getFinancialYear() == null && budget.getSearchBere() != null)
            return budgetDefinitionRepository.findByIsbereIsOrderByFinancialYearIdAscNameAsc(budget.getSearchBere());
        else
            return budgetDefinitionRepository.findAll();
    }

    public List<Budget> getBudgetByFinancialYearId(final Long financialYearId) {
        return budgetDefinitionRepository.findByFinancialYearIdOrderByFinancialYearIdAscNameAsc(financialYearId);
    }

    /**
     * Referenece Budget is Always RE
     *
     * @return
     */
    public List<Budget> getReferenceBudgetByFinancialYear() {
        final Long financialYearId = null;
        return budgetDefinitionRepository.findByIsbereIsAndFinancialYearIdIsOrderByFinancialYearIdAscNameAsc("RE",
                financialYearId);
    }

    public List<Budget> getReferenceBudgetList(final Long financialYearId, final List<Long> referenceBudgetIdList) {
        return budgetDefinitionRepository.findReferenceBudget("RE", financialYearId, referenceBudgetIdList);
    }

    public List<Budget> getReferenceBudgetEmpty(final Long financialYearId) {
        return budgetDefinitionRepository.findByIsActiveBudgetTrueAndIsbereIsAndFinancialYearIdIs("RE",
                financialYearId);
    }

    public List<Budget> getParentList(final String isbere, final Long financialYearId, final List<Long> budgetIdList) {
        if (budgetIdList.isEmpty())
            return budgetDefinitionRepository.findByIsActiveBudgetTrueAndIsbereIsAndFinancialYearIdIs(isbere, financialYearId);
        else
            return budgetDefinitionRepository.findByIsbereIsAndFinancialYearIdIsAndIdNotIn(isbere, financialYearId,
                    budgetIdList);
    }

    /**
     * @param budget
     * @param errors
     * @return
     */
    public String validate(final Budget budget) {
        String validationMessage = "";

        if (budget.getParent() != null && budget.getParent().getId() != null && budget.getParent().getId() > 0) {
            final Budget b = budgetDefinitionRepository.findOne(Long.valueOf(budget.getParent().getId()));
            if (!b.getIsbere().equals(budget.getIsbere()))
                validationMessage = messageSource.getMessage("budget.invalid.parent", new String[] { b.getName() },
                        null);
        }
        if (budget.getIsPrimaryBudget() && budget.getFinancialYear() != null && budget.getParent() == null) {
            final List<Budget> budgetList = budgetDefinitionRepository
                    .findByIsbereIsAndFinancialYearIdIsAndIsPrimaryBudgetTrueAndParentIsNull(budget.getIsbere(),
                            budget.getFinancialYear().getId());
            if (!budgetList.isEmpty())
                validationMessage = messageSource.getMessage("budget.primary.invalid", new String[] {
                        budgetList.get(0).getName(), budgetList.get(0).getFinancialYear().getFinYearRange() }, null);
        }
        return validationMessage;
    }

    public EgwStatus getBudgetStatus(final String code) {
        return egwStatusHibernate.getStatusByModuleAndCode(FinancialConstants.BUDGET, code);
    }

    public List<BudgetDetail> getBudgetDetailList(final Long budgetId) {
        return budgetDetailService.getBudgetDetailsByBudgetId(budgetId);
    }

    public List<Budget> referenceBudgetList(final Long financialYearId) {
        CFinancialYear financialYear;
        final List<Long> referenceBudgetIdList = getReferenceBudgetList(financialYearId);
        financialYear = cFinancialYearService.findOne(financialYearId);
        final CFinancialYear previousYear = cFinancialYearService
                .getPreviousFinancialYearForDate(financialYear.getStartingDate());

        if (!referenceBudgetIdList.isEmpty())
            return getReferenceBudgetList(previousYear.getId(), referenceBudgetIdList);
        else
            return getReferenceBudgetEmpty(previousYear.getId());
    }

    public List<Budget> parentList(final String isBere, final Long financialYearId) {
        final List<Long> budgetIdList = budgetDetailService.getBudgetIdList();
        return getParentList(isBere, financialYearId, budgetIdList);
    }

    public List<Long> getReferenceBudgetList(final Long financialYearId) {
        return budgetDefinitionRepository.getReferenceBudgetIds(financialYearId);
    }

    public Long getApproved(final Long financialYearId) {
        return budgetDefinitionRepository.countByIdNotInAndStatusIdInAndFinancialYearIdIsAndIsbereIs(
                budgetDefinitionRepository.findParentBudget(), getBudgetApprovedStatus().getId(),
                financialYearId, RE);

    }

    public Long getVerified(final Long financialYearId) {
        final List<Long> bb = budgetDetailService.getBudgetIdList();
        return budgetDefinitionRepository.countByStatusIdInAndFinancialYearIdIsAndIsbereIsAndIdIn(
                getBudgetStatus("Created").getId(), financialYearId, RE, bb);

    }

    public Long getNotInitalized(final Long financialYearId) {
        final List<Long> bb = budgetDetailService.getBudgetIdList();
        bb.addAll(budgetDefinitionRepository.findParentBudget());
        return budgetDefinitionRepository.countByIdNotInAndFinancialYearIdIsAndIsbereIs(bb, financialYearId, RE);
    }

    public Long getFinancialYearForBudget(final Long budgetId) {
        return findOne(budgetId).getFinancialYear().getId();
    }

    public Long getNotApprovedBudgetCount(final Long financialYearId) {
        final List<Long> bb = budgetDefinitionRepository.findParentBudget();
        return budgetDefinitionRepository.countByStatusIdNotInAndFinancialYearIdIsAndIsbereIsAndIdNotIn(
                getBudgetStatus("Approved").getId(), financialYearId, RE, bb);
    }

    public EgwStatus getBudgetApprovedStatus() {
        return egwStatusHibernate.getStatusByModuleAndCode(FinancialConstants.BUDGET, FinancialConstants.WORKFLOW_STATE_APPROVED);
    }

    public Budget getParentBudgetForApprovedChildBudgets(final Budget budget) {
        final String bgMaterializedPath = budget.getMaterializedPath().substring(0,
                budget.getMaterializedPath().lastIndexOf('.') + 1);
        return budgetDefinitionRepository.countNotApprovedBudgetByMaterializedPath(bgMaterializedPath) > 0 ? null
                : budgetDefinitionRepository
                        .findByMaterializedPath(bgMaterializedPath.substring(0, bgMaterializedPath.length() - 1));
    }
}
