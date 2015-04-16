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
package org.egov.infstr.flexfields.model;

import java.util.HashSet;
import java.util.Set;

public class EgAttributetype implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private EgApplDomain egApplDomain;
	private String attName;
	private String attDatatype;
	private String defaultValue;
	private Long isrequired;
	private Set egAttributevalueses = new HashSet(0);

	/** default constructor */
	public EgAttributetype() {
		// For Hibernate
	}

	/** minimal constructor */
	public EgAttributetype(Long id, EgApplDomain egApplDomain, String attName, String attDatatype, Long isrequired) {
		this.id = id;
		this.egApplDomain = egApplDomain;
		this.attName = attName;
		this.attDatatype = attDatatype;
		this.isrequired = isrequired;
	}

	/** full constructor */
	public EgAttributetype(Long id, EgApplDomain egApplDomain, String attName, String attDatatype, String defaultValue, Long isrequired, Set egAttributevalueses) {
		this.id = id;
		this.egApplDomain = egApplDomain;
		this.attName = attName;
		this.attDatatype = attDatatype;
		this.defaultValue = defaultValue;
		this.isrequired = isrequired;
		this.egAttributevalueses = egAttributevalueses;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EgApplDomain getEgApplDomain() {
		return this.egApplDomain;
	}

	public void setEgApplDomain(EgApplDomain egApplDomain) {
		this.egApplDomain = egApplDomain;
	}

	public String getAttName() {
		return this.attName;
	}

	public void setAttName(String attName) {
		this.attName = attName;
	}

	public String getAttDatatype() {
		return this.attDatatype;
	}

	public void setAttDatatype(String attDatatype) {
		this.attDatatype = attDatatype;
	}

	public String getDefaultValue() {
		return this.defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Long getIsrequired() {
		return this.isrequired;
	}

	public void setIsrequired(Long isrequired) {
		this.isrequired = isrequired;
	}

	public Set getEgAttributevalueses() {
		return this.egAttributevalueses;
	}

	public void setEgAttributevalueses(Set egAttributevalueses) {
		this.egAttributevalueses = egAttributevalueses;
	}

}