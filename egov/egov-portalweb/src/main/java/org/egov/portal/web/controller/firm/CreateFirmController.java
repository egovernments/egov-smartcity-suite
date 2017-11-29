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
package org.egov.portal.web.controller.firm;

import org.egov.portal.entity.Firm;
import org.egov.portal.firm.service.FirmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/firm")
public class CreateFirmController extends BaseFirmController {

    @Autowired
    private FirmService firmService;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @GetMapping("/create")
    public String showNewForm(final Model model) {
        final Firm firm = new Firm();
        firm.setTempFirmUsers(firm.getFirmUsers());
        model.addAttribute("firm", firm);
        return "firm-create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute final Firm firm,
            final BindingResult resultBinder, final Model model) {
        validateEmail(firm, resultBinder, "create");
        if (resultBinder.hasErrors()) {
            model.addAttribute("firm", firm);
            return "firm-create";
        }
        firmService.createFirm(firm);
        final Long firmId = firm.getId();
        return "redirect:/firm/firm-success?firmId=" + firmId;
    }

    @RequestMapping(value = "/firm-success", method = RequestMethod.GET)
    public String successView(final Model model, @RequestParam final String firmId) {
        final Firm firm = firmService.getFirmById(Long.valueOf(firmId));
        model.addAttribute("firm", firm);
        model.addAttribute("success",
                messageSource.getMessage("msg.firm.create.success", new String[] { firm.getName() }, null));
        return "firm-success";
    }

}
