package org.egov.web.actions.report;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.Bankaccount;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.Vouchermis;
import org.egov.egf.commons.EgovCommon;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfig;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.payment.Paymentheader;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.utils.ReportHelper;
import org.egov.web.actions.BaseFormAction;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
@Results(value={
		@Result(name="PDF",type="stream",location="inputStream", params={"inputName","inputStream","contentType","application/pdf","contentDisposition","no-cache;filename=BankBookReport.pdf"}),
		@Result(name="XLS",type="stream",location="inputStream", params={"inputName","inputStream","contentType","application/xls","contentDisposition","no-cache;filename=BankBookReport.xls"})
	})
	

@ParentPackage("egov")
public class BankBookReportAction extends BaseFormAction{
	private static final String EMPTY_STRING = "";
	private static final String PAYMENT = "Payment";
	private static final String RECEIPT = "Receipt";
	private static final String SURRENDERED = "Surrendered";
	private static final Logger	LOGGER	= Logger.getLogger(BankBookReportAction.class);
	String jasperpath = "/org/egov/web/actions/report/bankBookReport.jasper";
	List<Paymentheader> paymentHeaderList = new ArrayList<Paymentheader>();
	private List<BankBookEntry> bankBookEntries = new ArrayList<BankBookEntry>();
	private List<BankBookViewEntry> bankBookViewEntries = new ArrayList<BankBookViewEntry>();
	private Date startDate = new Date();
	private Date endDate = new Date();
	private BigDecimal bankBalance = BigDecimal.ZERO;
	private Bankaccount bankAccount;
	private InputStream inputStream;
	ReportHelper reportHelper;
	private EgovCommon egovCommon;
	protected List<String> headerFields = new ArrayList<String>();
	protected List<String> mandatoryFields = new ArrayList<String>();
	private Fund fundId = new Fund();
	private Vouchermis vouchermis = new Vouchermis();
	private Long voucherId;
	private Long chkforInstrumentNo;
	private List<InstrumentHeader> chequeDetails = new ArrayList<InstrumentHeader>();
	private String chequeStatus =EMPTY_STRING;
	private String voucherStr="";
	private StringBuffer header=new StringBuffer();
	private Date todayDate;
 	private GenericHibernateDaoFactory genericDao;
	private SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
	private List<String> voucherNo=new ArrayList<String>();
	private boolean isCreditOpeningBalance=false;
	private String queryFrom = "";
	private String getInstrumentsByVoucherIdsQuery = "";
	private Map<Long,List<Object[]>> voucherIdAndInstrumentMap = new HashMap<Long,List<Object[]>>();
	private Map<Long,List<Object[]>> InstrumentHeaderIdsAndInstrumentVouchersMap = new HashMap<Long,List<Object[]>>();
	public void setReportHelper(ReportHelper reportHelper) {
		this.reportHelper = reportHelper;
	}

	@Override
	public String execute() throws Exception {
		return "form";
	}
	
	public BankBookReportAction(){
		addRelatedEntity("vouchermis.departmentid", Department.class);
		addRelatedEntity("vouchermis.fundId", Fund.class);
		addRelatedEntity("vouchermis.schemeid", Scheme.class);
		addRelatedEntity("vouchermis.subschemeid", SubScheme.class);
		addRelatedEntity("vouchermis.functionary", Functionary.class);
		addRelatedEntity("vouchermis.fundsource", Fundsource.class);
		addRelatedEntity("vouchermis.divisionid", Boundary.class);
	}

	@Override
	public void prepare() {
	HibernateUtil.getCurrentSession().setDefaultReadOnly(true);
	HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
		super.prepare();
		if(!parameters.containsKey("skipPrepare")){
			EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
			addDropdownData("bankList", egovCommon.getBankBranchForActiveBanks());
			addDropdownData("accNumList", Collections.EMPTY_LIST);

			getHeaderFields();
			if(headerFields.contains(Constants.DEPARTMENT)){
				addDropdownData("departmentList", persistenceService.findAllBy("from Department order by deptName"));
			}
			if(headerFields.contains(Constants.FUNCTION)){
				addDropdownData("functionList", persistenceService.findAllBy("from CFunction where isactive=1 and isnotleaf=0  order by name"));
			}
			if(headerFields.contains(Constants.FUNCTIONARY)){
				addDropdownData("functionaryList", persistenceService.findAllBy(" from Functionary where isactive=1 order by name"));
			}
			if(headerFields.contains(Constants.FUND)){
				addDropdownData("fundList", persistenceService.findAllBy(" from Fund where isactive=1 and isnotleaf=0 order by name"));
			}
			if(headerFields.contains(Constants.FUNDSOURCE)){
				addDropdownData("fundsourceList", persistenceService.findAllBy(" from Fundsource where isactive=1 and isnotleaf=0 order by name"));
			}
			if(headerFields.contains(Constants.FIELD)){
				addDropdownData("fieldList", persistenceService.findAllBy(" from Boundary b where lower(b.boundaryType.name)='ward' "));
			}
			if(headerFields.contains(Constants.SCHEME)){
				addDropdownData("schemeList",  Collections.EMPTY_LIST );
			}
			if(headerFields.contains(Constants.SUBSCHEME)){
				addDropdownData("subschemeList", Collections.EMPTY_LIST);
			}
		}
	}
	
	protected void getHeaderFields(){
		List<AppConfig> appConfigList = (List<AppConfig>) persistenceService.findAllBy("from AppConfig where key_name = 'REPORT_SEARCH_MISATTRRIBUTES'");
		for (AppConfig appConfig : appConfigList){
			for (AppConfigValues appConfigVal : appConfig.getAppDataValues()){
			
				String value = appConfigVal.getValue();
				String header=value.substring(0, value.indexOf('|'));
				headerFields.add(header);
				String mandate = value.substring(value.indexOf('|')+1);
				if(mandate.equalsIgnoreCase("M")){
					mandatoryFields.add(header);
				}
			}
		}
	}
	public boolean shouldShowHeaderField(String fieldName){
		return headerFields.contains(fieldName);
	}
	
@Action(value="/report/bankBookReport-ajaxLoadBankBook")
	public String ajaxLoadBankBook(){
		if(parameters.containsKey("bankAccount.id") && parameters.get("bankAccount.id")[0]!=null){
			startDate = parseDate("startDate");
			endDate = parseDate("endDate");
			setTodayDate(new Date());
			bankAccount = (Bankaccount) persistenceService.find("from Bankaccount where id=?",Integer.valueOf(parameters.get("bankAccount.id")[0]));
			List<BankBookEntry> results = getResults(bankAccount.getChartofaccounts().getGlcode());
			Map<String,BankBookEntry> voucherNumberAndEntryMap = new HashMap<String,BankBookEntry>();
			List<String> multipleChequeVoucherNumber = new ArrayList<String>();
			List<BankBookEntry> rowsToBeRemoved = new ArrayList<BankBookEntry>();
			for (BankBookEntry row : results) {
				if(row.getType().equalsIgnoreCase(RECEIPT)){
					row.setType(RECEIPT);
				}else{
					row.setType(PAYMENT);
				}
				boolean shouldAddRow = true;
				if(voucherNumberAndEntryMap.containsKey(row.getVoucherNumber())){
					if(SURRENDERED.equalsIgnoreCase(row.getInstrumentStatus())||  FinancialConstants.INSTRUMENT_SURRENDERED_FOR_REASSIGN_STATUS.equalsIgnoreCase(row.getInstrumentStatus())){
						shouldAddRow = false;
					}else{
						BankBookEntry entryInMap = voucherNumberAndEntryMap.get(row.getVoucherNumber());
						if((SURRENDERED.equalsIgnoreCase(entryInMap.getInstrumentStatus())|| FinancialConstants.INSTRUMENT_SURRENDERED_FOR_REASSIGN_STATUS.equalsIgnoreCase(entryInMap.getInstrumentStatus())) &&( !SURRENDERED.equalsIgnoreCase(row.getInstrumentStatus())||!FinancialConstants.INSTRUMENT_SURRENDERED_FOR_REASSIGN_STATUS.equalsIgnoreCase(row.getInstrumentStatus()))){
							rowsToBeRemoved.add(entryInMap);
							voucherNumberAndEntryMap.put(row.getVoucherNumber(), row);
						}else{
							if(row.getVoucherDate().compareTo(entryInMap.getVoucherDate())==0 && row.getParticulars().equalsIgnoreCase(entryInMap.getParticulars()) 
									&& row.getAmount().equals(entryInMap.getAmount()) && !SURRENDERED.equalsIgnoreCase(entryInMap.getInstrumentStatus())){
								multipleChequeVoucherNumber.add(row.getVoucherNumber());
								shouldAddRow = false; 
							}
							else{ shouldAddRow = true; } //this is to handle multiple Glcodes  for a single payment voucher (different bills single payment)
						}
					}
				}
				else{
					voucherNumberAndEntryMap.put(row.getVoucherNumber(), row); 
				}
				if(shouldAddRow){
					bankBookEntries.add(row);
				}
			}
			if(!bankBookEntries.isEmpty()){
				computeTotals(bankAccount.getChartofaccounts().getGlcode(),bankAccount.getFund().getCode(),multipleChequeVoucherNumber,rowsToBeRemoved);
				prepareViewObject();
			}
		}
		return "results";
	}
	
	private void prepareViewObject() {
		for (BankBookEntry row : bankBookEntries) {
			BankBookViewEntry bankBookViewEntry = new BankBookViewEntry();
			if("Total".equalsIgnoreCase(row.getParticulars())){
				bankBookViewEntry.setReceiptAmount(row.getReceiptAmount());
				bankBookViewEntry.setReceiptParticulars(row.getParticulars());
				bankBookViewEntry.setPaymentAmount(row.getReceiptAmount());
				bankBookViewEntry.setPaymentParticulars(row.getParticulars());
			}else if("To Opening Balance".equalsIgnoreCase(row.getParticulars())){
				BigDecimal amt=row.getAmount();
				if(amt.longValue()<0)
				{
					bankBookViewEntry.setPaymentAmount(amt.abs());
					bankBookViewEntry.setPaymentParticulars(row.getParticulars());
				}
				else
				{
					bankBookViewEntry.setReceiptAmount(amt.abs());
					bankBookViewEntry.setReceiptParticulars(row.getParticulars());
				}
			}else if("Closing:By Balance c/d".equalsIgnoreCase(row.getParticulars())){
				BigDecimal amt=row.getAmount();
				if(amt.longValue()<0)
				{
					bankBookViewEntry.setReceiptAmount(amt.abs());
					bankBookViewEntry.setReceiptParticulars(row.getParticulars());					
				}
				else
				{
					bankBookViewEntry.setPaymentAmount(amt.abs());
					bankBookViewEntry.setPaymentParticulars(row.getParticulars());
				}
				
			} else {
				String voucherDate = row.getVoucherDate()==null?"":Constants.DDMMYYYYFORMAT2.format(row.getVoucherDate());
				if(row.getType().equalsIgnoreCase(RECEIPT)){
					bankBookViewEntry = new BankBookViewEntry(row.getVoucherNumber(),voucherDate,row.getParticulars(),row.getAmount(),row.getChequeDetail(),RECEIPT,row.getChequeNumber());
					bankBookViewEntry.setVoucherId(row.getVoucherId().longValue());
				}else{
					bankBookViewEntry = new BankBookViewEntry(row.getVoucherNumber(),voucherDate,row.getParticulars(),row.getAmount(),row.getChequeDetail(),PAYMENT,row.getChequeNumber());
					bankBookViewEntry.setVoucherId(row.getVoucherId().longValue());
				}
			}
		    bankBookViewEntries.add(bankBookViewEntry);
		}
	}

	private void computeTotals(String glCode,String fundCode,List<String> multipleChequeVoucherNumber,List<BankBookEntry> rowsToBeRemoved) {
		List<BankBookEntry> entries = new ArrayList<BankBookEntry>();
		getInstrumentsByVoucherIds();
		getInstrumentVouchersByInstrumentHeaderIds();
		Integer deptId=null;
		if(getVouchermis()!=null && getVouchermis().getDepartmentid()!=null && getVouchermis().getDepartmentid().getId()!=null && getVouchermis().getDepartmentid().getId()!=-1){
			deptId=getVouchermis().getDepartmentid().getId();
		}
		BankBookEntry initialOpeningBalance = getInitialAccountBalance(glCode,fundCode,deptId);
		entries.add(initialOpeningBalance);
		Date date = bankBookEntries.get(0).getVoucherDate();
		String voucherNumber = EMPTY_STRING;
		String chequeNumber="";
		BigDecimal receiptTotal = BigDecimal.ZERO;
		BigDecimal paymentTotal = BigDecimal.ZERO;
		BigDecimal initialBalance = initialOpeningBalance.getAmount();
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside computeTotals()");
		for (BankBookEntry bankBookEntry : bankBookEntries) {
			if(initialBalance.longValue()<0)
			{
				isCreditOpeningBalance=true;
			}
			if(!rowsToBeRemoved.contains(bankBookEntry)){// for a voucher there could be multiple surrendered cheques associated with it. remove the dupicate rows
				if(bankBookEntry.voucherDate.compareTo(date)!=0){
					date = bankBookEntry.getVoucherDate();
					BigDecimal closingBalance = initialBalance.add(receiptTotal).subtract(paymentTotal);
					if(closingBalance.longValue()<0)
					{
						entries.add(new BankBookEntry("Closing:By Balance c/d",closingBalance,PAYMENT,BigDecimal.ZERO,BigDecimal.ZERO));
						if(isCreditOpeningBalance)
							entries.add(new BankBookEntry("Total",BigDecimal.ZERO,RECEIPT,closingBalance.abs().add(receiptTotal),initialBalance.abs().add(paymentTotal)));
						else
						{
							entries.add(new BankBookEntry("Total",BigDecimal.ZERO,RECEIPT,initialBalance.abs().add(receiptTotal).add(closingBalance.abs()),paymentTotal));
						}
						entries.add(new BankBookEntry("To Opening Balance",closingBalance,RECEIPT,BigDecimal.ZERO,BigDecimal.ZERO));
					}
					else
					{
						entries.add(new BankBookEntry("Closing:By Balance c/d",closingBalance,RECEIPT,BigDecimal.ZERO,BigDecimal.ZERO));
						if(isCreditOpeningBalance)
							entries.add(new BankBookEntry("Total",BigDecimal.ZERO,RECEIPT,receiptTotal,closingBalance.abs().add(paymentTotal).add(initialBalance.abs())));
						else
							entries.add(new BankBookEntry("Total",BigDecimal.ZERO,RECEIPT,initialBalance.abs().add(receiptTotal),closingBalance.abs().add(paymentTotal)));
						entries.add(new BankBookEntry("To Opening Balance",closingBalance,PAYMENT,BigDecimal.ZERO,BigDecimal.ZERO));
					}
					
					receiptTotal = BigDecimal.ZERO;
					paymentTotal = BigDecimal.ZERO;
					initialBalance = closingBalance;
					isCreditOpeningBalance=false;
				}
				if(RECEIPT.equalsIgnoreCase(bankBookEntry.getType()) && !voucherNumber.equalsIgnoreCase(bankBookEntry.getVoucherNumber())){
					receiptTotal = receiptTotal.add(bankBookEntry.getAmount());
				}else if(!voucherNumber.equalsIgnoreCase(bankBookEntry.getVoucherNumber())){
					
					paymentTotal = paymentTotal.add(bankBookEntry.getAmount());
				}
				if(SURRENDERED.equalsIgnoreCase(bankBookEntry.getInstrumentStatus())){
					bankBookEntry.setChequeDetail(EMPTY_STRING);
				}
				if(multipleChequeVoucherNumber.contains(bankBookEntry.getVoucherNumber())){
					bankBookEntry.setChequeDetail("MULTIPLE");//Set the cheque details to MULTIPLE if the voucher has multiple cheques assigned to it
					List<Object[]> chequeDetails = voucherIdAndInstrumentMap.get(bankBookEntry.getVoucherId().longValue());
					StringBuffer listofcheque=new StringBuffer(100);
					String chequeNos=" ";
					String chequeComp=" ";
					
					if(!voucherNo.contains(bankBookEntry.getVoucherNumber())){
					for(Object[] iv:chequeDetails){
						chequeNumber=getStringValue(iv[1]);
						chequeStatus=" ";
						 chequeStatus=getStringValue(iv[2]);
						if(!(SURRENDERED.equalsIgnoreCase(chequeStatus) || FinancialConstants.INSTRUMENT_SURRENDERED_FOR_REASSIGN_STATUS.equalsIgnoreCase(chequeStatus))) {
							
						if(isInstrumentMultiVoucherMapped(getLongValue(iv[3]))){
							String chqDate=sdf.format(getDateValue(iv[4]));
							chequeComp= chequeNumber+" " +chqDate+"-MULTIPLE";
						 }
						
						listofcheque.append(getStringValue(iv[1])).append(" ").append(getDateValue(iv[4])!=null?sdf.format(getDateValue(iv[4])):"");
						//String chqDate=sdf.format(iv.getInstrumentHeaderId().getInstrumentDate());
					    if(chequeComp.contains("-MULTIPLE"))
						{   listofcheque.append(" ").append("-MULTIPLE,");                  
							chequeComp=" "; }  
					   	else
							listofcheque.append(" ").append(",");						
					}	
				}
				chequeNos=listofcheque.toString();
				if(chequeNos.length()>1){
					chequeNos=chequeNos.substring(0, chequeNos.length()-1); }
					
				bankBookEntry.setChequeNumber(chequeNos);
				voucherNumber = bankBookEntry.getVoucherNumber();	
				entries.add(bankBookEntry);
				voucherNo.add(bankBookEntry.getVoucherNumber());
			}
			}
				else{
				voucherStr=" ";
				List<Object[]> instrumentVoucherList = new ArrayList<Object[]>();
				instrumentVoucherList = voucherIdAndInstrumentMap.get(bankBookEntry.getVoucherId().longValue());
			if(instrumentVoucherList!=null){
				for (Object[] instrumentVoucher : instrumentVoucherList) {
					try{
						chequeNumber= getStringValue(instrumentVoucher[1]);             
						chequeStatus=" ";
						chequeStatus=getStringValue(instrumentVoucher[2]);
						if(!(SURRENDERED.equalsIgnoreCase(chequeStatus) || FinancialConstants.INSTRUMENT_SURRENDERED_FOR_REASSIGN_STATUS.equalsIgnoreCase(chequeStatus))) {
							if(isInstrumentMultiVoucherMapped(getLongValue(instrumentVoucher[3]))){
								if(chequeNumber!=null && !chequeNumber.equalsIgnoreCase("")){
								String chqDate=getDateValue(instrumentVoucher[4])!=null?sdf.format(getDateValue(instrumentVoucher[4])):"";
								voucherStr= chequeNumber+" " +chqDate+"-MULTIPLE";
								}                      
								else{
									chequeNumber= getStringValue(instrumentVoucher[5]);
									String chqDate=getDateValue(instrumentVoucher[6])!=null?sdf.format(getDateValue(instrumentVoucher[6])):"";
									voucherStr= chequeNumber+" " +chqDate+"-MULTIPLE";
								}  
							}
							else
								   if(chequeNumber!=null && !chequeNumber.equalsIgnoreCase("")){
								   String chqDate=getDateValue(instrumentVoucher[4])!=null?sdf.format(getDateValue(instrumentVoucher[4])):"";
								   voucherStr= chequeNumber +" "+chqDate;
							}             
						 else{  //voucherStr=" "; 
							   chequeNumber= getStringValue(instrumentVoucher[5]);
								String chqDate=sdf.format(getDateValue(instrumentVoucher[6]));
								voucherStr= chequeNumber+" " +chqDate;
						 //  }  
						   }
						}
						
					}
					catch(NumberFormatException ex){System.out.println("Exception"+ex);}
				}
			}
				bankBookEntry.setChequeDetail(voucherStr);
				entries.add(bankBookEntry);
				voucherNo.add(bankBookEntry.getVoucherNumber());
				}
				voucherNumber = bankBookEntry.getVoucherNumber();
			}
			
		}
		String vhNum = EMPTY_STRING;
		for (BankBookEntry bankBookEntry : bankBookEntries) {
			if(bankBookEntry.voucherNumber.equalsIgnoreCase(vhNum)){ //this is to handle multiple debits or credits for a single voucher.
				bankBookEntry.setVoucherDate(null);
				bankBookEntry.setAmount(null);
				bankBookEntry.setVoucherNumber(EMPTY_STRING);
			}else{
				
				vhNum  = bankBookEntry.getVoucherNumber();
			}
		}
		//adding total,closing and opening balance to the last group
		addTotalsSection(initialBalance,paymentTotal,receiptTotal,entries);
		bankBookEntries = entries;
		if(LOGGER.isDebugEnabled())     LOGGER.debug("End of computeTotals()");
	}
	
	private void getInstrumentsByVoucherIds(){
		String mainQuery = "";
	 mainQuery = "SELECT vh1.id,ih.instrumentnumber,es1.code,ih.id,ih.instrumentdate, ih.transactionnumber, ih.transactiondate";
	 getInstrumentsByVoucherIdsQuery = " FROM VOUCHERHEADER vh1,egf_instrumentvoucher iv ,egf_instrumentheader ih ,egw_status es1 WHERE vh1.id = iv.voucherheaderid AND iv.instrumentheaderid=ih.id" +
				" AND ih.id_status = es1.id AND vh1.id in (select vh.id "+queryFrom+")";
	 mainQuery = mainQuery + getInstrumentsByVoucherIdsQuery;
				
		List<Object[]> objs = HibernateUtil.getCurrentSession().createSQLQuery(mainQuery).list();
		
		for(Object[] obj:objs){
			if(voucherIdAndInstrumentMap.containsKey(getLongValue(obj[0]))){
				voucherIdAndInstrumentMap.get(getLongValue(obj[0])).add(obj);
			}else{
				List<Object[]> instrumentVouchers =new ArrayList<Object[]>();
				instrumentVouchers.add(obj);
				voucherIdAndInstrumentMap.put(getLongValue(obj[0]),instrumentVouchers);
			}
			
		}
	}
	private void getInstrumentVouchersByInstrumentHeaderIds(){
		
		List<Object[]> objs = HibernateUtil.getCurrentSession().createSQLQuery("SELECT ih.id,vh1.id" +
				" FROM VOUCHERHEADER vh1,egf_instrumentvoucher iv ,egf_instrumentheader ih ,egw_status es1 WHERE vh1.id = iv.voucherheaderid AND iv.instrumentheaderid=ih.id" +
				" AND ih.id_status = es1.id AND ih.id in (select ih.id "+getInstrumentsByVoucherIdsQuery+")").list();
		
		for(Object[] obj:objs){
			if(InstrumentHeaderIdsAndInstrumentVouchersMap.containsKey(getLongValue(obj[0]))){
				InstrumentHeaderIdsAndInstrumentVouchersMap.get(getLongValue(obj[0])).add(obj);
			}else{
				List<Object[]> instrumentVouchers =new ArrayList<Object[]>();
				instrumentVouchers.add(obj);
				InstrumentHeaderIdsAndInstrumentVouchersMap.put(getLongValue(obj[0]),instrumentVouchers);
			}
			
		}
	}
	
	private boolean isInstrumentMultiVoucherMapped(Long instrumentHeaderId) {
		List<Object[]> instrumentVoucherList = InstrumentHeaderIdsAndInstrumentVouchersMap.get(instrumentHeaderId);
		boolean rep = false;
		if (instrumentVoucherList != null && instrumentVoucherList.size() != 0) {
			
			Object[] obj = instrumentVoucherList.get(0);
			Long voucherId = getLongValue(obj[1]);
			for (Object[] instrumentVoucher : instrumentVoucherList) {
				if (voucherId != getLongValue(instrumentVoucher[1])) 
				{
					rep = true;
					break;
				}
			}
		}
		return rep;
	}

	

	private BankBookEntry getInitialAccountBalance(String glCode,String fundCode,Integer deptId) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
		BankBookEntry initialOpeningBalance = new BankBookEntry("To Opening Balance",egovCommon.getAccountBalanceforDate(calendar.getTime(), glCode, fundCode,null,null, deptId),RECEIPT,BigDecimal.ZERO,BigDecimal.ZERO);
		return initialOpeningBalance;
	}

	private void addTotalsSection(BigDecimal initialBalance,BigDecimal paymentTotal,BigDecimal receiptTotal,List<BankBookEntry> entries){
		BigDecimal closingBalance = initialBalance.add(receiptTotal).subtract(paymentTotal);
		entries.add(new BankBookEntry("Closing:By Balance c/d",closingBalance,PAYMENT,BigDecimal.ZERO,BigDecimal.ZERO));
		//Obtain the total accordingly. Similar to how it is done in computeTotals().
		if(initialBalance.longValue()<0)
		{
			isCreditOpeningBalance=true;
		}
		if(closingBalance.longValue()<0)
		{
			if(isCreditOpeningBalance)
				entries.add(new BankBookEntry("Total",BigDecimal.ZERO,RECEIPT,closingBalance.abs().add(receiptTotal),initialBalance.abs().add(paymentTotal)));
			else
			{
				entries.add(new BankBookEntry("Total",BigDecimal.ZERO,RECEIPT,initialBalance.abs().add(receiptTotal).add(closingBalance.abs()),paymentTotal));
			}
		}
		else
		{
			if(isCreditOpeningBalance)
				entries.add(new BankBookEntry("Total",BigDecimal.ZERO,RECEIPT,receiptTotal,closingBalance.abs().add(paymentTotal).add(initialBalance.abs())));
			else
				entries.add(new BankBookEntry("Total",BigDecimal.ZERO,RECEIPT,initialBalance.abs().add(receiptTotal),closingBalance.abs().add(paymentTotal)));
		}
		isCreditOpeningBalance=false;
		receiptTotal = BigDecimal.ZERO;
		paymentTotal = BigDecimal.ZERO;
		initialBalance = closingBalance;
	}

	private String getAppConfigValueFor(String module,String key){
		try {
			return genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(module,key).get(0).getValue();
		} catch (Exception e) {
			throw new ValidationException(EMPTY_STRING,"The key '"+key+"' is not defined in appconfig");
		}
	}
	
	private List<BankBookEntry> getResults(String glCode1){
		String miscQuery = getMiscQuery();
		String OrderBy = "";
		String voucherStatusToExclude = getAppConfigValueFor("finance", "statusexcludeReport");
		String query1 = "SELECT distinct vh.id as voucherId,vh.voucherDate AS voucherDate, vh.voucherNumber AS voucherNumber," +
				" gl.glcode||' - '||decode(gl.debitAmount,0,(case (gl.creditamount) when 0 then gl.creditAmount||'.00cr' when floor(gl.creditamount) then gl.creditAmount||'.00cr' else  gl.creditAmount||'cr'  end ) , (case (gl.debitamount) when 0 then gl.debitamount||'.00dr' when floor(gl.debitamount)  then gl.debitamount||'.00dr' else  gl.debitamount||'dr' 	 end ))" +
				" AS particulars,decode(gl1.debitAmount,0,gl1.creditamount,gl1.debitAmount) AS amount, DECODE(gl1.debitAmount,0,'Payment','Receipt') AS type," +
				" DECODE (DECODE(ch.instrumentnumber,NULL,ch.transactionnumber,ch.instrumentnumber) ||' , ' ||TO_CHAR(DECODE(ch.instrumentdate,NULL,ch.transactiondate,ch.instrumentdate),'dd/mm/yyyy'), ' , ' ,NULL,DECODE(ch.instrumentnumber,NULL,ch.transactionnumber,ch.instrumentnumber) ||' , ' ||TO_CHAR(DECODE(ch.instrumentdate,NULL,ch.transactiondate,ch.instrumentdate),'dd/mm/yyyy'))" +
				" AS chequeDetail,gl.glcode as glCode,ch.description as instrumentStatus  " ;
		 	queryFrom = " FROM generalLedger gl,generalLedger gl1" +
				",vouchermis vmis, VOUCHERHEADER vh left outer join (select iv.voucherheaderid,ih.instrumentnumber,ih.instrumentdate," +
				"es.description,ih.transactionnumber,ih.transactiondate from egf_instrumentheader ih,egw_status es,egf_instrumentvoucher iv where iv.instrumentheaderid=ih.id and " +
				"ih.id_status=es.id) ch on ch.voucherheaderid=vh.id  WHERE  gl.voucherHeaderId = vh.id  AND vmis.VOUCHERHEADERID=vh.id  " +
				"and gl.voucherheaderid  IN (SELECT voucherheaderid FROM generalledger gl WHERE glcode='"+glCode1+"') AND gl.voucherheaderid = gl1.voucherheaderid AND gl.glcode! = '"+glCode1+"' AND gl1.glcode = '"+glCode1+"' and vh.voucherDate>='"+Constants.DDMMYYYYFORMAT1.format(startDate)+"' " +
				"and vh.voucherDate<='"+Constants.DDMMYYYYFORMAT1.format(endDate)+"' and vh.status not in("+voucherStatusToExclude+") "+miscQuery+" " ;
		 	OrderBy = "order by voucherdate,vouchernumber";
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Main query :"+query1 + queryFrom+OrderBy);
		
		Query query = HibernateUtil.getCurrentSession().createSQLQuery(query1+queryFrom+OrderBy)
						.addScalar("voucherId")
						.addScalar("voucherDate")
						.addScalar("voucherNumber")
						.addScalar("particulars")
						.addScalar("amount")
						.addScalar("type")
						.addScalar("chequeDetail")
						.addScalar("glCode")
						.addScalar("instrumentStatus")
						.setResultTransformer(Transformers.aliasToBean(BankBookEntry.class));
		List<BankBookEntry> results = query.list();
		return results;
    }

	String getMiscQuery() {
		StringBuffer query = new StringBuffer();
			
		if(fundId!=null && fundId.getId()!=null && fundId.getId()!=-1){
			query.append(" and vh.fundId=").append(fundId.getId().toString());
			Fund fnd=(Fund) persistenceService.find("from Fund where id=?",fundId.getId());
			header.append(" for "+fnd.getName());
		}
		
		if(getVouchermis()!=null && getVouchermis().getDepartmentid()!=null && getVouchermis().getDepartmentid().getId()!=null && getVouchermis().getDepartmentid().getId()!=-1){
			query.append(" and vmis.DEPARTMENTID=").append(getVouchermis().getDepartmentid().getId().toString());
			Department dept=(Department) persistenceService.find("from Department where id=?", getVouchermis().getDepartmentid().getId());
			header.append(" in "+dept.getDeptName()+" ");
		}
		if(getVouchermis()!=null && getVouchermis().getFunctionary()!=null && getVouchermis().getFunctionary().getId()!=null && getVouchermis().getFunctionary().getId()!=-1){
			query.append(" and vmis.FUNCTIONARYID=").append(getVouchermis().getFunctionary().getId().toString());
			
		}
		if(getVouchermis()!=null && getVouchermis().getFundsource()!=null && getVouchermis().getFundsource().getId()!=null && getVouchermis().getFundsource().getId()!=-1){
			query.append(" and vmis.FUNDSOURCEID =").append(getVouchermis().getFundsource().getId().toString());
			
		}
		if(getVouchermis()!=null && getVouchermis().getSchemeid()!=null && getVouchermis().getSchemeid().getId()!=null && getVouchermis().getSchemeid().getId()!=-1){
			query.append(" and vmis.SCHEMEID =").append(getVouchermis().getSchemeid().getId().toString());
		}
		if(getVouchermis()!=null && getVouchermis().getSubschemeid()!=null && getVouchermis().getSubschemeid().getId()!=null && getVouchermis().getSubschemeid().getId()!=-1){
			query.append(" and vmis.SUBSCHEMEID =").append(getVouchermis().getSubschemeid().getId().toString());
		}
		if(getVouchermis()!=null && getVouchermis().getDivisionid()!=null && getVouchermis().getDivisionid().getId()!=null && getVouchermis().getDivisionid().getId()!=-1){
			query.append(" and vmis.DIVISIONID =").append(getVouchermis().getDivisionid().getId().toString());
		}
		return query.toString();
	}

	public String getUlbName(){
		Query query = HibernateUtil.getCurrentSession().createSQLQuery("select name from companydetail");
		List<String> result = query.list();
		if(result!=null)
			return result.get(0);
		return EMPTY_STRING;
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
	private Integer getIntegerValue(Object object) {
		return object!= null? new Integer(object.toString()):0;
	}
	private String getStringValue(Object object) {
		return object != null?object.toString():"";
	}
	private Date getDateValue(Object object) {
		return object != null?(Date)object:null;
	}
	private Long getLongValue(Object object) {
		return object!= null? new Long(object.toString()):0;
	}

	public BigDecimal getBankBalance() {
		return bankBalance;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getStartDate() {
		return startDate;
	}
	
	public String getFormattedDate(Date date){
		return Constants.DDMMYYYYFORMAT2.format(date);
	}

	public void setBankBalance(BigDecimal bankBalance) {
		this.bankBalance = bankBalance;
	}

	public void setBankAccount(Bankaccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	public Bankaccount getBankAccount() {
		return bankAccount;
	}
	
@Action(value="/report/bankBookReport-exportPdf")
	public String exportPdf() throws JRException, IOException{
		ajaxLoadBankBook();
		List<Object> dataSource = new ArrayList<Object>();
		for (BankBookViewEntry row : bankBookViewEntries) {
			dataSource.add(row);
		}
		setInputStream(reportHelper.exportPdf(getInputStream(), jasperpath, getParamMap(), dataSource));
	    return "PDF";
	}
	
@Action(value="/report/bankBookReport-exportXls")
	public String exportXls() throws JRException, IOException{
		ajaxLoadBankBook();
		List<Object> dataSource = new ArrayList<Object>();
		for (BankBookViewEntry row : bankBookViewEntries) {
			dataSource.add(row);
		}
		setInputStream(reportHelper.exportXls(getInputStream(), jasperpath, getParamMap(), dataSource));
	    return "XLS";
	}
	
	Map<String, Object> getParamMap() {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("ulbName", getUlbName());
		String name = bankAccount.getBankbranch().getBank().getName().concat("-")
					.concat(bankAccount.getBankbranch().getBranchname()).concat("-")
					.concat(bankAccount.getAccountnumber());
		paramMap.put("heading", getText("bank.book.heading",new String[]{name,header.toString(),Constants.DDMMYYYYFORMAT2.format(startDate),Constants.DDMMYYYYFORMAT2.format(endDate)}));
	//	paramMap.put("today", Constants.DDMMYYYYFORMAT2.format(new Date()));
		return paramMap;
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

	public void setBankBookViewEntries(List<BankBookViewEntry> bankBookViewEntries) {
		this.bankBookViewEntries = bankBookViewEntries;
	}

	public List<BankBookViewEntry> getBankBookViewEntries() {
		return bankBookViewEntries;
	}

	public void setFundId(Fund fundId) {
		this.fundId = fundId;
	}

	public Fund getFundId() {
		return fundId;
	}
	
	public Vouchermis getVouchermis(){
		return vouchermis;
	}

	@Override
	public Object getModel() {
		return null;
	}

	public void setVoucherId(Long voucherId) {
		this.voucherId = voucherId;
	}

	public Long getVoucherId() {
		return voucherId;
	}
	
@Action(value="/report/bankBookReport-showChequeDetails")
	public String showChequeDetails(){
		if(voucherId!=null){
			chequeDetails = persistenceService.findAllBy("select iv.instrumentHeaderId from InstrumentVoucher iv where iv.voucherHeaderId.id=?", voucherId);
		}
		return "chequeDetails";
	}

	public void setChequeDetails(List<InstrumentHeader> chequeDetails) {
		this.chequeDetails = chequeDetails;
	}

	public List<InstrumentHeader> getChequeDetails() {
		return chequeDetails;
	}

	public void setGenericDao(GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}

	public void setVouchermis(Vouchermis vouchermis) {
		this.vouchermis = vouchermis;
	}

	public StringBuffer getHeader() {
		return header;
	}

	public void setHeader(StringBuffer header) {
		this.header = header;
	}
	public Date getTodayDate() {
		return todayDate;
	}
	private void setTodayDate(Date todayDate) {
		this.todayDate = todayDate;
	}

	public String getQueryFrom() {
		return queryFrom;
	}

	public void setQueryFrom(String queryFrom) {
		this.queryFrom = queryFrom;
	}

	public String getGetInstrumentsByVoucherIdsQuery() {
		return getInstrumentsByVoucherIdsQuery;
	}

	public void setGetInstrumentsByVoucherIdsQuery(
			String getInstrumentsByVoucherIdsQuery) {
		this.getInstrumentsByVoucherIdsQuery = getInstrumentsByVoucherIdsQuery;
	}

	public Map<Long, List<Object[]>> getVoucherIdAndInstrumentMap() {
		return voucherIdAndInstrumentMap;
	}

	public void setVoucherIdAndInstrumentMap(
			Map<Long, List<Object[]>> voucherIdAndInstrumentMap) {
		this.voucherIdAndInstrumentMap = voucherIdAndInstrumentMap;
	}

	public Map<Long, List<Object[]>> getInstrumentHeaderIdsAndInstrumentVouchersMap() {
		return InstrumentHeaderIdsAndInstrumentVouchersMap;
	}

	public void setInstrumentHeaderIdsAndInstrumentVouchersMap(
			Map<Long, List<Object[]>> instrumentHeaderIdsAndInstrumentVouchersMap) {
		InstrumentHeaderIdsAndInstrumentVouchersMap = instrumentHeaderIdsAndInstrumentVouchersMap;
	}
	
}
