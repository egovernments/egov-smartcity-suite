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
package org.egov.egf.web.actions.budget;

import java.math.BigDecimal;

public class BudgetReportView {

    private String deptCode = "";
    private String functionCode = "";
    private String glCode = "";
    private String narration = "";
    private String reference;
    private BigDecimal amount;
    private BigDecimal appropriationAmount;
    private BigDecimal totalAmount;
    private String rowStyle;
    private BigDecimal reProposalAmount;
    private BigDecimal beProposalAmount;
    private BigDecimal reRecomAmount;
    private BigDecimal beRecomAmount;

    private BigDecimal reProposalTotalAmount;
    private BigDecimal beProposalTotalAmount;
    private BigDecimal reRecomTotalAmount;
    private BigDecimal beRecomTotalAmount;

    public BigDecimal getReProposalTotalAmount() {
        return reProposalTotalAmount;
    }

    public void setReProposalTotalAmount(final BigDecimal reProposalTotalAmount) {
        this.reProposalTotalAmount = reProposalTotalAmount;
    }

    public BigDecimal getBeProposalTotalAmount() {
        return beProposalTotalAmount;
    }

    public void setBeProposalTotalAmount(final BigDecimal beProposalTotalAmount) {
        this.beProposalTotalAmount = beProposalTotalAmount;
    }

    public BigDecimal getReRecomTotalAmount() {
        return reRecomTotalAmount;
    }

    public void setReRecomTotalAmount(final BigDecimal reRecomTotalAmount) {
        this.reRecomTotalAmount = reRecomTotalAmount;
    }

    public BigDecimal getBeRecomTotalAmount() {
        return beRecomTotalAmount;
    }

    public void setBeRecomTotalAmount(final BigDecimal beRecomTotalAmount) {
        this.beRecomTotalAmount = beRecomTotalAmount;
    }

    public BigDecimal getReProposalAmount() {
        return reProposalAmount;
    }

    public void setReProposalAmount(final BigDecimal reProposalAmount) {
        this.reProposalAmount = reProposalAmount;
    }

    public BigDecimal getBeProposalAmount() {
        return beProposalAmount;
    }

    public void setBeProposalAmount(final BigDecimal beProposalAmount) {
        this.beProposalAmount = beProposalAmount;
    }

    public BigDecimal getReRecomAmount() {
        return reRecomAmount;
    }

    public void setReRecomAmount(final BigDecimal reRecomAmount) {
        this.reRecomAmount = reRecomAmount;
    }

    public BigDecimal getBeRecomAmount() {
        return beRecomAmount;
    }

    public void setBeRecomAmount(final BigDecimal beRecomAmount) {
        this.beRecomAmount = beRecomAmount;
    }

    private Integer deptId;
    private Long functionId;
    private String type = "";
    private String majorCode = "";
    private BigDecimal tempamount = BigDecimal.ZERO;
    private Long detailId;

    public BudgetReportView(final String glCode, final String narration, final String reference, final BigDecimal amount,
            final BigDecimal appropriationAmount, final BigDecimal totalAmount) {
        this.glCode = glCode;
        this.narration = narration;
        this.reference = reference;
        this.amount = amount;
        this.appropriationAmount = appropriationAmount;
        this.totalAmount = totalAmount;
    }

    public BudgetReportView(final String deptCode, final String functionCode, final String glCode, final String narration,
            final String reference,
            final BigDecimal amount, final BigDecimal appropriationAmount, final BigDecimal totalAmount, final String rowstyle) {
        this.deptCode = deptCode;
        this.functionCode = functionCode;
        this.glCode = glCode;
        this.narration = narration;
        this.reference = reference;
        this.amount = amount;
        rowStyle = rowstyle;
        this.appropriationAmount = appropriationAmount;
        this.totalAmount = totalAmount;
    }

    public BudgetReportView(final String deptCode, final String functionCode, final String glCode, final String narration,
            final String reference,
            final BigDecimal reProposalAmount, final BigDecimal reRecomAmount, final BigDecimal beProposalAmount,
            final BigDecimal beRecomAmount,
            final String rowstyle) {
        this.deptCode = deptCode;
        this.functionCode = functionCode;
        this.glCode = glCode;
        this.narration = narration;
        this.reference = reference;
        this.beProposalAmount = beProposalAmount;
        this.reProposalAmount = reProposalAmount;
        this.beRecomAmount = beRecomAmount;
        this.reRecomAmount = reRecomAmount;
        rowStyle = rowstyle;

    }

    public BudgetReportView(final Integer deptId, final Long functionId, final String type, final String majorcode,
            final BigDecimal tempamount,
            final BigDecimal appropriationAmount,
            final BigDecimal totalAmount) {
        this.deptId = deptId;
        this.functionId = functionId;
        this.type = type;
        majorCode = majorcode;
        this.tempamount = tempamount;
        this.appropriationAmount = appropriationAmount;
        this.totalAmount = totalAmount;
    }

    public BudgetReportView()
    {

    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(final String deptCode) {
        this.deptCode = deptCode;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(final String functionCode) {
        this.functionCode = functionCode;
    }

    public String getGlCode() {
        return glCode;
    }

    public void setGlCode(final String glCode) {
        this.glCode = glCode;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(final String narration) {
        this.narration = narration;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(final String reference) {
        this.reference = reference;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getAmountAsString() {
        return format(amount);
    }

    private String format(final BigDecimal value) {
        return value == null ? "" : value.setScale(2).toPlainString();
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public String getRowStyle() {
        return rowStyle;
    }

    public void setRowStyle(final String rowStyle) {
        this.rowStyle = rowStyle;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(final Integer deptId) {
        this.deptId = deptId;
    }

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(final Long functionId) {
        this.functionId = functionId;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getMajorCode() {
        return majorCode;
    }

    public void setMajorCode(final String majorCode) {
        this.majorCode = majorCode;
    }

    public BigDecimal getTempamount() {
        return tempamount;
    }

    public void setTempamount(final BigDecimal tempamount) {
        this.tempamount = tempamount;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + (deptCode == null ? 0 : deptCode.hashCode());
        result = prime * result
                + (functionCode == null ? 0 : functionCode.hashCode());
        result = prime * result + (glCode == null ? 0 : glCode.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final BudgetReportView other = (BudgetReportView) obj;
        if (deptCode == null) {
            if (other.deptCode != null)
                return false;
        } else if (!deptCode.equals(other.deptCode))
            return false;
        if (functionCode == null) {
            if (other.functionCode != null)
                return false;
        } else if (!functionCode.equals(other.functionCode))
            return false;
        if (glCode == null) {
            if (other.glCode != null)
                return false;
        } else if (!glCode.equals(other.glCode))
            return false;
        return true;
    }

    public void setDetailId(final Long detailId) {
        this.detailId = detailId;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setAppropriationAmount(final BigDecimal appropriationAmount) {
        this.appropriationAmount = appropriationAmount;
    }

    public BigDecimal getAppropriationAmount() {
        return appropriationAmount;
    }

    public String getAppropriationAmountAsString() {
        return format(appropriationAmount);
    }

    public void setTotalAmount(final BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getTotalAmountAsString() {
        return format(totalAmount);
    }

}
