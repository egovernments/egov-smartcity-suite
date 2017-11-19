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

/**
 * 
 * @author subhash
 *
 */

public class TaxCollectionInfo {

	private String taxType;
	private BigDecimal sewerageTax = BigDecimal.ZERO;
	private BigDecimal waterTax = BigDecimal.ZERO;
	private BigDecimal generalTax = BigDecimal.ZERO;
	private BigDecimal fireTax = BigDecimal.ZERO;
	private BigDecimal lightTax = BigDecimal.ZERO;
	private BigDecimal sewerageBenefitTax = BigDecimal.ZERO;
	private BigDecimal waterBenefitTax = BigDecimal.ZERO;
	private BigDecimal streetTax = BigDecimal.ZERO;
	private BigDecimal municipalEduCess = BigDecimal.ZERO;
	private BigDecimal eduCess = BigDecimal.ZERO;
	private BigDecimal egsCess = BigDecimal.ZERO;
	private BigDecimal bigBuildingCess = BigDecimal.ZERO;
	private BigDecimal miscTax = BigDecimal.ZERO;
	private BigDecimal total = BigDecimal.ZERO;
	
	public BigDecimal getSewerageTax() {
		return sewerageTax;
	}
	public void setSewerageTax(BigDecimal sewerageTax) {
		this.sewerageTax = sewerageTax;
	}
	public BigDecimal getWaterTax() {
		return waterTax;
	}
	public void setWaterTax(BigDecimal waterTax) {
		this.waterTax = waterTax;
	}
	public BigDecimal getGeneralTax() {
		return generalTax;
	}
	public void setGeneralTax(BigDecimal generalTax) {
		this.generalTax = generalTax;
	}
	public BigDecimal getFireTax() {
		return fireTax;
	}
	public void setFireTax(BigDecimal fireTax) {
		this.fireTax = fireTax;
	}
	public BigDecimal getLightTax() {
		return lightTax;
	}
	public void setLightTax(BigDecimal lightTax) {
		this.lightTax = lightTax;
	}
	public BigDecimal getSewerageBenefitTax() {
		return sewerageBenefitTax;
	}
	public void setSewerageBenefitTax(BigDecimal sewerageBenefitTax) {
		this.sewerageBenefitTax = sewerageBenefitTax;
	}
	public BigDecimal getWaterBenefitTax() {
		return waterBenefitTax;
	}
	public void setWaterBenefitTax(BigDecimal waterBenefitTax) {
		this.waterBenefitTax = waterBenefitTax;
	}
	public BigDecimal getStreetTax() {
		return streetTax;
	}
	public void setStreetTax(BigDecimal streetTax) {
		this.streetTax = streetTax;
	}
	public BigDecimal getMunicipalEduCess() {
		return municipalEduCess;
	}
	public void setMunicipalEduCess(BigDecimal municipalEduCess) {
		this.municipalEduCess = municipalEduCess;
	}
	public BigDecimal getEduCess() {
		return eduCess;
	}
	public void setEduCess(BigDecimal eduCess) {
		this.eduCess = eduCess;
	}
	public BigDecimal getEgsCess() {
		return egsCess;
	}
	public void setEgsCess(BigDecimal egsCess) {
		this.egsCess = egsCess;
	}
	public BigDecimal getBigBuildingCess() {
		return bigBuildingCess;
	}
	public void setBigBuildingCess(BigDecimal bigBuildingCess) {
		this.bigBuildingCess = bigBuildingCess;
	}
	public BigDecimal getMiscTax() {
		return miscTax;
	}
	public void setMiscTax(BigDecimal miscTax) {
		this.miscTax = miscTax;
	}
	public String getTaxType() {
		return taxType;
	}
	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
}
