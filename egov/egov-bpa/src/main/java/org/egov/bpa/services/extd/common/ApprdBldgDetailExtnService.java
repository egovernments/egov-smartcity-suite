/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.bpa.services.extd.common;

import org.egov.bpa.models.extd.ApprdBuildingDetailsExtn;
import org.egov.bpa.models.extd.ApprdBuildingFloorDtlsExtn;
import org.egov.bpa.models.extd.RegistrationExtn;
import org.egov.infstr.services.PersistenceService;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Transactional(readOnly=true)
public class ApprdBldgDetailExtnService extends PersistenceService<ApprdBuildingDetailsExtn, Long>{
	private PersistenceService persistenceService;
	
	@Transactional
	public ApprdBuildingDetailsExtn saveApprdBldgDetail(ApprdBuildingDetailsExtn apprdBldgDtls) {
		apprdBldgDtls = (ApprdBuildingDetailsExtn) persist(apprdBldgDtls);
		
		return apprdBldgDtls;
	}
	
	public ApprdBuildingDetailsExtn createApprdBldgDetail(ApprdBuildingDetailsExtn apprdBldgDtls,List<ApprdBuildingFloorDtlsExtn> florDetailList, RegistrationExtn registration) {
		
		if(apprdBldgDtls.getIsBasementUnit() == null) {
			apprdBldgDtls.setIsBasementUnit(false);
		}
		apprdBldgDtls.setRegistration(registration);
		
		
		apprdBldgDtls=buildFlorDetails(apprdBldgDtls,florDetailList);
		if(apprdBldgDtls.getIsBasementUnit()!=null && apprdBldgDtls.getIsBasementUnit()){
			
			for(ApprdBuildingFloorDtlsExtn floorDet1 : florDetailList){
			if(floorDet1.getFloorNum().equals(-2))
			{
				apprdBldgDtls.setIsBasementUnit(true);
				break;
				
			}
			apprdBldgDtls.setIsBasementUnit(false);
			
		}
	}
		
		saveApprdBldgDetail(apprdBldgDtls);
		return apprdBldgDtls;
	}
	
	
	private ApprdBuildingDetailsExtn buildFlorDetails(ApprdBuildingDetailsExtn apprdBldgDtls,List<ApprdBuildingFloorDtlsExtn> unitDetail)
	{
		Set<ApprdBuildingFloorDtlsExtn> unitSet=new HashSet<ApprdBuildingFloorDtlsExtn>();
    	
			for(ApprdBuildingFloorDtlsExtn unitdetail:unitDetail)
	    	{
				if(unitdetail.getExistingBldgUsage()!=null && unitdetail.getExistingBldgUsage().getId()!=null && unitdetail.getExistingBldgUsage().getId()==-1){
					unitdetail.setExistingBldgUsage(null);
				}
				
	    		unitdetail.setApprovedBldgDtls(apprdBldgDtls);
	    		unitSet.add(unitdetail);
	    		
	    	}
			
		apprdBldgDtls.getApprdBuildingFlrDtlsSet().clear();
	  	apprdBldgDtls.getApprdBuildingFlrDtlsSet().addAll(unitSet);
   	
    	return apprdBldgDtls;
    	
	}

	
	
	
	public PersistenceService getPersistenceService() {
		return persistenceService;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
}
