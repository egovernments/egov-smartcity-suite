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
package org.egov.lib.rrbac.model;

import java.util.Date;

import org.egov.exceptions.RBACException;

public interface Rules {

	/**
	 * @return Returns the id
	 */
	Integer getId();

	/**
	 * @param id The id to set
	 */
	void setId(Integer id);

	/**
	 * @return Returns the name
	 */
	String getName();

	/**
	 * @param name The name to set
	 */
	void setName(String name);

	/**
	 * @return Returns the date
	 */
	Date getUpdatedTime();

	/**
	 * @param updatedtime the date to set
	 */
	void setUpdatedTime(Date updatedtime);

	/**
	 * @return Returns the default
	 */
	String getDefaultValue();

	/**
	 * @param defaultValue The default value to set
	 */
	void setDefaultValue(String defaultValue);

	/**
	 * @param acti To specify whether rule is active or not
	 */
	void setActive(Integer acti);

	/**
	 * @return Returns 1 if active or 0 if inactive
	 */
	Integer getActive();

	/**
	 * @param type To set type of Rule(AmountRule/FundRule/AccountCodeRule)
	 */
	void setType(String type);

	/**
	 * @return Returns type of rule
	 */
	String getType();

	/**
	 * This method returns true if Rules are validated. implemenation of method is different for subclasses. An entity class implements an interface(which implements interface RuleData) The rule can have either include or exclude condition not both
	 * @param rd
	 * @return boolean
	 */
	boolean isValid(RuleData rd) throws RBACException;
}