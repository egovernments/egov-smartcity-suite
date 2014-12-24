/*
 * Created on Jan 16, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.transactions;

import org.apache.log4j.Logger;
import org.egov.exceptions.RBACException;
import org.egov.lib.rjbac.role.Role;
import org.egov.lib.rjbac.role.ejb.api.RoleService;
import org.egov.lib.rrbac.model.Action;
import org.egov.lib.rrbac.model.RuleData;
import org.egov.lib.rrbac.services.RbacService;

/**
 * @author Administrator
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class RoleRuleValidate {

	private static final Logger LOGGER = Logger
			.getLogger(RoleRuleValidate.class);
	private RbacService rbacService;
	private RoleService roleService;

	public static void main(String args[]) {

	}

	public void validateAction(Integer roleId, Integer actionId, RuleData entity)
			throws RBACException {

		Role role = roleService.getRole(roleId);
		String roleName = role.getRoleName();
		LOGGER.info(" >>>>>name1 " + roleName);

		Action action = rbacService.getActionById(actionId);
		String actionName = action.getName();
		LOGGER.info(" >>>>>name2 " + actionName);

		boolean b = action.isActionValid(role);
		LOGGER.info(" >>>>>bbb " + b);

		boolean b1 = action.isValid(entity, role);
		LOGGER.info(" >>>>>bbb " + b1);

	}

	public void setRbacService(RbacService rbacService) {
		this.rbacService = rbacService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

}
