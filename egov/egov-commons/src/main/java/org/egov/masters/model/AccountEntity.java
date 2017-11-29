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
package org.egov.masters.model;

import org.egov.commons.Accountdetailtype;
import org.egov.commons.EgwStatus;
import org.egov.commons.utils.EntityType;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.AbstractPersistable;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.hibernate.validator.constraints.Length;

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
import java.util.Date;


@Entity
@Table(name="AccountEntityMaster")
@SequenceGenerator(name = AccountEntity.SEQ, sequenceName = AccountEntity.SEQ, allocationSize = 1)
@Unique(id = "id", tableName = "AccountEntityMaster", fields = { "code"}, columnName = { "code" }, enableDfltMsg = true)
public class AccountEntity extends AbstractPersistable<Integer> implements java.io.Serializable, EntityType {

	private static final long serialVersionUID = 1L;
	public static final String SEQ = "SEQ_AccountEntityMaster";
	
	@Id
	@GeneratedValue(generator = SEQ, strategy = GenerationType.SEQUENCE)
	private Integer id;

	//@SearchField
	//@SearchResult
	@NotNull
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="detailtypeid")
	private Accountdetailtype accountdetailtype;
	
	//@SearchField
	//@SearchResult
	@NotNull
	@Length(max=350)
	private String name;
	
	//@SearchField
	//@SearchResult
	@NotNull
	@Length(max=25)
	private String code;

	@Length(max=250)
	private String narration;
	//@SearchResult
	private Boolean isactive;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="createdBy")
	private User createdBy;

	private Date createdDate;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="lastModifiedBy")
	private User lastModifiedBy;

	private Date lastmodifiedDate;
	

	public AccountEntity() {
		//For hibernate to work
	}

	
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Accountdetailtype getAccountdetailtype() {
		return this.accountdetailtype;
	}

	public void setAccountdetailtype(Accountdetailtype accountdetailtype) {
		this.accountdetailtype = accountdetailtype;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getNarration() {
		return this.narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public Boolean getIsactive() {
		return this.isactive;
	}

	public void setIsactive(Boolean isactive) {
		this.isactive = isactive;
	}

	
	@Override
	public String getBankaccount() {
		return null;
	}

	@Override
	public String getBankname() {
		return null;
	}

	@Override
	public String getIfsccode() {		
		return null;
	}

	@Override
	public String getModeofpay() {		
		return null;
	}

	@Override
	public String getPanno() {		
		return null;
	}

	@Override
	public String getTinno() {		
		return null;
	}

	@Override
	public Integer getEntityId() {
		return this.id;

	}

	public String getEntityDescription() {
		return this.narration;
	}

	@Override
	public EgwStatus getEgwStatus() {

		return null;
	}


	public Date getLastmodifiedDate() {
		return lastmodifiedDate;
	}


	public void setLastmodifiedDate(Date lastmodifiedDate) {
		this.lastmodifiedDate = lastmodifiedDate;
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

}
