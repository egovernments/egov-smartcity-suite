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
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.egov.lcms.transactions.entity.AppealDocuments;
import org.egov.lcms.transactions.entity.Judgment;
import org.egov.lcms.transactions.entity.JudgmentImpl;
import org.egov.lcms.transactions.service.JudgmentImplService;
import org.egov.lcms.transactions.service.JudgmentService;
import org.egov.lcms.utils.LegalCaseUtil;
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
@RequestMapping("/judgmentimpl")
public class JudgmentImplController {

    @Autowired
    private JudgmentImplService judgmentImplService;

    @Autowired
    private JudgmentService judgmentService;
    @Autowired
    private LegalCaseUtil legalCaseUtil;

    @ModelAttribute
    private JudgmentImpl getJudgment(@RequestParam("lcNumber") final String lcNumber, final HttpServletRequest request,
            final Model model) {
        final Judgment judgment = judgmentService.findByLCNumber(lcNumber);
        if (judgment.getJudgmentImpl().isEmpty()) {
            final JudgmentImpl judgemnetImpl = new JudgmentImpl();
            model.addAttribute("mode", "create");
            return judgemnetImpl;
        } else {
            model.addAttribute("mode", "edit");
            return judgment.getJudgmentImpl().get(0);
        }
    }

    @RequestMapping(value = "/new/", method = RequestMethod.GET)
    public String viewForm(@ModelAttribute("judgmentImpl") JudgmentImpl judgmentImpl,
            @RequestParam("lcNumber") final String lcNumber, final Model model, final HttpServletRequest request) {
        final Judgment judgment = judgmentService.findByLCNumber(lcNumber);
        judgmentImpl = getJudgment(lcNumber, request, model);
        model.addAttribute("legalCase", judgment.getLegalCase());
        model.addAttribute("judgment", judgment);
        model.addAttribute("judgmentImpl", judgmentImpl);
        if (!judgmentImpl.getAppeal().isEmpty()) {
            final JudgmentImpl newjudgmentImpl = getAppealDocuments(judgmentImpl);
            model.addAttribute("judgmentImpl", newjudgmentImpl);
        }
        return "judgmentimpl-new";
    }

    @RequestMapping(value = "/new/", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute("judgmentImpl") final JudgmentImpl judgmentImpl,
            final BindingResult errors, final RedirectAttributes redirectAttrs,
            @RequestParam("lcNumber") final String lcNumber, final HttpServletRequest request,
            @RequestParam("file") final MultipartFile[] files, final Model model) throws IOException, ParseException {
        final Judgment judgment = judgmentService.findByLCNumber(lcNumber);
        if (errors.hasErrors()) {
            model.addAttribute("judgment", judgment);
            model.addAttribute("legalCase", judgment.getLegalCase());
            return "judgmentimpl-new";
        } else
            judgmentImpl.setJudgment(judgment);
        judgmentImplService.saveOrUpdate(judgmentImpl, files);
        if (judgmentImpl.getImplementationFailure() != null
                && judgmentImpl.getImplementationFailure().toString().equals("Appeal")) {
            final JudgmentImpl newjudgmentImpl = getAppealDocuments(judgmentImpl);
            model.addAttribute("judgmentImpl", newjudgmentImpl);
        }
        model.addAttribute("mode", "view");
        redirectAttrs.addFlashAttribute("judgmentImpl", judgmentImpl);
        model.addAttribute("message", "Judgment Implementation Saved successfully.");
        return "judgmentimpl-success";

    }

    private JudgmentImpl getAppealDocuments(final JudgmentImpl judgmentImpl) {
        List<AppealDocuments> documentDetailsList = new ArrayList<AppealDocuments>();
        documentDetailsList = legalCaseUtil.getAppealDocumentList(judgmentImpl);
        judgmentImpl.getAppeal().get(0).setAppealDocuments(documentDetailsList);
        return judgmentImpl;
    }

}