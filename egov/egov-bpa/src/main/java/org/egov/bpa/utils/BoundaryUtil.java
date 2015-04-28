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
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.admbndry.BoundaryTypeDAO;
import org.egov.lib.admbndry.HeirarchyTypeDAO;


public class BoundaryUtil {
	
	
	HeirarchyTypeDAO heirarchyTypeDao;
	BoundaryTypeDAO boundaryTypeDao;
	BoundaryDAO boundaryDao;
	//static UserDAO userDao;
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
	}
	 */
	/**
	 * Returns a map of BoundaryType as key and List<Boundary> as value.
	 * If adminBoundaryId and locationBoundaryId is null, then only the top level List<Boundary> values are populated
	 * If either of these values are passed to the method, then all Boundary values, in the hierarchy are added to the
	 * map. The top level list of Boundary is also added.
	 * @param adminBoundaryId
	 * @param locationBoundaryId
	 * @return
	 */ 
	public Map<BoundaryType, List<Boundary>> getMapofBndryTypeAndValues(Long adminBoundaryId, Long locationBoundaryId) {
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
	public Map<String, Long> getSelectedBndryTypeValueMap(Long adminBoundaryId, Long locationBoundaryId) {
		Map<String, Long> selectedBndryTypeValueMap = new HashMap<String, Long>();
		 Boundary boundary;
		 if (adminBoundaryId != null){
			 boundary = boundaryDao.getBoundary(adminBoundaryId);
			
			do {
				selectedBndryTypeValueMap.put(boundary.getBoundaryType().getName(),boundary.getId());
				boundary = boundary.getParent();
			}	while( boundary!= null );	
		}
		 if (locationBoundaryId != null){
			 boundary = boundaryDao.getBoundary(locationBoundaryId);
				
				do {
					selectedBndryTypeValueMap.put(boundary.getBoundaryType().getName(),boundary.getId());
					boundary = boundary.getParent();
				}	while( boundary!= null );		
		}
		
		return selectedBndryTypeValueMap;

	}
	
	
	/**
	 * @param boundaryId
	 * @param boundaryTypeValues
	 */
	private Map<String, List<Boundary>> getMapofBndryTypeNameToBndryValues(Long boundaryId) {
		Map<String, List<Boundary>> boundaryTypeValues = new HashMap<String, List<Boundary>>();
		Boundary boundary = boundaryDao.getBoundary(boundaryId);
		if (boundary == null)
			throw new EGOVRuntimeException("Boundary does not exist for this id -" + boundaryId);
		Boundary parentBoundary = boundary.getParent();
		while( parentBoundary!= null && parentBoundary.getParent() != null ) {
			//((BoundaryType)parentBoundary.getBoundaryType()).getChildBoundaryTypes()// TODOD pHIonix
			boundaryTypeValues.put("",new ArrayList<Boundary>(parentBoundary.getChildren()));
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


