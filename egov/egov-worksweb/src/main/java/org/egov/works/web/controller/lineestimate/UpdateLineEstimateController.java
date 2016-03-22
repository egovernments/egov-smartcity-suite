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
package org.egov.works.web.controller.lineestimate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.egov.commons.dao.EgwTypeOfWorkHibernateDAO;
import org.egov.commons.dao.FunctionHibernateDAO;
import org.egov.commons.dao.FundHibernateDAO;
import org.egov.dao.budget.BudgetGroupDAO;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.services.masters.SchemeService;
import org.egov.works.lineestimate.entity.Beneficiary;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.lineestimate.entity.LineEstimate;
import org.egov.works.lineestimate.entity.ModeOfAllotment;
import org.egov.works.lineestimate.entity.TypeOfSlum;
import org.egov.works.lineestimate.entity.WorkCategory;
import org.egov.works.lineestimate.entity.enums.LineEstimateStatus;
import org.egov.works.lineestimate.service.LineEstimateService;
import org.egov.works.master.services.NatureOfWorkService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "/lineestimate")
public class UpdateLineEstimateController extends GenericWorkFlowController{
    @Autowired
    private LineEstimateService lineEstimateService;

    @Autowired
    private FundHibernateDAO fundHibernateDAO;

    @Autowired
    private FunctionHibernateDAO functionHibernateDAO;

    @Autowired
    private BudgetGroupDAO budgetGroupDAO;

    @Autowired
    private SchemeService schemeService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private NatureOfWorkService natureOfWorkService;

    @Autowired
    private EgwTypeOfWorkHibernateDAO egwTypeOfWorkHibernateDAO;

    @Autowired
    private BoundaryService boundaryService;
    
    @Autowired
    private SecurityUtils securityUtils;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    protected AssignmentService assignmentService;

    @ModelAttribute
    public LineEstimate getLineEstimate(@PathVariable final String lineEstimateId) {
        final LineEstimate lineEstimate = lineEstimateService.getLineEstimateById(Long.parseLong(lineEstimateId));
        return lineEstimate;
    }

    @RequestMapping(value = "/update/{lineEstimateId}", method = RequestMethod.GET)
    public String viewLineEstimate(final Model model, @PathVariable final String lineEstimateId,
            final HttpServletRequest request)
            throws ApplicationException {
        final LineEstimate lineEstimate = getLineEstimate(lineEstimateId);
        if(lineEstimate.getStatus().getCode().equals(LineEstimateStatus.REJECTED.toString()))
            setDropDownValues(model);
        
        model.addAttribute("message", WorksConstants.LINEESTIMATE_CREATE);
        return loadViewData(model, request, lineEstimate);
    }

    @RequestMapping(value = "/update/{lineEstimateId}", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute("lineEstimate") final LineEstimate lineEstimate, final BindingResult errors,
            final RedirectAttributes redirectAttributes, final Model model, final HttpServletRequest request,
            @RequestParam final String removedLineEstimateDetailsIds, @RequestParam("file") final MultipartFile[] files)
                    throws ApplicationException, IOException {
        
        String mode = "";
        String workFlowAction = "";
        LineEstimate newLineEstimate = null;
        
        if (request.getParameter("mode") != null)
            mode = request.getParameter("mode");

        if (request.getParameter("workFlowAction") != null)
            workFlowAction = request.getParameter("workFlowAction");

        Long approvalPosition = 0l;
        String approvalComment = "";

        if (request.getParameter("approvalComent") != null)
            approvalComment = request.getParameter("approvalComent");

        if (request.getParameter("approvalPosition") != null && !request.getParameter("approvalPosition").isEmpty())
            approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));

        // For Get Configured ApprovalPosition from workflow history
        if (approvalPosition == null || approvalPosition.equals(Long.valueOf(0))) {
            approvalPosition = lineEstimateService.getApprovalPositionByMatrixDesignation(
                    lineEstimate, approvalPosition, WorksConstants.NEWLINEESTIMATE,
                    mode, workFlowAction);
        }

        if ((approvalPosition == null || approvalPosition.equals(Long.valueOf(0)))
                && request.getParameter("approvalPosition") != null
                && !request.getParameter("approvalPosition").isEmpty())
            approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));
        
        if (errors.hasErrors()) {
            setDropDownValues(model);
            return loadViewData(model, request, lineEstimate);
        }
        else {
             if (null != workFlowAction)
                 newLineEstimate = lineEstimateService.updateLineEstimateDetails(lineEstimate, approvalPosition,
                        approvalComment, WorksConstants.NEWLINEESTIMATE, workFlowAction,
                        mode, null, removedLineEstimateDetailsIds, files);
            redirectAttributes.addFlashAttribute("lineEstimate", newLineEstimate);
            
            final String pathVars = worksUtils.getPathVars(newLineEstimate, approvalPosition);
            
            return "redirect:/lineestimate/lineestimate-success?pathVars=" + pathVars;
        }
    }

    private void setDropDownValues(final Model model) {
        model.addAttribute("funds", fundHibernateDAO.findAllActiveFunds());
        model.addAttribute("functions", functionHibernateDAO.getAllActiveFunctions());
        model.addAttribute("budgetHeads", budgetGroupDAO.getBudgetGroupList());
        model.addAttribute("schemes", schemeService.findAll());
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("typeOfSlum", TypeOfSlum.values());
        model.addAttribute("beneficiary", Beneficiary.values());
        model.addAttribute("modeOfAllotment", ModeOfAllotment.values());
        model.addAttribute("typeOfWork", egwTypeOfWorkHibernateDAO.getTypeOfWorkForPartyTypeContractor());
        model.addAttribute("natureOfWork", natureOfWorkService.findAll());
        model.addAttribute("workCategory",WorkCategory.values());
    }

    private String loadViewData(final Model model, final HttpServletRequest request,
            final LineEstimate lineEstimate) {
        
        model.addAttribute("stateType", lineEstimate.getClass().getSimpleName());

        model.addAttribute("additionalRule", WorksConstants.NEWLINEESTIMATE);
        model.addAttribute("currentState", lineEstimate.getCurrentState().getValue());

        prepareWorkflow(model, lineEstimate, new WorkflowContainer());
        if(lineEstimate.getState().getValue().equals(WorksConstants.WF_STATE_REJECTED))
            model.addAttribute("mode", "edit");
        else
            model.addAttribute("mode", "view");

        model.addAttribute("applicationHistory", lineEstimateService.getHistory(lineEstimate));
        model.addAttribute("approvalDepartmentList", departmentService.getAllDepartments());

        LineEstimate newLineEstimate = getEstimateDocuments(lineEstimate);
        model.addAttribute("lineEstimate", newLineEstimate);
        if (request != null && request.getParameter("message") != null && request.getParameter("message").equals("update"))
            model.addAttribute("message", WorksConstants.LINEESTIMATE_UPDATE);
        return "newLineEstimate-edit";
    }

    private LineEstimate getEstimateDocuments(final LineEstimate lineEstimate) {
        List<DocumentDetails> documentDetailsList = new ArrayList<DocumentDetails>();
        documentDetailsList = worksUtils.findByObjectIdAndObjectType(lineEstimate.getId(),
                WorksConstants.MODULE_NAME_LINEESTIMATE);
        lineEstimate.setDocumentDetails(documentDetailsList);
        return lineEstimate;
    }
}
