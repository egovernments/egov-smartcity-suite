package org.egov.model.service;

import org.egov.infstr.services.PersistenceService;
import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetUploadReport;
import org.egov.services.budget.BudgetService;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BudgetUploadReportService {

    @Autowired
    @Qualifier("budgetService")
    private BudgetService budgetService;

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    public List<BudgetUploadReport> search(BudgetUploadReport budgetUploadReport) {

        String reMaterializedPath = "", beMaterializedPath = "";
        Long functionId = null, deptId = null;
        Integer fundId = null;
        Budget reBudget = new Budget();
        Budget beBudget = new Budget();
        if (budgetUploadReport.getReBudget() != null && budgetUploadReport.getReBudget().getId() != null) {
            reBudget = budgetService.findById(budgetUploadReport.getReBudget().getId(), false);
            reMaterializedPath = reBudget.getMaterializedPath();
        }

        if (reBudget != null) {
            beBudget = budgetService.getReferenceBudgetFor(reBudget);
            beMaterializedPath = beBudget.getMaterializedPath();
        }

        if (budgetUploadReport.getFund() != null && budgetUploadReport.getFund().getId() != null)
            fundId = budgetUploadReport.getFund().getId();

        if (budgetUploadReport.getFunction() != null && budgetUploadReport.getFunction().getId() != null)
            functionId = budgetUploadReport.getFunction().getId();

        if (budgetUploadReport.getDepartment() != null && budgetUploadReport.getDepartment().getId() != null)
            deptId = budgetUploadReport.getDepartment().getId();

        List<BudgetUploadReport> budgetUploadReportList = new ArrayList<BudgetUploadReport>();
        Query query = null;
        query = persistenceService.getSession()
                .createSQLQuery(getQuery(reMaterializedPath, beMaterializedPath, fundId, functionId, deptId))
                .addScalar("fundCode", StringType.INSTANCE)
                .addScalar("functionCode", StringType.INSTANCE)
                .addScalar("glCode", StringType.INSTANCE)
                .addScalar("deptCode", StringType.INSTANCE)
                .addScalar("approvedReAmount", BigDecimalType.INSTANCE)
                .addScalar("planningReAmount", BigDecimalType.INSTANCE)
                .addScalar("approvedBeAmount", BigDecimalType.INSTANCE)
                .addScalar("planningBeAmount", BigDecimalType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(BudgetUploadReport.class));
        budgetUploadReportList = query.list();

        return budgetUploadReportList;
    }

    public String getQuery(final String reMaterializedPath, final String beMaterializedPath,
            final Integer fundId, final Long functionId,
            final Long deptId) {

        String subQuery = "", reMaterializedPathQuery = "", beMaterializedPathQuery = "";
        if (reMaterializedPath != null)
            reMaterializedPathQuery = " and bd.materializedpath like '" + reMaterializedPath + "%'";
        if (beMaterializedPath != null)
            beMaterializedPathQuery = " and bd.materializedpath like '" + beMaterializedPath + "%'";
        if (fundId != null)
            subQuery = subQuery + " and bd.fund = " + fundId;
        if (deptId != null)
            subQuery = subQuery + " and bd.executing_department  = " + deptId;
        if (functionId != null)
            subQuery = subQuery + " and bd.function = " + functionId;

        return "SELECT budgetuploadreport.fundCode ,budgetuploadreport.functionCode,budgetuploadreport.glCode,budgetuploadreport.deptCode,sum(budgetuploadreport.approvedReAmount) as approvedReAmount,sum(budgetuploadreport.planningReAmount) as planningReAmount,sum(budgetuploadreport.approvedBeAmount) as approvedBeAmount,sum(budgetuploadreport.planningBeAmount) as planningBeAmount FROM "
                + "(SELECT f.code AS fundCode,fn.code  AS functionCode,coa.glcode  AS glCode,dept.code  AS deptCode,bd.approvedamount AS approvedReAmount,bd.budgetavailable AS planningReAmount,0 AS approvedBeAmount,0 AS planningBeAmount  FROM egf_budgetdetail bd , egf_budgetgroup bg , fund f, "
                + "function fn,eg_department dept,chartofaccounts coa,egw_status st WHERE bd.status  = st.id  AND st.moduletype  = 'BUDGETDETAIL' AND st.code  = 'Created' AND bd.budgetgroup = bg.id  AND bg.mincode = coa.id "
                + "AND bd.fund = f.id AND bd.function  = fn.id AND bd.executing_department = dept.id "
                + subQuery
                + " "
                + reMaterializedPathQuery
                + " UNION SELECT f.code  AS fundCode,fn.code AS functionCode,coa.glcode AS glCode, "
                + "dept.code  AS deptCode,0 AS approvedReAmount,0 AS planningReAmount,bd.approvedamount AS approvedBeAmount,bd.budgetavailable AS planningBeAmount FROM egf_budgetdetail bd ,egf_budgetgroup bg ,fund f,function fn, eg_department dept,chartofaccounts coa,egw_status st WHERE bd.status = st.id "
                + "AND st.moduletype = 'BUDGETDETAIL' AND st.code   = 'Created' AND bd.budgetgroup  = bg.id AND bg.mincode  = coa.id AND bd.fund  = f.id AND bd.function = fn.id AND bd.executing_department = dept.id "
                + subQuery
                + " "
                + beMaterializedPathQuery
                + " )  budgetuploadreport GROUP BY budgetuploadreport.fundCode ,budgetuploadreport.functionCode,budgetuploadreport.glCode,budgetuploadreport.deptCode ";
    }
}