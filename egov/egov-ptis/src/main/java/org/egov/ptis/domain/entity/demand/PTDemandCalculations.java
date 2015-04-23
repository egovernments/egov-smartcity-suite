/*
 * PTDemandCalculations.java Created on Mar 07, 2006
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.entity.demand;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.egov.exceptions.EGOVRuntimeException;
//TODO -- Uncomment this once demand code is available
//import org.egov.demand.model.DemandCalculations;
import org.egov.infstr.flexfields.model.EgAttributevalues;
import org.egov.infstr.models.BaseModel;

/**
 * This interface represents Demand Calculations for Property Tax System.
 * 
 * @author Gayathri Joshi
 * @version 1.00
 * @see
 * @see
 * @since 1.00
 */
public class PTDemandCalculations extends BaseModel /*implements DemandCalculations*/ {

	//TODO -- Uncomment this once demand code is available
	/*public DemandCalculations createDemandCalculations() {
		// TODO Auto-generated method stub
		return null;
	}*/

	//private Ptdemand ptDemand;
	private BigDecimal propertyTax;
	private BigDecimal rateOfTax;
	private Date lastUpdatedTimeStamp;
	private Date createTimeStamp;
	private Set<FloorwiseDemandCalculations> flrwiseDmdCalculations = new java.util.HashSet<FloorwiseDemandCalculations>();
	private Set<EgAttributevalues> attributeValues = new HashSet<EgAttributevalues>();
	private byte[] taxInfo;
	private BigDecimal alv;
	
	//TODO -- Uncomment this once demand code is available
	public PTDemandCalculations(/*Ptdemand ptDemand,*/ BigDecimal propertyTax, BigDecimal rateOfTax,
			Date lastUpdatedTimeStamp, Date createTimeStamp, Set<FloorwiseDemandCalculations> flrwiseDmdCalculations,
			Set<EgAttributevalues> attributeValues, byte[] taxInfo, BigDecimal alv) {
		super();
		//this.ptDemand = ptDemand;
		this.propertyTax = propertyTax;
		this.rateOfTax = rateOfTax;
		this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
		this.createTimeStamp = createTimeStamp;
		this.flrwiseDmdCalculations = flrwiseDmdCalculations;
		this.attributeValues = attributeValues;
		this.taxInfo = taxInfo;
		this.alv = alv;
	}
	
	public PTDemandCalculations(PTDemandCalculations ptDemandCalc) {
		this.propertyTax = ptDemandCalc.propertyTax;
		this.rateOfTax = ptDemandCalc.rateOfTax;
		this.lastUpdatedTimeStamp = new Date();
		
		for (FloorwiseDemandCalculations floorDmdCalc : ptDemandCalc.flrwiseDmdCalculations) {
			this.addFlrwiseDmdCalculations(new FloorwiseDemandCalculations(floorDmdCalc));
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
		//TODO -- Uncomment this once demand code is available
		if (this.getId() != null && thatDemand.getId() != null) {
			if (getId().equals(thatDemand.getId())) {
				return true;
			} else
				return false;
		} /*else if (this.getPtDemand() != null && thatDemand.getPtDemand() != null) {
			if (getPtDemand().equals(thatDemand.getPtDemand())) {
				return true;
			} else
				return false;
		}*/ else
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
		//TODO -- Uncomment this once demand code is available
		/*if (getPtDemand() != null) {
			hashCode += this.getPtDemand().hashCode();
		}
*/
		return hashCode;
	}

	/**
	 * @return Returns the boolean after validating the current object
	 */
	public boolean validateDmdCalc() {
		//TODO -- Uncomment this once demand code is available
		/*if (getPtDemand() == null)
			throw new EGOVRuntimeException("In PTDemandCalculations Validate : ptDemand is Not Set, Please Check !!");*/
		if (getFlrwiseDmdCalculations() == null || getFlrwiseDmdCalculations().size() == 0)
			throw new EGOVRuntimeException(
					"In PTDemandCalculations Validate : FloorwiseDmdCalculations is Not Set, Please Check !!");
		return true;
	}

	//TODO -- Uncomment this once demand code is available
	
	/*public Ptdemand getPtDemand() {
		return ptDemand;
	}

	public void setPtDemand(Ptdemand ptDemand) {
		this.ptDemand = ptDemand;
	}*/

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

	public void setFlrwiseDmdCalculations(Set<FloorwiseDemandCalculations> flrwiseDmdCalculations) {
		this.flrwiseDmdCalculations = flrwiseDmdCalculations;
	}

	public void addFlrwiseDmdCalculations(FloorwiseDemandCalculations flwiseDmd) {
		getFlrwiseDmdCalculations().add(flwiseDmd);
		flwiseDmd.setPTDemandCalculations(this);
	}

	public void removeFlrwiseDmdCalculations(FloorwiseDemandCalculations flwiseDmd) {
		getFlrwiseDmdCalculations().remove(flwiseDmd);
	}

	public Set<EgAttributevalues> getAttributeValues() {
		return attributeValues;
	}

	public void setAttributeValues(Set<EgAttributevalues> attributeValues) {
		this.attributeValues = attributeValues;
	}

	public void addAttributeValues(EgAttributevalues attributeValues) {
		getAttributeValues().add(attributeValues);
	}

	public void removeAttributeValues(EgAttributevalues attributeValues) {
		getAttributeValues().remove(attributeValues);
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

	//TODO -- Uncomment this once demand code is available
	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr.append("Id: ").append(getId()).append("|PtDemamd: ")/*.append(getPtDemand())*/.append("|PropertyTax: ")
				.append(getPropertyTax()).append("|RateOfTax: ").append(getRateOfTax()).append("|TaxInfo: ").append(
						getTaxInfo()).append("|Alv: ").append(getAlv());

		return objStr.toString();
	}

}
