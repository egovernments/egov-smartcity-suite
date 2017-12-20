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
package org.egov.demand.model;

import org.egov.commons.EgwStatus;
import org.egov.infra.exception.ApplicationRuntimeException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * EgDemandDetails entity.
 *
 * @author Sathish Reddy K
 */

public class EgDemandDetails implements Serializable, Cloneable {

    private static final BigDecimal ONE_PAISA_TOLERANCE_FOR_ADDCOLLECTED = new BigDecimal("0.01");
    private Long id;
    private EgDemandReason egDemandReason;
    private EgwStatus egwStatus;
    private String fileReferenceNo;
    private String remarks;
    private BigDecimal amount = BigDecimal.ZERO;
    private Date modifiedDate;
    private Date createDate;
    private BigDecimal amtCollected = BigDecimal.ZERO;
    private transient Set<EgdmCollectedReceipt> egdmCollectedReceipts = new HashSet<>();
    private BigDecimal amtRebate = BigDecimal.ZERO;
    private EgDemand egDemand;
    private Long version;

    /**
     * Factory method for convenient creation.
     */
    public static EgDemandDetails fromReasonAndAmounts(BigDecimal demandAmount,
                                                       EgDemandReason egDemandReason, BigDecimal collectedAmount) {
        EgDemandDetails dd = new EgDemandDetails();
        dd.setAmount(demandAmount);
        dd.setEgDemandReason(egDemandReason);
        dd.setAmtCollected(collectedAmount);
        dd.setModifiedDate(new Date());
        dd.setCreateDate(new Date());
        return dd;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        EgDemandDetails other = (EgDemandDetails) obj;

        if (id == null) {
            return false;
        }
        if (other.id == null) {
            return false;
        }
        return id.equals(other.id);
    }

    /**
     * Returns a copy that can be associated with another EgDemand. The copy has
     * the same amounts, reason, time stamp and (cloned) receipts if any. (Note:
     * making it public instead of protected to allow any class to use it.)
     */
    @Override
    public Object clone() {
        EgDemandDetails clone = null;
        try {
            clone = (EgDemandDetails) super.clone();
        } catch (CloneNotSupportedException e) {
            // this should never happen
            throw new InternalError(e);
        }

        clone.setId(null);
        clone.setEgwStatus(null);
        clone.setEgdmCollectedReceipts(new HashSet<>());
        return clone;
    }

    /**
     * Returns <code>true</code> if demand exceeds collection,
     * <code>false</code> otherwise.
     */
    public boolean hasOutstandingCollection() {
        return getAmtCollected() == null || getAmount().compareTo(getAmtCollected()) > 0;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EgDemandReason getEgDemandReason() {
        return egDemandReason;
    }

    public void setEgDemandReason(EgDemandReason egDemandReason) {
        this.egDemandReason = egDemandReason;
    }

    public EgwStatus getEgwStatus() {
        return egwStatus;
    }

    public void setEgwStatus(EgwStatus egwStatus) {
        this.egwStatus = egwStatus;
    }

    public String getFileReferenceNo() {
        return this.fileReferenceNo;
    }

    public void setFileReferenceNo(String fileReferenceNo) {
        this.fileReferenceNo = fileReferenceNo;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmtCollected() {
        return amtCollected;
    }

    public void setAmtCollected(BigDecimal amtCollected) {
        this.amtCollected = amtCollected;
    }

    public Set<EgdmCollectedReceipt> getEgdmCollectedReceipts() {
        return egdmCollectedReceipts;
    }

    public void setEgdmCollectedReceipts(Set<EgdmCollectedReceipt> egdmCollectedReceipt) {
        this.egdmCollectedReceipts = egdmCollectedReceipt;
    }

    public void addEgdmCollectedReceipt(EgdmCollectedReceipt egdmCollectedReceipt) {
        getEgdmCollectedReceipts().add(egdmCollectedReceipt);
    }

    public void removeEgdmCollectedReceipt(EgdmCollectedReceipt egdmCollectedReceipt) {
        getEgdmCollectedReceipts().remove(egdmCollectedReceipt);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("").append(amount).append("*").append(amtCollected).append("*").append(
                egDemandReason).append("*").append(amtRebate);
        return sb.toString();
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * Add an amount to the existing collected amount, allowing a "tolerance" of exceeding the balance.
     */
    public void addCollectedWithTolerance(BigDecimal amount, BigDecimal tolerance) {
        BigDecimal collected = getAmtCollected() != null ? getAmtCollected() : BigDecimal.ZERO;
        if (amount.compareTo(getAmount().subtract(collected).add(tolerance)) > 0) {
            throw new ApplicationRuntimeException("Amount being added " + amount + " is greater than " + getAmount() + " - "
                    + collected + " + tolerance " + tolerance + ", for demand detail " + this.toString());
        } else {
            setAmtCollected(collected.add(amount));
        }
    }

    /**
     * Add an amount to the existing collected amount, with zero tolerance i.e. balance cannot be exceeded.
     */
    public void addCollected(BigDecimal amount) {
        addCollectedWithTolerance(amount, BigDecimal.ZERO);
    }

    /**
     * Add an amount to the existing collected amount, with a tolerance of one paisa i.e. balance can be
     * exceeded by 1 paisa. This can be used when split amounts are being calculated by MoneyUtils.allocate(), where
     * amounts may be off by 1 paisa.
     */
    public void addCollectedWithOnePaisaTolerance(BigDecimal amount) {
        addCollectedWithTolerance(amount, ONE_PAISA_TOLERANCE_FOR_ADDCOLLECTED);
    }

    public BigDecimal getAmtRebate() {
        return amtRebate;
    }

    public void setAmtRebate(BigDecimal amtRebate) {
        this.amtRebate = amtRebate;
    }

    public void addRebateAmt(BigDecimal rebateAmt) {
        if (getAmtRebate() != null) {
            setAmtRebate(getAmtRebate().add(rebateAmt != null ? rebateAmt : BigDecimal.ZERO));
        } else {
            setAmtRebate(rebateAmt);
        }
    }

    public EgDemand getEgDemand() {
        return egDemand;
    }

    public void setEgDemand(EgDemand egDemand) {
        this.egDemand = egDemand;
    }

    public BigDecimal getBalance() {
        return getAmount().subtract(getAmtCollected() != null ? getAmtCollected() : BigDecimal.ZERO);

    }

    public Date getInstallmentStartDate() {
        return getEgDemandReason().getEgInstallmentMaster().getFromDate();

    }

    public Date getInstallmentEndDate() {
        return getEgDemandReason().getEgInstallmentMaster().getToDate();
    }

    public String getReasonCategory() {
        return getEgDemandReason().getEgDemandReasonMaster().getEgReasonCategory().getCode();
    }

    public Long getVersion() {
        return version;
    }
}