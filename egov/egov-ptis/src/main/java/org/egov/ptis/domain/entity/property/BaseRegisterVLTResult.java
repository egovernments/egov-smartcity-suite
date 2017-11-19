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

package org.egov.ptis.domain.entity.property;

import java.math.BigDecimal;

public class BaseRegisterVLTResult {

    private String assessmentNo;
    private String oldAssessmentNo;
    private BigDecimal sitalArea;
    private String ward;
    private String ownerName;
    private String surveyNo;
    private BigDecimal taxationRate;
    private BigDecimal marketValue;
    private BigDecimal documentValue;
    private BigDecimal higherValueForImposedtax;
    private String isExempted;
    private BigDecimal propertyTaxFirstHlf;
    private BigDecimal libraryCessTaxFirstHlf;
    private BigDecimal propertyTaxSecondHlf;
    private BigDecimal libraryCessTaxSecondHlf;
    private BigDecimal currTotal;
    private BigDecimal penaltyFines;
    private String arrearPeriod;
    private BigDecimal arrearPropertyTax;
    private BigDecimal arrearLibraryTax;
    private BigDecimal arrearPenaltyFines;
    private BigDecimal arrearTotal;
    private BigDecimal arrearColl;
    private BigDecimal currentColl;
    private BigDecimal totalColl;
    private boolean exemptedCase;

    public String getAssessmentNo() {
        return assessmentNo;
    }

    public void setAssessmentNo(String assessmentNo) {
        this.assessmentNo = assessmentNo;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getSurveyNo() {
        return surveyNo;
    }

    public void setSurveyNo(String surveyNo) {
        this.surveyNo = surveyNo;
    }

    public BigDecimal getTaxationRate() {
        return taxationRate;
    }

    public void setTaxationRate(BigDecimal taxationRate) {
        this.taxationRate = taxationRate;
    }

    public BigDecimal getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(BigDecimal marketValue) {
        this.marketValue = marketValue;
    }

    public BigDecimal getDocumentValue() {
        return documentValue;
    }

    public void setDocumentValue(BigDecimal documentValue) {
        this.documentValue = documentValue;
    }

    public BigDecimal getHigherValueForImposedtax() {
        return higherValueForImposedtax;
    }

    public void setHigherValueForImposedtax(BigDecimal higherValueForImposedtax) {
        this.higherValueForImposedtax = higherValueForImposedtax;
    }

    public BigDecimal getPropertyTaxFirstHlf() {
        return propertyTaxFirstHlf;
    }

    public void setPropertyTaxFirstHlf(BigDecimal propertyTaxFirstHlf) {
        this.propertyTaxFirstHlf = propertyTaxFirstHlf;
    }

    public BigDecimal getLibraryCessTaxFirstHlf() {
        return libraryCessTaxFirstHlf;
    }

    public void setLibraryCessTaxFirstHlf(BigDecimal libraryCessTaxFirstHlf) {
        this.libraryCessTaxFirstHlf = libraryCessTaxFirstHlf;
    }

    public BigDecimal getPropertyTaxSecondHlf() {
        return propertyTaxSecondHlf;
    }

    public void setPropertyTaxSecondHlf(BigDecimal propertyTaxSecondHlf) {
        this.propertyTaxSecondHlf = propertyTaxSecondHlf;
    }

    public BigDecimal getLibraryCessTaxSecondHlf() {
        return libraryCessTaxSecondHlf;
    }

    public void setLibraryCessTaxSecondHlf(BigDecimal libraryCessTaxSecondHlf) {
        this.libraryCessTaxSecondHlf = libraryCessTaxSecondHlf;
    }

    public BigDecimal getCurrTotal() {
        return currTotal;
    }

    public void setCurrTotal(BigDecimal currTotal) {
        this.currTotal = currTotal;
    }

    public BigDecimal getPenaltyFines() {
        return penaltyFines;
    }

    public void setPenaltyFines(BigDecimal penaltyFines) {
        this.penaltyFines = penaltyFines;
    }

    public String getArrearPeriod() {
        return arrearPeriod;
    }

    public void setArrearPeriod(String arrearPeriod) {
        this.arrearPeriod = arrearPeriod;
    }

    public BigDecimal getArrearPropertyTax() {
        return arrearPropertyTax;
    }

    public void setArrearPropertyTax(BigDecimal arrearPropertyTax) {
        this.arrearPropertyTax = arrearPropertyTax;
    }

    public BigDecimal getArrearLibraryTax() {
        return arrearLibraryTax;
    }

    public void setArrearLibraryTax(BigDecimal arrearLibraryTax) {
        this.arrearLibraryTax = arrearLibraryTax;
    }

    public BigDecimal getArrearPenaltyFines() {
        return arrearPenaltyFines;
    }

    public void setArrearPenaltyFines(BigDecimal arrearPenaltyFines) {
        this.arrearPenaltyFines = arrearPenaltyFines;
    }

    public BigDecimal getArrearTotal() {
        return arrearTotal;
    }

    public void setArrearTotal(BigDecimal arrearTotal) {
        this.arrearTotal = arrearTotal;
    }

    public String getOldAssessmentNo() {
        return oldAssessmentNo;
    }

    public void setOldAssessmentNo(String oldAssessmentNo) {
        this.oldAssessmentNo = oldAssessmentNo;
    }

    public BigDecimal getSitalArea() {
        return sitalArea;
    }

    public void setSitalArea(BigDecimal sitalArea) {
        this.sitalArea = sitalArea;
    }

    public String getIsExempted() {
        return isExempted;
    }

    public void setIsExempted(String isExempted) {
        this.isExempted = isExempted;
    }

    public BigDecimal getArrearColl() {
        return arrearColl;
    }

    public void setArrearColl(BigDecimal arrearColl) {
        this.arrearColl = arrearColl;
    }

    public BigDecimal getCurrentColl() {
        return currentColl;
    }

    public void setCurrentColl(BigDecimal currentColl) {
        this.currentColl = currentColl;
    }

    public BigDecimal getTotalColl() {
        return totalColl;
    }

    public void setTotalColl(BigDecimal totalColl) {
        this.totalColl = totalColl;
    }

    public boolean isExemptedCase() {
        return exemptedCase;
    }

    public void setExemptedCase(boolean exemptedCase) {
        this.exemptedCase = exemptedCase;
    }

}
