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
package org.egov.works.abstractestimate.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
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
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.commons.EgwTypeOfWork;
import org.egov.commons.Fundsource;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.Auditable;
import org.egov.infra.persistence.entity.component.Money;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.utils.StringUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.lineestimate.entity.EstimateAppropriation;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.entity.enums.Beneficiary;
import org.egov.works.lineestimate.entity.enums.WorkCategory;
import org.egov.works.masters.entity.DepositCode;
import org.egov.works.masters.entity.NatureOfWork;
import org.egov.works.models.tender.OfflineStatus;
import org.egov.works.revisionestimate.entity.enums.RevisionType;
import org.egov.works.utils.WorksConstants;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "EGW_ABSTRACTESTIMATE")
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries({
        @NamedQuery(name = AbstractEstimate.ABSTRACTESTIMATELIST_BY_ID, query = "from AbstractEstimate ab where ab.id in(:param_0)"),
        @NamedQuery(name = AbstractEstimate.REVISION_ESTIMATES_BY_ESTID, query = "from AbstractEstimate ae where ae.parent.id=? and ae.egwStatus.code='APPROVED' order by ae.id"),
        @NamedQuery(name = AbstractEstimate.REVISION_ESTIMATES_BY_ESTID_WOID, query = "from AbstractEstimate ae where ae.parent.id=? and ae.egwStatus.code='APPROVED' order by ae.id") })
@SequenceGenerator(name = AbstractEstimate.SEQ_EGW_ABSTRACTESTIMATE, sequenceName = AbstractEstimate.SEQ_EGW_ABSTRACTESTIMATE, allocationSize = 1)
public class AbstractEstimate extends StateAware implements Auditable {

    private static final long serialVersionUID = 5010991868891221454L;

    public enum EstimateStatus {
        NEW, CREATED, ADMIN_SANCTIONED, REJECTED, CANCELLED, APPROVED, RESUBMITTED, CHECKED
    }

    public enum Actions {
        SUBMIT_FOR_APPROVAL, TECH_SANCTION, BUDGET_DETAILS_SAVE, BUDGET_APPROPRIATION, ADMIN_SANCTION, REJECT, CANCEL;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    public enum OfflineStatusesForAbstractEstimate {
        NOTICEINVITINGTENDERRELEASED, TENDER_DOCUMENT_RELEASED, TENDER_OPENED, TECHNICAL_EVALUATION_DONE, COMMERCIAL_EVALUATION_DONE, L1_TENDER_FINALIZED;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    public static final String SEQ_EGW_ABSTRACTESTIMATE = "SEQ_EGW_ABSTRACTESTIMATE";
    public static final String ABSTRACTESTIMATELIST_BY_ID = "ABSTRACTESTIMATELIST_BY_ID";
    public static final String REVISION_ESTIMATES_BY_ESTID = "REVISION_ESTIMATES_BY_ESTID";
    public static final String REVISION_ESTIMATES_BY_ESTID_WOID = "REVISION_ESTIMATES_BY_ESTID_WOID";

    @Id
    @GeneratedValue(generator = SEQ_EGW_ABSTRACTESTIMATE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @SafeHtml
    @Length(max = 256)
    private String estimateNumber;

    @NotNull
    @Temporal(value = TemporalType.DATE)
    private Date estimateDate;

    @NotNull
    @SafeHtml
    @Length(max = 1024)
    @Column(name = "nameofwork")
    @OptionalPattern(regex = WorksConstants.ALPHANUMERICWITHALLSPECIALCHAR, message = "abstractEstimate.name.alphaNumeric")
    private String name;

    @NotNull
    @SafeHtml
    @Length(max = 1024)
    @OptionalPattern(regex = WorksConstants.ALPHANUMERICWITHALLSPECIALCHAR, message = "abstractEstimate.description.alphaNumeric")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userdepartment")
    private Department userDepartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "executingdepartment")
    private Department executingDepartment;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jurisdiction", nullable = false)
    private Boundary ward;

    @SafeHtml
    @Length(max = 250)
    private String location;

    private BigDecimal latitude;

    private BigDecimal longitude;

    @NotNull(message = "estimate.worktype.null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "natureofwork", nullable = false)
    private NatureOfWork natureOfWork;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "typeofwork", nullable = false)
    private EgwTypeOfWork parentCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subtypeofwork")
    private EgwTypeOfWork category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fundsource")
    private Fundsource fundSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status", nullable = false)
    private EgwStatus egwStatus;

    private double workValue;

    private BigDecimal estimateValue;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "projectcode")
    private ProjectCode projectCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "despositcode")
    private DepositCode depositCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    private AbstractEstimate parent;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "approveddate")
    private Date approvedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approvedby")
    private User approvedBy;

    private boolean copiedEstimate = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lineEstimateDetails")
    private LineEstimateDetails lineEstimateDetails;

    @Transient
    private String positionAndUserName;

    @Transient
    private Integer approverUserId;

    @Transient
    private Long approvalDepartment;

    @Transient
    private String approvalComent;

    @NotNull
    @Enumerated(EnumType.STRING)
    private WorkCategory workCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locality")
    private Boundary locality;

    @Enumerated(EnumType.STRING)
    private Beneficiary beneficiary;

    @NotNull
    private String modeOfAllotment;

    @SafeHtml
    @Length(max = 50)
    private String adminSanctionNumber;

    @Temporal(TemporalType.DATE)
    private Date adminSanctionDate;

    private String adminSanctionBy;

    private String councilResolutionNumber;

    @Temporal(TemporalType.DATE)
    private Date councilResolutionDate;

    private boolean spillOverFlag;

    private boolean workOrderCreated;

    private boolean billsCreated;

    private BigDecimal grossAmountBilled;

    @Valid
    @OrderBy("id")
    @OneToMany(mappedBy = "abstractEstimate", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = OverheadValue.class)
    private List<OverheadValue> overheadValues = new ArrayList<OverheadValue>(0);

    @Transient
    private List<OverheadValue> tempOverheadValues = new ArrayList<OverheadValue>(0);

    @Transient
    private List<AssetsForEstimate> tempAssetValues = new ArrayList<AssetsForEstimate>(0);

    @Valid
    @OrderBy("id")
    @OneToMany(mappedBy = "abstractEstimate", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = AssetsForEstimate.class)
    private List<AssetsForEstimate> assetValues = new ArrayList<AssetsForEstimate>(0);

    @Valid
    @OrderBy("id")
    @OneToMany(mappedBy = "abstractEstimate", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = Activity.class)
    private final List<Activity> activities = new ArrayList<Activity>(0);

    private transient List<Activity> sorActivities = new ArrayList<Activity>(0);

    private transient List<Activity> nonSorActivities = new ArrayList<Activity>(0);

    @Valid
    @OrderBy("id")
    @OneToMany(mappedBy = "abstractEstimate", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = MultiYearEstimate.class)
    private List<MultiYearEstimate> multiYearEstimates = new ArrayList<MultiYearEstimate>(0);

    @OrderBy("id")
    @OneToMany(mappedBy = "abstractEstimate", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = EstimateAppropriation.class)
    private List<EstimateAppropriation> estimateAppropriations = new ArrayList<EstimateAppropriation>(
            0);

    @OrderBy("id")
    @OneToMany(mappedBy = "abstractEstimate", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = FinancialDetail.class)
    private List<FinancialDetail> financialDetails = new ArrayList<FinancialDetail>(0);

    @OrderBy("id")
    @OneToMany(mappedBy = "abstractEstimate", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = EstimateTechnicalSanction.class)
    private List<EstimateTechnicalSanction> estimateTechnicalSanctions = new ArrayList<EstimateTechnicalSanction>(0);

    private final transient List<DocumentDetails> documentDetails = new ArrayList<DocumentDetails>(0);

    @JsonIgnore
    @Transient
    private List<OfflineStatus> offlineStatuses = new ArrayList<OfflineStatus>();

    private String cancellationReason;

    private String cancellationRemarks;

    @Column(name = "TOTAL_INCLUDING_RE")
    private Double totalIncludingRE;

    @OrderBy("id")
    @OneToMany(mappedBy = "abstractEstimate", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = AbstractEstimateDeduction.class)
    private final List<AbstractEstimateDeduction> absrtractEstimateDeductions = new ArrayList<AbstractEstimateDeduction>(0);

    @Transient
    private List<AbstractEstimateDeduction> tempDeductionValues = new ArrayList<AbstractEstimateDeduction>(0);

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COPIED_FROM", nullable = true)
    private AbstractEstimate copiedFrom;

    @OrderBy("id")
    @OneToMany(mappedBy = "abstractestimate", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = EstimatePhotographs.class)
    private List<EstimatePhotographs> estimatePhotographsList = new ArrayList<EstimatePhotographs>(0);

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    @Required(message = "estimate.name.null")
    @Length(max = 1024, message = "estimate.name.length")
    public String getName() {
        return name;
    }

    public String getNameJS() {
        return StringUtils.escapeJavaScript(name);
    }

    public String getNameJson() {
        return org.apache.commons.lang.StringUtils.escape(name);
    }

    public void setName(final String name) {
        this.name = StringEscapeUtils.unescapeHtml(name);
    }

    @Required(message = "estimate.date.null")
    @DateFormat(message = "invalid.fieldvalue.estimateDate")
    public Date getEstimateDate() {
        return estimateDate;
    }

    public void setEstimateDate(final Date estimateDate) {
        this.estimateDate = estimateDate;
    }

    @Required(message = "estimate.desc.null")
    @Length(max = 1024, message = "estimate.desc.length")
    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    @Length(max = 250, message = "estimate.loc.length")
    public String getLocation() {
        return location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    @Required(message = "estimate.natureofwork.null")
    public NatureOfWork getNatureOfWork() {
        return natureOfWork;
    }

    public void setNatureOfWork(final NatureOfWork natureOfWork) {
        this.natureOfWork = natureOfWork;
    }

    public EgwTypeOfWork getCategory() {
        return category;
    }

    public void setCategory(final EgwTypeOfWork category) {
        this.category = category;
    }

    public Department getUserDepartment() {
        return userDepartment;
    }

    public void setUserDepartment(final Department userDepartment) {
        this.userDepartment = userDepartment;
    }

    @Required(message = "estimate.executingDept.null")
    public Department getExecutingDepartment() {
        return executingDepartment;
    }

    public void setExecutingDepartment(final Department executingDepartment) {
        this.executingDepartment = executingDepartment;
    }

    @Required(message = "estimate.ward.null")
    public Boundary getWard() {
        return ward;
    }

    public void setWard(final Boundary ward) {
        this.ward = ward;
    }

    public double getWorkValue() {
        double amt = 0;
        if (!activities.isEmpty()) {
            for (final Activity activity : activities)
                if (activity.getRevisionType() != null
                        && activity.getRevisionType().equals(RevisionType.REDUCED_QUANTITY))
                    amt -= activity.getAmount().getValue();
                else
                    amt += activity.getAmount().getValue();
            workValue = amt;
        }
        return workValue;
    }

    /**
     * This method returns the grand total of the work value for all the activities ( both SOR and Non SOR combined)
     *
     * @return a double value representing the rounded figure of the total of the grand total of the work value for all the
     * activities ( both SOR and Non SOR combined)
     */
    public Money getWorkValueIncludingTaxes() {
        return new Money(getWorkValue() + getTotalTax().getValue());
    }

    public Money getTotalTax() {
        double amt = 0;
        for (final Activity activity : activities)
            if (activity.getRevisionType() != null && activity.getRevisionType().equals(RevisionType.REDUCED_QUANTITY))
                amt = amt - activity.getTaxAmount().getValue();
            else
                amt += activity.getTaxAmount().getValue();
        return new Money(amt);
    }

    public void setWorkValue(final double workValue) {
        this.workValue = workValue;
    }

    public List<OverheadValue> getOverheadValues() {
        return overheadValues;
    }

    public void setOverheadValues(final List<OverheadValue> overheadValues) {
        this.overheadValues = overheadValues;
    }

    public void addOverheadValue(final OverheadValue overheadValue) {
        overheadValues.add(overheadValue);
    }

    public List<AssetsForEstimate> getAssetValues() {
        return assetValues;
    }

    public void setAssetValues(final List<AssetsForEstimate> assetValues) {
        this.assetValues = assetValues;
    }

    public void addAssetValue(final AssetsForEstimate assetValue) {
        assetValues.add(assetValue);
    }

    public List<FinancialDetail> getFinancialDetails() {
        return financialDetails;
    }

    public void setFinancialDetails(final List<FinancialDetail> financialDetails) {
        this.financialDetails = financialDetails;
    }

    public void addFinancialDetails(final FinancialDetail financialDetails) {
        this.financialDetails.add(financialDetails);
    }

    public Money getTotalAmount() {
        return new Money(estimateValue.doubleValue());
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public ProjectCode getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(final ProjectCode projectCode) {
        this.projectCode = projectCode;
    }

    public Collection<Activity> getSORActivities() {
        return CollectionUtils.select(activities, activity -> ((Activity) activity).getSchedule() != null);
    }

    public Collection<Activity> getNonSORActivities() {
        return CollectionUtils.select(activities, activity -> ((Activity) activity).getNonSor() != null);
    }

    public void setActivities(final List<Activity> activities) {
        this.activities.clear();
        if (activities != null)
            this.activities.addAll(activities);
    }

    public void addActivity(final Activity activity) {
        activities.add(activity);
    }

    public String getEstimateNumber() {
        return estimateNumber;
    }

    public void setEstimateNumber(final String estimateNumber) {
        this.estimateNumber = estimateNumber;
    }

    public EgwTypeOfWork getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(final EgwTypeOfWork parentCategory) {
        this.parentCategory = parentCategory;
    }

    public List<MultiYearEstimate> getMultiYearEstimates() {
        return multiYearEstimates;
    }

    public void setMultiYearEstimates(final List<MultiYearEstimate> multiYearEstimates) {
        this.multiYearEstimates = multiYearEstimates;
    }

    public void addMultiYearEstimate(final MultiYearEstimate multiYearEstimate) {
        multiYearEstimates.add(multiYearEstimate);
    }

    public List<ValidationError> validate() {
        final List<ValidationError> validationErrors = new ArrayList<ValidationError>();
        validationErrors.addAll(validateActivities());
        validationErrors.addAll(validateOverheads());
        validationErrors.addAll(validateAssets());
        validationErrors.addAll(validateMultiYearEstimates());
        validationErrors.addAll(validateFinancialDetails());

        return validationErrors;
    }

    public List<ValidationError> validateFinancialDetails() {
        final List<ValidationError> validationErrors = new ArrayList<ValidationError>();
        for (final FinancialDetail financialDetail : financialDetails)
            validationErrors.addAll(financialDetail.validate());
        return validationErrors;
    }

    public List<ValidationError> validateActivities() {
        final List<ValidationError> validationErrors = new ArrayList<ValidationError>();
        for (final Activity activity : activities)
            validationErrors.addAll(activity.validate());
        return validationErrors;
    }

    public List<ValidationError> validateOverheads() {
        final List<ValidationError> validationErrors = new ArrayList<ValidationError>();
        for (final OverheadValue overheadValue : overheadValues)
            validationErrors.addAll(overheadValue.validate());

        return validationErrors;
    }

    public List<ValidationError> validateAssets() {
        final List<ValidationError> validationErrors = new ArrayList<ValidationError>();
        for (final AssetsForEstimate assetValue : assetValues)
            validationErrors.addAll(assetValue.validate());

        return validationErrors;
    }

    public List<ValidationError> validateMultiYearEstimates() {
        final List<ValidationError> validationErrors = new ArrayList<ValidationError>();
        for (final MultiYearEstimate multiYearEstimate : multiYearEstimates)
            validationErrors.addAll(multiYearEstimate.validate());

        return validationErrors;
    }

    /**
     * This method returns the least of the financial years chosen from the multi year estimates
     *
     * @return an instance of <code>CFinancialYear</code> representing the least financial year
     */
    public CFinancialYear getLeastFinancialYearForEstimate() {
        CFinancialYear minfinYr = null;

        Date minStartDate = new Date();
        Date startDate;

        if (getMultiYearEstimates() != null) {
            minStartDate = getMultiYearEstimates().get(0).getFinancialYear().getStartingDate();
            minfinYr = getMultiYearEstimates().get(0).getFinancialYear();
        }
        for (int i = 1; i < getMultiYearEstimates().size(); i++) {
            startDate = getMultiYearEstimates().get(i).getFinancialYear().getStartingDate();

            if (startDate.before(minStartDate)) {
                minStartDate = startDate;
                minfinYr = getMultiYearEstimates().get(i).getFinancialYear();
            }

        }
        return minfinYr;
    }

    @Override
    public String getStateDetails() {
        return "Abstract/Detailed Estimate Number : " + getEstimateNumber();
    }

    // @Required(message = "estimate.fund.null")
    public Fundsource getFundSource() {
        return fundSource;
    }

    public void setFundSource(final Fundsource fundSource) {
        this.fundSource = fundSource;
    }

    public void setApproverUserId(final Integer approverUserId) {
        this.approverUserId = approverUserId;
    }

    public Integer getApproverUserId() {
        return approverUserId;
    }

    public String getPositionAndUserName() {
        return positionAndUserName;
    }

    public void setPositionAndUserName(final String positionAndUserName) {
        this.positionAndUserName = positionAndUserName;
    }

    public DepositCode getDepositCode() {
        return depositCode;
    }

    public void setDepositCode(final DepositCode depositCode) {
        this.depositCode = depositCode;
    }

    public String getCurrentStateCreatedDate() {
        String createdDate = "";
        if (getCurrentState() != null)
            createdDate = new java.text.SimpleDateFormat("dd/MM/yyyy").format(getCurrentState().getCreatedDate());
        return createdDate;
    }

    public List<EstimateAppropriation> getEstimateAppropriations() {
        return estimateAppropriations;
    }

    public void setEstimateAppropriations(
            final List<EstimateAppropriation> estimateAppropriations) {
        this.estimateAppropriations = estimateAppropriations;
    }

    public AbstractEstimate getParent() {
        return parent;
    }

    public void setParent(final AbstractEstimate parent) {
        this.parent = parent;
    }

    public void setTotalAmount(final Money totalAmount) {
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(final Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(final User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLatitude(final BigDecimal latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(final BigDecimal longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "AbstractEstimate ( Id : " + getId() + "Estimate No: " + estimateNumber + ")";
    }

    public BigDecimal getEstimateValue() {
        return estimateValue;
    }

    public void setEstimateValue(final BigDecimal estimateValue) {
        this.estimateValue = estimateValue;
    }

    public LineEstimateDetails getLineEstimateDetails() {
        return lineEstimateDetails;
    }

    public void setLineEstimateDetails(final LineEstimateDetails lineEstimateDetails) {
        this.lineEstimateDetails = lineEstimateDetails;
    }

    public EgwStatus getEgwStatus() {
        return egwStatus;
    }

    public void setEgwStatus(final EgwStatus egwStatus) {
        this.egwStatus = egwStatus;
    }

    public boolean isCopiedEstimate() {
        return copiedEstimate;
    }

    public void setCopiedEstimate(final boolean copiedEstimate) {
        this.copiedEstimate = copiedEstimate;
    }

    public List<EstimateTechnicalSanction> getEstimateTechnicalSanctions() {
        return estimateTechnicalSanctions;
    }

    public void setEstimateTechnicalSanctions(final List<EstimateTechnicalSanction> estimateTechnicalSanctions) {
        this.estimateTechnicalSanctions = estimateTechnicalSanctions;
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

    public List<DocumentDetails> getDocumentDetails() {
        return documentDetails;
    }

    public void setDocumentDetails(final List<DocumentDetails> documentDetails) {
        this.documentDetails.clear();
        if (documentDetails != null)
            this.documentDetails.addAll(documentDetails);
    }

    public List<Activity> getSorActivities() {
        return sorActivities;
    }

    public void setSorActivities(final List<Activity> sorActivities) {
        this.sorActivities = sorActivities;
    }

    public List<Activity> getNonSorActivities() {
        return nonSorActivities;
    }

    public void setNonSorActivities(final List<Activity> nonSorActivities) {
        this.nonSorActivities = nonSorActivities;
    }

    public List<OverheadValue> getTempOverheadValues() {
        return tempOverheadValues;
    }

    public void setTempOverheadValues(final List<OverheadValue> tempOverheadValues) {
        this.tempOverheadValues = tempOverheadValues;
    }

    public List<OfflineStatus> getOfflineStatuses() {
        return offlineStatuses;
    }

    public void setOfflineStatuses(final List<OfflineStatus> offlineStatuses) {
        this.offlineStatuses = offlineStatuses;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(final String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getCancellationRemarks() {
        return cancellationRemarks;
    }

    public void setCancellationRemarks(final String cancellationRemarks) {
        this.cancellationRemarks = cancellationRemarks;
    }

    public List<AssetsForEstimate> getTempAssetValues() {
        return tempAssetValues;
    }

    public void setTempAssetValues(final List<AssetsForEstimate> tempAssetValues) {
        this.tempAssetValues = tempAssetValues;
    }

    public Double getTotalIncludingRE() {
        return totalIncludingRE;
    }

    public void setTotalIncludingRE(final Double totalIncludingRE) {
        this.totalIncludingRE = totalIncludingRE;
    }

    public List<AbstractEstimateDeduction> getAbsrtractEstimateDeductions() {
        return absrtractEstimateDeductions;
    }

    public void setAbsrtractEstimateDeductions(final List<AbstractEstimateDeduction> absrtractEstimateDeductions) {
        this.absrtractEstimateDeductions.clear();
        if (absrtractEstimateDeductions != null)
            this.absrtractEstimateDeductions.addAll(absrtractEstimateDeductions);
    }

    public List<AbstractEstimateDeduction> getTempDeductionValues() {
        return tempDeductionValues;
    }

    public void setTempDeductionValues(final List<AbstractEstimateDeduction> tempDeductionValues) {
        this.tempDeductionValues = tempDeductionValues;
    }

    public AbstractEstimate getCopiedFrom() {
        return copiedFrom;
    }

    public void setCopiedFrom(final AbstractEstimate copiedFrom) {
        this.copiedFrom = copiedFrom;
    }

    public WorkCategory getWorkCategory() {
        return workCategory;
    }

    public void setWorkCategory(final WorkCategory workCategory) {
        this.workCategory = workCategory;
    }

    public Boundary getLocality() {
        return locality;
    }

    public void setLocality(final Boundary locality) {
        this.locality = locality;
    }

    public Beneficiary getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(final Beneficiary beneficiary) {
        this.beneficiary = beneficiary;
    }

    public String getModeOfAllotment() {
        return modeOfAllotment;
    }

    public void setModeOfAllotment(final String modeOfAllotment) {
        this.modeOfAllotment = modeOfAllotment;
    }

    public String getAdminSanctionNumber() {
        return adminSanctionNumber;
    }

    public void setAdminSanctionNumber(final String adminSanctionNumber) {
        this.adminSanctionNumber = adminSanctionNumber;
    }

    public Date getAdminSanctionDate() {
        return adminSanctionDate;
    }

    public void setAdminSanctionDate(final Date adminSanctionDate) {
        this.adminSanctionDate = adminSanctionDate;
    }

    public String getAdminSanctionBy() {
        return adminSanctionBy;
    }

    public void setAdminSanctionBy(final String adminSanctionBy) {
        this.adminSanctionBy = adminSanctionBy;
    }

    public String getCouncilResolutionNumber() {
        return councilResolutionNumber;
    }

    public void setCouncilResolutionNumber(final String councilResolutionNumber) {
        this.councilResolutionNumber = councilResolutionNumber;
    }

    public Date getCouncilResolutionDate() {
        return councilResolutionDate;
    }

    public void setCouncilResolutionDate(final Date councilResolutionDate) {
        this.councilResolutionDate = councilResolutionDate;
    }

    public boolean isSpillOverFlag() {
        return spillOverFlag;
    }

    public void setSpillOverFlag(final boolean spillOverFlag) {
        this.spillOverFlag = spillOverFlag;
    }

    public boolean isWorkOrderCreated() {
        return workOrderCreated;
    }

    public void setWorkOrderCreated(final boolean workOrderCreated) {
        this.workOrderCreated = workOrderCreated;
    }

    public boolean isBillsCreated() {
        return billsCreated;
    }

    public void setBillsCreated(final boolean billsCreated) {
        this.billsCreated = billsCreated;
    }

    public BigDecimal getGrossAmountBilled() {
        return grossAmountBilled;
    }

    public void setGrossAmountBilled(final BigDecimal grossAmountBilled) {
        this.grossAmountBilled = grossAmountBilled;
    }

    public List<EstimatePhotographs> getEstimatePhotographsList() {
        return estimatePhotographsList;
    }

    public void setEstimatePhotographsList(final List<EstimatePhotographs> estimatePhotographsList) {
        this.estimatePhotographsList = estimatePhotographsList;
    }

}