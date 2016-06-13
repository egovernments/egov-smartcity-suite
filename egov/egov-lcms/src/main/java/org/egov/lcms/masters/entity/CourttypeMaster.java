package org.egov.lcms.masters.entity;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Max;

import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infstr.models.BaseModel;
import org.hibernate.validator.constraints.Length;

/**
 * CourttypeMaster entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Unique(fields = { "courtType", "code" }, id = "id", tableName = "EGLC_COURTTYPE_MASTER", columnName = {
		"COURT_TYPE", "CODE" }, message = "masters.courttypeMaster.isunique")
public class CourttypeMaster extends BaseModel {
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

	@Required(message = "masters.code.null")
	@Length(max = 8, message = "masters.code.length")
	@OptionalPattern(regex = "[0-9A-Za-z-]*", message = "masters.code.alpha2")
	private String code;

	@Required(message = "masters.courttypeMaster.null")
	@Length(max = 100, message = "masters.courttypeMaster.length")
	//@OptionalPattern(regex = "^[a-z|A-Z|]+[a-z|A-Z|0-9|&/() .:,-.]*", message = "masters.courttypeMaster.mixedChar")
	private String courtType;

	@Length(max = 128, message = "masters.description.length")
	private String notes;

	@Max(value = 1000, message = "masters.orderNumber.length")
	private Long ordernumber;

	private Set<CourtMaster> eglcCourtMasters = new HashSet<CourtMaster>(0);

	private Set<PetitiontypeMaster> eglcPetitiontypeMasters = new HashSet<PetitiontypeMaster>(
			0);

	public String getCourtType() {
		return this.courtType;
	}

	public void setCourtType(String courtType) {
		this.courtType = courtType;
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

	public Set<CourtMaster> getEglcCourtMasters() {
		return eglcCourtMasters;
	}

	public void setEglcCourtMasters(Set<CourtMaster> eglcCourtMasters) {
		this.eglcCourtMasters = eglcCourtMasters;
	}

	public Set<PetitiontypeMaster> getEglcPetitiontypeMasters() {
		return eglcPetitiontypeMasters;
	}

	public void setEglcPetitiontypeMasters(
			Set<PetitiontypeMaster> eglcPetitiontypeMasters) {
		this.eglcPetitiontypeMasters = eglcPetitiontypeMasters;
	}

}