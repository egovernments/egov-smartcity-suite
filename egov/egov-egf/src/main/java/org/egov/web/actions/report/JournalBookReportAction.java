package org.egov.web.actions.report;

import org.apache.struts2.convention.annotation.Action;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import com.exilant.eGov.src.reports.GeneralLedgerReport;
import com.exilant.eGov.src.transactions.JbReport;
import com.exilant.exility.common.TaskFailedException;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;


@ParentPackage("egov")   
public class JournalBookReportAction extends BaseFormAction{

	private static final Logger	LOGGER = Logger.getLogger(JournalBookReportAction.class);
	private GeneralLedgerBean journalBookReport = new GeneralLedgerBean() ;
	private JbReport journalBook = new JbReport();
	protected DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	protected LinkedList  journalBookDisplayList = new LinkedList();
	String heading = "";

	public JournalBookReportAction() {
		super();
	}
	
	@Override
	public Object getModel() {
		return journalBookReport;
	}
	public void prepareNewForm() {
		super.prepare();
		addDropdownData("fundList", persistenceService.findAllBy(" from Fund where isactive=1 and isnotleaf=0 order by name"));
		addDropdownData("departmentList", persistenceService.findAllBy("from Department order by deptName"));
		addDropdownData("fundsourceList", persistenceService.findAllBy(" from Fundsource where isactive=1 and isnotleaf=0 order by name"));
		addDropdownData("voucherNameList", VoucherHelper.VOUCHER_TYPE_NAMES.get(FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL));
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside  Prepare ........");
		
	}

	@SkipValidation
@Action(value="/report/journalBookReport-newForm")
	public String newForm() {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("..Inside NewForm method..");
		return FinancialConstants.STRUTS_RESULT_PAGE_SEARCH;
	}
	
	
	@Validations(requiredFields = {
			@RequiredFieldValidator(fieldName = "fund_id", message = "", key = FinancialConstants.REQUIRED),
			@RequiredFieldValidator(fieldName = "startDate", message = "", key = FinancialConstants.REQUIRED),
			@RequiredFieldValidator(fieldName = "endDate", message = "", key = FinancialConstants.REQUIRED)})
	
	@ValidationErrorPage(value=FinancialConstants.STRUTS_RESULT_PAGE_SEARCH)
	
	
	
	@SkipValidation
@Action(value="/report/journalBookReport-ajaxSearch")
	public String ajaxSearch() throws TaskFailedException{
		
	HibernateUtil.getCurrentSession().setDefaultReadOnly(true);
	HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("JournalBookAction | Search | start");
		try {
			journalBookDisplayList = journalBook.getJbReport(journalBookReport);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("JournalBookAction | list | End");
		heading=getGLHeading();
		prepareNewForm();
		return "result";
	}
	private String getGLHeading() {
		
		String heading="";
		heading = "Journal Book Report under "+ journalBookReport.getFundName()+" from "+journalBookReport.getStartDate()+" to "+journalBookReport.getEndDate();
		Department dept = new Department();
		Fundsource fundsource = new Fundsource();
		if(checkNullandEmpty(journalBookReport.getDept_name())){
			dept = (Department) persistenceService.find("from Department where  id = ?",Integer.parseInt(journalBookReport.getDept_name()));
			heading = heading + " and Department : " + dept.getDeptName() ;
		}
		if(checkNullandEmpty(journalBookReport.getFundSource_id())){
			fundsource = (Fundsource) persistenceService.find("from Fundsource where  id = ?",Integer.parseInt(journalBookReport.getFundSource_id()));
			heading = heading + " and Financing Source :" + fundsource.getName();
		}
		if(checkNullandEmpty(journalBookReport.getVoucher_name())){
			heading = heading + " and Voucher Type Name :" + journalBookReport.getVoucher_name() ;
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

	public GeneralLedgerBean getJournalBookReport() {
		return journalBookReport;
	}

	public void setJournalBookReport(GeneralLedgerBean journalBookReport) {
		this.journalBookReport = journalBookReport;
	}

	public JbReport getJournalBook() {
		return journalBook;
	}

	public void setJournalBook(JbReport journalBook) {
		this.journalBook = journalBook;
	}

	public LinkedList getJournalBookDisplayList() {
		return journalBookDisplayList;
	}

	public void setJournalBookDisplayList(LinkedList journalBookDisplayList) {
		this.journalBookDisplayList = journalBookDisplayList;
	}

	


}
