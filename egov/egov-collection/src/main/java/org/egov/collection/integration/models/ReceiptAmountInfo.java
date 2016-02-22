/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.collection.integration.models;

import java.math.BigDecimal;
import java.util.Date;

public class ReceiptAmountInfo {
    private BigDecimal arrearsAmount =  BigDecimal.ZERO;
    //In case of unauthorized construction, invalid usage etc. 
    private BigDecimal penaltyAmount =  BigDecimal.ZERO;
    private BigDecimal currentInstallmentAmount =  BigDecimal.ZERO;
    private BigDecimal advanceAmount =  BigDecimal.ZERO;
    private BigDecimal latePaymentCharges =  BigDecimal.ZERO;
    private BigDecimal arrearCess =  BigDecimal.ZERO;
    private BigDecimal currentCess =  BigDecimal.ZERO;
    private Date installmentFromDate;
    private Date installmentToDate;
    
    
    public BigDecimal getArrearsAmount() {
        return arrearsAmount;
    }

    /**
     * To set Arrears(If any). 
     * 
     * @param arrearsAmount
     */
    public void setArrearsAmount(final BigDecimal arrearsAmount) {
        this.arrearsAmount = arrearsAmount;
    }

    public BigDecimal getPenaltyAmount() {
        return penaltyAmount;
    }

    /**
     * To set unauthorized construction penalty, 
     * invalid usage etc. 
     * 
     * @param penaltyAmount
     */
    public void setPenaltyAmount(final BigDecimal penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public BigDecimal getCurrentInstallmentAmount() {
        return currentInstallmentAmount;
    }

    /**
     * To set current install amount
     * 
     * @param currentInstallmentAmount
     */
    public void setCurrentInstallmentAmount(final BigDecimal currentInstallmentAmount) {
        this.currentInstallmentAmount = currentInstallmentAmount;
    }

    public BigDecimal getAdvanceAmount() {
        return advanceAmount;
    }

    public void setAdvanceAmount(final BigDecimal advanceAmount) {
        this.advanceAmount = advanceAmount;
    }

    public BigDecimal getLatePaymentCharges() {
        return latePaymentCharges;
    }
    
    /**
     * To set late payment charges.
     * 
     * @param latePaymentCharges
     */
    public void setLatePaymentCharges(BigDecimal latePaymentCharges) {
        this.latePaymentCharges = latePaymentCharges;
    }

    /**
     * @return the arrearCess
     */
    public BigDecimal getArrearCess() {
        return arrearCess;
    }

    /**
     * @param arrearCess the arrearCess to set
     */
    public void setArrearCess(BigDecimal arrearCess) {
        this.arrearCess = arrearCess;
    }

    /**
     * @return the currentCess
     */
    public BigDecimal getCurrentCess() {
        return currentCess;
    }

    /**
     * @param currentCess the currentCess to set
     */
    public void setCurrentCess(BigDecimal currentCess) {
        this.currentCess = currentCess;
    }

    /**
     * @return the installmentFromDate
     */
    public Date getInstallmentFromDate() {
        return installmentFromDate;
    }

    /**
     * @param installmentFromDate the installmentFromDate to set
     */
    public void setInstallmentFromDate(Date installmentFromDate) {
        this.installmentFromDate = installmentFromDate;
    }

    /**
     * @return the installmentToDate
     */
    public Date getInstallmentToDate() {
        return installmentToDate;
    }

    /**
     * @param installmentToDate the installmentToDate to set
     */
    public void setInstallmentToDate(Date installmentToDate) {
        this.installmentToDate = installmentToDate;
    }

}
