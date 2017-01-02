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
package org.egov.eis.web.controller.masters.designation;

import org.apache.commons.io.IOUtils;
import org.egov.eis.service.DesignationService;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.DesignationAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static org.egov.infra.utils.JsonUtils.toJSON;

@Controller
@RequestMapping("/designation")
public class CreateAndViewDesignationController {

    private final DesignationService designationService;
    public static final String CONTENTTYPE_JSON = "application/json";

    @Autowired
    public CreateAndViewDesignationController(final DesignationService designationService) {
        this.designationService = designationService;
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createForm(final Model model) {
        model.addAttribute("designation", new Designation());
        return "designation-form";
    }

    @RequestMapping(value = "view", method = RequestMethod.GET)
    public String complaintTypeViewForm(@ModelAttribute final Designation designation, final Model model) {
        return "designation-view";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String createDesignation(@Valid @ModelAttribute final Designation designation, final BindingResult errors,
            final RedirectAttributes redirectAttrs, final Model model) {
        if (errors.hasErrors())
            return "designation-form";
        designationService.createDesignation(designation);
        redirectAttrs.addFlashAttribute("designation", designation);
        model.addAttribute("message", "Designation created successfully");
        return "success-designation";
    }

    @RequestMapping(value = "ajax/result", method = RequestMethod.GET)
    public @ResponseBody void springPaginationDataTables(final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        final List<Designation> designationList = designationService.getAllDesignations();
        final StringBuilder designationJSONData = new StringBuilder("{\"data\":").append(toJSON(designationList, Designation.class, DesignationAdaptor.class))
                .append("}");
        response.setContentType(CONTENTTYPE_JSON);
        IOUtils.write(designationJSONData, response.getWriter());
    }

}
