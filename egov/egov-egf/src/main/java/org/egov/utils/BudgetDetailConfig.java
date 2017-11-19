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
package org.egov.utils;

import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class BudgetDetailConfig {
    private static final String DELIMITER = ",";
    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;
    List<String> headerFields = new ArrayList<String>();
    List<String> gridFields = new ArrayList<String>();
    List<String> mandatoryFields = new ArrayList<String>();
    @Autowired
    private AppConfigValueService appConfigValueService;

    public BudgetDetailConfig() {
    }

    public final List<String> getGridFields() {
        return fetchAppConfigValues("budgetDetail.grid.component");
    }

    public final List<String> getMandatoryFields() {
        return fetchAppConfigValues("budgetDetail_mandatory_fields");
    }

    public final List<String> getHeaderFields() {
        return fetchAppConfigValues("budgetDetail.header.component");
    }

    final List<String> fetchAppConfigValues(final String keyName) {
        final List<AppConfigValues> appConfigValues = appConfigValueService
                .getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG, keyName);
        if (appConfigValues != null && !appConfigValues.isEmpty()) {
            for (AppConfigValues app : appConfigValues)
                return Arrays.asList(app.getValue().split(DELIMITER));

        }
        return new ArrayList<String>();
    }

    public final boolean shouldShowField(final List<String> fieldList, final String field) {
        return fieldList.isEmpty() || fieldList.contains(field);
    }

    public void checkHeaderMandatoryField(final Map<String, Object> valuesToBeChecked) {
        for (final Entry<String, Object> entry : valuesToBeChecked.entrySet())
            if (headerFields.contains(entry.getKey()) && mandatoryFields.contains(entry.getKey()) && entry.getValue() == null)
                throw new ValidationException(Arrays.asList(new ValidationError("budgetDetail." + entry.getKey() + ".mandatory",
                        "budgetDetail." + entry.getKey() + ".mandatory")));
    }

    public void checkGridMandatoryField(final Map<String, Object> valuesToBeChecked) {
        for (final Entry<String, Object> entry : valuesToBeChecked.entrySet())
            if (gridFields.contains(entry.getKey()) && mandatoryFields.contains(entry.getKey()) && entry.getValue() == null)
                throw new ValidationException(Arrays.asList(new ValidationError("budgetDetail." + entry.getKey() + ".mandatory",
                        "budgetDetail." + entry.getKey() + ".mandatory")));
    }
}
