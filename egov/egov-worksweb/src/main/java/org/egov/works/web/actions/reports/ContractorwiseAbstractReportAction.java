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
package org.egov.works.web.actions.reports;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.ContractorGrade;
import org.egov.commons.EgwTypeOfWork;
import org.egov.commons.Scheme;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.commons.dao.FunctionHibernateDAO;
import org.egov.commons.dao.FundHibernateDAO;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.persistence.utils.Page;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.utils.EgovPaginatedList;
import org.egov.infstr.search.SearchQuerySQL;
import org.egov.model.budget.BudgetGroup;
import org.egov.pims.service.PersonalInformationService;
import org.egov.works.services.WorkProgressAbstractReportService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.web.actions.estimate.AjaxEstimateAction;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("deprecation")
@Results({
        @Result(name = ContractorwiseAbstractReportAction.PDF, type = "stream", location = "reportInputStream", params = {
                "inputName", "contractorwiseAbstractReport", "contentType", "application/pdf", "contentDisposition",
                "no-cache;filename=ContractorwiseAbstractReport.pdf" }),
        @Result(name = ContractorwiseAbstractReportAction.XLS, type = "stream", location = "reportInputStream", params = {
                "inputName", "contractorwiseAbstractReport", "contentType", "application/xls", "contentDisposition",
                "no-cache;filename=ContractorwiseAbstractReport.xls" }),
        @Result(name = "estimateXLS", type = "stream", location = "reportInputStream", params = {
                "inputName", "contractorwiseReport_Estimates", "contentType", "application/xls", "contentDisposition",
                "no-cache;filename=ContractorwiseReport_Estimates.xls" }),
        @Result(name = "estimatePDF", type = "stream", location = "reportInputStream", params = {
                "inputName", "contractorwiseReport_Estimates", "contentType", "application/pdf", "contentDisposition",
                "no-cache;filename=ContractorwiseReport_Estimates.pdf" }) })
@ParentPackage("egov")
public class ContractorwiseAbstractReportAction extends BaseFormAction {

    private static final long serialVersionUID = 6545841427307931948L;
    private static final String DEPT_WISE = "deptwise";
    private String finYearId;
    private String finYearRange;
    private Date fromDate;
    private Date toDate;
    private Integer executingDepartment = -1;
    private Integer worksType = -1;
    private Integer worksSubType = -1;
    private Integer fund = -1;;
    private Integer function = -1;
    private Integer scheme = -1;
    private Integer subScheme = -1;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private FundHibernateDAO fundDao;
    @Autowired
    private FunctionHibernateDAO functionHibDao;
    @Autowired
    private FinancialYearHibernateDAO finHibernateDao;
    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;
    private static final String BUDGET_HEADS_APPCONFIG_KEY = "WORK_PROGRESS_ABSTRACT_RPT2_BUDGT_HEADS";
    private String budgetHeadsAppConfValue = null;
    private WorksService worksService;
    private List<String> dropDownBudgetHeads; // This contains values from the
    // drop down
    private String depositCodesAppConfValue = null;
    private static final String DEPOSITCODES_APPCONFIG_KEY = "WORK_PROGRESS_ABSTRACT_RPT2_DEPOSIT_CODES";
    private List<String> dropDownDepositCodes;
    private List<Long> allDepositCodes;
    private List<Long> allBudgetHeads; // This contains values from the drop
    // down
    private String currentFinancialYearId;
    private String finYearRangeStr;
    private String subHeader;
    private String contractorName = "";
    private Long contractorId;
    private Long gradeId;
    private List<String> budgetHeads;
    private List<Long> budgetHeadIds;
    private List<Long> depositCodeIds;
    private BudgetDetailsDAO budgetDetailsDAO;
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
    private List<ContractorwiseReportBean> resultList = new LinkedList<>();
    private String resultStatus = "beforeSearch";
    public static final String PDF = "PDF";
    public static final String XLS = "XLS";
    private InputStream reportInputStream;
    private ReportService reportService;
    private final String JASPER_NAME = "contractorwiseAbstractReport";
    private String searchType = "";
    private EgovPaginatedList paginatedList;
    private Integer page = 1;
    private Integer pageSize = 30;
    private final List<CommonDrillDownReportBean> commonBeanList = new ArrayList<>();
    private List<Object> paramList;
    public WorkProgressAbstractReportService workProgressAbstractReportService;
    private String budgetHeadsStr;
    private String depositCodesStr;
    private String depositCodeIdsStr;
    private String budgetHeadIdsStr;
    private String reportMessage;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Object getModel() {
        return null;
    }

    public String beforeSearch() {
        return DEPT_WISE;
    }

    @Override
    public void prepare() {
        super.prepare();
        final List<CFinancialYear> finYrList = worksService.getAllFinancialYearsForWorks();
        addDropdownData("finYearList", finYrList);
        addDropdownData("executingDepartmentList",
                entityManager.createQuery("from Department", Department.class).getResultList());
        addDropdownData("worksTypeList",
                entityManager.createQuery("from EgwTypeOfWork etw1 where etw1.parentid is null", EgwTypeOfWork.class)
                        .getResultList());
        final AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
        ajaxEstimateAction.setPersistenceService(getPersistenceService());
        ajaxEstimateAction.setAssignmentService(assignmentService);
        populateCategoryList(ajaxEstimateAction, getWorksType() == -1 ? false : getWorksType() != -1);
        addDropdownData("fundList", fundDao.findAllActiveIsLeafFunds());
        addDropdownData("functionList", functionHibDao.findAll());
        addDropdownData("schemeList", entityManager.createQuery("from Scheme sc where sc.isactive = true", Scheme.class)
                .getResultList());
        final AjaxWorkProgressAction ajaxWorkProgressAction = new AjaxWorkProgressAction();
        populateSubSchemeList(ajaxWorkProgressAction, getScheme() != null);
        addDropdownData("budgetHeadList", getBudgetGroupsFromAppConfig());
        addDropdownData("depositCodeList", getDepositCodesFromAppConfig());
        addDropdownData(
                "allDepositCodeCOAList",
                entityManager.createQuery(
                        new StringBuffer("select distinct(fd.coa)")
                                .append(" from FinancialDetail fd")
                                .append(" where fd.abstractEstimate in ( select woe.estimate from WorkOrderEstimate woe")
                                .append(" where woe.workOrder.egwStatus.code='APPROVED' ) order by name  ").toString(),
                        CChartOfAccounts.class)
                        .getResultList());
        addDropdownData(
                "allBudgetHeadList",
                entityManager.createQuery(
                        new StringBuffer("select distinct(fd.budgetGroup)")
                                .append(" from FinancialDetail fd")
                                .append(" where fd.abstractEstimate in ( select woe.estimate from WorkOrderEstimate woe")
                                .append(" where woe.workOrder.egwStatus.code='APPROVED' ) order by name  ").toString(),
                        BudgetGroup.class)
                        .getResultList());
        final CFinancialYear financialYear = finHibernateDao.getFinYearByDate(new Date());
        if (financialYear != null)
            currentFinancialYearId = financialYear.getId().toString();
        finYearRangeStr = generateFinYrList(finYrList);
        addDropdownData("gradeList",
                entityManager.createQuery("from ContractorGrade order by upper(grade)", ContractorGrade.class).getResultList());
        if (dropDownBudgetHeads != null && dropDownBudgetHeads.size() > 0)
            generateBudgetHeads();
        if (dropDownDepositCodes != null && dropDownDepositCodes.size() > 0)
            generateDepositCodes();
        // All deposit codes and budget heads
        if (allDepositCodes != null && allDepositCodes.size() > 0)
            getDepositCodesFromAllDepositCodes();
        if (allBudgetHeads != null && allBudgetHeads.size() > 0)
            getBudgetHeadsFromAllBudgetHeads();
        setBudgetHeadIdsAndDepositCodeIds();
    }

    private void setBudgetHeadIdsAndDepositCodeIds() {
        if (StringUtils.isNotBlank(budgetHeadsStr)) {
            budgetHeads = new ArrayList<>();
            budgetHeadIds = new ArrayList<>();
            workProgressAbstractReportService.setBudgetHeadsFromString(budgetHeadsStr, budgetHeads, budgetHeadIds);
        }
        if (StringUtils.isNotBlank(depositCodesStr)) {
            depositCodeIds = new ArrayList<>();
            workProgressAbstractReportService.setDepositCodesFromString(depositCodesStr, depositCodeIds);
        }
        if (StringUtils.isNotBlank(budgetHeadIdsStr)) {
            budgetHeads = new ArrayList<>();
            budgetHeadIds = new ArrayList<>();
            workProgressAbstractReportService.setBudgetHeadsFromIdString(budgetHeadIdsStr, budgetHeads, budgetHeadIds);
        }
        if (StringUtils.isNotBlank(depositCodeIdsStr)) {
            depositCodeIds = new ArrayList<>();
            workProgressAbstractReportService.setDepositCodesFromIdString(depositCodeIdsStr, depositCodeIds);
        }
    }

    private void formReportMessage() {
        final String dateStr = (String) entityManager
                .createNativeQuery(" select min(VIEW_TIMESTAMP) from EGW_WPREPORT_EST_WO_MVIEW ").getResultList()
                .get(0);
        reportMessage = getText("contractorwiseAbstractReport.data.message", new String[] { dateStr });
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public String search() {
        resultStatus = "afterSearch";
        formReportMessage();
        final Map<String, Object> queryMap = generateQuery();
        final String query = (String) queryMap.get("query");
        final String countQuery = (String) queryMap.get("count");
        final List<Object> params = (List<Object>) queryMap.get("params");
        Page resPage;
        if (searchType.equals("")) {
            final SearchQuerySQL sqlQuery = new SearchQuerySQL(query, countQuery, params);
            resPage = sqlQuery.getPage(getPersistenceService(), page, pageSize);
            paginatedList = new EgovPaginatedList(resPage, sqlQuery.getCount(getPersistenceService()));
            populateData(paginatedList.getList());
            if (paginatedList.getList() == null)
                paginatedList.setList(resultList);
            else {
                paginatedList.getList().clear();
                paginatedList.getList().addAll(resultList);
            }
        } else if (searchType.equalsIgnoreCase("report")) {
            List<Object[]> wholeQueryList = null;
            final Query qry = entityManager.createNativeQuery(query);
            for (int i = 0; i < params.size(); i++)
                qry.setParameter(i, params.get(i));
            wholeQueryList = qry.getResultList();
            populateData(wholeQueryList);
        }
        return DEPT_WISE;
    }

    public String viewEstimatePDF() {
        setSearchType("report");
        showEstimatesTakenUpDrillDown();
        final Map<String, Object> reportParams = new HashMap<>();
        reportParams.put("subHeader", subHeader + "\n" + reportMessage);
        reportParams.put("mainHeader", getText("contractorwiseAbstractReport.showTakenUpEst") + getContractorName());

        final ReportRequest reportRequest = new ReportRequest("contractorwiseReport_Estimates", commonBeanList,
                reportParams);
        final ReportOutput reportOutput = reportService.createReport(reportRequest);
        if (reportOutput != null && reportOutput.getReportOutputData() != null)
            reportInputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());

        return "estimatePDF";
    }

    public String viewEstimateXLS() {
        setSearchType("report");
        showEstimatesTakenUpDrillDown();
        final Map<String, Object> reportParams = new HashMap<>();
        reportParams.put("subHeader", subHeader + "\n" + reportMessage);

        reportParams.put("mainHeader", getText("contractorwiseAbstractReport.showTakenUpEst") + getContractorName());
        final ReportRequest reportRequest = new ReportRequest("contractorwiseReport_Estimates", commonBeanList,
                reportParams);
        reportRequest.setReportFormat(ReportFormat.XLS);
        final ReportOutput reportOutput = reportService.createReport(reportRequest);
        if (reportOutput != null && reportOutput.getReportOutputData() != null)
            reportInputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());

        return "estimateXLS";
    }

    private String getTakenUpQuery(final String finalBillType, Integer index, List<Object> params) {
        final StringBuffer query = new StringBuffer(
                "SELECT WORK_ORDER_CONTRACTOR_ID,WORK_ORDER_CONTRACTOR_NAME,WORK_ORDER_CONTRACTOR_CODE, ")
                        .append(" SUM(EST_COUNT) TAKEN_UP_EST_COUNT ,SUM(NVL(WORK_ORDER_WORKORDER_AMOUNT,0)) TAKEN_UP_WO_AMOUNT, ")
                        .append(" 0 COMPLETED_EST_COUNT,0 COMPLETED_PYMT_RLSD,0 IN_PROGRESS_EST_COUNT,0 IN_PROGRESS_NEGO_VAL ,")
                        .append("0 IN_PROGRESS_PYMT_RLSD,0 NOT_STARTED_EST_COUNT,0 NOT_STARTED_WO_AMT,")
                        .append(" SUM(NVL(REV_WO_TAKEN_UP_AMT,0)) REV_WO_TAKEN_UP_AMT_SUM, 0 REV_WO_IN_PROGRESS_NEG_VAL_SUM,")
                        .append(" 0 REV_WO_NOT_STARTED_AMT_SUM ")
                        .append(" FROM (select WORK_ORDER_CONTRACTOR_ID , WORK_ORDER_CONTRACTOR_NAME, ")
                        .append(" WORK_ORDER_CONTRACTOR_CODE, COUNT(DISTINCT(ABS_EST_ID)) EST_COUNT , WORK_ORDER_ID,")
                        .append(" WORK_ORDER_WORKORDER_AMOUNT,  ")
                        .append(" (SELECT SUM(NVL(INNER_VIEW.WORK_ORDER_WORKORDER_AMOUNT,0)) FROM EGW_WPREPORT_EST_WO_MVIEW INNER_VIEW")
                        .append(" WHERE INNER_VIEW.WORK_ORDER_PARENTID = OUTER_VIEW.WORK_ORDER_ID) REV_WO_TAKEN_UP_AMT ")
                        .append(" from EGW_WPREPORT_EST_WO_MVIEW OUTER_VIEW ")
                        .append(constructSearchFilter(index));
        params.addAll(paramList);
        query.append(" where ABS_EST_PARENTID IS NULL AND WORK_ORDER_STATUS_CODE = 'APPROVED' ")
                .append(constructSearchFilter(index));
        params.addAll(paramList);
        query.append(" group by WORK_ORDER_CONTRACTOR_ID, WORK_ORDER_CONTRACTOR_NAME , ")
                .append(" WORK_ORDER_CONTRACTOR_CODE,WORK_ORDER_ID,WORK_ORDER_WORKORDER_AMOUNT ) ")
                .append(" group by WORK_ORDER_CONTRACTOR_ID, WORK_ORDER_CONTRACTOR_NAME , WORK_ORDER_CONTRACTOR_CODE ");
        return query.toString();
    }

    private String getCompletedQuery(final String finalBillType, Integer index, List<Object> params) {
        final StringBuffer query = new StringBuffer(" SELECT WORK_ORDER_CONTRACTOR_ID,")
                .append("  WORK_ORDER_CONTRACTOR_NAME ,WORK_ORDER_CONTRACTOR_CODE, 0 TAKEN_UP_EST_COUNT  ,0 TAKEN_UP_WO_AMOUNT,")
                .append("  SUM(EST_COUNT) COMPLETED_EST_COUNT , SUM(PYMT_RELEASED_AMT) COMPLETED_PYMT_RLSD , 0 IN_PROGRESS_EST_COUNT,")
                .append("0 IN_PROGRESS_NEGO_VAL,0 IN_PROGRESS_PYMT_RLSD,0 NOT_STARTED_EST_COUNT,0 NOT_STARTED_WO_AMT, ")
                .append(" 0 REV_WO_TAKEN_UP_AMT_SUM, 0 REV_WO_IN_PROGRESS_NEG_VAL_SUM, 0 REV_WO_NOT_STARTED_AMT_SUM ")
                .append(" FROM (SELECT WORK_ORDER_CONTRACTOR_ID , WORK_ORDER_CONTRACTOR_NAME , WORK_ORDER_CONTRACTOR_CODE,")
                .append(" WORK_ORDER_ID, COUNT(DISTINCT(ABS_EST_ID)) EST_COUNT , (SELECT SUM(NVL(bill_view.PMT_RLSD_TO_CONTRACTOR_FOR_WO,0))")
                .append(" FROM EGW_WPREPORT_EST_BILLS_MVIEW bill_view WHERE bill_view.BILL_REG_BILLTYPE = ?")
                .append(index++);
        params.add(finalBillType);
        query.append(" AND bill_view.WORK_ORDER_ID = WO_VIEW.WORK_ORDER_ID AND bill_view.BILL_REG_BILLSTATUS='APPROVED'")
                .append(" ) AS PYMT_RELEASED_AMT FROM EGW_WPREPORT_EST_WO_MVIEW WO_VIEW WHERE WORK_ORDER_STATUS_CODE = 'APPROVED'")
                .append(" AND WORK_ORDER_ID IN (SELECT bill_view.WORK_ORDER_ID FROM EGW_WPREPORT_EST_BILLS_MVIEW bill_view")
                .append(" WHERE bill_view.BILL_REG_BILLTYPE = ?").append(index++);
        params.add(finalBillType);
        query.append(" AND bill_view.WORK_ORDER_ID = WO_VIEW.WORK_ORDER_ID AND bill_view.BILL_REG_BILLSTATUS='APPROVED'")
                .append(" ) AND ABS_EST_PARENTID IS NULL ")
                .append(constructSearchFilter(index));
        params.addAll(paramList);
        query.append(" GROUP BY WORK_ORDER_CONTRACTOR_ID , WORK_ORDER_CONTRACTOR_NAME , WORK_ORDER_CONTRACTOR_CODE,")
                .append(" WORK_ORDER_ID) GROUP BY WORK_ORDER_CONTRACTOR_ID, WORK_ORDER_CONTRACTOR_NAME , WORK_ORDER_CONTRACTOR_CODE");

        return query.toString();
    }

    private String getInProgressQuery(final String finalBillType, Integer index, List<Object> params) {
        final StringBuffer query = new StringBuffer(" SELECT WORK_ORDER_CONTRACTOR_ID , WORK_ORDER_CONTRACTOR_NAME,")
                .append(" WORK_ORDER_CONTRACTOR_CODE, 0 TAKEN_UP_EST_COUNT  ,0 TAKEN_UP_WO_AMOUNT, 0 COMPLETED_EST_COUNT,")
                .append(" 0 COMPLETED_PYMT_RLSD, SUM(EST_COUNT) IN_PROGRESS_EST_COUNT, ")
                .append(" SUM(NVL(TENDER_RESP_NEGOTIATED_VALUE,0)) IN_PROGRESS_NEGO_VAL , SUM(PYMT_RELEASED_AMT) IN_PROGRESS_PYMT_RLSD,")
                .append(" 0 NOT_STARTED_EST_COUNT,0 NOT_STARTED_WO_AMT, 0 REV_WO_TAKEN_UP_AMT_SUM,")
                .append(" SUM(NVL(REV_WO_IN_PROGRESS_NEG_VAL,0)) REV_WO_IN_PROGRESS_NEG_VAL_SUM,0 REV_WO_NOT_STARTED_AMT_SUM  ")
                .append(" FROM (SELECT WORK_ORDER_CONTRACTOR_ID , WORK_ORDER_CONTRACTOR_NAME , WORK_ORDER_CONTRACTOR_CODE ,")
                .append(" WORK_ORDER_ID, COUNT(DISTINCT(ABS_EST_ID)) EST_COUNT , TENDER_RESP_NEGOTIATED_VALUE,")
                .append(" (SELECT SUM(NVL(BILL_VIEW.PMT_RLSD_TO_CONTRACTOR_FOR_WO,0)) FROM EGW_WPREPORT_EST_BILLS_MVIEW BILL_VIEW")
                .append(" WHERE BILL_VIEW.WORK_ORDER_ID = EST_VIEW.WORK_ORDER_ID AND BILL_VIEW.BILL_REG_BILLSTATUS='APPROVED'")
                .append(" ) AS PYMT_RELEASED_AMT, (SELECT SUM(NVL(INNER_VIEW.WORK_ORDER_WORKORDER_AMOUNT,0))")
                .append(" FROM EGW_WPREPORT_EST_WO_MVIEW INNER_VIEW WHERE INNER_VIEW.WORK_ORDER_PARENTID = EST_VIEW.WORK_ORDER_ID)")
                .append(" REV_WO_IN_PROGRESS_NEG_VAL FROM EGW_WPREPORT_EST_WO_MVIEW EST_VIEW WHERE WORK_ORDER_STATUS_CODE = 'APPROVED'")
                .append(" AND WO_LATEST_OFFLINE_STATUS_CODE = ?").append(index++);
        params.add(WorksConstants.WO_STATUS_WOCOMMENCED);
        query.append(" AND ?").append(index++);
        params.add(finalBillType);
        query.append(" NOT IN (SELECT DISTINCT(BILL_VIEW.BILL_REG_BILLTYPE) FROM EGW_WPREPORT_EST_BILLS_MVIEW BILL_VIEW")
                .append(" WHERE BILL_VIEW.WORK_ORDER_ID = EST_VIEW.WORK_ORDER_ID AND BILL_VIEW.BILL_REG_BILLSTATUS='APPROVED'")
                .append(" ) AND EST_VIEW.TENDER_RESP_STATUS_CODE='APPROVED' AND ABS_EST_PARENTID IS NULL")
                .append(constructSearchFilter(index));
        params.addAll(paramList);
        query.append(" GROUP BY WORK_ORDER_CONTRACTOR_ID ,WORK_ORDER_CONTRACTOR_NAME ,WORK_ORDER_CONTRACTOR_CODE, WORK_ORDER_ID,")
                .append(" TENDER_RESP_NEGOTIATED_VALUE) GROUP BY WORK_ORDER_CONTRACTOR_ID ,")
                .append(" WORK_ORDER_CONTRACTOR_NAME, WORK_ORDER_CONTRACTOR_CODE");
        return query.toString();
    }

    private String getNotYetStartedQuery(final String finalBillType, Integer index, List<Object> params) {
        final StringBuffer query = new StringBuffer("SELECT WORK_ORDER_CONTRACTOR_ID, WORK_ORDER_CONTRACTOR_NAME,")
                .append(" WORK_ORDER_CONTRACTOR_CODE, 0 TAKEN_UP_EST_COUNT  ,0 TAKEN_UP_WO_AMOUNT, 0 COMPLETED_EST_COUNT,")
                .append(" 0 COMPLETED_PYMT_RLSD, 0 IN_PROGRESS_EST_COUNT, 0 IN_PROGRESS_NEGO_VAL, 0 IN_PROGRESS_PYMT_RLSD, ")
                .append(" SUM(EST_COUNT) NOT_STARTED_EST_COUNT, SUM(NVL(WORK_ORDER_WORKORDER_AMOUNT,0)) NOT_STARTED_WO_AMT,")
                .append(" 0 REV_WO_TAKEN_UP_AMT_SUM,0 REV_WO_IN_PROGRESS_NEG_VAL_SUM, SUM(NVL(REV_WO_NOT_STARTED_AMT,0))")
                .append(" REV_WO_NOT_STARTED_AMT_SUM ")
                .append(" FROM (SELECT WORK_ORDER_CONTRACTOR_ID ,WORK_ORDER_CONTRACTOR_NAME, WORK_ORDER_CONTRACTOR_CODE, WORK_ORDER_ID,")
                .append(" COUNT(DISTINCT(ABS_EST_ID)) EST_COUNT, WORK_ORDER_WORKORDER_AMOUNT,")
                .append(" (SELECT SUM(NVL(INNER_VIEW.WORK_ORDER_WORKORDER_AMOUNT,0)) FROM EGW_WPREPORT_EST_WO_MVIEW")
                .append(" INNER_VIEW WHERE INNER_VIEW.WORK_ORDER_PARENTID = OUTER_VIEW.WORK_ORDER_ID) REV_WO_NOT_STARTED_AMT ")
                .append(" FROM EGW_WPREPORT_EST_WO_MVIEW OUTER_VIEW WHERE (WO_LATEST_OFFLINE_STATUS_CODE != ?").append(index++);
        params.add(WorksConstants.WO_STATUS_WOCOMMENCED);
        query.append(" OR WO_LATEST_OFFLINE_STATUS_CODE IS NULL) AND WORK_ORDER_STATUS_CODE = 'APPROVED' ")
                .append(" AND ABS_EST_PARENTID IS NULL ")
                .append(constructSearchFilter(index));
        params.addAll(paramList);
        query.append(" GROUP BY WORK_ORDER_CONTRACTOR_ID ,WORK_ORDER_CONTRACTOR_NAME, WORK_ORDER_CONTRACTOR_CODE,")
                .append(" WORK_ORDER_ID, WORK_ORDER_WORKORDER_AMOUNT)")
                .append(" GROUP BY WORK_ORDER_CONTRACTOR_ID, WORK_ORDER_CONTRACTOR_NAME, WORK_ORDER_CONTRACTOR_CODE");
        return query.toString();
    }

    private Map<String, Object> generateQuery() {
        final String finalBillType = worksService.getWorksConfigValue("FinalBillType");
        Integer index = Integer.valueOf(1);
        List<Object> params = new ArrayList<>();
        final String getTakenUpQuery = getTakenUpQuery(finalBillType, index, params);
        final String getCompletedQuery = getCompletedQuery(finalBillType, index, params);
        final String getInProgressQuery = getInProgressQuery(finalBillType, index, params);
        final String getNotYetStartedQuery = getNotYetStartedQuery(finalBillType, index, params);

        final StringBuffer mainQuery = new StringBuffer(" select WORK_ORDER_CONTRACTOR_ID, WORK_ORDER_CONTRACTOR_NAME,")
                .append(" WORK_ORDER_CONTRACTOR_CODE, SUM(TAKEN_UP_EST_COUNT),SUM(TAKEN_UP_WO_AMOUNT), ")
                .append(" SUM(COMPLETED_EST_COUNT),SUM(COMPLETED_PYMT_RLSD), SUM(IN_PROGRESS_EST_COUNT),SUM(IN_PROGRESS_NEGO_VAL),")
                .append(" SUM(IN_PROGRESS_PYMT_RLSD),SUM(NOT_STARTED_EST_COUNT), SUM(NOT_STARTED_WO_AMT), SUM(NVL(REV_WO_TAKEN_UP_AMT_SUM,0)),")
                .append(" SUM(NVL(REV_WO_IN_PROGRESS_NEG_VAL_SUM,0)), SUM(NVL(REV_WO_NOT_STARTED_AMT_SUM,0)) FROM ( ")
                .append(getTakenUpQuery)
                .append(" UNION ")
                .append(getCompletedQuery)
                .append(" UNION ")
                .append(getInProgressQuery)
                .append(" UNION ")
                .append(getNotYetStartedQuery)
                .append(" ) GROUP BY WORK_ORDER_CONTRACTOR_ID, WORK_ORDER_CONTRACTOR_NAME,WORK_ORDER_CONTRACTOR_CODE ");

        final Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("query", mainQuery.toString());
        returnMap.put("count", "Select count (*) from (" + mainQuery.toString() + ")");
        returnMap.put("params", params);

        return returnMap;
    }

    private void populateData(final List<Object[]> objResultList) {
        if (objResultList != null && objResultList.size() > 0) {
            populateBeanData(objResultList);
            populateBalanceInfo();
            populateContractorClass();
            if (searchType.equalsIgnoreCase("report"))
                populateTotalRow();
            convertAllAmountsToCrores();
        }
    }

    private void populateBeanData(final List<Object[]> objResultList) {
        ContractorwiseReportBean bean = null;
        for (final Object[] objArr : objResultList) {
            bean = new ContractorwiseReportBean();
            if (objArr[0] != null)
                bean.setContractorId(((BigDecimal) objArr[0]).longValue());
            if (objArr[1] != null)
                bean.setContractorName(objArr[1].toString());
            if (objArr[2] != null)
                bean.setContractorCode(objArr[2].toString());
            if (objArr[3] != null)
                bean.setTakenUpEstimateCount(((BigDecimal) objArr[3]).intValue());
            if (objArr[4] != null)
                bean.setTakenUpWOAmount((BigDecimal) objArr[4]);
            if (objArr[5] != null)
                bean.setCompletedEstimateCount(((BigDecimal) objArr[5]).intValue());
            if (objArr[6] != null)
                bean.setCompletedWOAmount((BigDecimal) objArr[6]);
            if (objArr[7] != null)
                bean.setInProgressEstimateCount(((BigDecimal) objArr[7]).intValue());
            if (objArr[8] != null)
                bean.setInProgressTenderNegotiatedAmt((BigDecimal) objArr[8]);
            if (objArr[9] != null)
                bean.setInProgressPaymentReleasedAmt((BigDecimal) objArr[9]);
            if (objArr[10] != null)
                bean.setNotYetStartedEstimateCount(((BigDecimal) objArr[10]).intValue());
            if (objArr[11] != null)
                bean.setNotYetStartedWOAmount((BigDecimal) objArr[11]);
            if (objArr[12] != null)
                bean.setTakenUpWOAmount(bean.getTakenUpWOAmount().add((BigDecimal) objArr[12]));
            if (objArr[13] != null)
                bean.setInProgressTenderNegotiatedAmt(bean.getInProgressTenderNegotiatedAmt().add(
                        (BigDecimal) objArr[13]));
            if (objArr[14] != null)
                bean.setNotYetStartedWOAmount(bean.getNotYetStartedWOAmount().add((BigDecimal) objArr[14]));
            bean.setInProgressBalanceAmount(bean.getInProgressTenderNegotiatedAmt().subtract(
                    bean.getInProgressPaymentReleasedAmt()));

            resultList.add(bean);
        }
    }

    private void populateBalanceInfo() {

        for (final ContractorwiseReportBean bean : resultList) {
            bean.setBalanceAmount(bean.getInProgressTenderNegotiatedAmt().add(bean.getNotYetStartedWOAmount()));
            bean.setBalanceEstimateCount(bean.getInProgressEstimateCount() + bean.getNotYetStartedEstimateCount());
        }
    }

    private void convertAllAmountsToCrores() {
        for (final ContractorwiseReportBean bean : resultList) {
            bean.setBalanceAmount(workProgressAbstractReportService.getRoundedOfAmount(bean.getBalanceAmount(), 4));
            bean.setCompletedWOAmount(workProgressAbstractReportService.getRoundedOfAmount(bean.getCompletedWOAmount(),
                    4));
            bean.setInProgressBalanceAmount(workProgressAbstractReportService.getRoundedOfAmount(
                    bean.getInProgressBalanceAmount(), 4));
            bean.setInProgressPaymentReleasedAmt(workProgressAbstractReportService.getRoundedOfAmount(
                    bean.getInProgressPaymentReleasedAmt(), 4));
            bean.setInProgressTenderNegotiatedAmt(workProgressAbstractReportService.getRoundedOfAmount(
                    bean.getInProgressTenderNegotiatedAmt(), 4));
            bean.setNotYetStartedWOAmount(workProgressAbstractReportService.getRoundedOfAmount(
                    bean.getNotYetStartedWOAmount(), 4));
            bean.setTakenUpWOAmount(workProgressAbstractReportService.getRoundedOfAmount(bean.getTakenUpWOAmount(), 4));
        }
    }

    @SuppressWarnings("unchecked")
    private void populateContractorClass() {
        final List<Long> contractorIdList = new LinkedList<>();
        for (final ContractorwiseReportBean bean : resultList)
            contractorIdList.add(bean.getContractorId());
        final StringBuffer contractorClassQry = new StringBuffer(" select cd.contractor_id,cg.grade")
                .append(" from egw_contractor_detail cd, egw_contractor_grade cg")
                .append(" where cg.id= cd.contractor_grade_id and cd.contractor_id in (:contractorIdList)")
                .append(" group by cd.contractor_id,cg.grade ");
        final Query sqlQuery = entityManager.createNativeQuery(contractorClassQry.toString())
                .setParameter("contractorIdList", contractorIdList);
        final List<Object[]> resultObjList = sqlQuery.getResultList();
        if (resultObjList != null) {
            final Map<String, String> contractorIdGradeMap = new HashMap<>();
            String concGrade = null;
            for (final Object[] objArr : resultObjList)
                if (objArr[1] != null) {
                    concGrade = contractorIdGradeMap.get(objArr[0].toString());
                    if (concGrade == null)
                        contractorIdGradeMap.put(objArr[0].toString(), objArr[1].toString());
                    else
                        contractorIdGradeMap.put(objArr[0].toString(), concGrade + "," + objArr[1].toString());
                }
            for (final String key : contractorIdGradeMap.keySet())
                for (final ContractorwiseReportBean bean : resultList)
                    if (Long.parseLong(key) == bean.getContractorId().longValue())
                        bean.setContractorClass(contractorIdGradeMap.get(key));
        }
    }

    private void populateTotalRow() {
        final ContractorwiseReportBean totalBean = new ContractorwiseReportBean();
        totalBean.setContractorName("Total");
        for (final ContractorwiseReportBean bean : resultList) {
            totalBean.setBalanceAmount(bean.getBalanceAmount().add(totalBean.getBalanceAmount()));
            totalBean.setBalanceEstimateCount(bean.getBalanceEstimateCount() + totalBean.getBalanceEstimateCount());
            totalBean.setCompletedEstimateCount(bean.getCompletedEstimateCount()
                    + totalBean.getCompletedEstimateCount());
            totalBean.setCompletedWOAmount(bean.getCompletedWOAmount().add(totalBean.getCompletedWOAmount()));
            totalBean.setInProgressBalanceAmount(bean.getInProgressBalanceAmount().add(
                    totalBean.getInProgressBalanceAmount()));
            totalBean.setInProgressEstimateCount(bean.getInProgressEstimateCount()
                    + totalBean.getInProgressEstimateCount());
            totalBean.setInProgressPaymentReleasedAmt(bean.getInProgressPaymentReleasedAmt().add(
                    totalBean.getInProgressPaymentReleasedAmt()));
            totalBean.setInProgressTenderNegotiatedAmt(bean.getInProgressTenderNegotiatedAmt().add(
                    totalBean.getInProgressTenderNegotiatedAmt()));
            totalBean.setNotYetStartedEstimateCount(bean.getNotYetStartedEstimateCount()
                    + totalBean.getNotYetStartedEstimateCount());
            totalBean.setNotYetStartedWOAmount(bean.getNotYetStartedWOAmount()
                    .add(totalBean.getNotYetStartedWOAmount()));
            totalBean.setTakenUpEstimateCount(bean.getTakenUpEstimateCount() + totalBean.getTakenUpEstimateCount());
            totalBean.setTakenUpWOAmount(bean.getTakenUpWOAmount().add(totalBean.getTakenUpWOAmount()));
        }
        resultList.add(totalBean);
    }

    public String generatePDF() {
        searchType = "report";
        search();
        final ReportRequest reportRequest = new ReportRequest(JASPER_NAME, resultList, getParamMap());
        final ReportOutput reportOutput = reportService.createReport(reportRequest);
        if (reportOutput != null && reportOutput.getReportOutputData() != null)
            reportInputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
        return PDF;
    }

    public String generateXLS() {
        searchType = "report";
        search();
        final ReportRequest reportRequest = new ReportRequest(JASPER_NAME, resultList, getParamMap());
        reportRequest.setReportFormat(ReportFormat.XLS);
        final ReportOutput reportOutput = reportService.createReport(reportRequest);
        if (reportOutput != null && reportOutput.getReportOutputData() != null)
            reportInputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
        return XLS;
    }

    private Map<String, Object> getParamMap() {
        final Map<String, Object> reportParams = new HashMap<>();
        reportParams.put("reportTitle", getText("contractorwiseAbstractReport.title"));
        reportParams.put("subHeader", getSubHeader() + "\n" + reportMessage);
        return reportParams;
    }

    private String getTakenUpEstimateDrillDownQuery() {
        Integer index = Integer.valueOf(1);
        final String searchConditions = constructSearchFilter(index);
        final StringBuffer query = new StringBuffer("select ABS_EST_ID,ABS_EST_ESTIMATE_NUMBER,ABS_EST_ESTIMATEDATE,")
                .append("ABS_EST_NAME,ABS_EST_WARD_NAME,ABS_EST_APPROVEDDATE,ABS_EST_VALUE_WITH_OH from EGW_WPREPORT_EST_WO_MVIEW ")
                .append(" where  WORK_ORDER_STATUS_CODE = 'APPROVED' ")
                .append(searchConditions)
                .append(" order by ABS_EST_ESTIMATEDATE ");
        return query.toString();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public String showEstimatesTakenUpDrillDown() {
        Page resPage;
        formReportMessage();
        final String query = getTakenUpEstimateDrillDownQuery();
        final String countQuery = " select count (*) from ( " + query + ")";

        List<CommonDrillDownReportBean> resultBeanList = null;
        if (searchType.equals("")) {
            final SearchQuerySQL sqlQuery = new SearchQuerySQL(query, countQuery, paramList);
            resPage = sqlQuery.getPage(getPersistenceService(), page, pageSize);
            paginatedList = new EgovPaginatedList(resPage, sqlQuery.getCount(getPersistenceService()));
            resultBeanList = generateEstimateList(paginatedList.getList());
            if (paginatedList.getList() == null)
                paginatedList.setList(resultBeanList);
            else {
                paginatedList.getList().clear();
                paginatedList.getList().addAll(resultBeanList);
            }
        } else if (searchType.equalsIgnoreCase("report")) {
            List<Object[]> wholeQueryList = null;
            final Query qry = entityManager.createNativeQuery(query);
            for (int i = 0; i < paramList.size(); i++)
                qry.setParameter(i, paramList.get(i));
            wholeQueryList = qry.getResultList();
            resultBeanList = generateEstimateList(wholeQueryList);
            commonBeanList.addAll(resultBeanList);
        }
        return "showEstimatesForDrillDown";
    }

    private List<CommonDrillDownReportBean> generateEstimateList(final List<Object[]> objArrList) {
        final List<CommonDrillDownReportBean> beanList = new LinkedList<>();
        CommonDrillDownReportBean bean = null;
        BigDecimal amount = null;
        for (final Object[] objArr : objArrList) {
            bean = new CommonDrillDownReportBean();
            bean.setEstimateId(((BigDecimal) objArr[0]).longValue());
            bean.setEstNumber(objArr[1].toString());
            bean.setEstDate((Date) objArr[2]);
            bean.setEstName(objArr[3].toString());
            bean.setWardName(objArr[4].toString());
            bean.setEstApprovedDate((Date) objArr[5]);
            amount = (BigDecimal) objArr[6];
            if (amount != null)
                bean.setEstAmount(new BigDecimal(amount.toString()).setScale(2, RoundingMode.HALF_UP));
            beanList.add(bean);
        }
        return beanList;
    }

    private String constructSearchFilter(Integer index) {
        final StringBuffer queryBfr = new StringBuffer(1000);
        paramList = new LinkedList<>();
        if (fromDate != null && toDate != null) {
            queryBfr.append(" AND TRUNC(WORK_ORDER_APPROVEDDATE) between ?").append(index++)
                    .append(" and ?").append(index++);
            paramList.add(dateFormatter.format(fromDate));
            paramList.add(dateFormatter.format(toDate));
        }
        if (fromDate != null && toDate == null) {
            queryBfr.append(" AND TRUNC(WORK_ORDER_APPROVEDDATE) >= ?").append(index++);
            paramList.add(dateFormatter.format(fromDate));
        }
        if (fromDate == null && toDate != null) {
            queryBfr.append(" AND TRUNC(WORK_ORDER_APPROVEDDATE) <= ?").append(index++);
            paramList.add(dateFormatter.format(toDate));
        }
        if (executingDepartment != null && executingDepartment != -1) {
            queryBfr.append(" AND ABS_EST_EXECUTINGDEPARTMENT = ?").append(index++);
            paramList.add(executingDepartment);
        }
        if (worksType != null && worksType != -1) {
            queryBfr.append(" AND ABS_EST_PARENT_CATEGORY = ?").append(index++);
            paramList.add(worksType);
        }
        if (worksSubType != null && worksSubType != -1) {
            queryBfr.append(" AND ABS_EST_CATEGORY = ?").append(index++);
            paramList.add(worksSubType);
        }
        if (fund != null && fund != -1) {
            queryBfr.append(" AND FIN_DETAILS_FUND_ID = ?").append(index++);
            paramList.add(fund);
        }
        if (function != null && function != -1) {
            queryBfr.append(" AND FIN_DETAILS_FUNCTION_ID = ?").append(index++);
            paramList.add(function);
        }
        if (scheme != null && scheme != -1) {
            queryBfr.append(" AND FIN_DETAILS_SCHEME_ID = ?").append(index++);
            paramList.add(scheme);
        }

        if (subScheme != null && subScheme != -1) {
            queryBfr.append(" AND FIN_DETAILS_SUBSCHEME_ID = ?").append(index++);
            paramList.add(subScheme);
        }
        if (budgetHeads != null && !budgetHeads.isEmpty() && !budgetHeads.contains("-1")
                && !budgetHeads.get(0).equals("") && depositCodeIds != null && !depositCodeIds.isEmpty()) {
            // When both are selected
            queryBfr.append(" AND ( ");
            if (budgetHeads != null && !budgetHeads.isEmpty() && !budgetHeads.contains("-1")
                    && !budgetHeads.get(0).equals("")) {
                queryBfr.append("  FIN_DETAILS_BUDGETGROUP_ID in (");
                for (int i = 0; i < budgetHeadIds.size(); i++) {
                    if (i == 0)
                        queryBfr.append(" ?").append(index++);
                    else
                        queryBfr.append(" ,?").append(index++);
                    paramList.add(budgetHeadIds.get(i));
                }
                queryBfr.append(" ) ");
            }
            if (depositCodeIds != null && !depositCodeIds.isEmpty()) {
                queryBfr.append(" OR FIN_DETAILS_COA_ID in (");
                for (int i = 0; i < depositCodeIds.size(); i++) {
                    if (i == 0)
                        queryBfr.append(" ?").append(index++);
                    else
                        queryBfr.append(" ,?").append(index++);
                    paramList.add(depositCodeIds.get(i));
                }
                queryBfr.append(" ) ");
            }
            queryBfr.append("  ) ");
        } else {
            if (budgetHeads != null && !budgetHeads.isEmpty() && !budgetHeads.contains("-1")
                    && !budgetHeads.get(0).equals("")) {
                queryBfr.append(" AND FIN_DETAILS_BUDGETGROUP_ID in (");
                for (int i = 0; i < budgetHeadIds.size(); i++) {
                    if (i == 0)
                        queryBfr.append(" ?").append(index++);
                    else
                        queryBfr.append(" ,?").append(index++);
                    paramList.add(budgetHeadIds.get(i));
                }
                queryBfr.append(" ) ");
            }
            if (depositCodeIds != null && !depositCodeIds.isEmpty()) {
                queryBfr.append(" AND FIN_DETAILS_COA_ID in (");
                for (int i = 0; i < depositCodeIds.size(); i++) {
                    if (i == 0)
                        queryBfr.append(" ?").append(index++);
                    else
                        queryBfr.append(" ,?").append(index++);
                    paramList.add(depositCodeIds.get(i));
                }
                queryBfr.append(" ) ");
            }
        }
        if (contractorId != null && contractorId != -1 && StringUtils.isNotBlank(contractorName)) {
            queryBfr.append(" AND WORK_ORDER_CONTRACTOR_ID = ?").append(index++);
            paramList.add(contractorId);
        } else if (StringUtils.isNotBlank(contractorName)) {
            queryBfr.append(" AND WORK_ORDER_CONTRACTOR_NAME like ?").append(index++);
            paramList.add("%" + contractorName + "%");
        }
        if (gradeId != null && gradeId != -1) {
            queryBfr.append(" AND WORK_ORDER_CONTRACTOR_ID IN ( SELECT CONTRACTOR_ID FROM EGW_CONTRACTOR_DETAIL")
                    .append(" WHERE CONTRACTOR_GRADE_ID = ?").append(index++)
                    .append(" AND CONTRACTOR_ID = WORK_ORDER_CONTRACTOR_ID)");
            paramList.add(gradeId);
        }
        return queryBfr.toString();
    }

    private void generateBudgetHeads() {
        final List<CChartOfAccounts> coaList = new ArrayList<>();
        final List<BudgetGroup> budgetHeadList = new ArrayList<>();
        // In case all is selected, then should consider all the budget heads
        if (dropDownBudgetHeads.size() == 1 && dropDownBudgetHeads.get(0).equalsIgnoreCase(WorksConstants.ALL)) {
            final String[] budgetHeadsFromAppConf = budgetHeadsAppConfValue.split(",");
            for (final String element : budgetHeadsFromAppConf)
                // Split and obtain only the glcode
                coaList.addAll(chartOfAccountsHibernateDAO.getListOfDetailCode(element.split("-")[0]));
            budgetHeadList.addAll(budgetDetailsDAO.getBudgetHeadForGlcodeList(coaList));
        }
        // Incase all is not selected
        if (!dropDownBudgetHeads.get(0).equalsIgnoreCase(WorksConstants.ALL)) {
            for (int i = 0; i < dropDownBudgetHeads.size(); i++)
                coaList.addAll(chartOfAccountsHibernateDAO.getListOfDetailCode(dropDownBudgetHeads.get(i).split("-")[0]));
            budgetHeadList.addAll(budgetDetailsDAO.getBudgetHeadForGlcodeList(coaList));
        }
        final List<Long> budgetHeadIdsLong = new ArrayList<>();
        final List<String> budgetHeadIdStr = new ArrayList<>();
        if (budgetHeadList != null && budgetHeadList.size() > 0)
            for (final BudgetGroup bdgtGrp : budgetHeadList) {
                budgetHeadIdStr.add(bdgtGrp.getId().toString());
                budgetHeadIdsLong.add(bdgtGrp.getId());
            }
        budgetHeads = budgetHeadIdStr;
        budgetHeadIds = budgetHeadIdsLong;
    }

    private void generateDepositCodes() {
        final List<CChartOfAccounts> coaList = new ArrayList<>();
        if (dropDownDepositCodes.size() == 1 && dropDownDepositCodes.get(0).equalsIgnoreCase(WorksConstants.ALL)) {
            final String[] depositCodesFromAppConf = depositCodesAppConfValue.split(",");
            for (final String element : depositCodesFromAppConf)
                coaList.addAll(chartOfAccountsHibernateDAO.getListOfDetailCode(element.split("-")[0]));
        }
        // Incase all is not selected
        if (!dropDownDepositCodes.get(0).equalsIgnoreCase(WorksConstants.ALL))
            for (int i = 0; i < dropDownDepositCodes.size(); i++)
                coaList.addAll(chartOfAccountsHibernateDAO.getListOfDetailCode(dropDownDepositCodes.get(i).split("-")[0]));
        final List<Long> depositCodeIdsLong = new ArrayList<>();
        if (coaList != null && coaList.size() > 0)
            for (final CChartOfAccounts coa : coaList)
                depositCodeIdsLong.add(coa.getId());
        depositCodeIds = depositCodeIdsLong;
    }

    private void getBudgetHeadsFromAllBudgetHeads() {
        budgetHeadIds = allBudgetHeads;
        budgetHeads = new ArrayList<>();
        for (int i = 0; i < allBudgetHeads.size(); i++)
            budgetHeads.add(allBudgetHeads.get(i).toString());
    }

    private void getDepositCodesFromAllDepositCodes() {
        depositCodeIds = allDepositCodes;
    }

    private String generateFinYrList(final List<CFinancialYear> finYrList) {
        final Date todaysDate = new Date();
        final StringBuffer finStrBfr = new StringBuffer();
        for (final CFinancialYear yr : finYrList)
            if (yr.getStartingDate().compareTo(todaysDate) <= 0 && yr.getEndingDate().compareTo(todaysDate) >= 0)
                finStrBfr.append("id:" + yr.getId() + "--"
                        + DateUtils.getFormattedDate(yr.getStartingDate(), "dd/MM/yyyy") + "--"
                        + DateUtils.getFormattedDate(todaysDate, "dd/MM/yyyy"));
            else
                finStrBfr.append("id:" + yr.getId() + "--"
                        + DateUtils.getFormattedDate(yr.getStartingDate(), "dd/MM/yyyy") + "--"
                        + DateUtils.getFormattedDate(yr.getEndingDate(), "dd/MM/yyyy"));
        return finStrBfr.toString();
    }

    private List<String> getBudgetGroupsFromAppConfig() {
        final List<String> budgetGrpStrList = new ArrayList<>();
        budgetGrpStrList.add(WorksConstants.ALL);
        budgetHeadsAppConfValue = worksService.getWorksConfigValue(BUDGET_HEADS_APPCONFIG_KEY);
        if (budgetHeadsAppConfValue != null)
            for (final String appValues : budgetHeadsAppConfValue.split(","))
                budgetGrpStrList.add(appValues);
        return budgetGrpStrList;
    }

    private List<String> getDepositCodesFromAppConfig() {
        final List<String> glcodesStrList = new ArrayList<>();
        glcodesStrList.add(WorksConstants.ALL);
        depositCodesAppConfValue = worksService.getWorksConfigValue(DEPOSITCODES_APPCONFIG_KEY);
        if (depositCodesAppConfValue != null)
            for (final String appValues : depositCodesAppConfValue.split(","))
                glcodesStrList.add(appValues);
        return glcodesStrList;
    }

    private void populateCategoryList(final AjaxEstimateAction ajaxEstimateAction, final boolean categoryPopulated) {
        if (categoryPopulated) {
            ajaxEstimateAction.setCategory(getWorksType().longValue());
            ajaxEstimateAction.subcategories();
            addDropdownData("worksSubTypeList", ajaxEstimateAction.getSubCategories());
        } else
            addDropdownData("worksSubTypeList", Collections.emptyList());
    }

    private void populateSubSchemeList(final AjaxWorkProgressAction ajaxWorkProgressAction,
            final boolean schemePopulated) {
        if (schemePopulated) {
            ajaxWorkProgressAction.setPersistenceService(getPersistenceService());
            ajaxWorkProgressAction.setSchemeId(getScheme());
            ajaxWorkProgressAction.loadSubSchemes();
            addDropdownData("subSchemeList", ajaxWorkProgressAction.getSubSchemes());
        } else
            addDropdownData("subSchemeList", Collections.emptyList());
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public Integer getExecutingDepartment() {
        return executingDepartment;
    }

    public Integer getWorksType() {
        return worksType;
    }

    public Integer getWorksSubType() {
        return worksSubType;
    }

    public Integer getFund() {
        return fund;
    }

    public Integer getFunction() {
        return function;
    }

    public Integer getScheme() {
        return scheme;
    }

    public Integer getSubScheme() {
        return subScheme;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    public void setExecutingDepartment(final Integer executingDepartment) {
        this.executingDepartment = executingDepartment;
    }

    public void setWorksType(final Integer worksType) {
        this.worksType = worksType;
    }

    public void setWorksSubType(final Integer worksSubType) {
        this.worksSubType = worksSubType;
    }

    public void setFund(final Integer fund) {
        this.fund = fund;
    }

    public void setFunction(final Integer function) {
        this.function = function;
    }

    public void setScheme(final Integer scheme) {
        this.scheme = scheme;
    }

    public void setSubScheme(final Integer subScheme) {
        this.subScheme = subScheme;
    }

    public void setPersonalInformationService(final PersonalInformationService personalInformationService) {
    }

    public String getBudgetHeadsAppConfValue() {
        return budgetHeadsAppConfValue;
    }

    public void setBudgetHeadsAppConfValue(final String budgetHeadsAppConfValue) {
        this.budgetHeadsAppConfValue = budgetHeadsAppConfValue;
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }

    public List<String> getDropDownBudgetHeads() {
        return dropDownBudgetHeads;
    }

    public void setDropDownBudgetHeads(final List<String> dropDownBudgetHeads) {
        this.dropDownBudgetHeads = dropDownBudgetHeads;
    }

    public String getDepositCodesAppConfValue() {
        return depositCodesAppConfValue;
    }

    public void setDepositCodesAppConfValue(final String depositCodesAppConfValue) {
        this.depositCodesAppConfValue = depositCodesAppConfValue;
    }

    public List<String> getDropDownDepositCodes() {
        return dropDownDepositCodes;
    }

    public void setDropDownDepositCodes(final List<String> dropDownDepositCodes) {
        this.dropDownDepositCodes = dropDownDepositCodes;
    }

    public List<Long> getAllDepositCodes() {
        return allDepositCodes;
    }

    public void setAllDepositCodes(final List<Long> allDepositCodes) {
        this.allDepositCodes = allDepositCodes;
    }

    public List<Long> getAllBudgetHeads() {
        return allBudgetHeads;
    }

    public void setAllBudgetHeads(final List<Long> allBudgetHeads) {
        this.allBudgetHeads = allBudgetHeads;
    }

    public String getFinYearId() {
        return finYearId;
    }

    public void setFinYearId(final String finYearId) {
        this.finYearId = finYearId;
    }

    public String getFinYearRange() {
        return finYearRange;
    }

    public void setFinYearRange(final String finYearRange) {
        this.finYearRange = finYearRange;
    }

    public String getCurrentFinancialYearId() {
        return currentFinancialYearId;
    }

    public void setCurrentFinancialYearId(final String currentFinancialYearId) {
        this.currentFinancialYearId = currentFinancialYearId;
    }

    public String getFinYearRangeStr() {
        return finYearRangeStr;
    }

    public String getSubHeader() {
        return subHeader;
    }

    public void setSubHeader(final String subHeader) {
        this.subHeader = subHeader;
    }

    public String getContractorName() {
        return contractorName;
    }

    public Long getContractorId() {
        return contractorId;
    }

    public void setContractorName(final String contractorName) {
        this.contractorName = contractorName;
    }

    public void setContractorId(final Long contractorId) {
        this.contractorId = contractorId;
    }

    public Long getGradeId() {
        return gradeId;
    }

    public void setGradeId(final Long gradeId) {
        this.gradeId = gradeId;
    }

    public List<String> getBudgetHeads() {
        return budgetHeads;
    }

    public void setBudgetHeads(final List<String> budgetHeads) {
        this.budgetHeads = budgetHeads;
    }

    public void setBudgetDetailsDAO(final BudgetDetailsDAO budgetDetailsDAO) {
        this.budgetDetailsDAO = budgetDetailsDAO;
    }

    public List<ContractorwiseReportBean> getResultList() {
        return resultList;
    }

    public void setResultList(final List<ContractorwiseReportBean> resultList) {
        this.resultList = resultList;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public InputStream getReportInputStream() {
        return reportInputStream;
    }

    public void setReportService(final ReportService reportService) {
        this.reportService = reportService;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPage(final Integer page) {
        this.page = page;
    }

    public void setPageSize(final Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<CommonDrillDownReportBean> getCommonBeanList() {
        return commonBeanList;
    }

    public void setWorkProgressAbstractReportService(
            final WorkProgressAbstractReportService workProgressAbstractReportService) {
        this.workProgressAbstractReportService = workProgressAbstractReportService;
    }

    public void setSearchType(final String searchType) {
        this.searchType = searchType;
    }

    public EgovPaginatedList getPaginatedList() {
        return paginatedList;
    }

    public void setPaginatedList(final EgovPaginatedList paginatedList) {
        this.paginatedList = paginatedList;
    }

    public String getBudgetHeadsStr() {
        return budgetHeadsStr;
    }

    public String getDepositCodesStr() {
        return depositCodesStr;
    }

    public String getDepositCodeIdsStr() {
        return depositCodeIdsStr;
    }

    public String getBudgetHeadIdsStr() {
        return budgetHeadIdsStr;
    }

    public void setBudgetHeadsStr(final String budgetHeadsStr) {
        this.budgetHeadsStr = budgetHeadsStr;
    }

    public void setDepositCodesStr(final String depositCodesStr) {
        this.depositCodesStr = depositCodesStr;
    }

    public void setDepositCodeIdsStr(final String depositCodeIdsStr) {
        this.depositCodeIdsStr = depositCodeIdsStr;
    }

    public void setBudgetHeadIdsStr(final String budgetHeadIdsStr) {
        this.budgetHeadIdsStr = budgetHeadIdsStr;
    }

    public String getReportMessage() {
        return reportMessage;
    }
}
