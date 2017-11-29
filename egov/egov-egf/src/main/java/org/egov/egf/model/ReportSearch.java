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

import org.egov.commons.Bank;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;

import java.util.Date;
import java.util.List;

public class ReportSearch {
    private Department department;
    private Fund fund;
    private CFunction function;
    private Functionary functionary;
    private Fundsource fundsource;
    private Boundary field;
    private Scheme scheme;
    private SubScheme subScheme;
    private String startDate;
    private String endDate;
    private String incExp;
    private Integer minorCodeLen;
    private Integer majorCodeLen;
    public Long finYearId;
    public Long previousFinYearId;
    private boolean byDepartment;
    private Date asOnDate;
    private Date previousYearDate;
    private String exportType;
    private Date yearStartDate;
    private Date previousYearStartDate;
    private String scheduleName;
    private Long FIEscheduleId;
    private boolean byDetailCode;
    private String glcode;
    private Bankaccount bankAccount;
    private Bankbranch bankbranch;
    private Bank bank;

    public String getExportType() {
        return exportType;
    }

    public void setExportType(final String exportType) {
        this.exportType = exportType;
    }

    public Date getPreviousYearDate() {
        return previousYearDate;
    }

    public void setPreviousYearDate(final Date previousYearDate) {
        this.previousYearDate = previousYearDate;
    }

    public Long getPreviousFinYearId() {
        return previousFinYearId;
    }

    public void setPreviousFinYearId(final Long previousFinYearId) {
        this.previousFinYearId = previousFinYearId;
    }

    public Date getPreviousYearStartDate() {
        return previousYearStartDate;
    }

    public void setPreviousYearStartDate(final Date previousYearStartDate) {
        this.previousYearStartDate = previousYearStartDate;
    }

    public String getGlcode() {
        return glcode;
    }

    public void setGlcode(final String glcode) {
        this.glcode = glcode;
    }

    public void setByDetailCode(final boolean byDetailCode) {
        this.byDetailCode = byDetailCode;
    }

    public boolean getByDetailCode() {
        return byDetailCode;
    }

    private List<Department> deptList;

    public Long getFIEscheduleId() {
        return FIEscheduleId;
    }

    public void setFIEscheduleId(final Long escheduleId) {
        FIEscheduleId = escheduleId;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(final String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public void setAsOnDate(final Date asOnDate) {
        this.asOnDate = asOnDate;
    }

    public void setByDepartment(final boolean byDepartment) {
        this.byDepartment = byDepartment;
    }

    public Integer getMinorCodeLen() {
        return minorCodeLen;
    }

    public void setMinorCodeLen(final Integer minorCodeLen) {
        this.minorCodeLen = minorCodeLen;
    }

    public Integer getMajorCodeLen() {
        return majorCodeLen;
    }

    public void setMajorCodeLen(final Integer majorCodeLen) {
        this.majorCodeLen = majorCodeLen;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(final Department department) {
        this.department = department;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(final Fund fund) {
        this.fund = fund;
    }

    public CFunction getFunction() {
        return function;
    }

    public void setFunction(final CFunction function) {
        this.function = function;
    }

    public Functionary getFunctionary() {
        return functionary;
    }

    public void setFunctionary(final Functionary functionary) {
        this.functionary = functionary;
    }

    public Fundsource getFundsource() {
        return fundsource;
    }

    public void setFundsource(final Fundsource fundsource) {
        this.fundsource = fundsource;
    }

    public Boundary getField() {
        return field;
    }

    public void setField(final Boundary field) {
        this.field = field;
    }

    public Scheme getScheme() {
        return scheme;
    }

    public void setScheme(final Scheme scheme) {
        this.scheme = scheme;
    }

    public SubScheme getSubScheme() {
        return subScheme;
    }

    public void setSubScheme(final SubScheme subScheme) {
        this.subScheme = subScheme;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(final String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(final String endDate) {
        this.endDate = endDate;
    }

    public String getIncExp() {
        return incExp;
    }

    public void setIncExp(final String incExp) {
        this.incExp = incExp;
    }

    public boolean getByDepartment() {

        return byDepartment;
    }

    public Date getAsOnDate() {
        return asOnDate;
    }

    public Long getFinYearId() {
        return finYearId;
    }

    public void setFinYearId(final Long finYearId) {
        this.finYearId = finYearId;
    }

    public Date getYearStartDate() {
        return yearStartDate;
    }

    public void setYearStartDate(final Date yearStartDate) {
        this.yearStartDate = yearStartDate;
    }

    public void setDeptList(final List<Department> deptList) {
        this.deptList = deptList;
    }

    public List<Department> getDeptList() {
        return deptList;
    }

    public void getByDetailCode(final boolean b) {

    }

    public Bankaccount getBankAccount() {
        return bankAccount;
    }

    public Bankbranch getBankbranch() {
        return bankbranch;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBankAccount(final Bankaccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public void setBankbranch(final Bankbranch bankbranch) {
        this.bankbranch = bankbranch;
    }

    public void setBank(final Bank bank) {
        this.bank = bank;
    }

}
