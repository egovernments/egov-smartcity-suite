/**
 * Action class to route to appropriate URL for drilldown from INBOX
 */
package org.egov.web.actions.payment;

import org.apache.struts2.convention.annotation.Action;
import org.apache.log4j.Logger;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.model.advance.EgAdvanceRequisition;
import org.egov.model.payment.Paymentheader;
import org.egov.eis.service.EisCommonService;
import org.egov.utils.FinancialConstants;
import org.egov.web.actions.voucher.BaseVoucherAction;

import com.exilant.eGov.src.transactions.VoucherTypeForULB;

/**    
 * @author mani
 */

@Results( {
		@Result(name = "billpayment", type = ServletActionRedirectResult.class, value = "payment", params = { "namespace", "/payment", "method", "view" }),
		@Result(name = "advancepayment", type = ServletActionRedirectResult.class, value = "payment", params = { "namespace", "/payment", "method", "advanceView" }),
		@Result(name = "directbankpayment", type = ServletActionRedirectResult.class, value = "directBankPayment", params = { "namespace", "/payment","method", "viewInboxItem" }) ,
		@Result(name = "remitRecovery", type = ServletActionRedirectResult.class, value = "remitRecovery", params = { "namespace", "/deduction","method", "viewInboxItem" }),
		@Result(name = "contractoradvancepayment", type = ServletActionRedirectResult.class, value = "advancePayment", params = { "namespace", "/payment", "method", "viewInboxItem" })})
		
public class BasePaymentAction extends BaseVoucherAction {    
	EisCommonService eisCommonService;
	private static Logger LOGGER=Logger.getLogger(BasePaymentAction.class);
	public void setEisCommonService(EisCommonService eisCommonService) {
		this.eisCommonService = eisCommonService;
	}  

	public BasePaymentAction()
	{
	super();
	}
	protected String				action				= "";
	protected String				paymentid			= "";
	private final String			BILLPAYMENT			= "billpayment";
	private final String			DIRECTBANKPAYMENT	= "directbankpayment";
	private final String            REMITTANCEPAYMENT   = "remitRecovery";
	public static final String ARF_TYPE="Contractor";
	
	protected static final String	ACTIONNAME			= "actionname";
	protected boolean canCheckBalance=false;
	
	public boolean isCanCheckBalance() {
		return canCheckBalance;
	}

	public void setCanCheckBalance(boolean canCheckBalance) {
		this.canCheckBalance = canCheckBalance;
	}
	protected GenericHibernateDaoFactory genericDao;
	protected String showMode;
@Action(value="/payment/basePayment-viewInboxItems")
	public String viewInboxItems() {
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting viewInboxItems..... ");
		String result = null;
		Paymentheader paymentheader = (Paymentheader) persistenceService.find("from Paymentheader where id=?", Long.valueOf(paymentid));
		if(!validateOwner(paymentheader.getState())) 
		{
			return INVALIDPAGE;
		}
	HibernateUtil.getCurrentSession().put("paymentid", paymentid);
		if (paymentheader.getVoucherheader().getName().equalsIgnoreCase(FinancialConstants.PAYMENTVOUCHER_NAME_ADVANCE)) {
			EgAdvanceRequisition arf = (EgAdvanceRequisition)persistenceService.find("from EgAdvanceRequisition where arftype = ? and egAdvanceReqMises.voucherheader = ?",ARF_TYPE,paymentheader.getVoucherheader());
			if(arf != null)
				result = "contractoradvancepayment"; 
			else
				result = "advancepayment";
		}
		else if (paymentheader.getVoucherheader().getName().equalsIgnoreCase(FinancialConstants.PAYMENTVOUCHER_NAME_BILL) ||
				FinancialConstants.PAYMENTVOUCHER_NAME_SALARY.equalsIgnoreCase(paymentheader.getVoucherheader().getName()) ||
				FinancialConstants.PAYMENTVOUCHER_NAME_PENSION.equalsIgnoreCase(paymentheader.getVoucherheader().getName())) {
			result = BILLPAYMENT;
		}
		else if (paymentheader.getVoucherheader().getName().equalsIgnoreCase(FinancialConstants.PAYMENTVOUCHER_NAME_DIRECTBANK)) {
			result = DIRECTBANKPAYMENT;
		}
		else if (paymentheader.getVoucherheader().getName().equalsIgnoreCase(FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE)) {
			result = REMITTANCEPAYMENT;
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed viewInboxItems..... ");
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

	public String getFinConstExpendTypePension() {
		return FinancialConstants.STANDARD_EXPENDITURETYPE_PENSION;
	}

	
}
