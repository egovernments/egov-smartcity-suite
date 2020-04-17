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

public class YearWiseDCBReponse {
    private String drillDownType;
    private Long count;
    private String ownersName;
    private String doorNo;
    private BigDecimal arrearDemand;
    private BigDecimal arrearPenDemand;
    private BigDecimal arrearTotalDemand;
    private BigDecimal currentDemand;
    private BigDecimal currentPenDemand;
    private BigDecimal currentTotalDemand;
    private BigDecimal totalDemand;
    private BigDecimal arrearCollection;
    private BigDecimal arrearPenCollection;
    private BigDecimal arrearTotalCollection;
    private BigDecimal currentCollection;
    private BigDecimal currentPenCollection;
    private BigDecimal currentTotalCollection;
    private BigDecimal totalCollection;
    private BigDecimal arrearBalance;
    private BigDecimal arrearPenBalance;
    private BigDecimal currentBalance;
    private BigDecimal currentPenBalance;
    private BigDecimal totalBalance;
    private BigDecimal waivedoffAmount;
    private BigDecimal exemptedAmount;
    private BigDecimal arrearCourtVerdict;
    private BigDecimal currentCourtVerdict;
    private BigDecimal arrearPenCourtVerdict;
    private BigDecimal currentPenCourtVerdict;
    private BigDecimal arrearWriteOff;
    private BigDecimal currentWriteOff;
    private BigDecimal arrearPenWriteOff;
    private BigDecimal currentPenWriteOff;


    public String getDrillDownType() {
        return drillDownType;
    }

    public void setDrillDownType(String drillDownType) {
        this.drillDownType = drillDownType;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public BigDecimal getArrearDemand() {
        return arrearDemand;
    }

    public void setArrearDemand(BigDecimal arrearDemand) {
        this.arrearDemand = arrearDemand;
    }

    public BigDecimal getArrearPenDemand() {
        return arrearPenDemand;
    }

    public void setArrearPenDemand(BigDecimal arrearPenDemand) {
        this.arrearPenDemand = arrearPenDemand;
    }

    public BigDecimal getArrearTotalDemand() {
        return arrearTotalDemand;
    }

    public void setArrearTotalDemand(BigDecimal arrearTotalDemand) {
        this.arrearTotalDemand = arrearTotalDemand;
    }

    public BigDecimal getCurrentDemand() {
        return currentDemand;
    }

    public void setCurrentDemand(BigDecimal currentDemand) {
        this.currentDemand = currentDemand;
    }

    public BigDecimal getCurrentPenDemand() {
        return currentPenDemand;
    }

    public void setCurrentPenDemand(BigDecimal currentPenDemand) {
        this.currentPenDemand = currentPenDemand;
    }

    public BigDecimal getCurrentTotalDemand() {
        return currentTotalDemand;
    }

    public void setCurrentTotalDemand(BigDecimal currentTotalDemand) {
        this.currentTotalDemand = currentTotalDemand;
    }

    public BigDecimal getTotalDemand() {
        return totalDemand;
    }

    public void setTotalDemand(BigDecimal totalDemand) {
        this.totalDemand = totalDemand;
    }

    public BigDecimal getArrearCollection() {
        return arrearCollection;
    }

    public void setArrearCollection(BigDecimal arrearCollection) {
        this.arrearCollection = arrearCollection;
    }

    public BigDecimal getArrearPenCollection() {
        return arrearPenCollection;
    }

    public void setArrearPenCollection(BigDecimal arrearPenCollection) {
        this.arrearPenCollection = arrearPenCollection;
    }

    public BigDecimal getArrearTotalCollection() {
        return arrearTotalCollection;
    }

    public void setArrearTotalCollection(BigDecimal arrearTotalCollection) {
        this.arrearTotalCollection = arrearTotalCollection;
    }

    public BigDecimal getCurrentCollection() {
        return currentCollection;
    }

    public void setCurrentCollection(BigDecimal currentCollection) {
        this.currentCollection = currentCollection;
    }

    public BigDecimal getCurrentPenCollection() {
        return currentPenCollection;
    }

    public void setCurrentPenCollection(BigDecimal currentPenCollection) {
        this.currentPenCollection = currentPenCollection;
    }

    public BigDecimal getCurrentTotalCollection() {
        return currentTotalCollection;
    }

    public void setCurrentTotalCollection(BigDecimal currentTotalCollection) {
        this.currentTotalCollection = currentTotalCollection;
    }

    public BigDecimal getTotalCollection() {
        return totalCollection;
    }

    public void setTotalCollection(BigDecimal totalCollection) {
        this.totalCollection = totalCollection;
    }

    public BigDecimal getArrearBalance() {
        return arrearBalance;
    }

    public void setArrearBalance(BigDecimal arrearBalance) {
        this.arrearBalance = arrearBalance;
    }

    public BigDecimal getArrearPenBalance() {
        return arrearPenBalance;
    }

    public void setArrearPenBalance(BigDecimal arrearPenBalance) {
        this.arrearPenBalance = arrearPenBalance;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public BigDecimal getCurrentPenBalance() {
        return currentPenBalance;
    }

    public void setCurrentPenBalance(BigDecimal currentPenBalance) {
        this.currentPenBalance = currentPenBalance;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public String getOwnersName() {
        return ownersName;
    }

    public void setOwnersName(String ownersName) {
        this.ownersName = ownersName;
    }

    public String getDoorNo() {
        return doorNo;
    }

    public void setDoorNo(String doorNo) {
        this.doorNo = doorNo;
    }

    public BigDecimal getWaivedoffAmount() {
        return waivedoffAmount;
    }

    public void setWaivedoffAmount(BigDecimal waivedoffAmount) {
        this.waivedoffAmount = waivedoffAmount;
    }

    public BigDecimal getExemptedAmount() {
        return exemptedAmount;
    }

    public void setExemptedAmount(BigDecimal exemptedAmount) {
        this.exemptedAmount = exemptedAmount;
    }

    public BigDecimal getArrearCourtVerdict() {
        return arrearCourtVerdict;
    }

    public void setArrearCourtVerdict(BigDecimal arrearCourtVerdict) {
        this.arrearCourtVerdict = arrearCourtVerdict;
    }

    public BigDecimal getCurrentCourtVerdict() {
        return currentCourtVerdict;
    }

    public void setCurrentCourtVerdict(BigDecimal currentCourtVerdict) {
        this.currentCourtVerdict = currentCourtVerdict;
    }

    public BigDecimal getArrearPenCourtVerdict() {
        return arrearPenCourtVerdict;
    }

    public void setArrearPenCourtVerdict(BigDecimal arrearPenCourtVerdict) {
        this.arrearPenCourtVerdict = arrearPenCourtVerdict;
    }

    public BigDecimal getCurrentPenCourtVerdict() {
        return currentPenCourtVerdict;
    }

    public void setCurrentPenCourtVerdict(BigDecimal currentPenCourtVerdict) {
        this.currentPenCourtVerdict = currentPenCourtVerdict;
    }

    public BigDecimal getArrearWriteOff() {
        return arrearWriteOff;
    }

    public void setArrearWriteOff(BigDecimal arrearWriteOff) {
        this.arrearWriteOff = arrearWriteOff;
    }

    public BigDecimal getCurrentWriteOff() {
        return currentWriteOff;
    }

    public void setCurrentWriteOff(BigDecimal currentWriteOff) {
        this.currentWriteOff = currentWriteOff;
    }

    public BigDecimal getArrearPenWriteOff() {
        return arrearPenWriteOff;
    }

    public void setArrearPenWriteOff(BigDecimal arrearPenWriteOff) {
        this.arrearPenWriteOff = arrearPenWriteOff;
    }

    public BigDecimal getCurrentPenWriteOff() {
        return currentPenWriteOff;
    }

    public void setCurrentPenWriteOff(BigDecimal currentPenWriteOff) {
        this.currentPenWriteOff = currentPenWriteOff;
    }
}
