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
package org.egov.bpa.web.controller.application;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.egov.bpa.application.entity.BpaApplication;
import org.egov.bpa.application.service.ApplicationBpaService;
import org.egov.bpa.utils.BpaConstants;
import org.egov.eis.web.contract.WorkflowContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/application")
public class UpdateBpaApplicationController extends BpaGenericApplicationController {

    private static final String ADDITIONALRULE = "additionalRule";

    @Autowired
    private ApplicationBpaService applicationBpaService;

    @ModelAttribute
    public BpaApplication getBpaApplication(@PathVariable final String applicationNumber) {
        return applicationBpaService.findByApplicationNumber(applicationNumber);
    }

    @RequestMapping(value = "/update/{applicationNumber}", method = RequestMethod.GET)
    public String updateApplicationForm(final Model model, @PathVariable final String applicationNumber,
            final HttpServletRequest request) {
        final BpaApplication application = getBpaApplication(applicationNumber);
        loadViewdata(model, application);
        return "bpaapplication-Form";
    }

    private void loadViewdata(final Model model, final BpaApplication application) {
        model.addAttribute("stateType", application.getClass().getSimpleName());
        final WorkflowContainer workflowContainer = new WorkflowContainer();
        model.addAttribute(ADDITIONALRULE, BpaConstants.CREATE_ADDITIONAL_RULE_CREATE);
        workflowContainer.setAdditionalRule(BpaConstants.CREATE_ADDITIONAL_RULE_CREATE);
        prepareWorkflow(model, application, workflowContainer);
        model.addAttribute("currentState", application.getCurrentState().getValue());
        model.addAttribute("bpaApplication", application);
    }

    @RequestMapping(value = "/update/{applicationNumber}", method = RequestMethod.POST)
    public String updateApplication(@Valid @ModelAttribute("bpaApplication") BpaApplication bpaApplication,
            @PathVariable final String applicationNumber,
            final BindingResult resultBinder, final RedirectAttributes redirectAttributes,
            final HttpServletRequest request, final Model model, @RequestParam("files") final MultipartFile[] files) {
        if (resultBinder.hasErrors()) {
            loadViewdata(model, bpaApplication);
            return "bpaapplication-Form";
        }

        bpaApplication = applicationBpaService.updateApplication(bpaApplication);
        System.out.println(bpaApplication);
        return bpaApplication.getApplicationNumber();
    }

}
