/*******************************************************************************
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
 * 	1) All versions of this program, verbatim or modified must carry this
 * 	   Legal Notice.
 *
 * 	2) Any misrepresentation of the origin of the material is prohibited. It
 * 	   is required that all modified versions of this material be marked in
 * 	   reasonable ways as different from the original version.
 *
 * 	3) This license does not grant any rights to any user of the program
 * 	   with regards to rights under trademark law for use of the trade names
 * 	   or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.bnd.model;

import java.util.Date;

/**
 * @author pritiranjan
 */

public class RegistrationHistory {

    private Long reportid;
    private Long historyId;
    private String type;
    private String registrationno;
    private String changedValue;
    private Date modifiedDate;
    private Date eventDate;
    private Date registrationdate;
    private RegistrationUnit regUnit;
    private String isStillBirth;

    public Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(final Long historyId) {
        this.historyId = historyId;
    }

    public Date getRegistrationdate() {
        return registrationdate;
    }

    public void setRegistrationdate(final Date registrationdate) {
        this.registrationdate = registrationdate;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(final Date eventDate) {
        this.eventDate = eventDate;
    }

    public RegistrationUnit getRegUnit() {
        return regUnit;
    }

    public void setRegUnit(final RegistrationUnit regUnit) {
        this.regUnit = regUnit;
    }

    public Long getReportid() {
        return reportid;
    }

    public void setReportid(final Long reportid) {
        this.reportid = reportid;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getRegistrationno() {
        return registrationno;
    }

    public void setRegistrationno(final String registrationno) {
        this.registrationno = registrationno;
    }

    public String getChangedValue() {
        return changedValue;
    }

    public void setChangedValue(final String changedValue) {
        this.changedValue = changedValue;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(final Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getIsStillBirth() {
        return isStillBirth;
    }

    public void setIsStillBirth(final String isStillBirth) {
        this.isStillBirth = isStillBirth;
    }
}
