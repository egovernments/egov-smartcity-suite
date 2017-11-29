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
package org.egov.model.voucher;

import java.util.Date;

public class VoucherTypeBean {

    private String voucherName;
    private String voucherType;
    private String voucherNumType;
    private String cgnType;
    private String voucherNumFrom;
    private String voucherNumTo;
    private String voucherDateFrom;
    private String voucherSubType;
    private String totalAmount;
    /**
     * @description - added properties for the contractor,supplier,salary and fixed asset JV manual creation screen.
     *
     */

    private String partyBillNum;
    private String partyName;
    private Date partyBillDate;
    private String billNum;
    private Date billDate;

    public String getVoucherSubType() {
        return voucherSubType;
    }

    public void setVoucherSubType(final String voucherSubType) {
        this.voucherSubType = voucherSubType;
    }

    public String getPartyBillNum() {
        return partyBillNum;
    }

    public void setPartyBillNum(final String partyBillNum) {
        this.partyBillNum = partyBillNum;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(final String partyName) {
        this.partyName = partyName;
    }

    public Date getPartyBillDate() {
        return partyBillDate;
    }

    public void setPartyBillDate(final Date partyBillDate) {
        this.partyBillDate = partyBillDate;
    }

    public String getBillNum() {
        return billNum;
    }

    public void setBillNum(final String billNum) {
        this.billNum = billNum;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(final Date billDate) {
        this.billDate = billDate;
    }

    public String getVoucherDateFrom() {
        return voucherDateFrom;
    }

    public void setVoucherDateFrom(final String voucherDateFrom) {
        this.voucherDateFrom = voucherDateFrom;
    }

    public String getVoucherDateTo() {
        return voucherDateTo;
    }

    public void setVoucherDateTo(final String voucherDateTo) {
        this.voucherDateTo = voucherDateTo;
    }

    private String voucherDateTo;

    public String getVoucherNumFrom() {
        return voucherNumFrom;
    }

    public void setVoucherNumFrom(final String voucherNumFrom) {
        this.voucherNumFrom = voucherNumFrom;
    }

    public String getVoucherNumTo() {
        return voucherNumTo;
    }

    public void setVoucherNumTo(final String voucherNumTo) {
        this.voucherNumTo = voucherNumTo;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(final String voucherName) {
        this.voucherName = voucherName;
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(final String voucherType) {
        this.voucherType = voucherType;
    }

    public String getVoucherNumType() {
        return voucherNumType;
    }

    public void setVoucherNumType(final String voucherNumType) {
        this.voucherNumType = voucherNumType;
    }

    public String getCgnType() {
        return cgnType;
    }

    public void setCgnType(final String cgnType) {
        this.cgnType = cgnType;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

}
