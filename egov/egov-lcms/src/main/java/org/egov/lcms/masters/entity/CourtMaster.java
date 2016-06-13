package org.egov.lcms.masters.entity;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Max;

import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infstr.models.BaseModel;
import org.egov.lcms.transactions.entity.Legalcase;
import org.hibernate.validator.constraints.Length;

/**
 * CourtMaster entity.
 * 
 * @author srikanth
 */
@Unique(fields = { "courtName" }, id = "id", tableName = "EGLC_COURT_MASTER", columnName = { "COURT_NAME" }, message = "masters.courtMaster.isunique")
public class CourtMaster extends BaseModel {
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

	@Required(message = "masters.courtMaster.courtTypeSelect")
    private CourttypeMaster eglcCourttypeMaster;

    @Required(message = "masters.courtMaster.courtNameNull")
    @Length(max = 100, message = "masters.courtMaster.nameLength")
    // @OptionalPattern(regex = "[0-9a-zA-Z-&, .]+", message = "masters.courtMaster.courtNamePattern2")
    private String courtName;

    @Required(message = "masters.courtMaster.courtAddressNull")
    @Length(max = 256, message = "masters.courtMaster.addressLength")
    private String courtAddress;
    @Max(value = 1000, message = "masters.orderNumber.length")
    private Long ordernumber;

    private Set<Legalcase> eglcLegalcases = new HashSet<Legalcase>(0);

    public CourttypeMaster getEglcCourttypeMaster() {
        return this.eglcCourttypeMaster;
    }

    public void setEglcCourttypeMaster(CourttypeMaster eglcCourttypeMaster) {
        this.eglcCourttypeMaster = eglcCourttypeMaster;
    }

    public String getCourtName() {
        return this.courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public String getCourtAddress() {
        return this.courtAddress;
    }

    public void setCourtAddress(String courtAddress) {
        this.courtAddress = courtAddress;
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