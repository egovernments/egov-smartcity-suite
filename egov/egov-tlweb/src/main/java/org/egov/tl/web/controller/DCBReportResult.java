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

package org.egov.tl.web.controller;

import java.math.BigInteger;

public class DCBReportResult {

    private String mode;
    private String reportType;
    private String licensenumber;
    private Integer licenseid;
    private String username;
    private BigInteger curr_demand = BigInteger.ZERO;
    private BigInteger arr_demand = BigInteger.ZERO;
    private BigInteger curr_coll = BigInteger.ZERO;
    private BigInteger arr_coll = BigInteger.ZERO;
    private BigInteger curr_balance = BigInteger.ZERO;
    private BigInteger arr_balance = BigInteger.ZERO;

    public String getLicensenumber() {
        return licensenumber;
    }

    public void setLicensenumber(final String licensenumber) {
        this.licensenumber = licensenumber;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(final String reportType) {
        this.reportType = reportType;
    }

    public void setId(final Integer id) {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public BigInteger getCurr_demand() {
        return curr_demand;
    }

    public void setCurr_demand(final BigInteger curr_demand) {
        this.curr_demand = curr_demand;
    }

    public BigInteger getArr_demand() {
        return arr_demand;
    }

    public void setArr_demand(final BigInteger arr_demand) {
        this.arr_demand = arr_demand;
    }

    public BigInteger getTotal_demand() {
        return (curr_demand == null ? BigInteger.ZERO : curr_demand).add(arr_demand == null ? BigInteger.ZERO
                : arr_demand);
    }

    public void setTotal_demand(final BigInteger total_demand) {
    }

    public BigInteger getCurr_coll() {
        return curr_coll;
    }

    public void setCurr_coll(final BigInteger curr_coll) {
        this.curr_coll = curr_coll;
    }

    public BigInteger getArr_coll() {
        return arr_coll;
    }

    public void setArr_coll(final BigInteger arr_coll) {
        this.arr_coll = arr_coll;
    }

    public BigInteger getTotal_coll() {
        return (curr_coll == null ? BigInteger.ZERO : curr_coll).add(arr_coll == null ? BigInteger.ZERO : arr_coll);
    }

    public void setTotal_coll(final BigInteger total_coll) {
    }

    public BigInteger getCurr_balance() {
        return (curr_demand == null ? BigInteger.ZERO : curr_demand).subtract(curr_coll == null ? BigInteger.ZERO
                : curr_coll);
    }

    public void setCurr_balance(final BigInteger curr_balance) {
        this.curr_balance = curr_balance;
    }

    public BigInteger getArr_balance() {
        return (arr_demand == null ? BigInteger.ZERO : arr_demand).subtract(arr_coll == null ? BigInteger.ZERO
                : arr_coll);
    }

    public void setArr_balance(final BigInteger arr_balance) {
        this.arr_balance = arr_balance;
    }

    public BigInteger getTotal_balance() {
        return (curr_balance == null ? BigInteger.ZERO : curr_balance).add(arr_balance == null ? BigInteger.ZERO
                : arr_balance);
    }

    public void setTotal_balance(final BigInteger total_balance) {
    }

    public Integer getLicenseid() {
        return licenseid;
    }

    public void setLicenseid(final Integer licenseid) {
        this.licenseid = licenseid;
    }

}