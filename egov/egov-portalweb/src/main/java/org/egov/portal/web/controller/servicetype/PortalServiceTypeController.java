package org.egov.portal.web.controller.servicetype;

import static org.egov.infra.utils.JsonUtils.toJSON;

import java.io.IOException;
import java.util.List;

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
