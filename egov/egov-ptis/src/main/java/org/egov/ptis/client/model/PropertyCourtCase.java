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
package org.egov.ptis.client.model;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;

import java.io.Serializable;
import java.util.Date;

public class PropertyCourtCase implements Serializable {

    private String propertyId;
    private String ownerName;
    private String doorNo;
    private Boundary ward;
    private String localityName;
    private Double taxAmount;
    private Date caseDate;
    private String caseNo;
    private String courtName;
    private Integer ulbId;
    private Date createdDate;
    private boolean courtFlag;
    private String remarks;
    private User user;
    public String getPropertyId() {
        return propertyId;
    }
    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }
    public String getOwnerName() {
        return ownerName;
    }
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    public String getDoorNo() {
        return doorNo;
    }
    public void setDoorNo(String doorNo) {
        this.doorNo = doorNo;
    }
    public Boundary getWard() {
        return ward;
    }
    public void setWard(Boundary ward) {
        this.ward = ward;
    }
    public String getLocalityName() {
        return localityName;
    }
    public void setLocalityName(String localityName) {
        this.localityName = localityName;
    }
    public Double getTaxAmount() {
        return taxAmount;
    }
    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }
    public Date getCaseDate() {
        return caseDate;
    }
    public void setCaseDate(Date caseDate) {
        this.caseDate = caseDate;
    }
    public String getCaseNo() {
        return caseNo;
    }
    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }
    public String getCourtName() {
        return courtName;
    }
    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }
    public Integer getUlbId() {
        return ulbId;
    }
    public void setUlbId(Integer ulbId) {
        this.ulbId = ulbId;
    }
    public Date getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    public boolean isCourtFlag() {
        return courtFlag;
    }
    public void setCourtFlag(boolean courtFlag) {
        this.courtFlag = courtFlag;
    }
    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    
}
