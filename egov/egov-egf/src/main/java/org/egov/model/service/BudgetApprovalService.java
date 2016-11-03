package org.egov.model.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.service.CFinancialYearService;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.repository.BudgetApprovalRepository;
import org.egov.services.budget.BudgetDetailService;
import org.egov.utils.FinancialConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BudgetApprovalService {

    private final BudgetApprovalRepository budgetApprovalRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource messageSource;
    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;
    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernate;
    @Autowired
    private CFinancialYearService cFinancialYearService;
    @Autowired
    private BudgetDetailService budgetDetailService;

    @Autowired
    public BudgetApprovalService(final BudgetApprovalRepository BudgetApprovalRepository) {
        this.budgetApprovalRepository = BudgetApprovalRepository;
    }

    public EgwStatus getBudgetStatus(String moduleType, String code) {
        return egwStatusHibernate.getStatusByModuleAndCode(moduleType, code);
    }

    public List<CFinancialYear> financialYearList() {
       // List<CFinancialYear> financialYears = cFinancialYearService.getFinancialYears(financialYear());
        List<CFinancialYear> financialYears = cFinancialYearService.findAll();
        return financialYears;
    }

    public List<Long> financialYear() {
        final String query = "select bd.financialYear.id from Budget bd where bd.isbere='RE' and "
                + "bd.status.id=:statusId";
        List<Long> budgetDetailsList = persistenceService.getSession().createQuery(query)
                .setParameter("statusId", getBudgetStatus(FinancialConstants.BUDGET, "Created").getId()).list();
        return budgetDetailsList;
    }

    public List<BudgetDetail> search(Long financialYearId) {
        return budgetDetailService.getBudgetDetailByStatusAndFinancialYearId(
                getBudgetStatus(FinancialConstants.BUDGETDETAIL, "VERIFIED").getId(), financialYearId);
    }

}