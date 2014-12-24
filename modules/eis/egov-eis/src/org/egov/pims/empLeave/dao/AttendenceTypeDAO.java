package org.egov.pims.empLeave.dao;

import java.util.Map;

import org.egov.pims.empLeave.model.AttendenceType;



public interface AttendenceTypeDAO extends org.egov.infstr.dao.GenericDAO
{
	public AttendenceType getAttendenceTypeByID(Integer id) ;
	public AttendenceType getAttendenceTypeByName(String typeName) ;
	


}

