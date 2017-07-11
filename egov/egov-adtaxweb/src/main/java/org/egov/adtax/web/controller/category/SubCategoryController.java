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
package org.egov.adtax.web.controller.category;

import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.egov.adtax.entity.HoardingCategory;
import org.egov.adtax.entity.SubCategory;
import org.egov.adtax.entity.SubCategorySearch;
import org.egov.adtax.service.HoardingCategoryService;
import org.egov.adtax.service.SubCategoryService;
import org.egov.infra.config.core.GlobalSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/subcategory")
public class SubCategoryController {

    private final SubCategoryService subCategoryService;

    @Autowired
    private HoardingCategoryService hoardingCategoryService;

    @Autowired
    public SubCategoryController(final SubCategoryService subCategoryService) {
        this.subCategoryService = subCategoryService;
    }

    @ModelAttribute("subCategory")
    public SubCategory subCategory() {
        return new SubCategory();
    }

    @ModelAttribute(value = "subCategories")
    public List<SubCategory> getAllSubCategory() {
        return subCategoryService.getAllSubCategory();
    }

    @ModelAttribute(value = "hoardingCategories")
    public List<HoardingCategory> getAllHoardingCategory() {
        return hoardingCategoryService.getAllHoardingCategory();
    }


    @RequestMapping(value = "create", method = GET)
    public String create() {
        return "subcategory-form";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search() {
        return "subcategory-search";
    }

    @RequestMapping(value = "create", method = POST)
    public String create(@Valid @ModelAttribute final SubCategory subCategory,
                         final BindingResult errors, final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors())
            return "subcategory-form";
        subCategoryService.createSubCategory(subCategory);
        redirectAttrs.addFlashAttribute("subCategory", subCategory);
        redirectAttrs.addFlashAttribute("message", "message.subcategory.create");
        return "redirect:/subcategory/success/" + subCategory.getId();
    }

    @RequestMapping(value = "/success/{id}", method = GET)
    public ModelAndView successView(@PathVariable("id") final Long id, @ModelAttribute final SubCategory subCategory) {
        return new ModelAndView("subcategory/subcategory-success", "subCategory", subCategoryService.getSubCategoryById(id));

    }

    @RequestMapping(value = "/searchSubCategory", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void searchSubcategory(final Model model, @ModelAttribute final SubCategory subCategory, @RequestParam final String category,
                                  @RequestParam final String description, final HttpServletResponse response) throws IOException {
        List<SubCategorySearch> SubCategoryJson = subCategoryService.getSubcategory(Long.valueOf(category), description != null ? Long.valueOf(description) : null);
        IOUtils.write("{ \"data\":" + new GsonBuilder().setDateFormat(GlobalSettings.datePattern()).create()
                .toJson(SubCategoryJson)
                + "}", response.getWriter());
    }

}
