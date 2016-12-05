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
package org.egov.works.uploadsor;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.common.entity.UOM;
import org.egov.works.masters.entity.ScheduleCategory;
import org.egov.works.masters.entity.ScheduleOfRate;

public class UploadScheduleOfRate {

    private String sorCode;

    private String sorCategoryCode;

    private String sorDescription;

    private String uomCode;

    private BigDecimal rate;

    private String tempFromDate;

    private String tempToDate;

    private Date fromDate;

    private Date toDate;

    private BigDecimal marketRate;

    private String tempMarketFromDate;

    private String tempMarketToDate;

    private Date marketFromDate;

    private Date marketToDate;

    private String finalStatus;

    private ScheduleOfRate scheduleOfRate;

    private ScheduleCategory scheduleCategory;

    private UOM uom;

    private String errorReason;

    private Boolean createSor;

    private Boolean isToDateNull;

    public String getSorCode() {
        return sorCode;
    }

    public void setSorCode(final String sorCode) {
        this.sorCode = sorCode;
    }

    public String getSorCategoryCode() {
        return sorCategoryCode;
    }

    public void setSorCategoryCode(final String sorCategoryCode) {
        this.sorCategoryCode = sorCategoryCode;
    }

    public String getSorDescription() {
        return sorDescription;
    }

    public void setSorDescription(final String sorDescription) {
        this.sorDescription = sorDescription;
    }

    public String getUomCode() {
        return uomCode;
    }

    public void setUomCode(final String uomCode) {
        this.uomCode = uomCode;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(final BigDecimal rate) {
        this.rate = rate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    public BigDecimal getMarketRate() {
        return marketRate;
    }

    public void setMarketRate(final BigDecimal marketRate) {
        this.marketRate = marketRate;
    }

    public Date getMarketFromDate() {
        return marketFromDate;
    }

    public void setMarketFromDate(final Date marketFromDate) {
        this.marketFromDate = marketFromDate;
    }

    public Date getMarketToDate() {
        return marketToDate;
    }

    public void setMarketToDate(final Date marketToDate) {
        this.marketToDate = marketToDate;
    }

    public String getFinalStatus() {
        return finalStatus;
    }

    public void setFinalStatus(final String finalStatus) {
        this.finalStatus = finalStatus;
    }

    public ScheduleOfRate getScheduleOfRate() {
        return scheduleOfRate;
    }

    public void setScheduleOfRate(final ScheduleOfRate scheduleOfRate) {
        this.scheduleOfRate = scheduleOfRate;
    }

    public ScheduleCategory getScheduleCategory() {
        return scheduleCategory;
    }

    public void setScheduleCategory(final ScheduleCategory scheduleCategory) {
        this.scheduleCategory = scheduleCategory;
    }

    public UOM getUom() {
        return uom;
    }

    public void setUom(final UOM uom) {
        this.uom = uom;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(final String errorReason) {
        this.errorReason = errorReason;
    }

    public Boolean getCreateSor() {
        return createSor;
    }

    public void setCreateSor(final Boolean createSor) {
        this.createSor = createSor;
    }

    public String getTempFromDate() {
        return tempFromDate;
    }

    public void setTempFromDate(final String tempFromDate) {
        this.tempFromDate = tempFromDate;
    }

    public String getTempToDate() {
        return tempToDate;
    }

    public void setTempToDate(final String tempToDate) {
        this.tempToDate = tempToDate;
    }

    public String getTempMarketFromDate() {
        return tempMarketFromDate;
    }

    public void setTempMarketFromDate(final String tempMarketFromDate) {
        this.tempMarketFromDate = tempMarketFromDate;
    }

    public String getTempMarketToDate() {
        return tempMarketToDate;
    }

    public void setTempMarketToDate(final String tempMarketToDate) {
        this.tempMarketToDate = tempMarketToDate;
    }

    public Boolean getIsToDateNull() {
        return isToDateNull;
    }

    public void setIsToDateNull(final Boolean isToDateNull) {
        this.isToDateNull = isToDateNull;
    }

}
