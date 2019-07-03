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
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_COURT_VERDICT;
import static org.egov.ptis.constants.PropertyTaxConstants.APPROVAL_COMMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.APPROVAL_POSITION;
import static org.egov.ptis.constants.PropertyTaxConstants.COURT_VERDICT;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENT_DESG;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENT_STATE;
import static org.egov.ptis.constants.PropertyTaxConstants.CV_FORM;
import static org.egov.ptis.constants.PropertyTaxConstants.CV_SUCCESS_FORM;
import static org.egov.ptis.constants.PropertyTaxConstants.CV_VIEW;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMAND_DETAIL_LIST;
import static org.egov.ptis.constants.PropertyTaxConstants.HISTORY_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.LOGGED_IN_USER;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.RE_ASSESS;
import static org.egov.ptis.constants.PropertyTaxConstants.STATE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATE_AWARE_ID;
import static org.egov.ptis.constants.PropertyTaxConstants.STATE_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.SUCCESS_MSG;
import static org.egov.ptis.constants.PropertyTaxConstants.TRANSACTION_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.UPDATE_DEMAND_DIRECTLY;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_APPROVE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_ACTION;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_REJECTED;
import static org.egov.ptis.constants.PropertyTaxConstants.ENDORSEMENT_NOTICE;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.entity.property.CourtVerdict;
import org.egov.ptis.domain.service.courtverdict.CourtVerdictService;
import org.egov.ptis.domain.service.property.PropertyService;
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
    private PropertyService propertyService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private PropertyService propService;
    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

    @ModelAttribute
    public CourtVerdict courtVerdictModel(@PathVariable Long id) {
        return courtVerdictService.getCourtVerdictById(id);

    }

    @GetMapping
    public String view(@ModelAttribute CourtVerdict courtVerdict, Model model, HttpServletRequest request) {
        String target = null;
        String currState = courtVerdict.getState().getValue();

        User loggedInUser = securityUtils.getCurrentUser();
        List<HashMap<String, Object>> historyMap;
        String currentDesg = courtVerdictService.getLoggedInUserDesignation(
                courtVerdict.getCurrentState().getOwnerPosition().getId(),
                securityUtils.getCurrentUser());
        if (!currState.endsWith(WF_STATE_REJECTED)) {
            if (courtVerdict.getAction().equalsIgnoreCase(RE_ASSESS)) {
                model.addAttribute(COURT_VERDICT, courtVerdict);
                model.addAttribute(PROPERTY, courtVerdict.getProperty());
                model.addAttribute(ACTION, courtVerdict.getAction());
                model.addAttribute(CURRENT_STATE, courtVerdict.getCurrentState().getValue());
                model.addAttribute(TRANSACTION_TYPE, APPLICATION_TYPE_COURT_VERDICT);
                model.addAttribute(STATE_AWARE_ID, courtVerdict.getId());
                model.addAttribute(STATE_TYPE, courtVerdict.getClass().getSimpleName());
                model.addAttribute(ENDORSEMENT_NOTICE, null);
                model.addAttribute(LOGGED_IN_USER, propertyService.isEmployee(loggedInUser));
                model.addAttribute(CURRENT_DESG, currentDesg);
                courtVerdictService.addModelAttributes(model, courtVerdict.getProperty(), request);
                WorkflowContainer container = new WorkflowContainer();
                container.setCurrentDesignation(currentDesg);
                prepareWorkflow(model, courtVerdict, container);

                if (courtVerdict.getId() != null && courtVerdict.getState() != null) {
                    historyMap = propService.populateHistory(courtVerdict);
                    model.addAttribute(HISTORY_MAP, historyMap);
                    model.addAttribute(STATE, courtVerdict.getState());
                }
                target = CV_VIEW;
            } else if (courtVerdict.getAction().equalsIgnoreCase(UPDATE_DEMAND_DIRECTLY)) {
                model.addAttribute(COURT_VERDICT, courtVerdict);
                model.addAttribute(PROPERTY, courtVerdict.getProperty());
                model.addAttribute(ACTION, courtVerdict.getAction());
                model.addAttribute(CURRENT_STATE, courtVerdict.getCurrentState().getValue());
                model.addAttribute(TRANSACTION_TYPE, APPLICATION_TYPE_COURT_VERDICT);
                model.addAttribute(STATE_AWARE_ID, courtVerdict.getId());
                model.addAttribute(STATE_TYPE, courtVerdict.getClass().getSimpleName());
                model.addAttribute(ENDORSEMENT_NOTICE, null);
                courtVerdictService.addDemandDetails(courtVerdict);
                model.addAttribute(DEMAND_DETAIL_LIST, courtVerdict.getDemandDetailBeanList());
                model.addAttribute(LOGGED_IN_USER, propertyService.isEmployee(loggedInUser));
                model.addAttribute(CURRENT_DESG, currentDesg);
                courtVerdictService.addModelAttributes(model, courtVerdict.getProperty(), request);
                prepareWorkflow(model, courtVerdict, new WorkflowContainer());

                if (courtVerdict.getId() != null && courtVerdict.getState() != null) {
                    historyMap = propService.populateHistory(courtVerdict);
                    model.addAttribute(HISTORY_MAP, historyMap);
                    model.addAttribute(STATE, courtVerdict.getState());
                }
                target = CV_VIEW;
            }
        } else {
            if (courtVerdict.getAction().equalsIgnoreCase(RE_ASSESS)) {
                model.addAttribute(COURT_VERDICT, courtVerdict);
                model.addAttribute(PROPERTY, courtVerdict.getProperty());
                model.addAttribute(ACTION, courtVerdict.getAction());
                model.addAttribute(CURRENT_STATE, courtVerdict.getCurrentState().getValue());
                model.addAttribute(TRANSACTION_TYPE, APPLICATION_TYPE_COURT_VERDICT);
                model.addAttribute(STATE_AWARE_ID, courtVerdict.getId());
                model.addAttribute(STATE_TYPE, courtVerdict.getClass().getSimpleName());
                model.addAttribute(LOGGED_IN_USER, propertyService.isEmployee(loggedInUser));
                model.addAttribute(ENDORSEMENT_NOTICE, null);
                model.addAttribute(CURRENT_DESG, currentDesg);
                courtVerdictService.addModelAttributes(model, courtVerdict.getProperty(), request);
                prepareWorkflow(model, courtVerdict, new WorkflowContainer());

                if (courtVerdict.getId() != null && courtVerdict.getState() != null) {
                    historyMap = propService.populateHistory(courtVerdict);
                    model.addAttribute(HISTORY_MAP, historyMap);
                    model.addAttribute(STATE, courtVerdict.getState());
                }
                target = CV_FORM;
            } else if (courtVerdict.getAction().equalsIgnoreCase(UPDATE_DEMAND_DIRECTLY)) {
                model.addAttribute(COURT_VERDICT, courtVerdict);
                model.addAttribute(PROPERTY, courtVerdict.getProperty());
                model.addAttribute(ACTION, courtVerdict.getAction());
                model.addAttribute(CURRENT_STATE, courtVerdict.getCurrentState().getValue());
                model.addAttribute(TRANSACTION_TYPE, APPLICATION_TYPE_COURT_VERDICT);
                model.addAttribute(STATE_AWARE_ID, courtVerdict.getId());
                model.addAttribute(STATE_TYPE, courtVerdict.getClass().getSimpleName());
                model.addAttribute(ENDORSEMENT_NOTICE, null);
                courtVerdictService.addDemandDetails(courtVerdict);
                model.addAttribute(DEMAND_DETAIL_LIST, courtVerdict.getDemandDetailBeanList());
                model.addAttribute(LOGGED_IN_USER, propertyService.isEmployee(loggedInUser));
                model.addAttribute(CURRENT_DESG, currentDesg);
                courtVerdictService.addModelAttributes(model, courtVerdict.getProperty(), request);
                prepareWorkflow(model, courtVerdict, new WorkflowContainer());

                if (courtVerdict.getId() != null && courtVerdict.getState() != null) {
                    historyMap = propService.populateHistory(courtVerdict);
                    model.addAttribute(HISTORY_MAP, historyMap);
                    model.addAttribute(STATE, courtVerdict.getState());
                }
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
        String approvalComent = "";
        String workFlowAct = workFlowAction;
        String target = null;

        if (request.getParameter(APPROVAL_COMMENT) != null)
            approvalComent = request.getParameter(APPROVAL_COMMENT);
        if (request.getParameter(WF_ACTION) != null)
            workFlowAction = request.getParameter(WF_ACTION);
        if (request.getParameter(APPROVAL_POSITION) != null
                && !request.getParameter(APPROVAL_POSITION).isEmpty())
            approvalPosition = Long.valueOf(request.getParameter(APPROVAL_POSITION));

        if (workFlowAct.equalsIgnoreCase(WFLOW_ACTION_STEP_APPROVE)) {

            if (request.getParameter(ACTION).equalsIgnoreCase(UPDATE_DEMAND_DIRECTLY)) {

                courtVerdictService.updateDemand(courtVerdict);
            }

            courtVerdictService.saveCourtVerdict(courtVerdict, approvalPosition, approvalComent, null, workFlowAction,
                    loggedUserIsEmployee);

            successMessage = "Court Verdict is approved Successfully by : "
                    + propertyTaxUtil.getApproverUserName(courtVerdict.getState().getOwnerPosition().getId())
                    + " with application number : " + courtVerdict.getApplicationNumber();
            model.addAttribute(SUCCESS_MSG, successMessage);

            target = CV_SUCCESS_FORM;
        } else if (workFlowAct.equalsIgnoreCase(WFLOW_ACTION_STEP_REJECT)) {

            courtVerdictService.saveCourtVerdict(courtVerdict, approvalPosition, approvalComent, null, workFlowAction,
                    loggedUserIsEmployee);

            successMessage = "Court Verdict rejected and forwarded to :"
                    + propertyTaxUtil.getApproverUserName(courtVerdict.getState().getOwnerPosition().getId())
                    + " with application number "
                    + courtVerdict.getApplicationNumber();

            model.addAttribute(SUCCESS_MSG, successMessage);

            target = CV_SUCCESS_FORM;
        } else {

            courtVerdictService.saveCourtVerdict(courtVerdict, approvalPosition, approvalComent, null, workFlowAction,
                    loggedUserIsEmployee);

            successMessage = "Court Verdict Saved Successfully in the System and forwarded to :"
                    + propertyTaxUtil.getApproverUserName(courtVerdict.getState().getOwnerPosition().getId())
                    + " with application number "
                    + courtVerdict.getApplicationNumber();

            model.addAttribute(SUCCESS_MSG, successMessage);

            target = CV_SUCCESS_FORM;
        }

        return target;

    }
}
