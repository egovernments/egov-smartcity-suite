/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

package org.egov.tl.web.controller.transactions.closure;

import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.tl.entity.LicenseDocument;
import org.egov.tl.entity.LicenseDocumentType;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.enums.ApplicationType;
import org.egov.tl.service.LicenseClosureProcessflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

import static org.egov.infra.config.core.ApplicationThreadLocals.getUserId;
import static org.egov.tl.utils.Constants.MESSAGE;

@Component
public class UpdateLicenseClosureValidator extends LicenseClosureValidator {

    @Autowired
    private LicenseClosureProcessflowService licenseClosureProcessflowService;

    @Autowired
    private AssignmentService assignmentService;

    @Override
    public void validate(Object target, Errors errors) {
        super.validate(target, errors);
        TradeLicense license = (TradeLicense) target;

        List<LicenseDocument> supportDocs = license.getLicenseDocuments()
                .stream()
                .filter(licenseDocument -> licenseDocument.getType().isMandatory()
                        && licenseDocument.getMultipartFiles().stream().anyMatch(MultipartFile::isEmpty))
                .collect(Collectors.toList());

        List<LicenseDocument> existingDocs = license.getDocuments()
                .stream()
                .filter(licenseDocument -> licenseDocument.getType().getApplicationType().equals(ApplicationType.CLOSURE))
                .collect(Collectors.toList());


        List<Long> supportDocType = supportDocs.stream().map(LicenseDocument::getType).map(LicenseDocumentType::getId)
                .collect(Collectors.toList());

        List<Long> existingDocsType = existingDocs.stream().map(LicenseDocument::getType).map(LicenseDocumentType::getId)
                .collect(Collectors.toList());

        if (!supportDocs.isEmpty()
                && supportDocs.stream().anyMatch(
                licenseDocument -> licenseDocument.getMultipartFiles().stream().anyMatch(MultipartFile::isEmpty))
                && (existingDocs.isEmpty() || !supportDocType.stream().filter(
                licenseDocumentType -> !existingDocsType.contains(licenseDocumentType)).collect(Collectors.toList()).isEmpty())) {
            errors.reject("validate.supportDocs");
        }
    }

    public boolean closureInProgress(TradeLicense license, RedirectAttributes redirectAttributes) {

        List<Assignment> assignments = assignmentService.getAllAssignmentsByEmpId(getUserId());
        WorkFlowMatrix workFlowMatrix = licenseClosureProcessflowService.getWorkFlowMatrix(license);
        if (workFlowMatrix != null && !license.getCurrentState().getValue()
                .equals(workFlowMatrix.getCurrentState())) {
            redirectAttributes.addFlashAttribute(MESSAGE, "msg.license.process");
            return true;
        } else if (!assignments
                .stream()
                .anyMatch(assignment -> license.getCurrentState().getOwnerPosition().equals(assignment.getPosition()))) {
            redirectAttributes.addFlashAttribute(MESSAGE, "msg.reassigned");
            redirectAttributes.addFlashAttribute("ownerPosition", license.getCurrentState().getOwnerPosition().getName());
            return true;
        }
        return false;
    }

}
