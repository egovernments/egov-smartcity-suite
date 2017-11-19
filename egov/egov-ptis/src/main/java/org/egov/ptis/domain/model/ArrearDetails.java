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
package org.egov.ptis.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * The ArrearDetails class is used to contain arrears details.
 *
 */
@SuppressWarnings("serial")
public class ArrearDetails implements Serializable{
	private BigDecimal penalty;
	private BigDecimal tax;
	private BigDecimal totalSum;
	private String demandYear;
	private BigDecimal educationCess;
	private BigDecimal libraryCess;
	private BigDecimal propertyTax;
	private BigDecimal unAuthPenalty;
	private BigDecimal chqBouncePenalty;
	public BigDecimal getPenalty() {
		return penalty;
	}
	public void setPenalty(BigDecimal penalty) {
		this.penalty = penalty;
	}
	public BigDecimal getTax() {
		return tax;
	}
	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}
	public BigDecimal getTotalSum() {
		return totalSum;
	}
	public void setTotalSum(BigDecimal totalSum) {
		this.totalSum = totalSum;
	}
	public String getDemandYear() {
		return demandYear;
	}
	public void setDemandYear(String demandYear) {
		this.demandYear = demandYear;
	}
	public BigDecimal getEducationCess() {
		return educationCess;
	}
	public void setEducationCess(BigDecimal educationCess) {
		this.educationCess = educationCess;
	}
	public BigDecimal getLibraryCess() {
		return libraryCess;
	}
	public void setLibraryCess(BigDecimal libraryCess) {
		this.libraryCess = libraryCess;
	}
	public BigDecimal getPropertyTax() {
		return propertyTax;
	}
	public void setPropertyTax(BigDecimal propertyTax) {
		this.propertyTax = propertyTax;
	}
	public BigDecimal getUnAuthPenalty() {
		return unAuthPenalty;
	}
	public void setUnAuthPenalty(BigDecimal unAuthPenalty) {
		this.unAuthPenalty = unAuthPenalty;
	}
	@Override
	public String toString() {
		return "ArrearDetails [penalty=" + penalty + ", tax=" + tax + ", totalSum=" + totalSum + ", demandYear="
				+ demandYear + ", educationCess=" + educationCess + ", libraryCess=" + libraryCess + ", propertyTax="
				+ propertyTax + ", unAuthPenalty=" + unAuthPenalty + "]";
	}
	public BigDecimal getChqBouncePenalty() {
		return chqBouncePenalty;
	}
	public void setChqBouncePenalty(BigDecimal chqBouncePenalty) {
		this.chqBouncePenalty = chqBouncePenalty;
	}
	
}
