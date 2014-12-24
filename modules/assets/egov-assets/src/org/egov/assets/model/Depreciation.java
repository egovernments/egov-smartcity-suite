package org.egov.assets.model;

import java.util.Date;
import org.egov.commons.CFinancialYear;
import org.egov.infstr.models.BaseModel;


/**
 * Depreciation entity.
 * 
 * @author Nilesh
 */

public class Depreciation extends BaseModel {

	// Constructors

	/** default constructor */
	public Depreciation() {
	}
	
	// Fields

	private Long depreciatedValue;
	private Long voucherHeaderId;
	private Date dateOfDepreciation;
	private Date fromDate;
	private Date toDate;
	private CFinancialYear financialYear;
	private Asset asset;
	
	public Long getDepreciatedValue() {
		return depreciatedValue;
	}
	public void setDepreciatedValue(Long depreciatedValue) {
		this.depreciatedValue = depreciatedValue;
	}
	public Long getVoucherHeaderId() {
		return voucherHeaderId;
	}
	public void setVoucherHeaderId(Long voucherHeaderId) {
		this.voucherHeaderId = voucherHeaderId;
	}
	public Date getDateOfDepreciation() {
		return dateOfDepreciation;
	}
	public void setDateOfDepreciation(Date dateOfDepreciation) {
		this.dateOfDepreciation = dateOfDepreciation;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public CFinancialYear getFinancialYear() {
		return financialYear;
	}
	public void setFinancialYear(CFinancialYear financialYear) {
		this.financialYear = financialYear;
	}
	public Asset getAsset() {
		return asset;
	}
	public void setAsset(Asset asset) {
		this.asset = asset;
	}
	
		
}