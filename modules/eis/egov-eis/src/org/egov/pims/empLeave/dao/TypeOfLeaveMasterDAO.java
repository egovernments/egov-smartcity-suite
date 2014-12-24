
package org.egov.pims.empLeave.dao;

import java.util.List;

import org.egov.pims.empLeave.model.TypeOfLeaveMaster;

public interface TypeOfLeaveMasterDAO extends org.egov.infstr.dao.GenericDAO
{
	public TypeOfLeaveMaster getTypeOfLeaveMasterByID(Integer id) ;
	public TypeOfLeaveMaster getTypeOfLeaveMasterByName(String  name);
	public List getListOfAccumulativeTypeOfLeaves();
	
	
}

