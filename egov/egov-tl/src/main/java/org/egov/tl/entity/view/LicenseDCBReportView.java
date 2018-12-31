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

package org.egov.tl.entity.view;

import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Immutable
@Table(name = "EGTL_MV_DCB_VIEW")
public class LicenseDCBReportView implements Serializable {

    private static final long serialVersionUID = 1188596286215178643L;

    @Id
    private Integer licenseId;

    private String licenseNumber;

    private String oldLicenseNumber;

    private String userName;

    @Column(name = "curr_demand")
    private BigDecimal currentDemand;

    @Column(name = "arr_demand")
    private BigDecimal arrearDemand;

    @Column(name = "curr_coll")
    private BigDecimal currentCollection;

    @Column(name = "arr_coll")
    private BigDecimal arrearCollection;

    @Column(name = "curr_balance")
    private BigDecimal currentBalance;

    @Column(name = "arr_balance")
    private BigDecimal arrearBalance;

    private boolean active;

    private String licAddress;

    private Long wardId;

    private String wardName;

    private Long locality;

    private Long adminWard;

    private String adminWardName;

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getOldLicenseNumber() {
        return oldLicenseNumber;
    }

    public void setOldLicenseNumber(final String oldLicenseNumber) {
        this.oldLicenseNumber = oldLicenseNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getTotalDemand() {
        return (currentDemand == null ? BigDecimal.ZERO : currentDemand).add(arrearDemand == null ? BigDecimal.ZERO
                : arrearDemand);
    }

    public BigDecimal getTotalCollection() {
        return (currentCollection == null ? BigDecimal.ZERO : currentCollection)
                .add(arrearCollection == null ? BigDecimal.ZERO : arrearCollection);
    }


    public BigDecimal getCurrentBalance() {
        return (currentDemand == null ? BigDecimal.ZERO : currentDemand).subtract(currentCollection == null ? BigDecimal.ZERO
                : currentCollection);
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public BigDecimal getArrearBalance() {
        return (arrearDemand == null ? BigDecimal.ZERO : arrearDemand).subtract(arrearCollection == null ? BigDecimal.ZERO
                : arrearCollection);
    }

    public void setArrearBalance(BigDecimal arrearBalance) {
        this.arrearBalance = arrearBalance;
    }

    public BigDecimal getTotalBalance() {
        return (currentBalance == null ? BigDecimal.ZERO : currentBalance).add(arrearBalance == null ? BigDecimal.ZERO
                : arrearBalance);
    }

    public Integer getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(Integer licenseId) {
        this.licenseId = licenseId;
    }

    public String getLicAddress() {
        return licAddress;
    }

    public void setLicAddress(String licAddress) {
        this.licAddress = licAddress;
    }

    public Long getWardId() {
        return wardId;
    }

    public void setWardId(Long wardId) {
        this.wardId = wardId;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public Long getLocality() {
        return locality;
    }

    public void setLocality(Long locality) {
        this.locality = locality;
    }

    public BigDecimal getCurrentDemand() {
        return currentDemand;
    }

    public void setCurrentDemand(BigDecimal currentDemand) {
        this.currentDemand = currentDemand;
    }

    public BigDecimal getArrearDemand() {
        return arrearDemand;
    }

    public void setArrearDemand(BigDecimal arrearDemand) {
        this.arrearDemand = arrearDemand;
    }

    public BigDecimal getCurrentCollection() {
        return currentCollection;
    }

    public void setCurrentCollection(BigDecimal currentCollection) {
        this.currentCollection = currentCollection;
    }

    public BigDecimal getArrearCollection() {
        return arrearCollection;
    }

    public void setArrearCollection(BigDecimal arrearCollection) {
        this.arrearCollection = arrearCollection;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getAdminWard() {
        return adminWard;
    }

    public void setAdminWard(Long adminWard) {
        this.adminWard = adminWard;
    }

    public String getAdminWardName() {
        return adminWardName;
    }

    public void setAdminWardName(String adminWardName) {
        this.adminWardName = adminWardName;
    }
}