package org.egov.payroll.web.actions.reports;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage ;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.dispatcher.StreamResult;
import org.displaytag.pagination.PaginatedList;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.reporting.viewer.ReportViewerUtil;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQuerySQL;
import org.egov.payroll.services.reports.PayrollReportService;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.annotation.ValidationErrorPage;
 
@Results({ 
	@Result(name = "reportview", type = "stream", location = "inputStream", params = { "contentType", "${contentType}", "contentDisposition", "attachment; filename=${fileName}" })
	})
@ParentPackage("egov")
public class FestivalAdvanceReportAction extends SearchFormAction {  
	
	private static final Logger LOGGER = Logger.getLogger(FestivalAdvanceReportAction.class);
	private PayrollExternalInterface payrollExternalInterface;
	private PayrollReportService payrollReportService;
	private Integer month=0;
	private String monthStr="";
	private Integer year=0;
	private Integer department;
	private String billNumber;
	private Integer billNumberId;
	private String billNumberHeading = "";
	private String fileName;
	private String fileFormat;
	private static final String FA_DEDUCTION_TEMPLATE="FestivalAdvanceDeductionReport";
	private String deptName;
	
	private ReportService reportService;
	private ReportOutput ro;
	private String contentType;
	private InputStream inputStream;
	private String yearStr;
	private BigDecimal grandTotal;
	public Object getModel() { 
		return null;   
	}
	
	public void prepare(){
		addDropdownData("finYearList", payrollExternalInterface.getAllActivePostingFinancialYear());
		addDropdownData("departmentList", payrollExternalInterface.getAllDepartments());
		addDropdownData("billNoList",Collections.EMPTY_LIST);
	}

	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		Map<String,Object> map=payrollReportService.getFestivalAdvanceReport(month,year,department, billNumberId);
		SearchQuery searchQuery=new SearchQuerySQL((String)map.get("select"),(String)map.get("count"),(List)map.get("params"));
		return searchQuery;
	}	
	
	@Override
	public String execute() {
		return INDEX;   
	}
	
	@Override
	@ValidationErrorPage("list")  
	public String search() {
		if(month<=0 || year<=0 || department<=0) {
			addFieldError("searchCriteria", "Please select all search criteria");
			return INDEX;
		}
		setPageSize(30);
		super.search();
		if(billNumber!=null && !billNumber.isEmpty()){
			billNumberHeading = "BillNumber: "+billNumber;
		}
		//payrollReportService=new PayrollReportService();
		setYearStr(getFinancialYearRange())	;
		setDeptName(payrollExternalInterface.getDepartment(department).getDeptName());
		setMonthStr(getMonthName());
		if(searchResult.getFullListSize()>0) {
			List<EmpDeductionInfo> festivalAdvanceList = populateFestivalAdvance(searchResult.getList());
			searchResult.getList().clear();
			searchResult.getList().addAll(festivalAdvanceList);
			  if((getPageSize()*(getPage()-1)+searchResult.getList().size())==searchResult.getFullListSize()) {
					grandTotal=payrollReportService.getTotalFestiveAdvDeduction(month, year, department,billNumberId);
				}
		}
		loadBillNoByDepartment(department);
		return INDEX;		
	}
	
	@ValidationErrorPage(INDEX)  
	public String exportFestivalAdvancePDF() {
		if(billNumber!=null && !billNumber.isEmpty()){
			billNumberHeading = "BillNumber: "+billNumber;
		}
		loadBillNoByDepartment(department);
		Map<String, Object> reportParams = new HashMap<String, Object>();
		if(month<=0 || year<=0 || department<=0) {
			addFieldError("searchCriteria", "Please select all search criteria");
			return INDEX;
		}
		
		reportParams.put("month", getMonthName());
		reportParams.put("year", getFinancialYearRange());
		reportParams.put("departmentName",payrollExternalInterface.getDepartment(department).getDeptName());
		List festivalAdvanceList = payrollReportService.getFestivalAdvanceReportMonthYearDept(month,year,department, billNumberId);
		List<EmpDeductionInfo> empDeductionInfoList =(List<EmpDeductionInfo>) populateFestivalAdvance(festivalAdvanceList);
		reportParams.put("noOfEmp", empDeductionInfoList.size());
		reportParams.put("billNo",getBillNumberHeading());
		ReportRequest reportInput = new ReportRequest(FA_DEDUCTION_TEMPLATE,empDeductionInfoList, reportParams);
		reportInput.setReportFormat(FileFormat.PDF);
		this.contentType = ReportViewerUtil.getContentType(FileFormat.PDF);
		this.fileName="FestivalAdvanceDeduction." + FileFormat.PDF.toString().toLowerCase();
		ReportOutput reportOutput = reportService.createReport(reportInput);   
		if (reportOutput != null && reportOutput.getReportOutputData() != null)
			inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());

		return "reportview";
	}
	
	@ValidationErrorPage(INDEX)  
	public String exportFestivalAdvanceExcel() {
		if(billNumber!=null && !billNumber.isEmpty()){
			billNumberHeading = "BillNumber: "+billNumber;
		}
		Map<String, Object> reportParams = new HashMap<String, Object>();
		loadBillNoByDepartment(department);

		if(month<=0 || year<=0 || department<=0) {
			addFieldError("searchCriteria", "Please select all search criteria");
			return INDEX;
		}
		reportParams.put("month", getMonthName());
		reportParams.put("year", getFinancialYearRange());
		reportParams.put("departmentName",payrollExternalInterface.getDepartment(department).getDeptName());
		List festivalAdvanceList = payrollReportService.getFestivalAdvanceReportMonthYearDept(month,year,department, billNumberId);
		List<EmpDeductionInfo> empDeductionInfoList =(List<EmpDeductionInfo>) populateFestivalAdvance(festivalAdvanceList);
		reportParams.put("noOfEmp", empDeductionInfoList.size());
		reportParams.put("billNo",getBillNumberHeading());
		ReportRequest reportInput = new ReportRequest(FA_DEDUCTION_TEMPLATE,empDeductionInfoList, reportParams);
		reportInput.setReportFormat(FileFormat.XLS);
		this.contentType = ReportViewerUtil.getContentType(FileFormat.XLS);
		this.fileName="FestivalAdvanceDeduction." + FileFormat.XLS.toString().toLowerCase();
		ReportOutput reportOutput = reportService.createReport(reportInput);   
		if (reportOutput != null && reportOutput.getReportOutputData() != null)
			inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());

		return "reportview";
	}

	
	private List populateFestivalAdvance(List empList){		
		List<Object[]> retList = empList; 
		List<EmpDeductionInfo> empDeductionInfoList = new ArrayList<EmpDeductionInfo>();
		for (Object[] objArray : retList){
			EmpDeductionInfo empDeductionInfo = new EmpDeductionInfo();
			empDeductionInfo.setEmpCode((String)objArray[0]);
			if(objArray[2]!=null) {
				empDeductionInfo.setEmpName((String)objArray[2]+" "+(String)objArray[1]);
			}
			else {
				empDeductionInfo.setEmpName((String)objArray[1]);
			}
			empDeductionInfo.setAmount(((BigDecimal)objArray[3]));
			empDeductionInfo.setInstallmentNo((BigDecimal)objArray[5]+"/"+(BigDecimal)objArray[4]);
			empDeductionInfoList.add(empDeductionInfo);
		}
		return empDeductionInfoList;
	}

	private  void loadBillNoByDepartment(Integer deptId)
	{
		if(deptId != 0)
		  addDropdownData("billNoList",persistenceService.findAllBy("from BillNumberMaster b where  b.department.id = ? ",
				deptId));
	   else
		  addDropdownData("billNoList",Collections.EMPTY_LIST);
		
	}
	public PayrollReportService getPayrollReportService() {
		return payrollReportService;
	}

	public void setPayrollReportService(PayrollReportService payrollReportService) {
		this.payrollReportService = payrollReportService;
	}

	public String getFileFormat() {
		return fileFormat;
	}

	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getDepartment() {
		return department;
	}

	public void setDepartment(Integer department) {
		this.department = department;
	}

	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public ReportOutput getRo() {
		return ro;
	}

	public void setRo(ReportOutput ro) {
		this.ro = ro;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public PayrollExternalInterface getPayrollExternalInterface() {
		return payrollExternalInterface;
	}
	public void setPayrollExternalInterface(
			PayrollExternalInterface payrollExternalInterface) {
		this.payrollExternalInterface = payrollExternalInterface;
	}
	
	public String getMonthName() {
		if (month != null && month > 0) {
			Calendar monthCal = Calendar.getInstance();
			monthCal.set(Calendar.MONTH, month-1);
			monthCal.set(Calendar.DATE,1);
			monthStr = monthCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
		}
		return monthStr;
	}
	public String getFinancialYearRange() {
		if (year != null) {
			yearStr = payrollExternalInterface.findFinancialYearById(Long.valueOf(year)).getFinYearRange();
		}
		return yearStr;
	}
	public String getMonthStr() {
		return monthStr;
	}
	public void setMonthStr(String monthStr) {
		this.monthStr = monthStr;
	}
	public String getYearStr() {
		return yearStr;
	}
	public void setYearStr(String yearStr) {
		this.yearStr = yearStr;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public Integer getBillNumberId() {
		return billNumberId;
	}

	public void setBillNumberId(Integer billNumberId) {
		this.billNumberId = billNumberId;
	}

	public String getBillNumberHeading() {
		return billNumberHeading;
	}

	public void setBillNumberHeading(String billNumberHeading) {
		this.billNumberHeading = billNumberHeading;
	}

	public BigDecimal getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(BigDecimal grandTotal) {
		this.grandTotal = grandTotal;
	}
	
	
} 