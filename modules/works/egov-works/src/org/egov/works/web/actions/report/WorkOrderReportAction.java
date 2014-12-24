package org.egov.works.web.actions.report;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.dispatcher.StreamResult;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.utils.DateUtils;
import org.egov.model.budget.BudgetGroup;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.utils.EgovPaginatedList;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.ContractorService;
import org.egov.works.services.WorkOrderService;

@SuppressWarnings("serial")
@ParentPackage("egov")
@Results({
@Result(name=WorkOrderReportAction.EXPORTPDF,type="stream",location="PdfInputStream", params={"inputName","PdfInputStream","contentType","application/pdf","contentDisposition","no-cache;filename=WOReport.pdf"}),
@Result(name=WorkOrderReportAction.EXPORTEXCEL,type="stream",location="ExcelInputStream", params={"inputName","ExcelInputStream","contentType","application/msexcel","contentDisposition","no-cache;filename=WOReport.xls"})
})

public class WorkOrderReportAction extends SearchFormAction {
	
	private static final Logger logger = Logger.getLogger(WorkOrderReportAction.class);
	private static final String  SEARCH="search";
	
	private WorkOrderService workOrderService;
	private ContractorService contractorService;
	private AbstractEstimateService abstractEstimateService;
	
	private String workorderNumber;
	private String tenderNoticeNumber;
	private String tenderfileNumber;
	private Long contractorId;
	private Date  fromDate;
	private Date  toDate;
	private List<Object> paramList;
	private String reportSubTitle;
	private InputStream pdfInputStream;
	private InputStream excelInputStream;
	
	private List<WOReportBean> reportList;
	private String status;
	public static final String  EXPORTPDF = "exportPdf";
	public static final String  EXPORTEXCEL = "exportExcel";
	private ReportService reportService;
	
	private WorkOrder workOrder;
	
	private AbstractEstimate abstractEstimate;
	
	private String workOrderNumber;
	private String tenderNumber;
	private String tenderFileRefNumber;
	private Contractor contractor;
	private List<WorkOrder> paginatedWorkorderList = new ArrayList<WorkOrder>();
	private static final String COMMA_SEPARATOR = ", ";	
	@Override
	public Object getModel() {
		return null;
	}

	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder)
	{
		String query = getSearchQuery();
		String countQry =  " select count(*) " + query +" ";
		setPageSize(30);
		return new SearchQueryHQL(query, countQry, paramList);
	}
	  
	public String search()
	{
		return SEARCH;
	}
	
	public void prepare()
	{
		return;
	}

	public String searchList()
	{
		super.search();
		formatSearchResult();
		return SEARCH;
	}
	
	private String getSearchQuery()
	{
		logger.debug("---------Beginning of getSearchQuery---------");
		paramList = new ArrayList<Object>();
		StringBuilder dynQuery = new StringBuilder(800);
		StringBuffer titleBuffer = new StringBuffer();
		String queryBeginning =" ";
		
		queryBeginning = "from WorkOrder wo where wo.id is not null and wo.parent is null and wo.egwStatus.code!='NEW'";
		
		titleBuffer.append("List of Works Orders "); 
	
		if(contractorId!=null && contractorId!=-1) 
		{
			dynQuery.append("and wo.contractor.id = ?"); 
			paramList.add(contractorId);
			Contractor cs = contractorService.findById(contractorId, false);
			titleBuffer.append("for contractor "+cs.getName().toString());
		}
		if(fromDate!=null && toDate!=null){
			dynQuery.append(" and trunc(wo.workOrderDate) >= '"+DateUtils.getFormattedDate(fromDate,"dd-MMM-yyyy")+"' ");
			dynQuery.append(" and trunc(wo.workOrderDate) <= '"+DateUtils.getFormattedDate(toDate,"dd-MMM-yyyy")+"' ");
			titleBuffer.append(" with date range - "+DateUtils.getFormattedDate(fromDate,"dd/MM/yyyy")+" to "+DateUtils.getFormattedDate(toDate,"dd/MM/yyyy"));
		}
		if(workOrderNumber!=null && !workOrderNumber.equals(""))
		{
			dynQuery.append(" and wo.workOrderNumber like '%' || ? || '%' ");
			paramList.add(workOrderNumber);
			titleBuffer.append(" for Work Order Number "+workorderNumber);
		}
		
		if(tenderNumber!=null && !tenderNumber.equals("")) 
		{
			dynQuery.append(" and wo.tenderNumber like '%' || ? || '%' ");
			paramList.add(tenderNumber);
			titleBuffer.append(" with Tender Notice Number "+tenderNumber);
		}
		
		if(tenderFileRefNumber!=null && !tenderFileRefNumber.equals(""))
		{
			dynQuery.append(" and wo.tenderNumber in (select tn.number from TenderNotice tn where tn.number=wo.tenderNumber and tn.tenderFileRefNumber like '%' || ? || '%') ");
			paramList.add(tenderFileRefNumber);
			titleBuffer.append(" with Tender File Number "+tenderFileRefNumber);
		}
		
      	if(fromDate!=null && toDate!=null && !DateUtils.compareDates(getToDate(),getFromDate()))
			addFieldError("fromDate",getText("greaterthan.endDate.fromDate"));
		if(toDate!=null && !DateUtils.compareDates(new Date(),getToDate()))
			addFieldError("toDate",getText("greaterthan.endDate.currentdate"));
		
		reportSubTitle = titleBuffer.toString();
		logger.debug("---------reportSubTitle--------->>"+reportSubTitle);
		logger.debug("---------End of getSearchQuery---------");
		logger.debug("---------SearchQuery Returned---------"+queryBeginning+dynQuery.toString());
		return queryBeginning+dynQuery.toString();
	}

	@SuppressWarnings("unchecked")
	protected void formatSearchResult() {		
		
		reportList = new ArrayList<WOReportBean>();
		EgovPaginatedList egovPaginatedList = (EgovPaginatedList)searchResult;
	
		List<WorkOrder> list = egovPaginatedList.getList();
		WOReportBean repBean;
		for(WorkOrder wo : list)
		{
			repBean = new WOReportBean();
			for(WorkOrderEstimate woe : wo.getWorkOrderEstimates())
			{
				repBean.setEstNumAndDate(woe.getEstimate().getEstimateNumber()+ COMMA_SEPARATOR +DateUtils.getFormattedDate( woe.getEstimate().getEstimateDate(),"dd/MM/yyyy"));
				repBean.setNameOfWork(woe.getEstimate().getName());
				repBean.setJurisdiction(woe.getEstimate().getWard().getName());
				BudgetGroup bgtgrp = woe.getEstimate().getFinancialDetails().get(0).getBudgetGroup();
				if(bgtgrp!=null)
				{
					repBean.setBudgetHead(bgtgrp.getMaxCode().getGlcode()+" ~ "+bgtgrp.getName());
				}
				DecimalFormat df = new DecimalFormat("###.##");
				repBean.setEstAmt(new BigDecimal(df.format(abstractEstimateService.getTotalEstimateAmountInclusiveOfRE(woe.getEstimate()))).setScale(2));
				if(woe.getWorkOrder().getWorkOrderNumber()!=null && woe.getWorkOrder().getWorkOrderDate()!=null)
				{
					repBean.setWONumAndDate(woe.getWorkOrder().getWorkOrderNumber()+ COMMA_SEPARATOR + DateUtils.getFormattedDate(woe.getWorkOrder().getWorkOrderDate(),"dd/MM/yyyy"));
				}
				if(woe.getWorkOrder().getWorkOrderNumber()!=null && woe.getWorkOrder().getWorkOrderDate()==null)
				{
					repBean.setWONumAndDate(woe.getWorkOrder().getWorkOrderNumber());
				}
				if(woe.getWorkOrder().getWorkOrderDate()!=null && woe.getWorkOrder().getWorkCompletionDate()!=null)
				{
					Date date1 = woe.getWorkOrder().getWorkCompletionDate();
					Date date2 = woe.getWorkOrder().getWorkOrderDate();
					Integer d1 = getNumberOfDays(date2,date1) ;
					repBean.setDays(d1.toString());
				}
				repBean.setStatus(woe.getWorkOrder().getEgwStatus().getDescription());
				repBean.setWoAmt(new BigDecimal(df.format(workOrderService.getRevisionEstimateWOAmount(woe.getWorkOrder()))).setScale(2));
				repBean.setContractorName(woe.getWorkOrder().getContractor().getName());
			}
			reportList.add(repBean);
		} 
		egovPaginatedList.setList(reportList);
	}

	protected List<WOReportBean> formatSearchResultForExport(List<WorkOrder> searchResult) {		
		reportList = new ArrayList<WOReportBean>();
		WOReportBean repBean;
		for(WorkOrder wo : searchResult)
		{
			repBean = new WOReportBean();
			for(WorkOrderEstimate woe : wo.getWorkOrderEstimates())
			{				
				repBean.setEstNumAndDate(woe.getEstimate().getEstimateNumber()+ COMMA_SEPARATOR + DateUtils.getFormattedDate(woe.getEstimate().getEstimateDate(),"dd/MM/yyyy"));
				repBean.setNameOfWork(woe.getEstimate().getName());
				repBean.setJurisdiction(woe.getEstimate().getWard().getName());
				BudgetGroup bgtgrp = woe.getEstimate().getFinancialDetails().get(0).getBudgetGroup();
				if(bgtgrp!=null)
				{
					repBean.setBudgetHead(bgtgrp.getMaxCode().getGlcode()+" ~ "+bgtgrp.getName()); 
				}
				abstractEstimate = woe.getEstimate();
				DecimalFormat df = new DecimalFormat("###.##");
				repBean.setEstAmt(new BigDecimal(df.format(abstractEstimateService.getTotalEstimateAmountInclusiveOfRE(woe.getEstimate()))).setScale(2));
				if(woe.getWorkOrder().getWorkOrderNumber()!=null && woe.getWorkOrder().getWorkOrderDate()!=null)
				{
					repBean.setWONumAndDate(woe.getWorkOrder().getWorkOrderNumber()+ COMMA_SEPARATOR + DateUtils.getFormattedDate(woe.getWorkOrder().getWorkOrderDate(),"dd/MM/yyyy"));
				}
				if(woe.getWorkOrder().getWorkOrderNumber()!=null && woe.getWorkOrder().getWorkOrderDate()==null)
				{
					repBean.setWONumAndDate(woe.getWorkOrder().getWorkOrderNumber());
				}
				if(woe.getWorkOrder().getWorkOrderDate()!=null && woe.getWorkOrder().getWorkCompletionDate()!=null)
				{
					Date date1 = woe.getWorkOrder().getWorkCompletionDate();
					Date date2 = woe.getWorkOrder().getWorkOrderDate();
					Integer d1 = getNumberOfDays(date2,date1) ;
					repBean.setDays(d1.toString());
				}
				repBean.setStatus(woe.getWorkOrder().getEgwStatus().getDescription());
				repBean.setWoAmt(new BigDecimal(df.format(workOrderService.getRevisionEstimateWOAmount(woe.getWorkOrder()))).setScale(2));
				repBean.setContractorName(woe.getWorkOrder().getContractor().getName());
			}
			reportList.add(repBean);
		} 
		return reportList;
	}

	private static Integer getNumberOfDays(Date fromDate, Date toDate) {
		logger.info("Entering into getNumberOfDays");
		Calendar fromDateCalendar = Calendar.getInstance();
		Calendar toDateCalendar = Calendar.getInstance();
		fromDateCalendar.setTime(fromDate);
		toDateCalendar.setTime(toDate);
		Integer days = 0;
		while (fromDateCalendar.before(toDateCalendar)) {
		fromDateCalendar.add(Calendar.DAY_OF_MONTH, 1);
		days++;
		}
		logger.info("getNumberOfDays - days: " + days + " between " + fromDate + " and " + toDate);
		logger.info("Exiting getNumberOfDays");
		return days;
	}
	
	public Map<String,Object> getContractorForApprovedWorkOrder() {
		Map<String,Object> contractorsWithWOList = new LinkedHashMap<String, Object>();		
		if(workOrderService.getContractorsWithWO()!=null) {
			for(Contractor contractor :workOrderService.getContractorsWithWO()){
				contractorsWithWOList.put(contractor.getId()+"", contractor.getCode()+" - "+contractor.getName());
			}			
		}
		return contractorsWithWOList; 
	}
	
	@SuppressWarnings("unchecked")
	private List<WOReportBean> getReportData() { 
		String reportQuery = getSearchQuery();
		List searchResult = persistenceService.findAllBy(reportQuery, paramList.toArray());
		formatSearchResultForExport(searchResult);
		return reportList;
	}

	public String exportToPdf() throws JRException,Exception{
		Map<String,Object> reportParams = new HashMap<String,Object>();
		List<WOReportBean> reportData = getReportData();
		reportParams.put("reportSubTitle", reportSubTitle);
		ReportRequest reportRequest = new ReportRequest("WOReport",reportData, reportParams);
		ReportOutput reportOutput = reportService.createReport(reportRequest); 
		if(reportOutput!=null && reportOutput.getReportOutputData()!=null)
			pdfInputStream = new ByteArrayInputStream(reportOutput.getReportOutputData()); 
		return EXPORTPDF;
	}
	
	public String exportToExcel() throws JRException,Exception{
		Map<String,Object> reportParams = new HashMap<String,Object>();
		List<WOReportBean> reportData = getReportData();
		reportParams.put("reportSubTitle", reportSubTitle);
		ReportRequest reportRequest = new ReportRequest("WOReport",reportData, reportParams);
		reportRequest.setReportFormat(FileFormat.XLS);
		ReportOutput reportOutput = reportService.createReport(reportRequest); 
		if(reportOutput!=null && reportOutput.getReportOutputData()!=null)
			excelInputStream = new ByteArrayInputStream(reportOutput.getReportOutputData()); 
		return EXPORTEXCEL; 
	}
	
	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public List<WOReportBean> getReportList() {
		return reportList;
	}

	public void setReportList(List<WOReportBean> reportList) {
		this.reportList = reportList;
	}

	public ContractorService getContractorService() {
		return contractorService;
	}

	public void setContractorService(ContractorService contractorService) {
		this.contractorService = contractorService;
	}
	
	public Contractor getContractor() {
		return contractor;
	}

	public void setContractor(Contractor contractor) {
		this.contractor = contractor;
	}
	
	public WorkOrderService getWorkOrderService() {
		return workOrderService;
	}

	public void setWorkOrderService(WorkOrderService workOrderService) {
		this.workOrderService = workOrderService;
	}

	public WorkOrder getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrder(WorkOrder workOrder) {
		this.workOrder = workOrder;
	}
	
	public String getTenderNoticeNumber() {
		return tenderNoticeNumber;
	}

	public void setTenderNoticeNumber(String tenderNoticeNumber) {
		this.tenderNoticeNumber = tenderNoticeNumber;
	}

	public String getWorkorderNumber() {
		return workorderNumber;
	}

	public void setWorkorderNumber(String workorderNumber) {
		this.workorderNumber = workorderNumber;
	}

	public String getTenderNumber() {
		return tenderNumber;
	}

	public void setTenderNumber(String tenderNumber) {
		this.tenderNumber = tenderNumber;
	}
	
	public String getTenderFileRefNumber() {
		return tenderFileRefNumber;
	}

	public void setTenderFileRefNumber(String tenderFileRefNumber) {
		this.tenderFileRefNumber = tenderFileRefNumber;
	}
	
	public String getTenderfileNumber() {
		return tenderfileNumber;
	}

	public void setTenderfileNumber(String tenderfileNumber) {
		this.tenderfileNumber = tenderfileNumber;
	}
	
	public Long getContractorId() {
		return contractorId;
	}

	public void setContractorId(Long contractorId) {
		this.contractorId = contractorId;
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
	
	public String getWorkOrderNumber() {
		return workOrderNumber;
	}

	public void setWorkOrderNumber(String workOrderNumber) {
		this.workOrderNumber = workOrderNumber;
	}
	
	public String getReportSubTitle() {
		return reportSubTitle;
	}

	public void setReportSubTitle(String reportSubTitle) {
		this.reportSubTitle = reportSubTitle;
	}

	public InputStream getPdfInputStream() {
		return pdfInputStream;
	}

	public void setPdfInputStream(InputStream pdfInputStream) {
		this.pdfInputStream = pdfInputStream;
	}

	public InputStream getExcelInputStream() {
		return excelInputStream;
	}

	public void setExcelInputStream(InputStream excelInputStream) {
		this.excelInputStream = excelInputStream;
	}

	public List<WorkOrder> getPaginatedWorkorderList() {
		return paginatedWorkorderList;
	}

	public void setPaginatedWorkorderList(List<WorkOrder> paginatedWorkorderList) {
		this.paginatedWorkorderList = paginatedWorkorderList;
	}

	public AbstractEstimate getAbstractEstimate() {
		return abstractEstimate;
	}

	public void setAbstractEstimate(AbstractEstimate abstractEstimate) {
		this.abstractEstimate = abstractEstimate;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}  
}
