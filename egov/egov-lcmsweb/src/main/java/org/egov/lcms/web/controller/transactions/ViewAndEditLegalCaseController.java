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

import org.egov.lcms.masters.service.CourtMasterService;
import org.egov.lcms.masters.service.PetitionTypeMasterService;
import org.egov.lcms.transactions.entity.LegalCase;
import org.egov.lcms.transactions.entity.LegalCaseUploadDocuments;
import org.egov.lcms.transactions.service.LegalCaseService;
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

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/application/")
public class ViewAndEditLegalCaseController extends GenericLegalCaseController {

    @Autowired
    private LegalCaseService legalCaseService;

    @Autowired
    private PetitionTypeMasterService petitiontypeMasterService;

    @Autowired
    private CourtMasterService courtMasterService;

    @Autowired
    private LegalCaseUtil legalCaseUtil;

    @ModelAttribute
    private LegalCase getLegalCase(@RequestParam("lcNumber") final String lcNumber) {
        final LegalCase legalCase = legalCaseService.findByLcNumber(lcNumber);
        return legalCase;
    }

    @RequestMapping(value = "/view/", method = RequestMethod.GET)
    public String view(@RequestParam("lcNumber") final String lcNumber, final Model model) {
        final LegalCase legalCase = legalCaseService.findByLcNumber(lcNumber);
        final LegalCase newlegalCase = getLegalCaseDocuments(legalCase);
        model.addAttribute("legalCase", newlegalCase);
        model.addAttribute("mode", "view");
        return "legalcasedetails-view";
    }

    @RequestMapping(value = "/edit/", method = RequestMethod.GET)
    public String edit(@RequestParam("lcNumber") final String lcNumber, final Model model) {
        final LegalCase legalCase = legalCaseService.findByLcNumber(lcNumber);
        final LegalCase newlegalCase = getLegalCaseDocuments(legalCase);
        model.addAttribute("legalCase", newlegalCase);
        setDropDownValues(model);
        final String[] casenumberyear = legalCase.getCaseNumber().split("/");
        legalCase.setCaseNumber(casenumberyear[0]);
        if (casenumberyear.length > 1)
            legalCase.setWpYear(casenumberyear[1]);
        legalCase.getBipartisanPetitionerDetailsList().addAll(legalCase.getPetitioners());
        legalCase.getBipartisanRespondentDetailsList().addAll(legalCase.getRespondents());
        model.addAttribute("mode", "edit");
        return "legalcase-edit";
    }

    @RequestMapping(value = "/edit/", method = RequestMethod.POST)
    public String update(@ModelAttribute final LegalCase legalCase, @RequestParam("lcNumber") final String lcNumber,
            final BindingResult errors, @RequestParam("file") final MultipartFile[] files, final Model model,
            final RedirectAttributes redirectAttrs) throws IOException, ParseException {
        if (errors.hasErrors())
            return "legalcase-edit";

        legalCaseService.persist(legalCase, files);
        setDropDownValues(model);
        final LegalCase newlegalCase = getLegalCaseDocuments(legalCase);
        model.addAttribute("legalCase", newlegalCase);
        redirectAttrs.addFlashAttribute("legalCase", newlegalCase);
        model.addAttribute("mode", "view");
        model.addAttribute("message", "LegalCase updated successfully.");
        return "legalcase-success";
    }

    private void setDropDownValues(final Model model) {
        model.addAttribute("courtsList", courtMasterService.findAll());
        model.addAttribute("petitiontypeList", petitiontypeMasterService.getPetitiontypeList());
    }

    private LegalCase getLegalCaseDocuments(final LegalCase legalCase) {
        List<LegalCaseUploadDocuments> documentDetailsList = new ArrayList<LegalCaseUploadDocuments>();
        documentDetailsList = legalCaseUtil.getLegalCaseDocumentList(legalCase);
        legalCase.setLegalCaseUploadDocuments(documentDetailsList);
        return legalCase;
    }

}
