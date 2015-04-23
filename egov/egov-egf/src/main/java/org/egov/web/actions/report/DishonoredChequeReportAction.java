package org.egov.web.actions.report;

import org.apache.struts2.convention.annotation.Action;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.utils.FinancialConstants;
import org.egov.utils.VoucherHelper;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.actions.voucher.VoucherSearchAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.FlushMode;

import com.exilant.GLEngine.GeneralLedgerBean;
import com.exilant.eGov.src.reports.DishonoredChequeBean;
import com.exilant.eGov.src.reports.DishonoredChequeReport;
import com.exilant.eGov.src.reports.GeneralLedgerReport;
import com.exilant.eGov.src.transactions.JbReport;
import com.exilant.exility.common.TaskFailedException;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;


@ParentPackage("egov")   
public class DishonoredChequeReportAction extends BaseFormAction{

	private static final Logger	LOGGER = Logger.getLogger(DishonoredChequeReportAction.class);
	private DishonoredChequeBean dishonoredChequeReport = new DishonoredChequeBean() ;
	private DishonoredChequeReport dishonoredCheque = new DishonoredChequeReport();
	protected DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	protected ArrayList dishonoredChequeDisplayList = new ArrayList();
	String heading = "";
	private String showMode = "";

	public DishonoredChequeReportAction() {
		super();
	}
	
	@Override
	public Object getModel() {
		return dishonoredChequeReport;
	}
	public void prepareNewForm() {
		super.prepare();
		addDropdownData("fundList", persistenceService.findAllBy(" from Fund where isactive=1 and isnotleaf=0 order by name"));
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside  Prepare ........");
		
	}

	@SkipValidation
@Action(value="/report/dishonoredChequeReport-newForm")
	public String newForm() {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("..Inside NewForm method..");
		return FinancialConstants.STRUTS_RESULT_PAGE_SEARCH;
	}
	
	
	@Validations(requiredFields = {
			@RequiredFieldValidator(fieldName = "mode", message = "", key = FinancialConstants.REQUIRED),
			@RequiredFieldValidator(fieldName = "startDate", message = "", key = FinancialConstants.REQUIRED)})
	
	@ValidationErrorPage(value=FinancialConstants.STRUTS_RESULT_PAGE_SEARCH)
	
	
	
	@SkipValidation
@Action(value="/report/dishonoredChequeReport-ajaxSearch")
	public String ajaxSearch() throws TaskFailedException{
		
	HibernateUtil.getCurrentSession().setDefaultReadOnly(true);
	HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("DishonoredChequeAction | Search | start");
		try {
			dishonoredChequeDisplayList = dishonoredCheque.getDishonoredChequeDetails(dishonoredChequeReport);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("DishonoredChequeAction | list | End");
		heading=getGLHeading();
		prepareNewForm();
		showMode = "result";
		return "results";
	}
	private String getGLHeading() {
		
		String heading="";
		Fund fund = new Fund();
		heading = "Dishonored Cheque/DD Report under Mode of Payment:"+(dishonoredChequeReport.getMode().equalsIgnoreCase("2")?"Cheque":"DD")+" from "+dishonoredChequeReport.getStartDate();
		if(checkNullandEmpty(dishonoredChequeReport.getEndDate())){
			heading = heading + " to " + dishonoredChequeReport.getEndDate();
		}
		if(checkNullandEmpty(dishonoredChequeReport.getFundLst())){
			fund = (Fund) persistenceService.find("from Fund where  id = ?",Integer.parseInt(dishonoredChequeReport.getFundLst()));
			heading = heading + " and Fund :" + fund.getName();
		}
		if(checkNullandEmpty(dishonoredChequeReport.getChequeNo())){
			heading = heading + " and Cheque/DD Number :" + dishonoredChequeReport.getChequeNo() ;
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

	public DishonoredChequeBean getDishonoredChequeReport() {
		return dishonoredChequeReport;
	}

	public void setDishonoredChequeReport(
			DishonoredChequeBean dishonoredChequeReport) {
		this.dishonoredChequeReport = dishonoredChequeReport;
	}

	public DishonoredChequeReport getDishonoredCheque() {
		return dishonoredCheque;
	}

	public void setDishonoredCheque(DishonoredChequeReport dishonoredCheque) {
		this.dishonoredCheque = dishonoredCheque;
	}

	public ArrayList getDishonoredChequeDisplayList() {
		return dishonoredChequeDisplayList;
	}

	public void setDishonoredChequeDisplayList(ArrayList dishonoredChequeDisplayList) {
		this.dishonoredChequeDisplayList = dishonoredChequeDisplayList;
	}

	public String getShowMode() {
		return showMode;
	}

	public void setShowMode(String showMode) {
		this.showMode = showMode;
	}


	


}
