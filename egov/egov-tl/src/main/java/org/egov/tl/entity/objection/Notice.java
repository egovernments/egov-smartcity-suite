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

import org.egov.infstr.models.BaseModel;

import java.util.Date;

public class Notice extends BaseModel {
    private static final long serialVersionUID = -6369641818759075953L;
    private LicenseObjection objection;
    private String noticeNumber;
    private Date noticeDate;
    private String noticeType;
    private String moduleName;
    // for uploaded document references
    private String docNumber;

    public LicenseObjection getObjection() {
        return objection;
    }

    public void setObjection(final LicenseObjection objection) {
        this.objection = objection;
    }

    public String getNoticeNumber() {
        return noticeNumber;
    }

    public void setNoticeNumber(final String noticeNumber) {
        this.noticeNumber = noticeNumber;
    }

    public Date getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(final Date noticeDate) {
        this.noticeDate = noticeDate;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(final String noticeType) {
        this.noticeType = noticeType;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(final String moduleName) {
        this.moduleName = moduleName;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(final String docNumber) {
        this.docNumber = docNumber;
    }

    public String generateNumber(final String runningNumber) {
        noticeNumber = "NTC" + runningNumber;
        return noticeNumber;
    }

    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append("Notice={");
        str.append("objection=").append(objection == null ? "null" : objection.toString());
        str.append("noticeNumber=").append(noticeNumber == null ? "null" : noticeNumber.toString());
        str.append("noticeDate=").append(noticeDate == null ? "null" : noticeDate.toString());
        str.append("noticeType=").append(noticeType == null ? "null" : noticeType.toString());
        str.append("moduleName=").append(moduleName == null ? "null" : moduleName.toString());
        str.append("docNumber=").append(docNumber == null ? "null" : docNumber.toString());
        str.append("}");
        return str.toString();
    }
}
