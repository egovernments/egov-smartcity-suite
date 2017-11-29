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
package org.egov.portal.web.controller.servicetype;

import org.egov.infra.exception.ApplicationException;
import org.egov.portal.entity.PortalServiceType;
import org.egov.portal.service.PortalServiceTypeService;
import org.egov.portal.web.adaptor.SearchPortalServiceTypeJasonAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

import static org.egov.infra.utils.JsonUtils.toJSON;

@Controller
@RequestMapping(value = "/portalservicetype")
public class PortalServiceTypeController {

    private static final String PORTAL_EDIT = "portalservicetype-edit";
    private static final String PORTAL_ACK = "portalservicetype-ack";

    @Autowired
    private PortalServiceTypeService portalServiceTypeService;

    @RequestMapping(value = "/ajaxboundary-servicesbymodule", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<String> getServiceNames(@RequestParam final Long moduleId) {
        return portalServiceTypeService.findAllServiceTypes(moduleId);
    }

    @GetMapping("/search")
    public String searchPortalServiceType(final Model model) throws ApplicationException {
        model.addAttribute("portalServiceType", new PortalServiceType());
        model.addAttribute("modules", portalServiceTypeService.getAllModules());
        return "portalservicetype-search";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchPortalServiceTypeToModify(@RequestParam final Long module, @RequestParam final String name) {
        return new StringBuilder("{ \"data\":")
                .append(toJSON(portalServiceTypeService.searchPortalServiceType(module, name),
                        PortalServiceType.class, SearchPortalServiceTypeJasonAdaptor.class))
                .append("}").toString();

    }

    @GetMapping(value = "/update/{id}")
    public String updatePortalServiceType(@PathVariable final Long id, final Model model) throws IOException {
        final PortalServiceType portalServiceType = portalServiceTypeService.getPortalServiceTypeById(id);
        model.addAttribute("portalServiceType", portalServiceType);
        model.addAttribute("mode", "edit");
        return PORTAL_EDIT;
    }

    @PostMapping("/update")
    public String update(@ModelAttribute final PortalServiceType portalServiceType, final Model model) {
        portalServiceTypeService.update(portalServiceType);
        model.addAttribute("name", portalServiceType.getName());
        return PORTAL_ACK;
    }
}
