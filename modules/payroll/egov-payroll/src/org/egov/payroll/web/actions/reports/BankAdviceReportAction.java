package org.egov.payroll.web.actions.reports;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts2.convention.annotation.ParentPackage ;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results ;
import org.apache.struts2.dispatcher.StreamResult;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.viewer.ReportViewerUtil;
import org.egov.infstr.services.EISServeable;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.payroll.reports.BankAdviceReportDTO;
import org.egov.payroll.services.reports.PayrollReportService;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;
 
@Results({ 
	@Result(name = "reportView", type = "stream", location = "fileStream", params = { "contentType", "${contentType}", "contentDisposition", "attachment; filename=${fileName}" })
	})
@ParentPackage("egov")
public class BankAdviceReportAction extends BaseFormAction 
{
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(BankAdviceReportAction.class);
	
	private static final String BANK_BRANCH_SUMMARY_TEMPLATE = "BankBranchSummary";
	private static final String BANK_ADVICE_TEMPLATE = "BankAdvice";
	private PayrollReportService payrollReportService;
	private PayrollExternalInterface payrollExternalInterface;
	
	private ReportService reportService;
	private ReportOutput ro;
	private Integer month;
	private Integer financialyearId;
	private Integer bankId;
	
	private Boolean groupByDept = false;
	private Boolean groupByEmployeeType = false;
	
	private List<BankAdviceReportDTO> bankAdviceList;
	private List<BankAdviceReportDTO> branchWiseSummaryList;
	private EISServeable eisService;
	
	private String contentType;
	private InputStream fileStream;
	private String fileName;
	private String fileFormat;
	private String monthName;
	private String financialYearRange;

	public List<BankAdviceReportDTO> getBankAdviceList() {
		return bankAdviceList;
	}
	public void setBankAdviceList(List<BankAdviceReportDTO> bankAdviceList) {
		this.bankAdviceList = bankAdviceList;
	}	

	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public Integer getFinancialyearId() {
		return financialyearId;
	}
	public void setFinancialyearId(Integer financialyearId) {
		this.financialyearId = financialyearId;
	}
	public PayrollReportService getPayrollReportService() {
		return payrollReportService;
	}
	public void setPayrollReportService(PayrollReportService payrollReportService) {
		this.payrollReportService = payrollReportService;
	}
		
	public PayrollExternalInterface getPayrollExternalInterface() {
		return payrollExternalInterface;
	}
	public void setPayrollExternalInterface(
			PayrollExternalInterface payrollExternalInterface) {
		this.payrollExternalInterface = payrollExternalInterface;
	}
	
	public Boolean getGroupByDept() {
		return groupByDept;
	}
	public void setGroupByDept(Boolean groupByDept) {
		this.groupByDept = groupByDept;
	}
	public Boolean getGroupByEmployeeType() {
		return groupByEmployeeType;
	}
	public void setGroupByEmployeeType(Boolean groupByEmployeeType) {
		this.groupByEmployeeType = groupByEmployeeType;
	}
	
	public Integer getBankId() {
		return bankId;
	}
	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}
	public List<BankAdviceReportDTO> getBranchWiseSummaryList() {
		return branchWiseSummaryList;
	}
	public void setBranchWiseSummaryList(
			List<BankAdviceReportDTO> branchWiseSummaryList) {
		this.branchWiseSummaryList = branchWiseSummaryList;
	}
	
	
	public ReportService getReportService() {
		return reportService;
	}
	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}
	
	
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public InputStream getFileStream() {
		return new ByteArrayInputStream(ro.getReportOutputData());
	}

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileFormat() {
		return fileFormat;
	}
	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}
	
	public String getMonthName() {
		if (month != null && month > 0) {
			Calendar monthCal = Calendar.getInstance();
			monthCal.set(Calendar.MONTH, month-1);
			monthCal.set(Calendar.DATE,1);
			monthName = monthCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
		}
		return monthName;
	}
	public String getFinancialYearRange() {
		if (financialyearId != null) {
			financialYearRange = payrollExternalInterface.findFinancialYearById(Long.valueOf(financialyearId)).getFinYearRange();
		}
		return financialYearRange;
	}
	@Override
	public Object getModel() {
		return null;
	}
	
	@Override
	public void prepare(){ 
		addDropdownData("deptList", getEisService().getDeptsForUser());
		addDropdownData("finYearList", payrollExternalInterface.getAllActivePostingFinancialYear());
		addDropdownData("bankList", EgovMasterDataCaching.getInstance().get("egEmp-Bank"));
		List<LabelValueBean> monthList = new ArrayList<LabelValueBean>();
		monthList.add(new LabelValueBean("Jan","1"));
		monthList.add(new LabelValueBean("Feb","2"));
		monthList.add(new LabelValueBean("Mar","3"));
		monthList.add(new LabelValueBean("Apr","4"));
		monthList.add(new LabelValueBean("May","5"));
		monthList.add(new LabelValueBean("Jun","6"));
		monthList.add(new LabelValueBean("Jul","7"));
		monthList.add(new LabelValueBean("Aug","8"));
		monthList.add(new LabelValueBean("Sep","9"));
		monthList.add(new LabelValueBean("Oct","10"));
		monthList.add(new LabelValueBean("Nov","11"));
		monthList.add(new LabelValueBean("Dec","12"));		
		addDropdownData("monthList",monthList);
	}
	
	@Override
	@SkipValidation
	public String execute() throws Exception {
		return "list";
	}
	
	@ValidationErrorPage(value="list")
	public String showBankAdviceList() throws Exception{
		
		bankAdviceList = payrollReportService.getBankAdviceForMonthAndFinYear(month, financialyearId, groupByDept, groupByEmployeeType);
		if (fileFormat != null && (!FileFormat.HTM.name().equals(fileFormat)) )
		{
			Map<String, Object> reportParams = new HashMap<String, Object>();
			reportParams.put("EGOV_REP_MONTH", getMonthName());
			reportParams.put("EGOV_REP_FINYEAR_RANGE", getFinancialYearRange());
			reportParams.put("EGOV_CROUPBY_DEPT", (groupByDept==null ? false: groupByDept));
			ReportRequest reportInput = new ReportRequest(BANK_ADVICE_TEMPLATE, bankAdviceList,reportParams);
			reportInput.setReportFormat(FileFormat.valueOf(fileFormat));
		
			this.contentType = ReportViewerUtil.getContentType(FileFormat.valueOf(fileFormat));
			this.fileName = "bankAdvice." + fileFormat.toLowerCase();
			ro = reportService.createReport(reportInput);
			return "reportView";
		}
		return "list";
	}
	
	@SkipValidation
	public String branchWiseReport() throws Exception {
		return "branchWise";
	}
	
	@ValidationErrorPage(value="branchWise")
	public String showBranchWiseReport() throws Exception {
		branchWiseSummaryList = payrollReportService.getBranchWiseSummaryForMonthAndFinYear(month, financialyearId, bankId, groupByEmployeeType);
		if (fileFormat != null && (!FileFormat.HTM.name().equals(fileFormat)) )
		{
			Map<String, Object> reportParams = new HashMap<String, Object>();
			reportParams.put("EGOV_REP_MONTH", getMonthName());
			reportParams.put("EGOV_REP_FINYEAR_RANGE", getFinancialYearRange());
			ReportRequest reportInput = new ReportRequest(BANK_BRANCH_SUMMARY_TEMPLATE, branchWiseSummaryList,reportParams);
			reportInput.setReportFormat(FileFormat.valueOf(fileFormat));
		
			this.contentType = ReportViewerUtil.getContentType(FileFormat.valueOf(fileFormat));
			this.fileName = "branchSummary." + fileFormat.toLowerCase();
			ro = reportService.createReport(reportInput);
			return "reportView";
		}
		return "branchWise";
	}
	
	
	@Override
	public void validate() {
		if (month == null || month == -1)
			addFieldError("month", getText("month.required"));
		if (financialyearId == null || financialyearId == -1)
			addFieldError("financialyearId", getText("financialyearId.required"));
			
	}
	
	public EISServeable getEisService() {
		return eisService;
	}
	public void setEisService(EISServeable eisService) {
		this.eisService = eisService;
	}
}
