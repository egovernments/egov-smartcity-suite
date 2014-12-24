package org.egov.pims.empLeave.dao;

import org.egov.pims.empLeave.model.TypeOfLeaveMaster;
import org.egov.pims.empLeave.model.Wdaysconstnts;


public interface WdaysconstntsDAO extends org.egov.infstr.dao.GenericDAO
{
	public Wdaysconstnts getWdaysconstnts();
	public Wdaysconstnts getWdaysconstntsByID(Integer id) ;
	
}

