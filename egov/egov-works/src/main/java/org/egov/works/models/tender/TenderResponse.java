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
package org.egov.works.models.tender;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.egov.commons.EgwStatus;
import org.egov.infra.persistence.entity.component.Money;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.pims.model.PersonalInformation;
import org.egov.works.abstractestimate.entity.Activity;
import org.egov.works.models.workflow.WorkFlow;

public class TenderResponse extends WorkFlow {

    private static final long serialVersionUID = -6047271184417257561L;

    public enum TenderResponseStatus {
        CREATED, APPROVED, REJECTED, CANCELLED
    }

    public enum Actions {
        SAVE, SUBMIT_FOR_APPROVAL, REJECT, CANCEL, approval;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    @Valid
    private TenderEstimate tenderEstimate;

    // @Required(message="tenderResponse.percQuotedRate.null")
    private double percQuotedRate;

    // @Required(message="tenderResponse.percNegotiatedRate.null")
    private double percNegotiatedAmountRate;

    @Required(message = "tenderResponse.negotiationDate.null")
    @DateFormat(message = "invalid.fieldvalue.negotiationDate")
    private Date negotiationDate;

    private String negotiationNumber;

    private String narration;

    private String status;

    private double totalAmount;

    private Collection<EstimateLineItemsForWP> activitiesForWorkorder;

    private double workOrderAmount;

    @Valid
    private List<TenderResponseActivity> tenderResponseActivities = new LinkedList<TenderResponseActivity>();

    @Valid
    private List<TenderResponseContractors> tenderResponseContractors = new LinkedList<TenderResponseContractors>();

    // added by prashanth on jan 9th 2010
    // @Required(message = "tenderResponse.negotiationPreparedBy.null")
    private PersonalInformation negotiationPreparedBy;

    private Integer approverUserId;

    private Long documentNumber;

    private EgwStatus egwStatus;

    private String formattedTotalAmount;

    private double tenderNegotiatedValue;

    private Date approvedDate;

    private List<String> tenderNegotiationsActions = new ArrayList<String>();

    private List<WorksPackageDetails> worksPackageDetails = new LinkedList<WorksPackageDetails>();

    private Set<OfflineStatus> offlineStatuses = new HashSet<OfflineStatus>();

    public PersonalInformation getNegotiationPreparedBy() {
        return negotiationPreparedBy;
    }

    public void setNegotiationPreparedBy(final PersonalInformation negotiationPreparedBy) {
        this.negotiationPreparedBy = negotiationPreparedBy;
    }

    public TenderEstimate getTenderEstimate() {
        return tenderEstimate;
    }

    public void setTenderEstimate(final TenderEstimate tenderEstimate) {
        this.tenderEstimate = tenderEstimate;
    }

    public double getPercQuotedRate() {
        return percQuotedRate;
    }

    public String getFormattedPercQuotedRate() {
        final NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(10);
        return nf.format(percQuotedRate);
    }

    public String getFormattedPercNegotiatedAmountRate() {
        final NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(10);
        return nf.format(percNegotiatedAmountRate);
    }

    public void setPercQuotedRate(final double percQuotedRate) {
        this.percQuotedRate = percQuotedRate;
    }

    public double getPercNegotiatedAmountRate() {
        return percNegotiatedAmountRate;
    }

    public void setPercNegotiatedAmountRate(final double percNegotiatedAmountRate) {
        this.percNegotiatedAmountRate = percNegotiatedAmountRate;
    }

    public Date getNegotiationDate() {
        return negotiationDate;
    }

    public void setNegotiationDate(final Date negotiationDate) {
        this.negotiationDate = negotiationDate;
    }

    public List<TenderResponseActivity> getTenderResponseActivities() {
        return tenderResponseActivities;
    }

    public void setTenderResponseActivities(final List<TenderResponseActivity> tenderResponseActivities) {
        this.tenderResponseActivities = tenderResponseActivities;
    }

    public void addTenderResponseActivity(final TenderResponseActivity tenderResponseActivity) {
        tenderResponseActivities.add(tenderResponseActivity);
    }

    public String getNegotiationNumber() {
        return negotiationNumber;
    }

    public void setNegotiationNumber(final String negotiationNumber) {
        this.negotiationNumber = negotiationNumber;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public List<ValidationError> validate() {
        final List<ValidationError> validationErrors = new ArrayList<ValidationError>();
        if (!tenderResponseContractors.isEmpty()
                && (tenderResponseContractors.get(0) != null
                        && tenderResponseContractors.get(0).getContractor() != null
                        && tenderResponseContractors.get(0).getContractor().getId() == null
                        || tenderResponseContractors.get(0).getContractor().getId() == 0 || tenderResponseContractors
                                .get(0).getContractor().getId() == -1))
            validationErrors.add(new ValidationError("contractor", "tenderResponse.contractor.null"));
        else if (tenderResponseContractors == null)
            validationErrors.add(new ValidationError("contractor", "tenderResponse.contractor.null"));
        return validationErrors;
    }

    @Override
    public String getStateDetails() {
        return "Tender Negotiation : " + getNegotiationNumber();
    }

    /**
     * @return the narration
     */
    public String getNarration() {
        return narration;
    }

    /**
     * @param narration the narration to set
     */
    public void setNarration(final String narration) {
        this.narration = narration;
    }

    public Integer getApproverUserId() {
        return approverUserId;
    }

    public void setApproverUserId(final Integer approverUserId) {
        this.approverUserId = approverUserId;
    }

    public Long getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(final Long documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public void setTotalAmount(final double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Collection<EstimateLineItemsForWP> getActivitiesForWorkorder() {
        return activitiesForWorkorder;
    }

    public void setActivitiesForWorkorder(final Collection<EstimateLineItemsForWP> activitiesForWorkorder) {
        this.activitiesForWorkorder = activitiesForWorkorder;
    }

    public double getWorkOrderAmount() {
        return workOrderAmount;
    }

    public void setWorkOrderAmount(final double workOrderAmount) {
        this.workOrderAmount = workOrderAmount;
    }

    public List<String> getTenderNegotiationsActions() {
        return tenderNegotiationsActions;
    }

    public void setTenderNegotiationsActions(final List<String> tenderNegotiationsActions) {
        this.tenderNegotiationsActions = tenderNegotiationsActions;
    }

    public List<TenderResponseContractors> getTenderResponseContractors() {
        return tenderResponseContractors;
    }

    public void setTenderResponseContractors(final List<TenderResponseContractors> tenderResponseContractors) {
        this.tenderResponseContractors = tenderResponseContractors;
    }

    public void addTenderResponseContractors(final TenderResponseContractors tenderResponseContractors) {
        this.tenderResponseContractors.add(tenderResponseContractors);
    }

    public Money getTotalNegotiatedQuantity() {
        Money totalNegotiatedQuantity;
        double qty = 0;
        for (final TenderResponseActivity tra : tenderResponseActivities)
            qty += tra.getNegotiatedQuantity();
        totalNegotiatedQuantity = new Money(qty);
        return totalNegotiatedQuantity;
    }

    public Collection<EstimateLineItemsForTR> getNegotiationDetails() {
        final Map<Long, EstimateLineItemsForTR> resultMap = new HashMap<Long, EstimateLineItemsForTR>();
        for (final TenderResponseActivity tra : getTenderResponseActivities()) {
            final EstimateLineItemsForTR estlineItem = new EstimateLineItemsForTR();
            if (tra.getActivity().getSchedule() != null)
                if (resultMap.containsKey(tra.getActivity().getSchedule().getId())) {
                    final EstimateLineItemsForTR preEstlineItem = resultMap
                            .get(tra.getActivity().getSchedule().getId());
                    preEstlineItem.setQuantity(tra.getActivity().getQuantity() + preEstlineItem.getQuantity());
                    if (DateUtils.compareDates(tra.getActivity().getAbstractEstimate().getEstimateDate(),
                            preEstlineItem.getEstimateDate())) {
                        preEstlineItem.setRate(tra.getActivity().getSORCurrentRate().getValue());
                        preEstlineItem.setAmt(preEstlineItem.getQuantity() * tra.getActivity().getRate());
                        preEstlineItem.setActivity(tra.getActivity());
                        if (tra.getActivity().getSchedule()
                                .hasValidMarketRateFor(tra.getActivity().getAbstractEstimate().getEstimateDate()))
                            preEstlineItem.setMarketRate(preEstlineItem.getQuantity()
                                    * tra.getActivity().getSORCurrentMarketRate().getValue());
                        else
                            preEstlineItem.setMarketRate(tra.getActivity().getAmount().getValue());
                    }
                    preEstlineItem.setTenderResponseQuotes(tra.getTenderResponseQuotes());
                    preEstlineItem.setNegotiatedRate(tra.getNegotiatedRate());
                    resultMap.put(tra.getActivity().getSchedule().getId(), preEstlineItem);
                } else {
                    addEstLineItem(tra.getActivity(), estlineItem);
                    estlineItem.setNegotiatedRate(tra.getNegotiatedRate());
                    estlineItem.setTenderResponseQuotes(tra.getTenderResponseQuotes());
                    resultMap.put(tra.getActivity().getSchedule().getId(), estlineItem);
                }
            if (tra.getActivity().getNonSor() != null) {
                addEstLineItem(tra.getActivity(), estlineItem);
                estlineItem.setNegotiatedRate(tra.getNegotiatedRate());
                estlineItem.setTenderResponseQuotes(tra.getTenderResponseQuotes());
                resultMap.put(tra.getActivity().getNonSor().getId(), estlineItem);
            }
        }
        return getEstLineItemsWithSrlNo(resultMap.values());
    }

    public List<WorksPackageDetails> getWorksPackageDetails() {
        return worksPackageDetails;
    }

    public void setWorksPackageDetails(final List<WorksPackageDetails> worksPackageDetails) {
        this.worksPackageDetails = worksPackageDetails;
    }

    private void addEstLineItem(final Activity act, final EstimateLineItemsForTR estlineItem) {
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

    private Collection<EstimateLineItemsForTR> getEstLineItemsWithSrlNo(final Collection<EstimateLineItemsForTR> actList) {
        int i = 1;
        final Collection<EstimateLineItemsForTR> latestEstLineItemList = new ArrayList<EstimateLineItemsForTR>();
        for (final EstimateLineItemsForTR act : actList) {
            act.setSrlNo(i);
            latestEstLineItemList.add(act);
            i++;
        }
        return latestEstLineItemList;
    }

    public Set<OfflineStatus> getOfflineStatuses() {
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

    public String getFormattedTotalAmount() {
        return formattedTotalAmount;
    }

    public void setFormattedTotalAmount(final String formattedTotalAmount) {
        this.formattedTotalAmount = formattedTotalAmount;
    }

    public double getTenderNegotiatedValue() {
        return tenderNegotiatedValue;
    }

    public void setTenderNegotiatedValue(final double tenderNegotiatedValue) {
        this.tenderNegotiatedValue = tenderNegotiatedValue;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(final Date approvedDate) {
        this.approvedDate = approvedDate;
    }

}
