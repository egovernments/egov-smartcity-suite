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
package org.egov.infstr.commons.dao;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.infra.admin.common.entity.MenuLink;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.Role;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("moduleDAO")
@Transactional(readOnly = true)
public class ModuleService {

    @PersistenceContext
    private EntityManager entityManager;

    private Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

   
    public Module getModuleByName(final String moduleName) {
        final Query qry = getCurrentSession().createQuery("from Module M where M.moduleName=:moduleName");
        qry.setString("moduleName", moduleName);
        return (Module) qry.uniqueResult();
    }

    /**
     * returns module info based on roleIds
     * 
     * @param roles
     * @return moduleInfoMap
     */
   
    public List<MenuLink> getModuleInfoForRoleIds(final Set<Role> roles) {

        final StringBuffer sql = new StringBuffer();

        sql.append("SELECT DISTINCT mod.module_name,mod.baseurl,mod.module_desc, ");
        sql.append("mod.id_module FROM eg_module mod,eg_action act,eg_roleaction_map ram ");
        // removed "AND act.module_id=mod.id_module" from below line to fix
        // module not being
        // displayed if at least 1 action is mapped to root module
        sql.append("WHERE act.id=ram.actionid ");
        sql.append("AND act.id IN (SELECT DISTINCT actionid FROM eg_roleaction_map ");
        sql.append("WHERE roleid IN ( ");

        final int size = roles.size();
        for (int x = 0; x < size; x++)
            sql.append("?,");
        sql.deleteCharAt(sql.length() - 1);
        sql.append("))");
        sql.append(" AND mod.isenabled=1 AND (mod.parentid is null OR mod.id_module = mod.parentid)");
        sql.append(" ORDER BY mod.module_name ASC");
        final SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());

        int i = 0;
        final Iterator<Role> role = roles.iterator();
        while (role.hasNext())
            query.setLong(i++, role.next().getId());

        final List<MenuLink> menuLinks = new LinkedList<MenuLink>();
        final Iterator<Object[]> elements = query.list().iterator();
        while (elements.hasNext()) {
            final Object[] element = elements.next();
            final MenuLink menuLink = new MenuLink();
            menuLink.setName(element[0] != null ? element[0].toString() : "");
            menuLink.setUrl(element[1] != null ? element[1].toString() : "");
            menuLink.setDisplayName(element[2] != null ? element[2].toString() : "");
            menuLink.setId(Long.valueOf(element[3] != null ? element[3].toString() : "0"));
            menuLinks.add(menuLink);
        }
        return menuLinks;
    }

   
    public List<MenuLink> getApplicationModuleByParentId(final Long parentId, final Long userId) {

        final StringBuffer sql = new StringBuffer();

        sql.append(
                "SELECT DISTINCT view_ram.module_id as id,view_ram.module_name as name,null as url,view_ram.typeflag as typeflag,view_ram.context_root as ctx_root,view_ram.order_number as ordernumber ");
        sql.append(
                "FROM V_EG_ROLE_ACTION_MODULE_MAP view_ram WHERE  view_ram.parent_id =? and view_ram.typeflag='M' and view_ram.is_enabled=1 ");
        sql.append(
                "AND EXISTS (SELECT action.id FROM eg_action action, eg_roleaction_map roleaction where action.module_id = view_ram.module_id ");
        sql.append(
                "AND action.is_enabled = 1 AND action.id = roleaction.actionid  AND roleaction.roleid IN (SELECT roleid FROM eg_userrole userrole ");
        sql.append(
                "WHERE userrole.userid = ?) UNION (SELECT module.id_module FROM eg_module module WHERE module.parentid = view_ram.module_id AND module.isenabled=1) ) ");
        sql.append(
                "UNION SELECT distinct view_ram.action_id as id,view_ram.action_name as name,view_ram.action_url as url,view_ram.typeflag as typeflag, ");
        sql.append(
                "view_ram.context_root as ctx_root,view_ram.order_number as ordernumber FROM V_EG_ROLE_ACTION_MODULE_MAP view_ram where   parent_id = ? and typeflag='A' ");
        sql.append(
                "AND view_ram.is_enabled=1 and (view_ram.action_id in (select actionid from eg_roleaction_map ra  where ra.roleid in ");
        sql.append(
                "(select roleid from eg_userrole ur where ur.userid = ?))  OR NOT EXISTS (SELECT actionid FROM eg_roleaction_map ra ");
        sql.append("where actionid = view_ram.action_id)) order by typeflag desc,name asc");

        final SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
        query.setLong(0, parentId);
        query.setLong(1, userId);
        query.setLong(2, parentId);
        query.setLong(3, userId);
        final List<MenuLink> menuLinks = new LinkedList<MenuLink>();
        final Iterator<Object[]> elements = query.list().iterator();
        while (elements.hasNext()) {
            final Object[] element = elements.next();
            final MenuLink menuLink = new MenuLink();
            menuLink.setId(Long.valueOf(element[0] != null ? element[0].toString() : "0"));
            menuLink.setName(element[1] != null ? element[1].toString() : "");
            menuLink.setUrl(element[2] != null ? element[2].toString() : "");
            menuLink.setEnabled(element[3] != null ? element[3].toString().equals("A") ? true : false : false);
            menuLink.setContextRoot(element[4] != null ? element[4].toString() : "");
            menuLinks.add(menuLink);
        }
        return menuLinks;
    }

   
    public List<MenuLink> getUserFavourites(final Long userId) {
        final StringBuffer sql = new StringBuffer();
        sql.append("SELECT distinct view_ram.action_id,fav.name,fav.contextroot,view_ram.action_url ");
        sql.append(
                "FROM V_EG_ROLE_ACTION_MODULE_MAP view_ram, EG_FAVOURITES fav WHERE  fav.actionid = view_ram.action_id and fav.userid = ? ");
        sql.append(
                "and view_ram.typeflag='A' and view_ram.is_enabled=1 GROUP BY view_ram.action_id,fav.name,fav.contextroot,view_ram.action_url");
        final SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
        query.setLong(0, userId);
        final List<MenuLink> menuLinks = new LinkedList<MenuLink>();
        final Iterator<Object[]> elements = query.list().iterator();
        while (elements.hasNext()) {
            final Object[] element = elements.next();
            final MenuLink menuLink = new MenuLink();
            menuLink.setId(Long.valueOf(element[0] != null ? element[0].toString() : "0"));
            menuLink.setName(element[1] != null ? element[1].toString() : "");
            menuLink.setUrl(element[2] != null && element[3] != null ? element[2].toString() + element[3].toString() : "");
            menuLinks.add(menuLink);
        }
        return menuLinks;
    }

}
