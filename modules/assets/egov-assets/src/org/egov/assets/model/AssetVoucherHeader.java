/**
 * 
 */
package org.egov.assets.model;

import java.util.Date;

import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.infstr.models.BaseModel;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.rjbac.dept.DepartmentImpl;

/**
 * @author manoranjan
 *
 */
public class AssetVoucherHeader extends BaseModel{
	
	private static final long serialVersionUID = 1L;
	private long assetRefId;
	private Fund fund;
	private DepartmentImpl department;
	private Scheme scheme;
	private SubScheme subscheme;
	private Functionary functionary;
	private Fundsource fundsource;
	private BoundaryImpl field;
	private CFunction function;
	private Date voucherDate;
	private String description;
	
	public long getAssetRefId() {
		return assetRefId;
	}
	public Fund getFund() {
		return fund;
	}
	public DepartmentImpl getDepartment() {
		return department;
	}
	public Scheme getScheme() {
		return scheme;
	}
	public SubScheme getSubscheme() {
		return subscheme;
	}
	public Functionary getFunctionary() {
		return functionary;
	}
	public Fundsource getFundsource() {
		return fundsource;
	}
	public BoundaryImpl getField() {
		return field;
	}
	public CFunction getFunction() {
		return function;
	}
	public void setAssetRefId(long assetRefId) {
		this.assetRefId = assetRefId;
	}
	public void setFund(Fund fund) {
		this.fund = fund;
	}
	public void setDepartment(DepartmentImpl department) {
		this.department = department;
	}
	public void setScheme(Scheme scheme) {
		this.scheme = scheme;
	}
	public void setSubscheme(SubScheme subscheme) {
		this.subscheme = subscheme;
	}
	public void setFunctionary(Functionary functionary) {
		this.functionary = functionary;
	}
	public void setFundsource(Fundsource fundsource) {
		this.fundsource = fundsource;
	}
	public void setField(BoundaryImpl field) {
		this.field = field;
	}
	public void setFunction(CFunction function) {
		this.function = function;
	}
	public Date getVoucherDate() {
		return voucherDate;
	}
	public String getDescription() {
		return description;
	}
	public void setVoucherDate(Date voucherDate) {
		this.voucherDate = voucherDate;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
