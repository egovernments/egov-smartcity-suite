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

package org.egov.wtms.web.controller.masters;

import java.util.List;

import javax.validation.Valid;

import org.egov.wtms.masters.entity.PropertyPipeSize;
import org.egov.wtms.masters.service.PipeSizeService;
import org.egov.wtms.masters.service.PropertyPipeSizeService;
import org.egov.wtms.masters.service.PropertyTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/masters")
public class PropertyPipeSizeMasterController {

    private final PropertyTypeService propertyTypeService;

    private final PipeSizeService pipeSizeService;

    private final PropertyPipeSizeService propertyPipeSizeService;

    @Autowired
    public PropertyPipeSizeMasterController(final PropertyTypeService propertyTypeService,
            final PipeSizeService pipeSizeService, final PropertyPipeSizeService propertyPipeSizeService) {
        this.propertyTypeService = propertyTypeService;
        this.pipeSizeService = pipeSizeService;
        this.propertyPipeSizeService = propertyPipeSizeService;

    }

    @RequestMapping(value = "/propertyPipeSizeMaster", method = RequestMethod.GET)
    public String viewForm(final Model model) {
        final PropertyPipeSize propertyPipeSize = new PropertyPipeSize();
        model.addAttribute("propertyPipeSize", propertyPipeSize);
        model.addAttribute("pipeSize", pipeSizeService.getAllActivePipeSize());
        model.addAttribute("propertyTypeList", propertyTypeService.getAllActivePropertyTypes());
        model.addAttribute("reqAttr", "false");
        model.addAttribute("mode", "create");
        return "property-pipesize-master";
    }

    @RequestMapping(value = "/propertyPipeSizeMaster", method = RequestMethod.POST)
    public String createPropertyPipeSizeMasterData(@Valid @ModelAttribute final PropertyPipeSize propertyPipeSize,
            final BindingResult errors, final RedirectAttributes redirectAttrs, final Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("propertyTypeList", propertyTypeService.getAllActivePropertyTypes());
            model.addAttribute("pipeSize", pipeSizeService.getAllActivePipeSize());
            return "property-pipesize-master";
        } else
            propertyPipeSizeService.createPropertyPipeSize(propertyPipeSize);
        redirectAttrs.addFlashAttribute("propertyPipeSize", propertyPipeSize);
        model.addAttribute("message", "Property PipeSize created successfully.");
        model.addAttribute("mode", "create");
        return "property-pipesize-master-success";
    }

    @RequestMapping(value = "/propertyPipeSizeMaster/list", method = RequestMethod.GET)
    public String getPropertyPipeSizeMasterList(final Model model) {
        final List<PropertyPipeSize> propertyPipeSizeList = propertyPipeSizeService.findAll();
        model.addAttribute("propertyPipeSizeList", propertyPipeSizeList);
        return "property-pipesize-list";

    }

    @RequestMapping(value = "/propertyPipeSizeMaster/edit", method = RequestMethod.GET)
    public String getPropertyPipeSizeMaster(final Model model) {
        model.addAttribute("mode", "edit");
        return getPropertyPipeSizeMasterList(model);
    }

    @RequestMapping(value = "/propertyPipeSizeMaster/edit/{propertyPipeSizeId}", method = RequestMethod.GET)
    public String getPropertyPipeSizeDetails(final Model model, @PathVariable final String propertyPipeSizeId) {
        final PropertyPipeSize propertyPipeSize = propertyPipeSizeService.findOne(Long.parseLong(propertyPipeSizeId));
        model.addAttribute("propertyPipeSize", propertyPipeSize);
        model.addAttribute("pipeSize", pipeSizeService.getAllActivePipeSize());
        model.addAttribute("propertyTypeList", propertyTypeService.getAllActivePropertyTypes());
        model.addAttribute("reqAttr", "true");
        return "property-pipesize-master";
    }

    @RequestMapping(value = "/propertyPipeSizeMaster/edit/{propertyPipeSizeId}", method = RequestMethod.POST)
    public String editPropertyPipeSizeData(@Valid @ModelAttribute final PropertyPipeSize propertyPipeSize,
            final BindingResult errors, final RedirectAttributes redirectAttrs, final Model model,
            @PathVariable final long propertyPipeSizeId) {
        if (errors.hasErrors()) {
            model.addAttribute("propertyTypeList", propertyTypeService.getAllActivePropertyTypes());
            model.addAttribute("pipeSize", pipeSizeService.getAllActivePipeSize());
            return "property-pipesize-master";
        } else
            propertyPipeSizeService.updatePropertyPipeSize(propertyPipeSize);
        redirectAttrs.addFlashAttribute("propertyPipeSize", propertyPipeSize);
        model.addAttribute("message", "Property PipeSize updated successfully.");
        return "property-pipesize-master-success";

    }

}