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
package org.egov.works.models.estimate;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.commons.EgwTypeOfWork;
import org.egov.commons.Fundsource;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.persistence.entity.Auditable;
import org.egov.infra.persistence.entity.component.Money;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.utils.StringUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.works.models.masters.DepositCode;
import org.egov.works.models.masters.NatureOfWork;
import org.egov.works.models.revisionEstimate.RevisionType;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class AbstractEstimate extends StateAware implements Auditable {

    private static final long serialVersionUID = 5010991868891221454L;

    public enum EstimateStatus {
        CREATED, TECH_SANCTION_CHECKED, TECH_SANCTIONED, BUDGETARY_APPR_CHECKED, BUDGETARY_APPROPRIATION_DONE, ADMIN_CHECKED, ADMIN_SANCTIONED, REJECTED, CANCELLED, COMP_CERTIFICATE, APPROVED
    }

    public enum Actions {
        SUBMIT_FOR_APPROVAL, TECH_SANCTION, BUDGET_DETAILS_SAVE, BUDGET_APPROPRIATION, ADMIN_SANCTION, REJECT, CANCEL;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    private Long id;

    private Boundary ward;

    private String name;

    private Date estimateDate;

    private String description;

    private String location;

    private NatureOfWork type;

    private EgwTypeOfWork category;
    private EgwTypeOfWork parentCategory;

    private Department userDepartment;

    private Department executingDepartment;

    private BigDecimal budgetAvailable = new BigDecimal("0.0");

    @Valid
    private List<OverheadValue> overheadValues = new LinkedList<OverheadValue>();

    @Valid
    private List<AssetsForEstimate> assetValues = new LinkedList<AssetsForEstimate>();

    @Valid
    private List<Activity> activities = new LinkedList<Activity>();
    @Valid
    private List<MultiYearEstimate> multiYearEstimates = new LinkedList<MultiYearEstimate>();

    private Set<AbstractEstimateAppropriation> abstractEstimateAppropriations = new HashSet<AbstractEstimateAppropriation>();

    private Money workValue;
    private Money estimateValue;
    private String estimateNumber;
    private Long documentNumber;
    private List<FinancialDetail> financialDetails = new LinkedList<FinancialDetail>();
    private ProjectCode projectCode;
    private String budgetApprNo;

    private String positionAndUserName;

    private Fundsource fundSource;

    private Integer approverUserId;
    private DepositCode depositCode;
    private String budgetRejectionNo;
    private EgwStatus egwStatus;

    private AbstractEstimate parent;
    private Date approvedDate;
    private String isCopiedEst = "N";
    private List<EstimatePhotographs> estimatePhotographsList = new ArrayList<EstimatePhotographs>();
    private BigDecimal latitude;
    private BigDecimal longitude;

    private Date budgetApprDate;

    public EgwStatus getEgwStatus() {
        return egwStatus;
    }

    public void setEgwStatus(final EgwStatus egwStatus) {
        this.egwStatus = egwStatus;
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
    public NatureOfWork getType() {
        return type;
    }

    public void setType(final NatureOfWork type) {
        this.type = type;
    }

    public EgwTypeOfWork getCategory() {
        return category;
    }

    public void setCategory(final EgwTypeOfWork category) {
        this.category = category;
    }

    @Required(message = "estimate.userDept.null")
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

    public Money getWorkValue() {
        double amt = 0;
        for (final Activity activity : activities)
            if (activity.getRevisionType() != null && activity.getRevisionType().equals(RevisionType.REDUCED_QUANTITY))
                amt -= activity.getAmount().getValue();
            else
                amt += activity.getAmount().getValue();
        workValue = new Money(amt);
        return workValue;
    }

    /**
     * This method returns the grand total of the work value for all the activities ( both SOR and Non SOR combined)
     *
     * @return a double value representing the rounded figure of the total of the grand total of the work value for all the
     * activities ( both SOR and Non SOR combined)
     */
    public Money getWorkValueIncludingTaxes() {
        double amt = 0;
        for (final Activity activity : activities)
            if (activity.getRevisionType() != null && activity.getRevisionType().equals(RevisionType.REDUCED_QUANTITY))
                amt = amt - (activity.getAmount().getValue() + activity.getTaxAmount().getValue());
            else
                amt += activity.getAmount().getValue() + activity.getTaxAmount().getValue();
        return new Money(amt);
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

    public void setWorkValue(final Money workValue) {
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
        double totalAmt = getWorkValue().getValue();
        for (final OverheadValue oh : overheadValues)
            totalAmt += oh.getAmount().getValue();
        return new Money(Math.round(totalAmt));
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

    public String getBudgetApprNo() {
        return budgetApprNo;
    }

    public void setBudgetApprNo(final String budgetApprNo) {
        this.budgetApprNo = budgetApprNo;
    }

    public Collection<Activity> getSORActivities() {
        return CollectionUtils.select(activities, activity -> ((Activity) activity).getSchedule() != null);
    }

    private String totalSOREstimatedAmt;
    private String totalSORServiceTaxPerc;
    private String totalSORServiceTaxAmt;
    private String totalSORAmtInclTax;

    public Collection<Activity> getWorkOrderSORActivities() {
        final Collection<Activity> sorActivities = CollectionUtils.select(activities,
                activity -> ((Activity) activity).getSchedule() != null);

        final NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        double totalSOREstimatedAmt = 0;
        double totalSORServiceTaxPerc = 0;
        double totalSORServiceTaxAmt = 0;
        double totalSORAmtInclTax = 0;
        for (final Activity activity : sorActivities) {
            activity.getRate().setValue(Double.valueOf(nf.format(Math.round(activity.getRate().getValue()))));
            activity.getAmount().setValue(Double.valueOf(nf.format(Math.round(activity.getAmount().getValue()))));
            activity.getTaxAmount().setValue(Double.valueOf(nf.format(Math.round(activity.getTaxAmount().getValue()))));
            activity.getAmountIncludingTax().setValue(
                    Double.valueOf(nf.format(Math.round(activity.getAmountIncludingTax().getValue()))));

            totalSOREstimatedAmt += activity.getAmount().getValue();
            totalSORServiceTaxPerc += activity.getServiceTaxPerc();
            totalSORServiceTaxAmt += activity.getTaxAmount().getValue();
            totalSORAmtInclTax += activity.getAmountIncludingTax().getValue();
        }

        setTotalSOREstimatedAmt(nf.format(totalSOREstimatedAmt));
        setTotalSORServiceTaxPerc(nf.format(totalSORServiceTaxPerc));
        setTotalSORServiceTaxAmt(nf.format(totalSORServiceTaxAmt));
        setTotalSORAmtInclTax(nf.format(totalSORAmtInclTax));
        return sorActivities;
    }

    private String totalNonSOREstimatedAmt;
    private String totalNonSORServiceTaxPerc;
    private String totalNonSORServiceTaxAmt;
    private String totalNonSORAmtInclTax;

    public Collection<Activity> getWorkOrderNonSORActivities() {
        final Collection<Activity> nonsorActivities = CollectionUtils.select(activities,
                activity -> ((Activity) activity).getNonSor() != null);

        final NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);

        double totalNonSOREstimatedAmt = 0;
        double totalNonSORServiceTaxPerc = 0;
        double totalNonSORServiceTaxAmt = 0;
        double totalNonSORAmtInclTax = 0;

        for (final Activity activity : nonsorActivities) {
            activity.getRate().setValue(Double.valueOf(nf.format(Math.round(activity.getRate().getValue()))));
            activity.getAmount().setValue(Double.valueOf(nf.format(Math.round(activity.getAmount().getValue()))));
            activity.getTaxAmount().setValue(Double.valueOf(nf.format(Math.round(activity.getTaxAmount().getValue()))));
            activity.getAmountIncludingTax().setValue(
                    Double.valueOf(nf.format(Math.round(activity.getAmountIncludingTax().getValue()))));

            totalNonSOREstimatedAmt += activity.getAmount().getValue();
            totalNonSORServiceTaxPerc += activity.getServiceTaxPerc();
            totalNonSORServiceTaxAmt += activity.getTaxAmount().getValue();
            totalNonSORAmtInclTax += activity.getAmountIncludingTax().getValue();
        }

        setTotalNonSOREstimatedAmt(nf.format(totalNonSOREstimatedAmt));
        setTotalNonSORServiceTaxPerc(nf.format(totalNonSORServiceTaxPerc));
        setTotalNonSORServiceTaxAmt(nf.format(totalNonSORServiceTaxAmt));
        setTotalNonSORAmtInclTax(nf.format(totalNonSORAmtInclTax));
        return nonsorActivities;
    }

    public Collection<Activity> getNonSORActivities() {
        return CollectionUtils.select(activities, activity -> ((Activity) activity).getNonSor() != null);
    }

    public void setActivities(final List<Activity> activities) {
        this.activities = activities;
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
        return "Abstract Estimate : " + getEstimateNumber();
    }

    public String getTotalSOREstimatedAmt() {
        return totalSOREstimatedAmt;
    }

    public void setTotalSOREstimatedAmt(final String totalSOREstimatedAmt) {
        this.totalSOREstimatedAmt = totalSOREstimatedAmt;
    }

    public String getTotalSORServiceTaxPerc() {
        return totalSORServiceTaxPerc;
    }

    public void setTotalSORServiceTaxPerc(final String totalSORServiceTaxPerc) {
        this.totalSORServiceTaxPerc = totalSORServiceTaxPerc;
    }

    public String getTotalSORServiceTaxAmt() {
        return totalSORServiceTaxAmt;
    }

    public void setTotalSORServiceTaxAmt(final String totalSORServiceTaxAmt) {
        this.totalSORServiceTaxAmt = totalSORServiceTaxAmt;
    }

    public String getTotalSORAmtInclTax() {
        return totalSORAmtInclTax;
    }

    public void setTotalSORAmtInclTax(final String totalSORAmtInclTax) {
        this.totalSORAmtInclTax = totalSORAmtInclTax;
    }

    public String getTotalNonSOREstimatedAmt() {
        return totalNonSOREstimatedAmt;
    }

    public void setTotalNonSOREstimatedAmt(final String totalNonSOREstimatedAmt) {
        this.totalNonSOREstimatedAmt = totalNonSOREstimatedAmt;
    }

    public String getTotalNonSORServiceTaxPerc() {
        return totalNonSORServiceTaxPerc;
    }

    public void setTotalNonSORServiceTaxPerc(final String totalNonSORServiceTaxPerc) {
        this.totalNonSORServiceTaxPerc = totalNonSORServiceTaxPerc;
    }

    public String getTotalNonSORServiceTaxAmt() {
        return totalNonSORServiceTaxAmt;
    }

    public void setTotalNonSORServiceTaxAmt(final String totalNonSORServiceTaxAmt) {
        this.totalNonSORServiceTaxAmt = totalNonSORServiceTaxAmt;
    }

    public String getTotalNonSORAmtInclTax() {
        return totalNonSORAmtInclTax;
    }

    public void setTotalNonSORAmtInclTax(final String totalNonSORAmtInclTax) {
        this.totalNonSORAmtInclTax = totalNonSORAmtInclTax;
    }

    public Long getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(final Long documentNumber) {
        this.documentNumber = documentNumber;
    }

    @Required(message = "estimate.fund.null")
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

    public BigDecimal getBudgetAvailable() {
        return budgetAvailable;
    }

    public void setBudgetAvailable(final BigDecimal budgetAvailable) {
        this.budgetAvailable = budgetAvailable;
    }

    public String getBudgetRejectionNo() {
        return budgetRejectionNo;
    }

    public void setBudgetRejectionNo(final String budgetRejectionNo) {
        this.budgetRejectionNo = budgetRejectionNo;
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

    public Set<AbstractEstimateAppropriation> getAbstractEstimateAppropriations() {
        return abstractEstimateAppropriations;
    }

    public void setAbstractEstimateAppropriations(
            final Set<AbstractEstimateAppropriation> abstractEstimateAppropriations) {
        this.abstractEstimateAppropriations = abstractEstimateAppropriations;
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

    public String getIsCopiedEst() {
        return isCopiedEst;
    }

    public void setIsCopiedEst(final String isCopiedEst) {
        this.isCopiedEst = isCopiedEst;
    }

    public List<EstimatePhotographs> getEstimatePhotographsList() {
        return estimatePhotographsList;
    }

    public void setEstimatePhotographsList(final List<EstimatePhotographs> estimatePhotographsList) {
        this.estimatePhotographsList = estimatePhotographsList;
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

    public Date getBudgetApprDate() {
        return budgetApprDate;
    }

    public void setBudgetApprDate(final Date budgetApprDate) {
        this.budgetApprDate = budgetApprDate;
    }

    public Money getEstimateValue() {
        return estimateValue;
    }

    public void setEstimateValue(final Money estimateValue) {
        this.estimateValue = estimateValue;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

}
