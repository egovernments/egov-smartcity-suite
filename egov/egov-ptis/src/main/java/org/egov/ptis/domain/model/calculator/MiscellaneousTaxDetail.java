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
package org.egov.ptis.domain.model.calculator;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.math.BigDecimal;
import java.util.Date;

@XStreamAlias("taxdetail")
public abstract class MiscellaneousTaxDetail {
    // %value or amount value
    private BigDecimal taxValue;
    private BigDecimal actualTaxValue;
    private BigDecimal calculatedTaxValue;
    private Integer noOfDays;
    private BigDecimal historyALV;

    @XStreamAsAttribute
    private Date fromDate;

    @XStreamAsAttribute
    private Character isHistory = 'N';


    public MiscellaneousTaxDetail() {}

    public MiscellaneousTaxDetail(MiscellaneousTaxDetail taxDetail) {
    	this.taxValue = taxDetail.getTaxValue();
    	this.actualTaxValue = taxDetail.getActualTaxValue();
    	this.calculatedTaxValue = taxDetail.getCalculatedTaxValue();
    	this.fromDate = taxDetail.getFromDate();
    	this.noOfDays = taxDetail.getNoOfDays();
    	this.isHistory = taxDetail.getIsHistory();
    	this.historyALV = taxDetail.getHistoryALV();
    }

	public BigDecimal getTaxValue() {
		return taxValue;
	}

	public void setTaxValue(BigDecimal taxValue) {
		this.taxValue = taxValue;
	}

	public BigDecimal getActualTaxValue() {
		return actualTaxValue;
	}

	public void setActualTaxValue(BigDecimal actualTaxValue) {
		this.actualTaxValue = actualTaxValue;
	}

	public BigDecimal getCalculatedTaxValue() {
		return calculatedTaxValue;
	}

	public void setCalculatedTaxValue(BigDecimal calculatedTaxValue) {
		this.calculatedTaxValue = calculatedTaxValue;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Integer getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(Integer noOfDays) {
		this.noOfDays = noOfDays;
	}

	public Character getIsHistory() {
		return isHistory;
	}

	public void setIsHistory(Character isHistory) {
		this.isHistory = isHistory;
	}

	public BigDecimal getHistoryALV() {
		return historyALV;
	}

	public void setHistoryALV(BigDecimal historyALV) {
		this.historyALV = historyALV;
	}

	@Override
	public String toString() {
		return new StringBuilder().append("MiscellaneousTaxDetail [")
				.append("taxValue=").append(getTaxValue())
				.append(", actualTaxValue=").append(getActualTaxValue())
				.append(", calculatedTaxValue=").append(getCalculatedTaxValue())
				.append(", fromDate=").append(getFromDate())
				.append(", noOfDays=").append(getNoOfDays())
				.append(", isHistory=").append(getIsHistory())
				.append(", historyALV=").append(getHistoryALV())
				.append("]").toString();
	}

}
