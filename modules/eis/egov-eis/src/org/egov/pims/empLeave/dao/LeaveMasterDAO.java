package org.egov.pims.empLeave.dao;

import java.util.List;
import java.util.Set;

import org.egov.pims.empLeave.model.LeaveApplication;
import org.egov.pims.empLeave.model.LeaveMaster;


public interface LeaveMasterDAO extends org.egov.infstr.dao.GenericDAO
{
	public LeaveMaster getLeaveMasterByID(Integer id) ;
	public List getListOfLeaveMastersForDesID(Integer desigId) ;
	public Integer getNoOfDaysForDesIDandType(Integer desigId,Integer typeId) ;
}

