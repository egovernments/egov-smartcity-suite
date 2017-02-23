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
package org.egov.works.web.controller.contractorportal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.egov.egf.commons.EgovCommon;
import org.egov.infra.exception.ApplicationException;
import org.egov.works.contractorportal.entity.ContractorMBDetails;
import org.egov.works.contractorportal.entity.ContractorMBHeader;
import org.egov.works.contractorportal.service.ContractorMBHeaderService;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.milestone.entity.TrackMilestone;
import org.egov.works.milestone.service.TrackMilestoneService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/contractorportal/mb")
public class ViewContractorMBController {

    @Autowired
    private ContractorMBHeaderService contractorMBHeaderService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private EgovCommon egovCommon;

    @Autowired
    private TrackMilestoneService trackMilestoneService;

    @ModelAttribute("contractorMB")
    public ContractorMBHeader getContractorMBHeader(@PathVariable final String id) {
        final ContractorMBHeader contractorMB = contractorMBHeaderService
                .getContractorMBHeaderById(Long.parseLong(id));
        return contractorMB;
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String viewForm(final Model model, @PathVariable final Long id,
            final HttpServletRequest request) {
        final ContractorMBHeader contractorMB = contractorMBHeaderService
                .getContractorMBHeaderById(id);
        final ContractorMBHeader newcontractorMB = getContractorMBDocuments(contractorMB);
        splitBOQAndAdditionalDetails(newcontractorMB);
        model.addAttribute("documentDetails", newcontractorMB.getDocumentDetails());
        model.addAttribute(WorksConstants.MODE, WorksConstants.VIEW);
        final List<Long> projectCodeIdList = new ArrayList<Long>();
        projectCodeIdList
                .add(contractorMB.getWorkOrderEstimate().getEstimate().getProjectCode().getId());
        Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();
        try {
            result = egovCommon.getTotalPaymentforProjectCode(projectCodeIdList, new Date());
        } catch (final ApplicationException e) {
            e.printStackTrace();
        }
        model.addAttribute("totalBillsPaidSoFar", result.get("amount"));
        final TrackMilestone trackMilestone = trackMilestoneService
                .getTrackMilestoneTotalPercentage(contractorMB.getWorkOrderEstimate().getId());
        String mileStoneStatus;
        if (trackMilestone == null || trackMilestone.getTotalPercentage().compareTo(BigDecimal.ZERO) < 0)
            mileStoneStatus = WorksConstants.MILESTONE_NOT_YET_STARTED;
        else if (trackMilestone.getTotalPercentage().compareTo(BigDecimal.ZERO) > 0
                && trackMilestone.getTotalPercentage().compareTo(new BigDecimal(100)) < 0)
            mileStoneStatus = WorksConstants.MILESTONE_IN_PROGRESS;
        else
            mileStoneStatus = WorksConstants.MILESTONE_COMPLETED;
        model.addAttribute("mileStoneStatus", mileStoneStatus);
        return "contractormb-view";
    }

    private ContractorMBHeader getContractorMBDocuments(final ContractorMBHeader contractorMBHeader) {
        List<DocumentDetails> documentDetailsList = new ArrayList<DocumentDetails>();
        documentDetailsList = worksUtils.findByObjectIdAndObjectType(contractorMBHeader.getId(),
                WorksConstants.CONTRACTORMBHEADER);
        contractorMBHeader.setDocumentDetails(documentDetailsList);
        return contractorMBHeader;
    }

    @RequestMapping(value = "/contractormbs/{id}", method = RequestMethod.GET)
    public String viewContractorMBs(final Model model, @PathVariable final Long id,
            final HttpServletRequest request) {
        final List<ContractorMBHeader> contractorMBHeaders = contractorMBHeaderService
                .getContractorMBHeaderByWorkOrderEstimateId(Long.valueOf(id));
        model.addAttribute("contractorMBHeaders", contractorMBHeaders);
        return "previous-contractormbs";
    }

    private void splitBOQAndAdditionalDetails(final ContractorMBHeader contractorMBHeader) {
        contractorMBHeader.setWorkOrderBOQs((List<ContractorMBDetails>) contractorMBHeader.getWorkOrderBOQDetails());
        contractorMBHeader.setAdditionalMBDetails((List<ContractorMBDetails>) contractorMBHeader.getAdditionalDetails());
    }
}
