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
package org.egov.bpa.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.admbndry.BoundaryTypeDAO;
import org.egov.lib.admbndry.HeirarchyTypeDAO;
//import org.egov.lib.rjbac.user.UserRole;
//import org.egov.lib.rjbac.user.dao.UserDAO;

public class BoundaryUtil {
	
	
	HeirarchyTypeDAO heirarchyTypeDao;
	BoundaryTypeDAO boundaryTypeDao;
	BoundaryDAO boundaryDao;
	
	static String userRole;
	public HeirarchyTypeDAO getHeirarchyTypeDao() {
		return heirarchyTypeDao;
	}
	public void setHeirarchyTypeDao(HeirarchyTypeDAO heirarchyTypeDao) {
		this.heirarchyTypeDao = heirarchyTypeDao;
	}
	
	 public BoundaryTypeDAO getBoundaryTypeDao() {
		return boundaryTypeDao;
	}
	public void setBoundaryTypeDao(BoundaryTypeDAO boundaryTypeDao) {
		this.boundaryTypeDao = boundaryTypeDao;
	}
	
	public BoundaryDAO getBoundaryDao() {
		return boundaryDao;
	}
	public void setBoundaryDao(BoundaryDAO boundaryDao) {
		this.boundaryDao = boundaryDao;
	}
	/*public UserDAO getUserDao() {
		return userDao;
	}
	public void setUserDao(UserDAO userDao) {
		this.userDao = userDao;
	}*/
	 
	/**
	 * Returns a map of BoundaryType as key and List<Boundary> as value.
	 * If adminBoundaryId and locationBoundaryId is null, then only the top level List<Boundary> values are populated
	 * If either of these values are passed to the method, then all Boundary values, in the hierarchy are added to the
	 * map. The top level list of Boundary is also added.
	 * @param adminBoundaryId
	 * @param locationBoundaryId
	 * @return
	 */ 
	public Map<BoundaryType, List<Boundary>> getMapofBndryTypeAndValues(Integer adminBoundaryId, Integer locationBoundaryId) {
		Map<BoundaryType, List<Boundary>> bndryTypeValuesMap = new LinkedHashMap<BoundaryType, List<Boundary>>();
		Set<HierarchyType> heirarchyTypeSet =heirarchyTypeDao.getAllHeirarchyTypes();
		
		 Map<String,List <Boundary>> boundaryTypeValues = new HashMap<String,List <Boundary>>();
		
		 if (adminBoundaryId != null){
			 boundaryTypeValues.putAll(getMapofBndryTypeNameToBndryValues(adminBoundaryId));		
		}
		 if (locationBoundaryId != null){
			 boundaryTypeValues.putAll(getMapofBndryTypeNameToBndryValues(locationBoundaryId));		
		}
		for(HierarchyType hType: heirarchyTypeSet){
			List<BoundaryType> bTypes = boundaryTypeDao.getParentBoundaryTypeByHirarchy(hType);
			boundaryTypeValues.putAll( boundaryTypeDao.getSecondLevelBoundaryByPassingHeirarchy(hType));

			for (BoundaryType bType: bTypes) {
				
				if (boundaryTypeValues.containsKey(bType.getName())){
					bndryTypeValuesMap.put(bType, boundaryTypeValues.get(bType.getName()));
				}else
					bndryTypeValuesMap.put(bType, new ArrayList<Boundary>());
			}
			
		}
		return bndryTypeValuesMap;

	}
	
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public Map<String, Integer> getSelectedBndryTypeValueMap(Long adminBoundaryId, Long locationBoundaryId) {
		Map<String, Integer> selectedBndryTypeValueMap = new HashMap<String, Integer>();
		 Boundary boundary;
		 if (adminBoundaryId != null){
			 boundary = boundaryDao.getBoundary(adminBoundaryId);
			
			do {
				selectedBndryTypeValueMap.put(boundary.getBoundaryType().getName(),(int)Long.parseLong(boundary.getId().toString()));
				boundary = boundary.getParent();
			}	while( boundary!= null );	
		}
		 if (locationBoundaryId != null){
			 boundary = boundaryDao.getBoundary(locationBoundaryId);
				
				do {
					selectedBndryTypeValueMap.put(boundary.getBoundaryType().getName(),(int)Long.parseLong(boundary.getId().toString()));
					boundary = boundary.getParent();
				}	while( boundary!= null );		
		}
		
		return selectedBndryTypeValueMap;

	}
	
	
	/**
	 * @param boundaryId
	 * @param boundaryTypeValues
	 */
	private Map<String, List<Boundary>> getMapofBndryTypeNameToBndryValues(Integer boundaryId) {
		Map<String, List<Boundary>> boundaryTypeValues = new HashMap<String, List<Boundary>>();
		Boundary boundary =null;// after commenting UserDao  giving error for initialize boundaryDao
		//= boundaryDao.getBoundary(Long.parseLong(boundary.getId().toString()));
		if (boundary == null)
			throw new EGOVRuntimeException("Boundary does not exist for this id -" + boundaryId);
		Boundary parentBoundary = boundary.getParent();
		while( parentBoundary!= null && parentBoundary.getParent() != null ) {
			boundaryTypeValues.put(((BoundaryType)parentBoundary.getBoundaryType()).getName(),new ArrayList<Boundary>(parentBoundary.getChildren()));
			//TODO: REMOVING as not sure which Column refers getFirstChild  variable parentBoundary.getBoundaryType()).getFirstChild().getName()
			
			parentBoundary = parentBoundary.getParent();
		}
		return boundaryTypeValues;
	}

	/*public static String getRolesForLoggedInUser()
	{
	 UserImpl user = (UserImpl) userDao.getUserByID(Integer.parseInt(EGOVThreadLocals.getUserId()));
		if (user != null) {
			for (UserRole role : user.getUserRoles()) {
				userRole = ((role!=null && role.getRole()!=null) ?role.getRole().getRoleName():"");
		
			}
			return userRole;
		}
	return null;
	
	
 }	*/
}


