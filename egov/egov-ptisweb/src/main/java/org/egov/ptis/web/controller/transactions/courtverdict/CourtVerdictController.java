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
import static org.egov.ptis.constants.PropertyTaxConstants.COURT_VERDICT;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENT_STATE;
import static org.egov.ptis.constants.PropertyTaxConstants.CV_FORM;
import static org.egov.ptis.constants.PropertyTaxConstants.CV_SUCCESS_FORM;
import static org.egov.ptis.constants.PropertyTaxConstants.ENDORSEMENT_NOTICE;
import static org.egov.ptis.constants.PropertyTaxConstants.LOGGED_IN_USER;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.RE_ASSESS;
import static org.egov.ptis.constants.PropertyTaxConstants.STATE_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.SUCCESS_MSG;
import static org.egov.ptis.constants.PropertyTaxConstants.TARGET_WORKFLOW_ERROR;
import static org.egov.ptis.constants.PropertyTaxConstants.UPDATE_DEMAND_DIRECTLY;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_ACTION;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.egov.demand.model.EgDemandDetails;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.ptis.bean.demand.DemandDetail;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.CourtVerdict;
import org.egov.ptis.domain.entity.property.PropertyCourtCase;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.service.courtverdict.CourtVerdictDCBService;
import org.egov.ptis.domain.service.courtverdict.CourtVerdictService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.master.service.PropertyCourtCaseService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/courtverdict")
public class CourtVerdictController extends GenericWorkFlowController {

    private static final String APPROVAL_POSITION = "approvalPosition";
    private static final String ERROR_MSG = "errorMsg";
    private static final String CREATED = "Created";
    private static final String PROPERTY_ID = "propertId";
    private static final String CV_SUCCESS_MSG = "Court Verdict Saved Successfully in the System and forwarded to :";
    private CourtVerdict oldCourtVerdict;

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private BasicPropertyDAO basicPropertyDAO;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private PropertyCourtCaseService propCourtCaseService;
    @Autowired
    private CourtVerdictService courtVerdictService;
    @Autowired
    private CourtVerdictDCBService courtVerdictDCBService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;
    @Autowired
    private PropertyTaxUtil propertyTaxUtil;
    @Autowired
    private PtDemandDao ptDemandDAO;

    @ModelAttribute
    public CourtVerdict courtVerdict(@PathVariable("assessmentNo") String assessmentNo) {
        CourtVerdict courtVerdict = new CourtVerdict();
        PropertyImpl propertyImpl = new PropertyImpl();
        BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
        if (basicProperty != null) {
            courtVerdict.setBasicProperty((BasicPropertyImpl) basicProperty);
            propertyImpl = (PropertyImpl) basicProperty.getActiveProperty().createPropertyclone();
            courtVerdict.setProperty(propertyImpl);
        }
        return courtVerdict;
    }

    @GetMapping(value = "/viewform/{assessmentNo}")
    public String view(@ModelAttribute CourtVerdict courtVerdict, Model model, final HttpServletRequest request,
            @PathVariable String assessmentNo) {

        String status = "";
        String date = "";
        BasicProperty basicProperty = courtVerdict.getBasicProperty();
        PropertyImpl property = courtVerdict.getProperty();
        User loggedInUser = securityUtils.getCurrentUser();
        propertyService.isCitizenPortalUser(loggedInUser);
        PropertyCourtCase propCourtCase = propCourtCaseService.getByAssessmentNo(assessmentNo);
        if (propCourtCase != null) {
            List<Map<String, String>> legalCaseDetails = propertyTaxCommonUtils.getLegalCaseDetails(
                    propCourtCase.getCaseNo(),
                    request);
            for (Map<String, String> map : legalCaseDetails) {
                status = map.get("caseStatus");
                date = map.get("caseDate");
            }
            if (status.equalsIgnoreCase(CREATED)) {
                model.addAttribute("wfPendingMsg",
                        "Court Verdict will do when the case status is under Interim Stay/Judgement/Hearing in Progress/ Closed.");
                return TARGET_WORKFLOW_ERROR;
            } else
                courtVerdict.setPropertyCourtCase(propCourtCase);
        } else {
            model.addAttribute("wfPendingMsg",
                    "Could not do Court Verdict now, mark the property under court case");
            return TARGET_WORKFLOW_ERROR;
        }
        if (basicProperty.isUnderWorkflow()
                || courtVerdict.getCurrentState() != null) {
            model.addAttribute("wfPendingMsg",
                    "Could not do Court Verdict now, property is undergoing some work flow.");
            return TARGET_WORKFLOW_ERROR;
        }

        if (courtVerdict.getPropertyCourtCase() != null) {
            model.addAttribute(COURT_VERDICT, courtVerdict);
            model.addAttribute(PROPERTY, property);
            model.addAttribute(CURRENT_STATE, CREATED);
            model.addAttribute(STATE_TYPE, courtVerdict.getClass().getSimpleName());
            model.addAttribute(ENDORSEMENT_NOTICE, new ArrayList<>());
            model.addAttribute(LOGGED_IN_USER, propertyService.isEmployee(loggedInUser));
            property.getPropertyDetail().setFloorDetailsProxy(courtVerdict.getProperty().getPropertyDetail().getFloorDetails());

            courtVerdictService.addModelAttributes(model, property, request);

            Set<EgDemandDetails> demandDetails = ptDemandDAO.getNonHistoryCurrDmdForProperty(basicProperty.getProperty())
                    .getEgDemandDetails();

            List<EgDemandDetails> dmndDetails = new ArrayList<>(demandDetails);
            if (!dmndDetails.isEmpty())
                dmndDetails = courtVerdictDCBService.sortDemandDetails(dmndDetails);
            List<DemandDetail> demandDetailBeanList = courtVerdictDCBService.setDemandBeanList(dmndDetails);
            courtVerdict.setDemandDetailBeanList(demandDetailBeanList);
            model.addAttribute("dmndDetails", demandDetailBeanList);
            model.addAttribute("caseStatus", status);
            model.addAttribute("caseDate", date);
            prepareWorkflow(model, courtVerdict, new WorkflowContainer());

        }
        oldCourtVerdict = courtVerdict;
        return CV_FORM;
    }

    @PostMapping(value = "/viewform/{assessmentNo}")
    public String save(@ModelAttribute("courtVerdict") CourtVerdict courtVerdict, final RedirectAttributes redirectAttributes,
            final Model model, final HttpServletRequest request, @RequestParam String workFlowAction,
            @PathVariable String assessmentNo) {
        Long plotAreaId = null;
        Long layoutAuthorityId = null;
        String action = courtVerdict.getAction();
        String status = "";
        String date = "";
        String target = null;
        PropertyImpl property = courtVerdict.getProperty();
        PropertyImpl oldProperty = courtVerdict.getBasicProperty().getActiveProperty();
        if (property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)) {
            if (action.equalsIgnoreCase(RE_ASSESS)) {
                plotAreaId = Long.valueOf(request.getParameter("plotId"));
                layoutAuthorityId = Long.valueOf(request.getParameter("layoutId"));
            } else if (courtVerdict.getProperty().getPropertyDetail().getVacantLandPlotArea() != null
                    && courtVerdict.getProperty().getPropertyDetail().getLayoutApprovalAuthority() != null) {
                plotAreaId = courtVerdict.getProperty().getPropertyDetail().getVacantLandPlotArea().getId();
                layoutAuthorityId = courtVerdict.getProperty().getPropertyDetail().getLayoutApprovalAuthority().getId();

            }
        }
        PropertyCourtCase propCourtCase = propCourtCaseService.getByAssessmentNo(assessmentNo);
        courtVerdict.setPropertyCourtCase(propCourtCase);
        List<Map<String, String>> legalCaseDetails = propertyTaxCommonUtils.getLegalCaseDetails(
                propCourtCase.getCaseNo(),
                request);
        for (Map<String, String> map : legalCaseDetails) {
            status = map.get("caseStatus");
            date = map.get("caseDate");
        }
        final Boolean loggedUserIsEmployee = Boolean.valueOf(request.getParameter(LOGGED_IN_USER));
        User loggedInUser = securityUtils.getCurrentUser();
        Map<String, String> errorMessages = new HashMap<>();
        if (action.isEmpty()) {
            errorMessages.put(ACTION, "action.required");
            model.addAttribute(ERROR_MSG, errorMessages);
            model.addAttribute(COURT_VERDICT, oldCourtVerdict);
            model.addAttribute(PROPERTY, oldProperty);
            model.addAttribute(CURRENT_STATE, CREATED);
            model.addAttribute(STATE_TYPE, oldCourtVerdict.getClass().getSimpleName());
            model.addAttribute(ENDORSEMENT_NOTICE, new ArrayList<>());
            model.addAttribute("caseStatus", status);
            model.addAttribute("caseDate", date);
            model.addAttribute(LOGGED_IN_USER, propertyService.isEmployee(loggedInUser));
            courtVerdictService.addModelAttributes(model, oldCourtVerdict.getProperty(), request);

            prepareWorkflow(model, oldCourtVerdict, new WorkflowContainer());

            target = CV_FORM;

        } else {
            if (action.equalsIgnoreCase(RE_ASSESS)) {
                errorMessages = courtVerdictService.validate(courtVerdict, plotAreaId, layoutAuthorityId);
                if (errorMessages.isEmpty()) {
                    Long approvalPosition = 0l;
                    String approvalComent = "";

                    if (request.getParameter(APPROVAL_COMMENT) != null)
                        approvalComent = request.getParameter(APPROVAL_COMMENT);
                    if (request.getParameter(WF_ACTION) != null)
                        workFlowAction = request.getParameter(WF_ACTION);
                    if (request.getParameter(APPROVAL_POSITION) != null
                            && !request.getParameter(APPROVAL_POSITION).isEmpty())
                        approvalPosition = Long.valueOf(request.getParameter(APPROVAL_POSITION));

                    courtVerdictService.updatePropertyDetails(courtVerdict, plotAreaId, layoutAuthorityId);
                    PropertyImpl modProperty = courtVerdictDCBService.modifyDemand(property, oldProperty);

                    if (modProperty == null)
                        courtVerdict.getBasicProperty().addProperty(property);
                    else
                        courtVerdict.getBasicProperty().addProperty(modProperty);

                    courtVerdictService.saveCourtVerdict(courtVerdict, approvalPosition, approvalComent, null,
                            workFlowAction, loggedUserIsEmployee, action);

                    final String successMsg = CV_SUCCESS_MSG
                            + propertyTaxUtil.getApproverUserName(courtVerdict.getState().getOwnerPosition().getId())
                            + " with application number : " + courtVerdict.getApplicationNumber();

                    model.addAttribute(SUCCESS_MSG, successMsg);
                    model.addAttribute(PROPERTY_ID, courtVerdict.getBasicProperty().getUpicNo());
                    target = CV_SUCCESS_FORM;
                } else
                    target = courtVerdictService.displayErrors(courtVerdict, model, errorMessages, request);
            } else if (action.equalsIgnoreCase(UPDATE_DEMAND_DIRECTLY)) {
                Long approvalPosition = 0l;
                String approvalComent = "";
                errorMessages = courtVerdictDCBService.validateDemand(courtVerdict.getDemandDetailBeanList());
                if (errorMessages.isEmpty()) {
                    if (request.getParameter(APPROVAL_COMMENT) != null)
                        approvalComent = request.getParameter(APPROVAL_COMMENT);
                    if (request.getParameter(WF_ACTION) != null)
                        workFlowAction = request.getParameter(WF_ACTION);
                    if (request.getParameter(APPROVAL_POSITION) != null
                            && !request.getParameter(APPROVAL_POSITION).isEmpty())
                        approvalPosition = Long.valueOf(request.getParameter(APPROVAL_POSITION));

                    courtVerdictService.updatePropertyDetails(courtVerdict, plotAreaId, layoutAuthorityId);
                    courtVerdictDCBService.updateDemandDetails(courtVerdict);
                    courtVerdict.getBasicProperty().addProperty(courtVerdict.getProperty());
                    courtVerdictService.saveCourtVerdict(courtVerdict, approvalPosition, approvalComent, null,
                            workFlowAction, loggedUserIsEmployee, action);
                    final String successMsg = CV_SUCCESS_MSG
                            + propertyTaxUtil.getApproverUserName(courtVerdict.getState().getOwnerPosition().getId())
                            + " with application number : " + courtVerdict.getApplicationNumber();

                    model.addAttribute(SUCCESS_MSG, successMsg);
                    model.addAttribute(PROPERTY_ID, courtVerdict.getBasicProperty().getUpicNo());
                    target = CV_SUCCESS_FORM;
                } else
                    target = courtVerdictService.displayErrors(courtVerdict, model, errorMessages, request);

            } else {
                Long approvalPosition = 0l;
                String approvalComent = "";
                if (errorMessages.isEmpty()) {
                    if (request.getParameter(APPROVAL_COMMENT) != null)
                        approvalComent = request.getParameter(APPROVAL_COMMENT);
                    if (request.getParameter(WF_ACTION) != null)
                        workFlowAction = request.getParameter(WF_ACTION);
                    if (request.getParameter(APPROVAL_POSITION) != null
                            && !request.getParameter(APPROVAL_POSITION).isEmpty())
                        approvalPosition = Long.valueOf(request.getParameter(APPROVAL_POSITION));

                    courtVerdictService.updatePropertyDetails(courtVerdict, plotAreaId, layoutAuthorityId);
                    courtVerdictService.setPtDemandSet(courtVerdict);
                    courtVerdict.getBasicProperty().addProperty(courtVerdict.getProperty());

                    courtVerdictService.saveCourtVerdict(courtVerdict, approvalPosition, approvalComent, null,
                            workFlowAction, loggedUserIsEmployee, action);
                    final String successMsg = CV_SUCCESS_MSG
                            + propertyTaxUtil.getApproverUserName(courtVerdict.getState().getOwnerPosition().getId())
                            + " with application number : " + courtVerdict.getApplicationNumber();

                    model.addAttribute(SUCCESS_MSG, successMsg);
                    model.addAttribute(PROPERTY_ID, courtVerdict.getBasicProperty().getUpicNo());
                    target = CV_SUCCESS_FORM;
                }
            }

        }
        return target;
    }

}
