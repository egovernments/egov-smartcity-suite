package org.egov.assets.web.actions.assetmaster;


import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.assets.model.AssetCategory;
import org.egov.assets.service.CommonAssetsService;
import org.egov.assets.util.AssetConstants;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.admbndry.BoundaryTypeDAO;
import org.egov.lib.admbndry.HeirarchyType;
import org.egov.lib.admbndry.HeirarchyTypeDAO;
import org.egov.services.financingsource.FinancingSourceService;
import org.egov.utils.Constants;
import org.egov.web.actions.BaseFormAction;

public class AjaxAssetAction extends BaseFormAction{
	
	private static final Logger LOGGER = Logger.getLogger(AjaxAssetAction.class);
	private Long assetTypeId;	// Set by Ajax call
	private int areaId;			// Set by Ajax call
	private int locationId;		// Set by Ajax call
	private Long categoryId;	// Set by Ajax call
	private Long zoneId;	    // Set by Ajax call
	private int wardId;         // Set by Ajax call
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
	private CommonAssetsService commonAssetsService;
	private String value;
	private List<Scheme> schemeList;
	private Integer fundId;
	private Integer schemeId;
	private List<SubScheme> subSchemes;
	private Integer subSchemeId;
	private List<Fundsource> fundSouceList;
	private FinancingSourceService financingSourceService;
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
		HeirarchyType hType = null;
		try{	
			hType = new HeirarchyTypeDAO().getHierarchyTypeByName(hierarchyTypeName);
		}catch(Exception e){
			LOGGER.error("Error while loading areas - areas." + e.getMessage());
			addFieldError("areas", "Unable to load areas information");
			throw new EGOVRuntimeException("Unable to load areas information",e);
		}
		BoundaryType childBoundaryType = new BoundaryTypeDAO().getBoundaryType("Area", hType);
		Boundary parentBoundary = new BoundaryDAO().getBoundaryById(wardId);
		if(wardId!=-1)
		areaList = new LinkedList(new BoundaryDAO().getCrossHeirarchyChildren(parentBoundary, childBoundaryType));
		
		LOGGER.info("***********Ajax AreaList: " + areaList.toString());
		return AREAS;
	}
	/**
	 * Populate the Area list by Location
	 */
	public String populateAreaByLocation(){
		HeirarchyType hType = null;
		try{	
			hType = new HeirarchyTypeDAO().getHierarchyTypeByName(hierarchyTypeName);
		}catch(Exception e){
			LOGGER.error("Error while loading areas - areas." + e.getMessage());
			addFieldError("areas", "Unable to load areas information");
			throw new EGOVRuntimeException("Unable to load areas information",e);
		}
		BoundaryType bType = new BoundaryTypeDAO().getBoundaryType(AREA_BOUNDARY_TYPE,hType);
		areaList = new BoundaryDAO().getAllBoundariesByBndryTypeId(bType.getId());
		LOGGER.info("***********Ajax AreaList: " + areaList.toString());
		return AREAS;
	}
	
	/**
	 * Populate the location list by area 
	 */
	public String populateLocations(){
		try{	
			locationList = new BoundaryDAO().getChildBoundaries(String.valueOf(areaId));
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
			zoneList = new BoundaryDAO().getChildBoundaries(String.valueOf(zoneId));
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
		HeirarchyType hType = null;
		try{	
			hType = new HeirarchyTypeDAO().getHierarchyTypeByName(hierarchyTypeName);
		}catch(Exception e){
			LOGGER.error("Error while loading Streets." + e.getMessage());
			addFieldError("streets", "Unable to load Streets Information");
			throw new EGOVRuntimeException("Unable to load Streets information",e);
		}
		BoundaryType childBoundaryType = new BoundaryTypeDAO().getBoundaryType("Street", hType);
		Boundary parentBoundary = new BoundaryDAO().getBoundaryById(wardId);
		if(wardId!=-1)
			streetList = new LinkedList(new BoundaryDAO().getCrossHeirarchyChildren(parentBoundary, childBoundaryType));		
		return STREETS;
	}
	/**
	 * Populate the street list by Ward 
	 * @throws Exception 
	 * @throws Exception 
	 */
	public String populateStreetsByLocation() throws Exception{
		if(locationId!=-1)
			street2List = new BoundaryDAO().getChildBoundaries(String.valueOf(locationId));		 
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

	public String  getAssetValueToDate() {
		LOGGER.debug("AjaxAssetAction |  getAssetValueToDate | start");
		LOGGER.debug("AjaxAssetAction |  getAssetValueToDate | Date = "+ parameters.get("revaldate")[0]+"asset id="+parameters.get("assetid")[0] );
		try {
			Date reValDate = AssetConstants.DDMMYYYYFORMATS.parse(parameters.get("revaldate")[0]);
			BigDecimal amount = commonAssetsService.getAssetValueToDate(Long.valueOf(parameters.get("assetid")[0]), reValDate);
			value  = ""+amount;
		} catch (Exception e) {
			LOGGER.debug("AjaxAssetAction |  getAssetValueToDate |"+e);
		}
	
		return "result";
	}
	
	@SuppressWarnings("unchecked")
	public String ajaxLoadSchemes()
	{
		LOGGER.debug("Fund Id received is : " + fundId);
		if (null == fundId) {
			schemeList = getPersistenceService().findAllBy(
					" from Scheme where fund.id=? and isActive=1 order by name", -1);
		} else {
			schemeList = getPersistenceService().findAllBy(" from Scheme where fund.id=? and isActive=1 order by name",fundId);
		}
		LOGGER.debug("Scheme List size : " + schemeList.size());
		return "schemes";
	}

	@SuppressWarnings("unchecked")
	public String ajaxLoadSubSchemes()
	{
		LOGGER.debug("Scheme Id received is : "+schemeId);
		if(null != schemeId && schemeId !=-1){
			subSchemes = getPersistenceService().findAllBy("from SubScheme where scheme.id=? and isActive=1 order by name", schemeId);
			LOGGER.debug("Subscheme List size : "+subSchemes.size());
		}else{
			subSchemes = Collections.EMPTY_LIST;
		}

		return "subSchemes";
	}
	public String ajaxLoadFundSource(){
		LOGGER.debug("CommonAction | subscheme id received = "+ subSchemeId);
		if(null != subSchemeId){
			fundSouceList = financingSourceService.getFinancialSourceBasedOnSubScheme(subSchemeId);
		}
		return "fundsource";
	}
	// Property accessors
	
	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}
	
	public void setLocationId(int locationId) {
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

	public void setWardId(int wardId) {
		this.wardId = wardId;
	}

	public void setHierarchyTypeName(String hierarchyTypeName) {
		this.hierarchyTypeName = hierarchyTypeName;
	}

	public List<Boundary> getAreaList() {
		return areaList;
	}

	public void setCommonAssetsService(CommonAssetsService commonAssetsService) {
		this.commonAssetsService = commonAssetsService;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<Scheme> getSchemeList() {
		return schemeList;
	}

	public void setSchemeList(List<Scheme> schemeList) {
		this.schemeList = schemeList;
	}

	public Integer getFundId() {
		return fundId;
	}

	public void setFundId(Integer fundId) {
		this.fundId = fundId;
	}

	public Integer getSchemeId() {
		return schemeId;
	}

	public List<SubScheme> getSubSchemes() {
		return subSchemes;
	}

	public void setSchemeId(Integer schemeId) {
		this.schemeId = schemeId;
	}

	public void setSubSchemes(List<SubScheme> subSchemes) {
		this.subSchemes = subSchemes;
	}

	public Integer getSubSchemeId() {
		return subSchemeId;
	}

	public List<Fundsource> getFundSouceList() {
		return fundSouceList;
	}

	public void setSubSchemeId(Integer subSchemeId) {
		this.subSchemeId = subSchemeId;
	}

	public void setFundSouceList(List<Fundsource> fundSouceList) {
		this.fundSouceList = fundSouceList;
	}

	public void setFinancingSourceService(
			FinancingSourceService financingSourceService) {
		this.financingSourceService = financingSourceService;
	}
	
}
