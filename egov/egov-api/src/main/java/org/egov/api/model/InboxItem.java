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

package org.egov.api.model;

public class InboxItem {
    private String refNum;
    private String refDate;
    private String task;

    private String citizenName;
    private String citizenPhoneno;
    private String citizenAddress;
    private String status;
    private String resolutionDate;
    private String sender;
    private String senderPhoneno;
    private String location;
    private String itemDetails;
    private String link;

    public String getRefNum() {
        return refNum;
    }

    public void setRefNum(final String refNum) {
        this.refNum = refNum;
    }

    public String getRefDate() {
        return refDate;
    }

    public void setRefDate(final String refDate) {
        this.refDate = refDate;
    }

    public String getTask() {
        return task;
    }

    public void setTask(final String task) {
        this.task = task;
    }

    public String getCitizenName() {
        return citizenName;
    }

    public void setCitizenName(final String citizenName) {
        this.citizenName = citizenName;
    }

    public String getCitizenAddress() {
        return citizenAddress;
    }

    public void setCitizenAddress(final String citizenAddress) {
        this.citizenAddress = citizenAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getResolutionDate() {
        return resolutionDate;
    }

    public void setResolutionDate(final String resolutionDate) {
        this.resolutionDate = resolutionDate;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(final String sender) {
        this.sender = sender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    public String getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(final String itemDetails) {
        this.itemDetails = itemDetails;
    }

    public String getLink() {
        return link;
    }

    public void setLink(final String link) {
        this.link = link;
    }

    public String getCitizenPhoneno() {
        return citizenPhoneno;
    }

    public void setCitizenPhoneno(final String citizenPhoneno) {
        this.citizenPhoneno = citizenPhoneno;
    }

    public String getSenderPhoneno() {
        return senderPhoneno;
    }

    public void setSenderPhoneno(final String senderPhoneno) {
        this.senderPhoneno = senderPhoneno;
    }

}
