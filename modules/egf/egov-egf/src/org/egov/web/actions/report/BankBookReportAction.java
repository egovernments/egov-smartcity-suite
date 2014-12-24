package org.egov.web.actions.report;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

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
import org.egov.infstr.ValidationException;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfig;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.model.instrument.InstrumentVoucher;
import org.egov.model.payment.Paymentheader;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.utils.ReportHelper;
import org.egov.web.actions.BaseFormAction;
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
	private List<InstrumentVoucher> chequeDetails = new ArrayList<InstrumentVoucher>();
	private GenericHibernateDaoFactory genericDao;

	public void setReportHelper(ReportHelper reportHelper) {
		this.reportHelper = reportHelper;
	}

	@Override
	public String execute() throws Exception {
		return "form";
	}
	
	public BankBookReportAction(){
		addRelatedEntity("vouchermis.departmentid", DepartmentImpl.class);
		addRelatedEntity("fundId", Fund.class);
		addRelatedEntity("vouchermis.schemeid", Scheme.class);
		addRelatedEntity("vouchermis.subschemeid", SubScheme.class);
		addRelatedEntity("vouchermis.functionary", Functionary.class);
		addRelatedEntity("vouchermis.fundsource", Fundsource.class);
		addRelatedEntity("vouchermis.divisionid", BoundaryImpl.class);
	}

	@Override
	public void prepare() {
		super.prepare();
		if(!parameters.containsKey("skipPrepare")){
			EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
			addDropdownData("bankList", egovCommon.getBankBranchForActiveBanks());
			addDropdownData("accNumList", Collections.EMPTY_LIST);
			addDropdownData("fundList",  masterCache.get("egi-fund"));
			getHeaderFields();
			if(headerFields.contains(Constants.DEPARTMENT)){
				addDropdownData("departmentList", persistenceService.findAllBy("from DepartmentImpl order by deptName"));
			}
			if(headerFields.contains(Constants.FUNCTION)){
				addDropdownData("functionList", persistenceService.findAllBy("from CFunction where isactive=1 and isnotleaf=0  order by name"));
			}
			if(headerFields.contains(Constants.FUNCTIONARY)){
				addDropdownData("functionaryList", persistenceService.findAllBy(" from Functionary where isactive='1' order by name"));
			}
			if(headerFields.contains(Constants.FUND)){
				addDropdownData("fundList", persistenceService.findAllBy(" from Fund where isactive='1' and isnotleaf='0' order by name"));
			}
			if(headerFields.contains(Constants.FUNDSOURCE)){
				addDropdownData("fundsourceList", persistenceService.findAllBy(" from Fundsource where isactive='1' and isnotleaf='0' order by name"));
			}
			if(headerFields.contains(Constants.FIELD)){
				addDropdownData("fieldList", persistenceService.findAllBy(" from BoundaryImpl b where lower(b.boundaryType.name)='ward' "));
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
	
	public String ajaxLoadBankBook(){
		if(parameters.containsKey("bankAccount.id") && parameters.get("bankAccount.id")[0]!=null){
			startDate = parseDate("startDate");
			endDate = parseDate("endDate");
			bankAccount = (Bankaccount) persistenceService.find("from Bankaccount where id=?",Integer.valueOf(parameters.get("bankAccount.id")[0]));
			List<BankBookEntry> results = getResults(bankAccount.getChartofaccounts().getGlcode());
			Map<String,BankBookEntry> voucherNumberAndEntryMap = new HashMap<String,BankBookEntry>();
			List<String> multipleChequeVoucherNumber = new ArrayList<String>();
			List<BankBookEntry> rowsToBeRemoved = new ArrayList<BankBookEntry>();
			for (BankBookEntry row : results) {
				if(BigDecimal.ZERO.equals(row.getCreditAmount())){
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
						}
					}
				}else{
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
				bankBookViewEntry.setReceiptAmount(row.getAmount());
				bankBookViewEntry.setReceiptParticulars(row.getParticulars());
			}else if("Closing:By Balance c/d".equalsIgnoreCase(row.getParticulars())){
				bankBookViewEntry.setPaymentAmount(row.getAmount());
				bankBookViewEntry.setPaymentParticulars(row.getParticulars());
			} else {
				String voucherDate = row.getVoucherDate()==null?"":Constants.DDMMYYYYFORMAT2.format(row.getVoucherDate());
				if(BigDecimal.ZERO.equals(row.getCreditAmount())){
					bankBookViewEntry = new BankBookViewEntry(row.getVoucherNumber(),voucherDate,row.getParticulars(),row.getAmount(),row.getChequeDetail(),RECEIPT);
					bankBookViewEntry.setVoucherId(row.getVoucherId().longValue());
				}else{
					bankBookViewEntry = new BankBookViewEntry(row.getVoucherNumber(),voucherDate,row.getParticulars(),row.getAmount(),row.getChequeDetail(),PAYMENT);
					bankBookViewEntry.setVoucherId(row.getVoucherId().longValue());
				}
			}
			bankBookViewEntries.add(bankBookViewEntry);
		}
	}

	private void computeTotals(String glCode,String fundCode,List<String> multipleChequeVoucherNumber,List<BankBookEntry> rowsToBeRemoved) {
		List<BankBookEntry> entries = new ArrayList<BankBookEntry>();
		BankBookEntry initialOpeningBalance = getInitialAccountBalance(glCode,fundCode);
		entries.add(initialOpeningBalance);
		Date date = bankBookEntries.get(0).getVoucherDate();
		String voucherNumber = EMPTY_STRING;
		BigDecimal receiptTotal = BigDecimal.ZERO;
		BigDecimal paymentTotal = BigDecimal.ZERO;
		BigDecimal initialBalance = initialOpeningBalance.getAmount();
		for (BankBookEntry bankBookEntry : bankBookEntries) {
			if(!rowsToBeRemoved.contains(bankBookEntry)){// for a voucher there could be multiple surrendered cheques associated with it. remove the dupicate rows
				if(bankBookEntry.voucherDate.compareTo(date)!=0){
					date = bankBookEntry.getVoucherDate();
					BigDecimal closingBalance = initialBalance.add(receiptTotal).subtract(paymentTotal);
					entries.add(new BankBookEntry("Closing:By Balance c/d",closingBalance,PAYMENT,BigDecimal.ZERO,BigDecimal.ZERO));
					entries.add(new BankBookEntry("Total",BigDecimal.ZERO,RECEIPT,initialBalance.add(receiptTotal),closingBalance.add(paymentTotal)));
					receiptTotal = BigDecimal.ZERO;
					paymentTotal = BigDecimal.ZERO;
					initialBalance = closingBalance;
					entries.add(new BankBookEntry("To Opening Balance",closingBalance,RECEIPT,BigDecimal.ZERO,BigDecimal.ZERO));
				}
				if(RECEIPT.equalsIgnoreCase(bankBookEntry.getType())){
					receiptTotal = receiptTotal.add(bankBookEntry.getAmount());
				}else{
					paymentTotal = paymentTotal.add(bankBookEntry.getAmount());
				}
				if(bankBookEntry.voucherNumber.equalsIgnoreCase(voucherNumber)){ //this is to handle multiple debits or credits for a single voucher. 
					bankBookEntry.setVoucherDate(null);
					bankBookEntry.setAmount(null);
					bankBookEntry.setVoucherNumber(EMPTY_STRING);
				}
				if(SURRENDERED.equalsIgnoreCase(bankBookEntry.getInstrumentStatus())){
					bankBookEntry.setChequeDetail(EMPTY_STRING);
				}
				if(multipleChequeVoucherNumber.contains(bankBookEntry.getVoucherNumber())){
					bankBookEntry.setChequeDetail("MULTIPLE");//Set the cheque details to MULTIPLE if the voucher has multiple cheques assigned to it
				}
				voucherNumber = bankBookEntry.getVoucherNumber();
				entries.add(bankBookEntry);
			}
		}
		//adding total,closing and opening balance to the last group
		addTotalsSection(initialBalance,paymentTotal,receiptTotal,entries);
		bankBookEntries = entries;
	}

	private BankBookEntry getInitialAccountBalance(String glCode,String fundCode) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)-1);
		BankBookEntry initialOpeningBalance = new BankBookEntry("To Opening Balance",egovCommon.getAccountBalanceforDate(calendar.getTime(), glCode, fundCode,null,null),RECEIPT,BigDecimal.ZERO,BigDecimal.ZERO);
		return initialOpeningBalance;
	}

	private void addTotalsSection(BigDecimal initialBalance,BigDecimal paymentTotal,BigDecimal receiptTotal,List<BankBookEntry> entries){
		BigDecimal closingBalance = initialBalance.add(receiptTotal).subtract(paymentTotal);
		entries.add(new BankBookEntry("Closing:By Balance c/d",closingBalance,PAYMENT,BigDecimal.ZERO,BigDecimal.ZERO));
		entries.add(new BankBookEntry("Total",BigDecimal.ZERO,RECEIPT,initialBalance.add(receiptTotal),closingBalance.add(paymentTotal)));
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
		String voucherStatusToExclude = getAppConfigValueFor("finance", "statusexcludeReport");
		String query1 = "SELECT distinct vh.id as voucherId,vh.voucherDate AS voucherDate, vh.voucherNumber AS voucherNumber," +
				" gl1.glcode||' - '||decode(gl1.debitAmount,0,(case (gl1.creditamount) when 0 then gl1.creditAmount||'.00cr' when floor(gl1.creditamount) then gl1.creditAmount||'.00cr' else  gl1.creditAmount||'cr'  end ) , (case (gl1.debitamount) when 0 then gl1.debitamount||'.00dr' when floor(gl1.debitamount)  then gl1.debitamount||'.00dr' else  gl1.debitamount||'dr' 	 end ))" +
				" AS particulars,floor(decode(gl.debitAmount,0,gl.creditamount,gl.debitAmount)) AS amount," +
				"gl.creditAmount as creditAmount,gl.debitAmount as debitAmount, DECODE (ch.instrumentnumber ||' , " +
				"'||TO_CHAR(ch.instrumentdate,'dd/mm/yyyy'), ' , ' ,NULL,ch.instrumentnumber ||' , '||TO_CHAR(ch.instrumentdate,'dd/mm/yyyy'))" +
				" AS chequeDetail,gl.glcode as glCode,ch.description as instrumentStatus  FROM generalLedger gl, chartOfAccounts coa, " +
				"generalLedger gl1 ,vouchermis vmis, VOUCHERHEADER vh left outer join (select voucherheaderid,instrumentnumber,instrumentdate," +
				"description from egf_instrumentheader ih,egw_status es,egf_instrumentvoucher iv where iv.instrumentheaderid=ih.id and " +
				"ih.id_status=es.id) ch on ch.voucherheaderid=vh.id  WHERE coa.glCode = gl1.glCode AND gl.voucherHeaderId = vh.id  AND vmis.VOUCHERHEADERID=vh.id and " +
				"gl.voucherHeaderId = gl1.voucherHeaderId AND gl.glcode="+glCode1+" and gl1.glcode!="+glCode1+" AND vh.voucherDate>='"+Constants.DDMMYYYYFORMAT1.format(startDate)+"' " +
				"and vh.voucherDate<='"+Constants.DDMMYYYYFORMAT1.format(endDate)+"' and vh.status not in("+voucherStatusToExclude+") "+miscQuery+" order by voucherdate,vouchernumber";  
		Query query = persistenceService.getSession().createSQLQuery(query1)
						.addScalar("voucherId")
						.addScalar("voucherDate")
						.addScalar("voucherNumber")
						.addScalar("particulars")
						.addScalar("amount")
						.addScalar("creditAmount")
						.addScalar("debitAmount")
						.addScalar("chequeDetail")
						.addScalar("glCode")
						.addScalar("instrumentStatus")
						.setResultTransformer(Transformers.aliasToBean(BankBookEntry.class));
		return query.list();
    }

	String getMiscQuery() {
		StringBuffer query = new StringBuffer();
		if(fundId!=null && fundId.getId()!=null && fundId.getId()!=-1){
			query.append(" and vh.fundId=").append(fundId.getId().toString());
		}
		if(getVouchermis()!=null && getVouchermis().getDepartmentid()!=null && getVouchermis().getDepartmentid().getId()!=null && getVouchermis().getDepartmentid().getId()!=-1){
			query.append(" and vmis.DEPARTMENTID=").append(getVouchermis().getDepartmentid().getId().toString());
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
		Query query = persistenceService.getSession().createSQLQuery("select name from companydetail");
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
	
	public String exportPdf() throws JRException, IOException{
		ajaxLoadBankBook();
		List<Object> dataSource = new ArrayList<Object>();
		for (BankBookViewEntry row : bankBookViewEntries) {
			dataSource.add(row);
		}
		setInputStream(reportHelper.exportPdf(getInputStream(), jasperpath, getParamMap(), dataSource));
	    return "PDF";
	}
	
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
		paramMap.put("heading", getText("bank.book.heading",new String[]{name,Constants.DDMMYYYYFORMAT2.format(startDate),Constants.DDMMYYYYFORMAT2.format(endDate)}));
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
	
	public String showChequeDetails(){
		if(voucherId!=null){
			chequeDetails = persistenceService.findAllBy("from InstrumentVoucher where voucherHeaderId.id=?", voucherId);
		}
		return "chequeDetails";
	}

	public void setChequeDetails(List<InstrumentVoucher> chequeDetails) {
		this.chequeDetails = chequeDetails;
	}

	public List<InstrumentVoucher> getChequeDetails() {
		return chequeDetails;
	}

	public void setGenericDao(GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}

	public void setVouchermis(Vouchermis vouchermis) {
		this.vouchermis = vouchermis;
	}

}
