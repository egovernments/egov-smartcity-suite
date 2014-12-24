package org.egov.works.models.masters;

import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.Required;
import org.egov.infstr.models.validator.Unique;

@Unique(fields={"code"},id="id",tableName="EGW_NEWSPAPER_MASTER",columnName={"CODE"},message="contractor.code.isunique")
public class NewsPaper  extends BaseModel{
	
	@Required(message="newspaper.code.null")
	private String code;
	 	 
	@Required(message="newspaper.name.null") 
	private String name;
	
	@Required(message="newspaper.sequence.null") 
	private double sequence;
	
	private boolean isEnabled;
	
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

	public double getSequence() {
		return sequence;
	}

	public void setSequence(double sequence) {
		this.sequence = sequence;
	}

	public boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
}

