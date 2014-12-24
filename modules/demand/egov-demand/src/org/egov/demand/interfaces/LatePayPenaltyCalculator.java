package org.egov.demand.interfaces;

import java.math.BigDecimal;
import java.util.Date;

public interface LatePayPenaltyCalculator {
	public enum LPPenaltyCalcType {
		SIMPLE, COMPOUND
	};

	BigDecimal getLPPPercentage();

	LPPenaltyCalcType getLPPenaltyCalcType();

	void setPenaltyCalcType(LPPenaltyCalcType penaltyType);

	/**
	 * Calculates the Late pay penalty for given amount b/w given period with
	 * interest rate (interest rate will be taken from getPercentage() API)
	 * 
	 * @param fromDate - from date to calculate interest
	 * @param toDate - to date to calculate interest
	 * @param amount - amount for which interest to be calculated
	 * @return Interest amount.
	 */
	BigDecimal calcLPPenaltyForPeriod(Date fromDate, Date toDate,
			BigDecimal amount);

	/**
	 * Calculates the Late pay penalty for given amount from given date to Till date with
	 * interest rate (interest rate will be taken from getPercentage() API)
	 * 
	 * @param fromDate - from date to calculate interest
	 * @param amount - amount for which interest to be calculated
	 * @return Interest amount.
	 */
	BigDecimal calcPanalty(Date fromDate, BigDecimal amount);

}
