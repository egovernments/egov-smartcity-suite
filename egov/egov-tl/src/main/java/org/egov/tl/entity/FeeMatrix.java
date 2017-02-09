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

import org.egov.commons.CFinancialYear;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.CompositeUnique;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "egtl_feematrix")
@SequenceGenerator(name = FeeMatrix.SEQ, sequenceName = FeeMatrix.SEQ, allocationSize = 1)
@CompositeUnique(fields = {"natureOfBusiness", "licenseCategory", "subCategory", "licenseAppType",
        "financialYear", "feeType", "unitOfMeasurement"}, enableDfltMsg = true, message = "{feematrix.exist}")
public class FeeMatrix extends AbstractAuditable {
    public static final String SEQ = "seq_egtl_feematrix";
    private static final long serialVersionUID = 3119126267277124321L;

    @Id
    @GeneratedValue(generator = SEQ, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "natureOfBusiness")
    private NatureOfBusiness natureOfBusiness;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "licenseCategory")
    private LicenseCategory licenseCategory;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subCategory")
    private LicenseSubCategory subCategory;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "licenseAppType")
    private LicenseAppType licenseAppType;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feeType")
    private FeeType feeType;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "financialYear")
    private CFinancialYear financialYear;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unitOfMeasurement")
    private UnitOfMeasurement unitOfMeasurement;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectiveFrom;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectiveTo;

    private boolean sameForNewAndRenew;

    private boolean sameForPermanentAndTemporary;

    @Valid
    @OrderBy("uomFrom")
    @OneToMany(mappedBy = "feeMatrix", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            fetch = FetchType.LAZY, orphanRemoval = true)
    private List<FeeMatrixDetail> feeMatrixDetail = new ArrayList<>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

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

    public BigDecimal getAmount() {
        return null;
    }

    public List<FeeMatrixDetail> getFeeMatrixDetail() {
        return feeMatrixDetail;
    }

    public void setFeeMatrixDetail(final List<FeeMatrixDetail> feeMatrixDetail) {
        this.feeMatrixDetail = feeMatrixDetail;
    }

    public CFinancialYear getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(final CFinancialYear financialYear) {
        this.financialYear = financialYear;
    }

    public Date getEffectiveFrom() {
        return this.effectiveFrom;
    }

    public void setEffectiveFrom(final Date effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public Date getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(final Date effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public boolean isSameForNewAndRenew() {
        return sameForNewAndRenew;
    }

    public void setSameForNewAndRenew(final boolean sameForNewAndRenew) {
        this.sameForNewAndRenew = sameForNewAndRenew;
    }

    public boolean isSameForPermanentAndTemporary() {
        return sameForPermanentAndTemporary;
    }

    public void setSameForPermanentAndTemporary(final boolean sameForPermanentAndTemporary) {
        this.sameForPermanentAndTemporary = sameForPermanentAndTemporary;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (!(o instanceof FeeMatrix))
            return false;
        final FeeMatrix feeMatrix = (FeeMatrix) o;
        return Objects.equals(natureOfBusiness.getId(), feeMatrix.natureOfBusiness.getId()) &&
                Objects.equals(licenseCategory.getId(), feeMatrix.licenseCategory.getId()) &&
                Objects.equals(subCategory.getId(), feeMatrix.subCategory.getId()) &&
                Objects.equals(licenseAppType.getId(), feeMatrix.licenseAppType.getId()) &&
                Objects.equals(feeType.getId(), feeMatrix.feeType.getId()) &&
                Objects.equals(financialYear.getId(), feeMatrix.financialYear.getId()) &&
                Objects.equals(unitOfMeasurement.getId(), feeMatrix.unitOfMeasurement.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(natureOfBusiness.getId(), licenseCategory.getId(), subCategory.getId(),
                licenseAppType.getId(), feeType.getId(), financialYear.getId(), unitOfMeasurement.getId());
    }
}