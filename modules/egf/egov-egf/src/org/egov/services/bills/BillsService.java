package org.egov.services.bills;

import java.util.List;


import org.egov.commons.CVoucherHeader;
import org.egov.model.bills.EgBillregister;

public interface BillsService{
	/**
	 * @param billregister
	 * @return EgBillregister
	 */
	public EgBillregister createBillRegister(EgBillregister billregister);
	/**
	 * @param billregister
	 * @return EgBillregister
	 */
	public EgBillregister updateBillRegister(EgBillregister billregister);
	/**
	 * @param billid
	 * @return EgBillregister
	 */
	public EgBillregister getBillRegisterById(Integer billid);
	
	/**
	 * 
	 * @return  List<EgBillregister>
	 */
	public List<String> getDistExpType();
	/**
	 * 
	 * @return
	 */
	public String getBillTypeforVoucher(CVoucherHeader voucherHeader);
	
	public String getBillSubTypeforVoucher(CVoucherHeader voucherHeader);
}
