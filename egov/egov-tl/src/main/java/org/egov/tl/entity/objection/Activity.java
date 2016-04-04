/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
package org.egov.tl.entity.objection;

import java.util.Date;

import org.egov.infstr.models.BaseModel;

public class Activity extends BaseModel {
    private static final long serialVersionUID = -6369641818759075953L;
    private LicenseObjection objection;
    private String details;
    private Date activityDate;
    private String remarks;
    private Date expectedDateOfResponse;
    // for uploaded document references
    private String docNumber;
    private String type;

    public LicenseObjection getObjection() {
        return objection;
    }

    public void setObjection(final LicenseObjection objection) {
        this.objection = objection;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(final String details) {
        this.details = details;
    }

    public Date getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(final Date activityDate) {
        this.activityDate = activityDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public Date getExpectedDateOfResponse() {
        return expectedDateOfResponse;
    }

    public void setExpectedDateOfResponse(final Date expectedDateOfResponse) {
        this.expectedDateOfResponse = expectedDateOfResponse;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(final String docNumber) {
        this.docNumber = docNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append("Activity={");
        str.append("objection=").append(objection == null ? "null" : objection.toString());
        str.append("details=").append(details == null ? "null" : details.toString());
        str.append("activityDate=").append(activityDate == null ? "null" : activityDate.toString());
        str.append("remarks=").append(remarks == null ? "null" : remarks.toString());
        str.append("expectedDateOfResponse=").append(expectedDateOfResponse == null ? "null" : expectedDateOfResponse.toString());
        str.append("docNumber=").append(docNumber == null ? "null" : docNumber.toString());
        str.append("type=").append(type == null ? "null" : type.toString());
        str.append("}");
        return str.toString();
    }

}
