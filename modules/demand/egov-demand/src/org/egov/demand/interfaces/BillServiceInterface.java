package org.egov.demand.interfaces;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.demand.dao.DCBDaoFactory;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillDetails;
import org.egov.demand.model.EgDemand;
import org.egov.demand.utils.DemandUtils;

/**
 * This is an abstract which will be overridden by individual Bill service
 * (PropertyTax, Professional Tax etc).
 * 
 * @author Satyam
 * 
 */

public abstract class BillServiceInterface {
	
	private Billable billObj = null;
	public static final Logger LOGGER = Logger
			.getLogger(BillServiceInterface.class);

	/**
	 * To get an XML as string passing Billable object.
	 * 
	 * @param The
	 *            object for which the basic property is set and implements
	 *            Billable interface.
	 * @return XML string
	 */

	public String getBillXML(Billable billObj) {
		EgBill bill = null;
		if (billObj == null) {
			throw new EGOVRuntimeException(
					"Exception in getBillXML....Billable is null");
		} else {
			bill = generateBill(billObj);
		}
		return generateXML(bill);
	}

	public String generateXML(EgBill bill) {
		if (bill == null) {
			throw new EGOVRuntimeException(
					"Exception in generateXML..Bill is null");
		}
		String msg = bill.getDisplayMessage();
		return new DemandUtils().generateBillXML(bill, msg);
	}

	/**
	 * Creates and returns a Bill Object
	 * 
	 * @param The
	 *            Object for which the Bill Which needs to be inserted. Would be
	 *            BasicProperty, TradeLicense objects and so on
	 * @return
	 */
	public final EgBill generateBill(Billable billObj) {
		EgBill bill = new EgBill();
		bill.setBillNo(billObj.getReferenceNumber());
		bill.setBoundaryNum((billObj.getBoundaryNum()));
		bill.setBoundaryType(billObj.getBoundaryType());
		bill.setCitizenAddress(billObj.getBillAddress());
		bill.setCitizenName(billObj.getBillPayee());
		bill.setCollModesNotAllowed(billObj.getCollModesNotAllowed());
		bill.setDepartmentCode(billObj.getDepartmentCode());
		bill.setEgBillType(billObj.getBillType());
		bill.setFunctionaryCode(billObj.getFunctionaryCode());
		bill.setFundCode(billObj.getFundCode());
		bill.setFundSourceCode(billObj.getFundSourceCode());
		bill.setIssueDate(new Date());
		bill.setLastDate(billObj.getBillLastDueDate());
		bill.setModule(billObj.getModule());
		bill.setOverrideAccountHeadsAllowed(billObj
				.getOverrideAccountHeadsAllowed());
		bill.setPartPaymentAllowed(billObj.getPartPaymentAllowed());
		bill.setServiceCode(billObj.getServiceCode());
		bill.setIs_Cancelled("N");
		bill.setIs_History("N");
		bill.setLastUpdatedTimeStamp(new Date());
		bill.setTotalAmount(billObj.getTotalAmount());
		bill.setUserId(billObj.getUserId());
		bill.setCreateTimeStamp(new Date());
		EgDemand currentDemand = billObj.getCurrentDemand();
        bill.setEgDemand(currentDemand);
		bill.setDescription(billObj.getDescription());
		bill.setDisplayMessage(billObj.getDisplayMessage());
		if (currentDemand != null && currentDemand.getMinAmtPayable() != null) {
			bill.setMinAmtPayable(currentDemand.getMinAmtPayable());
		} else {
			bill.setMinAmtPayable(BigDecimal.ZERO);
		}
		// Get it from the concrete implementation
		List<EgBillDetails> bd = getBilldetails(billObj);
		for (EgBillDetails billdetails : bd) {
			bill.addEgBillDetails(billdetails);
			billdetails.setEgBill(bill);
		}
		bill.setConsumerId(billObj.getPropertyId());
		bill.setCallBackForApportion(billObj.isCallbackForApportion());
		DCBDaoFactory.getDaoFactory().getEgBillDao().create(bill);
		return bill;
	};

	/**
	 * The default implementation will take the same account heads as the
	 * BillDetails and put the appropriate debits and credits The Billing
	 * instance may choose to override the API;
	 * 
	 * @param dmd
	 * @return
	 */
	// This might be overridden by the individual applications
	public abstract List<EgBillDetails> getBilldetails(Billable billObj);

	public void setBillObj(Billable billObj) {
		this.billObj = billObj;
	}

	abstract public void cancelBill();
}