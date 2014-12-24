package org.egov.web.actions.payment;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.commons.Bankaccount;
import org.egov.egf.commons.EgovCommon;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.model.payment.Paymentheader;
import org.egov.utils.Constants;
import org.egov.web.actions.BaseFormAction;

@ParentPackage("egov")
public class OutstandingPaymentAction extends BaseFormAction{
	List<Paymentheader> paymentHeaderList = new ArrayList<Paymentheader>();
	private Date asOnDate = new Date();
	private BigDecimal bankBalance = BigDecimal.ZERO;
	private EgovCommon egovCommon;
	private BigDecimal currentReceiptsAmount = BigDecimal.ZERO;
	private GenericHibernateDaoFactory genericDao;
	private Bankaccount bankAccount;
	private String voucherStatusKey = "VOUCHER_STATUS_TO_CHECK_BANK_BALANCE";

	@Override
	public String execute() throws Exception {
		return "form";
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
	
	public String ajaxLoadPaymentHeader(){
		if(parameters.containsKey("bankAccount.id") && parameters.get("bankAccount.id")[0]!=null){
			if(parameters.containsKey("asOnDate") && parameters.get("asOnDate")[0]!=null){
				try {
					setAsOnDate(Constants.DDMMYYYYFORMAT2.parse(parameters.get("asOnDate")[0]));
				} catch (ParseException e) {
					throw new ValidationException("Invalid date","Invalid date");
				}
			}
			if(parameters.containsKey("currentReceiptsAmount") && parameters.get("currentReceiptsAmount")[0]!=null){
				currentReceiptsAmount = new BigDecimal(parameters.get("currentReceiptsAmount")[0]);
			}
			Integer id = Integer.valueOf(parameters.get("bankAccount.id")[0]);
			bankAccount = (Bankaccount) persistenceService.find("from Bankaccount where id=?",id);
			List<AppConfigValues> appConfig = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(Constants.EGF,"VOUCHER_STATUS_TO_CHECK_BANK_BALANCE");
			if(appConfig == null || appConfig.isEmpty())
				throw new ValidationException("","VOUCHER_STATUS_TO_CHECK_BANK_BALANCE is not defined in AppConfig");
			String voucherStatus = ((AppConfigValues)appConfig.get(0)).getValue();
			paymentHeaderList.addAll(persistenceService.findPageBy("from Paymentheader where voucherheader.voucherDate<=? and bankaccount.id=? and state.type='Paymentheader' and state.value like '"+voucherStatus+"' order by state.createdDate desc",1,100,getAsOnDate(),id).getList());
			bankBalance = egovCommon.getBankBalanceAvailableforPayment(getAsOnDate(), id);
		}
		return "results";
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
