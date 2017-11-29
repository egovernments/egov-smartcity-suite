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
package org.egov.ptis.domain.entity.property;

import java.math.BigDecimal;

public class PropertyArrear {
	private Long id;
	private BasicProperty basicProperty;
	private Integer fromDate;
	private Integer toDate;
	private BigDecimal generalTax = BigDecimal.ZERO;
	private BigDecimal sewerageTax = BigDecimal.ZERO;
	private BigDecimal fireServiceTax = BigDecimal.ZERO;
	private BigDecimal lightingTax = BigDecimal.ZERO;
	private BigDecimal generalWaterTax = BigDecimal.ZERO;
	private BigDecimal educationCess = BigDecimal.ZERO;
	private BigDecimal egCess = BigDecimal.ZERO;
	private BigDecimal bigResidentailTax = BigDecimal.ZERO;
	private BigDecimal penalty = BigDecimal.ZERO;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public BasicProperty getBasicProperty() {
		return basicProperty;
	}
	
	public void setBasicProperty(BasicProperty basicProperty) {
		this.basicProperty = basicProperty;
	}
	
	public Integer getFromDate() {
		return fromDate;
	}
	
	public void setFromDate(Integer fromDate) {
		this.fromDate = fromDate;
	}
	
	public Integer getToDate() {
		return toDate;
	}
	
	public void setToDate(Integer toDate) {
		this.toDate = toDate;
	}
	
	public BigDecimal getGeneralTax() {
		return generalTax;
	}
	
	public void setGeneralTax(BigDecimal generalTax) {
		this.generalTax = generalTax;
	}
	
	public BigDecimal getSewerageTax() {
		return sewerageTax;
	}
	
	public void setSewerageTax(BigDecimal sewerageTax) {
		this.sewerageTax = sewerageTax;
	}
	
	public BigDecimal getFireServiceTax() {
		return fireServiceTax;
	}
	
	public void setFireServiceTax(BigDecimal fireServiceTax) {
		this.fireServiceTax = fireServiceTax;
	}
	
	public BigDecimal getLightingTax() {
		return lightingTax;
	}
	
	public void setLightingTax(BigDecimal lightingTax) {
		this.lightingTax = lightingTax;
	}
	
	public BigDecimal getGeneralWaterTax() {
		return generalWaterTax;
	}
	
	public void setGeneralWaterTax(BigDecimal generalWaterTax) {
		this.generalWaterTax = generalWaterTax;
	}
	
	public BigDecimal getEducationCess() {
		return educationCess;
	}
	
	public void setEducationCess(BigDecimal educationCess) {
		this.educationCess = educationCess;
	}
	
	public BigDecimal getEgCess() {
		return egCess;
	}
	
	public void setEgCess(BigDecimal egCess) {
		this.egCess = egCess;
	}
	
	public BigDecimal getBigResidentailTax() {
		return bigResidentailTax;
	}
	
	public void setBigResidentailTax(BigDecimal bigResidentailTax) {
		this.bigResidentailTax = bigResidentailTax;
	}
	
	public BigDecimal getPenalty() {
		return penalty;
	}

	public void setPenalty(BigDecimal penalty) {
		this.penalty = penalty;
	}

	@Override
	public String toString() {
		return new StringBuilder(150)
				.append("PropertyArrear")
				.append(" [")
				.append("id=").append(getId())
				.append(", idBasicProperty=").append((getBasicProperty() != null) ? getBasicProperty().getId() : " ")
				.append(", fromDate=").append(getFromDate())
				.append(", toDate=").append(getToDate())
				.append(", generalTax=").append(getGeneralTax())
				.append(", sewerageTax=").append(getSewerageTax())
				.append(", fireServiceTax=").append(getFireServiceTax())
				.append(", lightingTax=").append(getLightingTax())
				.append(", generalWaterTax=").append(getGeneralWaterTax())
				.append(", educationCess=").append(getEducationCess())
				.append(", egCess=").append(getEgCess())
				.append(", bigResidentailTax=").append(getBigResidentailTax())
				.append(", penalty=").append(getPenalty())
				.append("]").toString();
	}
}
