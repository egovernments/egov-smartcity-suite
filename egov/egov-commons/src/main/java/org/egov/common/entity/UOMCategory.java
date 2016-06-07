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

package org.egov.common.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class UOMCategory implements java.io.Serializable {

	private static final long serialVersionUID = -5071889556823525112L;

	private Integer id;

	private String category;

	private String narration;

	private Date lastmodified;

	private Date createddate;

	private BigDecimal createdby;

	private BigDecimal lastmodifiedby;

	private Set<UOM> uoms = new HashSet<>();

	public UOMCategory() {
	}

	public UOMCategory(final Integer id, final String category, final Date lastmodified, final Date createddate,
			final BigDecimal createdby) {
		this.id = id;
		this.category = category;
		this.lastmodified = lastmodified;
		this.createddate = createddate;
		this.createdby = createdby;
	}

	public UOMCategory(final Integer id, final String category, final String narration, final Date lastmodified,
			final Date createddate, final BigDecimal createdby, final BigDecimal lastmodifiedby, final Set<UOM> uoms) {
		this.id = id;
		this.category = category;
		this.narration = narration;
		this.lastmodified = lastmodified;
		this.createddate = createddate;
		this.createdby = createdby;
		this.lastmodifiedby = lastmodifiedby;
		this.uoms = uoms;
	}

	public Integer getId() {
		return id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(final String category) {
		this.category = category;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(final String narration) {
		this.narration = narration;
	}

	public Date getLastmodified() {
		return lastmodified;
	}

	public void setLastmodified(final Date lastmodified) {
		this.lastmodified = lastmodified;
	}

	public Date getCreateddate() {
		return createddate;
	}

	public void setCreateddate(final Date createddate) {
		this.createddate = createddate;
	}

	public BigDecimal getCreatedby() {
		return createdby;
	}

	public void setCreatedby(final BigDecimal createdby) {
		this.createdby = createdby;
	}

	public BigDecimal getLastmodifiedby() {
		return lastmodifiedby;
	}

	public void setLastmodifiedby(final BigDecimal lastmodifiedby) {
		this.lastmodifiedby = lastmodifiedby;
	}

	public Set<UOM> getUoms() {
		return uoms;
	}

	public void setUoms(final Set<UOM> uoms) {
		this.uoms = uoms;
	}

}
