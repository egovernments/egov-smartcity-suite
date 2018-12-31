/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Immutable
@Table(name = "egtl_dcb_aggr_view")
public class LicenseInstallmentwiseDCBReportView {

    @Id
    private Long id;
    private String licenseNumber;
    private Long licenseId;
    private String licAddress;
    private String userName;
    private Long wardId;
    private Long locality;

    private BigDecimal currentDemand;

    private BigDecimal currentCollection;

    private BigDecimal currentBalance;

    @Transient
    private BigDecimal arrearDemand;
    @Transient
    private BigDecimal arrearCollection;
    @Transient
    private BigDecimal arrearBalance;

    private boolean active;

    private Date installment;

    private String demandReason;

    private String financialYear;

    public LicenseInstallmentwiseDCBReportView(long licenseId, String licenseNumber, boolean active, BigDecimal currentDemand,
                                               BigDecimal currentCollection, BigDecimal currentBalance, BigDecimal arrearDemand,
                                               BigDecimal arrearCollection, BigDecimal arrearBalance) {
        this.licenseId = licenseId;
        this.licenseNumber = licenseNumber;
        this.active = active;
        this.currentDemand = currentDemand;
        this.currentCollection = currentCollection;
        this.currentBalance = currentBalance;
        this.arrearDemand = arrearDemand;
        this.arrearCollection = arrearCollection;
        this.arrearBalance = arrearBalance;

    }

    public LicenseInstallmentwiseDCBReportView(Long count) {
        //Do nothing for jpa spec
    }

    public LicenseInstallmentwiseDCBReportView() {
        //Do nothing for jpa spec
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Long getLocality() {
        return locality;
    }

    public void setLocality(Long locality) {
        this.locality = locality;
    }

    public Date getInstallment() {
        return installment;
    }

    public void setInstallment(Date installment) {
        this.installment = installment;
    }

    public Long getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(Long licenseId) {
        this.licenseId = licenseId;
    }

    public String getLicAddress() {
        return licAddress;
    }

    public void setLicAddress(String licAddress) {
        this.licAddress = licAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getWardId() {
        return wardId;
    }

    public void setWardId(Long wardId) {
        this.wardId = wardId;
    }

    public BigDecimal getCurrentDemand() {
        return currentDemand;
    }

    public void setCurrentDemand(BigDecimal currentDemand) {
        this.currentDemand = currentDemand;
    }

    public BigDecimal getCurrentCollection() {
        return currentCollection;
    }

    public void setCurrentCollection(BigDecimal currentCollection) {
        this.currentCollection = currentCollection;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public BigDecimal getArrearDemand() {
        return arrearDemand;
    }

    public void setArrearDemand(BigDecimal arrearDemand) {
        this.arrearDemand = arrearDemand;
    }

    public BigDecimal getArrearCollection() {
        return arrearCollection;
    }

    public void setArrearCollection(BigDecimal arrearCollection) {
        this.arrearCollection = arrearCollection;
    }

    public BigDecimal getArrearBalance() {
        return arrearBalance;
    }

    public void setArrearBalance(BigDecimal arrearBalance) {
        this.arrearBalance = arrearBalance;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDemandReason() {
        return demandReason;
    }

    public void setDemandReason(String demandReason) {
        this.demandReason = demandReason;
    }

    public String getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }

    public BigDecimal getTotalDemand() {
        return (currentDemand == null ? BigDecimal.ZERO : currentDemand).add(arrearDemand == null ? BigDecimal.ZERO
                : arrearDemand);
    }

    public BigDecimal getTotalCollection() {
        return (currentCollection == null ? BigDecimal.ZERO : currentCollection)
                .add(arrearCollection == null ? BigDecimal.ZERO : arrearCollection);
    }

    public BigDecimal getTotalBalance() {
        return (currentBalance == null ? BigDecimal.ZERO : currentBalance).add(arrearBalance == null ? BigDecimal.ZERO
                : arrearBalance);
    }
}