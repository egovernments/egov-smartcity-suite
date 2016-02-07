/* eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.mrs.web.controller.application.registration;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.mrs.application.Constants;
import org.egov.mrs.domain.entity.Document;
import org.egov.mrs.domain.entity.Registration;
import org.egov.mrs.domain.entity.Witness;
import org.egov.mrs.domain.enums.ApplicationStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Shows a Marriage Registration with read only fields
 *
 * @author nayeem
 *
 */

@Controller
@RequestMapping(value = "/registration")
public class ViewRegistrationController extends RegistrationController {

    @RequestMapping(value = "/{registrationId}", method = RequestMethod.GET)
    public String viewRegistration(@PathVariable final Long registrationId, @RequestParam(required = false) String mode,
            final Model model) throws IOException {
        final Registration registration = registrationService.get(registrationId);

        model.addAttribute("registration", registration);
        model.addAttribute("husbandPhoto", Base64.getEncoder().encodeToString(registration.getHusband().getPhoto()));
        model.addAttribute("wifePhoto", Base64.getEncoder().encodeToString(registration.getWife().getPhoto()));
        model.addAttribute("mode", mode);
        
        registrationService.prepareDocumentsForView(registration);
        applicantService.prepareDocumentsForView(registration.getHusband());
        applicantService.prepareDocumentsForView(registration.getWife());

        String screen = null;

        if (registration.getStatus() != ApplicationStatus.Approved) {
            if (mode == null)
                mode = utils.isLoggedInUserApprover() ? "view" : mode;

            screen = mode != null && mode.equalsIgnoreCase("view") ? "registration-view" : "registration-form";

        } else
            screen = "registration-view";

        int i = 0;
        
        //TODO move this logic to Witness with a property, implement like Applicant
        for (final Witness witness : registration.getWitnesses())
            model.addAttribute("witness" + i++ + "Photo", Base64.getEncoder().encodeToString(witness.getPhoto()));

        prepareWorkflow(model, registration, new WorkflowContainer());
        return screen;
    }

}
