/* eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.adtax.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.egov.adtax.entity.enums.HoardingDuration;
import org.egov.adtax.entity.enums.HoardingPropertyType;
import org.egov.adtax.entity.enums.HoardingStatus;
import org.egov.adtax.entity.enums.HoardingType;
import org.egov.demand.model.EgDemand;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "EGADTAX_HOARDING")
@SequenceGenerator(name = Hoarding.SEQ_HOARDING, sequenceName = Hoarding.SEQ_HOARDING, allocationSize = 1)
@Unique(id = "id", tableName = "EGADTAX_HOARDING", columnName = { "applicationNumber", "permissionNumber", "hoardingNumber" }, fields = {
        "applicationNumber", "permissionNumber", "hoardingNumber" }, enableDfltMsg = true)
public class Hoarding extends AbstractAuditable {

    private static final long serialVersionUID = 5612476685142904195L;
    public static final String SEQ_HOARDING = "SEQ_EGADTAX_HOARDING";
    
    @Id
    @GeneratedValue(generator = SEQ_HOARDING, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Column(name = "applicationNumber", unique = true)
    @SafeHtml
    @Length(max = 25)
    private String applicationNumber;

    @NotNull
    @Column(name = "permissionNumber", unique = true)
    @SafeHtml
    @Length(max = 25)
    private String permissionNumber;

    @NotNull
    @Column(name = "hoardingNumber", unique = true)
    @SafeHtml
    @Length(max = 25)
    private String hoardingNumber;

    @NotNull
    @SafeHtml
    @Length(max = 125)
    private String hoardingName;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private HoardingType type;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "agency", nullable = false)
    private Agency agency;

    @NotNull
    @SafeHtml
    @Length(max = 125)
    private String advertiser;

    @NotNull
    @SafeHtml
    @Length(max = 512)
    private String advertisementParticular;

    @NotNull
    @Temporal(value = TemporalType.DATE)
    private Date applicationDate;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private HoardingPropertyType propertyType;

    @SafeHtml
    @Length(max = 50)
    private String propertyNumber;

    @SafeHtml
    @Length(max = 125)
    private String ownerDetail;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private HoardingStatus status;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "category", nullable = false)
    private HoardingCategory category;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "subcategory", nullable = false)
    private SubCategory subCategory;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "unitofmeasure", nullable = false)
    private UnitOfMeasure unitOfMeasure;

    private Double measurement;
    private Double length;
    private Double width;
    private Double breadth;
    private Double totalHeight;
    private Double taxAmount;
    private Double encroachmentFee;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "class", nullable = false)
    private RatesClass rateClass;

    @ManyToOne
    @JoinColumn(name = "revenueinspector")
    private RevenueInspector revenueInspector;

    @ManyToOne
    @JoinColumn(name = "revenueboundary", nullable = true)
    private Boundary revenueBoundary;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "adminBoundry", nullable = false)
    private Boundary adminBoundry;

    @NotNull
    @SafeHtml
    @Length(max = 512)
    private String address;

    @Enumerated(EnumType.ORDINAL)
    private HoardingDuration advertisementDuration;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "demandid", nullable = false)
    private EgDemand demandId;

    private double longitude;
    
    private double latitude;
    
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinTable(name = "egadtax_hoarding_docs", joinColumns = @JoinColumn(name = "hoarding") , inverseJoinColumns = @JoinColumn(name = "document") )
    private List<HoardingDocument> documents = new ArrayList<>();

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

    public String getHoardingNumber() {
        return hoardingNumber;
    }

    public void setHoardingNumber(final String hoardingNumber) {
        this.hoardingNumber = hoardingNumber;
    }

    public String getHoardingName() {
        return hoardingName;
    }

    public void setHoardingName(final String hoardingName) {
        this.hoardingName = hoardingName;
    }

    public HoardingType getType() {
        return type;
    }

    public void setType(final HoardingType type) {
        this.type = type;
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

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(final Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public HoardingPropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(final HoardingPropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public String getPropertyNumber() {
        return propertyNumber;
    }

    public void setPropertyNumber(final String propertyNumber) {
        this.propertyNumber = propertyNumber;
    }

    public String getOwnerDetail() {
        return ownerDetail;
    }

    public void setOwnerDetail(final String ownerDetail) {
        this.ownerDetail = ownerDetail;
    }

    public HoardingStatus getStatus() {
        return status;
    }

    public void setStatus(final HoardingStatus status) {
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

    public Double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(final Double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Double getEncroachmentFee() {
        return encroachmentFee;
    }

    public void setEncroachmentFee(final Double encroachmentFee) {
        this.encroachmentFee = encroachmentFee;
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

    public Boundary getRevenueBoundary() {
        return revenueBoundary;
    }

    public void setRevenueBoundary(final Boundary revenueBoundary) {
        this.revenueBoundary = revenueBoundary;
    }

    public Boundary getAdminBoundry() {
        return adminBoundry;
    }

    public void setAdminBoundry(final Boundary adminBoundry) {
        this.adminBoundry = adminBoundry;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public HoardingDuration getAdvertisementDuration() {
        return advertisementDuration;
    }

    public void setAdvertisementDuration(final HoardingDuration advertisementDuration) {
        this.advertisementDuration = advertisementDuration;
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

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public List<HoardingDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(final List<HoardingDocument> documents) {
        this.documents = documents;
    }

}