/*
 * PropertyID.java Created on Nov 25, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.entity.property;

import java.util.ArrayList;
import java.util.List;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infstr.ValidationError;
import org.egov.infstr.models.BaseModel;

/**
 * This class reperesents the IOnfor,ation which is required to create a unique
 * Identification number for the Proeprty.
 * 
 * @author Neetu
 * @version 2.00
 */

public class PropertyID extends BaseModel {
	private BasicProperty basicProperty;
	private Boundary zone;
	private Boundary ward;
	private Integer colonyId;
	private String doorNum;
	private Integer assUnit;
	private Boundary area;
	private Boundary locality;
	private Boundary Street;
	private Boundary frontBndryStreet;
	private Boundary backBndryStreet;
	private Boundary leftBndryStreet;
	private Boundary rightBndryStreet;
	private Boundary bndryStreetForDmdCalc;
	private String northBoundary;
	private String southBoundary;
	private String eastBoundary;
	private String westBoundary;

	/**
	 * @return Returns the hashCode
	 */
	public int hashCode() {
		int hashCode = 0;
		if (getId() != null) {
			hashCode = hashCode + getId().hashCode();
		} else if (getBasicProperty() != null) {
			hashCode = hashCode + getBasicProperty().hashCode();
		}

		return hashCode;
	}

	public List<ValidationError> validate() {
		return new ArrayList<ValidationError>();
	}

	/**
	 * @return Returns the boolean after validating the current object
	 */

	public boolean validatePropertID() {
		if (getBasicProperty() == null) {
			throw new EGOVRuntimeException("PropertyID.validate : BasicProperty is not set, Please Check !!");
		}
		return true;
	}

	public BasicProperty getBasicProperty() {
		return basicProperty;
	}

	public void setBasicProperty(BasicProperty basicProperty) {
		this.basicProperty = basicProperty;
	}

	public Boundary getZone() {
		return zone;
	}

	public void setZone(Boundary zone) {
		this.zone = zone;
	}

	public Boundary getWard() {
		return ward;
	}

	public void setWard(Boundary ward) {
		this.ward = ward;
	}

	public Integer getColonyId() {
		return colonyId;
	}

	public void setColonyId(Integer colonyId) {
		this.colonyId = colonyId;
	}

	public String getDoorNum() {
		return doorNum;
	}

	public void setDoorNum(String doorNum) {
		this.doorNum = doorNum;
	}

	public Integer getAssUnit() {
		return assUnit;
	}

	public void setAssUnit(Integer assUnit) {
		this.assUnit = assUnit;
	}

	public Boundary getArea() {
		return area;
	}

	public void setArea(Boundary area) {
		this.area = area;
	}

	public Boundary getLocality() {
		return locality;
	}

	public void setLocality(Boundary locality) {
		this.locality = locality;
	}

	public Boundary getStreet() {
		return Street;
	}

	public void setStreet(Boundary street) {
		Street = street;
	}

	public Boundary getFrontBndryStreet() {
		return frontBndryStreet;
	}

	public void setFrontBndryStreet(Boundary frontBndryStreet) {
		this.frontBndryStreet = frontBndryStreet;
	}

	public Boundary getBackBndryStreet() {
		return backBndryStreet;
	}

	public void setBackBndryStreet(Boundary backBndryStreet) {
		this.backBndryStreet = backBndryStreet;
	}

	public Boundary getLeftBndryStreet() {
		return leftBndryStreet;
	}

	public void setLeftBndryStreet(Boundary leftBndryStreet) {
		this.leftBndryStreet = leftBndryStreet;
	}

	public Boundary getRightBndryStreet() {
		return rightBndryStreet;
	}

	public void setRightBndryStreet(Boundary rightBndryStreet) {
		this.rightBndryStreet = rightBndryStreet;
	}

	public Boundary getBndryStreetForDmdCalc() {
		return bndryStreetForDmdCalc;
	}

	public void setBndryStreetForDmdCalc(Boundary bndryStreetForDmdCalc) {
		this.bndryStreetForDmdCalc = bndryStreetForDmdCalc;
	}

	public String getNorthBoundary() {
		return northBoundary;
	}

	public void setNorthBoundary(String northBoundary) {
		this.northBoundary = northBoundary;
	}

	public String getSouthBoundary() {
		return southBoundary;
	}

	public void setSouthBoundary(String southBoundary) {
		this.southBoundary = southBoundary;
	}

	public String getEastBoundary() {
		return eastBoundary;
	}

	public void setEastBoundary(String eastBoundary) {
		this.eastBoundary = eastBoundary;
	}

	public String getWestBoundary() {
		return westBoundary;
	}

	public void setWestBoundary(String westBoundary) {
		this.westBoundary = westBoundary;
	}

	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr.append("Id: ").append(getId()).append("|BasicProperty: ");
		objStr = (getBasicProperty() != null) ? objStr.append(getBasicProperty().getUpicNo()) : objStr.append("");
		objStr = (getZone() != null) ? objStr.append("|Zone: ").append(getZone().getName()) : objStr.append("");
		objStr = (getWard() != null) ? objStr.append("|Ward: ").append(getWard().getName()) : objStr.append("");
		objStr.append("|Colony: ").append(getColonyId());
		objStr = (getArea() != null) ? objStr.append("|Area: ").append(getArea().getName()) : objStr.append("");
		objStr = (getLocality() != null) ? objStr.append("|Locality: ").append(getLocality().getName()) : objStr
				.append("");
		objStr = (getStreet() != null) ? objStr.append("|Street: ").append(getStreet().getName()) : objStr.append("");

		return objStr.toString();
	}
}
