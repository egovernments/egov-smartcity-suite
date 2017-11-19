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

package org.egov.mrs.web.controller.masters;

import org.egov.mrs.domain.entity.DocumentProofType;
import org.egov.mrs.domain.entity.MarriageDocument;
import org.egov.mrs.domain.enums.MarriageDocumentType;
import org.egov.mrs.domain.service.MarriageDocumentService;
import org.egov.mrs.web.adaptor.MarriageDocumentJsonAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

import static org.egov.infra.utils.JsonUtils.toJSON;

@Controller
@RequestMapping(value = "/masters")
public class DocumentController {

    private static final String MARRIAGETYPE = "marriagetype";
    private static final String MARRIAGE_DOCUMENT = "marriageDocument";
    private static final String DOCUMENT_CREATE = "document-create";
    private static final String DOCUMENT_UPDATE = "document-update";
    private static final String DOCUMENT_VIEW = "document-view";
    private static final String DOCUMENT_SEARCH = "document-search";
    private static final String DOCUMENT_SUCCESS = "document-success";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private MarriageDocumentService marriageDocumentService;

    @RequestMapping(value = "/document/create", method = RequestMethod.GET)
    public String loadCreateForm(final Model model) {

        model.addAttribute(MARRIAGE_DOCUMENT, new MarriageDocument());
        model.addAttribute("currentstate", "NEW");
        model.addAttribute("documentprooftype", DocumentProofType.values());
        model.addAttribute(MARRIAGETYPE, MarriageDocumentType.values());

        return DOCUMENT_CREATE;
    }

    @RequestMapping(value = "/document/create", method = RequestMethod.POST)
    public String createDcoument(
            @Valid @ModelAttribute(MARRIAGE_DOCUMENT) final MarriageDocument marriageDocument,
            final Errors errors, final Model model,
            final RedirectAttributes redirectAttributes) {
        validate(errors, marriageDocument);
        if (errors.hasErrors()) {
            model.addAttribute("currentstate", "NEW");
            return DOCUMENT_CREATE;
        }
        marriageDocumentService.create(marriageDocument);
        redirectAttributes.addFlashAttribute("message", messageSource
                .getMessage("msg.document.create.success", null, null));
        return "redirect:/masters/document/success/" + marriageDocument.getId();
    }

    private void validate(final Errors errors, final MarriageDocument marriageDocument) {
        if (marriageDocument.getName() == null)
            errors.rejectValue("name", "Notempty.mrg.Document.name");
        if (marriageDocument.getCode() == null)
            errors.rejectValue("code", "Notempty.mrg.code");
        if (marriageDocument.getType() == null)
            errors.rejectValue("type", "Notempty.mrg.Registration.type");
        if (marriageDocument.getDocumentProofType() == null)
            errors.rejectValue("documentProofType", "Notempty.mrg.Document.proof");

    }

    @RequestMapping(value = "/document/success/{id}", method = RequestMethod.GET)
    public String documentSuccess(@PathVariable final Long id, final Model model) {
        model.addAttribute(MARRIAGE_DOCUMENT, marriageDocumentService.get(id));
        return DOCUMENT_SUCCESS;
    }

    @RequestMapping(value = "/document/search/{mode}", method = RequestMethod.GET)
    public String search(final Model model, @PathVariable("mode") final String mode) {

        model.addAttribute(MARRIAGE_DOCUMENT, new MarriageDocument());
        model.addAttribute(MARRIAGETYPE, MarriageDocumentType.values());
        return DOCUMENT_SEARCH;
    }

    @RequestMapping(value = "/document/view/{id}", method = RequestMethod.GET)
    public String viewDocument(final Model model, @PathVariable("id") final Long id) {

        model.addAttribute(MARRIAGE_DOCUMENT, marriageDocumentService.get(id));
        return DOCUMENT_VIEW;
    }

    @RequestMapping(value = "/document/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String ajaxsearchDocument(
            @PathVariable("mode") final String mode,
            final Model model,
            @ModelAttribute final MarriageDocument marriageDocument) {

        final List<MarriageDocument> searchResultList = marriageDocumentService
                .searchMarriageDocument(marriageDocument);
        return new StringBuilder("{ \"data\":")
                .append(toJSON(searchResultList, MarriageDocument.class,
                        MarriageDocumentJsonAdaptor.class))
                .append("}")
                .toString();
    }

    @RequestMapping(value = "/document/update/{id}", method = RequestMethod.GET)
    public String updateForm(final Model model, @PathVariable("id") final Long id) {

        model.addAttribute(MARRIAGE_DOCUMENT, marriageDocumentService.get(id));
        model.addAttribute("documentprooftype", DocumentProofType.values());
        model.addAttribute(MARRIAGETYPE, MarriageDocumentType.values());

        return DOCUMENT_UPDATE;
    }

    @RequestMapping(value = "/document/update", method = RequestMethod.POST)
    public String updateDocument(final Model model,
            @Valid @ModelAttribute final MarriageDocument marriageDocument,
            final BindingResult errors,
            final RedirectAttributes redirectAttributes) {
        validate(errors, marriageDocument);
        if (errors.hasErrors())
            return DOCUMENT_UPDATE;
        marriageDocumentService
                .updateDocument(marriageDocument);

        redirectAttributes.addFlashAttribute("message", messageSource
                .getMessage("msg.document.update.success", null, null));
        return "redirect:/masters/document/success/" + marriageDocument.getId();
    }

}
