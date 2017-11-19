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
package org.egov.ptis.bean;

import java.math.BigDecimal;
import java.util.List;

/**
 * 
 * @author subhash
 *
 */

public class CollectionInfo {

	private String operator;
	private BigDecimal collByCash;
	private BigDecimal collByCheque;
	private BigDecimal otherColl;
	private BigDecimal eduEgsCurrColl;
	private BigDecimal eduEgsArrColl;
	private BigDecimal totalCurrColl;
	private BigDecimal totalArrColl;
	private BigDecimal grandTotal;
	private List<ReceiptInfo> rcptInfoList;
	public BigDecimal getCollByCash() {
		return collByCash;
	}
	public void setCollByCash(BigDecimal collByCash) {
		this.collByCash = collByCash;
	}
	public BigDecimal getCollByCheque() {
		return collByCheque;
	}
	public void setCollByCheque(BigDecimal collByCheque) {
		this.collByCheque = collByCheque;
	}
	public BigDecimal getOtherColl() {
		return otherColl;
	}
	public void setOtherColl(BigDecimal otherColl) {
		this.otherColl = otherColl;
	}
	public BigDecimal getEduEgsCurrColl() {
		return eduEgsCurrColl;
	}
	public void setEduEgsCurrColl(BigDecimal eduEgsCurrColl) {
		this.eduEgsCurrColl = eduEgsCurrColl;
	}
	public BigDecimal getEduEgsArrColl() {
		return eduEgsArrColl;
	}
	public void setEduEgsArrColl(BigDecimal eduEgsArrColl) {
		this.eduEgsArrColl = eduEgsArrColl;
	}
	public BigDecimal getTotalCurrColl() {
		return totalCurrColl;
	}
	public void setTotalCurrColl(BigDecimal totalCurrColl) {
		this.totalCurrColl = totalCurrColl;
	}
	public BigDecimal getTotalArrColl() {
		return totalArrColl;
	}
	public void setTotalArrColl(BigDecimal totalArrColl) {
		this.totalArrColl = totalArrColl;
	}
	public BigDecimal getGrandTotal() {
		return grandTotal;
	}
	public void setGrandTotal(BigDecimal grandTotal) {
		this.grandTotal = grandTotal;
	}
	public List<ReceiptInfo> getRcptInfoList() {
		return rcptInfoList;
	}
	public void setRcptInfoList(List<ReceiptInfo> rcptInfoList) {
		this.rcptInfoList = rcptInfoList;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
}
