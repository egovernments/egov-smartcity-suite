/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.pims.utils;

import org.egov.infra.admin.master.service.AppConfigValueService;
import org.springframework.beans.factory.annotation.Autowired;

public  class Helper {
	 @Autowired
	    private AppConfigValueService appConfigValuesService;
	
	/*public static Integer getStatusTypeByDescAndModule()
	{
		String statusDesc=null;
		String moduleType=null;
		EgwStatus statusType=null;
		Integer statusId=null;
		
		try {
			//moduleType=EGovConfig.getProperty("eis_egov_config.xml", "STATUS_MODULE_TYPE", "","EIS.STATUS");
			AppConfigValues appConfigValuesForModule = appConfigValuesService.getAppConfigValueByDate("EIS_STATUS","STATUS_MODULE_TYPE",new Date());
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
		} catch (ApplicationRuntimeException e) {
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
