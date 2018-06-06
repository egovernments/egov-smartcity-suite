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

package org.egov.tl.web.controller.transactions.newlicense;

import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.enums.ApplicationType;
import org.egov.tl.service.DocumentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

import static org.egov.infra.config.core.ApplicationThreadLocals.getUserId;
import static org.egov.infra.utils.StringUtils.EMPTY;
import static org.egov.tl.utils.Constants.NEWLICENSE;
import static org.egov.tl.utils.Constants.NEWLICENSECOLLECTION;
import static org.egov.tl.utils.Constants.PERCENTILE;
import static org.egov.tl.utils.Constants.STATUS_ACKNOWLEDGED;
import static org.egov.tl.utils.Constants.STATUS_COLLECTIONPENDING;

@Controller
public class UpdateLicenseProcessflowController extends NewApplicationProcessflowController {

    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private DocumentTypeService documentTypeService;

    @ModelAttribute("tradeLicense")
    public TradeLicense getTradeLicense(@PathVariable Long licenseId, Model model) {
        TradeLicense license = (TradeLicense) licenseService.getLicenseById(licenseId);
        license.getWorkflowContainer().setAdditionalRule(license.isCollectionPending() ? NEWLICENSECOLLECTION : NEWLICENSE);
        license.getWorkflowContainer().setCurrentDesignation(license.hasState()
                ? PERCENTILE + license.getCurrentState().getOwnerPosition().getDeptDesig().getDesignation().getName() + PERCENTILE
                : EMPTY);
        model.addAttribute("isEmployee", securityUtils.currentUserIsEmployee());
        model.addAttribute("documentTypes", documentTypeService
                .getDocumentTypesByApplicationType(ApplicationType.valueOf(license.getLicenseAppType().getName().toUpperCase())));

        WorkFlowMatrix workFlowMatrix = licenseNewApplicationProcessflowService.getWorkFlowMatrix(license);
        model.addAttribute("enableFields", workFlowMatrix == null ? EMPTY : workFlowMatrix.getEnableFields());
        model.addAttribute("forwardEnabled", workFlowMatrix == null ? false : workFlowMatrix.isForwardEnabled());
        model.addAttribute("outstandingFee", tradeLicenseService.getOutstandingFee(license));
        model.addAttribute("licenseHistory", tradeLicenseService.populateHistory(license));
        model.addAttribute("validActionList", getValidActions(license));
        model.addAttribute("nextAction", workFlowMatrix == null ? EMPTY : workFlowMatrix.getNextAction());
        if (STATUS_ACKNOWLEDGED.equals(license.getStatus().getStatusCode())
                || STATUS_COLLECTIONPENDING.equals(license.getStatus().getStatusCode()))
            model.addAttribute("message", "msg.collection.pending");
        return license;
    }

    private List<String> getValidActions(TradeLicense license) {
        List<String> validActions = new ArrayList<>();
        WorkflowContainer container = license.getWorkflowContainer();
        if (license.hasState()) {
            validActions.addAll(customizedWorkFlowService.getNextValidActions(
                    license.getStateType(), container.getWorkFlowDepartment(), container.getAmountRule(),
                    container.getAdditionalRule(), license.getCurrentState().getValue(),
                    container.getPendingActions(), license.getCreatedDate(), container.getCurrentDesignation()));
            validActions.removeIf(validAction -> "Reassign".equals(validAction)
                    && license.getState().getCreatedBy().getId().equals(getUserId()));
        }
        return validActions;
    }
}