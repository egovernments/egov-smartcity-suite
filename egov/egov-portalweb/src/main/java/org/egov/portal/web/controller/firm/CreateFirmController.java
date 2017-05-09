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
package org.egov.portal.web.controller.firm;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.egov.infra.exception.ApplicationException;
import org.egov.infra.validation.regex.Constants;
import org.egov.portal.entity.Firm;
import org.egov.portal.entity.FirmUser;
import org.egov.portal.firm.service.FirmService;
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
@RequestMapping(value = "/firm")
public class CreateFirmController {

    @Autowired
    private FirmService firmService;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @RequestMapping(value = "/firm-newform", method = RequestMethod.GET)
    public String showNewForm(final Model model) {
        final Firm firm = new Firm();
        firm.setTempFirmUsers(firm.getFirmUsers());
        model.addAttribute("firm", firm);
        return "firm-form";
    }

    @RequestMapping(value = "/firm-save", method = RequestMethod.POST)
    public String create(@ModelAttribute final Firm firm, final Model model, final HttpServletRequest request,
            final BindingResult resultBinder) throws ApplicationException, IOException {
        validateFirm(firm, resultBinder);
        if (resultBinder.hasErrors()) {
            model.addAttribute("firm", firm);
            return "firm-form";
        }
        firmService.createFirm(firm);
        final Long firmId = firm.getId();
        return "redirect:/firm/firm-success?firmId=" + firmId;
    }

    private void validateFirm(final Firm firm, final BindingResult resultBinder) {

        validateFirmHeader(firm, resultBinder);

        int index = 0;
        for (final FirmUser firmUsers : firm.getTempFirmUsers()) {

            if (firmUsers.getEmailId() != null && !firmUsers.getEmailId().matches(Constants.EMAIL))
                resultBinder.rejectValue("tempFirmUsers[" + index + "].emailId",
                        "error.firm.emailid");
            if (firmUsers.getMobileNumber() != null && firmUsers.getMobileNumber().length() != 10)
                resultBinder.rejectValue("tempFirmUsers[" + index + "].mobileNumber",
                        "Pattern.citizen.mobileNumber");
            index++;
        }
    }

    private void validateFirmHeader(final Firm firm, final BindingResult resultBinder) {
        if (firm.getTempFirmUsers() == null)
            resultBinder.reject("error.firm.altleastone.overusers.needed",
                    "error.firm.altleastone.firmusers.needed");
        if (firm.getFirmName() == null)
            resultBinder.reject("error.firm.firmname", "error.firm.firmname");
        if (firm.getPan() == null)
            resultBinder.reject("error.firm.pan", "error.firm.pan");
        if (firm.getPan() != null && firm.getId() == null) {
            resultBinder.reject("error.firm.pan.unique", "error.firm.pan.unique");
        }
        if (firm.getPan() != null) {
            final Firm exitingFirm = firmService.getFirmByPan(firm.getPan());
            if (exitingFirm != null && firm.getId() != null && !exitingFirm.getId().equals(firm.getId()))
                resultBinder.reject("error.firm.pan.unique", "error.firm.pan.unique");
        }
    }

    @RequestMapping(value = "/firm-success", method = RequestMethod.GET)
    public String successView(final Model model, final HttpServletRequest request) {
        final Long firmId = Long.valueOf(request.getParameter("firmId"));
        final Firm firm = firmService.getFirmById(firmId);
        model.addAttribute("firm", firm);
        model.addAttribute("success",
                messageSource.getMessage("msg.firm.create.success", new String[] { firm.getFirmName() }, null));
        return "firm-success";
    }

}
