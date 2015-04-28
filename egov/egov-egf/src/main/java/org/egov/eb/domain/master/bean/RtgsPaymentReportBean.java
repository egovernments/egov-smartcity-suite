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
package org.egov.eb.domain.master.bean;

import java.math.BigDecimal;
import java.util.Date;

public class RtgsPaymentReportBean {
	
	private String rtgsDate;
	private String rtgsNumber;
	private String region;
	private BigDecimal paymentAmount;
	private String month;
	private BigDecimal IOBPaidAmount;
	private BigDecimal unPaidAmount;
	private Integer numOfBillsUnpaid;
	private String finYearRange;
	
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getRtgsDate() {
		return rtgsDate;
	}
	public void setRtgsDate(String rtgsDate) {
		this.rtgsDate = rtgsDate;
	}
	public String getRtgsNumber() {
		return rtgsNumber;
	}
	public void setRtgsNumber(String rtgsNumber) {
		this.rtgsNumber = rtgsNumber;
	}
	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public BigDecimal getIOBPaidAmount() {
		return IOBPaidAmount;
	}
	public void setIOBPaidAmount(BigDecimal iOBPaidAmount) {
		IOBPaidAmount = iOBPaidAmount;
	}
	public BigDecimal getUnPaidAmount() {
		return unPaidAmount;
	}
	public void setUnPaidAmount(BigDecimal unPaidAmount) {
		this.unPaidAmount = unPaidAmount;
	}
	public Integer getNumOfBillsUnpaid() {
		return numOfBillsUnpaid;
	}
	public void setNumOfBillsUnpaid(Integer numOfBillsUnpaid) {
		this.numOfBillsUnpaid = numOfBillsUnpaid;
	}
	public String getFinYearRange() {
		return finYearRange;
	}
	public void setFinYearRange(String finYearRange) {
		this.finYearRange = finYearRange;
	}

}
