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

package org.egov.ptis.bean.dashboard;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

public class DCBDetails {

    private String ulbName = StringUtils.EMPTY;
    private BigDecimal totalAssessments = BigDecimal.ZERO;
    private BigDecimal arrearDemand = BigDecimal.ZERO;
    private BigDecimal arrearPenalty = BigDecimal.ZERO;
    private BigDecimal currentDemand = BigDecimal.ZERO;
    private BigDecimal currentPenalty = BigDecimal.ZERO;
    private BigDecimal totalDemand = BigDecimal.ZERO;
    private BigDecimal adjustment = BigDecimal.ZERO;
    private BigDecimal arrearColl = BigDecimal.ZERO;
    private BigDecimal currentColl = BigDecimal.ZERO;
    private BigDecimal arrearPenaltyColl = BigDecimal.ZERO;
    private BigDecimal currentPenaltyColl = BigDecimal.ZERO;
    private BigDecimal advanceColl = BigDecimal.ZERO;
    private BigDecimal rebate = BigDecimal.ZERO;
    private BigDecimal totalColl = BigDecimal.ZERO;
    private BigDecimal percentage = BigDecimal.ZERO;
    
    public String getUlbName() {
        return ulbName;
    }
    public void setUlbName(String ulbName) {
        this.ulbName = ulbName;
    }
    public BigDecimal getTotalAssessments() {
        return totalAssessments;
    }
    public void setTotalAssessments(BigDecimal totalAssessments) {
        this.totalAssessments = totalAssessments;
    }
    public BigDecimal getTotalDemand() {
        return totalDemand;
    }
    public void setTotalDemand(BigDecimal totalDemand) {
        this.totalDemand = totalDemand;
    }
    public BigDecimal getTotalColl() {
        return totalColl;
    }
    public void setTotalColl(BigDecimal totalColl) {
        this.totalColl = totalColl;
    }
    public BigDecimal getArrearDemand() {
        return arrearDemand;
    }
    public void setArrearDemand(BigDecimal arrearDemand) {
        this.arrearDemand = arrearDemand;
    }
    public BigDecimal getArrearPenalty() {
        return arrearPenalty;
    }
    public void setArrearPenalty(BigDecimal arrearPenalty) {
        this.arrearPenalty = arrearPenalty;
    }
    public BigDecimal getCurrentDemand() {
        return currentDemand;
    }
    public void setCurrentDemand(BigDecimal currentDemand) {
        this.currentDemand = currentDemand;
    }
    public BigDecimal getCurrentPenalty() {
        return currentPenalty;
    }
    public void setCurrentPenalty(BigDecimal currentPenalty) {
        this.currentPenalty = currentPenalty;
    }
    public BigDecimal getAdjustment() {
        return adjustment;
    }
    public void setAdjustment(BigDecimal adjustment) {
        this.adjustment = adjustment;
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
    public BigDecimal getArrearPenaltyColl() {
        return arrearPenaltyColl;
    }
    public void setArrearPenaltyColl(BigDecimal arrearPenaltyColl) {
        this.arrearPenaltyColl = arrearPenaltyColl;
    }
    public BigDecimal getCurrentPenaltyColl() {
        return currentPenaltyColl;
    }
    public void setCurrentPenaltyColl(BigDecimal currentPenaltyColl) {
        this.currentPenaltyColl = currentPenaltyColl;
    }
    public BigDecimal getAdvanceColl() {
        return advanceColl;
    }
    public void setAdvanceColl(BigDecimal advanceColl) {
        this.advanceColl = advanceColl;
    }
    public BigDecimal getRebate() {
        return rebate;
    }
    public void setRebate(BigDecimal rebate) {
        this.rebate = rebate;
    }
    public BigDecimal getPercentage() {
        return percentage;
    }
    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

}