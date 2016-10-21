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

    public Budget findOne(Long id) {
        return budgetDefinitionRepository.findOne(id);
    }

    public List<Budget> search(Budget budget) {
        if (budget.getFinancialYear() != null) {
            return budgetDefinitionRepository.findByIsbereIsAndFinancialYearIdIs(budget.getIsbere(),
                    budget.getFinancialYear().getId());
        } else
            return budgetDefinitionRepository.findAll();
    }

    public List<Budget> getParentByFinancialYearId(Long financialYearId) {
        return budgetDefinitionRepository.findByFinancialYearIdOrderByFinancialYearIdAscNameAsc(financialYearId);
    }

    /**
     * Referenece Budget is Always RE
     * 
     * @return
     */
    public List<Budget> getReferenceBudgetByFinancialYear() {
        Long financialYearId = null;
        return budgetDefinitionRepository.findByIsbereIsAndFinancialYearIdIs("RE", financialYearId);
    }

    public List<Budget> getReferenceBudgetList(Long financialYearId, List<Long> referenceBudgetIdList) {
        return budgetDefinitionRepository.findReferenceBudget("RE", financialYearId, referenceBudgetIdList);
    }

    public List<Budget> getReferenceBudgetEmpty(Long financialYearId) {
        return budgetDefinitionRepository.findByIsActiveBudgetTrueAndIsbereIsAndFinancialYearIdIs("RE",
                financialYearId);
    }

    public List<Budget> getParentList(String isbere, Long financialYearId, List<Long> budgetIdList) {
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
        if (budget.getIsPrimaryBudget() && budget.getFinancialYear() != null && (budget.getParent() == null)) {
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

    public EgwStatus getBudgetStatus() {
        return egwStatusHibernate.getStatusByModuleAndCode("BUDGET", "Created");
    }

    public List<BudgetDetail> getBudgetDetailList(Long budgetId) {
        return budgetDetailService.getBudgetDetailsByBudgetId(budgetId);
    }

    public List<Budget> referenceBudgetList(Long financialYearId) {
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

    public List<Budget> parentList(String isBere, Long financialYearId) {
        final List<Long> budgetIdList = budgetDetailService.getBudgetIdList();
        return getParentList(isBere, financialYearId, budgetIdList);
    }

    public List<Long> getReferenceBudgetList(Long financialYearId) {
        final String query = "select bd.referenceBudget.id from Budget bd where bd.referenceBudget.id is not null and "
                + "bd.financialYear.id=:financialYearId";
        List<Long> budgetDetailsList = persistenceService.getSession().createQuery(query)
                .setParameter("financialYearId", financialYearId).list();
        return budgetDetailsList;
    }

}