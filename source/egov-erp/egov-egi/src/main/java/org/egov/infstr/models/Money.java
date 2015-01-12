/*
 * @(#)Money.java 3.0, 17 Jun, 2013 2:50:26 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.models;

import java.io.Serializable;
import java.text.DecimalFormat;

import javax.validation.constraints.Min;

public class Money implements Serializable {
	private static final long serialVersionUID = 1L;
	@Min(value = 0, message = "non.negative")
	private double value;

	private Money() {
	}

	public Money(double value) {
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String toString() {
		return String.valueOf(value);
	}

	public Money add(Money money) {
		return new Money(value + money.getValue());
	}

	public Money addAll(Money... monies) {
		double v = value;
		for (Money m : monies) {
			v += m.value;
		}
		return new Money(v);
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Money other = (Money) obj;
		if (Double.doubleToLongBits(value) != Double.doubleToLongBits(other.value))
			return false;
		return true;
	}

	public String getFormattedString() {
		double rounded = Math.round(value * 100) / 100.0;
		DecimalFormat formatter = new DecimalFormat("0.00");
		formatter.setDecimalSeparatorAlwaysShown(true);
		return formatter.format(rounded);
	}
}
