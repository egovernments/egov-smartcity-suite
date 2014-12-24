package org.egov.pims.empLeave.dao;

import java.util.Date;
import java.util.List;

import org.egov.pims.empLeave.model.LeaveApplication;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.model.StatusMaster;

public interface LeaveApplicationDAO extends org.egov.infstr.dao.GenericDAO
{
	public LeaveApplication getLeaveApplicationByID(Integer ID) ;
	public LeaveApplication getLeaveApplicationBySancNo(String sanctionNo) ;
	public LeaveApplication getLeaveApplicationByEmpAndDate(Date date,PersonalInformation employee);
	public boolean checkLeaveReportsForAnEmployee(Integer employee);
	public boolean checkLeaveForAnEmployee(Integer employee);
	public List<LeaveApplication> getLeaveApplicationByEmpStatus(Integer empId, StatusMaster status)throws Exception;	
	public List<LeaveApplication> getEncashmentLeaveApplicationByStatus(String statusName)throws Exception;
	
}

