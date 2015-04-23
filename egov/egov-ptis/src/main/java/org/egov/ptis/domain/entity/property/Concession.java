/*
 * Concession.java Created on Oct 20, 2005

 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.ptis.domain.entity.property;

import java.math.BigDecimal;

/**
 * TODO Brief Description of the purpose of the class/interface
 * 
 * @author Gayathri Joshi
 * @version 1.00
 * @see
 * @see
 * @since 1.00
 */

public class Concession {

	public Concession() {
		super();
	}

	private String reason = null;
	private Float percentage = null;
	private BigDecimal concessedAmount = null;

	/**
	 * @return Returns the concessedAmount.
	 */
	public BigDecimal getConcessedAmount() {
		return concessedAmount;
	}

	/**
	 * @param concessedAmount
	 *            The concessedAmount to set.
	 */
	public void setConcessedAmount(BigDecimal concessedAmount) {
		this.concessedAmount = concessedAmount;
	}

	/**
	 * @return Returns the percentage.
	 */
	public Float getPercentage() {
		return percentage;
	}

	/**
	 * @param percentage
	 *            The percentage to set.
	 */
	public void setPercentage(Float percentage) {
		this.percentage = percentage;
	}

	/**
	 * @return Returns the reason.
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason
	 *            The reason to set.
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr.append("Reason: ").append(getReason()).append("|Percentage: ").append(getPercentage()).append(
				"|Amount: ").append(getConcessedAmount());

		return objStr.toString();
	}

}
