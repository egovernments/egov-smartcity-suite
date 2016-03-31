/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.wtms.web.controller.masters;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import javax.validation.Valid;

import org.egov.wtms.masters.entity.UsageType;
import org.egov.wtms.masters.entity.WaterPropertyUsage;
import org.egov.wtms.masters.repository.WaterPropertyUsageRepository;
import org.egov.wtms.masters.service.PropertyTypeService;
import org.egov.wtms.masters.service.UsageTypeService;
import org.egov.wtms.masters.service.WaterPropertyUsageService;
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
public class UsageTypeMasterController {

    private final PropertyTypeService propertyTypeService;

    private final WaterPropertyUsageService waterPropertyUsageService;

    private final UsageTypeService usageTypeService;

    @Autowired
    public UsageTypeMasterController(final PropertyTypeService propertyTypeService,
            final WaterPropertyUsageService waterPropertyUsageService, final UsageTypeService usageTypeService,
            final WaterPropertyUsageRepository waterPropertyUsageRepository) {
        this.propertyTypeService = propertyTypeService;
        this.waterPropertyUsageService = waterPropertyUsageService;
        this.usageTypeService = usageTypeService;
    }

    @RequestMapping(value = "/usageTypeMaster", method = GET)
    public String viewForm(@ModelAttribute WaterPropertyUsage waterPropertyUsage, final Model model) {
        waterPropertyUsage = new WaterPropertyUsage();
        model.addAttribute("waterPropertyUsage", waterPropertyUsage);
        model.addAttribute("propertyType", propertyTypeService.getAllActivePropertyTypes());
        model.addAttribute("usageType", usageTypeService.getActiveUsageTypes());
        model.addAttribute("reqAttr", "false");
        return "usage-type-master";
    }

    @RequestMapping(value = "/usageTypeMaster", method = RequestMethod.POST)
    public String addUsageTypeMasterData(@Valid @ModelAttribute final WaterPropertyUsage waterPropertyUsage,
            final RedirectAttributes redirectAttrs, final Model model, final BindingResult resultBinder) {
        if (resultBinder.hasErrors())
            return "usage-type-master";
        WaterPropertyUsage waterpropertyUsage = new WaterPropertyUsage();
        final UsageType usageTypeObj = usageTypeService
                .findByNameIgnoreCase(waterPropertyUsage.getUsageType().getName().toUpperCase().trim());
        if (usageTypeObj != null)
            waterpropertyUsage = waterPropertyUsageService
                    .findByPropertyTypeAndUsageType(waterPropertyUsage.getPropertyType(), usageTypeObj);
        else
            waterpropertyUsage = null;
        if (waterpropertyUsage != null) {
            redirectAttrs.addFlashAttribute("waterPropertyUsage", waterpropertyUsage);
            model.addAttribute("message", "Entered Usage Type for the Chosen Property Type is already Exists");
        } else {
            UsageType usagetype = new UsageType();
            usagetype = waterPropertyUsage.getUsageType();
            if (usageTypeObj == null) {
                usagetype.setName(usagetype.getName().trim());
                usagetype.setActive(true);
                usagetype.setCode(usagetype.getName().toUpperCase());
                usageTypeService.createUsageType(usagetype);
                waterPropertyUsageService.createPropertyCategory(waterPropertyUsage);
                redirectAttrs.addFlashAttribute("waterPropertyUsage", waterPropertyUsage);
            } else {
                final WaterPropertyUsage waterpropertyusage = new WaterPropertyUsage();
                waterpropertyusage.setPropertyType(waterPropertyUsage.getPropertyType());
                waterpropertyusage.setUsageType(usageTypeObj);
                waterPropertyUsageService.createPropertyCategory(waterpropertyusage);
                redirectAttrs.addFlashAttribute("waterPropertyUsage", waterpropertyusage);
            }
        }
        final List<WaterPropertyUsage> waterPropertyUsageList = waterPropertyUsageService.findAll();
        model.addAttribute("waterPropertyUsageList", waterPropertyUsageList);
        return "usage-type-master-list";
    }

    @RequestMapping(value = "/usageTypeMaster/list", method = GET)
    public String getUsageTypeMasterList(final Model model) {
        final List<WaterPropertyUsage> waterPropertyUsageList = waterPropertyUsageService.findAll();
        model.addAttribute("waterPropertyUsageList", waterPropertyUsageList);
        return "usage-type-master-list";

    }

    @RequestMapping(value = "/usageTypeMaster/{waterPropertyUsageId}", method = GET)
    public String getUsageTypeMasterDetails(final Model model, @PathVariable final String waterPropertyUsageId) {
        final WaterPropertyUsage waterPropertyUsage = waterPropertyUsageService
                .findOne(Long.parseLong(waterPropertyUsageId));
        model.addAttribute("waterPropertyUsage", waterPropertyUsage);
        model.addAttribute("propertyType", propertyTypeService.getAllActivePropertyTypes());
        model.addAttribute("usageType", usageTypeService.getActiveUsageTypes());
        model.addAttribute("reqAttr", "true");
        return "usage-type-master";
    }

    @RequestMapping(value = "/usageTypeMaster/{waterPropertyUsageId}", method = RequestMethod.POST)
    public String editUsageTypeMasterData(@Valid @ModelAttribute final WaterPropertyUsage waterPropertyUsage,
            @PathVariable final long waterPropertyUsageId, final RedirectAttributes redirectAttrs, final Model model,
            final BindingResult resultBinder) {
        if (resultBinder.hasErrors())
            return "usage-type-master";
        final boolean status = waterPropertyUsage.getUsageType().isActive();
        WaterPropertyUsage waterpropertyUsageValidateObj = null;
        final UsageType usageTypeObj = usageTypeService.findByNameIgnoreCaseAndActive(
                waterPropertyUsage.getUsageType().getName().toUpperCase().trim(), status);
        if (usageTypeObj != null)
            waterpropertyUsageValidateObj = waterPropertyUsageService
                    .findByPropertyTypeAndUsageType(waterPropertyUsage.getPropertyType(), usageTypeObj);

        if (waterpropertyUsageValidateObj != null) {
            redirectAttrs.addFlashAttribute("waterPropertyUsage", waterPropertyUsage);
            model.addAttribute("propertyType", propertyTypeService.getAllActivePropertyTypes());
            model.addAttribute("message", "Entered Usage Type for the Chosen Property Type is already Exists");
            return "usage-type-master";
        } else if (waterPropertyUsage != null) {

            waterPropertyUsage.getUsageType().setActive(status);
            waterPropertyUsageService.createPropertyCategory(waterPropertyUsage);
            redirectAttrs.addFlashAttribute("WaterPropertyUsage", waterPropertyUsage);
        }

        final List<WaterPropertyUsage> waterPropertyUsageList = waterPropertyUsageService.findAll();
        model.addAttribute("waterPropertyUsageList", waterPropertyUsageList);
        return "usage-type-master-list";

    }
}