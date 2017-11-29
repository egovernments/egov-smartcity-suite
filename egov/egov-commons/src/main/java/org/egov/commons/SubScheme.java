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

import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;

import java.math.BigDecimal;
import java.util.Date;


public class SubScheme implements java.io.Serializable
{

	private Integer id;

	private Scheme scheme;

	private String code;

	private String name;

	private Date validfrom;

	private Date validto;

	private Boolean isactive;

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
	
	private User createdBy;
	private User lastModifiedBy;
	private Date createdDate;

	public SubScheme(){
	}

	

	public Integer getId()
	{
		return this.id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Scheme getScheme()
	{
		return this.scheme;
	}

	public void setScheme(Scheme scheme)
	{
		this.scheme = scheme;
	}

	public String getCode()
	{
		return this.code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Date getValidfrom()
	{
		return this.validfrom;
	}

	public void setValidfrom(Date validfrom)
	{
		this.validfrom = validfrom;
	}

	public Date getValidto()
	{
		return this.validto;
	}

	public void setValidto(Date validto)
	{
		this.validto = (Date) validto;
	}

	

	public Date getLastmodifieddate()
	{
		return this.lastmodifieddate;
	}

	public void setLastmodifieddate(Date lastmodifieddate)
	{
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

	public void setCouncilLoanProposalNumber(
			String councilLoanProposalEstimateNumber) {
		this.councilLoanProposalNumber = councilLoanProposalEstimateNumber;
	}

	public String getCouncilLoanProposalNumber() {
		return councilLoanProposalNumber;
	}

	public void setCouncilAdminSanctionNumber(
			String councilAdminSanctionEstimateNumber) {
		this.councilAdminSanctionNumber = councilAdminSanctionEstimateNumber;
	}

	public String getCouncilAdminSanctionNumber() {
		return councilAdminSanctionNumber;
	}

	public void setGovtLoanProposalNumber(
			String govtLoanProposalEstimateNumber) {
		this.govtLoanProposalNumber = govtLoanProposalEstimateNumber;
	}

	public String getGovtLoanProposalNumber() {
		return govtLoanProposalNumber;
	}

	public void setGovtAdminSanctionNumber(
			String govtAdminSanctionEstimateNumber) {
		this.govtAdminSanctionNumber = govtAdminSanctionEstimateNumber;
	}

	public String getGovtAdminSanctionNumber() {
		return govtAdminSanctionNumber;
	}

	public void setCouncilLoanProposalDate(
			Date councilLoanProposalEstimateDate) {
		this.councilLoanProposalDate = councilLoanProposalEstimateDate;
	}

	public Date getCouncilLoanProposalDate() {
		return councilLoanProposalDate;
	}

	public void setCouncilAdminSanctionDate(
			Date councilAdminSanctionEstimateDate) {
		this.councilAdminSanctionDate = councilAdminSanctionEstimateDate;
	}

	public Date getCouncilAdminSanctionDate() {
		return councilAdminSanctionDate;
	}

	public void setGovtLoanProposalDate(
			Date govtLoanProposalEstimateDate) {
		this.govtLoanProposalDate = govtLoanProposalEstimateDate;
	}

	public Date getGovtLoanProposalDate() {
		return govtLoanProposalDate;
	}

	public void setGovtAdminSanctionDate(
			Date govtAdminSanctionEstimateDate) {
		this.govtAdminSanctionDate = govtAdminSanctionEstimateDate;
	}

	public Date getGovtAdminSanctionDate() {
		return govtAdminSanctionDate;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public User getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(User lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}



	public Boolean getIsactive() {
		return isactive;
	}



	public void setIsactive(Boolean isactive) {
		this.isactive = isactive;
	}

}
