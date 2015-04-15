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
package org.egov.lib.rrbac.services;

import java.util.List;

import org.egov.lib.rrbac.model.Action;
import org.egov.lib.rrbac.model.Entity;
import org.egov.lib.rrbac.model.RuleGroup;
import org.egov.lib.rrbac.model.RuleType;
import org.egov.lib.rrbac.model.Rules;
import org.egov.lib.rrbac.model.Task;

public interface RbacService {

	/**
	 * To create an Entity.
	 * @param ent the ent
	 */
	public void createEntity(Entity ent);

	/**
	 * Gets the entity by id.
	 * @param id the id
	 * @return Entity object
	 */
	public Entity getEntityByID(Integer id);

	/**
	 * Gets the entity by name.
	 * @param name the name
	 * @return entity object
	 */
	public Entity getEntityByName(String name);

	/**
	 * Returns collection of all Entity.
	 * @return List
	 */
	public List<Entity> getEntityList();

	/**
	 * To update an Entity.
	 * @param ent the ent
	 */
	public void updateEntity(Entity ent);

	/**
	 * To create an task.
	 * @param tsk the tsk
	 */
	public void createTask(Task tsk);

	/**
	 * Gets the task by id.
	 * @param id the id
	 * @return task object
	 */
	public Task getTaskByID(Integer id);

	/**
	 * Gets the task by name.
	 * @param name the name
	 * @return task object
	 */
	public Task getTaskByName(String name);

	/**
	 * Returns collection of all task.
	 * @return List
	 */
	public List<Task> getTaskList();

	/**
	 * To update an Task.
	 * @param tsk the tsk
	 */
	public void updateTask(Task tsk);

	/**
	 * Gets the action by id.
	 * @param id the id
	 * @return Action object
	 */
	public Action getActionById(Integer id);

	/**
	 * Gets the action by name.
	 * @param name the name
	 * @return Action object
	 */
	public Action getActionByName(String name);

	/**
	 * Gets the action by url.
	 * @param contextPath - the root context path of the web application
	 * @param url - the url of the web page stripped of server name, port and context path. Contains query parameters
	 * @return Action object
	 */
	public Action getActionByURL(String contextPath, String url);

	/**
	 * To create an Action.
	 * @param ac Action object
	 */
	public void createAction(Action ac);

	/**
	 * To delete an Action.
	 * @param ac the ac
	 */
	public void deleteAction(Action ac);

	/**
	 * Returns collection of all actions.
	 * @return List
	 */
	public List<Action> getActionList();

	/**
	 * Returns collection of all actions with Roles.
	 * @return List
	 */
	public List<Action> getActionListWithRoles();

	/**
	 * Returns collection of all actions with Rulegroup.
	 * @return List
	 */
	public List<Action> getActionListWithRG();

	/**
	 * To update an Action.
	 * @param act the act
	 */
	public void updateAction(Action act);

	/**
	 * Gets the rule group by id.
	 * @param id the id
	 * @return RuleGroup object
	 */
	public RuleGroup getRuleGroupById(Integer id);

	/**
	 * Gets the rule group by name.
	 * @param name the name
	 * @return RuleGroup object
	 */
	public RuleGroup getRuleGroupByName(String name);

	/**
	 * Gets the rule by name.
	 * @param name the name
	 * @return Rules object
	 */
	public Rules getRuleByName(String name);

	/**
	 * Returns collection of all ruleGroups.
	 * @return List
	 */
	public List<RuleGroup> getRuleGroupList();

	/**
	 * Returns collection of all RuleType.
	 * @return List
	 */
	public List<RuleType> getRuleTypeList();

}