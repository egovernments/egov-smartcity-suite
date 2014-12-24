package org.egov.payroll.web.actions.reports;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.dispatcher.StreamResult;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.displaytag.pagination.PaginatedList;
import org.egov.commons.CFinancialYear;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.reporting.viewer.ReportViewerUtil;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQuerySQL;
import org.egov.payroll.services.reports.PayrollReportService;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.annotation.ValidationErrorPage;

@Results({ 
	@Result(name = "reportView", type = "stream", location = "fileStream", params = { "contentType", "${contentType}", "contentDisposition", "attachment; filename=${fileName}" })
	})
@ParentPackage("egov")
public class PFDeductionReportAction extends SearchFormAction {
	private Integer month=0;
	private String monthStr="";
	private Integer year=0;
	private Integer department;
	private String deptName;
	private PayrollReportService payrollReportService;
	private PayrollExternalInterface payrollExternalInterface;
	private PaginatedList pfPaginatedList;
	private List<PFDeductionDTO> pfList;
	private String fileName;
	private String fileFormat;
	private static final String PF_DEDUCTION_TEMPLATE="PFDeductionReport";
	
	private ReportService reportService;
	private ReportOutput ro;
	private String contentType;
	private InputStream fileStream;
	private String yearStr;
	private BigDecimal totalPF;
	private BigDecimal totalPFAdv;
	private BigDecimal grandTotal;
	private String billNumber;
	private Integer billNumberId;
	private String billNumberHeading;
	
	public String getBillNumberHeading() {
		return billNumberHeading;
	}
	public void setBillNumberHeading(String billNumberHeading) {
		this.billNumberHeading = billNumberHeading;
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
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void prepare(){ 
		addDropdownData("finYearList", payrollExternalInterface.getAllActivePostingFinancialYear());
		addDropdownData("departmentList", payrollExternalInterface.getAllDepartments());
		addDropdownData("billNoList",Collections.EMPTY_LIST);
	}

	@Override
	public SearchQuery prepareQuery(String arg0, String arg1) {
		if(null==billNumber || billNumber.equalsIgnoreCase(""))
		{
			billNumberId=0;
		}
		Map<String,Object> map=payrollReportService.getPFReportQuery(month,year,department,billNumberId.toString());
		SearchQuery searchQuery=new SearchQuerySQL((String)map.get("select"),(String)map.get("count"),(List)map.get("params"));
		return searchQuery;
	}
	@SkipValidation
	public String execute()  
	{
		return INDEX;   
	}
	
	@Override
	@ValidationErrorPage("list")  
	public String search()   
	{
		
		setPageSize(15);
		super.search();
		setYearStr(getFinancialYear())	;
		if((!billNumber.equalsIgnoreCase("")) && (!billNumber.isEmpty()))
        {
			setBillNumberHeading("Bill Number : "+billNumber);
        }
		
		setDeptName(payrollExternalInterface.getDepartment(department).getDeptName());
		setMonthStr(getMonthName());
		List<PFDeductionDTO> dedList=getDTOList(searchResult.getList()); 
		searchResult.getList().clear();
		searchResult.getList().addAll(dedList);
		setPfPaginatedList(searchResult);
		
	   loadBillNoByDepartment(department);
		return "list";
		
	}
	private List getDTOList(List<Object[]> objArrList)
	{
		List<PFDeductionDTO> dtoList = new ArrayList<PFDeductionDTO>();
		String empcode="";
		for (Object[] objArray : objArrList){
			
			PFDeductionDTO pfhelper = new PFDeductionDTO();
			pfhelper.setCode((String)objArray[1]);
			pfhelper.setSalaryCode((String)objArray[3]);
			if(!dtoList.isEmpty() && empcode.equals(pfhelper.getCode()))//prev and current emp code are equal
			{
				PFDeductionDTO beforePfhelper = dtoList.get(dtoList.size()-1);
				if(pfhelper.getSalaryCode().equals(PayrollConstants.SALCODE_GPF_ADV))//check prev head is GPFSUB,
				{
					beforePfhelper.setGPFADVamt((BigDecimal)objArray[5]);
					beforePfhelper.setNoofinst((BigDecimal)objArray[9]);
					beforePfhelper.setInstno((BigDecimal)objArray[10]);
				}
				else if(pfhelper.getSalaryCode().equals(PayrollConstants.SALCODE_GPF))
				{
					beforePfhelper.setGPFSUbamt((BigDecimal)objArray[4]);
				}
				continue;
			}
			empcode=(String)objArray[1];
			pfhelper.setPFNum((String)objArray[0]);
			pfhelper.setName((String)objArray[2]);
			pfhelper.setGPFSUbamt((BigDecimal)objArray[4]);
			pfhelper.setGPFADVamt((BigDecimal)objArray[5]);
			pfhelper.setNoofinst((BigDecimal)objArray[9]);
			pfhelper.setInstno((BigDecimal)objArray[10]);
			dtoList.add(pfhelper);
		}
		return dtoList;
	}
	@ValidationErrorPage(value="list")
	public String showPFList() throws Exception{ 
		
		List<Object[]> objArrList = (List<Object[]>)payrollReportService.getPFReportMonthYearDept(month,year,department,billNumberId.toString());
		setPfList(getDTOList(objArrList));
       
		if (fileFormat != null && (!FileFormat.HTM.name().equals(fileFormat)) )
		{
			Map<String, Object> reportParams = new HashMap<String, Object>();
			reportParams.put("month", getMonthName());
			reportParams.put("year", getFinancialYear());
			reportParams.put("departmentName",payrollExternalInterface.getDepartment(department).getDeptName());
			if(billNumberId != 0 )
	        {
				reportParams.put("billNum","Bill Number : "+billNumber);
	        }else{
	        	reportParams.put("billNum","");
	        }
			reportParams.put("numOfRecords",pfList.size());
			
			ReportRequest reportInput = new ReportRequest(PF_DEDUCTION_TEMPLATE, pfList,reportParams);
			reportInput.setReportFormat(FileFormat.valueOf(fileFormat));
		
			this.contentType = ReportViewerUtil.getContentType(FileFormat.valueOf(fileFormat));
			this.fileName = "PFDeductionReport." + fileFormat.toLowerCase();
			ro = reportService.createReport(reportInput);
			return "reportView";
		}
		return "list";
	}
	@Override
	public void validate() {
		if (month == null || month <=0)
			addFieldError("month", getText("month.required"));
		if (year == null || year <=0)
			addFieldError("year", getText("year.required"));
		if (department == null || department <=0)
			addFieldError("department", getText("department.required"));			
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

	public InputStream getFileStream() {
		return new ByteArrayInputStream(ro.getReportOutputData());
	}

	public void setFileStream(InputStream fileStream) {
		this.fileStream = fileStream;
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
	public String getFinancialYear() {
		if (year != null) {
			CFinancialYear finYear=payrollExternalInterface.findFinancialYearById(Long.valueOf(year));
			if((month-1)<finYear.getStartingDate().getMonth())
			{
				yearStr=String.valueOf(finYear.getEndingDate().getYear()+1900);
			}
			else
				yearStr=String.valueOf(finYear.getStartingDate().getYear()+1900);
		}
		System.out.println("yearStr "+yearStr);
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
	public PaginatedList getPfPaginatedList() {
		return pfPaginatedList;
	}
	public void setPfPaginatedList(PaginatedList pfPaginatedList) {
		this.pfPaginatedList = pfPaginatedList;
	}
	public List getPfList() {
		return pfList;
	}
	public void setPfList(List pfList) {
		this.pfList = pfList;
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
	public BigDecimal getTotalPF() {
		List<Object[]> totObjArr=payrollReportService.getTotalForPFReportMonthYearDept(month, year, department,billNumberId.toString());
		for(Object[] obj:totObjArr)
		{
			totalPF=(BigDecimal)obj[0];
			totalPFAdv=(BigDecimal)obj[1];
			grandTotal=(BigDecimal)obj[2];
		}
		return totalPF;
	}
	public BigDecimal getTotalPFAdv() {
		return totalPFAdv;
	}
	public BigDecimal getGrandTotal() {
		return grandTotal;
	}
	public boolean getLastpage() {
		BigDecimal divident=new BigDecimal(searchResult.getFullListSize());
		BigDecimal divisor=new BigDecimal(getPageSize());
		divisor=divident.divide(divisor, RoundingMode.CEILING);//example 32/15 is 2.1that leads to 3rd page so using ceiling
		return divisor.intValue()==getPage();
	}
	
	
	
}
