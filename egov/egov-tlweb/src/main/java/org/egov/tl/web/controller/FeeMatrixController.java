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

package org.egov.tl.web.controller;

import org.egov.commons.dao.FinancialYearDAO;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infstr.services.PersistenceService;
import org.egov.tl.entity.FeeMatrix;
import org.egov.tl.entity.FeeMatrixDetail;
import org.egov.tl.entity.FeeType;
import org.egov.tl.entity.UnitOfMeasurement;
import org.egov.tl.service.FeeMatrixDetailService;
import org.egov.tl.service.FeeMatrixService;
import org.egov.tl.service.LicenseCategoryService;
import org.egov.tl.web.response.adaptor.FeeMatrixResponseAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/feematrix/")
public class FeeMatrixController {
    private static final String FEEMATRIX_NEW = "feematrix-new";
    private static final String FEEMATRIX_RESULT = "feematrix-result";
    private static final String FEEMATRIX_EDIT = "feematrix-edit";
    private static final String FEEMATRIX_VIEW = "feematrix-view";
    private static final String FEE_MATRIX_MODEL_ATTRIB_NAME = "feeMatrix";

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    @Autowired
    private FeeMatrixService feeMatrixService;
    @Autowired
    private FeeMatrixDetailService feeMatrixDetailService;
    @Autowired
    private FinancialYearDAO financialYearDAO;

    @Autowired
    private AppConfigValueService appConfigValueService;

    @Autowired
    private LicenseCategoryService licenseCategoryService;

    private void prepareForNewForm(Model model) {

        new ArrayList<UnitOfMeasurement>();
        new ArrayList<FeeType>();
        model.addAttribute("licenseCategorys", licenseCategoryService.getCategoriesOrderByName());
        model.addAttribute("natureOfBusinesss",
                persistenceService.findAllBy("select n from org.egov.tl.entity.NatureOfBusiness n order by name asc"));
        model.addAttribute("financialYears", financialYearDAO.getAllActiveFinancialYearList());

        List<AppConfigValues> permTempAppconfigList = appConfigValueService.getConfigValuesByModuleAndKey("Trade License",
                "Is Fee For Permanent and Temporary Same");
        if ("Y".equals(permTempAppconfigList.get(0).getValue()))
            model.addAttribute("hideTemporary", true);

        List<AppConfigValues> newRenewAppconfigList = appConfigValueService.getConfigValuesByModuleAndKey("Trade License",
                "Is Fee For New and Renew Same");
        if ("Y".equals(newRenewAppconfigList.get(0).getValue()))
            model.addAttribute("hideRenew", true);
        model.addAttribute(FEE_MATRIX_MODEL_ATTRIB_NAME, new FeeMatrix());
        model.addAttribute("subCategorys", emptyList());
        model.addAttribute("feeTypes", emptyList());
        model.addAttribute("unitOfMeasurements", emptyList());
        model.addAttribute("licenseAppTypes", persistenceService.findAllBy("select a from LicenseAppType a order by name asc"));
    }

    @RequestMapping(value = "create", method = GET)
    public String newForm(Model model, @RequestParam(required = false) String fee) {
        prepareForNewForm(model);
        return FEEMATRIX_NEW;
    }

    @RequestMapping(value = "create", method = POST)
    public String create(@Valid @ModelAttribute FeeMatrix feeMatrix, BindingResult errors, Model model) {
        if (errors.hasErrors())
            return FEEMATRIX_RESULT;
        feeMatrixService.create(feeMatrix);
        model.addAttribute(FEE_MATRIX_MODEL_ATTRIB_NAME, feeMatrix);
        return FEEMATRIX_RESULT;
    }

    @RequestMapping(value = "search", method = GET)
    public String search(@ModelAttribute FeeMatrix feeMatrix, BindingResult errors, Model model) {
        if (errors.hasErrors())
            return FEEMATRIX_RESULT;
        FeeMatrix searchfeeMatrix = feeMatrixService.search(feeMatrix);
        if (searchfeeMatrix == null)
            searchfeeMatrix = new FeeMatrix();
        model.addAttribute(FEE_MATRIX_MODEL_ATTRIB_NAME, searchfeeMatrix);
        return FEEMATRIX_RESULT;
    }

    @RequestMapping(value = "edit/{id}", method = POST)
    public String edit(@PathVariable("id") String id) {
        return FEEMATRIX_EDIT;
    }

    @RequestMapping(value = "update", method = POST)
    public String update(@Valid @ModelAttribute FeeMatrix feeMatrix, BindingResult errors) {
        if (errors.hasErrors())
            return FEEMATRIX_RESULT;
        feeMatrixService.update(feeMatrix);
        return FEEMATRIX_RESULT;
    }

    @RequestMapping(value = "view/{id}", method = POST)
    public String view(@PathVariable("id") String id) {
        return FEEMATRIX_VIEW;
    }

    @RequestMapping(value = "view-feematrix", method = GET)
    public String viewForm(Model model) {
        model.addAttribute(FEE_MATRIX_MODEL_ATTRIB_NAME, new FeeMatrix());
        model.addAttribute("financialYears", financialYearDAO.getAllActiveFinancialYearList());
        model.addAttribute("licenseCategorys", licenseCategoryService.getCategoriesOrderByName());
        model.addAttribute("subCategorys", emptyList());
        return "feematrix-view";
    }

    @RequestMapping(value = "viewresult", method = POST, produces = TEXT_PLAIN_VALUE)
    @ResponseBody
    public String viewresult(@RequestParam(required = false) Long category,
                             @RequestParam(required = false) Long subCategory,
                             @RequestParam(required = false) Long finyear) {
        return new StringBuilder("{ \"data\":").
                append(toJSON(
                        feeMatrixDetailService.searchFeeMatrix(category, subCategory, finyear), FeeMatrixDetail.class,
                        FeeMatrixResponseAdaptor.class)).append("}").toString();
    }
}