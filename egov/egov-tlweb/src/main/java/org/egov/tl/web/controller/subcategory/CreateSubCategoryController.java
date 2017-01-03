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

import org.egov.tl.entity.FeeType;
import org.egov.tl.entity.LicenseCategory;
import org.egov.tl.entity.LicenseSubCategory;
import org.egov.tl.entity.UnitOfMeasurement;
import org.egov.tl.entity.enums.RateTypeEnum;
import org.egov.tl.service.FeeTypeService;
import org.egov.tl.service.LicenseCategoryService;
import org.egov.tl.service.LicenseSubCategoryService;
import org.egov.tl.service.UnitOfMeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/licensesubcategory")
public class CreateSubCategoryController {

    @Autowired
    private LicenseSubCategoryService licenseSubCategoryService;

    @Autowired
    private LicenseCategoryService licenseCategoryService;

    @Autowired
    private UnitOfMeasurementService unitOfMeasurementService;

    @Autowired
    private FeeTypeService feeTypeService;

    @ModelAttribute
    public LicenseSubCategory licenseSubCategory() {
        return new LicenseSubCategory();
    }

    @ModelAttribute(value = "licenseCategories")
    public List<LicenseCategory> licenseCategories() {
        return licenseCategoryService.getCategories();
    }

    @ModelAttribute(value = "licenseUomTypes")
    public List<UnitOfMeasurement> activeUOMs() {
        return unitOfMeasurementService.getAllActiveUOM();
    }

    @ModelAttribute(value = "licenseFeeTypes")
    public List<FeeType> feeTypes() {
        return feeTypeService.findAll();
    }

    @RequestMapping(value = "/create", method = GET)
    public String showCreateSubCategoryForm(Model model) {
        model.addAttribute("rateTypes", RateTypeEnum.values());
        return "subcategory-create";
    }

    @RequestMapping(value = "/create", method = POST)
    public String createSubCategory(@ModelAttribute @Valid LicenseSubCategory licenseSubCategory,
                                    BindingResult bindingResult, RedirectAttributes responseAttrbs) {
        if (bindingResult.hasErrors())
            return "subcategory-create";
        licenseSubCategoryService.createSubCategory(licenseSubCategory);
        responseAttrbs.addFlashAttribute("message", "msg.subcategory.save.success");
        return "redirect:/licensesubcategory/view/" + licenseSubCategory.getCode();
    }
}
