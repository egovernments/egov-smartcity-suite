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
package org.egov.works.web.controller.masters;

import javax.servlet.http.HttpServletRequest;

import org.egov.infra.exception.ApplicationException;
import org.egov.works.masters.entity.Contractor;
import org.egov.works.masters.service.ContractorService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/masters")
public class CreateContractorController extends BaseContractorController {

    @Autowired
    private ContractorService contractorService;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Autowired
    private WorksUtils worksUtils;

    @RequestMapping(value = "/contractor-newform", method = RequestMethod.GET)
    public String showContractorForm(final Model model) {
        final Contractor contractor = new Contractor();
        model.addAttribute(CONTRACTOR, contractor);
        model.addAttribute("defaultDepartmentId", worksUtils.getDefaultDepartmentId());
        contractorService.loadModelValues(model);
        return "contractor-form";
    }

    @RequestMapping(value = "/contractor-save", method = RequestMethod.POST)
    public String createContractor(@ModelAttribute final Contractor contractor, final BindingResult resultBinder,
            final Model model, final HttpServletRequest request) throws ApplicationException {
        validateContractor(contractor, resultBinder);
        validateContractorDetails(contractor, resultBinder);
        if (resultBinder.hasErrors()) {
            contractorService.loadModelValues(model);
            return "contractor-form";
        }
        contractorService.createContractorDetails(contractor);
        contractorService.save(contractor);
        return "redirect:/masters/contractor-success?contractorId=" + contractor.getId();

    }

    @RequestMapping(value = "/contractor-success", method = RequestMethod.GET)
    public String successView(final Model model, final HttpServletRequest request) {
        final Long contractorId = Long.valueOf(request.getParameter("contractorId"));
        final Contractor newContractor = contractorService.getContractorById(contractorId);
        final String mode = request.getParameter(WorksConstants.MODE);
        model.addAttribute(CONTRACTOR, newContractor);
        contractorService.loadModelValues(model);
        if (mode != null && mode.equalsIgnoreCase(WorksConstants.EDIT)) {
            model.addAttribute(WorksConstants.MODE, mode);
            model.addAttribute("successMessage", messageSource.getMessage("msg.contractor.modify.success",
                    new String[] { newContractor.getCode() }, null));
        } else
            model.addAttribute("successMessage", messageSource.getMessage("msg.contractor.create.success",
                    new String[] { newContractor.getCode() }, null));
        return "contractor-success";

    }

}
