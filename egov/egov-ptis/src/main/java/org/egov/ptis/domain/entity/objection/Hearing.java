/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.ptis.domain.entity.objection;

import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infstr.models.BaseModel;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * @author manoranjan
 * 
 */

public class Hearing extends BaseModel {

    protected static final long serialVersionUID = 1L;
   

    protected RevisionPetition objection;

    @DateFormat(message = "invalid.fieldvalue.hearingDate")
    private Date plannedHearingDt;

    @DateFormat(message = "invalid.fieldvalue.hearingDate")
    protected Date actualHearingDt;

    @Length(max = 1024, message = "objectionHearing.objectionNumber.length")
    protected String hearingDetails;

    protected String documentNumber;
    
    protected String hearingNumber;
    
    protected Boolean inspectionRequired;
    
    private String hearingTime;
    private String hearingVenue;
    

    public String getHearingTime() {
        return hearingTime;
    }

    public void setHearingTime(String hearingTime) {
        this.hearingTime = hearingTime;
    }

    public String getHearingVenue() {
        return hearingVenue;
    }

    public void setHearingVenue(String hearingVenue) {
        this.hearingVenue = hearingVenue;
    }

    public RevisionPetition getObjection() {
        return objection;
    }

    public String getHearingDetails() {
        return hearingDetails;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setObjection(RevisionPetition objection) {
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
