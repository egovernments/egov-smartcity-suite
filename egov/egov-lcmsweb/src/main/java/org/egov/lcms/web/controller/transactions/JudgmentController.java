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

import org.egov.lcms.masters.service.JudgmentTypeService;
import org.egov.lcms.transactions.entity.Judgment;
import org.egov.lcms.transactions.entity.LegalCase;
import org.egov.lcms.transactions.service.JudgmentService;
import org.egov.lcms.transactions.service.LegalCaseService;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;

@Controller
@RequestMapping("/judgment")
public class JudgmentController {

    @Autowired
    private JudgmentTypeService judgmentTypeService;

    @Autowired
    private LegalCaseService legalcaseService;

    @Autowired
    private JudgmentService judgmentService;

    private void prepareNewForm(final Model model) {
        model.addAttribute("judgmentTypes", judgmentTypeService.getActiveJudgementTypes());
    }

    @RequestMapping(value = "/new/", method = RequestMethod.GET)
    public String viewForm(@ModelAttribute("judgment") final Judgment judgment,
            @RequestParam("lcNumber") final String lcNumber, final HttpServletRequest request, final Model model) {
        prepareNewForm(model);
        final LegalCase legalCase = getLegalCase(lcNumber, request);
        model.addAttribute("legalCase", legalCase);
        model.addAttribute("judgment", judgment);
        model.addAttribute("mode", "create");
        return "judgment-new";
    }

    @ModelAttribute
    private LegalCase getLegalCase(@RequestParam("lcNumber") final String lcNumber, final HttpServletRequest request) {
        final LegalCase legalCase = legalcaseService.findByLcNumber(lcNumber);
        return legalCase;
    }

    @RequestMapping(value = "/new/", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute("judgment") final Judgment judgment, final BindingResult errors,
            final RedirectAttributes redirectAttrs, @RequestParam("lcNumber") final String lcNumber,
            @RequestParam("file") final MultipartFile[] files, final HttpServletRequest request, final Model model)
            throws IOException, ParseException {
        final LegalCase legalcase = getLegalCase(lcNumber, request);
        if (errors.hasErrors()) {
            prepareNewForm(model);
            model.addAttribute("legalcase", legalcase);
            return "judgment-new";
        } else
            judgment.setLegalCase(legalcase);
        judgmentService.persist(judgment, files);
        model.addAttribute("mode", "view");
        redirectAttrs.addFlashAttribute("judgment", judgment);
        model.addAttribute("message", "Judgment Created successfully.");
        return "judgment-success";

    }

}
