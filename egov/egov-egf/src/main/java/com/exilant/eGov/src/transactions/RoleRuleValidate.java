/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
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
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
/*
 * Created on Jan 16, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.transactions;



import org.apache.log4j.Logger;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.service.RoleService;
import org.egov.lib.rrbac.dao.ActionHibernateDAO;
import org.egov.lib.rrbac.model.Action;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RoleRuleValidate {
	
	private static final Logger LOGGER = Logger.getLogger(RoleRuleValidate.class);
	private ActionHibernateDAO rbacService;
	private RoleService roleService;
	
	public static void main(String args[])
	{
	
	}
	
	public void validateAction(Integer roleId,Integer actionId)
	{
		
		Role role = roleService.getRoleById(roleId.longValue());
		String roleName=role.getName();
		if(LOGGER.isInfoEnabled())     LOGGER.info(" >>>>>name1 "+roleName);

		Action action= (Action)rbacService.findById(actionId,false);
		String actionName=action.getName();
		if(LOGGER.isInfoEnabled())     LOGGER.info(" >>>>>name2 "+actionName);

		boolean b=action.isActionValid(role);
		if(LOGGER.isInfoEnabled())     LOGGER.info(" >>>>>bbb "+b);
		
		
		/*boolean b1=action.isValid(entity,role);
		if(LOGGER.isInfoEnabled())     LOGGER.info(" >>>>>bbb "+b1);*/
	
	

	}
}
