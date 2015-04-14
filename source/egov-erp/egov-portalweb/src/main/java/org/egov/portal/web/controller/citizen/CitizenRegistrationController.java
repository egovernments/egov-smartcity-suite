package org.egov.portal.web.controller.citizen;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.Valid;

import org.egov.exceptions.DuplicateElementException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.citizen.entity.Citizen;
import org.egov.infra.citizen.service.CitizenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/citizen")
public class CitizenRegistrationController {
    private CitizenService citizenService;
    private String mobnumberValid = "false";

    @Autowired
    public CitizenRegistrationController(CitizenService citizenService) {
        this.citizenService = citizenService;
    }

    @RequestMapping(value = "/register", method = POST)
    public String registerCitizen(@Valid @ModelAttribute Citizen citizen, final BindingResult errors,
            final RedirectAttributes redirectAttributes) {

        String SUCCESS = "redirect:/../egi/login/securityLogin.jsp";
        try {
            citizenService.create(citizen);
            citizenService.sendActivationMessage(citizen);
            SUCCESS = SUCCESS + "?citizenActivation=true&citizenId=" + citizen.getId();

        } catch (DuplicateElementException e) {

            if (e.getMessage().equals("Mobile Number already exists")) {
                SUCCESS = SUCCESS + "?mobInvalid=true";
            } else if (e.getMessage().equals("Email already exists")) {
                SUCCESS = SUCCESS + "?emailInvalid=true";
            }
        } catch (EGOVRuntimeException e) {

            SUCCESS = SUCCESS + "?activationCodeSendingFailed=true";

        }

        return SUCCESS;
    }

    @RequestMapping(value = "/activation/{citizenId}", method = POST)
    public String citizenActivation(@PathVariable Long citizenId, @ModelAttribute Citizen model) {
        Citizen citizen = citizenService.getCitizenById(citizenId);
        if (citizen.getActivationCode().equals(model.getActivationCode())) {
            citizen.setActive(true);
            citizenService.update(citizen);
            return "redirect:/../egi/login/securityLogin.jsp?citizenActivationSuccess=true";
        } else {
            return "redirect:/../egi/login/securityLogin.jsp?citizenActivationFailed=true&citizenId=" + citizenId;
        }

    }

    public String getMobnumberValid() {
        return mobnumberValid;
    }

    public void setMobnumberValid(String mobnumberValid) {
        this.mobnumberValid = mobnumberValid;
    }

}
