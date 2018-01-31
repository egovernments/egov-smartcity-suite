package org.egov.portal.web.controller.link;

import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.StringUtils;
import org.egov.portal.entity.PortalLink;
import org.egov.portal.service.PortalLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PortalLinkController {

    @Autowired
    private PortalLinkService portalLinkService;
    @Autowired
    private SecurityUtils securityUtils;

    @RequestMapping(value = "/citizen/linkconnection/", method = RequestMethod.POST)
    public String linkConnection(
            final String consumerCode, final String moduleName, final String url, final String applicantName,
            final Model model) {
        if (portalLinkService.findByConsumerNumber(consumerCode) != null) {
            model.addAttribute("message", "connection.already.linked");
            return "common-error";
        } else {
            PortalLink portalLink = new PortalLink();
            portalLink.setConsumerNo(consumerCode);
            portalLink.setModuleName(moduleName);
            portalLink.setUser(securityUtils.getCurrentUser());
            portalLink.setIsActive(Boolean.TRUE);
            portalLink.setUrl(url);
            portalLink.setApplicantName(applicantName);
            portalLink.setPaymentURL(getPaymentURL(moduleName, consumerCode));
            portalLinkService.link(portalLink);
            model.addAttribute("consumerCode", consumerCode);
            return "connectionlink-success";
        }
    }

    public String getPaymentURL(String moduleName, String consumerCode) {
        String url = StringUtils.EMPTY;
        if (moduleName.equalsIgnoreCase("Property Tax"))
            url = "/ptis/citizen/collection/collection-searchOwnerDetails.action?assessmentNumber=" + consumerCode + '&'
                    + "isCitizen=true";
        else if (moduleName.equalsIgnoreCase("Water Charges"))
            url = "/wtms/application/generatebillOnline/" + consumerCode + "?applicationTypeCode=New connection";
        return url;

    }
}
