/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2017>  eGovernments Foundation
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
package org.egov.works.models.tender;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.EgwStatus;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.persistence.entity.Auditable;
import org.egov.infra.persistence.entity.component.Money;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.pims.commons.Position;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.Activity;
import org.egov.works.utils.WorksConstants;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WorksPackage extends StateAware<Position> implements Auditable {

    private static final long serialVersionUID = -4874817415037202881L;
    private Long id;

    @NotEmpty(message = "wp.name.is.null")
    @Length(max = 1024, message = "workspackage.name.length")
    private String name;

    @Length(max = 1024, message = "workspackage.description.length")
    private String description;

    @NotNull(message = "wp.userDepartment.is.null")
    private Department department;

    @NotNull(message = "wp.wpDate.is.null")
    @DateFormat(message = "invalid.fieldvalue.wpDate")
    private Date wpDate;

    @NotEmpty(message = "wp.wpNumber.is.null")
    private String wpNumber;

    private String employeeName;
    private Money workValueIncludingTaxes;

    private List<WorksPackageDetails> worksPackageDetails = new LinkedList<>();
    private List<RetenderHistory> retenderHistoryDetails = new LinkedList<>();
    private List<Retender> retenderDetails = new LinkedList<>();

    @NotEmpty(message = "wp.tenderFileNumber.is.null")
    @Length(max = 50, message = "wp.tenderFileNumber.length")
    @OptionalPattern(regex = WorksConstants.alphaNumericwithspecialchar, message = "wp.tenderFileNumber.alphaNumeric")
    private String tenderFileNumber;
    private Long documentNumber;
    private EgwStatus egwStatus;
    private String wpOfflineStatus;
    private OfflineStatus latestOfflineStatus;
    private Set<OfflineStatus> offlineStatuses = Collections.emptySet();
    private List<String> worksPackageActions = new LinkedList<>();
    private String worksPackageStatus;
    private Date approvedDate;

    private Set<TenderEstimate> tenderEstimateSet = new HashSet<>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public Long getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(final Long documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(final Department department) {
        this.department = department;
    }

    public Date getWpDate() {
        return wpDate;
    }

    public void setWpDate(final Date wpDate) {
        this.wpDate = wpDate;
    }

    public String getWpNumber() {
        return wpNumber;
    }

    public void setWpNumber(final String wpNumber) {
        this.wpNumber = wpNumber;
    }

    public void addEstimates(final WorksPackageDetails wpDetailsObj) {
        worksPackageDetails.add(wpDetailsObj);
    }

    public List<ValidationError> validate() {
        final List<ValidationError> errors = new ArrayList<>();
        if (worksPackageDetails.isEmpty())
            errors.add(new ValidationError("estimates", "estimates.null"));
        return errors;
    }

    public List<WorksPackageDetails> getWorksPackageDetails() {
        return worksPackageDetails;
    }

    public void setWorksPackageDetails(final List<WorksPackageDetails> worksPackageDetails) {
        this.worksPackageDetails = worksPackageDetails;
    }

    @Override
    public String getStateDetails() {
        return "Works Package : " + getWpNumber();
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(final String employeeName) {
        this.employeeName = employeeName;
    }

    public Money getWorkValueIncludingTaxes() {
        double amt = 0;
        if (!worksPackageDetails.isEmpty())
            for (final WorksPackageDetails wpd : worksPackageDetails)
                amt += wpd.getEstimate().getWorkValueIncludingTaxes().getValue();
        workValueIncludingTaxes = new Money(amt);
        return workValueIncludingTaxes;
    }

    public void setWorkValueIncludingTaxes(final Money workValueIncludingTaxes) {
        this.workValueIncludingTaxes = workValueIncludingTaxes;
    }

    public Collection<EstimateLineItemsForWP> getActivitiesForEstimate() {
        final Map<Long, EstimateLineItemsForWP> resultMap = new HashMap<>();
        for (final Activity act : getAllActivities()) {
            final EstimateLineItemsForWP estlineItem = new EstimateLineItemsForWP();
            if (act.getSchedule() != null)
                if (resultMap.containsKey(act.getSchedule().getId())) {
                    final EstimateLineItemsForWP preEstlineItem = resultMap.get(act.getSchedule().getId());
                    preEstlineItem.setQuantity(act.getQuantity() + preEstlineItem.getQuantity());
                    if (DateUtils.compareDates(act.getAbstractEstimate().getEstimateDate(),
                            preEstlineItem.getEstimateDate())) {
                        preEstlineItem.setRate(act.getSORCurrentRate().getValue());
                        preEstlineItem.setAmt(preEstlineItem.getQuantity() * act.getRate());
                        preEstlineItem.setActivity(act);
                        if (act.getSchedule().hasValidMarketRateFor(act.getAbstractEstimate().getEstimateDate()))
                            preEstlineItem.setMarketRate(preEstlineItem.getQuantity()
                                    * act.getSORCurrentMarketRate().getValue());
                        else
                            preEstlineItem.setMarketRate(act.getAmount().getValue());
                    } else {
                        preEstlineItem.setRate(preEstlineItem.getRate());
                        preEstlineItem.setAmt(preEstlineItem.getQuantity() * preEstlineItem.getRate()
                                * act.getConversionFactor());
                        preEstlineItem.setActivity(act);
                        if (act.getSchedule().hasValidMarketRateFor(act.getAbstractEstimate().getEstimateDate()))
                            preEstlineItem.setMarketRate(preEstlineItem.getQuantity()
                                    * act.getSORCurrentMarketRate().getValue());
                        else
                            preEstlineItem.setMarketRate(act.getAmount().getValue());
                    }
                    resultMap.put(act.getSchedule().getId(), preEstlineItem);
                } else {
                    addEstLineItem(act, estlineItem);
                    resultMap.put(act.getSchedule().getId(), estlineItem);
                }
            if (act.getNonSor() != null) {
                addEstLineItem(act, estlineItem);
                resultMap.put(act.getNonSor().getId(), estlineItem);
            }
        }
        return getEstLineItemsWithSrlNo(resultMap.values());
    }

    private void addEstLineItem(final Activity act, final EstimateLineItemsForWP estlineItem) {
        if (act.getSchedule() == null) {
            estlineItem.setCode("");
            estlineItem.setSummary("");
            estlineItem.setDescription(act.getNonSor().getDescription());
            estlineItem.setRate(act.getRate());
            estlineItem.setMarketRate(act.getAmount().getValue());
        } else {
            estlineItem.setCode(act.getSchedule().getCode());
            estlineItem.setDescription(act.getSchedule().getDescription());
            estlineItem.setRate(act.getSORCurrentRate().getValue());
            if (act.getSchedule().hasValidMarketRateFor(act.getAbstractEstimate().getEstimateDate()))
                estlineItem.setMarketRate(act.getQuantity() * act.getSORCurrentMarketRate().getValue());
            else
                estlineItem.setMarketRate(act.getAmount().getValue());
            estlineItem.setSummary(act.getSchedule().getSummary());
        }

        estlineItem.setActivity(act);
        estlineItem.setAmt(act.getQuantity() * act.getRate());
        estlineItem.setEstimateDate(act.getAbstractEstimate().getEstimateDate());
        estlineItem.setQuantity(act.getQuantity());
        estlineItem.setUom(act.getUom().getUom());
        estlineItem.setConversionFactor(act.getConversionFactor());
    }

    public List<Activity> getAllActivities() {
        final List<Activity> actList = new ArrayList<>();
        for (final AbstractEstimate ab : getAllEstimates())
            actList.addAll(ab.getActivities());
        return actList;
    }

    public List<Activity> getSorActivities() {
        final List<Activity> actList = Collections.emptyList();
        for (final Activity act : getAllActivities())
            if (act.getSchedule() != null)
                actList.add(act);
        return actList;
    }

    public List<Activity> getNonSorActivities() {
        final List<Activity> actList = Collections.emptyList();
        for (final Activity act : getAllActivities())
            if (act.getNonSor() != null)
                actList.add(act);
        return actList;
    }

    public double getTotalAmount() {
        double totalAmt = 0;
        for (final EstimateLineItemsForWP act : getActivitiesForEstimate())
            totalAmt += act.getAmt();
        return totalAmt;
    }

    public void setTotalAmount(final double totalAmount) {
        //Not set ?
    }

    public double getMarketRateTotalAmount() {
        double totalAmt = 0;
        for (final EstimateLineItemsForWP act : getActivitiesForEstimate())
            totalAmt += act.getMarketRate();
        return totalAmt;
    }

    private Collection<EstimateLineItemsForWP> getEstLineItemsWithSrlNo(final Collection<EstimateLineItemsForWP> actList) {
        int i = 1;
        final Collection<EstimateLineItemsForWP> latestEstLineItemList = new ArrayList<>();
        for (final EstimateLineItemsForWP act : actList) {
            act.setSrlNo(i);
            latestEstLineItemList.add(act);
            i++;
        }
        return latestEstLineItemList;
    }

    public List<AbstractEstimate> getAllEstimates() {
        final List<AbstractEstimate> abList = new ArrayList<>();
        if (!getWorksPackageDetails().isEmpty())
            for (final WorksPackageDetails wpd : getWorksPackageDetails())
                abList.add(wpd.getEstimate());
        return abList;
    }

    public String getTenderFileNumber() {
        return tenderFileNumber;
    }

    public void setTenderFileNumber(final String tenderFileNumber) {
        this.tenderFileNumber = tenderFileNumber;
    }

    public String getPackageNumberWithoutWP() {
        if (StringUtils.isNotBlank(wpNumber)) {
            final String[] number = wpNumber.split("/");
            return number.length == 0 ? "0" : number[2] + "/" + number[3];
        }
        return "0";
    }

    public String getNegotiationNumber() {
        String negotiationNumber = "";
        for (final TenderEstimate te : getTenderEstimateSet())
            for (final TenderResponse tr : te.getTenderResponseSet())
                if (WorksConstants.APPROVED.equals(tr.getEgwStatus().getCode())) {
                    negotiationNumber = tr.getNegotiationNumber();
                    break;
                }
        return negotiationNumber;
    }

    public Set<OfflineStatus> getOfflineStatuses() {
        //FIXME - Commented out for time being since it is giving issue on forward for already saved object
        /*
         * final Set<SetStatus> returnList = new HashSet<SetStatus>(); // Get only statuses which are of WorksPackage if
         * (setStatuses != null && setStatuses.size() > 0) for (final SetStatus ss : setStatuses) if (ss.getObjectType() != null
         * && ss.getObjectType().equalsIgnoreCase("WorksPackage")) returnList.add(ss);
         */
        return offlineStatuses;
    }

    public void setOfflineStatuses(final Set<OfflineStatus> offlineStatuses) {
        this.offlineStatuses = offlineStatuses;
    }

    public EgwStatus getEgwStatus() {
        return egwStatus;
    }

    public void setEgwStatus(final EgwStatus egwStatus) {
        this.egwStatus = egwStatus;
    }

    public String getWpOfflineStatus() {
        return wpOfflineStatus;
    }

    public void setWpOfflineStatus(final String wpOfflineStatus) {
        this.wpOfflineStatus = wpOfflineStatus;
    }

    public Set<TenderEstimate> getTenderEstimateSet() {
        return tenderEstimateSet;
    }

    public void setTenderEstimateSet(final Set<TenderEstimate> tenderEstimateSet) {
        this.tenderEstimateSet = tenderEstimateSet;
    }

    public List<RetenderHistory> getRetenderHistoryDetails() {
        return retenderHistoryDetails;
    }

    public void setRetenderHistoryDetails(final List<RetenderHistory> retenderHistoryDetails) {
        this.retenderHistoryDetails = retenderHistoryDetails;
    }

    public List<Retender> getRetenderDetails() {
        return retenderDetails;
    }

    public void setRetenderDetails(final List<Retender> retenderDetails) {
        this.retenderDetails = retenderDetails;
    }

    public OfflineStatus getLatestOfflineStatus() {
        return latestOfflineStatus;
    }

    public void setLatestOfflineStatus(final OfflineStatus latestOfflineStatus) {
        this.latestOfflineStatus = latestOfflineStatus;
    }

    public List<String> getWorksPackageActions() {
        return worksPackageActions;
    }

    public void setWorksPackageActions(final List<String> worksPackageActions) {
        this.worksPackageActions = worksPackageActions;
    }

    public String getWorksPackageStatus() {
        return worksPackageStatus;
    }

    public void setWorksPackageStatus(final String worksPackageStatus) {
        this.worksPackageStatus = worksPackageStatus;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(final Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public enum WorkPacakgeStatus {
        CREATED, CHECKED, APPROVED, REJECTED, CANCELLED, RESUBMITTED;

        @Override
        public String toString() {
            return StringUtils.capitalize(name());
        }
    }

    public enum Actions {
        SUBMIT_FOR_APPROVAL, APPROVE, REJECT, CANCEL;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}
