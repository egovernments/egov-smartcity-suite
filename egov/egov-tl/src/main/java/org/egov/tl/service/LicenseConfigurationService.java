/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

package org.egov.tl.service;

import org.egov.tl.entity.LicenseConfiguration;
import org.egov.tl.repository.LicenseConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.egov.infra.utils.ApplicationConstant.COMMA;


@Service
@Transactional(readOnly = true)
public class LicenseConfigurationService {
    private static final String INCLUDE_DIGITAL_SIGN_KEY = "INCLUDE_DIGITAL_SIGN_WORKFLOW";
    private static final String DEPARTMENT_FOR_GENERATEBILL = "DEPARTMENT_FOR_GENERATEBILL";
    private static final String NOTIFY_ON_DEMAND_GENERATION = "NOTIFY_ON_DEMAND_GENERATION";
    private static final String NEW_APPTYPE_DEFAULT_SLA = "NEW_APPTYPE_DEFAULT_SLA";
    private static final String RENEW_APPTYPE_DEFAULT_SLA = "RENEW_APPTYPE_DEFAULT_SLA";
    private static final String CLOSURE_APPTYPE_DEFAULT_SLA = "CLOSURE_APPTYPE_DEFAULT_SLA";
    private static final String FEE_COLLECTOR_ROLES = "FEE_COLLECTOR_ROLES";
    private static final String CHEQUE_BOUNCE_PENALTY = "CHEQUE_BOUNCE_PENALTY";
    private static final String JURISDICTION_BASED_ROUTING = "JURISDICTION_BASED_ROUTING";

    @Autowired
    private LicenseConfigurationRepository licenseConfigurationRepository;

    public boolean digitalSignEnabled() {
        return Boolean.valueOf(getValueByKey(INCLUDE_DIGITAL_SIGN_KEY));
    }

    public boolean notifyOnDemandGeneration() {
        return Boolean.valueOf(getValueByKey(NOTIFY_ON_DEMAND_GENERATION));
    }

    public String getDepartmentCodeForBillGenerate() {
        return getValueByKey(DEPARTMENT_FOR_GENERATEBILL);
    }

    public Integer getNewAppTypeSla() {
        return Integer.valueOf(getValueByKey(NEW_APPTYPE_DEFAULT_SLA));
    }

    public Integer getRenewAppTypeSla() {
        return Integer.valueOf(getValueByKey(RENEW_APPTYPE_DEFAULT_SLA));
    }

    public Integer getClosureAppTypeSla() {
        return Integer.valueOf(getValueByKey(CLOSURE_APPTYPE_DEFAULT_SLA));
    }

    public String[] getFeeCollectorRoles() {
        return getValueByKey(FEE_COLLECTOR_ROLES).split(COMMA);
    }

    public boolean jurisdictionBasedRoutingEnabled() {
        return Boolean.valueOf(getValueByKey(JURISDICTION_BASED_ROUTING));
    }

    public BigDecimal chequeBouncePenalty() {
        return new BigDecimal(getValueByKey(CHEQUE_BOUNCE_PENALTY));
    }

    public String getValueByKey(String key) {
        return getConfigurationByKey(key).getValue();
    }

    public LicenseConfiguration getConfigurationByKey(String key) {
        return licenseConfigurationRepository.findByKey(key);
    }
}