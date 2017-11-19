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

package org.egov.ptis.actions.reports;

import java.math.BigDecimal;
import java.math.BigInteger;

public class DCBReportResult {

    private String boundaryName;
    private Integer boundaryId;
    private String basicPropertyId;
    private String assessmentNo;
    private BigDecimal dmnd_arrearPT = BigDecimal.ZERO; // GeneralTax
    private BigDecimal dmnd_arrearLC = BigDecimal.ZERO; // LibCessTax
    private BigDecimal dmnd_arrearEC = BigDecimal.ZERO; // EduCessTax
    private BigDecimal dmnd_arrearUPT = BigDecimal.ZERO; // UnauthPenaltyTax
    private BigDecimal dmnd_arrearPFT = BigDecimal.ZERO; // PenaltyFinesTax
    private BigDecimal dmnd_arrearST = BigDecimal.ZERO; // SewarageTax
    private BigDecimal dmnd_arrearVLT = BigDecimal.ZERO; // VacantLandTax
    private BigDecimal dmnd_arrearPSCT = BigDecimal.ZERO; // PubSerChrgTax
    private BigDecimal dmnd_currentPT = BigDecimal.ZERO;
    private BigDecimal dmnd_currentLC = BigDecimal.ZERO;
    private BigDecimal dmnd_currentEC = BigDecimal.ZERO;
    private BigDecimal dmnd_currentUPT = BigDecimal.ZERO;
    private BigDecimal dmnd_currentPFT = BigDecimal.ZERO;
    private BigDecimal dmnd_currentST = BigDecimal.ZERO;
    private BigDecimal dmnd_currentVLT = BigDecimal.ZERO;
    private BigDecimal dmnd_currentPSCT = BigDecimal.ZERO;
    private BigDecimal clctn_arrearPT = BigDecimal.ZERO;
    private BigDecimal clctn_arrearLC = BigDecimal.ZERO;
    private BigDecimal clctn_arrearEC = BigDecimal.ZERO;
    private BigDecimal clctn_arrearUPT = BigDecimal.ZERO;
    private BigDecimal clctn_arrearPFT = BigDecimal.ZERO;
    private BigDecimal clctn_arrearST = BigDecimal.ZERO;
    private BigDecimal clctn_arrearVLT = BigDecimal.ZERO;
    private BigDecimal clctn_arrearPSCT = BigDecimal.ZERO;
    private BigDecimal clctn_currentPT = BigDecimal.ZERO;
    private BigDecimal clctn_currentLC = BigDecimal.ZERO;
    private BigDecimal clctn_currentEC = BigDecimal.ZERO;
    private BigDecimal clctn_currentUPT = BigDecimal.ZERO;
    private BigDecimal clctn_currentPFT = BigDecimal.ZERO;
    private BigDecimal clctn_currentST = BigDecimal.ZERO;
    private BigDecimal clctn_currentVLT = BigDecimal.ZERO;
    private BigDecimal clctn_currentPSCT = BigDecimal.ZERO;
    private String houseNo;
    private String ownerName;
    private BigInteger assessmentCount;

    public String getBoundaryName() {
        return boundaryName;
    }

    public void setBoundaryName(final String boundaryName) { 
        this.boundaryName = boundaryName;
    }

    public BigDecimal getDmnd_arrearPT() {
        return dmnd_arrearPT;
    }

    public void setDmnd_arrearPT(final BigDecimal dmnd_arrearPT) {
        this.dmnd_arrearPT = dmnd_arrearPT;
    }

    public BigDecimal getDmnd_arrearLC() {
        return dmnd_arrearLC;
    }

    public void setDmnd_arrearLC(final BigDecimal dmnd_arrearLC) {
        this.dmnd_arrearLC = dmnd_arrearLC;
    }

    public BigDecimal getDmnd_arrearTotal() {
        return getDmnd_arrearPT().add(getDmnd_arrearPFT());
    }

    public BigDecimal getDmnd_currentPT() {
        return dmnd_currentPT;
    }

    public void setDmnd_currentPT(final BigDecimal dmnd_currentPT) {
        this.dmnd_currentPT = dmnd_currentPT;
    }

    public BigDecimal getDmnd_currentLC() {
        return dmnd_currentLC;
    }

    public void setDmnd_currentLC(final BigDecimal dmnd_currentLC) {
        this.dmnd_currentLC = dmnd_currentLC;
    }

    public BigDecimal getDmnd_currentTotal() {
        return getDmnd_currentPT().add(getDmnd_currentPFT());
    }

    public BigDecimal getTotalDemand() {
        return getDmnd_arrearTotal().add(getDmnd_currentTotal());
    }

    public BigDecimal getClctn_arrearPT() {
        return clctn_arrearPT;
    }

    public void setClctn_arrearPT(final BigDecimal clctn_arrearPT) {
        this.clctn_arrearPT = clctn_arrearPT;
    }

    public BigDecimal getClctn_arrearLC() {
        return clctn_arrearLC;
    }

    public void setClctn_arrearLC(final BigDecimal clctn_arrearLC) {
        this.clctn_arrearLC = clctn_arrearLC;
    }

    public BigDecimal getClctn_arrearTotal() {
        return getClctn_arrearPFT().add(getClctn_arrearPT());
    }

    public BigDecimal getClctn_currentPT() {
        return clctn_currentPT;
    }

    public void setClctn_currentPT(final BigDecimal clctn_currentPT) {
        this.clctn_currentPT = clctn_currentPT;
    }

    public BigDecimal getClctn_currentLC() {
        return clctn_currentLC;
    }

    public void setClctn_currentLC(final BigDecimal clctn_currentLC) {
        this.clctn_currentLC = clctn_currentLC;
    }

    public BigDecimal getClctn_currentTotal() {
        return getClctn_currentPFT().add(getClctn_currentPT());
    }

    public BigDecimal getTotalCollection() {
        return getClctn_arrearTotal().add(getClctn_currentTotal());
    }

    public BigDecimal getBal_arrearPT() {
        return getDmnd_arrearPT().subtract(getClctn_arrearPT());
    }
    
    public BigDecimal getBal_arrearPFT() {
        return getDmnd_arrearPFT().subtract(getClctn_arrearPFT());
    }

    public BigDecimal getBal_currentPT() {
        return getDmnd_currentPT().subtract(getClctn_currentPT());
    }
    
    public BigDecimal getBal_currentPFT() {
        return getDmnd_currentPFT().subtract(getClctn_currentPFT());
    }

    public BigDecimal getTotalPTBalance() {
        return getBal_arrearPT().add(getBal_arrearPFT()).add(getBal_currentPT()).add(getBal_currentPFT());
    }

    public BigDecimal getDmnd_arrearEC() {
        return dmnd_arrearEC;
    }

    public void setDmnd_arrearEC(final BigDecimal dmnd_arrearEC) {
        this.dmnd_arrearEC = dmnd_arrearEC;
    }

    public BigDecimal getDmnd_arrearUPT() {
        return dmnd_arrearUPT;
    }

    public void setDmnd_arrearUPT(final BigDecimal dmnd_arrearUPT) {
        this.dmnd_arrearUPT = dmnd_arrearUPT;
    }

    public BigDecimal getDmnd_arrearPFT() {
        return dmnd_arrearPFT;
    }

    public void setDmnd_arrearPFT(final BigDecimal dmnd_arrearPFT) {
        this.dmnd_arrearPFT = dmnd_arrearPFT;
    }

    public BigDecimal getDmnd_arrearST() {
        return dmnd_arrearST;
    }

    public void setDmnd_arrearST(final BigDecimal dmnd_arrearST) {
        this.dmnd_arrearST = dmnd_arrearST;
    }

    public BigDecimal getDmnd_arrearVLT() {
        return dmnd_arrearVLT;
    }

    public void setDmnd_arrearVLT(final BigDecimal dmnd_arrearVLT) {
        this.dmnd_arrearVLT = dmnd_arrearVLT;
    }

    public BigDecimal getDmnd_arrearPSCT() {
        return dmnd_arrearPSCT;
    }

    public void setDmnd_arrearPSCT(final BigDecimal dmnd_arrearPSCT) {
        this.dmnd_arrearPSCT = dmnd_arrearPSCT;
    }

    public BigDecimal getDmnd_currentEC() {
        return dmnd_currentEC;
    }

    public void setDmnd_currentEC(final BigDecimal dmnd_currentEC) {
        this.dmnd_currentEC = dmnd_currentEC;
    }

    public BigDecimal getDmnd_currentUPT() {
        return dmnd_currentUPT;
    }

    public void setDmnd_currentUPT(final BigDecimal dmnd_currentUPT) {
        this.dmnd_currentUPT = dmnd_currentUPT;
    }

    public BigDecimal getDmnd_currentPFT() {
        return dmnd_currentPFT;
    }

    public void setDmnd_currentPFT(final BigDecimal dmnd_currentPFT) {
        this.dmnd_currentPFT = dmnd_currentPFT;
    }

    public BigDecimal getDmnd_currentST() {
        return dmnd_currentST;
    }

    public void setDmnd_currentST(final BigDecimal dmnd_currentST) {
        this.dmnd_currentST = dmnd_currentST;
    }

    public BigDecimal getDmnd_currentVLT() {
        return dmnd_currentVLT;
    }

    public void setDmnd_currentVLT(final BigDecimal dmnd_currentVLT) {
        this.dmnd_currentVLT = dmnd_currentVLT;
    }

    public BigDecimal getDmnd_currentPSCT() {
        return dmnd_currentPSCT;
    }

    public void setDmnd_currentPSCT(final BigDecimal dmnd_currentPSCT) {
        this.dmnd_currentPSCT = dmnd_currentPSCT;
    }

    public BigDecimal getClctn_arrearEC() {
        return clctn_arrearEC;
    }

    public void setClctn_arrearEC(final BigDecimal clctn_arrearEC) {
        this.clctn_arrearEC = clctn_arrearEC;
    }

    public BigDecimal getClctn_arrearUPT() {
        return clctn_arrearUPT;
    }

    public void setClctn_arrearUPT(final BigDecimal clctn_arrearUPT) {
        this.clctn_arrearUPT = clctn_arrearUPT;
    }

    public BigDecimal getClctn_arrearPFT() {
        return clctn_arrearPFT;
    }

    public void setClctn_arrearPFT(final BigDecimal clctn_arrearPFT) {
        this.clctn_arrearPFT = clctn_arrearPFT;
    }

    public BigDecimal getClctn_arrearST() {
        return clctn_arrearST;
    }

    public void setClctn_arrearST(final BigDecimal clctn_arrearST) {
        this.clctn_arrearST = clctn_arrearST;
    }

    public BigDecimal getClctn_arrearVLT() {
        return clctn_arrearVLT;
    }

    public void setClctn_arrearVLT(final BigDecimal clctn_arrearVLT) {
        this.clctn_arrearVLT = clctn_arrearVLT;
    }

    public BigDecimal getClctn_arrearPSCT() {
        return clctn_arrearPSCT;
    }

    public void setClctn_arrearPSCT(final BigDecimal clctn_arrearPSCT) {
        this.clctn_arrearPSCT = clctn_arrearPSCT;
    }

    public BigDecimal getClctn_currentEC() {
        return clctn_currentEC;
    }

    public void setClctn_currentEC(final BigDecimal clctn_currentEC) {
        this.clctn_currentEC = clctn_currentEC;
    }

    public BigDecimal getClctn_currentUPT() {
        return clctn_currentUPT;
    }

    public void setClctn_currentUPT(final BigDecimal clctn_currentUPT) {
        this.clctn_currentUPT = clctn_currentUPT;
    }

    public BigDecimal getClctn_currentPFT() {
        return clctn_currentPFT;
    }

    public void setClctn_currentPFT(final BigDecimal clctn_currentPFT) {
        this.clctn_currentPFT = clctn_currentPFT;
    }

    public BigDecimal getClctn_currentST() {
        return clctn_currentST;
    }

    public void setClctn_currentST(final BigDecimal clctn_currentST) {
        this.clctn_currentST = clctn_currentST;
    }

    public BigDecimal getClctn_currentVLT() {
        return clctn_currentVLT;
    }

    public void setClctn_currentVLT(final BigDecimal clctn_currentVLT) {
        this.clctn_currentVLT = clctn_currentVLT;
    }

    public BigDecimal getClctn_currentPSCT() {
        return clctn_currentPSCT;
    }

    public void setClctn_currentPSCT(final BigDecimal clctn_currentPSCT) {
        this.clctn_currentPSCT = clctn_currentPSCT;
    }

    public String getBasicPropertyId() {
        return basicPropertyId;
    }

    public void setBasicPropertyId(final String basicPropertyId) {
        this.basicPropertyId = basicPropertyId;
    }

    public String getAssessmentNo() {
        return assessmentNo;
    }

    public void setAssessmentNo(final String assessmentNo) {
        this.assessmentNo = assessmentNo;
    }

    public Integer getBoundaryId() {
        return boundaryId;
    }

    public void setBoundaryId(final Integer boundaryId) {
        this.boundaryId = boundaryId;
    }

    public String getHouseNo() {
        return houseNo; 
    }

    public void setHouseNo(String houseNo) {
    	this.houseNo = houseNo;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

	public BigInteger getAssessmentCount() {
		return assessmentCount;
	}

	public void setAssessmentCount(BigInteger assessmentCount) {
		this.assessmentCount = assessmentCount;
	}

}