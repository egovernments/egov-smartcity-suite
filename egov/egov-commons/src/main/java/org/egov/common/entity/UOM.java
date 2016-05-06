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

public class UOM implements java.io.Serializable {

	private static final long serialVersionUID = 8964393733499647786L;

	private Integer id;

	private UOMCategory uomCategory;

	private String uom;

	private String narration;

	private BigDecimal convFactor;

	private boolean baseuom;

	private Date lastmodified;

	private Date createddate;

	private BigDecimal createdby;

	private BigDecimal lastmodifiedby;

	public UOM() {
	}

	public UOM(final Integer id, final UOMCategory egUomcategory, final String uom, final BigDecimal convFactor,
			final boolean baseuom, final Date lastmodified, final Date createddate, final BigDecimal createdby) {
		this.id = id;
		this.uomCategory = egUomcategory;
		this.uom = uom;
		this.convFactor = convFactor;
		this.baseuom = baseuom;
		this.lastmodified = lastmodified;
		this.createddate = createddate;
		this.createdby = createdby;
	}

	public UOM(final Integer id, final UOMCategory egUomcategory, final String uom, final String narration,
			final BigDecimal convFactor, final boolean baseuom, final Date lastmodified, final Date createddate,
			final BigDecimal createdby, final BigDecimal lastmodifiedby) {
		this.id = id;
		this.uomCategory = egUomcategory;
		this.uom = uom;
		this.narration = narration;
		this.convFactor = convFactor;
		this.baseuom = baseuom;
		this.lastmodified = lastmodified;
		this.createddate = createddate;
		this.createdby = createdby;
		this.lastmodifiedby = lastmodifiedby;
	}

	public Integer getId() {
		return id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public UOMCategory getUomCategory() {
		return uomCategory;
	}

	public void setUomCategory(final UOMCategory uomCategory) {
		this.uomCategory = uomCategory;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(final String uom) {
		this.uom = uom;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(final String narration) {
		this.narration = narration;
	}

	public BigDecimal getConvFactor() {
		return convFactor;
	}

	public void setConvFactor(final BigDecimal convFactor) {
		this.convFactor = convFactor;
	}

	public boolean isBaseuom() {
		return baseuom;
	}

	public void setBaseuom(final boolean baseuom) {
		this.baseuom = baseuom;
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

}
