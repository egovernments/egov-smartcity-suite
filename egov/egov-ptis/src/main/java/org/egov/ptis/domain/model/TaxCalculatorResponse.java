/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
package org.egov.ptis.domain.model;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

public class TaxCalculatorResponse {

    private String assessmentNo = StringUtils.EMPTY;
    private String referenceId = StringUtils.EMPTY;
    private long zoneNo;
    private BigDecimal existingARV = BigDecimal.ZERO;
    private BigDecimal calculatedARV = BigDecimal.ZERO;
    private BigDecimal existingHalfYearlyTax = BigDecimal.ZERO;
    private BigDecimal newHalfYearlyTax = BigDecimal.ZERO;
    private BigDecimal taxVariance = BigDecimal.ZERO;
    private BigDecimal arvVariance = BigDecimal.ZERO;
    private ErrorDetails errorDetails;

    public String getAssessmentNo() {
        return assessmentNo;
    }

    public void setAssessmentNo(String assessmentNo) {
        this.assessmentNo = assessmentNo;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public long getZoneNo() {
        return zoneNo;
    }

    public void setZoneNo(long zoneNo) {
        this.zoneNo = zoneNo;
    }

    public BigDecimal getExistingARV() {
        return existingARV;
    }

    public void setExistingARV(BigDecimal existingARV) {
        this.existingARV = existingARV;
    }

    public BigDecimal getCalculatedARV() {
        return calculatedARV;
    }

    public void setCalculatedARV(BigDecimal calculatedARV) {
        this.calculatedARV = calculatedARV;
    }

    public BigDecimal getExistingHalfYearlyTax() {
        return existingHalfYearlyTax;
    }

    public void setExistingHalfYearlyTax(BigDecimal existingHalfYearlyTax) {
        this.existingHalfYearlyTax = existingHalfYearlyTax;
    }

    public BigDecimal getNewHalfYearlyTax() {
        return newHalfYearlyTax;
    }

    public void setNewHalfYearlyTax(BigDecimal newHalfYearlyTax) {
        this.newHalfYearlyTax = newHalfYearlyTax;
    }

    public BigDecimal getTaxVariance() {
        return taxVariance;
    }

    public void setTaxVariance(BigDecimal taxVariance) {
        this.taxVariance = taxVariance;
    }

    public BigDecimal getArvVariance() {
        return arvVariance;
    }

    public void setArvVariance(BigDecimal arvVariance) {
        this.arvVariance = arvVariance;
    }

    public ErrorDetails getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(ErrorDetails errorDetails) {
        this.errorDetails = errorDetails;
    }
}
