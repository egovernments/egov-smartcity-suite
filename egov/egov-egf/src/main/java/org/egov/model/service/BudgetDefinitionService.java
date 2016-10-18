package org.egov.model.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.repository.CFinancialYearRepository;
import org.egov.dao.budget.BudgetDetailsHibernateDAO;
import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.repository.BudgetDefinitionRepository;
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
    private Long financialYearId;
    private CFinancialYearRepository financialYear;
    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource messageSource;
    @Autowired
    private BudgetDetailsHibernateDAO budgetDetails;
    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernate;
    
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
        if (budget.getFinancialYear() != null && budget.getIsbere().isEmpty()) {
            return budgetDefinitionRepository
                    .findByFinancialYearIdOrderByFinancialYearIdAscNameAsc(budget.getFinancialYear().getId());
        }
        if (!budget.getIsbere().isEmpty() && budget.getFinancialYear() == null) {
            return budgetDefinitionRepository.findByIsbereIsOrderByFinancialYearIdAscNameAsc(budget.getIsbere());
        }
        if (budget.getFinancialYear() != null && !budget.getIsbere().isEmpty()) {
            return budgetDefinitionRepository.findByIsbereIsAndFinancialYearIdIs(budget.getIsbere(),
                    budget.getFinancialYear().getId());
        } else
            return budgetDefinitionRepository.findAll();
    }

    public List<Budget> getParentByFinancialYearId(Long financialYearId) {
        return budgetDefinitionRepository.findByFinancialYearIdOrderByFinancialYearIdAscNameAsc(financialYearId);
    }

    public List<Budget> getReferenceBudgetByFinancialYear() {
        return budgetDefinitionRepository.findByIsbereIsAndFinancialYearIdIs("RE", financialYearId);
    }

    public List<CFinancialYear> getFinancialYear() {
        return financialYear.getAllFinancialYears();
    }

    public List<Long> getreferenceBudget(Long financialYearId) {
        final List<Budget> budget =budgetDefinitionRepository.findByFinancialYearIdIsAndReferenceBudgetIsNotNull(financialYearId);
         List<Long> referenceBudgetIdList = new ArrayList<Long>();
         for (Budget b : budget) {
             referenceBudgetIdList.add(b.getReferenceBudget().getId());
         }
         return referenceBudgetIdList;
    }

    public List<Long> getBudgetIdList()
    {
        final List<BudgetDetail> budgetDetailsList = budgetDetails.findAll();
        List<Long> budgetIdList = new ArrayList<Long>();
        for (BudgetDetail b : budgetDetailsList) {
            budgetIdList.add(b.getBudget().getId());
        }
        return budgetIdList;
    }
    public List<Budget> getreferenceBudget1(Long financialYearId, List<Long> referenceBudgetIdList) {
        return budgetDefinitionRepository.findByIsActiveBudgetTrueAndIsbereIsAndFinancialYearIdIsAndIdNotIn("RE",
                financialYearId, referenceBudgetIdList);
    }

    public List<Budget> getParentList(String isbere, Long financialYearId, List<Long> budgetIdList) {
        return budgetDefinitionRepository.findByIsbereIsAndFinancialYearIdIsAndIdNotIn(isbere, financialYearId,
                budgetIdList);
    }

    public List<Budget> getReferenceBudgetForEdit(Long id) {
        return budgetDefinitionRepository.findByIdIs(id);
    }
    
    public String validate(final Budget budget, final BindingResult errors) {
        String validationMessage = "";
       
        if (budget.getParent()!=null && budget.getParent().getId() != null && budget.getParent().getId() > 0) {
            final List<Budget> b =budgetDefinitionRepository.findByIdIs(Long.valueOf(budget.getParent().getId()));
            if (!b.get(0).getIsbere().equals(budget.getIsbere()))
                validationMessage = messageSource.getMessage("budget.invalid.parent", new String[] { b.get(0).getName() },
                        null);
        }
        if (budget.getIsPrimaryBudget() && budget.getFinancialYear() != null && (budget.getParent() == null)) {
            final List<Budget> budgetList =budgetDefinitionRepository.findByIsbereIsAndFinancialYearIdIsAndIsPrimaryBudgetTrueAndParentIsNull(budget.getIsbere(),budget.getFinancialYear().getId());
           if(!budgetList.isEmpty())
            validationMessage = messageSource.getMessage("budget.primary.invalid1", new String[] { budgetList.get(0).getName(),budgetList.get(0).getFinancialYear().getFinYearRange() },
                    null);
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
    
    public EgwStatus getBudgetStatus()
    {
        return egwStatusHibernate.getStatusByModuleAndCode("BUDGET","Created");
    }
    
    public List<BudgetDetail> getBudgetDetailList(Long budgetId)
    {
        return budgetDetails.getBudgetDetailsByBudgetId(budgetId);
    }
  
}