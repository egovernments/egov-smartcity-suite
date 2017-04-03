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
package org.egov.bpa.application.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFunction;
import org.egov.commons.Fund;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "EGBPA_MSTR_BPAFEE")
@SequenceGenerator(name = BpaFee.SEQ_BPAFEE, sequenceName = BpaFee.SEQ_BPAFEE, allocationSize = 1)
public class BpaFee extends AbstractAuditable {

    private static final long serialVersionUID = 3078684328383202788L;
    public static final String SEQ_BPAFEE = "SEQ_EGBPA_MSTR_BPAFEE";

    @Id
    @GeneratedValue(generator = SEQ_BPAFEE, strategy = GenerationType.SEQUENCE)
    private Long id;
    @JoinColumn(name = "glcode")
    @ManyToOne(fetch = FetchType.LAZY)
    private CChartOfAccounts glcode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "function")
    private CFunction function;
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "servicetype")
    private ServiceType serviceType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fund")
    private Fund fund;
    @NotNull
    @Length(min = 1, max = 128)
    private String feeType;
    @NotNull
    @Length(min = 1, max = 128)
    @Column(name = "code", unique = true)
    private String code;
    @NotNull
    @Length(min = 1, max = 256)
    private String description;
    @NotNull
    private Boolean isFixedAmount;
    @NotNull
    private Boolean isActive;
    @NotNull
    private Boolean isMandatory;
    @Transient
    private BigDecimal feeAmount;
    @Transient
    private Long demandDetailId;
    @NotNull
    @Length(min = 1, max = 256)
    private String feeDescriptionLocal;
    private Long orderNumber;
    @NotNull
    private Boolean isPlanningPermitFee;
    @Length(min = 1, max = 128)
    private String feeGroup;

    @OneToMany(mappedBy = "bpafee", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<BpaFeeDetail> feeDetail = new ArrayList<BpaFeeDetail>(0);

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public CChartOfAccounts getGlcode() {
        return glcode;
    }

    public void setGlcode(final CChartOfAccounts glcode) {
        this.glcode = glcode;
    }

    public CFunction getFunction() {
        return function;
    }

    public void setFunction(final CFunction function) {
        this.function = function;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(final ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(final Fund fund) {
        this.fund = fund;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(final String feeType) {
        this.feeType = feeType;
    }

    public Boolean getIsFixedAmount() {
        return isFixedAmount;
    }

    public void setIsFixedAmount(final Boolean isFixedAmount) {
        this.isFixedAmount = isFixedAmount;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(final Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(final Boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(final BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public Long getDemandDetailId() {
        return demandDetailId;
    }

    public void setDemandDetailId(final Long demandDetailId) {
        this.demandDetailId = demandDetailId;
    }

    public String getFeeDescriptionLocal() {
        return feeDescriptionLocal;
    }

    public void setFeeDescriptionLocal(final String feeDescriptionLocal) {
        this.feeDescriptionLocal = feeDescriptionLocal;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(final Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Boolean getIsPlanningPermitFee() {
        return isPlanningPermitFee;
    }

    public void setIsPlanningPermitFee(final Boolean isPlanningPermitFee) {
        this.isPlanningPermitFee = isPlanningPermitFee;
    }

    public String getFeeGroup() {
        return feeGroup;
    }

    public void setFeeGroup(final String feeGroup) {
        this.feeGroup = feeGroup;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public List<BpaFeeDetail> getFeeDetail() {
        return feeDetail;
    }

}