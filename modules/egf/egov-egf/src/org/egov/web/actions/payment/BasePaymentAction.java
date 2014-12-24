/**
 * Action class to route to appropriate URL for drilldown from INBOX
 */
package org.egov.web.actions.payment;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.model.payment.Paymentheader;
import org.egov.utils.FinancialConstants;
import org.egov.web.actions.voucher.BaseVoucherAction;

import com.exilant.eGov.src.transactions.VoucherTypeForULB;

/**    
 * @author mani
 */

@Results( {
		@Result(name = "billpayment", type = "redirect", location="payment", params = { "namespace", "/payment", "method", "view" }),
		@Result(name = "advancepayment", type = "redirect", location="payment", params = { "namespace", "/payment", "method", "advanceView" }),
		@Result(name = "directbankpayment", type = "redirect", location="directBankPayment", params = { "namespace", "/payment","method", "approve" }) ,
		@Result(name = "remitRecovery", type = "redirect", location="remitRecovery", params = { "namespace", "/deduction","method", "approve" }) })
public class BasePaymentAction extends BaseVoucherAction {    
	public BasePaymentAction()
	{
	super();
	}
	protected String				action				= "";
	protected String				paymentid			= "";
	private final String			BILLPAYMENT			= "billpayment";
	private final String			DIRECTBANKPAYMENT	= "directbankpayment";
	private final String            REMITTANCEPAYMENT   = "remitRecovery";
	protected static final String	ACTIONNAME			= "actionname";
	protected GenericHibernateDaoFactory genericDao;
	protected String showMode;
	
	public String viewInboxItems() {
		String result = null;
		Paymentheader paymentheader = (Paymentheader) persistenceService.find("from Paymentheader where id=?", Long.valueOf(paymentid));
		getSession().put("paymentid", paymentid);
		if (paymentheader.getVoucherheader().getName().equalsIgnoreCase("Advance Payment")) {
			result = "advancepayment";
		}else if (paymentheader.getVoucherheader().getName().equalsIgnoreCase(FinancialConstants.PAYMENTVOUCHER_NAME_BILL) ||
				"Salary Bill Payment".equalsIgnoreCase(paymentheader.getVoucherheader().getName())) {
			result = BILLPAYMENT;
		}
		else if (paymentheader.getVoucherheader().getName().equalsIgnoreCase(FinancialConstants.PAYMENTVOUCHER_NAME_DIRECTBANK)) {
			result = DIRECTBANKPAYMENT;
		}
		else if (paymentheader.getVoucherheader().getName().equalsIgnoreCase(FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE)) {
			result = REMITTANCEPAYMENT;
		}
		return result; 
	}
	
	//used only in create
	public boolean shouldshowVoucherNumber()
	{
		String  vNumGenMode="Manual";
		vNumGenMode= new VoucherTypeForULB().readVoucherTypes(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);
			if(!"Auto".equalsIgnoreCase(vNumGenMode)){
				mandatoryFields.add("vouchernumber");
				return true	;
			}else
			{
			return false;
			}
	}
	
	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	public GenericHibernateDaoFactory getGenericDao() {
		return genericDao;
	}

	public void setGenericDao(GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}

	public String getPaymentid() {
		return paymentid;
	}
	
	public void setPaymentid(String paymentid) {
		this.paymentid = paymentid;
	}
	public String getShowMode() {
		return showMode;
	}
	public void setShowMode(String showMode) {
		this.showMode = showMode;
	}

	public String getFinConstExpendTypeContingency() {
		return FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT;
	}

}
