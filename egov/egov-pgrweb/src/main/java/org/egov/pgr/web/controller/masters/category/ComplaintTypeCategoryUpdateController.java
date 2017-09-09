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

package org.egov.pgr.web.controller.masters.category;

import org.egov.pgr.entity.ComplaintTypeCategory;
import org.egov.pgr.service.ComplaintTypeCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/complainttype-category")
public class ComplaintTypeCategoryUpdateController {

    private final ComplaintTypeCategoryService complaintTypeCategoryService;

    @Autowired
    public ComplaintTypeCategoryUpdateController(final ComplaintTypeCategoryService complaintTypeCategoryService) {
        this.complaintTypeCategoryService = complaintTypeCategoryService;
    }

    @ModelAttribute
    public ComplaintTypeCategory complaintTypeCategory(@PathVariable final String categoryName) {
        return complaintTypeCategoryService.findByName(categoryName);
    }

    @RequestMapping(value = "update/{categoryName}", method = RequestMethod.GET)
    public String editComplaintTypeCategoryForm(@PathVariable final String categoryName,
                                                @ModelAttribute final ComplaintTypeCategory complaintTypeCategory, final RedirectAttributes redirectAttrs, final Model model) {
        if (complaintTypeCategory == null) {
            redirectAttrs.addFlashAttribute("error", "grievance.category.not.found");
            redirectAttrs.addFlashAttribute("categoryname", categoryName);
            return "redirect:/complainttype-category/search-for-edit";
        }
        return "complainttype-category-update";
    }

    @RequestMapping(value = "update/{categoryName}", method = RequestMethod.POST)
    public String editComplaintTypeCategory(@PathVariable final String categoryName,
                                            @Valid @ModelAttribute final ComplaintTypeCategory complaintTypeCategory,
                                            final BindingResult error, final RedirectAttributes redirectAttrs) {
        if (error.hasErrors())
            return "complainttype-category-update";
        complaintTypeCategoryService.createComplaintTypeCategory(complaintTypeCategory);
        redirectAttrs.addFlashAttribute("message", "msg.comp.type.catgory.update.success");
        return "redirect:/complainttype-category/update/" + categoryName;
    }

}
