/*
 * @(#)RuleGroupMapForm.java 3.0, 17 Jun, 2013 5:14:55 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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