package org.egov.web.actions.report;

import org.apache.struts2.convention.annotation.Action;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.apache.struts2.config.ParentPackage;
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
import com.exilant.eGov.src.transactions.RptSubLedgerSchedule;
import com.exilant.exility.common.TaskFailedException;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;


@ParentPackage("egov")   
public class SubLedgerScheduleReportAction extends BaseFormAction{

	private static final Logger	LOGGER = Logger.getLogger(SubLedgerScheduleReportAction.class);
	private GeneralLedgerBean subLedgerScheduleReport = new GeneralLedgerBean() ;
	private RptSubLedgerSchedule subLedgerSchedule = new RptSubLedgerSchedule();
	protected DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	protected LinkedList  subLedgerScheduleDisplayList = new LinkedList();
	String heading = "";

	public SubLedgerScheduleReportAction() {
		super();
	}
	
	@Override
	public Object getModel() {
		return subLedgerScheduleReport;
	}
	public void prepareNewForm() {
		super.prepare();
	HibernateUtil.getCurrentSession().setDefaultReadOnly(true);
	HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
		addDropdownData("fundList", persistenceService.findAllBy(" from Fund where isactive=1 and isnotleaf=0 order by name"));
		addDropdownData("departmentList", persistenceService.findAllBy("from Department order by deptName"));
		if(subLedgerScheduleReport!=null && subLedgerScheduleReport.getGlcode()!=null && !subLedgerScheduleReport.getGlcode().equalsIgnoreCase("")){
			addDropdownData("subLedgerTypeList", persistenceService.findAllBy("select distinct adt from Accountdetailtype adt, CChartOfAccountDetail cad where cad.glCodeId.glcode = ? and cad.detailTypeId = adt ",subLedgerScheduleReport.getGlcode()));
		}else{
		addDropdownData("subLedgerTypeList", Collections.EMPTY_LIST);
		}
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside  Prepare ........");
		
	}

	@SkipValidation
@Action(value="/report/subLedgerScheduleReport-newForm")
	public String newForm() {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("..Inside NewForm method..");
		return FinancialConstants.STRUTS_RESULT_PAGE_SEARCH;
	}
	
	
	@Validations(requiredFields = { @RequiredFieldValidator(fieldName = "glcode", message = "", key = FinancialConstants.REQUIRED),
			@RequiredFieldValidator(fieldName = "fund_id", message = "", key = FinancialConstants.REQUIRED),
			@RequiredFieldValidator(fieldName = "accEntityId", message = "", key = FinancialConstants.REQUIRED),
			@RequiredFieldValidator(fieldName = "startDate", message = "", key = FinancialConstants.REQUIRED),
			@RequiredFieldValidator(fieldName = "endDate", message = "", key = FinancialConstants.REQUIRED)})
	
	@ValidationErrorPage(value=FinancialConstants.STRUTS_RESULT_PAGE_SEARCH)
	
	
	
@Action(value="/report/subLedgerScheduleReport-ajaxSearch")
	public String ajaxSearch() throws TaskFailedException{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("SubLedgerScheduleReportAction | Search | start");
		try {
			subLedgerScheduleDisplayList = subLedgerSchedule.getSubLedgerTypeSchedule(subLedgerScheduleReport);
		}catch (ValidationException e) {
			throw new ValidationException(e.getErrors());
		} catch(Exception e)
		{
			throw new EGOVRuntimeException(e.getMessage());
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("SubLedgerScheduleReportAction | list | End");
		heading=getGLHeading();
		prepareNewForm();
	HibernateUtil.getCurrentSession().setFlushMode(FlushMode.AUTO);
		return "result";
	}
	private String getGLHeading() {
		
		String heading="";
		CChartOfAccounts glCode = new CChartOfAccounts();
		Fund fund  = new Fund();
		if(checkNullandEmpty(subLedgerScheduleReport.getGlcode()) && checkNullandEmpty(subLedgerScheduleReport.getGlcode())){
			glCode = (CChartOfAccounts) persistenceService.find("from CChartOfAccounts where glcode = ?",subLedgerScheduleReport.getGlcode());
			fund = (Fund) persistenceService.find("from Fund where id = ?",Integer.parseInt(subLedgerScheduleReport.getFund_id()));
		}
		heading = "Sub Ledger Schedule Report for "+glCode.getGlcode() +" - "+ glCode.getName() +" for "+ fund.getName()
				+" from "+subLedgerScheduleReport.getStartDate()+" to "+subLedgerScheduleReport.getEndDate()+" and Sub Ledger Type :"+subLedgerScheduleReport.getSubLedgerTypeName();
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

	public GeneralLedgerBean getSubLedgerScheduleReport() {
		return subLedgerScheduleReport;
	}

	public void setSubLedgerScheduleReport(GeneralLedgerBean subLedgerScheduleReport) {
		this.subLedgerScheduleReport = subLedgerScheduleReport;
	}

	public RptSubLedgerSchedule getSubLedgerSchedule() {
		return subLedgerSchedule;
	}

	public void setSubLedgerSchedule(RptSubLedgerSchedule subLedgerSchedule) {
		this.subLedgerSchedule = subLedgerSchedule;
	}

	public LinkedList getSubLedgerScheduleDisplayList() {
		return subLedgerScheduleDisplayList;
	}

	public void setSubLedgerScheduleDisplayList(
			LinkedList subLedgerScheduleDisplayList) {
		this.subLedgerScheduleDisplayList = subLedgerScheduleDisplayList;
	}





}
