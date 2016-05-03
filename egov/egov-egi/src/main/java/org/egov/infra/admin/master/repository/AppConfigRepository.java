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

import org.egov.infra.admin.master.entity.AppConfig;
import org.egov.infra.admin.master.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppConfigRepository extends JpaRepository<AppConfig, Long> {

    AppConfig findByIdAndModule_Id(final Long keyid, final Long moduleid);

    AppConfig findByKeyNameAndModule_Name(final String keyName, final String moduleName);

    AppConfig findById(final Long id);

    // @Query("select b from AppConfig b where b.module.id=:id")
    List<AppConfig> findByModule_Id(final Long id);

    AppConfig findBykeyName(final String keyName);

    List<AppConfig> findByModule_Name(final String moduleName);

    @Query("select distinct(a.module.name) from AppConfig a order by a.module.name")
    List<String> getAllAppConfigModule();

    @Query("select app from AppConfig app where app.id = :keyid and app.module.id = :moduleid")
    public AppConfig findBykeyIdAndModuleId(@Param("keyid") Long keyid, @Param("moduleid") Long moduleid);

    @Query("select app from AppConfig app where app.keyName = :keyName and app.module.name = :moduleName")
    public AppConfig getConfigKeyByName(@Param("keyName") String keyName, @Param("moduleName") String moduleName);

    @Query("select b from AppConfig b where b.module.id=:id")
    List<AppConfig> findAllByModuleId(@Param("id") Long id);

    @Query("select b from Module b where  b.enabled=true AND "
            + "(b.parentModule IS NULL OR (b.parentModule IN (select c.id from Module c where c.parentModule IS NULL ))) "
            + "AND  UPPER(b.name) like UPPER(:name) order by b.id")
    List<Module> findByNameContainingIgnoreCase(@Param("name") String name);

    @Query("select b from Module b where  b.enabled=true AND "
            + "(b.parentModule IS NULL OR (b.parentModule IN (select c.id from Module c where c.parentModule IS NULL ))) "
            + " order by b.name")
    List<Module> findAllModules();

    @Query("select b from Module b where  b.enabled=true AND b.id=(:id)")
    Module findByModuleById(@Param("id") Long id);

    @Query("select b from AppConfig b where b.module.name=:moduleName")
    List<AppConfig> getAppConfigKeys(@Param("moduleName") String moduleName);

}
