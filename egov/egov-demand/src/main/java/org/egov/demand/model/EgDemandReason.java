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

import org.egov.commons.CChartOfAccounts;
import org.egov.commons.Installment;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * EgDemandReason entity.
 *
 * @author MyEclipse Persistence Tools
 */

public class EgDemandReason implements java.io.Serializable {

    private static final long serialVersionUID = -350500622911242114L;
    private Long id;
    private EgDemandReason egDemandReason;
    private EgDemandReasonMaster egDemandReasonMaster;
    private Installment egInstallmentMaster;
    private BigDecimal percentageBasis;
    private Date createDate;
    private Date modifiedDate;
    private Set<EgDemandDetails> egDemandDetails = new HashSet<>(
            0);
    private CChartOfAccounts glcodeId;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(egDemandReasonMaster).append("-").append(egInstallmentMaster);
        return sb.toString();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EgDemandReason getEgDemandReason() {
        return this.egDemandReason;
    }

    public void setEgDemandReason(EgDemandReason egDemandReason) {
        this.egDemandReason = egDemandReason;
    }

    public EgDemandReasonMaster getEgDemandReasonMaster() {
        return this.egDemandReasonMaster;
    }

    public void setEgDemandReasonMaster(
            EgDemandReasonMaster egDemandReasonMaster) {
        this.egDemandReasonMaster = egDemandReasonMaster;
    }

    public Installment getEgInstallmentMaster() {
        return this.egInstallmentMaster;
    }

    public void setEgInstallmentMaster(Installment egInstallmentMaster) {
        this.egInstallmentMaster = egInstallmentMaster;
    }

    public BigDecimal getPercentageBasis() {
        return this.percentageBasis;
    }

    public void setPercentageBasis(BigDecimal percentageBasis) {
        this.percentageBasis = percentageBasis;
    }

    public Set<EgDemandDetails> getEgDemandDetails() {
        return egDemandDetails;
    }

    public void setEgDemandDetails(Set<EgDemandDetails> egDemandDetails) {
        this.egDemandDetails = egDemandDetails;
    }

    public void addEgDemandDetails(EgDemandDetails egDemandDetails) {
        getEgDemandDetails().add(egDemandDetails);
    }

    public void removeEgDemandDetails(EgDemandDetails egDemandDetails) {
        getEgDemandDetails().remove(egDemandDetails);
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

    /**
     * @return Returns if the given Object is equal to PropertyImpl
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (this == obj)
            return true;

        if (!(obj instanceof EgDemandReason))
            return false;

        final EgDemandReason other = (EgDemandReason) obj;

        if (getId() != null || other.getId() != null) {
            if (getId().equals(other.getId())) {
                return true;
            }
        }
        if ((getEgDemandReasonMaster() != null || other
                .getEgDemandReasonMaster() != null)
                && (getEgInstallmentMaster() != null || other
                        .getEgInstallmentMaster() != null)) {
            return getEgDemandReasonMaster().equals(
                    other.getEgDemandReasonMaster())
                    && getEgInstallmentMaster().equals(
                            other.getEgInstallmentMaster());
        } else
            return false;
    }

    /**
     * @return Returns the hashCode
     */
    @Override
    public int hashCode() {
        int hashCode = 0;

        if (getId() != null) {
            hashCode = hashCode + this.getId().hashCode();
        }
        if (getEgDemandReasonMaster() != null) {
            hashCode = hashCode + this.getEgDemandReasonMaster().hashCode();
        }
        if (getEgInstallmentMaster() != null) {
            hashCode = hashCode + this.getEgInstallmentMaster().hashCode();
        }
        return hashCode;
    }

    public CChartOfAccounts getGlcodeId() {
        return glcodeId;
    }

    public void setGlcodeId(CChartOfAccounts glcodeId) {
        this.glcodeId = glcodeId;
    }

}
