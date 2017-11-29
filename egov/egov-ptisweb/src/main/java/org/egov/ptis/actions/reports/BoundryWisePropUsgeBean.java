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
package org.egov.ptis.actions.reports;

import java.math.BigDecimal;

public class BoundryWisePropUsgeBean {

	private String zoneId;
	private Integer wardId;
	private Integer propUsgeId;
	private BigDecimal arrDmd;
	private BigDecimal currDmd;
	private BigDecimal totalDemand;
	private Integer aggProps;
	private BigDecimal aggCurrDemand;
	private BigDecimal aggArrDemand;
	private BigDecimal aggTotDemand;
	private Integer propCount;

	private BigDecimal indCurrDemand = BigDecimal.ZERO;
	private BigDecimal indArrDemand = BigDecimal.ZERO;
	private BigDecimal indTotDemand = BigDecimal.ZERO;
	private BigDecimal indAvAmt = BigDecimal.ZERO;

	public BigDecimal getIndAvAmt() {
		return indAvAmt;
	}

	public void setIndAvAmt(BigDecimal indAvAmt) {
		this.indAvAmt = indAvAmt;
	}

	private Integer indNoOfPropCount = 0;

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public Integer getPropUsgeId() {
		return propUsgeId;
	}

	public void setPropUsgeId(Integer propUsgeId) {
		this.propUsgeId = propUsgeId;
	}

	public BigDecimal getArrDmd() {
		return arrDmd;
	}

	public void setArrDmd(BigDecimal arrDmd) {
		this.arrDmd = arrDmd;
	}

	public BigDecimal getCurrDmd() {
		return currDmd;
	}

	public void setCurrDmd(BigDecimal currDmd) {
		this.currDmd = currDmd;
	}

	public Integer getPropCount() {
		return propCount;
	}

	public void setPropCount(Integer propCount) {
		this.propCount = propCount;
	}

	public BigDecimal getTotalDemand() {
		return totalDemand;
	}

	public void setTotalDemand(BigDecimal totalDemand) {
		this.totalDemand = totalDemand;
	}

	public Integer getAggProps() {
		return aggProps;
	}

	public void setAggProps(Integer aggProps) {
		this.aggProps = aggProps;
	}

	public BigDecimal getAggCurrDemand() {
		return aggCurrDemand;
	}

	public void setAggCurrDemand(BigDecimal aggCurrDemand) {
		this.aggCurrDemand = aggCurrDemand;
	}

	public BigDecimal getAggArrDemand() {
		return aggArrDemand;
	}

	public void setAggArrDemand(BigDecimal aggArrDemand) {
		this.aggArrDemand = aggArrDemand;
	}

	public BigDecimal getAggTotDemand() {
		return aggTotDemand;
	}

	public void setAggTotDemand(BigDecimal aggTotDemand) {
		this.aggTotDemand = aggTotDemand;
	}

	public BigDecimal getIndCurrDemand() {
		return indCurrDemand;
	}

	public void setIndCurrDemand(BigDecimal indCurrDemand) {
		this.indCurrDemand = indCurrDemand;
	}

	public BigDecimal getIndArrDemand() {
		return indArrDemand;
	}

	public void setIndArrDemand(BigDecimal indArrDemand) {
		this.indArrDemand = indArrDemand;
	}

	public BigDecimal getIndTotDemand() {
		return indTotDemand;
	}

	public void setIndTotDemand(BigDecimal indTotDemand) {
		this.indTotDemand = indTotDemand;
	}

	public Integer getIndNoOfPropCount() {
		return indNoOfPropCount;
	}

	public void setIndNoOfPropCount(Integer indNoOfPropCount) {
		this.indNoOfPropCount = indNoOfPropCount;
	}

}
