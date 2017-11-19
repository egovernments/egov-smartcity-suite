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

import java.util.Date;

public class EgwTypeOfWork implements java.io.Serializable{
	
    private static final long serialVersionUID = 1L;
        private Long id;
	private String code;
	private EgwTypeOfWork parentid;
	private EgPartytype egPartytype;
	private String description;
	private Integer createdby;
	private Integer lastmodifiedby;
	private Date createddate;
	private Date lastmodifieddate;

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the createdby
	 */
	public Integer getCreatedby() {
		return createdby;
	}

	/**
	 * @param createdby the createdby to set
	 */
	public void setCreatedby(Integer createdby) {
		this.createdby = createdby;
	}

	/**
	 * @return the createddate
	 */
	public Date getCreateddate() {
		return createddate;
	}

	/**
	 * @param createddate the createddate to set
	 */
	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the lastmodifiedby
	 */
	public Integer getLastmodifiedby() {
		return lastmodifiedby;
	}

	/**
	 * @param lastmodifiedby the lastmodifiedby to set
	 */
	public void setLastmodifiedby(Integer lastmodifiedby) {
		this.lastmodifiedby = lastmodifiedby;
	}

	/**
	 * @return the lastmodifieddate
	 */
	public Date getLastmodifieddate() {
		return lastmodifieddate;
	}

	/**
	 * @param lastmodifieddate the lastmodifieddate to set
	 */
	public void setLastmodifieddate(Date lastmodifieddate) {
		this.lastmodifieddate = lastmodifieddate;
	}

	/**
	 * @return the parentid
	 */
	public EgwTypeOfWork getParentid() {
		return parentid;
	}

	/**
	 * @param parentid the parentid to set
	 */
	public void setParentid(EgwTypeOfWork parentid) {
		this.parentid = parentid;
	}

	/**
	 * @return the egPartytype
	 */
	public EgPartytype getEgPartytype() {
		return egPartytype;
	}

	/**
	 * @param egPartytype the egPartytype to set
	 */
	public void setEgPartytype(EgPartytype egPartytype) {
		this.egPartytype = egPartytype;
	}

}
