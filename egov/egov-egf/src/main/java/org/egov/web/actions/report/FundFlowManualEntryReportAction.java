package org.egov.web.actions.report;

import org.apache.struts2.convention.annotation.Action;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.log4j.Logger;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.StreamResult;
import org.egov.commons.Bank;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.Fund;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.report.FundFlowBean;
import org.egov.utils.Constants;
import org.egov.utils.ReportHelper;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.actions.payment.OutstandingPaymentAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.opensymphony.xwork2.validator.annotations.Validation;

@Results(value={
		@Result(name="PDF",type=StreamResult.class,value=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"application/pdf",Constants.CONTENT_DISPOSITION,"no-cache;filename=ManualEntryReport.pdf"}),
		@Result(name="XLS",type=StreamResult.class,value=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"application/xls",Constants.CONTENT_DISPOSITION,"no-cache;filename=ManualEntryReport.xls"})
	})
@SuppressWarnings("serial")
@ParentPackage("egov")  
@Validation
public class FundFlowManualEntryReportAction extends BaseFormAction{

	protected ReportSearch reportSearch = new ReportSearch();
	private String selectedAccountNumber;
	public List<Object> manualEntryReportList=new ArrayList<Object>() ;
	FundFlowBean manualEntry=new FundFlowBean();
	List<FundFlowBean> entryReportList = new ArrayList<FundFlowBean>(); 
	private InputStream inputStream;
	private ReportHelper reportHelper;
	private StringBuffer heading=new StringBuffer();
	public static final Locale LOCALE = new Locale("en","IN");
	public static final SimpleDateFormat  DDMMYYYYFORMATS= new SimpleDateFormat("dd/MM/yyyy",LOCALE);
	private static final Logger	LOGGER	= Logger.getLogger(FundFlowManualEntryReportAction.class);
	private Map<String,Object> paramMap=new HashMap<String,Object>();
	BigDecimal grandTotal=BigDecimal.ZERO;
	private static final String JASPERPATH = "/reports/templates/manualEntryReport.jasper";
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return reportSearch;
	}
	
	public void prepare (){
	HibernateUtil.getCurrentSession().setDefaultReadOnly(true);
	HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
		super.prepare();
		EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
		addDropdownData("fundList", masterCache.get("egi-fund"));
		addDropdownData("bankList", Collections.EMPTY_LIST);
		addDropdownData("accNumList", Collections.EMPTY_LIST);
	}
	
@Action(value="/report/fundFlowManualEntryReport-newForm")
	public String newForm()
	{
		manualEntryReportList=null;
		return NEW;
	}

	
	
	@ValidationErrorPage(value=NEW)
	public String search(){
		 
		 populateReAppropriationData() ;
		 return NEW;
	}
	
	@SuppressWarnings("unchecked")
	private void populateReAppropriationData() {
		setRelatedEntitesOn();
		getResultList();
		
	}
	
	@SuppressWarnings("unchecked")
	public void getResultList() {
		
		
		//manualEntryReportList = new ArrayList<Map<String,Object>>(); 
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside  getResultList ");
		Date startdt=null;
		Date enddt=null;
		grandTotal=BigDecimal.ZERO;
		try{
			 startdt=Constants.DDMMYYYYFORMAT2.parse( reportSearch.getStartDate());
			 enddt=Constants.DDMMYYYYFORMAT2.parse( reportSearch.getEndDate());
		}catch(ParseException e){
			LOGGER.error("Error in parsing Date ");
		}
		Criteria critQuery =HibernateUtil.getCurrentSession().createCriteria(FundFlowBean.class)
				
				.add(Restrictions.between("reportDate",startdt,enddt))
				.add(Restrictions.ne("currentReceipt",BigDecimal.ZERO))
				.add(Restrictions.eq("bankAccountId",BigDecimal.valueOf( reportSearch.getBankAccount().getId())))
				.addOrder( Order.asc("reportDate") );
		entryReportList.addAll(critQuery.list());
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug("No of Fund flow Manual entry during the period "+reportSearch.getStartDate()
				+ "to "+reportSearch.getEndDate() +"is "+entryReportList.size());
		
		int index=0;
		for(FundFlowBean entry :entryReportList)
		{
			manualEntry = new FundFlowBean(); 
			//manulEntryMap.put("slNo", ++index);
			manualEntry.setReportDate(entry.getReportDate());
			manualEntry.setCurrentReceipt(entry.getCurrentReceipt());
			grandTotal=grandTotal.add(entry.getCurrentReceipt());
			manualEntryReportList.add(manualEntry);
			//}
		}
	HibernateUtil.getCurrentSession().put("entryResultReportList", entryReportList);
	HibernateUtil.getCurrentSession().put("headingStr", heading);
	HibernateUtil.getCurrentSession().put("total", grandTotal);
		/HibernateUtil.getCurrentSession().put("manualEntryReportList", manualEntryReportList);
		if(LOGGER.isInfoEnabled())     LOGGER.info("manualEntryReportList+"+manualEntryReportList.size());
	}
	   
	@SuppressWarnings("unchecked")
	private void populateData() {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Getting the value from session");
		entryReportList=(List<FundFlowBean>)HibernateUtil.getCurrentSession().get("entryResultReportList");
		
		for(FundFlowBean entry:entryReportList)
		{
			manualEntry = new FundFlowBean(); 
			manualEntry.setReportDate(entry.getReportDate());
			manualEntry.setCurrentReceipt(entry.getCurrentReceipt());
			manualEntryReportList.add(manualEntry);
		}   
		setParamMap();
	}
	
	protected void setRelatedEntitesOn() {
		if(LOGGER.isInfoEnabled())     LOGGER.info(" Inside setRelatedEntitesOn:-  Adding heading and getting object values");
		
		heading.append("Manual Entry Report for ");
		if(reportSearch.getBankAccount() != null && reportSearch.getBankAccount().getId() != null && reportSearch.getBankAccount().getId() !=0){
			reportSearch.setBankAccount((Bankaccount)getPersistenceService().find("from Bankaccount where id=?",Integer.parseInt(reportSearch.getBankAccount().getId().toString())));
			heading.append(" Bank Name -" +reportSearch.getBankAccount().getBankbranch().getBank().getName() );
			heading.append(" Account Number-" +reportSearch.getBankAccount().getAccountnumber() );
		}
		if(reportSearch.getStartDate()!=null && reportSearch.getEndDate()!=null){
			heading.append(" From "+ reportSearch.getStartDate() +" to "+reportSearch.getEndDate());
		}
	}
	
	  
	public FundFlowManualEntryReportAction(){
		addRelatedEntity("fund", Fund.class);
		addRelatedEntity("bankAccount", Bankaccount.class);
		addRelatedEntity("bankbranch", Bankbranch.class);
		addRelatedEntity("bank", Bank.class);
	}
	
	@SuppressWarnings("unchecked")
	public String getUlbName() {
		Query query = HibernateUtil.getCurrentSession().createSQLQuery("select name from companydetail");
		List<String> result = query.list();
		if (result != null)
			return result.get(0);
		return "";
	}
	public void setParamMap()
	{
		String str= (StringHibernateUtil.getCurrentSession().get("headingStr").toString();
		BigDecimal amt= (BigDecimalHibernateUtil.getCurrentSession().get("total");
		String title=getUlbName();
		paramMap.put("title",getUlbName());
		paramMap.put("heading",str);
		paramMap.put("grandTotal",amt);
		
	}
	
	protected Map<String, Object> getParamMap() {
		return paramMap;
	}
	
	public String generatePdf() throws Exception{
		populateData();
		inputStream = reportHelper.exportPdf(inputStream, JASPERPATH,getParamMap(),manualEntryReportList);
		
		return "PDF";
	}
	public String generateXls() throws Exception{
		populateData();
		inputStream = reportHelper.exportXls(inputStream, JASPERPATH,getParamMap(),manualEntryReportList);
		return "XLS";
	}
	
	public String getFormattedDate(Date date) {
		return Constants.DDMMYYYYFORMAT2.format(date);
	}

	public ReportSearch getReportSearch() {
		return reportSearch;
	}

	public void setReportSearch(ReportSearch reportSearch) {
		this.reportSearch = reportSearch;
	}

	public StringBuffer getHeading() {
		return heading;
	}

	public String getSelectedAccountNumber() {
		return selectedAccountNumber;
	}

	public void setSelectedAccountNumber(String selectedAccountNumber) {
		this.selectedAccountNumber = selectedAccountNumber;
	}

	public void setHeading(StringBuffer heading) {
		this.heading = heading;
	}

	public List<Object> getManualEntryReportList() {
		return manualEntryReportList;
	}

	public List<FundFlowBean> getEntryReportList() {
		return entryReportList;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public ReportHelper getReportHelper() {
		return reportHelper;
	}

	public void setManualEntryReportList(
			List<Object> manualEntryReportList) {
		this.manualEntryReportList = manualEntryReportList;
	} 

	public void setEntryReportList(List<FundFlowBean> entryReportList) {
		this.entryReportList = entryReportList;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public void setReportHelper(ReportHelper reportHelper) {
		this.reportHelper = reportHelper;
	}

	public BigDecimal getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(BigDecimal grandTotal) {
		this.grandTotal = grandTotal;
	}

	

}
