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

import org.egov.infra.utils.DateUtils;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Immutable
@Table(name = "EGTL_MV_BASEREGISTER_VIEW")
public class LicenseBaseRegisterReportView implements Serializable {

    private static final long serialVersionUID = -5366096182840879108L;

    @Id
    private BigInteger licenseId;

    @Column(name = "cat")
    private Long categoryId;

    @Column(name = "subcat")
    private Long subCategoryId;

    @Column(name = "status")
    private Long statusId;

    @Column(name = "ward")
    private Long wardId;
    private Long adminWard;
    private String adminWardName;
    private String licenseNumber;
    private String oldLicenseNumber;
    private String tradeTitle;
    private String owner;
    private String mobile;
    private String categoryName;
    private String subCategoryName;
    private String assessmentNo;
    private String wardName;
    private String localityName;
    private String tradeAddress;
    private String commencementDate;
    private String statusName;
    private BigDecimal arrearLicenseFee;
    private BigDecimal arrearPenaltyFee;
    private BigDecimal curLicenseFee;
    private BigDecimal curPenaltyFee;
    private String unitOfMeasure;
    private BigInteger tradeWt;
    private BigDecimal rateVal;
    private Long locality;
    private Long uom;
    private Long appType;
    private Date appDate;

    @Transient
    private String filterName;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public Long getWardId() {
        return wardId;
    }

    public void setWardId(Long wardId) {
        this.wardId = wardId;
    }

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

    public String getTradeTitle() {
        return tradeTitle;
    }

    public void setTradeTitle(String tradeTitle) {
        this.tradeTitle = tradeTitle;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getAssessmentNo() {
        return assessmentNo;
    }

    public void setAssessmentNo(String assessmentNo) {
        this.assessmentNo = assessmentNo;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getLocalityName() {
        return localityName;
    }

    public void setLocalityName(String localityName) {
        this.localityName = localityName;
    }

    public String getTradeAddress() {
        return tradeAddress;
    }

    public void setTradeAddress(String tradeAddress) {
        this.tradeAddress = tradeAddress;
    }

    public String getCommencementDate() {
        return commencementDate;
    }

    public void setCommencementDate(Date commencementDate) {
        this.commencementDate = DateUtils.getDefaultFormattedDate(commencementDate);
    }

    public void setCommencementDate(final String commencementDate) {
        this.commencementDate = commencementDate;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public BigDecimal getArrearLicenseFee() {
        return arrearLicenseFee;
    }

    public void setArrearLicenseFee(BigDecimal arrearLicenseFee) {
        this.arrearLicenseFee = arrearLicenseFee;
    }

    public BigDecimal getArrearPenaltyFee() {
        return arrearPenaltyFee;
    }

    public void setArrearPenaltyFee(BigDecimal arrearPenaltyFee) {
        this.arrearPenaltyFee = arrearPenaltyFee;
    }

    public BigDecimal getCurLicenseFee() {
        return curLicenseFee;
    }

    public void setCurLicenseFee(BigDecimal curLicenseFee) {
        this.curLicenseFee = curLicenseFee;
    }

    public BigDecimal getCurPenaltyFee() {
        return curPenaltyFee;
    }

    public void setCurPenaltyFee(BigDecimal curPenaltyFee) {
        this.curPenaltyFee = curPenaltyFee;
    }

    public BigInteger getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(BigInteger licenseId) {
        this.licenseId = licenseId;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public BigInteger getTradeWt() {
        return tradeWt;
    }

    public void setTradeWt(BigInteger tradeWt) {
        this.tradeWt = tradeWt;
    }

    public BigDecimal getRateVal() {
        return rateVal;
    }

    public void setRateVal(BigDecimal rateVal) {
        this.rateVal = rateVal;
    }

    public Long getLocality() {
        return locality;
    }

    public void setLocality(Long locality) {
        this.locality = locality;
    }

    public Long getUom() {
        return uom;
    }

    public void setUom(Long uom) {
        this.uom = uom;
    }

    public Long getAppType() {
        return appType;
    }

    public void setAppType(Long appType) {
        this.appType = appType;
    }

    public Date getAppDate() {
        return appDate;
    }

    public void setAppDate(Date appDate) {
        this.appDate = appDate;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(final String filterName) {
        this.filterName = filterName;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (licenseId == null ? 0 : licenseId.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final LicenseBaseRegisterReportView other = (LicenseBaseRegisterReportView) obj;
        if (licenseId == null) {
            if (other.licenseId != null)
                return false;
        } else if (!licenseId.equals(other.licenseId))
            return false;
        return true;
    }

}