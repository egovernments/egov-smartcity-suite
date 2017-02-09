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

package org.egov.tl.web.controller.feematrix;

import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.tl.entity.FeeMatrix;
import org.egov.tl.entity.LicenseAppType;
import org.egov.tl.entity.LicenseCategory;
import org.egov.tl.entity.NatureOfBusiness;
import org.egov.tl.service.FeeMatrixService;
import org.egov.tl.service.LicenseAppTypeService;
import org.egov.tl.service.LicenseCategoryService;
import org.egov.tl.service.NatureOfBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/feematrix/create")
public class CreateFeeMatrixController {

    @Autowired
    private LicenseCategoryService licenseCategoryService;

    @Autowired
    private NatureOfBusinessService natureOfBusinessService;

    @Autowired
    private FinancialYearDAO financialYearDAO;

    @Autowired
    private LicenseAppTypeService licenseAppTypeService;

    @Autowired
    private FeeMatrixService feeMatrixService;

    @ModelAttribute
    public List<LicenseCategory> licenseCategories() {
        return licenseCategoryService.getCategoriesOrderByName();
    }

    @ModelAttribute
    public List<NatureOfBusiness> natureOfBusinesses() {
        return natureOfBusinessService.getNatureOfBusinesses();
    }

    @ModelAttribute("financialYears")
    public List<CFinancialYear> financialYears() {
        return financialYearDAO.getAllActiveFinancialYearList();
    }

    @ModelAttribute
    public List<LicenseAppType> licenseAppTypes() {
        return licenseAppTypeService.getLicenseAppTypes();
    }

    @ModelAttribute
    public FeeMatrix feeMatrix() {
        return new FeeMatrix();
    }

    @RequestMapping(method = GET)
    public String newForm() {
        return "feematrix-create";
    }

    @RequestMapping(method = POST)
    public String create(@ModelAttribute @Valid FeeMatrix feeMatrix, BindingResult bindingResult, RedirectAttributes responseAttribs) {
        if (bindingResult.hasErrors())
            return "feematrix-create";
        feeMatrixService.create(feeMatrix);
        responseAttribs.addFlashAttribute("message", "msg.feematrix.create.success");
        return "redirect:/feematrix/view/" + feeMatrix.getId();
    }
}
