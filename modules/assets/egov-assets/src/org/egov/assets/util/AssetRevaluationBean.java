/**
 * 
 */
package org.egov.assets.util;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author manoranjan
 *
 */
public class AssetRevaluationBean {
	
	private String typeOfChange;
	
	private BigDecimal revalAmt;
	
	private BigDecimal valAfterReval;
	
	private Date revalDate;
	
	private String revaluatedBy;
	
	private String description;

	private Long wrtnOffAccCodeId; 
	
	public String getTypeOfChange() {
		return typeOfChange;
	}

	public BigDecimal getRevalAmt() {
		return revalAmt;
	}

	public Date getRevalDate() {
		return revalDate;
	}

	public String getRevaluatedBy() {
		return revaluatedBy;
	}

	public String getDescription() {
		return description;
	}

	public void setTypeOfChange(String typeOfChange) {
		this.typeOfChange = typeOfChange;
	}

	public void setRevalAmt(BigDecimal revalAmt) {
		this.revalAmt = revalAmt;
	}

	public void setRevalDate(Date revalDate) {
		this.revalDate = revalDate;
	}

	public void setRevaluatedBy(String revaluatedBy) {
		this.revaluatedBy = revaluatedBy;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getWrtnOffAccCodeId() {
		return wrtnOffAccCodeId;
	}

	public void setWrtnOffAccCodeId(Long wrtnOffAccCodeId) {
		this.wrtnOffAccCodeId = wrtnOffAccCodeId;
	}

	public BigDecimal getValAfterReval() {
		return valAfterReval;
	}

	public void setValAfterReval(BigDecimal valAfterReval) {
		this.valAfterReval = valAfterReval;
	}

	

}