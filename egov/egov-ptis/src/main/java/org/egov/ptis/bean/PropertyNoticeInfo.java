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
package org.egov.ptis.bean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.report.bean.PropertyAckNoticeInfo;

public class PropertyNoticeInfo {
    private final PropertyImpl property;
    private String noticeNo;
    private PropertyAckNoticeInfo ownerInfo;
    private List<PropertyAckNoticeInfo> floorDetailsForNotice = new ArrayList<>();

    public PropertyNoticeInfo(final PropertyImpl property, final String noticeNo) {
        this.property = property;
        this.noticeNo = noticeNo;
    }

    public void setNoticeNo(final String noticeNo) {
        this.noticeNo = noticeNo;
    }

    public PropertyAckNoticeInfo getOwnerInfo() {
        return ownerInfo;
    }

    public void setOwnerInfo(final PropertyAckNoticeInfo ownerInfo) {
        this.ownerInfo = ownerInfo;
    }

    public List<PropertyAckNoticeInfo> getFloorDetailsForNotice() {
        return floorDetailsForNotice;
    }

    public void setFloorDetailsForNotice(
            final List<PropertyAckNoticeInfo> floorDetailsForNotice) {
        this.floorDetailsForNotice = floorDetailsForNotice;
    }

    public String getNoticeNo() {
        return noticeNo;
    }

    public String getNoticeDate() {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(new Date());
    }

    public PropertyImpl getProperty() {
        return property;
    }
}
