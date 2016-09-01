/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
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
package org.egov.stms.web.controller.transactions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.ValidationException;

import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.stms.masters.entity.enums.PropertyType;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.service.SewerageApplicationDetailsService;
import org.egov.stms.transactions.service.SewerageThirdPartyServices;
import org.egov.stms.utils.SewerageTaxUtils;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/transactions")
public class SewerageCloseUpdateConnectionController extends GenericWorkFlowController {

    @Autowired
    private SewerageApplicationDetailsService sewerageApplicationDetailsService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private SewerageTaxUtils sewerageTaxUtils;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    @Autowired
    private SewerageThirdPartyServices sewerageThirdPartyServices;
    

    @ModelAttribute("sewerageApplicationDetails")
    public SewerageApplicationDetails getSewerageApplicationDetails(@PathVariable final String applicationNumber) {
        final SewerageApplicationDetails sewerageApplicationDetails = sewerageApplicationDetailsService
                .findByApplicationNumber(applicationNumber);
        return sewerageApplicationDetails;
    }

    @RequestMapping(value = "/closeSewerageConnection-update/{applicationNumber}", method = RequestMethod.GET)
    public String view(final Model model, @PathVariable final String applicationNumber, final HttpServletRequest request) {
        final SewerageApplicationDetails sewerageApplicationDetails = sewerageApplicationDetailsService
                .findByApplicationNumber(applicationNumber);
        model.addAttribute("sewerageApplcationDetails", sewerageApplicationDetails);
        
        final AssessmentDetails propertyOwnerDetails = sewerageThirdPartyServices.getPropertyDetails(sewerageApplicationDetails.getConnection().getShscNumber(), request);
        if(propertyOwnerDetails!=null){
           model.addAttribute("propertyOwnerDetails", propertyOwnerDetails); 
        }
        
        model.addAttribute("stateType", sewerageApplicationDetails.getClass().getSimpleName());
        model.addAttribute("sewerageApplicationDetails", sewerageApplicationDetails);
        model.addAttribute("approvalDepartmentList", departmentService.getAllDepartments());
        model.addAttribute("currentUser", sewerageTaxUtils.getCurrentUserRole(securityUtils.getCurrentUser()));
        model.addAttribute("currentState", sewerageApplicationDetails.getCurrentState().getValue());
        WorkflowContainer container = new WorkflowContainer();
        container.setAdditionalRule(sewerageApplicationDetails.getApplicationType().getCode()); 
        prepareWorkflow(model, sewerageApplicationDetails, container);
        model.addAttribute("additionalRule", sewerageApplicationDetails.getApplicationType().getCode());
        model.addAttribute("propertyTypes", PropertyType.values());
        return "closeSewerageConnection";
    }
    
    @RequestMapping(value = "/closeSewerageConnection-update/{applicationNumber}", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute SewerageApplicationDetails sewerageApplicationDetails,
            final BindingResult resultBinder, final RedirectAttributes redirectAttributes,
            final HttpServletRequest request, final HttpSession session, final Model model,@RequestParam String workFlowAction, 
            @RequestParam("files") final MultipartFile[] files) {
        
        Long approvalPosition = 0l;
        String approvalComment = "";

        if (request.getParameter("approvalComent") != null)
            approvalComment = request.getParameter("approvalComent");

        if (request.getParameter("approvalPosition") != null && !request.getParameter("approvalPosition").isEmpty())
            approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));

        if (request.getParameter("workFlowAction") != null)
            workFlowAction = request.getParameter("workFlowAction");

        try {
            if (workFlowAction != null && !workFlowAction.isEmpty()
                    && workFlowAction.equalsIgnoreCase(SewerageTaxConstants.APPROVEWORKFLOWACTION)){
                SewerageApplicationDetails parentSewerageAppDtls = sewerageApplicationDetailsService.findByConnection_ShscNumberAndIsActive(sewerageApplicationDetails.getConnection().getShscNumber());
                if(parentSewerageAppDtls!=null){
                    parentSewerageAppDtls.setActive(false);
                    sewerageApplicationDetails.setParent(parentSewerageAppDtls);
                }
            }

            SewerageApplicationDetails sdc = sewerageApplicationDetailsService.updateCloseSewerageApplicationDetails(sewerageApplicationDetails,
                    approvalPosition, approvalComment, sewerageApplicationDetails.getApplicationType().getCode(),
                    workFlowAction, null,request,session);
        } catch (final ValidationException e) {
            throw new ValidationException(e.getMessage());
        }
        
        
        //TODO : show closer notice from sewerage tax notice object.
       if (workFlowAction != null && !workFlowAction.isEmpty()
                && workFlowAction.equalsIgnoreCase(SewerageTaxConstants.WF_STATE_CONNECTION_CLOSE_BUTTON))
            return "redirect:/transactions/closeConnectionNotice?pathVar="
            + sewerageApplicationDetails.getApplicationNumber();
        
        final Assignment currentUserAssignment = assignmentService.getPrimaryAssignmentForGivenRange(securityUtils
                .getCurrentUser().getId(), new Date(), new Date());
        String nextDesign = "";
        Assignment assignObj = null;
        List<Assignment> asignList = null;
        if(approvalPosition==null || approvalPosition==0){
            approvalPosition=assignmentService.getPrimaryAssignmentForUser(sewerageApplicationDetails.getCreatedBy().getId()).getPosition().getId();
        }
        if (approvalPosition != null)
            assignObj = assignmentService.getPrimaryAssignmentForPositon(approvalPosition);
        if (assignObj != null) {
            asignList = new ArrayList<Assignment>();
            asignList.add(assignObj);
        } else if (assignObj == null && approvalPosition != null)
            asignList = assignmentService.getAssignmentsForPosition(approvalPosition, new Date());
        nextDesign = asignList != null && !asignList.isEmpty() ? asignList.get(0).getDesignation().getName() : "";
        final String pathVars = sewerageApplicationDetails.getApplicationNumber() + ","
                + sewerageTaxUtils.getApproverName(approvalPosition) + ","
                + (currentUserAssignment != null ? currentUserAssignment.getDesignation().getName() : "") + ","
                + (nextDesign != null ? nextDesign : "");
        return "redirect:/transactions/closeConnection-success?pathVars=" + pathVars;   
    }
}