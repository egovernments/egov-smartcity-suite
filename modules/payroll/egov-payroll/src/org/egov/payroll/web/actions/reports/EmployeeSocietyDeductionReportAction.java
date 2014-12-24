package org.egov.payroll.web.actions.reports;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.ParentPackage ;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.dispatcher.StreamResult;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.reporting.viewer.ReportViewerUtil;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQuerySQL;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.Query;
/**
 * Shows employeewise Salary deduction details.
 * @author Pradeep Kumar CM
 *
 */ 
@ParentPackage("egov") 
@SuppressWarnings("serial")
@Results({
@Result(name = "reportview", type = "stream", location = "returnStream", params = { "contentType", "${contentType}", "contentDisposition", "attachment; filename=${fileName}" })
})

public class EmployeeSocietyDeductionReportAction extends CommonSearchReport{  
	
	private static final int PAGESIZE = 25;  
	private Integer deductionSalId;
	private String contentType;
	private String fileName;
	private ReportService reportService;
	private InputStream returnStream;
	private String fileNamePrefix = "SalaryDeductions.";
	private String SALARY_DEDUCTION_TEMPLATE= "SalaryDeduction";
	protected String deductionSalStr;
	protected String deductionSalCode;
	private BigDecimal grandTotal=BigDecimal.ZERO;
	
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		List<Object> params=new ArrayList<Object>();
		params.add(PayrollConstants.PAYSLIP_MODULE);
		params.add(PayrollConstants.PAYSLIP_CANCELLED_STATUS);
		params.add(BigDecimal.valueOf(month));
		params.add(Long.valueOf(year));
		params.add(Integer.valueOf(department));
		params.add(Integer.valueOf(deductionSalId));
		if(billNumberId != 0){
			params.add(Integer.valueOf(billNumberId));
		}
		String countQuery=payrollReportService.getEmployeeSalaryDeductionQuery(month,year,department,billNumberId,deductionSalId,"countQuery");
		SearchQuery searchQuery=new SearchQuerySQL(payrollReportService.getEmployeeSalaryDeductionQuery(month,year,department,billNumberId,deductionSalId,"selectQuery"),
				countQuery, params); 
		return searchQuery;
	
	}
	
	@Override
	public void prepare(){         
		
		addDropdownData("finYearList", payrollExternalInterface.getAllActivePostingFinancialYear());
		addDropdownData("departmentList", payrollExternalInterface.getAllDepartments()); 
		/*addDropdownData("deductionList",HibernateUtil.getCurrentSession().createQuery("from SalaryCodes salarycode where" +
					" salarycode.categoryMaster.catType='D' order by salarycode.head").list());
		*/
		addDropdownData("deductionList", persistenceService.findAllBy("from SalaryCodes salarycode where  salarycode.categoryMaster.catType=?  " +
				"and salarycode.categoryMaster.name=? or salarycode.categoryMaster.name=? order by salarycode.head",
				PayrollConstants.CATEGORYMASTER_CATTYPE_DEDUCTUION, PayrollConstants.Deduction_Other,PayrollConstants.Deduction_Advance));
		addDropdownData("billNoList",Collections.EMPTY_LIST);
		
	}
	
	@SkipValidation
	@Override
	public String execute()
	{
		return "list";
	}
	@Override
	@ValidationErrorPage("list")
	public String search()   
	{		
		setPageSize(PAGESIZE);
		if(billNumber!=null && !billNumber.isEmpty()){
			billNumberHeading = "BillNumber: "+billNumber;
		}
		super.search();
	//	payrollReportService=new PayrollReportService();
		
		List<EmpDeductionInfo> deductionInfoList=populateIncomeTaxDeduction(searchResult.getList());
		searchResult.getList().clear();
		searchResult.getList().addAll(deductionInfoList);
		
		setSalaryDeductionList(searchResult); 
		System.out.println("Total records are " + (searchResult!=null ? searchResult.getFullListSize(): "0"));
		
		getGrandTotalByFieldParams();
		
		loadBillNoByDepartment(department);
		
		return "list";
	}

	private void getGrandTotalByFieldParams() {
		StringBuffer qryStr = new StringBuffer(500);
		qryStr.append(" SELECT SUM (amount) AS grosstotal ");
		qryStr.append(" FROM EGPAY_DEDUCTIONS deduction, EGPAY_EMPPAYROLL payroll , EG_EMP_ASSIGNMENT assignment,EG_EMPLOYEE emp "
				+ " WHERE deduction.ID_EMPPAYROLL=payroll.ID AND payroll.ID_EMP_ASSIGNMENT=assignment.ID "
				+ " AND (payroll.STATUS IN (SELECT egwstatus.ID FROM EGW_STATUS egwstatus WHERE egwstatus.MODULETYPE= :moduleType "
				+ " AND (egwstatus.DESCRIPTION NOT IN (:status)))) AND payroll.id_employee=emp.id ");
		
		if (month != 0) {
			qryStr.append("  AND payroll.MONTH=:month ");
		}
		if (year != 0) {
			qryStr.append(" AND payroll.FINANCIALYEARID= :finyear ");
		}
		if (department != 0) {
			qryStr.append(" and main_dept=:deptid ");
		}
		if (deductionSalId != 0) {
			qryStr.append(" AND  id_salcode=:salCodeId ");
		}
		if (billNumberId != 0) {
			qryStr.append(" AND  payroll.ID_BILLNUMBER=:billNumId ");
		}
	
		Query query=HibernateUtil.getCurrentSession().createSQLQuery(qryStr.toString());
		query.setString("moduleType", PayrollConstants.PAYSLIP_MODULE);
		query.setString("status",PayrollConstants.PAYSLIP_CANCELLED_STATUS);
		query.setInteger("month", month);
		query.setInteger("finyear", year);
		query.setInteger("deptid", department);
		query.setInteger("salCodeId", deductionSalId);
		if(billNumberId != 0){
			query.setInteger("billNumId", billNumberId);
		}
		
		 grandTotal=(BigDecimal) query.uniqueResult();
	}
	
private List populateIncomeTaxDeduction(List empList){
		
		List<Object[]> retList = empList; 
		List<EmpDeductionInfo> empDeductionInfoList = new ArrayList<EmpDeductionInfo>();

		for (Object[] objArray : retList){
			EmpDeductionInfo empDeductionInfo = new EmpDeductionInfo();
			empDeductionInfo.setEmpCode((String)objArray[0]);
			
			StringBuffer employeeName= new StringBuffer(200);
			
			if(objArray[1]!=null) {
				employeeName.append((String)objArray[1]+" ");
			}
			if(objArray[2]!=null) {
				employeeName.append((String)objArray[2]+" ");
			}
			if(objArray[3]!=null) {
				employeeName.append((String)objArray[3]+" ");
			}
			if(objArray[4]!=null) {
				employeeName.append((String)objArray[4]+" ");
			}
			
			 empDeductionInfo.setEmpName(employeeName.toString());
			
			empDeductionInfo.setAmount((BigDecimal)objArray[5]);
			
			empDeductionInfoList.add(empDeductionInfo);
		}
		return empDeductionInfoList;
	}

	public String exportReportToPdf()
	{
		if(billNumber!=null && !billNumber.isEmpty()){
			billNumberHeading = "BillNumber: "+billNumber;
		}
	    buildReportFormatByPassingType(FileFormat.PDF);
        return "reportview";

		
	}
	
	public String exportReportToExcel() {
		if(billNumber!=null && !billNumber.isEmpty()){
			billNumberHeading = "BillNumber: "+billNumber;
		}
        buildReportFormatByPassingType(FileFormat.XLS);
       return "reportview";
   }

	private void buildReportFormatByPassingType(FileFormat fileFormat) {
		Map<String, Object> reportParams = new HashMap<String, Object>();
		
		if(month!=null) 
			reportParams.put("month",DateUtils.getAllMonthsWithFullNames().get(month));
		else 
			reportParams.put("month", "");
		
		reportParams.put("year", getYearStr());
		reportParams.put("deductionType", getDeductionSalStr());
		reportParams.put("department", getDepartmentStr());
		reportParams.put("deductionSalCode", getDeductionSalCode());
		reportParams.put("billNo", getBillNumberHeading());
		
		setPageSize(-1); 
		super.search();
		List<EmpDeductionInfo> deductionInfoList=populateIncomeTaxDeduction(searchResult.getList());
		searchResult.getList().clear();
		searchResult.getList().addAll(deductionInfoList);
		
		setSalaryDeductionList(searchResult);
		reportParams.put("totalCount", (searchResult!=null ? searchResult.getFullListSize(): "0"));
		
		if((getSalaryDeductionList()!=null)&& getSalaryDeductionList().getList()!=null)
		{
			ReportRequest reportInput = new ReportRequest(
					SALARY_DEDUCTION_TEMPLATE, getSalaryDeductionList().getList(), reportParams);

			reportInput.setReportFormat(fileFormat);
			this.contentType = ReportViewerUtil.getContentType(fileFormat);
			this.fileName = fileNamePrefix + fileFormat.toString().toLowerCase();

			ReportOutput reportOutput = reportService.createReport(reportInput);

			if (reportOutput != null && reportOutput.getReportOutputData() != null)
				setReturnStream(new ByteArrayInputStream(
						reportOutput.getReportOutputData()));	
		}
	}


	private  void loadBillNoByDepartment(Integer deptId)
	{
		if(deptId != 0)
		  addDropdownData("billNoList",persistenceService.findAllBy("from BillNumberMaster b where  b.department.id = ? ",
				deptId));
	   else
		  addDropdownData("billNoList",Collections.EMPTY_LIST);
		
	}
	
	public Integer getDeductionSalId() {
		return deductionSalId;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setDeductionSalId(Integer deductionSalId) {
		this.deductionSalId = deductionSalId;
	}

	public void validate()
	{
		if (getMonth() == 0) {
			           addFieldError("month",  getText("month.required"));
			     }
		        if (getYear() == 0) {
			            addFieldError("year", getText("year.required"));
			   }
		        if (getDepartment() == 0) {
			           addFieldError("department",  getText("department.required")); 
			     }   
		        if(getDeductionSalId()==0) 
		        {
		        	   addFieldError("deductionSalId",  getText("deductionSalId.required"));
		        }else
			     {
		        	setDeductionSalCode( (String) persistenceService.find(" select head from SalaryCodes where id=?",getDeductionSalId())); 
			    	 
			     }
	}

	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public InputStream getReturnStream() {
		return returnStream;
	}

	public void setReturnStream(InputStream returnStream) {
		this.returnStream = returnStream;
	}

	public String getDeductionSalStr() {
		return deductionSalStr;
	}

	public void setDeductionSalStr(String deductionSalStr) {
		this.deductionSalStr = deductionSalStr;
	}

	public String getDeductionSalCode() {
		return deductionSalCode;
	}

	public void setDeductionSalCode(String deductionSalCode) {
		this.deductionSalCode = deductionSalCode;
	}

	public BigDecimal getGrandTotal() {
		return grandTotal;
	}
	
}
