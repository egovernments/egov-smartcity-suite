/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.infra.web.taglib;

import org.egov.infra.admin.master.entity.Action;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.service.ActionService;
import org.egov.infra.security.utils.AuthorizeRule;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A tag that shows/hides html within it depending on user's access
 * 
 * @author sahinab
 */
public class EgovAuthorizeTag extends  RequestContextAwareTag {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long actionId;
	private String actionName;
	private AuthorizeRule ruleObject;

	/**
	 * @return Returns the actionId.
	 */
	public Long getActionId() {
		return this.actionId;
	}

	/**
	 * @param actionId The actionId to set.
	 */
	public void setActionId(final Long actionId) {
		this.actionId = actionId;
	}

	/**
	 * @return Returns the actionName.
	 */
	public String getActionName() {
		return this.actionName;
	}

	/**
	 * @param actionName The actionName to set.
	 */
	public void setActionName(final String actionName) {
		this.actionName = actionName;
	}

	/**
	 * @return Returns the rule.
	 */
	public AuthorizeRule getRuleObject() {
		return this.ruleObject;
	}

	/**
	 * @param rule The rule to set.
	 */
	public void setRuleObject(final AuthorizeRule rule) {
		this.ruleObject = rule;
	}

	/**
	 * evaluates if body content within <authorize> tag is to be included.
	 */
	@Override
	public int doStartTagInternal() throws JspTagException {
		Action action = null;
		final Set userRoles = this.getPrincipalRoles();

		// no params specified
		if ((null == this.actionId || 0 >= this.actionId) && (null == this.actionName || "".equals(this.actionName)) && null == this.ruleObject) {
			return Tag.SKIP_BODY;
		}
		// get logged in user
		if (this.ruleObject != null) {
			/*
			 * Authentication currentUser = SecurityContextHolder.getContext() .getAuthentication(); if (ruleObject.isAuthorized(currentUser)) return EVAL_BODY_INCLUDE; else return SKIP_BODY;
			 */
		}
		//
		final ActionService rbacService = (ActionService) getRequestContext().getWebApplicationContext().getBean("actionService");

		if (this.actionName != null) {
			action = rbacService.getActionByName(this.actionName);
		}
		if (this.actionId != null) {
			action = rbacService.getActionById(this.actionId);
		}
		if (action != null) {
			// if user's role belongs to action roles
			final Set actionRoles = action.getRoles();
			final Set grantedCopy = this.retainAll(userRoles, actionRoles);
			if (grantedCopy.isEmpty()) {
				return Tag.SKIP_BODY;
			}

		}

		return Tag.EVAL_BODY_INCLUDE;
	}

	private Set getPrincipalRoles() {

		final Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();

		if (null == currentUser) {
			return Collections.EMPTY_SET;
		}
		if (null == currentUser.getAuthorities() || currentUser.getAuthorities().isEmpty()) {
			return Collections.EMPTY_SET;
		}

		final Set<String> grantedRoles = new HashSet<String>();
		for (final GrantedAuthority authority : currentUser.getAuthorities()) {
			grantedRoles.add(authority.getAuthority());
		}
		return grantedRoles;

	}

	private Set retainAll(final Set<String> granted, final Set<Role> required) {
		final Set<String> requiredRoles = new HashSet<String>();
		for (final Role role : required) {
			requiredRoles.add(role.getName());
		}
		granted.retainAll(requiredRoles);

		return granted;

	}
}
