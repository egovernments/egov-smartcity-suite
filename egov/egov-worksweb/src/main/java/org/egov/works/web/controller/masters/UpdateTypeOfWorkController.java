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

import org.egov.commons.EgPartytype;
import org.egov.commons.EgwTypeOfWork;
import org.egov.commons.dao.EgPartytypeHibernateDAO;
import org.egov.commons.service.TypeOfWorkService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.validation.regex.Constants;
import org.egov.works.utils.WorksConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/masters")
public class UpdateTypeOfWorkController {

    @Autowired
    private TypeOfWorkService typeOfWorkService;

    @Autowired
    private EgPartytypeHibernateDAO egPartytypeHibernateDAO;

    @RequestMapping(value = "/typeofwork-update/{typeOfWorkId}", method = RequestMethod.GET)
    public String showTypeOfWorkFormToModify(final Model model, @PathVariable final Long typeOfWorkId)
            throws ApplicationException {
        final EgwTypeOfWork typeOfWork = typeOfWorkService.getTypeOfWorkById(typeOfWorkId);
        model.addAttribute("egwTypeOfWork", typeOfWork);
        model.addAttribute(WorksConstants.MODE, WorksConstants.EDIT);
        return "typeofwork-modify";
    }

    @RequestMapping(value = "/subtypeofwork-update/{subTypeOfWorkId}", method = RequestMethod.GET)
    public String showSubTypeOfWorkFormToModify(final Model model, @PathVariable final Long subTypeOfWorkId)
            throws ApplicationException {
        final EgwTypeOfWork typeOfWork = typeOfWorkService.getTypeOfWorkById(subTypeOfWorkId);
        model.addAttribute("typeOfWork", typeOfWork.getParentid());
        model.addAttribute("egwTypeOfWork", typeOfWork);
        model.addAttribute(WorksConstants.MODE, WorksConstants.EDIT);
        return "subtypeofwork-modify";
    }

    @RequestMapping(value = "/modifytypeofwork", method = RequestMethod.POST)
    public String modifyTypeOfWork(@ModelAttribute final EgwTypeOfWork egwTypeOfWork, final BindingResult resultBinder,
            final Model model, final HttpServletRequest request) throws ApplicationException {
        final String mode = request.getParameter(WorksConstants.MODE);
        if (mode.equalsIgnoreCase(WorksConstants.EDIT) && egwTypeOfWork.getId() != null)
            model.addAttribute(WorksConstants.MODE, mode);
        validateTypeOfWork(egwTypeOfWork, resultBinder);
        if (resultBinder.hasErrors()) {
            model.addAttribute("typeofwork", egwTypeOfWork);
            return "typeofwork-modify";
        }
        final EgPartytype egPartytype = egPartytypeHibernateDAO
                .getPartytypeByCode(WorksConstants.PARTY_TYPE_CONTRACTOR);
        egwTypeOfWork.setEgPartytype(egPartytype);
        typeOfWorkService.update(egwTypeOfWork);
        return "redirect:/masters/typeofwork-success?typeOfWorkId=" + egwTypeOfWork.getId();

    }

    @RequestMapping(value = "/modifysubtypeofwork", method = RequestMethod.POST)
    public String modifySubTypeOfWork(@ModelAttribute final EgwTypeOfWork egwTypeOfWork,
            final BindingResult resultBinder, final Model model, final HttpServletRequest request)
            throws ApplicationException, IOException {
        final String mode = request.getParameter(WorksConstants.MODE);
        if (mode.equalsIgnoreCase(WorksConstants.EDIT) && egwTypeOfWork.getId() != null)
            model.addAttribute(WorksConstants.MODE, mode);
        validateSubTypeOfWork(egwTypeOfWork, resultBinder);
        if (resultBinder.hasErrors()) {
            model.addAttribute("typeofwork",
                    typeOfWorkService.getTypeOfWorkByPartyType(WorksConstants.PARTY_TYPE_CONTRACTOR));
            model.addAttribute("subtypeofwork", egwTypeOfWork);
            return "subtypeofwork-modify";
        }
        final EgPartytype egPartytype = egPartytypeHibernateDAO
                .getPartytypeByCode(WorksConstants.PARTY_TYPE_CONTRACTOR);
        egwTypeOfWork.setEgPartytype(egPartytype);
        typeOfWorkService.update(egwTypeOfWork);
        return "redirect:/masters/subtypeofwork-success?subTypeOfWorkId=" + egwTypeOfWork.getId();

    }

    private void validateTypeOfWork(final EgwTypeOfWork typeOfWork, final BindingResult resultBinder) {
        final EgwTypeOfWork existingTypeOfWork = typeOfWorkService.getTypeOfWorkByCode(typeOfWork.getCode());

        checkExistingTypeOfWork(typeOfWork, resultBinder, existingTypeOfWork);

        if (typeOfWork.getCode() == null)
            resultBinder.reject("error.typeofwork.code", "error.typeofwork.code");

        if (typeOfWork.getName() == null)
            resultBinder.reject("error.typeofwork.name", "error.typeofwork.name");

        if (typeOfWork.getCode() != null && !typeOfWork.getCode().matches(WorksConstants.ALPHANUMERICWITHHYPHENSLASH))
            resultBinder.reject("error.typeofwork.code.invalid", "error.typeofwork.code.invalid");

        if (typeOfWork.getName() != null && !typeOfWork.getName().matches(WorksConstants.ALPHANUMERICWITHSPECIALCHAR))
            resultBinder.reject("error.name.invalid", "error.name.invalid");

        if (typeOfWork.getDescription() != null
                && !typeOfWork.getDescription().matches(Constants.ALPHANUMERICWITHSPECIALCHAR))
            resultBinder.reject("error.typeofwork.description.invalid", "error.typeofwork.description.invalid");

    }

    private void validateSubTypeOfWork(final EgwTypeOfWork subTypeOfWork, final BindingResult resultBinder) {
        validateTypeOfWork(subTypeOfWork, resultBinder);
        if (subTypeOfWork.getParentid() == null)
            resultBinder.reject("error.typeofwork.select", "error.typeofwork.select");
    }

    private void checkExistingTypeOfWork(final EgwTypeOfWork typeOfWork, final BindingResult resultBinder,
            final EgwTypeOfWork existingTypeOfWork) {
        if (existingTypeOfWork != null && !existingTypeOfWork.getId().equals(typeOfWork.getId()))
            resultBinder.reject("error.typeofwork.exists", new String[] { typeOfWork.getCode() },
                    "error.typeofwork.exists");
    }
}
