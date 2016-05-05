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
package org.egov.commons;

import org.egov.infra.admin.master.entity.User;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Fundsource {

	private Integer id;

	private Fundsource fundsource;

	private String code;

	private String name;

	private String type;

	private BigDecimal llevel;

	private Boolean isactive;

	private Boolean isnotleaf;

	private FinancingInstitution finInstId;

	private String fundingType;

	private Double loanPercentage;

	private BigDecimal sourceAmount;

	private Double rateOfIntrest;

	private Double loanPeriod;

	private Double moratoriumPeriod;

	private String repaymentFrequency;

	private Integer noOfInstallment;

	private Bankaccount bankAccountId;

	private String govtOrder;

	private Date govtDate;

	private String dpCodeNum;

	private String dpCodeResistration;

	private String finInstLetterNum;

	private Date finInstLetterDate;

	private String finInstSchmNum;

	private Date finInstSchmDate;

	private SubScheme subSchemeId;

	protected User createdBy;
	protected Date created;
	protected BigDecimal modifiedby;
	protected Date lastmodified;

	private Set voucherheaders = new HashSet(0);

	private Set fundsources = new HashSet(0);

	private Set sharedFundSource = new HashSet(0);

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public BigDecimal getModifiedby() {
		return modifiedby;
	}

	public void setModifiedby(BigDecimal modifiedby) {
		this.modifiedby = modifiedby;
	}

	public Date getLastmodified() {
		return lastmodified;
	}

	public void setLastmodified(Date lastmodified) {
		this.lastmodified = lastmodified;
	}

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

	public Set getVoucherheaders() {
		return this.voucherheaders;
	}

	public void setVoucherheaders(Set voucherheaders) {
		this.voucherheaders = voucherheaders;
	}

	public Set getFundsources() {
		return this.fundsources;
	}

	public void setFundsources(Set fundsources) {
		this.fundsources = fundsources;
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

	public Set getSharedFundSource() {
		return sharedFundSource;
	}

	public void setSharedFundSource(Set sharedFundSource) {
		this.sharedFundSource = sharedFundSource;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
