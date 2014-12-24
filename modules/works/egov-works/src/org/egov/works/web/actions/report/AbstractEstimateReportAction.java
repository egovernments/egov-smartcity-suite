package org.egov.works.web.actions.report;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.StringUtils;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.admbndry.BoundaryTypeDAO;
import org.egov.lib.admbndry.HeirarchyType;
import org.egov.lib.admbndry.HeirarchyTypeDAO;
import org.egov.model.budget.BudgetGroup;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.egov.web.utils.EgovPaginatedList;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.TechnicalSanction;

@SuppressWarnings("serial")
@ParentPackage("egov")
@Results({
@Result(name=AbstractEstimateReportAction.EXPORTPDF,type="stream",location="PdfInputStream", params={"inputName","PdfInputStream","contentType","application/pdf","contentDisposition","no-cache;filename=AbstractEstimateReport.pdf"}),
@Result(name=AbstractEstimateReportAction.EXPORTEXCEL,type="stream",location="ExcelInputStream", params={"inputName","ExcelInputStream","contentType","application/msexcel","contentDisposition","no-cache;filename=AbstractEstimateReport.xls"})
})
public class AbstractEstimateReportAction extends SearchFormAction {
	private Date fromDate;
	private Date toDate;
	private Integer zoneId;
	private Integer wardId;
	private String wardName="";
	private Long budgetHeadId;
	private String budgetHeadName;
	private Double estimateLowerAmt;
	private Double estimateUpperAmt;
	private String estimateStatus;
	private List<String> estimateStatusList = new ArrayList<String>();
	private String reportSubTitle;
	private InputStream pdfInputStream;
	private InputStream excelInputStream;
	private ReportService reportService;
	private List<Object> paramList;
	private boolean includeCityEstimates;
	//private EmployeeService employeeService;
	private List<AbstractEstimate> paginatedEstimateList = new ArrayList<AbstractEstimate>();
	
	private static final Logger logger = Logger.getLogger(AbstractEstimateReportAction.class);
	private static final String  SEARCH="search";
	private static final String  CREATED_STATUS="Created";
	private static final String  TECH_SANCTIONED_STATUS="Technical Sanctioned";
	private static final String  ADMIN_SANCTIONED_STATUS="Administrative Sanctioned";
	private static final String ADMIN_HIERARCHY_TYPE = "ADMINISTRATION";
	private static final String COMMA_NEWLINE = " ,\n";
	public static final String  EXPORTPDF = "exportPdf";
	public static final String  EXPORTEXCEL = "exportExcel";
	
	
	@Override
	public Object getModel() {
		return null;
	}

	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		String reportSearchQuery = getSearchQuery();
		String countQry =  " select count(*)  " + reportSearchQuery;
		setPageSize(30);
		return new SearchQueryHQL(reportSearchQuery, countQry, paramList);
	}
	
	private String getSearchQuery()
	{
		logger.debug("---------Beginning of getSearchQuery---------");
		paramList = new ArrayList<Object>();
		StringBuilder dynQuery = new StringBuilder(800);
		StringBuffer titleBuffer = null;
		String queryBeginning =" ";
		
		titleBuffer = new StringBuffer( "List of estimates in "+estimateStatus+" state");
		
		if(budgetHeadId!=null){
			queryBeginning =" from AbstractEstimate ae left outer join ae.technicalSanctions as ts  join ae.financialDetails f  where f.budgetGroup.id=? ";
			paramList.add(budgetHeadId);
			titleBuffer.append(" with budget head-"+budgetHeadName);
		}
		else
		{
			queryBeginning =" from AbstractEstimate ae left outer join ae.technicalSanctions as ts where ae.id is not null ";
		}
		
		if(estimateStatus.equalsIgnoreCase(CREATED_STATUS))
		{
			dynQuery.append(" and (ae.egwStatus.code =? or ae.egwStatus.code =?  or ae.egwStatus.code =? ) ");
			paramList.add(AbstractEstimate.EstimateStatus.CREATED.toString());
			paramList.add("RESUBMITTED");
			paramList.add(AbstractEstimate.EstimateStatus.TECH_SANCTION_CHECKED.toString());
			
			if(fromDate!=null && toDate!=null){
				dynQuery.append(" and trunc(ae.estimateDate) >= '"+DateUtils.getFormattedDate(fromDate,"dd-MMM-yyyy")+"' ");
				dynQuery.append(" and trunc(ae.estimateDate) <= '"+DateUtils.getFormattedDate(toDate,"dd-MMM-yyyy")+"' ");
			}
		}
		if(estimateStatus.equalsIgnoreCase(TECH_SANCTIONED_STATUS))
		{
			if(fromDate!=null && toDate!=null){
				dynQuery.append(" and trunc(ts.techSanctionDate) >= '"+DateUtils.getFormattedDate(fromDate,"dd-MMM-yyyy")+"' ");
				dynQuery.append(" and trunc(ts.techSanctionDate) <= '"+DateUtils.getFormattedDate(toDate,"dd-MMM-yyyy")+"' ");
				dynQuery.append(" and ts.id=(select max(id)  from TechnicalSanction ts1 where ts1.abstractEstimate.id=ae.id) ");
			}
			dynQuery.append(" and ae.egwStatus.code =?  ");
			paramList.add(AbstractEstimate.EstimateStatus.TECH_SANCTIONED.toString());
		}
		if(estimateStatus.equalsIgnoreCase(ADMIN_SANCTIONED_STATUS))
		{
			if(fromDate!=null && toDate!=null){
				dynQuery.append(" and trunc(ae.adminsanctionDate) >= '"+DateUtils.getFormattedDate(fromDate,"dd-MMM-yyyy")+"' ");
				dynQuery.append(" and trunc(ae.adminsanctionDate) <= '"+DateUtils.getFormattedDate(toDate,"dd-MMM-yyyy")+"' ");
			}
			dynQuery.append(" and ae.egwStatus.code =?  ");
			paramList.add(AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString());
		}
		if(includeCityEstimates)
		{
			dynQuery.append(" and upper(ae.ward.boundaryType.name) in ('CITY') and upper(ae.ward.boundaryType.heirarchyType.name)='ADMINISTRATION' and ae.ward.isHistory='N'  ");
			titleBuffer.append(" with Jurisdiction as Headquarters");
		}
		else
		{
			if(zoneId!=null && zoneId!=-1 && (wardId==null ))
			{
				dynQuery.append(" and (ae.ward.id=? or ae.ward.parent.id=?) ");
				paramList.add(zoneId);
				paramList.add(zoneId);
				BoundaryImpl zone = (BoundaryImpl) persistenceService.find("from BoundaryImpl where id=?" ,zoneId);
				titleBuffer.append(" in "+zone.getName());
			}
			if(wardId!=null)
			{
				dynQuery.append(" and ae.ward.id=? ");
				paramList.add(wardId);
				BoundaryImpl ward = (BoundaryImpl) persistenceService.find("from BoundaryImpl where id=?" ,wardId);
				titleBuffer.append(" in "+ward.getName());
			}
		}
		if(fromDate!=null && toDate!=null && !DateUtils.compareDates(getToDate(),getFromDate()))
			throw new ValidationException(Arrays.asList(new ValidationError("greaterthan.endDate.fromDate",getText("greaterthan.endDate.fromDate"))));
		if(toDate!=null && !DateUtils.compareDates(new Date(),getToDate()))
			throw new ValidationException(Arrays.asList(new ValidationError("greaterthan.endDate.currentdate",getText("greaterthan.endDate.currentdate"))));
		if(fromDate!=null && toDate!=null){
			titleBuffer.append(" with date range - "+DateUtils.getFormattedDate(fromDate,"dd/MM/yyyy")+" to "+DateUtils.getFormattedDate(toDate,"dd/MM/yyyy"));
		}
		if(estimateLowerAmt!=null && estimateUpperAmt!=null){
			dynQuery.append(" and ((select coalesce(sum(oh.amount),0) from OverheadValue oh where oh.abstractEstimate.id=ae.id ) <= ( ? - round(ae.workValue.value,2) ) ) ");
			dynQuery.append(" and (( ? - ae.workValue.value ) <= (select coalesce(sum(oh.amount),0) from OverheadValue oh where oh.abstractEstimate.id=ae.id ) )");
			paramList.add(estimateUpperAmt);
			paramList.add(estimateLowerAmt);
			titleBuffer.append(" with amount between Rs."+String.format("%.2f", estimateLowerAmt)+" - Rs."+String.format("%.2f", estimateUpperAmt));
		}
		//Dont consider REs
		dynQuery.append(" and ae.parent is null  ");
		reportSubTitle = titleBuffer.toString();
		logger.debug("---------reportSubTitle--------->>"+reportSubTitle);
		logger.debug("---------End of getSearchQuery---------");
		logger.debug("---------SearchQuery Returned---------"+queryBeginning+dynQuery.toString());
		return queryBeginning+dynQuery.toString();
	}
	
	@SuppressWarnings("unchecked")
	private void formatEstimateList()
	{
		EgovPaginatedList egovPaginatedList = (EgovPaginatedList)searchResult;
		List<Object[]> list = egovPaginatedList.getList();
		List<AbstractEstimate> absEstList = new ArrayList<AbstractEstimate>();
		AbstractEstimate estimate;
		TechnicalSanction techSan;
		String techSancNoAndDate;
		for(Object[] object : list) {
			techSancNoAndDate = "";
			estimate = (AbstractEstimate) object[0];
			techSan = (TechnicalSanction) object[1];
			if(techSan!=null)
			{
				if(techSan.getTechSanctionNumber()!=null && techSan.getTechSanctionDate()!=null)
				{
					techSancNoAndDate = techSan.getTechSanctionNumber() + COMMA_NEWLINE + DateUtils.getFormattedDate(techSan.getTechSanctionDate(),"dd/MM/yyyy");
				}
				if(techSan.getTechSanctionNumber()!=null && techSan.getTechSanctionDate()==null)
				{
					techSancNoAndDate = techSan.getTechSanctionNumber() ;
				}
				if(techSan.getTechSanctionNumber()==null && techSan.getTechSanctionDate()!=null)
				{
					techSancNoAndDate =  DateUtils.getFormattedDate(techSan.getTechSanctionDate(),"dd/MM/yyyy") ;
				}
			}
			estimate.setTechSanctionNumber(techSancNoAndDate);
			absEstList.add(estimate);
		}
		egovPaginatedList.setList(absEstList);
	}
	
	private List<AbstractEstimate> formatEstimateListForExport(List<Object[]> list)
	{
		List<AbstractEstimate> absEstList = new ArrayList<AbstractEstimate>();
		if(list!=null && !list.isEmpty())
		{
			absEstList = new ArrayList<AbstractEstimate>();
			AbstractEstimate estimate;
			TechnicalSanction techSan;
			String techSancNoAndDate;
			for(Object[] object : list) {
				techSancNoAndDate = "";
				estimate = (AbstractEstimate) object[0];
				techSan = (TechnicalSanction) object[1];
				if(techSan!=null)
				{
					if(techSan.getTechSanctionNumber()!=null && techSan.getTechSanctionDate()!=null)
					{
						techSancNoAndDate = techSan.getTechSanctionNumber() + COMMA_NEWLINE + DateUtils.getFormattedDate(techSan.getTechSanctionDate(),"dd/MM/yyyy");
					}
					if(techSan.getTechSanctionNumber()!=null && techSan.getTechSanctionDate()==null)
					{
						techSancNoAndDate = techSan.getTechSanctionNumber() ;
					}
					if(techSan.getTechSanctionNumber()==null && techSan.getTechSanctionDate()!=null)
					{
						techSancNoAndDate = DateUtils.getFormattedDate(techSan.getTechSanctionDate(),"dd/MM/yyyy") ;
					}
				}
				estimate.setTechSanctionNumber(techSancNoAndDate);
				absEstList.add(estimate);
			}
		}
		return absEstList;
	}
	
	//From Admin Sanctioned State, this will return the Approval Authority
	/*private void getPositionOwner(Long aeList) {
		String empName;
		if(aeList!=null && !aeList.isEmpty())
		{
			employeeNameList.clear();
			for(AbstractEstimate ae:aeList)
			{
				empName = employeeService.getEmployeeforPosition(ae.getCurrentState().getOwner()).getEmployeeName();
				employeeNameList.add(empName);
			}
		}
	}*/

	public void prepare()
	{
		addDropdownData("zoneList", getAllZone());
		estimateStatusList.add(CREATED_STATUS);
		estimateStatusList.add(TECH_SANCTIONED_STATUS);
		estimateStatusList.add(ADMIN_SANCTIONED_STATUS);
		addDropdownData("estimateStatusList", estimateStatusList);
		return;
	}
	
	public String search()
	{
		return SEARCH;
	}
	
	@ValidationErrorPage(value=SEARCH)
	public String searchList(){
		super.search();
		formatEstimateList();
		return SEARCH;
	}
	
	/* To Populate Zone DropDown */
	@SuppressWarnings("unchecked")
	public List<Boundary> getAllZone() {
	    HeirarchyType hType = null;
		try{	
			hType = new HeirarchyTypeDAO().getHierarchyTypeByName(ADMIN_HIERARCHY_TYPE);
		}catch(EGOVException e){
			logger.error("Error while loading HeirarchyType - HeirarchyType."+ e.getMessage());
			throw new EGOVRuntimeException("Unable To Load Heirarchy Information",e);
		}
		List<Boundary> zoneList = null;
		BoundaryType bType = new BoundaryTypeDAO().getBoundaryType("zone",hType);
		zoneList = new BoundaryDAO().getAllBoundariesByBndryTypeId(bType.getId());
		return zoneList;
	}
	
	public String exportToPdf() throws JRException,Exception{
		ReportRequest reportRequest; 
		if(estimateStatus.equalsIgnoreCase(CREATED_STATUS))
			reportRequest = new ReportRequest("AbstractEstimateReport-Created",getReportData(), createHeaderParams());
		else
			reportRequest = new ReportRequest("AbstractEstimateReport",getReportData(), createHeaderParams());
		ReportOutput reportOutput = reportService.createReport(reportRequest); 
		if(reportOutput!=null && reportOutput.getReportOutputData()!=null)
			pdfInputStream = new ByteArrayInputStream(reportOutput.getReportOutputData()); 
		return EXPORTPDF; 
	}
	
	public String exportToExcel() throws JRException,Exception{
		ReportRequest reportRequest; 
		if(estimateStatus.equalsIgnoreCase(CREATED_STATUS))
			reportRequest = new ReportRequest("AbstractEstimateReport-Created",getReportData(), createHeaderParams());
		else
			reportRequest = new ReportRequest("AbstractEstimateReport",getReportData(), createHeaderParams());
		reportRequest.setReportFormat(FileFormat.XLS);
		ReportOutput reportOutput = reportService.createReport(reportRequest); 
		if(reportOutput!=null && reportOutput.getReportOutputData()!=null)
			excelInputStream = new ByteArrayInputStream(reportOutput.getReportOutputData()); 
		return EXPORTEXCEL; 
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getReportData()
	{
		Map<String,Object>  reportMap;
		List<Map<String,Object>> reportMapList = new ArrayList<Map<String,Object>>();
		logger.debug("---------Beginning of getReportData()---------");
		String reportQuery = getSearchQuery();
		List<Object[]> resultList =  persistenceService.findAllBy(reportQuery, paramList.toArray());
		logger.debug("---------Length of resultList---------"+resultList==null?0:resultList.size());
		List<AbstractEstimate> estimateResultList =  formatEstimateListForExport(resultList);
		
		if(estimateResultList!=null && !estimateResultList.isEmpty())
		{
			Integer i = 0;
			for(AbstractEstimate estimate:estimateResultList)
			{
				reportMap = new HashMap<String, Object>();
				reportMap.put("srlNo", i+1);
				reportMap.put("estimateNoDt", estimate.getEstimateNumber()+COMMA_NEWLINE+DateUtils.getFormattedDate(estimate.getEstimateDate(),"dd/MM/yyyy"));
				reportMap.put("nameOfWork", estimate.getName());
				reportMap.put("wardName", estimate.getWard().getName());
				if(estimate.getFinancialDetails()!=null &&  estimate.getFinancialDetails().size()>0   
						&& estimate.getFinancialDetails().get(0)!=null 
						&& estimate.getFinancialDetails().get(0).getBudgetGroup()!=null)
				{
					BudgetGroup bgtGrp = estimate.getFinancialDetails().get(0).getBudgetGroup();
					reportMap.put("bdgtHead", bgtGrp.getMaxCode().getGlcode()+"~"+bgtGrp.getName());
				}
				reportMap.put("estAmt", estimate.getTotalAmount().getValue());
				if(estimateStatus.equalsIgnoreCase(TECH_SANCTIONED_STATUS) || estimateStatus.equalsIgnoreCase(ADMIN_SANCTIONED_STATUS) )
				{
					reportMap.put("transcNoDt", estimate.getTechSanctionNumber());
					if(estimateStatus.equalsIgnoreCase(ADMIN_SANCTIONED_STATUS))
					{
						if(estimate.getAdminsanctionNo()!=null && estimate.getAdminsanctionDate()!=null)
							reportMap.put("adminNoDt", estimate.getAdminsanctionNo()+COMMA_NEWLINE+DateUtils.getFormattedDate(estimate.getAdminsanctionDate(),"dd/MM/yyyy"));
						/*if(employeeNameList.get(i)!=null)
							reportMap.put("approver", employeeNameList.get(i));*/
					}
				}	
				reportMapList.add(reportMap);
				i++;
			}
		}
		logger.debug("---------End of getReportData()---------");
		return reportMapList;
	}
	
	public Map<String,Object> createHeaderParams()
	{
		Map<String,Object> reportParams = new HashMap<String,Object>();
		reportParams.put("estimateStatus", estimateStatus);
		reportParams.put("reportSubTitle", reportSubTitle);
		return reportParams;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Integer getZoneId() {
		return zoneId;
	}

	public void setZoneId(Integer zoneId) {
		this.zoneId = zoneId;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public Long getBudgetHeadId() {
		return budgetHeadId;
	}

	public void setBudgetHeadId(Long budgetHeadId) {
		this.budgetHeadId = budgetHeadId;
	}

	public String getEstimateStatus() {
		return estimateStatus;
	}

	public void setEstimateStatus(String estimateStatus) {
		this.estimateStatus = estimateStatus;
	}

	public String getBudgetHeadName() {
		return StringUtils.escapeSpecialChars(budgetHeadName);
	}

	public void setBudgetHeadName(String budgetHeadName) {
		this.budgetHeadName = StringEscapeUtils.unescapeHtml(budgetHeadName); 
	}

	public String getReportSubTitle() {
		return reportSubTitle;
	}

	public InputStream getPdfInputStream() {
		return pdfInputStream;
	}

	public InputStream getExcelInputStream() {
		return excelInputStream;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public Double getEstimateLowerAmt() {
		
		return estimateLowerAmt;
	}

	public void setEstimateLowerAmt(Double estimateLowerAmt) {
		this.estimateLowerAmt = estimateLowerAmt;
	}

	public Double getEstimateUpperAmt() {
		return estimateUpperAmt;
	}

	public void setEstimateUpperAmt(Double estimateUpperAmt) {
		this.estimateUpperAmt = estimateUpperAmt;
	}

	/*public void setEmployeeService(employeeService employeeService) {
		this.employeeService = employeeService;
	}*/

	public List<AbstractEstimate> getPaginatedEstimateList() {
		return paginatedEstimateList;
	}

	public void setPaginatedEstimateList(
			List<AbstractEstimate> paginatedEstimateList) {
		this.paginatedEstimateList = paginatedEstimateList;
	}

	public boolean isIncludeCityEstimates() {
		return includeCityEstimates;
	}

	public void setIncludeCityEstimates(boolean includeCityEstimates) {
		this.includeCityEstimates = includeCityEstimates;
	}

}
