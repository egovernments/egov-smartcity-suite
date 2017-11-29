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
package org.egov.egf.model;

import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetDetail;
import org.egov.utils.Constants;

import java.math.BigDecimal;

public class BudgetReAppropriationView {
    Long id;
    private BudgetDetail budgetDetail = new BudgetDetail();
    Budget budget;
    private BigDecimal deltaAmount = new BigDecimal(Constants.ZERO);
    private BigDecimal approvedDeltaAmount = new BigDecimal(Constants.ZERO);
    private BigDecimal addedReleased = new BigDecimal(Constants.ZERO);
    private BigDecimal approvedAmount = new BigDecimal(Constants.ZERO);
    private BigDecimal appropriatedAmount = new BigDecimal(Constants.ZERO);
    private BigDecimal actuals = new BigDecimal(Constants.ZERO);
    private BigDecimal anticipatoryAmount = new BigDecimal(Constants.ZERO);
    private BigDecimal availableAmount = new BigDecimal(Constants.ZERO);
    private BigDecimal planningPercent = new BigDecimal(Constants.ZERO);
    private BigDecimal planningBudgetApproved = new BigDecimal(Constants.ZERO);
    private BigDecimal planningBudgetUsage = new BigDecimal(Constants.ZERO);
    private BigDecimal planningBudgetAvailable = new BigDecimal(Constants.ZERO);
    public String changeRequestType;
    public String sequenceNumber;
    public String comments;

    public String getComments() {
        return comments;
    }

    public void setComments(final String comments) {
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(final String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(final BigDecimal availableAmount) {
        this.availableAmount = availableAmount;
    }

    public void setBudget(final Budget budget) {
        this.budget = budget;
    }

    public void setDeltaAmount(final BigDecimal deltaAmount) {
        this.deltaAmount = deltaAmount;
    }

    public Budget getBudget() {
        return budget;
    }

    public BigDecimal getDeltaAmount() {
        return deltaAmount;
    }

    public BigDecimal getApprovedAmount() {
        return approvedAmount;
    }

    public BigDecimal getActuals() {
        return actuals;
    }

    public String getChangeRequestType() {
        return changeRequestType;
    }

    public void setApprovedAmount(final BigDecimal approvedAmount) {
        this.approvedAmount = approvedAmount;
    }

    public void setActuals(final BigDecimal actuals) {
        this.actuals = actuals;
    }

    public void setChangeRequestType(final String changeRequestType) {
        this.changeRequestType = changeRequestType;
    }

    public BigDecimal getAnticipatoryAmount() {
        return anticipatoryAmount;
    }

    public void setAnticipatoryAmount(final BigDecimal anticipatoryAmount) {
        this.anticipatoryAmount = anticipatoryAmount;
    }

    public BudgetDetail getBudgetDetail() {
        return budgetDetail;
    }

    public void setBudgetDetail(final BudgetDetail budgetDetail) {
        this.budgetDetail = budgetDetail;
    }

    public void setAppropriatedAmount(final BigDecimal appropriatedAmount) {
        this.appropriatedAmount = appropriatedAmount;
    }

    public BigDecimal getAppropriatedAmount() {
        return appropriatedAmount;
    }

    public void setAddedReleased(final BigDecimal addedReleased) {
        this.addedReleased = addedReleased;
    }

    public BigDecimal getAddedReleased() {
        return addedReleased;
    }

    public void setApprovedDeltaAmount(final BigDecimal approvedDeltaAmount) {
        this.approvedDeltaAmount = approvedDeltaAmount;
    }

    public BigDecimal getApprovedDeltaAmount() {
        return approvedDeltaAmount;
    }

    public BigDecimal getPlanningPercent() {
        return planningPercent;
    }

    public void setPlanningPercent(final BigDecimal planningPercent) {
        this.planningPercent = planningPercent;
    }

    public BigDecimal getPlanningBudgetApproved() {
        return planningBudgetApproved;
    }

    public void setPlanningBudgetApproved(final BigDecimal planningBudgetApproved) {
        this.planningBudgetApproved = planningBudgetApproved;
    }

    public BigDecimal getPlanningBudgetUsage() {
        return planningBudgetUsage;
    }

    public void setPlanningBudgetUsage(final BigDecimal planningBudgetUsage) {
        this.planningBudgetUsage = planningBudgetUsage;
    }

    public BigDecimal getPlanningBudgetAvailable() {
        return planningBudgetAvailable;
    }

    public void setPlanningBudgetAvailable(final BigDecimal planningBudgetAvailable) {
        this.planningBudgetAvailable = planningBudgetAvailable;
    }

}
