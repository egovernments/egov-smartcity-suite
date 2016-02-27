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
package org.egov.tl.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;

import org.egov.commons.CFinancialYear;
import org.egov.infra.persistence.entity.AbstractAuditable;

@Entity
@Table(name = "egtl_feematrix")
@SequenceGenerator(name = FeeMatrix.SEQ, sequenceName = FeeMatrix.SEQ)
public class FeeMatrix extends AbstractAuditable {
    public static final String SEQ = "seq_egtl_feematrix";
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = SEQ, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "natureOfBusiness", nullable = false)
    private NatureOfBusiness natureOfBusiness;
    
    @ManyToOne
    @JoinColumn(name = "licenseCategory", nullable = false)
    private LicenseCategory licenseCategory;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subCategory", nullable = false)
    private LicenseSubCategory subCategory;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "licenseAppType", nullable = false)
    private LicenseAppType licenseAppType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feeType", nullable = false)
    private FeeType feeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "financialYear", nullable = false)
    private CFinancialYear financialYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unitOfMeasurement", nullable = false)
    private UnitOfMeasurement unitOfMeasurement;

    private String uniqueNo;

    @Valid
    @OrderBy("uomFrom")
    @OneToMany(mappedBy = "feeMatrix", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FeeMatrixDetail> feeMatrixDetail = new ArrayList<FeeMatrixDetail>(0);

    public FeeType getFeeType() {
        return feeType;
    }

    public void setFeeType(final FeeType feeType) {
        this.feeType = feeType;
    }

    public NatureOfBusiness getNatureOfBusiness() {
        return natureOfBusiness;
    }

    public void setNatureOfBusiness(final NatureOfBusiness natureOfBusiness) {
        this.natureOfBusiness = natureOfBusiness;
    }

    public LicenseCategory getLicenseCategory() {
        return licenseCategory;
    }

    public void setLicenseCategory(final LicenseCategory licenseCategory) {
        this.licenseCategory = licenseCategory;
    }

    public LicenseSubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(final LicenseSubCategory subCategory) {
        this.subCategory = subCategory;
    }

    public LicenseAppType getLicenseAppType() {
        return licenseAppType;
    }

    public void setLicenseAppType(final LicenseAppType licenseAppType) {
        this.licenseAppType = licenseAppType;
    }

    public UnitOfMeasurement getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    public void setUnitOfMeasurement(final UnitOfMeasurement unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return null;
    }

    public List<FeeMatrixDetail> getFeeMatrixDetail() {
        return feeMatrixDetail;
    }

    public void setFeeMatrixDetail(final List<FeeMatrixDetail> feeMatrixDetail) {
        this.feeMatrixDetail = feeMatrixDetail;
    }

    public String getUniqueNo() {
        return uniqueNo;
    }

    public void setUniqueNo(final String uniqueNo) {
        this.uniqueNo = uniqueNo;
    }

    public CFinancialYear getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(final CFinancialYear financialYear) {
        this.financialYear = financialYear;
    }

    public String genUniqueNo() {
        final StringBuilder sb = new StringBuilder();
        if (natureOfBusiness != null)
            sb.append(natureOfBusiness.getId());
        if (licenseAppType != null)
            sb.append("-" + licenseAppType.getId());
        if (licenseCategory != null)
            sb.append("-" + licenseCategory.getId());
        if (subCategory != null)
            sb.append("-" + subCategory.getId());
        if (feeType != null)
            sb.append("-" + feeType.getId());
        if (unitOfMeasurement != null)
            sb.append("-" + unitOfMeasurement.getId());
        if (financialYear != null)
            sb.append("-" + financialYear.getId());
        return sb.toString();

    }
}