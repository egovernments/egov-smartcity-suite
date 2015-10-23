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
package org.egov.adtax.web.controller.hoarding;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.Valid;

import org.egov.adtax.entity.Hoarding;
import org.egov.adtax.exception.HoardingValidationError;
import org.egov.adtax.web.controller.common.HoardingControllerSupport;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/hoarding")
public class UpdateHoardingController extends HoardingControllerSupport {

    @ModelAttribute("hoarding")
    public Hoarding hoarding(@PathVariable final String hoardingNumber) {
        return hoardingService.findByHoardingNumber(hoardingNumber);
    }

    @RequestMapping(value = "update/{hoardingNumber}")
    public String updateHoarding(@PathVariable final String hoardingNumber, final Model model) {
        final Hoarding hoarding = hoardingService.getHoardingByHoardingNumber(hoardingNumber);
        
        model.addAttribute("dcPending", advertisementDemandService.anyDemandPendingForCollection(hoarding));
        model.addAttribute("hoarding", hoarding);
        return "hoarding-update";
    }

    @RequestMapping(value = "update/{hoardingNumber}", method = POST)
    public String updateHoarding(@Valid @ModelAttribute final Hoarding hoarding, final BindingResult resultBinder, final RedirectAttributes redirAttrib) {
     
        validateHoardingDocsOnUpdate(hoarding, resultBinder,redirAttrib);
              
        if (resultBinder.hasErrors())
            return "hoarding-update";
        try {
            updateHoardingDocuments(hoarding);
            hoardingService.updateHoarding(hoarding);
            redirAttrib.addFlashAttribute("message", "hoarding.update.success");
            return "redirect:/hoarding/view/" + hoarding.getHoardingNumber();
        } catch (final HoardingValidationError e) {
            resultBinder.rejectValue(e.fieldName(), e.errorCode());
            return "hoarding-update";
        }
    }
}
