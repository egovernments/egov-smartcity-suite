package org.egov.works.web.controller.revisionestimate;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.lineestimate.entity.LineEstimateAppropriation;
import org.egov.works.lineestimate.service.LineEstimateAppropriationService;
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
    private LineEstimateAppropriationService lineEstimateAppropriationService;

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
            final String mbRefNumbers = revisionEstimateService.checkIfMBCreatedForREChangedQuantity(revisionEstimate,
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
        final LineEstimateAppropriation lea = lineEstimateAppropriationService
                .findLatestByLineEstimateDetails_EstimateNumber(revisionEstimate.getParent().getEstimateNumber());
        message = messageSource.getMessage("msg.revisionestimate.cancelled",
                new String[] { revisionEstimate.getEstimateNumber(), lea.getBudgetUsage().getAppropriationnumber() },
                null);
        model.addAttribute("revisionEstimate", revisionEstimate);
        model.addAttribute("message", message);
        return REVISIONESTIMATE_SUCCESS;
    }

}
