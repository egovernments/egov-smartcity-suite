package org.egov.works.reports.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.works.lineestimate.entity.LineEstimateAppropriation;
import org.egov.works.reports.entity.EstimateAppropriationRegisterSearchRequest;
import org.egov.works.reports.entity.WorkProgressRegister;
import org.egov.works.reports.entity.WorkProgressRegisterSearchRequest;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

@Service
public class EstimateAppropriationRegisterService {

    @PersistenceContext
    private EntityManager entityManager;
    
    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }
    
    public List<LineEstimateAppropriation> searchEstimateAppropriationRegister(
            EstimateAppropriationRegisterSearchRequest estimateAppropriationRegisterSearchRequest) {
        if (estimateAppropriationRegisterSearchRequest != null) {
            final Criteria criteria = getCurrentSession().createCriteria(LineEstimateAppropriation.class)
                    .createAlias("lineEstimateDetails", "lineEstimateDetails")
                    .createAlias("lineEstimateDetails.lineEstimate", "lineEstimate")
                    .createAlias("budgetUsage", "budgetUsage")
                    .createAlias("budgetUsage.budgetDetail", "budgetDetail")
                    .createAlias("budgetDetail.budget", "budget")
                    .createAlias("budget.financialYear", "financialYear");
                    criteria.add(Restrictions.eq("lineEstimate.executingDepartment.id",estimateAppropriationRegisterSearchRequest.getDepartment()));
                    criteria.add(Restrictions.eq("lineEstimate.fund.id",estimateAppropriationRegisterSearchRequest.getFund().intValue()));
                    criteria.add(Restrictions.eq("lineEstimate.function.id",estimateAppropriationRegisterSearchRequest.getFunction()));
                    criteria.add(Restrictions.eq("financialYear.id",estimateAppropriationRegisterSearchRequest.getFinancialYear()));
                    criteria.add(Restrictions.le("financialYear.startingDate",estimateAppropriationRegisterSearchRequest.getAsOnDate()));
                    criteria.add(Restrictions.eq("lineEstimate.budgetHead.id",estimateAppropriationRegisterSearchRequest.getBudgetHead()));
                   
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            return criteria.list();
        } else
            return new ArrayList<LineEstimateAppropriation>();
    }
}
    
