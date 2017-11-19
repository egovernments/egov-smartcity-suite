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
import java.util.HashSet;
import java.util.Set;

public class EgPartytype implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	private Integer id;

	private EgPartytype egPartytype;

	private String code;

	private String description;

	private BigDecimal createdby;

	private Date createddate;

	private BigDecimal lastmodifiedby;

	private Date lastmodifieddate;

	private Set<EgwTypeOfWork> egwTypeofworks = new HashSet<EgwTypeOfWork>(0);

	private Set<EgPartytype> egPartytypes = new HashSet<EgPartytype>(0);

	public EgPartytype() {
	}

	public EgPartytype(String code, String description, BigDecimal createdby, Date createddate) {
		this.code = code;
		this.description = description;
		this.createdby = createdby;
		this.createddate = createddate;
	}

	public EgPartytype(EgPartytype egPartytype, String code, String description, BigDecimal createdby, Date createddate, BigDecimal lastmodifiedby, Date lastmodifieddate, Set<EgwTypeOfWork> egwTypeofworks, Set<EgPartytype> egPartytypes) {
		this.egPartytype = egPartytype;
		this.code = code;
		this.description = description;
		this.createdby = createdby;
		this.createddate = createddate;
		this.lastmodifiedby = lastmodifiedby;
		this.lastmodifieddate = lastmodifieddate;
		this.egwTypeofworks = egwTypeofworks;
		this.egPartytypes = egPartytypes;
	}

	public EgPartytype getEgPartytype() {
		return this.egPartytype;
	}

	public void setEgPartytype(EgPartytype egPartytype) {
		this.egPartytype = egPartytype;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getCreatedby() {
		return this.createdby;
	}

	public void setCreatedby(BigDecimal createdby) {
		this.createdby = createdby;
	}

	public Date getCreateddate() {
		return this.createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}

	public BigDecimal getLastmodifiedby() {
		return this.lastmodifiedby;
	}

	public void setLastmodifiedby(BigDecimal lastmodifiedby) {
		this.lastmodifiedby = lastmodifiedby;
	}

	public Date getLastmodifieddate() {
		return this.lastmodifieddate;
	}

	public void setLastmodifieddate(Date lastmodifieddate) {
		this.lastmodifieddate = lastmodifieddate;
	}

	public Set<EgwTypeOfWork> getEgwTypeofworks() {
		return this.egwTypeofworks;
	}

	public void setEgwTypeofworks(Set<EgwTypeOfWork> egwTypeofworks) {
		this.egwTypeofworks = egwTypeofworks;
	}

	public Set<EgPartytype> getEgPartytypes() {
		return this.egPartytypes;
	}

	public void setEgPartytypes(Set<EgPartytype> egPartytypes) {
		this.egPartytypes = egPartytypes;
	}

}
