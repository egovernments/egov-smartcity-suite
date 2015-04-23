package org.egov.web.actions.report;

import org.apache.struts2.convention.annotation.Action;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.infstr.ValidationException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.utils.FinancialConstants;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.actions.voucher.VoucherSearchAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.FlushMode;

import com.exilant.GLEngine.GeneralLedgerBean;
import com.exilant.eGov.src.reports.DayBookList;
import com.exilant.eGov.src.reports.DayBookReportBean;
import com.exilant.eGov.src.reports.GeneralLedgerReport;
import com.exilant.eGov.src.reports.GeneralLedgerReportBean;
import com.exilant.exility.common.TaskFailedException;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;


@ParentPackage("egov")   
public class DayBookReportAction extends BaseFormAction{

	private static final Logger	LOGGER = Logger.getLogger(DayBookReportAction.class);
	private DayBookReportBean dayBookReport = new DayBookReportBean() ;
	private DayBookList dayBook = new DayBookList();
	protected DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	protected LinkedList  dayBookDisplayList = new LinkedList();
	String heading = "";

	public DayBookReportAction() {
		super();
	}
	
	@Override
	public Object getModel() {
		return dayBookReport;
	}
	
	public void prepareNewForm() {
		super.prepare();
	HibernateUtil.getCurrentSession().setDefaultReadOnly(true);
	HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
		addDropdownData("fundList", persistenceService.findAllBy(" from Fund where isactive=1 and isnotleaf=0 order by name"));
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside  Prepare ........");
		
	}

	@SkipValidation
@Action(value="/report/dayBookReport-newForm")
	public String newForm() {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("..Inside NewForm method..");
		return FinancialConstants.STRUTS_RESULT_PAGE_SEARCH;
	}
	
	
	@Validations(requiredFields = {@RequiredFieldValidator(fieldName = "startDate", message = "", key = FinancialConstants.REQUIRED),
			@RequiredFieldValidator(fieldName = "endDate", message = "", key = FinancialConstants.REQUIRED)})
	
	@ValidationErrorPage(value=FinancialConstants.STRUTS_RESULT_PAGE_SEARCH)
	
	
@Action(value="/report/dayBookReport-ajaxSearch")
	public String ajaxSearch() throws TaskFailedException{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("dayBookAction | Search | start");
		try {
			dayBookDisplayList = dayBook.getDayBookList(dayBookReport);
		} catch (ValidationException e) {
			throw new ValidationException(e.getErrors());
		} catch(Exception e)
		{
			throw new EGOVRuntimeException(e.getMessage());
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("dayBookAction | list | End");
		heading=getGLHeading();
		prepareNewForm();
	HibernateUtil.getCurrentSession().setFlushMode(FlushMode.AUTO);
		return "result";
	}
	private String getGLHeading() {
		
		String heading="Day Book report from " + dayBookReport.getStartDate() + " to "+dayBookReport.getEndDate();
		Fund fund  = new Fund();
		if(checkNullandEmpty(dayBookReport.getFundId())){
			fund = (Fund) persistenceService.find("from Fund where id = ?",Integer.parseInt(dayBookReport.getFundId()));
			heading = heading +" under "+fund.getName()+" ";
		}
		return heading;
	}
	private boolean checkNullandEmpty(String column)
	{
		if(column!=null && !column.isEmpty() && !column.equalsIgnoreCase("0") )
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}

	public DayBookReportBean getDayBookReport() {
		return dayBookReport;
	}

	public void setDayBookReport(DayBookReportBean dayBookReport) {
		this.dayBookReport = dayBookReport;
	}

	public DayBookList getDayBook() {
		return dayBook;
	}

	public void setDayBook(DayBookList dayBook) {
		this.dayBook = dayBook;
	}

	public LinkedList getDayBookDisplayList() {
		return dayBookDisplayList;
	}

	public void setDayBookDisplayList(LinkedList dayBookDisplayList) {
		this.dayBookDisplayList = dayBookDisplayList;
	}

	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}





}
