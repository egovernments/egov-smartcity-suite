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
package org.egov.ptis.web.controller.vacancyremission;

import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_REJECT;

import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.entity.enums.TransactionType;
import org.egov.ptis.domain.entity.property.Document;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.VacancyRemission;
import org.egov.ptis.domain.entity.property.VacancyRemissionDetails;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.domain.service.property.VacancyRemissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/vacancyremission/")
public class VacanyRemissionMonthlyUpdateController {

    private VacancyRemissionService vacancyRemissionService;
    
    @Autowired
    private PropertyService propertyService;

    @Autowired
    public VacanyRemissionMonthlyUpdateController(VacancyRemissionService vacancyRemissionService,
            PropertyTaxUtil propertyTaxUtil) {
        this.vacancyRemissionService = vacancyRemissionService;
    }
     
    @ModelAttribute
    public VacancyRemission getVacancyRemission(@PathVariable final String assessmentNo) {
        final VacancyRemission vacancyRemission = vacancyRemissionService
                .getApprovedVacancyRemissionForProperty(assessmentNo);
        return vacancyRemission;
    }
    
    @ModelAttribute("documentsList")
    public List<DocumentType> documentsList(@ModelAttribute final VacancyRemission vacancyRemission) {
        return vacancyRemissionService.getDocuments(TransactionType.VRMONTHLYUPDATE);
    }

    @RequestMapping(value = "/monthlyupdate/{assessmentNo}", method = RequestMethod.GET)
    public String newform(final Model model, @PathVariable final String assessmentNo, final HttpServletRequest request) {
        VacancyRemission vacancyRemission = vacancyRemissionService
                .getApprovedVacancyRemissionForProperty(assessmentNo);
        List<DocumentType> documentTypes;
        VacancyRemissionDetails remissionDetails = new VacancyRemissionDetails();
        documentTypes = propertyService.getDocumentTypesForTransactionType(TransactionType.VRMONTHLYUPDATE);
        model.addAttribute("remissionDetailsObj", remissionDetails);
        model.addAttribute("documentTypes", documentTypes);
        if (!vacancyRemission.getDocuments().isEmpty()) {
            model.addAttribute("attachedDocuments", vacancyRemission.getDocuments());
        }
        vacancyRemissionService.addModelAttributes(model, vacancyRemission.getBasicProperty());
        return "vacancyRemissionDetails-form";
    }

    public void populateRemissionDetails(final VacancyRemissionDetails vacancyRemissionDetails,
            final VacancyRemission vacancyRemission) {
        List<VacancyRemissionDetails> remissionDetailsList = new ArrayList<>();
        if (vacancyRemissionDetails != null) {
            vacancyRemissionDetails.setVacancyRemission(vacancyRemission);
            if (vacancyRemission.getVacancyRemissionDetails() == null
                    || vacancyRemission.getVacancyRemissionDetails().isEmpty()) {
                remissionDetailsList.add(vacancyRemissionDetails);
            } else {
                remissionDetailsList = vacancyRemission.getVacancyRemissionDetails();
                remissionDetailsList.add(vacancyRemissionDetails);
            }
            vacancyRemission.setVacancyRemissionDetails(remissionDetailsList);
        }
    }

    @RequestMapping(value = "/monthlyupdate/{assessmentNo}", method = RequestMethod.POST)
    public String saveRemissionDetails(@Valid @ModelAttribute VacancyRemission vacancyRemission,
            final BindingResult errors, RedirectAttributes redirectAttrs, final Model model,
            final HttpServletRequest request) {
        final String wfAction = request.getParameter("wfAction");
        String comments = request.getParameter("remissionComments");
        VacancyRemissionDetails remissionDetailsObj = new VacancyRemissionDetails();
        final List<Document> documents = new ArrayList<>();
        remissionDetailsObj.setComments(comments);
        remissionDetailsObj.setCheckinDate(new Date());
        documents.addAll(vacancyRemission.getDocuments());
        vacancyRemission.getDocuments().clear();
        vacancyRemission.getDocuments().addAll(documents);
        processAndStoreApplicationDocuments(vacancyRemission);
        if(WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(wfAction)){
            vacancyRemissionService.rejectVacancyRemission(vacancyRemission,comments,request);
            model.addAttribute("successMessage", "Vacancy Remission application rejected !");   
        }else{
        populateRemissionDetails(remissionDetailsObj, vacancyRemission);
        vacancyRemissionService.saveRemissionDetails(vacancyRemission);
        model.addAttribute("successMessage", "Remission details saved successfully!!");
        }
        return "vacancyRemission-success";
    }
    protected void processAndStoreApplicationDocuments(final VacancyRemission vacancyRemission) {
        if (!vacancyRemission.getDocuments().isEmpty())
            for (final Document applicationDocument : vacancyRemission.getDocuments()) {
                if(applicationDocument.getFile() != null) {
                    applicationDocument.setType(vacancyRemissionService.getDocType(applicationDocument.getType().getName()));
                    applicationDocument.setFiles(propertyService.addToFileStore(applicationDocument.getFile()));
                }
            }
    }
}
