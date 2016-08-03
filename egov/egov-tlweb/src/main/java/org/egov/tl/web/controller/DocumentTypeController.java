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

package org.egov.tl.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.tl.entity.LicenseDocumentType;
import org.egov.tl.entity.enums.ApplicationType;
import org.egov.tl.service.DocumentTypeService;
import org.egov.tl.web.adaptor.LicenseDocumentTypeJsonAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping("/documenttype")
public class DocumentTypeController {

    @Autowired
    private DocumentTypeService documentTypeService;
    

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String documentTypeForm(@ModelAttribute("documenttype") final LicenseDocumentType documenttype,
            final BindingResult result) {
        return "document-new";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String documentTypeCreate(@Valid @ModelAttribute("documenttype") final LicenseDocumentType documenttype,
            final BindingResult errors, final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()){
            
            return "document-new";
        }
      
        documentTypeService.create(documenttype);
        redirectAttrs.addFlashAttribute("documenttype", documenttype);
        redirectAttrs.addFlashAttribute("message", "msg.document.success");
        redirectAttrs.addFlashAttribute("heading", "msg.heading.success");
        return "redirect:/documenttype/view/" + documenttype.getId();
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String documentTypeView(@PathVariable("id") final Long id, final Model model) {
        final LicenseDocumentType documenttype = documentTypeService.getDocumentTypeById(id);
        model.addAttribute("documenttype", documenttype);
        return "document-view";
    }  
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String documentTypeSearch(@ModelAttribute("documenttype") final LicenseDocumentType documenttype,
            final BindingResult result) {
        return "document-search";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String documentTypeSearch(@RequestParam final String name,
            @RequestParam final ApplicationType applicationType) {
        final List<LicenseDocumentType> searchResultList = documentTypeService.search(name, applicationType);
        final String result = new StringBuilder("{ \"data\":").append(toSearchResultJson(searchResultList)).append("}")
                .toString();
        return result;
    }

    public Object toSearchResultJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(LicenseDocumentType.class, new LicenseDocumentTypeJsonAdaptor())
                .create();
        final String json = gson.toJson(object);
        return json;
    }

}
