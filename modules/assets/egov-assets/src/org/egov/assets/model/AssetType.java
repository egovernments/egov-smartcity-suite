package org.egov.assets.model;

import java.util.LinkedList;
import java.util.List;

import org.egov.infstr.models.BaseModel;

/**
 * AssetType entity.
 * 
 * @author Nilesh
 */

public class AssetType extends BaseModel {

	// Constructors

	/** default constructor */
	public AssetType() {
	}

	public AssetType(String name) {
		this.name = name;
	}
	
	// Fields

	private String name;
	private List<AssetCategory> assetCategories = new LinkedList<AssetCategory>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<AssetCategory> getAssetCategories() {
		return assetCategories;
	}

	public void setAssetCategories(List<AssetCategory> assetCategories) {
		this.assetCategories = assetCategories;
	}
	
}