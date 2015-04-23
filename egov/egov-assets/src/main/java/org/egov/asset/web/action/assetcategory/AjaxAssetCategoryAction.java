package org.egov.asset.web.action.assetcategory;

import java.util.List;

import org.egov.asset.model.AssetCategory;
import org.egov.web.actions.BaseFormAction;

public class AjaxAssetCategoryAction extends BaseFormAction{
	
//	private static final Logger logger = Logger.getLogger(AjaxAssetCategoryAction.class);
	public static final String PARENT_CATEGORIES = "parentcategories";
	public static final String ASSET_CAT_DETAILS = "assetcatdetails";
	private Long assetTypeId;	// Set by Ajax call
	private Long parentCatId;	// Set by Ajax call
	private AssetCategory parentCategory;
	private List<AssetCategory> assetCategoryList;
	
	public Object getModel() {
		return null;
	}
	
	public String execute(){
		return SUCCESS;
	}
	
	public String populateParentCategories(){
		if(assetTypeId==-1)
			assetCategoryList = getPersistenceService().findAllBy("from AssetCategory");
		else
			assetCategoryList = getPersistenceService()
									.findAllBy("from AssetCategory where assetType.id=?", assetTypeId);
		
		return PARENT_CATEGORIES;
	}


	public String populateParentDetails(){
		parentCategory = (AssetCategory)getPersistenceService().find("from AssetCategory where id=?", parentCatId);
		return ASSET_CAT_DETAILS;
	}
	
	// Property accessors
	
	public void setAssetTypeId(Long assetTypeId) {
		this.assetTypeId = assetTypeId;
	}
	
	public void setParentCatId(Long parentCatId) {
		this.parentCatId = parentCatId;
	}
	
	public List<AssetCategory> getAssetCategoryList() {
		return assetCategoryList;
	}

	public AssetCategory getParentCategory() {
		return parentCategory;
	}

}
