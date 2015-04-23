//TODO -- Fix compilation issues once demand module id committed
/*package org.egov.ptis.domain.entity.demand;

import org.egov.demand.model.EgDemand;
import org.egov.ptis.domain.entity.property.Property;

public class Ptdemand extends EgDemand {
	private Property egptProperty;
	private PTDemandCalculations dmdCalculations = null;

	*//**
	 * @return the egptProperty
	 *//*
	public Property getEgptProperty() {
		return egptProperty;
	}

	*//**
	 * @param egptProperty
	 *            the egptProperty to set
	 *//*
	public void setEgptProperty(Property egptProperty) {
		this.egptProperty = egptProperty;
	}

	*//**
	 * @return the dmdCalculations
	 *//*
	public PTDemandCalculations getDmdCalculations() {
		return dmdCalculations;
	}

	*//**
	 * @param dmdCalculations
	 *            the dmdCalculations to set
	 *//*
	public void setDmdCalculations(PTDemandCalculations dmdCalculations) {
		this.dmdCalculations = dmdCalculations;
	}

	@Override
	public Object clone() {
		Ptdemand ptdemandClone = (Ptdemand) super.clone(); 
		ptdemandClone.setEgptProperty(getEgptProperty());		
		PTDemandCalculations copyOfDmdCalc = new PTDemandCalculations(dmdCalculations);
		
		for (FloorwiseDemandCalculations floorDmdCalc : copyOfDmdCalc.getFlrwiseDmdCalculations()) {
			floorDmdCalc.setPTDemandCalculations(copyOfDmdCalc);
		}
						
		ptdemandClone.setDmdCalculations(copyOfDmdCalc);
		ptdemandClone.getDmdCalculations().setPtDemand(ptdemandClone);
		return ptdemandClone;
	}
	
	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr.append("Id: ").append(getId()).append("|Property: ").append(null!=getEgptProperty()?getEgptProperty().getId():"").append("|PTDemandCalc: ")
				.append(null!=getDmdCalculations()?getDmdCalculations().getId():"");

		return objStr.toString();
	}
}
*/