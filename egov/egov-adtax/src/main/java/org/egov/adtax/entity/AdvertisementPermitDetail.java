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

package org.egov.adtax.entity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.egov.adtax.entity.enums.AdvertisementDuration;
import org.egov.commons.EgwStatus;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infra.workflow.entity.StateAware;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "EGADTAX_PERMITDETAILS")
@SequenceGenerator(name = AdvertisementPermitDetail.SEQ_ADTAX_APPLICATION, sequenceName = AdvertisementPermitDetail.SEQ_ADTAX_APPLICATION, allocationSize = 1)
@Unique(id = "id", tableName = "EGADTAX_PERMITDETAILS", columnName = { "applicationNumber", "permissionNumber" }, fields = {
        "applicationNumber", "permissionNumber" }, enableDfltMsg = true)
public class AdvertisementPermitDetail extends StateAware {

    private static final long serialVersionUID = 845357231248646624L;

    public static final String SEQ_ADTAX_APPLICATION = "SEQ_EGADTAX_PERMITDETAILS";

    @Id
    @GeneratedValue(generator = SEQ_ADTAX_APPLICATION, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "advertisement", nullable = false)
    private Advertisement advertisement;

    @Column(name = "applicationNumber", unique = true)
    @SafeHtml
    @Length(max = 25)
    private String applicationNumber;

    @Column(name = "permissionNumber", unique = true)
    @SafeHtml
    @Length(max = 25)
    private String permissionNumber;

    @NotNull
    private Date applicationDate;

    @Enumerated(EnumType.ORDINAL)
    private AdvertisementDuration advertisementDuration;

    @ManyToOne
    @JoinColumn(name = "status", nullable = false)
    private EgwStatus status;

    @NotNull
    private BigDecimal taxAmount;

    private BigDecimal encroachmentFee;

    @ManyToOne
    @JoinColumn(name = "previousapplicationid")
    private AdvertisementPermitDetail previousapplicationid;

    private Boolean isActive = false;

    // @NotNull
    private Date permissionstartdate;

    // @NotNull
    private Date permissionenddate;

    @SafeHtml
    @Length(max = 125)
    private String ownerDetail;

    @ManyToOne
    @JoinColumn(name = "agency")
    private Agency agency;

    @SafeHtml
    @Length(max = 125)
    private String advertiser;

    @SafeHtml
    @Length(max = 512)
    private String advertisementParticular;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "unitofmeasure", nullable = false)
    private UnitOfMeasure unitOfMeasure;

    private Double measurement;
    private Double length;
    private Double width;
    private Double breadth;
    private Double totalHeight;

    private String deactivation_remarks;

    private Date deactivation_date;

    private String Source;

    @Transient
    private Long approvalDepartment;

    @Transient
    private String approvalComent;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(final String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getPermissionNumber() {
        return permissionNumber;
    }

    public void setPermissionNumber(final String permissionNumber) {
        this.permissionNumber = permissionNumber;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(final Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public AdvertisementDuration getAdvertisementDuration() {
        return advertisementDuration;
    }

    public void setAdvertisementDuration(final AdvertisementDuration advertisementDuration) {
        this.advertisementDuration = advertisementDuration;
    }

    public EgwStatus getStatus() {
        return status;
    }

    public void setStatus(final EgwStatus status) {
        this.status = status;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(final BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getEncroachmentFee() {
        return encroachmentFee;
    }

    public void setEncroachmentFee(final BigDecimal encroachmentFee) {
        this.encroachmentFee = encroachmentFee;
    }

    public AdvertisementPermitDetail getPreviousapplicationid() {
        return previousapplicationid;
    }

    public void setPreviousapplicationid(final AdvertisementPermitDetail previousapplicationid) {
        this.previousapplicationid = previousapplicationid;
    }

    public Date getPermissionstartdate() {
        return permissionstartdate;
    }

    public void setPermissionstartdate(final Date permissionstartdate) {
        this.permissionstartdate = permissionstartdate;
    }

    public Date getPermissionenddate() {
        return permissionenddate;
    }

    public void setPermissionenddate(final Date permissionenddate) {
        this.permissionenddate = permissionenddate;
    }

    public Advertisement getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(final Advertisement advertisement) {
        this.advertisement = advertisement;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(final Boolean isActive) {
        this.isActive = isActive;
    }

    public String getOwnerDetail() {
        return ownerDetail;
    }

    public void setOwnerDetail(final String ownerDetail) {
        this.ownerDetail = ownerDetail;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(final Agency agency) {
        this.agency = agency;
    }

    public String getAdvertiser() {
        return advertiser;
    }

    public void setAdvertiser(final String advertiser) {
        this.advertiser = advertiser;
    }

    public String getAdvertisementParticular() {
        return advertisementParticular;
    }

    public void setAdvertisementParticular(final String advertisementParticular) {
        this.advertisementParticular = advertisementParticular;
    }

    public UnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(final UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public Double getMeasurement() {
        return measurement;
    }

    public void setMeasurement(final Double measurement) {
        this.measurement = measurement;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(final Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(final Double width) {
        this.width = width;
    }

    public Double getBreadth() {
        return breadth;
    }

    public void setBreadth(final Double breadth) {
        this.breadth = breadth;
    }

    public Double getTotalHeight() {
        return totalHeight;
    }

    public void setTotalHeight(final Double totalHeight) {
        this.totalHeight = totalHeight;
    }

    @Override
    public String getStateDetails() {
        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return String.format("Application Number %s with date %s.", applicationNumber,
                applicationDate != null ? formatter.format(applicationDate) : formatter.format(new Date()));
    }

    public Long getApprovalDepartment() {
        return approvalDepartment;
    }

    public void setApprovalDepartment(final Long approvalDepartment) {
        this.approvalDepartment = approvalDepartment;
    }

    public String getApprovalComent() {
        return approvalComent;
    }

    public void setApprovalComent(final String approvalComent) {
        this.approvalComent = approvalComent;
    }

    public String getDeactivation_remarks() {
        return deactivation_remarks;
    }

    public void setDeactivation_remarks(String deactivation_remarks) {
        this.deactivation_remarks = deactivation_remarks;
    }

    public Date getDeactivation_date() {
        return deactivation_date;
    }

    public void setDeactivation_date(Date deactivation_date) {
        this.deactivation_date = deactivation_date;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

}