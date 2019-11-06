/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
package org.egov.ptis.web.controller.transactions.courtverdict;

import static org.egov.ptis.constants.PropertyTaxConstants.ACTION;
import static org.egov.ptis.constants.PropertyTaxConstants.APPROVAL_COMMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.APPROVAL_POSITION;
import static org.egov.ptis.constants.PropertyTaxConstants.CV_FORM;
import static org.egov.ptis.constants.PropertyTaxConstants.CV_SUCCESS_FORM;
import static org.egov.ptis.constants.PropertyTaxConstants.CV_VIEW;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMAND_DETAIL_LIST;
import static org.egov.ptis.constants.PropertyTaxConstants.LOGGED_IN_USER;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_OFFICER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.RE_ASSESS;
import static org.egov.ptis.constants.PropertyTaxConstants.SUCCESS_MSG;
import static org.egov.ptis.constants.PropertyTaxConstants.UPDATE_DEMAND_DIRECTLY;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_APPROVE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_ACTION;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_REJECTED;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.entity.property.CourtVerdict;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.service.courtverdict.CourtVerdictDCBService;
import org.egov.ptis.domain.service.courtverdict.CourtVerdictService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/courtverdict/update/{id}")
public class UpdateCourtVerdictController extends GenericWorkFlowController {

    @Autowired
    private CourtVerdictService courtVerdictService;
    @Autowired
    private CourtVerdictDCBService courtVerdictDCBService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private PropertyTaxUtil propertyTaxUtil;
    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;

    @ModelAttribute
    public CourtVerdict courtVerdictModel(@PathVariable Long id) {
        return courtVerdictService.getCourtVerdictById(id);

    }

    @GetMapping
    public String view(@ModelAttribute CourtVerdict courtVerdict, Model model, HttpServletRequest request) {
        String target = null;
        String status = "";
        String date = "";
        String currState = courtVerdict.getState().getValue();
        String currentDesg = courtVerdictService.getLoggedInUserDesignation(
                courtVerdict.getCurrentState().getOwnerPosition().getId(),
                securityUtils.getCurrentUser());
        List<Map<String, String>> legalCaseDetails = propertyTaxCommonUtils.getLegalCaseDetails(
                courtVerdict.getPropertyCourtCase().getCaseNo(),
                request);
        for (Map<String, String> map : legalCaseDetails) {
            status = map.get("caseStatus");
            date = map.get("caseDate");
        }
        if (!currState.endsWith(WF_STATE_REJECTED)) {
            if (!courtVerdict.getAction().equalsIgnoreCase(UPDATE_DEMAND_DIRECTLY)) {
                courtVerdictService.addModelAttributesForUpdateCV(courtVerdict, model);
                model.addAttribute("caseStatus", status);
                model.addAttribute("caseDate", date);
                courtVerdictService.addModelAttributes(model, courtVerdict.getProperty(), request);
                WorkflowContainer container = new WorkflowContainer();
                container.setCurrentDesignation(currentDesg);
                prepareWorkflow(model, courtVerdict, container);

                target = CV_VIEW;
            } else {
                courtVerdictService.addModelAttributesForUpdateCV(courtVerdict, model);
                courtVerdictDCBService.addDemandDetails(courtVerdict);
                model.addAttribute("caseStatus", status);
                model.addAttribute("caseDate", date);
                model.addAttribute(DEMAND_DETAIL_LIST, courtVerdict.getDemandDetailBeanList());
                courtVerdictService.addModelAttributes(model, courtVerdict.getProperty(), request);
                WorkflowContainer container = new WorkflowContainer();
                container.setCurrentDesignation(currentDesg);
                prepareWorkflow(model, courtVerdict, container);

                target = CV_VIEW;
            }
        } else {
            if (!courtVerdict.getAction().equalsIgnoreCase(UPDATE_DEMAND_DIRECTLY)) {
                courtVerdictService.addModelAttributesForUpdateCV(courtVerdict, model);
                courtVerdict.getProperty().getPropertyDetail()
                        .setFloorDetailsProxy(courtVerdict.getProperty().getPropertyDetail().getFloorDetails());
                courtVerdictService.addModelAttributes(model, courtVerdict.getProperty(), request);
                model.addAttribute("caseStatus", status);
                model.addAttribute("caseDate", date);
                prepareWorkflow(model, courtVerdict, new WorkflowContainer());

                target = CV_FORM;
            } else {

                courtVerdictDCBService.addDemandDetails(courtVerdict);
                model.addAttribute("dmndDetails", courtVerdict.getDemandDetailBeanList());
                courtVerdictService.addModelAttributesForUpdateCV(courtVerdict, model);
                model.addAttribute("caseStatus", status);
                model.addAttribute("caseDate", date);
                courtVerdictService.addModelAttributes(model, courtVerdict.getProperty(), request);
                prepareWorkflow(model, courtVerdict, new WorkflowContainer());

                target = CV_FORM;
            }
        }
        return target;

    }

    @PostMapping
    public String save(@ModelAttribute("courtVerdict") CourtVerdict courtVerdict, BindingResult resultBinder,
            final RedirectAttributes redirectAttributes, final Model model, final HttpServletRequest request,
            @RequestParam String workFlowAction) {

        final Boolean loggedUserIsEmployee = Boolean.valueOf(request.getParameter(LOGGED_IN_USER));
        String successMessage = null;
        Long approvalPosition = 0l;
        Long plotAreaId = null;
        Long layoutAuthorityId = null;
        String approvalComent = "";
        String workFlowAct = workFlowAction;
        String target = null;
        Map<String, String> errorMessages = new HashMap<>();
        String currentDesg = courtVerdictService.getLoggedInUserDesignation(
                courtVerdict.getCurrentState().getOwnerPosition().getId(),
                securityUtils.getCurrentUser());
        if (request.getParameter(APPROVAL_COMMENT) != null)
            approvalComent = request.getParameter(APPROVAL_COMMENT);
        if (request.getParameter(WF_ACTION) != null)
            workFlowAction = request.getParameter(WF_ACTION);
        if (request.getParameter(APPROVAL_POSITION) != null
                && !request.getParameter(APPROVAL_POSITION).isEmpty())
            approvalPosition = Long.valueOf(request.getParameter(APPROVAL_POSITION));

        if (workFlowAct.equalsIgnoreCase(WFLOW_ACTION_STEP_APPROVE)) {
            courtVerdictService.saveCourtVerdict(courtVerdict, approvalPosition, approvalComent, null, workFlowAction,
                    loggedUserIsEmployee, courtVerdict.getAction());

            successMessage = "Court Verdict is approved Successfully by : "
                    + propertyTaxUtil.getApproverUserName(courtVerdict.getState().getOwnerPosition().getId())
                    + " with application number : " + courtVerdict.getApplicationNumber();
            model.addAttribute(SUCCESS_MSG, successMessage);

            target = CV_SUCCESS_FORM;
        } else if (workFlowAct.equalsIgnoreCase(WFLOW_ACTION_STEP_REJECT)) {

            courtVerdictService.saveCourtVerdict(courtVerdict, approvalPosition, approvalComent, null, workFlowAction,
                    loggedUserIsEmployee, courtVerdict.getAction());
            if (currentDesg.contains(REVENUE_OFFICER_DESGN))
                successMessage = "Court Verdict is completely rejected by :"
                        + propertyTaxUtil.getApproverUserName(courtVerdict.getState().getOwnerPosition().getId())
                        + " with application number "
                        + courtVerdict.getApplicationNumber();
            else
                successMessage = "Court Verdict rejected and forwarded to :"
                        + propertyTaxUtil.getApproverUserName(courtVerdict.getState().getOwnerPosition().getId())
                        + " with application number "
                        + courtVerdict.getApplicationNumber();

            model.addAttribute(SUCCESS_MSG, successMessage);

            target = CV_SUCCESS_FORM;
        } else {
            if (courtVerdict.getProperty().getPropertyDetail().getPropertyTypeMaster().getCode()
                    .equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)) {
                if (courtVerdict.getAction().equalsIgnoreCase(RE_ASSESS)) {
                    plotAreaId = Long.valueOf(request.getParameter("plotId"));
                    layoutAuthorityId = Long.valueOf(request.getParameter("layoutId"));
                } else if (courtVerdict.getProperty().getPropertyDetail().getVacantLandPlotArea() != null
                        && courtVerdict.getProperty().getPropertyDetail().getLayoutApprovalAuthority() != null) {
                    plotAreaId = courtVerdict.getProperty().getPropertyDetail().getVacantLandPlotArea().getId();
                    layoutAuthorityId = courtVerdict.getProperty().getPropertyDetail().getLayoutApprovalAuthority().getId();

                }
            }

            if (courtVerdict.getAction().equalsIgnoreCase(RE_ASSESS) && currentDesg.contains(REVENUE_OFFICER_DESGN)) {
                errorMessages = courtVerdictService.validate(courtVerdict, plotAreaId, layoutAuthorityId);
                if (errorMessages.isEmpty()) {
                    courtVerdictService.updatePropertyDetails(courtVerdict, plotAreaId, layoutAuthorityId);
                    PropertyImpl modProperty = courtVerdictDCBService.modifyDemand(courtVerdict.getProperty(),
                            courtVerdict.getBasicProperty().getActiveProperty());

                    if (modProperty == null)
                        courtVerdict.getBasicProperty().addProperty(courtVerdict.getProperty());
                    else
                        courtVerdict.getBasicProperty().addProperty(modProperty);
                    target = successWorkflow(courtVerdict, approvalPosition, approvalComent, workFlowAction, loggedUserIsEmployee,
                            model);
                } else
                    target = courtVerdictService.displayErrors(courtVerdict, model, errorMessages, request);
            } else if (courtVerdict.getAction().equalsIgnoreCase(UPDATE_DEMAND_DIRECTLY)
                    && currentDesg.contains(REVENUE_OFFICER_DESGN)) {
                errorMessages = courtVerdictDCBService.validateDemand(courtVerdict.getDemandDetailBeanList());
                if (errorMessages.isEmpty()) {
                    courtVerdictDCBService.updateDemandDetailVariation(courtVerdict);
                    target = successWorkflow(courtVerdict, approvalPosition, approvalComent, workFlowAction, loggedUserIsEmployee,
                            model);
                } else
                    target = courtVerdictService.displayErrors(courtVerdict, model, errorMessages, request);
            } else if (courtVerdict.getAction().equalsIgnoreCase("CANCEL_PROP") && currentDesg.contains(REVENUE_OFFICER_DESGN)) {
                courtVerdictService.updatePropertyDetails(courtVerdict, plotAreaId, layoutAuthorityId);
                courtVerdictService.setPtDemandSet(courtVerdict);
                courtVerdict.getBasicProperty().addProperty(courtVerdict.getProperty());
                target = successWorkflow(courtVerdict, approvalPosition, approvalComent, workFlowAction, loggedUserIsEmployee,
                        model);
            }
            else {
                target = successWorkflow(courtVerdict, approvalPosition, approvalComent, workFlowAction, loggedUserIsEmployee,
                        model);
            }

        }

        return target;

    }

    public String successWorkflow(CourtVerdict courtVerdict, Long approvalPosition, String approvalComent, String workFlowAction,
            Boolean loggedUserIsEmployee, Model model) {
        String successMessage = null;
        courtVerdictService.saveCourtVerdict(courtVerdict, approvalPosition, approvalComent, null, workFlowAction,
                loggedUserIsEmployee, courtVerdict.getAction());

        successMessage = "Court Verdict Saved Successfully in the System and forwarded to :"
                + propertyTaxUtil.getApproverUserName(courtVerdict.getState().getOwnerPosition().getId())
                + " with application number "
                + courtVerdict.getApplicationNumber();

        model.addAttribute(SUCCESS_MSG, successMessage);

        return CV_SUCCESS_FORM;
    }
}