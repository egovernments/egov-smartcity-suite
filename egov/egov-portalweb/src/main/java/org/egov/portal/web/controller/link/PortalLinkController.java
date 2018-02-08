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
package org.egov.portal.web.controller.link;

import static java.lang.String.format;
import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.egov.portal.util.constant.PortalConstants.PROPERTY_TAX;
import static org.egov.portal.util.constant.PortalConstants.PTIS_URL;
import static org.egov.portal.util.constant.PortalConstants.SEWERAGE_TAX;
import static org.egov.portal.util.constant.PortalConstants.STMS_URL;
import static org.egov.portal.util.constant.PortalConstants.WATER_CHARGES;
import static org.egov.portal.util.constant.PortalConstants.WTMS_URL;

import javax.servlet.http.HttpServletRequest;

import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.StringUtils;
import org.egov.infra.web.utils.WebUtils;
import org.egov.portal.entity.PortalLink;
import org.egov.portal.entity.PortalLinkRequest;
import org.egov.portal.entity.PortalLinkResponse;
import org.egov.portal.entity.SearchConnectionRequest;
import org.egov.portal.entity.SearchPropertyRequest;
import org.egov.portal.service.PortalLinkService;
import org.egov.portal.util.constant.PortalConstants;
import org.egov.portal.web.adaptor.PortallinkJsonAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class PortalLinkController {

    private static final String REFERER_IP = "103.21.58.98";
    @Autowired
    private PortalLinkService portalLinkService;
    @Autowired
    private SecurityUtils securityUtils;

    @RequestMapping(value = "/citizen/searchforlinking", method = RequestMethod.GET)
    public String searchForm(final Model model) {

        model.addAttribute("modulesList", PortalConstants.getModuleList());
        model.addAttribute("portalLinkRequest", new PortalLinkRequest());
        return "seachfor-link";
    }

    @RequestMapping(value = "/citizen/searchresult", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String searchResult(final Model model,
            final HttpServletRequest request, final PortalLinkRequest portalLinkRequest) {
        final RestTemplate restTemplate = new RestTemplate();
        String restURL = null;
        PortalLinkResponse response = null;
        if (portalLinkRequest.getModuleName() != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("REFERER", REFERER_IP);
            if (portalLinkRequest.getModuleName().equalsIgnoreCase(PROPERTY_TAX)) {
                SearchPropertyRequest searchPortalRequest = new SearchPropertyRequest();
                searchPortalRequest.setAssessmentNo(portalLinkRequest.getAssessmentNo());
                searchPortalRequest.setUlbCode(ApplicationThreadLocals.getCityCode());
                restURL = format(PTIS_URL,
                        WebUtils.extractRequestDomainURL(request, false));
                HttpEntity<SearchPropertyRequest> requestObj = new HttpEntity<>(searchPortalRequest, headers);
                response = restTemplate.postForObject(restURL, requestObj, PortalLinkResponse.class);
            } else if (portalLinkRequest.getModuleName().equalsIgnoreCase(WATER_CHARGES)) {
                SearchConnectionRequest searchConnectionRequest = new SearchConnectionRequest();
                searchConnectionRequest.setConsumerNo(portalLinkRequest.getAssessmentNo());
                searchConnectionRequest.setUlbCode(ApplicationThreadLocals.getCityCode());
                restURL = format(WTMS_URL,
                        WebUtils.extractRequestDomainURL(request, false));
                HttpEntity<SearchConnectionRequest> requestObj = new HttpEntity<>(searchConnectionRequest, headers);
                response = restTemplate.postForObject(restURL, requestObj, PortalLinkResponse.class);
            } else if (portalLinkRequest.getModuleName().equalsIgnoreCase(SEWERAGE_TAX)) {
                SearchConnectionRequest searchConnectionRequest = new SearchConnectionRequest();
                searchConnectionRequest.setConsumerNo(portalLinkRequest.getAssessmentNo());
                searchConnectionRequest.setUlbCode(ApplicationThreadLocals.getCityCode());
                restURL = format(STMS_URL,
                        WebUtils.extractRequestDomainURL(request, false));
                response = restTemplate.postForObject(restURL, searchConnectionRequest, PortalLinkResponse.class);
            }
            if (response != null)
                response.setModuleName(portalLinkRequest.getModuleName());

        }
        return new StringBuilder("{ \"data\":")
                .append(toJSON(response, PortallinkJsonAdaptor.class)).append("}")
                .toString();
    }

    @RequestMapping(value = "/citizen/linkconnection/", method = RequestMethod.POST)
    public String linkConnection(
            final String consumerCode, final String moduleName, final String applicantName,
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
            portalLink.setUrl(getViewURL(moduleName, consumerCode));
            portalLink.setApplicantName(applicantName);
            portalLink.setPaymentURL(getPaymentURL(moduleName, consumerCode));
            portalLink.setViewDcbURL(getViewDcbURL(moduleName, consumerCode));
            portalLinkService.link(portalLink);
            model.addAttribute("consumerCode", consumerCode);
            return "connectionlink-success";
        }
    }

    public String getPaymentURL(String moduleName, String consumerCode) {
        String paymenturl = StringUtils.EMPTY;
        if (moduleName.equalsIgnoreCase(PROPERTY_TAX))
            paymenturl = "/ptis/citizen/collection/collection-searchOwnerDetails.action?assessmentNumber=" + consumerCode + '&'
                    + "isCitizen=true";
        else if (moduleName.equalsIgnoreCase(WATER_CHARGES))
            paymenturl = "/wtms/application/generatebillOnline/" + consumerCode + "?applicationTypeCode=New connection";
        return paymenturl;

    }

    public String getViewURL(String moduleName, String consumerCode) {
        String viewurl = StringUtils.EMPTY;
        if (moduleName.equalsIgnoreCase(PROPERTY_TAX))
            viewurl = "/ptis/view/viewProperty-viewForm.action?propertyId=" + consumerCode;
        else if (moduleName.equalsIgnoreCase(WATER_CHARGES))
            viewurl = "/wtms/application/generatebillOnline/" + consumerCode;
        return viewurl;

    }

    public String getViewDcbURL(String moduleName, String consumerCode) {
        String viewurl = StringUtils.EMPTY;
        if (moduleName.equalsIgnoreCase(PROPERTY_TAX))
            viewurl = "/ptis/view/viewDCBProperty-displayPropInfo.action?propertyId=" + consumerCode;
        else if (moduleName.equalsIgnoreCase(WATER_CHARGES))
            viewurl = "/wtms/viewDcb/consumerCodeWis/" + consumerCode;
        return viewurl;

    }
}
