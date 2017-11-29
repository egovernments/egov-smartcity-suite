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

import org.egov.commons.EgwStatus;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

public class CollectionSummary implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -5582334620824717063L;
    private Integer receiptHeaderId;
    private String receiptNumber;
    private Date receiptDate;
    private Property property;
    private String propertyId;
    private Boundary zoneId;
    private Boundary wardId;
    private Boundary areaId;
    private Boundary localityId;
    private Boundary streetId;
    private String payeeName;
    private Character collectionType;
    private String paymentMode;
    private User user;
    private String houseNumber;
    private String paidAt;
    private EgwStatus status;
    private BigDecimal taxColl;
    private Set<CollectionSummaryDetails> collectionDetails;

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(final String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(final Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(final String propertyId) {
        this.propertyId = propertyId;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(final String payeeName) {
        this.payeeName = payeeName;
    }

    public Character getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(final Character collectionType) {
        this.collectionType = collectionType;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(final String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boundary getZoneId() {
        return zoneId;
    }

    public void setZoneId(final Boundary zoneId) {
        this.zoneId = zoneId;
    }

    public Boundary getWardId() {
        return wardId;
    }

    public void setWardId(final Boundary wardId) {
        this.wardId = wardId;
    }

    public Boundary getAreaId() {
        return areaId;
    }

    public void setAreaId(final Boundary areaId) {
        this.areaId = areaId;
    }

    public Boundary getLocalityId() {
        return localityId;
    }

    public void setLocalityId(final Boundary localityId) {
        this.localityId = localityId;
    }

    public Boundary getStreetId() {
        return streetId;
    }

    public void setStreetId(final Boundary streetId) {
        this.streetId = streetId;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(final Property property) {
        this.property = property;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(String paidAt) {
        this.paidAt = paidAt;
    }

    public EgwStatus getStatus() {
        return status;
    }

    public void setStatus(EgwStatus status) {
        this.status = status;
    }

    
    public Set<CollectionSummaryDetails> getCollectionDetails() {
        return collectionDetails;
    }

    public void setCollectionDetails(Set<CollectionSummaryDetails> collectionDetails) {
        this.collectionDetails = collectionDetails;
    }

    public Integer getReceiptHeaderId() {
        return receiptHeaderId;
    }

    public void setReceiptHeaderId(Integer receiptHeaderId) {
        this.receiptHeaderId = receiptHeaderId;
    }

    public BigDecimal getTaxColl() {
        return taxColl;
    }

    public void setTaxColl(final BigDecimal taxColl) {
        this.taxColl = taxColl;
    }

}
