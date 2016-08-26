package org.egov.works.web.controller.revisionestimate;

import javax.servlet.http.HttpServletRequest;

import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.revisionestimate.entity.RevisionAbstractEstimate;
import org.egov.works.revisionestimate.entity.SearchRevisionEstimate;
import org.egov.works.revisionestimate.service.RevisionEstimateService;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.egov.works.workorder.service.WorkOrderEstimateService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/revisionestimate")
public class CancelRevisionEstimateController {

    @Autowired
    private RevisionEstimateService revisionEstimateService;
    
    @Autowired
    private WorkOrderEstimateService workOrderEstimateService;
    
    @Autowired
    private ResourceBundleMessageSource messageSource;
    
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
        String message = "";
        RevisionAbstractEstimate revisionEstimate = revisionEstimateService.getRevisionEstimateById(revisionEstimateId);
        WorkOrderEstimate workOrderEstimate = workOrderEstimateService.findWorkOrderByRevisionEstimateNumber(revisionEstimate.getEstimateNumber());
        final String mbRefNumbers = revisionEstimateService.checkIfMBCreatedForRE(workOrderEstimate);
        if (!mbRefNumbers.isEmpty())  {
            model.addAttribute("message", messageSource.getMessage("error.re.mb.created",
                    new String[] { mbRefNumbers }, null));
            return "revisionEstimate-success";  
        } else {
            final String revisionEstimates = revisionEstimateService
                    .getRevisionEstimatesGreaterThanCurrent(revisionEstimate.getParent().getId(),revisionEstimate.getCreatedDate());
            if (!revisionEstimates.equals(""))
                message = messageSource.getMessage("error.reexists.greaterthancreateddate",
                        new String[] { revisionEstimates }, null);
        }
        message = messageSource.getMessage("msg.revisionestimate.cancelled", new String[] { revisionEstimate.getEstimateNumber() }, null);
        revisionEstimate.setCancellationReason(cancellationReason);
        revisionEstimate.setCancellationRemarks(cancellationRemarks);
        revisionEstimate = revisionEstimateService.cancel(revisionEstimate);
        model.addAttribute("revisionEstimate", revisionEstimate);
        model.addAttribute("message", message);
        return "revisionEstimate-success";
    }
    
}
