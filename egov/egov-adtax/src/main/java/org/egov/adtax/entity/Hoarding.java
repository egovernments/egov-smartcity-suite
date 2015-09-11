/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
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

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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

import org.egov.commons.EgwStatus;
import org.egov.demand.model.EgDemand;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "EGADTAX_HOARDING")
@SequenceGenerator(name = Hoarding.SEQ_HOARDING, sequenceName = Hoarding.SEQ_HOARDING, allocationSize = 1)
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
    @Column(name = "permissionNumber")
    @SafeHtml
    @Length(max = 25)
    private String permissionNumber;
    
    @NotNull
    @Column(name = "hoardingnumber", unique = true)
    @SafeHtml
    @Length(max = 25)
    private String hoardingnumber;
    
    @NotNull
    @Column(name = "hoardingName")
    @SafeHtml
    @Length(max = 125)
    private String hoardingName;
    
    @NotNull
    @Column(name = "type")
    @SafeHtml
    @Length(max = 25)
    private String type;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "agency", nullable = false)
    private Agency agency;
        
    @NotNull
    @Column(name = "advertiser")
    @SafeHtml
    @Length(max = 125)
    private String advertiser;
    
    @NotNull
    @Column(name = "advertiserParticular")
    @SafeHtml
    @Length(max = 512)
    private String advertiserParticular;
    
    @NotNull
    @Temporal(value = TemporalType.DATE)
    private Date applicationdate;
    
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "propertyType", nullable = false)
    private HoardingPropertyType propertyType;

    @Column(name = "propertyNumber")
    @SafeHtml
    @Length(max = 50)
    private String propertyNumber;
    
    @Column(name = "ownerdetail")
    @SafeHtml
    @Length(max = 125)
    private String ownerDetail;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "status", nullable = false)
    private EgwStatus status;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "category",nullable = false)
    private HoardingCategory category;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "subcategory",nullable = false)
    private SubCategory subCategory;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "unitofmeasure",nullable = false)
    private UnitOfMeasure unitofmeasure;
    
    private Double measurement;
    private Double length;
    private Double width;
    private Double breadth;
    private Double totalHeight;
    private Double taxAmount;
    private Double encroachmentFee;
    

    @ManyToOne
    @JoinColumn(name = "class",nullable = false)
    private RatesClass rateClass;
   
    @NotNull
    @ManyToOne
    @JoinColumn(name = "revenueinspector",nullable = false)
    private RevenueInspector revenueInspector;
    

    @ManyToOne
    @JoinColumn(name = "revenueboundary",nullable = false)
    private Boundary revenueBoundary;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "adminBoundry",nullable = false)
    private Boundary adminBoundry;
  
    @NotNull
    @Column(name = "address")
    @SafeHtml
    @Length(max = 512)
    private String address;
    
    @NotNull
    @Column(name = "advertisementduration")
    @SafeHtml
    @Length(max = 25)
    private String advertisementDuration;
    
    @ManyToOne
    @JoinColumn(name = "demandid",nullable = false)
    private EgDemand demandid;

    
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinTable(name = "egadtax_hoarding_docs", joinColumns = @JoinColumn(name = "hoarding") , inverseJoinColumns = @JoinColumn(name = "document") )
    private Set<HoardingDocument> documents = new HashSet<>();
    
    
    
    public Set<HoardingDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<HoardingDocument> documents) {
        this.documents = documents;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getPermissionNumber() {
        return permissionNumber;
    }

    public void setPermissionNumber(String permissionNumber) {
        this.permissionNumber = permissionNumber;
    }

    public String getHoardingnumber() {
        return hoardingnumber;
    }

    public void setHoardingnumber(String hoardingnumber) {
        this.hoardingnumber = hoardingnumber;
    }

    public String getHoardingName() {
        return hoardingName;
    }

    public void setHoardingName(String hoardingName) {
        this.hoardingName = hoardingName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public String getAdvertiser() {
        return advertiser;
    }

    public void setAdvertiser(String advertiser) {
        this.advertiser = advertiser;
    }

    public String getAdvertiserParticular() {
        return advertiserParticular;
    }

    public void setAdvertiserParticular(String advertiserParticular) {
        this.advertiserParticular = advertiserParticular;
    }

    public Date getApplicationdate() {
        return applicationdate;
    }

    public void setApplicationdate(Date applicationdate) {
        this.applicationdate = applicationdate;
    }

    public HoardingPropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(HoardingPropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public String getPropertyNumber() {
        return propertyNumber;
    }

    public void setPropertyNumber(String propertyNumber) {
        this.propertyNumber = propertyNumber;
    }

    public String getOwnerDetail() {
        return ownerDetail;
    }

    public void setOwnerDetail(String ownerDetail) {
        this.ownerDetail = ownerDetail;
    }

    public EgwStatus getStatus() {
        return status;
    }

    public void setStatus(EgwStatus status) {
        this.status = status;
    }

    public HoardingCategory getCategory() {
        return category;
    }

    public void setCategory(HoardingCategory category) {
        this.category = category;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    public UnitOfMeasure getUnitofmeasure() {
        return unitofmeasure;
    }

    public void setUnitofmeasure(UnitOfMeasure unitofmeasure) {
        this.unitofmeasure = unitofmeasure;
    }

    public Double getMeasurement() {
        return measurement;
    }

    public void setMeasurement(Double measurement) {
        this.measurement = measurement;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getBreadth() {
        return breadth;
    }

    public void setBreadth(Double breadth) {
        this.breadth = breadth;
    }

    public Double getTotalHeight() {
        return totalHeight;
    }

    public void setTotalHeight(Double totalHeight) {
        this.totalHeight = totalHeight;
    }

    public Double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Double getEncroachmentFee() {
        return encroachmentFee;
    }

    public void setEncroachmentFee(Double encroachmentFee) {
        this.encroachmentFee = encroachmentFee;
    }

    public RatesClass getRateClass() {
        return rateClass;
    }

    public void setRateClass(RatesClass rateClass) {
        this.rateClass = rateClass;
    }

    public RevenueInspector getRevenueInspector() {
        return revenueInspector;
    }

    public void setRevenueInspector(RevenueInspector revenueInspector) {
        this.revenueInspector = revenueInspector;
    }

    public Boundary getRevenueBoundary() {
        return revenueBoundary;
    }

    public void setRevenueBoundary(Boundary revenueBoundary) {
        this.revenueBoundary = revenueBoundary;
    }

    public Boundary getAdminBoundry() {
        return adminBoundry;
    }

    public void setAdminBoundry(Boundary adminBoundry) {
        this.adminBoundry = adminBoundry;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAdvertisementDuration() {
        return advertisementDuration;
    }

    public void setAdvertisementDuration(String advertisementDuration) {
        this.advertisementDuration = advertisementDuration;
    }

    public EgDemand getDemandid() {
        return demandid;
    }

    public void setDemandid(EgDemand demandid) {
        this.demandid = demandid;
    }
    
}