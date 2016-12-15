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
package org.egov.works.web.controller.masters;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.egov.commons.EgwTypeOfWork;
import org.egov.commons.service.TypeOfWorkService;
import org.egov.infra.exception.ApplicationException;
import org.egov.works.utils.WorksConstants;
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
@RequestMapping(value = "/masters")
public class CreateTypeOfWorkController {

    @Autowired
    private TypeOfWorkService typeOfWorkService;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @RequestMapping(value = "/typeofwork-newform", method = RequestMethod.GET)
    public String showTypeOfWorkForm(final Model model) {
        final EgwTypeOfWork typeOfWork = new EgwTypeOfWork();
        model.addAttribute("typeofwork", typeOfWork);
        return "typeofwork-form";
    }

    @RequestMapping(value = "/typeofwork-save", method = RequestMethod.POST)
    public String createTypeOfWork(@ModelAttribute("typeofwork") final EgwTypeOfWork typeOfWork, final Model model,
            final BindingResult resultBinder) throws ApplicationException, IOException {
        validateTypeOfWork(typeOfWork, resultBinder);
        if (resultBinder.hasErrors()) {
            model.addAttribute("typeofwork", typeOfWork);
            return "typeofwork-form";
        }
        typeOfWorkService.create(typeOfWork);
        return "redirect:/masters/typeofwork-success?typeOfWorkId=" + typeOfWork.getId();

    }

    @RequestMapping(value = "/typeofwork-success", method = RequestMethod.GET)
    public String successView(final Model model, final HttpServletRequest request) {
        final Long typeOfWorkId = Long.valueOf(request.getParameter("typeOfWorkId"));
        final EgwTypeOfWork newTypeOfWork = typeOfWorkService.getTypeOfWorkById(typeOfWorkId);

        model.addAttribute("typeofwork", newTypeOfWork);
        model.addAttribute("success", messageSource.getMessage("msg.typeofwork.create.success",
                new String[] { newTypeOfWork.getName() }, null));

        return "typeofwork-success";

    }

    private void validateTypeOfWork(final EgwTypeOfWork typeOfWork, final BindingResult resultBinder) {
        final EgwTypeOfWork existingTypeOfWork = typeOfWorkService.getTypeOfWorkByCode(typeOfWork.getCode());

        if (existingTypeOfWork != null && !existingTypeOfWork.getId().equals(typeOfWork.getId()))
            resultBinder.reject("error.typeofwork.exists", new String[] { typeOfWork.getCode() },
                    "error.typeofwork.exists");

        if (typeOfWork.getCode() == null)
            resultBinder.reject("error.typeofwork.code", "error.typeofwork.code");

        if (typeOfWork.getName() == null)
            resultBinder.reject("error.typeofwork.name", "error.typeofwork.name");

        if (!typeOfWork.getCode().matches(WorksConstants.alphaNumericwithspecialchar))
            resultBinder.reject("error.typeofwork.code.invalid", "error.typeofwork.code.invalid");

        if (typeOfWork.getName() != null && !typeOfWork.getName().matches(WorksConstants.alphaNumericwithspecialchar))
            resultBinder.reject("error.overheadname.invalid", "error.overheadname.invalid");

        if (typeOfWork.getDescription() != null
                && !typeOfWork.getDescription().matches(WorksConstants.alphaNumericwithspecialchar))
            resultBinder.reject("error.typeofwork.description.invalid", "error.typeofwork.description.invalid");

    }

}
