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

package org.egov.stms.masters.pojo;

import org.egov.infra.admin.master.entity.Boundary;

import java.math.BigDecimal;
import java.util.List;

public class DCBReportWardwiseResult {
    private String searchText;
    private List<Boundary> wards;
    private Long wardId;
    private String propertyType;
    private String mode;

    private String ulbName;
    private String revenueWard;
    private String status;
    private String installmentYearDescription;
    private int noofassessments;
    private String shscnumber;
    private String applicationNumber;
    private String ownerName;

    private BigDecimal advanceAmount = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    private BigDecimal curr_demand = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    private BigDecimal arr_demand = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    private BigDecimal curr_collection = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    private BigDecimal arr_collection = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    private BigDecimal curr_balance = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    private BigDecimal arr_balance = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    private BigDecimal total_collection = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
    private BigDecimal total_demand = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
    private BigDecimal total_balance = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);

    public List<Boundary> getWards() {
        return wards;
    }

    public void setWards(final List<Boundary> wards) {
        this.wards = wards;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(final String propertyType) {
        this.propertyType = propertyType;
    }

    public BigDecimal getCurr_demand() {
        return curr_demand;
    }

    public void setCurr_demand(final BigDecimal curr_demand) {
        this.curr_demand = curr_demand;
    }

    public BigDecimal getArr_demand() {
        return arr_demand;
    }

    public void setArr_demand(final BigDecimal arr_demand) {
        this.arr_demand = arr_demand;
    }

    public BigDecimal getCurr_collection() {
        return curr_collection;
    }

    public void setCurr_collection(final BigDecimal curr_collection) {
        this.curr_collection = curr_collection;
    }

    public BigDecimal getArr_collection() {
        return arr_collection;
    }

    public void setArr_collection(final BigDecimal arr_collection) {
        this.arr_collection = arr_collection;
    }

    public BigDecimal getCurr_balance() {
        return curr_balance;
    }

    public void setCurr_balance(final BigDecimal curr_balance) {
        this.curr_balance = curr_balance;
    }

    public BigDecimal getArr_balance() {
        return arr_balance;
    }

    public void setArr_balance(final BigDecimal arr_balance) {
        this.arr_balance = arr_balance;
    }

    public String searchQuery() {
        return searchText;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(final String searchText) {
        this.searchText = searchText;
    }

    public String getUlbName() {
        return ulbName;
    }

    public void setUlbName(final String ulbName) {
        this.ulbName = ulbName;
    }

    public String getRevenueWard() {
        return revenueWard;
    }

    public void setRevenueWard(final String revenueWard) {
        this.revenueWard = revenueWard;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getInstallmentYearDescription() {
        return installmentYearDescription;
    }

    public void setInstallmentYearDescription(final String installmentYearDescription) {
        this.installmentYearDescription = installmentYearDescription;
    }

    public int getNoofassessments() {
        return noofassessments;
    }

    public void setNoofassessments(final int noofassessments) {
        this.noofassessments = noofassessments;
    }

    public BigDecimal getTotal_collection() {
        return total_collection;
    }

    public void setTotal_collection(final BigDecimal total_collection) {
        this.total_collection = total_collection;
    }

    public BigDecimal getTotal_demand() {
        return total_demand;
    }

    public void setTotal_demand(final BigDecimal total_demand) {
        this.total_demand = total_demand;
    }

    public BigDecimal getTotal_balance() {
        return total_balance;
    }

    public void setTotal_balance(final BigDecimal total_balance) {
        this.total_balance = total_balance;
    }

    public Long getWardId() {
        return wardId;
    }

    public void setWardId(final Long wardId) {
        this.wardId = wardId;
    }

    public String getShscnumber() {
        return shscnumber;
    }

    public void setShscnumber(final String shscnumber) {
        this.shscnumber = shscnumber;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(final String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(final String ownerName) {
        this.ownerName = ownerName;
    }

    public BigDecimal getAdvanceAmount() {
        return advanceAmount;
    }

    public void setAdvanceAmount(final BigDecimal advanceAmount) {
        this.advanceAmount = advanceAmount;
    }

}
