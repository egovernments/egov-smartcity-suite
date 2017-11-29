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


public class BaseRegisterResult {

    private String assessmentNo;
    private String ownerName;
    private String doorNO;
    private String natureOfUsage;
    private BigDecimal propertyTax;
    private BigDecimal libraryCessTax;
    private BigDecimal eduCessTax;
    private BigDecimal penaltyFines;
    private BigDecimal currTotal;
    private BigDecimal arrearPropertyTax;
    private BigDecimal arrearLibraryTax;
    private BigDecimal arrearEduCess;
    private BigDecimal arrearPenaltyFines;
    private BigDecimal arrearTotal;
    private String isExempted;
    private String courtCase;
    private String arrearPeriod;
    private String propertyUsage;
    private String classificationOfBuilding;
    private BigDecimal plinthArea;
    private String propertyType;
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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getDoorNO() {
        return doorNO;
    }

    public void setDoorNO(String doorNO) {
        this.doorNO = doorNO;
    }

    public BigDecimal getLibraryCessTax() {
        return libraryCessTax;
    }

    public void setLibraryCessTax(BigDecimal libraryCessTax) {
        this.libraryCessTax = libraryCessTax;
    }

    public BigDecimal getEduCessTax() {
        return eduCessTax;
    }

    public void setEduCessTax(BigDecimal eduCessTax) {
        this.eduCessTax = eduCessTax;
    }

    public BigDecimal getPenaltyFines() {
        return penaltyFines;
    }

    public void setPenaltyFines(BigDecimal penaltyFines) {
        this.penaltyFines = penaltyFines;
    }

    public BigDecimal getArrearPenaltyFines() {
        return arrearPenaltyFines;
    }

    public void setArrearPenaltyFines(BigDecimal arrearPenaltyFines) {
        this.arrearPenaltyFines = arrearPenaltyFines;
    }

    public BigDecimal getCurrTotal() {
        return currTotal;
    }

    public void setCurrTotal(BigDecimal currTotal) {
        this.currTotal = currTotal;
    }

    public BigDecimal getArrearLibraryTax() {
        return arrearLibraryTax;
    }

    public void setArrearLibraryTax(BigDecimal arrearLibraryTax) {
        this.arrearLibraryTax = arrearLibraryTax;
    }

    public BigDecimal getArrearEduCess() {
        return arrearEduCess;
    }

    public void setArrearEduCess(BigDecimal arrearEduCess) {
        this.arrearEduCess = arrearEduCess;
    }

    public BigDecimal getArrearTotal() {
        return arrearTotal;
    }

    public void setArrearTotal(BigDecimal arrearTotal) {
        this.arrearTotal = arrearTotal;
    }

    public String getNatureOfUsage() {
        return natureOfUsage;
    }

    public void setNatureOfUsage(String natureOfUsage) {
        this.natureOfUsage = natureOfUsage;
    }


    public String getIsExempted() {
        return isExempted;
    }

    public void setIsExempted(String isExempted) {
        this.isExempted = isExempted;
    }

    public String getCourtCase() {
        return courtCase;
    }

    public void setCourtCase(String courtCase) {
        this.courtCase = courtCase;
    }

    public String getArrearPeriod() {
        return arrearPeriod;
    }

    public void setArrearPeriod(String arrearPeriod) {
        this.arrearPeriod = arrearPeriod;
    }

    public String getPropertyUsage() {
        return propertyUsage;
    }

    public void setPropertyUsage(String propertyUsage) {
        this.propertyUsage = propertyUsage;
    }

    public String getClassificationOfBuilding() {
        return classificationOfBuilding;
    }

    public void setClassificationOfBuilding(String classificationOfBuilding) {
        this.classificationOfBuilding = classificationOfBuilding;
    }

    public BigDecimal getPlinthArea() {
        return plinthArea;
    }

    public void setPlinthArea(BigDecimal plinthArea) {
        this.plinthArea = plinthArea;
    }

    public BigDecimal getPropertyTax() {
        return propertyTax;
    }

    public void setPropertyTax(BigDecimal propertyTax) {
        this.propertyTax = propertyTax;
    }

    public BigDecimal getArrearPropertyTax() {
        return arrearPropertyTax;
    }

    public void setArrearPropertyTax(BigDecimal arrearPropertyTax) {
        this.arrearPropertyTax = arrearPropertyTax;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
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
