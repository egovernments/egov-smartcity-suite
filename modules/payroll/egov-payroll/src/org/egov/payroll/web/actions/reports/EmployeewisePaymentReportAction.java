package org.egov.payroll.web.actions.reports;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.displaytag.pagination.PaginatedList;
import org.egov.commons.CFinancialYear;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.payroll.services.reports.PayrollReportService;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.annotation.ValidationErrorPage;
/**
 * to display EmployeewisePayment by bank and branch
 * @author suhasini
 *
 */
@ParentPackage("egov") 
public class EmployeewisePaymentReportAction extends SearchFormAction{  
	
	private static final long serialVersionUID = 80471657602070730L;
	private static final Logger LOGGER = Logger.getLogger(EmployeewisePaymentReportAction.class);
	
	
	private Integer month=0;
	private String monthStr="";
	private Integer year=0;
	private String yearStr="";
	private Integer bank;
	private Date todayDate;
	
	private boolean groupByEmpType=false;
	private PayrollReportService payrollReportService;
	private PayrollExternalInterface payrollExternalInterface;
	private PaginatedList empwisePaymentList;

	@Override
	public Object getModel() {   
		// TODO Auto-generated method stub
		return null; 
	}
	
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		List<Object> params=new ArrayList<Object>();
		params.add(BigDecimal.valueOf(month));
		params.add(Long.valueOf(year));
		String countQuery=payrollReportService.getCountEmpwisePaymentAdviceForMonthAndFinYear(bank, groupByEmpType);
		SearchQuery searchQuery=new SearchQueryHQL(payrollReportService.getEmpwisePaymentAdviceForMonthAndFinYear(bank, groupByEmpType),
				countQuery, params);
		return searchQuery;
	}
	
	
	@Override
	public void prepare(){         
		
		addDropdownData("finYearList", payrollExternalInterface.getAllActivePostingFinancialYear());
		addDropdownData("empBankList", HibernateUtil.getCurrentSession().createQuery(" from Bank order by name asc ").list());  

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
		setPageSize(15);
		super.search();
		payrollReportService=new PayrollReportService();
		CFinancialYear finyear=payrollExternalInterface.findFinancialYearById(Long.valueOf(getYear()));
		setTodayDate(new Date());
		this.yearStr=setYearStr(finyear.getStartingDate(),getMonth());	
		setEmpwisePaymentList(searchResult);
		return "list";
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
	public Integer getBank() {
		return bank;
	}
	public void setBank(Integer bank) {
		this.bank = bank;
	}
	public boolean getGroupByEmpType() {
		return groupByEmpType;
	}
	public void setGroupByEmpType(boolean groupByEmpType) {
		this.groupByEmpType = groupByEmpType;
	}
	public void validate()
	{
		if (getMonth() == 0) {
			           addFieldError("month",  getText("month.required"));
			     }
		        if (getYear() == 0) {
			            addFieldError("year", getText("year.required"));
			   }
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
	public PaginatedList getEmpwisePaymentList() {
		return empwisePaymentList;
	}
	private void setEmpwisePaymentList(PaginatedList empwisePaymentList) {
		this.empwisePaymentList = empwisePaymentList;
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
	public Date getTodayDate() {
		return todayDate;
	}
	private void setTodayDate(Date todayDate) {
		this.todayDate = todayDate;
	}
	private static String  setYearStr(Date  finYearStartDate,Integer givenMonth) {
		Calendar cal=Calendar.getInstance();
		cal.setTime(finYearStartDate);
		cal.add(cal.MONTH, 1);//since month starts from zero onwards
		if(cal.get(cal.MONTH)>givenMonth )//if the month select is jan-mar then dd 1 year 
		{
			cal.add(1, cal.YEAR);
		}
		return String.valueOf(cal.get(cal.YEAR));	
	}
	
}
