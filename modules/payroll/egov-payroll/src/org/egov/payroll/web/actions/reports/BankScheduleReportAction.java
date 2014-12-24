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

import org.apache.struts2.convention.annotation.ParentPackage ;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.dispatcher.StreamResult;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.reporting.viewer.ReportViewerUtil;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQuerySQL;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.dept.Department;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.Query;

@Results({ 
	@Result(name = "reportview", type = "stream", location = "inputStream", params = { "contentType", "${contentType}", "contentDisposition", "attachment; filename=${fileName}" })
	})
@ParentPackage("egov")
public class BankScheduleReportAction extends SearchFormAction { 
	
	private PayrollExternalInterface payrollExternalInterface;
	private Integer month=0;
	private String monthStr="";
	private Integer year=0;
	private String yearStr="";
	private Integer department;
	private String sourcepage="";
	private InputStream inputStream;
	private String contentType;
	private String fileName;
	private ReportService reportService;
	private String deptStr;
    private static final String BANK_SCHEDULE_TEMPLATE="bankSchedule";
    private BigDecimal grandtotalGrossPay;
    private BigDecimal grandtotalNetPay;
	private String billNumber;
	private Integer billNumberId;
	private String billNumberHeading="";
	
	
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void prepare(){
		addDropdownData("finYearList", payrollExternalInterface.getAllActivePostingFinancialYear());
		addDropdownData("departmentList", payrollExternalInterface.getAllDepartments());
		addDropdownData("billNoList",Collections.EMPTY_LIST);
	}
	
	@Override
	@ValidationErrorPage(INDEX)  
	public String search() {
		if(billNumber!=null && !billNumber.isEmpty()){
			billNumberHeading = "BillNumber: "+billNumber;
		}
		setPageSize(30);
		if(month<=0 || year<=0 || department<=0) {
			addFieldError("searchCriteria", "Please select all search criteria");
			return INDEX;
		}

		super.search();

		if(searchResult.getFullListSize()>0) {
			List<EmpDeductionInfo> deductionInfoList=populateBankSchedule(searchResult.getList());
			searchResult.getList().clear();
			searchResult.getList().addAll(deductionInfoList);
			if((getPageSize()*(getPage()-1)+searchResult.getList().size())==searchResult.getFullListSize()) {
				setTotalBankSchedule(month, year, department, billNumberId);
			}
		}
		loadBillNoByDepartment(department);

        return INDEX;  
	}
	
	public Map getEmpBankScheduleQueryAndParams(Integer month, Integer financialYearId, Integer deptId, Integer billNumId) {
		HashMap<String,Object> queryAndParams=new HashMap<String,Object>();
        String billNumberCond = "";
        if(billNumId != null && billNumId > 0){  
        	billNumberCond = " and payroll.ID_BILLNUMBER=? ";
        }
		String query="select b.name,branch.branchname,emp.code, upper(DECODE(NVL(emp.gender,0),'F','Ms. ',DECODE(NVL(emp.gender,0),'M','Mr. ',''))||emp.name), " +
				"bank.account_number, sum(payroll.gross_pay), sum(payroll.net_pay) " +
				"from egpay_emppayroll payroll inner join eg_employee emp on payroll.id_employee=emp.id inner join " +
				"EGEIS_BANK_DET bank on emp.id=bank.id inner join eg_emp_assignment assig on payroll.id_emp_assignment= assig.id " +
				"inner join egw_status status on payroll.status= status.id inner join bank b on bank.bank=b.id inner join bankbranch branch " +
				"on bank.branch= branch.id " +
				"where payroll.month=? and payroll.financialyearid=? and assig.main_dept=? and status.code!=? " +billNumberCond+
				"group by  b.name,branch.branchname,emp.code, bank.account_number, emp.name,emp.gender order by b.name,branch.branchname";

		
		String countQuery="select count(distinct(emp.id)) from egpay_emppayroll payroll " +
				"inner join eg_employee emp on payroll.id_employee=emp.id inner join EGEIS_BANK_DET bank on emp.id=bank.id " +
				"inner join eg_emp_assignment assig on payroll.id_emp_assignment= assig.id " +
				"inner join egw_status status on payroll.status= status.id inner join bank b on bank.bank=b.id inner join bankbranch branch " +
				"on bank.branch= branch.id where payroll.month=? and payroll.financialyearid=? " +
				"and assig.main_dept=? and status.code!=? "+billNumberCond;
		List<Object> paramList = new ArrayList<Object>();

		paramList.add(month);
		paramList.add(financialYearId);
		paramList.add(deptId);
		paramList.add(PayrollConstants.PAYSLIP_CANCELLED_STATUS);
        if(billNumId != null && billNumId > 0){
        	paramList.add(billNumId);
        }

		queryAndParams.put("query", query);
		queryAndParams.put("countQuery", countQuery);
		queryAndParams.put("params", paramList);
		
		return queryAndParams;
	}
	
    private List populateBankSchedule(List empList){
		
		List<Object[]> retList = empList; 
		List<EmpDeductionInfo> empDeductionInfoList = new ArrayList<EmpDeductionInfo>();

		for (Object[] objArray : retList){
			EmpDeductionInfo empDeductionInfo = new EmpDeductionInfo();
			empDeductionInfo.setBank((String)objArray[0]);
			empDeductionInfo.setBranch((String)objArray[1]);
			empDeductionInfo.setEmpCode((String)objArray[2]);
			empDeductionInfo.setEmpName((String)objArray[3]);
			empDeductionInfo.setAccountNumber((String)objArray[4]);
			empDeductionInfo.setGrossSalary(((BigDecimal)objArray[5]));
			empDeductionInfo.setNetSalary(((BigDecimal)objArray[6]));
			empDeductionInfoList.add(empDeductionInfo);
		}
		return empDeductionInfoList;
	}
	
	@ValidationErrorPage(INDEX)  
	public String exportPDF() {
		Map<String, Object> reportParams = new HashMap<String, Object>();
		if(billNumber!=null && !billNumber.isEmpty()){
			billNumberHeading = "BillNumber: "+billNumber;
		}
		loadBillNoByDepartment(department);
		if(month<=0 || year<=0 || department<=0) {
			addFieldError("searchCriteria", "Please select all search criteria");
			return INDEX;
		}
		reportParams.put("month", getMonthName(month));
		reportParams.put("year", getFinancialYearRange(year));
		reportParams.put("deptName",getDeptName(department));
		List<EmpDeductionInfo> empDeductionInfoList =(List<EmpDeductionInfo>) getBankScheduleList();
		reportParams.put("noOfEmp", empDeductionInfoList.size());
		reportParams.put("billNo", getBillNumberHeading());
		ReportRequest reportInput = new ReportRequest(BANK_SCHEDULE_TEMPLATE,empDeductionInfoList, reportParams);
		reportInput.setReportFormat(FileFormat.PDF);
		this.contentType = ReportViewerUtil.getContentType(FileFormat.PDF);
		this.fileName="bankSchedule." + FileFormat.PDF.toString().toLowerCase();
		ReportOutput reportOutput = reportService.createReport(reportInput);   
		if (reportOutput != null && reportOutput.getReportOutputData() != null)
			inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());

		return "reportview";
	}

	@ValidationErrorPage(INDEX)  
	public String exportExcel() {
		Map<String, Object> reportParams = new HashMap<String, Object>();
		if(billNumber!=null && !billNumber.isEmpty()){
			billNumberHeading = "BillNumber: "+billNumber;
		}
		loadBillNoByDepartment(department);
		if(month<=0 || year<=0 || department<=0) {
			addFieldError("searchCriteria", "Please select all search criteria");
			return INDEX;
		}
		reportParams.put("month", getMonthName(month));
		reportParams.put("year", getFinancialYearRange(year));
		reportParams.put("deptName",getDeptName(department));
		List<EmpDeductionInfo> empDeductionInfoList =(List<EmpDeductionInfo>) getBankScheduleList();
		reportParams.put("noOfEmp", empDeductionInfoList.size());
		reportParams.put("billNo", getBillNumberHeading());
		ReportRequest reportInput = new ReportRequest(BANK_SCHEDULE_TEMPLATE,empDeductionInfoList, reportParams);
		reportInput.setReportFormat(FileFormat.XLS);
		this.contentType = ReportViewerUtil.getContentType(FileFormat.XLS);
		this.fileName="bankSchedule." + FileFormat.XLS.toString().toLowerCase();
		ReportOutput reportOutput = reportService.createReport(reportInput);   
		if (reportOutput != null && reportOutput.getReportOutputData() != null)
			inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());

		return "reportview";
	}
	
	private List getBankScheduleList() {
		Map queryParams=getEmpBankScheduleQueryAndParams(month,year,department,billNumberId);
		Query query=HibernateUtil.getCurrentSession().createSQLQuery((String)queryParams.get("query"));
		Object[] params=((List<Object>)queryParams.get("params")).toArray();
		
		if(params != null && params.length > 0) {
			for (int index = 0; index < params.length; index++) {
				query.setParameter(index, params[index]);
			}
		}

		List<Object[]> retList = query.list();     
		if(retList!=null && !retList.isEmpty()) {
			return populateBankSchedule(query.list());
		}
		else {
			return Collections.EMPTY_LIST;
		}
	}

	private void setTotalBankSchedule(Integer month, Integer financialYearId, Integer deptId, Integer billNumId) {
		String billNumberCond = "";
        if(billNumId != null){
        	billNumberCond = " and payroll.ID_BILLNUMBER=:billNumId ";
        }
		String qry="select sum(nvl(payroll.gross_pay,0)),sum(nvl(payroll.net_pay,0)) from egpay_emppayroll payroll " +
				"inner join eg_employee emp on payroll.id_employee=emp.id inner join EGEIS_BANK_DET bank on emp.id=bank.id " +
				"inner join eg_emp_assignment assig on payroll.id_emp_assignment= assig.id " +
				"inner join egw_status status on payroll.status= status.id inner join bank b on bank.bank=b.id inner join bankbranch branch " +
				"on bank.branch= branch.id where payroll.month=:month and payroll.financialyearid=:finyear " +
				"and assig.main_dept=:deptId and status.code!=:status "+billNumberCond;
		
		Query query=HibernateUtil.getCurrentSession().createSQLQuery(qry);
		query.setString("status",PayrollConstants.PAYSLIP_CANCELLED_STATUS);
		query.setInteger("month", month);
		query.setInteger("finyear", financialYearId);
		query.setInteger("deptId", deptId);
        if(billNumId != null){
        	query.setInteger("billNumId", billNumId);
        }
		
		
		List<Object[]> retList = query.list();
		if(!retList.isEmpty()) {
			Object[] objArray=retList.get(0);
			grandtotalGrossPay=(BigDecimal) objArray[0];
			grandtotalNetPay=(BigDecimal) objArray[1];
		}
		else {
			grandtotalGrossPay=BigDecimal.ZERO;
			grandtotalNetPay=BigDecimal.ZERO;
		}
	}
	
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		List<Object> paramList = null;
		String query=null;
		String countQuery=null;
        Map queryParams=getEmpBankScheduleQueryAndParams(month, year, department,billNumberId);
        paramList=(List<Object>)queryParams.get("params");
        query=(String) queryParams.get("query");
        countQuery=(String) queryParams.get("countQuery");

        return new  SearchQuerySQL(query, countQuery, paramList);
	}
	
	public String getMonthName(Integer month) {
		String monthName=null;
		if (month != null && month > 0) {
			Calendar monthCal = Calendar.getInstance();
			monthCal.set(Calendar.MONTH, month-1);
			monthCal.set(Calendar.DATE,1);
			monthName = monthCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
		}
		return monthName;
	}
	
	public String getDeptName(Integer deptId) {
		Department dept=payrollExternalInterface.getDepartment(deptId);
		if(dept!=null) {
			return dept.getDeptName().toUpperCase();
		}
		else {
			return "";
		}
	}

	public String getFinancialYearRange(Integer year) {
		String financialYearRange=null;
		if (year != null) {
			financialYearRange = payrollExternalInterface.findFinancialYearById(Long.valueOf(year)).getFinYearRange();
		}
		return financialYearRange;
	}
	private  void loadBillNoByDepartment(Integer deptId)
	{
		if(deptId != 0)
		  addDropdownData("billNoList",persistenceService.findAllBy("from BillNumberMaster b where  b.department.id = ? ",
				deptId));
	   else
		  addDropdownData("billNoList",Collections.EMPTY_LIST);
		
	}

	public void setPayrollExternalInterface(
			PayrollExternalInterface payrollExternalInterface) {
		this.payrollExternalInterface = payrollExternalInterface;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public String getMonthStr() {
		return monthStr;
	}

	public void setMonthStr(String monthStr) {
		this.monthStr = monthStr;
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

	public String getSourcepage() {
		return sourcepage;
	}

	public void setSourcepage(String sourcepage) {
		this.sourcepage = sourcepage;
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public BigDecimal getGrandtotalGrossPay() {
		return grandtotalGrossPay;
	}

	public BigDecimal getGrandtotalNetPay() {
		return grandtotalNetPay;
	}

	public String getDeptStr() {
		return getDeptName(department);
	}

	public String getYearStr() {
		return getFinancialYearRange(year);
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
}
