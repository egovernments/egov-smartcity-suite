package org.egov.demand.dao;

import org.egov.demand.model.EgBillType;
import org.egov.infstr.dao.GenericDAO;

public interface EgBillDao  extends GenericDAO
{
	public EgBillType getBillTypeByCode(String code);
}
