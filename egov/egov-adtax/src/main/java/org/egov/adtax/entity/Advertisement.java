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

import org.egov.adtax.entity.enums.AdvertisementPropertyType;
import org.egov.adtax.entity.enums.AdvertisementStatus;
import org.egov.adtax.entity.enums.AdvertisementStructureType;
import org.egov.demand.model.EgDemand;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "EGADTAX_ADVERTISEMENT")
@SequenceGenerator(name = Advertisement.SEQ_ADVERTISEMENT, sequenceName = Advertisement.SEQ_ADVERTISEMENT, allocationSize = 1)
@Unique(id = "id", tableName = "EGADTAX_ADVERTISEMENT", columnName = { "advertisementnumber" }, fields = {
        "advertisementnumber" }, enableDfltMsg = true)
public class Advertisement extends AbstractAuditable {

    private static final long serialVersionUID = 8916477826209092997L;

    public static final String SEQ_ADVERTISEMENT = "SEQ_EGADTAX_ADVERTISEMENT";

    @Id
    @GeneratedValue(generator = SEQ_ADVERTISEMENT, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "advertisementnumber", unique = true)
    @SafeHtml
    @Length(max = 25)
    private String advertisementNumber;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private AdvertisementStructureType type = AdvertisementStructureType.PERMANENT;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private AdvertisementPropertyType propertyType;

    @SafeHtml
    @Length(max = 50)
    private String propertyNumber;

    @SafeHtml
    @Length(max = 50)
    private String electricityServiceNumber;

    // @NotNull
    @Enumerated(EnumType.ORDINAL)
    private AdvertisementStatus status;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "category", nullable = false)
    private HoardingCategory category;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "subcategory", nullable = false)
    private SubCategory subCategory;
    private Boolean legacy = false;
    private BigDecimal pendingTax;
    /*
     * This field will be used to save penalty calculation date. For legacy entries current financial year start date
     * will be saved in this field. For new advertisement entries, application date will be consider as penalty calculation date.
     * If record in workflow, we can consider approval date to calculate penalty.
     */
    private Date penaltyCalculationDate;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "class", nullable = false)
    private RatesClass rateClass;

    @ManyToOne
    @JoinColumn(name = "revenueinspector")
    private RevenueInspector revenueInspector;

    @ManyToOne
    @JoinColumn(name = "locality")
    private Boundary locality;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ward", nullable = false)
    private Boundary ward;

    @ManyToOne
    @JoinColumn(name = "block")
    private Boundary block;

    @ManyToOne
    @JoinColumn(name = "street")
    private Boundary street;

    @ManyToOne
    @JoinColumn(name = "electionward")
    private Boundary electionWard;

    @NotNull
    @SafeHtml
    @Length(max = 512)
    private String address;

    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<AdvertisementPermitDetail> advertisementPermitDetail = new HashSet<AdvertisementPermitDetail>(0);

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "demandid")
    private EgDemand demandId;

    private double longitude;

    private double latitude;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinTable(name = "egadtax_advertisement_docs", joinColumns = @JoinColumn(name = "advertisement") , inverseJoinColumns = @JoinColumn(name = "document") )
    private List<HoardingDocument> documents = new ArrayList<>();

    @Transient
    private Boolean taxPaidForCurrentYear = false;

    public AdvertisementPermitDetail getActiveAdvertisementPermit() {
        AdvertisementPermitDetail advPermitDtl = null;
        for (final AdvertisementPermitDetail advPermitDetail : getAdvertisementPermitDetail()){
            if (advPermitDetail.getIsActive())
                advPermitDtl = advPermitDetail;
        }
        return advPermitDtl;
    }
    
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public BigDecimal getPendingTax() {
        return pendingTax;
    }

    public void setPendingTax(final BigDecimal pendingTax) {
        this.pendingTax = pendingTax;
    }

    public AdvertisementPropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(final AdvertisementPropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public String getPropertyNumber() {
        return propertyNumber;
    }

    public void setPropertyNumber(final String propertyNumber) {
        this.propertyNumber = propertyNumber;
    }

    public AdvertisementStatus getStatus() {
        return status;
    }

    public void setStatus(final AdvertisementStatus status) {
        this.status = status;
    }

    public HoardingCategory getCategory() {
        return category;
    }

    public void setCategory(final HoardingCategory category) {
        this.category = category;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(final SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    public RatesClass getRateClass() {
        return rateClass;
    }

    public void setRateClass(final RatesClass rateClass) {
        this.rateClass = rateClass;
    }

    public RevenueInspector getRevenueInspector() {
        return revenueInspector;
    }

    public void setRevenueInspector(final RevenueInspector revenueInspector) {
        this.revenueInspector = revenueInspector;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public EgDemand getDemandId() {
        return demandId;
    }

    public void setDemandId(final EgDemand demandId) {
        this.demandId = demandId;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(final double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(final double latitude) {
        this.latitude = latitude;
    }

    public List<HoardingDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(final List<HoardingDocument> documents) {
        this.documents = documents;
    }

    public Boolean getLegacy() {
        return legacy;
    }

    public void setLegacy(final Boolean legacy) {
        this.legacy = legacy;
    }

    public Date getPenaltyCalculationDate() {
        return penaltyCalculationDate;
    }

    public void setPenaltyCalculationDate(final Date penaltyCalculationDate) {
        this.penaltyCalculationDate = penaltyCalculationDate;
    }

    public Boundary getLocality() {
        return locality;
    }

    public void setLocality(final Boundary locality) {
        this.locality = locality;
    }

    public Boundary getWard() {
        return ward;
    }

    public void setWard(final Boundary ward) {
        this.ward = ward;
    }

    public Boundary getBlock() {
        return block;
    }

    public void setBlock(final Boundary block) {
        this.block = block;
    }

    public Boundary getStreet() {
        return street;
    }

    public void setStreet(final Boundary street) {
        this.street = street;
    }

    public Boundary getElectionWard() {
        return electionWard;
    }

    public void setElectionWard(final Boundary electionWard) {
        this.electionWard = electionWard;
    }

    public String getElectricityServiceNumber() {
        return electricityServiceNumber;
    }

    public void setElectricityServiceNumber(final String electricityServiceNumber) {
        this.electricityServiceNumber = electricityServiceNumber;
    }

    public String getAdvertisementNumber() {
        return advertisementNumber;
    }

    public void setAdvertisementNumber(final String advertisementNumber) {
        this.advertisementNumber = advertisementNumber;
    }

    public AdvertisementStructureType getType() {
        return type;
    }

    public void setType(final AdvertisementStructureType type) {
        this.type = type;
    }

    public Set<AdvertisementPermitDetail> getAdvertisementPermitDetail() {
        return advertisementPermitDetail;
    }

    public void setAdvertisementPermitDetail(final Set<AdvertisementPermitDetail> advertisementPermitDetail) {
        this.advertisementPermitDetail = advertisementPermitDetail;
    }

    public Boolean getTaxPaidForCurrentYear() {
        return taxPaidForCurrentYear;
    }

    public void setTaxPaidForCurrentYear(final Boolean taxPaidForCurrentYear) {
        this.taxPaidForCurrentYear = taxPaidForCurrentYear;
    }

}