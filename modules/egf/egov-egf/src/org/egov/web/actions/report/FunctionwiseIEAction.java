package org.egov.web.actions.report;

import java.io.InputStream;
import java.util.Date;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFinancialYear;
import org.egov.lib.admbndry.CityWebsite;
import org.egov.lib.admbndry.CityWebsiteDAO;
import org.egov.services.report.FunctionwiseIEService;
import org.egov.utils.Constants;
import org.egov.utils.ReportHelper;

@Results(value={
		@Result(name="functionwiseIE-PDF",type="stream",location=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"application/pdf","contentDisposition","no-cache;filename=FunctionwiseIE.pdf"}),
		@Result(name="functionwiseIE-XLS",type="stream",location=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"application/xls","contentDisposition","no-cache;filename=FunctionwiseIE.xls"}),
		@Result(name="functionwiseIE-HTML",type="stream",location=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"text/html"})
	})
	
@ParentPackage("egov")
public class FunctionwiseIEAction extends ReportAction
{
	private static final long serialVersionUID = 1L;
	protected InputStream inputStream;
	private ReportHelper reportHelper;
	private FunctionwiseIEService functionwiseIEService;
	private final FunctionwiseIE functionwiseIE = new FunctionwiseIE();
	private CityWebsiteDAO cityWebsiteDAO;
	private CityWebsite cityWebsite;
	public void setCityWebsiteDAO(final CityWebsiteDAO cityWebsiteDAO) {
		this.cityWebsiteDAO = cityWebsiteDAO;
	}
	public void setFunctionwiseIEService(final FunctionwiseIEService functionwiseIEService) {
		this.functionwiseIEService = functionwiseIEService;
	}
	public void prepare()
	{
		super.prepare();
		if(reportSearch.getStartDate()==null || reportSearch.getStartDate().equals(""))
			reportSearch.setStartDate(sdf.format(((CFinancialYear)persistenceService.find(" from CFinancialYear where startingDate <= '"+formatter.format(new Date())+"' and endingDate >= '"+formatter.format(new Date())+"'")).getStartingDate()));
		if(reportSearch.getEndDate()==null || reportSearch.getEndDate().equals(""))
			reportSearch.setEndDate(sdf.format(new Date()));
	}
	@Override
	public Object getModel() {
		return reportSearch;
	}
	@SkipValidation
	public String beforesearch()
	{
		return REPORT;
	}
	
	public String search() throws Exception
	{
		populateDataSource();
		return "print";
	}
	
	public String generateFunctionwiseIEHtml() throws Exception
	{
		populateDataSource();
		inputStream = reportHelper.exportHtml(inputStream, reportHelper.generateFunctionwiseIEJasperPrint(functionwiseIE,cityWebsite.getCityName(),getvalue(reportSearch.getIncExp())));
		return "functionwiseIE-HTML";
	}
	public String generateFunctionwiseIEPdf() throws Exception{
		populateDataSource();
		inputStream = reportHelper.exportPdf(inputStream, reportHelper.generateFunctionwiseIEJasperPrint(functionwiseIE,cityWebsite.getCityName(),getvalue(reportSearch.getIncExp())));
		return "functionwiseIE-PDF";
	}
	
	public String generateFunctionwiseIEXls() throws Exception{
		populateDataSource();
		inputStream = reportHelper.exportXls(inputStream, reportHelper.generateFunctionwiseIEJasperPrint(functionwiseIE,cityWebsite.getCityName(),getvalue(reportSearch.getIncExp())));
		return "functionwiseIE-XLS";
	}
	public void populateDataSource()throws Exception
	{
		functionwiseIEService.setReportSearch(reportSearch);
		functionwiseIEService.populateData(functionwiseIE);
		cityWebsite = cityWebsiteDAO.getCityWebSiteByURL((String) this.getSession().get("cityurl"));
		functionwiseIE.setCityName(cityWebsite.getCityName());
	}
	
	public String getvalue(final String incExp)
	{
		if("I".equals(incExp))
			return "Income";
		else
			return "Expense";
	}
	public InputStream getInputStream() {
		return inputStream;
	}

	public void setReportHelper(final ReportHelper reportHelper) {
		this.reportHelper = reportHelper;
	}
	public void validate()
	{
		if(reportSearch.getIncExp()==null || reportSearch.getIncExp().equals("-1") )
			addFieldError("incExp", getMessage("report.income.expense.mandatory"));
		if(reportSearch.getStartDate()==null || reportSearch.getStartDate().equals(""))
			addFieldError("startDate", getMessage("report.startdate.mandatory"));
		if(reportSearch.getEndDate()==null || reportSearch.getEndDate().equals(""))
			addFieldError("endDate", getMessage("report.enddate.mandatory"));
		try{
			if(!reportSearch.getStartDate().equals("") )
				 sdf.parse(reportSearch.getStartDate());
		}catch(Exception e){addFieldError("startDate", getMessage("report.startdate.invalid.format"));}
		try{
			if(!reportSearch.getEndDate().equals("") )
				 sdf.parse(reportSearch.getEndDate());
		}catch(Exception e){addFieldError("endDate", getMessage("report.enddate.invalid.format"));}
		super.validate();
	}
	public FunctionwiseIE getFunctionwiseIE() {
		return functionwiseIE;
	}
	protected String getMessage(String key) {
		return getText(key);
	}
}
