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
package org.egov.works.models.contractorBill;

import org.egov.infra.workflow.entity.StateHistory;
import org.egov.pims.commons.Position;
import org.egov.works.models.workorder.WorkOrderEstimate;

import java.util.Date;
import java.util.List;

public class WorkCompletionInfo {

    private WorkOrderEstimate workOrderEstimate;
    private String mbNumbers;
    private Date workCommencedOnDate;
    private List<StateHistory<Position>> workflowHistory;

    /**
     * @param workOrderEstimate
     */
    public WorkCompletionInfo(final WorkOrderEstimate workOrderEstimate) {
        this.workOrderEstimate = workOrderEstimate;
    }

    public WorkCompletionInfo() {

    }

    /**
     * @param contractorBillregister
     * @param workOrderEstimate
     */
    public WorkCompletionInfo(final WorkOrderEstimate workOrderEstimate, final String mbNumbers) {
        this.workOrderEstimate = workOrderEstimate;
        this.mbNumbers = mbNumbers;
    }

    /**
     * @return name of work
     */
    public String getWorkName() {
        return workOrderEstimate.getEstimate().getName();
    }

    /**
     * @return estimate number
     */
    public String getEstimateNo() {
        return workOrderEstimate.getEstimate().getEstimateNumber();
    }

    /**
     * @return estimate amount
     */
    public String getEstimateAmount() {
        return workOrderEstimate.getEstimate().getTotalAmount().getFormattedString();
    }

    /**
     * @return estimate amount value
     */
    public Double getEstimateAmountValue() {
        return workOrderEstimate.getEstimate().getTotalAmount().getValue();

    }

    /**
     * @return estimate name
     */
    public String getEstimateName() {
        return workOrderEstimate.getEstimate().getName();
    }

    /**
     * @return budgetheader
     */
    public String getBudgetHeader() {
        return workOrderEstimate.getEstimate().getFinancialDetails() == null ? null : workOrderEstimate.getEstimate()
                .getFinancialDetails().get(0).getBudgetGroup() == null ? null : workOrderEstimate.getEstimate()
                .getFinancialDetails().get(0).getBudgetGroup().getName();
    }

    /**
     * @return appr no
     */
    public String getApprNo() {
        // TODO:Fixme: Get latest appropriation number from estimate and display here
        return "";
    }

    /**
     * @return project code
     */
    public String getProjectCode() {
        return workOrderEstimate.getEstimate().getProjectCode() == null ? null : workOrderEstimate.getEstimate()
                .getProjectCode().getCode();
    }

    /**
     * @return contractor name
     */
    public String getContractorName() {
        return workOrderEstimate.getWorkOrder().getContractor().getName();
    }

    /**
     * @return list of mbno as string
     */
    public String getAllMBNO() {
        return mbNumbers;
    }

    /**
     * @return work commenced date
     */
    public Date getWorkCommencedOn() {
        return workCommencedOnDate;
    }

    /**
     * @set the work commeced date
     */
    public void setWorkCommencedOn(final Date workCommencedOnDate) {
        this.workCommencedOnDate = workCommencedOnDate;
    }

    /**
     * @return work completed date
     */
    public Date getWorkCompletedDate() {
        return workOrderEstimate.getWorkCompletionDate();
    }

    /**
     * @return workflow history
     */
    public List<StateHistory<Position>> getWorkflowHistory() {
        return workflowHistory;
    }

    /**
     * @set the history
     */
    public void setWorkflowHistory(final List<StateHistory<Position>> workflowHistory) {
        this.workflowHistory = workflowHistory;
    }
}
