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
package org.egov.commons;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name="FUNDSOURCE")
@SequenceGenerator(name = Fundsource.SEQ_FUNDSOURCE, sequenceName = Fundsource.SEQ_FUNDSOURCE, allocationSize = 1)
@NamedQuery(name = "getListOfFundSourceForCodes", query = "from Fundsource where code in (:param_0)")
public class Fundsource extends AbstractAuditable{
	
	private static final long serialVersionUID = -5767136187522324679L;
	public static final String SEQ_FUNDSOURCE = "SEQ_FUNDSOURCE";

	@Id
	@GeneratedValue(generator=SEQ_FUNDSOURCE, strategy=GenerationType.SEQUENCE)
	private Long id;

	@ManyToOne
	@JoinColumn(name="PARENTID")
	private Fundsource fundsource;

	@Column(nullable=false, unique=true)
	@Length(max=50)
	private String code;

	@Column(nullable=false, unique=true)
	@Length(max=50)
	private String name;

	@Length(max=50)
	private String type;

	private BigDecimal llevel;

	private boolean isactive;

	private boolean isnotleaf;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FINANCIALINSTID")
	private FinancingInstitution finInstId;

	@Column(name="FUNDING_TYPE")
	@Length(max=50)
	private String fundingType;

	@Column(name="LOAN_PERCENTAGE")
	private Double loanPercentage;

	@Column(name="SOURCE_AMOUNT")
	private BigDecimal sourceAmount;

	@Column(name="RATE_OF_INTEREST")
	private Double rateOfIntrest;

	@Column(name="LOAN_PERIOD")
	private Double loanPeriod;
	
	@Column(name="MORATORIUM_PERIOD")
	private Double moratoriumPeriod;

	@Column(name="REPAYMENT_FREQUENCY")
	@Length(max=15)
	private String repaymentFrequency;

	@Column(name="NO_OF_INSTALLMENT")
	private Integer noOfInstallment;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="BANKACCOUNTID")
	private Bankaccount bankAccountId;

	@Column(name="GOVT_ORDER")
	@Length(max=250)
	private String govtOrder;

	@Column(name="GOVT_DATE")
	private Date govtDate;

	@Column(name="DP_CODE_NUMBER")
	@Length(max=250)
	private String dpCodeNum;

	@Column(name="DP_CODE_RESG")
	@Length(max=250)
	private String dpCodeResistration;

	@Column(name="FIN_INST_LETTER_NUM")
	@Length(max=250)
	private String finInstLetterNum;

	@Column(name="FIN_INST_LETTER_DATE")
	private Date finInstLetterDate;

	@Column(name="FIN_INST_SCHM_NUM")
	@Length(max=250)
	private String finInstSchmNum;

	@Column(name="FIN_INST_SCHM_DATE")
	private Date finInstSchmDate;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SUBSCHEMEID")
	private SubScheme subSchemeId;

	
	public Fundsource getFundsource() {
		return this.fundsource;
	}

	public void setFundsource(Fundsource fundsource) {
		this.fundsource = fundsource;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getLlevel() {
		return this.llevel;
	}

	public void setLlevel(BigDecimal llevel) {
		this.llevel = llevel;
	}

	public boolean isIsactive() {
		return this.isactive;
	}

	public void setIsactive(boolean isactive) {
		this.isactive = isactive;
	}

	public boolean isIsnotleaf() {
		return this.isnotleaf;
	}

	public void setIsnotleaf(boolean isnotleaf) {
		this.isnotleaf = isnotleaf;
	}


	public Double getLoanPercentage() {
		return loanPercentage;
	}

	public void setLoanPercentage(Double loanPercentage) {
		this.loanPercentage = loanPercentage;
	}

	public BigDecimal getSourceAmount() {
		return sourceAmount;
	}

	public void setSourceAmount(BigDecimal sourceAmount) {
		this.sourceAmount = sourceAmount;
	}

	public Double getRateOfIntrest() {
		return rateOfIntrest;
	}

	public void setRateOfIntrest(Double rateOfIntrest) {
		this.rateOfIntrest = rateOfIntrest;
	}

	public Double getLoanPeriod() {
		return loanPeriod;
	}

	public void setLoanPeriod(Double loanPeriod) {
		this.loanPeriod = loanPeriod;
	}

	public Double getMoratoriumPeriod() {
		return moratoriumPeriod;
	}

	public void setMoratoriumPeriod(Double moratoriumPeriod) {
		this.moratoriumPeriod = moratoriumPeriod;
	}

	public String getRepaymentFrequency() {
		return repaymentFrequency;
	}

	public void setRepaymentFrequency(String repaymentFrequency) {
		this.repaymentFrequency = repaymentFrequency;
	}

	public Integer getNoOfInstallment() {
		return noOfInstallment;
	}

	public void setNoOfInstallment(Integer noOfInstallment) {
		this.noOfInstallment = noOfInstallment;
	}

	public Bankaccount getBankAccountId() {
		return bankAccountId;
	}

	public void setBankAccountId(Bankaccount bankAccountId) {
		this.bankAccountId = bankAccountId;
	}

	public String getGovtOrder() {
		return govtOrder;
	}

	public void setGovtOrder(String govtOrder) {
		this.govtOrder = govtOrder;
	}

	public Date getGovtDate() {
		return govtDate;
	}

	public void setGovtDate(Date govtDate) {
		this.govtDate = govtDate;
	}

	public String getDpCodeNum() {
		return dpCodeNum;
	}

	public void setDpCodeNum(String dpCodeNum) {
		this.dpCodeNum = dpCodeNum;
	}

	public String getDpCodeResistration() {
		return dpCodeResistration;
	}

	public void setDpCodeResistration(String dpCodeResistration) {
		this.dpCodeResistration = dpCodeResistration;
	}

	public String getFinInstLetterNum() {
		return finInstLetterNum;
	}

	public void setFinInstLetterNum(String finInstLetterNum) {
		this.finInstLetterNum = finInstLetterNum;
	}

	public Date getFinInstLetterDate() {
		return finInstLetterDate;
	}

	public void setFinInstLetterDate(Date finInstLetterDate) {
		this.finInstLetterDate = finInstLetterDate;
	}

	public String getFinInstSchmNum() {
		return finInstSchmNum;
	}

	public void setFinInstSchmNum(String finInstSchmNum) {
		this.finInstSchmNum = finInstSchmNum;
	}

	public Date getFinInstSchmDate() {
		return finInstSchmDate;
	}

	public void setFinInstSchmDate(Date finInstSchmDate) {
		this.finInstSchmDate = finInstSchmDate;
	}

	public SubScheme getSubSchemeId() {
		return subSchemeId;
	}

	public void setSubSchemeId(SubScheme subSchemeId) {
		this.subSchemeId = subSchemeId;
	}

	public String getFundingType() {
		return fundingType;
	}

	public void setFundingType(String fundingType) {
		this.fundingType = fundingType;
	}

	public FinancingInstitution getFinInstId() {
		return finInstId;
	}

	public void setFinInstId(FinancingInstitution finInstId) {
		this.finInstId = finInstId;
	}

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
