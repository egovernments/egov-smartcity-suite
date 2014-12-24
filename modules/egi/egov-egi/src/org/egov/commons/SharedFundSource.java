/*
 * @(#)SharedFundSource.java 3.0, 6 Jun, 2013 4:37:23 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons;

import java.math.BigDecimal;

public class SharedFundSource {

	private Long id;

	private Fundsource fundSourceId;

	private SubScheme subSchemeId;

	private BigDecimal amount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SubScheme getSubSchemeId() {
		return subSchemeId;
	}

	public void setSubSchemeId(SubScheme subSchemeId) {
		this.subSchemeId = subSchemeId;
	}

	public Fundsource getFundSourceId() {
		return fundSourceId;
	}

	public void setFundSourceId(Fundsource fundSourceId) {
		this.fundSourceId = fundSourceId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
