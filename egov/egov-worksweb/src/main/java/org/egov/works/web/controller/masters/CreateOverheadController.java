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

import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.infra.exception.ApplicationException;
import org.egov.works.masters.entity.Overhead;
import org.egov.works.masters.entity.OverheadRate;
import org.egov.works.masters.service.OverheadService;
import org.egov.works.services.WorksService;
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
public class CreateOverheadController {

    @Autowired
    private WorksService worksService;

    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;

    @Autowired
    private OverheadService overheadService;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @RequestMapping(value = "/overhead-newform", method = RequestMethod.GET)
    public String showNewForm(final Model model) {
        final Overhead overhead = new Overhead();
        overhead.setTempOverheadRateValues(overhead.getOverheadRates());
        setDropDownValues(model);
        model.addAttribute("overhead", overhead);
        return "overhead-form";
    }

    @RequestMapping(value = "/overhead-save", method = RequestMethod.POST)
    public String create(@ModelAttribute final Overhead overhead, final Model model, final HttpServletRequest request,
            final BindingResult resultBinder) throws ApplicationException, IOException {
        validateOverhead(overhead, resultBinder);
        final String mode = request.getParameter("mode");
        if (mode.equalsIgnoreCase("edit") && overhead.getId() != null)
            model.addAttribute("mode", "edit");
        if (resultBinder.hasErrors()) {
            setDropDownValues(model);
            model.addAttribute("overhead", overhead);
            return "overhead-form";
        }
        overheadService.createOverheadValues(overhead);
        overheadService.create(overhead);
        final Long overheadId = overhead.getId();
        return "redirect:/masters/overhead-success?overheadId=" + overheadId;
    }

    @RequestMapping(value = "/overhead-success", method = RequestMethod.GET)
    public String successView(final Model model, final HttpServletRequest request) {
        final Long overheadId = Long.valueOf(request.getParameter("overheadId"));
        final Overhead newOverhead = overheadService.getOverheadById(overheadId);
        model.addAttribute("overhead", newOverhead);
        final String mode = request.getParameter("mode");
        if (mode != null && mode.equalsIgnoreCase("edit"))
            model.addAttribute("mode", "edit");
        model.addAttribute("success",
                messageSource.getMessage("msg.overhead.create.success", new String[] { newOverhead.getName() }, null));
        model.addAttribute("modify",
                messageSource.getMessage("msg.overhead.modify.success", new String[] { newOverhead.getName() }, null));
        return "overhead-success";
    }

    private void setDropDownValues(final Model model) {
        if (worksService.getWorksConfigValue("OVERHEAD_PURPOSE") != null)
            model.addAttribute("accounts", chartOfAccountsHibernateDAO
                    .getAccountCodeByPurpose(Integer.valueOf(worksService.getWorksConfigValue("OVERHEAD_PURPOSE"))));
    }

    private void validateOverhead(final Overhead overhead, final BindingResult resultBinder) {
        final Overhead existingOverhead = overheadService.getOverheadByName(overhead.getName());
        if (existingOverhead != null && !existingOverhead.getId().equals(overhead.getId()))
            resultBinder.reject("error.overheadname.exists", new String[] { overhead.getName() },
                    "error.overheadname.exists");
        if (overhead.getName() != null && !overhead.getName().matches(WorksConstants.ALPHANUMERICWITHSPECIALCHAR))
            resultBinder.reject("error.name.invalid", "error.name.invalid");

        if (overhead.getTempOverheadRateValues() == null)
            resultBinder.reject("error.overhead.altleastone.overheadrate.needed",
                    "error.overhead.altleastone.overheadrate.needed");
        if (overhead.getName() == null)
            resultBinder.reject("error.overhead.overheadname", "error.overhead.overheadname");
        if (overhead.getDescription() == null)
            resultBinder.reject("error.overhead.overheaddescription", "error.overhead.overheaddescription");
        if (overhead.getAccountCode() == null)
            resultBinder.reject("error.overhead.overheadaccountcode", "error.overhead.overheadaccountcode");
        for (final OverheadRate overheadRates : overhead.getTempOverheadRateValues()) {
            if (overheadRates.getValidity().getEndDate() != null
                    && overheadRates.getValidity().getStartDate().after(overheadRates.getValidity().getEndDate())) {
                resultBinder.reject("overhead.date.invalid", "overhead.date.invalid");
                break;
            }
            if ((overheadRates.getPercentage() == null || overheadRates.getPercentage() == 0.0)
                    && (overheadRates.getLumpsumAmount() == null || overheadRates.getLumpsumAmount() == 0.0)) {
                resultBinder.reject("overhead.overheadRates.invalid", "overhead.overheadRates.invalid");
                break;
            }

            if (overheadRates.getPercentage() != null && overheadRates.getPercentage() > 0.0
                    && overheadRates.getLumpsumAmount() != null && overheadRates.getLumpsumAmount() > 0.0) {
                resultBinder.reject("overhead.lumpsumandpercentage.invalid", "overhead.lumpsumandpercentage.invalid");
                break;
            }

            if (overheadRates.getPercentage() != null && overheadRates.getPercentage() > 0.0
                    && overheadRates.getLumpsumAmount() != null && overheadRates.getLumpsumAmount() <= 0.0) {
                resultBinder.reject("overhead.lumpsumandpercentage.invalid", "overhead.lumpsumandpercentage.invalid");
                break;
            }

            if (overheadRates.getPercentage() != null && overheadRates.getPercentage() <= 0.0
                    && overheadRates.getLumpsumAmount() != null && overheadRates.getLumpsumAmount() > 0.0) {
                resultBinder.reject("overhead.lumpsumandpercentage.invalid", "overhead.lumpsumandpercentage.invalid");
                break;
            }

            if (overheadRates.getPercentage() != null
                    && (overheadRates.getPercentage() < 0.0 || overheadRates.getPercentage() > 100)) {
                resultBinder.reject("overhead.percentage.invalid", "overhead.percentage.invalid");
                break;
            }
        }
    }
}