package org.egov.web.actions.budget;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import net.sf.jasperreports.engine.JRException;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.budget.BudgetGroup;
import org.egov.services.budget.BudgetService;
import org.egov.utils.Constants;
import org.egov.utils.ReportHelper;
import org.egov.web.actions.BaseFormAction;

@Results(value={
		@Result(name="department-PDF",type="redirect",location=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"application/pdf",Constants.CONTENT_DISPOSITION,"no-cache;filename=BudgetReport.pdf"}),
		@Result(name="department-XLS",type="redirect",location=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"application/xls",Constants.CONTENT_DISPOSITION,"no-cache;filename=BudgetReport.xls"}),
		@Result(name="department-HTML",type="redirect",location=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"text/html"}),
		@Result(name="functionwise-PDF",type="redirect",location=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"application/pdf",Constants.CONTENT_DISPOSITION,"no-cache;filename=BudgetReport-functionwise.pdf"}),
		@Result(name="functionwise-XLS",type="redirect",location=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"application/xls",Constants.CONTENT_DISPOSITION,"no-cache;filename=BudgetReport-functionwise.xls"}),
		@Result(name="functionwise-HTML",type="redirect",location=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"text/html"})
	})

@ParentPackage("egov")
public class BudgetReportAction extends BaseFormAction{
	private static final String DEPTWISEPATH = "/org/egov/web/actions/budget/departmentWiseBudgetReport.jasper";
	private static final String FUNCTIONWISEPATH="/org/egov/web/actions/budget/budgetReportFunctionwise.jasper";
	ReportHelper reportHelper;
	private static final long serialVersionUID = 1L;
	InputStream inputStream;
	BudgetReport budgetReport = new BudgetReport();
	List budgetReportList = new ArrayList<BudgetReportView>();
	private GenericHibernateDaoFactory genericDao;	
	int majorCodeLength =0;
	FinancialYearDAO financialYearDAO;
	BudgetService budgetService;
	private static final String EMPTYSTRING="";
	private static final String TOTALROW="totalrow";
	private static final String TOTALSTRING="TOTAL";
	private final Map<String,String> coaMap = new HashMap<String,String>();
	private Map<String,String> refNoMap = new HashMap<String,String>();
	private Map<Object,BigDecimal> reAppropriationMap =  new HashMap<Object,BigDecimal>();
	private final List<BudgetReportView> reportStoreList = new ArrayList<BudgetReportView>();
	public void setBudgetService(final BudgetService budgetService) {
		this.budgetService = budgetService;
	}
	
	public void setFinancialYearDAO(final FinancialYearDAO financialYearDAO) {
		this.financialYearDAO = financialYearDAO;
	}
	public void setGenericDao(final GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}

	public void setReportHelper(final ReportHelper reportHelper) {
		this.reportHelper = reportHelper;
	}

	public InputStream getInputStream() {
		return inputStream;
	}
	
	@Override
	public Object getModel() {
		return budgetReport;
	}
	public String functionwise(){
		return "functionwise";
	}
	
	public BudgetReportAction(){
		addRelatedEntity("department", DepartmentImpl.class);
		addRelatedEntity("function", CFunction.class);
		addRelatedEntity("financialYear",CFinancialYear.class);
	}
	
	public void prepare()
	{
		EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
		super.prepare();
		addDropdownData("departmentList", masterCache.get("egi-department"));
		addDropdownData("functionList", masterCache.get("egi-function"));
		addDropdownData("financialYearList", getPersistenceService().findAllBy("from CFinancialYear where isActive=1 and isActiveForPosting=1 order by finYearRange desc "));
		setRelatedEntitesOn();
		majorCodeLength = Integer.valueOf(getAppConfigValueFor(Constants.EGF,"coa_majorcode_length"));
	}
	public String getFunctionwiseReport()
	{
		return "printFunctionwise";
	}
	public String ajaxGenerateFunctionWiseHtml() throws IOException{
		inputStream = reportHelper.exportHtml(inputStream, FUNCTIONWISEPATH, getParamMap(),getDataForFunctionwise(),"pt");
		return "functionwise-HTML";
	}
	public String generateFunctionWisePdf() throws JRException, IOException{
		inputStream = reportHelper.exportPdf(inputStream, FUNCTIONWISEPATH, getParamMap(), getDataForFunctionwise());
		return "functionwise-PDF";
	}
	
	public String generateFunctionWiseXls() throws JRException, IOException{
		inputStream = reportHelper.exportXls(inputStream, FUNCTIONWISEPATH, getParamMap(), getDataForFunctionwise());
		return "functionwise-XLS";
	}
	
	private String getSql()
	{
		String sql = "";
		sql=" bd.budget.financialYear="+budgetReport.getFinancialYear().getId();
		if(budgetReport.getDepartment()!=null && budgetReport.getDepartment().getId()!=null)
			sql=sql+" and bd.executingDepartment.id="+budgetReport.getDepartment().getId();
		if(budgetReport.getFunction()!=null && budgetReport.getFunction().getId()!=null)
			sql=sql+" and bd.function.id="+budgetReport.getFunction().getId();
		return sql;
	}
	private List<BudgetDetail> getMincodeData()
	{
		final String finalStatus = getFinalStatus();
		String sql = getSql();
		if(budgetReport.getType().equalsIgnoreCase("IE"))
			sql=sql+" and bd.budgetGroup.minCode.type in ('I','E')";
		else if(!budgetReport.getType().equalsIgnoreCase("All"))
			sql=sql+" and bd.budgetGroup.minCode.type='"+budgetReport.getType()+"'";
		
		List<BudgetDetail> budgetDetailList = getPersistenceService().findAllBy(" from BudgetDetail bd where "+sql+" and bd.budget.isbere='RE' and bd.approvedAmount is not null and bd.approvedAmount!=0 and bd.budget.state in (from org.egov.infstr.models.State where type='Budget' and value='"+finalStatus+"' ) order by bd.executingDepartment,bd.function,bd.budgetGroup.minCode.type,bd.budgetGroup.minCode.glcode");
		if(budgetDetailList.isEmpty())
			budgetDetailList = getPersistenceService().findAllBy(" from BudgetDetail bd where "+sql+" and bd.budget.isbere='BE' and bd.approvedAmount is not null and bd.approvedAmount!=0 and bd.budget.state in (from org.egov.infstr.models.State where type='Budget' and value='"+finalStatus+"' ) order by bd.executingDepartment,bd.function,bd.budgetGroup.minCode.type,bd.budgetGroup.minCode.glcode");
		return budgetDetailList;
	}
	
	private List<BudgetDetail> getMajorcodeData()
	{
		final String finalStatus = getFinalStatus();
		String sql = getSql();
		if(budgetReport.getType().equalsIgnoreCase("IE"))
			sql=sql+" and bd.budgetGroup.majorCode.type in ('I','E')";
		else if(!budgetReport.getType().equalsIgnoreCase("All"))
			sql=sql+" and bd.budgetGroup.majorCode.type='"+budgetReport.getType()+"'";
		
		List<BudgetDetail> budgetDetailList = getPersistenceService().findAllBy(" from BudgetDetail bd where "+sql+" and bd.budget.isbere='RE' and bd.approvedAmount is not null and bd.approvedAmount!=0 and bd.budget.state in (from org.egov.infstr.models.State where type='Budget' and value='"+finalStatus+"' ) order by bd.executingDepartment,bd.function,bd.budgetGroup.majorCode.type,bd.budgetGroup.majorCode.glcode");
		if(budgetDetailList.isEmpty())
			budgetDetailList = getPersistenceService().findAllBy(" from BudgetDetail bd where "+sql+" and bd.budget.isbere='BE' and bd.approvedAmount is not null and bd.approvedAmount!=0 and bd.budget.state in (from org.egov.infstr.models.State where type='Budget' and value='"+finalStatus+"' ) order by bd.executingDepartment,bd.function,bd.budgetGroup.majorCode.type,bd.budgetGroup.majorCode.glcode");
		return budgetDetailList;
	}
	
	private List<Object> getDataForFunctionwise()
	{
		List<BudgetDetail> budgetDetailList=null; 
		budgetDetailList = getMincodeData();
		budgetDetailList.addAll(getMajorcodeData());
		if(budgetDetailList.isEmpty())
			return budgetReportList;
		
		Integer deptId=0;Long functionId=0L;
		String type="",majorCode="",glcode="",glType="",glName="",tempMajorCode="";
		BigDecimal totalAmt=BigDecimal.ZERO;
		BigDecimal totalAppropriationAmt=BigDecimal.ZERO;
		BigDecimal reAppropriationAmt=BigDecimal.ZERO; 
		boolean printed=true;
		boolean isFirst=true;
		boolean majorcodewise=false;
		refNoMap = getReferenceNumber("functionWiseBudgetReport");
		getCOA();
		getBudgetReappropriationAmt();
		loadAmountForMajorcodewise(budgetReport.getFinancialYear(),budgetReport.getDepartment(),budgetReport.getFunction());
		for(BudgetDetail detail :budgetDetailList)
		{
			if(detail.getExecutingDepartment()==null || detail.getFunction()==null)
				continue;
			reAppropriationAmt = reAppropriationMap.get(detail.getId())==null?BigDecimal.ZERO:reAppropriationMap.get(detail.getId());
			if(detail.getBudgetGroup().getMajorCode()==null)
			{
				glcode = getGlCode(detail);
				glType = detail.getBudgetGroup().getMinCode().getType().toString();
				glName = getGlName(detail);
			}
			else
			{
				glcode = detail.getBudgetGroup().getMajorCode().getGlcode();
				glType = detail.getBudgetGroup().getMajorCode().getType().toString();
				glName = detail.getBudgetGroup().getMajorCode().getName();
				majorcodewise=true;
			}
			tempMajorCode = glcode.substring(0,majorCodeLength);
			
			if(!detail.getExecutingDepartment().getId().equals(deptId)) // for dept heading  
			{
				if(totalAmt.compareTo(BigDecimal.ZERO)!=0 && !isFirst)
				{
					budgetReportList.add(new BudgetReportView(EMPTYSTRING,EMPTYSTRING,EMPTYSTRING,TOTALSTRING,EMPTYSTRING,totalAmt,
							totalAppropriationAmt,totalAmt.add(totalAppropriationAmt),TOTALROW));
					totalAmt=BigDecimal.ZERO;
					totalAppropriationAmt=BigDecimal.ZERO;
				}
				budgetReportList.add(new BudgetReportView(EMPTYSTRING,EMPTYSTRING,EMPTYSTRING,detail.getExecutingDepartment().getDeptName(),EMPTYSTRING,null,null,null,"deptrow"));
				type="";
				functionId= null;
				majorCode="";
			}
			if(!glType.equals(type))// for type heading 
			{
				if(totalAmt.compareTo(BigDecimal.ZERO)!=0 && !isFirst)
				{
					budgetReportList.add(new BudgetReportView(EMPTYSTRING,EMPTYSTRING,EMPTYSTRING,TOTALSTRING,EMPTYSTRING,totalAmt,
							totalAppropriationAmt,totalAmt.add(totalAppropriationAmt),TOTALROW));
					totalAmt=BigDecimal.ZERO;
					totalAppropriationAmt=BigDecimal.ZERO;
				}
				budgetReportList.add(new BudgetReportView(EMPTYSTRING,EMPTYSTRING,EMPTYSTRING,"FUNCTIONWISE "+BudgetReport.getValueFor(glType).toUpperCase()+" BUDGET SUMMARY",refNoMap.get(BudgetReport.getValueFor(glType)),null,null,null,"typerow"));
				
				functionId= null;
				majorCode="";
			}	
			if(!detail.getFunction().getId().equals(functionId))	// for function heading
			{
				if(totalAmt.compareTo(BigDecimal.ZERO)!=0 && !isFirst)
				{
					budgetReportList.add(new BudgetReportView(EMPTYSTRING,EMPTYSTRING,EMPTYSTRING,TOTALSTRING,EMPTYSTRING,totalAmt,
							totalAppropriationAmt,totalAmt.add(totalAppropriationAmt),TOTALROW));
					totalAmt=BigDecimal.ZERO;
					totalAppropriationAmt=BigDecimal.ZERO;
				}
				budgetReportList.add(new BudgetReportView(EMPTYSTRING,EMPTYSTRING,EMPTYSTRING,"FUNCTION CENTRE-"+detail.getFunction().getName(),EMPTYSTRING,null,null,null,"functionrow"));
				final List<Object> majorCodeList = getAmountForMajorcodewise(detail.getExecutingDepartment().getId(),detail.getFunction().getId(),glType);  // majorcodewise total
				budgetReportList.addAll(majorCodeList);
				printed = false;
				majorCode="";
			}	
			if(!tempMajorCode.equals(majorCode)  && detail.getBudgetGroup().getMajorCode()==null)// majorcodewise - heading 
			{
				if(printed)
				{
					budgetReportList.add(new BudgetReportView(EMPTYSTRING,EMPTYSTRING,EMPTYSTRING,TOTALSTRING,EMPTYSTRING,totalAmt,
							totalAppropriationAmt,totalAmt.add(totalAppropriationAmt),TOTALROW));
					totalAmt=BigDecimal.ZERO;
					totalAppropriationAmt=BigDecimal.ZERO;
				}
				budgetReportList.add(new BudgetReportView(EMPTYSTRING,EMPTYSTRING,refNoMap.get(tempMajorCode),tempMajorCode+"-"+coaMap.get(tempMajorCode),"",null,null,null,"majorcodeheadingrow"));
			}
			
			// detail
			if(detail.getExecutingDepartment()!=null && detail.getFunction()!=null && detail.getBudgetGroup().getMajorCode()==null)
				budgetReportList.add(new BudgetReportView(detail.getExecutingDepartment().getDeptCode(),detail.getFunction().getCode(),glcode,glName,"",detail.getApprovedAmount(),
						reAppropriationAmt,detail.getApprovedAmount().add(reAppropriationAmt),"detailrow"));
			if(detail.getExecutingDepartment()!=null)
				deptId = detail.getExecutingDepartment().getId();
			if(detail.getFunction()!=null)
				functionId = detail.getFunction().getId();
			type = glType;
			majorCode = tempMajorCode;
			totalAmt = totalAmt.add(detail.getApprovedAmount());
			totalAppropriationAmt=totalAppropriationAmt.add(reAppropriationAmt);
			printed=true;
			isFirst=false;
		}
		if(!totalAmt.equals(BigDecimal.ZERO)  && !majorcodewise)
			budgetReportList.add(new BudgetReportView(EMPTYSTRING,EMPTYSTRING,EMPTYSTRING,TOTALSTRING,EMPTYSTRING,totalAmt,
					totalAppropriationAmt,totalAmt.add(totalAppropriationAmt),TOTALROW));
		return budgetReportList;
	}

	public void loadAmountForMajorcodewise(final CFinancialYear finyear, DepartmentImpl dept, CFunction function){
		final String finalStatus = getFinalStatus();
		String miscQuery = "";
		if(dept!=null && dept.getId()!=null)
			miscQuery = miscQuery+" and bd.executingDepartment.id="+dept.getId();
		if(function!=null && function.getId()!=null)
			miscQuery = miscQuery+" and bd.function.id="+function.getId();
		List<Object[]> amountList = getPersistenceService().findAllBy("select substr(bd.budgetGroup.minCode.glcode,0,"+majorCodeLength+") ," +
				"sum(bd.approvedAmount),bd.executingDepartment.id,bd.function.id,bd.budgetGroup.minCode.type,bd.id from BudgetDetail bd where " +
				"bd.budget.financialYear=? and bd.budget.state in (from org.egov.infstr.models.State where type='Budget' and value='"+
				finalStatus+"' ) "+miscQuery+" group by substr(bd.budgetGroup.minCode.glcode,0,"+majorCodeLength+"),bd.executingDepartment.id," +
				"bd.function.id,bd.budgetGroup.minCode.type,bd.id order by  substr(bd.budgetGroup.minCode.glcode,0,"+majorCodeLength+"),bd.executingDepartment.id,bd.function.id", 
				finyear);
		BigDecimal reAppropriationAmt = BigDecimal.ZERO;
		for(Object[] obj :amountList){
			if(obj[0]!=null && obj[1]!=null && obj[2]!=null && obj[3]!=null && obj[4]!=null && !BigDecimal.ZERO.equals(BigDecimal.valueOf(Double.valueOf(obj[1].toString())))){
				reAppropriationAmt = reAppropriationMap.get(obj[5])==null?BigDecimal.ZERO:reAppropriationMap.get(obj[5]);
				reportStoreList.add(new BudgetReportView(Integer.valueOf(obj[2]+EMPTYSTRING),Long.valueOf(obj[3]+EMPTYSTRING),obj[4]+EMPTYSTRING,obj[0]+EMPTYSTRING,
						((BigDecimal)obj[1]),reAppropriationAmt,((BigDecimal)obj[1]).add(reAppropriationAmt)));
			}
		}
		
		amountList = getPersistenceService().findAllBy("select substr(bd.budgetGroup.majorCode.glcode,0,"+majorCodeLength+") ,sum(bd.approvedAmount),bd.executingDepartment.id,bd.function.id,bd.budgetGroup.majorCode.type,bd.id from BudgetDetail bd where bd.budget.financialYear=? and bd.budget.state in (from org.egov.infstr.models.State where type='Budget' and value='"+finalStatus+"' ) group by substr(bd.budgetGroup.majorCode.glcode,0,"+majorCodeLength+"),bd.executingDepartment.id,bd.function.id,bd.budgetGroup.majorCode.type,bd.id order by  substr(bd.budgetGroup.majorCode.glcode,0,"+majorCodeLength+")", finyear);
		for(Object[] obj :amountList){
			if(obj[0]!=null && obj[1]!=null && obj[2]!=null && obj[3]!=null && obj[4]!=null && !BigDecimal.ZERO.equals(BigDecimal.valueOf(Double.valueOf(obj[1].toString())))){
				reAppropriationAmt = reAppropriationMap.get(obj[5])==null?BigDecimal.ZERO:reAppropriationMap.get(obj[5]);
				reportStoreList.add(new BudgetReportView(Integer.valueOf(obj[2]+EMPTYSTRING),Long.valueOf(obj[3]+EMPTYSTRING),obj[4]+EMPTYSTRING,
						obj[0]+EMPTYSTRING,((BigDecimal)obj[1]),reAppropriationAmt,((BigDecimal)obj[1]).add(reAppropriationAmt)));
			}
		}
	}
	
	public List<Object> getAmountForMajorcodewise(final Integer deptId,final Long functionId,final String type){
		BigDecimal grandAmt = BigDecimal.ZERO;
		BigDecimal totalAmt = BigDecimal.ZERO;
		BigDecimal appropriationGrandAmt = BigDecimal.ZERO;
		BigDecimal appropriationTotalAmt = BigDecimal.ZERO;
		List<Object> majorCodeList = new ArrayList<Object>();
		Map<String, BudgetReportView> entries = new TreeMap<String, BudgetReportView>();
		for(BudgetReportView reportStore:reportStoreList){
			if(deptId.equals(reportStore.getDeptId()) && functionId.equals(reportStore.getFunctionId()) && type.equals(reportStore.getType())){
				if(entries.get(reportStore.getMajorCode()) == null){
					totalAmt=BigDecimal.ZERO;
					appropriationTotalAmt=BigDecimal.ZERO;
					entries.put(reportStore.getMajorCode(), new BudgetReportView(EMPTYSTRING,EMPTYSTRING,EMPTYSTRING,reportStore.getMajorCode()+"-"+
							coaMap.get(reportStore.getMajorCode()),refNoMap.get(reportStore.getMajorCode())==null?EMPTYSTRING:refNoMap.get(reportStore.getMajorCode()).toString(),
									reportStore.getTempamount(),reportStore.getAppropriationAmount(),reportStore.getTempamount().add(reportStore.getAppropriationAmount()),"majorcoderow"));
					totalAmt = totalAmt.add(reportStore.getTempamount());
					appropriationTotalAmt = appropriationTotalAmt.add(reportStore.getAppropriationAmount());
				}
				else{
					totalAmt = totalAmt.add(reportStore.getTempamount());
					appropriationTotalAmt = appropriationTotalAmt.add(reportStore.getAppropriationAmount());
					entries.get(reportStore.getMajorCode()).setAmount(totalAmt);
					entries.get(reportStore.getMajorCode()).setAppropriationAmount(appropriationTotalAmt);
					entries.get(reportStore.getMajorCode()).setTotalAmount(totalAmt.add(appropriationTotalAmt));
				}
				grandAmt = grandAmt.add(reportStore.getTempamount());
				appropriationGrandAmt = appropriationGrandAmt.add(reportStore.getAppropriationAmount());
			}
		}
		for (Entry<String, BudgetReportView> row : entries.entrySet()) {
			majorCodeList.add(row.getValue());
		}
		if(!totalAmt.equals(BigDecimal.ZERO))
			majorCodeList.add(new BudgetReportView(EMPTYSTRING,EMPTYSTRING,EMPTYSTRING,TOTALSTRING,EMPTYSTRING,grandAmt,appropriationGrandAmt,grandAmt.add(appropriationGrandAmt),TOTALROW));
		return majorCodeList;
	}
	
	public void getCOA(){
		List<CChartOfAccounts> coaList = getPersistenceService().findAllBy("from CChartOfAccounts where length(glcode)="+majorCodeLength);
		for(CChartOfAccounts coa :coaList){
			coaMap.put(coa.getGlcode(), coa.getName());
		}
	}
	
	public String departmentWiseReport(){
		return "dept";
	}
	
	public String printDepartmentWiseReport(){
		validateFinancialYear();
		return "print";
	}

	public String generateDepartmentWiseXls() throws JRException, IOException{
		validateFinancialYear();
		populateData();
		inputStream = reportHelper.exportXls(inputStream, DEPTWISEPATH, getParamMap(),budgetReportList);
		return "department-XLS";
	}
	
	public String generateDepartmentWisePdf() throws Exception{
		validateFinancialYear();
		populateData();
		inputStream = reportHelper.exportPdf(inputStream, DEPTWISEPATH, getParamMap(),budgetReportList);
		return "department-PDF";
	}

	public String ajaxGenerateDepartmentWiseHtml() throws Exception{
		populateData();
		inputStream = reportHelper.exportHtml(inputStream, DEPTWISEPATH, getParamMap(),budgetReportList,"px");
		return "department-HTML";
	}

	public BudgetReport getBudgetReport() {
		return budgetReport;
	}

	public void setBudgetReport(BudgetReport budgetReport) {
		this.budgetReport = budgetReport;
	}
	
	protected void setRelatedEntitesOn() {
		if(budgetReport.getDepartment() == null || budgetReport.getDepartment().getId()==null)
			budgetReport.setDepartment(null);	
		else
			budgetReport.setDepartment((DepartmentImpl)getPersistenceService().find("from DepartmentImpl where id=?", budgetReport.getDepartment().getId()));
			
		if(budgetReport.getFinancialYear() != null)
			budgetReport.setFinancialYear((CFinancialYear)getPersistenceService().find("from CFinancialYear where id=?", budgetReport.getFinancialYear().getId()));
	}
	
	protected void validateFinancialYear() {
		if(budgetReport.getFinancialYear() == null || budgetReport.getFinancialYear().getId() == null)
			throw new ValidationException(Arrays.asList(new ValidationError("report.financialyear.not.selected","report.financialyear.not.selected")));
	}

	protected String getBudgetType(String finalStatus) {
		String isBeRe = "BE";
		Budget budget = (Budget) persistenceService.find("from Budget where financialYear.id=? and parent is null and isPrimaryBudget=1 and isActiveBudget=1 and isBeRe='RE' and state in (from org.egov.infstr.models.State where type='Budget' and value='"+finalStatus+"')",budgetReport.getFinancialYear().getId());
		if(budget!=null)
			isBeRe = "RE";
		return isBeRe;
	}

	protected void addEmptyRow() {
		budgetReportList.add(new BudgetReportView("","","",null,null,null));
	}

	protected String getQueryForSelectedType(String code) {
		if(budgetReport.getType() == null)
			return "";
		if(!"ALL".equalsIgnoreCase(budgetReport.getType())){
			if("IE".equalsIgnoreCase(budgetReport.getType()))
				return "and (bd.budgetGroup."+code+".type='I' or bd.budgetGroup."+code+".type='E')";
			else
				return "and bd.budgetGroup."+code+".type='"+budgetReport.getType()+"'";
		}
		return "";
	}

	protected Map<String, Object> getParamMap() {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("finYear", budgetReport.getFinancialYear().getFinYearRange());
		if(budgetReport.getType()!=null)
			paramMap.put("type", BudgetReport.getValueFor(budgetReport.getType()));
		return paramMap;
	}
	
	protected Map<String,String> getReferenceNumber(String appConfigKey){
		Map<String,String> referenceNo = new HashMap<String, String>();
		List<AppConfigValues> appConfigList = (List<AppConfigValues>) persistenceService.findAllBy("from AppConfigValues where key.keyName like '"+appConfigKey+"-%'");
		for (AppConfigValues appConfigVal : appConfigList) {
			referenceNo.put(appConfigVal.getKey().getKeyName().split("-")[1],appConfigVal.getValue());
		}
		return referenceNo;
	}
	
	protected String getFinalStatus() {
		return getAppConfigValueFor(Constants.EGF,"budget_final_approval_status");
	}

	protected String getAppConfigValueFor(String module,String key){
		return genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(module,key).get(0).getValue();
	}
	
	protected void populateData() {
		String finalStatus = getFinalStatus();
		String isBeRe = getBudgetType(finalStatus);
		String deptQuery = "";
		if(budgetReport.getDepartment() != null && budgetReport.getDepartment().getId() != null){
			deptQuery = " and bd.executingDepartment.id="+(budgetReport.getDepartment().getId().toString());
		}
		getBudgetReappropriationAmt();
		String budgetType = BudgetReport.getValueFor(budgetReport.getType());
		if(budgetType!=null && !"ALL".equals(budgetReport.getType()))
			budgetReportList.add(new BudgetReportView("",budgetType.toUpperCase()+" BUDGET SUMMARY","",null,null,null));
		//budgetdetails for all mincode 
		LinkedList<BudgetDetail> budgetDetails = new LinkedList<BudgetDetail>();
		fetchBudgetDetails(budgetDetails, deptQuery, finalStatus, isBeRe,"minCode");
		//budgetdetails for all majorcode
		fetchBudgetDetails(budgetDetails, deptQuery, finalStatus, isBeRe,"majorCode");
		populateSummarySection(budgetDetails);
		addRowsToReport(budgetDetails);
	}
	
	public Map<String,BigDecimal> getMajorCodeToAmountMap(List<BudgetDetail> budgetDetails) {
		Map<String,BigDecimal> map = new HashMap<String, BigDecimal>();
		for (BudgetDetail entry : budgetDetails) {
			String glCode = "";
			if(entry.getBudgetGroup().getMajorCode() == null)
				glCode = entry.getBudgetGroup().getMinCode().getMajorCode();
			else
				glCode = entry.getBudgetGroup().getMajorCode().getMajorCode();
			BigDecimal approvedAmount = entry.getApprovedAmount()==null?BigDecimal.ZERO:entry.getApprovedAmount();
			BigDecimal totalAmount = approvedAmount;
			if(map.get(glCode ) != null)
				map.put(glCode, map.get(glCode).add(totalAmount));
			else
				map.put(glCode, totalAmount);
		}
		return map;
	}
	public String getUniqueMajorCodesAsString(Map<String, BigDecimal> majorCodeToAmountMap) {
		String result = "";
		Set<String> uniqueMajorCodes = majorCodeToAmountMap.keySet();
		for (String row : uniqueMajorCodes) {
			if(row!=null)
				result = result.concat("'").concat(row).concat("',");
		}
		if(result.length()>0)
			result = result.substring(0,result.length()-1);
		return result;
	}

	public BigDecimal getMajorCodeTotals(Map<String, BigDecimal> majorCodeToAmountMap) {
		BigDecimal sum = BigDecimal.ZERO;
		for (Entry<String, BigDecimal> entry : majorCodeToAmountMap.entrySet()) {
			if(entry.getValue() != null)
				sum = sum.add(entry.getValue());
		}
		return sum;
	}
	public BigDecimal getMajorCodeApproriationTotals(Map<String, BigDecimal> majorCodeToApproriationAmountMap) {
		BigDecimal sum = BigDecimal.ZERO;
		for (Entry<String, BigDecimal> entry : majorCodeToApproriationAmountMap.entrySet()) {
			if(entry.getValue() != null)
				sum = sum.add(entry.getValue());
		}
		return sum;
	}

	void populateSummarySection(List<BudgetDetail> budgetDetails) {
		Map<String, BigDecimal> majorCodeToAmountMap = getMajorCodeToAmountMap(budgetDetails);
		Map<String, BigDecimal> majorCodeToAppropriationAmountMap = getMajorCodeToAppropriationAmountMap(budgetDetails);
		Map<String, String> referenceNo = getReferenceNumber("departmentWiseBudgetReport");
		String uniqueMajorCodesAsString = getUniqueMajorCodesAsString(majorCodeToAmountMap);
		if("".equals(uniqueMajorCodesAsString)){
			budgetReportList.add(new BudgetReportView("","No records found","",null,null,null));
			return;
		}
		List<CChartOfAccounts> chartOfAccounts = getPersistenceService().findAllBy("from CChartOfAccounts where glCode in ("+uniqueMajorCodesAsString+")");
		for (CChartOfAccounts account : chartOfAccounts) {
			BigDecimal approved = majorCodeToAmountMap.get(account.getMajorCode());
			BigDecimal reApp = majorCodeToAppropriationAmountMap.get(account.getMajorCode());
			budgetReportList.add(new BudgetReportView("",account.getMajorCode()+"-"+account.getName(),referenceNo.get(account.getMajorCode()),
					approved,reApp,approved.add(reApp)));
		}
		if(!chartOfAccounts.isEmpty()) {
			BigDecimal majorCodeApprovedTotals = getMajorCodeTotals(majorCodeToAmountMap);
			BigDecimal majorCodeApproriationTotals = getMajorCodeApproriationTotals(majorCodeToAppropriationAmountMap);
			budgetReportList.add(new BudgetReportView("","Total","",majorCodeApprovedTotals,majorCodeApproriationTotals,
					majorCodeApprovedTotals.add(majorCodeApproriationTotals)));
		}
		addEmptyRow();
	}

	private Map<String, BigDecimal> getMajorCodeToAppropriationAmountMap(List<BudgetDetail> budgetDetails) {
		Map<String,BigDecimal> map = new HashMap<String, BigDecimal>();
		for (BudgetDetail entry : budgetDetails) {
			String glCode = "";
			if(entry.getBudgetGroup().getMajorCode() == null)
				glCode = entry.getBudgetGroup().getMinCode().getMajorCode();
			else
				glCode = entry.getBudgetGroup().getMajorCode().getMajorCode();
			BigDecimal reAppAmount = reAppropriationMap.get(entry.getId());
			BigDecimal totalAmount = reAppAmount==null?BigDecimal.ZERO:reAppAmount;
			if(map.get(glCode ) != null)
				map.put(glCode, map.get(glCode).add(totalAmount));
			else
				map.put(glCode, totalAmount);
		}
		return map;
	}

	/*
	 * Assumes budgetDetails are sorted by deptId,glCode
	 */
	void addRowsToReport(List<BudgetDetail> budgetDetails){
		Integer deptId=0;
		BudgetReportView row=new BudgetReportView(null,null,null,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO);
		String glcode=null;
		String glName;
		BigDecimal sum = BigDecimal.ZERO;
		BigDecimal appropriationSum = BigDecimal.ZERO;
		sortByDepartmentName(budgetDetails);
		//not interested in major code details
		for (BudgetDetail budgetDetail : budgetDetails) {
//			if(budgetDetail.getBudgetGroup().getMajorCode()!=null){
//				continue;
//			}
			//details for next department have started
			if(budgetDetail.getExecutingDepartment() != null && !budgetDetail.getExecutingDepartment().getId().equals(deptId)){
				if(!deptId.equals(0)){
					budgetReportList.add(new BudgetReportView("","Total","",sum,appropriationSum,sum.add(appropriationSum)));
				}
				sum = BigDecimal.ZERO;
				addEmptyRow();
				budgetReportList.add(new BudgetReportView("",budgetDetail.getExecutingDepartment().getDeptName().toUpperCase(),"",null,null,null));
				deptId = budgetDetail.getExecutingDepartment().getId();
				glcode=null;
			}
			//next glcode within same department
			if(!getGlCode(budgetDetail).equals(glcode)){
				glcode = getGlCode(budgetDetail);
				glName = getGlName(budgetDetail);
				row = new BudgetReportView(glcode,glName,"",BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO);
				budgetReportList.add(row);
			}
			BigDecimal approvedAmount = budgetDetail.getApprovedAmount()==null?BigDecimal.ZERO:budgetDetail.getApprovedAmount();
			BigDecimal reAppAmount = reAppropriationMap.get(budgetDetail.getId());
			row.setAmount(approvedAmount);
			row.setAppropriationAmount(reAppAmount==null?BigDecimal.ZERO:reAppAmount);
			row.setTotalAmount(approvedAmount.add(reAppAmount==null?BigDecimal.ZERO:reAppAmount));
			sum=sum.add(approvedAmount);
		}
		if(!budgetDetails.isEmpty())
			budgetReportList.add(new BudgetReportView("","Total","",sum,appropriationSum,sum.add(appropriationSum)));
	}

	private void sortByDepartmentName(List<BudgetDetail> budgetDetails) {
		Collections.sort(budgetDetails, new Comparator<BudgetDetail>(){
			public int compare(BudgetDetail o1, BudgetDetail o2) {
				return o1.getExecutingDepartment().getDeptName().toUpperCase().
									compareTo(o2.getExecutingDepartment().getDeptName().toUpperCase());
			}});
	}

	private String getGlName(BudgetDetail budgetDetail) {
		BudgetGroup budgetGroup = budgetDetail.getBudgetGroup();
		return budgetGroup.getMinCode()==null?budgetGroup.getMajorCode().getName():budgetGroup.getMinCode().getName();
	}

	private String getGlCode(BudgetDetail budgetDetail) {
		BudgetGroup budgetGroup = budgetDetail.getBudgetGroup();
		return budgetGroup.getMinCode()==null?budgetGroup.getMajorCode().getGlcode():budgetGroup.getMinCode().getGlcode();
	}
	
	void fetchBudgetDetails(List<BudgetDetail> budgetDetails,String deptQuery,String finalStatus, String budgetType,String code) {
		List<BudgetDetail> results = getPersistenceService().getSession().createQuery(
				" from BudgetDetail bd where bd.budget.financialYear.id="+budgetReport.getFinancialYear().getId()+deptQuery+
				" and bd.budget.isbere='"+budgetType+"' and bd.budget.state in (from org.egov.infstr.models.State where type='Budget'" +
				" and value='"+finalStatus+"') "+getQueryForSelectedType(code)+"  order by bd.executingDepartment.deptName,bd.budgetGroup."+code+".glcode")
				.list();
		budgetDetails.addAll(results);
	}
	
	private void getBudgetReappropriationAmt(){
		final String status = getFinalStatus();
		List<Object[]> list =  getPersistenceService().findAllBy("select sum(br.additionAmount)-sum(br.deductionAmount),br.budgetDetail.id from BudgetReAppropriation br where br.state in (from org.egov.infstr.models.State where type='BudgetReAppropriation' and value='"+status+"' ) group by br.budgetDetail.id");
		if(!list.isEmpty() && list.size()!=0){
			for(Object[] obj :list)
				reAppropriationMap.put(obj[1], (BigDecimal)obj[0]);
		}
	}
}
