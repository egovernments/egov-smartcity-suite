package org.egov.web.actions.report;

import org.apache.struts2.convention.annotation.Action;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.log4j.Logger;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.StreamResult;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.reporting.viewer.ReportViewerUtil;
import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.budget.BudgetProposalBean;
import org.egov.pims.commons.Position;
import org.egov.services.budget.BudgetDetailService;
import org.egov.utils.Constants;
import org.egov.utils.ReportHelper;
import org.egov.web.actions.BaseFormAction;

/*@Results(value={
		@Result(name="PDF",type=StreamResult.class,value=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"application/pdf",Constants.CONTENT_DISPOSITION,"no-cache;filename=ConsolidatedBudgetReport.pdf"}),
		@Result(name="XLS",type=StreamResult.class,value=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"application/xls",Constants.CONTENT_DISPOSITION,"no-cache;filename=ConsolidatedBudgetReport.xls"})
	})*/
@Results({ 
	@Result(name = "reportview", type = StreamResult.class, value = "inputStream", params = { "contentType", "${contentType}", "contentDisposition", "attachment; filename=${fileName}" })
	})
@ParentPackage("egov") 
public class ConsolidatedBudgetReportAction extends BaseFormAction { 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private CFinancialYear financialYear;
	private String fundType;
	private String budgetType;
	private CFinancialYear prevFinYear;
	private String prevFinYearRange;
	private String currentFinYearRange;
	private String nextFinYearRange;
	private static final String DETAIL = "detail";
	private static final String HEADING = "heading";
	private static final String TOTAL = "total";
	
	private static final Logger    LOGGER    = Logger.getLogger(ConsolidatedBudgetReportAction.class);
	
	protected BudgetDetailService budgetDetailService;
	protected FinancialYearHibernateDAO financialYearDAO;
	private String contentType;
	private String fileName;
	private ReportService reportService;

	private List<Budget> topBudgetList;
	private Map<String, String> majorCodeAndNameMap = new TreeMap<String, String>();
	private Map<String, String> glCodeAndNameMap = new TreeMap<String, String>();
	private List<BudgetProposalBean> bpBeanMajList;
	private List<BudgetProposalBean> bpBeanDetList;
	private List<BudgetProposalBean> bpBeanList;
	private Map<String,BigDecimal> previousYearBudgetDetailIdsAndAmount = new HashMap<String, BigDecimal>();

	private ReportHelper reportHelper;
	private InputStream inputStream;

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
@Action(value="/report/consolidatedBudgetReport-consolidatedReport")
    public String consolidatedReport(){
		addDropdownData("financialYearList", getPersistenceService().findAllBy("from CFinancialYear where isActive=1 order by finYearRange desc "));
    	return "reportSearch";
    }
    
    public String search(){
    	addDropdownData("financialYearList", getPersistenceService().findAllBy("from CFinancialYear where isActive=1 order by finYearRange desc "));
    	
    	populateData();
    	
    	return "reportSearch";
    }

	public void populateData() {
		financialYear = (CFinancialYear) persistenceService.find("from CFinancialYear where id=?", financialYear.getId());
    	
    	prevFinYear = getFinancialYearDAO().getPreviousFinancialYearByDate(financialYear.getStartingDate());
    	prevFinYearRange = prevFinYear.getFinYearRange();
    	currentFinYearRange = financialYear.getFinYearRange();
    	nextFinYearRange=getFinancialYearDAO().getNextFinancialYearByDate(financialYear.getStartingDate()).getFinYearRange();
    	
    	bpBeanMajList = new ArrayList<BudgetProposalBean>();
    	bpBeanDetList = new ArrayList<BudgetProposalBean>();
    	
    	getMajorCodeData();
    	
    	populateMajorCodewiseDetailDataForReport();
	}
   
    
    void getMajorCodeData(){
		Map<String, BigDecimal> majorCodePreviousYearActuals = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> majorCodeBEMap = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> majorCodeREMap = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> majorCodeNextYrBEMap = new HashMap<String, BigDecimal>();
		
		
		
		List<Object[]> resultMajorCode = budgetDetailService.fetchMajorCodeAndNameForReport(financialYear, fundType, budgetType);
		addToMap(resultMajorCode,majorCodeAndNameMap);	
		 
		List<Object[]> resultCurrentActuals = budgetDetailService.fetchMajorCodeAndActualsForReport(financialYear, prevFinYear, fundType, budgetType);
		addToMapStringBigDecimal(resultCurrentActuals,majorCodePreviousYearActuals);
		
		List<Object[]> resultMajorCodeBE = budgetDetailService.fetchMajorCodeAndBEAmountForReport(financialYear, fundType, budgetType);
		addToMapStringBigDecimal(resultMajorCodeBE,majorCodeBEMap);
		
		List<Object[]> resultMajorCodeApproved = budgetDetailService.fetchMajorCodeAndApprovedAmountForReport(financialYear, fundType, budgetType);
		addToMapStringBigDecimal(resultMajorCodeApproved,majorCodeREMap);
		
		List<Object[]> resultMajorCodeBENextYrApproved = budgetDetailService.fetchMajorCodeAndBENextYrApprovedForReport(financialYear, fundType, budgetType);
		addToMapStringBigDecimal(resultMajorCodeBENextYrApproved,majorCodeNextYrBEMap);
        
        BudgetProposalBean bpbeanTotal = new BudgetProposalBean();
        BigDecimal bigThousand = new BigDecimal(1000);
        Integer i = 1;
        for(Map.Entry<String, String> entry : majorCodeAndNameMap.entrySet()){
        	BudgetProposalBean bpbean = new BudgetProposalBean();
        	bpbean.setAccountCode(i.toString());
        	bpbean.setBudgetGroup(entry.getValue());
        	bpbean.setPreviousYearActuals(majorCodePreviousYearActuals.get(entry.getKey())==null?BigDecimal.ZERO.toString():majorCodePreviousYearActuals.get(entry.getKey()).divide(bigThousand).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
        	bpbean.setCurrentYearBE(majorCodeBEMap.get(entry.getKey())==null?BigDecimal.ZERO.toString():majorCodeBEMap.get(entry.getKey()).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
        	bpbean.setApprovedRE(majorCodeREMap.get(entry.getKey())==null?BigDecimal.ZERO:majorCodeREMap.get(entry.getKey()).setScale(0, BigDecimal.ROUND_HALF_UP));
        	bpbean.setApprovedBE(majorCodeNextYrBEMap.get(entry.getKey())==null?BigDecimal.ZERO:majorCodeNextYrBEMap.get(entry.getKey()).setScale(0, BigDecimal.ROUND_HALF_UP));
        	bpbean.setRowType(this.DETAIL);
        	bpBeanMajList.add(bpbean);
        	i++;
        	
        	computeTotal(bpbeanTotal, bpbean);
        }
        bpbeanTotal.setRowType(this.HEADING);
        bpbeanTotal.setAccountCode(null);
        bpBeanMajList.add(bpbeanTotal);
    }
    
	void populateMajorCodewiseDetailDataForReport(){
		if(LOGGER.isInfoEnabled())     LOGGER.info("Starting populateMajorCodewiseDetailData()................");
		
		Map<String, BigDecimal> glCodeBEMap = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> glCodeREMap = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> glCodeNextYrBEMap = new HashMap<String, BigDecimal>();
		
		
		List<Object[]> resultMajorCode = budgetDetailService.fetchGlCodeAndNameForReport(financialYear, fundType, budgetType);
		addToMap(resultMajorCode,glCodeAndNameMap);	
		
		List<Object[]> resultPreviousActuals = budgetDetailService.fetchActualsForReport(financialYear, prevFinYear, fundType, budgetType);
		addToMapStringBigDecimal(resultPreviousActuals,previousYearBudgetDetailIdsAndAmount);
		
		List<Object[]> resultMajorCodeBE = budgetDetailService.fetchGlCodeAndBEAmountForReport(financialYear, fundType, budgetType);
		addToMapStringBigDecimal(resultMajorCodeBE,glCodeBEMap);
		
		List<Object[]> resultMajorCodeApproved = budgetDetailService.fetchGlCodeAndApprovedAmountForReport(financialYear, fundType, budgetType);
		addToMapStringBigDecimal(resultMajorCodeApproved,glCodeREMap);
		
		List<Object[]> resultMajorCodeBENextYrApproved = budgetDetailService.fetchGlCodeAndBENextYrApprovedForReport(financialYear, fundType, budgetType);
		addToMapStringBigDecimal(resultMajorCodeBENextYrApproved,glCodeNextYrBEMap);
		
		BigDecimal bigThousand = new BigDecimal(1000);
		for(Map.Entry<String, String> entry : majorCodeAndNameMap.entrySet()){
			bpBeanDetList.add(new BudgetProposalBean(entry.getValue(), this.HEADING));
			
			BudgetProposalBean bpbeanTotal = new BudgetProposalBean();
			for(Map.Entry<String, String> glEntry : glCodeAndNameMap.entrySet()){
				if(entry.getKey().equals(glEntry.getKey().substring(0,3))){
					BudgetProposalBean bpbean = new BudgetProposalBean();
					
					bpbean.setAccountCode(glEntry.getKey());
		        	bpbean.setBudgetGroup(glEntry.getValue());
		        	bpbean.setPreviousYearActuals(previousYearBudgetDetailIdsAndAmount.get(glEntry.getKey())==null?BigDecimal.ZERO.toString():previousYearBudgetDetailIdsAndAmount.get(glEntry.getKey()).divide(bigThousand).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
		        	bpbean.setCurrentYearBE(glCodeBEMap.get(glEntry.getKey())==null?BigDecimal.ZERO.toString():glCodeBEMap.get(glEntry.getKey()).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
		        	bpbean.setApprovedRE(glCodeREMap.get(glEntry.getKey())==null?BigDecimal.ZERO:glCodeREMap.get(glEntry.getKey()).setScale(0, BigDecimal.ROUND_HALF_UP));
		        	bpbean.setApprovedBE(glCodeNextYrBEMap.get(glEntry.getKey())==null?BigDecimal.ZERO:glCodeNextYrBEMap.get(glEntry.getKey()).setScale(0, BigDecimal.ROUND_HALF_UP));
		        	bpbean.setRowType(this.DETAIL);
					
		        	bpBeanDetList.add(bpbean);
					
					computeTotal(bpbeanTotal, bpbean);
				}
			}
			bpbeanTotal.setRowType(this.HEADING);
			bpBeanDetList.add(bpbeanTotal);
			bpBeanDetList.add(new BudgetProposalBean("",this.DETAIL));
		}
		if(LOGGER.isInfoEnabled())     LOGGER.info("Finished populateMajorCodewiseDetailData()");
	}
    
	private Map<String,String> addToMap(List<Object[]>  tempList,Map<String,String> resultMap) {
		for (Object[] row : tempList) {
			resultMap.put(row[0].toString(), row[1].toString());
        }
		return resultMap;
	}
	
	private Map<String,BigDecimal> addToMapStringBigDecimal(List<Object[]>  tempList,Map<String,BigDecimal> resultMap) {
		for (Object[] row : tempList) {
			resultMap.put(row[0].toString(), ((BigDecimal)row[1]).setScale(2));
        }
		return resultMap;
	}
	
	void computeTotal(BudgetProposalBean bpbeanTotal, BudgetProposalBean bpbean){
		if(LOGGER.isInfoEnabled())     LOGGER.info("Starting computeTotal................");
		bpbeanTotal.setPreviousYearActuals(bpbeanTotal.getPreviousYearActuals() == null?bpbean.getPreviousYearActuals():(new BigDecimal(bpbeanTotal.getPreviousYearActuals())).add((new BigDecimal(bpbean.getPreviousYearActuals()))).toString());
		bpbeanTotal.setCurrentYearBE(bpbeanTotal.getCurrentYearBE() == null?bpbean.getCurrentYearBE():(new BigDecimal(bpbeanTotal.getCurrentYearBE())).add((new BigDecimal(bpbean.getCurrentYearBE()))).toString());
		bpbeanTotal.setApprovedRE(bpbeanTotal.getApprovedRE() == null?bpbean.getApprovedRE():bpbeanTotal.getApprovedRE().add(bpbean.getApprovedRE()).setScale(0));
		bpbeanTotal.setApprovedBE(bpbeanTotal.getApprovedBE() == null?bpbean.getApprovedBE():bpbeanTotal.getApprovedBE().add(bpbean.getApprovedBE()).setScale(0));
		bpbeanTotal.setBudgetGroup("TOTAL");
		if(LOGGER.isInfoEnabled())     LOGGER.info("Finished computeTotal");
	}
	
/*	public String generatePdf() throws Exception{
		
		bpBeanList=new ArrayList<BudgetProposalBean>();   
		populateData();
		bpBeanList.addAll(bpBeanMajList);
		bpBeanList.addAll(bpBeanDetList);
		//String title=getUlbName() ;
		String title = this.fundType.toUpperCase()+" "+this.budgetType.toUpperCase();
		//String subtitle=getTopBudget().getName()!=null?"Budget-:"+getTopBudget().getName():"";
		String subtitle = "Amount in Thousands";
		JasperPrint jasper = reportHelper.generateConsolidatedBudgetReport(bpBeanList,title,subtitle,prevFinYearRange,currentFinYearRange,nextFinYearRange);
		inputStream = reportHelper.exportPdf(inputStream, jasper);
		return "PDF";
	}
	
	public String generateXls() throws Exception{
		
		bpBeanList=new ArrayList<BudgetProposalBean>();   
		populateData();
		bpBeanList.addAll(bpBeanMajList);
		bpBeanList.addAll(bpBeanDetList);
		//String title=getUlbName() ;
		String title = "Consolidated Budget Report "+this.currentFinYearRange+"\n"+this.fundType.toUpperCase()+" "+this.budgetType.toUpperCase();
		//String subtitle=getTopBudget().getName()!=null?"Budget-:"+getTopBudget().getName():"";
		String subtitle = "Amount in Thousands";
		JasperPrint jasper = reportHelper.generateConsolidatedBudgetReport(bpBeanList,title,subtitle,prevFinYearRange,currentFinYearRange,nextFinYearRange);
		inputStream = reportHelper.exportXls(inputStream, jasper);
		return "XLS";
	}*/
	
@Action(value="/report/consolidatedBudgetReport-exportPDF")
    public String exportPDF() {
        Map<String, Object> reportParams = new HashMap<String, Object>();
        reportParams.put("title",this.fundType+" "+this.budgetType);
		bpBeanList=new ArrayList<BudgetProposalBean>();   
		populateData();
        reportParams.put("prevFinYearRange", this.prevFinYearRange);
        reportParams.put("currentFinYearRange", this.currentFinYearRange);
        reportParams.put("nextFinYearRange",this.nextFinYearRange);
		bpBeanList.addAll(bpBeanMajList);
		bpBeanList.add(new BudgetProposalBean("",this.DETAIL));
		//this for jasper report
		BudgetProposalBean bpbean = new BudgetProposalBean("Account Head", this.HEADING);
		bpbean.setAccountCode("D.P.Code");
		bpbean.setPreviousYearActuals("Actuals\n"+this.prevFinYearRange);
		bpbean.setCurrentYearBE("Budget Estimate\n"+this.currentFinYearRange);
		bpbean.setFund("Revised Estimate\n"+this.currentFinYearRange);
		bpbean.setFunction("Budget Estimate\n"+this.nextFinYearRange);
		bpbean.setRowType("jrxml");
		bpBeanList.add(bpbean);
		//this for jasper report
		bpBeanList.addAll(bpBeanDetList);
        ReportRequest reportInput = new ReportRequest("consolidatedBudgetReport",bpBeanList, reportParams);
        reportInput.setReportFormat(FileFormat.PDF);
        this.contentType = ReportViewerUtil.getContentType(FileFormat.PDF);
        this.fileName="ConsolidatedBudgetReport." + FileFormat.PDF.toString().toLowerCase();
        ReportOutput reportOutput = reportService.createReport(reportInput);  
        if (reportOutput != null && reportOutput.getReportOutputData() != null)
            inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());

        return "reportview";
    }
    
@Action(value="/report/consolidatedBudgetReport-exportExcel")
    public String exportExcel() {
        Map<String, Object> reportParams = new HashMap<String, Object>();
        reportParams.put("title",this.fundType+" "+this.budgetType);
		bpBeanList=new ArrayList<BudgetProposalBean>();   
		populateData();
        reportParams.put("prevFinYearRange", this.prevFinYearRange);
        reportParams.put("currentFinYearRange", this.currentFinYearRange);
        reportParams.put("nextFinYearRange",this.nextFinYearRange);
		bpBeanList.addAll(bpBeanMajList);
		//this for jasper report
		BudgetProposalBean bpbean = new BudgetProposalBean("Account Head", this.HEADING);
		bpbean.setAccountCode("D.P.Code");
		bpbean.setPreviousYearActuals("Actuals\n"+this.prevFinYearRange);
		bpbean.setCurrentYearBE("Budget Estimate\n"+this.currentFinYearRange);
		bpbean.setFund("Revised Estimate\n"+this.currentFinYearRange);
		bpbean.setFunction("Budget Estimate\n"+this.nextFinYearRange);
		bpbean.setRowType("jrxml");
		bpBeanList.add(bpbean);
		//this for jasper report
		bpBeanList.addAll(bpBeanDetList);
        ReportRequest reportInput = new ReportRequest("consolidatedBudgetReport",bpBeanList, reportParams);
        reportInput.setReportFormat(FileFormat.XLS);
        this.contentType = ReportViewerUtil.getContentType(FileFormat.XLS);
        this.fileName="ConsolidatedBudgetReport." + FileFormat.XLS.toString().toLowerCase();
        ReportOutput reportOutput = reportService.createReport(reportInput);  
        if (reportOutput != null && reportOutput.getReportOutputData() != null)
            inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());

        return "reportview";
    }

	public CFinancialYear getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear(CFinancialYear financialYear) {
		this.financialYear = financialYear;
	}

	public String getFundType() {
		return fundType;
	}

	public void setFundType(String fundType) {
		this.fundType = fundType;
	}

	public String getBudgetType() {
		return budgetType;
	}

	public void setBudgetType(String budgetType) {
		this.budgetType = budgetType;
	}

	public BudgetDetailService getBudgetDetailService() {
		return budgetDetailService;
	}

	public void setBudgetDetailService(BudgetDetailService budgetDetailService) {
		this.budgetDetailService = budgetDetailService;
	}

	public FinancialYearHibernateDAO getFinancialYearDAO() {
		return financialYearDAO;
	}

	public void setFinancialYearDAO(FinancialYearHibernateDAO financialYearDAO) {
		this.financialYearDAO = financialYearDAO;
	}

	public List<BudgetProposalBean> getBpBeanMajList() {
		return bpBeanMajList;
	}

	public void setBpBeanMajList(List<BudgetProposalBean> bpBeanMajList) {
		this.bpBeanMajList = bpBeanMajList;
	}

	public List<BudgetProposalBean> getBpBeanDetList() {
		return bpBeanDetList;
	}

	public void setBpBeanDetList(List<BudgetProposalBean> bpBeanDetList) {
		this.bpBeanDetList = bpBeanDetList;
	}

	public String getPrevFinYearRange() {
		return prevFinYearRange;
	}

	public void setPrevFinYearRange(String prevFinYearRange) {
		this.prevFinYearRange = prevFinYearRange;
	}

	public String getCurrentFinYearRange() {
		return currentFinYearRange;
	}

	public void setCurrentFinYearRange(String currentFinYearRange) {
		this.currentFinYearRange = currentFinYearRange;
	}

	public String getNextFinYearRange() {
		return nextFinYearRange;
	}

	public void setNextFinYearRange(String nextFinYearRange) {
		this.nextFinYearRange = nextFinYearRange;
	}

	public List<BudgetProposalBean> getBpBeanList() {
		return bpBeanList;
	}

	public void setBpBeanList(List<BudgetProposalBean> bpBeanList) {
		this.bpBeanList = bpBeanList;
	}

	public ReportHelper getReportHelper() {
		return reportHelper;
	}

	public void setReportHelper(ReportHelper reportHelper) {
		this.reportHelper = reportHelper;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

}
