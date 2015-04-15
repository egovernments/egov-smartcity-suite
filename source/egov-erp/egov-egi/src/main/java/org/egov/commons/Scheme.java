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
package org.egov.commons;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Scheme implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private Fund fund;

	private String code;

	private String name;

	private Date validfrom;

	private Date validto;

	private String isactive;

	private String description;

	private BigDecimal sectorid;

	private BigDecimal aaes;

	private BigDecimal fieldid;

	private Set<SubScheme> subSchemes = new HashSet<SubScheme>(0);

	public Scheme() {
		 //For hibernate to work
	}

	public Scheme(Integer id) {
		this.id = id;
	}

	public Scheme(Integer id, Fund fund, String code, String name, Date validfrom, Date validto, String isactive, String description, BigDecimal sectorid, BigDecimal aaes, BigDecimal fieldid, Set<SubScheme> subSchemes) {
		this.id = id;
		this.fund = fund;
		this.code = code;
		this.name = name;
		this.validfrom = validfrom;
		this.validto = validto;
		this.isactive = isactive;
		this.description = description;
		this.sectorid = sectorid;
		this.aaes = aaes;
		this.fieldid = fieldid;
		this.subSchemes = subSchemes;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Fund getFund() {
		return this.fund;
	}

	public void setFund(Fund fund) {
		this.fund = fund;
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

	public void setIsactive(String isactive) {
		this.isactive = isactive;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getSectorid() {
		return this.sectorid;
	}

	public void setSectorid(BigDecimal sectorid) {
		this.sectorid = sectorid;
	}

	public BigDecimal getAaes() {
		return this.aaes;
	}

	public void setAaes(BigDecimal aaes) {
		this.aaes = aaes;
	}

	public BigDecimal getFieldid() {
		return this.fieldid;
	}

	public void setFieldid(BigDecimal fieldid) {
		this.fieldid = fieldid;
	}

	public Set<SubScheme> getSubSchemes() {
		return this.subSchemes;
	}

	public void setSubSchemes(Set<SubScheme> subSchemes) {
		this.subSchemes = subSchemes;
	}

}
