package org.egov.pims.empLeave.dao;

import java.util.List;

import org.egov.pims.empLeave.model.LeaveApplication;
import org.egov.pims.empLeave.model.LeaveOpeningBalance;

public interface LeaveOpeningBalanceDAO extends org.egov.infstr.dao.GenericDAO
{
	public LeaveOpeningBalance getLeaveOpeningBalanceByID(Integer id) ;
	public LeaveOpeningBalance getLeaveOpeningBalanceByEmpID(Integer empId,Integer leaveType) ;
	
	
}

