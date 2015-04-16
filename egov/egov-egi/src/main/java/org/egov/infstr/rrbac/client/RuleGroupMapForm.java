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
package org.egov.infstr.rrbac.client;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.egov.infstr.client.EgovActionForm;

public class RuleGroupMapForm extends EgovActionForm implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String ruleGroupId = "";
	private String ruleGroupId1 = "";
	private String ruleName = "";
	private String default1 = "";
	private String maxRange = "";
	private String minRange = "";
	private String type = "";
	private String active = "";
	private String include = "";
	private String exclude = "";
	private String includeList = "";
	private String excludeList = "";

	public String getRuleGroupId() {
		return this.ruleGroupId;
	}

	public void setRuleGroupId(final String ruleGroupId) {
		this.ruleGroupId = ruleGroupId;
	}

	public String getRuleGroupId1() {
		return this.ruleGroupId1;
	}

	public void setRuleGroupId1(final String ruleGroupId1) {
		this.ruleGroupId1 = ruleGroupId1;
	}

	public String getRuleName() {
		return this.ruleName;
	}

	public void setRuleName(final String ruleName) {
		this.ruleName = ruleName;
	}

	public String getDefault1() {
		return this.default1;
	}

	public void setDefault1(final String default1) {
		this.default1 = default1;
	}

	public String getMaxRange() {
		return this.maxRange;
	}

	public void setMaxRange(final String maxRange) {
		this.maxRange = maxRange;
	}

	public String getMinRange() {
		return this.minRange;
	}

	public void setMinRange(final String minRange) {
		this.minRange = minRange;
	}

	public String getType() {
		return this.type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public String getActive() {
		return this.active;
	}

	public void setActive(final String active) {
		this.active = active;
	}

	public String getInclude() {
		return this.include;
	}

	public void setInclude(final String include) {
		this.include = include;
	}

	public String getExclude() {
		return this.exclude;
	}

	public void setExclude(final String exclude) {
		this.exclude = exclude;
	}

	public String getIncludeList() {
		return this.includeList;
	}

	public void setIncludeList(final String includeList) {
		this.includeList = includeList;
	}

	public String getExcludeList() {
		return this.excludeList;
	}

	public void setExcludeList(final String excludeList) {
		this.excludeList = excludeList;
	}

	@Override
	public void reset(final ActionMapping mapping, final HttpServletRequest req) {

		this.ruleGroupId = "";
		this.ruleGroupId1 = "";
		this.ruleName = "";
		this.default1 = "";
		this.maxRange = "";
		this.minRange = "";
		this.type = "";
		this.active = "";
		this.include = "";
		this.exclude = "";
		this.includeList = "";
		this.excludeList = "";

	}

}