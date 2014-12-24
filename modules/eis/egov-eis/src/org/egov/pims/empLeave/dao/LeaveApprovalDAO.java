package org.egov.pims.empLeave.dao;

import org.egov.pims.empLeave.model.LeaveApplication;
import org.egov.pims.empLeave.model.LeaveApproval;

public interface LeaveApprovalDAO extends org.egov.infstr.dao.GenericDAO
{
	public LeaveApproval getLeaveApprovalByID(Integer id) ;
	public LeaveApproval getLeaveApprovalByApplicationID(Integer id) ;
}

