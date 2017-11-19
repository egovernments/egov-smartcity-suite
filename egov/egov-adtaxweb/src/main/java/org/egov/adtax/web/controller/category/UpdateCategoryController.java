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
package org.egov.adtax.web.controller.category;

import org.egov.adtax.entity.HoardingCategory;
import org.egov.adtax.service.HoardingCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/category")
public class UpdateCategoryController {

    private final HoardingCategoryService hoardingCategoryService;

    @Autowired
    public UpdateCategoryController(final HoardingCategoryService hoardingCategoryService) {
        this.hoardingCategoryService = hoardingCategoryService;
    }

    @ModelAttribute
    public HoardingCategory hoardingCategoryModel(@PathVariable final Long id, final Model model) {
        return hoardingCategoryService.getCategoryById(id);
    }
    
    @RequestMapping(value = "/update/{id}", method = POST) 
    public String update(@Valid @ModelAttribute final HoardingCategory hoardingCategory, final BindingResult errors,
            final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors())
            return "category-form";
        hoardingCategoryService.updateHoardingCategory(hoardingCategory);
        redirectAttrs.addFlashAttribute("hoardingCategory", hoardingCategory);
        redirectAttrs.addFlashAttribute("message", "message.category.update"); 
        return "redirect:/category/success/" + hoardingCategory.getId();
    }
    
    
    @RequestMapping(value = "/updateCategory/{id}", method = GET)
    public String update(@PathVariable final Long id) {
        return "redirect:/category/update/" + id;
    }

    
    @RequestMapping(value = "/update/{id}", method = GET)
    public ModelAndView updateView(@PathVariable("id") final Long id, @ModelAttribute final HoardingCategory hoardingCategory) {
         return new ModelAndView("category/category-form", "hoardingCategory", hoardingCategoryService.getCategoryById(id));

    }

    @RequestMapping(value = "/view/{id}", method = GET)
    public String view(@ModelAttribute final HoardingCategory hoardingCategory, final BindingResult errors) {
        if (errors.hasErrors())
            return "category-search";
        return "redirect:/category/success/" + hoardingCategory.getId();
    }
}
