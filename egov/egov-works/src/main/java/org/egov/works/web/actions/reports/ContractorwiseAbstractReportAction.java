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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.service.CommonsService;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.search.SearchQuerySQL;
import org.egov.infstr.services.Page;
import org.egov.infstr.utils.DateUtils;
import org.egov.model.budget.BudgetGroup;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.service.PersonalInformationService;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.utils.EgovPaginatedList;
import org.egov.works.services.WorkProgressAbstractReportService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.web.actions.estimate.AjaxEstimateAction;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;

@Results({
@Result(name=ContractorwiseAbstractReportAction.PDF,type="StreamResult.class",location="reportInputStream", params={"inputName","contractorwiseAbstractReport","contentType","application/pdf","contentDisposition","no-cache;filename=ContractorwiseAbstractReport.pdf"}),
@Result(name=ContractorwiseAbstractReportAction.XLS,type="StreamResult.class",location="reportInputStream", params={"inputName","contractorwiseAbstractReport","contentType","application/xls","contentDisposition","no-cache;filename=ContractorwiseAbstractReport.xls"}),
@Result(name="estimateXLS",type="StreamResult.class",location="reportInputStream", params={"inputName","contractorwiseReport_Estimates","contentType","application/xls","contentDisposition","no-cache;filename=ContractorwiseReport_Estimates.xls"}),
@Result(name="estimatePDF",type="StreamResult.class",location="reportInputStream", params={"inputName","contractorwiseReport_Estimates","contentType","application/pdf","contentDisposition","no-cache;filename=ContractorwiseReport_Estimates.pdf"})
})
@ParentPackage("egov")
public class ContractorwiseAbstractReportAction extends BaseFormAction {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ContractorwiseAbstractReportAction.class);
	private static final String DEPT_WISE="deptwise";
	private String finYearId;
	private String finYearRange;
	private Date fromDate;
	private Date toDate;
	private Integer executingDepartment=-1;
	private Integer worksType=-1;
	private Integer worksSubType=-1;
	private Integer fund=-1;;
	private Integer function=-1;
	private Integer scheme=-1;
	private Integer subScheme=-1;
	@Autowired
        private EmployeeService employeeService;
	private PersonalInformationService personalInformationService;
	@Autowired
        private CommonsService commonsService;
	private static final String BUDGET_HEADS_APPCONFIG_KEY = "WORK_PROGRESS_ABSTRACT_RPT2_BUDGT_HEADS";
	private String budgetHeadsAppConfValue = null;
	private WorksService worksService;
	private List<String> dropDownBudgetHeads; //This contains values from the drop down
	private String depositCodesAppConfValue = null;
	private static final String DEPOSITCODES_APPCONFIG_KEY = "WORK_PROGRESS_ABSTRACT_RPT2_DEPOSIT_CODES";
	private List<String> dropDownDepositCodes;
	private List<Long> allDepositCodes;
	private List<Long> allBudgetHeads; //This contains values from the drop down
	private String currentFinancialYearId;
	private String finYearRangeStr;
	private String subHeader;
	private String contractorName="";
	private Long contractorId;
	private Long	gradeId;
	private List<String> budgetHeads;
	private List<Long> budgetHeadIds;
	private List<Long> depositCodeIds;
	private BudgetDetailsDAO budgetDetailsDAO;
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
	private List<ContractorwiseReportBean> resultList = new  LinkedList<ContractorwiseReportBean>();
	private String resultStatus = "beforeSearch";
	public static final String PDF = "PDF";
	public static final String XLS = "XLS";
	private InputStream reportInputStream;
	private ReportService reportService;
	private String JASPER_NAME = "contractorwiseAbstractReport";
	private String searchType="";
	private EgovPaginatedList paginatedList;
	private Integer page=1;
	private Integer pageSize=30;
	private List<CommonDrillDownReportBean> commonBeanList=new ArrayList<CommonDrillDownReportBean>();
	private List<Object> paramList;
	public WorkProgressAbstractReportService workProgressAbstractReportService;
	private String budgetHeadsStr;
	private String depositCodesStr;
	private String depositCodeIdsStr;
	private String budgetHeadIdsStr;
	private String reportMessage;

	@Override
	public Object getModel() {
		return null;
	}
	
	public String beforeSearch(){ 
		return DEPT_WISE;
	}
	
	@SuppressWarnings("unchecked")
	public void prepare()
	{
		super.prepare();
		List<CFinancialYear> finYrList =worksService.getAllFinancialYearsForWorks();
		addDropdownData("finYearList", finYrList);
		addDropdownData("executingDepartmentList", getPersistenceService().findAllBy("from DepartmentImpl"));
		addDropdownData("worksTypeList", getPersistenceService().findAllBy("from EgwTypeOfWork etw1 where etw1.parentid is null"));
		AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
		ajaxEstimateAction.setPersistenceService(getPersistenceService());
		ajaxEstimateAction.setEmployeeService(employeeService);
		ajaxEstimateAction.setPersonalInformationService(personalInformationService);
		populateCategoryList(ajaxEstimateAction, getWorksType()==-1?false:getWorksType() != -1);
		addDropdownData("fundList", commonsService.getAllActiveIsLeafFunds());
		addDropdownData("functionList", commonsService.getAllFunction());
		addDropdownData("schemeList", getPersistenceService().findAllBy("from org.egov.commons.Scheme sc where sc.isactive=1"));
		AjaxWorkProgressAction ajaxWorkProgressAction = new AjaxWorkProgressAction();
		populateSubSchemeList(ajaxWorkProgressAction, getScheme() != null);
		addDropdownData("budgetHeadList", getBudgetGroupsFromAppConfig());
		addDropdownData("depositCodeList", getDepositCodesFromAppConfig());
		addDropdownData("allDepositCodeCOAList", getPersistenceService().findAllBy("select distinct(fd.coa) from FinancialDetail fd where fd.abstractEstimate in ( select woe.estimate from WorkOrderEstimate woe where woe.workOrder.egwStatus.code='APPROVED' ) order by name  "));
		addDropdownData("allBudgetHeadList", getPersistenceService().findAllBy("select distinct(fd.budgetGroup) from FinancialDetail fd where fd.abstractEstimate in ( select woe.estimate from WorkOrderEstimate woe where woe.workOrder.egwStatus.code='APPROVED' ) order by name  "));
		CFinancialYear financialYear = commonsService.getFinancialYearByDate(new Date());
		if(financialYear!=null) {
			 currentFinancialYearId=financialYear.getId().toString();
		}
		finYearRangeStr = generateFinYrList(finYrList);
		addDropdownData("gradeList", getPersistenceService().findAllBy("from ContractorGrade order by upper(grade)"));
		if(dropDownBudgetHeads!=null && dropDownBudgetHeads.size()>0)
			generateBudgetHeads();
		if(dropDownDepositCodes!=null && dropDownDepositCodes.size()>0)
			generateDepositCodes();
		//All deposit codes and budget heads
		if(allDepositCodes!=null && allDepositCodes.size()>0 )
			getDepositCodesFromAllDepositCodes();
		if(allBudgetHeads!=null && allBudgetHeads.size()>0 )
			getBudgetHeadsFromAllBudgetHeads();
		setBudgetHeadIdsAndDepositCodeIds();
	}
	
	private void setBudgetHeadIdsAndDepositCodeIds() {
		if(StringUtils.isNotBlank(budgetHeadsStr))
		{
			budgetHeads = new ArrayList<String>();
			budgetHeadIds = new ArrayList<Long>();
			workProgressAbstractReportService.setBudgetHeadsFromString(budgetHeadsStr, budgetHeads, budgetHeadIds);
		}
		if(StringUtils.isNotBlank(depositCodesStr))
		{
			depositCodeIds = new ArrayList<Long>();
			workProgressAbstractReportService.setDepositCodesFromString(depositCodesStr, depositCodeIds);
		}
		if(StringUtils.isNotBlank(budgetHeadIdsStr))
		{
			budgetHeads = new ArrayList<String>();
			budgetHeadIds = new ArrayList<Long>();
			workProgressAbstractReportService.setBudgetHeadsFromIdString(budgetHeadIdsStr, budgetHeads, budgetHeadIds);
		}
		if(StringUtils.isNotBlank(depositCodeIdsStr))
		{
			depositCodeIds = new ArrayList<Long>();
			workProgressAbstractReportService.setDepositCodesFromIdString(depositCodeIdsStr, depositCodeIds);
		}
	}
	
	private void formReportMessage()
	{
	 	String dateStr = (String) persistenceService.getSession().createSQLQuery(" select min(VIEW_TIMESTAMP) from EGW_WPREPORT_EST_WO_MVIEW ").list().get(0);
	 	reportMessage = getText("contractorwiseAbstractReport.data.message",new String[]{dateStr});
	}
	
	public String search(){
		resultStatus = "afterSearch";
		formReportMessage();
		String commonSearchFilter = constructSearchFilter();
		Map<String, String> queryMap = generateQuery(commonSearchFilter);
		String query = queryMap.get("query");
		String countQuery = queryMap.get("count");
		Page resPage;
		List<Object> listWithRepeatedPramsForUnion = new LinkedList();
		listWithRepeatedPramsForUnion.addAll( paramList);
		listWithRepeatedPramsForUnion.addAll( paramList);
		listWithRepeatedPramsForUnion.addAll( paramList);
		listWithRepeatedPramsForUnion.addAll( paramList);
		if(searchType.equals("")) {
			SearchQuerySQL sqlQuery = new SearchQuerySQL(query, countQuery, listWithRepeatedPramsForUnion);
			resPage=sqlQuery.getPage(getPersistenceService(), page, pageSize);  
			paginatedList = new EgovPaginatedList(resPage, sqlQuery.getCount(getPersistenceService()));
			populateData(paginatedList.getList());
			if(paginatedList.getList()==null)
				paginatedList.setList(resultList);
			else
			{
				paginatedList.getList().clear();
				paginatedList.getList().addAll(resultList);
			}
		}
		else if(searchType.equalsIgnoreCase("report")) {
			List<Object[]> wholeQueryList = null; 
			Query qry = getPersistenceService().getSession().createSQLQuery(query);
			for(int i =0;i< listWithRepeatedPramsForUnion.size();i++)
			{
				qry.setParameter(i,listWithRepeatedPramsForUnion.get(i));
			}
			wholeQueryList = qry.list();
			populateData(wholeQueryList);
		}
		return DEPT_WISE;
	}
	
	public String viewEstimatePDF() {
		setSearchType("report");
		showEstimatesTakenUpDrillDown();
		Map<String, Object> reportParams = new HashMap<String, Object>();
		reportParams.put("subHeader", subHeader+"\n"+reportMessage);
		reportParams.put("mainHeader", getText("contractorwiseAbstractReport.showTakenUpEst")+getContractorName());
		
		ReportRequest reportRequest = new ReportRequest("contractorwiseReport_Estimates",commonBeanList, reportParams);
		ReportOutput reportOutput = reportService.createReport(reportRequest);
		if (reportOutput != null && reportOutput.getReportOutputData() != null)
			reportInputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
		
		return "estimatePDF";
	}
	
	public String viewEstimateXLS() {
		setSearchType("report");
		showEstimatesTakenUpDrillDown();
		Map<String, Object> reportParams = new HashMap<String, Object>();
		reportParams.put("subHeader", subHeader+"\n"+reportMessage);
		
		reportParams.put("mainHeader", getText("contractorwiseAbstractReport.showTakenUpEst")+getContractorName());
		ReportRequest reportRequest = new ReportRequest("contractorwiseReport_Estimates",commonBeanList, reportParams);
		reportRequest.setReportFormat(FileFormat.XLS);
		ReportOutput reportOutput = reportService.createReport(reportRequest);
		if (reportOutput != null && reportOutput.getReportOutputData() != null)
			reportInputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
		
		return "estimateXLS";
	}
	
	private String getTakenUpQuery(String commonSearchCriteriaQueryStr, String finalBillType)
	{
		StringBuffer query = new StringBuffer(" SELECT WORK_ORDER_CONTRACTOR_ID,WORK_ORDER_CONTRACTOR_NAME,WORK_ORDER_CONTRACTOR_CODE, " +
				" SUM(EST_COUNT) TAKEN_UP_EST_COUNT ,SUM(NVL(WORK_ORDER_WORKORDER_AMOUNT,0)) TAKEN_UP_WO_AMOUNT, " +
				" 0 COMPLETED_EST_COUNT,0 COMPLETED_PYMT_RLSD,0 IN_PROGRESS_EST_COUNT,0 IN_PROGRESS_NEGO_VAL ,0 IN_PROGRESS_PYMT_RLSD,0 NOT_STARTED_EST_COUNT,0 NOT_STARTED_WO_AMT," +
				" SUM(NVL(REV_WO_TAKEN_UP_AMT,0)) REV_WO_TAKEN_UP_AMT_SUM, 0 REV_WO_IN_PROGRESS_NEG_VAL_SUM, 0 REV_WO_NOT_STARTED_AMT_SUM " +
				" FROM (select WORK_ORDER_CONTRACTOR_ID , WORK_ORDER_CONTRACTOR_NAME, " +
				" WORK_ORDER_CONTRACTOR_CODE, COUNT(DISTINCT(ABS_EST_ID)) EST_COUNT , WORK_ORDER_ID, WORK_ORDER_WORKORDER_AMOUNT,  " +
				" (SELECT SUM(NVL(INNER_VIEW.WORK_ORDER_WORKORDER_AMOUNT,0)) FROM EGW_WPREPORT_EST_WO_MVIEW INNER_VIEW WHERE INNER_VIEW.WORK_ORDER_PARENTID = OUTER_VIEW.WORK_ORDER_ID) REV_WO_TAKEN_UP_AMT " +
				" from EGW_WPREPORT_EST_WO_MVIEW OUTER_VIEW ");
		query.append(" where ABS_EST_PARENTID IS NULL AND WORK_ORDER_STATUS_CODE = 'APPROVED' " +commonSearchCriteriaQueryStr+
				" group by WORK_ORDER_CONTRACTOR_ID, WORK_ORDER_CONTRACTOR_NAME , " +
				" WORK_ORDER_CONTRACTOR_CODE,WORK_ORDER_ID,WORK_ORDER_WORKORDER_AMOUNT ) " +
				" group by WORK_ORDER_CONTRACTOR_ID, WORK_ORDER_CONTRACTOR_NAME , WORK_ORDER_CONTRACTOR_CODE ");
		return query.toString();
	}
	
	private String getCompletedQuery(String commonSearchCriteriaQueryStr, String finalBillType)
	{
		StringBuffer query = new StringBuffer("");
		query.append(" SELECT WORK_ORDER_CONTRACTOR_ID,");
		query.append("  WORK_ORDER_CONTRACTOR_NAME ,");
		query.append("  WORK_ORDER_CONTRACTOR_CODE, 0 TAKEN_UP_EST_COUNT  ,0 TAKEN_UP_WO_AMOUNT,");
		query.append("  SUM(EST_COUNT) COMPLETED_EST_COUNT ,");
		query.append("  SUM(PYMT_RELEASED_AMT) COMPLETED_PYMT_RLSD , 0 IN_PROGRESS_EST_COUNT,0 IN_PROGRESS_NEGO_VAL,0 IN_PROGRESS_PYMT_RLSD,0 NOT_STARTED_EST_COUNT,0 NOT_STARTED_WO_AMT, " +
				"0 REV_WO_TAKEN_UP_AMT_SUM, 0 REV_WO_IN_PROGRESS_NEG_VAL_SUM, 0 REV_WO_NOT_STARTED_AMT_SUM ");
		query.append(" FROM");
		query.append("  (SELECT WORK_ORDER_CONTRACTOR_ID ,");
		query.append("    WORK_ORDER_CONTRACTOR_NAME ,");
		query.append("    WORK_ORDER_CONTRACTOR_CODE,");
		query.append("    WORK_ORDER_ID,");
		query.append("    COUNT(DISTINCT(ABS_EST_ID)) EST_COUNT ,");
		query.append("    (SELECT SUM(NVL(bill_view.PMT_RLSD_TO_CONTRACTOR_FOR_WO,0))");
		query.append("    FROM EGW_WPREPORT_EST_BILLS_MVIEW bill_view");
		query.append("    WHERE bill_view.BILL_REG_BILLTYPE='"+finalBillType+"'");
		query.append("    AND bill_view.WORK_ORDER_ID      =WO_VIEW.WORK_ORDER_ID");
		query.append("    AND bill_view.BILL_REG_BILLSTATUS='APPROVED'");
		query.append("    ) AS PYMT_RELEASED_AMT");
		query.append("  FROM EGW_WPREPORT_EST_WO_MVIEW WO_VIEW");
		query.append("  WHERE WORK_ORDER_STATUS_CODE = 'APPROVED'");
		query.append("  AND WORK_ORDER_ID           IN");
		query.append("    (SELECT bill_view.WORK_ORDER_ID");
		query.append("    FROM EGW_WPREPORT_EST_BILLS_MVIEW bill_view");
		query.append("    WHERE bill_view.BILL_REG_BILLTYPE='"+finalBillType+"'");
		query.append("    AND bill_view.WORK_ORDER_ID      =WO_VIEW.WORK_ORDER_ID");
		query.append("    AND bill_view.BILL_REG_BILLSTATUS='APPROVED'");
		query.append("    )");
		query.append("  AND ABS_EST_PARENTID IS NULL   "+commonSearchCriteriaQueryStr);
		query.append("  GROUP BY WORK_ORDER_CONTRACTOR_ID ,");
		query.append("    WORK_ORDER_CONTRACTOR_NAME ,");
		query.append("    WORK_ORDER_CONTRACTOR_CODE,");
		query.append("    WORK_ORDER_ID");
		query.append("  )");
		query.append(" GROUP BY WORK_ORDER_CONTRACTOR_ID,");
		query.append("  WORK_ORDER_CONTRACTOR_NAME ,");
		query.append("  WORK_ORDER_CONTRACTOR_CODE");

		return query.toString();
	}

	private String getInProgressQuery(String commonSearchCriteriaQueryStr, String finalBillType)
	{
		StringBuffer query = new StringBuffer("");
		query.append(" SELECT WORK_ORDER_CONTRACTOR_ID , WORK_ORDER_CONTRACTOR_NAME,");
		query.append(" WORK_ORDER_CONTRACTOR_CODE, 0 TAKEN_UP_EST_COUNT  ,0 TAKEN_UP_WO_AMOUNT, 0 COMPLETED_EST_COUNT,0 COMPLETED_PYMT_RLSD, SUM(EST_COUNT) IN_PROGRESS_EST_COUNT, ");
		query.append(" SUM(NVL(TENDER_RESP_NEGOTIATED_VALUE,0)) IN_PROGRESS_NEGO_VAL , SUM(PYMT_RELEASED_AMT) IN_PROGRESS_PYMT_RLSD , 0 NOT_STARTED_EST_COUNT,0 NOT_STARTED_WO_AMT," +
				" 0 REV_WO_TAKEN_UP_AMT_SUM, SUM(NVL(REV_WO_IN_PROGRESS_NEG_VAL,0)) REV_WO_IN_PROGRESS_NEG_VAL_SUM,0 REV_WO_NOT_STARTED_AMT_SUM  ");
		query.append(" FROM ");
		query.append(" (");
		query.append(" SELECT WORK_ORDER_CONTRACTOR_ID ,");
		query.append("  WORK_ORDER_CONTRACTOR_NAME ,");
		query.append("  WORK_ORDER_CONTRACTOR_CODE ,");
		query.append("  WORK_ORDER_ID,");
		query.append("  COUNT(DISTINCT(ABS_EST_ID)) EST_COUNT ,");
		query.append("  TENDER_RESP_NEGOTIATED_VALUE,");
		query.append("    (SELECT SUM(NVL(BILL_VIEW.PMT_RLSD_TO_CONTRACTOR_FOR_WO,0))");
		query.append("    FROM EGW_WPREPORT_EST_BILLS_MVIEW BILL_VIEW");
		query.append("    WHERE BILL_VIEW.WORK_ORDER_ID      =EST_VIEW.WORK_ORDER_ID");
		query.append("    AND BILL_VIEW.BILL_REG_BILLSTATUS='APPROVED'");
		query.append("    ) AS PYMT_RELEASED_AMT, " +
				" (SELECT SUM(NVL(INNER_VIEW.WORK_ORDER_WORKORDER_AMOUNT,0)) FROM EGW_WPREPORT_EST_WO_MVIEW INNER_VIEW WHERE INNER_VIEW.WORK_ORDER_PARENTID = EST_VIEW.WORK_ORDER_ID) REV_WO_IN_PROGRESS_NEG_VAL ");
		query.append(" FROM EGW_WPREPORT_EST_WO_MVIEW EST_VIEW");
		query.append(" WHERE WORK_ORDER_STATUS_CODE      = 'APPROVED'");
		query.append(" AND WO_LATEST_OFFLINE_STATUS_CODE = '"+WorksConstants.WO_STATUS_WOCOMMENCED+"'");
		query.append(" AND '"+finalBillType+"' NOT      IN");
		query.append("  (SELECT DISTINCT(BILL_VIEW.BILL_REG_BILLTYPE)");
		query.append("  FROM EGW_WPREPORT_EST_BILLS_MVIEW BILL_VIEW");
		query.append("  WHERE BILL_VIEW.WORK_ORDER_ID    = EST_VIEW.WORK_ORDER_ID");
		query.append("  AND BILL_VIEW.BILL_REG_BILLSTATUS='APPROVED'");
		query.append("  )");
		query.append(" AND EST_VIEW.TENDER_RESP_STATUS_CODE='APPROVED' ");
		query.append("  AND ABS_EST_PARENTID IS NULL   "+commonSearchCriteriaQueryStr);
		query.append(" GROUP BY WORK_ORDER_CONTRACTOR_ID ,");
		query.append("  WORK_ORDER_CONTRACTOR_NAME ,");
		query.append("  WORK_ORDER_CONTRACTOR_CODE,");
		query.append("  WORK_ORDER_ID,");
		query.append("  TENDER_RESP_NEGOTIATED_VALUE");
		query.append(" )");
		query.append(" GROUP BY WORK_ORDER_CONTRACTOR_ID ,");
		query.append("  WORK_ORDER_CONTRACTOR_NAME ,");
		query.append("  WORK_ORDER_CONTRACTOR_CODE");
		return query.toString();
	}

	private String getNotYetStartedQuery(String commonSearchCriteriaQueryStr, String finalBillType)
	{
		StringBuffer query = new StringBuffer("");
		query.append(" SELECT WORK_ORDER_CONTRACTOR_ID, WORK_ORDER_CONTRACTOR_NAME,");
		query.append(" WORK_ORDER_CONTRACTOR_CODE, " +
					 " 0 TAKEN_UP_EST_COUNT  ,0 TAKEN_UP_WO_AMOUNT, 0 COMPLETED_EST_COUNT,0 COMPLETED_PYMT_RLSD, "+
					 " 0 IN_PROGRESS_EST_COUNT, 0 IN_PROGRESS_NEGO_VAL, 0 IN_PROGRESS_PYMT_RLSD, ");
		query.append(" SUM(EST_COUNT) NOT_STARTED_EST_COUNT, SUM(NVL(WORK_ORDER_WORKORDER_AMOUNT,0)) NOT_STARTED_WO_AMT," +
				" 0 REV_WO_TAKEN_UP_AMT_SUM,0 REV_WO_IN_PROGRESS_NEG_VAL_SUM, SUM(NVL(REV_WO_NOT_STARTED_AMT,0)) REV_WO_NOT_STARTED_AMT_SUM ");
		query.append(" FROM(");
		query.append(" SELECT WORK_ORDER_CONTRACTOR_ID ,");
		query.append("  WORK_ORDER_CONTRACTOR_NAME ,");
		query.append("  WORK_ORDER_CONTRACTOR_CODE ,");
		query.append("  WORK_ORDER_ID,");
		query.append("  COUNT(DISTINCT(ABS_EST_ID)) EST_COUNT,");
		query.append("  WORK_ORDER_WORKORDER_AMOUNT," +
				" (SELECT SUM(NVL(INNER_VIEW.WORK_ORDER_WORKORDER_AMOUNT,0)) FROM EGW_WPREPORT_EST_WO_MVIEW INNER_VIEW WHERE INNER_VIEW.WORK_ORDER_PARENTID = OUTER_VIEW.WORK_ORDER_ID) REV_WO_NOT_STARTED_AMT ");
		query.append(" FROM EGW_WPREPORT_EST_WO_MVIEW OUTER_VIEW ");
		query.append(" WHERE (WO_LATEST_OFFLINE_STATUS_CODE!='" +WorksConstants.WO_STATUS_WOCOMMENCED+"'");
		query.append(" OR WO_LATEST_OFFLINE_STATUS_CODE    IS NULL)");
		query.append(" AND WORK_ORDER_STATUS_CODE           = 'APPROVED' ");
		query.append(" AND ABS_EST_PARENTID IS NULL   "+commonSearchCriteriaQueryStr);
		query.append(" GROUP BY WORK_ORDER_CONTRACTOR_ID , ");
		query.append(" WORK_ORDER_CONTRACTOR_NAME ,");
		query.append(" WORK_ORDER_CONTRACTOR_CODE,");
		query.append(" WORK_ORDER_ID,");
		query.append(" WORK_ORDER_WORKORDER_AMOUNT ");
		query.append(" )");
		query.append(" GROUP BY WORK_ORDER_CONTRACTOR_ID , ");
		query.append(" WORK_ORDER_CONTRACTOR_NAME ,");
		query.append(" WORK_ORDER_CONTRACTOR_CODE");
		return query.toString();
	}
	
	private Map<String, String> generateQuery(String commonSearchCriteriaQueryStr) {
		String finalBillType = worksService.getWorksConfigValue("FinalBillType");
		String getTakenUpQuery = getTakenUpQuery(commonSearchCriteriaQueryStr,finalBillType);
		String getCompletedQuery = getCompletedQuery(commonSearchCriteriaQueryStr,finalBillType);
		String getInProgressQuery = getInProgressQuery(commonSearchCriteriaQueryStr,finalBillType);
		String getNotYetStartedQuery = getNotYetStartedQuery(commonSearchCriteriaQueryStr,finalBillType);
		
		String mainQuery =" select WORK_ORDER_CONTRACTOR_ID, WORK_ORDER_CONTRACTOR_NAME, WORK_ORDER_CONTRACTOR_CODE, "+
				" SUM(TAKEN_UP_EST_COUNT),SUM(TAKEN_UP_WO_AMOUNT), "+
				" SUM(COMPLETED_EST_COUNT),SUM(COMPLETED_PYMT_RLSD),"+
				" SUM(IN_PROGRESS_EST_COUNT),SUM(IN_PROGRESS_NEGO_VAL),"+
				" SUM(IN_PROGRESS_PYMT_RLSD),SUM(NOT_STARTED_EST_COUNT),"+
				" SUM(NOT_STARTED_WO_AMT), SUM(NVL(REV_WO_TAKEN_UP_AMT_SUM,0))," +
				" SUM(NVL(REV_WO_IN_PROGRESS_NEG_VAL_SUM,0))," +
				" SUM(NVL(REV_WO_NOT_STARTED_AMT_SUM,0)) FROM ( "  +
				getTakenUpQuery  + " UNION " + getCompletedQuery + " UNION " + getInProgressQuery + " UNION " +getNotYetStartedQuery
				+" ) GROUP BY WORK_ORDER_CONTRACTOR_ID, WORK_ORDER_CONTRACTOR_NAME,WORK_ORDER_CONTRACTOR_CODE ";
		
		Map returnMap = new HashMap<String,String>();
		returnMap.put("query", mainQuery.toString()  );
		returnMap.put("count","Select count (*) from ("+mainQuery.toString()+ ")" );
		
		return returnMap;
	}

	private void populateData(List<Object[]> objResultList) {
		if(objResultList!=null && objResultList.size()>0)
		{
			populateBeanData(objResultList);
			populateBalanceInfo();
			populateContractorClass();
			if(searchType.equalsIgnoreCase("report"))
				populateTotalRow();
			convertAllAmountsToCrores();
		}
	}
	
	private void populateBeanData(List<Object[]> objResultList)
	{
		ContractorwiseReportBean bean = null;
		for(Object[] objArr:objResultList )
		{
			bean = new ContractorwiseReportBean();
			if(objArr[0]!=null)
				bean.setContractorId(((BigDecimal)objArr[0]).longValue());
			if(objArr[1]!=null)
				bean.setContractorName(objArr[1].toString());
			if(objArr[2]!=null)
				bean.setContractorCode(objArr[2].toString());
			if(objArr[3]!=null)
				bean.setTakenUpEstimateCount(((BigDecimal)objArr[3]).intValue());
			if(objArr[4]!=null)
				bean.setTakenUpWOAmount((BigDecimal)objArr[4]);
			if(objArr[5]!=null)
				bean.setCompletedEstimateCount(((BigDecimal)objArr[5]).intValue());
			if(objArr[6]!=null)
				bean.setCompletedWOAmount((BigDecimal)objArr[6]);
			if(objArr[7]!=null)
				bean.setInProgressEstimateCount(((BigDecimal)objArr[7]).intValue());
			if(objArr[8]!=null)
				bean.setInProgressTenderNegotiatedAmt((BigDecimal)objArr[8]);
			if(objArr[9]!=null)
				bean.setInProgressPaymentReleasedAmt((BigDecimal)objArr[9]);
			if(objArr[10]!=null)
				bean.setNotYetStartedEstimateCount(((BigDecimal)objArr[10]).intValue());
			if(objArr[11]!=null)
				bean.setNotYetStartedWOAmount((BigDecimal)objArr[11]);
			if(objArr[12]!=null)
				bean.setTakenUpWOAmount(bean.getTakenUpWOAmount().add((BigDecimal)objArr[12]));
			if(objArr[13]!=null)
				bean.setInProgressTenderNegotiatedAmt(bean.getInProgressTenderNegotiatedAmt().add((BigDecimal)objArr[13]));
			if(objArr[14]!=null)
				bean.setNotYetStartedWOAmount(bean.getNotYetStartedWOAmount().add((BigDecimal)objArr[14]));
			bean.setInProgressBalanceAmount(bean.getInProgressTenderNegotiatedAmt().subtract(bean.getInProgressPaymentReleasedAmt()));
			
			resultList.add(bean);
		}
	}
	
	private void populateBalanceInfo(){
		
		for(ContractorwiseReportBean bean:resultList)
		{
			bean.setBalanceAmount(bean.getInProgressTenderNegotiatedAmt().add(bean.getNotYetStartedWOAmount()));
			bean.setBalanceEstimateCount(bean.getInProgressEstimateCount()+bean.getNotYetStartedEstimateCount());
		}
	}
	
	private void convertAllAmountsToCrores()
	{
		for(ContractorwiseReportBean bean:resultList)
		{
			bean.setBalanceAmount(workProgressAbstractReportService.getRoundedOfAmount(bean.getBalanceAmount(), 4));
			bean.setCompletedWOAmount(workProgressAbstractReportService.getRoundedOfAmount(bean.getCompletedWOAmount(), 4));
			bean.setInProgressBalanceAmount(workProgressAbstractReportService.getRoundedOfAmount(bean.getInProgressBalanceAmount(), 4));
			bean.setInProgressPaymentReleasedAmt(workProgressAbstractReportService.getRoundedOfAmount(bean.getInProgressPaymentReleasedAmt(), 4));
			bean.setInProgressTenderNegotiatedAmt(workProgressAbstractReportService.getRoundedOfAmount(bean.getInProgressTenderNegotiatedAmt(), 4));
			bean.setNotYetStartedWOAmount(workProgressAbstractReportService.getRoundedOfAmount(bean.getNotYetStartedWOAmount(), 4));
			bean.setTakenUpWOAmount(workProgressAbstractReportService.getRoundedOfAmount(bean.getTakenUpWOAmount(), 4));
		}
	}

	private void populateContractorClass() {
		List<Long> contractorIdList = new LinkedList<Long>();
		for(ContractorwiseReportBean bean:resultList)
			contractorIdList.add(bean.getContractorId());
		String contractorClassQry = " select cd.contractor_id,cg.grade from egw_contractor_detail cd, " +
				"  egw_contractor_grade cg where cg.id= cd.contractor_grade_id and cd.contractor_id in (:contractorIdList) group by cd.contractor_id,cg.grade ";
		Query sqlQuery = getPersistenceService().getSession().createSQLQuery(contractorClassQry);
		sqlQuery.setParameterList("contractorIdList",contractorIdList);
		List<Object[]> resultObjList = sqlQuery.list();
		if(resultObjList!=null)
		{
			Map<String,String> contractorIdGradeMap = new HashMap<String, String>();
			String concGrade = null;
			for(Object[] objArr:resultObjList)
			{
				if(objArr[1]!=null)
				{
					concGrade = contractorIdGradeMap.get(objArr[0].toString());
					if(concGrade==null)
						contractorIdGradeMap.put(objArr[0].toString(), objArr[1].toString());
					else
						contractorIdGradeMap.put(objArr[0].toString(), concGrade+","+objArr[1].toString());
				}
			}
			for(String key:contractorIdGradeMap.keySet())
			{
				for(ContractorwiseReportBean bean:resultList)
				{
					if(Long.parseLong(key)==bean.getContractorId().longValue())
						bean.setContractorClass(contractorIdGradeMap.get(key));
				}
			}
		}
	}

	private void populateTotalRow() {
		ContractorwiseReportBean totalBean = new ContractorwiseReportBean();
		totalBean.setContractorName("Total");
		for(ContractorwiseReportBean bean:resultList)
		{
			totalBean.setBalanceAmount(bean.getBalanceAmount().add(totalBean.getBalanceAmount()));
			totalBean.setBalanceEstimateCount(bean.getBalanceEstimateCount()+totalBean.getBalanceEstimateCount());
			totalBean.setCompletedEstimateCount(bean.getCompletedEstimateCount()+totalBean.getCompletedEstimateCount());
			totalBean.setCompletedWOAmount(bean.getCompletedWOAmount().add(totalBean.getCompletedWOAmount()));
			totalBean.setInProgressBalanceAmount(bean.getInProgressBalanceAmount().add(totalBean.getInProgressBalanceAmount()));
			totalBean.setInProgressEstimateCount(bean.getInProgressEstimateCount()+totalBean.getInProgressEstimateCount());
			totalBean.setInProgressPaymentReleasedAmt(bean.getInProgressPaymentReleasedAmt().add(totalBean.getInProgressPaymentReleasedAmt()));
			totalBean.setInProgressTenderNegotiatedAmt(bean.getInProgressTenderNegotiatedAmt().add(totalBean.getInProgressTenderNegotiatedAmt()));
			totalBean.setNotYetStartedEstimateCount(bean.getNotYetStartedEstimateCount()+totalBean.getNotYetStartedEstimateCount());
			totalBean.setNotYetStartedWOAmount(bean.getNotYetStartedWOAmount().add(totalBean.getNotYetStartedWOAmount()));
			totalBean.setTakenUpEstimateCount(bean.getTakenUpEstimateCount()+totalBean.getTakenUpEstimateCount());
			totalBean.setTakenUpWOAmount(bean.getTakenUpWOAmount().add(totalBean.getTakenUpWOAmount()));
		}
		resultList.add(totalBean);
	}
	
	public String generatePDF() {
		searchType="report";
		search();
		ReportRequest reportRequest = new ReportRequest(JASPER_NAME, resultList, getParamMap());
		ReportOutput reportOutput = reportService.createReport(reportRequest);
		if (reportOutput != null && reportOutput.getReportOutputData() != null)
			reportInputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
		return PDF;
	}

	public String generateXLS() {
		searchType="report";
		search();
		ReportRequest reportRequest = new ReportRequest(JASPER_NAME,resultList, getParamMap());
		reportRequest.setReportFormat(FileFormat.XLS);
		ReportOutput reportOutput = reportService.createReport(reportRequest);
		if (reportOutput != null && reportOutput.getReportOutputData() != null)
			reportInputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
		return XLS;
	}
	
	private Map<String, Object> getParamMap() {
		Map<String, Object> reportParams = new HashMap<String, Object>();
		reportParams.put("reportTitle", getText("contractorwiseAbstractReport.title"));
		reportParams.put("subHeader", getSubHeader()+"\n"+reportMessage);
		return reportParams;
	}

	private String getTakenUpEstimateDrillDownQuery()
	{
		String searchConditions = constructSearchFilter();
		StringBuffer query = new StringBuffer(" select ABS_EST_ID,ABS_EST_ESTIMATE_NUMBER,ABS_EST_ESTIMATEDATE,ABS_EST_NAME,ABS_EST_WARD_NAME,ABS_EST_APPROVEDDATE,ABS_EST_VALUE_WITH_OH from EGW_WPREPORT_EST_WO_MVIEW ");
		query.append(" where  WORK_ORDER_STATUS_CODE = 'APPROVED' " +searchConditions+
				" order by ABS_EST_ESTIMATEDATE ");
		return query.toString();
	}
	
	public String showEstimatesTakenUpDrillDown()
	{
		Page resPage;
		formReportMessage();
		String query = getTakenUpEstimateDrillDownQuery();
		String countQuery = " select count (*) from ( " +query+")";
		
		List<CommonDrillDownReportBean> resultBeanList =  null;
		if(searchType.equals("")) {
			SearchQuerySQL sqlQuery = new SearchQuerySQL(query, countQuery, paramList);
			resPage=sqlQuery.getPage(getPersistenceService(), page, pageSize);  
			paginatedList = new EgovPaginatedList(resPage, sqlQuery.getCount(getPersistenceService()));
			resultBeanList = generateEstimateList(paginatedList.getList());
			if(paginatedList.getList()==null)
			{
				paginatedList.setList(resultBeanList);
			}
			else
			{
				paginatedList.getList().clear();
				paginatedList.getList().addAll(resultBeanList);
			}
		}
		else if(searchType.equalsIgnoreCase("report")) {
			List<Object[]> wholeQueryList = null; 
			Query qry = getPersistenceService().getSession().createSQLQuery(query);
			for(int i =0;i< paramList.size();i++)
			{
				qry.setParameter(i,paramList.get(i));
			}
			wholeQueryList = qry.list();
			resultBeanList = generateEstimateList(wholeQueryList);
			commonBeanList.addAll(resultBeanList );
		}
		return "showEstimatesForDrillDown";
	}
	
	private List<CommonDrillDownReportBean> generateEstimateList(List<Object[]> objArrList)
	{
		List<CommonDrillDownReportBean> beanList = new LinkedList<CommonDrillDownReportBean>();
		CommonDrillDownReportBean bean = null;
		BigDecimal amount = null;
		for(Object[] objArr:objArrList)
		{
			bean = new CommonDrillDownReportBean();
			bean.setEstimateId(((BigDecimal)objArr[0]).longValue());
			bean.setEstNumber(objArr[1].toString());
			bean.setEstDate((Date) objArr[2]);
			bean.setEstName(objArr[3].toString());
			bean.setWardName(objArr[4].toString());
			bean.setEstApprovedDate((Date) objArr[5]);
			amount = (BigDecimal)objArr[6];
			if(amount!=null)
				bean.setEstAmount(new BigDecimal(amount.toString()).setScale(2, RoundingMode.HALF_UP));	
			beanList.add(bean);
		}
		return beanList;
	}
	
	private String constructSearchFilter() {
		StringBuffer queryBfr = new StringBuffer(1000);
		paramList = new LinkedList<Object>();
		if(fromDate!=null && toDate!=null){
			queryBfr.append(" AND TRUNC(WORK_ORDER_APPROVEDDATE) between '"+dateFormatter.format(fromDate)+"' and '"+dateFormatter.format(toDate)+"' ");
		}
		if(fromDate!=null && toDate==null){
			queryBfr.append(" AND TRUNC(WORK_ORDER_APPROVEDDATE) >= '"+dateFormatter.format(fromDate)+"' ");
		}
		if(fromDate==null && toDate!=null){
			queryBfr.append(" AND TRUNC(WORK_ORDER_APPROVEDDATE) <= '"+dateFormatter.format(toDate)+"' ");
		}
		if(executingDepartment!=null && executingDepartment!=-1)
		{
			queryBfr.append(" AND ABS_EST_EXECUTINGDEPARTMENT=?");
			paramList.add( executingDepartment);
		}
		if(worksType!=null && worksType!= -1){
			queryBfr.append(" AND ABS_EST_PARENT_CATEGORY=?");
			paramList.add(worksType);
		}
		if(worksSubType!=null && worksSubType!= -1){
			queryBfr.append(" AND ABS_EST_CATEGORY=?");
			paramList.add(worksSubType);
		}
		if(fund!=null && fund!=-1){
			queryBfr.append(" AND FIN_DETAILS_FUND_ID=? ");
			paramList.add( fund);
		}
		if(function!=null && function!=-1){
			queryBfr.append(" AND FIN_DETAILS_FUNCTION_ID=?");
			paramList.add( function);
		}
		if(scheme!=null && scheme!= -1){
			queryBfr.append(" AND FIN_DETAILS_SCHEME_ID=?");
			paramList.add( scheme);
		}
		
		if(subScheme!=null && subScheme!= -1){
			queryBfr.append(" AND FIN_DETAILS_SUBSCHEME_ID=?");
			paramList.add( subScheme);
		}
		if((budgetHeads!=null && !budgetHeads.isEmpty() && !budgetHeads.contains("-1") 
				&& !budgetHeads.get(0).equals("")) && (depositCodeIds!=null && !depositCodeIds.isEmpty()))
		{
			//When both are selected
			queryBfr.append(" AND ( ");
			if(budgetHeads!=null && !budgetHeads.isEmpty() && !budgetHeads.contains("-1") 
					&& !budgetHeads.get(0).equals("")){
				queryBfr.append("  FIN_DETAILS_BUDGETGROUP_ID in (");
				for(int i =0; i <budgetHeadIds.size();i++)
				{
					if(i==0)
						queryBfr.append(" ?");
					else
						queryBfr.append(" ,? ");
					paramList.add(budgetHeadIds.get(i));
				}
				queryBfr.append(" ) ");
			} 
			if(depositCodeIds!=null && !depositCodeIds.isEmpty()){
				queryBfr.append(" OR FIN_DETAILS_COA_ID in (");
				for(int i =0; i <depositCodeIds.size();i++)
				{
					if(i==0)
						queryBfr.append(" ?");
					else
						queryBfr.append(" ,? ");
					paramList.add(depositCodeIds.get(i));
				}
				queryBfr.append(" ) ");
			}
			queryBfr.append("  ) ");
		}
		else {
			if(budgetHeads!=null && !budgetHeads.isEmpty() && !budgetHeads.contains("-1") 
					&& !budgetHeads.get(0).equals("")){
				queryBfr.append(" AND FIN_DETAILS_BUDGETGROUP_ID in (");
				for(int i =0; i <budgetHeadIds.size();i++)
				{
					if(i==0)
						queryBfr.append(" ?");
					else
						queryBfr.append(" ,? ");
					paramList.add(budgetHeadIds.get(i));
				}
				queryBfr.append(" ) ");
			} 
			if(depositCodeIds!=null && !depositCodeIds.isEmpty()){
				queryBfr.append(" AND FIN_DETAILS_COA_ID in (");
				for(int i =0; i <depositCodeIds.size();i++)
				{
					if(i==0)
						queryBfr.append(" ?");
					else
						queryBfr.append(" ,? ");
					paramList.add(depositCodeIds.get(i));
				}
				queryBfr.append(" ) ");
			}
		}
		if(contractorId!=null && contractorId!= -1 && StringUtils.isNotBlank(contractorName) ){
			queryBfr.append(" AND WORK_ORDER_CONTRACTOR_ID=?");
			paramList.add(contractorId);
		}
		else
		{
			if(StringUtils.isNotBlank(contractorName)){
				queryBfr.append(" AND WORK_ORDER_CONTRACTOR_NAME like '%"+contractorName+"%'");
			}
		}
		if(gradeId!=null && gradeId!=-1)
		{
			queryBfr.append(" AND WORK_ORDER_CONTRACTOR_ID IN ( SELECT CONTRACTOR_ID FROM EGW_CONTRACTOR_DETAIL WHERE CONTRACTOR_GRADE_ID=? AND CONTRACTOR_ID=WORK_ORDER_CONTRACTOR_ID)");
			paramList.add(gradeId);
		}
		return  queryBfr.toString();
	}

	private void generateBudgetHeads()
	{
		List<CChartOfAccounts> coaList = new ArrayList<CChartOfAccounts>();
		List<BudgetGroup> budgetHeadList = new ArrayList<BudgetGroup>();
		//In case all is selected, then should consider all the budget heads 
		if(dropDownBudgetHeads.size()==1 && dropDownBudgetHeads.get(0).equalsIgnoreCase(WorksConstants.ALL))
		{
			String[] budgetHeadsFromAppConf = budgetHeadsAppConfValue.split(",");
			for(int i=0;i<budgetHeadsFromAppConf.length;i++)
			{
				// Split and obtain only the glcode
				coaList.addAll(commonsService.getListOfDetailCode(budgetHeadsFromAppConf[i].split("-")[0]));
			}
			budgetHeadList.addAll( budgetDetailsDAO.getBudgetHeadForGlcodeList(coaList));
		}
		//Incase all is not selected
		if(!dropDownBudgetHeads.get(0).equalsIgnoreCase(WorksConstants.ALL))
		{
			for(int i=0;i<dropDownBudgetHeads.size();i++)
			{
				coaList.addAll(commonsService.getListOfDetailCode(dropDownBudgetHeads.get(i).split("-")[0]));
			}
			budgetHeadList.addAll( budgetDetailsDAO.getBudgetHeadForGlcodeList(coaList));
		}
		List<Long> budgetHeadIdsLong= new ArrayList<Long>();
		List<String> budgetHeadIdStr= new ArrayList<String>();
		if(budgetHeadList!=null && budgetHeadList.size()>0)
		{
			for(BudgetGroup bdgtGrp:budgetHeadList)
			{
				budgetHeadIdStr.add(bdgtGrp.getId().toString());
				budgetHeadIdsLong.add(bdgtGrp.getId());
			}
		}
		budgetHeads = budgetHeadIdStr;
		budgetHeadIds = budgetHeadIdsLong;
	}
	
	private void generateDepositCodes()
	{
		List<CChartOfAccounts> coaList = new ArrayList<CChartOfAccounts>();
		if( dropDownDepositCodes.size()==1 && dropDownDepositCodes.get(0).equalsIgnoreCase(WorksConstants.ALL))
		{
			String[] depositCodesFromAppConf = depositCodesAppConfValue.split(",");
			for(int i=0;i<depositCodesFromAppConf.length;i++)
				coaList.addAll(commonsService.getListOfDetailCode(depositCodesFromAppConf[i].split("-")[0]));
		}
		//Incase all is not selected
		if( !dropDownDepositCodes.get(0).equalsIgnoreCase(WorksConstants.ALL))
		{
			for(int i=0;i<dropDownDepositCodes.size();i++)
				coaList.addAll(commonsService.getListOfDetailCode(dropDownDepositCodes.get(i).split("-")[0]));
		}
		List<Long> depositCodeIdsLong= new ArrayList<Long>();
		if(coaList!=null && coaList.size()>0)
		{
			for(CChartOfAccounts coa:coaList)
				depositCodeIdsLong.add(coa.getId());
		}
		depositCodeIds = depositCodeIdsLong;
	}
	
	private void getBudgetHeadsFromAllBudgetHeads()
	{
		budgetHeadIds = allBudgetHeads;
		budgetHeads = new ArrayList<String>();
		for(int i=0;i<allBudgetHeads.size();i++)
		{
			budgetHeads.add(allBudgetHeads.get(i).toString());
		}
	}
	
	private void getDepositCodesFromAllDepositCodes()
	{
		depositCodeIds = allDepositCodes;
	}
	
	private String generateFinYrList(List<CFinancialYear> finYrList)
	{
		Date todaysDate = new Date();
		StringBuffer finStrBfr = new StringBuffer();
		for(CFinancialYear yr:finYrList)
		{
			if((yr.getStartingDate().compareTo(todaysDate)<=0) && (yr.getEndingDate().compareTo(todaysDate)>=0))
			{
				finStrBfr.append("id:"+yr.getId()+"--"+DateUtils.getFormattedDate(yr.getStartingDate(), "dd/MM/yyyy")+"--"+
						DateUtils.getFormattedDate(todaysDate, "dd/MM/yyyy"));
			}
			else
			{
				finStrBfr.append("id:"+yr.getId()+"--"+DateUtils.getFormattedDate(yr.getStartingDate(), "dd/MM/yyyy")+"--"+
						DateUtils.getFormattedDate(yr.getEndingDate(), "dd/MM/yyyy"));
			}
		}
		return finStrBfr.toString();
	}
	
	private List<String> getBudgetGroupsFromAppConfig()
	{
		List<String> budgetGrpStrList = new ArrayList<String>();
		budgetGrpStrList.add(WorksConstants.ALL);
		budgetHeadsAppConfValue =worksService.getWorksConfigValue(BUDGET_HEADS_APPCONFIG_KEY);
		if(budgetHeadsAppConfValue!=null ){
			for(String appValues:budgetHeadsAppConfValue.split(","))
				budgetGrpStrList.add(appValues);
		}
		return budgetGrpStrList;
	}
	
	private List<String> getDepositCodesFromAppConfig()
	{
		List<String> glcodesStrList = new ArrayList<String>();
		glcodesStrList.add(WorksConstants.ALL);
		depositCodesAppConfValue =worksService.getWorksConfigValue(DEPOSITCODES_APPCONFIG_KEY);
		if(depositCodesAppConfValue!=null ){
			for(String appValues:depositCodesAppConfValue.split(","))
				glcodesStrList.add(appValues);
		}
		return glcodesStrList;
	}
	
	private void populateCategoryList(
			AjaxEstimateAction ajaxEstimateAction, boolean categoryPopulated) {
		if (categoryPopulated) {
			ajaxEstimateAction.setCategory(getWorksType().longValue());
			ajaxEstimateAction.subcategories();
			addDropdownData("worksSubTypeList", ajaxEstimateAction.getSubCategories());		
		}
		else {
			addDropdownData("worksSubTypeList", Collections.emptyList());
		}
	}
	
	private void populateSubSchemeList(
			AjaxWorkProgressAction ajaxWorkProgressAction, 
			boolean schemePopulated) {
		if (schemePopulated) {
			ajaxWorkProgressAction.setPersistenceService(getPersistenceService());
			ajaxWorkProgressAction.setSchemeId(getScheme());
			ajaxWorkProgressAction.loadSubSchemes();
			addDropdownData("subSchemeList", ajaxWorkProgressAction.getSubSchemes());		
		}
		else {
			addDropdownData("subSchemeList", Collections.emptyList());
		}
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

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public void setExecutingDepartment(Integer executingDepartment) {
		this.executingDepartment = executingDepartment;
	}

	public void setWorksType(Integer worksType) {
		this.worksType = worksType;
	}

	public void setWorksSubType(Integer worksSubType) {
		this.worksSubType = worksSubType;
	}

	public void setFund(Integer fund) {
		this.fund = fund;
	}

	public void setFunction(Integer function) {
		this.function = function;
	}

	public void setScheme(Integer scheme) {
		this.scheme = scheme;
	}

	public void setSubScheme(Integer subScheme) {
		this.subScheme = subScheme;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public void setPersonalInformationService(
			PersonalInformationService personalInformationService) {
		this.personalInformationService = personalInformationService;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public String getBudgetHeadsAppConfValue() {
		return budgetHeadsAppConfValue;
	}

	public void setBudgetHeadsAppConfValue(String budgetHeadsAppConfValue) {
		this.budgetHeadsAppConfValue = budgetHeadsAppConfValue;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public List<String> getDropDownBudgetHeads() {
		return dropDownBudgetHeads;
	}

	public void setDropDownBudgetHeads(List<String> dropDownBudgetHeads) {
		this.dropDownBudgetHeads = dropDownBudgetHeads;
	}

	public String getDepositCodesAppConfValue() {
		return depositCodesAppConfValue;
	}

	public void setDepositCodesAppConfValue(String depositCodesAppConfValue) {
		this.depositCodesAppConfValue = depositCodesAppConfValue;
	}

	public List<String> getDropDownDepositCodes() {
		return dropDownDepositCodes;
	}

	public void setDropDownDepositCodes(List<String> dropDownDepositCodes) {
		this.dropDownDepositCodes = dropDownDepositCodes;
	}

	public List<Long> getAllDepositCodes() {
		return allDepositCodes;
	}

	public void setAllDepositCodes(List<Long> allDepositCodes) {
		this.allDepositCodes = allDepositCodes;
	}

	public List<Long> getAllBudgetHeads() {
		return allBudgetHeads;
	}

	public void setAllBudgetHeads(List<Long> allBudgetHeads) {
		this.allBudgetHeads = allBudgetHeads;
	}

	public String getFinYearId() {
		return finYearId;
	}

	public void setFinYearId(String finYearId) {
		this.finYearId = finYearId;
	}

	public String getFinYearRange() {
		return finYearRange;
	}

	public void setFinYearRange(String finYearRange) {
		this.finYearRange = finYearRange;
	}

	public String getCurrentFinancialYearId() {
		return currentFinancialYearId;
	}

	public void setCurrentFinancialYearId(String currentFinancialYearId) {
		this.currentFinancialYearId = currentFinancialYearId;
	}

	public String getFinYearRangeStr() {
		return finYearRangeStr;
	}

	public String getSubHeader() {
		return subHeader;
	}

	public void setSubHeader(String subHeader) {
		this.subHeader = subHeader;
	}

	public String getContractorName() {
		return contractorName;
	}

	public Long getContractorId() {
		return contractorId;
	}

	public void setContractorName(String contractorName) {
		this.contractorName = contractorName;
	}

	public void setContractorId(Long contractorId) {
		this.contractorId = contractorId;
	}

	public Long getGradeId() {
		return gradeId;
	}

	public void setGradeId(Long gradeId) {
		this.gradeId = gradeId;
	}

	public List<String> getBudgetHeads() {
		return budgetHeads;
	}

	public void setBudgetHeads(List<String> budgetHeads) {
		this.budgetHeads = budgetHeads;
	}

	public void setBudgetDetailsDAO(BudgetDetailsDAO budgetDetailsDAO) {
		this.budgetDetailsDAO = budgetDetailsDAO;
	}

	public List<ContractorwiseReportBean> getResultList() {
		return resultList;
	}

	public void setResultList(List<ContractorwiseReportBean> resultList) {
		this.resultList = resultList;
	}

	public String getResultStatus() {
		return resultStatus;
	}

	public InputStream getReportInputStream() {
		return reportInputStream;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public Integer getPage() {
		return page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public List<CommonDrillDownReportBean> getCommonBeanList() {
		return commonBeanList;
	}

	public void setWorkProgressAbstractReportService(
			WorkProgressAbstractReportService workProgressAbstractReportService) {
		this.workProgressAbstractReportService = workProgressAbstractReportService;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public EgovPaginatedList getPaginatedList() {
		return paginatedList;
	}

	public void setPaginatedList(EgovPaginatedList paginatedList) {
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

	public void setBudgetHeadsStr(String budgetHeadsStr) {
		this.budgetHeadsStr = budgetHeadsStr;
	}

	public void setDepositCodesStr(String depositCodesStr) {
		this.depositCodesStr = depositCodesStr;
	}

	public void setDepositCodeIdsStr(String depositCodeIdsStr) {
		this.depositCodeIdsStr = depositCodeIdsStr;
	}

	public void setBudgetHeadIdsStr(String budgetHeadIdsStr) {
		this.budgetHeadIdsStr = budgetHeadIdsStr;
	}

	public String getReportMessage() {
		return reportMessage;
	}
}
