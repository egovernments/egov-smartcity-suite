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
package org.egov.portal.entity;

import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.entity.enums.UserType;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.workflow.entity.State;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PortalInboxBuilder {

    private final PortalInbox portalInbox;

    /***
     * 
     * @param module -- Mandatory. Module details.
     * @param serviceType --Mandatory. Service type.
     * @param applicationNumber -- Mandatory. Application number
     * @param consumerNumber -- Non mandatory field. Individual object consumer number.
     * @param entityId -- Main object reference id
     * @param headerMsg -- Brief detail about application
     * @param detailedMessage --Mandatory Detail about application
     * @param link -- Mandatory. Url required to view application.
     * @param isResolved -- true. If application processed completely.
     * @param status -- Mandatory field. Current application status
     * @param slaEndDate -- SLA end date
     * @param state -- Workflow state 
     * @param user -- List  Assignee user's
     */
    public PortalInboxBuilder(final Module module, final String serviceType, final String applicationNumber,
            final String consumerNumber, final Long entityId, final String headerMsg, final String detailedMessage,
            final String link, final boolean isResolved, final String status, final Date slaEndDate, final State state,
            final List<User> user) {
        portalInbox = new PortalInbox();
        portalInbox.setModule(module);
        portalInbox.setServiceType(serviceType);
        portalInbox.setApplicationNumber(applicationNumber);
        portalInbox.setEntityRefNumber(consumerNumber);
        portalInbox.setEntityRefId(entityId);
        portalInbox.setHeaderMessage(headerMsg);
        portalInbox.setResolved(isResolved);
        portalInbox.setDetailedMessage(detailedMessage);
        portalInbox.setLink(link);
        portalInbox.setRead(false);
        portalInbox.setStatus(status);
        portalInbox.setSlaEndDate(slaEndDate);
        portalInbox.setState(state);
        portalInbox.setApplicationDate(DateUtils.now());

        if (user != null && !user.isEmpty()) {
            for (User userObject : user) {
                if (UserType.BUSINESS.toString().equalsIgnoreCase(userObject.getType().toString()) || UserType.CITIZEN
                        .toString().equalsIgnoreCase(userObject.getType().toString())) {
                    PortalInboxUser portalInboxUser = new PortalInboxUser();
                    portalInboxUser.setUser(userObject);
                    portalInboxUser.setPortalInbox(portalInbox);
                    portalInbox.setTempPortalInboxUser(new ArrayList<PortalInboxUser>(Arrays.asList(portalInboxUser)));
                }
            }
        }
    }

    public PortalInbox build() throws ApplicationRuntimeException {
        validate();
        return portalInbox;
    }

    private void validate() throws ApplicationRuntimeException {
        // add validation for mandatory fields
        validateParams();
        if (portalInbox.getLink() == null || portalInbox.getLink().isEmpty())
            throw new ApplicationRuntimeException("Link is mandatory");
        if (portalInbox.getStatus() == null || portalInbox.getStatus().isEmpty())
            throw new ApplicationRuntimeException("Status is mandatory");
        }

    private void validateParams() {
        if (portalInbox.getModule() == null)
            throw new ApplicationRuntimeException("Module is mandatory");
        if (portalInbox.getServiceType() == null || portalInbox.getServiceType().isEmpty())
            throw new ApplicationRuntimeException("ServiceType is mandatory");
        if (portalInbox.getApplicationNumber() == null || portalInbox.getApplicationNumber().isEmpty())
            throw new ApplicationRuntimeException("ApplicationNumber is mandatory");
        if (portalInbox.getDetailedMessage() == null || portalInbox.getDetailedMessage().isEmpty())
            throw new ApplicationRuntimeException("DetailedMessage is mandatory");
    }

}
