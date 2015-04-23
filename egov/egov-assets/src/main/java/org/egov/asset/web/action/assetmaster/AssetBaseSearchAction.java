/**
 * 
 */
package org.egov.asset.web.action.assetmaster;

import java.util.Collections;

import org.apache.log4j.Logger;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetType;
import org.egov.asset.util.AssetCommonUtil;
import org.egov.commons.EgwStatus;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infstr.search.SearchQuery;
import org.egov.web.actions.SearchFormAction;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author manoranjan
 *
 */
public abstract class AssetBaseSearchAction extends SearchFormAction {

	private static final long serialVersionUID = 1L;
	
	protected Asset asset = new Asset();
	protected String xmlconfigname;
	protected String categoryname;
	protected Long catTypeId;
	protected Long zoneId;
	
	@Autowired
	private AssetCommonUtil assetCommonUtil;
	private static final Logger	LOGGER	= Logger.getLogger(AssetBaseSearchAction.class);
	public AssetBaseSearchAction(){
		addRelatedEntity("assetType", AssetType.class);
		addRelatedEntity("department", Department.class);
		addRelatedEntity("assetCategory", AssetCategory.class);
		addRelatedEntity("area", Boundary.class);
		addRelatedEntity("location", Boundary.class);
		addRelatedEntity("street", Boundary.class);
		addRelatedEntity("ward", Boundary.class);
		addRelatedEntity("zone", Boundary.class);
		addRelatedEntity("status", EgwStatus.class,"description");
	}

	public abstract SearchQuery prepareQuery(String sortField, String sortOrder);

	
	@Override
	public Object getModel() {
	
		return asset;
	}
	@Override
	public void prepare() {
		super.prepare();
		setupDropdownDataExcluding("area","location","street","ward","zone","status");	
		addDropdownData("areaList", Collections.EMPTY_LIST);
		addDropdownData("locationList", Collections.EMPTY_LIST);
		addDropdownData("wardList", Collections.EMPTY_LIST);
		addDropdownData("streetList", Collections.EMPTY_LIST);
		addDropdownData("zoneList", assetCommonUtil.getAllZoneOfHTypeAdmin());
	}
	 /**
	  *  data loaded by Ajax need to be reloaded again in the screen(specially required when validaion fails)
	  */
	protected void loadPreviousData(){
		LOGGER.debug("Loading ajax data");
		setXmlconfigname(xmlconfigname);
	    setCategoryname(categoryname);
	    setCatTypeId(catTypeId);
	    if(null != zoneId && zoneId != -1){
	    	 addDropdownData("wardList", assetCommonUtil.populateWard(zoneId));
	    }
	    if(null != asset.getWard() && asset.getWard().getId() != -1){
	    	addDropdownData("streetList", assetCommonUtil.populateStreets( asset.getWard().getId()));
	 	    addDropdownData("areaList", assetCommonUtil.populateArea( asset.getWard().getId()));
	    }
	    if(null != asset.getArea() && asset.getArea().getId() != -1){
	    	addDropdownData("locationList", assetCommonUtil.populateLocations(asset.getArea().getId()));
	    }
		
	}
	public Asset getAsset() {
		return asset;
	}



	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	public String getXmlconfigname() {
		return xmlconfigname;
	}

	public String getCategoryname() {
		return categoryname;
	}

	public Long getCatTypeId() {
		return catTypeId;
	}

	public Long getZoneId() {
		return zoneId;
	}

	public void setXmlconfigname(String xmlconfigname) {
		this.xmlconfigname = xmlconfigname;
	}

	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}

	public void setCatTypeId(Long catTypeId) {
		this.catTypeId = catTypeId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}

}
