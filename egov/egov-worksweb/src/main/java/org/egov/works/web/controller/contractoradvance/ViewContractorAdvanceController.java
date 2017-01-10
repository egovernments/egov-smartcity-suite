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
package org.egov.works.web.controller.contractoradvance;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egov.infra.exception.ApplicationException;
import org.egov.works.contractoradvance.entity.ContractorAdvanceRequisition;
import org.egov.works.contractoradvance.service.ContractorAdvanceService;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/contractoradvance")
public class ViewContractorAdvanceController {

    @Autowired
    private ContractorAdvanceService contractorAdvanceService;

    @Autowired
    private WorksUtils worksUtils;

    @RequestMapping(value = "/view/{advanceRequisitionId}", method = RequestMethod.GET)
    public String viewMilestoneTemplate(@PathVariable final String advanceRequisitionId, final Model model,
            final HttpServletRequest request)
            throws ApplicationException {
        final ContractorAdvanceRequisition contractorAdvanceRequisition = contractorAdvanceService
                .getContractorAdvanceRequisitionById(Long.parseLong(advanceRequisitionId));
        final ContractorAdvanceRequisition updatedContractorAdvance = getContractorAdvanceDocuments(contractorAdvanceRequisition);
        final Double advancePaidTillNow = contractorAdvanceService.getTotalAdvancePaid(
                contractorAdvanceRequisition.getId() == null ? -1L : contractorAdvanceRequisition.getId(),
                contractorAdvanceRequisition.getWorkOrderEstimate().getId(),
                ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.APPROVED.toString());
        model.addAttribute("advancePaidTillNow", advancePaidTillNow);
        model.addAttribute("contractorAdvanceRequisition", updatedContractorAdvance);
        model.addAttribute("workOrderEstimate", updatedContractorAdvance.getWorkOrderEstimate());
        model.addAttribute("documentDetails", updatedContractorAdvance.getDocumentDetails());
        model.addAttribute("mode", "view");
        model.addAttribute("workflowHistory",
                worksUtils.getHistory(updatedContractorAdvance.getState(), updatedContractorAdvance.getStateHistory()));
        return "contractorAdvance-view";
    }

    private ContractorAdvanceRequisition getContractorAdvanceDocuments(
            final ContractorAdvanceRequisition contractorAdvanceRequisition) {
        List<DocumentDetails> documentDetailsList = new ArrayList<DocumentDetails>();
        documentDetailsList = worksUtils.findByObjectIdAndObjectType(contractorAdvanceRequisition.getId(),
                WorksConstants.CONTRACTOR_ADVANCE);
        contractorAdvanceRequisition.setDocumentDetails(documentDetailsList);
        return contractorAdvanceRequisition;
    }

}
