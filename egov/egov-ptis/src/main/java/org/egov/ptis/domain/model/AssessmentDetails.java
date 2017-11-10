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
package org.egov.ptis.domain.model;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

/**
 * The AssessmentDetails class is used to contain assessment details such as property id, owner details, boundary details, and
 * block details.
 *
 * @author ranjit
 *
 */
@SuppressWarnings("serial")
public class AssessmentDetails implements Serializable {

    private static final long serialVersionUID = 355399781881256186L;
    private String propertyID;
    private String oldAssessmentNo = StringUtils.EMPTY;
    private String owners = StringUtils.EMPTY;
    private String houseNo = StringUtils.EMPTY;
    private Set<OwnerName> ownerNames;
    private BoundaryDetails boundaryDetails;
    private String propertyAddress;
    private PropertyDetails propertyDetails;
    private String primaryEmail = StringUtils.EMPTY;
    private String primaryMobileNo = StringUtils.EMPTY;
    private Integer flag;
    private double latitude;
    private double longitude;
    private boolean status;
    private boolean exempted;
    private BigDecimal propertyDue = BigDecimal.ZERO;
    private BigDecimal waterTaxDue = BigDecimal.ZERO;
    private BigDecimal sewerageDue = BigDecimal.ZERO;
    private int connectionCount;
    private ErrorDetails errorDetails;

    public String getPropertyID() {
        return propertyID;
    }

    public void setPropertyID(final String propertyID) {
        this.propertyID = propertyID;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(final String houseNo) {
        this.houseNo = houseNo;
    }

    public Set<OwnerName> getOwnerNames() {
        return ownerNames;
    }

    public void setOwnerNames(final Set<OwnerName> ownerNames) {
        this.ownerNames = ownerNames;
    }

    public BoundaryDetails getBoundaryDetails() {
        return boundaryDetails;
    }

    public void setBoundaryDetails(final BoundaryDetails boundaryDetails) {
        this.boundaryDetails = boundaryDetails;
    }

    public PropertyDetails getPropertyDetails() {
        return propertyDetails;
    }

    public void setPropertyDetails(final PropertyDetails propertyDetails) {
        this.propertyDetails = propertyDetails;
    }

    public ErrorDetails getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(final ErrorDetails errorDetails) {
        this.errorDetails = errorDetails;
    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(final String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public void setPrimaryEmail(final String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    public String getPrimaryMobileNo() {
        return primaryMobileNo;
    }

    public void setPrimaryMobileNo(final String primaryMobileNo) {
        this.primaryMobileNo = primaryMobileNo;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(final Integer flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "AssessmentDetails [propertyID=" + propertyID + ", isexempted=" + exempted + ", ownerNames=" + ownerNames
                + ", boundaryDetails="
                + boundaryDetails + ", propertyAddress=" + propertyAddress + ", propertyDetails=" + propertyDetails
                + ", errorDetails=" + errorDetails + "]";
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(final double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(final double longitude) {
        this.longitude = longitude;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isExempted() {
        return exempted;
    }

    public void setExempted(boolean exempted) {
        this.exempted = exempted;
    }

    public String getOldAssessmentNo() {
        return oldAssessmentNo;
    }

    public void setOldAssessmentNo(String oldAssessmentNo) {
        this.oldAssessmentNo = oldAssessmentNo;
    }

    public String getOwners() {
        return owners;
    }

    public void setOwners(String owners) {
        this.owners = owners;
    }

    public BigDecimal getPropertyDue() {
        return propertyDue;
    }

    public void setPropertyDue(BigDecimal propertyDue) {
        this.propertyDue = propertyDue;
    }

    public BigDecimal getWaterTaxDue() {
        return waterTaxDue;
    }

    public void setWaterTaxDue(BigDecimal waterTaxDue) {
        this.waterTaxDue = waterTaxDue;
    }

    public BigDecimal getSewerageDue() {
        return sewerageDue;
    }

    public void setSewerageDue(BigDecimal sewerageDue) {
        this.sewerageDue = sewerageDue;
    }

    public int getConnectionCount() {
        return connectionCount;
    }

    public void setConnectionCount(int connectionCount) {
        this.connectionCount = connectionCount;
    }
}
