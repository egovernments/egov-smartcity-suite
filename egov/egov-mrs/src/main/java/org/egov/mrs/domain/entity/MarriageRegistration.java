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
package org.egov.mrs.domain.entity;

import org.egov.commons.EgwStatus;
import org.egov.demand.model.EgDemand;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.mrs.masters.entity.MarriageAct;
import org.egov.mrs.masters.entity.MarriageFee;
import org.egov.mrs.masters.entity.MarriageRegistrationUnit;
import org.egov.pims.commons.Position;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.egov.mrs.domain.entity.MarriageRegistration.SEQ_REGISTRATION;

@Entity
@Unique(fields = "serialNo", enableDfltMsg = true, message = "Serial No. already exist.")
@Table(name = "egmrs_registration")
@SequenceGenerator(name = SEQ_REGISTRATION, sequenceName = SEQ_REGISTRATION, allocationSize = 1)
public class MarriageRegistration extends StateAware<Position> {

    public static final String SEQ_REGISTRATION = "SEQ_EGMRS_REGISTRATION";
    private static final long serialVersionUID = 6743094118312883758L;
    @Id
    @GeneratedValue(generator = SEQ_REGISTRATION, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private String applicationNo;

    @NotNull
    private Date applicationDate;

    private String registrationNo;

    @NotNull
    private Date dateOfMarriage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marriageact")
    private MarriageAct marriageAct;

    @SafeHtml
    @Length(max = 100)
    @NotNull
    private String placeOfMarriage;

    @SafeHtml
    @NotNull
    private String venue;

    @NotNull
    @Length(max = 100)
    private String street;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "locality")
    private Boundary locality;

    @NotNull
    @Length(max = 30)
    private String city;

    @NotNull
    @Valid
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "husband")
    private MrApplicant husband = new MrApplicant();

    @NotNull
    @Valid
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "wife")
    private MrApplicant wife = new MrApplicant();

    @NotNull
    @Valid
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registration")
    @Size(max = 4)
    @OrderBy("id")
    private List<MarriageWitness> witnesses = new LinkedList<>();

    @Valid
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "priest")
    private MarriagePriest priest;

    private boolean coupleFromSamePlace;

    private boolean memorandumOfMarriage;
    private boolean courtFeeStamp;
    private boolean affidavit;
    private boolean marriageCard;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "feeCriteria")
    private MarriageFee feeCriteria;

    @NotNull
    private Double feePaid;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone")
    private Boundary zone;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registrationUnit")
    private MarriageRegistrationUnit marriageRegistrationUnit;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "demand")
    private EgDemand demand;

    @ManyToOne
    @JoinColumn(name = "status", nullable = false)
    private EgwStatus status;

    @Length(max = 256)
    private String rejectionReason;

    @Length(max = 256)
    private String remarks;

    @Transient
    private Long approvalDepartment;

    @Transient
    private String approvalComent;
    @Transient
    private Date fromDate;
    @Transient
    private Date toDate;

    @Transient
    private String monthYear;

    @Transient
    private int year;

    private boolean isLegacy;
    private boolean isActive;

    @NotNull
    @Valid
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registration")
    private Set<RegistrationDocument> registrationDocuments = new HashSet<>();

    @OneToMany(mappedBy = "registration", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MarriageCertificate> marriageCertificate = new ArrayList<>();

    @Transient
    private List<MarriageDocument> documents;

    @Transient
    private byte[] marriagePhoto;

    private transient MultipartFile marriagePhotoFile;

    @Transient
    private String encodedMarriagePhoto;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "marriagePhotoFileStore")
    private FileStoreMapper marriagePhotoFileStore;

    @NotNull
    @Column(name = "serialno", unique = true)
    private String serialNo;
    @NotNull
    private String pageNo;

    @SafeHtml
    @Length(max = 100)
    private String registrarName;

    @SafeHtml
    @Length(max = 15)
    private String source;

    @Override
    public String getStateDetails() {
        return "Marriage registration application no : " + applicationNo;
    }

    public boolean isFeeCollected() {
        if (demand != null)
            return demand.getBaseDemand().compareTo(demand.getAmtCollected()) <= 0 ? true : false;
        else
            return false;
    }

    public void addRegistrationDocument(final RegistrationDocument registrationDocument) {
        registrationDocument.setRegistration(this);
        getRegistrationDocuments().add(registrationDocument);
    }

    public void addWitness(final MarriageWitness witness) {
        witness.setRegistration(this);
        getWitnesses().add(witness);
    }

    public String getRegistrarName() {
        return registrarName;
    }

    public void setRegistrarName(String registrarName) {
        this.registrarName = registrarName;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(final String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(final String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public Date getDateOfMarriage() {
        return dateOfMarriage;
    }

    public void setDateOfMarriage(final Date dateOfMarriage) {
        this.dateOfMarriage = dateOfMarriage;
    }

    public MarriageAct getMarriageAct() {
        return marriageAct;
    }

    public void setMarriageAct(final MarriageAct marriageAct) {
        this.marriageAct = marriageAct;
    }

    public MrApplicant getHusband() {
        return husband;
    }

    public void setHusband(final MrApplicant husband) {
        this.husband = husband;
    }

    public MrApplicant getWife() {
        return wife;
    }

    public void setWife(final MrApplicant wife) {
        this.wife = wife;
    }

    public MarriagePriest getPriest() {
        return priest;
    }

    public void setPriest(final MarriagePriest priest) {
        this.priest = priest;
    }

    public boolean hasMemorandumOfMarriage() {
        return memorandumOfMarriage;
    }

    public boolean getMemorandumOfMarriage() {
        return memorandumOfMarriage;
    }

    public void setMemorandumOfMarriage(final boolean memorandumOfMarriage) {
        this.memorandumOfMarriage = memorandumOfMarriage;
    }

    public boolean getCourtFeeStamp() {
        return courtFeeStamp;
    }

    public void setCourtFeeStamp(final boolean courtFeeStamp) {
        this.courtFeeStamp = courtFeeStamp;
    }

    public boolean hasAffidavit() {
        return affidavit;
    }

    public boolean getAffidavit() {
        return affidavit;
    }

    public void setAffidavit(final boolean affidavit) {
        this.affidavit = affidavit;
    }

    public boolean hasMarriageCard() {
        return marriageCard;
    }

    public boolean getMarriageCard() {
        return marriageCard;
    }

    public void setMarriageCard(final boolean marriageCard) {
        this.marriageCard = marriageCard;
    }

    public boolean isCoupleFromSamePlace() {
        return coupleFromSamePlace;
    }

    public void setCoupleFromSamePlace(final boolean coupleFromSamePlace) {
        this.coupleFromSamePlace = coupleFromSamePlace;
    }

    public MarriageFee getFeeCriteria() {
        return feeCriteria;
    }

    public void setFeeCriteria(final MarriageFee feeCriteria) {
        this.feeCriteria = feeCriteria;
    }

    public Double getFeePaid() {
        return feePaid;
    }

    public void setFeePaid(final Double feePaid) {
        this.feePaid = feePaid;
    }

    public List<MarriageWitness> getWitnesses() {
        return witnesses;
    }

    public void setWitnesses(final List<MarriageWitness> witnesses) {
        this.witnesses = witnesses;
    }

    public Boundary getZone() {
        return zone;
    }

    public void setZone(final Boundary zone) {
        this.zone = zone;
    }

    public EgDemand getDemand() {
        return demand;
    }

    public void setDemand(final EgDemand demand) {
        this.demand = demand;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(final String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
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

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(final Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public List<MarriageDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(final List<MarriageDocument> documents) {
        this.documents = documents;
    }

    public EgwStatus getStatus() {
        return status;
    }

    public void setStatus(final EgwStatus status) {
        this.status = status;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(final boolean isActive) {
        this.isActive = isActive;
    }

    public List<MarriageCertificate> getMarriageCertificate() {
        return marriageCertificate;
    }

    public void setMarriageCertificate(final List<MarriageCertificate> marriageCertificate) {
        this.marriageCertificate = marriageCertificate;
    }

    public void addCertificate(final MarriageCertificate certificate) {
        getMarriageCertificate().add(certificate);
    }

    public void removeCertificate(final MarriageCertificate certificate) {
        getMarriageCertificate().remove(certificate);
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    public MarriageRegistrationUnit getMarriageRegistrationUnit() {
        return marriageRegistrationUnit;
    }

    public void setMarriageRegistrationUnit(
            final MarriageRegistrationUnit marriageRegistrationUnit) {
        this.marriageRegistrationUnit = marriageRegistrationUnit;
    }

    public boolean isLegacy() {
        return isLegacy;
    }

    public void setLegacy(final boolean isLegacy) {
        this.isLegacy = isLegacy;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public int getYear() {
        return year;
    }

    public void setYear(final int year) {
        this.year = year;
    }

    public String getPlaceOfMarriage() {
        return placeOfMarriage;
    }

    public void setPlaceOfMarriage(final String placeOfMarriage) {
        this.placeOfMarriage = placeOfMarriage;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(final String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(final String venue) {
        this.venue = venue;
    }

    public Boundary getLocality() {
        return locality;
    }

    public void setLocality(final Boundary locality) {
        this.locality = locality;
    }

    public byte[] getMarriagePhoto() {
        return marriagePhoto;
    }

    public void setMarriagePhoto(final byte[] marriagePhoto) {
        this.marriagePhoto = marriagePhoto;
    }

    public MultipartFile getMarriagePhotoFile() {
        return marriagePhotoFile;
    }

    public void setMarriagePhotoFile(final MultipartFile marriagePhotoFile) {
        this.marriagePhotoFile = marriagePhotoFile;
    }

    public String getEncodedMarriagePhoto() {
        return encodedMarriagePhoto;
    }

    public void setEncodedMarriagePhoto(final String encodedMarriagePhoto) {
        this.encodedMarriagePhoto = encodedMarriagePhoto;
    }

    public FileStoreMapper getMarriagePhotoFileStore() {
        return marriagePhotoFileStore;
    }

    public void setMarriagePhotoFileStore(final FileStoreMapper marriagePhotoFileStore) {
        this.marriagePhotoFileStore = marriagePhotoFileStore;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(final String serialNo) {
        this.serialNo = serialNo;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(final String pageNo) {
        this.pageNo = pageNo;
    }

    public Set<RegistrationDocument> getRegistrationDocuments() {
        return registrationDocuments;
    }

    public void setRegistrationDocuments(final Set<RegistrationDocument> registrationDocuments) {
        this.registrationDocuments = registrationDocuments;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public enum RegistrationStatus {
        CREATED, APPROVED, REJECTED, REGISTERED, CANCELLED, DIGITALSIGNED
    }

}