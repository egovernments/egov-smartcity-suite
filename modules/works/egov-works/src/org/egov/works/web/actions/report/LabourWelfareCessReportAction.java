/**
 * 
 */
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

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.dispatcher.StreamResult;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.service.CommonsService;
import org.egov.egf.commons.EgovCommon;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.utils.DateUtils;
import org.egov.model.bills.EgBilldetails;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.utils.EgovPaginatedList;
import org.egov.works.models.contractorBill.ContractorBillRegister;
import org.egov.works.models.measurementbook.MBBill;
import org.egov.works.services.WorkOrderService;
import org.egov.works.services.WorksService;

/**
 * @author Sathish P
 *
 */
@SuppressWarnings("serial")
@ParentPackage("egov")
@Results({
@Result(name=LabourWelfareCessReportAction.EXPORTPDF,type="stream",location="PdfInputStream", params={"inputName","PdfInputStream","contentType","application/pdf","contentDisposition","no-cache;filename=LabourWelfareCessReport.pdf"}),
@Result(name=LabourWelfareCessReportAction.EXPORTEXCEL,type="stream",location="ExcelInputStream", params={"inputName","ExcelInputStream","contentType","application/msexcel","contentDisposition","no-cache;filename=LabourWelfareCessReport.xls"})
})
public class LabourWelfareCessReportAction extends SearchFormAction { 

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(LabourWelfareCessReportAction.class);
	private Date fromDate;
	private Date toDate;
	public static final String  EXPORTPDF = "exportPdf";
	public static final String  EXPORTEXCEL = "exportExcel";
	public static final Locale LOCALE = new Locale("en","IN");
	public static final SimpleDateFormat  DDMMYYYYFORMATS= new SimpleDateFormat("dd/MM/yyyy",LOCALE);
	private static final String  SEARCH="search";

	private List<Object> paramList;
	private List<ReportBean> reportList;
	private EgovCommon egovCommon;
	private WorksService worksService;
	private CommonsService commonsService;
	private InputStream pdfInputStream;
	private InputStream excelInputStream;
	private ReportService reportService;
	private WorkOrderService workOrderService;
	
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
	}
	
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {	
		String query = getQuery();	
		String countQry =  " select count(*) " + query +" ";
		return new SearchQueryHQL(query,countQry,paramList); 
	}
	
	@Override
	public String search() { 
		return SEARCH;
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
		String queryBeginning =" from MBBill mbbill where mbbill.egBillregister is not null ";
		
		dynQuery.append(" and mbbill.egBillregister.billstatus = ? ");
		paramList.add(ContractorBillRegister.BillStatus.APPROVED.toString());
		
		if(fromDate!=null && toDate!=null){ 
			dynQuery.append(" and trunc(mbbill.egBillregister.billdate) >= ? ");
			dynQuery.append(" and trunc(mbbill.egBillregister.billdate) <= ? ");
			paramList.add(new Date(DateUtils.getFormattedDate(fromDate,"dd-MMM-yyyy"))); 
			paramList.add(new Date(DateUtils.getFormattedDate(toDate,"dd-MMM-yyyy")));
		}
		
		dynQuery.append(" and mbbill.egBillregister.id in (select ebd.egBillregister.id from EgBilldetails ebd where ebd.glcodeid=? and ebd.egBillregister.id=mbbill.egBillregister.id ) ");
		CChartOfAccounts chartOfAccounts = getLabourWelfareCessAcoountCode();
		paramList.add(new BigDecimal(chartOfAccounts.getId()));
	  	dynQuery.append(" order by mbbill.mbHeader.workOrder.contractor.id,mbbill.mbHeader.workOrder.id ");
		return queryBeginning+dynQuery.toString();		
	}
	
	@SuppressWarnings("unchecked")
	protected void formatSearchResult() {		
		CChartOfAccounts chartOfAccounts = getLabourWelfareCessAcoountCode();
		reportList = new ArrayList<ReportBean>();
		EgovPaginatedList egovPaginatedList = (EgovPaginatedList)searchResult;
		List<MBBill> list = egovPaginatedList.getList();
		List<String> uniqueEntity = new ArrayList<String>(); // used to display the entity (contractor) name once.
		for(MBBill mbBill : list) {	
			
			ReportBean reportBean = new ReportBean();
			if(!uniqueEntity.contains(mbBill.getMbHeader().getWorkOrder().getContractor().getId().toString())){
				if(mbBill.getMbHeader().getWorkOrder().getContractor().getPaymentAddress()!=null)
					reportBean.setContractorNameAndAddress(mbBill.getMbHeader().getWorkOrder().getContractor().getName()+", "+mbBill.getMbHeader().getWorkOrder().getContractor().getPaymentAddress());
				else
					reportBean.setContractorNameAndAddress(mbBill.getMbHeader().getWorkOrder().getContractor().getName()); 
				
				uniqueEntity.add(mbBill.getMbHeader().getWorkOrder().getContractor().getId().toString());
			}
			else 
				reportBean.setContractorNameAndAddress("");
						
			
			DecimalFormat df = new DecimalFormat("###.##"); 
			reportBean.setWorkOrderValue(new BigDecimal(df.format(workOrderService.getRevisionEstimateWOAmount(mbBill.getMbHeader().getWorkOrder()))).setScale(2));
			
			List<Long> billIdList = new ArrayList<Long>();
			billIdList.add(mbBill.getEgBillregister().getId());
			
			Map<Long,Map<String,String>> billPaymentDetailMap = null;
			Map<String,String> paymentSumAndDateMap = null;
			billPaymentDetailMap = egovCommon.getTotalPaymentAmountAndPaymentDates(billIdList);
			if(billPaymentDetailMap != null)
				paymentSumAndDateMap = billPaymentDetailMap.get(mbBill.getEgBillregister().getId());
			
			if(paymentSumAndDateMap != null && paymentSumAndDateMap.get("amount") != null) {
				reportBean.setPaymentAmount(new BigDecimal(paymentSumAndDateMap.get("amount")).setScale(2));
			}
			if(paymentSumAndDateMap != null && paymentSumAndDateMap.get("date") != null) {
				reportBean.setPaymentDate(paymentSumAndDateMap.get("date"));  
			}	
							
			reportBean.setBillDate(DDMMYYYYFORMATS.format(mbBill.getEgBillregister().getBilldate()));
			for (EgBilldetails egbd : mbBill.getEgBillregister().getEgBilldetailes()) {
				if(egbd.getGlcodeid().equals(new BigDecimal(chartOfAccounts.getId()))) {
					reportBean.setTaxDeductedAmt(new BigDecimal(df.format(egbd.getCreditamount())).setScale(2));
				}
			}
							
			String remittanceDate = egovCommon.getDeductionDateList(billIdList, new Long(chartOfAccounts.getId())).get(mbBill.getEgBillregister().getId());	
			reportBean.setRemittedDate(remittanceDate);
			reportList.add(reportBean);
			
		}
		egovPaginatedList.setList(reportList);
	}
	
	@SuppressWarnings("unchecked")
	protected List<ReportBean> formatSearchResultForExport(List<MBBill> searchResult) {		
		CChartOfAccounts chartOfAccounts = getLabourWelfareCessAcoountCode();
		reportList = new ArrayList<ReportBean>();
		List<String> uniqueEntity = new ArrayList<String>(); // used to display the entity (contractor) name once.
		for(MBBill mbBill : searchResult) {	
			
			ReportBean reportBean = new ReportBean();
			if(!uniqueEntity.contains(mbBill.getMbHeader().getWorkOrder().getContractor().getId().toString())){
				if(mbBill.getMbHeader().getWorkOrder().getContractor().getPaymentAddress()!=null)
					reportBean.setContractorNameAndAddress(mbBill.getMbHeader().getWorkOrder().getContractor().getName()+", "+mbBill.getMbHeader().getWorkOrder().getContractor().getPaymentAddress());
				else
					reportBean.setContractorNameAndAddress(mbBill.getMbHeader().getWorkOrder().getContractor().getName()); 
				
				uniqueEntity.add(mbBill.getMbHeader().getWorkOrder().getContractor().getId().toString());
			}
			else 
				reportBean.setContractorNameAndAddress("");
							
			DecimalFormat df = new DecimalFormat("###.##"); 
			reportBean.setWorkOrderValue(new BigDecimal(df.format(workOrderService.getRevisionEstimateWOAmount(mbBill.getMbHeader().getWorkOrder()))).setScale(2));
			
			List<Long> billIdList = new ArrayList<Long>();
			billIdList.add(mbBill.getEgBillregister().getId());
			
			Map<Long,Map<String,String>> billPaymentDetailMap = null;
			Map<String,String> paymentSumAndDateMap = null;
			billPaymentDetailMap = egovCommon.getTotalPaymentAmountAndPaymentDates(billIdList);
			if(billPaymentDetailMap != null)
				paymentSumAndDateMap = billPaymentDetailMap.get(mbBill.getEgBillregister().getId());
			
			if(paymentSumAndDateMap != null && paymentSumAndDateMap.get("amount") != null) {
				reportBean.setPaymentAmount(new BigDecimal(paymentSumAndDateMap.get("amount")).setScale(2));
			}
			if(paymentSumAndDateMap != null && paymentSumAndDateMap.get("date") != null) {
				reportBean.setPaymentDate(paymentSumAndDateMap.get("date"));  
			}	
							
			reportBean.setBillDate(DDMMYYYYFORMATS.format(mbBill.getEgBillregister().getBilldate()));
			for (EgBilldetails egbd : mbBill.getEgBillregister().getEgBilldetailes()) {
				if(egbd.getGlcodeid().equals(new BigDecimal(chartOfAccounts.getId()))) {
					reportBean.setTaxDeductedAmt(new BigDecimal(df.format(egbd.getCreditamount())).setScale(2));
				}
			}
							
			String remittanceDate = egovCommon.getDeductionDateList(billIdList, new Long(chartOfAccounts.getId())).get(mbBill.getEgBillregister().getId());	
			reportBean.setRemittedDate(remittanceDate);
			reportList.add(reportBean);
			
		}
		return reportList; 
	}
	
	public String exportToPdf() throws JRException,Exception{
		Map<String,Object> reportParams = new HashMap<String,Object>();
		reportParams.put("reportSubTitle", getText("lwc.report.header"));
		ReportRequest reportRequest = new ReportRequest("LabourWelfareCessReport",getReportData(), reportParams);
		ReportOutput reportOutput = reportService.createReport(reportRequest); 
		if(reportOutput!=null && reportOutput.getReportOutputData()!=null)
			pdfInputStream = new ByteArrayInputStream(reportOutput.getReportOutputData()); 
		return EXPORTPDF; 
	}
	
	public String exportToExcel() throws JRException,Exception{
		Map<String,Object> reportParams = new HashMap<String,Object>();
		reportParams.put("reportSubTitle", getText("lwc.report.header"));
		ReportRequest reportRequest = new ReportRequest("LabourWelfareCessReport",getReportData(), reportParams);
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
	
	private CChartOfAccounts getLabourWelfareCessAcoountCode() {
		String lwcGlcode = worksService.getWorksConfigValue("LABOURWELFARECESS_GLCODE");
		return commonsService.getCChartOfAccountsByGlCode(lwcGlcode); 
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

	public void setEgovCommon(EgovCommon egovCommon) {
		this.egovCommon = egovCommon;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
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

}