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
package org.egov.model.deduction;

public class AutoRemittanceBean {
    private int functionId;
    private int fundId;
    private double gldtlAmount;
    private int detailtypeId;
    private int detailkeyId;
    private int remittanceGldtlId;
    private int deptId;
    private double pendingAmount;
    private int generalledgerId;
    private int bankAccountId;

    public int getFundId() {
        return fundId;
    }

    public void setFundId(final int fundId) {
        this.fundId = fundId;
    }

    public double getGldtlAmount() {
        return gldtlAmount;
    }

    public void setGldtlAmount(final double gldtlAmount) {
        this.gldtlAmount = gldtlAmount;
    }

    public int getDetailtypeId() {
        return detailtypeId;
    }

    public void setDetailtypeId(final int detailtypeId) {
        this.detailtypeId = detailtypeId;
    }

    public int getDetailkeyId() {
        return detailkeyId;
    }

    public void setDetailkeyId(final int detailkeyId) {
        this.detailkeyId = detailkeyId;
    }

    public int getRemittanceGldtlId() {
        return remittanceGldtlId;
    }

    public void setRemittanceGldtlId(final int remittanceGldtlId) {
        this.remittanceGldtlId = remittanceGldtlId;
    }

    public int getFunctionId() {
        return functionId;
    }

    public void setFunctionId(final int functionId) {
        this.functionId = functionId;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(final int deptId) {
        this.deptId = deptId;
    }

    public double getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(final double pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    @Override
    public String toString() {
        return "AutoRemittanceBean [functionId=" + functionId + ", fundId="
                + fundId + ", gldtlAmount=" + gldtlAmount + ", detailtypeId="
                + detailtypeId + ", detailkeyId=" + detailkeyId
                + ", remittanceGldtlId=" + remittanceGldtlId + ", deptId=" + deptId
                + ", pendingAmount=" + pendingAmount + "]";
    }

    public int getGeneralledgerId() {
        return generalledgerId;
    }

    public void setGeneralledgerId(final int generalledgerId) {
        this.generalledgerId = generalledgerId;
    }

    public int getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(final int bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

}
