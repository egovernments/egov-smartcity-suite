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

package org.egov.tl.web.controller.subcategory;

import org.egov.tl.entity.LicenseCategory;
import org.egov.tl.entity.LicenseSubCategory;
import org.egov.tl.service.LicenseCategoryService;
import org.egov.tl.service.LicenseSubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/licensesubcategory")
public class SearchSubCategoryController {

    @Autowired
    private LicenseSubCategoryService licenseSubCategoryService;

    @Autowired
    private LicenseCategoryService licenseCategoryService;

    @ModelAttribute
    public LicenseSubCategory licenseSubCategory() {
        return new LicenseSubCategory();
    }

    @ModelAttribute(value = "licenseCategories")
    public List<LicenseCategory> categories() {
        return licenseCategoryService.getCategories();
    }

    @RequestMapping(value = "/subcategories-by-category", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<LicenseSubCategory> getSubCategories(@RequestParam Long categoryId) {
        return licenseSubCategoryService.getSubCategoriesByCategory(categoryId);
    }

    @RequestMapping(value = "/update", method = GET)
    public String showUpdateSubCategorySearchForm() {
        return "subcategory-search-update";
    }

    @RequestMapping(value = "/view", method = GET)
    public String showViewSubCategorySearchForm() {
        return "subcategory-search-view";
    }

    @RequestMapping(value = "/view", method = POST)
    public String showViewSubCategoryForm(@ModelAttribute LicenseSubCategory licenseSubCategory) {
        return "redirect:/licensesubcategory/view/" + licenseSubCategory.getCode();
    }

    @RequestMapping(value = "/update", method = POST)
    public String showUpdateSubCategoryForm(@ModelAttribute LicenseSubCategory licenseSubCategory) {
        return "redirect:/licensesubcategory/update/" + licenseSubCategory.getCode();
    }
}
