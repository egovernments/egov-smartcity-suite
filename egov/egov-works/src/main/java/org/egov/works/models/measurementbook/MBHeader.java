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
package org.egov.works.models.measurementbook;

import org.egov.commons.EgwStatus;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.GreaterThan;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.pims.commons.Position;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MBHeader extends StateAware<Position> {

    private static final long serialVersionUID = 121631467636260459L;
    private Long id;

    @Required(message = "mbheader.workorder.null")
    private WorkOrder workOrder;
    @Required(message = "mbheader.mbrefno.null")
    @Length(max = 50, message = "mbheader.mbrefno.length")
    private String mbRefNo;

    @Length(max = 400, message = "mbheader.contractorComments.length")
    private String contractorComments;

    @Required(message = "mbheader.mbdate.null")
    @ValidateDate(allowPast = true, dateFormat = "dd/MM/yyyy", message = "mbheader.mbDate.futuredate")
    @DateFormat(message = "invalid.fieldvalue.mbDate")
    private Date mbDate;

    @Length(max = 400, message = "mbheader.mbabstract.length")
    private String mbAbstract;

    @Required(message = "mbheader.fromPageNo.null")
    @GreaterThan(value = 0, message = "mbheader.fromPageNo.non.negative")
    private Integer fromPageNo;

    @Min(value = 0, message = "mbheader.toPageNo.non.negative")
    private Integer toPageNo;
    private WorkOrderEstimate workOrderEstimate;
    private Integer approverUserId;
    private ContractorBillRegister egBillregister;

    @Valid
    private List<MBDetails> mbDetails = new LinkedList<>();
    private String owner;
    private List<String> mbActions = new ArrayList<>();
    private EgwStatus egwStatus;
    private boolean isLegacyMB;
    private BigDecimal mbAmount;
    private Date approvedDate;

    public List<ValidationError> validate() {
        final List<ValidationError> validationErrors = new ArrayList<>();
        if (workOrder != null && (workOrder.getId() == null || workOrder.getId() == 0 || workOrder.getId() == -1))
            validationErrors.add(new ValidationError("workOrder", "mbheader.workorder.null"));

        if (fromPageNo != null && toPageNo != null && fromPageNo > toPageNo)
            validationErrors.add(new ValidationError("toPageNo", "mbheader.toPageNo.invalid"));
        if (mbDate != null && workOrder != null && workOrder.getWorkOrderDate() != null
                && mbDate.before(workOrder.getWorkOrderDate()))
            validationErrors.add(new ValidationError("mbDate", "mbheader.mbDate.invalid"));

        return validationErrors;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(final WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public String getMbRefNo() {
        return mbRefNo;
    }

    public void setMbRefNo(final String mbRefNo) {
        this.mbRefNo = mbRefNo;
    }

    public Date getMbDate() {
        return mbDate;
    }

    public void setMbDate(final Date mbDate) {
        this.mbDate = mbDate;
    }

    public String getMbAbstract() {
        return mbAbstract;
    }

    public void setMbAbstract(final String mbAbstract) {
        this.mbAbstract = mbAbstract;
    }

    public Integer getFromPageNo() {
        return fromPageNo;
    }

    public void setFromPageNo(final Integer fromPageNo) {
        this.fromPageNo = fromPageNo;
    }

    public Integer getToPageNo() {
        return toPageNo;
    }

    public void setToPageNo(final Integer toPageNo) {
        this.toPageNo = toPageNo;
    }

    public String getContractorComments() {
        return contractorComments;
    }

    public void setContractorComments(final String contractorComments) {
        this.contractorComments = contractorComments;
    }

    public List<MBDetails> getMbDetails() {
        return mbDetails;
    }

    public void setMbDetails(final List<MBDetails> mbDetails) {
        this.mbDetails = mbDetails;
    }

    public void addMbDetails(final MBDetails mbDetails) {
        this.mbDetails.add(mbDetails);
    }

    // to show in inbox
    @Override
    public String getStateDetails() {
        return "MbHeader : " + getMbRefNo();
    }

    public ContractorBillRegister getEgBillregister() {
        return egBillregister;
    }

    public void setEgBillregister(final ContractorBillRegister egBillregister) {
        this.egBillregister = egBillregister;
    }

    public Integer getApproverUserId() {
        return approverUserId;
    }

    public void setApproverUserId(final Integer approverUserId) {
        this.approverUserId = approverUserId;
    }

    public WorkOrderEstimate getWorkOrderEstimate() {
        return workOrderEstimate;
    }

    public void setWorkOrderEstimate(final WorkOrderEstimate workOrderEstimate) {
        this.workOrderEstimate = workOrderEstimate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public List<String> getMbActions() {
        return mbActions;
    }

    public void setMbActions(final List<String> mbActions) {
        this.mbActions = mbActions;
    }

    public EgwStatus getEgwStatus() {
        return egwStatus;
    }

    public void setEgwStatus(final EgwStatus egwStatus) {
        this.egwStatus = egwStatus;
    }

    public boolean getIsLegacyMB() {
        return isLegacyMB;
    }

    public void setIsLegacyMB(final boolean isLegacyMB) {
        this.isLegacyMB = isLegacyMB;
    }

    public BigDecimal getMbAmount() {
        return mbAmount;
    }

    public void setMbAmount(final BigDecimal mbAmount) {
        this.mbAmount = mbAmount;
    }

    @Override
    public String toString() {
        return "MBHeader ( Id : " + getId() + "MB Ref No: " + mbRefNo + ")";
    }

    public BigDecimal getTotalMBAmount() {
        double amount;
        BigDecimal resultAmount = BigDecimal.ZERO;
        for (final MBDetails mbd : mbDetails) {
            if (mbd.getWorkOrderActivity().getActivity().getNonSor() == null)
                amount = mbd.getWorkOrderActivity().getApprovedRate() * mbd.getQuantity()
                        * mbd.getWorkOrderActivity().getConversionFactor();
            else
                amount = mbd.getWorkOrderActivity().getApprovedRate() * mbd.getQuantity();
            resultAmount = resultAmount.add(BigDecimal.valueOf(amount));
        }
        return resultAmount;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(final Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public enum MeasurementBookStatus {
        NEW, CREATED, CHECKED, REJECTED, RESUBMITTED, CANCELLED, APPROVED
    }

    public enum Actions {
        SAVE, SUBMIT_FOR_APPROVAL, REJECT, CANCEL, APPROVAL;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

}