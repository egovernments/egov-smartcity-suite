package org.egov.works.services;

import java.util.List;

import org.egov.exceptions.EGOVException;
import org.egov.commons.CChartOfAccounts;
import org.egov.model.bills.EgBillregister;
import org.egov.works.models.retentionMoney.RetentionMoneyRefund;
import org.egov.works.models.workorder.WorkOrder;

public interface RetentionMoneyRefundService  extends BaseService<RetentionMoneyRefund,Long>{
	
	/**
	 * This method will get the Total Outstanding Retention Money  for all approved bills for that 
	 * workorder and coa id and a boolean value to check if refunded amount should be considered or not
	 * @param WorkOrder
	 * @param glCodeId 
	 * @param considerRefund
	 * @return
	 */
	public double getTotalOutstandingRetentionMoney(WorkOrder workorder, Long glcodeId, boolean considerRefund);
	
	/**
	 * This method will return the list of all Retention Money Refund chartofaccounts 
	 * @return
	 */
	public List<CChartOfAccounts> getRetentionMRCOAList()  throws NumberFormatException, EGOVException;
	
	public EgBillregister createExpenseBill(RetentionMoneyRefund retentionMoneyRefund) throws Exception;
	
}
