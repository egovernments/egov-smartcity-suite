package org.egov.pims.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.EgwStatus;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.lib.rjbac.dept.Department;

import org.egov.pims.empLeave.model.TypeOfLeaveMaster;

public  class Helper {
	
	
	public static Integer getLeaveTypeIdforEL()
	
	{
		
		String eisLeaveType=null;
		TypeOfLeaveMaster leaveType=null;
		Integer leaveId=null;
		
		try {
			eisLeaveType=EGovConfig.getProperty("eis_egov_config.xml", "LEAVE_TYPE_NAME_FOR_EL", "","EIS.LEAVE");
			if(eisLeaveType!=null)
			{
			  
			leaveType =  EisManagersUtill.getEmpLeaveService().getTypeOfLeaveMasterByName(eisLeaveType);
			if(leaveType!=null)
			{
				
				leaveId=leaveType.getId();
			}
			}
		} catch (RuntimeException e) {
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
		
		return leaveId;
	
	}
	
	public static Integer getStatusTypeByDescAndModule()
	{
		String statusDesc=null;
		String moduleType=null;
		EgwStatus statusType=null;
		Integer statusId=null;
		
		try {
			//moduleType=EGovConfig.getProperty("eis_egov_config.xml", "STATUS_MODULE_TYPE", "","EIS.STATUS");
			AppConfigValues appConfigValuesForModule = GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("EIS_STATUS","STATUS_MODULE_TYPE",new Date());
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
	/**
	 * isCompOffValid API checks is requested  compensatory off date is within the validity period 
	 * @param workedOn
	 * @param compoffDate
	 * @return Boolean
	 */
	public static Boolean isCompOffValid(Date workedOn,Date compoffDate){
		
		Boolean isCompOffValid=false;
		List<AppConfigValues> compoffValidityList=GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getConfigValuesByModuleAndKey(EisConstants.MODULE_LEAVEAPP, EisConstants.MODULE_KEY_COMPVAL);
		
		Calendar calworkedOnAndThirty=Calendar.getInstance();
		Calendar calComoffDate=Calendar.getInstance();
		calComoffDate.setTime(compoffDate);
		calworkedOnAndThirty.setTime(workedOn);
		calworkedOnAndThirty.add(Calendar.DAY_OF_MONTH,Integer.valueOf(compoffValidityList.get(0).getValue()));
		isCompOffValid=(calworkedOnAndThirty.compareTo(calComoffDate)>0 || calworkedOnAndThirty.compareTo(calComoffDate)==0);		
		return isCompOffValid;
	}
	
}
