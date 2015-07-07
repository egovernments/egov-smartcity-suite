package org.egov.demand.interfaces;

import java.math.BigDecimal;

public interface RebateCalculator {

    /**
     * Calculates the early pay rebate for given amount
     *
     * @param tax
     *            - amount for which rebate is to be calculated
     * @return rebate amount.
     */
    public BigDecimal calculateEarlyPayRebate(BigDecimal tax);

    /**
     * Checks if Rebate is active or not
     *
     * @return boolean value
     */
    public boolean isEarlyPayRebateActive();
}
