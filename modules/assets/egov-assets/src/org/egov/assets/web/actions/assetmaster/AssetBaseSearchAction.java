/**
 * 
 */
package org.egov.assets.web.actions.assetmaster;

import java.util.Collections;

import org.apache.log4j.Logger;
import org.egov.assets.model.Asset;
import org.egov.assets.model.AssetCategory;
import org.egov.assets.model.AssetType;
import org.egov.assets.util.AssetCommonUtil;
import org.egov.commons.EgwStatus;
import org.egov.infstr.search.SearchQuery;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.web.actions.SearchFormAction;

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
	protected Integer zoneId;
	private static final Logger	LOGGER	= Logger.getLogger(AssetBaseSearchAction.class);
	public AssetBaseSearchAction(){
		addRelatedEntity("assetType", AssetType.class);
		addRelatedEntity("department", DepartmentImpl.class);
		addRelatedEntity("assetCategory", AssetCategory.class);
		addRelatedEntity("area", BoundaryImpl.class);
		addRelatedEntity("location", BoundaryImpl.class);
		addRelatedEntity("street", BoundaryImpl.class);
		addRelatedEntity("ward", BoundaryImpl.class);
		addRelatedEntity("zone", BoundaryImpl.class);
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
		addDropdownData("zoneList", AssetCommonUtil.getAllZoneOfHTypeAdmin());
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
	    	 addDropdownData("wardList", AssetCommonUtil.populateWard(zoneId));
	    }
	    if(null != asset.getWard() && asset.getWard().getId() != -1){
	    	addDropdownData("streetList", AssetCommonUtil.populateStreets( asset.getWard().getId()));
	 	    addDropdownData("areaList", AssetCommonUtil.populateArea( asset.getWard().getId()));
	    }
	    if(null != asset.getArea() && asset.getArea().getId() != -1){
	    	addDropdownData("locationList", AssetCommonUtil.populateLocations(asset.getArea().getId().toString()));
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

	public Integer getZoneId() {
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

	public void setZoneId(Integer zoneId) {
		this.zoneId = zoneId;
	}

}
