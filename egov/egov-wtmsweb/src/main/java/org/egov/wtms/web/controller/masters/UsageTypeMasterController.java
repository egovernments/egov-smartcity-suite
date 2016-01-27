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

import javax.validation.Valid;



import org.egov.wtms.masters.entity.UsageType;
import org.egov.wtms.masters.entity.WaterPropertyUsage;
import org.egov.wtms.masters.service.PropertyTypeService;
import org.egov.wtms.masters.service.UsageTypeService;
import org.egov.wtms.masters.service.WaterPropertyUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
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
            final WaterPropertyUsageService waterPropertyUsageService,
            final UsageTypeService usageTypeService) {
        this.propertyTypeService = propertyTypeService;
        this.waterPropertyUsageService = waterPropertyUsageService;
        this.usageTypeService = usageTypeService;

    }

    @RequestMapping(value = "/usageTypeMaster", method = GET)
    public String viewForm(@ModelAttribute WaterPropertyUsage waterPropertyUsage, final Model model) {
        waterPropertyUsage = new WaterPropertyUsage();
        model.addAttribute("waterPropertyUsage", waterPropertyUsage);
        model.addAttribute("propertyType", propertyTypeService.getAllActivePropertyTypes());
        return "usage-type-master";
    }

    @RequestMapping(value = "/usageTypeMaster", method = RequestMethod.POST)
    public String addCategoryMasterData(@Valid @ModelAttribute final WaterPropertyUsage waterPropertyUsage,
            final RedirectAttributes redirectAttrs, final Model model, final BindingResult resultBinder) {
        if (resultBinder.hasErrors())
            return "usage-type-master";
        WaterPropertyUsage waterpropertyUsage = new WaterPropertyUsage();
        waterpropertyUsage = waterPropertyUsageService.findByPropertyTypeAndUsageType(
                waterPropertyUsage.getPropertyType(),
                waterPropertyUsage.getUsagetype().getName().toUpperCase()
                        .trim());
        if (waterpropertyUsage != null) {
            redirectAttrs.addFlashAttribute("waterPropertyUsage", waterpropertyUsage);
            model.addAttribute("message", "Entered Usage Type for the Chosen Property Type is already Exists");
        } else {
            UsageType usagetype = new UsageType();
            usagetype = waterPropertyUsage.getUsagetype();
            usagetype.setName(usagetype.getName().trim());
            usagetype.setActive(true);
            usagetype.setCode(usagetype.getName().toUpperCase());
            usageTypeService.createUsageType(usagetype);
            waterPropertyUsageService.createPropertyCategory(waterPropertyUsage);
            redirectAttrs.addFlashAttribute("waterPropertyUsage", waterPropertyUsage);
            model.addAttribute("message", "Usage Type Data created successfully");
        }
        return "usage-master-success";
    }
}
