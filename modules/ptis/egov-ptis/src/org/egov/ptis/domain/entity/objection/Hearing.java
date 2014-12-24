/**
 * 
 */
package org.egov.ptis.domain.entity.objection;

import java.util.Date;

import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.CheckDateFormat;
import org.hibernate.validator.constraints.Length;

/**
 * @author manoranjan
 * 
 */

public class Hearing extends BaseModel {

    protected static final long serialVersionUID = 1L;
   

    protected Objection objection;

    @CheckDateFormat(message = "invalid.fieldvalue.hearingDate")
    private Date plannedHearingDt;

    @CheckDateFormat(message = "invalid.fieldvalue.hearingDate")
    protected Date actualHearingDt;

    @Length(max = 1024, message = "objectionHearing.objectionNumber.length")
    protected String hearingDetails;

    protected String documentNumber;
    
    protected String hearingNumber;
    
    protected Boolean inspectionRequired;

    public Objection getObjection() {
        return objection;
    }

    public String getHearingDetails() {
        return hearingDetails;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setObjection(Objection objection) {
        this.objection = objection;
    }

    public void setHearingDetails(String hearingDetails) {
        this.hearingDetails = hearingDetails;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Date getPlannedHearingDt() {
        return plannedHearingDt;
    }

    public Date getActualHearingDt() {
        return actualHearingDt;
    }

    public void setPlannedHearingDt(Date plannedHearingDt) {
        this.plannedHearingDt = plannedHearingDt;
    }

    public void setActualHearingDt(Date actualHearingDt) {
        this.actualHearingDt = actualHearingDt;
    }

	public String getHearingNumber() {
		return hearingNumber;
	}

	public Boolean getInspectionRequired() {
		return inspectionRequired;
	}

	public void setHearingNumber(String hearingNumber) {
		this.hearingNumber = hearingNumber;
	}

	public void setInspectionRequired(Boolean inspectionRequired) {
		this.inspectionRequired = inspectionRequired;
	}
	
	@Override
	public String toString() {
	
		StringBuilder sb = new StringBuilder();
		
		sb.append("objectionNo :").append(null!=objection?objection.getObjectionNumber():" ");
		sb.append("plannedHearingDt :").append(null!=plannedHearingDt?plannedHearingDt:"");
		sb.append("actualHearingDt :").append(null!=actualHearingDt?actualHearingDt:"");
		sb.append("hearingNumber :").append(null != hearingNumber?hearingNumber:"");
		sb.append("inspectionRequired").append(null!=inspectionRequired?inspectionRequired:"");
				
		return sb.toString();
	}
}
