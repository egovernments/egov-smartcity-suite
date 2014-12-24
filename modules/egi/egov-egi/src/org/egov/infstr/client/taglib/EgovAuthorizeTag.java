package org.egov.infstr.client.taglib;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

import org.egov.infstr.beanfactory.ApplicationContextBeanProvider;
import org.egov.infstr.security.AuthorizeRule;
import org.egov.lib.rjbac.role.Role;
import org.egov.lib.rrbac.model.Action;
import org.egov.lib.rrbac.services.RbacService;
import org.egov.web.utils.ERPWebApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * A tag that shows/hides html within it depending on user's access
 * 
 * @author sahinab
 */
public class EgovAuthorizeTag extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer actionId;
	private String actionName;
	private AuthorizeRule ruleObject;

	/**
	 * @return Returns the actionId.
	 */
	public Integer getActionId() {
		return this.actionId;
	}

	/**
	 * @param actionId The actionId to set.
	 */
	public void setActionId(final Integer actionId) {
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
	public int doStartTag() throws JspTagException {
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
		final ApplicationContextBeanProvider provider = new ApplicationContextBeanProvider();
		provider.setApplicationContext(WebApplicationContextUtils.getWebApplicationContext(ERPWebApplicationContext.getServletContext()));
		final RbacService rbacService = (RbacService) provider.getBean("rbacService");

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
			requiredRoles.add(role.getRoleName());
		}
		granted.retainAll(requiredRoles);

		return granted;

	}
}
