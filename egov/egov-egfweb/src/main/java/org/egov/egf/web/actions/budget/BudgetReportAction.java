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
package org.egov.egf.web.actions.budget;

import net.sf.jasperreports.engine.JRException;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.repository.FunctionRepository;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.budget.BudgetGroup;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.services.budget.BudgetDetailService;
import org.egov.services.budget.BudgetService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.utils.ReportHelper;
import org.hibernate.query.Query;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.ObjectType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;

@ParentPackage("egov")
@Results(value = {
        @Result(name = "department-PDF", type = "stream", location = Constants.INPUT_STREAM, params = {Constants.INPUT_NAME,
                Constants.INPUT_STREAM,
                Constants.CONTENT_TYPE, "application/pdf", Constants.CONTENT_DISPOSITION, "no-cache;filename=BudgetReport.pdf"}),
        @Result(name = "department-XLS", type = "stream", location = Constants.INPUT_STREAM, params = {Constants.INPUT_NAME,
                Constants.INPUT_STREAM,
                Constants.CONTENT_TYPE, "application/xls", Constants.CONTENT_DISPOSITION, "no-cache;filename=BudgetReport.xls"}),
        @Result(name = "department-HTML", type = "stream", location = Constants.INPUT_STREAM, params = {Constants.INPUT_NAME,
                Constants.INPUT_STREAM,
                Constants.CONTENT_TYPE, "text/html"}),
        @Result(name = "functionwise-PDF", type = "stream", location = Constants.INPUT_STREAM, params = {Constants.INPUT_NAME,
                Constants.INPUT_STREAM,
                Constants.CONTENT_TYPE, "application/pdf", Constants.CONTENT_DISPOSITION,
                "no-cache;filename=BudgetReport-functionwise.pdf"}),
        @Result(name = "functionwise-XLS", type = "stream", location = Constants.INPUT_STREAM, params = {Constants.INPUT_NAME,
                Constants.INPUT_STREAM,
                Constants.CONTENT_TYPE, "application/xls", Constants.CONTENT_DISPOSITION,
                "no-cache;filename=BudgetReport-functionwise.xls"}),
        @Result(name = "functionwise-HTML", type = "stream", location = Constants.INPUT_STREAM, params = {Constants.INPUT_NAME,
                Constants.INPUT_STREAM,
                Constants.CONTENT_TYPE, "text/html"}),
        @Result(name = "functionwise-dept-HTML", type = "stream", location = Constants.INPUT_STREAM, params = {
                Constants.INPUT_NAME, Constants.INPUT_STREAM,
                Constants.CONTENT_TYPE, "text/html"}),
        @Result(name = "printFunctionwise", location = "budgetReport-printFunctionwise.jsp"),
        @Result(name = "print", location = "budgetReport-print.jsp"),
        @Result(name = "functionwise", location = "budgetReport-functionwise.jsp"),
        @Result(name = "atGlance", location = "budgetReport-atGlance.jsp"),
        @Result(name = "dept", location = "budgetReport-dept.jsp"),
})
public class BudgetReportAction extends BaseFormAction {
    private static final String DEPTWISEPATH = "/reports/templates/departmentWiseBudgetReport.jasper";
    private static final String FUNCTIONWISEPATH = "/reports/templates/budgetReportFunctionwise.jasper";
    private static final String WORKINGCOPYFORFINALAPPROVER = "/reports/templates/budgetReportWorkingCopyForFinalApprover.jasper";
    private static final String WORKINGCOPYWITHALLMOUNTS = "/reports/templates/budgetReportWorkingCopy.jasper";
    private static final String WORKINGCOPYWITHONLYPROPOSALS = "/reports/templates/budgetReportWorkingCopyWithOnlyProposals.jasper";
    private static final long serialVersionUID = 1L;
    private static final String EMPTYSTRING = "";
    private static final String TOTALROW = "totalrow";
    private static final String TOTALSTRING = "TOTAL";
    private static Logger LOGGER = Logger.getLogger(BudgetReportAction.class);
    private final Map<String, String> coaMap = new HashMap<String, String>();
    private final Map<Object, BigDecimal> reAppropriationMap = new HashMap<Object, BigDecimal>();
    private final List<BudgetReportView> reportStoreList = new ArrayList<BudgetReportView>();
    private final List<BudgetDetail> budgetDetailListForBE = new ArrayList<BudgetDetail>();
    protected boolean canViewApprovedAmount = true;// shows both proposed and recommended like fmus
    protected boolean finalApprover = false;// shows only the recommended value like commissioner
    private ReportHelper reportHelper;
    private InputStream inputStream;
    private BudgetReport budgetReport = new BudgetReport();
    @Autowired
    private EisCommonService eisCommonService;
    private List budgetReportList = new ArrayList<BudgetReportView>();
    @Autowired
    private AppConfigValueService appConfigValuesService;
    private int majorCodeLength = 0;
    @Autowired
    private FinancialYearDAO financialYearDAO;
    @Autowired
    @Qualifier("budgetService")
    private BudgetService budgetService;
    private Map<String, String> refNoMap = new HashMap<String, String>();
    @Autowired
    @Qualifier("budgetDetailService")
    private BudgetDetailService budgetDetailService;
    private boolean onSaveOrForward = false;
    private List<BudgetDetail> budgetDetailListForRE = new ArrayList<BudgetDetail>();
    private CFinancialYear financialYearForRE;
    private CFinancialYear financialYearForBE;
    private String path = FUNCTIONWISEPATH;
    private String isBERE = "RE";
    private BudgetDetail budgetDetail;
    private Position pos;
    private Budget topBudget;
    private boolean departmentBudget = false;
    private String workFlowstateCondn = "";
    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private FunctionRepository functionRepository;

    public BudgetReportAction() {
        addRelatedEntity("department", Department.class);
        addRelatedEntity("function", CFunction.class);
        addRelatedEntity("financialYear", CFinancialYear.class);
    }

    public boolean isDepartmentBudget() {
        return departmentBudget;
    }

    public void setDepartmentBudget(final boolean departmentBudget) {
        this.departmentBudget = departmentBudget;
    }

    public BudgetDetail getBudgetDetail() {
        return budgetDetail;
    }

    public void setBudgetDetail(final BudgetDetail budgetDetail) {
        this.budgetDetail = budgetDetail;
    }

    public void setBudgetService(final BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    public void setFinancialYearDAO(final FinancialYearDAO financialYearDAO) {
        this.financialYearDAO = financialYearDAO;
    }

    public void setReportHelper(final ReportHelper reportHelper) {
        this.reportHelper = reportHelper;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public Object getModel() {
        return budgetReport;
    }

    @Action(value = "/budget/budgetReport-functionwise")
    public String functionwise() {
        return "functionwise";
    }

    @Action(value = "/budget/budgetReport-atGlance")
    public String atGlance() {
        return "atGlance";
    }

    @Override
    public void prepare() {
        super.prepare();
        addDropdownData("departmentList", departmentService.getAllDepartments());
        addDropdownData("functionList", functionRepository.findByIsActiveAndIsNotLeaf(true, false));
        addDropdownData("financialYearList", getPersistenceService().findAllBy(
                "from CFinancialYear where isActive=true  order by finYearRange desc "));
        setRelatedEntitesOn();
        majorCodeLength = Integer.valueOf(getAppConfigValueFor(Constants.EGF, "coa_majorcode_length"));
    }

    @Action(value = "/budget/budgetReport-getFunctionwiseReport")
    public String getFunctionwiseReport() {
        return "printFunctionwise";
    }

    @Action(value = "/budget/budgetReport-ajaxGenerateFunctionWiseHtml")
    public String ajaxGenerateFunctionWiseHtml() throws IOException {
        if (topBudget != null && topBudget.getId() != null)
            topBudget = budgetService.find("from Budget where id=?1", topBudget.getId());
        if (departmentBudget)
            workFlowstateCondn = " and (bd.status.code='Approved')";

        final Map<String, Object> paramMap = getParamMap();
        inputStream = reportHelper.exportHtml(inputStream, path, paramMap, getDataForFunctionwise(), "pt");
        return "functionwise-HTML";
    }

    public String ajaxFunctionWiseConsolidated() throws IOException {
        final List<Object> dataForFunctionwiseForConsolidation = getDataForFunctionwiseForConsolidation();
        final Map<String, Object> paramMap = getParamMap();
        inputStream = reportHelper.exportHtml(inputStream, path, paramMap, dataForFunctionwiseForConsolidation, "pt");
        return "functionwise-HTML";
    }

    @Action(value = "/budget/budgetReport-generateFunctionWisePdf")
    public String generateFunctionWisePdf() throws JRException, IOException {
        final Map<String, Object> paramMap = getParamMap();
        inputStream = reportHelper.exportPdf(inputStream, path, paramMap, getDataForFunctionwise());
        return "functionwise-PDF";
    }

    @Action(value = "/budget/budgetReport-generateFunctionWiseXls")
    public String generateFunctionWiseXls() throws JRException, IOException {
        final Map<String, Object> paramMap = getParamMap();
        inputStream = reportHelper.exportXls(inputStream, path, paramMap, getDataForFunctionwise());
        return "functionwise-XLS";
    }

    public String getAtGlanceReport() {
        return "printAtGlance";
    }

    @Action(value = "/budget/budgetReport-ajaxGenerateAtGlanceHtml")
    public String ajaxGenerateAtGlanceHtml() throws IOException {
        final Map<String, Object> paramMap = getParamMap();
        paramMap.put("heading", "BUDGET AT GLANCE ");
        inputStream = reportHelper.exportHtml(inputStream, path, paramMap, getDataForGlance(), "pt");
        return "functionwise-HTML";
    }

    public String generateAtGlancePdf() throws JRException, IOException {
        final Map<String, Object> paramMap = getParamMap();
        paramMap.put("heading", "BUDGET AT GLANCE ");
        inputStream = reportHelper.exportPdf(inputStream, path, paramMap, getDataForGlance());
        return "functionwise-PDF";
    }

    public String generateAtGlanceXls() throws JRException, IOException {
        final Map<String, Object> paramMap = getParamMap();
        paramMap.put("heading", "BUDGET AT GLANCE ");
        inputStream = reportHelper.exportXls(inputStream, path, paramMap, getDataForGlance());
        return "functionwise-XLS";
    }

    private Map<String, Map<String, Object>> getSql() {
        final Map<String, Map<String, Object>> queryMap = new HashMap<>();
        final Map<String, Object> queryParams = new HashMap<>();
        StringBuilder sql = new StringBuilder("");
        sql.append(" bd.budget.financialYear.id=:finYearId");
        queryParams.put("finYearId", Long.valueOf(budgetReport.getFinancialYear().getId()));
        if (budgetReport.getDepartment() != null && budgetReport.getDepartment().getId() != null) {
            sql.append(" and bd.executingDepartment.id=:execDeptId");
            queryParams.put("execDeptId", Long.valueOf(budgetReport.getDepartment().getId()));
        }
        if (budgetReport.getFunction() != null && budgetReport.getFunction().getId() != null) {
            sql.append(" and bd.function.id=:functionId");
            queryParams.put("functionId", Long.valueOf(budgetReport.getFunction().getId()));
        }
        queryMap.put(sql.toString(), queryParams);
        return queryMap;
    }

    private List<BudgetDetail> getMincodeData() {
        final String finalStatus = getFinalStatus();
        final Map.Entry<String, Map<String, Object>> queryMapEntry = getSql().entrySet().iterator().next();
        final StringBuilder sql = new StringBuilder(queryMapEntry.getKey());
        final Map<String, Object> queryParams = queryMapEntry.getValue();
        if (budgetReport.getType().equalsIgnoreCase("IE"))
            sql.append(" and bd.budgetGroup.minCode.type in ('I','E')");
        else if (!budgetReport.getType().equalsIgnoreCase("All")) {
            sql.append(" and bd.budgetGroup.minCode.type =:minCodeType");
            queryParams.put("minCodeType", budgetReport.getType().charAt(0));
        }
        List<BudgetDetail> budgetDetailList = new ArrayList<BudgetDetail>();
        StringBuilder queryString = new StringBuilder(" from BudgetDetail bd where ")
                .append(sql)
                .append(" and bd.budget.isbere='RE' order by bd.executingDepartment,bd.function.name,bd.budgetGroup.minCode.type,bd.budgetGroup.minCode.glcode");
        if (isOnSaveOrForward()) {
            final Query qry1 = getPersistenceService().getSession().createQuery(queryString.toString());
            queryParams.entrySet().forEach(entry -> qry1.setParameter(entry.getKey(), entry.getValue()));
            budgetDetailList = qry1.list();

            if (budgetDetailList.isEmpty()) {
                StringBuilder query = new StringBuilder(" from BudgetDetail bd where ")
                        .append(sql)
                        .append(" and bd.budget.isbere='BE' order by bd.executingDepartment,bd.function.name,bd.budgetGroup.minCode.type,bd.budgetGroup.minCode.glcode");
                final Query qry2 = getPersistenceService().getSession().createQuery(query.toString());
                queryParams.entrySet().forEach(entry -> qry2.setParameter(entry.getKey(), entry.getValue()));
                budgetDetailList = qry2.list();
            }
        } else {
            StringBuilder query1 = new StringBuilder(" from BudgetDetail bd where ")
                    .append(sql)
                    .append(" and bd.budget.isbere='RE' and bd.approvedAmount is not null  and bd.budget.status.code=:status")
                    .append(" order by bd.executingDepartment,bd.function.name,bd.budgetGroup.minCode.type,bd.budgetGroup.minCode.glcode");
            final Query qry1 = getPersistenceService().getSession().createQuery(query1.toString());
            qry1.setParameter("status", finalStatus, StringType.INSTANCE);
            queryParams.entrySet().forEach(entry -> qry1.setParameter(entry.getKey(), entry.getValue()));
            budgetDetailList = qry1.list();
            isBERE = "RE";
            if (budgetDetailList.isEmpty()) {
                StringBuilder query2 = new StringBuilder(" from BudgetDetail bd where ")
                        .append(sql)
                        .append(" and bd.budget.isbere='BE' and  bd.budget.status.code =:status")
                        .append(" order by bd.executingDepartment,bd.function.name,bd.budgetGroup.minCode.type,bd.budgetGroup.minCode.glcode");
                final Query qry2 = getPersistenceService().getSession().createQuery(query2.toString());
                qry2.setParameter("status", finalStatus, StringType.INSTANCE);
                queryParams.entrySet().forEach(entry -> qry2.setParameter(entry.getKey(), entry.getValue()));
                budgetDetailList = qry2.list();
                isBERE = "BE";
            }
        }
        return budgetDetailList;
    }

    @SuppressWarnings("unchecked")
    private void getMincodeDataForWorkingCopy() {
        String sql;
        /*if (budgetReport.getType().equalsIgnoreCase("IE"))
            sql = sql + " and bd.budgetGroup.minCode.type in ('I','E')";
        else if (!budgetReport.getType().equalsIgnoreCase("All"))
            sql = sql + " and bd.budgetGroup.minCode.type='" + budgetReport.getType() + "'";
*/
        Map.Entry<String, Map<String, Object>> queryMapEntry = getSqlForFinYear(financialYearForRE.getId()).entrySet().iterator().next();
        sql = queryMapEntry.getKey();
        Map<String, Object> queryParams = queryMapEntry.getValue();
        StringBuilder query = new StringBuilder(" from BudgetDetail bd where ")
                .append(sql)
                .append(workFlowstateCondn)
                .append(" and bd.budget.isbere='RE'   order by bd.executingDepartment,bd.function,bd.budgetGroup.minCode.type,bd.budgetGroup.minCode.glcode");
        final Query qry = getPersistenceService().getSession().createQuery(query.toString());
        queryParams.entrySet().forEach(entry -> qry.setParameter(entry.getKey(), entry.getValue()));
        budgetDetailListForRE = qry.list();

        queryMapEntry = getSqlForFinYearBE(financialYearForBE.getId()).entrySet().iterator().next();
        sql = queryMapEntry.getKey();
        queryParams = queryMapEntry.getValue();
        StringBuilder query2 = new StringBuilder(" from BudgetDetail bd where ")
                .append(sql)
                .append(workFlowstateCondn)
                .append(" and bd.budget.isbere='BE'   order by bd.executingDepartment,bd.function,bd.budgetGroup.minCode.type,bd.budgetGroup.minCode.glcode");
        final Query qry2 = getPersistenceService().getSession().createQuery(query2.toString());
        queryParams.entrySet().forEach(entry -> qry2.setParameter(entry.getKey(), entry.getValue()));
        budgetDetailListForBE.addAll(qry2.list());
    }

    @SuppressWarnings("unchecked")
    private List<BudgetDetail> getMajorcodeData() {
        final String finalStatus = getFinalStatus();
        final Map.Entry<String, Map<String, Object>> queryMapEntry = getSql().entrySet().iterator().next();
        final StringBuilder sql = new StringBuilder(queryMapEntry.getKey());
        final Map<String, Object> queryParams = queryMapEntry.getValue();
        if (budgetReport.getType().equalsIgnoreCase("IE"))
            sql.append(" and bd.budgetGroup.majorCode.type in ('I','E')");
        else if (!budgetReport.getType().equalsIgnoreCase("All")) {
            sql.append(" and bd.budgetGroup.majorCode.type=:majorCodeType");
            queryParams.put("majorCodeType", budgetReport.getType().charAt(0));
        }
        List<BudgetDetail> budgetDetailList = new ArrayList<BudgetDetail>();
        if (onSaveOrForward) {
            StringBuilder query = new StringBuilder(" from BudgetDetail bd where ")
                    .append(sql)
                    .append("  order by bd.executingDepartment,bd.function.name,bd.budgetGroup.majorCode.type,bd.budgetGroup.majorCode.glcode");

            final Query qry = getPersistenceService().getSession().createQuery(query.toString());
            queryParams.entrySet().forEach(entry -> qry.setParameter(entry.getKey(), entry.getValue()));
            budgetDetailList = qry.list();

            if (budgetDetailList.isEmpty()) {
                StringBuilder query1 = new StringBuilder(" from BudgetDetail bd where ")
                        .append(sql)
                        .append(" and bd.budget.isbere='BE'  order by bd.executingDepartment,bd.function.name,bd.budgetGroup.majorCode.type,")
                        .append(" bd.budgetGroup.majorCode.glcode");
                final Query qry1 = getPersistenceService().getSession().createQuery(query1.toString());
                queryParams.entrySet().forEach(entry -> qry1.setParameter(entry.getKey(), entry.getValue()));
                budgetDetailList = qry1.list();
            }
        } else {
            StringBuilder query2 = new StringBuilder(" from BudgetDetail bd where ")
                    .append(sql)
                    .append(" and bd.budget.isbere='RE' and bd.budget.status.code=:status")
                    .append(" order by bd.executingDepartment,bd.function.name,bd.budgetGroup.majorCode.type,bd.budgetGroup.majorCode.glcode");
            final Query qry2 = getPersistenceService().getSession().createQuery(query2.toString());
            queryParams.entrySet().forEach(entry -> qry2.setParameter(entry.getKey(), entry.getValue()));
            qry2.setParameter("status", finalStatus, StringType.INSTANCE);
            budgetDetailList = qry2.list();
            if (budgetDetailList.isEmpty()) {
                StringBuilder query3 = new StringBuilder(" from BudgetDetail bd where ")
                        .append(sql)
                        .append(" and bd.budget.isbere='BE'  and bd.budget.status.code=:status")
                        .append(" order by bd.executingDepartment,bd.function.name,bd.budgetGroup.majorCode.type,bd.budgetGroup.majorCode.glcode");
                final Query qry3 = getPersistenceService().getSession().createQuery(query3.toString());
                queryParams.entrySet().forEach(entry -> qry3.setParameter(entry.getKey(), entry.getValue()));
                qry3.setParameter("status", finalStatus, StringType.INSTANCE);
                budgetDetailList = qry3.list();
            }
        }
        return budgetDetailList;
    }

    @SuppressWarnings("unchecked")
    private void getMajorcodeDataForWorkingCopy() {
        String sql;

        /*if (budgetReport.getType().equalsIgnoreCase("IE"))
            sql = sql + " and bd.budgetGroup.majorCode.type in ('I','E')";
        else if (!budgetReport.getType().equalsIgnoreCase("All"))
            sql = sql + " and bd.budgetGroup.majorCode.type='" + budgetReport.getType() + "'";*/

        Map.Entry<String, Map<String, Object>> queryMapEntry = getSqlForFinYear(financialYearForRE.getId()).entrySet().iterator().next();
        sql = queryMapEntry.getKey();
        Map<String, Object> queryParams = queryMapEntry.getValue();
        StringBuilder query = new StringBuilder(" from BudgetDetail bd where ")
                .append(sql)
                .append(workFlowstateCondn)
                .append(" order by bd.executingDepartment,bd.function,bd.budgetGroup.majorCode.type,bd.budgetGroup.majorCode.glcode");
        final Query qry = getPersistenceService().getSession().createQuery(query.toString());
        queryParams.entrySet().forEach(entry -> qry.setParameter(entry.getKey(), entry.getValue()));
        budgetDetailListForRE.addAll(qry.list());

        queryMapEntry = getSqlForFinYearBE(financialYearForBE.getId()).entrySet().iterator().next();
        sql = queryMapEntry.getKey();
        queryParams = queryMapEntry.getValue();

        StringBuilder query1 = new StringBuilder(" from BudgetDetail bd where ")
                .append(sql)
                .append(workFlowstateCondn)
                .append(" and bd.budget.isbere='BE'  order by bd.executingDepartment,bd.function,bd.budgetGroup.majorCode.type,bd.budgetGroup.majorCode.glcode");
        final Query qry1 = getPersistenceService().getSession().createQuery(query1.toString());
        queryParams.entrySet().forEach(entry -> qry1.setParameter(entry.getKey(), entry.getValue()));
        budgetDetailListForBE.addAll(qry1.list());
    }

    private List<Object> getDataForFunctionwise() {
        if (onSaveOrForward)
            return getDataFunctionWiseForWorkingCopy();
        List<BudgetDetail> budgetDetailList = null;
        budgetDetailList = getMincodeData();
        budgetDetailList.addAll(getMajorcodeData());
        if (budgetDetailList.isEmpty())
            return budgetReportList;

        Integer deptId = 0;
        Long functionId = 0L;
        String type = "", majorCode = "", glcode = "", glType = "", glName = "", tempMajorCode = "";
        BigDecimal totalAmt = BigDecimal.ZERO;
        BigDecimal totalAppropriationAmt = BigDecimal.ZERO;
        BigDecimal reAppropriationAmt = BigDecimal.ZERO;
        boolean printed = true;
        boolean isFirst = true;
        boolean majorcodewise = false;
        refNoMap = getReferenceNumber("functionWiseBudgetReport");
        getCOA();
        getBudgetReappropriationAmt();
        loadAmountForMajorcodewise(budgetReport.getFinancialYear(), budgetReport.getDepartment(), budgetReport.getFunction());
        for (final BudgetDetail detail : budgetDetailList) {
            if (detail.getExecutingDepartment() == null || detail.getFunction() == null)
                continue;
            reAppropriationAmt = reAppropriationMap.get(detail.getId()) == null ? BigDecimal.ZERO : reAppropriationMap.get(detail
                    .getId());
            if (detail.getBudgetGroup().getMajorCode() == null) {
                glcode = getGlCode(detail);
                glType = detail.getBudgetGroup().getMinCode().getType().toString();
                glName = getGlName(detail);
            } else {
                glcode = detail.getBudgetGroup().getMajorCode().getGlcode();
                glType = detail.getBudgetGroup().getMajorCode().getType().toString();
                glName = detail.getBudgetGroup().getMajorCode().getName();
                majorcodewise = true;
            }
            tempMajorCode = glcode.substring(0, majorCodeLength);

            if (detail.getExecutingDepartment().getId().compareTo(deptId.longValue()) != 0) // for dept heading
            {
                if (totalAmt.compareTo(BigDecimal.ZERO) != 0 && !isFirst) {
                    budgetReportList.add(new BudgetReportView(EMPTYSTRING, EMPTYSTRING, EMPTYSTRING, TOTALSTRING, EMPTYSTRING,
                            totalAmt,
                            totalAppropriationAmt, totalAmt.add(totalAppropriationAmt), TOTALROW));
                    totalAmt = BigDecimal.ZERO;
                    totalAppropriationAmt = BigDecimal.ZERO;
                }
                budgetReportList.add(new BudgetReportView(EMPTYSTRING, EMPTYSTRING, EMPTYSTRING, detail.getExecutingDepartment()
                        .getName(), EMPTYSTRING, null, null, null, "deptrow"));
                type = "";
                functionId = null;
                majorCode = "";
            }
            if (!glType.equals(type))// for type heading
            {
                if (totalAmt.compareTo(BigDecimal.ZERO) != 0 && !isFirst) {
                    budgetReportList.add(new BudgetReportView(EMPTYSTRING, EMPTYSTRING, EMPTYSTRING, TOTALSTRING, EMPTYSTRING,
                            totalAmt,
                            totalAppropriationAmt, totalAmt.add(totalAppropriationAmt), TOTALROW));
                    totalAmt = BigDecimal.ZERO;
                    totalAppropriationAmt = BigDecimal.ZERO;
                }
                budgetReportList.add(new BudgetReportView(EMPTYSTRING, EMPTYSTRING, EMPTYSTRING, "FUNCTIONWISE "
                        + BudgetReport.getValueFor(glType).toUpperCase() + " BUDGET SUMMARY", refNoMap.get(BudgetReport
                        .getValueFor(glType)), null, null, null, "typerow"));

                functionId = null;
                majorCode = "";
            }
            if (detail.getFunction().getId().compareTo(functionId != null ? functionId : 0l) != 0)    // for function heading
            {
                if (totalAmt.compareTo(BigDecimal.ZERO) != 0 && !isFirst) {
                    budgetReportList.add(new BudgetReportView(EMPTYSTRING, EMPTYSTRING, EMPTYSTRING, TOTALSTRING, EMPTYSTRING,
                            totalAmt,
                            totalAppropriationAmt, totalAmt.add(totalAppropriationAmt), TOTALROW));
                    totalAmt = BigDecimal.ZERO;
                    totalAppropriationAmt = BigDecimal.ZERO;
                }
                budgetReportList.add(new BudgetReportView(EMPTYSTRING, EMPTYSTRING, EMPTYSTRING, "FUNCTION CENTRE-"
                        + detail.getFunction().getName(), EMPTYSTRING, null, null, null, "functionrow"));
                final List<Object> majorCodeList = getAmountForMajorcodewise(detail.getExecutingDepartment().getId().intValue(),
                        detail.getFunction().getId(), glType);  // majorcodewise total
                budgetReportList.addAll(majorCodeList);
                printed = false;
                majorCode = "";
            }
            if (!tempMajorCode.equals(majorCode) && detail.getBudgetGroup().getMajorCode() == null)// majorcodewise - heading
            {
                if (printed) {
                    budgetReportList.add(new BudgetReportView(EMPTYSTRING, EMPTYSTRING, EMPTYSTRING, TOTALSTRING, EMPTYSTRING,
                            totalAmt,
                            totalAppropriationAmt, totalAmt.add(totalAppropriationAmt), TOTALROW));
                    totalAmt = BigDecimal.ZERO;
                    totalAppropriationAmt = BigDecimal.ZERO;
                }
                budgetReportList.add(new BudgetReportView(EMPTYSTRING, EMPTYSTRING, refNoMap.get(tempMajorCode), tempMajorCode
                        + "-" + coaMap.get(tempMajorCode), "", null, null, null, "majorcodeheadingrow"));
            }

            // detail
            if (detail.getExecutingDepartment() != null && detail.getFunction() != null
                    && detail.getBudgetGroup().getMajorCode() == null)
                budgetReportList.add(new BudgetReportView(detail.getExecutingDepartment().getCode(), detail.getFunction()
                        .getCode(), glcode, glName, "", detail.getApprovedAmount(),
                        reAppropriationAmt, detail.getApprovedAmount().add(reAppropriationAmt), "detailrow"));
            if (detail.getExecutingDepartment() != null)
                deptId = detail.getExecutingDepartment().getId().intValue();
            if (detail.getFunction() != null)
                functionId = detail.getFunction().getId();
            type = glType;
            majorCode = tempMajorCode;
            totalAmt = totalAmt.add(detail.getApprovedAmount());
            totalAppropriationAmt = totalAppropriationAmt.add(reAppropriationAmt);
            printed = true;
            isFirst = false;
        }
        if (totalAmt.compareTo(BigDecimal.ZERO) != 0 && !majorcodewise)
            budgetReportList.add(new BudgetReportView(EMPTYSTRING, EMPTYSTRING, EMPTYSTRING, TOTALSTRING, EMPTYSTRING, totalAmt,
                    totalAppropriationAmt, totalAmt.add(totalAppropriationAmt), TOTALROW));
        return budgetReportList;
    }

    /**
     * function ,department and budget will be passed here
     *
     * @return
     */
    private List<Object> getDataForFunctionwiseForConsolidation() {

        List<BudgetDetail> budgetDetailList = null;

        pos = getPosition();

        if (budgetDetail.getId() != null) {
            budgetDetail = budgetDetailService.find("from BudgetDetail where id=?1", budgetDetail.getId());
            topBudget = budgetDetail.getBudget();
            budgetReport.setFinancialYear(budgetDetail.getBudget().getFinancialYear());
            budgetDetailList = budgetDetailService.findAllBy(
                    "from BudgetDetail where budget.id=?1 and state.value=?2 and function=?3", budgetDetail.getBudget().getId(),
                    "END", budgetDetail.getFunction());
        } else if (budgetDetail.getBudget().getId() != null) {
            topBudget = budgetService.find("from Budget where id=?1", budgetDetail.getBudget().getId());
            budgetReport.setFinancialYear(topBudget.getFinancialYear());
            budgetDetailList = budgetDetailService.findAllBy(
                    "from BudgetDetail where budget=?1 and( state.value=?2 or state.owner=?3)", topBudget, "END", pos);
        }

        // budgetDetailList =
        // budgetDetailService.findAllBy("from BudgetDetail where budget=? where state.value=?",budgetDetail.getBudget(),"END");

        if (budgetDetailList.isEmpty())
            return budgetReportList;

        Integer deptId = 0;
        Long functionId = 0L;
        String type = "", majorCode = "", glcode = "", glType = "", glName = "", tempMajorCode = "";
        BigDecimal totalAmt = BigDecimal.ZERO;
        BigDecimal totalAppropriationAmt = BigDecimal.ZERO;
        final BigDecimal reAppropriationAmt = BigDecimal.ZERO;
        boolean printed = true;
        boolean isFirst = true;
        boolean majorcodewise = false;
        refNoMap = getReferenceNumber("functionWiseBudgetReport");
        getCOA();
        // getBudgetReappropriationAmt();
        loadAmountForMajorcodewiseConsolidated(budgetReport.getFinancialYear(), budgetReport.getDepartment(),
                budgetReport.getFunction());
        for (final BudgetDetail detail : budgetDetailList) {
            if (detail.getExecutingDepartment() == null || detail.getFunction() == null)
                continue;
            if (detail.getBudgetGroup().getMajorCode() == null) {
                glcode = getGlCode(detail);
                glType = detail.getBudgetGroup().getMinCode().getType().toString();
                glName = getGlName(detail);
            } else {
                glcode = detail.getBudgetGroup().getMajorCode().getGlcode();
                glType = detail.getBudgetGroup().getMajorCode().getType().toString();
                glName = detail.getBudgetGroup().getMajorCode().getName();
                majorcodewise = true;
            }
            tempMajorCode = glcode.substring(0, majorCodeLength);

            if (!detail.getExecutingDepartment().getId().equals(deptId)) // for dept heading
            {
                if (totalAmt.compareTo(BigDecimal.ZERO) != 0 && !isFirst) {
                    budgetReportList.add(new BudgetReportView(EMPTYSTRING, EMPTYSTRING, EMPTYSTRING, TOTALSTRING, EMPTYSTRING,
                            totalAmt,
                            totalAppropriationAmt, totalAmt.add(totalAppropriationAmt), TOTALROW));
                    totalAmt = BigDecimal.ZERO;
                    totalAppropriationAmt = BigDecimal.ZERO;
                }
                budgetReportList.add(new BudgetReportView(EMPTYSTRING, EMPTYSTRING, EMPTYSTRING, detail.getExecutingDepartment()
                        .getName(), EMPTYSTRING, null, null, null, "deptrow"));
                type = "";
                functionId = null;
                majorCode = "";
            }
            if (!glType.equals(type))// for type heading
            {
                if (totalAmt.compareTo(BigDecimal.ZERO) != 0 && !isFirst) {
                    budgetReportList.add(new BudgetReportView(EMPTYSTRING, EMPTYSTRING, EMPTYSTRING, TOTALSTRING, EMPTYSTRING,
                            totalAmt,
                            totalAppropriationAmt, totalAmt.add(totalAppropriationAmt), TOTALROW));
                    totalAmt = BigDecimal.ZERO;
                    totalAppropriationAmt = BigDecimal.ZERO;
                }
                budgetReportList.add(new BudgetReportView(EMPTYSTRING, EMPTYSTRING, EMPTYSTRING, "FUNCTIONWISE "
                        + BudgetReport.getValueFor(glType).toUpperCase() + " BUDGET SUMMARY", refNoMap.get(BudgetReport
                        .getValueFor(glType)), null, null, null, "typerow"));

                functionId = null;
                majorCode = "";
            }
            if (!detail.getFunction().getId().equals(functionId))    // for function heading
            {
                if (totalAmt.compareTo(BigDecimal.ZERO) != 0 && !isFirst) {
                    budgetReportList.add(new BudgetReportView(EMPTYSTRING, EMPTYSTRING, EMPTYSTRING, TOTALSTRING, EMPTYSTRING,
                            totalAmt,
                            totalAppropriationAmt, totalAmt.add(totalAppropriationAmt), TOTALROW));
                    totalAmt = BigDecimal.ZERO;
                    totalAppropriationAmt = BigDecimal.ZERO;
                }
                budgetReportList.add(new BudgetReportView(EMPTYSTRING, EMPTYSTRING, EMPTYSTRING, "FUNCTION CENTRE-"
                        + detail.getFunction().getName(), EMPTYSTRING, null, null, null, "functionrow"));
                final List<Object> majorCodeList = getAmountForMajorcodewise(detail.getExecutingDepartment().getId().intValue(),
                        detail.getFunction().getId(), glType);  // majorcodewise total
                budgetReportList.addAll(majorCodeList);
                printed = false;
                majorCode = "";
            }
            if (!tempMajorCode.equals(majorCode) && detail.getBudgetGroup().getMajorCode() == null)// majorcodewise - heading
            {
                if (printed) {
                    budgetReportList.add(new BudgetReportView(EMPTYSTRING, EMPTYSTRING, EMPTYSTRING, TOTALSTRING, EMPTYSTRING,
                            totalAmt,
                            totalAppropriationAmt, totalAmt.add(totalAppropriationAmt), TOTALROW));
                    totalAmt = BigDecimal.ZERO;
                    totalAppropriationAmt = BigDecimal.ZERO;
                }
                budgetReportList.add(new BudgetReportView(EMPTYSTRING, EMPTYSTRING, refNoMap.get(tempMajorCode), tempMajorCode
                        + "-" + coaMap.get(tempMajorCode), "", null, null, null, "majorcodeheadingrow"));
            }

            // detail
            if (detail.getExecutingDepartment() != null && detail.getFunction() != null
                    && detail.getBudgetGroup().getMajorCode() == null)
                budgetReportList.add(new BudgetReportView(detail.getExecutingDepartment().getCode(), detail.getFunction()
                        .getCode(), glcode, glName, "", detail.getApprovedAmount(),
                        reAppropriationAmt, detail.getApprovedAmount().add(reAppropriationAmt), "detailrow"));
            if (detail.getExecutingDepartment() != null)
                deptId = detail.getExecutingDepartment().getId().intValue();
            if (detail.getFunction() != null)
                functionId = detail.getFunction().getId();
            type = glType;
            majorCode = tempMajorCode;
            totalAmt = totalAmt.add(detail.getApprovedAmount());
            totalAppropriationAmt = totalAppropriationAmt.add(reAppropriationAmt);
            printed = true;
            isFirst = false;
        }
        if (totalAmt.compareTo(BigDecimal.ZERO) != 0 && !majorcodewise)
            budgetReportList.add(new BudgetReportView(EMPTYSTRING, EMPTYSTRING, EMPTYSTRING, TOTALSTRING, EMPTYSTRING, totalAmt,
                    totalAppropriationAmt, totalAmt.add(totalAppropriationAmt), TOTALROW));
        return budgetReportList;
    }

    public Budget getTopBudget() {
        return topBudget;
    }

    public void setTopBudget(final Budget topBudget) {
        this.topBudget = topBudget;
    }

    private Position getPosition() {
        Position pos;
        try {
            // TODO: Now employee is extending user so passing userid to get assingment -- changes done by Vaibhav
            pos = eisCommonService.getPrimaryAssignmentPositionForEmp(ApplicationThreadLocals.getUserId());
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("Unable to get Position for the user");
        }
        return pos;
    }

    public void loadAmountForMajorcodewise(final CFinancialYear finyear, final Department dept, final CFunction function) {
        String finalStatus = getFinalStatus();
        StringBuilder miscQuery = new StringBuilder("");
        String floatingColumn = "sum(bd.approvedAmount)";
        if (onSaveOrForward) {

            if (LOGGER.isInfoEnabled())
                LOGGER.info("......Can view the approved Amount" + canViewApprovedAmount);
            if (!canViewApprovedAmount) {
                floatingColumn = "sum(bd.originalAmount)";
                finalStatus = "%";
            }
        }
        if (dept != null && dept.getId() != null)
            miscQuery.append(" and bd.executingDepartment.id=:execDeptId");
        if (function != null && function.getId() != null)
            miscQuery.append(" and bd.function.id=:functionId");
        StringBuilder queryString = new StringBuilder("select substr(bd.budgetGroup.minCode.glcode,0,").append(majorCodeLength)
                .append(") ,")
                .append(floatingColumn)
                .append(",bd.executingDepartment.id,bd.function.id,bd.budgetGroup.minCode.type,bd.id from BudgetDetail bd where ")
                .append("bd.budget.financialYear=:finYear and bd.budget.state in (from org.egov.infra.workflow.entity.State where type='Budget' and value=:status) ")
                .append(miscQuery)
                .append(" and bd.budget.isbere=:isBere")
                .append(" group by substr(bd.budgetGroup.minCode.glcode,0,").append(majorCodeLength)
                .append("),bd.executingDepartment.id,")
                .append("bd.function.id,bd.budgetGroup.minCode.type,bd.id")
                .append(" order by substr(bd.budgetGroup.minCode.glcode,0,").append(majorCodeLength)
                .append("),bd.executingDepartment.id,bd.function.id");
        final Query qry = getPersistenceService().getSession().createQuery(queryString.toString());
        if (dept != null && dept.getId() != null)
            qry.setParameter("execDeptId", dept.getId(), LongType.INSTANCE);
        if (function != null && function.getId() != null)
            qry.setParameter("functionId", function.getId(), LongType.INSTANCE);
        qry.setParameter("finYear", finyear)
            .setParameter("status", finalStatus, StringType.INSTANCE)
            .setParameter("isBere", isBERE, StringType.INSTANCE);
        List<Object[]> amountList = qry.list();

        BigDecimal reAppropriationAmt = BigDecimal.ZERO;
        for (final Object[] obj : amountList)
            if (obj[0] != null && obj[1] != null && obj[2] != null && obj[3] != null && obj[4] != null
                    && !(BigDecimal.ZERO.compareTo(BigDecimal.valueOf(Double.valueOf(obj[1].toString()))) == 0)) {
                reAppropriationAmt = reAppropriationMap.get(obj[5]) == null ? BigDecimal.ZERO : reAppropriationMap.get(obj[5]);
                reportStoreList.add(new BudgetReportView(Integer.valueOf(obj[2] + EMPTYSTRING), Long
                        .valueOf(obj[3] + EMPTYSTRING), obj[4] + EMPTYSTRING,
                        obj[0] + EMPTYSTRING, (BigDecimal) obj[1], reAppropriationAmt, ((BigDecimal) obj[1])
                        .add(reAppropriationAmt)));
            }
        StringBuilder query = new StringBuilder("select substr(bd.budgetGroup.majorCode.glcode,0,").append(majorCodeLength)
                .append(") ,")
                .append(floatingColumn)
                .append(",bd.executingDepartment.id,bd.function.id,bd.budgetGroup.majorCode.type,bd.id from BudgetDetail bd where bd.budget.financialYear=:finYear")
                .append(" and bd.budget.state in (from org.egov.infra.workflow.entity.State where type='Budget' and value=:status) and bd.budget.isbere=:isBeRe")
                .append(" group by substr(bd.budgetGroup.majorCode.glcode,0,").append(majorCodeLength)
                .append("),bd.executingDepartment.id,bd.function.id,bd.budgetGroup.majorCode.type,bd.id")
                .append(" order by  substr(bd.budgetGroup.majorCode.glcode,0,").append(majorCodeLength)
                .append(")");
        Query qry1 = getPersistenceService().getSession().createQuery(query.toString());
        qry1.setParameter("finYear", finyear)
            .setParameter("status", finalStatus, StringType.INSTANCE)
            .setParameter("isBeRe", isBERE, StringType.INSTANCE);
        amountList = qry1.list();

        for (final Object[] obj : amountList)
            if (obj[0] != null && obj[1] != null && obj[2] != null && obj[3] != null && obj[4] != null
                    && !(BigDecimal.ZERO.compareTo(BigDecimal.valueOf(Double.valueOf(obj[1].toString()))) == 0)) {
                reAppropriationAmt = reAppropriationMap.get(obj[5]) == null ? BigDecimal.ZERO : reAppropriationMap.get(obj[5]);
                reportStoreList.add(new BudgetReportView(Integer.valueOf(obj[2] + EMPTYSTRING), Long
                        .valueOf(obj[3] + EMPTYSTRING), obj[4] + EMPTYSTRING,
                        obj[0] + EMPTYSTRING, (BigDecimal) obj[1], reAppropriationAmt, ((BigDecimal) obj[1])
                        .add(reAppropriationAmt)));
            }
    }

    public void loadAmountForMajorcodewiseConsolidated(final CFinancialYear finyear, final Department dept,
                                                       final CFunction function) {
        getFinalStatus();
        StringBuilder miscQuery = new StringBuilder("");
        final String floatingColumn = "sum(bd.approvedAmount)";

        if (dept != null && dept.getId() != null)
            miscQuery.append(" and bd.executingDepartment.id=:execDeptId" + dept.getId());
        if (function != null && function.getId() != null)
            miscQuery.append(" and bd.function.id=:functionId" + function.getId());
        StringBuilder queryString = new StringBuilder("select substr(bd.budgetGroup.minCode.glcode,0,").append(majorCodeLength)
                .append(") ,")
                .append(floatingColumn)
                .append(",bd.executingDepartment.id,bd.function.id,bd.budgetGroup.minCode.type,bd.id from BudgetDetail bd where ")
                .append("bd.budget.financialYear=:finYear and( bd.state.value ='END'  or bd.state.owner=:owner) and bd.budget=:budget")
                .append(miscQuery)
                .append(" and bd.budget.isbere=:isBeRe group by substr(bd.budgetGroup.minCode.glcode,0,").append(majorCodeLength)
                .append("),bd.executingDepartment.id,")
                .append("bd.function.id,bd.budgetGroup.minCode.type,bd.id")
                .append(" order by  substr(bd.budgetGroup.minCode.glcode,0,").append(majorCodeLength)
                .append("),bd.executingDepartment.id,bd.function.id");
        Query query = getPersistenceService().getSession().createQuery(queryString.toString());
        if (dept != null && dept.getId() != null)
            query.setParameter("execDeptID", dept.getId(), LongType.INSTANCE);
        if (function != null && function.getId() != null)
            query.setParameter("functionId", function.getId(), LongType.INSTANCE);
        query.setParameter("finYear", finyear)
            .setParameter("owner", pos)
            .setParameter("budget", topBudget)
            .setParameter("isBeRe", isBERE, StringType.INSTANCE);
        final List<Object[]> amountList = query.list();

        BigDecimal reAppropriationAmt = BigDecimal.ZERO;
        for (final Object[] obj : amountList)
            if (obj[0] != null && obj[1] != null && obj[2] != null && obj[3] != null && obj[4] != null
                    && !(BigDecimal.ZERO.compareTo(BigDecimal.valueOf(Double.valueOf(obj[1].toString()))) == 0)) {
                reAppropriationAmt = reAppropriationMap.get(obj[5]) == null ? BigDecimal.ZERO : reAppropriationMap.get(obj[5]);
                reportStoreList.add(new BudgetReportView(Integer.valueOf(obj[2] + EMPTYSTRING), Long
                        .valueOf(obj[3] + EMPTYSTRING), obj[4] + EMPTYSTRING,
                        obj[0] + EMPTYSTRING, (BigDecimal) obj[1], reAppropriationAmt, ((BigDecimal) obj[1])
                        .add(reAppropriationAmt)));
            }
    }

    @SuppressWarnings("unchecked")
    public void loadAmountForMajorcodewiseForWorkingCopy(final CFinancialYear finyear, final Department dept,
                                                         final CFunction function) {
        final String floatingColumn = "sum(bd.originalAmount),sum(bd.approvedAmount)";
        getFinYearForRE();
        Map.Entry<String, Map<String, Object>> queryMapEntry = getSqlForFinYear(financialYearForRE.getId()).entrySet().iterator().next();
        String miscQuery = queryMapEntry.getKey();
        Map<String, Object> queryParams = queryMapEntry.getValue();
        // find sum for RE
        StringBuilder queryString = new StringBuilder("select substr(bd.budgetGroup.minCode.glcode,0,").append(majorCodeLength)
                .append("),")
                .append(floatingColumn)
                .append(",bd.executingDepartment.id,bd.function.id,bd.budgetGroup.minCode.type,bd.budgetGroup.minCode.glcode,bd.id from BudgetDetail bd where ")
                .append(" ")
                .append(miscQuery)
                .append(workFlowstateCondn)
                .append(" and bd.budget.isbere='RE' group by substr(bd.budgetGroup.minCode.glcode,0,").append(majorCodeLength)
                .append("),bd.executingDepartment.id,")
                .append("bd.function.id,bd.budgetGroup.minCode.type,bd.budgetGroup.minCode.glcode,bd.id order by  substr(bd.budgetGroup.minCode.glcode,0,")
                .append(majorCodeLength).append("),")
                .append("bd.executingDepartment.id,bd.function.id");
        final Query qryStr = getPersistenceService().getSession().createQuery(queryString.toString());
        queryParams.entrySet().forEach(entry -> qryStr.setParameter(entry.getKey(), entry.getValue()));
        final List<Object[]> amountListForRE = qryStr.list();

        StringBuilder query = new StringBuilder("select substr(bd.budgetGroup.majorCode.glcode,0,")
                .append(majorCodeLength).append(") ,")
                .append(floatingColumn)
                .append(",bd.executingDepartment.id,bd.function.id,bd.budgetGroup.majorCode.type,bd.id from BudgetDetail bd where ")
                .append(miscQuery)
                .append(workFlowstateCondn)
                .append(" and bd.budget.isbere='RE'  group by substr(bd.budgetGroup.majorCode.glcode,0,").append(majorCodeLength)
                .append("),bd.executingDepartment.id,bd.function.id,")
                .append("bd.budgetGroup.majorCode.type,bd.budgetGroup.majorCode.glcode,bd.budgetGroup.minCode.glcode,bd.id")
                .append(" order by  substr(bd.budgetGroup.majorCode.glcode,0,").append(majorCodeLength)
                .append(")");
        final Query qry = getPersistenceService().getSession().createQuery(query.toString());
        queryParams.entrySet().forEach(entry -> qry.setParameter(entry.getKey(), entry.getValue()));
        amountListForRE.addAll(qry.list());

        queryMapEntry = getSqlForFinYearBE(financialYearForBE.getId()).entrySet().iterator().next();
        miscQuery = queryMapEntry.getKey();
        queryParams = queryMapEntry.getValue();

        /**
         * order of retrieval 0-majorcode 1-sum(originalamount) 2-sum(approvedamount) 3-department 4-function 5-type 6-glcode/id
         * 7-id
         */
        StringBuilder query1 = new StringBuilder("select substr(bd.budgetGroup.minCode.glcode,0,").append(majorCodeLength)
                .append(") ,")
                .append(floatingColumn)
                .append(",bd.executingDepartment.id,bd.function.id,bd.budgetGroup.minCode.type,bd.budgetGroup.minCode.glcode,bd.id from BudgetDetail bd where ")
                .append(" ")
                .append(miscQuery)
                .append(workFlowstateCondn)
                .append(" and bd.budget.isbere='BE'  group by substr(bd.budgetGroup.minCode.glcode,0,")
                .append(majorCodeLength).append("),bd.executingDepartment.id,")
                .append("bd.function.id,bd.budgetGroup.minCode.type,bd.budgetGroup.minCode.glcode,bd.id")
                .append(" order by  substr(bd.budgetGroup.minCode.glcode,0,")
                .append(majorCodeLength).append("),bd.executingDepartment.id,bd.function.id");
        final Query qry1 = getPersistenceService().getSession().createQuery(query1.toString());
        queryParams.entrySet().forEach(entry -> qry1.setParameter(entry.getKey(), entry.getValue()));
        final List<Object[]> amountListForBE = qry1.list();

        StringBuilder query2 = new StringBuilder("select substr(bd.budgetGroup.majorCode.glcode,0,")
                .append(majorCodeLength).append(") ,")
                .append(floatingColumn)
                .append(",bd.executingDepartment.id,bd.function.id,bd.budgetGroup.majorCode.type,bd.budgetGroup.majorCode.glcode,bd.id from BudgetDetail bd where ")
                .append(miscQuery)
                .append(workFlowstateCondn)
                .append(" and bd.budget.isbere='BE'  group by substr(bd.budgetGroup.majorCode.glcode,0,")
                .append(majorCodeLength).append("),bd.executingDepartment.id,bd.function.id,")
                .append("bd.budgetGroup.majorCode.type,bd.budgetGroup.majorCode.glcode,bd.id order by  substr(bd.budgetGroup.majorCode.glcode,0,")
                .append(majorCodeLength).append(")");

        final Query qry2 = getPersistenceService().getSession().createQuery(query2.toString());
        queryParams.entrySet().forEach(entry -> qry2.setParameter(entry.getKey(), entry.getValue()));
        amountListForBE.addAll(qry2.list());

        // Merge both and set to budget Report
        // u may require null check here
        // handle un equal be and re major code totals

        outer:
        for (final Object[] re : amountListForRE)
            inner:for (final Object[] be : amountListForBE)
                if (re[0].toString().equalsIgnoreCase(be[0].toString()) && re[3].toString().equalsIgnoreCase(be[3].toString())
                        && re[4].toString().equalsIgnoreCase(be[4].toString())
                        && re[6].toString().equalsIgnoreCase(be[6].toString())) {
                    final BudgetReportView v1 = new BudgetReportView();
                    v1.setDeptId(Integer.valueOf(re[3].toString()));
                    v1.setFunctionId((Long) re[4]);
                    v1.setType(re[5].toString());
                    v1.setMajorCode(re[0].toString());
                    v1.setReProposalAmount((BigDecimal) re[1]);
                    v1.setReRecomAmount((BigDecimal) re[2]);
                    v1.setBeProposalAmount((BigDecimal) be[1]);
                    v1.setBeRecomAmount((BigDecimal) be[2]);
                    reportStoreList.add(v1);
                    break inner;
                }

    }

    public List<Object> getAmountForMajorcodewise(final Integer deptId, final Long functionId, final String type) {
        BigDecimal grandAmt = BigDecimal.ZERO;
        BigDecimal totalAmt = BigDecimal.ZERO;
        BigDecimal appropriationGrandAmt = BigDecimal.ZERO;
        BigDecimal appropriationTotalAmt = BigDecimal.ZERO;
        final List<Object> majorCodeList = new ArrayList<Object>();
        final Map<String, BudgetReportView> entries = new TreeMap<String, BudgetReportView>();
        for (final BudgetReportView reportStore : reportStoreList)
            if (deptId.equals(reportStore.getDeptId()) && functionId.equals(reportStore.getFunctionId())
                    && type.equals(reportStore.getType())) {
                if (entries.get(reportStore.getMajorCode()) == null) {
                    totalAmt = BigDecimal.ZERO;
                    appropriationTotalAmt = BigDecimal.ZERO;
                    entries.put(
                            reportStore.getMajorCode(),
                            new BudgetReportView(EMPTYSTRING, EMPTYSTRING, EMPTYSTRING, reportStore.getMajorCode() + "-"
                                    + coaMap.get(reportStore.getMajorCode()),
                                    refNoMap.get(reportStore.getMajorCode()) == null ? EMPTYSTRING : refNoMap.get(
                                            reportStore.getMajorCode()).toString(), reportStore.getTempamount(), reportStore
                                    .getAppropriationAmount(), reportStore
                                    .getTempamount().add(reportStore.getAppropriationAmount()), "majorcoderow"));
                    totalAmt = totalAmt.add(reportStore.getTempamount());
                    appropriationTotalAmt = appropriationTotalAmt.add(reportStore.getAppropriationAmount());
                } else {
                    totalAmt = totalAmt.add(reportStore.getTempamount());
                    appropriationTotalAmt = appropriationTotalAmt.add(reportStore.getAppropriationAmount());
                    entries.get(reportStore.getMajorCode()).setAmount(totalAmt);
                    entries.get(reportStore.getMajorCode()).setAppropriationAmount(appropriationTotalAmt);
                    entries.get(reportStore.getMajorCode()).setTotalAmount(totalAmt.add(appropriationTotalAmt));
                }
                grandAmt = grandAmt.add(reportStore.getTempamount());
                appropriationGrandAmt = appropriationGrandAmt.add(reportStore.getAppropriationAmount());
            }
        for (final Entry<String, BudgetReportView> row : entries.entrySet())
            majorCodeList.add(row.getValue());
        if (totalAmt.compareTo(BigDecimal.ZERO) != 0)
            majorCodeList.add(new BudgetReportView(EMPTYSTRING, EMPTYSTRING, EMPTYSTRING, TOTALSTRING, EMPTYSTRING, grandAmt,
                    appropriationGrandAmt, grandAmt
                    .add(appropriationGrandAmt), TOTALROW));
        return majorCodeList;
    }

    public List<Object> getAmountForMajorcodewiseForWorkingCopy(final Integer deptId, final Long functionId, final String type) {
        BigDecimal reProposalTotalAmountLocal = BigDecimal.ZERO;
        BigDecimal beProposalTotalAmountLocal = BigDecimal.ZERO;
        BigDecimal reRecomTotalAmountLocal = BigDecimal.ZERO;
        BigDecimal beRecomTotalAmountLocal = BigDecimal.ZERO;
        BigDecimal reProposalTotalAmountLocalGrand = BigDecimal.ZERO;
        BigDecimal beProposalTotalAmountLocalGrand = BigDecimal.ZERO;
        BigDecimal reRecomTotalAmountLocalGrand = BigDecimal.ZERO;
        BigDecimal beRecomTotalAmountLocalGrand = BigDecimal.ZERO;

        final List<Object> majorCodeList = new ArrayList<Object>();
        final Map<String, BudgetReportView> entries = new TreeMap<String, BudgetReportView>();
        for (final BudgetReportView reportStore : reportStoreList)
            if (deptId.equals(reportStore.getDeptId()) && functionId.equals(reportStore.getFunctionId())
                    && type.equals(reportStore.getType())) {
                if (entries.get(reportStore.getMajorCode()) == null) {
                    reProposalTotalAmountLocal = BigDecimal.ZERO;
                    beProposalTotalAmountLocal = BigDecimal.ZERO;
                    reRecomTotalAmountLocal = BigDecimal.ZERO;
                    beRecomTotalAmountLocal = BigDecimal.ZERO;
                    final BudgetReportView v2 = new BudgetReportView();
                    v2.setNarration("1414");
                    v2.setNarration(reportStore.getMajorCode() + "-" + coaMap.get(reportStore.getMajorCode()));
                    v2.setReference(refNoMap.get(reportStore.getMajorCode()) == null ? EMPTYSTRING : refNoMap.get(
                            reportStore.getMajorCode()).toString());
                    v2.setBeProposalAmount(reportStore.getBeProposalAmount());
                    v2.setReProposalAmount(reportStore.getReProposalAmount());
                    v2.setBeRecomAmount(reportStore.getBeRecomAmount());
                    v2.setReRecomAmount(reportStore.getReRecomAmount());
                    entries.put(reportStore.getMajorCode(), v2);
                    reProposalTotalAmountLocal = reProposalTotalAmountLocal.add(reportStore.getReProposalAmount());
                    beProposalTotalAmountLocal = beProposalTotalAmountLocal.add(reportStore.getBeProposalAmount());
                    beRecomTotalAmountLocal = beRecomTotalAmountLocal.add(reportStore.getBeRecomAmount());
                    reRecomTotalAmountLocal = reRecomTotalAmountLocal.add(reportStore.getReRecomAmount());

                } else {
                    reProposalTotalAmountLocal = reProposalTotalAmountLocal.add(reportStore.getReProposalAmount());
                    beProposalTotalAmountLocal = beProposalTotalAmountLocal.add(reportStore.getBeProposalAmount());
                    beRecomTotalAmountLocal = beRecomTotalAmountLocal.add(reportStore.getBeRecomAmount());
                    reRecomTotalAmountLocal = reRecomTotalAmountLocal.add(reportStore.getReRecomAmount());
                    entries.get(reportStore.getMajorCode()).setReProposalAmount(reProposalTotalAmountLocal);
                    entries.get(reportStore.getMajorCode()).setBeProposalAmount(beProposalTotalAmountLocal);
                    entries.get(reportStore.getMajorCode()).setReRecomAmount(reRecomTotalAmountLocal);
                    entries.get(reportStore.getMajorCode()).setBeRecomAmount(beRecomTotalAmountLocal);

                }
                reProposalTotalAmountLocalGrand = reProposalTotalAmountLocalGrand.add(reportStore.getReProposalAmount());
                beProposalTotalAmountLocalGrand = beProposalTotalAmountLocalGrand.add(reportStore.getBeProposalAmount());
                beRecomTotalAmountLocalGrand = beRecomTotalAmountLocalGrand.add(reportStore.getBeRecomAmount());
                reRecomTotalAmountLocalGrand = reRecomTotalAmountLocalGrand.add(reportStore.getReRecomAmount());
                // grandAmt = grandAmt.add(reportStore.getTempamount());

            }
        for (final Entry<String, BudgetReportView> row : entries.entrySet())
            majorCodeList.add(row.getValue());
        final BudgetReportView v3 = new BudgetReportView();
        v3.setBeProposalAmount(beProposalTotalAmountLocalGrand);
        v3.setReProposalAmount(reProposalTotalAmountLocalGrand);
        v3.setBeRecomAmount(beRecomTotalAmountLocalGrand);
        v3.setReRecomAmount(reRecomTotalAmountLocalGrand);
        v3.setRowStyle(TOTALROW);
        v3.setNarration(TOTALSTRING);
        majorCodeList.add(v3);

        return majorCodeList;
    }

    public void getCOA() {
        final List<CChartOfAccounts> coaList = getPersistenceService().findAllBy(
                "from CChartOfAccounts where length(glcode)=?1", majorCodeLength);
        for (final CChartOfAccounts coa : coaList)
            coaMap.put(coa.getGlcode(), coa.getName());
    }

    @Action(value = "/budget/budgetReport-departmentWiseReport")
    public String departmentWiseReport() {
        return "dept";
    }

    @ValidationErrorPage(value = "dept")
    @Action(value = "/budget/budgetReport-printDepartmentWiseReport")
    public String printDepartmentWiseReport() {
        try {
            validateFinancialYear();
        } catch (final ValidationException e) {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getErrors().get(0).getMessage(),
                    e.getErrors().get(0).getMessage())));
        } catch (final Exception e) {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(),
                    e.getMessage())));
        }
        return "print";
    }

    @Action(value = "/budget/budgetReport-generateDepartmentWiseXls")
    public String generateDepartmentWiseXls() throws JRException, IOException {
        validateFinancialYear();
        populateData();
        inputStream = reportHelper.exportXls(inputStream, DEPTWISEPATH, getParamMap(), budgetReportList);
        return "department-XLS";
    }

    @Action(value = "/budget/budgetReport-generateDepartmentWisePdf")
    public String generateDepartmentWisePdf() throws Exception {
        validateFinancialYear();
        populateData();
        inputStream = reportHelper.exportPdf(inputStream, DEPTWISEPATH, getParamMap(), budgetReportList);
        return "department-PDF";
    }

    @Action(value = "/budget/budgetReport-ajaxGenerateDepartmentWiseHtml")
    public String ajaxGenerateDepartmentWiseHtml() throws Exception {
        populateData();
        inputStream = reportHelper.exportHtml(inputStream, DEPTWISEPATH, getParamMap(), budgetReportList, "px");
        return "department-HTML";
    }

    public BudgetReport getBudgetReport() {
        return budgetReport;
    }

    public void setBudgetReport(final BudgetReport budgetReport) {
        this.budgetReport = budgetReport;
    }

    protected void setRelatedEntitesOn() {
        if (budgetReport.getDepartment() == null || budgetReport.getDepartment().getId() == null)
            budgetReport.setDepartment(null);
        else
            budgetReport.setDepartment((Department) getPersistenceService().find("from Department where id=?1",
                    budgetReport.getDepartment().getId()));

        if (budgetReport.getFinancialYear() != null)
            budgetReport.setFinancialYear((CFinancialYear) getPersistenceService().find("from CFinancialYear where id=?1",
                    budgetReport.getFinancialYear().getId()));
    }

    protected void validateFinancialYear() {
        if (budgetReport.getFinancialYear() == null || budgetReport.getFinancialYear().getId() == null)
            throw new ValidationException(Arrays.asList(new ValidationError("report.financialyear.not.selected",
                    "report.financialyear.not.selected")));
    }

    protected String getBudgetType(final String finalStatus) {
        String isBeRe = "BE";
        final Budget budget = (Budget) persistenceService
                .find(new StringBuilder("from Budget where financialYear.id=?1 and parent is null and isPrimaryBudget=true and")
                                .append(" isActiveBudget=true and isBeRe='RE' and status.code=?2").toString(),
                        budgetReport.getFinancialYear().getId(), finalStatus);
        if (budget != null)
            isBeRe = "RE";
        return isBeRe;
    }

    protected void addEmptyRow() {
        budgetReportList.add(new BudgetReportView("", "", "", null, null, null));
    }

    protected String getQueryForSelectedType(final String code) {
        if (budgetReport.getType() == null)
            return "";
        if (!"ALL".equalsIgnoreCase(budgetReport.getType()))
            if ("IE".equalsIgnoreCase(budgetReport.getType()))
                return String.format("and (bd.budgetGroup.%s.type='I' or bd.budgetGroup.%s.type='E') ", code, code);
            else
                return String.format("and bd.budgetGroup.%s.type=", code).concat(budgetReport.getType());
        return "";
    }

    @ReadOnly
    protected Map<String, Object> getParamMap() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("finYear", budgetReport.getFinancialYear().getFinYearRange());
        if (onSaveOrForward)
            paramMap = getReportConfigs(paramMap);
        if (budgetReport.getType() != null)
            paramMap.put("type", BudgetReport.getValueFor(budgetReport.getType()));
        return paramMap;

    }

    /**
     *
     */
    private Map<String, Object> getReportConfigs(final Map<String, Object> paramMap) {
        if (financialYearForRE == null) {
            final Long finYearForRE = getFinYearForRE();
            if (finYearForRE == budgetReport.getFinancialYear().getId()) {
                final CFinancialYear finYear = getFinYear("next");
                financialYearForBE = finYear;
            } else
                financialYearForBE = budgetReport.getFinancialYear();
        }
        final List<AppConfigValues> list = appConfigValuesService.getConfigValuesByModuleAndKey(Constants.EGF,
                "budget_toplevel_approver_designation");
        final String value = list.get(0).getValue();
        // TODO: Now employee is extending user so passing userid to get assingment -- changes done by Vaibhav
        final Assignment empAssignment = eisCommonService.getLatestAssignmentForEmployeeByToDate(ApplicationThreadLocals.getUserId(),
                new Date());
        final Designation designation = empAssignment.getDesignation();
        if (designation.getName().equalsIgnoreCase(value))
            finalApprover = true;
        canViewApprovedAmount = budgetDetailService.canViewApprovedAmount(persistenceService, null);
        if (!canViewApprovedAmount && !finalApprover)
            path = WORKINGCOPYWITHONLYPROPOSALS;
        else if (finalApprover)
            path = WORKINGCOPYFORFINALAPPROVER;
        else
            path = WORKINGCOPYWITHALLMOUNTS;
        if (departmentBudget)
            path = WORKINGCOPYFORFINALAPPROVER;
        paramMap.put("financialYearForRE", financialYearForRE.getFinYearRange());
        paramMap.put("financialYearForBE", financialYearForBE.getFinYearRange());
        paramMap.put("heading", " FUNCTIONWISE  BUDGET SUMMARY ");
        return paramMap;

    }

    protected Map<String, String> getReferenceNumber(final String appConfigKey) {
        final Map<String, String> referenceNo = new HashMap<String, String>();
        //TODO THIS HAS TO BE CHANGED, THIS WILL RETURN UNDESIRED RESULT
        final List<AppConfigValues> appConfigValues = appConfigValuesService.getConfigValuesByModuleAndKeyLike(FinancialConstants.MODULE_NAME_APPCONFIG, appConfigKey + "-%");
        for (final AppConfigValues appConfigVal : appConfigValues)
            referenceNo.put(appConfigVal.getConfig().getKeyName().split("-")[1], appConfigVal.getValue());
        return referenceNo;
    }

    protected String getFinalStatus() {
        return getAppConfigValueFor(Constants.EGF, "budget_final_approval_status");
    }

    protected String getAppConfigValueFor(final String module, final String key) {
        return appConfigValuesService.getConfigValuesByModuleAndKey(module, key).get(0).getValue();
    }

    @ReadOnly
    protected void populateData() {
        final String finalStatus = getFinalStatus();
        final String isBeRe = getBudgetType(finalStatus);
        String deptQuery = "";
        if (budgetReport.getDepartment() != null && budgetReport.getDepartment().getId() != null)
            deptQuery = " and bd.executingDepartment.id=" + budgetReport.getDepartment().getId().toString();
        getBudgetReappropriationAmt();
        final String budgetType = BudgetReport.getValueFor(budgetReport.getType());
        if (budgetType != null && !"ALL".equals(budgetReport.getType()))
            budgetReportList.add(new BudgetReportView("", budgetType.toUpperCase() + " BUDGET SUMMARY", "", null, null, null));
        // budgetdetails for all mincode
        final LinkedList<BudgetDetail> budgetDetails = new LinkedList<BudgetDetail>();
        fetchBudgetDetails(budgetDetails, deptQuery, finalStatus, isBeRe, "minCode");
        // budgetdetails for all majorcode
        fetchBudgetDetails(budgetDetails, deptQuery, finalStatus, isBeRe, "majorCode");
        populateSummarySection(budgetDetails, isBeRe);
        addRowsToReport(budgetDetails, isBeRe);
    }

    public Map<String, BigDecimal> getMajorCodeToAmountMap(final List<BudgetDetail> budgetDetails) {
        final Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
        for (final BudgetDetail entry : budgetDetails) {
            String glCode = "";
            if (entry.getBudgetGroup().getMajorCode() == null)
                glCode = entry.getBudgetGroup().getMinCode().getMajorCode();
            else
                glCode = entry.getBudgetGroup().getMajorCode().getMajorCode();
            final BigDecimal approvedAmount = entry.getApprovedAmount() == null ? BigDecimal.ZERO : entry.getApprovedAmount();
            final BigDecimal totalAmount = approvedAmount;
            if (map.get(glCode) != null)
                map.put(glCode, map.get(glCode).add(totalAmount));
            else
                map.put(glCode, totalAmount);
        }
        return map;
    }

    public String getUniqueMajorCodesAsString(final Map<String, BigDecimal> majorCodeToAmountMap) {
        String result = "";
        final Set<String> uniqueMajorCodes = majorCodeToAmountMap.keySet();
        for (final String row : uniqueMajorCodes)
            if (row != null)
                result = result.concat("'").concat(row).concat("',");
        if (result.length() > 0)
            result = result.substring(0, result.length() - 1);
        return result;
    }

    public BigDecimal getMajorCodeTotals(final Map<String, BigDecimal> majorCodeToAmountMap) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final Entry<String, BigDecimal> entry : majorCodeToAmountMap.entrySet())
            if (entry.getValue() != null)
                sum = sum.add(entry.getValue());
        return sum;
    }

    public BigDecimal getMajorCodeApproriationTotals(final Map<String, BigDecimal> majorCodeToApproriationAmountMap) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final Entry<String, BigDecimal> entry : majorCodeToApproriationAmountMap.entrySet())
            if (entry.getValue() != null)
                sum = sum.add(entry.getValue());
        return sum;
    }

    void populateSummarySection(final List<BudgetDetail> budgetDetails, final String isBeRe) {
        final Map<String, BigDecimal> majorCodeToAmountMap = getMajorCodeToAmountMap(budgetDetails);
        final Map<String, BigDecimal> majorCodeToAppropriationAmountMap = getMajorCodeToAppropriationAmountMap(budgetDetails);
        final Map<String, String> referenceNo = getReferenceNumber("departmentWiseBudgetReport");
        final String uniqueMajorCodesAsString = getUniqueMajorCodesAsString(majorCodeToAmountMap);
        if ("".equals(uniqueMajorCodesAsString)) {
            budgetReportList.add(new BudgetReportView("", "No records found", "", null, null, null));
            return;
        }
        final List<CChartOfAccounts> chartOfAccounts = getPersistenceService().findAllBy(
                "from CChartOfAccounts where glCode in (?1)", uniqueMajorCodesAsString);
        for (final CChartOfAccounts account : chartOfAccounts) {
            final BigDecimal approved = majorCodeToAmountMap.get(account.getMajorCode());
            final BigDecimal reApp = majorCodeToAppropriationAmountMap.get(account.getMajorCode());
            if ("RE".equalsIgnoreCase(isBeRe) && !getConsiderReAppropriationAsSeperate())
                budgetReportList.add(new BudgetReportView("", account.getMajorCode() + "-" + account.getName(), referenceNo
                        .get(account.getMajorCode()), approved.add(reApp),
                        BigDecimal.ZERO, approved.add(reApp)));
            else
                budgetReportList.add(new BudgetReportView("", account.getMajorCode() + "-" + account.getName(), referenceNo
                        .get(account.getMajorCode()), approved,
                        reApp, approved.add(reApp)));
        }
        if (!chartOfAccounts.isEmpty()) {
            final BigDecimal majorCodeApprovedTotals = getMajorCodeTotals(majorCodeToAmountMap);
            final BigDecimal majorCodeApproriationTotals = getMajorCodeApproriationTotals(majorCodeToAppropriationAmountMap);
            if ("RE".equalsIgnoreCase(isBeRe) && !getConsiderReAppropriationAsSeperate())
                budgetReportList.add(new BudgetReportView("", "Total", "", majorCodeApprovedTotals
                        .add(majorCodeApproriationTotals), BigDecimal.ZERO,
                        majorCodeApprovedTotals.add(majorCodeApproriationTotals)));
            else
                budgetReportList.add(new BudgetReportView("", "Total", "", majorCodeApprovedTotals, majorCodeApproriationTotals,
                        majorCodeApprovedTotals
                                .add(majorCodeApproriationTotals)));

        }
        addEmptyRow();
    }

    private Map<String, BigDecimal> getMajorCodeToAppropriationAmountMap(final List<BudgetDetail> budgetDetails) {
        final Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
        for (final BudgetDetail entry : budgetDetails) {
            String glCode = "";
            if (entry.getBudgetGroup().getMajorCode() == null)
                glCode = entry.getBudgetGroup().getMinCode().getMajorCode();
            else
                glCode = entry.getBudgetGroup().getMajorCode().getMajorCode();
            final BigDecimal reAppAmount = reAppropriationMap.get(entry.getId());
            final BigDecimal totalAmount = reAppAmount == null ? BigDecimal.ZERO : reAppAmount;
            if (map.get(glCode) != null)
                map.put(glCode, map.get(glCode).add(totalAmount));
            else
                map.put(glCode, totalAmount);
        }
        return map;
    }

    /*
     * Assumes budgetDetails are sorted by deptId,glCode
     */
    void addRowsToReport(List<BudgetDetail> budgetDetails, final String isBeRe) {
        Integer deptId = 0;
        BudgetReportView row = new BudgetReportView(null, null, null, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        String glcode = null;
        String glName;
        BigDecimal sum = BigDecimal.ZERO;
        final BigDecimal appropriationSum = BigDecimal.ZERO;
        budgetDetails = budgetDetailService.sortByDepartmentName(budgetDetails);
        // not interested in major code details
        for (final BudgetDetail budgetDetail : budgetDetails) {
            // if(budgetDetail.getBudgetGroup().getMajorCode()!=null){
            // continue;
            // }
            // details for next department have started
            if (budgetDetail.getExecutingDepartment() != null && budgetDetail.getExecutingDepartment().getId().compareTo(deptId.longValue()) != 0) {
                if (!deptId.equals(0))
                    if ("RE".equalsIgnoreCase(isBeRe) && !getConsiderReAppropriationAsSeperate())
                        budgetReportList.add(new BudgetReportView("", "Total", "", sum.add(appropriationSum), BigDecimal.ZERO,
                                sum.add(appropriationSum)));
                    else
                        budgetReportList.add(new BudgetReportView("", "Total", "", sum, appropriationSum, sum
                                .add(appropriationSum)));
                sum = BigDecimal.ZERO;
                addEmptyRow();
                budgetReportList.add(new BudgetReportView("", budgetDetail.getExecutingDepartment().getName().toUpperCase(), "",
                        null, null, null));
                deptId = budgetDetail.getExecutingDepartment().getId().intValue();
                glcode = null;
            }
            // next glcode within same department
            if (!getGlCode(budgetDetail).equals(glcode)) {
                glcode = getGlCode(budgetDetail);
                glName = getGlName(budgetDetail);
                row = new BudgetReportView(glcode, glName, "", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
                budgetReportList.add(row);
            }
            final BigDecimal approvedAmount = budgetDetail.getApprovedAmount() == null ? BigDecimal.ZERO : budgetDetail
                    .getApprovedAmount();
            final BigDecimal reAppAmount = reAppropriationMap.get(budgetDetail.getId());
            if ("RE".equalsIgnoreCase(isBeRe) && !getConsiderReAppropriationAsSeperate()) {
                row.setAmount(approvedAmount.add(reAppAmount == null ? BigDecimal.ZERO : reAppAmount));
                row.setAppropriationAmount(BigDecimal.ZERO);
                row.setTotalAmount(approvedAmount.add(reAppAmount == null ? BigDecimal.ZERO : reAppAmount));
            } else {
                row.setAmount(approvedAmount);
                row.setAppropriationAmount(reAppAmount == null ? BigDecimal.ZERO : reAppAmount);
                row.setTotalAmount(approvedAmount.add(reAppAmount == null ? BigDecimal.ZERO : reAppAmount));
            }

            sum = sum.add(approvedAmount);
        }
        if (!budgetDetails.isEmpty())
            if ("RE".equalsIgnoreCase(isBeRe) && !getConsiderReAppropriationAsSeperate())
                budgetReportList.add(new BudgetReportView("", "Total", "", sum.add(appropriationSum), BigDecimal.ZERO, sum
                        .add(appropriationSum)));
            else
                budgetReportList.add(new BudgetReportView("", "Total", "", sum, appropriationSum, sum.add(appropriationSum)));

    }

    private String getGlName(final BudgetDetail budgetDetail) {
        final BudgetGroup budgetGroup = budgetDetail.getBudgetGroup();
        return budgetGroup.getMinCode() == null ? budgetGroup.getMajorCode().getName() : budgetGroup.getMinCode().getName();
    }

    private String getGlCode(final BudgetDetail budgetDetail) {
        final BudgetGroup budgetGroup = budgetDetail.getBudgetGroup();
        return budgetGroup.getMinCode() == null ? budgetGroup.getMajorCode().getGlcode() : budgetGroup.getMinCode().getGlcode();
    }

    void fetchBudgetDetails(final List<BudgetDetail> budgetDetails, final String deptQuery, final String finalStatus,
                            final String budgetType, final String code) {
        final List<BudgetDetail> results = persistenceService.getSession()
                .createQuery(new StringBuilder(" from BudgetDetail bd where bd.budget.financialYear.id=:finYearId")
                        .append(deptQuery).append(" and bd.budget.isbere=:isBeRe and bd.budget.status.code =:status ")
                        .append(getQueryForSelectedType(code))
                        .append(String.format("  order by bd.executingDepartment.name,bd.budgetGroup.%s.glcode", code))
                        .toString())
                .setParameter("finYearId", budgetReport.getFinancialYear().getId(), LongType.INSTANCE)
                .setParameter("isBeRe", budgetType, StringType.INSTANCE)
                .setParameter("status", finalStatus, StringType.INSTANCE)
                .list();
        budgetDetails.addAll(results);
    }

    private void getBudgetReappropriationAmt() {
        final String status = getFinalStatus();
        StringBuilder queryString = new StringBuilder("select sum(br.additionAmount)-sum(br.deductionAmount),br.budgetDetail.id from BudgetReAppropriation br ")
                .append("where br.status = (select id from EgwStatus where moduletype='BudgetReAppropriation' ")
                .append("and description='Approved') group by br.budgetDetail.id");
        final List<Object[]> list = getPersistenceService().findAllBy(queryString.toString());
        if (!list.isEmpty() && list.size() != 0)
            for (final Object[] obj : list)
                reAppropriationMap.put(obj[1], (BigDecimal) obj[0]);
    }

    public boolean isOnSaveOrForward() {
        return onSaveOrForward;
    }

    public void setOnSaveOrForward(final boolean onSaveOrForward) {
        this.onSaveOrForward = onSaveOrForward;
    }

    public BudgetDetailService getBudgetDetailService() {
        return budgetDetailService;
    }

    public void setBudgetDetailService(final BudgetDetailService budgetDetailService) {
        this.budgetDetailService = budgetDetailService;
    }

    // ----------------------------------------------working copy starts here
    private List<Object> getDataFunctionWiseForWorkingCopy() {

        getMincodeDataForWorkingCopy();
        getMajorcodeDataForWorkingCopy();
        if (budgetDetailListForRE.isEmpty())
            return budgetReportList;

        Integer deptId = 0;
        Long functionId = 0L;
        String type = "", majorCode = "", glcode = "", glType = "", glName = "", tempMajorCode = "";
        BigDecimal reProposalTotalLocal = BigDecimal.ZERO;
        BigDecimal beProposalTotalLocal = BigDecimal.ZERO;
        BigDecimal reRecomTotalLocal = BigDecimal.ZERO;
        BigDecimal beRecomTotalLocal = BigDecimal.ZERO;
        boolean printed = true;
        boolean isFirst = true;
        boolean majorcodewise = false;
        refNoMap = getReferenceNumber("functionWiseBudgetReport");
        getCOA();
        getBudgetReappropriationAmt();
        loadAmountForMajorcodewiseForWorkingCopy(budgetReport.getFinancialYear(), budgetReport.getDepartment(),
                budgetReport.getFunction());
        for (final BudgetDetail detail : budgetDetailListForRE) {
            BudgetDetail beDetail = new BudgetDetail();

            for (final BudgetDetail detail1 : budgetDetailListForBE)
                if (detail.getExecutingDepartment().getId() == detail1.getExecutingDepartment().getId()
                        && detail.getFunction().getId() == detail1.getFunction().getId()
                        && detail.getBudgetGroup().getId() == detail1.getBudgetGroup().getId()) {
                    beDetail = detail1;
                    break;
                }
            if (detail.getExecutingDepartment() == null || detail.getFunction() == null)
                continue;
            // reAppropriationAmt =
            // reAppropriationMap.get(detail.getId())==null?BigDecimal.ZERO:reAppropriationMap.get(detail.getId());
            if (detail.getBudgetGroup().getMajorCode() == null) {
                glcode = getGlCode(detail);
                glType = detail.getBudgetGroup().getMinCode().getType().toString();
                glName = getGlName(detail);
            } else {
                glcode = detail.getBudgetGroup().getMajorCode().getGlcode();
                glType = detail.getBudgetGroup().getMajorCode().getType().toString();
                glName = detail.getBudgetGroup().getMajorCode().getName();
                majorcodewise = true;
            }
            tempMajorCode = glcode.substring(0, majorCodeLength);

            if (!detail.getExecutingDepartment().getId().equals(deptId)) // for
            // dept
            // heading
            {
                if (reProposalTotalLocal.compareTo(BigDecimal.ZERO) != 0 && !isFirst) {
                    budgetReportList.add(new BudgetReportView(EMPTYSTRING, EMPTYSTRING, EMPTYSTRING, TOTALSTRING, EMPTYSTRING,
                            reProposalTotalLocal,
                            beProposalTotalLocal, reRecomTotalLocal, beRecomTotalLocal, TOTALROW));
                    reProposalTotalLocal = BigDecimal.ZERO;
                    beProposalTotalLocal = BigDecimal.ZERO;
                    reRecomTotalLocal = BigDecimal.ZERO;
                    beRecomTotalLocal = BigDecimal.ZERO;
                }
                budgetReportList.add(new BudgetReportView(EMPTYSTRING, EMPTYSTRING, EMPTYSTRING, detail.getExecutingDepartment()
                        .getName(), EMPTYSTRING, null,
                        null, null, "deptrow"));
                type = "";
                functionId = null;
                majorCode = "";
            }
            if (!glType.equals(type))// for type heading
            {
                if (reProposalTotalLocal.compareTo(BigDecimal.ZERO) != 0 && !isFirst) {
                    budgetReportList.add(new BudgetReportView(EMPTYSTRING, EMPTYSTRING, EMPTYSTRING, TOTALSTRING, EMPTYSTRING,
                            reProposalTotalLocal,
                            reRecomTotalLocal, beProposalTotalLocal, beRecomTotalLocal, TOTALROW));
                    reProposalTotalLocal = BigDecimal.ZERO;
                    beProposalTotalLocal = BigDecimal.ZERO;
                    reRecomTotalLocal = BigDecimal.ZERO;
                    beRecomTotalLocal = BigDecimal.ZERO;
                }
                budgetReportList.add(new BudgetReportView(EMPTYSTRING, EMPTYSTRING, EMPTYSTRING, "FUNCTIONWISE "
                        + BudgetReport.getValueFor(glType).toUpperCase()
                        + " BUDGET SUMMARY", refNoMap.get(BudgetReport.getValueFor(glType)), null, null, null, "typerow"));

                functionId = null;
                majorCode = "";
            }
            if (!detail.getFunction().getId().equals(functionId)) // for
            // function
            // heading
            {
                if (reProposalTotalLocal.compareTo(BigDecimal.ZERO) != 0 && !isFirst) {
                    budgetReportList.add(new BudgetReportView(EMPTYSTRING, EMPTYSTRING, EMPTYSTRING, TOTALSTRING, EMPTYSTRING,
                            reProposalTotalLocal,
                            reRecomTotalLocal, beProposalTotalLocal, beRecomTotalLocal, TOTALROW));
                    reProposalTotalLocal = BigDecimal.ZERO;
                    beProposalTotalLocal = BigDecimal.ZERO;
                    reRecomTotalLocal = BigDecimal.ZERO;
                    beRecomTotalLocal = BigDecimal.ZERO;
                }
                budgetReportList.add(new BudgetReportView(EMPTYSTRING, EMPTYSTRING, EMPTYSTRING, "FUNCTION CENTRE-"
                        + detail.getFunction().getName(), EMPTYSTRING,
                        null, null, null, "functionrow"));
                final List<Object> majorCodeList = getAmountForMajorcodewiseForWorkingCopy(detail.getExecutingDepartment()
                        .getId().intValue(), detail.getFunction()
                        .getId(), glType); // majorcodewise total
                budgetReportList.addAll(majorCodeList);
                printed = false;
                majorCode = "";
            }
            if (!tempMajorCode.equals(majorCode) && detail.getBudgetGroup().getMajorCode() == null)// majorcodewise
            // -
            // heading
            {
                if (printed) {
                    budgetReportList.add(new BudgetReportView(EMPTYSTRING, EMPTYSTRING, EMPTYSTRING, TOTALSTRING, EMPTYSTRING,
                            reProposalTotalLocal,
                            reRecomTotalLocal, beProposalTotalLocal, beRecomTotalLocal, TOTALROW));
                    reProposalTotalLocal = BigDecimal.ZERO;
                    beProposalTotalLocal = BigDecimal.ZERO;
                    reRecomTotalLocal = BigDecimal.ZERO;
                    beRecomTotalLocal = BigDecimal.ZERO;
                }
                budgetReportList.add(new BudgetReportView(EMPTYSTRING, EMPTYSTRING, refNoMap.get(tempMajorCode), tempMajorCode
                        + "-" + coaMap.get(tempMajorCode), "",
                        null, null, null, "majorcodeheadingrow"));
            }

            // detail
            if (detail.getExecutingDepartment() != null && detail.getFunction() != null
                    && detail.getBudgetGroup().getMajorCode() == null)
                if (onSaveOrForward && !canViewApprovedAmount)
                    budgetReportList.add(new BudgetReportView(detail.getExecutingDepartment().getCode(), detail.getFunction()
                            .getCode(), glcode, glName,
                            EMPTYSTRING, detail.getOriginalAmount(), detail.getApprovedAmount(), beDetail.getOriginalAmount(),
                            beDetail.getApprovedAmount(),
                            "detailrow"));
                else
                    budgetReportList.add(new BudgetReportView(detail.getExecutingDepartment().getCode(), detail.getFunction()
                            .getCode(), glcode, glName,
                            EMPTYSTRING, detail.getOriginalAmount(), detail.getApprovedAmount(), beDetail.getOriginalAmount(),
                            beDetail.getApprovedAmount(),
                            "detailrow"));

            if (detail.getExecutingDepartment() != null)
                deptId = detail.getExecutingDepartment().getId().intValue();
            if (detail.getFunction() != null)
                functionId = detail.getFunction().getId();
            type = glType;
            majorCode = tempMajorCode;
            reProposalTotalLocal = reProposalTotalLocal.add(detail.getOriginalAmount());
            beProposalTotalLocal = beProposalTotalLocal.add(beDetail.getOriginalAmount());
            reRecomTotalLocal = reRecomTotalLocal.add(detail.getApprovedAmount());
            beRecomTotalLocal = beRecomTotalLocal.add(beDetail.getApprovedAmount());
            printed = true;
            isFirst = false;
        }
        if (reProposalTotalLocal.compareTo(BigDecimal.ZERO) != 0 && !majorcodewise)
            budgetReportList.add(new BudgetReportView(EMPTYSTRING, EMPTYSTRING, EMPTYSTRING, TOTALSTRING, EMPTYSTRING,
                    reProposalTotalLocal, reRecomTotalLocal,
                    beProposalTotalLocal, beRecomTotalLocal, TOTALROW));
        return budgetReportList;

    }

    /**
     * 1.get whole Sum for the function 2.get DepartmentWise sum for the function 3.Get Individual amount
     *
     * @return
     */

    private List getDataForGlance() {
        final List<BudgetReportView> budgetAtGlanceList = new ArrayList<BudgetReportView>();
        budgetAtGlanceList.addAll(getFunctionwiseSumForGlance());
        budgetAtGlanceList.addAll(getBudgetWiseSumAndDetail());
        return budgetAtGlanceList;
    }

    /**
     * get Sum for Each Budget/Department(Budget and Department are same) and details
     */
    private List<BudgetReportView> getBudgetWiseSumAndDetail() {
        // getSumforEachDepartment

        final List<BudgetReportView> ReportList = new ArrayList<BudgetReportView>();
        final LinkedHashMap<String, BudgetReportView> function_deptSumMap = new LinkedHashMap<String, BudgetReportView>();
        // Fetch For RE
        int i = 1;
        List params = new ArrayList();
        StringBuffer query = new StringBuffer("select function.name,executingDepartment.deptCode,sum(originalAmount),sum(approvedAmount) ")
                .append("from BudgetDetail bd  where bd.budget.financialYear.id=?").append(i++)
                .append(" and bd.budget.isbere='RE' ");
        params.add(budgetReport.getFinancialYear().getId());
        if (budgetReport.getFunction() != null && budgetReport.getFunction().getId() != null
                && budgetReport.getFunction().getId() != 0) {
            query.append("  and bd.function.id=?").append(i++);
            params.add(budgetReport.getFunction().getId());
        }
        query.append(" group by function.name,executingDepartment.deptCode order by function.name,executingDepartment.deptCode");
        List<Object[]> findAllBy = persistenceService.findAllBy(query.toString(), params);
        for (final Object[] o : findAllBy) {
            final BudgetReportView bv = new BudgetReportView();
            bv.setNarration((String) o[0]);
            bv.setDeptCode((String) o[1]);
            bv.setFunctionCode("");
            bv.setReProposalAmount(o[2] != null ? new BigDecimal(o[2].toString()) : BigDecimal.ZERO);
            bv.setReRecomAmount(o[3] != null ? new BigDecimal(o[3].toString()) : BigDecimal.ZERO);

            bv.setRowStyle("majorcodeheadingrow");
            function_deptSumMap.put((String) o[0] + "-" + (String) o[1], bv);
        }
        // Fetch For BE
        i = 1;
        params = new ArrayList();
        query = new StringBuffer("select function.name,executingDepartment.deptCode,sum(originalAmount),sum(approvedAmount) ")
                .append("from BudgetDetail bd  where bd.budget.financialYear.id=?").append(i++)
                .append(" and bd.budget.isbere='BE' ");
        params.add(getFinYear("next").getId());
        if (budgetReport.getFunction() != null && budgetReport.getFunction().getId() != null
                && budgetReport.getFunction().getId() != 0) {
            query.append("  and bd.function.id=?").append(i++);
            params.add(budgetReport.getFunction().getId());
        }
        query.append(" group by function.name, executingDepartment.deptCode order by function.name,executingDepartment.deptCode");
        findAllBy = persistenceService.findAllBy(query.toString(), params);
        for (final Object[] o : findAllBy) {
            final String key = (String) o[0] + "-" + (String) o[1];
            BudgetReportView bv = function_deptSumMap.get(key);

            // if RE dont have a function But BE has (only old data)
            if (bv == null) {
                bv = new BudgetReportView();
                bv.setNarration((String) o[0]);
                bv.setDeptCode((String) o[1]);
                bv.setFunctionCode("");
                bv.setBeProposalAmount(o[2] != null ? new BigDecimal(o[2].toString()) : BigDecimal.ZERO);
                bv.setBeRecomAmount(o[3] != null ? new BigDecimal(o[3].toString()) : BigDecimal.ZERO);
                function_deptSumMap.put((String) o[0] + "-" + (String) o[1], bv);
                continue;
            } else {
                bv.setBeProposalAmount(o[2] != null ? new BigDecimal(o[2].toString()) : BigDecimal.ZERO);
                bv.setBeRecomAmount(o[3] != null ? new BigDecimal(o[3].toString()) : BigDecimal.ZERO);
            }
        }
        // getDetails
        i = 1;
        params = new ArrayList();
        query = new StringBuffer(" from BudgetDetail bd  where bd.budget.financialYear.id=?").append(i++)
                .append(" and bd.budget.isbere='RE' ");
        params.add(budgetReport.getFinancialYear().getId());
        if (budgetReport.getFunction() != null && budgetReport.getFunction().getId() != null
                && budgetReport.getFunction().getId() != 0) {
            query.append("  and bd.function.id=?").append(i++);
            params.add(budgetReport.getFunction().getId());
        }
        query.append("  order by function.name,executingDepartment.deptCode");
        final List<BudgetDetail> details = persistenceService.findAllBy(query.toString(), params);
        final LinkedHashMap<String, List<BudgetReportView>> function_dept_DetailedMap = new LinkedHashMap<String, List<BudgetReportView>>();
        final LinkedHashMap<String, List<BudgetDetail>> function_dept_DetailedBudgetMap = new LinkedHashMap<String, List<BudgetDetail>>();
        String key = "";
        String glcode;
        String glName;
        for (final BudgetDetail detail : details) {
            key = detail.getFunction().getName() + "-" + detail.getExecutingDepartment().getCode();
            if (function_dept_DetailedBudgetMap.get(key) == null) {
                final List<BudgetDetail> fun_dept_dtlList = new ArrayList<BudgetDetail>();
                function_dept_DetailedBudgetMap.put(key, fun_dept_dtlList);
            }
            function_dept_DetailedBudgetMap.get(key).add(detail);
        }
        // Fetch For BE
        i = 1;
        params = new ArrayList();
        query = new StringBuffer("from BudgetDetail bd  where bd.budget.financialYear.id=?").append(i++)
                .append(" and bd.budget.isbere='BE' ");
        params.add(getFinYear("next").getId());
        if (budgetReport.getFunction() != null && budgetReport.getFunction().getId() != null
                && budgetReport.getFunction().getId() != 0) {
            query.append("  and bd.function.id=?").append(i++);
            params.add(budgetReport.getFunction().getId());
        }
        query.append(" order by function.name, executingDepartment.deptCode");
        final List<BudgetDetail> beDetails = persistenceService.findAllBy(query.toString(), params);
        for (final BudgetDetail beDetail : beDetails) {
            key = beDetail.getFunction().getName() + "-" + beDetail.getExecutingDepartment().getCode();
            for (final BudgetDetail reDetail : function_dept_DetailedBudgetMap.get(key))
                if (reDetail == null) {
                    if (beDetail.getBudgetGroup().getMajorCode() == null) {
                        glcode = getGlCode(beDetail);
                        beDetail.getBudgetGroup().getMinCode().getType().toString();
                        glName = getGlName(beDetail);
                    } else {
                        glcode = beDetail.getBudgetGroup().getMajorCode().getGlcode();
                        beDetail.getBudgetGroup().getMajorCode().getType().toString();
                        glName = beDetail.getBudgetGroup().getMajorCode().getName();

                    }

                    final BudgetReportView bv = new BudgetReportView();
                    bv.setNarration(glName);
                    bv.setDeptCode(beDetail.getExecutingDepartment().getCode());
                    bv.setGlCode(glcode);
                    bv.setFunctionCode(reDetail.getFunction().getCode());
                    bv.setReProposalAmount(reDetail.getOriginalAmount());
                    bv.setReRecomAmount(reDetail.getApprovedAmount());
                    bv.setRowStyle("typerow");
                    bv.setBeProposalAmount(beDetail.getOriginalAmount());
                    bv.setBeRecomAmount(beDetail.getApprovedAmount());
                    function_dept_DetailedMap.get(key).add(bv);
                    break;
                } else if (beDetail.compareTo(reDetail)) {

                    if (function_dept_DetailedMap.get(key) == null) {
                        final List<BudgetReportView> fun_dept_dtlList = new ArrayList<BudgetReportView>();
                        function_dept_DetailedMap.put(key, fun_dept_dtlList);
                    }

                    if (beDetail.getBudgetGroup().getMajorCode() == null) {
                        glcode = getGlCode(beDetail);
                        beDetail.getBudgetGroup().getMinCode().getType().toString();
                        glName = getGlName(beDetail);
                    } else {
                        glcode = beDetail.getBudgetGroup().getMajorCode().getGlcode();
                        beDetail.getBudgetGroup().getMajorCode().getType().toString();
                        glName = beDetail.getBudgetGroup().getMajorCode().getName();

                    }

                    final BudgetReportView bv = new BudgetReportView();
                    bv.setNarration(glName);
                    bv.setDeptCode(beDetail.getExecutingDepartment().getCode());
                    bv.setGlCode(glcode);
                    bv.setFunctionCode(reDetail.getFunction().getCode());
                    bv.setReProposalAmount(reDetail.getOriginalAmount());
                    bv.setReRecomAmount(reDetail.getApprovedAmount());
                    bv.setBeProposalAmount(beDetail.getOriginalAmount());
                    bv.setBeRecomAmount(beDetail.getApprovedAmount());
                    function_dept_DetailedMap.get(key).add(bv);
                    break;
                }
        }
        // Now add Individual into reportListfu
        for (final String tempKey : function_deptSumMap.keySet()) {

            final BudgetReportView bvHead = new BudgetReportView();
            bvHead.setNarration(function_deptSumMap.get(tempKey).getNarration());
            bvHead.setRowStyle("majorcodeheadingrow");
            ReportList.add(bvHead);
            // ReportList.add(function_deptSumMap.get(tempKey));
            ReportList.addAll(function_dept_DetailedMap.get(tempKey));
            final BudgetReportView bvTotal = new BudgetReportView();
            bvTotal.setNarration("TOTAL");
            bvTotal.setBeProposalAmount(function_deptSumMap.get(tempKey).getBeProposalAmount());
            bvTotal.setReProposalAmount(function_deptSumMap.get(tempKey).getReProposalAmount());
            bvTotal.setBeRecomAmount(function_deptSumMap.get(tempKey).getBeRecomAmount());
            bvTotal.setReRecomAmount(function_deptSumMap.get(tempKey).getReRecomAmount());
            bvTotal.setRowStyle(TOTALROW);
            ReportList.add(bvTotal);
        }
        return ReportList;
    }

    /**
     *
     */
    @SuppressWarnings("unchecked")
    private List<BudgetReportView> getFunctionwiseSumForGlance() {

        final List<BudgetReportView> ReportList = new ArrayList<BudgetReportView>();

        // Fetch For RE
        int i = 1;
        List params = new ArrayList();
        StringBuffer query = new StringBuffer("select function.name,sum(originalAmount),sum(approvedAmount) ")
                .append(" from BudgetDetail bd  where bd.budget.financialYear.id=?").append(i++)
                .append(" and bd.budget.isbere='RE' ");
        params.add(budgetReport.getFinancialYear().getId());
        if (budgetReport.getFunction() != null && budgetReport.getFunction().getId() != null
                && budgetReport.getFunction().getId() != 0) {
            query.append("  and bd.function.id=?").append(i++)
                    .append(" group by function.name order by function.name");
            params.add(budgetReport.getFunction().getId());
        }

        List<Object[]> findAllBy = persistenceService.findAllBy(query.toString(), params);
        for (final Object[] o : findAllBy) {
            final BudgetReportView bv = new BudgetReportView();
            bv.setNarration((String) o[0]);
            bv.setFunctionCode("");
            bv.setReProposalAmount(o[1] != null ? new BigDecimal(o[1].toString()) : BigDecimal.ZERO);
            bv.setReRecomAmount(o[2] != null ? new BigDecimal(o[2].toString()) : BigDecimal.ZERO);
            bv.setRowStyle("functionrow");
            ReportList.add(bv);
        }
        // Fetch For BE
        i = 1;
        params = new ArrayList();
        query = new StringBuffer("select function.name,sum(originalAmount),sum(approvedAmount)")
                .append(" from BudgetDetail bd  where bd.budget.financialYear.id=?").append(i++)
                .append(" and bd.budget.isbere='BE' ");
        params.add(getFinYear("next").getId());
        if (budgetReport.getFunction() != null && budgetReport.getFunction().getId() != null
                && budgetReport.getFunction().getId() != 0) {
            query.append("  and bd.function.id=?").append(i++);
            params.add(budgetReport.getFunction().getId());
        }
        query.append(" group by function.name");
        findAllBy = persistenceService.findAllBy(query.toString(), params);
        for (final BudgetReportView bv : ReportList)
            for (final Object[] o : findAllBy)
                if (bv.getNarration().equalsIgnoreCase((String) o[0])) {
                    bv.setBeProposalAmount(o[1] != null ? new BigDecimal(o[1].toString()) : BigDecimal.ZERO);
                    bv.setBeRecomAmount(o[2] != null ? new BigDecimal(o[2].toString()) : BigDecimal.ZERO);
                    break;
                }
        return ReportList;

    }

    /**
     * @param string --- "previous" for previous FinancialYearId --- "next" for next FinancialyearId
     * @return
     */
    private CFinancialYear getFinYear(final String option) {
        final Calendar cal = Calendar.getInstance();
        CFinancialYear finYear = null;
        if (option.equalsIgnoreCase("previous")) {
            cal.setTime(budgetReport.getFinancialYear().getStartingDate());
            cal.add(Calendar.DATE, -1);
            finYear = (CFinancialYear) persistenceService.find("from CFinancialYear c where c.endingDate=?1", cal.getTime());
            if (finYear == null)
                throw new ValidationException(Arrays.asList(new ValidationError("next.financial.year.not.defined",
                        "Previous financial year not defined")));
        } else if (option.equalsIgnoreCase("next")) {

            cal.setTime(budgetReport.getFinancialYear().getEndingDate());
            cal.add(Calendar.DATE, 1);
            finYear = (CFinancialYear) persistenceService.find("from CFinancialYear c where c.startingDate=?1", cal.getTime());
            if (finYear == null)
                throw new ValidationException(Arrays.asList(new ValidationError("next.financial.year.not.defined",
                        "Next financial year not defined")));
        }

        return finYear;
    }

    /**
     * @param finYearForRE
     * @return
     */
    private Map<String, Map<String, Object>> getSqlForFinYear(final Long finYearForRE) {
        final Map<String, Map<String, Object>> queryMap = new HashMap<>();
        final Map<String, Object> queryParams = new HashMap<>();
        final StringBuilder sql = new StringBuilder("");
        sql.append(" bd.budget.financialYear.id=:finYearId");
        queryParams.put("finYearId", finYearForRE);
        if (budgetReport.getDepartment() != null && budgetReport.getDepartment().getId() != null) {
            sql.append(" and bd.executingDepartment.id=:execDeptId");
            queryParams.put("execDeptId", budgetReport.getDepartment().getId());
        }
        if (budgetReport.getFunction() != null && budgetReport.getFunction().getId() != null) {
            sql.append(" and bd.function.id=:functionId");
            queryParams.put("functionId", budgetReport.getFunction().getId());
        }
        if (topBudget != null) {
            sql.append(" and bd.budget.id=:budgetId");
            queryParams.put("budgetId", topBudget.getId());
        }
        queryMap.put(sql.toString(), queryParams);
        return queryMap;
    }

    private Map<String, Map<String, Object>> getSqlForFinYearBE(final Long finYearForRE) {
        final Map<String, Map<String, Object>> queryMap = new HashMap<>();
        final Map<String, Object> queryParams = new HashMap<>();
        final StringBuilder sql = new StringBuilder("");
        sql.append(" bd.budget.financialYear.id=:finYearId");
        queryParams.put("finYearId", finYearForRE);
        if (budgetReport.getDepartment() != null && budgetReport.getDepartment().getId() != null) {
            sql.append(" and bd.executingDepartment.id=:execDeptId");
            queryParams.put("execDeptId", budgetReport.getDepartment().getId());
        }
        if (budgetReport.getFunction() != null && budgetReport.getFunction().getId() != null) {
            sql.append(" and bd.function.id=:functionId");
            queryParams.put("functionId", budgetReport.getFunction().getId());
        }
        if (topBudget != null) {
            sql.append(" and bd.budget.referenceBudget.id=:budgetId");
            queryParams.put("budgetId", topBudget.getId());
        }
        queryMap.put(sql.toString(), queryParams);
        return queryMap;
    }

    private Long getFinYearForRE() {
        final Long finId = budgetReport.getFinancialYear().getId();
        final Long budgetCount = (Long) persistenceService.find(
                "select count(*) from Budget b where b.financialYear.id=?1 and b.isbere='RE'", finId);
        if (budgetCount == 0) {
            final Date startingDate = budgetReport.getFinancialYear().getStartingDate();
            final Calendar cal = Calendar.getInstance();
            cal.setTime(startingDate);
            cal.add(Calendar.DATE, -1);
            final CFinancialYear prevFinyear = (CFinancialYear) persistenceService.find(
                    "from CFinancialYear c where c.endingDate=?1",
                    cal.getTime());
            if (prevFinyear == null)
                throw new ValidationException(Arrays.asList(new ValidationError("next.financial.year.not.defined",
                        "Next financial year not defined")));
            else {
                financialYearForRE = prevFinyear;
                return prevFinyear.getId();
            }
        } else {
            financialYearForRE = budgetReport.getFinancialYear();
            return budgetReport.getFinancialYear().getId();
        }

    }

    public void setEisCommonService(final EisCommonService eisCommonService) {
        this.eisCommonService = eisCommonService;
    }

    private boolean getConsiderReAppropriationAsSeperate() {
        final List<AppConfigValues> appList = appConfigValuesService.getConfigValuesByModuleAndKey(Constants.EGF,
                "CONSIDER_RE_REAPPROPRIATION_AS_SEPARATE");
        String appValue = "-1";
        appValue = appList.get(0).getValue();
        return "Y".equalsIgnoreCase(appValue);
    }

}