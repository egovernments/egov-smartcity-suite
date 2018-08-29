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
package org.egov.lcms.web.controller.transactions;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.lcms.transactions.entity.LegalCase;
import org.egov.lcms.transactions.entity.PwrDocuments;
import org.egov.lcms.transactions.service.LegalCaseService;
import org.egov.lcms.utils.LegalCaseUtil;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/counterAffidavit")
public class CounterAffidavitController {
    
   

    @Autowired
    private LegalCaseService legalCaseService;

    @Autowired
    private LegalCaseUtil legalCaseUtil;

    @Autowired
    private SecurityUtils securityUtils;

    @RequestMapping(value = "/create/", method = RequestMethod.GET)
    public String viewForm(@ModelAttribute("legalCase") final LegalCase legalCase,
            @RequestParam("lcNumber") final String lcNumber, final Model model, final HttpServletRequest request) {
        final String advocateUserRole = checkLoggedInUserIsAdvocate(securityUtils.getCurrentUser());
        model.addAttribute("advocateUserRole", advocateUserRole);
        if (legalCase.getCounterAffidavits().isEmpty()) {
            model.addAttribute(LcmsConstants.LEGALCASE, "countercreate");
            model.addAttribute(LcmsConstants.LEGALCASE, legalCase);
        } else {
            final LegalCase newlegalCase = getPwrDocuments(legalCase);
            model.addAttribute(LcmsConstants.MODE, "counteredit");
            model.addAttribute(LcmsConstants.LEGALCASE, newlegalCase);
        }
        return "legalcase-caaffidavit";
    }

    @ModelAttribute
    private LegalCase getLegalCase(@RequestParam("lcNumber") final String lcNumber) {
        return legalCaseService.findByLcNumber(lcNumber);
    }

    @RequestMapping(value = "/create/", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute("legalCase") final LegalCase legalCase, final BindingResult errors,
            final RedirectAttributes redirectAttrs, @RequestParam("lcNumber") final String lcNumber,
            @RequestParam("file") final MultipartFile[] files, final Model model) throws IOException, ParseException {
        if (errors.hasErrors())
            return "legalcase-caaffidavit";
        else
            legalCaseService.update(legalCase, files);
        redirectAttrs.addFlashAttribute(LcmsConstants.LEGALCASE, legalCase);
        model.addAttribute("message", "Counter Affidavit Details Saved Successfully.");
        final LegalCase newlegalCase = getPwrDocuments(legalCase);
        model.addAttribute(LcmsConstants.LEGALCASE, newlegalCase);
        model.addAttribute(LcmsConstants.MODE, "view");
        return "legalcase-ca-success";

    }

    private LegalCase getPwrDocuments(final LegalCase legalCase) {
        final List<PwrDocuments> documentDetailsList = legalCaseUtil.getPwrDocumentList(legalCase);
        legalCase.getPwrList().get(0).setPwrDocuments(documentDetailsList);
        return legalCase;
    }

    public String checkLoggedInUserIsAdvocate(final User user) {
        String advocateUserRole = "";
        for (final Role role : user.getRoles())
            if (role != null && role.getName().equalsIgnoreCase(LcmsConstants.STANDING_COUNSEL_ROLE))
                advocateUserRole = role.getName();
        return advocateUserRole;

    }

}
