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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import org.egov.exceptions.AccountCodeException;

public class AccountCodeRule implements Rules, AccountCodeRuleData {

	private Integer id;
	private String name;
	private Integer included;
	private Integer excluded;
	private Date updatedTime;
	private String defaultValue;
	private String type;
	private Integer active;
	private Set ieList;

	/**
	 * @return Returns the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id The id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return Returns the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the date
	 */
	public Date getUpdatedTime() {
		return updatedTime;
	}

	/**
	 * @param updatedtime the date to set
	 */
	public void setUpdatedTime(Date updatedtime) {
		this.updatedTime = updatedtime;
	}

	/**
	 * @return Returns the default
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param defaultValue The default value to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @return Returns 1 if active or 0 if inactive
	 */
	public Integer getActive() {
		return active;
	}

	/**
	 * @param acti To specify whether rule is active or not
	 */
	public void setActive(Integer acti) {
		this.active = acti;
	}

	/**
	 * @return Returns type of rule
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type To set type of Rule(AmountRule/FundRule/AccountCodeRule)
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return Returns 1 if include List is Specified else 0
	 */
	public Integer getIncluded() {
		return included;
	}

	/**
	 * @param included To set include
	 */
	public void setIncluded(Integer included) {
		this.included = included;
	}

	/**
	 * @return Returns 1 if exclude List is Specified else 0
	 */
	public Integer getExcluded() {
		return excluded;
	}

	/**
	 * @param excluded To set exclude
	 */
	public void setExcluded(Integer excluded) {
		this.excluded = excluded;
	}

	/**
	 * @return Returns collection of objects of type IEList, which gives include and exclude list for the Rule
	 */
	public Set getIeList() {
		return ieList;
	}

	/**
	 * To set include and exclude list for the Rule
	 * @param ieList
	 */
	public void setIeList(Set ieList) {
		this.ieList = ieList;
	}

	/**
	 * @param ie The IEList to add
	 */
	public void addIeList(IEList ie) {
		if (this.getIeList() != null) {
			this.getIeList().add(ie);
		} else {
			this.ieList = new HashSet();
			ieList.add(ie);
		}
	}

	/**
	 * @param ie The IEList to Remove
	 */
	public void removeIeList(IEList ie) {

		if (getIeList().contains(ie))
			getIeList().remove(ie);

	}

	/*
	 * public RuleGroup getRuleGroup() { return ruleGroup; } public void setRuleGroup(RuleGroup rg) { this.ruleGroup=rg; }
	 */

	/**
	 * @return Returns if the given Object is equal to AccountCodeRule
	 */
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (this == obj)
			return true;

		if (!(obj instanceof AccountCodeRule))
			return false;

		final AccountCodeRule other = (AccountCodeRule) obj;

		if (getName().equals(other.getName())) {
			return true;
		} else
			return false;
	}

	/**
	 * @return Returns the hashCode
	 */
	public int hashCode() {
		int hashCode = 0;
		if (this.getName() != null)
			hashCode = hashCode + this.getName().hashCode();

		return hashCode;
	}

	/**
	 * class specific implementation of isValid method checks account code of an entity is one of the include list and not of type exclude list Assumption The starting range of the account code is lower than the ending range
	 */
	public boolean isValid(RuleData rd) throws AccountCodeException {
		int isActive = (this.getActive()).intValue();
		boolean retVal = true;
		if (isActive != 0) {
			AccountCodeRuleData ac = (AccountCodeRuleData) rd;
			int include = (getIncluded()).intValue();
			int exclude = (getExcluded()).intValue();
			if (include != 0) {
				retVal = false;
				Set vs = ac.getIeList();
				Iterator itr = vs.iterator();
				String val = (String) itr.next();
				Set rs = this.getIeList();

				for (Iterator itr1 = rs.iterator(); itr1.hasNext();) {
					IEList ie = (IEList) itr1.next();
					if (ie.getType().equalsIgnoreCase("I")) {
						// iVal[0] = starting range
						// iVal[0] = ending range
						long iVal[] = new long[2];
						int count = 0;
						String iRange = ie.getValue();
						StringTokenizer st = new StringTokenizer(iRange, "-");
						for (count = 0; st.hasMoreTokens(); count++) {
							iVal[count] = (Long.parseLong(st.nextToken()));
						}
						if (count > 1) {
							if (Long.parseLong(val) >= iVal[0] && Long.parseLong(val) <= iVal[1]) {
								retVal = true;
								break;
							}
						} else {
							int len = (ie.getValue()).length();
							val = val.substring(0, len);
							if ((ie.getValue().equals(val))) {
								retVal = true;
								break;
							}
						}
					}
				}
				if (retVal == false) {
					throw new AccountCodeException("Unauthorized to use Account Code " + val);
				}
			}
			if (exclude != 0) {
				retVal = true;
				Set vs = ac.getIeList();
				Iterator itr = vs.iterator();
				String val = (String) itr.next();
				Set rs = this.getIeList();
				for (Iterator itr1 = rs.iterator(); itr1.hasNext();) {
					IEList ie = (IEList) itr1.next();
					if (ie.getType().equalsIgnoreCase("E")) {
						// iVal[0] = starting range
						// iVal[0] = ending range
						long eVal[] = new long[2];
						int count = 0;
						String eRange = ie.getValue();
						StringTokenizer st = new StringTokenizer(eRange, "-");
						for (count = 0; st.hasMoreTokens(); count++) {
							eVal[count] = (Long.parseLong(st.nextToken()));
						}
						if (count > 1) {
							if ((Long.parseLong(val) >= eVal[0]) && (Long.parseLong(val) <= eVal[1])) {
								retVal = false;
								break;
							}
						} else {
							int len = (ie.getValue()).length();
							val = val.substring(0, len);
							if ((ie.getValue().equals(val))) {
								retVal = false;
								break;
							}
						}

					}
				}
				if (retVal == false) {
					throw new AccountCodeException("Unauthorized to use Account Code " + val);
				}

			}
		}
		return retVal;
	}

}