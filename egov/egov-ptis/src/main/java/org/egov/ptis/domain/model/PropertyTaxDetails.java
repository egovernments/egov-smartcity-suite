/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.ptis.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * The PropertyTaxDetails class is used to contain property tax details, arrears
 * details and the corresponding error details if any.
 * 
 * @author ranjit
 *
 */
@SuppressWarnings("serial")
public class PropertyTaxDetails implements Serializable{
	private String demandYear;
	private String guardianName;
	private String ownerAddress;
	private String ownerName;
	private Boolean hasArrears;
	private BigDecimal unAuthPenalty;
	private BigDecimal taxAmt;
	private BigDecimal educationCess;
	private BigDecimal libraryCess;
	private BigDecimal propertyTax;
	private BigDecimal penalty;
	private BigDecimal totalTaxAmt;
	private Set<ArrearDetails> arrearDetails;
	private ErrorDetails errorDetails;
	public String getDemandYear() {
		return demandYear;
	}
	public void setDemandYear(String demandYear) {
		this.demandYear = demandYear;
	}
	public String getGuardianName() {
		return guardianName;
	}
	public void setGuardianName(String guardianName) {
		this.guardianName = guardianName;
	}
	public String getOwnerAddress() {
		return ownerAddress;
	}
	public void setOwnerAddress(String ownerAddress) {
		this.ownerAddress = ownerAddress;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public Boolean getHasArrears() {
		return hasArrears;
	}
	public void setHasArrears(Boolean hasArrears) {
		this.hasArrears = hasArrears;
	}
	public BigDecimal getUnAuthPenalty() {
		return unAuthPenalty;
	}
	public void setUnAuthPenalty(BigDecimal unAuthPenalty) {
		this.unAuthPenalty = unAuthPenalty;
	}
	public BigDecimal getTaxAmt() {
		return taxAmt;
	}
	public void setTaxAmt(BigDecimal taxAmt) {
		this.taxAmt = taxAmt;
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
	public BigDecimal getPenalty() {
		return penalty;
	}
	public void setPenalty(BigDecimal penalty) {
		this.penalty = penalty;
	}
	public BigDecimal getTotalTaxAmt() {
		return totalTaxAmt;
	}
	public void setTotalTaxAmt(BigDecimal totalTaxAmt) {
		this.totalTaxAmt = totalTaxAmt;
	}
	public Set<ArrearDetails> getArrearDetails() {
		return arrearDetails;
	}
	public void setArrearDetails(Set<ArrearDetails> arrearDetails) {
		this.arrearDetails = arrearDetails;
	}
	public ErrorDetails getErrorDetails() {
		return errorDetails;
	}
	public void setErrorDetails(ErrorDetails errorDetails) {
		this.errorDetails = errorDetails;
	}
	@Override
	public String toString() {
		return "PropertyTaxDetails [demandYear=" + demandYear + ", guardianName=" + guardianName + ", ownerAddress="
				+ ownerAddress + ", ownerName=" + ownerName + ", hasArrears=" + hasArrears + ", unAuthPenalty="
				+ unAuthPenalty + ", taxAmt=" + taxAmt + ", educationCess=" + educationCess + ", libraryCess="
				+ libraryCess + ", propertyTax=" + propertyTax + ", penalty=" + penalty + ", totalTaxAmt=" + totalTaxAmt
				+ ", arrearDetails=" + arrearDetails + ", errorDetails=" + errorDetails + "]";
	}
	
	
}
