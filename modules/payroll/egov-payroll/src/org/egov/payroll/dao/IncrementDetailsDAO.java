package org.egov.payroll.dao;

import java.math.BigDecimal;

public interface IncrementDetailsDAO extends org.egov.infstr.dao.GenericDAO{

	public BigDecimal getPendingIncrementAmount(Integer employeeid,Integer month,Integer financialyearid);
	public boolean resolvePendingIncr(Integer empid,Integer month,Integer year);
	public Boolean checkingForIncrementApplied(Integer empid,Integer month,Integer year);
}
