package org.egov.infstr.models;

import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.hibernate.validator.constraints.Length;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author manoranjan
 *
 */
@Unique(fields = { "code" }, id = "id", tableName = "EGCL_SERVICECATEGORY", columnName = { "CODE" }, message = "masters.serviceCategoryCode.isunique")

public class ServiceCategory extends BaseModel {
	

	private static final long serialVersionUID = 1L;

	@Required(message = "serviceCategoryName.null.validation")
	@Length(max = 256, message = "masters.serviceCategory.nameLength")
	private String name;
	
	@Required(message = "serviceCategoryCode.null.validation")
	@Length(max = 256, message = "masters.serviceCategory.codeLength")
	private String code;
	
	private Boolean isActive;
	
	private Set<ServiceDetails> services = new LinkedHashSet<ServiceDetails>(
			0);

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the isActive
	 */
	public Boolean getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(Boolean isActive) {
		if(null == isActive){
            this.isActive = Boolean.FALSE;
		}else{
            this.isActive = isActive;
		}
	}
	public Set<ServiceDetails> getServices() {
		return services;
	}

	public void setServices(Set<ServiceDetails> services) {
		this.services = services;
	}

}