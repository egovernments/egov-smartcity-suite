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

/**
 * CasetypeMaster entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Unique(fields = { "caseType", "code" }, id = "id", tableName = "EGLC_CASETYPE_MASTER", columnName = {
        "CASE_TYPE", "CODE" }, message = "casetype.isunique.validation")
public class CasetypeMaster extends BaseModel {
    /**
     * Serial version uid
     */
    private static final long serialVersionUID = 1L;

    @Required(message = "casetype.null.validation")
    @Length(max = 100, message = "casetype.casetype.length.validation")
    // @OptionalPattern(regex = "^[a-z|A-Z|]+[a-z|A-Z|0-9|&/() .:,-.]*", message = "casetype.mixedChar.validation")
    private String caseType;

    private Boolean active;

    public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Length(max = 256, message = "masters.description.length")
    private String notes;
    @Max(value = 1000, message = "masters.orderNumber.length")
    private Long ordernumber;

    @Required(message = "masters.code.null")
    @Length(max = 8, message = "masters.code.length")
    @OptionalPattern(regex = "[0-9A-Za-z-]*", message = "masters.code.alpha2")
    private String code;

    private Set<Legalcase> eglcLegalcases = new HashSet<Legalcase>(0);

    public String getCaseType() {
        return this.caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getOrdernumber() {
        return this.ordernumber;
    }

    public void setOrdernumber(Long ordernumber) {
        this.ordernumber = ordernumber;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<Legalcase> getEglcLegalcases() {
        return eglcLegalcases;
    }

    public void setEglcLegalcases(Set<Legalcase> eglcLegalcases) {
        this.eglcLegalcases = eglcLegalcases;
    }

}