package org.egov.web.actions.report;

import org.apache.struts2.convention.annotation.Action;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CFinancialYear;
import org.egov.commons.Fund;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infra.admin.master.entity.Department;
import org.egov.utils.FinancialConstants;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.actions.voucher.VoucherSearchAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.FlushMode;
import org.egov.infstr.ValidationException;
import com.exilant.eGov.src.reports.OpeningBalance;
import com.exilant.eGov.src.reports.OpeningBalanceInputBean;
import com.exilant.exility.common.TaskFailedException;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;


@ParentPackage("egov")   
public class OpeningBalanceReportAction extends BaseFormAction{

	private static final Logger	LOGGER = Logger.getLogger(OpeningBalanceReportAction.class);
	private OpeningBalanceInputBean openingBalanceReport = new OpeningBalanceInputBean() ;
	private OpeningBalance openingBalance = new OpeningBalance();
	protected DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	protected ArrayList  openingBalanceDisplayList = new ArrayList();
	String heading = "";

	public OpeningBalanceReportAction() {
		super();
	}
	
	@Override
	public Object getModel() {
		return openingBalanceReport;
	}
	public void prepareNewForm() {
		super.prepare();
	HibernateUtil.getCurrentSession().setDefaultReadOnly(true);
	HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
		addDropdownData("fundList", persistenceService.findAllBy(" from Fund where isactive=1 and isnotleaf=0 order by name"));
		addDropdownData("departmentList", persistenceService.findAllBy("from Department order by deptName"));
		addDropdownData("financialYearList", persistenceService.findAllBy("from CFinancialYear order by finYearRange desc "));
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside  Prepare ........");
		
	}

	@SkipValidation
@Action(value="/report/openingBalanceReport-newForm")
	public String newForm() {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("..Inside NewForm method..");
		return FinancialConstants.STRUTS_RESULT_PAGE_SEARCH;
	}
	
	
	@Validations(requiredFields = { @RequiredFieldValidator(fieldName = "finYear", message = "", key = FinancialConstants.REQUIRED)})
	
	@ValidationErrorPage(value=FinancialConstants.STRUTS_RESULT_PAGE_SEARCH)
	
	
	
@Action(value="/report/openingBalanceReport-ajaxSearch")
	public String ajaxSearch() throws TaskFailedException{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("OpeningBalanceReportAction | Search | start");
		try {
			openingBalanceDisplayList = openingBalance.getOBReport(openingBalanceReport);
		} catch (ValidationException e) {
				throw new ValidationException(e.getErrors());
		} catch(Exception e)
		{
			throw new EGOVRuntimeException(e.getMessage());
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("OpeningBalanceReportAction | list | End");
		heading=getGLHeading();
		prepareNewForm();
	HibernateUtil.getCurrentSession().setFlushMode(FlushMode.AUTO);
		return "result";
	}
	private String getGLHeading() {
		
		String heading="Opening Balance for the Year ";
		CFinancialYear finYear = new CFinancialYear();
		Fund fund  = new Fund();
		Department dept = new Department();
	if(checkNullandEmpty(openingBalanceReport.getFinYear())){
		finYear = (CFinancialYear) persistenceService.find("from CFinancialYear where id = ?",Long.parseLong(openingBalanceReport.getFinYear()));
		heading = heading + finYear.getFinYearRange() ;
	}
	if(checkNullandEmpty(openingBalanceReport.getObFund_id())){
		fund = (Fund) persistenceService.find("from Fund where id = ?",Integer.parseInt(openingBalanceReport.getObFund_id()));
		heading = heading + " under " +fund.getName() ;
	}
	
	if(checkNullandEmpty(openingBalanceReport.getDeptId())){
		dept = (Department) persistenceService.find("from Department where id = ?",Integer.parseInt(openingBalanceReport.getDeptId()));
		heading = heading + " and " +dept.getName()+ " Department ";
	}
		return heading;
	}
	private boolean checkNullandEmpty(String column)
	{
		if(column!=null && !column.isEmpty())
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}

	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public OpeningBalanceInputBean getOpeningBalanceReport() {
		return openingBalanceReport;
	}

	public void setOpeningBalanceReport(OpeningBalanceInputBean openingBalanceReport) {
		this.openingBalanceReport = openingBalanceReport;
	}

	public OpeningBalance getOpeningBalance() {
		return openingBalance;
	}

	public void setOpeningBalance(OpeningBalance openingBalance) {
		this.openingBalance = openingBalance;
	}

	public ArrayList getOpeningBalanceDisplayList() {
		return openingBalanceDisplayList;
	}

	public void setOpeningBalanceDisplayList(ArrayList openingBalanceDisplayList) {
		this.openingBalanceDisplayList = openingBalanceDisplayList;
	}

	



}
