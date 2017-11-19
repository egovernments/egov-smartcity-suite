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
package org.egov.commons;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.commons.Bankaccount;
import org.egov.commons.FinancingInstitution;
import org.egov.commons.SubScheme;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "fundsource")
@Unique(id = "id", tableName = "fundsource", fields = { "code", "name" }, columnName = { "code", "name" }, enableDfltMsg = true)
@SequenceGenerator(name = Fundsource.SEQ_FUNDSOURCE, sequenceName = Fundsource.SEQ_FUNDSOURCE, allocationSize = 1)
public class Fundsource extends AbstractAuditable {

    private static final long serialVersionUID = -6601962644148353761L;

    public static final String SEQ_FUNDSOURCE = "SEQ_FUNDSOURCE";

    @Id
    @GeneratedValue(generator = SEQ_FUNDSOURCE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Length(min = 1, max = 25)
    @NotNull
    private String code;

    @Length(min = 1, max = 25)
    @NotNull
    private String name;

    @Length(min = 1, max = 25)
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentid")
    private Fundsource fundsource;

    private BigDecimal llevel;

    private Boolean isactive;

    private Boolean isnotleaf;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "financialinstid")
    private FinancingInstitution finInstId;

    @Length(min = 1, max = 25)
    @Column(name = "funding_type")
    private String fundingType;

    @Column(name = "loan_percentage")
    private Double loanPercentage;

    @Column(name = "source_amount")
    private BigDecimal sourceAmount;

    @Column(name = "rate_of_interest")
    private Double rateOfIntrest;

    @Column(name = "loan_period")
    private Double loanPeriod;

    @Column(name = "moratorium_period")
    private Double moratoriumPeriod;

    @Length(min = 1, max = 25)
    @Column(name = "repayment_frequency")
    private String repaymentFrequency;

    @Column(name = "no_of_installment")
    private Integer noOfInstallment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bankaccountid")
    private Bankaccount bankAccountId;

    @Length(min = 1, max = 25)
    @Column(name = "govt_order")
    private String govtOrder;

    @Column(name = "govt_date")
    private Date govtDate;

    @Length(min = 1, max = 25)
    @Column(name = "dp_code_number")
    private String dpCodeNum;

    @Length(min = 1, max = 25)
    @Column(name = "dp_code_resg")
    private String dpCodeResistration;

    @Length(min = 1, max = 25)
    @Column(name = "fin_inst_letter_num")
    private String finInstLetterNum;

    @Column(name = "fin_inst_letter_date")
    private Date finInstLetterDate;

    @Length(min = 1, max = 25)
    @Column(name = "fin_inst_schm_num")
    private String finInstSchmNum;

    @Column(name = "fin_inst_schm_date")
    private Date finInstSchmDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subschemeid")
    private SubScheme subSchemeId;

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Fundsource getFundsource() {
        return fundsource;
    }

    public BigDecimal getLlevel() {
        return llevel;
    }

    public Boolean getIsactive() {
        return isactive;
    }

    public Boolean getIsnotleaf() {
        return isnotleaf;
    }

    public FinancingInstitution getFinInstId() {
        return finInstId;
    }

    public String getFundingType() {
        return fundingType;
    }

    public Double getLoanPercentage() {
        return loanPercentage;
    }

    public BigDecimal getSourceAmount() {
        return sourceAmount;
    }

    public Double getRateOfIntrest() {
        return rateOfIntrest;
    }

    public Double getLoanPeriod() {
        return loanPeriod;
    }

    public Double getMoratoriumPeriod() {
        return moratoriumPeriod;
    }

    public String getRepaymentFrequency() {
        return repaymentFrequency;
    }

    public Integer getNoOfInstallment() {
        return noOfInstallment;
    }

    public Bankaccount getBankAccountId() {
        return bankAccountId;
    }

    public String getGovtOrder() {
        return govtOrder;
    }

    public Date getGovtDate() {
        return govtDate;
    }

    public String getDpCodeNum() {
        return dpCodeNum;
    }

    public String getDpCodeResistration() {
        return dpCodeResistration;
    }

    public String getFinInstLetterNum() {
        return finInstLetterNum;
    }

    public Date getFinInstLetterDate() {
        return finInstLetterDate;
    }

    public String getFinInstSchmNum() {
        return finInstSchmNum;
    }

    public Date getFinInstSchmDate() {
        return finInstSchmDate;
    }

    public SubScheme getSubSchemeId() {
        return subSchemeId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFundsource(Fundsource fundsource) {
        this.fundsource = fundsource;
    }

    public void setLlevel(BigDecimal llevel) {
        this.llevel = llevel;
    }

    public void setIsactive(Boolean isactive) {
        this.isactive = isactive;
    }

    public void setIsnotleaf(Boolean isnotleaf) {
        this.isnotleaf = isnotleaf;
    }

    public void setFinInstId(FinancingInstitution finInstId) {
        this.finInstId = finInstId;
    }

    public void setFundingType(String fundingType) {
        this.fundingType = fundingType;
    }

    public void setLoanPercentage(Double loanPercentage) {
        this.loanPercentage = loanPercentage;
    }

    public void setSourceAmount(BigDecimal sourceAmount) {
        this.sourceAmount = sourceAmount;
    }

    public void setRateOfIntrest(Double rateOfIntrest) {
        this.rateOfIntrest = rateOfIntrest;
    }

    public void setLoanPeriod(Double loanPeriod) {
        this.loanPeriod = loanPeriod;
    }

    public void setMoratoriumPeriod(Double moratoriumPeriod) {
        this.moratoriumPeriod = moratoriumPeriod;
    }

    public void setRepaymentFrequency(String repaymentFrequency) {
        this.repaymentFrequency = repaymentFrequency;
    }

    public void setNoOfInstallment(Integer noOfInstallment) {
        this.noOfInstallment = noOfInstallment;
    }

    public void setBankAccountId(Bankaccount bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public void setGovtOrder(String govtOrder) {
        this.govtOrder = govtOrder;
    }

    public void setGovtDate(Date govtDate) {
        this.govtDate = govtDate;
    }

    public void setDpCodeNum(String dpCodeNum) {
        this.dpCodeNum = dpCodeNum;
    }

    public void setDpCodeResistration(String dpCodeResistration) {
        this.dpCodeResistration = dpCodeResistration;
    }

    public void setFinInstLetterNum(String finInstLetterNum) {
        this.finInstLetterNum = finInstLetterNum;
    }

    public void setFinInstLetterDate(Date finInstLetterDate) {
        this.finInstLetterDate = finInstLetterDate;
    }

    public void setFinInstSchmNum(String finInstSchmNum) {
        this.finInstSchmNum = finInstSchmNum;
    }

    public void setFinInstSchmDate(Date finInstSchmDate) {
        this.finInstSchmDate = finInstSchmDate;
    }

    public void setSubSchemeId(SubScheme subSchemeId) {
        this.subSchemeId = subSchemeId;
    }

}
