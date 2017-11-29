/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */

package org.egov.infra.admin.master.service;

import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.repository.AppConfigValueRepository;
import org.egov.infra.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AppConfigValueService {

    private final AppConfigValueRepository appConfigValueRepository;

    @Autowired
    public AppConfigValueService(final AppConfigValueRepository appConfigValueRepos) {
        appConfigValueRepository = appConfigValueRepos;
    }

    public AppConfigValues getById(final Long id) {
        return appConfigValueRepository.getOne(id);
    }

    public List<AppConfigValues> getConfigValuesByModuleAndKey(final String moduleName, final String keyName) {
        return appConfigValueRepository.findByConfig_KeyNameAndConfig_Module_Name(keyName, moduleName);
    }

    public List<AppConfigValues> getConfigValuesByModuleAndKeyLike(final String moduleName, final String keyName) {
        return appConfigValueRepository.findByConfig_KeyNameLikeAndConfig_Module_Name(keyName, moduleName);
    }

    public List<AppConfigValues> getConfigValuesByModuleAndKeyByValueAsc(final String moduleName, final String keyName) {
        return appConfigValueRepository.findByConfig_KeyNameAndConfig_Module_NameOrderByValueAsc(keyName, moduleName);
    }

    public AppConfigValues getAppConfigValueByDate(final String moduleName, final String keyName, final Date effectiveFrom) {
        final Date[] dateRange = DateUtils.constructDateRange(effectiveFrom, effectiveFrom);
        final Date fromDate = dateRange[0];
        final Date toDate = dateRange[1];

        final List<AppConfigValues> appConfigValues = appConfigValueRepository.getAppConfigValueByModuleAndKeyAndDate(moduleName,
                keyName, effectiveFrom, fromDate, toDate);
        return appConfigValues.isEmpty() ? null : appConfigValues.get(appConfigValues.size() - 1);
    }

    public String getAppConfigValue(final String moduleName, final String keyName, final String defaultVal) {
        final Date effectiveFrom = new Date();
        final Date[] dateRange = DateUtils.constructDateRange(effectiveFrom, effectiveFrom);
        final Date fromDate = dateRange[0];
        final Date toDate = dateRange[1];
        final List<AppConfigValues> appConfigValues = appConfigValueRepository.getAppConfigValueByModuleAndKeyAndDate(moduleName,
                keyName, effectiveFrom, fromDate, toDate);
        return appConfigValues.isEmpty() ? defaultVal : appConfigValues.get(appConfigValues.size() - 1).toString();

    }

}
