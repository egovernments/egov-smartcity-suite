package org.egov.works.web.actions.report;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.dispatcher.StreamResult;
import org.egov.commons.EgwStatus;
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
import org.egov.model.budget.BudgetGroup;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.utils.EgovPaginatedList;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.ContractorService;
import org.egov.works.services.WorkOrderService;

/**
 * @author Sathish P
 *
 */
@SuppressWarnings("serial")
@ParentPackage("egov")
@Results({
@Result(name=MeasurementBookReportAction.EXPORTPDF,type="stream",location="PdfInputStream", params={"inputName","PdfInputStream","contentType","application/pdf","contentDisposition","no-cache;filename=MeasurementBookReport.pdf"}),
@Result(name=MeasurementBookReportAction.EXPORTEXCEL,type="stream",location="ExcelInputStream", params={"inputName","ExcelInputStream","contentType","application/msexcel","contentDisposition","no-cache;filename=MeasurementBookReport.xls"})
})
public class MeasurementBookReportAction extends SearchFormAction {  

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(MeasurementBookReportAction.class);
	public static final String  EXPORTPDF = "exportPdf";
	public static final String  EXPORTEXCEL = "exportExcel";
	public static final Locale LOCALE = new Locale("en","IN");
	public static final SimpleDateFormat  DDMMYYYYFORMATS= new SimpleDateFormat("dd/MM/yyyy",LOCALE);
	private static final String  SEARCH="search";

	private List<Object> paramList;
	private List<ReportBean> reportList;
	private InputStream pdfInputStream;
	private InputStream excelInputStream;
	private ReportService reportService;
	private WorkOrderService workOrderService;
	private AbstractEstimateService abstractEstimateService;
	private ContractorService contractorService;
	
	private String mbStatus;
	private Date fromDate;
	private Date toDate;
	private String mbRefNo;
	private String workOrderNo;
	private Long workOrderId;
	private String estimateNo;
	private Long estimateId;
	private Long contractorId;
	private String jurisdictionName;
	private Integer jurisdictionId;
	private String budgetHeadName;
	private Long budgetHeadId;
	private Integer workOrderPreparedBy;	
	private String reportSubTitle;	
	private DecimalFormat df = new DecimalFormat("###.##"); 
	
	public String execute(){
		return INDEX;
	}
	 
	@Override
	public Object getModel() {		
		return null;
	}
	
	@Override
	public void prepare() {
		super.prepare();
		addDropdownData("woPreparedByList", persistenceService.findAllBy("select distinct mbh.workOrder.workOrderPreparedBy from MBHeader mbh " +
				"where mbh.egwStatus.code <>? order by mbh.workOrder.workOrderPreparedBy.employeeName",MBHeader.MeasurementBookStatus.CANCELLED.toString()));
	}
	
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {	
		String query = getQuery();	
		String countQry =  " select count(distinct mb.workOrder.id)  " + query +" ";
		return new SearchQueryHQL(query,countQry,paramList); 
	}
	
	@Override
	public String search() { 
		return SEARCH;
	}
	
	 public List<EgwStatus> getMbStatusList() { 
		 return persistenceService.findAllBy("from EgwStatus s where moduletype=? and code in(?,?) order by orderId",
				 MBHeader.class.getSimpleName(),MBHeader.MeasurementBookStatus.CREATED.toString(),MBHeader.MeasurementBookStatus.APPROVED.toString());
	}
	 
	 public Map<String,Object> getContractorForApprovedWorkOrder() {
		Map<String,Object> contractorsWithWOList = new HashMap<String, Object>();		
		if(workOrderService.getContractorsWithWO()!=null) {
			for(Contractor contractor :workOrderService.getContractorsWithWO()){ 
				contractorsWithWOList.put(contractor.getId()+"", contractor.getCode()+" - "+contractor.getName());
			}			
		} 
		return contractorsWithWOList; 
	} 
	
	public String searchList(){
		validateDate();
		setPageSize(30);
		super.search();
		formatSearchResult();
		return SEARCH;
	}
	
	private void validateDate(){		
		if(null == fromDate){
			throw new ValidationException(Arrays.asList(new ValidationError("report.search.fromdate","report.search.fromdate")));
		}
		else if(null == toDate){
			throw new ValidationException(Arrays.asList(new ValidationError("report.search.todate","report.search.todate")));
		}
		else if(fromDate.after(toDate)){
			throw new ValidationException(Arrays.asList(new ValidationError("report.validate.fromDate","report.validate.fromDate")));
		}				
	}
		
	private String getQuery(){	
		
		paramList = new ArrayList<Object>();		
		StringBuilder dynQuery = new StringBuilder(800);
		StringBuffer titleBuffer = null;
		String queryBeginning =" ";
		
		queryBeginning =" from MBHeader mb where mb.egwStatus.code <> ? "; 
		paramList.add(MBHeader.MeasurementBookStatus.NEW.toString());
		titleBuffer = new StringBuffer( "List of work order issued with MB amount in "+mbStatus+" status");
				
		if(mbStatus != null && !"".equals(mbStatus)) {
			if(mbStatus.equals(MBHeader.MeasurementBookStatus.CREATED.toString())) {
				dynQuery.append(" and mb.egwStatus.code in (?,?,?) ");
				paramList.add(MBHeader.MeasurementBookStatus.CREATED.toString());
				paramList.add(MBHeader.MeasurementBookStatus.RESUBMITTED.toString());
				paramList.add(MBHeader.MeasurementBookStatus.CHECKED.toString());
			}
			else {
				dynQuery.append(" and mb.egwStatus.code = ? ");
				paramList.add(mbStatus);
			}
		}

		if(contractorId != null && contractorId != -1) { 
			dynQuery.append(" and mb.workOrder.contractor.id = ? "); 
			paramList.add(contractorId);
			titleBuffer.append(" for contractor - "+contractorService.getBidderById(contractorId.intValue()).getName());
		}
		
		if(fromDate!=null && toDate!=null){ 
			dynQuery.append(" and trunc(mb.mbDate) >= ? ");
			dynQuery.append(" and trunc(mb.mbDate) <= ? ");
			paramList.add(new Date(DateUtils.getFormattedDate(fromDate,"dd-MMM-yyyy"))); 
			paramList.add(new Date(DateUtils.getFormattedDate(toDate,"dd-MMM-yyyy")));
			titleBuffer.append(" for the date range "+DateUtils.getFormattedDate(fromDate,"dd/MM/yyyy")+" - "+DateUtils.getFormattedDate(toDate,"dd/MM/yyyy"));
		}
		
		if(mbRefNo!=null && !"".equals(mbRefNo)) { 
			dynQuery.append(" and upper(mbRefNo) like '%' || ? || '%' "); 
			paramList.add(mbRefNo);
			titleBuffer.append(" with MB reference number - "+mbRefNo);
		}
		
		if(workOrderId != null && workOrderId != -1) { 
			dynQuery.append(" and mb.workOrder.id = ? "); 
			paramList.add(workOrderId);
			titleBuffer.append(" with Work Order number - "+workOrderNo);
		}
		
		if(estimateNo!=null && !"".equals(estimateNo)) { 
			dynQuery.append(" and mb.workOrderEstimate.estimate.estimateNumber = ? "); 
			paramList.add(estimateNo);
			titleBuffer.append(", Estimate number - "+estimateNo); 
		}
		
 
		if(jurisdictionId != null && jurisdictionId != -1) { 
			dynQuery.append(" and mb.workOrderEstimate.estimate.ward.id = ? "); 
			paramList.add(jurisdictionId);
			titleBuffer.append(" in Jurisdiction - "+jurisdictionName);
		}
	
		if(budgetHeadId != null && budgetHeadId != -1) {			
			dynQuery.append(" and mb.workOrderEstimate.estimate.financialDetails[0].budgetGroup.id=? ");
			paramList.add(budgetHeadId);
			titleBuffer.append(" with budget head - "+budgetHeadName);
		}	
		
		if(workOrderPreparedBy != null && workOrderPreparedBy != -1) { 
			dynQuery.append(" and mb.workOrder.workOrderPreparedBy.id = ? "); 
			paramList.add(workOrderPreparedBy);
			//titleBuffer.append(", Work Order prepared by "+workOrderPreparedBy);
		}
		reportSubTitle = titleBuffer.toString();
	  	dynQuery.append(" order by mb.workOrder.workOrderNumber ");
		return queryBeginning+dynQuery.toString();		
	}
	
	@SuppressWarnings("unchecked")
	protected void formatSearchResult() {	
		reportList = new ArrayList<ReportBean>();
		EgovPaginatedList egovPaginatedList = (EgovPaginatedList)searchResult;
		List<MBHeader> list = egovPaginatedList.getList();
		List<String> uniqueEntity = new ArrayList<String>(); // used to display the work order wise details once.		
		BigDecimal mbAmount = new BigDecimal(0);		
		ReportBean reportBean = new ReportBean();
		
		for(MBHeader mbh : list) {
			WorkOrder workOrder = mbh.getWorkOrder();
			if(reportBean.getWoId() != null && !reportBean.getWoId().equals(workOrder.getId())) {  
				reportList.add(reportBean);
			}

			if(!uniqueEntity.contains(workOrder.getWorkOrderNumber()+", "+DDMMYYYYFORMATS.format(workOrder.getWorkOrderDate()))){
				reportBean = new ReportBean();
				mbAmount = new BigDecimal(0);
				AbstractEstimate abstractEstimate = mbh.getWorkOrderEstimate().getEstimate();
				reportBean.setEstNoAndDate(abstractEstimate.getEstimateNumber()+", "+DDMMYYYYFORMATS.format(abstractEstimate.getEstimateDate()));
				reportBean.setNameOfWork(abstractEstimate.getName());
				reportBean.setJurisdiction(abstractEstimate.getWard().getName());
				BudgetGroup budgetGroup = abstractEstimate.getFinancialDetails().get(0).getBudgetGroup();
				if(budgetGroup != null)
					reportBean.setBudgetHead(budgetGroup.getMaxCode().getGlcode()+" ~ "+budgetGroup.getName());
				
				reportBean.setEstimateAmount(new BigDecimal(df.format(abstractEstimateService.getTotalEstimateAmountInclusiveOfRE(abstractEstimate))).setScale(2));
				
				reportBean.setWoId(workOrder.getId()); 
				reportBean.setWoNoAndDate(workOrder.getWorkOrderNumber()+", "+DDMMYYYYFORMATS.format(workOrder.getWorkOrderDate()));
				reportBean.setWoAmount(new BigDecimal(df.format(workOrderService.getRevisionEstimateWOAmount(mbh.getWorkOrder()))).setScale(2));
				reportBean.setContractorName(workOrder.getContractor().getName()); 
				
				uniqueEntity.add(workOrder.getWorkOrderNumber()+", "+DDMMYYYYFORMATS.format(workOrder.getWorkOrderDate()));
			}		
			mbAmount = mbAmount.add(new BigDecimal(df.format(mbh.getMbAmount().getValue())));
			reportBean.setTotalRecordedMbAmount(mbAmount.setScale(2));
		}
		if(reportBean.getWoId() != null)
				reportList.add(reportBean);
		
		egovPaginatedList.setList(reportList);
	}

	@SuppressWarnings("unchecked")
	protected List<ReportBean> formatSearchResultForExport(List<MBHeader> searchResult) {		
		reportList = new ArrayList<ReportBean>();
		List<String> uniqueEntity = new ArrayList<String>(); // used to display the work order wise details once.		
		BigDecimal mbAmount = new BigDecimal(0);		
		ReportBean reportBean = new ReportBean();
		
		for(MBHeader mbh : searchResult) {
			if(reportBean.getWoId() != null && !reportBean.getWoId().equals(mbh.getWorkOrder().getId())) { 
				reportList.add(reportBean);
			}
			WorkOrder workOrder = mbh.getWorkOrder();
			
			if(!uniqueEntity.contains(workOrder.getWorkOrderNumber()+", "+DDMMYYYYFORMATS.format(workOrder.getWorkOrderDate()))){
				reportBean = new ReportBean();
				mbAmount = new BigDecimal(0);
				AbstractEstimate abstractEstimate = mbh.getWorkOrderEstimate().getEstimate();
				reportBean.setEstNoAndDate(abstractEstimate.getEstimateNumber()+", "+DDMMYYYYFORMATS.format(abstractEstimate.getEstimateDate()));
				reportBean.setNameOfWork(abstractEstimate.getName());
				reportBean.setJurisdiction(abstractEstimate.getWard().getName());
				BudgetGroup budgetGroup = abstractEstimate.getFinancialDetails().get(0).getBudgetGroup();
				if(budgetGroup != null)
					reportBean.setBudgetHead(budgetGroup.getMaxCode().getGlcode()+" ~ "+budgetGroup.getName());

				reportBean.setEstimateAmount(new BigDecimal(df.format(abstractEstimateService.getTotalEstimateAmountInclusiveOfRE(abstractEstimate))).setScale(2));
				
				reportBean.setWoId(workOrder.getId()); 
				reportBean.setWoNoAndDate(workOrder.getWorkOrderNumber()+", "+DDMMYYYYFORMATS.format(workOrder.getWorkOrderDate()));
				reportBean.setWoAmount(new BigDecimal(df.format(workOrderService.getRevisionEstimateWOAmount(mbh.getWorkOrder()))).setScale(2));
				reportBean.setContractorName(workOrder.getContractor().getName()); 
				
				uniqueEntity.add(workOrder.getWorkOrderNumber()+", "+DDMMYYYYFORMATS.format(workOrder.getWorkOrderDate()));
			}	
			mbAmount = mbAmount.add(new BigDecimal(df.format(mbh.getMbAmount().getValue())));	
			reportBean.setTotalRecordedMbAmount(mbAmount.setScale(2));
		}
		if(reportBean.getWoId() != null)
			reportList.add(reportBean);
		
		return reportList; 
	}
	
	public String exportToPdf() throws JRException,Exception{
		Map<String,Object> reportParams = new HashMap<String,Object>();
		List<ReportBean> reportData = getReportData();
		reportParams.put("reportSubTitle", reportSubTitle);
		ReportRequest reportRequest = new ReportRequest("MeasurementBookReport",reportData, reportParams);
		ReportOutput reportOutput = reportService.createReport(reportRequest); 
		if(reportOutput!=null && reportOutput.getReportOutputData()!=null)
			pdfInputStream = new ByteArrayInputStream(reportOutput.getReportOutputData()); 
		return EXPORTPDF; 
	}
	
	public String exportToExcel() throws JRException,Exception{
		Map<String,Object> reportParams = new HashMap<String,Object>();
		List<ReportBean> reportData = getReportData();
		reportParams.put("reportSubTitle", reportSubTitle);
		ReportRequest reportRequest = new ReportRequest("MeasurementBookReport",reportData, reportParams);
		reportRequest.setReportFormat(FileFormat.XLS);
		ReportOutput reportOutput = reportService.createReport(reportRequest); 
		if(reportOutput!=null && reportOutput.getReportOutputData()!=null)
			excelInputStream = new ByteArrayInputStream(reportOutput.getReportOutputData()); 
		return EXPORTEXCEL; 
	}
	
	@SuppressWarnings("unchecked")
	private List<ReportBean> getReportData() { 
		String reportQuery = getQuery();
		List searchResult = persistenceService.findAllBy(reportQuery, paramList.toArray());
		formatSearchResultForExport(searchResult);
		return reportList;
	}
		
	public Date getFromDate() {
		return fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public InputStream getPdfInputStream() {
		return pdfInputStream;
	}

	public InputStream getExcelInputStream() {
		return excelInputStream;
	}

	public void setWorkOrderService(WorkOrderService workOrderService) {
		this.workOrderService = workOrderService;
	}

	public String getMbStatus() {
		return mbStatus;
	}

	public void setMbStatus(String mbStatus) {
		this.mbStatus = mbStatus;
	}

	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}

	public String getMbRefNo() {
		return mbRefNo;
	}

	public void setMbRefNo(String mbRefNo) {
		this.mbRefNo = mbRefNo;
	}

	public String getWorkOrderNo() {
		return workOrderNo;
	}

	public void setWorkOrderNo(String workOrderNo) {
		this.workOrderNo = workOrderNo;
	}

	public Long getWorkOrderId() {
		return workOrderId;
	}

	public void setWorkOrderId(Long workOrderId) {
		this.workOrderId = workOrderId;
	}

	public String getEstimateNo() {
		return estimateNo;
	}

	public void setEstimateNo(String estimateNo) {
		this.estimateNo = estimateNo;
	}

	public Long getEstimateId() {
		return estimateId;
	}

	public void setEstimateId(Long estimateId) {
		this.estimateId = estimateId;
	}

	public Long getContractorId() {
		return contractorId;
	}

	public void setContractorId(Long contractorId) {
		this.contractorId = contractorId;
	}

	public String getJurisdictionName() {
		return jurisdictionName;
	}

	public void setJurisdictionName(String jurisdictionName) {
		this.jurisdictionName = jurisdictionName;
	}

	public Integer getJurisdictionId() {
		return jurisdictionId;
	}

	public void setJurisdictionId(Integer jurisdictionId) {
		this.jurisdictionId = jurisdictionId;
	}

	public String getBudgetHeadName() {
		return StringUtils.escapeSpecialChars(budgetHeadName);
	}

	public void setBudgetHeadName(String budgetHeadName) {
		this.budgetHeadName = StringEscapeUtils.unescapeHtml(budgetHeadName); 
	}

	public Long getBudgetHeadId() {
		return budgetHeadId;
	}

	public void setBudgetHeadId(Long budgetHeadId) {
		this.budgetHeadId = budgetHeadId;
	}

	public Integer getWorkOrderPreparedBy() {
		return workOrderPreparedBy;
	}

	public void setWorkOrderPreparedBy(Integer workOrderPreparedBy) {
		this.workOrderPreparedBy = workOrderPreparedBy;
	}

	public void setContractorService(ContractorService contractorService) {
		this.contractorService = contractorService;
	}

	public String getReportSubTitle() {
		return reportSubTitle;
	}

}