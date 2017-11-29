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
package org.egov.model.budget;

import java.math.BigDecimal;

public class BudgetProposalBean {
    private String accountCode;
    private Long id = null;
    private Long nextYrId = null;
    private String budget = "";
    private String fund = "";
    private String function = "";
    private String budgetGroup = "";
    private String executingDepartment = "";
    private String previousYearActuals;
    private String twoPreviousYearActuals;
    private String currentYearActuals;
    private String currentYearBE;
    private String reappropriation;
    private String total;
    private String anticipatory;
    private BigDecimal proposedRE = BigDecimal.ZERO;
    private BigDecimal proposedBE = BigDecimal.ZERO;
    private BigDecimal approvedRE = BigDecimal.ZERO;
    private BigDecimal approvedBE = BigDecimal.ZERO;
    private String remarks = "";
    private Long documentNumber;
    private Long stateId;
    private String rowType = "detail";
    private String reference;

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(final Long stateId) {
        this.stateId = stateId;
    }

    public BudgetProposalBean() {

    }

    public BudgetProposalBean(final String name, final String type) {
        budgetGroup = name;
        rowType = type;
        proposedBE = null;
        proposedRE = null;
        approvedRE = null;
        approvedBE = null;
    }

    public BudgetProposalBean(final String name, final String type, final String ref) {
        budgetGroup = name;
        rowType = type;
        reference = ref;
        // this.proposedBE=null;
        // this.proposedRE=null;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(final String budget) {
        this.budget = budget;
    }

    public String getFund() {
        return fund;
    }

    public void setFund(final String fund) {
        this.fund = fund;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(final String function) {
        this.function = function;
    }

    public String getBudgetGroup() {
        return budgetGroup;
    }

    public void setBudgetGroup(final String budgetGroup) {
        this.budgetGroup = budgetGroup;
    }

    public String getPreviousYearActuals() {
        return previousYearActuals;
    }

    public void setPreviousYearActuals(final String previousYearActuals) {
        this.previousYearActuals = previousYearActuals;
    }

    public String getTwoPreviousYearActuals() {
        return twoPreviousYearActuals;
    }

    public void setTwoPreviousYearActuals(final String twoPreviousYearActuals) {
        this.twoPreviousYearActuals = twoPreviousYearActuals;
    }

    public String getCurrentYearActuals() {
        return currentYearActuals;
    }

    public void setCurrentYearActuals(final String currentYearActuals) {
        this.currentYearActuals = currentYearActuals;
    }

    public String getCurrentYearBE() {
        return currentYearBE;
    }

    public void setCurrentYearBE(final String currentYearBE) {
        this.currentYearBE = currentYearBE;
    }

    public String getReappropriation() {
        return reappropriation;
    }

    public void setReappropriation(final String reappropriation) {
        this.reappropriation = reappropriation;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(final String total) {
        this.total = total;
    }

    public String getAnticipatory() {
        return anticipatory;
    }

    public void setAnticipatory(final String anticipatory) {
        this.anticipatory = anticipatory;
    }

    public BigDecimal getProposedRE() {
        return proposedRE;
    }

    public void setProposedRE(final BigDecimal proposedRE) {
        this.proposedRE = proposedRE;
    }

    public BigDecimal getProposedBE() {
        return proposedBE;
    }

    public void setProposedBE(final BigDecimal proposedBE) {
        this.proposedBE = proposedBE;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public String getExecutingDepartment() {
        return executingDepartment;
    }

    public void setExecutingDepartment(final String executingDepartment) {
        this.executingDepartment = executingDepartment;
    }

    public BigDecimal getApprovedRE() {
        return approvedRE;
    }

    public void setApprovedRE(final BigDecimal approvedRE) {
        this.approvedRE = approvedRE;
    }

    public BigDecimal getApprovedBE() {
        return approvedBE;
    }

    public void setApprovedBE(final BigDecimal approvedBE) {
        this.approvedBE = approvedBE;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getNextYrId() {
        return nextYrId;
    }

    public void setNextYrId(final Long nextYrId) {
        this.nextYrId = nextYrId;
    }

    public Long getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(final Long documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getRowType() {
        return rowType;
    }

    public void setRowType(final String rowType) {
        this.rowType = rowType;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(final String reference) {
        this.reference = reference;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(final String accountCode) {
        this.accountCode = accountCode;
    }

}
