/* eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.mrs.web.controller.application.registration;

import static org.egov.mrs.application.MarriageConstants.BOUNDARY_TYPE;
import static org.egov.mrs.application.MarriageConstants.REVENUE_HIERARCHY_TYPE;

import java.util.Arrays;

import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.mrs.application.MarriageUtils;
import org.egov.mrs.domain.enums.MaritalStatus;
import org.egov.mrs.domain.enums.ReligionPractice;
import org.egov.mrs.domain.service.MarriageApplicantService;
import org.egov.mrs.domain.service.MarriageDocumentService;
import org.egov.mrs.domain.service.MarriageRegistrationService;
import org.egov.mrs.masters.service.MarriageActService;
import org.egov.mrs.masters.service.MarriageFeeService;
import org.egov.mrs.masters.service.MarriageRegistrationUnitService;
import org.egov.mrs.masters.service.ReligionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

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
    
    @ModelAttribute
    public void prepareForm(final Model model) {
        model.addAttribute("zones",
                boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(BOUNDARY_TYPE, REVENUE_HIERARCHY_TYPE));
        model.addAttribute("religions", religionService.getReligions());
        model.addAttribute("acts", marriageActService.getActs());
        model.addAttribute("religionPractice", Arrays.asList(ReligionPractice.values()));
        model.addAttribute("maritalStatusList", Arrays.asList(MaritalStatus.values()));
        model.addAttribute("feesList", marriageFeeService.searchGeneralTypeFeeses());
        model.addAttribute("generalDocuments", marriageDocumentService.getGeneralDocuments());
        model.addAttribute("individualDocuments", marriageDocumentService.getIndividualDocuments());
        model.addAttribute("marriageRegistrationUnit", marriageRegistrationUnitService.getActiveRegistrationunit());
    }

}
