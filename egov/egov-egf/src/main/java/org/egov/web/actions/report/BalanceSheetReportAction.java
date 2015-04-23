package org.egov.web.actions.report;

import org.apache.struts2.convention.annotation.Action;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.StreamResult;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.services.report.BalanceSheetScheduleService;
import org.egov.services.report.BalanceSheetService;
import org.egov.utils.Constants;
import org.egov.utils.ReportHelper;
import org.egov.web.actions.BaseFormAction;
import org.hibernate.FlushMode;
import org.hibernate.Query;

@Results(value={
		@Result(name="balanceSheet-PDF",type=StreamResult.class,value=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"application/pdf",Constants.CONTENT_DISPOSITION,"no-cache;filename=BalanceSheet.pdf"}),
		@Result(name="balanceSheet-XLS",type=StreamResult.class,value=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"application/xls",Constants.CONTENT_DISPOSITION,"no-cache;filename=BalanceSheet.xls"}),
		@Result(name="balanceSheet-HTML",type=StreamResult.class,value=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"text/html"})
	})
	
@ParentPackage("egov")
public class BalanceSheetReportAction extends BaseFormAction{
	private static final String BALANCE_SHEET_PDF = "balanceSheet-PDF";
	private static final String BALANCE_SHEET_XLS = "balanceSheet-XLS";
	InputStream inputStream;
	ReportHelper reportHelper;
	Statement balanceSheet = new Statement();
	private Date todayDate;
	private Date fromDate;
	private Date toDate;
	private Date currentYearfromDate;
	private Date currentYeartoDate;
	private Date previousYearfromDate;
	private Date previousYeartoDate;
	public Date getFromDate() {
		return balanceSheetService.getFromDate(balanceSheet)	;
	}

	public Date getToDate() {
		return balanceSheetService.getToDate(balanceSheet)	;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getCurrentYearfromDate() {
		return getFromDate();
	}

	public void setCurrentYearfromDate(Date currentYearfromDate) {
		this.currentYearfromDate = currentYearfromDate;
	}

	public Date getCurrentYeartoDate() {
		return getToDate();
	}

	public void setCurrentYeartoDate(Date currentYeartoDate) {
		this.currentYeartoDate = currentYeartoDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public Date getPreviousYearfromDate() {
		return balanceSheetService.getPreviousYearFor(getFromDate());
	}

	public Date getPreviousYeartoDate() {
		return balanceSheetService.getPreviousYearFor(getToDate());
	}

	public void setPreviousYearfromDate(Date previousYearfromDate) {
		this.previousYearfromDate = previousYearfromDate;
	}

	public void setPreviousYeartoDate(Date previousYeartoDate) {
		this.previousYeartoDate = previousYeartoDate;
	}

	private StringBuffer header=new StringBuffer();
	//private String heading;
	BalanceSheetService balanceSheetService;
	BalanceSheetScheduleService balanceSheetScheduleService;
	public static final Locale LOCALE = new Locale("en","IN");
	public static final SimpleDateFormat  DDMMYYYYFORMATS= new SimpleDateFormat("dd/MM/yyyy",LOCALE);
	
	
	public void setBalanceSheetService(BalanceSheetService balanceSheetService) {
		this.balanceSheetService = balanceSheetService;
	}
	
	public void setBalanceSheetScheduleService(BalanceSheetScheduleService balanceSheetScheduleService) {
		this.balanceSheetScheduleService = balanceSheetScheduleService;
	}

	public void setReportHelper(final ReportHelper reportHelper) {
		this.reportHelper = reportHelper;
	}
	
	public InputStream getInputStream() {
		return inputStream;
	}
	
	public Statement getBalanceSheet() {
		return balanceSheet;
	}
	
	public BalanceSheetReportAction(){
		addRelatedEntity("department", Department.class);
		addRelatedEntity("function", CFunction.class);
		addRelatedEntity("fund", Fund.class);
		addRelatedEntity("functionary", Functionary.class);
		addRelatedEntity("financialYear",CFinancialYear.class);
		addRelatedEntity("field",Boundary.class);
	}
	
	public void prepare(){
	HibernateUtil.getCurrentSession().setDefaultReadOnly(true);
	HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
		super.prepare();
		if(!parameters.containsKey("showDropDown")){
			EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
			addDropdownData("departmentList", masterCache.get("egi-department"));
			addDropdownData("fundList", masterCache.get("egi-fund"));
			addDropdownData("functionList", masterCache.get("egi-function"));
			addDropdownData("functionaryList", masterCache.get("egi-functionary"));
			addDropdownData("fieldList", masterCache.get("egi-ward"));
			//addDropdownData("financialYearList", getPersistenceService().findAllBy("from CFinancialYear where isActive=1 and isActiveForPosting=1 order by finYearRange desc "));
			addDropdownData("financialYearList", persistenceService.findAllBy("from CFinancialYear order by finYearRange desc "));
		}
	}

	protected void setRelatedEntitesOn() {
		setTodayDate(new Date());
		if(balanceSheet.getFinancialYear() != null && balanceSheet.getFinancialYear().getId() !=null)
			balanceSheet.setFinancialYear((CFinancialYear)getPersistenceService().find("from CFinancialYear where id=?", balanceSheet.getFinancialYear().getId()));
		if(balanceSheet.getDepartment() != null && balanceSheet.getDepartment().getId()!=null &&  balanceSheet.getDepartment().getId()!=0){
			balanceSheet.setDepartment((Department)getPersistenceService().find("from Department where id=?", balanceSheet.getDepartment().getId()));
			Department dept=(Department) persistenceService.find("from Department where id=?", balanceSheet.getDepartment().getId());
			header.append(" in "+ balanceSheet.getDepartment().getDeptName());
		}else
			balanceSheet.setDepartment(null);
		if(balanceSheet.getField() != null && balanceSheet.getField().getId()!=null  && balanceSheet.getField().getId()!=0){
			balanceSheet.setField((Boundary)getPersistenceService().find("from Boundary where id=?", balanceSheet.getField().getId()));
			header.append(" in "+balanceSheet.getField().getName());
		}if(balanceSheet.getFund() != null && balanceSheet.getFund().getId() != null && balanceSheet.getFund().getId() !=0){
			balanceSheet.setFund((Fund)getPersistenceService().find("from Fund where id=?", balanceSheet.getFund().getId()));
			header.append(" for "+ balanceSheet.getFund().getName());
		}if(balanceSheet.getFunction() != null && balanceSheet.getFunction().getId() != null && balanceSheet.getFunction().getId() !=0){
			balanceSheet.setFunction((CFunction)getPersistenceService().find("from CFunction where id=?", balanceSheet.getFunction().getId()));
			header.append(" for "+ balanceSheet.getFunction().getName());
		}if(balanceSheet.getFunctionary() != null && balanceSheet.getFunctionary().getId()!=null && balanceSheet.getFunctionary().getId()!=0) {
			balanceSheet.setFunctionary((Functionary)getPersistenceService().find("from Functionary where id=?", balanceSheet.getFunctionary().getId()));
			header.append(" in "+ balanceSheet.getFunctionary().getName());
		}if(balanceSheet.getAsOndate()!=null){
			header.append(" as on "+ DDMMYYYYFORMATS.format(balanceSheet.getAsOndate()));
		}
		header.toString();
	}
	
	@Override
	public Object getModel() {
		return balanceSheet;
	}
	
@Action(value="/report/balanceSheetReport-generateBalanceSheetReport")
	public String generateBalanceSheetReport(){
		return "report";
	}
	
@Action(value="/report/balanceSheetReport-generateBalanceSheetSubReport")
	public String generateBalanceSheetSubReport(){
		populateDataSourceForSchedule();
		return "scheduleResults";
	}
	
@Action(value="/report/balanceSheetReport-generateScheduleReport")
	public String generateScheduleReport(){
		populateDataSourceForAllSchedules();
		return "allScheduleResults";
	}
	/*for Detailed*/
@Action(value="/report/balanceSheetReport-generateScheduleReportDetailed")
	public String generateScheduleReportDetailed(){
		populateDataSourceForAllSchedulesDetailed();
		return "allScheduleDetailedResults";
	}

	private void populateDataSourceForSchedule() {
		setRelatedEntitesOn();
		if(balanceSheet.getFund() != null && balanceSheet.getFund().getId() != null){
			List<Fund> selFund = new ArrayList<Fund>();
			selFund.add(balanceSheet.getFund());
			balanceSheet.setFunds(selFund);
		}else {
			balanceSheet.setFunds(balanceSheetService.getFunds());
		}
		balanceSheetScheduleService.populateDataForSchedule(balanceSheet,parameters.get("majorCode")[0]);
	}
	
	private void populateDataSourceForAllSchedules() {
		setRelatedEntitesOn();
		if(balanceSheet.getFund() != null && balanceSheet.getFund().getId() != null && balanceSheet.getFund().getId() != 0){
			List<Fund> selFund = new ArrayList<Fund>();
			selFund.add(balanceSheet.getFund());
			balanceSheet.setFunds(selFund);
		}else {
			balanceSheet.setFunds(balanceSheetService.getFunds());
		}
		balanceSheetScheduleService.populateDataForAllSchedules(balanceSheet);
	}
	/*for detailed*/
	private void populateDataSourceForAllSchedulesDetailed() {
		setRelatedEntitesOn();
		if(balanceSheet.getFund() != null && balanceSheet.getFund().getId() != null && balanceSheet.getFund().getId() != 0){
			List<Fund> selFund = new ArrayList<Fund>();
			selFund.add(balanceSheet.getFund());
			balanceSheet.setFunds(selFund);
		}else {
			balanceSheet.setFunds(balanceSheetService.getFunds());
		}
		balanceSheetScheduleService.populateDataForAllSchedulesDetailed(balanceSheet);
	}

	public String printBalanceSheetReport(){
		populateDataSource();
		return "report";
	}
	
@Action(value="/report/balanceSheetReport-generateBalanceSheetPdf")
	public String generateBalanceSheetPdf() throws Exception{
		populateDataSource();
		JasperPrint jasper = reportHelper.generateFinancialStatementReportJasperPrint(balanceSheet,getText("report.heading"),header.toString(),
				getCurrentYearToDate(),getPreviousYearToDate(),true);
		inputStream = reportHelper.exportPdf(inputStream, jasper);
		return BALANCE_SHEET_PDF;
	}
	
@Action(value="/report/balanceSheetReport-generateBalanceSheetXls")
	public String generateBalanceSheetXls() throws Exception{
		populateDataSource();
		JasperPrint jasper =null;
		if(!balanceSheet.getPeriod().equalsIgnoreCase("Yearly"))
			
		{
			jasper= reportHelper.generateFinancialStatementReportJasperPrint(balanceSheet,getText("report.heading"),header.toString(),
					getCurrentYearToDate(),getPreviousYearToDate(),true);
		}else
		{
			 jasper = reportHelper.generateFinancialStatementReportJasperPrint(balanceSheet,getText("report.heading"),header.toString(),
					 getCurrentYearToDate(),getPreviousYearToDate(),true);  
		}
		inputStream = reportHelper.exportXls(inputStream, jasper);
		return BALANCE_SHEET_XLS;
	}

@Action(value="/report/balanceSheetReport-generateSchedulePdf")
	public String generateSchedulePdf() throws Exception{
		populateDataSourceForAllSchedules();
		JasperPrint jasper = reportHelper.generateFinancialStatementReportJasperPrint(balanceSheet,getText("report.heading"),header.toString(),
				getCurrentYearToDate(),getPreviousYearToDate(),false);
		inputStream = reportHelper.exportPdf(inputStream, jasper);
		return BALANCE_SHEET_PDF;
	}
	
@Action(value="/report/balanceSheetReport-generateScheduleXls")
	public String generateScheduleXls() throws Exception{
		populateDataSourceForAllSchedules();
		JasperPrint jasper = reportHelper.generateFinancialStatementReportJasperPrint(balanceSheet,getText("report.heading"),header.toString(),
				getCurrentYearToDate(),getPreviousYearToDate(),false);
		inputStream = reportHelper.exportXls(inputStream, jasper);
		return BALANCE_SHEET_XLS;
	}
	/*for detailed*/
@Action(value="/report/balanceSheetReport-generateDetailedSchedulePdf")
	public String generateDetailedSchedulePdf() throws Exception{
		populateDataSourceForAllSchedulesDetailed();
		JasperPrint jasper = reportHelper.generateFinancialStatementReportJasperPrint(balanceSheet,getText("report.heading"),header.toString(),
				getCurrentYearToDate(),getPreviousYearToDate(),false);
		inputStream = reportHelper.exportPdf(inputStream, jasper);
		return BALANCE_SHEET_PDF;
	}
	/*for detailed*/
@Action(value="/report/balanceSheetReport-generateDetailedScheduleXls")
	public String generateDetailedScheduleXls() throws Exception{
		populateDataSourceForAllSchedulesDetailed();
		JasperPrint jasper = reportHelper.generateFinancialStatementReportJasperPrint(balanceSheet,getText("report.heading"),header.toString(),
				getCurrentYearToDate(),getPreviousYearToDate(),false);
		inputStream = reportHelper.exportXls(inputStream, jasper);
		return BALANCE_SHEET_XLS;
	}

@Action(value="/report/balanceSheetReport-generateBalanceSheetSchedulePdf")
	public String generateBalanceSheetSchedulePdf() throws Exception{
		populateDataSourceForSchedule();
		JasperPrint jasper = reportHelper.generateFinancialStatementReportJasperPrint(balanceSheet,getText("report.sub.schedule.heading"),header.toString(),
				getCurrentYearToDate(),getPreviousYearToDate(),false);
		inputStream = reportHelper.exportPdf(inputStream, jasper);
		return BALANCE_SHEET_PDF;
	}  
	
@Action(value="/report/balanceSheetReport-generateBalanceSheetScheduleXls")
	public String generateBalanceSheetScheduleXls() throws Exception{
		populateDataSourceForSchedule();
		JasperPrint jasper = reportHelper.generateFinancialStatementReportJasperPrint(balanceSheet,getText("report.sub.schedule.heading"),header.toString(),
				getCurrentYearToDate(),getPreviousYearToDate(),false);
		inputStream = reportHelper.exportXls(inputStream, jasper);
		return BALANCE_SHEET_XLS;
	}

	protected void populateDataSource() {
		setRelatedEntitesOn();
		if(balanceSheet.getFund() != null && balanceSheet.getFund().getId() != null){
			List<Fund> selFund = new ArrayList<Fund>();
			selFund.add(balanceSheet.getFund());
			balanceSheet.setFunds(selFund);
		}else {
			balanceSheet.setFunds(balanceSheetService.getFunds());
		}
		balanceSheetService.populateBalanceSheet(balanceSheet);
	}
	
	public String getUlbName(){
		Query query = HibernateUtil.getCurrentSession().createSQLQuery("select name from companydetail");
		List<String> result = query.list();
		if(result!=null)
			return result.get(0);
		return " ";
	}

	public String getCurrentYearToDate(){
		return balanceSheetService.getFormattedDate(balanceSheetService.getToDate(balanceSheet));
	}
	public String getPreviousYearToDate(){
		return balanceSheetService.getFormattedDate(balanceSheetService.getPreviousYearFor(balanceSheetService.getToDate(balanceSheet)));
	}

	public Date getTodayDate() {
		return todayDate;
	}

	public void setTodayDate(Date todayDate) {
		this.todayDate = todayDate;
	}

	public StringBuffer getHeader() {
		return header;
	}

	public void setHeader(StringBuffer header) {
		this.header = header;
	}

	
}
