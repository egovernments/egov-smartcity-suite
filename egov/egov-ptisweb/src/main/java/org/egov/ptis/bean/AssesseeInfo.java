package org.egov.ptis.bean;

import java.math.BigDecimal;
import java.util.List;

/**
 * 
 * @author subhash
 *
 */
public class AssesseeInfo {

	private String indexNo;
	private String houseNo;
	private String ownerName;
	private String propType;
	private String unitNo;
	private BigDecimal alv;
	private List<TaxInfo> taxInfoList;
	
	public String getIndexNo() {
		return indexNo;
	}
	public void setIndexNo(String indexNo) {
		this.indexNo = indexNo;
	}
	public String getHouseNo() {
		return houseNo;
	}
	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getPropType() {
		return propType;
	}
	public void setPropType(String propType) {
		this.propType = propType;
	}

	public BigDecimal getAlv() {
		return alv;
	}
	public void setAlv(BigDecimal alv) {
		this.alv = alv;
	}
	public List<TaxInfo> getTaxInfoList() {
		return taxInfoList;
	}
	public void setTaxInfoList(List<TaxInfo> taxInfoList) {
		this.taxInfoList = taxInfoList;
	}
	public String getUnitNo() {
		return unitNo;
	}
	public void setUnitNo(String unitNo) {
		this.unitNo = unitNo;
	}
}
