package org.egov.billsaccounting.services;

import java.util.List;



import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CVoucherHeader;
import org.egov.infstr.ValidationException;
import org.egov.model.voucher.PreApprovedVoucher;

/**
 * 
 * @author Manikanta
 *
 */
public interface BillsAccountingService
{ 
/**
 * Creates voucher from bill 
 * @param billId
 * @return  long : will be vouherheadeId
 * 
 */
	public long createVoucherFromBill(int billId) throws EGOVRuntimeException;	
	public void createVoucherfromPreApprovedVoucher(long vouhcerheaderid) throws EGOVRuntimeException;
	public long createPreApprovedVoucherFromBill(int billId) throws EGOVRuntimeException,ValidationException;
	public long createPreApprovedVoucherFromBillForPJV(int billId,List<PreApprovedVoucher> voucherdetailList,List<PreApprovedVoucher> subLedgerList) throws EGOVRuntimeException;
	public void updatePJV(CVoucherHeader vh, List<PreApprovedVoucher> detailList,List<PreApprovedVoucher> subledgerlist)  throws EGOVRuntimeException;
	public CVoucherHeader getPJVNumberForBill(String billNumber) throws EGOVException;
}