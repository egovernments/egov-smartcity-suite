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

public class EgAttributevalues implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private EgAttributetype egAttributetype;
	private EgApplDomain egApplDomain;
	private String attValue;
	private Long domaintxnid;

	/** default constructor */
	public EgAttributevalues() {
		// For Hibernate
	}

	/** full constructor */
	public EgAttributevalues(Long id, EgAttributetype egAttributetype, EgApplDomain egApplDomain, String attValue, Long domaintxnid) {
		this.id = id;
		this.egAttributetype = egAttributetype;
		this.egApplDomain = egApplDomain;
		this.attValue = attValue;
		this.domaintxnid = domaintxnid;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EgAttributetype getEgAttributetype() {
		return this.egAttributetype;
	}

	public void setEgAttributetype(EgAttributetype egAttributetype) {
		this.egAttributetype = egAttributetype;
	}

	public EgApplDomain getEgApplDomain() {
		return this.egApplDomain;
	}

	public void setEgApplDomain(EgApplDomain egApplDomain) {
		this.egApplDomain = egApplDomain;
	}

	public String getAttValue() {
		return this.attValue;
	}

	public void setAttValue(String attValue) {
		this.attValue = attValue;
	}

	public Long getDomaintxnid() {
		return this.domaintxnid;
	}

	public void setDomaintxnid(Long domaintxnid) {
		this.domaintxnid = domaintxnid;
	}

}