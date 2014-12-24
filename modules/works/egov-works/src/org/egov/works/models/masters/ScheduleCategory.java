package org.egov.works.models.masters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.egov.infstr.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.Unique;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

@Unique(fields="code", id="id",tableName="EGW_SCHEDULECATEGORY",columnName="CODE",message="scheduleCategory.code.isunique")
public class ScheduleCategory extends BaseModel { 
	@Length(max=150, message="ScheCategory.description.length")
	private String description;
	@Length(max=15, message="ScheCategory.code.length")
	private String code;
	private ScheduleCategory parent;
	public ScheduleCategory(){
	}
	public ScheduleCategory getParent() {
		return parent;  
	}
	public void setParent(ScheduleCategory parent) {
		this.parent = parent;
	}
	@StringLengthFieldValidator(fieldName= "description",message="ScheCategory.description.length", key = "i18n.key", shortCircuit=true, trim =true, minLength ="1", maxLength="150")

	@NotEmpty(message="scheduleCategory.description.not.empty")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@NotEmpty(message="scheduleCategory.code.not.empty")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public ScheduleCategory(String code, String description) {
		super();
		this.code = code;
		this.description = description;
	}
	public String getSearchableData() {
		return this.getCode()+" "+getDescription()+" "+(parent==null?"":parent.getSearchableData());
	}
	
	public List<ValidationError> validate() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		
		if(code == null && description == null) {
			return Arrays.asList(new ValidationError( "code","scheduleCategory.code.not.empty"));			
		}
		if(description == null ) {
			return Arrays.asList(new ValidationError("description","scheduleCategory.description.not.empty"));
		}
		
		if(validationErrors.isEmpty()) {
			return null;
		}
		else {
			return validationErrors;
		}
	}	
}
