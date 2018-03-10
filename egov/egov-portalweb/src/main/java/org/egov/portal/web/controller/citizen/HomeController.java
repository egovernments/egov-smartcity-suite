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
package org.egov.portal.web.controller.citizen;

import static org.egov.infra.persistence.entity.enums.UserType.BUSINESS;
import static org.egov.infra.persistence.entity.enums.UserType.CITIZEN;

import java.util.List;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.portal.entity.CitizenInbox;
import org.egov.portal.entity.PortalInboxUser;
import org.egov.portal.entity.PortalLink;
import org.egov.portal.service.CitizenInboxService;
import org.egov.portal.service.PortalInboxUserService;
import org.egov.portal.service.PortalLinkService;
import org.egov.portal.service.PortalServiceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/home")
public class HomeController {

    @Autowired
    private CitizenInboxService citizenInboxService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private PortalInboxUserService portalInboxUserService;

    @Autowired
    private PortalServiceTypeService portalServiceTypeService;

    @Autowired
    private CityService cityService;
    
    @Autowired
    private PortalLinkService portalLinkService;

    @RequestMapping(method = RequestMethod.GET)
    public String showHomePage(ModelMap modelData) {
        return setupHomePage(modelData);
    }

    @RequestMapping(value = "/refreshInbox", method = RequestMethod.GET)
    public @ResponseBody Integer refreshInbox(@RequestParam final Long citizenInboxId) {
        final CitizenInbox citizenInbox = citizenInboxService.getInboxMessageById(citizenInboxId);
        citizenInbox.setRead(true);
        citizenInboxService.updateMessage(citizenInbox);
        return citizenInboxService.findUnreadMessagesCount(securityUtils.getCurrentUser());
    }

    private String setupHomePage(final ModelMap modelData) {
        String moduleName = "moduleNames";
        String services = "services";
        final User user = securityUtils.getCurrentUser();
        modelData.addAttribute("unreadMessageCount", getUnreadMessageCount());
        modelData.addAttribute("inboxMessages", getAllInboxMessages());
        modelData.addAttribute("myAccountMessages", getMyAccountMessages());
        modelData.addAttribute("cityLogo", cityService.getCityLogoURL());
        modelData.addAttribute("cityName", cityService.getMunicipalityName());
        modelData.addAttribute("userName", user.getName() == null ? "Anonymous" : user.getName());

        if (null != user) {

            if (user.getType().equals(BUSINESS)) {
                modelData.addAttribute(moduleName, portalServiceTypeService.getDistinctModuleNamesForBusinessUser());
                modelData.addAttribute(services, portalServiceTypeService.findAllServiceTypesForBusinessUser());
            } else if (user.getType().equals(CITIZEN)) {
                modelData.addAttribute(moduleName, portalServiceTypeService.getDistinctModuleNamesForCitizen());
                modelData.addAttribute(services, portalServiceTypeService.findAllServiceTypesForCitizenUser());
            } else {
                modelData.addAttribute(moduleName, portalServiceTypeService.getDistinctModuleNames());
                modelData.addAttribute(services, portalServiceTypeService.getAllPortalService());
            }
        }

        modelData.addAttribute("distinctModuleNames", portalServiceTypeService.getAllModules());
        modelData.addAttribute("userId", user.getId());

        List<PortalInboxUser> totalServicesApplied = portalInboxUserService.getPortalInboxByUserId(user.getId());
        List<PortalInboxUser> totalServicesCompleted = portalInboxUserService.getPortalInboxByResolved(user.getId(), true);
        List<PortalInboxUser> totalServicesPending = portalInboxUserService.getPortalInboxByResolved(user.getId(), false);
        List<PortalLink> totalServicesLinked = portalLinkService.findByUser(user);
        modelData.addAttribute("totalServicesLinked",totalServicesLinked);
        modelData.addAttribute("totalServicesPending", totalServicesPending);
        modelData.addAttribute("totalServicesApplied", totalServicesApplied);
        modelData.addAttribute("totalServicesCompleted", totalServicesCompleted);

        modelData.addAttribute("totalServicesPendingSize", totalServicesPending.size());
        modelData.addAttribute("totalServicesAppliedSize", totalServicesApplied.size());
        modelData.addAttribute("totalServicesCompletedSize", totalServicesCompleted.size());
        return "citizen-home";
    }

    private List<CitizenInbox> getMyAccountMessages() {
        return citizenInboxService.findMyAccountMessages(securityUtils.getCurrentUser());
    }

    private List<CitizenInbox> getAllInboxMessages() {
        return citizenInboxService.findAllInboxMessage(securityUtils.getCurrentUser());
    }

    private Integer getUnreadMessageCount() {
        return citizenInboxService.findUnreadMessagesCount(securityUtils.getCurrentUser());
    }

}
