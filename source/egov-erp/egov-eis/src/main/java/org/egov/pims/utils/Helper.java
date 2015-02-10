package org.egov.pims.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.egov.commons.EgwStatus;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.config.dao.AppConfigValuesDAO;

public  class Helper {
	private AppConfigValuesDAO appConfigValuesDAO;
	
	/*public static Integer getStatusTypeByDescAndModule()
	{
		String statusDesc=null;
		String moduleType=null;
		EgwStatus statusType=null;
		Integer statusId=null;
		
		try {
			//moduleType=EGovConfig.getProperty("eis_egov_config.xml", "STATUS_MODULE_TYPE", "","EIS.STATUS");
			AppConfigValues appConfigValuesForModule = appConfigValuesDAO.getAppConfigValueByDate("EIS_STATUS","STATUS_MODULE_TYPE",new Date());
			if(appConfigValuesForModule!=null){
				moduleType= appConfigValuesForModule.getValue();
			}
			//statusDesc=EGovConfig.getProperty("eis_egov_config.xml", "STATUS_DESCRIPTION", "","EIS.STATUS");
			AppConfigValues appConfigValuesForStatusDesc = GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("EIS_STATUS","STATUS_DESCRIPTION",new Date());
			if(appConfigValuesForStatusDesc!=null){
				statusDesc = appConfigValuesForStatusDesc.getValue();
			}
			if(moduleType!=null && statusDesc!=null)
			{
			  
				statusType =  EisManagersUtill.getCommonsService().getStatusByModuleAndCode(moduleType, statusDesc);
				if(statusType!=null)
				{
					
					statusId=statusType.getId();
				}
			}
		} catch (EGOVRuntimeException e) {
			throw e;
		}
		
		return statusId;
	}
	*//**
	 * isCompOffValid API checks is requested  compensatory off date is within the validity period 
	 * @param workedOn
	 * @param compoffDate
	 * @return Boolean
	 *//*
	public static Boolean isCompOffValid(Date workedOn,Date compoffDate){
		
		Boolean isCompOffValid=false;
		List<AppConfigValues> compoffValidityList=appConfigValuesDAO.getConfigValuesByModuleAndKey(EisConstants.MODULE_LEAVEAPP, EisConstants.MODULE_KEY_COMPVAL);
		
		Calendar calworkedOnAndThirty=Calendar.getInstance();
		Calendar calComoffDate=Calendar.getInstance();
		calComoffDate.setTime(compoffDate);
		calworkedOnAndThirty.setTime(workedOn);
		calworkedOnAndThirty.add(Calendar.DAY_OF_MONTH,Integer.valueOf(compoffValidityList.get(0).getValue()));
		isCompOffValid=(calworkedOnAndThirty.compareTo(calComoffDate)>0 || calworkedOnAndThirty.compareTo(calComoffDate)==0);		
		return isCompOffValid;
	}*/
	
}
