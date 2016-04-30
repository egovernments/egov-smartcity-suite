package org.egov.works.master.services;

import org.egov.commons.ContractorGrade;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.utils.WorksConstants;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContractorGradeService extends PersistenceService<ContractorGrade, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    public ContractorGrade getContractorGradeById(final Long contractorGradeId) {
        final ContractorGrade contractorGrade = entityManager.find(ContractorGrade.class, contractorGradeId);
        return contractorGrade;
    }

    public List<ContractorGrade> getAllContractorGrades() {
        final Query query = entityManager.createQuery("from ContractorGrade order by upper(grade)");
        final List<ContractorGrade> contractorGradeList = query.getResultList();
        return contractorGradeList;
    }

    public SearchQuery prepareSearchQuery(final Map<String, Object> criteriaMap) {
        final StringBuffer contractorGradeSql = new StringBuffer(100);
        String contractorGradeStr = "";
        final List<Object> paramList = new ArrayList<Object>();
        contractorGradeSql.append(" from ContractorGrade cg");
        final String grade = (String) criteriaMap.get(WorksConstants.GRADE);
        final Double minAmount = (Double) criteriaMap.get(WorksConstants.MIN_AMOUNT);
        final Double maxAmount = (Double) criteriaMap.get(WorksConstants.MAX_AMOUNT);
        if (grade != null && !grade.trim().equals("") || minAmount != -1 || maxAmount != -1)
            contractorGradeSql.append(" where 1=1");

        if (grade != null && !grade.trim().equals("")) {
            contractorGradeSql.append(" and UPPER(cg.grade) like ?");
            paramList.add("%" + grade.trim().toUpperCase() + "%");
        }

        if (minAmount != -1) {
            contractorGradeSql.append(" and cg.minAmount = ?");
            paramList.add(BigDecimal.valueOf(minAmount));
        }

        if (maxAmount != -1) {
            contractorGradeSql.append(" and cg.maxAmount = ?");
            paramList.add(BigDecimal.valueOf(maxAmount));
        }
        contractorGradeSql.append(" group by cg.id");
        contractorGradeStr = contractorGradeSql.toString();
        final String countQuery = "select count(*) " + contractorGradeStr;
        return new SearchQueryHQL(contractorGradeStr, countQuery, paramList);
    }
}
