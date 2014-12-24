package org.egov.pims.empLeave.dao;

import org.egov.pims.empLeave.model.CompOff;
import org.egov.pims.empLeave.model.LeaveApplication;
import org.egov.pims.empLeave.model.LeaveApproval;

public interface CompOffDAO extends org.egov.infstr.dao.GenericDAO
{
	public CompOff getCompOffByID(Integer id);

}

