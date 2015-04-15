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
package org.egov.infstr.commonMasters;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class EgUomcategory implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private String category;

	private String narration;

	private Date lastmodified;

	private Date createddate;

	private BigDecimal createdby;

	private BigDecimal lastmodifiedby;

	private Set egUoms = new HashSet(0);

	public EgUomcategory() {
		// For Hibernate
	}

	public EgUomcategory(Integer id, String category, Date lastmodified, Date createddate, BigDecimal createdby) {
		this.id = id;
		this.category = category;
		this.lastmodified = lastmodified;
		this.createddate = createddate;
		this.createdby = createdby;
	}

	public EgUomcategory(Integer id, String category, String narration, Date lastmodified, Date createddate, BigDecimal createdby, BigDecimal lastmodifiedby, Set egUoms) {
		this.id = id;
		this.category = category;
		this.narration = narration;
		this.lastmodified = lastmodified;
		this.createddate = createddate;
		this.createdby = createdby;
		this.lastmodifiedby = lastmodifiedby;
		this.egUoms = egUoms;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getNarration() {
		return this.narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public Date getLastmodified() {
		return this.lastmodified;
	}

	public void setLastmodified(Date lastmodified) {
		this.lastmodified = lastmodified;
	}

	public Date getCreateddate() {
		return this.createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}

	public BigDecimal getCreatedby() {
		return this.createdby;
	}

	public void setCreatedby(BigDecimal createdby) {
		this.createdby = createdby;
	}

	public BigDecimal getLastmodifiedby() {
		return this.lastmodifiedby;
	}

	public void setLastmodifiedby(BigDecimal lastmodifiedby) {
		this.lastmodifiedby = lastmodifiedby;
	}

	public Set getEgUoms() {
		return this.egUoms;
	}

	public void setEgUoms(Set egUoms) {
		this.egUoms = egUoms;
	}

}
