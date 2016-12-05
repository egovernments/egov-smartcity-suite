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
package org.egov.works.contractorbill.entity;

import org.egov.works.workorder.entity.WorkOrderActivity;

public class ContractorBillCertificateInfo {

    private WorkOrderActivity workOrderActivity;
    private double executionQuantity;
    private double lastExecutionQuantity;
    private double lastExecutionAmount;
    private double executionAmount;
    private double tenderRate;
    private double tenderQuantity;
    private double tenderAmount;
    private double executionRate;
    private double totalAsPerTender;
    private double totalAsPerExecution;
    private double totalDifference;

    public WorkOrderActivity getWorkOrderActivity() {
        return workOrderActivity;
    }

    public void setWorkOrderActivity(final WorkOrderActivity workOrderActivity) {
        this.workOrderActivity = workOrderActivity;
    }

    public double getExecutionQuantity() {
        return executionQuantity;
    }

    public void setExecutionQuantity(final double executionQuantity) {
        this.executionQuantity = executionQuantity;
    }

    public double getLastExecutionQuantity() {
        return lastExecutionQuantity;
    }

    public void setLastExecutionQuantity(final double lastExecutionQuantity) {
        this.lastExecutionQuantity = lastExecutionQuantity;
    }

    public double getLastExecutionAmount() {
        return lastExecutionAmount;
    }

    public void setLastExecutionAmount(final double lastExecutionAmount) {
        this.lastExecutionAmount = lastExecutionAmount;
    }

    public double getExecutionAmount() {
        return executionAmount;
    }

    public void setExecutionAmount(final double executionAmount) {
        this.executionAmount = executionAmount;
    }

    public double getTenderRate() {
        return tenderRate;
    }

    public void setTenderRate(final double tenderRate) {
        this.tenderRate = tenderRate;
    }

    public double getTenderQuantity() {
        return tenderQuantity;
    }

    public void setTenderQuantity(final double tenderQuantity) {
        this.tenderQuantity = tenderQuantity;
    }

    public double getTenderAmount() {
        return tenderAmount;
    }

    public void setTenderAmount(final double tenderAmount) {
        this.tenderAmount = tenderAmount;
    }

    public double getExecutionRate() {
        return executionRate;
    }

    public void setExecutionRate(final double executionRate) {
        this.executionRate = executionRate;
    }

    public double getTotalAsPerTender() {
        return totalAsPerTender;
    }

    public void setTotalAsPerTender(final double totalAsPerTender) {
        this.totalAsPerTender = totalAsPerTender;
    }

    public double getTotalAsPerExecution() {
        return totalAsPerExecution;
    }

    public void setTotalAsPerExecution(final double totalAsPerExecution) {
        this.totalAsPerExecution = totalAsPerExecution;
    }

    public double getTotalDifference() {
        return totalDifference;
    }

    public void setTotalDifference(final double totalDifference) {
        this.totalDifference = totalDifference;
    }

}
