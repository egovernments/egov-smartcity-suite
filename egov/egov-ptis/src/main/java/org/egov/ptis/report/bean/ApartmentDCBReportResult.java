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

package org.egov.ptis.report.bean;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ApartmentDCBReportResult {
    private String apartmentName;
    private Integer apartmentId;
    private String basicPropertyId;
    private String assessmentNo;
    private BigDecimal dmndArrearPT = BigDecimal.ZERO;
    private BigDecimal dmndArrearPFT = BigDecimal.ZERO;
    private BigDecimal dmndCurrentPT = BigDecimal.ZERO;
    private BigDecimal dmndCurrentPFT = BigDecimal.ZERO;
    private BigDecimal dmndCurrentVLT = BigDecimal.ZERO;
    private BigDecimal clctnArrearPT = BigDecimal.ZERO;
    private BigDecimal clctnArrearPFT = BigDecimal.ZERO;
    private BigDecimal clctnArrearVLT = BigDecimal.ZERO;
    private BigDecimal clctnCurrentPT = BigDecimal.ZERO;
    private BigDecimal clctnCurrentPFT = BigDecimal.ZERO;
    private BigDecimal clctnCurrentVLT = BigDecimal.ZERO;
    private String houseNo;
    private String ownerName;
    private BigInteger assessmentCount;
	public String getApartmentName() {
		return apartmentName;
	}
	public void setApartmentName(String apartmentName) {
		this.apartmentName = apartmentName;
	}
	
	public Integer getApartmentId() {
		return apartmentId;
	}
	public void setApartmentId(Integer apartmentId) {
		this.apartmentId = apartmentId;
	}
	public String getBasicPropertyId() {
		return basicPropertyId;
	}
	public void setBasicPropertyId(String basicPropertyId) {
		this.basicPropertyId = basicPropertyId;
	}
	public String getAssessmentNo() {
		return assessmentNo;
	}
	public void setAssessmentNo(String assessmentNo) {
		this.assessmentNo = assessmentNo;
	}
	public BigDecimal getDmndArrearPT() {
		return dmndArrearPT;
	}
	public void setDmndArrearPT(BigDecimal dmndArrearPT) {
		this.dmndArrearPT = dmndArrearPT;
	}
	public BigDecimal getDmndArrearPFT() {
		return dmndArrearPFT;
	}
	public void setDmndArrearPFT(BigDecimal dmndArrearPFT) {
		this.dmndArrearPFT = dmndArrearPFT;
	}
	public BigDecimal getDmndCurrentPT() {
		return dmndCurrentPT;
	}
	public void setDmndCurrentPT(BigDecimal dmndCurrentPT) {
		this.dmndCurrentPT = dmndCurrentPT;
	}
	public BigDecimal getDmndCurrentPFT() {
		return dmndCurrentPFT;
	}
	public void setDmndCurrentPFT(BigDecimal dmndCurrentPFT) {
		this.dmndCurrentPFT = dmndCurrentPFT;
	}
	public BigDecimal getDmndCurrentVLT() {
		return dmndCurrentVLT;
	}
	public void setDmndCurrentVLT(BigDecimal dmndCurrentVLT) {
		this.dmndCurrentVLT = dmndCurrentVLT;
	}
	public BigDecimal getClctnArrearPT() {
		return clctnArrearPT;
	}
	public void setClctnArrearPT(BigDecimal clctnArrearPT) {
		this.clctnArrearPT = clctnArrearPT;
	}
	public BigDecimal getClctnArrearPFT() {
		return clctnArrearPFT;
	}
	public void setClctnArrearPFT(BigDecimal clctnArrearPFT) {
		this.clctnArrearPFT = clctnArrearPFT;
	}
	public BigDecimal getClctnArrearVLT() {
		return clctnArrearVLT;
	}
	public void setClctnArrearVLT(BigDecimal clctnArrearVLT) {
		this.clctnArrearVLT = clctnArrearVLT;
	}
	public BigDecimal getClctnCurrentPT() {
		return clctnCurrentPT;
	}
	public void setClctnCurrentPT(BigDecimal clctnCurrentPT) {
		this.clctnCurrentPT = clctnCurrentPT;
	}
	public BigDecimal getClctnCurrentPFT() {
		return clctnCurrentPFT;
	}
	public void setClctnCurrentPFT(BigDecimal clctnCurrentPFT) {
		this.clctnCurrentPFT = clctnCurrentPFT;
	}
	public BigDecimal getClctnCurrentVLT() {
		return clctnCurrentVLT;
	}
	public void setClctnCurrentVLT(BigDecimal clctnCurrentVLT) {
		this.clctnCurrentVLT = clctnCurrentVLT;
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
    public BigDecimal getDmndArrearTotal() {
        return getDmndArrearPT().add(getDmndArrearPFT());
    }
    public BigDecimal getDmndCurrentTotal() {
        return getDmndCurrentPT().add(getDmndCurrentPFT());
    }
    public BigDecimal getTotalDemand() {
        return getDmndArrearTotal().add(getDmndCurrentTotal());
    }
    public BigDecimal getClctnArrearTotal() {
        return getClctnArrearPFT().add(getClctnArrearPT());
    }
    public BigDecimal getClctnCurrentTotal() {
        return getClctnCurrentPFT().add(getClctnCurrentPT());
    }
    public BigDecimal getTotalCollection() {
        return getClctnArrearTotal().add(getClctnCurrentTotal());
    }
    public BigDecimal getBalArrearPT() {
        return getDmndArrearPT().subtract(getClctnArrearPT());
    }
    
    public BigDecimal getBalArrearPFT() {
        return getDmndArrearPFT().subtract(getClctnArrearPFT());
    }

    public BigDecimal getBalCurrentPT() {
        return getDmndCurrentPT().subtract(getClctnCurrentPT());
    }
    
    public BigDecimal getBalCurrentPFT() {
        return getDmndCurrentPFT().subtract(getClctnCurrentPFT());
    }

    public BigDecimal getTotalPTBalance() {
        return getBalArrearPT().add(getBalArrearPFT()).add(getBalCurrentPT()).add(getBalCurrentPFT());
    }
}