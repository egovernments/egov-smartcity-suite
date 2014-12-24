package org.egov.assets.model;

import java.util.ArrayList;
import java.util.List;

import org.egov.infstr.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.Required;
import org.egov.commons.CFinancialYear;
import javax.validation.constraints.Min;

/**
 * DepreciationMetaData entity.
 * 
 * @author Nilesh
 */

public class DepreciationMetaData extends BaseModel {

	// Constructors

	/** default constructor */
	public DepreciationMetaData() {
	}
	
	// Fields
	@Required(message="depmetadata.rate.null")
	@Min(value=0,message="depmetadata.rate.not.negative")
	private Float depreciationRate;
	
	@Required(message="depmetadata.financialyear.null")
	private CFinancialYear financialYear;
	
	private AssetCategory assetCategory;
	
	public Float getDepreciationRate() {
		return depreciationRate;
	}
	public void setDepreciationRate(Float depreciationRate) {
		this.depreciationRate = depreciationRate;
	}
	public CFinancialYear getFinancialYear() {
		return financialYear;
	}
	public void setFinancialYear(CFinancialYear financialYear) {
		this.financialYear = financialYear;
	}
	public AssetCategory getAssetCategory() {
		return assetCategory;
	}
	public void setAssetCategory(AssetCategory assetCategory) {
		this.assetCategory = assetCategory;
	}
	
	@Override 
	public String toString() {
	    StringBuilder objString = new StringBuilder();
	    String NEW_LINE = System.getProperty("line.separator");

	    objString.append(this.getClass().getName() + " Object {" + NEW_LINE);
	    objString.append(" Id: " + id + NEW_LINE);
	    objString.append(" Dep Rate: " + depreciationRate + NEW_LINE);
	    objString.append(" Year: " + ((financialYear==null)?"null":financialYear.getId()) + NEW_LINE);
	    objString.append(" Cat: " + ((assetCategory==null)?"null":assetCategory.getId()) + NEW_LINE);
	    objString.append("}");

	    return objString.toString();
	  }	
	
	@Override
	public List<ValidationError> validate() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>(); 
		if(depreciationRate<=0.0){
			validationErrors.add(new ValidationError("deprate","depmetadata.rate.percentage_greater_than_0"));
		}
		if(depreciationRate>100.0){
			validationErrors.add(new ValidationError("deprate","depmetadata.rate.percentage_less_than_100"));
		} 
		if(financialYear==null || financialYear.getId() == null){
			 validationErrors.add(new ValidationError("financialyear","depmetadata.financialyear.required"));
		 }
		return validationErrors;
	}
}