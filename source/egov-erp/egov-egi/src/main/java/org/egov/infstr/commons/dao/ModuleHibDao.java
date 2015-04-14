/*
 * @(#)ModuleHibDao.java 3.0, 17 Jun, 2013 11:26:17 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.commons.dao;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.infra.admin.master.entity.Role;
import org.egov.infstr.commons.Module;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("moduleDAO")
@Transactional(readOnly=true)
public class ModuleHibDao implements ModuleDao {

        @PersistenceContext
	private EntityManager entityManager;

	private Session getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}

	@Override
	public Module getModuleByName(final String moduleName) {
		final Query qry = getCurrentSession().createQuery("from Module M where M.moduleName=:moduleName");
		qry.setString("moduleName", moduleName);
		return (Module) qry.uniqueResult();
	}

	/**
	 * returns module info based on roleIds
	 * @param roles
	 * @return moduleInfoMap
	 */
	@Override
	public List getModuleInfoForRoleIds(final Set<Role> roles) {

		final StringBuffer sql = new StringBuffer();

		sql.append("SELECT DISTINCT mod.module_name,mod.baseurl,mod.module_desc, ");
		sql.append("mod.id_module FROM eg_module mod,eg_action act,eg_roleaction_map ram ");
		sql.append("WHERE act.id=ram.actionid AND act.module_id=mod.id_module ");
		sql.append("AND act.id IN (SELECT DISTINCT actionid FROM eg_roleaction_map ");
		sql.append("WHERE roleid IN ( ");

		final int size = roles.size();
		for (int x = 0; x < size; x++) {
			sql.append("?,");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append("))");
		sql.append(" AND mod.isenabled=1 AND (mod.parentid is null OR mod.id_module = mod.parentid)");
		sql.append(" ORDER BY mod.module_name ASC");
		final SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());

		int i = 0;
		final Iterator<Role> role = roles.iterator();
		while (role.hasNext()) {
			query.setLong(i++, role.next().getId());
		}

		final List<Module> moduleList = new LinkedList<Module>();
		final Iterator<Object[]> elements = query.list().iterator();
		while (elements.hasNext()) {
			final Object[] element = elements.next();
			final Module module = new Module();
			module.setModuleName(element[0] != null ? element[0].toString() : "");
			module.setBaseUrl(element[1] != null ? element[1].toString() : "");
			module.setModuleDescription(element[2] != null ? element[2].toString() : "");
			module.setId(Integer.valueOf(element[3] != null ? element[3].toString() : "0"));
			moduleList.add(module);
		}
		return moduleList;
	}

	@Override
	public List<Module> getApplicationModuleByParentId(final Integer parentId, final Long userId) {

		final StringBuffer sql = new StringBuffer(1200);

		sql.append("SELECT DISTINCT view_ram.module_id as id,view_ram.module_name as name,null as url,view_ram.typeflag as typeflag,view_ram.context_root as ctx_root,view_ram.order_number as ordernumber ");
		sql.append("FROM V_EG_ROLE_ACTION_MODULE_MAP view_ram WHERE  view_ram.parent_id =? and view_ram.typeflag='M' and view_ram.is_enabled=1 ");
		sql.append("AND EXISTS (SELECT action.id FROM eg_action action, eg_roleaction_map roleaction where action.module_id = view_ram.module_id ");
		sql.append("AND action.is_enabled = 1 AND action.id = roleaction.actionid  AND roleaction.roleid IN (SELECT role FROM eg_userrole userrole ");
		sql.append("WHERE userrole.user = ?) UNION (SELECT module.id_module FROM eg_module module WHERE module.parentid = view_ram.module_id AND module.isenabled=1) ) ");
		sql.append("UNION SELECT distinct view_ram.action_id as id,view_ram.action_name as name,view_ram.action_url as url,view_ram.typeflag as typeflag, ");
		sql.append("view_ram.context_root as ctx_root,view_ram.order_number as ordernumber FROM V_EG_ROLE_ACTION_MODULE_MAP view_ram where   parent_id = ? and typeflag='A' ");
		sql.append("AND view_ram.is_enabled=1 and (view_ram.action_id in (select actionid from eg_roleaction_map ra  where ra.roleid in ");
		sql.append("(select role from eg_userrole ur where ur.user = ?))  OR NOT EXISTS (SELECT actionid FROM eg_roleaction_map ra ");
		sql.append("where actionid = view_ram.action_id)) order by typeflag desc,name asc");

		final SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		query.setInteger(0, parentId);
		query.setLong(1, userId);
		query.setInteger(2, parentId);
		query.setLong(3, userId);
		final List<Module> moduleList = new LinkedList<Module>();
		final Iterator<Object[]> elements = query.list().iterator();
		while (elements.hasNext()) {
			final Object[] element = elements.next();
			final Module module = new Module();
			module.setId(Integer.valueOf(element[0] != null ? element[0].toString() : "0"));
			module.setModuleName(element[1] != null ? element[1].toString() : "");
			module.setBaseUrl(element[2] != null ? element[2].toString() : "");
			module.setIsEnabled(element[3] != null ? (element[3].toString().equals("A") ? true : false) : false);
			module.setContextRoot(element[4] != null ? element[4].toString() : "");
			moduleList.add(module);
		}
		return moduleList;
	}

	@Override
	public List<Module> getUserFavourites(final Long userId) {
		final StringBuffer sql = new StringBuffer();
		sql.append("SELECT distinct view_ram.action_id,fav.name,fav.contextroot,view_ram.action_url ");
		sql.append("FROM V_EG_ROLE_ACTION_MODULE_MAP view_ram, EG_FAVOURITES fav WHERE  fav.actionid = view_ram.action_id and fav.userid = ? ");
		sql.append("and view_ram.typeflag='A' and view_ram.is_enabled=1 GROUP BY view_ram.action_id,fav.name,fav.contextroot,view_ram.action_url");
		final SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		query.setLong(0, userId);
		final List<Module> moduleList = new LinkedList<Module>();
		final Iterator<Object[]> elements = query.list().iterator();
		while (elements.hasNext()) {
			final Object[] element = elements.next();
			final Module module = new Module();
			module.setId(Integer.valueOf(element[0] != null ? element[0].toString() : "0"));
			module.setModuleName(element[1] != null ? element[1].toString() : "");
			module.setBaseUrl(element[2] != null && element[3] != null ? element[2].toString() + element[3].toString() : "");
			moduleList.add(module);
		}
		return moduleList;
	}

}
