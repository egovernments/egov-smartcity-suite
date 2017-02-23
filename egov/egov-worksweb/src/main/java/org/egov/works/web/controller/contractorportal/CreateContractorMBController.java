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

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.service.UOMService;
import org.egov.egf.commons.EgovCommon;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.messaging.MessagingService;
import org.egov.works.config.properties.WorksApplicationProperties;
import org.egov.works.contractorportal.entity.ContractorMBHeader;
import org.egov.works.contractorportal.service.ContractorMBHeaderService;
import org.egov.works.letterofacceptance.service.LetterOfAcceptanceService;
import org.egov.works.milestone.entity.TrackMilestone;
import org.egov.works.milestone.service.TrackMilestoneService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.workorder.entity.WorkOrder;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.egov.works.workorder.service.WorkOrderEstimateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/contractorportal/mb")
public class CreateContractorMBController {

    @Autowired
    private WorkOrderEstimateService workOrderEstimateService;

    @Autowired
    private LetterOfAcceptanceService letterOfAcceptanceService;

    @Autowired
    private ContractorMBHeaderService contractorMBHeaderService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private TrackMilestoneService trackMilestoneService;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private EgovCommon egovCommon;

    @Autowired
    private UOMService uomService;
    
    @Autowired
    private WorksApplicationProperties worksApplicationProperties;

    @RequestMapping(value = "/search-loaform", method = RequestMethod.GET)
    public String showSearchLoaForm() {
        return "contractormb-loasearch";
    }

    @RequestMapping(value = "/create-form", method = RequestMethod.GET)
    public String redirectToSearchForm(final ContractorMBHeader contractorMBHeader,
            final Model model, final HttpServletRequest request) {
        return "redirect:/contractorportal/mb/search-loaform";
    }

    @RequestMapping(value = "/create-form", method = RequestMethod.POST)
    public String showContractorMBCreateForm(@ModelAttribute("contractorMBHeader") final ContractorMBHeader contractorMBHeader,
            final Model model, final HttpServletRequest request) {
        final String loaNumber = request.getParameter("workOrderNumber");
        final String isOtpValidated = request.getParameter("isotpvalidated");
        if ("false".equals(isOtpValidated))
            return "redirect:/contractorportal/mb/search-loaform";
        final WorkOrder workOrder = letterOfAcceptanceService.getApprovedWorkOrder(loaNumber);
        final WorkOrderEstimate workOrderEstimate = workOrderEstimateService.getWorkOrderEstimateByWorkOrderId(workOrder.getId());
        loadViewData(contractorMBHeader, workOrderEstimate, model);
        return "contractormb-form";
    }

    private void loadViewData(final ContractorMBHeader contractorMBHeader, final WorkOrderEstimate workOrderEstimate,
            final Model model) {
        contractorMBHeader.setWorkOrderEstimate(workOrderEstimate);
        contractorMBHeaderService.populateContractorMBDetails(contractorMBHeader);
        model.addAttribute("contractorMB", contractorMBHeader);
        model.addAttribute("documentDetails", contractorMBHeader.getDocumentDetails());
        model.addAttribute("uoms", uomService.findAll());

        final List<Long> projectCodeIdList = new ArrayList<Long>();
        projectCodeIdList.add(workOrderEstimate.getEstimate().getProjectCode().getId());
        Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();
        try {
            result = egovCommon.getTotalPaymentforProjectCode(projectCodeIdList, new Date());
        } catch (final ApplicationException e) {
            e.printStackTrace();
        }
        model.addAttribute("totalBillsPaidSoFar", result.get("amount"));

        final TrackMilestone trackMilestone = trackMilestoneService.getTrackMilestoneTotalPercentage(workOrderEstimate.getId());
        String mileStoneStatus;
        if (trackMilestone == null || trackMilestone.getTotalPercentage().compareTo(BigDecimal.ZERO) < 0)
            mileStoneStatus = WorksConstants.MILESTONE_NOT_YET_STARTED;
        else if (trackMilestone.getTotalPercentage().compareTo(BigDecimal.ZERO) > 0
                && trackMilestone.getTotalPercentage().compareTo(new BigDecimal(100)) < 0)
            mileStoneStatus = WorksConstants.MILESTONE_IN_PROGRESS;
        else
            mileStoneStatus = WorksConstants.MILESTONE_COMPLETED;
        model.addAttribute("mileStoneStatus", mileStoneStatus);
        model.addAttribute("lineEstimateRequired", worksApplicationProperties.lineEstimateRequired());
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@ModelAttribute("contractorMBHeader") final ContractorMBHeader contractorMBHeader,
            final Model model, final BindingResult errors, final HttpServletRequest request,
            final HttpServletResponse response, @RequestParam("file") final MultipartFile[] files)
            throws ApplicationException, IOException {

        contractorMBHeader.setWorkOrderEstimate(
                workOrderEstimateService.getWorkOrderEstimateById(contractorMBHeader.getWorkOrderEstimate().getId()));
        final String errorMessage = contractorMBHeaderService.validateContractorMBHeader(contractorMBHeader);
        if (!StringUtils.EMPTY.equals(errorMessage)) {
            model.addAttribute("errorMessage", errorMessage);
            loadViewData(contractorMBHeader, contractorMBHeader.getWorkOrderEstimate(), model);
            return "contractormb-form";
        } else {

            final ContractorMBHeader savedContractorMBHeader = contractorMBHeaderService.create(contractorMBHeader, files);

            final String smsMessage = messageSource.getMessage("msg.mb.created.sms",
                    new String[] { contractorMBHeader.getWorkOrderEstimate().getWorkOrder().getWorkOrderNumber(),
                            contractorMBHeader.getWorkOrderEstimate().getWorkOrder().getContractor().getName(),
                            contractorMBHeader.getMbAmount().toString() },
                    null);

            final String mobileNumber = contractorMBHeader.getWorkOrderEstimate().getWorkOrder().getEngineerIncharge()
                    .getMobileNumber();
            if (StringUtils.isNotBlank(mobileNumber))
                messagingService.sendSMS(mobileNumber, smsMessage);

            return "redirect:/contractorportal/mb/contractormb-success?contractormb=" + savedContractorMBHeader.getId();
        }
    }

    @RequestMapping(value = "/contractormb-success", method = RequestMethod.GET)
    public ModelAndView successView(@ModelAttribute ContractorMBHeader contractorMBHeader,
            @RequestParam("contractormb") final Long id, final HttpServletRequest request, final Model model) {
        if (id != null)
            contractorMBHeader = contractorMBHeaderService.getContractorMBHeaderById(id);
        final String message = messageSource.getMessage("msg.contractor.mbheader.created",
                new String[] { contractorMBHeader.getMbRefNo() }, null);
        model.addAttribute("message", message);
        return new ModelAndView("contractormb-success", "contractorMBHeader", contractorMBHeader);
    }
}
