package org.egov.lcms.masters.entity;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Max;

import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infstr.models.BaseModel;
import org.egov.lcms.transactions.entity.Legalcase;
import org.hibernate.validator.constraints.Length;

@Unique(fields = "name", id = "id", tableName = "EGLC_GOVERNMENTDEPARTMENT", columnName = "NAME", message = "masters.name.isunique")
public class GovernmentDept extends BaseModel {
    /**
     * Serial version uid
     */
    private static final long serialVersionUID = 1L;
    @Required(message = "masters.name.null")
    @Length(max = 32, message = "masters.name.length")
    @OptionalPattern(regex = "[0-9a-zA-Z-&, .]+", message = "masters.name.mixedChar2")
    private String name;
    private Boolean active;

    public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Length(max = 128, message = "masters.description.length")
    private String Description;
    @Max(value = 1000, message = "masters.orderNumber.length")
    private Long orderNumber;

    private Set<Legalcase> eglcLegalcases = new HashSet<Legalcase>(0);

    public GovernmentDept() {
    }

    public GovernmentDept(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Set<Legalcase> getEglcLegalcases() {
        return eglcLegalcases;
    }

    public void setEglcLegalcases(Set<Legalcase> eglcLegalcases) {
        this.eglcLegalcases = eglcLegalcases;
    }

}
