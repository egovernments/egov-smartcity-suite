package org.egov.demand.dao;

import java.util.List;

import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillDetails;
import org.egov.infstr.dao.GenericDAO;


public interface EgBillDetailsDao extends GenericDAO{

	public abstract List<EgBillDetails> getBillDetailsByBill(EgBill egBill);
}
