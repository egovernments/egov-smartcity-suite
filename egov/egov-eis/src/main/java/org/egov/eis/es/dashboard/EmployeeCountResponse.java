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

package org.egov.eis.es.dashboard;

import org.egov.infra.utils.StringUtils;

public class EmployeeCountResponse {

    private String region = StringUtils.EMPTY;
    private String district = StringUtils.EMPTY;
    private String grade = StringUtils.EMPTY;
    private String ulbCode = StringUtils.EMPTY;
    private String ulbName = StringUtils.EMPTY;
    private String department = StringUtils.EMPTY;
    private Long totalMale;
    private Long totalFemale;
    private Long totalEmployee;
    private Long totalRegularMale;
    private Long totalRegularFemale;
    private Long totalRegularEmployee;
    private Long totalContractMale = 0L;
    private Long totalContractFemale = 0L;
    private Long totalContractEmployee = 0L;
    private Long totalSanctioned = 0L;
    private Long totalWorking = 0L;
    private Long totalVacant = 0L;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getUlbName() {
        return ulbName;
    }

    public void setUlbName(String ulbName) {
        this.ulbName = ulbName;
    }

    public String getUlbCode() {
        return ulbCode;
    }

    public void setUlbCode(String ulbCode) {
        this.ulbCode = ulbCode;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Long getTotalMale() {
        return totalMale;
    }

    public void setTotalMale(Long totalMale) {
        this.totalMale = totalMale;
    }

    public Long getTotalFemale() {
        return totalFemale;
    }

    public void setTotalFemale(Long totalFemale) {
        this.totalFemale = totalFemale;
    }

    public Long getTotalEmployee() {
        return totalEmployee;
    }

    public void setTotalEmployee(Long totalEmployee) {
        this.totalEmployee = totalEmployee;
    }

    public Long getTotalRegularMale() {
        return totalRegularMale;
    }

    public void setTotalRegularMale(Long totalRegularMale) {
        this.totalRegularMale = totalRegularMale;
    }

    public Long getTotalRegularFemale() {
        return totalRegularFemale;
    }

    public void setTotalRegularFemale(Long totalRegularFemale) {
        this.totalRegularFemale = totalRegularFemale;
    }

    public Long getTotalRegularEmployee() {
        return totalRegularEmployee;
    }

    public void setTotalRegularEmployee(Long totalRegularEmployee) {
        this.totalRegularEmployee = totalRegularEmployee;
    }

    public Long getTotalContractMale() {
        return totalContractMale;
    }

    public void setTotalContractMale(Long totalContractMale) {
        this.totalContractMale = totalContractMale;
    }

    public Long getTotalContractFemale() {
        return totalContractFemale;
    }

    public void setTotalContractFemale(Long totalContractFemale) {
        this.totalContractFemale = totalContractFemale;
    }

    public Long getTotalContractEmployee() {
        return totalContractEmployee;
    }

    public void setTotalContractEmployee(Long totalContractEmployee) {
        this.totalContractEmployee = totalContractEmployee;
    }

    public Long getTotalSanctioned() {
        return totalSanctioned;
    }

    public void setTotalSanctioned(Long totalSanctioned) {
        this.totalSanctioned = totalSanctioned;
    }

    public Long getTotalWorking() {
        return totalWorking;
    }

    public void setTotalWorking(Long totalWorking) {
        this.totalWorking = totalWorking;
    }

    public Long getTotalVacant() {
        return totalVacant;
    }

    public void setTotalVacant(Long totalVacant) {
        this.totalVacant = totalVacant;
    }

}
