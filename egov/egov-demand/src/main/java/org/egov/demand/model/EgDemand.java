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

import org.egov.commons.Installment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * EgDemand entity.
 *
 * @author MyEclipse Persistence Tools
 */

public class EgDemand implements Serializable, Cloneable {

    private Long id;
    private Installment egInstallmentMaster;
    private BigDecimal baseDemand = BigDecimal.ZERO;
    private String isHistory;
    private Date createDate;
    private Date modifiedDate;
    private Set<EgDemandDetails> egDemandDetails = new HashSet<>(
            0);
    private Set<EgBill> egBills = new HashSet<>(0);
    private BigDecimal amtCollected = BigDecimal.ZERO;
    private Character status;
    private BigDecimal minAmtPayable = BigDecimal.ZERO;
    private BigDecimal amtRebate = BigDecimal.ZERO;
    private Long version;

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
        EgDemand other = (EgDemand) obj;
        if (id != null && other != null && id.equals(other.id)) {
            return true;
        }
        return false;
    }

    /**
     * Returns a copy that can be associated with another billing system entity.
     * The copy has the same amounts, installment, time stamps and (cloned)
     * demand-details if any. It will NOT have a copy of the EgBills of the
     * original demand. (Note: making it public instead of protected to allow
     * any class to use it.)
     */
    @Override
    public Object clone() {
        EgDemand clone = null;
        try {
            clone = (EgDemand) super.clone();
        } catch (CloneNotSupportedException e) {
            // this should never happen
            throw new InternalError(e.toString());
        }
        clone.setId(null);
        clone.setEgBills(new HashSet<EgBill>());
        clone.setEgDemandDetails(new HashSet<EgDemandDetails>());
        for (EgDemandDetails det : egDemandDetails) {
            clone.addEgDemandDetails((EgDemandDetails) det.clone());
        }
        return clone;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public BigDecimal getMinAmtPayable() {
        return minAmtPayable;
    }

    public void setMinAmtPayable(BigDecimal minAmtPayable) {
        this.minAmtPayable = minAmtPayable;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Installment getEgInstallmentMaster() {
        return this.egInstallmentMaster;
    }

    public void setEgInstallmentMaster(Installment egInstallmentMaster) {
        this.egInstallmentMaster = egInstallmentMaster;
    }

    public BigDecimal getBaseDemand() {
        return this.baseDemand;
    }

    public void setBaseDemand(BigDecimal baseDemand) {
        this.baseDemand = baseDemand;
    }

    public void addBaseDemand(BigDecimal amount) {
        setBaseDemand(getBaseDemand().add(amount));
    }

    public String getIsHistory() {
        return this.isHistory;
    }

    public void setIsHistory(String isHistory) {
        this.isHistory = isHistory;
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

    public Set<EgBill> getEgBills() {
        return this.egBills;
    }

    public void setEgBills(Set<EgBill> egBills) {
        this.egBills = egBills;
    }

    public Set<EgDemandDetails> getEgDemandDetails() {
        return egDemandDetails;
    }

    public void setEgDemandDetails(Set<EgDemandDetails> egDemandDetails) {
        this.egDemandDetails = egDemandDetails;
    }

    public void addEgBill(EgBill egBill) {
        getEgBills().add(egBill);
    }

    public void removeEgBill(EgBill egBill) {
        getEgBills().remove(egBill);
    }

    public void addEgDemandDetails(EgDemandDetails egDemandDetails) {
        getEgDemandDetails().add(egDemandDetails);
    }

    public void removeEgDemandDetails(EgDemandDetails egDemandDetails) {
        getEgDemandDetails().remove(egDemandDetails);
    }

    public BigDecimal getAmtCollected() {
        return amtCollected;
    }

    public void setAmtCollected(BigDecimal amtCollected) {
        this.amtCollected = amtCollected;
    }

    /**
     * Adds an amount to the existing collected amount.
     */
    public void addCollected(BigDecimal amount) {
        if (getAmtCollected() != null) {
            setAmtCollected(getAmtCollected().add(
                    amount != null ? amount : BigDecimal.ZERO));
        } else {
            setAmtCollected(amount);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append("|").append(egInstallmentMaster).append("|")
                .append(baseDemand).append("|").append(amtCollected)
                .append("|").append(egDemandDetails).append("|")
                .append(amtRebate);
        return sb.toString();
    }

    public BigDecimal getAmtRebate() {
        return amtRebate;
    }

    public void setAmtRebate(BigDecimal amtRebate) {
        this.amtRebate = amtRebate;
    }

    public void addRebateAmt(BigDecimal rebateAmt) {
        if (getAmtRebate() != null) {
            setAmtRebate(getAmtRebate().add(
                    rebateAmt != null ? rebateAmt : BigDecimal.ZERO));
        } else {
            setAmtRebate(rebateAmt);
        }
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}