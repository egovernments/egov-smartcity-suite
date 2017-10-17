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
package org.egov.restapi.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class VacantLandDetails implements Serializable {

	private String surveyNumber;
	private String pattaNumber;
	private Float vacantLandArea;
	private Double marketValue;
	private Double currentCapitalValue;
	private String effectiveDate;
	private Long vacantLandPlot;
	private Long layoutApprovalAuthority;
	private String layoutPermitNumber;
	private String layoutPermitDate;

	public Long getVacantLandPlot() {
		return vacantLandPlot;
	}

	public void setVacantLandPlot(Long vacantLandPlot) {
		this.vacantLandPlot = vacantLandPlot;
	}

	public Long getLayoutApprovalAuthority() {
		return layoutApprovalAuthority;
	}

	public void setLayoutApprovalAuthority(Long layoutApprovalAuthority) {
		this.layoutApprovalAuthority = layoutApprovalAuthority;
	}

	public String getLayoutPermitNumber() {
		return layoutPermitNumber;
	}

	public void setLayoutPermitNumber(String layoutPermitNumber) {
		this.layoutPermitNumber = layoutPermitNumber;
	}

	public String getLayoutPermitDate() {
		return layoutPermitDate;
	}

	public void setLayoutPermitDate(String layoutPermitDate) {
		this.layoutPermitDate = layoutPermitDate;
	}

	@Override
	public String toString() {
		return "VacantLandDetailsRequest [surveyNumber=" + surveyNumber + ", pattaNumber=" + pattaNumber
				+ ", vacantLandArea=" + vacantLandArea + ", marketValue=" + marketValue + ", currentCapitalValue="
				+ currentCapitalValue + ", effectiveDate=" + effectiveDate + "]";
	}
	
	public String getSurveyNumber() {
		return surveyNumber;
	}

	public void setSurveyNumber(String surveyNumber) {
		this.surveyNumber = surveyNumber;
	}

	public String getPattaNumber() {
		return pattaNumber;
	}

	public void setPattaNumber(String pattaNumber) {
		this.pattaNumber = pattaNumber;
	}

	public Float getVacantLandArea() {
		return vacantLandArea;
	}

	public void setVacantLandArea(Float vacantLandArea) {
		this.vacantLandArea = vacantLandArea;
	}

	public Double getMarketValue() {
		return marketValue;
	}

	public void setMarketValue(Double marketValue) {
		this.marketValue = marketValue;
	}

	public Double getCurrentCapitalValue() {
		return currentCapitalValue;
	}

	public void setCurrentCapitalValue(Double currentCapitalValue) {
		this.currentCapitalValue = currentCapitalValue;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

}
