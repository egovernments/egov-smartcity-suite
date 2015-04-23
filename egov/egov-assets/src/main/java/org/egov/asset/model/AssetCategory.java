package org.egov.asset.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;

import org.egov.asset.util.AssetConstants;
import org.egov.commons.CChartOfAccounts;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infstr.ValidationError;
import org.egov.infstr.commonMasters.EgUom;
import org.egov.infstr.models.BaseModel;
import org.hibernate.validator.constraints.Length;

/**
 * AssetCategory entity.
 * 
 * @author Nilesh
 */
@Unique(fields={"code"},id="id",tableName="EGASSET_ASSET_CATEGORY",columnName={"CODE"},message="assetcat.code.isunique")
public class AssetCategory extends BaseModel {

	// Constructors

	/** default constructor */
	public AssetCategory() {
	}

	/** minimal constructor */
	public AssetCategory(AssetType assetType) {
		this.assetType = assetType;
	}
	
	public AssetCategory(String code, String name, AssetType assetType) {
		this.code = code;
		this.name = name;
		this.assetType = assetType;
	}
	
	@Required(message="assetcat.assetcode.null")
	private CChartOfAccounts assetCode;
	
	private Long maxLife;
	private CChartOfAccounts accDepCode;
	
	@Required(message="assetcat.revcode.null")
	private CChartOfAccounts revCode;
	
	private DepreciationMethod depreciationMethod;
	
	private CChartOfAccounts depExpCode;
	
	@Required(message="assetcat.code.null")
	@Length(max=50,message="assetcat.code.length")
	@OptionalPattern(regex=AssetConstants.alphaNumericwithspecialchar,message="assetcat.code.alphaNumericwithspecialchar")
	private String code;
	
	@Required(message="assetcat.name.null")
	@Length(max=100,message="assetcat.name.length")
	@OptionalPattern(regex=AssetConstants.alphaNumericwithspecialchar,message="assetcat.name.alphaNumericwithspecialchar")
	private String name;
	
	private String catAttrTemplate;
	
	@Required(message="assetcat.assettype.null")
	private AssetType assetType;
	
	
	
	@Required(message="assetcat.uom.null")
	private EgUom uom;
	
	private AssetCategory parent;
	
	@Valid
	private List<DepreciationMetaData> depreciationMetaDatas = new LinkedList<DepreciationMetaData>();
	
	private List<Asset> assets = new LinkedList<Asset>();
	
	public CChartOfAccounts getAssetCode() {
		return assetCode;
	}

	public void setAssetCode(CChartOfAccounts assetCode) {
		this.assetCode = assetCode;
	}

	public Long getMaxLife() {
		return maxLife;
	}

	public void setMaxLife(Long maxLife) {
		this.maxLife = maxLife;
	}

	public CChartOfAccounts getAccDepCode() {
		return accDepCode;
	}

	public void setAccDepCode(CChartOfAccounts accDepCode) {
		this.accDepCode = accDepCode;
	}
	
	public CChartOfAccounts getRevCode() {
		return revCode;
	}

	public void setRevCode(CChartOfAccounts revCode) {
		this.revCode = revCode;
	}

	
	public DepreciationMethod getDepreciationMethod() {
            return depreciationMethod;
	}

        public void setDepreciationMethod(DepreciationMethod depreciationMethod) {
            this.depreciationMethod = depreciationMethod;
        }

        public CChartOfAccounts getDepExpCode() {
		return this.depExpCode;
	}

	public void setDepExpCode(CChartOfAccounts depExpCode) {
		this.depExpCode = depExpCode;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCatAttrTemplate() {
		return catAttrTemplate;
	}

	public void setCatAttrTemplate(String catAttrTemplate) {
		this.catAttrTemplate = catAttrTemplate;
	}

	public AssetType getAssetType() {
		return assetType;
	}

	public void setAssetType(AssetType assetType) {
		this.assetType = assetType;
	}

	public EgUom getUom() {
		return uom;
	}

	public void setUom(EgUom uom) {
		this.uom = uom;
	}

	public AssetCategory getParent() {
		return parent;
	}

	public void setParent(AssetCategory parent) {
		this.parent = parent;
	}

	public List<DepreciationMetaData> getDepreciationMetaDatas() {
		return depreciationMetaDatas;
	}

	public void setDepreciationMetaDatas(
			List<DepreciationMetaData> depreciationMetaDatas) {
		this.depreciationMetaDatas = depreciationMetaDatas;
	}
	
	public void addDepreciationMetaData(DepreciationMetaData depreciationMetaData){
		this.depreciationMetaDatas.add(depreciationMetaData);
	}
	
	public List<Asset> getAssets() {
		return assets;
	}

	public void setAssets(List<Asset> assets) {
		this.assets = assets;
	}
	
	
	@Override 
	public String toString() {
	    StringBuilder objString = new StringBuilder();
	    String NEW_LINE = System.getProperty("line.separator");
	    String NULL_STRING = "null";

	    objString.append(this.getClass().getName() + " Object {" + NEW_LINE);
	    objString.append(" Id: " + id + NEW_LINE);
	    objString.append(" Name: " + name + NEW_LINE);
	    objString.append(" Code: " + code + NEW_LINE);
	    objString.append(" Asset Type: " 	+ ((assetType==null)?NULL_STRING:assetType.toString()) + NEW_LINE);
	    objString.append(" Parent: " 		+ ((parent==null)	?NULL_STRING:parent.getId()) + NEW_LINE);
	    objString.append(" UOM: " 			+ ((uom==null)		?NULL_STRING:uom.getId()) + NEW_LINE);
	    objString.append("}");

	    return objString.toString();
	  }
	
	@Override
	public List<ValidationError> validate()	{
		List<ValidationError> errorList = new ArrayList<ValidationError>();	
		if(depreciationMetaDatas!= null && !depreciationMetaDatas.isEmpty()) {			
			for (DepreciationMetaData depMetaData : depreciationMetaDatas) {				
				errorList.addAll(depMetaData.validate());
			}			
		}
		return errorList;
	}	
}