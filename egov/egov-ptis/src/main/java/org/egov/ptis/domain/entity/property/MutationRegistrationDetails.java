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
package org.egov.ptis.domain.entity.property;

import org.egov.infra.persistence.entity.AbstractPersistable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author subhash
 *
 */
public class MutationRegistrationDetails extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String seller;
    private String buyer;
    private String typeOfTransfer;
    private String documentNo;
    private Date documentDate;
    private BigDecimal partyValue;
    private BigDecimal departmentValue;
    private String doorNo;
    private String address;
    private String eastBoundary;
    private String westBoundary;
    private String northBoundary;
    private String southBoundary;
    private Double plinthArea;
    private Double plotArea;
    private String sroName;
    private BigDecimal documentValue;
    private String documentLink;
    
    public String getTypeOfTransfer() {
        return typeOfTransfer;
    }
    public void setTypeOfTransfer(String typeOfTransfer) {
        this.typeOfTransfer = typeOfTransfer;
    }
    public String getDocumentNo() {
        return documentNo;
    }
    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }
    public Date getDocumentDate() {
        return documentDate;
    }
    public void setDocumentDate(Date documentDate) {
        this.documentDate = documentDate;
    }
    public BigDecimal getPartyValue() {
        return partyValue;
    }
    public void setPartyValue(BigDecimal partyValue) {
        this.partyValue = partyValue;
    }
    public BigDecimal getDepartmentValue() {
        return departmentValue;
    }
    public void setDepartmentValue(BigDecimal departmentValue) {
        this.departmentValue = departmentValue;
    }
    public String getDoorNo() {
        return doorNo;
    }
    public void setDoorNo(String doorNo) {
        this.doorNo = doorNo;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getEastBoundary() {
        return eastBoundary;
    }
    public void setEastBoundary(String eastBoundary) {
        this.eastBoundary = eastBoundary;
    }
    public String getWestBoundary() {
        return westBoundary;
    }
    public void setWestBoundary(String westBoundary) {
        this.westBoundary = westBoundary;
    }
    public String getNorthBoundary() {
        return northBoundary;
    }
    public void setNorthBoundary(String northBoundary) {
        this.northBoundary = northBoundary;
    }
    public String getSouthBoundary() {
        return southBoundary;
    }
    public void setSouthBoundary(String southBoundary) {
        this.southBoundary = southBoundary;
    }
    public Double getPlinthArea() {
        return plinthArea;
    }
    public void setPlinthArea(Double plinthArea) {
        this.plinthArea = plinthArea;
    }
    public Double getPlotArea() {
        return plotArea;
    }
    public void setPlotArea(Double plotArea) {
        this.plotArea = plotArea;
    }
    
    @Override
    protected void setId(Long id) {
        this.id = id;
    }
    @Override
    public Long getId() {
        return id;
    }
    public String getSeller() {
        return seller;
    }
    public void setSeller(String seller) {
        this.seller = seller;
    }
    public String getBuyer() {
        return buyer;
    }
    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }
    public String getSroName() {
        return sroName;
    }
    public void setSroName(String sroName) {
        this.sroName = sroName;
    }
    public BigDecimal getDocumentValue() {
        return documentValue;
    }
    public void setDocumentValue(BigDecimal documentValue) {
        this.documentValue = documentValue;
    }
    public String getDocumentLink() {
        return documentLink;
    }
    public void setDocumentLink(String documentLink) {
        this.documentLink = documentLink;
    }

}
