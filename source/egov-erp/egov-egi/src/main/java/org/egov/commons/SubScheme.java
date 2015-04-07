/*
 * @(#)SubScheme.java 3.0, 6 Jun, 2013 4:38:39 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.infra.admin.master.entity.Department;

public class SubScheme implements java.io.Serializable {

	private Integer id;

	private Scheme scheme;

	private String code;

	private String name;

	private Date validfrom;

	private Date validto;

	private String isactive = "0";

	private Date lastmodifieddate;
	private Department department;
	private BigDecimal initialEstimateAmount;
	private String councilLoanProposalNumber;
	private String councilAdminSanctionNumber;
	private String govtLoanProposalNumber;
	private String govtAdminSanctionNumber;
	private Date councilLoanProposalDate;
	private Date councilAdminSanctionDate;
	private Date govtLoanProposalDate;
	private Date govtAdminSanctionDate;

	public SubScheme() {
		//For hibernate to work
	}

	public SubScheme(Integer id, Scheme scheme, String code, String name, Date validfrom, String isactive, Date lastmodifieddate) {
		this.id = id;
		this.scheme = scheme;
		this.code = code;
		this.name = name;
		this.validfrom = validfrom;
		this.isactive = isactive;
		this.lastmodifieddate = lastmodifieddate;
	}

	public SubScheme(Integer id, Scheme scheme, String code, String name, Date validfrom, Date validto, String isactive, Date lastmodifieddate) {
		this.id = id;
		this.scheme = scheme;
		this.code = code;
		this.name = name;
		this.validfrom = validfrom;
		this.validto = validto;
		this.isactive = isactive;
		this.lastmodifieddate = lastmodifieddate;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Scheme getScheme() {
		return this.scheme;
	}

	public void setScheme(Scheme scheme) {
		this.scheme = scheme;
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

	public Date getValidfrom() {
		return this.validfrom;
	}

	public void setValidfrom(Date validfrom) {
		this.validfrom = validfrom;
	}

	public Date getValidto() {
		return this.validto;
	}

	public void setValidto(Date validto) {
		this.validto = validto;
	}

	public String getIsactive() {
		return this.isactive;
	}

	@Deprecated
	public void setIsactive(String isactive) {
		this.isactive = isactive;
	}

	public void setIsactive(boolean isactive) {
		if (isactive)
			this.isactive = "1";
		else
			this.isactive = "0";
	}

	public Date getLastmodifieddate() {
		return this.lastmodifieddate;
	}

	public void setLastmodifieddate(Date lastmodifieddate) {
		this.lastmodifieddate = lastmodifieddate;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Department getDepartment() {
		return department;
	}

	public void setInitialEstimateAmount(BigDecimal initialEstimateAmount) {
		this.initialEstimateAmount = initialEstimateAmount;
	}

	public BigDecimal getInitialEstimateAmount() {
		return initialEstimateAmount;
	}

	public void setCouncilLoanProposalNumber(String councilLoanProposalEstimateNumber) {
		this.councilLoanProposalNumber = councilLoanProposalEstimateNumber;
	}

	public String getCouncilLoanProposalNumber() {
		return councilLoanProposalNumber;
	}

	public void setCouncilAdminSanctionNumber(String councilAdminSanctionEstimateNumber) {
		this.councilAdminSanctionNumber = councilAdminSanctionEstimateNumber;
	}

	public String getCouncilAdminSanctionNumber() {
		return councilAdminSanctionNumber;
	}

	public void setGovtLoanProposalNumber(String govtLoanProposalEstimateNumber) {
		this.govtLoanProposalNumber = govtLoanProposalEstimateNumber;
	}

	public String getGovtLoanProposalNumber() {
		return govtLoanProposalNumber;
	}

	public void setGovtAdminSanctionNumber(String govtAdminSanctionEstimateNumber) {
		this.govtAdminSanctionNumber = govtAdminSanctionEstimateNumber;
	}

	public String getGovtAdminSanctionNumber() {
		return govtAdminSanctionNumber;
	}

	public void setCouncilLoanProposalDate(Date councilLoanProposalEstimateDate) {
		this.councilLoanProposalDate = councilLoanProposalEstimateDate;
	}

	public Date getCouncilLoanProposalDate() {
		return councilLoanProposalDate;
	}

	public void setCouncilAdminSanctionDate(Date councilAdminSanctionEstimateDate) {
		this.councilAdminSanctionDate = councilAdminSanctionEstimateDate;
	}

	public Date getCouncilAdminSanctionDate() {
		return councilAdminSanctionDate;
	}

	public void setGovtLoanProposalDate(Date govtLoanProposalEstimateDate) {
		this.govtLoanProposalDate = govtLoanProposalEstimateDate;
	}

	public Date getGovtLoanProposalDate() {
		return govtLoanProposalDate;
	}

	public void setGovtAdminSanctionDate(Date govtAdminSanctionEstimateDate) {
		this.govtAdminSanctionDate = govtAdminSanctionEstimateDate;
	}

	public Date getGovtAdminSanctionDate() {
		return govtAdminSanctionDate;
	}

}
