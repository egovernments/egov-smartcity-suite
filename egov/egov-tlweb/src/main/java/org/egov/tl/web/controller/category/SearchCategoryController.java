/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */

package org.egov.tl.web.controller.category;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egov.tl.entity.LicenseCategory;
import org.egov.tl.service.masters.LicenseCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/licensecategory")
public class SearchCategoryController {

    private final LicenseCategoryService licenseCategoryService;

    @Autowired
    public SearchCategoryController(final LicenseCategoryService licenseCategoryService) {
        this.licenseCategoryService = licenseCategoryService;
    }

    @ModelAttribute
    public LicenseCategory licenseCategoryModel() {
        return new LicenseCategory();
    }

    @ModelAttribute(value = "licenseCategories")
    public List<LicenseCategory> getAllCategories() {
        return licenseCategoryService.findAll();
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public String updateLicenseCategory(final Model model) {
        return "licensecategory-search-update";
    }

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String viewLicenseCategory(final Model model) {
        return "licensecategory-search-view";
    }

    @RequestMapping(value = "/view", method = RequestMethod.POST)
    public String searchView(@ModelAttribute final LicenseCategory licenseCategory, final BindingResult errors,
            final RedirectAttributes redirectAttrs, final HttpServletRequest request) {
        if (errors.hasErrors())
            return "licensecategory-search-view";
        return "redirect:/licensecategory/view/" + licenseCategory.getCode();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String searchUpdate(@ModelAttribute final LicenseCategory licenseCategory, final BindingResult errors,
            final RedirectAttributes redirectAttrs, final HttpServletRequest request) {
        if (errors.hasErrors())
            return "licensecategory-search-update";
        return "redirect:/licensecategory/update/" + licenseCategory.getCode();

    }
}
