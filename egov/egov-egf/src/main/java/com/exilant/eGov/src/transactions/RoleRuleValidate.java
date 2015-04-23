/*
 * Created on Jan 16, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.transactions;



import org.apache.log4j.Logger;
import org.egov.exceptions.RBACException;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.service.RoleService;
import org.egov.lib.rrbac.model.Action;
import org.egov.lib.rrbac.model.RuleData;
import org.egov.lib.rrbac.services.RbacService;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RoleRuleValidate {
	
	private static final Logger LOGGER = Logger.getLogger(RoleRuleValidate.class);
	private RbacService rbacService;
	private RoleService roleService;
	
	public static void main(String args[])
	{
	
	}
	
	public void validateAction(Integer roleId,Integer actionId,RuleData entity) throws RBACException
	{
		
		Role role = roleService.getRoleById(roleId.longValue());
		String roleName=role.getName();
		if(LOGGER.isInfoEnabled())     LOGGER.info(" >>>>>name1 "+roleName);

		Action action=rbacService.getActionById(actionId);
		String actionName=action.getName();
		if(LOGGER.isInfoEnabled())     LOGGER.info(" >>>>>name2 "+actionName);

		boolean b=action.isActionValid(role);
		if(LOGGER.isInfoEnabled())     LOGGER.info(" >>>>>bbb "+b);
		
		
		boolean b1=action.isValid(entity,role);
		if(LOGGER.isInfoEnabled())     LOGGER.info(" >>>>>bbb "+b1);
	
	

	}
}
