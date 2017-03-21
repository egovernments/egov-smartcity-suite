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
package org.egov.demand.model;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.commons.Installment;

/**
 * EgBillDetails entity.
 *
 * @author MyEclipse Persistence Tools
 */

public class EgBillDetails implements java.io.Serializable, Comparable<EgBillDetails> {

    // Fields

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Long id;
    private EgBill egBill;
    private Date createDate;
    private Date modifiedDate;
    private String glcode;
    private BigDecimal collectedAmount;
    private Integer orderNo;
    private String functionCode;
    private BigDecimal crAmount;
    private BigDecimal drAmount;
    private String description;
    private Installment egInstallmentMaster;
    private Integer additionalFlag;
    private EgDemandReason egDemandReason;
    private String purpose;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EgBillDetails [glcode=").append(glcode).append(", collectedAmount=").append(collectedAmount)
                .append(", orderNo=").append(orderNo).append(", functionCode=").append(functionCode)
                .append(", crAmount=").append(crAmount).append(", drAmount=").append(drAmount).append(", description=")
                .append(description).append(", additionalFlag=").append(additionalFlag).append(", egDemandReason=")
                .append(egDemandReason).append(purpose).append("]");
        return builder.toString();
    }

    /**
     * The "orderNo" field is used as the key to sort bill details.
     */
    @Override
    public int compareTo(EgBillDetails other) {
        return this.orderNo.compareTo(other.orderNo);
    }

    /**
     * Returns the difference between the CR and DR amount.
     */
    public BigDecimal balance() {
        return crAmount.subtract(drAmount);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EgBill getEgBill() {
        return this.egBill;
    }

    public void setEgBill(EgBill egBill) {
        this.egBill = egBill;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getGlcode() {
        return glcode;
    }

    public void setGlcode(String glcode) {
        this.glcode = glcode;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public BigDecimal getCrAmount() {
        return crAmount;
    }

    public void setCrAmount(BigDecimal crAmount) {
        this.crAmount = crAmount;
    }

    public BigDecimal getDrAmount() {
        return drAmount;
    }

    public void setDrAmount(BigDecimal drAmount) {
        this.drAmount = drAmount;
    }

    public BigDecimal getCollectedAmount() {
        return collectedAmount;
    }

    public void setCollectedAmount(BigDecimal collectedAmount) {
        this.collectedAmount = collectedAmount;
    }

    public Installment getEgInstallmentMaster() {
        return egInstallmentMaster;
    }

    public void setEgInstallmentMaster(Installment egInstallmentMaster) {
        this.egInstallmentMaster = egInstallmentMaster;
    }

    public Integer getAdditionalFlag() {
        return additionalFlag;
    }

    public void setAdditionalFlag(Integer additionalFlag) {
        this.additionalFlag = additionalFlag;
    }

    public EgDemandReason getEgDemandReason() {
        return egDemandReason;
    }

    public void setEgDemandReason(EgDemandReason egDemandReason) {
        this.egDemandReason = egDemandReason;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Date getInstallmentStartDate() {
        return getEgDemandReason().getEgInstallmentMaster().getFromDate();

    }

    public Date getInstallmentEndDate() {
        return getEgDemandReason().getEgInstallmentMaster().getToDate();
    }
}