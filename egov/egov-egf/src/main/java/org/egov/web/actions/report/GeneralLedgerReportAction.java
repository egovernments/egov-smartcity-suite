package org.egov.web.actions.report;

import org.apache.struts2.convention.annotation.Action;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.utils.FinancialConstants;
import org.egov.web.actions.voucher.VoucherSearchAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.FlushMode;

import com.exilant.GLEngine.GeneralLedgerBean;
import com.exilant.eGov.src.reports.GeneralLedgerReport;
import com.exilant.eGov.src.reports.GeneralLedgerReportBean;
import com.exilant.exility.common.TaskFailedException;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;


@ParentPackage("egov")   
public class GeneralLedgerReportAction extends VoucherSearchAction{

	private static final Logger	LOGGER = Logger.getLogger(GeneralLedgerReportAction.class);
	private GeneralLedgerReportBean generalLedgerReport = new GeneralLedgerReportBean() ;
	private GeneralLedgerReport generalLedger = new GeneralLedgerReport();
	protected DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	protected LinkedList  generalLedgerDisplayList = new LinkedList();
	String heading = "";

	public GeneralLedgerReportAction() {
		super();
	}
	
	@Override
	public Object getModel() {
		return generalLedgerReport;
	}
	public void prepareNewForm() {
		super.prepare();
		addDropdownData("fundList", persistenceService.findAllBy(" from Fund where isactive=1 and isnotleaf=0 order by name"));
		addDropdownData("departmentList", persistenceService.findAllBy("from Department order by deptName"));
		addDropdownData("functionaryList", persistenceService.findAllBy(" from Functionary where isactive=1 order by name"));
		addDropdownData("fundsourceList", persistenceService.findAllBy(" from Fundsource where isactive=1 and isnotleaf=0 order by name"));
		addDropdownData("fieldList", persistenceService.findAllBy(" from Boundary b where lower(b.boundaryType.name)='ward' "));
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside  Prepare ........");
		
	}

	@SkipValidation
@Action(value="/report/generalLedgerReport-newForm")
	public String newForm() {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("..Inside NewForm method..");
		return FinancialConstants.STRUTS_RESULT_PAGE_SEARCH;
	}
	
	
	@Validations(requiredFields = { @RequiredFieldValidator(fieldName = "glCode1", message = "", key = FinancialConstants.REQUIRED),
			@RequiredFieldValidator(fieldName = "fund_id", message = "", key = FinancialConstants.REQUIRED),
			@RequiredFieldValidator(fieldName = "startDate", message = "", key = FinancialConstants.REQUIRED),
			@RequiredFieldValidator(fieldName = "endDate", message = "", key = FinancialConstants.REQUIRED)})
	
	@ValidationErrorPage(value=FinancialConstants.STRUTS_RESULT_PAGE_SEARCH)
	
	
	
	@SkipValidation
@Action(value="/report/generalLedgerReport-ajaxSearch")
	public String ajaxSearch() throws TaskFailedException{
		
	HibernateUtil.getCurrentSession().setDefaultReadOnly(true);
	HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("GeneralLedgerAction | Search | start");
		try {
			generalLedgerDisplayList = generalLedger.getGeneralLedgerList(generalLedgerReport);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("GeneralLedgerAction | list | End");
		heading=getGLHeading();
		generalLedgerReport.setHeading(getGLHeading());
		prepareNewForm();
		return "results";
	}
	private String getGLHeading() {
		
		String heading="";
		CChartOfAccounts glCode = new CChartOfAccounts();
		Fund fund  = new Fund();
		if(checkNullandEmpty(generalLedgerReport.getGlCode1()) && checkNullandEmpty(generalLedgerReport.getGlCode1())){
			glCode = (CChartOfAccounts) persistenceService.find("from CChartOfAccounts where glcode = ?",generalLedgerReport.getGlCode1());
			fund = (Fund) persistenceService.find("from Fund where id = ?",Integer.parseInt(generalLedgerReport.getFund_id()));
		}
		heading = "General Ledger Report for "+glCode.getGlcode() +":"+ glCode.getName() +" for "+ fund.getName()+" from "+generalLedgerReport.getStartDate()+" to "+generalLedgerReport.getEndDate();
		if(checkNullandEmpty(generalLedgerReport.getDepartmentId()))
		{
			Department dept = (Department) persistenceService.find("from Department where id = ?",Integer.parseInt(generalLedgerReport.getDepartmentId()));
			heading = heading +" under "+dept.getDeptName()+" ";
		}
		if(checkNullandEmpty(generalLedgerReport.getFunctionCode()))
		{
			CFunction function  = (CFunction) persistenceService.find("from CFunction where code = ?",generalLedgerReport.getFunctionCode());
			heading = heading +" in "+function.getName()+" Function ";
		}
		
		if(checkNullandEmpty(generalLedgerReport.getFunctionaryId()))
		{  	
			Functionary functionary  = (Functionary) persistenceService.find("from Functionary where id = ?",Integer.parseInt(generalLedgerReport.getFunctionaryId()));
			heading = heading +" in "+functionary.getName()+" Functionary ";
		}
		
		if(checkNullandEmpty(generalLedgerReport.getFieldId()))
		{
			Boundary ward  = (Boundary) persistenceService.find("from Boundary where id = ?",Integer.parseInt(generalLedgerReport.getFieldId()));
			heading = heading +" in "+ward.getName()+" Field ";		
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


	public GeneralLedgerReportBean getGeneralLedgerReport() {
		return generalLedgerReport;
	}

	public void setGeneralLedgerReport(GeneralLedgerReportBean generalLedgerReport) {
		this.generalLedgerReport = generalLedgerReport;
	}

	public GeneralLedgerReport getGeneralLedger() {
		return generalLedger;
	}

	public void setGeneralLedger(GeneralLedgerReport generalLedger) {
		this.generalLedger = generalLedger;
	}

	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public LinkedList getGeneralLedgerDisplayList() {
		return generalLedgerDisplayList;
	}

	public void setGeneralLedgerDisplayList(LinkedList generalLedgerDisplayList) {
		this.generalLedgerDisplayList = generalLedgerDisplayList;
	}



}
