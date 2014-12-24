package org.egov.web.actions.report;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.Vouchermis;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.egf.commons.EgovCommon;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.budget.BudgetGroup;
import org.egov.model.payment.Paymentheader;
import org.egov.services.budget.BudgetDetailService;
import org.egov.services.budget.BudgetService;
import org.egov.utils.BudgetAccountType;
import org.egov.utils.BudgetDetailConfig;
import org.egov.utils.Constants;
import org.egov.web.actions.BaseFormAction;

@Results(value={
		@Result(name="PDF",type="stream",location="inputStream", params={"inputName","inputStream","contentType","application/pdf","contentDisposition","no-cache;filename=BudgetVarianceReport.pdf"}),
		@Result(name="XLS",type="stream",location="inputStream", params={"inputName","inputStream","contentType","application/xls","contentDisposition","no-cache;filename=BudgetVarianceReport.xls"})
	})

@ParentPackage("egov")
public class BudgetVarianceReportAction extends BaseFormAction{
	String jasperpath = "budgetVarianceReport";
	List<Paymentheader> paymentHeaderList = new ArrayList<Paymentheader>();
	private List<BudgetVarianceEntry> budgetVarianceEntries = new ArrayList<BudgetVarianceEntry>();
	private Date asOnDate = new Date();
	private InputStream inputStream;
	private EgovCommon egovCommon;
	protected List<String> headerFields = new ArrayList<String>();
	protected List<String> mandatoryFields = new ArrayList<String>();
	private Vouchermis vouchermis = new Vouchermis();
	private GenericHibernateDaoFactory genericDao;
	private ReportService reportService;
	private List<String> accountTypeList = new ArrayList<String>(); 
	private String accountType = "";
	private BudgetDetail budgetDetail = new BudgetDetail();
	private BudgetDetailConfig budgetDetailConfig;
	protected List<String> gridFields = new ArrayList<String>();
	protected BudgetDetailService budgetDetailService;
	private FinancialYearHibernateDAO financialYearDAO;
	private String type = "Budget";
	private BudgetService budgetService;
	String budgetType = Constants.BE;
	private Map<String,Integer> queryParamMap = new HashMap<String,Integer>();
	@Override
	public String execute() throws Exception {
		return "form";
	}
	
	public BudgetVarianceReportAction(BudgetDetailConfig config){
		this.budgetDetailConfig = config;
		headerFields = budgetDetailConfig.getHeaderFields();
		gridFields = budgetDetailConfig.getGridFields();
		mandatoryFields = budgetDetailConfig.getMandatoryFields();
		if(shouldShowHeaderField(Constants.EXECUTING_DEPARTMENT)){
			addRelatedEntity("executingDepartment", DepartmentImpl.class);
		}
		if(shouldShowHeaderField(Constants.FUND)){
			addRelatedEntity("fund", Fund.class);
		}
		if(shouldShowHeaderField(Constants.FUNCTION)){
			addRelatedEntity("function", CFunction.class);
		}
		if(shouldShowHeaderField(Constants.SCHEME)){
			addRelatedEntity("scheme", Scheme.class);
		}
		if(shouldShowHeaderField(Constants.SUBSCHEME)){
			addRelatedEntity("subscheme", SubScheme.class);
		}
		if(shouldShowHeaderField(Constants.FUNCTIONARY)){
			addRelatedEntity("functionary", Functionary.class);
		}
		if(shouldShowHeaderField(Constants.FUNDSOURCE)){
			addRelatedEntity("fundsource", Fundsource.class);
		}
		if(shouldShowHeaderField(Constants.BOUNDARY)){
			addRelatedEntity("boundary", BoundaryImpl.class);
		}
		addRelatedEntity("budgetGroup", BudgetGroup.class);
	}

	@Override
	public void prepare() {
		super.prepare();
		if(!parameters.containsKey("skipPrepare")){
			accountTypeList.add(BudgetAccountType.REVENUE_EXPENDITURE.name());
			accountTypeList.add(BudgetAccountType.REVENUE_RECEIPTS.name());
			accountTypeList.add(BudgetAccountType.CAPITAL_EXPENDITURE.name());
			accountTypeList.add(BudgetAccountType.CAPITAL_RECEIPTS.name());
			EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
			addDropdownData("accountTypeList", accountTypeList);
			dropdownData.put("budgetGroupList", masterCache.get("egf-budgetGroup"));
			if(shouldShowHeaderField(Constants.EXECUTING_DEPARTMENT)){
				addDropdownData("departmentList", persistenceService.findAllBy("from DepartmentImpl order by deptName"));
			}
			if(shouldShowHeaderField(Constants.FUNCTION)){
				addDropdownData("functionList", persistenceService.findAllBy("from CFunction where isactive=1 and isnotleaf=0  order by name"));
			}
			if(shouldShowHeaderField(Constants.FUNCTIONARY)){
				addDropdownData("functionaryList", persistenceService.findAllBy(" from Functionary where isactive=1 order by name"));
			}
			if(shouldShowHeaderField(Constants.FUND)){
				addDropdownData("fundList", persistenceService.findAllBy(" from Fund where isactive=1 and isnotleaf=0 order by name"));
			}
			if(shouldShowHeaderField(Constants.FIELD)){
				addDropdownData("fieldList", persistenceService.findAllBy(" from BoundaryImpl b where lower(b.boundaryType.name)='ward' "));
			}
			if(shouldShowHeaderField(Constants.SCHEME)){
				addDropdownData("schemeList",  Collections.EMPTY_LIST );
			}
			if(shouldShowHeaderField(Constants.SUBSCHEME)){
				addDropdownData("subschemeList", Collections.EMPTY_LIST);
			}
		}
	}
	
	public String ajaxLoadData(){
		populateData();
		return "results";
	}

	public boolean shouldShowHeaderField(String fieldName){
		return headerFields.contains(fieldName) || gridFields.contains(fieldName);
	}
	
	Date parseDate(String stringDate) {
		if(parameters.containsKey(stringDate) && parameters.get(stringDate)[0]!=null){
			try {
				return Constants.DDMMYYYYFORMAT2.parse(parameters.get(stringDate)[0]);
			} catch (ParseException e) {
				throw new ValidationException("Invalid date","Invalid date");
			}
		}
		return new Date();
	}
	
	public List<Paymentheader> getPaymentHeaderList(){
		return paymentHeaderList;
	}

	public void setAsOnDate(Date startDate) {
		this.asOnDate = startDate;
	}

	public Date getAsOnDate() {
		return asOnDate;
	}
	
	public String getFormattedDate(Date date){
		return Constants.DDMMYYYYFORMAT2.format(date);
	}

	public String exportPdf() throws JRException, IOException{
		generateReport();
	    return "PDF";
	}

	private void generateReport() {
		populateData();
		ReportRequest reportInput = new ReportRequest(jasperpath, budgetVarianceEntries, getParamMap());
		ReportOutput reportOutput = reportService.createReport(reportInput);
		inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
	}
	
	Map<String, Object> getParamMap() {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("departmentName", getDepartmentName());
		String estimateHeading = "";
		if(Constants.BE.equalsIgnoreCase(budgetType))
			estimateHeading = "Budget Estimate";
		else
			estimateHeading = "Revised Estimate";
		paramMap.put("estimateHeading", estimateHeading);
		paramMap.put("asOnDate", Constants.DDMMYYYYFORMAT2.format(asOnDate));
		return paramMap;
	}


	private void populateData() {
		CFinancialYear financialYear = financialYearDAO.getFinancialYearByDate(asOnDate);
		boolean hasReForYear = budgetService.hasReForYear(financialYear.getId());
		if(hasReForYear){
			type = "Revised";
			budgetType = Constants.RE;
		}
		List<BudgetDetail> result = persistenceService.findAllBy("from BudgetDetail where budget.isbere='"+budgetType+"' and " +
				"budget.isActiveBudget=1 and budget.state.value='END' and budget.financialYear.id="+financialYear.getId()
				+getMiscQuery()+" order by budget.name,budgetGroup.name");
		if(budgetVarianceEntries==null)
			budgetVarianceEntries = new ArrayList<BudgetVarianceEntry>();
		for (BudgetDetail budgetDetail : result) {
			BudgetVarianceEntry budgetVarianceEntry = new BudgetVarianceEntry();
			budgetVarianceEntry.setBudgetHead(budgetDetail.getBudgetGroup().getName());
			if(budgetDetail.getExecutingDepartment()!=null){
				budgetVarianceEntry.setDepartmentCode(budgetDetail.getExecutingDepartment().getDeptCode());
				budgetVarianceEntry.setDepartmentName(budgetDetail.getExecutingDepartment().getDeptName());
			}
			if(budgetDetail.getFund()!=null)
				budgetVarianceEntry.setFundCode(budgetDetail.getFund().getName());
			if(budgetDetail.getFunction()!=null)
				budgetVarianceEntry.setFunctionCode(budgetDetail.getFunction().getName());
			budgetVarianceEntry.setEstimate(budgetDetail.getApprovedAmount()==null?BigDecimal.ZERO:budgetDetail.getApprovedAmount());
			budgetVarianceEntry.setDetailId(budgetDetail.getId());
			budgetVarianceEntry.setBudgetCode(budgetDetail.getBudget().getName());
			budgetVarianceEntry.setAdditionalAppropriation(budgetDetail.getApprovedReAppropriationsTotal()==null?BigDecimal.ZERO:budgetDetail.getApprovedReAppropriationsTotal());
			budgetVarianceEntries.add(budgetVarianceEntry);
		}
		populateActualData(financialYear);
	}
	
	private String getMiscQuery() {
		StringBuilder query = new StringBuilder();
		if(budgetDetail.getExecutingDepartment()!=null && budgetDetail.getExecutingDepartment().getId()!=null && budgetDetail.getExecutingDepartment().getId()!=-1)
			query.append(" and executingDepartment.id=").append(budgetDetail.getExecutingDepartment().getId());	
		if(budgetDetail.getBudgetGroup()!=null && budgetDetail.getBudgetGroup().getId()!=null && budgetDetail.getBudgetGroup().getId()!=-1)
			query.append(" and budgetGroup.id=").append(budgetDetail.getBudgetGroup().getId());	
		if(budgetDetail.getFunction()!=null && budgetDetail.getFunction().getId()!=null && budgetDetail.getFunction().getId()!=-1)
			query.append(" and function.id=").append(budgetDetail.getFunction().getId());	
		if(budgetDetail.getFund()!=null && budgetDetail.getFund().getId()!=null && budgetDetail.getFund().getId()!=-1)
			query.append(" and fund.id=").append(budgetDetail.getFund().getId());	
		if(budgetDetail.getFunctionary()!=null && budgetDetail.getFunctionary().getId()!=null && budgetDetail.getFunctionary().getId()!=-1)
			query.append(" and functionary.id=").append(budgetDetail.getFunctionary().getId());	
		if(budgetDetail.getScheme()!=null && budgetDetail.getScheme().getId()!=null && budgetDetail.getScheme().getId()!=-1)
			query.append(" and scheme.id=").append(budgetDetail.getScheme().getId());	
		if(budgetDetail.getSubScheme()!=null && budgetDetail.getSubScheme().getId()!=null && budgetDetail.getSubScheme().getId()!=-1)
			query.append(" and subScheme.id=").append(budgetDetail.getSubScheme().getId());	
		if(budgetDetail.getBoundary()!=null && budgetDetail.getBoundary().getId()!=null && budgetDetail.getBoundary().getId()!=-1)
			query.append(" and boundary.id=").append(budgetDetail.getBoundary().getId());	
		if(!"".equals(accountType) && !"-1".equals(accountType))
			query.append(" and budgetGroup.accountType='").append(accountType).append("'");
		return query.toString();
	}
	private void setQueryParams()
	{
		if(budgetVarianceEntries!=null && budgetVarianceEntries.size()!=0)
		{
			if(budgetDetail.getExecutingDepartment()!=null && budgetDetail.getExecutingDepartment().getId()!=null && budgetDetail.getExecutingDepartment().getId()!=-1)
				queryParamMap.put("deptId",budgetDetail.getExecutingDepartment().getId());
			if(budgetDetail.getFunction()!=null && budgetDetail.getFunction().getId()!=null && budgetDetail.getFunction().getId()!=-1)
				queryParamMap.put("functionId",Integer.parseInt(budgetDetail.getFunction().getId().toString()));
			if(budgetDetail.getFund()!=null && budgetDetail.getFund().getId()!=null && budgetDetail.getFund().getId()!=-1)
				queryParamMap.put("fundId",budgetDetail.getFund().getId());
		}
	}
	private void populateActualData(CFinancialYear financialYear){
		String fromDate = Constants.DDMMYYYYFORMAT2.format(financialYear.getStartingDate());
		setQueryParams();
		List<Object[]> resultForVoucher = budgetDetailService.fetchActualsForFYWithParams(fromDate,"'"+Constants.DDMMYYYYFORMAT2.format(asOnDate)+"'",mandatoryFields,queryParamMap);
		extractData(resultForVoucher);
		List<Object[]> resultForBill = budgetDetailService.fetchActualsForBillWithParams(fromDate,"'"+Constants.DDMMYYYYFORMAT2.format(asOnDate)+"'",mandatoryFields,queryParamMap);
		extractData(resultForBill);
	}

	private void extractData(List<Object[]> result) {
		Map<String,String> budgetDetailIdsAndAmount = new HashMap<String, String>();
		if(result==null )
			return ;
		for (Object[] row : result) {
			if(row[0]!=null && row[1]!=null)
				budgetDetailIdsAndAmount.put(row[0].toString(), row[1].toString());
		}
		for (BudgetVarianceEntry row : budgetVarianceEntries) {
			BigDecimal actual = row.getActual();
			if(budgetDetailIdsAndAmount.get(row.getDetailId().toString()) != null){
				if(actual == null || BigDecimal.ZERO.compareTo(actual)==0)
					row.setActual(new BigDecimal(budgetDetailIdsAndAmount.get(row.getDetailId().toString())));
				else
					row.setActual(row.getActual().add(new BigDecimal(budgetDetailIdsAndAmount.get(row.getDetailId().toString()))));
			}else{
				if(actual == null)
					row.setActual(BigDecimal.ZERO);
			}
			row.setVariance(row.getEstimate().add(row.getAdditionalAppropriation().subtract(row.getActual()==null?BigDecimal.ZERO:row.getActual())));
		}
	}

	public String exportXls() throws JRException, IOException{
		populateData();
		ReportRequest reportInput = new ReportRequest(jasperpath, budgetVarianceEntries, getParamMap());
		reportInput.setReportFormat(FileFormat.XLS);
		ReportOutput reportOutput = reportService.createReport(reportInput);
		inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
	    return "XLS";
	}
	
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setEgovCommon(EgovCommon egovCommon) {
		this.egovCommon = egovCommon;
	}

	public EgovCommon getEgovCommon() {
		return egovCommon;
	}

	public void setBudgetVarianceEntries(List<BudgetVarianceEntry> bankBookViewEntries) {
		this.budgetVarianceEntries = bankBookViewEntries;
	}

	public List<BudgetVarianceEntry> getBudgetVarianceEntries() {
		return budgetVarianceEntries;
	}

	public Vouchermis getVouchermis(){
		return vouchermis;
	}

	@Override
	public Object getModel() {
		return budgetDetail;
	}

	public void setGenericDao(GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}

	public GenericHibernateDaoFactory getGenericDao() {
		return genericDao;
	}

	public void setVouchermis(Vouchermis vouchermis) {
		this.vouchermis = vouchermis;
	}

	public List<String> getAccountTypeList() {
		return accountTypeList;
	}

	public void setBudgetDetail(BudgetDetail budgetDetail) {
		this.budgetDetail = budgetDetail;
	}

	public BudgetDetail getBudgetDetail() {
		return budgetDetail;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setBudgetDetailConfig(BudgetDetailConfig budgetDetailConfig) {
		this.budgetDetailConfig = budgetDetailConfig;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}
	
	public void setBudgetDetailService(BudgetDetailService budgetDetailService) {
		this.budgetDetailService = budgetDetailService;
	}

	public void setFinancialYearDAO(FinancialYearHibernateDAO financialYearDAO) {
		this.financialYearDAO = financialYearDAO;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setBudgetService(BudgetService budgetService) {
		this.budgetService = budgetService;
	}
	
	public boolean isFieldMandatory(String field){
		return mandatoryFields.contains(field);
	}

	public String getDepartmentName(){
		if(budgetDetail.getExecutingDepartment()!=null && budgetDetail.getExecutingDepartment().getId()!=null && budgetDetail.getExecutingDepartment().getId()!=-1){
			DepartmentImpl department = (DepartmentImpl) persistenceService.find("from DepartmentImpl where id=?",budgetDetail.getExecutingDepartment().getId());
			return department.getDeptName();
		}
		return "";
	}

}
