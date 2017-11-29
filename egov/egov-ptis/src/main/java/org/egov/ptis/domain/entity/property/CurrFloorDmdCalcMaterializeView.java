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

import java.io.Serializable;
import java.math.BigDecimal;

public class CurrFloorDmdCalcMaterializeView implements Serializable {

	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Long floorId;
	private PropertyMaterlizeView propMatView;
	private String unitNo;
	private String unitTypeConst;
	private BigDecimal fireTax;
	private BigDecimal lightTax;
	private BigDecimal sewerageTax;
	private BigDecimal generalTax;
	private BigDecimal waterTax;
	private String waterScheme;
	private BigDecimal egsTax;
	private BigDecimal bigBldgTax;
	private BigDecimal eduCessResdTax;
	private BigDecimal eduCessNonResdTax;
	private BigDecimal totalTax = BigDecimal.ZERO; 
	private BigDecimal alv;
	
	
	public Long getFloorId() {
		return floorId;
	}

	public void setFloorId(Long floorId) {
		this.floorId = floorId;
	}

	public PropertyMaterlizeView getPropMatView() {
		return propMatView;
	}

	public void setPropMatView(PropertyMaterlizeView propMatView) {
		this.propMatView = propMatView;
	}

	public String getUnitNo() {
		return unitNo;
	}

	public void setUnitNo(String unitNo) {
		this.unitNo = unitNo;
	}

	public String getUnitTypeConst() {
		return unitTypeConst;
	}

	public void setUnitTypeConst(String unitTypeConst) {
		this.unitTypeConst = unitTypeConst;
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

	public BigDecimal getSewerageTax() {
		return sewerageTax;
	}

	public void setSewerageTax(BigDecimal sewerageTax) {
		this.sewerageTax = sewerageTax;
	}

	public BigDecimal getGeneralTax() {
		return generalTax;
	}

	public void setGeneralTax(BigDecimal generalTax) {
		this.generalTax = generalTax;
	}

	public BigDecimal getWaterTax() {
		return waterTax;
	}

	public void setWaterTax(BigDecimal waterTax) {
		this.waterTax = waterTax;
	}

	public BigDecimal getEgsTax() {
		return egsTax;
	}

	public void setEgsTax(BigDecimal egsTax) {
		this.egsTax = egsTax;
	}

	public BigDecimal getBigBldgTax() {
		return bigBldgTax;
	}

	public void setBigBldgTax(BigDecimal bigBldgTax) {
		this.bigBldgTax = bigBldgTax;
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

	public BigDecimal getTotalTax() {
		totalTax = this.getSewerageTax().add(this.getGeneralTax()).add(this.getLightTax()).add(this.getWaterTax()).add(this.getBigBldgTax());
		return totalTax;
	}

	public void setTotalTax(BigDecimal totalTax) {
		this.totalTax = totalTax;
	}

	public String getWaterScheme() {
		return waterScheme;
	}

	public void setWaterScheme(String waterScheme) {
		this.waterScheme = waterScheme;
	}

	public BigDecimal getAlv() {
		return alv;
	}

	public void setAlv(BigDecimal alv) {
		this.alv = alv;
	}

	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr.append("PropertyMatView: " + getPropMatView()).append("|UnitNo: ").append(getUnitNo())
				.append("|UnitTypeConst").append(getUnitTypeConst())
				.append("|GeneralTax: ").append(getGeneralTax()).append("|EgsTax: ").append(getEgsTax())
				.append("|EduCessResdTax: ").append(getEduCessResdTax()).append("|EduCessNonResdTax: ").append(getEduCessNonResdTax())
				.append("|WaterTax: ").append(getWaterTax()).append("|FireTax: ").append(getFireTax())
				.append("|SewerageTax: ").append(getSewerageTax()).append("|LightTax: ").append(getLightTax())
				.append("|BigBldgTax: ").append(getBigBldgTax()).append("|WaterScheme").append(getWaterScheme())
				.append("|TotalTax").append(getTotalTax()).append("|ALV").append(getAlv());

		return objStr.toString();
	}
}
