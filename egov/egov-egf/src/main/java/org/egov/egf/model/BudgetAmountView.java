/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.egf.model;

import org.egov.utils.Constants;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Transactional(readOnly = true)
public class BudgetAmountView {

    private BigDecimal previousYearActuals = BigDecimal.ZERO;
    private BigDecimal twopreviousYearActuals = BigDecimal.ZERO;
    private String oldActuals = Constants.ZERO;
    private String reappropriation = Constants.ZERO;
    private BigDecimal currentYearBeActuals = BigDecimal.ZERO;
    private String currentYearBeApproved = Constants.ZERO;
    private String currentYearReApproved = Constants.ZERO;
    private String total = Constants.ZERO;
    private Long id;
    private String LastBEApproved = Constants.ZERO;
    private String lastTotal = Constants.ZERO;

    /**
     * @return the lastBEApproved
     */
    public String getLastBEApproved() {
        return LastBEApproved;
    }

    /**
     * @return the lastTotal
     */
    public String getLastTotal() {
        return lastTotal;
    }

    /**
     * @param lastTotal the lastTotal to set
     */
    public void setLastTotal(final String lastTotal) {
        this.lastTotal = lastTotal;
    }

    /**
     * @param lastBEApproved the lastBEApproved to set
     */
    public void setLastBEApproved(final String lastBEApproved) {
        LastBEApproved = lastBEApproved;
    }

    public BigDecimal getPreviousYearActuals() {
        return previousYearActuals;
    }

    public String getReappropriation() {
        return reappropriation;
    }

    public String getOldActuals() {
        return oldActuals;
    }

    public BigDecimal getCurrentYearBeActuals() {
        return currentYearBeActuals;
    }

    public String getCurrentYearBeApproved() {
        return currentYearBeApproved;
    }

    public String getCurrentYearReApproved() {
        return currentYearReApproved;
    }

    public void setPreviousYearActuals(final BigDecimal previousYearActuals) {
        this.previousYearActuals = previousYearActuals;
    }

    public void setReappropriation(final String reappropriation) {
        this.reappropriation = reappropriation;
    }

    public void setOldActuals(final String oldActuals) {
        this.oldActuals = oldActuals;
    }

    public void setCurrentYearBeActuals(final BigDecimal currentYearBeActuals) {
        this.currentYearBeActuals = currentYearBeActuals;
    }

    public void setCurrentYearBeApproved(final String currentYearBeApproved) {
        this.currentYearBeApproved = currentYearBeApproved;
    }

    public void setCurrentYearReApproved(final String currentYearReApproved) {
        this.currentYearReApproved = currentYearReApproved;
    }

    public void setTotal(final String total) {
        this.total = total;
    }

    public String getTotal() {
        return total;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getTwopreviousYearActuals() {
        return twopreviousYearActuals;
    }

    public void setTwopreviousYearActuals(final BigDecimal twopreviousYearActuals) {
        this.twopreviousYearActuals = twopreviousYearActuals;
    }

    @Override
    public String toString() {
        return new StringBuffer(500).append(id).append("--").append(currentYearBeActuals).append("--")
                .append(previousYearActuals).append("--").append(twopreviousYearActuals).toString();
    }
}
