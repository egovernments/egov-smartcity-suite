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
package org.egov.infstr.flexfields;

public class AttributeTypeForm extends org.apache.struts.action.ActionForm {

	private static final long serialVersionUID = 1L;
	String id;
	String applDomainId;
	String attributeName;
	String attributeDataType;
	String forward;
	String isRequired;
	String isList;
	String key[];
	String value[];
	String defaultValue;

	public String getDefaultValue() {
		return this.defaultValue;
	}

	public void setDefaultValue(final String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getIsList() {
		return this.isList;
	}

	public void setIsList(final String isList) {
		this.isList = isList;
	}

	public String getIsRequired() {
		return this.isRequired;
	}

	public void setIsRequired(final String isRequired) {
		this.isRequired = isRequired;
	}

	public String[] getKey() {
		return this.key;
	}

	public void setKey(final String[] key) {
		this.key = key;
	}

	public String[] getValue() {
		return this.value;
	}

	public void setValue(final String[] value) {
		this.value = value;
	}

	public String getForward() {
		return this.forward;
	}

	public void setForward(final String forward) {
		this.forward = forward;
	}

	public String getAttributeDataType() {
		return this.attributeDataType;
	}

	public void setAttributeDataType(final String attributeDataType) {
		this.attributeDataType = attributeDataType;
	}

	public String getAttributeName() {
		return this.attributeName;
	}

	public void setAttributeName(final String attributeName) {
		this.attributeName = attributeName;
	}

	public String getApplDomainId() {
		return this.applDomainId;
	}

	public void setApplDomainId(final String domainId) {
		this.applDomainId = domainId;
	}

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

}
