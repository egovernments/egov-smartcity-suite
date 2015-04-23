package org.egov.web.actions.payment;

import org.apache.struts2.convention.annotation.Action;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.StreamResult;
import org.egov.commons.Bankaccount;
import org.egov.egf.commons.EgovCommon;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.payment.Paymentheader;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.utils.ReportHelper;
import org.egov.web.actions.BaseFormAction;
import org.hibernate.Query;



@Results(value={
		@Result(name="PDF",type="stream",location="inputStream", params={"inputName","inputStream","contentType","application/pdf","contentDisposition","no-cache;filename=OutstandingPaymentReport.pdf"}),
		@Result(name="XLS",type="stream",location="inputStream", params={"inputName","inputStream","contentType","application/xls","contentDisposition","no-cache;filename=OutstandingPaymentReport.xls"})
})
@ParentPackage("egov")
public class OutstandingPaymentAction extends BaseFormAction{
	private static final Logger	LOGGER	= Logger.getLogger(OutstandingPaymentAction.class);
	private List<Paymentheader> paymentHeaderList = new ArrayList<Paymentheader>();
	private Map<Long,String> voucherHeaderMap = new HashMap<Long,String>();
	private Date asOnDate = new Date();
	private BigDecimal bankBalance = BigDecimal.ZERO;
	private EgovCommon egovCommon;
	private BigDecimal currentReceiptsAmount = BigDecimal.ZERO;
	private BigDecimal runningBalance = BigDecimal.ZERO;
	private GenericHibernateDaoFactory genericDao;
	private Bankaccount bankAccount;
	private String voucherStatusKey = "VOUCHER_STATUS_TO_CHECK_BANK_BALANCE";
	private String jasperpath ="/reports/templates/OutstandingPaymentReport.jasper";
	private ReportHelper reportHelper;
	private InputStream inputStream;
	private String selectedVhs;
	private Long[] selectdVhs;
	private BigDecimal rBalance= BigDecimal.ZERO;
	

	@Override
	public String execute() throws Exception {
		return "form";
	}

	public Long[] getSelectdVhs() {
		return selectdVhs;
	}

	public void setSelectdVhs(Long[] selectdVhs) {
		this.selectdVhs = selectdVhs;
	}

	@Override
	public void prepare() {
		super.prepare();
		if(!parameters.containsKey("skipPrepare")){
			EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
			addDropdownData("bankList", Collections.EMPTY_LIST);
			addDropdownData("accNumList", Collections.EMPTY_LIST);
			addDropdownData("fundList",  masterCache.get("egi-fund"));
		}
	}

@Action(value="/payment/outstandingPayment-ajaxLoadPaymentHeader")
	public String ajaxLoadPaymentHeader(){
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting ajaxLoadPaymentHeader...");
		if(parameters.containsKey("bankAccount.id") && parameters.get("bankAccount.id")[0]!=null){
			if(parameters.containsKey("asOnDate") && parameters.get("asOnDate")[0]!=null){
				try {
					setAsOnDate(Constants.DDMMYYYYFORMAT2.parse(parameters.get("asOnDate")[0]));
				} catch (ParseException e) {
					throw new ValidationException("Invalid date","Invalid date");
				}
			}
			if(parameters.containsKey("asOnDate") && parameters.get("asOnDate")[0]!=null){

				setSelectedVhs("selectedVhs");
			}
			Integer id = Integer.valueOf(parameters.get("bankAccount.id")[0]);
			bankAccount = (Bankaccount) persistenceService.find("from Bankaccount where id=?",id);
			//this is actually approval status
			List<AppConfigValues> appConfig = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(Constants.EGF,"VOUCHER_STATUS_TO_CHECK_BANK_BALANCE");
			if(appConfig == null || appConfig.isEmpty())
				throw new ValidationException("","VOUCHER_STATUS_TO_CHECK_BANK_BALANCE is not defined in AppConfig");
			
			String appConfigValue = "";
			boolean condtitionalAppConfigIsPresent = false;
			String designationName = null;
			String functionaryName = null;
			String stateWithoutCondition = "";
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Beginning app config check...");
			for(AppConfigValues app:appConfig)
			{
				appConfigValue = app.getValue(); 
				if(appConfigValue.contains(FinancialConstants.DELIMITER_FOR_VOUCHER_STATUS_TO_CHECK_BANK_BALANCE))
				{
					condtitionalAppConfigIsPresent = true;
					String [] array = appConfigValue.split(FinancialConstants.DELIMITER_FOR_VOUCHER_STATUS_TO_CHECK_BANK_BALANCE);
					if( array.length!=2)
						throw new ValidationException("","VOUCHER_STATUS_TO_CHECK_BANK_BALANCE is invalid");
					// Order assumed is first is designation Name, second functionary Name
					designationName = array[0];
					functionaryName = array[1];
				}
				else
				{
					stateWithoutCondition = appConfigValue;
				}
			}
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Ending app config check...");
			StringBuffer query = new StringBuffer();
			query.append("from Paymentheader where voucherheader.voucherDate<=? and voucherheader.status in ( " +
			+FinancialConstants.CREATEDVOUCHERSTATUS+","+FinancialConstants.PREAPPROVEDVOUCHERSTATUS+") and bankaccount.id=? and" +
					" state.type='Paymentheader'");
			if(condtitionalAppConfigIsPresent)
			{
				String ownerIdList = getCommaSeperatedListForDesignationNameAndFunctionaryName(designationName, functionaryName);
				query.append(" and state.owner in ("+ownerIdList+") order by state.createdDate desc ");
				if(LOGGER.isDebugEnabled())     LOGGER.debug("In condtitionalAppConfigIsPresent - qry" + query.toString());
				paymentHeaderList.addAll(persistenceService.findPageBy(query.toString(),1,100,getAsOnDate(),id).getList());
			}
			else
			{
				query.append(" and state.value like '"+stateWithoutCondition+"' order by state.createdDate desc ");
				if(LOGGER.isDebugEnabled())     LOGGER.debug("In ELSE - qry" + query.toString());
				paymentHeaderList.addAll(persistenceService.findPageBy(query.toString(),1,100,getAsOnDate(),id).getList());
			}
			bankBalance = egovCommon.getBankBalanceAvailableforPayment(getAsOnDate(), id);
			
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Ending ajaxLoadPaymentHeader...");
		return "results";
	}
	
	private String getCommaSeperatedListForDesignationNameAndFunctionaryName(String designationName, String functionaryName)
	{
		String qrySQL = "select pos_id from eg_eis_employeeinfo empinfo, eg_designation desg, functionary func   " +
						" where empinfo.functionary_id=func.id and empinfo.DESIGNATIONID=desg.DESIGNATIONID " +
						" and empinfo.isactive=1   " + 
						" and desg.DESIGNATION_NAME like '" + designationName + "' and func.NAME like '"+functionaryName+"' ";
		Query query = HibernateUtil.getCurrentSession().createSQLQuery(qrySQL);
		List<BigDecimal> result = query.list();
		if(result == null || result.isEmpty())
			throw new ValidationException("","No employee with functionary -" +functionaryName+" and designation - "+designationName);
		StringBuffer returnListSB = new StringBuffer(); 
		String commaSeperatedList = "";
		for(BigDecimal posId : result)
		{
			returnListSB.append(posId.toString()  + ",");
		}
		commaSeperatedList = returnListSB.substring(0, returnListSB.length()-1);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Commo seperated  list - " + commaSeperatedList);
		return commaSeperatedList;
	}

	public String getUlbName(){
		Query query = HibernateUtil.getCurrentSession().createSQLQuery("select name from companydetail");
		List<String> result = query.list();
		if(result!=null)
			return result.get(0);
		return "";
	}
@Action(value="/payment/outstandingPayment-exportPdf")
	public String exportPdf() throws JRException, IOException{
		List<Object> dataSource = generateReportData();
		setInputStream(reportHelper.exportPdf(getInputStream(), jasperpath, getParamMap(), dataSource));
		return "PDF";
	}

	public void setPaymentHeaderList(List<Paymentheader> paymentHeaderList) {
		this.paymentHeaderList = paymentHeaderList;
	}

@Action(value="/payment/outstandingPayment-exportXls")
	public String exportXls() throws JRException, IOException{
		List<Object> dataSource = generateReportData();
		setInputStream(reportHelper.exportXls(getInputStream(), jasperpath, getParamMap(),dataSource));
		return "XLS";
	}

	private List<Object> generateReportData() {
		String str="";
		String[] splitVh=null;
		if(parameters.containsKey("selectedVhs") && parameters.get("selectedVhs")[0]!=null){
			String[] vh_ids=parameters.get("selectedVhs");
			splitVh=vh_ids[0].split(",");
			for(int i=0,j=0;i<splitVh.length;i++){
				voucherHeaderMap.put(Long.parseLong(splitVh[i]),"Selected");
			}

		}
		
		ajaxLoadPaymentHeader();
		List<Object> dataSource = new ArrayList<Object>();
		for (Paymentheader row : paymentHeaderList) {
			String chkSelected=voucherHeaderMap.get(row.getVoucherheader().getId());
			if("Selected".equals(chkSelected)){
				row.setIsSelected("Selected");
			}else{
				row.setIsSelected(null);
			}
			dataSource.add(row);
		}
		return dataSource;
	}



	public String getSelectedVhs() {
		return selectedVhs;
	}

	public BigDecimal getRunningBalance() {
		return runningBalance;
	}

	public void setRunningBalance(BigDecimal runningBalance) {
		this.runningBalance = runningBalance;
	}

	public void setSelectedVhs(String selectedVhs) {
		this.selectedVhs = selectedVhs;
	}

	public BigDecimal getRBalance() {
		return rBalance;
	}

	public void setRBalance(BigDecimal balance) {
		rBalance = balance;
	}

	Map<String, Object> getParamMap() {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("ulbName", getUlbName());
		String name = bankAccount.getBankbranch().getBank().getName().concat("-")
		.concat(bankAccount.getBankbranch().getBranchname()).concat("-")
		.concat(bankAccount.getAccountnumber());
		String heading="Outstanding Payment Report as on "+Constants.DDMMYYYYFORMAT2.format(asOnDate);
		String bankDetail="Bank Balance Details as on " +Constants.DDMMYYYYFORMAT2.format(asOnDate);
		paramMap.put("heading",heading);
		paramMap.put("bankDetail",bankDetail);
		paramMap.put("bankName", bankAccount.getBankbranch().getBank().getName().toString());
		paramMap.put("bankBranchName", bankAccount.getBankbranch().getBranchname());
		paramMap.put("bankAccountNumber", bankAccount.getAccountnumber().toString());
		paramMap.put("chartOfAccount", bankAccount.getChartofaccounts().getGlcode());
		paramMap.put("currentBalance", bankBalance.toString());
		paramMap.put("runningBalance",runningBalance.toString());
		return paramMap;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public List<Paymentheader> getPaymentHeaderList(){
		return paymentHeaderList;
	}

	public void setEgovCommon(EgovCommon egovCommon) {
		this.egovCommon = egovCommon;
	}

	public BigDecimal getBankBalance() {
		return bankBalance;
	}

	public ReportHelper getReportHelper() {
		return reportHelper;
	}

	public void setReportHelper(ReportHelper reportHelper) {
		this.reportHelper = reportHelper;
	}

	public void setGenericDao(GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}

	public String getFormattedDate(Date date){
		return Constants.DDMMYYYYFORMAT2.format(date);
	}

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setVoucherStatusKey(String voucherStatus) {
		this.voucherStatusKey = voucherStatus;
	}

	public String getVoucherStatusKey() {
		return voucherStatusKey;
	}

	public void setAsOnDate(Date asOnDate) {
		this.asOnDate = asOnDate;
	}

	public Date getAsOnDate() {
		return asOnDate;
	}
	public void setCurrentReceiptsAmount(BigDecimal currentReceiptsAmount) {
		this.currentReceiptsAmount = currentReceiptsAmount;
	}

	public BigDecimal getCurrentReceiptsAmount() {
		return currentReceiptsAmount;
	}
	public void setBankAccount(Bankaccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	public Bankaccount getBankAccount() {
		return bankAccount;
	}
}
