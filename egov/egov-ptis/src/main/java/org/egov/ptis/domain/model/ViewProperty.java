package org.egov.ptis.domain.model;

import java.util.Date;

public class ViewProperty {
	private String propertyId;

	private PropertyBoundary propertyBoundary;
	private PropertyOwnerBean propertyOwnerInfo;
	private PropertyTaxBean propertyTaxInfo;

	private String BpaNo;
	private Date BpaDate;
	private String RegdDocNo;
	private Date RegdDocDate;

	private String propertyType;
	private String areaOfPlot;
	private Date dateOfOccupation;

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public PropertyBoundary getPropertyBoundary() {
		return propertyBoundary;
	}

	public void setPropertyBoundary(PropertyBoundary propertyBoundary) {
		this.propertyBoundary = propertyBoundary;
	}

	public PropertyOwnerBean getPropertyOwnerInfo() {
		return propertyOwnerInfo;
	}

	public void setPropertyOwnerInfo(PropertyOwnerBean propertyOwnerInfo) {
		this.propertyOwnerInfo = propertyOwnerInfo;
	}

	public PropertyTaxBean getPropertyTaxInfo() {
		return propertyTaxInfo;
	}

	public void setPropertyTaxInfo(PropertyTaxBean propertyTaxInfo) {
		this.propertyTaxInfo = propertyTaxInfo;
	}

	public String getBpaNo() {
		return BpaNo;
	}

	public void setBpaNo(String bpaNo) {
		BpaNo = bpaNo;
	}

	public Date getBpaDate() {
		return BpaDate;
	}

	public void setBpaDate(Date bpaDate) {
		BpaDate = bpaDate;
	}

	public String getRegdDocNo() {
		return RegdDocNo;
	}

	public void setRegdDocNo(String regdDocNo) {
		RegdDocNo = regdDocNo;
	}

	public Date getRegdDocDate() {
		return RegdDocDate;
	}

	public void setRegdDocDate(Date regdDocDate) {
		RegdDocDate = regdDocDate;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	public String getAreaOfPlot() {
		return areaOfPlot;
	}

	public void setAreaOfPlot(String areaOfPlot) {
		this.areaOfPlot = areaOfPlot;
	}

	public Date getDateOfOccupation() {
		return dateOfOccupation;
	}

	public void setDateOfOccupation(Date dateOfOccupation) {
		this.dateOfOccupation = dateOfOccupation;
	}

}
