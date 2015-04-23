package org.egov.web.actions.report;

import org.apache.struts2.convention.annotation.Action;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.StreamResult;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.Fund;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.services.report.RPService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.utils.ReportHelper;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.actions.bill.ContingentBillAction.COAcomparator;
import org.hibernate.Query;

@Results(value={
		@Result(name="PDF",type="stream",location=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"application/pdf",Constants.CONTENT_DISPOSITION,"no-cache;filename=receiptPaymentReport.pdf"}),
		@Result(name="XLS",type="stream",location=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"application/xls",Constants.CONTENT_DISPOSITION,"no-cache;filename=receiptPaymentReport.xls"})
	})

@ParentPackage("egov") 
public class ReceiptPaymentReportAction extends BaseFormAction {
	
	private static final long serialVersionUID = 1L;
	private static final String RECEIPT_PAYMENT_PDF = "PDF";
	private static final String RECEIPT_PAYMENT_XLS = "XLS";
	private static final String R = "R";
	private static final String P = "P";
	private String scheduleNo;
	private RPService rpService;
	private Date todayDate;
	Statement receiptPayment = new Statement();
	private StringBuffer header=new StringBuffer();
	Map<String, String> scheduleMasterMap = new LinkedHashMap<String, String>();
	protected FinancialYearHibernateDAO financialYearDAO;
	public static final Locale LOCALE = new Locale("en","IN");
	public static final SimpleDateFormat  DDMMYYYYFORMATS= new SimpleDateFormat("dd/MM/yyyy",LOCALE);
	final static Logger	LOGGER	= Logger.getLogger(ReceiptPaymentReportAction.class);
	InputStream inputStream;
	ReportHelper reportHelper;
	
	@Override
	public Object getModel() {
		return receiptPayment;
	}
	
@Action(value="/report/receiptPaymentReport-newForm")
	public String newForm()
	{
		loadDropDownData();
		return NEW;
	}

	      
	public String search()
	{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside Search|Ready to fetch data");
		generateReceiptPaymentReport();
		loadDropDownData();
		return NEW;
	}
	
@Action(value="/report/receiptPaymentReport-searchDetail")
	public String searchDetail()
	{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside searchDetail| fetaching data for detail code result");
		generateScheduleReport();
		
		return "detail";
	}
	
	private void generateScheduleReport() {
		header.append("Receipt Payment Report ").append(" for schedule No"+getScheduleNo()); 
		setRelatedEntitesOn();
		if(receiptPayment.getFund() != null && receiptPayment.getFund().getId() != null && !receiptPayment.getFund().getId().equals(0)){
			fetchDetailDataAndPopulate();
		}else{
			populateConsolidatedScheduleReport();
		}
	}
	
	private void loadDropDownData() {
		EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
		addDropdownData("fundList", masterCache.get("egi-fund"));
		addDropdownData("financialYearList", getPersistenceService().findAllBy("from CFinancialYear where isActive=1 order by finYearRange desc "));
	}
	
	
	private void addAomuntToScheduleMap(List<StatementResultObject>  receiptPaymentList,Map<String,BigDecimal> prepareMap) {
		
		//prepareMap.put("Operating Receipt",BigDecimal.ZERO);
		for (StatementResultObject row : receiptPaymentList) {
			prepareMap.put(row.getScheduleNumber(),row.getAmount());
        }
		
	}

	
	public void fetchDetailDataAndPopulate() {
		//CFinancialYear currentYear = this.financialYear;
		CFinancialYear previousYear = getFinancialYearDAO().getPreviousFinancialYearByDate(receiptPayment.getFinancialYear().getStartingDate());
		
		Map<String, String> scheduleDetailNonSubMasterMap = new LinkedHashMap<String, String>();
		Map<String, String> scheduleDetailSubMasterMap = new LinkedHashMap<String, String>();
		Map<String, String> subScheduleMasterMap = new LinkedHashMap<String, String>();
/*		Map<String, BigDecimal> currentDetailMap = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> previousDetailMap = new HashMap<String, BigDecimal>();*/
		
		List<Object> tranType = rpService.getTransactionType(this.scheduleNo);
		
		List<Object[]> subScheduleMaster = rpService.getSubScheduleMaster(this.scheduleNo,receiptPayment.getFund().getCode());
		addScheduleNameAndNumberToMap(subScheduleMaster,subScheduleMasterMap);
		
		List<Object[]> scheduleDetailMasterNonSub = rpService.getDetailGlcodeNonSubSchedule(this.scheduleNo,receiptPayment.getFund().getCode());
		addScheduleNameAndNumberToMap(scheduleDetailMasterNonSub,scheduleDetailNonSubMasterMap);
		
		List<Object[]> scheduleDetailMasterSub = rpService.getDetailGlcodeSubSchedule(this.scheduleNo,receiptPayment.getFund().getCode());
		addScheduleNameAndNumberToMap(scheduleDetailMasterSub,scheduleDetailSubMasterMap);

		List<StatementResultObject> currentDetail = rpService.getDetailData(receiptPayment.getFinancialYear(),tranType.get(0).toString(), this.scheduleNo,receiptPayment);
		//addAmountToGlcodeMap(currentDetail, currentDetailMap);
		List<StatementResultObject> previousDetail = rpService.getDetailData(previousYear,tranType.get(0).toString(), this.scheduleNo,receiptPayment);
		//addAmountToGlcodeMap(previousDetail, previousDetailMap);
		
		
		addtoReceiptPaymentForDetailCode(subScheduleMasterMap, scheduleDetailSubMasterMap, scheduleDetailNonSubMasterMap, currentDetail,previousDetail,this.scheduleNo);
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug("size of detail code"+receiptPayment.getEntries().size());
     
}
	
	
	
	private void generateReceiptPaymentReport() {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("-----Starting generateReceiptPaymentReport-----");
		header.append("Receipt Payment Report");
		setRelatedEntitesOn();
		if(receiptPayment.getFund() != null && receiptPayment.getFund().getId() != null && receiptPayment.getFund().getId() !=0){
				fetchDataAndPopulate();
		}else{
			   populateConsolidatedReport();
		}
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug("size of receipt Payment  report is"+receiptPayment.getEntries().size());
		
		/*for(int index=0;index<receiptPayment.getEntries().size();index++){
			if(LOGGER.isDebugEnabled())     LOGGER.debug("size of receipt Payment  report is"+receiptPayment.getEntries().get(index).getFundWiseAmount());
		}*/
	}
	
	public void fetchDataAndPopulate() {
		if(LOGGER.isDebugEnabled())     LOGGER.debug(" ReceiptPaymentReportAction| Inside fetchDataAndPopulate");
		Map<String, BigDecimal> currentReceiptPaymentMap = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> previousReceiptPaymentMap = new HashMap<String, BigDecimal>();
		StatementEntry rpbean = new StatementEntry();
		List<StatementResultObject> scheduleReceiptPaymentMaster = rpService.getScheduleNoAndName();
		
		CFinancialYear previousYear = getFinancialYearDAO().getPreviousFinancialYearByDate(receiptPayment.getFinancialYear().getStartingDate());
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Getting Current year Payment receipt");
		List<StatementResultObject> currentYearReceiptPaymentResult = rpService.getData(receiptPayment.getFinancialYear(),receiptPayment);
		addAomuntToScheduleMap(currentYearReceiptPaymentResult,currentReceiptPaymentMap);
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug(" Getting previous year Payment receipt");
		List<StatementResultObject> previousYearReceiptPaymentResult = rpService.getData(previousYear,receiptPayment);
		addAomuntToScheduleMap(previousYearReceiptPaymentResult,previousReceiptPaymentMap);
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug(" Populating receipt and payment to receipt Payment object"); 
		addtoReceiptPayment(scheduleReceiptPaymentMaster,currentReceiptPaymentMap,previousReceiptPaymentMap);
		
	}
	
	private void addtoReceiptPayment(List<StatementResultObject> scheduleReceiptPaymentMasterList,
			Map<String, BigDecimal> currentReceiptPaymentMap,Map<String, BigDecimal> previousReceiptPaymentMap) {
		
		BigDecimal currentReceiptTotal=BigDecimal.ZERO;
		BigDecimal previousReceiptTotal=BigDecimal.ZERO;
		BigDecimal currentPaymentTotal=BigDecimal.ZERO;
		BigDecimal previousPaymentTotal=BigDecimal.ZERO;
		
		List<StatementEntry> receiptList=new ArrayList<StatementEntry>();
		List<StatementEntry> paymentList=new ArrayList<StatementEntry>();
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug("// Iterating to populate Receipt List and sum up total ");
		receiptList.add(new StatementEntry(null,"Operating Receipt",null,null,null,true));
		paymentList.add(new StatementEntry(null,"Operating Payment",null,null,null,true));
		for(StatementResultObject entry:scheduleReceiptPaymentMasterList){
			StatementEntry receiptbean=new StatementEntry();
			StatementEntry paymentbean=new StatementEntry();
			if(R.equals(entry.getType().toString())){
				receiptbean.setScheduleNo(entry.getScheduleNumber());
				receiptbean.setAccountName(entry.getScheduleName());
	        	receiptbean.setCurrentYearTotal(currentReceiptPaymentMap.containsKey(entry.getScheduleNumber())?currentReceiptPaymentMap.get(entry.getScheduleNumber()):BigDecimal.ZERO);
	        	currentReceiptTotal=currentReceiptTotal.add(receiptbean.getCurrentYearTotal());
	        	receiptbean.setPreviousYearTotal(previousReceiptPaymentMap.containsKey(entry.getScheduleNumber())?previousReceiptPaymentMap.get(entry.getScheduleNumber()):BigDecimal.ZERO);
	        	previousReceiptTotal=previousReceiptTotal.add(receiptbean.getPreviousYearTotal());
	        	receiptList.add(receiptbean);
			}else{
				if(LOGGER.isDebugEnabled())     LOGGER.debug(" Populating Payment list and adding up total");
				paymentbean.setScheduleNo(entry.getScheduleNumber());
				paymentbean.setAccountName(entry.getScheduleName());
	        	paymentbean.setCurrentYearTotal(currentReceiptPaymentMap.containsKey(entry.getScheduleNumber())?currentReceiptPaymentMap.get(entry.getScheduleNumber()):BigDecimal.ZERO);
	        	currentPaymentTotal=currentPaymentTotal.add(paymentbean.getCurrentYearTotal());
	        	paymentbean.setPreviousYearTotal(previousReceiptPaymentMap.containsKey(entry.getScheduleNumber())?previousReceiptPaymentMap.get(entry.getScheduleNumber()):BigDecimal.ZERO);
	        	previousPaymentTotal=previousPaymentTotal.add(paymentbean.getPreviousYearTotal());
	        	paymentList.add(paymentbean);
			}
        }
		receiptList.add( new StatementEntry(null,"Grand Total","",previousReceiptTotal,currentReceiptTotal,true));
		paymentList.add( new StatementEntry(null,"Grand Total","",previousPaymentTotal,currentPaymentTotal,true));
        
		receiptPayment.getEntries().addAll(receiptList);
		receiptPayment.getEntries().addAll(paymentList);
	}
	
	public void populateConsolidatedReport() {
		if(LOGGER.isDebugEnabled())     LOGGER.debug(" ReceiptPaymentReportAction| Inside fetchDataAndPopulate");
		List<StatementResultObject> scheduleReceiptPaymentMaster = rpService.getScheduleNoAndName();
		Statement receiptEntry=new Statement();
		Statement paymentEntry=new Statement();
		
		receiptEntry.add(new StatementEntry(null,FinancialConstants.RECEIPTS,null,null,null,true));
		paymentEntry.add(new StatementEntry(null,FinancialConstants.PAYMENTS,null,null,null,true));
		CFinancialYear previousYear = getFinancialYearDAO().getPreviousFinancialYearByDate(receiptPayment.getFinancialYear().getStartingDate());
		
		List<StatementResultObject> currentReceiptPaymentList = rpService.getConsolidatedResult(receiptPayment.getFinancialYear(),receiptPayment);
		addFundWiseAmountToStatement(currentReceiptPaymentList,receiptEntry,paymentEntry);
		if(LOGGER.isDebugEnabled())     LOGGER.debug(" ReceiptPaymentReportAction| Receipt Payment size after adding current year receipt is"+receiptPayment.getEntries().size());
		List<StatementResultObject> previousReceipt= rpService.getData(previousYear,receiptPayment);
		addPreviousYearAmountToStatement(previousReceipt,receiptEntry,paymentEntry);
		computeCurrentYearTotals(receiptEntry);     
		computeCurrentYearTotals(paymentEntry);
		computeTotals(receiptEntry);
		computeTotals(paymentEntry);
		receiptPayment.addAll(receiptEntry);
		receiptPayment.addAll(paymentEntry);
		
	}
	
	private void computeTotals(Statement receipt) {
		
			Map<String,BigDecimal> fundNetTotals = new HashMap<String, BigDecimal>();
			BigDecimal currentFundAmount=BigDecimal.ZERO;
			BigDecimal preAmount=BigDecimal.ZERO;
			BigDecimal curAmount=BigDecimal.ZERO;
			StatementEntry statementObj=new StatementEntry();
			for (StatementEntry entry : receipt.getEntries()) {
				if( entry.getFundWiseAmount()!=null){
					for (Map.Entry<String, BigDecimal> row : entry.getFundWiseAmount().entrySet()) {
						if(fundNetTotals.get(row.getKey())==null){
							fundNetTotals.put(row.getKey(),BigDecimal.ZERO);
						}
						 currentFundAmount=row.getValue();
						 fundNetTotals.put(row.getKey(),currentFundAmount.add(fundNetTotals.get(row.getKey())));
					}
				}
				preAmount=preAmount.add(entry.getPreviousYearTotal()!=null?entry.getPreviousYearTotal():BigDecimal.ZERO);
				curAmount=curAmount.add(entry.getCurrentYearTotal()!=null?entry.getCurrentYearTotal():BigDecimal.ZERO);
			}
			statementObj.setAccountName("Grand Total");
			statementObj.setDisplayBold(true);
			statementObj.setFundWiseAmount(fundNetTotals);
			statementObj.setCurrentYearTotal(curAmount);
			statementObj.setPreviousYearTotal(preAmount);
			receipt.add(statementObj);
		
		
	}

	protected void setRelatedEntitesOn() {
		setTodayDate(new Date());
		if(receiptPayment.getFinancialYear() != null && receiptPayment.getFinancialYear().getId() !=null){
			receiptPayment.setFinancialYear((CFinancialYear)getPersistenceService().find("from CFinancialYear where id=?", receiptPayment.getFinancialYear().getId()));
			header.append(" for the Financial Year "+receiptPayment.getFinancialYear().getFinYearRange());
		}
		if(receiptPayment.getFund() != null && receiptPayment.getFund().getId() != null && receiptPayment.getFund().getId() !=0){
			receiptPayment.setFund((Fund)getPersistenceService().find("from Fund where id=?", receiptPayment.getFund().getId()));
			//receiptPayment.setFunds(list)
			header.append(" for "+ receiptPayment.getFund().getName());
		}else{
			receiptPayment.setFunds(rpService.getFunds());
		}
		if(receiptPayment.getAsOndate()!=null){
			header.append(" as on "+ DDMMYYYYFORMATS.format(receiptPayment.getAsOndate()));
		}
		header.toString();
	}
	   
	public String exportReceiptPaymentScheduleXls() throws Exception{
		generateScheduleReport();
		String heading=getUlbName()+"\\n"+header.toString();
		String subtitle="Report Run Date-"+DDMMYYYYFORMATS.format(getTodayDate());
		JasperPrint jasper = reportHelper.generateReceiptPaymentReportJasperPrint(receiptPayment,heading,subtitle,getCurrentYearToDate(),getPreviousYearToDate(),false);
		inputStream = reportHelper.exportXls(inputStream, jasper);
		return RECEIPT_PAYMENT_XLS;
	}
	public String exportReceiptPaymentSchedulePdf() throws Exception{
		generateScheduleReport();
		String heading=getUlbName()+"\\n"+header.toString();
		String subtitle="Report Run Date-"+DDMMYYYYFORMATS.format(getTodayDate());
		JasperPrint jasper = reportHelper.generateReceiptPaymentReportJasperPrint(receiptPayment,heading,subtitle,getCurrentYearToDate(),getPreviousYearToDate(),false);
		inputStream = reportHelper.exportPdf(inputStream, jasper);
		return RECEIPT_PAYMENT_PDF;
	}

	public String exportReceiptPaymentXls() throws Exception{
		generateReceiptPaymentReport();
		String heading=getUlbName()+"\\n"+header.toString();
		String subtitle="Report Run Date-"+DDMMYYYYFORMATS.format(getTodayDate());
		JasperPrint jasper = reportHelper.generateReceiptPaymentReportJasperPrint(receiptPayment,heading,subtitle,getCurrentYearToDate(),getPreviousYearToDate(),true);
		inputStream = reportHelper.exportXls(inputStream, jasper);
		return RECEIPT_PAYMENT_XLS;
	}
	public String exportReceiptPaymentPdf() throws Exception{
		generateReceiptPaymentReport();
		String heading=getUlbName()+"\\n"+header.toString();
		String subtitle="Report Run Date-"+DDMMYYYYFORMATS.format(getTodayDate());
		JasperPrint jasper = reportHelper.generateReceiptPaymentReportJasperPrint(receiptPayment,heading,subtitle,getCurrentYearToDate(),getPreviousYearToDate(),true);
		inputStream = reportHelper.exportPdf(inputStream, jasper);
		return RECEIPT_PAYMENT_PDF;
	}
	
	private void addScheduleNameAndNumberToMap(List<Object[]>  tempList,Map<String,String> resultMap) {
		for (Object[] row : tempList) {
			resultMap.put(row[0].toString(), row[1].toString());
        }
	}
	
	private void addToList(List<Object[]>  tempList,List<String> resultList) {
		for (Object[] row : tempList) {
			resultList.add(row[0].toString());
        }
	}
	
	private void addAmountToGlcodeMap(List<StatementResultObject>  tempList,Map<String,BigDecimal> resultMap) {
		for (StatementResultObject row : tempList) {
			resultMap.put(row.getGlCode(), row.getAmount());
        }
	}
	private void addtoReceiptPaymentForDetailCode(Map<String, String> subScheduleMasterMap, Map<String, String> scheduleDetailSubMasterMap, Map<String, String> scheduleDetailNonSubMasterMap, List<StatementResultObject> currentPayment,List<StatementResultObject> previousPayment, String scheduleNo) {
		BigDecimal currentTotal=BigDecimal.ZERO;
		BigDecimal	previousTotal=BigDecimal.ZERO;
		boolean loadBean = false;
		for (Map.Entry<String, String> entry : subScheduleMasterMap.entrySet())
		{
			BigDecimal currentSubTotal=BigDecimal.ZERO;
			BigDecimal	previousSubTotal=BigDecimal.ZERO;
			receiptPayment.add(new StatementEntry(null,entry.getKey()+" : "+entry.getValue(),null,null,null,true));
			for(Map.Entry<String, String> subEntry : scheduleDetailSubMasterMap.entrySet())
			{
				if(entry.getKey().equals(subEntry.getValue())){
					StatementEntry rpbean = new StatementEntry();
		        	//rpbean.setAccountName(entry.getValue());
		        	rpbean.setGlCode(subEntry.getKey());	
					for (StatementResultObject currentEntry : currentPayment){
						if(currentEntry.getScheduleNumber().equals(scheduleNo) && subEntry.getKey().equals(currentEntry.getGlCode())){
							loadBean = true;
				        	rpbean.setCurrentYearTotal(currentEntry.getAmount());
				        	currentTotal=currentTotal.add(rpbean.getCurrentYearTotal());
				        	currentSubTotal=currentSubTotal.add(rpbean.getCurrentYearTotal());
						}
					}
					for(StatementResultObject previousEntry : previousPayment){
						if(previousEntry.getScheduleNumber().equals(scheduleNo) && subEntry.getKey().equals(previousEntry.getGlCode())){
							loadBean = true;
				        	rpbean.setPreviousYearTotal(previousEntry.getAmount());
				        	previousTotal=previousTotal.add(rpbean.getPreviousYearTotal());
				        	previousSubTotal=previousSubTotal.add(rpbean.getPreviousYearTotal());
						}
					}
					if(loadBean){
						receiptPayment.getEntries().add(rpbean);
						loadBean = false;
					}
				}
	        }
			receiptPayment.add(new StatementEntry(null,"Sub Total",null,previousSubTotal,currentSubTotal,true));
		}
		
		for(Map.Entry<String, String> nonSubEntry : scheduleDetailNonSubMasterMap.entrySet())
		{
			StatementEntry rpbean = new StatementEntry();
        	rpbean.setGlCode(nonSubEntry.getKey());	
			for (StatementResultObject currentEntry : currentPayment){
				if(currentEntry.getScheduleNumber().equals(scheduleNo) && nonSubEntry.getKey().equals(currentEntry.getGlCode())){
					loadBean = true;
		        	rpbean.setCurrentYearTotal(currentEntry.getAmount());
		        	currentTotal=currentTotal.add(rpbean.getCurrentYearTotal());
				}
			}
			for(StatementResultObject previousEntry : previousPayment){
				if(previousEntry.getScheduleNumber().equals(scheduleNo) && nonSubEntry.getKey().equals(previousEntry.getGlCode())){
					loadBean = true;
		        	rpbean.setPreviousYearTotal(previousEntry.getAmount());
		        	previousTotal=previousTotal.add(rpbean.getPreviousYearTotal());
				}
			}
			if(loadBean){
				receiptPayment.getEntries().add(rpbean);
				loadBean = false;
			}
        }
			receiptPayment.add(new StatementEntry(null,"Grand Total",null,previousTotal,currentTotal,true));
	}
	
	private void addtoReceiptPaymentForDetailCodeConsolidated(List<String> fundMasterList, Map<String, String> subScheduleMasterMap, Map<String, String> scheduleDetailSubMasterMap, Map<String, String> scheduleDetailNonSubMasterMap, List<StatementResultObject> currentPayment,List<StatementResultObject> previousPayment, String scheduleNo) {
		BigDecimal currentTotal=BigDecimal.ZERO;
		BigDecimal	previousTotal=BigDecimal.ZERO;
		boolean loadBean = false;
		for (Map.Entry<String, String> entry : subScheduleMasterMap.entrySet())
		{
			BigDecimal currentSubTotal=BigDecimal.ZERO;
			BigDecimal	previousSubTotal=BigDecimal.ZERO;
			receiptPayment.add(new StatementEntry(null,entry.getKey()+" : "+entry.getValue(),null,null,null,true));
			for(Map.Entry<String, String> subEntry : scheduleDetailSubMasterMap.entrySet())
			{
				if(entry.getKey().equals(subEntry.getValue())){
					StatementEntry rpbean = new StatementEntry();
		        	//rpbean.setAccountName(entry.getValue());
		        	rpbean.setGlCode(subEntry.getKey());	
					for (StatementResultObject currentEntry : currentPayment){
						for(String fundCode : fundMasterList){
							if(currentEntry.getScheduleNumber().equals(scheduleNo) && subEntry.getKey().equals(currentEntry.getGlCode()) && fundCode.equals(currentEntry.getFundCode())){
								loadBean = true;
								rpbean.putFundWiseAmount(currentEntry.getFundCode(), currentEntry.getAmount());
					        	rpbean.setCurrentYearTotal(rpbean.getCurrentYearTotal().add(currentEntry.getAmount()));
					        	currentTotal=currentTotal.add(rpbean.getCurrentYearTotal());
					        	currentSubTotal=currentSubTotal.add(rpbean.getCurrentYearTotal());
							}
						}
					}
					for(StatementResultObject previousEntry : previousPayment){
						if(previousEntry.getScheduleNumber().equals(scheduleNo) && subEntry.getKey().equals(previousEntry.getGlCode())){
							loadBean = true;
				        	rpbean.setPreviousYearTotal(previousEntry.getAmount());
				        	previousTotal=previousTotal.add(rpbean.getPreviousYearTotal());
				        	previousSubTotal=previousSubTotal.add(rpbean.getPreviousYearTotal());
						}
					}
					if(loadBean){
						receiptPayment.getEntries().add(rpbean);
						loadBean = false;
					}
				}
	        }
			receiptPayment.add(new StatementEntry(null,"Sub Total",null,previousSubTotal,currentSubTotal,true));
		}
		
		for(Map.Entry<String, String> nonSubEntry : scheduleDetailNonSubMasterMap.entrySet())
		{
			StatementEntry rpbean = new StatementEntry();
        	rpbean.setGlCode(nonSubEntry.getKey());	
			for (StatementResultObject currentEntry : currentPayment){
				for(String fundCode : fundMasterList){
					if(currentEntry.getScheduleNumber().equals(scheduleNo) && nonSubEntry.getKey().equals(currentEntry.getGlCode()) && fundCode.equals(currentEntry.getFundCode())){
						loadBean = true;
						rpbean.putFundWiseAmount(currentEntry.getFundCode(), currentEntry.getAmount());
						rpbean.setCurrentYearTotal(rpbean.getCurrentYearTotal().add(currentEntry.getAmount()));
			        	currentTotal=currentTotal.add(rpbean.getCurrentYearTotal());
					}
				}
			}
			for(StatementResultObject previousEntry : previousPayment){
				if(previousEntry.getScheduleNumber().equals(scheduleNo) && nonSubEntry.getKey().equals(previousEntry.getGlCode())){
					loadBean = true;
		        	rpbean.setPreviousYearTotal(previousEntry.getAmount());
		        	previousTotal=previousTotal.add(rpbean.getPreviousYearTotal());
				}
			}
			if(loadBean){
				receiptPayment.getEntries().add(rpbean);
				loadBean = false;
			}
        }
			receiptPayment.add(new StatementEntry(null,"Grand Total",null,previousTotal,currentTotal,true));
	}
	
   
	private void addFundWiseAmountToStatement(List<StatementResultObject>  rpCurrentYearList,
								Statement receipt,Statement payment) {
		boolean addRow=false;
		Map<String,BigDecimal> fundWiseAmountTotal = new HashMap<String, BigDecimal>();
		if(LOGGER.isDebugEnabled())     LOGGER.debug("addFundWiseAmountToStatement | populating fundwise amount for current year");
		for(StatementResultObject row:rpCurrentYearList){
		StatementEntry rpbean = new StatementEntry();
		if(R.equals(row.getType().toString())){
				if(LOGGER.isDebugEnabled())     LOGGER.debug(" Populating recipt list");
				if(receipt.containsStatementEntryScheduleNo(row.getScheduleNumber())){
					if(LOGGER.isDebugEnabled())     LOGGER.debug(" receipt entry contains more than 1 fund fro the "+row.getScheduleNumber()+"Schedule");
					for(int index=1;index<receipt.size();index++)
					{
						if(receipt.get(index).getScheduleNo().equals(row.getScheduleNumber()))
						{
							receipt.get(index).getFundWiseAmount().put(row.getFundCode(),row.getAmount() );
							//fundWiseAmountTotal.put(row.getFundCode(),receiptEntry.get(index).getFundWiseAmount().get(row.getFundCode()).add(row.getAmount()));
						}
					}
					
				}else
				{
					addRow=true;
					rpbean.setAccountName(row.getScheduleName());
					rpbean.setScheduleNo(row.getScheduleNumber());
					rpbean.setFundCode(row.getFundCode());
					rpbean.getFundWiseAmount().put(row.getFundCode(), row.getAmount() );
				}
				if(addRow){	
					receipt.add(rpbean);
				}addRow=false;
				
			}
		else
		{
				if(LOGGER.isDebugEnabled())     LOGGER.debug(" Populating Payment list");
				if(payment.containsStatementEntryScheduleNo(row.getScheduleNumber())){
					for(int index=1;index<payment.size();index++)
					{
						if(payment.get(index).getScheduleNo().equals(row.getScheduleNumber()))
						{
							payment.get(index).getFundWiseAmount().put(row.getFundCode(),row.getAmount());
						}
						
					}
				}else{
					addRow=true;
					rpbean.setAccountName(row.getScheduleName());
					rpbean.setScheduleNo(row.getScheduleNumber());
					rpbean.setFundCode(row.getFundCode());
					rpbean.getFundWiseAmount().put(row.getFundCode(), row.getAmount());
				}if(addRow)
				{
					payment.add(rpbean);
				}addRow=false;
			}
		}
	}
	public void computeCurrentYearTotals(Statement receipt) {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Adding current year total for receipt / payment");
		for (StatementEntry receiptObj : receipt.getEntries()) {
		if (receiptObj.getFundWiseAmount()!=null){
			BigDecimal currentYearTotal = BigDecimal.ZERO;
			for (Entry<String, BigDecimal> entry : receiptObj.getFundWiseAmount().entrySet()) {
				currentYearTotal = currentYearTotal.add(entry.getValue());
			}
			receiptObj.setCurrentYearTotal(currentYearTotal);
			}
		}
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug("finished calculating  current year total for receipt / payment");
	}
	
	private void addPreviousYearAmountToStatement(List<StatementResultObject>  previousYearList,Statement receipt,Statement payment) {
		boolean addRow=false;
		for (StatementResultObject row : previousYearList) {
			StatementEntry entry=new StatementEntry();
				if(LOGGER.isDebugEnabled())     LOGGER.debug("Adding to receipt List");
				if(R.equals(row.getType().toString())){
				if(receipt.containsStatementEntryScheduleNo(row.getScheduleNumber()))
				{ // Starting from 1 as the 0th element contains receipt/payment header
					if(LOGGER.isDebugEnabled())     LOGGER.debug("iNSIDE RECEIPT FOR PREVIOUS YEAR"+row.getScheduleNumber());
					for(StatementEntry obj:receipt.getEntries())
					{
						if( obj.getScheduleNo()!=null 
								&& obj.getScheduleNo().equals(row.getScheduleNumber())){
							obj.setPreviousYearTotal(row.getAmount());
						}
					
					}
				}
				else
				{
					if(LOGGER.isDebugEnabled())     LOGGER.debug("iNSIDE RECEIPT FOR PREVIOUS YEAR"+row.getScheduleNumber()+"aDDING NEW ENTRY");
					addRow=true;
					entry.setScheduleNo(row.getScheduleNumber());
					entry.setAccountName(row.getScheduleName());
					entry.setPreviousYearTotal(row.getAmount());
				}if(addRow){
					receipt.add(entry);
				}addRow=false;
				}else{
					if(payment.containsStatementEntryScheduleNo(row.getScheduleNumber()))
					{ 
						if(LOGGER.isDebugEnabled())     LOGGER.debug("iNSIDE RECEIPT FOR PREVIOUS YEAR"+row.getScheduleNumber());
							for(StatementEntry obj:payment.getEntries())
							{
								if( obj.getScheduleNo()!=null 
										&& obj.getScheduleNo().equals(row.getScheduleNumber())){
									obj.setPreviousYearTotal(row.getAmount());
								}
							
							}
					}
					else
					{
						if(LOGGER.isDebugEnabled())     LOGGER.debug("iNSIDE RECEIPT FOR PREVIOUS YEAR"+row.getScheduleNumber()+"aDDING NEW ENTRY");
						addRow=true;
						entry.setScheduleNo(row.getScheduleNumber());
						entry.setAccountName(row.getScheduleName());
						entry.setPreviousYearTotal(row.getAmount());
					}if(addRow){
						payment.add(entry);
					}addRow=false;
					}
				}
	}
	
	public void populateConsolidatedScheduleReport() {
/*		if(LOGGER.isDebugEnabled())     LOGGER.debug(" ReceiptPaymentReportAction| Inside fetchDataAndPopulate");
		//populateScheduleNoMap();
		
		CFinancialYear previousYear = getFinancialYearDAO().getPreviousFinancialYearByDate(receiptPayment.getFinancialYear().getStartingDate());
		
		Map<String, String> scheduleDetailMasterMap = new LinkedHashMap<String, String>();
		Map<String, BigDecimal> currentDetailMap = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> previousDetailMap = new HashMap<String, BigDecimal>();
		
		List<Object> tranType = rpService.getTransactionType(this.scheduleNo);
		
		List<Object[]> scheduleDetailMaster = rpService.getGlcodeForConsolidatedReport(this.scheduleNo);
		addToMap(scheduleDetailMaster, scheduleDetailMasterMap);

		List<StatementResultObject> currentDetail = rpService.getCurrentYearConsolidatedReportForGlcode(receiptPayment.getFinancialYear().getId(),tranType.get(0).toString(), this.scheduleNo,receiptPayment);
		addFundWiseAmountToStatementForDetailCode(currentDetail, scheduleDetailMasterMap);
		
		List<StatementResultObject> previousDetail = rpService.getDetailData(previousYear.getId(),tranType.get(0).toString(), this.scheduleNo,receiptPayment);
		addFundWiseAmountForPreviousYearToDetailCode(previousDetail, scheduleDetailMasterMap);
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug("size of detail code"+receiptPayment.getEntries().size());*/

		if(LOGGER.isDebugEnabled())     LOGGER.debug(" ReceiptPaymentReportAction| Inside fetchDataAndPopulate");
		
		CFinancialYear previousYear = getFinancialYearDAO().getPreviousFinancialYearByDate(receiptPayment.getFinancialYear().getStartingDate());
		
		Map<String, String> scheduleDetailNonSubMasterMap = new LinkedHashMap<String, String>();
		Map<String, String> scheduleDetailSubMasterMap = new LinkedHashMap<String, String>();
		Map<String, String> subScheduleMasterMap = new LinkedHashMap<String, String>();
		List<String> fundMasterList = new ArrayList<String>();
		List<Object> tranType = rpService.getTransactionType(this.scheduleNo);
		
		List<Object[]> fundMaster = rpService.getfundMaster();
		addToList(fundMaster, fundMasterList);
		
		List<Object[]> subScheduleMaster = rpService.getSubScheduleMasterConsolidated(this.scheduleNo);
		addScheduleNameAndNumberToMap(subScheduleMaster,subScheduleMasterMap);
		
		List<Object[]> scheduleDetailMasterNonSub = rpService.getDetailGlcodeNonSubScheduleConsolidated(this.scheduleNo);
		addScheduleNameAndNumberToMap(scheduleDetailMasterNonSub,scheduleDetailNonSubMasterMap);
		
		List<Object[]> scheduleDetailMasterSub = rpService.getDetailGlcodeSubScheduleConsolidated(this.scheduleNo);
		addScheduleNameAndNumberToMap(scheduleDetailMasterSub,scheduleDetailSubMasterMap);

		List<StatementResultObject> currentDetail = rpService.getCurrentYearConsolidatedReportForGlcode(receiptPayment.getFinancialYear(),tranType.get(0).toString(), this.scheduleNo,receiptPayment);
		
		List<StatementResultObject> previousDetail = rpService.getDetailData(previousYear,tranType.get(0).toString(), this.scheduleNo,receiptPayment);
		
		
		addtoReceiptPaymentForDetailCodeConsolidated(fundMasterList, subScheduleMasterMap, scheduleDetailSubMasterMap, scheduleDetailNonSubMasterMap, currentDetail,previousDetail,this.scheduleNo);
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug("size of detail code"+receiptPayment.getEntries().size());
	}
	private void addFundWiseAmountToStatementForDetailCode(List<StatementResultObject>  tempList,Map<String, String> scheduleDetailMasterMap) {
		boolean addRow=false;
		// add receipt payment first line item here
		for(StatementResultObject row : tempList){
			StatementEntry entry=new StatementEntry();
			if(receiptPayment.containsStatementEntryOfDetailedCode(row.getGlCode()))
			{ /* This block is executed when there is multiple entry for the same schedule no ie for consolidate report
				*  different fund is mapped to same schedule. 
				*  Here we are adding fund amount to the index wer the 
				*/
				//if(scheduleMasterMap.containsKey(row.getScheduleNo()))
				{
					for(int index=0;index<receiptPayment.getEntries().size();index++)
					{
						if(receiptPayment.get(index).getGlCode().equals(row.getGlCode()))
						{
							receiptPayment.get(index).getFundWiseAmount().put(row.getFundCode(), row.getAmount());
						}
						
					}
				}
			}
			else   //  this block is implemented when there is no entry for scheno in statementEntry
			{
				addRow=true;
				// Need not check this condition alway, apply diff logic for it
				//if(scheduleMasterMap.containsKey(row.getScheduleNo()))
				{
					entry.setAccountName(scheduleDetailMasterMap.get(row.getGlCode()));
					entry.setGlCode(row.getGlCode());
					entry.setFundCode(row.getFundCode());
					if(R.equals(row.getType().toString()))
						entry.getFundWiseAmount().put(row.getFundCode(), row.getAmount() );
					else
						entry.getFundWiseAmount().put(row.getFundCode(), row.getAmount());
				}
					
			}
			if(addRow){
				receiptPayment.add(entry);
			}addRow=false;
		}
		
		
		
	}
	private void addFundWiseAmountForPreviousYearToDetailCode(List<StatementResultObject>  tempList,Map<String,String> resultMap) {
		boolean addRow=false;
		for (StatementResultObject row : tempList) {
			StatementEntry entry=new StatementEntry();
			if(receiptPayment.containsStatementEntryOfDetailedCode(row.getGlCode()))
			{ 
				for(int index=0;index<receiptPayment.getEntries().size();index++)
				{
					if(receiptPayment.get(index).getGlCode().equals(row.getGlCode()))
					{
						if(R.equals(row.getType().toString()))
							receiptPayment.get(index).setPreviousYearTotal(row.getAmount());
						else
							receiptPayment.get(index).setPreviousYearTotal(row.getAmount());
							
					}
					
				}
				
			}
			else
			{
				addRow=true;
				entry.setGlCode(row.getGlCode());
				entry.setAccountName(resultMap.get(row.getGlCode()));
				if(R.equals(row.getType().toString()))
					entry.setPreviousYearTotal(row.getAmount());
				else
					entry.setPreviousYearTotal(row.getAmount());
				
			}if(addRow){
				receiptPayment.add(entry);
			}addRow=false;
				
			//resultMap.put(row.getScheduleNo(), row.getCurrentYearTotal());
			
        }
	//	return resultMap;
	}

	public String getCurrentYearToDate(){
		return rpService.getFormattedDate(rpService.getCurrentYearToDate(receiptPayment));
	}
	public String getPreviousYearToDate(){
		return rpService.getFormattedDate(rpService.getPreviousYearToDate(receiptPayment));
	}
	

	private void addToMap(List<Object[]>  tempList,Map<String,String> resultMap) {
		for (Object[] row : tempList) {
			resultMap.put(row[0].toString(), row[1].toString());
        }
		//return resultMap;
	}
	public void setFinancialYearDAO(FinancialYearHibernateDAO financialYearDAO) {
		this.financialYearDAO = financialYearDAO;
	}

	public void setHeader(StringBuffer header) {
		this.header = header;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public void setReceiptPayment(Statement receiptPayment) {
		this.receiptPayment = receiptPayment;
	}

	public void setReportHelper(ReportHelper reportHelper) {
		this.reportHelper = reportHelper;
	}

	public void setRpService(RPService rpService) {
		this.rpService = rpService;
	}

	public void setScheduleNo(String scheduleNo) {
		this.scheduleNo = scheduleNo;
	}

	public void setTodayDate(Date todayDate) {
		this.todayDate = todayDate;
	}
	
	   
	public FinancialYearHibernateDAO getFinancialYearDAO() {
		return financialYearDAO;
	}
	public StringBuffer getHeader() {
		return header;
	}
	
	public InputStream getInputStream() {
		return inputStream;
	}

	public String getFormattedDate(Date date) {
		return Constants.DDMMYYYYFORMAT2.format(date);
	}
	
	public Statement getReceiptPayment() {
		return receiptPayment;
	}
	public ReportHelper getReportHelper() {
		return reportHelper;
	}
	
	public RPService getRpService() {
		return rpService;
	}
	public String getScheduleNo() {
		return scheduleNo;
	}
	public Date getTodayDate() {
		return todayDate;
	}
	
	public String getUlbName() {
		Query query = HibernateUtil.getCurrentSession().createSQLQuery("select name from companydetail");
		List<String> result = query.list();
		if (result != null)
			return result.get(0);
		return "";
	}
	
	

}
