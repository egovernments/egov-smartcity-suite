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
package org.egov.wtms.application.rest;

import java.math.BigDecimal;
import java.util.List;

public class WaterTaxDue {
    private String propertyID;
    private BigDecimal currentDemand;
    private BigDecimal currentCollection;
    private BigDecimal arrearDemand;
    private BigDecimal arrearCollection;
    private BigDecimal totalTaxDue;
    private List<String> consumerCode;
    private Integer connectionCount;
    private String errorCode;
    private String errorMessage;
    private Boolean isSuccess;
    private Boolean isInWorkFlow;
    private BigDecimal currentInstDemand;


    public Boolean getIsInWorkFlow() {
        return isInWorkFlow;
    }

    public void setIsInWorkFlow(Boolean isInWorkFlow) {
        this.isInWorkFlow = isInWorkFlow;
    }

    public String getPropertyID() {
        return org.apache.commons.lang.StringUtils.defaultIfEmpty(propertyID, "");
    }

    public void setPropertyID(final String propertyID) {
        this.propertyID = propertyID;
    }

    public BigDecimal getCurrentDemand() {
        return currentDemand != null ? currentDemand : BigDecimal.ZERO;
    }

    public void setCurrentDemand(final BigDecimal currentDemand) {
        this.currentDemand = currentDemand;
    }

    public BigDecimal getCurrentCollection() {
        return currentCollection != null ? currentCollection : BigDecimal.ZERO;
    }

    public void setCurrentCollection(final BigDecimal currentCollection) {
        this.currentCollection = currentCollection;
    }

    public BigDecimal getArrearDemand() {
        return arrearDemand != null ? arrearDemand : BigDecimal.ZERO;
    }

    public void setArrearDemand(final BigDecimal arrearDemand) {
        this.arrearDemand = arrearDemand;
    }

    public BigDecimal getArrearCollection() {
        return arrearCollection != null ? arrearCollection : BigDecimal.ZERO;
    }

    public void setArrearCollection(final BigDecimal arrearCollection) {
        this.arrearCollection = arrearCollection;
    }

    public BigDecimal getTotalTaxDue() {
        return totalTaxDue != null ? totalTaxDue : BigDecimal.ZERO;
    }

    public void setTotalTaxDue(final BigDecimal totalTaxDue) {
        this.totalTaxDue = totalTaxDue;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(final Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public List<String> getConsumerCode() {
        return consumerCode;
    }

    public void setConsumerCode(final List<String> consumerCode) {
        this.consumerCode = consumerCode;
    }

    public Integer getConnectionCount() {
        return connectionCount;
    }

    public void setConnectionCount(final Integer connectionCount) {
        this.connectionCount = connectionCount;
    }

    public String getErrorCode() {
        return org.apache.commons.lang.StringUtils.defaultIfEmpty(errorCode, "");
    }

    public void setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return org.apache.commons.lang.StringUtils.defaultIfEmpty(errorMessage, "");
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public BigDecimal getCurrentInstDemand() {
        return currentInstDemand;
    }

    public void setCurrentInstDemand(BigDecimal currentInstDemand) {
        this.currentInstDemand = currentInstDemand;
    }

}