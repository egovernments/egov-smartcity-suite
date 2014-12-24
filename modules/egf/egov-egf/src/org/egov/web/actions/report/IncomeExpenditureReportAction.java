package org.egov.web.actions.report;

import java.io.InputStream;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.services.report.IncomeExpenditureScheduleService;
import org.egov.services.report.IncomeExpenditureService;
import org.egov.utils.Constants;
import org.egov.utils.ReportHelper;
import org.egov.web.actions.BaseFormAction;

@Results(value={
		@Result(name="PDF",type="stream",location=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"application/pdf",Constants.CONTENT_DISPOSITION,"no-cache;filename=IncomeExpenditureStatement.pdf"}),
		@Result(name="XLS",type="stream",location=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"application/xls",Constants.CONTENT_DISPOSITION,"no-cache;filename=IncomeExpenditureStatement.xls"})
	})
	
@ParentPackage("egov")
public class IncomeExpenditureReportAction extends BaseFormAction{
	private static final String INCOME_EXPENSE_PDF = "PDF";
	private static final String INCOME_EXPENSE_XLS = "XLS";
	InputStream inputStream;
	ReportHelper reportHelper;
	Statement incomeExpenditureStatement = new Statement();
	IncomeExpenditureService incomeExpenditureService;
	IncomeExpenditureScheduleService incomeExpenditureScheduleService;
	
	public void setIncomeExpenditureService(IncomeExpenditureService incomeExpenditureService) {
		this.incomeExpenditureService = incomeExpenditureService;
	}
	
	public void setIncomeExpenditureScheduleService(IncomeExpenditureScheduleService incomeExpenditureScheduleService) {
		this.incomeExpenditureScheduleService = incomeExpenditureScheduleService;
	}

	public void setReportHelper(final ReportHelper reportHelper) {
		this.reportHelper = reportHelper;
	}
	
	public InputStream getInputStream() {
		return inputStream;
	}
	
	public Statement getIncomeExpenditureStatement() {
		return incomeExpenditureStatement;
	}
	
	public IncomeExpenditureReportAction(){
		addRelatedEntity("department", DepartmentImpl.class);
		addRelatedEntity("function", CFunction.class);
		addRelatedEntity("functionary", Functionary.class);
		addRelatedEntity("financialYear",CFinancialYear.class);
		addRelatedEntity("field",BoundaryImpl.class);
	}
	
	public void prepare(){
		super.prepare();
		if(!parameters.containsKey("showDropDown")){
			EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
			addDropdownData("departmentList", masterCache.get("egi-department"));
			addDropdownData("functionList", masterCache.get("egi-function"));
			addDropdownData("functionaryList", masterCache.get("egi-functionary"));
			addDropdownData("fieldList", persistenceService.findAllBy("from BoundaryImpl order by name"));
			addDropdownData("financialYearList", getPersistenceService().findAllBy("from CFinancialYear where isActive=1 and isActiveForPosting=1 order by finYearRange desc "));
		}
	}

	protected void setRelatedEntitesOn() {
		if(incomeExpenditureStatement.getDepartment() != null && incomeExpenditureStatement.getDepartment().getId()!=null && incomeExpenditureStatement.getDepartment().getId()!=0)
			incomeExpenditureStatement.setDepartment((DepartmentImpl)getPersistenceService().find("from DepartmentImpl where id=?", incomeExpenditureStatement.getDepartment().getId()));
		else
			incomeExpenditureStatement.setDepartment(null);
		if(incomeExpenditureStatement.getFinancialYear() != null && incomeExpenditureStatement.getFinancialYear().getId() !=null && incomeExpenditureStatement.getFinancialYear().getId() !=0)
			incomeExpenditureStatement.setFinancialYear((CFinancialYear)getPersistenceService().find("from CFinancialYear where id=?", incomeExpenditureStatement.getFinancialYear().getId()));
		if(incomeExpenditureStatement.getField() != null && incomeExpenditureStatement.getField().getId()!=null && incomeExpenditureStatement.getField().getId()!=0)
			incomeExpenditureStatement.setField((BoundaryImpl)getPersistenceService().find("from BoundaryImpl where id=?", incomeExpenditureStatement.getField().getId()));
		if(incomeExpenditureStatement.getFunction() != null && incomeExpenditureStatement.getFunction().getId() != null && incomeExpenditureStatement.getFunction().getId() != 0)
			incomeExpenditureStatement.setFunction((CFunction)getPersistenceService().find("from CFunction where id=?", incomeExpenditureStatement.getFunction().getId()));
		if(incomeExpenditureStatement.getFunctionary() != null && incomeExpenditureStatement.getFunctionary().getId()!=null && incomeExpenditureStatement.getFunctionary().getId()!=0)
			incomeExpenditureStatement.setFunctionary((Functionary)getPersistenceService().find("from Functionary where id=?", incomeExpenditureStatement.getFunctionary().getId()));
	}
	
	@Override
	public Object getModel() {
		return incomeExpenditureStatement;
	}
	
	public String generateIncomeExpenditureReport(){
		return "report";
	}
	
	public String generateIncomeExpenditureSubReport(){
		populateDataSourceForSchedule();
		return "scheduleResults";
	}
	
	public String generateScheduleReport(){
		populateDataSourceForAllSchedules();
		return "allScheduleResults";
	}

	private void populateDataSourceForSchedule() {
		setRelatedEntitesOn();
		incomeExpenditureStatement.setFunds(incomeExpenditureService.getFunds());
		incomeExpenditureScheduleService.populateDataForSchedule(incomeExpenditureStatement,parameters.get("majorCode")[0]);
	}

	private void populateDataSourceForAllSchedules() {
		setRelatedEntitesOn();
		incomeExpenditureStatement.setFunds(incomeExpenditureService.getFunds());
		incomeExpenditureScheduleService.populateDataForAllSchedules(incomeExpenditureStatement);
	}

	public String printIncomeExpenditureReport(){
		populateDataSource();
		return "report";
	}
	
	public String ajaxPrintIncomeExpenditureReport(){
		populateDataSource();
		return "results";
	}
	
	public String generateIncomeExpenditurePdf() throws Exception{
		populateDataSource();
		JasperPrint jasper = reportHelper.generateFinancialStatementReportJasperPrint(incomeExpenditureStatement,getText("report.ie.heading"),
				getPreviousYearToDate(),getCurrentYearToDate(),true);
		inputStream = reportHelper.exportPdf(inputStream, jasper);
		return INCOME_EXPENSE_PDF;
	}
	
	public String generateIncomeExpenditureXls() throws Exception{
		populateDataSource();
		JasperPrint jasper = reportHelper.generateFinancialStatementReportJasperPrint(incomeExpenditureStatement,getText("report.ie.heading"),
				getPreviousYearToDate(),getCurrentYearToDate(),true);
		inputStream = reportHelper.exportXls(inputStream, jasper);
		return INCOME_EXPENSE_XLS;
	}

	public String generateSchedulePdf() throws Exception{
		populateDataSourceForAllSchedules();
		JasperPrint jasper = reportHelper.generateFinancialStatementReportJasperPrint(incomeExpenditureStatement,getText("report.ie.heading"),
				getPreviousYearToDate(),getCurrentYearToDate(),false);
		inputStream = reportHelper.exportPdf(inputStream, jasper);
		return INCOME_EXPENSE_PDF;
	}
	
	public String generateScheduleXls() throws Exception{
		populateDataSourceForAllSchedules();
		JasperPrint jasper = reportHelper.generateFinancialStatementReportJasperPrint(incomeExpenditureStatement,getText("report.ie.heading"),
				getPreviousYearToDate(),getCurrentYearToDate(),false);
		inputStream = reportHelper.exportXls(inputStream, jasper);
		return INCOME_EXPENSE_XLS;
	}

	public String generateIncomeExpenditureSchedulePdf() throws Exception{
		populateDataSourceForSchedule();
		JasperPrint jasper = reportHelper.generateFinancialStatementReportJasperPrint(incomeExpenditureStatement,getText("report.ie.schedule.heading"),
				getPreviousYearToDate(),getCurrentYearToDate(),false);
		inputStream = reportHelper.exportPdf(inputStream, jasper);
		return INCOME_EXPENSE_PDF;
	}
	
	public String generateIncomeExpenditureScheduleXls() throws Exception{
		populateDataSourceForSchedule();
		JasperPrint jasper = reportHelper.generateFinancialStatementReportJasperPrint(incomeExpenditureStatement,getText("report.ie.schedule.heading"),
				getPreviousYearToDate(),getCurrentYearToDate(),false);
		inputStream = reportHelper.exportXls(inputStream, jasper);
		return INCOME_EXPENSE_XLS;
	}

	protected void populateDataSource() {
		setRelatedEntitesOn();
		incomeExpenditureStatement.setFunds(incomeExpenditureService.getFunds());
		incomeExpenditureService.populateIEStatement(incomeExpenditureStatement);
	}
	
	public String getCurrentYearToDate(){
		return incomeExpenditureService.getFormattedDate(incomeExpenditureService.getToDate(incomeExpenditureStatement));
	}
	public String getPreviousYearToDate(){
		return incomeExpenditureService.getFormattedDate(incomeExpenditureService.getPreviousYearFor(incomeExpenditureService.getToDate(incomeExpenditureStatement)));
	}
}
