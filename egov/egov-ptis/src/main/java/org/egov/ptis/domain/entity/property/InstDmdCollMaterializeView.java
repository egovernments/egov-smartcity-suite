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
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
package org.egov.ptis.domain.entity.property;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.egov.commons.Installment;

public class InstDmdCollMaterializeView implements Serializable {

	private PropertyMaterlizeView propMatView;
	private Installment installment;
	private BigDecimal generalTax;
	private BigDecimal egsTax;
	private BigDecimal eduCessResdTax;
	private BigDecimal eduCessNonResdTax;
	private BigDecimal waterTax;
	private BigDecimal fireTax;
	private BigDecimal sewerageTax;
	private BigDecimal lightTax;
	private BigDecimal bigBldgTax;
	private BigDecimal generalTaxColl;
	private BigDecimal egsTaxColl;
	private BigDecimal eduCessResdTaxColl;
	private BigDecimal eduCessNonResdTaxColl;
	private BigDecimal waterTaxColl;
	private BigDecimal fireTaxColl;
	private BigDecimal sewerageTaxColl;
	private BigDecimal lightTaxColl;
	private BigDecimal bigBldgTaxColl;
	private BigDecimal chqBouncePenalty;
	private BigDecimal penaltyFine;
	private BigDecimal chqBuncPnltyColl;
	private BigDecimal penaltyFineColl;
	private Date createdDate;
	
	public PropertyMaterlizeView getPropMatView() {
		return propMatView;
	}

	public void setPropMatView(PropertyMaterlizeView propMatView) {
		this.propMatView = propMatView;
	}

	public Installment getInstallment() {
		return installment;
	}

	public void setInstallment(Installment installment) {
		this.installment = installment;
	}

	public BigDecimal getGeneralTax() {
		return generalTax;
	}

	public void setGeneralTax(BigDecimal generalTax) {
		this.generalTax = generalTax;
	}

	public BigDecimal getEgsTax() {
		return egsTax;
	}

	public void setEgsTax(BigDecimal egsTax) {
		this.egsTax = egsTax;
	}

	public BigDecimal getEduCessResdTax() {
		return eduCessResdTax;
	}

	public void setEduCessResdTax(BigDecimal eduCessResdTax) {
		this.eduCessResdTax = eduCessResdTax;
	}

	public BigDecimal getEduCessNonResdTax() {
		return eduCessNonResdTax;
	}

	public void setEduCessNonResdTax(BigDecimal eduCessNonResdTax) {
		this.eduCessNonResdTax = eduCessNonResdTax;
	}

	public BigDecimal getWaterTax() {
		return waterTax;
	}

	public void setWaterTax(BigDecimal waterTax) {
		this.waterTax = waterTax;
	}

	public BigDecimal getFireTax() {
		return fireTax;
	}

	public void setFireTax(BigDecimal fireTax) {
		this.fireTax = fireTax;
	}

	public BigDecimal getSewerageTax() {
		return sewerageTax;
	}

	public void setSewerageTax(BigDecimal sewerageTax) {
		this.sewerageTax = sewerageTax;
	}

	public BigDecimal getLightTax() {
		return lightTax;
	}

	public void setLightTax(BigDecimal lightTax) {
		this.lightTax = lightTax;
	}

	public BigDecimal getBigBldgTax() {
		return bigBldgTax;
	}

	public void setBigBldgTax(BigDecimal bigBldgTax) {
		this.bigBldgTax = bigBldgTax;
	}

	public BigDecimal getGeneralTaxColl() {
		return generalTaxColl;
	}

	public void setGeneralTaxColl(BigDecimal generalTaxColl) {
		this.generalTaxColl = generalTaxColl;
	}

	public BigDecimal getEgsTaxColl() {
		return egsTaxColl;
	}

	public void setEgsTaxColl(BigDecimal egsTaxColl) {
		this.egsTaxColl = egsTaxColl;
	}

	public BigDecimal getEduCessResdTaxColl() {
		return eduCessResdTaxColl;
	}

	public void setEduCessResdTaxColl(BigDecimal eduCessResdTaxColl) {
		this.eduCessResdTaxColl = eduCessResdTaxColl;
	}

	public BigDecimal getEduCessNonResdTaxColl() {
		return eduCessNonResdTaxColl;
	}

	public void setEduCessNonResdTaxColl(BigDecimal eduCessNonResdTaxColl) {
		this.eduCessNonResdTaxColl = eduCessNonResdTaxColl;
	}

	public BigDecimal getWaterTaxColl() {
		return waterTaxColl;
	}

	public void setWaterTaxColl(BigDecimal waterTaxColl) {
		this.waterTaxColl = waterTaxColl;
	}

	public BigDecimal getFireTaxColl() {
		return fireTaxColl;
	}

	public void setFireTaxColl(BigDecimal fireTaxColl) {
		this.fireTaxColl = fireTaxColl;
	}

	public BigDecimal getSewerageTaxColl() {
		return sewerageTaxColl;
	}

	public void setSewerageTaxColl(BigDecimal sewerageTaxColl) {
		this.sewerageTaxColl = sewerageTaxColl;
	}

	public BigDecimal getLightTaxColl() {
		return lightTaxColl;
	}

	public void setLightTaxColl(BigDecimal lightTaxColl) {
		this.lightTaxColl = lightTaxColl;
	}

	public BigDecimal getBigBldgTaxColl() {
		return bigBldgTaxColl;
	}

	public void setBigBldgTaxColl(BigDecimal bigBldgTaxColl) {
		this.bigBldgTaxColl = bigBldgTaxColl;
	}

	public BigDecimal getChqBouncePenalty() {
		return chqBouncePenalty;
	}

	public void setChqBouncePenalty(BigDecimal chqBouncePenalty) {
		this.chqBouncePenalty = chqBouncePenalty;
	}

	public BigDecimal getPenaltyFine() {
		return penaltyFine;
	}

	public void setPenaltyFine(BigDecimal penaltyFine) {
		this.penaltyFine = penaltyFine;
	}

	public BigDecimal getChqBuncPnltyColl() {
		return chqBuncPnltyColl;
	}

	public void setChqBuncPnltyColl(BigDecimal chqBuncPnltyColl) {
		this.chqBuncPnltyColl = chqBuncPnltyColl;
	}

	public BigDecimal getPenaltyFineColl() {
		return penaltyFineColl;
	}

	public void setPenaltyFineColl(BigDecimal penaltyFineColl) {
		this.penaltyFineColl = penaltyFineColl;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr.append("|InstallmentId: ").append(getInstallment())
				.append("|GeneralTax: ").append(getGeneralTax()).append("|EgsTax: ").append(getEgsTax())
				.append("|EduCessResdTax: ").append(getEduCessResdTax()).append("|EduCessNonResdTax: ").append(getEduCessNonResdTax())
				.append("|WaterTax: ").append(getWaterTax()).append("|FireTax: ").append(getFireTax())
				.append("|SewerageTax: ").append(getSewerageTax()).append("|LightTax: ").append(getLightTax())
				.append("|BigBldgTax: ").append(getBigBldgTax())
				.append("|GeneralTaxColl: ").append(getGeneralTaxColl())
				.append("|EgsTaxColl: ").append(getEgsTaxColl()).append("|EduCessResdTaxColl: ").append(getEduCessResdTaxColl())
				.append("|EduCessNonResdTaxColl: ").append(getEduCessNonResdTaxColl()).append("|WaterTaxColl: ").append(getWaterTaxColl())
				.append("|FireTaxColl: ").append(getFireTaxColl()).append("|SewerageTaxColl").append(getSewerageTaxColl())
				.append("|LightTaxColl: ").append(getLightTaxColl()).append("|BigBldgTaxColl: ").append(getBigBldgTaxColl());

		return objStr.toString();
	}
}
