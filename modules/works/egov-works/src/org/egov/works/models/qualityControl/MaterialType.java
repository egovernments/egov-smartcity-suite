package org.egov.works.models.qualityControl;

import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.Required;
import org.egov.infstr.models.validator.Unique;
import org.hibernate.validator.constraints.Length;

@Unique(fields={"name"},id="id", tableName="EGW_QC_MATERIAL_TYPE",columnName={"NAME"},message="materialType.Name.isUnique")
public class MaterialType extends BaseModel{
	
	@Required(message="materialType.Name.null") 
	@Length(max=256,message="materialType.Name.length")
	private String name;
	
	@Length(max=1024,message="materialType.description.length")
	private String description;
	
	private Boolean isActive;
	
	private Long documentNumber;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Long getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(Long documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
