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

package org.egov.mrs.web.controller.application.registration;

import org.egov.commons.service.EducationalQualificationService;
import org.egov.commons.service.NationalityService;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.utils.DateUtils;
import org.egov.mrs.application.MarriageConstants;
import org.egov.mrs.application.MarriageUtils;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.egov.mrs.domain.enums.MaritalStatus;
import org.egov.mrs.domain.enums.ReligionPractice;
import org.egov.mrs.domain.service.MarriageApplicantService;
import org.egov.mrs.domain.service.MarriageDocumentService;
import org.egov.mrs.domain.service.MarriageRegistrationService;
import org.egov.mrs.masters.service.MarriageActService;
import org.egov.mrs.masters.service.MarriageFeeService;
import org.egov.mrs.masters.service.MarriageRegistrationUnitService;
import org.egov.mrs.masters.service.ReligionService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Arrays;
import java.util.Date;

import static org.egov.mrs.application.MarriageConstants.ADMINISTRATION_HIERARCHY_TYPE;
import static org.egov.mrs.application.MarriageConstants.BOUNDARY_TYPE;
import static org.egov.mrs.application.MarriageConstants.REGISTER_NO_OF_DAYS;

public class MarriageRegistrationController extends GenericWorkFlowController {

    @Autowired
    protected ReligionService religionService;

    @Autowired
    protected BoundaryService boundaryService;

    @Autowired
    protected MarriageActService marriageActService;

    @Autowired
    protected MarriageRegistrationService marriageRegistrationService;

    @Autowired
    protected MarriageFeeService marriageFeeService;

    @Autowired
    protected MarriageUtils utils;

    @Autowired
    protected MarriageDocumentService marriageDocumentService;

    @Autowired
    protected ResourceBundleMessageSource messageSource;

    @Autowired
    protected MarriageApplicantService marriageApplicantService;

    @Autowired
    protected MarriageRegistrationUnitService marriageRegistrationUnitService;

    @Autowired
    protected NationalityService nationalityService;
    
    @Autowired
    private EducationalQualificationService educationalQualificationService;
    
    @Autowired
    protected AppConfigValueService appConfigValuesService;
    
    @ModelAttribute
    public void prepareForm(final Model model) {        
        getBoundaryTypeForRegistration(model);
        model.addAttribute("localitylist", boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                MarriageConstants.BOUNDARYTYPE_LOCALITY, MarriageConstants.LOCATION_HIERARCHY_TYPE));
        model.addAttribute("religions", religionService.getReligions());
        model.addAttribute("acts", marriageActService.getActs());
        model.addAttribute("religionPractice", Arrays.asList(ReligionPractice.values()));
        model.addAttribute("maritalStatusList", Arrays.asList(MaritalStatus.values()));
        model.addAttribute("venuelist", MarriageConstants.venuelist);
        model.addAttribute("witnessRelation", MarriageConstants.witnessRelation);
        model.addAttribute("Educationqualificationlist", educationalQualificationService.getActiveQualifications());
        model.addAttribute("nationalitylist", nationalityService.findAll());
        model.addAttribute("feesList", marriageFeeService.getActiveGeneralTypeFeeses());
        model.addAttribute("generalDocuments", marriageDocumentService.getGeneralDocuments());
        model.addAttribute("individualDocuments", marriageDocumentService.getIndividualDocuments());
        model.addAttribute("marriageRegistrationUnit", marriageRegistrationUnitService.getActiveRegistrationunit());
        final AppConfigValues allowValidation = marriageFeeService.getDaysValidationAppConfValue(
                MarriageConstants.MODULE_NAME, MarriageConstants.MARRIAGEREGISTRATION_DAYS_VALIDATION);
        model.addAttribute("allowDaysValidation",
                allowValidation != null && !allowValidation.getValue().isEmpty() ? allowValidation.getValue() : "NO");
    }

    private void getBoundaryTypeForRegistration(final Model model) {
        final AppConfigValues heirarchyType = appConfigValuesService.getConfigValuesByModuleAndKey(
                MarriageConstants.MODULE_NAME, MarriageConstants.MARRIAGE_REGISTRATIONUNIT_HEIRARCHYTYPE).get(0);

        final AppConfigValues boundaryType = appConfigValuesService.getConfigValuesByModuleAndKey(
                MarriageConstants.MODULE_NAME, MarriageConstants.MARRIAGE_REGISTRATIONUNIT_BOUNDARYYTYPE).get(0);

        if (heirarchyType != null && heirarchyType.getValue() != null && !"".equals(heirarchyType.getValue())
                && boundaryType != null && boundaryType.getValue() != null && !"".equals(boundaryType.getValue())) {

            model.addAttribute("zones", boundaryService
                    .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                            boundaryType.getValue(), heirarchyType.getValue()));

        } else
            model.addAttribute("zones", boundaryService
                    .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                            BOUNDARY_TYPE, ADMINISTRATION_HIERARCHY_TYPE));
    }

    public void validateApplicationDate(final MarriageRegistration registration,
            final BindingResult errors) {
        final AppConfigValues allowValidation = marriageFeeService.getDaysValidationAppConfValue(
                MarriageConstants.MODULE_NAME, MarriageConstants.MARRIAGEREGISTRATION_DAYS_VALIDATION);
        if (allowValidation != null && !allowValidation.getValue().isEmpty()
                && "YES".equalsIgnoreCase(allowValidation.getValue()) && registration.getDateOfMarriage() != null
                && !registration.isLegacy()) {
            validateDateOfMarriage(registration, errors);
        }
    }

    private void validateDateOfMarriage(final MarriageRegistration registration, final BindingResult errors) {
        if (registration.getApplicationDate() != null) {
            if (!new DateTime(registration.getApplicationDate())
                    .isBefore(new DateTime(registration.getDateOfMarriage()).plusDays(Integer
                            .parseInt(REGISTER_NO_OF_DAYS) + 1))) {
                errors.reject("err.validate.marriageRegistration.applicationDate",
                        new String[] { DateUtils.getDefaultFormattedDate(registration.getDateOfMarriage()) }, null);
            }
        } else if (!new DateTime(new Date()).isBefore(new DateTime(registration.getDateOfMarriage()).plusDays(Integer
                .parseInt(REGISTER_NO_OF_DAYS) + 1))) {
            errors.reject("err.validate.marriageRegistration.applicationDate",
                    new String[] { DateUtils.getDefaultFormattedDate(registration.getDateOfMarriage()) }, null);
        }
    }

}
