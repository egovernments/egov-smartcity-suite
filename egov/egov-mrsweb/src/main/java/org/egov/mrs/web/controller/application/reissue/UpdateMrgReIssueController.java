package org.egov.mrs.web.controller.application.reissue;

import org.apache.log4j.Logger;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.mrs.application.MarriageConstants;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.egov.mrs.domain.entity.ReIssue;
import org.egov.mrs.domain.service.MarriageApplicantService;
import org.egov.mrs.domain.service.MarriageDocumentService;
import org.egov.mrs.domain.service.MarriageRegistrationService;
import org.egov.mrs.domain.service.ReIssueService;
import org.egov.mrs.masters.entity.MarriageFee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller to correct the registration data
 *
 * @author nayeem
 *
 */

@Controller
@RequestMapping(value = "/reissue")
public class UpdateMrgReIssueController extends GenericWorkFlowController {
        
    private static final Logger LOG = Logger.getLogger(UpdateMrgReIssueController.class);
        
    @Autowired
    private FileStoreService fileStoreService;
        
    @Autowired
    private ReIssueService reIssueService;

    @Autowired
    private MarriageRegistrationService marriageRegistrationService;

    @Autowired
    private MarriageApplicantService marriageApplicantService;

    @Autowired
    private MarriageDocumentService marriageDocumentService;

    @Autowired
    private ReIssueService reissueService;
    
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String showReIssueForm(@PathVariable final Long id, final Model model) {

        final ReIssue reIssue = reIssueService.get(id);
       // final MarriageRegistration registration = reIssue.getRegistration();
        
        model.addAttribute("documents", marriageDocumentService.getReIssueApplicantDocs());
        model.addAttribute("reissue", reIssue);
        
        marriageRegistrationService.prepareDocumentsForView(reIssue.getRegistration());
        marriageApplicantService.prepareDocumentsForView(reIssue.getRegistration().getHusband());
        marriageApplicantService.prepareDocumentsForView(reIssue.getRegistration().getWife());
        marriageApplicantService.prepareDocumentsForView(reIssue.getApplicant());
        model.addAttribute("applicationHistory",
                reissueService.getHistory(reIssue));
        prepareWorkFlowForReIssue(reIssue, model);
        model.addAttribute("reIssue", reIssue);
        model.addAttribute("documents", marriageDocumentService.getGeneralDocuments());
        return "reissue-view";
    }
    
    private void prepareWorkFlowForReIssue(final ReIssue reIssue, final Model model) {
        WorkflowContainer workFlowContainer = new WorkflowContainer();
        workFlowContainer.setAdditionalRule(MarriageConstants.ADDITIONAL_RULE_REGISTRATION);
        prepareWorkflow(model, reIssue, workFlowContainer);
        model.addAttribute("additionalRule", MarriageConstants.ADDITIONAL_RULE_REGISTRATION);
        model.addAttribute("stateType", reIssue.getClass().getSimpleName());  
    } 

 
}