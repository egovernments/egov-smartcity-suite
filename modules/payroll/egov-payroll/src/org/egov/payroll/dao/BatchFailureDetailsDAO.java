package org.egov.payroll.dao;

import java.util.List;

public interface BatchFailureDetailsDAO extends org.egov.infstr.dao.GenericDAO {
	
	//public void updateIsHistory(Integer month,Integer year,Integer deptid,Integer functionaryId,Integer billNumberId);
	public void updateIsHistory(Integer month,Integer year,List empIdsList);
	public List getPendingPaySlipsList(Integer finyr,Integer month,Integer deptid,Integer empid,Integer type,Integer functionaryId,Integer billNo,Integer errPay);
	public boolean resolvePaySlipFailure(Integer empid,Integer month,Integer year,Integer paytype);
	public boolean isEmphasFailureForMonthFinyrPaytype(Integer empid,Integer month,Integer year,Integer paytype);
}
