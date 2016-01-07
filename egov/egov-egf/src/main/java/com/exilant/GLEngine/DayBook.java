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
package com.exilant.GLEngine;

public class DayBook
{
    // This is day book
    // private DecimalFormat moneyFormat = new DecimalFormat("#,###,###.00");
    private String voucherdate;
    private String voucher;
    private String type;
    private String narration;
    private String status;
    private String glcode;
    private String particulars;
    private String debitamount;
    private String creditamount;
    private String cgn;
    private String vhId;

    /**
     *
     */
    public DayBook() {

    }

    /**
     * @return Returns the cgn.
     */
    public String getCgn() {
        return cgn;
    }

    /**
     * @param cgn The cgn to set.
     */
    public void setCgn(final String cgn) {
        this.cgn = cgn;
    }

    /**
     * @return Returns the creditamount.
     */
    public String getCreditamount() {
        return creditamount;
    }

    /**
     * @param creditamount The creditamount to set.
     */
    public void setCreditamount(final String creditamount) {
        this.creditamount = creditamount;
    }

    /**
     * @return Returns the debitamount.
     */
    public String getDebitamount() {
        return debitamount;
    }

    /**
     * @param debitamount The debitamount to set.
     */
    public void setDebitamount(final String debitamount) {
        this.debitamount = debitamount;
    }

    /**
     * @return Returns the glcode.
     */
    public String getGlcode() {
        return glcode;
    }

    /**
     * @param glcode The glcode to set.
     */
    public void setGlcode(final String glcode) {
        this.glcode = glcode;
    }

    /**
     * @return Returns the narration.
     */
    public String getNarration() {
        return narration;
    }

    /**
     * @param narration The narration to set.
     */
    public void setNarration(final String narration) {
        this.narration = narration;
    }

    /**
     * @return Returns the particulars.
     */
    public String getParticulars() {
        return particulars;
    }

    /**
     * @param particulars The particulars to set.
     */
    public void setParticulars(final String particulars) {
        this.particulars = particulars;
    }

    /**
     * @return Returns the status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status to set.
     */
    public void setStatus(final String status) {
        this.status = status;
    }

    /**
     * @return Returns the type.
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type to set.
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * @return Returns the voucherdate.
     */
    public String getVoucherdate() {
        return voucherdate;
    }

    /**
     * @param voucherdate The voucherdate to set.
     */
    public void setVoucherdate(final String voucherdate) {
        this.voucherdate = voucherdate;
    }

    /**
     * @return Returns the vouchernumber.
     */
    public String getVoucher() {
        return voucher;
    }

    /**
     * @param vouchernumber The vouchernumber to set.
     */
    public void setVoucher(final String vouchernumber) {
        voucher = vouchernumber;
    }

    public String getVhId() {
        return vhId;
    }

    public void setVhId(final String vhId) {
        this.vhId = vhId;
    }

}
