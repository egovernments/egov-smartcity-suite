package org.egov.works.services;

import java.util.List;

import org.egov.exceptions.EGOVException;
import org.egov.commons.CChartOfAccounts;
import org.egov.model.bills.EgBillregister;
import org.egov.works.models.securityDeposit.ReturnSecurityDeposit;
import org.egov.works.models.workorder.WorkOrder;

public interface ReturnSecurityDepositService  extends BaseService<ReturnSecurityDeposit,Long>{
	
	/**
	 * This method will get the total security deposit amount deducted from all approved bills for that 
	 * workorder and coa id and a boolean value to check if refunded amount should be considered or not
	 * @param WorkOrder
	 * @param glCodeId 
	 * @param withRefund
	 * @return
	 */
	public double getTotalSDAmountDeducted(WorkOrder workorder, Long glcodeId,boolean withRefund);
	
	/**
	 * This method will return the list of all chartofaccounts linked with collection of security deposit amount
	 * @return
	 */
	public List<CChartOfAccounts> getSDCOAList()  throws NumberFormatException, EGOVException;
	
	public EgBillregister createExpenseBill(ReturnSecurityDeposit returnSecurityDeposit) throws Exception;
	
}
