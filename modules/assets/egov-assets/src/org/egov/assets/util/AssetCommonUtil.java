/**
 * 
 */
package org.egov.assets.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.EgwStatus;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.admbndry.BoundaryTypeDAO;
import org.egov.lib.admbndry.HeirarchyType;
import org.egov.lib.admbndry.HeirarchyTypeDAO;
import org.hibernate.Session;

/**
 * @author manoranjan
 *
 */
public class AssetCommonUtil {

	private static final Logger LOGGER = Logger.getLogger(AssetCommonUtil.class);
	private static final String ADMIN_HIERARCHY_TYPE = "ADMINISTRATION";
	private static final String Zone_BOUNDARY_TYPE = "Zone";
	private static String hierarchyTypeName = "LOCATION";
	
	
	@SuppressWarnings("unchecked")
	public static List<Boundary> getAllZoneOfHTypeAdmin() {
	    HeirarchyType hType = null;
		try{	
			hType = new HeirarchyTypeDAO().getHierarchyTypeByName(ADMIN_HIERARCHY_TYPE);
		}catch(EGOVException e){
			LOGGER.error("Error_While_Loading_HeirarchyType"+ e.getMessage());
			throw new EGOVRuntimeException("Unable_To_Load_Heirarchy_Information",e);
		}
		List<Boundary> zoneList = null;
		BoundaryType bType = new BoundaryTypeDAO().getBoundaryType(Zone_BOUNDARY_TYPE,hType);
		zoneList = new BoundaryDAO().getAllBoundariesByBndryTypeId(bType.getId());
		return zoneList;
	}
	
	/**
	 * Populate the ward list by  zone
	 */
	public static List<Boundary> populateWard(Integer zoneId){
		List<Boundary> wardList = new LinkedList<Boundary>();
		try{	
			wardList = new BoundaryDAO().getChildBoundaries(String.valueOf(zoneId));
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
	public static List<Boundary> populateArea(int wardId ){
		HeirarchyType hType = null;
		List<Boundary> areaList = new LinkedList<Boundary>();
		try{	
			hType = new HeirarchyTypeDAO().getHierarchyTypeByName(hierarchyTypeName);
		}catch(Exception e){
			LOGGER.error("Error while loading areas - areas." + e.getMessage());
			throw new EGOVRuntimeException("Unable to load areas information",e);
		}
		BoundaryType childBoundaryType = new BoundaryTypeDAO().getBoundaryType("Area", hType);
		Boundary parentBoundary = new BoundaryDAO().getBoundaryById(wardId);
		areaList = new LinkedList(new BoundaryDAO().getCrossHeirarchyChildren(parentBoundary, childBoundaryType));
		
		LOGGER.info("***********Ajax AreaList: " + areaList.toString());
		return areaList;
	}
	
	/**
	 * Populate the street list by Ward 
	 * @throws Exception 
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public static List<Boundary> populateStreets(int wardId){
		HeirarchyType hType = null;
		List<Boundary> streetList = new LinkedList<Boundary>();
		try{	
			hType = new HeirarchyTypeDAO().getHierarchyTypeByName(hierarchyTypeName);
		}catch(Exception e){
			LOGGER.error("Error while loading Streets." + e.getMessage());
			throw new EGOVRuntimeException("Unable to load Streets information",e);
		}
		BoundaryType childBoundaryType = new BoundaryTypeDAO().getBoundaryType("Street", hType);
		Boundary parentBoundary = new BoundaryDAO().getBoundaryById(wardId);
		streetList = new LinkedList(new BoundaryDAO().getCrossHeirarchyChildren(parentBoundary, childBoundaryType));		
		return streetList;
	}
	
	
	/**
	 * Populate the location list by area 
	 */
	public static List<Boundary>  populateLocations(String areaId){
		 List<Boundary> locationList = new LinkedList<Boundary>();
		try{	
			locationList = new BoundaryDAO().getChildBoundaries(areaId);
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

public static StringBuffer getAssetRegisterReportquery(String whereCondquery){
		
	StringBuffer captzQuery = new StringBuffer(600);
	StringBuffer assetAllQuery = new StringBuffer(300);
	StringBuffer assethvngChldActvQuery = new StringBuffer(300);
	StringBuffer assetHvCapQry = new StringBuffer(300);
	StringBuffer query1 = new StringBuffer(1500);
	StringBuffer query2 = new StringBuffer(1500);
		
	captzQuery.append("select asset.code,asset.name,b1.name as zone,b2.name as word ,b3.name as area,b4.name location, b5.name as steet,");
	captzQuery.append(" dept.dept_name ,status.description,SUM(assetdep.amount),assetactv.additionamount,assetactv.activitydate");
	captzQuery.append(" from eg_asset asset , egasset_depreciation assetdep, egasset_activities assetactv,eg_boundary  b1,");
	captzQuery.append(" eg_boundary  b2,eg_boundary  b3,eg_boundary  b4,eg_boundary b5 ,eg_department dept,egw_status status ");
	captzQuery.append(" where asset.id= assetdep.assetid(+) and asset.id = assetactv.assetid and b1.id_bndry(+) =b2.parent ");
	captzQuery.append(" and b2.id_bndry(+) = asset.ward_id and b3.id_bndry(+) = asset.area_id and b4.id_bndry(+) = asset.location_id ");
	captzQuery.append(" and b5.id_bndry(+) =asset.street_id and dept.id_dept(+) = asset.departmentid and  status.id= asset.statusid ");
	captzQuery.append(" and assetactv.identifier='C' and "+whereCondquery+"GROUP BY asset.code,asset.name,b1.name, b2.name, b3.name, b4.name,b5.name,");
	captzQuery.append(" dept.dept_name,status.description, assetactv.additionamount,assetactv.activitydate ");
	
	assetAllQuery.append(" select asset.code,asset.name,b1.name as zone,b2.name as word ,b3.name as area,b4.name location,");
	assetAllQuery.append(" b5.name as steet, dept.dept_name ,status.description,null,null,null from eg_asset asset ,eg_boundary  b1,");
	assetAllQuery.append(" eg_boundary  b2,eg_boundary  b3,eg_boundary  b4,eg_boundary b5 ,eg_department dept,egw_status status");
	assetAllQuery.append(" where b1.id_bndry(+) =b2.parent and b2.id_bndry(+) = asset.ward_id and");
	assetAllQuery.append(" b3.id_bndry(+) = asset.area_id and b4.id_bndry(+) = asset.location_id and b5.id_bndry(+) =asset.street_id");
	assetAllQuery.append(" and dept.id_dept(+) = asset.departmentid and status.id= asset.statusid and " + whereCondquery);
	
	assethvngChldActvQuery.append("select asset.code,asset.name,b1.name as zone,b2.name as word ,b3.name as area,b4.name location,");
	assethvngChldActvQuery.append("b5.name as steet, dept.dept_name ,status.description,null,null,null from eg_asset asset ,eg_boundary  b1");
	assethvngChldActvQuery.append(",eg_boundary  b2,eg_boundary  b3,eg_boundary  b4,eg_boundary b5 ,egasset_activities assetactv,");
	assethvngChldActvQuery.append(" eg_department dept,egw_status status where asset.id = assetactv.assetid  and b1.id_bndry(+) =b2.parent ");
	assethvngChldActvQuery.append(" and b2.id_bndry(+) = asset.ward_id and b3.id_bndry(+) = asset.area_id and b4.id_bndry(+) = asset.location_id");
	assethvngChldActvQuery.append(" and b5.id_bndry(+) =asset.street_id and dept.id_dept(+) = asset.departmentid and status.id= asset.statusid and "+whereCondquery);
	
	
	query1.append(captzQuery.toString()+" UNION ("+assetAllQuery.toString() +" MINUS "+assethvngChldActvQuery.toString() +" ) ");
	
	assetHvCapQry.append("select asset.code,asset.name,b1.name as zone,b2.name as word ,b3.name as area,b4.name location,");;
	assetHvCapQry.append(" b5.name as steet, dept.dept_name ,status.description,null,null,null");
	assetHvCapQry.append(" from eg_asset asset , egasset_depreciation assetdep, egasset_activities assetactv,eg_boundary  b1,");
	assetHvCapQry.append(" eg_boundary  b2,eg_boundary  b3,eg_boundary  b4,eg_boundary b5 ,eg_department dept,egw_status status");
	assetHvCapQry.append(" where asset.id= assetdep.assetid(+) and asset.id = assetactv.assetid and b1.id_bndry(+) =b2.parent");
	assetHvCapQry.append(" and b2.id_bndry(+) = asset.ward_id and b3.id_bndry(+) = asset.area_id and b4.id_bndry(+) = asset.location_id");
	assetHvCapQry.append(" and b5.id_bndry(+) =asset.street_id and dept.id_dept(+) = asset.departmentid and");
	assetHvCapQry.append("  status.id= asset.statusid and assetactv.identifier='C' and "+ whereCondquery);
	
	query2.append(assetAllQuery.toString()+"MINUS("+assetHvCapQry.toString()+" UNION("+assetAllQuery.toString() +" MINUS "+assethvngChldActvQuery.toString()+") )");
	
	
	return query1.append("UNION("+query2+")");
	}
}
