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

import org.apache.commons.lang3.RandomStringUtils;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.mrs.application.MarriageConstants;
import org.egov.mrs.masters.entity.MarriageRegistrationUnit;
import org.egov.mrs.masters.service.MarriageRegistrationUnitService;
import org.egov.mrs.web.adaptor.MarriageRegistrationUnitJsonAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.egov.mrs.application.MarriageConstants.ADMINISTRATION_HIERARCHY_TYPE;
import static org.egov.mrs.application.MarriageConstants.BOUNDARY_TYPE;

@Controller
@RequestMapping(value = "/masters")
public class RegistrationUnitController {
    private static final String MARRIAGE_REGISTRATION_UNIT = "marriageRegistrationUnit";
    private static final String REGISTRATIONUNIT_CREATE = "registrationunit-create";
    private static final String REGISTRATIONUNIT_RESULT = "registrationunit-result";
    private static final String REGISTRATIONUNIT_EDIT = "registrationunit-edit";
    private static final String REGISTRATIONUNIT_VIEW = "registrationunit-view";
    private static final String REGISTRATIONUNIT_SEARCH = "registrationunit-search";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    protected AppConfigValueService appConfigValuesService;
    
    @Autowired
    private MarriageRegistrationUnitService marriageRegistrationUnitService;

    private void prepareNewForm(Model model) {

        final AppConfigValues heirarchyType = appConfigValuesService.getConfigValuesByModuleAndKey(
                MarriageConstants.MODULE_NAME, MarriageConstants.MARRIAGE_REGISTRATIONUNIT_HEIRARCHYTYPE).get(0);

        final AppConfigValues boundaryType = appConfigValuesService.getConfigValuesByModuleAndKey(
                MarriageConstants.MODULE_NAME, MarriageConstants.MARRIAGE_REGISTRATIONUNIT_BOUNDARYYTYPE).get(0);
        
        if (heirarchyType != null && heirarchyType.getValue() != null && !"".equals(heirarchyType.getValue())
                && boundaryType != null && boundaryType.getValue() != null && !"".equals(boundaryType.getValue())) {
            model.addAttribute("zones", boundaryService
                .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                        boundaryType.getValue(), heirarchyType.getValue()));
            
        }else
            model.addAttribute("zones", boundaryService
                    .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                            BOUNDARY_TYPE, ADMINISTRATION_HIERARCHY_TYPE));
        
    }

    @RequestMapping(value = "/mrregistrationunit/new", method = RequestMethod.GET)
    public String newForm(final Model model) {
        prepareNewForm(model);
        MarriageRegistrationUnit marriageRegistrationUnit = new MarriageRegistrationUnit();
        marriageRegistrationUnit.setCode(RandomStringUtils.random(4,
                Boolean.TRUE, Boolean.TRUE).toUpperCase());
        model.addAttribute(MARRIAGE_REGISTRATION_UNIT, marriageRegistrationUnit);
        return REGISTRATIONUNIT_CREATE;
    }

    @RequestMapping(value = "/mrregistrationunit/create", method = RequestMethod.POST)
    public String createRegistrationunit(
            @Valid @ModelAttribute final MarriageRegistrationUnit marriageRegistrationUnit,
            final BindingResult errors, final Model model,
            final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            prepareNewForm(model);
            return REGISTRATIONUNIT_CREATE;
        }
        marriageRegistrationUnitService
        .createMrRegistrationUnit(marriageRegistrationUnit);
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage(
                "msg.registrationunit.success", null, null));
        return "redirect:/masters/mrregistrationunit/result/"
        + marriageRegistrationUnit.getId();
    }

    @RequestMapping(value = "/mrregistrationunit/result/{id}", method = RequestMethod.GET)
    public String resultRegistrationunit(@PathVariable("id") final Long id,
            Model model) {
        model.addAttribute(MARRIAGE_REGISTRATION_UNIT,
                marriageRegistrationUnitService.findById(id));
        return REGISTRATIONUNIT_RESULT;
    }

    @RequestMapping(value = "/mrregistrationunit/search/{mode}", method = RequestMethod.GET)
    public String searchRegistrationunit(
            @PathVariable("mode") final String mode, Model model) {
        MarriageRegistrationUnit marriageRegistrationUnit = new MarriageRegistrationUnit();
        prepareNewForm(model);
        model.addAttribute(MARRIAGE_REGISTRATION_UNIT, marriageRegistrationUnit);
        return REGISTRATIONUNIT_SEARCH;

    }

    @RequestMapping(value = "/mrregistrationunit/view/{id}", method = RequestMethod.GET)
    public String viewRegistrationunit(@PathVariable("id") final Long id,
            Model model) {
        MarriageRegistrationUnit marriageRegistrationUnit = marriageRegistrationUnitService
                .findById(id);
        prepareNewForm(model);
        model.addAttribute(MARRIAGE_REGISTRATION_UNIT, marriageRegistrationUnit);
        return REGISTRATIONUNIT_VIEW;
    }

    @RequestMapping(value = "/mrregistrationunit/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String ajaxsearchRegistrationunit(
            @PathVariable("mode") final String mode,
            Model model,
            @ModelAttribute final MarriageRegistrationUnit marriageRegistrationUnit) {
        List<MarriageRegistrationUnit> searchResultList = marriageRegistrationUnitService
                .searchMarriageRegistrationUnit(marriageRegistrationUnit);
        return new StringBuilder("{ \"data\":")
                .append(toJSON(searchResultList, MarriageRegistrationUnit.class, MarriageRegistrationUnitJsonAdaptor.class))
                .append("}").toString();
    }

    @RequestMapping(value = "/mrregistrationunit/edit/{id}", method = RequestMethod.GET)
    public String editRegistrationunit(@PathVariable("id") Long id,
            final Model model) {
        prepareNewForm(model);
        model.addAttribute(MARRIAGE_REGISTRATION_UNIT,
                marriageRegistrationUnitService.findById(id));
        return REGISTRATIONUNIT_EDIT;
    }

    @RequestMapping(value = "/mrregistrationunit/update", method = RequestMethod.POST)
    public String updateRegistrationunit(
            @Valid @ModelAttribute final MarriageRegistrationUnit marriageRegistrationUnit,
            final BindingResult errors,
            final RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            return REGISTRATIONUNIT_EDIT;
        }
        marriageRegistrationUnitService
        .updateMrRegistrationUnit(marriageRegistrationUnit);
        redirectAttributes.addFlashAttribute("message", messageSource
                .getMessage("msg.registrationunit.update.success", null, null));
        return "redirect:/masters/mrregistrationunit/result/"
        + marriageRegistrationUnit.getId();
    }

}
