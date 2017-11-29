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

public class TaxInfo {

	
	private String installment;
	private BigDecimal conservancyTax;
	private BigDecimal waterTax;
	private BigDecimal generalTax;
	private BigDecimal fireServiceTax;
	private BigDecimal lightTax;
	private BigDecimal eduCess;
	private BigDecimal egsCess;
	private BigDecimal bigBuildingCess;
	private BigDecimal total;
	private BigDecimal grandTotal;
	
	public String getInstallment() {
		return installment;
	}
	public void setInstallment(String installment) {
		this.installment = installment;
	}
	public BigDecimal getConservancyTax() {
		return conservancyTax;
	}
	public void setConservancyTax(BigDecimal conservancyTax) {
		this.conservancyTax = conservancyTax;
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
	public BigDecimal getFireServiceTax() {
		return fireServiceTax;
	}
	public void setFireServiceTax(BigDecimal fireServiceTax) {
		this.fireServiceTax = fireServiceTax;
	}
	public BigDecimal getLightTax() {
		return lightTax;
	}
	public void setLightTax(BigDecimal lightTax) {
		this.lightTax = lightTax;
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
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
	public BigDecimal getGrandTotal() {
		return grandTotal;
	}
	
	public void setGrandTotal(BigDecimal grandTotal) {
		this.grandTotal = grandTotal;
	}	
}
