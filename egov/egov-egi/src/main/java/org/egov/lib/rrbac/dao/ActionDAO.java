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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.lib.rrbac.model.Action;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class ActionDAO  {
	private static Logger LOGGER = LoggerFactory.getLogger(ActionDAO.class);

	@PersistenceContext
	private EntityManager entityManager;
    
	public Session  getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}
	
	
	public Action findActionByName(final String name) {
		final Query qry = getCurrentSession().createQuery("from org.egov.lib.rrbac.model.Action act where act.name =:name ");
		qry.setString("name", name);
		return (Action) qry.uniqueResult();
	}

	private List<Action> getQueryResult(String query, final String contextPath, final String url, final boolean hasContextPath) {
		if (hasContextPath) {
			query = query + " and upper(contextRoot) like :contextPath";
		}
		final Query qry = getCurrentSession().createQuery(query + "  order by urlOrderId desc");
		qry.setString("fullURL", url);
		if (hasContextPath) {
			qry.setString("contextPath", contextPath);
		}
		return qry.list();
	}

	
	public Action findActionByURL(String contextPath, final String url) {
		List<Action> queryResult = new ArrayList<Action>();
		Action actionForURL = null;
		LOGGER.info("findActionByURL(contextPath, url) : URL--" + url);
		final boolean hasContextPath = org.apache.commons.lang.StringUtils.isNotBlank(contextPath);
		if (hasContextPath) {
			contextPath = contextPath.startsWith("/") ? contextPath.substring(1).toUpperCase() : contextPath.toUpperCase();
		}
		// There are no query params with URL
		if (url.indexOf("?") == -1) {
			queryResult = this.getQueryResult("from org.egov.lib.rrbac.model.Action where replace(url,'/../'||contextRoot||'/','/') = :fullURL and queryParams is null", contextPath, url, hasContextPath);
			if (queryResult.isEmpty()) 
                            queryResult = this.getQueryResult("from org.egov.lib.rrbac.model.Action where :fullURL like url||'%'", contextPath, url, hasContextPath);
		} else { // There are query params exists with URL
			queryResult = this.getQueryResult("from org.egov.lib.rrbac.model.Action where :fullURL = replace(url,'/../'||contextRoot||'/','/')||'?'||queryParams ", contextPath, url, hasContextPath);
			if (queryResult.isEmpty()) {
				queryResult = this.getQueryResult("from org.egov.lib.rrbac.model.Action where :fullURL like replace(url,'/../'||contextRoot||'/','/')||'?'||'%' ", contextPath, url, hasContextPath);
			}
		}
		actionForURL = queryResult.isEmpty() ? null : queryResult.get(0);
		LOGGER.info("findActionByURL(contextPath, url) : actionForURL--" + actionForURL);
		return actionForURL;
	}
    
	
	public List<Action> getActionWithRoles() {
		try {
			return getCurrentSession().createQuery("from org.egov.lib.rrbac.model.Action act left join fetch act.roles").list();
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Error occurred at getActionWithRG. ", e);
		}
	}

	
	public List<Action> getActionWithRG() {
		try {
			return getCurrentSession().createQuery("from org.egov.lib.rrbac.model.Action act left join fetch act.ruleGroup").list();
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Error occurred at getActionWithRG. ", e);
		}
	}
	
	public Action findById(Integer id) {
	    return entityManager.find(Action.class, id);
	}
}
