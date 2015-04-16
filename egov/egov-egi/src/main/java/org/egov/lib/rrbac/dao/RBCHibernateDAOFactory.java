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
package org.egov.lib.rrbac.dao;

import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rrbac.model.Entity;
import org.egov.lib.rrbac.model.RuleGroup;
import org.egov.lib.rrbac.model.RuleType;
import org.egov.lib.rrbac.model.Rules;
import org.egov.lib.rrbac.model.Task;
import org.hibernate.Session;

/**
 * Returns Hibernate-specific instances of DAOs.
 * <p>
 * One of the responsiblities of the factory is to inject a Hibernate Session
 * into the DAOs. You can customize the getCurrentSession() method if you
 * are not using the default strategy, which simply delegates to
 * <tt>HibernateUtil.getCurrentSession()</tt>, and also starts a transaction
 * lazily, if none exists already for the current thread or current EJB.
 * <p>
 * If for a particular DAO there is no additional non-CRUD functionality, we use
 * an inner class to implement the interface in a generic way. This allows clean
 * refactoring later on, should the interface implement business data access
 * methods at some later time. Then, we would externalize the implementation into
 * its own first-level class. We can't use anonymous inner classes for this trick
 * because they can't extend or implement an interface and they can't include
 * constructors.
 *
 */
public class RBCHibernateDAOFactory extends RBCDAOFactory {
	
	
	public EntityDAO getEntityDAO() {
		return new EntityHibernateDAO(Entity.class, getCurrentSession());
	}
	
	public TaskDAO getTaskDAO() {
		return new TaskHibernateDAO(Task.class, getCurrentSession());
	}
	
	public ActionDAO getActionDAO() {
		return new ActionHibernateDAO();
	}
	
	public RuleGroupDAO getRuleGroupDAO() {
		return new RuleGroupHibernateDAO(RuleGroup.class, getCurrentSession());
	}
	
	public RuleTypeDAO getRuleTypeDAO() {
		return new RuleTypeHibernateDAO(RuleType.class, getCurrentSession());
	}
	
	public RulesDAO getRulesDAO() {
		return new RulesHibernateDAO(Rules.class, getCurrentSession());
	}
	
	protected Session getCurrentSession() {
		// Get a Session and begin a database transaction. If the current
		// thread/EJB already has an open Sessio n and an ongoing Transaction,
		// this is a no-op and only returns a reference to the current Session.
		// HibernateUtil.beginTransaction();
		return HibernateUtil.getCurrentSession();
	}
	
}
