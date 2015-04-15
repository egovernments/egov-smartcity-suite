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
package org.egov.mdm.masters.administration;

public class State {

	private Integer id;
	private String name;
	private String nameLocal;
	private String stateConst;

	public Integer getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @hibernate.property name="name"
	 * @hibernate.column name="STATENAME"
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @hibernate.property name="nameLocal"
	 * @hibernate.column name="STATENAMELOCAL"
	 * @return Returns the nameLocal.
	 */
	public String getNameLocal() {
		return nameLocal;
	}

	/**
	 * @param nameLocal The nameLocal to set.
	 */
	public void setNameLocal(String nameLocal) {
		this.nameLocal = nameLocal;
	}

	/**
	 * @hibernate.property name="stateConst"
	 * @hibernate.column name="STATECONST"
	 * @return Returns the stateConst.
	 */
	public String getStateConst() {
		return stateConst;
	}

	/**
	 * @param stateConst The stateConst to set.
	 */
	public void setStateConst(String stateConst) {
		this.stateConst = stateConst;
	}

}
