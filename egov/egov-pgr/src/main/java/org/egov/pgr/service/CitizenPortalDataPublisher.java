/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *  
 *  Copyright (C) 2017  eGovernments Foundation
 *  
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *  
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *  
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *  
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *  
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *  
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.pgr.service;

import org.apache.commons.lang3.time.DateUtils;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.pgr.entity.Complaint;
import org.egov.portal.entity.PortalInboxBuilder;
import org.egov.portal.service.PortalInboxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINT_REGISTERED;
import static org.egov.pgr.utils.constants.PGRConstants.MODULE_NAME;

@Service
public class CitizenPortalDataPublisher {

    private static final String COMPLAINT_UPDATE_URL = "/pgr/complaint/update/%s";

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private PortalInboxService portalInboxService;

    @Autowired
    private SecurityUtils securityUtils;

    @Value("${default.resolution.time}")
    private Integer defaultSLAHours;

    public void onRegistration(Complaint complaint) {
        Integer slaHours = complaint.getComplaintType().getSlaHours();
        String messageHeader;
        if (COMPLAINT_REGISTERED.equals(complaint.getStatus().getName()))
            messageHeader = "Grievance Recorded";
        else
            messageHeader = "Grievance Redressal";

        StringBuilder detailedMessage = new StringBuilder()
                .append("Complaint Type : ").append(complaint.getComplaintType().getName())
                .append(" in ").append(complaint.getLocation().getName());
        PortalInboxBuilder portalInboxBuilder = new PortalInboxBuilder(moduleService.getModuleByName(MODULE_NAME),
                complaint.getStateType(), complaint.getCrn(), complaint.getCrn(), complaint.getId(),
                messageHeader, detailedMessage.toString(), String.format(COMPLAINT_UPDATE_URL, complaint.getCrn()),
                false, complaint.getStatus().getName(),
                DateUtils.addHours(new Date(), slaHours == null ? defaultSLAHours : slaHours), complaint.getState(),
                Arrays.asList(securityUtils.getCurrentUser()));

        portalInboxService.pushInboxMessage(portalInboxBuilder.build());
    }


    public void onUpdation(Complaint complaint) {
        portalInboxService.updateInboxMessage(complaint.getCrn(), moduleService.getModuleByName(MODULE_NAME).getId(),
                complaint.getStatus().getName(), complaint.completed(), null, complaint.getState(), complaint.getCreatedBy(),
                complaint.getCrn(), String.format(COMPLAINT_UPDATE_URL, complaint.getCrn()));
    }
}
