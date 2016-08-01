/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.demand.interfaces;

import org.apache.log4j.Logger;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillDetails;
import org.egov.demand.model.EgDemand;
import org.egov.demand.utils.DemandUtils;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * This is an abstract which will be overridden by individual Bill service
 * (PropertyTax, Professional Tax etc).
 * 
 * @author Satyam
 * 
 */

public abstract class BillServiceInterface {

	@Autowired
	private EgBillDao egBillDAO;
	private Billable billObj = null;
	public static final Logger LOGGER = Logger.getLogger(BillServiceInterface.class);

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
			throw new ApplicationRuntimeException("Exception in getBillXML....Billable is null");
		} else {
			bill = generateBill(billObj);
		}
		return generateXML(bill);
	}

	public String generateXML(EgBill bill) {
		if (bill == null) {
			throw new ApplicationRuntimeException("Exception in generateXML..Bill is null");
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
		bill.setBoundaryNum(billObj.getBoundaryNum().intValue());
		bill.setTransanctionReferenceNumber(billObj.getTransanctionReferenceNumber());
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
		bill.setOverrideAccountHeadsAllowed(billObj.getOverrideAccountHeadsAllowed());
		bill.setPartPaymentAllowed(billObj.getPartPaymentAllowed());
		bill.setServiceCode(billObj.getServiceCode());
		bill.setIs_Cancelled("N");
		bill.setIs_History("N");
		bill.setModifiedDate(new Date());
		bill.setTotalAmount(billObj.getTotalAmount());
		bill.setUserId(billObj.getUserId());
		bill.setCreateDate(new Date());
		EgDemand currentDemand = billObj.getCurrentDemand();
		bill.setEgDemand(currentDemand);
		bill.setDescription(billObj.getDescription());
		bill.setDisplayMessage(billObj.getDisplayMessage());
		bill.setEmailId(billObj.getEmailId());
		
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
		
		bill.setConsumerId(billObj.getConsumerId());
		bill.setCallBackForApportion(billObj.isCallbackForApportion());
		egBillDAO.create(bill);
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