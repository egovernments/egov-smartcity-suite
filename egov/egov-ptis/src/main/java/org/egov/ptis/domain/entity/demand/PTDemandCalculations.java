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

import org.egov.demand.model.DemandCalculations;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.models.BaseModel;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 * This interface represents Demand Calculations for Property Tax System.
 * 
 * @author Gayathri Joshi
 * @version 1.00
 * @see
 * @see
 * @since 1.00
 */
public class PTDemandCalculations extends BaseModel implements DemandCalculations {

	public DemandCalculations createDemandCalculations() {

		return null;
	}

	private Ptdemand ptDemand;
	private BigDecimal propertyTax;
	private BigDecimal rateOfTax;
	private Date lastUpdatedTimeStamp;
	private Date createTimeStamp;
	private Set<FloorwiseDemandCalculations> flrwiseDmdCalculations = new java.util.HashSet<FloorwiseDemandCalculations>();
	private byte[] taxInfo;
	private BigDecimal alv;

	public PTDemandCalculations(Ptdemand ptDemand, BigDecimal propertyTax,
			BigDecimal rateOfTax, Date lastUpdatedTimeStamp,
			Date createTimeStamp,
			Set<FloorwiseDemandCalculations> flrwiseDmdCalculations,
			byte[] taxInfo,
			BigDecimal alv) {
		super();
		this.ptDemand = ptDemand;
		this.propertyTax = propertyTax;
		this.rateOfTax = rateOfTax;
		this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
		this.createTimeStamp = createTimeStamp;
		this.flrwiseDmdCalculations = flrwiseDmdCalculations;
		this.taxInfo = taxInfo;
		this.alv = alv;
	}

	public PTDemandCalculations(PTDemandCalculations ptDemandCalc) {
		this.propertyTax = ptDemandCalc.propertyTax;
		this.rateOfTax = ptDemandCalc.rateOfTax;
		this.lastUpdatedTimeStamp = new Date();

		for (FloorwiseDemandCalculations floorDmdCalc : ptDemandCalc.flrwiseDmdCalculations) {
			this.addFlrwiseDmdCalculations(new FloorwiseDemandCalculations(
					floorDmdCalc));
		}

		this.taxInfo = ptDemandCalc.taxInfo;
	}

	public PTDemandCalculations() {
		super();
	}

	/**
	 * @return true if the given Object is equal to PTDemandCalculations
	 */
	public boolean equals(Object that) {
		if (that == null)
			return false;

		if (this == that)
			return true;
		if (that.getClass() != this.getClass())
			return false;

		final PTDemandCalculations thatDemand = (PTDemandCalculations) that;
		if (this.getId() != null && thatDemand.getId() != null) {
			if (getId().equals(thatDemand.getId())) {
				return true;
			} else
				return false;
		} else if (this.getPtDemand() != null
				&& thatDemand.getPtDemand() != null) {
			if (getPtDemand().equals(thatDemand.getPtDemand())) {
				return true;
			} else
				return false;
		} else
			return false;

	}

	/**
	 * @return Returns the hashCode
	 */
	public int hashCode() {
		int hashCode = 0;
		if (getId() != null) {
			hashCode += this.getId().hashCode();
		}
		if (getPtDemand() != null) {
			hashCode += this.getPtDemand().hashCode();
		}
		return hashCode;
	}

	/**
	 * @return Returns the boolean after validating the current object
	 */
	public boolean validateDmdCalc() {
		if (getPtDemand() == null)
			throw new ApplicationRuntimeException(
					"In PTDemandCalculations Validate : ptDemand is Not Set, Please Check !!");
		if (getFlrwiseDmdCalculations() == null
				|| getFlrwiseDmdCalculations().size() == 0)
			throw new ApplicationRuntimeException(
					"In PTDemandCalculations Validate : FloorwiseDmdCalculations is Not Set, Please Check !!");
		return true;
	}

	public Ptdemand getPtDemand() {
		return ptDemand;
	}

	public void setPtDemand(Ptdemand ptDemand) {
		this.ptDemand = ptDemand;
	}

	public BigDecimal getPropertyTax() {
		return propertyTax;
	}

	public void setPropertyTax(BigDecimal propertyTax) {
		this.propertyTax = propertyTax;
	}

	public BigDecimal getRateOfTax() {
		return rateOfTax;
	}

	public void setRateOfTax(BigDecimal rateOfTax) {
		this.rateOfTax = rateOfTax;
	}

	public Date getLastUpdatedTimeStamp() {
		return lastUpdatedTimeStamp;
	}

	public void setLastUpdatedTimeStamp(Date lastUpdatedTimeStamp) {
		this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
	}

	public Date getCreateTimeStamp() {
		return createTimeStamp;
	}

	public void setCreateTimeStamp(Date createTimeStamp) {
		this.createTimeStamp = createTimeStamp;
	}

	public Set<FloorwiseDemandCalculations> getFlrwiseDmdCalculations() {
		return flrwiseDmdCalculations;
	}

	public void setFlrwiseDmdCalculations(
			Set<FloorwiseDemandCalculations> flrwiseDmdCalculations) {
		this.flrwiseDmdCalculations = flrwiseDmdCalculations;
	}

	public void addFlrwiseDmdCalculations(FloorwiseDemandCalculations flwiseDmd) {
		getFlrwiseDmdCalculations().add(flwiseDmd);
		flwiseDmd.setPTDemandCalculations(this);
	}

	public void removeFlrwiseDmdCalculations(
			FloorwiseDemandCalculations flwiseDmd) {
		getFlrwiseDmdCalculations().remove(flwiseDmd);
	}

	public byte[] getTaxInfo() {
		return taxInfo;
	}

	public void setTaxInfo(byte[] taxInfo) {
		this.taxInfo = taxInfo;
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

		objStr.append("Id: ").append(getId()).append("|PtDemamd: ")
				.append(getPtDemand()).append("|PropertyTax: ")
				.append(getPropertyTax()).append("|RateOfTax: ")
				.append(getRateOfTax()).append("|TaxInfo: ")
				.append(getTaxInfo()).append("|Alv: ").append(getAlv());

		return objStr.toString();
	}

}
