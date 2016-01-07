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
/*
 * Created on Apr 24,2008
 * @author Iliyaraja S
 */
package com.exilant.eGov.src.reports;

public class AdvanceRegisterBean
{

    private String startDate = "";
    private String endDate = "";
    private String partytype_id = "";
    private String accEntityList = "";
    private String entityName = "";
    private String accEntityKey = "";

    private String sltype = "";
    private String slid = "";
    private String remarks;

    private String vdate = "";
    private String slname = "";
    private String payordernumberdate = "";
    private String bankpayvnumberdate = "";
    private String particulars = "";
    private String payamount = "";
    private String recovnumberdate = "";
    private String recovamount = "";
    private String closingBal = "";

    private String serialNo = "";

    public String getBankpayvnumberdate() {
        return bankpayvnumberdate;
    }

    public void setBankpayvnumberdate(final String bankpayvnumberdate) {
        this.bankpayvnumberdate = bankpayvnumberdate;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(final String particulars) {
        this.particulars = particulars;
    }

    public String getPayamount() {
        return payamount;
    }

    public void setPayamount(final String payamount) {
        this.payamount = payamount;
    }

    public String getPayordernumberdate() {
        return payordernumberdate;
    }

    public void setPayordernumberdate(final String payordernumberdate) {
        this.payordernumberdate = payordernumberdate;
    }

    public String getRecovamount() {
        return recovamount;
    }

    public void setRecovamount(final String recovamount) {
        this.recovamount = recovamount;
    }

    public String getRecovnumberdate() {
        return recovnumberdate;
    }

    public void setRecovnumberdate(final String recovnumberdate) {
        this.recovnumberdate = recovnumberdate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(final String serialNo) {
        this.serialNo = serialNo;
    }

    public String getSlname() {
        return slname;
    }

    public void setSlname(final String slname) {
        this.slname = slname;
    }

    public String getVdate() {
        return vdate;
    }

    public void setVdate(final String vdate) {
        this.vdate = vdate;
    }

    public String getAccEntityKey() {
        return accEntityKey;
    }

    public void setAccEntityKey(final String accEntityKey) {
        this.accEntityKey = accEntityKey;
    }

    public String getAccEntityList() {
        return accEntityList;
    }

    public void setAccEntityList(final String accEntityList) {
        this.accEntityList = accEntityList;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(final String endDate) {
        this.endDate = endDate;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(final String entityName) {
        this.entityName = entityName;
    }

    public String getPartytype_id() {
        return partytype_id;
    }

    public void setPartytype_id(final String partytype_id) {
        this.partytype_id = partytype_id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(final String startDate) {
        this.startDate = startDate;
    }

    public String getSlid() {
        return slid;
    }

    public void setSlid(final String slid) {
        this.slid = slid;
    }

    public String getSltype() {
        return sltype;
    }

    public void setSltype(final String sltype) {
        this.sltype = sltype;
    }

    public String getClosingBal() {
        return closingBal;
    }

    public void setClosingBal(final String closingBal) {
        this.closingBal = closingBal;
    }
}
