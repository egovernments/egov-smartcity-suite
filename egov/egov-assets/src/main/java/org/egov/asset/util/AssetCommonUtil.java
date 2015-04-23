/**
 * 
 */
package org.egov.asset.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.admbndry.BoundaryTypeDAO;
import org.egov.lib.admbndry.HeirarchyTypeDAO;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author manoranjan
 *
 */
public class AssetCommonUtil {

	private static final Logger LOGGER = Logger.getLogger(AssetCommonUtil.class);
	private static final String ADMIN_HIERARCHY_TYPE = "ADMINISTRATION";
	private static final String Zone_BOUNDARY_TYPE = "Zone";
	private static String hierarchyTypeName = "LOCATION";
	@Autowired
	private BoundaryDAO boundaryDAO;
	@Autowired
	private BoundaryTypeDAO boundaryTypeDAO;
	@Autowired
	private HeirarchyTypeDAO heirarchyTypeDAO;
	
	@SuppressWarnings("unchecked")
	public List<Boundary> getAllZoneOfHTypeAdmin() {
	    HierarchyType hType = null;
		try{	
			hType = heirarchyTypeDAO.getHierarchyTypeByName(ADMIN_HIERARCHY_TYPE);
		}catch(EGOVException e){
			LOGGER.error("Error_While_Loading_HeirarchyType"+ e.getMessage());
			throw new EGOVRuntimeException("Unable_To_Load_Heirarchy_Information",e);
		}
		List<Boundary> zoneList = null;
		BoundaryType bType = boundaryTypeDAO.getBoundaryType(Zone_BOUNDARY_TYPE,hType);
		zoneList = boundaryDAO.getAllBoundariesInclgHxByBndryTypeId(bType.getId());
		return zoneList;
	}
	
	/**
	 * Populate the ward list by  zone
	 */
	public List<Boundary> populateWard(Long zoneId){
		List<Boundary> wardList = new LinkedList<Boundary>();
		try{	
			wardList = boundaryDAO.getChildBoundariesInclgHx(zoneId);
		}catch(Exception e){
			LOGGER.error("Error while loading warda - wards." + e.getMessage());
			throw new EGOVRuntimeException("Unable to load ward information",e);
		}
		return wardList;
	}
	
	
	/**
	 * Populate the Area list by ward
	 */
	@SuppressWarnings("unchecked")
	public List<Boundary> populateArea(Long wardId ){
	        HierarchyType hType = null;
		List<Boundary> areaList = new LinkedList<Boundary>();
		try{	
			hType = heirarchyTypeDAO.getHierarchyTypeByName(hierarchyTypeName);
		}catch(Exception e){
			LOGGER.error("Error while loading areas - areas." + e.getMessage());
			throw new EGOVRuntimeException("Unable to load areas information",e);
		}
		BoundaryType childBoundaryType = boundaryTypeDAO.getBoundaryType("Area", hType);
		Boundary parentBoundary = boundaryDAO.getBoundaryInclgHxById(wardId);
		areaList = new LinkedList(boundaryDAO.getCrossHeirarchyChildren(parentBoundary, childBoundaryType));
		
		LOGGER.info("***********Ajax AreaList: " + areaList.toString());
		return areaList;
	}
	
	/**
	 * Populate the street list by Ward 
	 * @throws Exception 
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public List<Boundary> populateStreets(Long wardId){
	        HierarchyType hType = null;
		List<Boundary> streetList = new LinkedList<Boundary>();
		try{	
			hType = heirarchyTypeDAO.getHierarchyTypeByName(hierarchyTypeName);
		}catch(Exception e){
			LOGGER.error("Error while loading Streets." + e.getMessage());
			throw new EGOVRuntimeException("Unable to load Streets information",e);
		}
		BoundaryType childBoundaryType = boundaryTypeDAO.getBoundaryType("Street", hType);
		Boundary parentBoundary = boundaryDAO.getBoundaryInclgHxById(wardId);
		streetList = new LinkedList(boundaryDAO.getCrossHeirarchyChildren(parentBoundary, childBoundaryType));		
		return streetList;
	}
	
	
	/**
	 * Populate the location list by area 
	 */
	public List<Boundary>  populateLocations(Long areaId){
		 List<Boundary> locationList = new LinkedList<Boundary>();
		try{	
			locationList = boundaryDAO.getChildBoundariesInclgHx(areaId);
		}catch(Exception e){
			LOGGER.error("Error while loading locations - locations." + e.getMessage());
			throw new EGOVRuntimeException("Unable to load location information",e);
		}
		LOGGER.info("***********Ajax locationList: " + locationList.toString());
		return locationList;
	}
	
	public static Date loadCurrentDate(){
		final Date currDate = new Date();
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			return sdf.parse(sdf.format(currDate));
		} catch (final ParseException e) {
			throw new ValidationException(Arrays.asList(new ValidationError("Exception while formatting voucher date","Transaction failed")));
		}
	}

    public void setBoundaryDAO(BoundaryDAO boundaryDAO) {
        this.boundaryDAO = boundaryDAO;
    }

    public void setBoundaryTypeDAO(BoundaryTypeDAO boundaryTypeDAO) {
        this.boundaryTypeDAO = boundaryTypeDAO;
    }

    public void setHeirarchyTypeDAO(HeirarchyTypeDAO heirarchyTypeDAO) {
        this.heirarchyTypeDAO = heirarchyTypeDAO;
    }	

}
