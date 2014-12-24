package org.egov.pims.dao;

import java.util.List;

import org.egov.pims.model.GradeMaster;

public interface GradeMasterDao extends org.egov.infstr.dao.GenericDAO
{
	public abstract GradeMaster getGradeMstrById(Integer gradeId) throws Exception;
	public abstract List getAllGradeMstr()throws Exception;
	public abstract List getAllDesgBasedOnGrade(Integer gradeId) throws Exception;
}
