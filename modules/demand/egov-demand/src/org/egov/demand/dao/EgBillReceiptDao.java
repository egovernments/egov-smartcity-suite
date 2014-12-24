package org.egov.demand.dao;

import org.egov.demand.model.BillReceipt;
import org.egov.demand.model.EgBill;
import org.egov.infstr.dao.GenericDAO;

public interface EgBillReceiptDao extends GenericDAO{
	public BillReceipt getBillReceiptByEgBill(EgBill bill);

}
