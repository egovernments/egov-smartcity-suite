/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
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
 */
package org.egov.infra.admin.master.repository;

import org.egov.infra.admin.master.entity.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Set;

public class ModuleRepositoryImpl implements ModuleRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Object[]> fetchModulesForRoles(final Set<Role> roles) {
        final StringBuffer sql = new StringBuffer()
                .append("SELECT DISTINCT mod.name,mod.contextRoot,mod.displayName, mod.id, mod.ordernumber FROM eg_module mod,eg_action act ")
                .append("WHERE act.id IN (SELECT DISTINCT actionid FROM eg_roleaction WHERE roleid IN ( ");
        roles.parallelStream().forEach(role -> sql.append("?,"));
        sql.deleteCharAt(sql.length() - 1);
        sql.append(
                "))  AND mod.enabled=true AND act.enabled = true AND act.application=mod.id AND mod.parentmodule is null ORDER BY mod.ordernumber ASC");
        final Query query = entityManager.createNativeQuery(sql.toString());

        int i = 1;
        for (final Role role : roles)
            query.setParameter(i++, role.getId());
        return query.getResultList();
    }

    @Override
    public List<Object[]> fetchModulesByParentModuleId(final Long parentId, final Long userId) {

        final StringBuffer sql = new StringBuffer()
                .append("SELECT DISTINCT view_ram.module_id as id,view_ram.module_name as name,null as url,view_ram.typeflag as typeflag,view_ram.context_root as ctx_root,view_ram.order_number as ordernumber ")
                .append("FROM VIEW_EG_MENULINK view_ram WHERE  view_ram.parent_id =? and view_ram.typeflag='M' and view_ram.is_enabled=true ")
                .append("AND EXISTS (SELECT action.id FROM eg_action action, eg_roleaction roleaction where action.parentmodule = view_ram.module_id ")
                .append("AND action.enabled = true AND action.id = roleaction.actionid  AND roleaction.roleid IN (SELECT roleid FROM eg_userrole userrole ")
                .append("WHERE userrole.userid = ?) UNION (SELECT module.id FROM eg_module module WHERE module.parentmodule = view_ram.module_id AND module.enabled=true) ) ")
                .append("UNION SELECT distinct view_ram.action_id as id,view_ram.action_name as name,view_ram.action_url as url,view_ram.typeflag as typeflag, ")
                .append("view_ram.context_root as ctx_root,view_ram.order_number as ordernumber FROM VIEW_EG_MENULINK view_ram where   parent_id = ? and typeflag='A' ")
                .append("AND view_ram.is_enabled=true and (view_ram.action_id in (select actionid from eg_roleaction ra  where ra.roleid in ")
                .append("(select roleid from eg_userrole ur where ur.userid = ?))  OR NOT EXISTS (SELECT actionid FROM eg_roleaction ra ")
                .append("where actionid = view_ram.action_id)) order by typeflag desc,ordernumber asc");

        final Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter(1, parentId);
        query.setParameter(2, userId);
        query.setParameter(3, parentId);
        query.setParameter(4, userId);
        return query.getResultList();
    }

    @Override
    public List<Object[]> fetchUserFavourateModules(final Long userId) {
        final StringBuffer sql = new StringBuffer()
                .append("SELECT distinct view_ram.action_id,fav.name,fav.contextroot,view_ram.action_url ")
                .append("FROM VIEW_EG_MENULINK view_ram, EG_FAVOURITES fav WHERE  fav.actionid = view_ram.action_id and fav.userid = ? ")
                .append("and view_ram.typeflag='A' and view_ram.is_enabled=true GROUP BY view_ram.action_id,fav.name,fav.contextroot,view_ram.action_url");
        final Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter(1, userId);
        return query.getResultList();
    }
}
