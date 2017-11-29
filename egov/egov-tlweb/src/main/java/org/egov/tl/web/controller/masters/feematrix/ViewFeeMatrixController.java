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

package org.egov.tl.web.controller.masters.feematrix;

import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.tl.entity.FeeMatrix;
import org.egov.tl.entity.LicenseCategory;
import org.egov.tl.service.FeeMatrixService;
import org.egov.tl.service.LicenseCategoryService;
import org.egov.tl.web.response.adaptor.FeeMatrixResponseAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@Controller
@RequestMapping("/feematrix")
public class ViewFeeMatrixController {

    @Autowired
    private LicenseCategoryService licenseCategoryService;

    @Autowired
    private FinancialYearDAO financialYearDAO;

    @Autowired
    private FeeMatrixService feeMatrixService;

    @ModelAttribute
    public List<LicenseCategory> licenseCategories() {
        return licenseCategoryService.getCategoriesOrderByName();
    }

    @ModelAttribute("financialYears")
    public List<CFinancialYear> financialYears() {
        return financialYearDAO.getAllActiveFinancialYearList();
    }

    @GetMapping("view/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("feeMatrix", feeMatrixService.getFeeMatrixById(id));
        return "feematrix-view";
    }

    @GetMapping("search")
    public String searchForm(Model model) {
        model.addAttribute("feeMatrix", new FeeMatrix());
        return "feematrix-search";
    }

    @PostMapping(value = "search", produces = TEXT_PLAIN_VALUE)
    @ResponseBody
    public String search(@RequestParam(required = false) Long categoryId,
                         @RequestParam(required = false) Long subcategoryId,
                         @RequestParam(required = false) Long financialYearId) {

        return new StringBuilder("{ \"data\":").
                append(toJSON(
                        feeMatrixService.getFeeMatrix(categoryId, subcategoryId, financialYearId), FeeMatrix.class,
                        FeeMatrixResponseAdaptor.class)).append("}").toString();
    }
}
