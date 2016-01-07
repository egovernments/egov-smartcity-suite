/*******************************************************************************
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
 * 	1) All versions of this program, verbatim or modified must carry this
 * 	   Legal Notice.
 *
 * 	2) Any misrepresentation of the origin of the material is prohibited. It
 * 	   is required that all modified versions of this material be marked in
 * 	   reasonable ways as different from the original version.
 *
 * 	3) This license does not grant any rights to any user of the program
 * 	   with regards to rights under trademark law for use of the trade names
 * 	   or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
/**
 *
 */
package org.egov.model.contra;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;

/**
 * @author msahoo
 *
 */
public class TransactionSummary {
    private Integer id;
    private Accountdetailtype accountdetailtype;
    private CFinancialYear financialyear;
    private Fundsource fundsource;
    private Fund fund;
    private CChartOfAccounts glcodeid;
    private BigDecimal openingdebitbalance;
    private BigDecimal openingcreditbalance;
    private BigDecimal debitamount;
    private BigDecimal creditamount;
    private Integer accountdetailkey;
    private String narration;
    private User modifiedBy;
    private Date modifiedDate;
    private Department departmentid;
    private Functionary functionaryid;
    private CFunction functionid;
    private Integer divisionid;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Accountdetailtype getAccountdetailtype() {
        return accountdetailtype;
    }

    public void setAccountdetailtype(final Accountdetailtype accountdetailtype) {
        this.accountdetailtype = accountdetailtype;
    }

    public CFinancialYear getFinancialyear() {
        return financialyear;
    }

    public void setFinancialyear(final CFinancialYear financialyear) {
        this.financialyear = financialyear;
    }

    public Fundsource getFundsource() {
        return fundsource;
    }

    public void setFundsource(final Fundsource fundsource) {
        this.fundsource = fundsource;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(final Fund fund) {
        this.fund = fund;
    }

    public CChartOfAccounts getGlcodeid() {
        return glcodeid;
    }

    public void setGlcodeid(final CChartOfAccounts glcodeid) {
        this.glcodeid = glcodeid;
    }

    public BigDecimal getOpeningdebitbalance() {
        return openingdebitbalance;
    }

    public void setOpeningdebitbalance(final BigDecimal openingdebitbalance) {
        this.openingdebitbalance = openingdebitbalance;
    }

    public BigDecimal getOpeningcreditbalance() {
        return openingcreditbalance;
    }

    public void setOpeningcreditbalance(final BigDecimal openingcreditbalance) {
        this.openingcreditbalance = openingcreditbalance;
    }

    public BigDecimal getDebitamount() {
        return debitamount;
    }

    public void setDebitamount(final BigDecimal debitamount) {
        this.debitamount = debitamount;
    }

    public BigDecimal getCreditamount() {
        return creditamount;
    }

    public void setCreditamount(final BigDecimal creditamount) {
        this.creditamount = creditamount;
    }

    public Integer getAccountdetailkey() {
        return accountdetailkey;
    }

    public void setAccountdetailkey(final Integer accountdetailkey) {
        this.accountdetailkey = accountdetailkey;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(final String narration) {
        this.narration = narration;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(final User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(final Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Functionary getFunctionaryid() {
        return functionaryid;
    }

    public void setFunctionaryid(final Functionary functionaryid) {
        this.functionaryid = functionaryid;
    }

    public CFunction getFunctionid() {
        return functionid;
    }

    public void setFunctionid(final CFunction functionid) {
        this.functionid = functionid;
    }

    public Integer getDivisionid() {
        return divisionid;
    }

    public void setDivisionid(final Integer divisionid) {
        this.divisionid = divisionid;
    }

    public Department getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(final Department departmentid) {
        this.departmentid = departmentid;
    }
}
