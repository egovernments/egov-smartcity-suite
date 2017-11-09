/*
 * eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
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
 *           Further, all user interfaces, including but not limited to citizen facing interfaces,
 *           Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *           derived works should carry eGovernments Foundation logo on the top right corner.
 *
 * 	       For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 * 	       For any further queries on attribution, including queries on brand guidelines,
 *           please contact contact@egovernments.org
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

import org.apache.commons.lang3.time.DateUtils;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.portal.entity.PortalInboxBuilder;
import org.egov.portal.service.PortalInboxService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.utils.LicenseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

import static org.egov.tl.utils.Constants.TRADE_LICENSE;


@Service
public class LicenseCitizenPortalService {
    private static final String APPLICATION_VIEW_URL = "/tl/viewtradelicense/viewTradeLicense-view.action?applicationNo=%s";
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private PortalInboxService portalInboxService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private LicenseUtils licenseUtils;

    public void onCreate(TradeLicense tradeLicense) {
        final Module module = moduleService.getModuleByName(TRADE_LICENSE);
        final PortalInboxBuilder portalInboxBuilder = new PortalInboxBuilder(module,
                tradeLicense.getState().getNatureOfTask(),
                tradeLicense.getApplicationNumber(), tradeLicense.getLicenseNumber() != null ? tradeLicense.getLicenseNumber() : tradeLicense.getApplicationNumber(),
                tradeLicense.getId(), tradeLicense.getStateType(), getDetailedMessage(tradeLicense),
                String.format(APPLICATION_VIEW_URL, tradeLicense.getApplicationNumber()),
                tradeLicense.transitionCompleted(), tradeLicense.getStatus().getName(),
                DateUtils.addHours(new Date(), licenseUtils.getSlaForAppType(tradeLicense.getLicenseAppType())), tradeLicense.getState(),
                Arrays.asList(securityUtils.getCurrentUser()));
        portalInboxService.pushInboxMessage(portalInboxBuilder.build());
    }

    private String getDetailedMessage(final TradeLicense tradeLicense) {
        final StringBuilder detailedMessage = new StringBuilder();
        if (tradeLicense.getLicenseAppType() != null)
            detailedMessage.append("Application Number -").append(tradeLicense.getApplicationNumber()).append(" regarding ")
                    .append(tradeLicense.getState().getNatureOfTask() + " ").append(" is initiated");
        return detailedMessage.toString();
    }

    public void onUpdate(TradeLicense tradeLicense) {
        portalInboxService.updateInboxMessage(tradeLicense.getApplicationNumber(), moduleService.getModuleByName(TRADE_LICENSE).getId(),
                tradeLicense.getStatus().getName(), tradeLicense.transitionCompleted(), null, tradeLicense.getState(), tradeLicense.getCreatedBy(),
                tradeLicense.getLicenseNumber() != null ? tradeLicense.getLicenseNumber() : tradeLicense.getApplicationNumber(),
                String.format(APPLICATION_VIEW_URL, tradeLicense.getApplicationNumber()));
    }
}
