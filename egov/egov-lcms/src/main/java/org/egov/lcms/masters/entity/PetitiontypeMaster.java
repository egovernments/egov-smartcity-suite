package org.egov.lcms.masters.entity;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infstr.models.BaseModel;
import org.egov.lcms.transactions.entity.Legalcase;
import org.egov.lcms.utils.LcmsConstants;
import org.hibernate.validator.constraints.Length;

/**
 * PetitiontypeMaster entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Unique(fields = "petitionCode", id = "id", columnName = "PETITION_CODE", tableName = "EGLC_PETITIONTYPE_MASTER", message = "masters.petitionmaster.isunique")
public class PetitiontypeMaster extends BaseModel {
    /**
     * Serial version uid
     */
    private static final long serialVersionUID = 1L;
    private Boolean active;

    public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Required(message = "petition.courttype.null")
    private CourttypeMaster eglcCourttypeMaster;

    @Required(message = "masters.petitionmaster.petitioncodereq")
    @Length(max = 15, message = "masters.petitionmaster.petitioncodemaxleng")
    @OptionalPattern(regex = "[0-9A-Za-z-]*", message = "masters.petitionmaster.petitioncodePattern")
    private String petitionCode;

    @Required(message = "masters.petitionmaster.petitiontypereq")
    @Length(max = 128, message = "masters.petitionmaster.petitiontypemaxleng")
    @OptionalPattern(regex = LcmsConstants.mixedChar, message = "masters.petitionmaster.petitiontypePattern")
    private String petitionType;

    @Max(value = 1000, message = "masters.orderNumber.length")
    @Min(value = 1, message = "masters.orderNumber.minlength")
    private Long ordernumber;
    private Set<Legalcase> eglcLegalcases = new HashSet<Legalcase>(0);

    public CourttypeMaster getEglcCourttypeMaster() {
        return this.eglcCourttypeMaster;
    }

    public void setEglcCourttypeMaster(CourttypeMaster eglcCourttypeMaster) {
        this.eglcCourttypeMaster = eglcCourttypeMaster;
    }

    public String getPetitionCode() {
        return this.petitionCode;
    }

    public void setPetitionCode(String petitionCode) {
        this.petitionCode = petitionCode;
    }

    public String getPetitionType() {
        return this.petitionType;
    }

    public void setPetitionType(String petitionType) {
        this.petitionType = petitionType;
    }

    public Long getOrdernumber() {
        return this.ordernumber;
    }

    public void setOrdernumber(Long ordernumber) {
        this.ordernumber = ordernumber;
    }

    public Set<Legalcase> getEglcLegalcases() {
        return eglcLegalcases;
    }

    public void setEglcLegalcases(Set<Legalcase> eglcLegalcases) {
        this.eglcLegalcases = eglcLegalcases;
    }

}