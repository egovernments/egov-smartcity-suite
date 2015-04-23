package org.egov.ptis.nmc.service;

import java.math.BigDecimal;

public final class Amount {
	private BigDecimal amount;
	private static Amount ZERO = new Amount(BigDecimal.ZERO);

	public Amount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public boolean isZero() {
		return amount.compareTo(BigDecimal.ZERO) == 0;
	}

	public boolean isGreaterThan(BigDecimal bd) {
		return amount.compareTo(bd) > 0;
	}

	public boolean isGreaterThanZero() {
		return isGreaterThan(BigDecimal.ZERO);
	}

	public boolean isGreaterThanOrEqualTo(BigDecimal bd) {
		return amount.compareTo(bd) >= 0;
	}

	public boolean isLessThanOrEqualTo(BigDecimal bd) {
		return amount.compareTo(bd) <= 0;
	}

	public boolean isLessThan(BigDecimal bd) {
		return amount.compareTo(bd) < 0;
	}

	public boolean isGreaterThanOrEqualToZero() {
		return isGreaterThanOrEqualTo(BigDecimal.ZERO);
	}

	public Amount minus(BigDecimal bd) {
		return new Amount(amount.subtract(bd));
	}

	public Amount plus(BigDecimal bd) {
		return new Amount(amount.add(bd));
	}

	public Amount multiply(BigDecimal bd) {
		return new Amount(amount.multiply(bd));
	}

	public Amount divide(BigDecimal bd) {
		return new Amount(amount.divide(bd));
	}

}