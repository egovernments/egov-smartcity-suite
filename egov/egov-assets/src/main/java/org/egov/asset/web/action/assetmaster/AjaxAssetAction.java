package org.egov.asset.web.action.assetmaster;


import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.asset.model.AssetCategory;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.admbndry.BoundaryTypeDAO;
import org.egov.lib.admbndry.HeirarchyTypeDAO;
import org.egov.web.actions.BaseFormAction;
import org.springframework.beans.factory.annotation.Autowired;

public class AjaxAssetAction extends BaseFormAction{
	
	private static final Logger LOGGER = Logger.getLogger(AjaxAssetAction.class);
	private Long assetTypeId;	// Set by Ajax call
	private Long areaId;			// Set by Ajax call
	private Long locationId;		// Set by Ajax call
	private Long categoryId;	// Set by Ajax call
	private Long zoneId;	    // Set by Ajax call
	private Long wardId;         // Set by Ajax call
	private List<Boundary> locationList = new LinkedList<Boundary>();
	private List<Boundary> zoneList = new LinkedList<Boundary>();
	private List<Boundary> streetList = new LinkedList<Boundary>();
	private List<Boundary> street2List = new LinkedList<Boundary>();
	private List<Boundary> areaList = new LinkedList<Boundary>();
	private AssetCategory assetCategory;
	private List<AssetCategory> assetCategoryList;
	public static final String LOCATIONS = "locations";
	public static final String WARDS = "wards";
	public static final String AREAS = "areas";
	public static final String STREETS = "streets";
	public static final String STREETS2 = "streets2";
	public static final String ASSET_CAT_DETAILS = "assetcatdetails";
	public static final String PARENT_CATEGORIES = "parentcategories";
	private String hierarchyTypeName = "LOCATION";
	private static final String AREA_BOUNDARY_TYPE = "Area";
	private String value;
	private Integer departmentId;
	private static  SimpleDateFormat FORMATTER = new SimpleDateFormat("dd-MMM-yyyy");
	private Long parentCatId;	// Set by Ajax call
	private List<AssetCategory> assetSubCategoryList;
	public static final String SUB_CATEGORIES = "subcategories";
	
	@Autowired
        private HeirarchyTypeDAO heirarchyTypeDAO;
        @Autowired
        private BoundaryDAO boundaryDAO;
        @Autowired
        private BoundaryTypeDAO boundaryTypeDAO;

	public Object getModel() {
		return null;
	}
	
	public String execute(){
		return SUCCESS;
	}
	/**
	 * Populate the Area list by ward
	 */
	public String populateArea(){
		Boundary boundary = boundaryDAO.getBoundaryInclgHxById(wardId);

		if(wardId!=-1) {
			areaList.add(boundaryDAO.getBoundaryInclgHxById(boundary.getParent().getId()));
		}
		else {
			areaList=Collections.EMPTY_LIST;
		}
		
		LOGGER.info("***********Ajax AreaList: " + areaList.toString());
		return AREAS;
	}
	/**
	 * Populate the Area list by Location
	 */
	public String populateAreaByLocation(){
	    HierarchyType hType = null;
		try{	
			hType = heirarchyTypeDAO.getHierarchyTypeByName(hierarchyTypeName);
		}catch(Exception e){
			LOGGER.error("Error while loading areas - areas." + e.getMessage());
			addFieldError("areas", "Unable to load areas information");
			throw new EGOVRuntimeException("Unable to load areas information",e);
		}
		BoundaryType bType = boundaryTypeDAO.getBoundaryType(AREA_BOUNDARY_TYPE,hType);
		areaList = boundaryDAO.getAllBoundariesInclgHxByBndryTypeId(bType.getId());
		LOGGER.info("***********Ajax AreaList: " + areaList.toString());
		return AREAS;
	}
	
	/**
	 * Populate the location list by area 
	 */
	public String populateLocations(){
		try{	
			if(areaId!=-1) {
			Boundary boundary = boundaryDAO.getBoundaryInclgHxById(areaId);
			locationList.add(boundaryDAO.getBoundaryInclgHxById(boundary.getParent().getId()));
			}
			else {
				locationList=Collections.EMPTY_LIST;
			}
		}catch(Exception e){
			LOGGER.error("Error while loading locations - locations." + e.getMessage());
			addFieldError("location", "Unable to load location information");
			throw new EGOVRuntimeException("Unable to load location information",e);
		}
		LOGGER.info("***********Ajax locationList: " + locationList.toString());
		return LOCATIONS;
	}
	
	/**
	 * Populate the ward list by  zone
	 */
	public String populateWard(){
		try{	
			zoneList = boundaryDAO.getChildBoundariesInclgHx(zoneId);
		}catch(Exception e){
			LOGGER.error("Error while loading warda - wards." + e.getMessage());
			addFieldError("location", "Unable to load ward information");
			throw new EGOVRuntimeException("Unable to load ward information",e);
		}
		LOGGER.info("***********Ajax ward: " + locationList.toString());
		return WARDS;
	}
	
	/**
	 * Populate the street list by Ward 
	 * @throws Exception 
	 * @throws Exception 
	 */
	public String populateStreets() throws Exception{
	        HierarchyType hType = null;
		try{	
			hType = heirarchyTypeDAO.getHierarchyTypeByName(hierarchyTypeName);
		}catch(Exception e){
			LOGGER.error("Error while loading Streets." + e.getMessage());
			addFieldError("streets", "Unable to load Streets Information");
			throw new EGOVRuntimeException("Unable to load Streets information",e);
		}
		BoundaryType childBoundaryType = boundaryTypeDAO.getBoundaryType("Street", hType);
		if(wardId!=-1) {
			Boundary parentBoundary = boundaryDAO.getBoundaryInclgHxById(wardId);
			Set<Boundary> boundarySet=boundaryDAO.getCrossHeirarchyChildren(parentBoundary, childBoundaryType);
			streetList =new LinkedList<Boundary>(boundarySet);
		}
		else {
			streetList=Collections.EMPTY_LIST;
		}
			//streetList = new LinkedList(boundaryDAO.getCrossHeirarchyChildren(parentBoundary, childBoundaryType));		
		return STREETS;
	}
	/**
	 * Populate the street list by Ward 
	 * @throws Exception 
	 * @throws Exception 
	 */
	public String populateStreetsByLocation() throws Exception{
		if(locationId!=-1)
			street2List = boundaryDAO.getChildBoundariesInclgHx(locationId);		 
		return STREETS2;
	}
	
	public String populateCategoryDetails(){
		assetCategory = (AssetCategory)getPersistenceService().find("from AssetCategory where id=?", categoryId);
		return ASSET_CAT_DETAILS;
	}
	
	public String populateParentCategories(){
		if(assetTypeId==null || assetTypeId==-1)
			assetCategoryList = getPersistenceService().findAllBy("from AssetCategory");
		else
			assetCategoryList = getPersistenceService()
									.findAllBy("from AssetCategory where assetType.id=?", assetTypeId);
		return PARENT_CATEGORIES;
	}
	
	public String populateParentAssetCategoryList(){
		if(assetTypeId==null || assetTypeId==-1)
			assetCategoryList = getPersistenceService().findAllBy("from AssetCategory where parent is null");
		else
			assetCategoryList = getPersistenceService().findAllBy("from AssetCategory where parent is null and assetType.id=?", assetTypeId);
		return PARENT_CATEGORIES;
	}

		
	public String populateSubCategories(){
		if(parentCatId!=-1 && parentCatId!=null)
			assetSubCategoryList = getPersistenceService()
									.findAllBy("from AssetCategory where parent.id=?", parentCatId);
		else
			assetSubCategoryList = Collections.emptyList();
		return SUB_CATEGORIES;
	}
	
	/**
	 * Populate the location list by area - View Asset Screen 
	 */
	public String populateLocationsByArea(){
		try{	
			if(areaId!=-1) {
				locationList = boundaryDAO.getChildBoundariesInclgHx(areaId);
			}
			else {
				locationList=Collections.EMPTY_LIST;
			}
		}catch(Exception e){
			LOGGER.error("Error while loading locations - locations." + e.getMessage());
			addFieldError("location", "Unable to load location information");
			throw new EGOVRuntimeException("Unable to load location information",e);
		}
		LOGGER.info("***********Ajax locationList: " + locationList.toString());
		return LOCATIONS;
	}
	
	// Property accessors
	
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	
	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}
	
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public void setAssetTypeId(Long assetTypeId) {
		this.assetTypeId = assetTypeId;
	}
	
	public List<Boundary> getLocationList() {
		return locationList;
	}

	public List<Boundary> getStreetList() {
		return streetList;
	}
	public List<Boundary> getStreet2List() {
		return street2List;
	}
	public AssetCategory getAssetCategory() {
		return assetCategory;
	}

	public List<AssetCategory> getAssetCategoryList() {
		return assetCategoryList;
	}

	public List<Boundary> getZoneList() {
		return zoneList;
	}

	
	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}

	public void setWardId(Long wardId) {
		this.wardId = wardId;
	}

	public void setHierarchyTypeName(String hierarchyTypeName) {
		this.hierarchyTypeName = hierarchyTypeName;
	}

	public List<Boundary> getAreaList() {
		return areaList;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	
	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public List<AssetCategory> getAssetSubCategoryList() {
		return assetSubCategoryList;
	}

	public void setAssetSubCategoryList(List<AssetCategory> assetSubCategoryList) {
		this.assetSubCategoryList = assetSubCategoryList;
	}

	public Long getParentCatId() {
		return parentCatId;
	}

	public void setParentCatId(Long parentCatId) {
		this.parentCatId = parentCatId;
	}
	
}
