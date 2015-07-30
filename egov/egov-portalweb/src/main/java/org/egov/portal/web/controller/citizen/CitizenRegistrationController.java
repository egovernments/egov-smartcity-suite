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
package org.egov.portal.web.controller.citizen;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.servlet.http.HttpServletRequest;

import org.egov.exceptions.DuplicateElementException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.validation.ValidatorUtils;
import org.egov.portal.entity.Citizen;
import org.egov.portal.service.CitizenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/citizen")
public class CitizenRegistrationController {
    private final CitizenService citizenService;

    @Autowired
    public CitizenRegistrationController(final CitizenService citizenService) {
        this.citizenService = citizenService;
    }

    @RequestMapping(value = "/register", method = POST)
    public String registerCitizen(@ModelAttribute final Citizen citizen, final BindingResult errors,
            final HttpServletRequest request) {
        //TODO Rework this
        String SUCCESS = "redirect:/../egi/login/secure";
        if (!ValidatorUtils.isValidPassword(citizen.getPassword()))
            return SUCCESS + "?pwdInvalid=true";
        if(!ValidatorUtils.isCaptchaValid(request))
            return SUCCESS + "?captchaInvalid=true";
        try {
            citizenService.create(citizen);
            citizenService.sendActivationMessage(citizen);
            SUCCESS = SUCCESS + "?citizenActivation=true&citizenId=" + citizen.getId();

        } catch (final DuplicateElementException e) {

            if (e.getMessage().equals("Mobile Number already exists"))
                SUCCESS = SUCCESS + "?mobInvalid=true";
            else if (e.getMessage().equals("Email already exists"))
                SUCCESS = SUCCESS + "?emailInvalid=true";
        } catch (final EGOVRuntimeException e) {

            SUCCESS = SUCCESS + "?activationCodeSendingFailed=true";

        }

        return SUCCESS;
    }

    @RequestMapping(value = "/activation/{citizenId}", method = POST)
    public String citizenActivation(@PathVariable final Long citizenId, @ModelAttribute final Citizen model) {
        final Citizen citizen = citizenService.getCitizenById(citizenId);
        if (citizen.getActivationCode().equals(model.getActivationCode())) {
            citizen.setActive(true);
            citizenService.update(citizen);
            return "redirect:/../egi/login/secure?citizenActivationSuccess=true";
        } else
            return "redirect:/../egi/login/secure?citizenActivationFailed=true&citizenId=" + citizenId;

    }

    @RequestMapping(value = "/activation", method = POST)
    public String citizenOTPActivation(@ModelAttribute final Citizen model) {
        final Citizen citizen = citizenService.getCitizenByActivationCode(model.getActivationCode());
        if (citizen != null) {
            if (citizen.getActivationCode().equals(model.getActivationCode()) && !citizen.isActive()) {
                citizen.setActive(true);
                citizenService.update(citizen);
                return "redirect:/../egi/login/secure?citizenActivationSuccess=true";
            } else
                return "redirect:/../egi/login/secure?citizenActivationFailed=true";
        } else
            return "redirect:/../egi/login/secure?citizenActivationFailed=true";

    }

}
