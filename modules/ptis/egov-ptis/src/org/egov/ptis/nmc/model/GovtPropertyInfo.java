package org.egov.ptis.nmc.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author subhash
 *
 */

public class GovtPropertyInfo {
	
	private String ward;
	private String area;
	private String houseNumber;
	private String propertyAddress;
	private String propertyOwnerName;
	private String parcelId;
	private String indexNo;
	private String propertyType;
	private List<GovtPropertyTaxCalInfo> govtPropTaxInfoList = new ArrayList<GovtPropertyTaxCalInfo>();
	
	public String getWard() {
		return ward;
	}
	public void setWard(String ward) {
		this.ward = ward;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getHouseNumber() {
		return houseNumber;
	}
	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}
	public String getPropertyAddress() {
		return propertyAddress;
	}
	public void setPropertyAddress(String propertyAddress) {
		this.propertyAddress = propertyAddress;
	}
	public String getPropertyOwnerName() {
		return propertyOwnerName;
	}
	public void setPropertyOwnerName(String propertyOwnerName) {
		this.propertyOwnerName = propertyOwnerName;
	}
	public String getParcelId() {
		return parcelId;
	}
	public void setParcelId(String parcelId) {
		this.parcelId = parcelId;
	}
	public String getIndexNo() {
		return indexNo;
	}
	public void setIndexNo(String indexNo) {
		this.indexNo = indexNo;
	}
	public String getPropertyType() {
		return propertyType;
	}
	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}
	public List<GovtPropertyTaxCalInfo> getGovtPropTaxInfoList() {
		return govtPropTaxInfoList;
	}
	public void setGovtPropTaxInfoList(
			List<GovtPropertyTaxCalInfo> govtPropTaxInfoList) {
		this.govtPropTaxInfoList = govtPropTaxInfoList;
	}
	
	public void addGovtPropTaxCalInfo(GovtPropertyTaxCalInfo govtPropTaxCalInfo) {
		this.getGovtPropTaxInfoList().add(govtPropTaxCalInfo);
	}
	
}
