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
package com.exilant.eGov.src.domain;


/**
* @author Iliyaraja
* Created on Aug 03, 2007
* TODO To change the template for this generated type comment go to
* Window - Preferences - Java - Code Style - Code Templates
*/

public class CBillDisplayBean
{
//	 for search param
	private String billDateFrom;
	private String billDateTo;
	private String fundSearch_id=null;
	private String section_id=null;
	private String billvoucherNumber="";
	private String inputFieldSearch;
	// for search results
	private String contBillNumberP;
	private String contBill_idP;//hidden
	private String param_CBillNoP;//hidden
	private String billDate1P;
	private String voucherHeader_voucherNumber1P;
	private String voucherHeaderIdP;//hidden
	private String sectionNameP;
	private String fundNameSearchP;
	private String fundSearchIdP;//hidden
	private String sectionSearchIdP;//hidden
	private String contBillDetail_netPayP;

	public String getBillDate1P() {
		return billDate1P;
	}
	public void setBillDate1P(String billDate1P) {
		this.billDate1P = billDate1P;
	}
public String getBillDateFrom() {
	return billDateFrom;
}
public void setBillDateFrom(String billDateFrom) {
	this.billDateFrom = billDateFrom;
}
	public String getBillDateTo() {
		return billDateTo;
	}
	public void setBillDateTo(String billDateTo) {
		this.billDateTo = billDateTo;
	}
	public String getBillvoucherNumber() {
		return billvoucherNumber;
	}
	public void setBillvoucherNumber(String billvoucherNumber) {
		this.billvoucherNumber = billvoucherNumber;
	}
	public String getContBill_idP() {
		return contBill_idP;
	}
	public void setContBill_idP(String contBill_idP) {
		this.contBill_idP = contBill_idP;
	}
	public String getContBillDetail_netPayP() {
		return contBillDetail_netPayP;
	}
	public void setContBillDetail_netPayP(String contBillDetail_netPayP) {
		this.contBillDetail_netPayP = contBillDetail_netPayP;
	}
	public String getContBillNumberP() {
		return contBillNumberP;
	}
	public void setContBillNumberP(String contBillNumberP) {
		this.contBillNumberP = contBillNumberP;
	}
	public String getFundNameSearchP() {
		return fundNameSearchP;
	}
	public void setFundNameSearchP(String fundNameSearchP) {
		this.fundNameSearchP = fundNameSearchP;
	}
	public String getFundSearch_id() {
		return fundSearch_id;
	}
	public void setFundSearch_id(String fundSearch_id) {
		this.fundSearch_id = fundSearch_id;
	}
	public String getFundSearchIdP() {
		return fundSearchIdP;
	}
	public void setFundSearchIdP(String fundSearchIdP) {
		this.fundSearchIdP = fundSearchIdP;
	}
	public String getParam_CBillNoP() {
		return param_CBillNoP;
	}
	public void setParam_CBillNoP(String param_CBillNoP) {
		this.param_CBillNoP = param_CBillNoP;
	}
	public String getSection_id() {
		return section_id;
	}
	public void setSection_id(String section_id) {
		this.section_id = section_id;
	}
	public String getSectionNameP() {
		return sectionNameP;
	}
	public void setSectionNameP(String sectionNameP) {
		this.sectionNameP = sectionNameP;
	}
	public String getSectionSearchIdP() {
		return sectionSearchIdP;
	}
	public void setSectionSearchIdP(String sectionSearchIdP) {
		this.sectionSearchIdP = sectionSearchIdP;
	}
	public String getVoucherHeader_voucherNumber1P() {
		return voucherHeader_voucherNumber1P;
	}
	public void setVoucherHeader_voucherNumber1P(
			String voucherHeader_voucherNumber1P) {
		this.voucherHeader_voucherNumber1P = voucherHeader_voucherNumber1P;
	}
	public String getVoucherHeaderIdP() {
		return voucherHeaderIdP;
	}
	public void setVoucherHeaderIdP(String voucherHeaderIdP) {
		this.voucherHeaderIdP = voucherHeaderIdP;
	}
	public String getInputFieldSearch() {
		return inputFieldSearch;
	}
	public void setInputFieldSearch(String inputFieldSearch) {
		this.inputFieldSearch = inputFieldSearch;
	}
}
