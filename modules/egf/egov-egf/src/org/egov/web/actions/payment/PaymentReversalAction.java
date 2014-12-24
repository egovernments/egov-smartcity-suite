package org.egov.web.actions.payment;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.egov.exceptions.EGOVException;
import org.egov.commons.Bankaccount;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.Vouchermis;
import org.egov.egf.commons.EgovCommon;
import org.egov.egf.commons.VoucherSearchUtil;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.model.payment.Paymentheader;
import org.egov.web.actions.voucher.BaseVoucherAction;

public class PaymentReversalAction extends BaseVoucherAction{
	private VoucherSearchUtil voucherSearchUtil;
	private Date fromDate;
	private Date toDate;
	private List<CVoucherHeader> voucherHeaderList = new ArrayList<CVoucherHeader>();
	private boolean close = false;
	private String message = "";
	private EgovCommon egovCommon;
	Bankaccount bankAccount;
	private List<Paymentheader> paymentHeaderList = new ArrayList<Paymentheader>();
	private Paymentheader paymentHeader = new Paymentheader();
	private GenericHibernateDaoFactory genericDao;

	@Override
	public void prepare() {
		super.prepare();
		List<AppConfigValues> appList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("finance","statusexcludeReport");
		String statusExclude = appList.get(0).getValue();
		if("".equalsIgnoreCase(statusExclude) || statusExclude == null)
			throw new ValidationException(Arrays.asList(new ValidationError("voucher.excludestatus.not.set", "voucher.excludestatus.not.set")));
		addDropdownData("bankList", egovCommon.getBankBranchForActiveBanks());
		addDropdownData("accNumList", Collections.EMPTY_LIST);
		addDropdownData("voucherNameList", persistenceService.findAllBy("select distinct vh.name from CVoucherHeader vh where vh.type='Payment' and status not in ("+statusExclude+") order by vh.name"));
	}

	public PaymentReversalAction(){
		voucherHeader.setVouchermis(new Vouchermis());
		addRelatedEntity("vouchermis.departmentid", DepartmentImpl.class);
		addRelatedEntity("fundId", Fund.class);
		addRelatedEntity("vouchermis.schemeid", Scheme.class);
		addRelatedEntity("vouchermis.subschemeid", SubScheme.class);
		addRelatedEntity("vouchermis.functionary", Functionary.class);
		addRelatedEntity("fundsourceId", Fundsource.class);
		addRelatedEntity("vouchermis.divisionid", BoundaryImpl.class);
	}
	
	public String reverse(){
		if(paymentHeader.getId() != null){
			paymentHeader = (Paymentheader) persistenceService.find("from Paymentheader where id=?", paymentHeader.getId());
			voucherHeader = paymentHeader.getVoucherheader();
		}
		return "reverse";
	}
	public String saveReverse(){
		if(voucherHeader.getId() != null)
			voucherHeader = (CVoucherHeader) persistenceService.find("from CVoucherHeader where id=?", voucherHeader.getId());
		saveReverse(voucherHeader.getName(), "Receipt");
		message = getText("transaction.success") + voucherHeader.getVoucherNumber();
		return "reverse";
	}
	
	public String saveReverseAndClose(){
		close = true;
		return saveReverse();
	}
	public String vouchersForReversal(){
		return "reversalVouchers";
	}
	public String searchVouchersForReversal() throws EGOVException, ParseException{
		voucherHeader.setType("Payment");
		voucherHeaderList = voucherSearchUtil.search(voucherHeader, getFromDate(), getToDate(), "reverse");
		String query = formQuery(voucherHeaderList);
		if(voucherHeaderList != null && voucherHeaderList.size()>0){
			if(bankAccount != null && bankAccount.getId()!=null)
				paymentHeaderList = (List<Paymentheader>) persistenceService.findAllBy(query+" and bankaccount.id=?",bankAccount.getId());
			else
				paymentHeaderList = (List<Paymentheader>) persistenceService.findAllBy(query);
		}
		if(paymentHeaderList.size() == 0)
			message = getText("no.records");
		return "reversalVouchers";
	}
	
	private String formQuery(List<CVoucherHeader> voucherHeaderList) {
		StringBuffer query = new StringBuffer("from Paymentheader where voucherheader.id in (");
		for (CVoucherHeader voucherHeader : voucherHeaderList) {
			query = query.append(voucherHeader.getId()).append(",");
		}
		if(voucherHeaderList.size()>0)
			return query.substring(0, query.length()-1).concat(" ) ");
		return query.toString().concat(" ) ");
	}

	public void setVoucherSearchUtil(VoucherSearchUtil voucherSearchUtil) {
		this.voucherSearchUtil = voucherSearchUtil;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public boolean isFieldMandatory(String field){
		return mandatoryFields.contains(field);
	}
	public boolean shouldShowHeaderField(String field){
		return  headerFields.contains(field);
	}

	public List<CVoucherHeader> getVoucherHeaderList() {
		return voucherHeaderList;
	}

	public void setClose(boolean close) {
		this.close = close;
	}

	public boolean isClose() {
		return close;
	}

	public String getMessage() {
		return message;
	}

	public void setEgovCommon(EgovCommon egovCommon) {
		this.egovCommon = egovCommon;
	}

	public List<Paymentheader> getPaymentHeaderList() {
		return paymentHeaderList;
	}

	public Paymentheader getPaymentHeader() {
		return paymentHeader;
	}

	public void setGenericDao(GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}

	@Override
	public CVoucherHeader getVoucherHeader() {
		return super.getVoucherHeader();
	}
}
