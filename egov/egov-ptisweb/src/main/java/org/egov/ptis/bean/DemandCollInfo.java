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
package org.egov.ptis.bean;

import java.math.BigDecimal;

public class DemandCollInfo {

	private Integer orderNo;
	private String taxType;
	private BigDecimal arrDemand;
	private BigDecimal curDemand;
	private BigDecimal arrColl;
	private BigDecimal curColl;
	
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	public String getTaxType() {
		return taxType;
	}
	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}
	public BigDecimal getArrDemand() {
		return arrDemand;
	}
	public void setArrDemand(BigDecimal arrDemand) {
		this.arrDemand = arrDemand;
	}
	public BigDecimal getCurDemand() {
		return curDemand;
	}
	public void setCurDemand(BigDecimal curDemand) {
		this.curDemand = curDemand;
	}
	public BigDecimal getArrColl() {
		return arrColl;
	}
	public void setArrColl(BigDecimal arrColl) {
		this.arrColl = arrColl;
	}
	public BigDecimal getCurColl() {
		return curColl;
	}
	public void setCurColl(BigDecimal curColl) {
		this.curColl = curColl;
	}
	
	
}
