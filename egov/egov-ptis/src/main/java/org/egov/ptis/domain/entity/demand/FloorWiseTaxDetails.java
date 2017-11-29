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
package org.egov.ptis.domain.entity.demand;

import org.egov.commons.Area;

import java.math.BigDecimal;

/**
 * Data Carrier for Floor Wise Demand and other factors. Used primarily during
 * Property Creation/Edit
 * 
 * @author Administrator
 * @version 1.00
 * @see
 * @see
 * @since 1.00
 */
public class FloorWiseTaxDetails {

	private Integer floorNum;
	private BigDecimal unitAreaTax = null;
	private Area floorArea = null;
	private Float ageFactor = null;
	private Float structFactor = null;
	private Float usageFactor = null;
	private Float occupanyFactor = null;
	private Float flatFactor = null;
	private BigDecimal floorTax = null;
	private BigDecimal netTax = null;

	/**
	 * @return Returns the netTax.
	 */
	public BigDecimal getNetTax() {
		return netTax;
	}

	/**
	 * @param netTax
	 *            The netTax to set.
	 */
	public void setNetTax(BigDecimal netTax) {
		this.netTax = netTax;
	}

	/**
	 * @return Returns the flatFactor.
	 */
	public Float getFlatFactor() {
		return flatFactor;
	}

	/**
	 * @param flatFactor
	 *            The flatFactor to set.
	 */
	public void setFlatFactor(Float flatFactor) {
		this.flatFactor = flatFactor;
	}

	/**
	 * @return Returns the occupanyFactor.
	 */
	public Float getOccupanyFactor() {
		return occupanyFactor;
	}

	/**
	 * @param occupanyFactor
	 *            The occupanyFactor to set.
	 */
	public void setOccupanyFactor(Float occupanyFactor) {
		this.occupanyFactor = occupanyFactor;
	}

	/**
	 * @return Returns the structFactor.
	 */
	public Float getStructFactor() {
		return structFactor;
	}

	/**
	 * @param structFactor
	 *            The structFactor to set.
	 */
	public void setStructFactor(Float structFactor) {
		this.structFactor = structFactor;
	}

	/**
	 * @return Returns the usageFactor.
	 */
	public Float getUsageFactor() {
		return usageFactor;
	}

	/**
	 * @param usageFactor
	 *            The usageFactor to set.
	 */
	public void setUsageFactor(Float usageFactor) {
		this.usageFactor = usageFactor;
	}

	/**
	 * @return Returns the ageFactor.
	 */
	public Float getAgeFactor() {
		return ageFactor;
	}

	/**
	 * @param ageFactor
	 *            The ageFactor to set.
	 */
	public void setAgeFactor(Float ageFactor) {
		this.ageFactor = ageFactor;
	}

	/**
	 * @return Returns the floorArea.
	 */
	public Area getFloorArea() {
		return floorArea;
	}

	/**
	 * @param floorArea
	 *            The floorArea to set.
	 */
	public void setFloorArea(Area floorArea) {
		this.floorArea = floorArea;
	}

	/**
	 * @return Returns the floorNum.
	 */
	public Integer getFloorNum() {
		return floorNum;
	}

	/**
	 * @param floorNum
	 *            The floorNum to set.
	 */
	public void setFloorNum(Integer floorNum) {
		this.floorNum = floorNum;
	}

	/**
	 * @return Returns the floorTax.
	 */
	public BigDecimal getFloorTax() {
		return floorTax;
	}

	/**
	 * @param floorTax
	 *            The floorTax to set.
	 */
	public void setFloorTax(BigDecimal floorTax) {
		this.floorTax = floorTax;
	}

	/**
	 * @return Returns the unitAreaTax.
	 */
	public BigDecimal getUnitAreaTax() {
		return unitAreaTax;
	}

	/**
	 * @param unitAreaTax
	 *            The unitAreaTax to set.
	 */
	public void setUnitAreaTax(BigDecimal unitAreaTax) {
		this.unitAreaTax = unitAreaTax;
	}

	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr.append("FloorNum: ").append(getFloorNum()).append("|UnitAreaTax: ").append(getUnitAreaTax());
		objStr = (getFloorArea() != null) ? objStr.append("|FloorArea: ").append(getFloorArea().getArea()) : objStr
				.append("");
		objStr.append("|AgeFactor: ").append(getAgeFactor()).append("|StructFactor: ").append(getStructFactor())
				.append("|UsageFactor: ").append(getUsageFactor()).append("|OccupFactor: ").append(getOccupanyFactor())
				.append("|FlatFactor: ").append(getFlatFactor()).append("|FloorTax: ").append(getFloorTax()).append(
						"|NetTax: ").append(getNetTax());

		return objStr.toString();
	}
}
