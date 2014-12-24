/*
 * @(#)Bankaccount.java 3.0, 6 Jun, 2013 2:45:59 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.egov.commons.utils.BankAccountType;

public class Bankaccount implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Bankbranch bankbranch;
	private CChartOfAccounts chartofaccounts;
	private Fund fund;
	private String accountnumber;
	private String accounttype;
	private String narration;
	private boolean isactive;
	private Date created;
	private BigDecimal modifiedby;
	private Date lastmodified;
	private BigDecimal currentbalance;
	private String payTo;
	private BankAccountType type;
	private Set<EgSurrenderedCheques> egSurrenderedChequeses = new HashSet<EgSurrenderedCheques>(0);

	public Bankaccount() {
		//For hibernate to work
	}

	public Bankaccount(Bankbranch bankbranch, String accountnumber, String accounttype, boolean isactive, Date created, BigDecimal modifiedby, Date lastmodified, BigDecimal currentbalance, String payTo, BankAccountType type) {
		this.bankbranch = bankbranch;
		this.accountnumber = accountnumber;
		this.accounttype = accounttype;
		this.isactive = isactive;
		this.created = created;
		this.modifiedby = modifiedby;
		this.lastmodified = lastmodified;
		this.currentbalance = currentbalance;
		this.payTo = payTo;
		this.type = type;
	}

	public Bankaccount(Bankbranch bankbranch, CChartOfAccounts chartofaccounts, Fund fund, String accountnumber, String accounttype, String narration, boolean isactive, Date created, BigDecimal modifiedby, Date lastmodified, BigDecimal currentbalance,
			String payTo, Set<EgSurrenderedCheques> egSurrenderedChequeses) {
		this.bankbranch = bankbranch;
		this.chartofaccounts = chartofaccounts;
		this.fund = fund;
		this.accountnumber = accountnumber;
		this.accounttype = accounttype;
		this.narration = narration;
		this.isactive = isactive;
		this.created = created;
		this.modifiedby = modifiedby;
		this.lastmodified = lastmodified;
		this.currentbalance = currentbalance;
		this.payTo = payTo;
		this.egSurrenderedChequeses = egSurrenderedChequeses;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Bankbranch getBankbranch() {
		return this.bankbranch;
	}

	public void setBankbranch(Bankbranch bankbranch) {
		this.bankbranch = bankbranch;
	}

	public CChartOfAccounts getChartofaccounts() {
		return this.chartofaccounts;
	}

	public void setChartofaccounts(CChartOfAccounts chartofaccounts) {
		this.chartofaccounts = chartofaccounts;
	}

	public Fund getFund() {
		return this.fund;
	}

	public void setFund(Fund fund) {
		this.fund = fund;
	}

	public String getAccountnumber() {
		return this.accountnumber;
	}

	public void setAccountnumber(String accountnumber) {
		this.accountnumber = accountnumber;
	}

	public String getAccounttype() {
		return this.accounttype;
	}

	public void setAccounttype(String accounttype) {
		this.accounttype = accounttype;
	}

	public String getNarration() {
		return this.narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public boolean isIsactive() {
		return this.isactive;
	}

	public void setIsactive(boolean isactive) {
		this.isactive = isactive;
	}

	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public BigDecimal getModifiedby() {
		return this.modifiedby;
	}

	public void setModifiedby(BigDecimal modifiedby) {
		this.modifiedby = modifiedby;
	}

	public Date getLastmodified() {
		return this.lastmodified;
	}

	public void setLastmodified(Date lastmodified) {
		this.lastmodified = lastmodified;
	}

	public BigDecimal getCurrentbalance() {
		return this.currentbalance;
	}

	public void setCurrentbalance(BigDecimal currentbalance) {
		this.currentbalance = currentbalance;
	}
	
	public BankAccountType getType() {
		return type;
	}

	public void setType(BankAccountType type) {
		this.type = type;
	}
	
	public Set<EgSurrenderedCheques> getEgSurrenderedChequeses() {
		return this.egSurrenderedChequeses;
	}

	public void setEgSurrenderedChequeses(Set<EgSurrenderedCheques> egSurrenderedChequeses) {
		this.egSurrenderedChequeses = egSurrenderedChequeses;
	}

	public String getPayTo() {
		return payTo;
	}

	public void setPayTo(String payTo) {
		this.payTo = payTo;
	}

}
