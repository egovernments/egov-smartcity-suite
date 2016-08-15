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

package org.egov.infra.admin.master.service;


import org.egov.infra.admin.master.entity.AppConfig;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.repository.AppConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@Transactional(readOnly = true)
public class AppConfigService {

    private final AppConfigRepository appConfigRepository;

    @Autowired
    public AppConfigService(final AppConfigRepository appConfigRepository) {
        this.appConfigRepository = appConfigRepository;
    }

    public AppConfig getAppConfigByModuleNameAndKeyName(final String moduleName, final String keyName) {
        return appConfigRepository.findByModule_NameAndKeyName(moduleName, keyName);
    }

    public AppConfig getAppConfigByKeyName(final String keyName) {
        return appConfigRepository.findByKeyName(keyName);
    }

    public List<AppConfig> getAllAppConfigByModuleName(final String moduleName) {
        return appConfigRepository.findByModule_Name(moduleName);
    }

    public Page<AppConfig> getAllAppConfig(String moduleName, final Integer pageNumber, final Integer pageSize) {
        final Pageable pageable = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.ASC, "module.name");
        return isBlank(moduleName) ? appConfigRepository.findAll(pageable) : appConfigRepository.findByModule_Name(moduleName, pageable);
    }

    @Transactional
    public void createAppConfig(AppConfig appConfig) {
        for(AppConfigValues configValue : appConfig.getConfValues()) {
            configValue.setConfig(appConfig);
        }
        appConfigRepository.save(appConfig);
    }

    @Transactional
    public void updateAppConfig(AppConfig appConfig) {
        final List<AppConfigValues> newConfigVaues = new ArrayList<>(appConfig.getConfValues());
        appConfig.getConfValues().clear();
        appConfig.setConfValues(newConfigVaues);
        appConfigRepository.save(appConfig);
    }
}
