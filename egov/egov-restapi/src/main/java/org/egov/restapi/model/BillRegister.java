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
package org.egov.restapi.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class BillRegister implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1433064701524657608L;
    private String projectCode;
    private Date billDate;
    private String billType;
    private String nameOfWork;
    private String payTo;
    private BigDecimal billAmount;
    private String narration;
    private String partyBillNumber;
    private Date partyBillDate;
    private String departmentCode;
    private String functionCode;
    private String schemeCode;
    private String subSchemeCode;
    private String fundCode;
    private List<BillDetails> billDetails;
    private List<BillPayeeDetails> billPayeeDetails;

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(final String projectCode) {
        this.projectCode = projectCode;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(final Date billDate) {
        this.billDate = billDate;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(final String billType) {
        this.billType = billType;
    }

    public String getNameOfWork() {
        return nameOfWork;
    }

    public void setNameOfWork(final String nameOfWork) {
        this.nameOfWork = nameOfWork;
    }

    public String getPayTo() {
        return payTo;
    }

    public void setPayTo(final String payTo) {
        this.payTo = payTo;
    }

    public BigDecimal getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(final BigDecimal billAmount) {
        this.billAmount = billAmount;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(final String narration) {
        this.narration = narration;
    }

    public String getPartyBillNumber() {
        return partyBillNumber;
    }

    public void setPartyBillNumber(final String partyBillNumber) {
        this.partyBillNumber = partyBillNumber;
    }

    public Date getPartyBillDate() {
        return partyBillDate;
    }

    public void setPartyBillDate(final Date partyBillDate) {
        this.partyBillDate = partyBillDate;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(final String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(final String functionCode) {
        this.functionCode = functionCode;
    }

    public String getSchemeCode() {
        return schemeCode;
    }

    public void setSchemeCode(final String schemeCode) {
        this.schemeCode = schemeCode;
    }

    public String getSubSchemeCode() {
        return subSchemeCode;
    }

    public void setSubSchemeCode(final String subSchemeCode) {
        this.subSchemeCode = subSchemeCode;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(final String fundCode) {
        this.fundCode = fundCode;
    }

    public List<BillDetails> getBillDetails() {
        return billDetails;
    }

    public void setBillDetails(final List<BillDetails> billDetails) {
        this.billDetails = billDetails;
    }

    public List<BillPayeeDetails> getBillPayeeDetails() {
        return billPayeeDetails;
    }

    public void setBillPayeeDetails(final List<BillPayeeDetails> billPayeeDetails) {
        this.billPayeeDetails = billPayeeDetails;
    }
}
