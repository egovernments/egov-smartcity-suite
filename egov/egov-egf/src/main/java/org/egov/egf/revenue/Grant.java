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
package org.egov.egf.revenue;

import org.egov.commons.CFinancialYear;
import org.egov.commons.CVoucherHeader;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infstr.models.BaseModel;
import org.egov.model.instrument.InstrumentHeader;

import java.math.BigDecimal;
import java.util.Date;

public class Grant extends BaseModel {
    private static final long serialVersionUID = 5059477505404700650L;
    private Department department;
    private CFinancialYear financialYear;
    private String period;// is a string with I half,II half,Quarter I,Quarter II etc
    private String proceedingsNo;
    private Date proceedingsDate;
    private CVoucherHeader accrualVoucher;
    private BigDecimal accrualAmount;
    private CVoucherHeader generalVoucher; // GJV
    private CVoucherHeader receiptVoucher;
    private BigDecimal grantAmount; // either Receipt or GJV amount
    private InstrumentHeader ihID;
    private String remarks;
    private String grantType;// like SFC,CFC,StampDuty etc
    private String commTaxOfficer;

    public Department getDepartment() {
        return department;
    }

    public String getPeriod() {
        return period;
    }

    public String getProceedingsNo() {
        return proceedingsNo;
    }

    public Date getProceedingsDate() {
        return proceedingsDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setDepartment(final Department department) {
        this.department = department;
    }

    public void setPeriod(final String period) {
        this.period = period;
    }

    public void setProceedingsNo(final String proceedingsNo) {
        this.proceedingsNo = proceedingsNo;
    }

    public void setProceedingsDate(final Date proceedingsDate) {
        this.proceedingsDate = proceedingsDate;
    }

    public void setAccrualAmount(final BigDecimal accrualAmount) {
        this.accrualAmount = accrualAmount;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public void setGrantType(final String grantType) {
        this.grantType = grantType;
    }

    public CVoucherHeader getAccrualVoucher() {
        return accrualVoucher;
    }

    public void setAccrualVoucher(final CVoucherHeader accrualVoucher) {
        this.accrualVoucher = accrualVoucher;
    }

    public CVoucherHeader getGeneralVoucher() {
        return generalVoucher;
    }

    public void setGeneralVoucher(final CVoucherHeader generalVoucher) {
        this.generalVoucher = generalVoucher;
    }

    public CVoucherHeader getReceiptVoucher() {
        return receiptVoucher;
    }

    public void setReceiptVoucher(final CVoucherHeader receiptVoucher) {
        this.receiptVoucher = receiptVoucher;
    }

    public BigDecimal getGrantAmount() {
        return grantAmount;
    }

    public void setGrantAmount(final BigDecimal grantAmount) {
        this.grantAmount = grantAmount;
    }

    public BigDecimal getAccrualAmount() {
        return accrualAmount;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder(64);
        sb.append("(Id=").append(id)
        .append(",proceedingsNo=").append(proceedingsNo)
        .append(",proceedingsDate").append(proceedingsDate)
        .append(",GrantType=").append(grantType)
        .append(",grantAmount=").append(grantAmount).append(")");
        return sb.toString();
    }

    public CFinancialYear getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(final CFinancialYear financialYear) {
        this.financialYear = financialYear;
    }

    public InstrumentHeader getIhID() {
        return ihID;
    }

    public void setIhID(final InstrumentHeader ihID) {
        this.ihID = ihID;
    }

    public String getCommTaxOfficer() {
        return commTaxOfficer;
    }

    public void setCommTaxOfficer(final String commTaxOfficer) {
        this.commTaxOfficer = commTaxOfficer;
    }

}
