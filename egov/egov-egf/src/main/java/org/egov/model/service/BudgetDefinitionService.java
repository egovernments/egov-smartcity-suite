/*
*eGov suite of products aim to improve the internal efficiency,transparency,
*     accountability and the service delivery of the government  organizations.
*
*      Copyright (C) <2015>  eGovernments Foundation
*
*      The updated version of eGov suite of products as by eGovernments Foundation
*      is available at http://www.egovernments.org
*
*      This program is free software: you can redistribute it and/or modify
*      it under the terms of the GNU General Public License as published by
*      the Free Software Foundation, either version 3 of the License, or
*      any later version.
*
*      This program is distributed in the hope that it will be useful,
*      but WITHOUT ANY WARRANTY; without even the implied warranty of
*      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*      GNU General Public License for more details.
*
*      You should have received a copy of the GNU General Public License
*      along with this program. If not, see http://www.gnu.org/licenses/ or
*      http://www.gnu.org/licenses/gpl.html .
*
*      In addition to the terms of the GPL license to be adhered to in using this
*      program, the following additional terms are to be complied with:
*
*          1) All versions of this program, verbatim or modified must carry this
*             Legal Notice.
*
*          2) Any misrepresentation of the origin of the material is prohibited. It
*             is required that all modified versions of this material be marked in
*             reasonable ways as different from the original version.
*
*          3) This license does not grant any rights to any user of the program
*             with regards to rights under trademark law for use of the trade names
*             or trademarks of eGovernments Foundation.
*
*    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
*/
package org.egov.model.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.service.CFinancialYearService;
import org.egov.infstr.services.PersistenceService;
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
import org.springframework.validation.BindingResult;

@Service
@Transactional(readOnly = true)
public class BudgetDefinitionService {

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
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    @Autowired
    public BudgetDefinitionService(final BudgetDefinitionRepository budgetDefinitionRepository) {
        this.budgetDefinitionRepository = budgetDefinitionRepository;
    }

    @Transactional
    public Budget create(final Budget budget) {
        return budgetDefinitionRepository.save(budget);
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

    public List<Budget> getParentByFinancialYearId(final Long financialYearId) {
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
        return budgetDefinitionRepository.findByIsbereIsAndFinancialYearIdIsAndIdNotIn(isbere, financialYearId,
                budgetIdList);
    }

    /**
     * @param budget
     * @param errors
     * @return
     */
    public String validate(final Budget budget, final BindingResult errors) {
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
                validationMessage = messageSource.getMessage("budget.primary.invalid1", new String[] {
                        budgetList.get(0).getName(), budgetList.get(0).getFinancialYear().getFinYearRange() }, null);
        }
        return validationMessage;
    }

    protected String subtract(final String value) {
        final int val = Integer.parseInt(value) - 1;
        if (val < 10)
            return "0" + val;
        return String.valueOf(val);
    }

    public String computeYearRange(final String range) {
        if (StringUtils.isNotBlank(range)) {
            final String[] list = range.split("-");
            return subtract(list[0]) + "-" + subtract(list[1]);
        }
        return "";
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
                .findByFinYearRange(computeYearRange(financialYear.getFinYearRange()));
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
        final String query = "select bd.referenceBudget.id from Budget bd where bd.referenceBudget.id is not null and "
                + "bd.financialYear.id=:financialYearId";
        final List<Long> budgetDetailsList = persistenceService.getSession().createQuery(query)
                .setParameter("financialYearId", financialYearId).list();
        return budgetDetailsList;
    }

    public Long getApproved(final Long financialYearId) {
        return budgetDefinitionRepository.countByStatusIdInAndFinancialYearIdIs(getBudgetStatus("Approved").getId(),
                financialYearId);
    }

    public Long getVerified(final Long financialYearId) {
        return budgetDefinitionRepository.countByStatusIdInAndFinancialYearIdIs(getBudgetStatus("Created").getId(),
                financialYearId);
    }

    public Long getNotInitalized(final Long financialYearId) {
        final List<Long> bb = budgetDetailService.getBudgetIdList();
        return budgetDefinitionRepository.countByIdNotInAndFinancialYearIdIs(bb, financialYearId);
    }
}
