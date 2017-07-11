package org.egov.works.web.controller.revisionestimate;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.lineestimate.entity.EstimateAppropriation;
import org.egov.works.lineestimate.service.EstimateAppropriationService;
import org.egov.works.revisionestimate.entity.RevisionAbstractEstimate;
import org.egov.works.revisionestimate.entity.SearchRevisionEstimate;
import org.egov.works.revisionestimate.service.RevisionEstimateService;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.egov.works.workorder.service.WorkOrderEstimateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/revisionestimate")
public class CancelRevisionEstimateController {

    private static final String REVISIONESTIMATE_SUCCESS = "revisionEstimate-success";
    @Autowired
    private RevisionEstimateService revisionEstimateService;

    @Autowired
    private WorkOrderEstimateService workOrderEstimateService;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Autowired
    private EstimateAppropriationService estimateAppropriationService;

    @RequestMapping(value = "/cancel/search", method = RequestMethod.GET)
    public String cancelRevisionEstimateSearchForm(@ModelAttribute final SearchRevisionEstimate searchRevisionEstimate,
            final Model model) {
        searchRevisionEstimate.setRevisionEstimateStatus(AbstractEstimate.EstimateStatus.APPROVED.toString());
        model.addAttribute("createdUsers", revisionEstimateService.getRevisionEstimateCreatedByUsers());
        model.addAttribute("searchRevisionEstimate", searchRevisionEstimate);
        return "revisionestimate-cancel";
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public String cancelLetterOfAcceptance(final HttpServletRequest request, final Model model) {
        final Long revisionEstimateId = Long.parseLong(request.getParameter("id"));
        final String cancellationReason = request.getParameter("cancellationReason");
        final String cancellationRemarks = request.getParameter("cancellationRemarks");
        String message;
        RevisionAbstractEstimate revisionEstimate = revisionEstimateService.getRevisionEstimateById(revisionEstimateId);
        final WorkOrderEstimate workOrderEstimate = workOrderEstimateService
                .getWorkOrderEstimateByAbstractEstimateId(revisionEstimate.getParent().getId());

        final String revisionEstimates = revisionEstimateService.getRevisionEstimatesGreaterThanCurrent(
                revisionEstimate.getParent().getId(), revisionEstimate.getCreatedDate());
        if (!StringUtils.EMPTY.equals(revisionEstimates)) {
            messageSource.getMessage("error.reexists.greaterthancreateddate", new String[] { revisionEstimates }, null);
            return REVISIONESTIMATE_SUCCESS;
        } else {
            final String mbRefNumbers = revisionEstimateService.checkIfMBCreatedForRENonTenderedLumpSum(revisionEstimate,
                    workOrderEstimate);
            if (!StringUtils.EMPTY.equals(mbRefNumbers)) {
                model.addAttribute("message",
                        messageSource.getMessage("error.re.mb.created", new String[] { mbRefNumbers }, null));
                return REVISIONESTIMATE_SUCCESS;
            } else {
                message = revisionEstimateService.checkIfMBCreatedForREChangedQuantity(revisionEstimate,
                        workOrderEstimate);
                if (!StringUtils.EMPTY.equals(message))
                    return REVISIONESTIMATE_SUCCESS;
            }
        }
        revisionEstimate.setCancellationReason(cancellationReason);
        revisionEstimate.setCancellationRemarks(cancellationRemarks);
        revisionEstimate = revisionEstimateService.cancelRevisionEstimate(revisionEstimate);
        final EstimateAppropriation estimateAppropriation = estimateAppropriationService
                .findLatestByAbstractEstimate(revisionEstimate.getId());
        if(estimateAppropriation != null && estimateAppropriation.getBudgetUsage() != null)
            message = messageSource.getMessage("msg.revisionestimate.cancelled",
                    new String[] { revisionEstimate.getEstimateNumber(),
                            estimateAppropriation.getBudgetUsage().getAppropriationnumber() },
                    null);
        else
            message = messageSource.getMessage("msg.revisionestimate.cancelled.success",
                    new String[] { revisionEstimate.getEstimateNumber() },
                    null);
        model.addAttribute("revisionEstimate", revisionEstimate);
        model.addAttribute("message", message);
        return REVISIONESTIMATE_SUCCESS;
    }

}
