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
import org.egov.services.report.BalanceSheetScheduleService;
import org.egov.services.report.BalanceSheetService;
import org.egov.utils.Constants;
import org.egov.utils.ReportHelper;
import org.egov.web.actions.BaseFormAction;

@Results(value={
		@Result(name="balanceSheet-PDF",type="stream",location=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"application/pdf",Constants.CONTENT_DISPOSITION,"no-cache;filename=BalanceSheet.pdf"}),
		@Result(name="balanceSheet-XLS",type="stream",location=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"application/xls",Constants.CONTENT_DISPOSITION,"no-cache;filename=BalanceSheet.xls"}),
		@Result(name="balanceSheet-HTML",type="stream",location=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"text/html"})
	})
	
@ParentPackage("egov")
public class BalanceSheetReportAction extends BaseFormAction{
	private static final String BALANCE_SHEET_PDF = "balanceSheet-PDF";
	private static final String BALANCE_SHEET_XLS = "balanceSheet-XLS";
	InputStream inputStream;
	ReportHelper reportHelper;
	Statement balanceSheet = new Statement();
	BalanceSheetService balanceSheetService;
	BalanceSheetScheduleService balanceSheetScheduleService;
	
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
		if(balanceSheet.getDepartment() != null && balanceSheet.getDepartment().getId()!=null)
			balanceSheet.setDepartment((DepartmentImpl)getPersistenceService().find("from DepartmentImpl where id=?", balanceSheet.getDepartment().getId()));
		else
			balanceSheet.setDepartment(null);
		if(balanceSheet.getFinancialYear() != null && balanceSheet.getFinancialYear().getId() !=null)
			balanceSheet.setFinancialYear((CFinancialYear)getPersistenceService().find("from CFinancialYear where id=?", balanceSheet.getFinancialYear().getId()));
		if(balanceSheet.getField() != null && balanceSheet.getField().getId()!=null)
			balanceSheet.setField((BoundaryImpl)getPersistenceService().find("from BoundaryImpl where id=?", balanceSheet.getField().getId()));
		if(balanceSheet.getFunction() != null && balanceSheet.getFunction().getId() != null)
			balanceSheet.setFunction((CFunction)getPersistenceService().find("from CFunction where id=?", balanceSheet.getFunction().getId()));
		if(balanceSheet.getFunctionary() != null && balanceSheet.getFunctionary().getId()!=null)
			balanceSheet.setFunctionary((Functionary)getPersistenceService().find("from Functionary where id=?", balanceSheet.getFunctionary().getId()));
	}
	
	@Override
	public Object getModel() {
		return balanceSheet;
	}
	
	public String generateBalanceSheetReport(){
		return "report";
	}
	
	public String generateBalanceSheetSubReport(){
		populateDataSourceForSchedule();
		return "scheduleResults";
	}
	
	public String generateScheduleReport(){
		populateDataSourceForAllSchedules();
		return "allScheduleResults";
	}

	private void populateDataSourceForSchedule() {
		setRelatedEntitesOn();
		balanceSheet.setFunds(balanceSheetService.getFunds());
		balanceSheetScheduleService.populateDataForSchedule(balanceSheet,parameters.get("majorCode")[0]);
	}

	private void populateDataSourceForAllSchedules() {
		setRelatedEntitesOn();
		balanceSheet.setFunds(balanceSheetService.getFunds());
		balanceSheetScheduleService.populateDataForAllSchedules(balanceSheet);
	}

	public String printBalanceSheetReport(){
		populateDataSource();
		return "report";
	}
	
	public String generateBalanceSheetPdf() throws Exception{
		populateDataSource();
		JasperPrint jasper = reportHelper.generateFinancialStatementReportJasperPrint(balanceSheet,getText("report.heading"),
				balanceSheetService.getFormattedDate(balanceSheetService.getFromDate(balanceSheet)),balanceSheetService.getFormattedDate(balanceSheetService.getToDate(balanceSheet)),true);
		inputStream = reportHelper.exportPdf(inputStream, jasper);
		return BALANCE_SHEET_PDF;
	}
	
	public String generateBalanceSheetXls() throws Exception{
		populateDataSource();
		JasperPrint jasper = reportHelper.generateFinancialStatementReportJasperPrint(balanceSheet,getText("report.heading"),
				balanceSheetService.getFormattedDate(balanceSheetService.getFromDate(balanceSheet)),balanceSheetService.getFormattedDate(balanceSheetService.getToDate(balanceSheet)),true);
		inputStream = reportHelper.exportXls(inputStream, jasper);
		return BALANCE_SHEET_XLS;
	}

	public String generateSchedulePdf() throws Exception{
		populateDataSourceForAllSchedules();
		JasperPrint jasper = reportHelper.generateFinancialStatementReportJasperPrint(balanceSheet,getText("report.heading"),
				balanceSheetService.getFormattedDate(balanceSheetService.getFromDate(balanceSheet)),balanceSheetService.getFormattedDate(balanceSheetService.getToDate(balanceSheet)),false);
		inputStream = reportHelper.exportPdf(inputStream, jasper);
		return BALANCE_SHEET_PDF;
	}
	
	public String generateScheduleXls() throws Exception{
		populateDataSourceForAllSchedules();
		JasperPrint jasper = reportHelper.generateFinancialStatementReportJasperPrint(balanceSheet,getText("report.heading"),
				balanceSheetService.getFormattedDate(balanceSheetService.getFromDate(balanceSheet)),balanceSheetService.getFormattedDate(balanceSheetService.getToDate(balanceSheet)),false);
		inputStream = reportHelper.exportXls(inputStream, jasper);
		return BALANCE_SHEET_XLS;
	}

	public String generateBalanceSheetSchedulePdf() throws Exception{
		populateDataSourceForSchedule();
		JasperPrint jasper = reportHelper.generateFinancialStatementReportJasperPrint(balanceSheet,getText("report.sub.schedule.heading"),
				balanceSheetService.getFormattedDate(balanceSheetService.getFromDate(balanceSheet)),balanceSheetService.getFormattedDate(balanceSheetService.getToDate(balanceSheet)),false);
		inputStream = reportHelper.exportPdf(inputStream, jasper);
		return BALANCE_SHEET_PDF;
	}
	
	public String generateBalanceSheetScheduleXls() throws Exception{
		populateDataSourceForSchedule();
		JasperPrint jasper = reportHelper.generateFinancialStatementReportJasperPrint(balanceSheet,getText("report.sub.schedule.heading"),
				balanceSheetService.getFormattedDate(balanceSheetService.getFromDate(balanceSheet)),balanceSheetService.getFormattedDate(balanceSheetService.getToDate(balanceSheet)),false);
		inputStream = reportHelper.exportXls(inputStream, jasper);
		return BALANCE_SHEET_XLS;
	}

	protected void populateDataSource() {
		setRelatedEntitesOn();
		balanceSheet.setFunds(balanceSheetService.getFunds());
		balanceSheetService.populateBalanceSheet(balanceSheet);
	}
	
	public String getCurrentYearToDate(){
		return balanceSheetService.getFormattedDate(balanceSheetService.getToDate(balanceSheet));
	}
	public String getPreviousYearToDate(){
		return balanceSheetService.getFormattedDate(balanceSheetService.getPreviousYearFor(balanceSheetService.getToDate(balanceSheet)));
	}
}
