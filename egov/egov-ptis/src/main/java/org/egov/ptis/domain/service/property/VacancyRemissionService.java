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
package org.egov.ptis.domain.service.property;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.Installment;
import org.egov.demand.model.EgDemandDetails;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.messaging.MessagingService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.ApplicationNumberGenerator;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Position;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.entity.property.VacancyRemission;
import org.egov.ptis.domain.entity.property.VacancyRemissionApproval;
import org.egov.ptis.domain.entity.property.VacancyRemissionDetails;
import org.egov.ptis.domain.repository.vacancyremission.VacancyRemissionApprovalRepository;
import org.egov.ptis.domain.repository.vacancyremission.VacancyRemissionRepository;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.egov.ptis.constants.PropertyTaxConstants.*;

@Service
@Transactional(readOnly = true)
public class VacancyRemissionService {

    private static final Logger LOG = LoggerFactory.getLogger(VacancyRemissionService.class);

    @Autowired
    private VacancyRemissionRepository vacancyRemissionRepository;

    @Autowired
    private VacancyRemissionApprovalRepository vacancyRemissionApprovalRepository;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<VacancyRemission> vacancyRemissionWorkflowService;

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

    @Autowired
    private PtDemandDao ptDemandDAO;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private ApplicationNumberGenerator applicationNumberGenerator;
    
    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource ptisMessageSource;

    @Autowired
    private MessagingService messagingService;
    
    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;
    
    public VacancyRemission getApprovedVacancyRemissionForProperty(final String upicNo) {
        return vacancyRemissionRepository.findByUpicNo(upicNo);
    }

    public VacancyRemission getLatestRejectAckGeneratedVacancyRemissionForProperty(final String upicNo) {
        VacancyRemission vacancyRemission = null;
        List<VacancyRemission> rejectedRemissionList = vacancyRemissionRepository
                .findAllRejectionAckGeneratedForUpicNo(upicNo);
        if (!rejectedRemissionList.isEmpty()) {
            vacancyRemission = rejectedRemissionList.get(0);
        }
        return vacancyRemission;
    }

    public VacancyRemission getVacancyRemissionById(final Long id) {
        return vacancyRemissionRepository.findOne(id);
    }

    public List<VacancyRemission> getAllVacancyRemissionByUpicNo(final String upicNo) {
        return vacancyRemissionRepository.getAllVacancyRemissionByUpicNo(upicNo);
    }

    public VacancyRemission getRejectedVacancyRemissionForProperty(final String upicNo) {
        return vacancyRemissionRepository.findRejectedByUpicNo(upicNo);
    }

    @Transactional
    public VacancyRemission saveVacancyRemission(final VacancyRemission vacancyRemission, Long approvalPosition,
            final String approvalComent, final String additionalRule, final String workFlowAction,
            final Boolean propertyByEmployee) {
        if (LOG.isDebugEnabled())
            LOG.debug(" Create WorkFlow Transition Started  ...");
        final User user = securityUtils.getCurrentUser();
        final DateTime currentDate = new DateTime();
        final Assignment userAssignment = assignmentService.getPrimaryAssignmentForUser(user.getId());
        Position pos = null;
        Assignment wfInitiator = null;
        String currentState = "";

        if (!propertyByEmployee) {
            currentState = "Created";
            final Assignment assignment = propertyService.getUserPositionByZone(vacancyRemission.getBasicProperty(),
            		vacancyRemission.getBasicProperty().getSource());
            if (null != assignment)
                approvalPosition = assignment.getPosition().getId();
        } else
            currentState = null;

        if (vacancyRemission.getId() != null
                && (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_REJECT) || workFlowAction
                        .equalsIgnoreCase(WFLOW_ACTION_STEP_NOTICE_GENERATE)))
            wfInitiator = getWorkflowInitiator(vacancyRemission);

        if (StringUtils.isBlank(vacancyRemission.getApplicationNumber()))
            vacancyRemission.setApplicationNumber(applicationNumberGenerator.generate());
        if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_NOTICE_GENERATE)) {
            if (wfInitiator.equals(userAssignment)) {
                vacancyRemission.setStatus(VR_STATUS_REJECTION_ACK_GENERATED);
                vacancyRemission.transition(true).end().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withDateInfo(currentDate.toDate());
                vacancyRemission.getBasicProperty().setUnderWorkflow(false);
            }
        } else if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_REJECT)) {
            final String stateValue = WF_STATE_REJECTED;
            vacancyRemission.setStatus(VR_STATUS_REJECTED);
            vacancyRemission.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(approvalComent).withStateValue(stateValue).withDateInfo(currentDate.toDate())
                    .withOwner(wfInitiator.getPosition()).withNextAction("Application Rejected");
            buildSMS(vacancyRemission, workFlowAction);
        } else {
            if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_FORWARD))
                vacancyRemission.setStatus(VR_STATUS_WORKFLOW);
            else if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_APPROVE))
                vacancyRemission.setStatus(VR_STATUS_APPROVED);

            if (null != approvalPosition && approvalPosition != -1 && !approvalPosition.equals(Long.valueOf(0)))
                pos = positionMasterService.getPositionById(approvalPosition);
            WorkFlowMatrix wfmatrix = null;
            if (null == vacancyRemission.getState()) {
                wfmatrix = vacancyRemissionWorkflowService.getWfMatrix(vacancyRemission.getStateType(), null, null,
                        additionalRule, currentState, null);
                vacancyRemission.transition().start().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState()).withDateInfo(new Date())
                        .withOwner(pos).withNextAction(wfmatrix.getNextAction()).withNatureOfTask(NATURE_VACANCY_REMISSION);
                vacancyRemission.getBasicProperty().setUnderWorkflow(true);
                //to be enabled once acknowledgement feature is developed
                //buildSMS(vacancyRemission, workFlowAction);
            } else {
                wfmatrix = vacancyRemissionWorkflowService.getWfMatrix(vacancyRemission.getStateType(), null, null,
                        additionalRule, vacancyRemission.getCurrentState().getValue(), null);

                if (wfmatrix != null)
                    if (wfmatrix.getNextAction().equalsIgnoreCase("END")){
                        vacancyRemission.transition(true).end()
                                .withSenderName(user.getUsername() + "::" + user.getName())
                                .withComments(approvalComent).withDateInfo(currentDate.toDate());
                        vacancyRemission.getBasicProperty().setUnderWorkflow(false);
                    }
                    else
                        vacancyRemission.transition(false).withSenderName(user.getUsername() + "::" + user.getName())
                                .withComments(approvalComent).withStateValue(wfmatrix.getNextState())
                                .withDateInfo(currentDate.toDate()).withOwner(pos)
                                .withNextAction(wfmatrix.getNextAction());
	                if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_APPROVE)){
	            		buildSMS(vacancyRemission, workFlowAction);
	                }
            }
        }
        if (LOG.isDebugEnabled())
            LOG.debug(" WorkFlow Transition Completed  ...");
        return vacancyRemissionRepository.save(vacancyRemission);
    }

    /**
     * Provides 50% rebate on the next installment taxes
     * @param vacancyRemission
     * @param demandInstallment
     * @param effectiveInstallment
     */
    private void updateDemandDetailsWithRebate(VacancyRemission vacancyRemission, Installment demandInstallment, Installment effectiveInstallment){
    	Set<Ptdemand> activePropPtDemandSet = vacancyRemission.getBasicProperty().getActiveProperty().getPtDemandSet();
    	Ptdemand currPtDemand = null;
    	for(Ptdemand ptDemand : activePropPtDemandSet){
    		if (ptDemand.getIsHistory().equalsIgnoreCase("N"))
                if (ptDemand.getEgInstallmentMaster().equals(demandInstallment)) {
                	currPtDemand = ptDemand;
                    break;
                }
    	}
    	for(EgDemandDetails dmdDet : currPtDemand.getEgDemandDetails()){
    		if(dmdDet.getInstallmentStartDate().equals(effectiveInstallment.getFromDate())){
    			dmdDet.setAmount((dmdDet.getAmount().divide(new BigDecimal("2"))).setScale(0,
                        BigDecimal.ROUND_HALF_UP));
    		}
    	}
    	ptDemandDAO.update(currPtDemand);
    }
    
    public void addModelAttributes(final Model model, final BasicProperty basicProperty) {
        final Property property = basicProperty.getActiveProperty();
        model.addAttribute("property", property);
        final Ptdemand ptDemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(property);
        if (ptDemand != null && ptDemand.getDmdCalculations() != null && ptDemand.getDmdCalculations().getAlv() != null)
            model.addAttribute("ARV", ptDemand.getDmdCalculations().getAlv());
        else
            model.addAttribute("ARV", BigDecimal.ZERO);
        if (!basicProperty.getActiveProperty().getIsExemptedFromTax()) {
        	try {
        		//Based on the current installment, fetch tax details for the respective installment
				Map<String, Map<String,BigDecimal>> demandCollMap = propertyTaxUtil.prepareDemandDetForView(property,
				        propertyTaxCommonUtils.getCurrentInstallment());
				Map<String, BigDecimal> currentTaxDetails = propertyService.getCurrentTaxDetails(demandCollMap, new Date());
				model.addAttribute("propertyTax", currentTaxDetails.get(DEMANDRSN_STR_GENERAL_TAX));
	            model.addAttribute("eduCess", (currentTaxDetails.get(DEMANDRSN_STR_EDUCATIONAL_CESS) == null ? BigDecimal.ZERO : currentTaxDetails.get(DEMANDRSN_STR_EDUCATIONAL_CESS)));
	            model.addAttribute("libraryCess", (currentTaxDetails.get(DEMANDRSN_STR_LIBRARY_CESS) == null ? BigDecimal.ZERO : currentTaxDetails.get(DEMANDRSN_STR_LIBRARY_CESS)));
	            model.addAttribute("currTax", currentTaxDetails.get(CURR_DMD_STR));
				model.addAttribute("currTaxDue", currentTaxDetails.get(CURR_BAL_STR));
	            model.addAttribute("totalTax", currentTaxDetails.get(CURR_DMD_STR));
	            model.addAttribute("totalArrDue", currentTaxDetails.get(ARR_BAL_STR));
			} catch (Exception e) {
				LOG.error("Exception in addModelAttributes : ", e);
	            throw new ApplicationRuntimeException("Exception in addModelAttributes : " + e);
			}
            Boolean propertyByEmployee = Boolean.TRUE;
            propertyByEmployee = checkIfEmployee(getLoggedInUser());
            model.addAttribute("propertyByEmployee", propertyByEmployee);
        }
    }

    public Boolean checkIfEmployee(final User user) {
        return propertyService.isEmployee(user);
    }

    public String getInitiatorName(final VacancyRemission vacancyRemission) {
        String initiatorName = "";
        Assignment assignment = new Assignment();
        if (checkIfEmployee(vacancyRemission.getCreatedBy()))
            assignment = assignmentService.getPrimaryAssignmentForUser(vacancyRemission.getCreatedBy().getId());
        else
            assignment = assignmentService
                    .getPrimaryAssignmentForPositon(
                            vacancyRemission.getStateHistory().get(0).getOwnerPosition().getId());
        initiatorName=assignment.getEmployee().getName().concat("~").concat(assignment.getPosition().getName());
        return initiatorName;
    }

    protected Assignment getWorkflowInitiator(final VacancyRemission vacancyRemission) {
        Assignment wfInitiator;
        if (checkIfEmployee(vacancyRemission.getCreatedBy()))
            wfInitiator = assignmentService.getPrimaryAssignmentForUser(vacancyRemission.getCreatedBy().getId());
        else if (!vacancyRemission.getStateHistory().isEmpty())
            wfInitiator = assignmentService.getPrimaryAssignmentForPositon(vacancyRemission.getStateHistory().get(0)
                    .getOwnerPosition().getId());
        else
            wfInitiator = assignmentService.getPrimaryAssignmentForPositon(vacancyRemission.getState()
                    .getOwnerPosition().getId());
        return wfInitiator;
    }

    @Transactional
    public void saveRemissionDetails(final VacancyRemission vacancyRemission) {
        vacancyRemissionRepository.save(vacancyRemission);
    }

    public List<VacancyRemissionDetails> getMonthlyDetailsHistory(final VacancyRemission vacancyRemission) {
        List<VacancyRemissionDetails> historyList = new ArrayList<VacancyRemissionDetails>();
        if (!vacancyRemission.getVacancyRemissionDetails().isEmpty()) {
            historyList = vacancyRemission.getVacancyRemissionDetails();
            Collections.reverse(historyList);
        }
        return historyList;
    }

    public VacancyRemissionApproval getVacancyRemissionApprovalById(final Long id) {
        return vacancyRemissionApprovalRepository.findOne(id);
    }

    @Transactional
    public void saveVacancyRemissionApproval(final VacancyRemissionApproval vacancyRemissionApproval,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
            final String workFlowAction) {

        if (LOG.isDebugEnabled())
            LOG.debug(" Create WorkFlow Transition Started  ...");
        final User user = securityUtils.getCurrentUser();
        final DateTime currentDate = new DateTime();
        final Assignment userAssignment = assignmentService.getPrimaryAssignmentForUser(user.getId());
        final String designationList = propertyTaxCommonUtils.getAllDesignationsForUser(user.getId());
        Position pos = null;
        Assignment wfInitiator = null;

        if (vacancyRemissionApproval.getId() != null
                && (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_REJECT) || workFlowAction
                        .equalsIgnoreCase(WFLOW_ACTION_STEP_NOTICE_GENERATE)))
            if (StringUtils.containsIgnoreCase(designationList, REVENUE_INSPECTOR_DESGN))
                wfInitiator = assignmentService.getPrimaryAssignmentForUser(vacancyRemissionApproval
                        .getVacancyRemission().getCreatedBy().getId());
            else
                wfInitiator = assignmentService.getPrimaryAssignmentForUser(vacancyRemissionApproval.getCreatedBy()
                        .getId());

        if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_NOTICE_GENERATE)) {
            if (wfInitiator.equals(userAssignment)) {
                vacancyRemissionApproval.setStatus(VR_STATUS_REJECTION_ACK_GENERATED);
                vacancyRemissionApproval.transition().end().withSenderName(user.getName()).withComments(approvalComent)
                        .withDateInfo(currentDate.toDate());
            }
        } else if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_REJECT)) {
            String stateValue = "";
            String nextAction = "";
            if (StringUtils.containsIgnoreCase(designationList, REVENUE_OFFICER_DESGN)
                    || StringUtils.containsIgnoreCase(designationList, COMMISSIONER_DESGN)) {
                stateValue = WF_STATE_REJECTED;
                nextAction = WF_STATE_REVENUE_INSPECTOR_APPROVAL_PENDING;
            } else {
                stateValue = WF_STATE_REVENUE_INSPECTOR_REJECTED;
                nextAction = WF_STATE_ASSISTANT_APPROVAL_PENDING;
            }
            vacancyRemissionApproval.setStatus(VR_STATUS_REJECTED);
            vacancyRemissionApproval.transition().withSenderName(user.getName()).withComments(approvalComent)
                    .withStateValue(stateValue).withDateInfo(currentDate.toDate()).withOwner(wfInitiator.getPosition())
                    .withNextAction(nextAction);
        } else {
            if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_FORWARD))
                vacancyRemissionApproval.setStatus(VR_STATUS_WORKFLOW);
            else if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_APPROVE))
                vacancyRemissionApproval.setStatus(VR_STATUS_APPROVED);

            if (null != approvalPosition && approvalPosition != -1 && !approvalPosition.equals(Long.valueOf(0)))
                pos = positionMasterService.getPositionById(approvalPosition);
            else if (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workFlowAction))
                pos = assignmentService.getPrimaryAssignmentForUser(
                        vacancyRemissionApproval.getVacancyRemission().getCreatedBy().getId()).getPosition();
            WorkFlowMatrix wfmatrix = null;
            if (null == vacancyRemissionApproval.getState()) {
                wfmatrix = vacancyRemissionWorkflowService.getWfMatrix(vacancyRemissionApproval.getStateType(), null,
                        null, additionalRule, null, null);
                vacancyRemissionApproval.transition().start().withSenderName(user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState()).withDateInfo(new Date())
                        .withOwner(pos).withNextAction(wfmatrix.getNextAction()).withNatureOfTask(NATURE_VACANCY_REMISSION);
            } else {
                wfmatrix = vacancyRemissionWorkflowService.getWfMatrix(vacancyRemissionApproval.getStateType(), null,
                        null, additionalRule, vacancyRemissionApproval.getCurrentState().getValue(), null);
                if (wfmatrix != null){
                    if (wfmatrix.getNextAction().equalsIgnoreCase("END"))
                        vacancyRemissionApproval.transition().end().withSenderName(user.getName())
                                .withComments(approvalComent).withDateInfo(currentDate.toDate());
                    else
                        vacancyRemissionApproval.transition(false).withSenderName(user.getName())
                                .withComments(approvalComent).withStateValue(wfmatrix.getNextState())
                                .withDateInfo(currentDate.toDate()).withOwner(pos)
                                .withNextAction(wfmatrix.getNextAction());
	                if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_APPROVE)){
	                	Map<String, Installment> installmentMap = propertyTaxUtil.getInstallmentsForCurrYear(new Date());
	                    Installment installmentFirstHalf = installmentMap.get(PropertyTaxConstants.CURRENTYEAR_FIRST_HALF);
	                    Installment installmentSecondHalf = installmentMap.get(PropertyTaxConstants.CURRENTYEAR_SECOND_HALF);
	                    /*
	                     * If VR is done in 1st half, provide 50% rebate on taxes of the 2nd half
	                     */
	                    if(DateUtils.between(vacancyRemissionApproval.getVacancyRemission().getVacancyToDate(), installmentFirstHalf.getFromDate(), installmentFirstHalf.getToDate()))
	                    	updateDemandDetailsWithRebate(vacancyRemissionApproval.getVacancyRemission(), installmentFirstHalf, installmentSecondHalf);
	                }
                }
            }
        }
        if (LOG.isDebugEnabled())
            LOG.debug(" WorkFlow Transition Completed  ...");
        vacancyRemissionApprovalRepository.save(vacancyRemissionApproval);
    }

    public User getLoggedInUser() {
        return securityUtils.getCurrentUser();
    }

    public VacancyRemission saveVacancyRemission(final VacancyRemission vacancyRemission, final Long approvalPosition,
            final String approvalComent, final String additionalRule, final String workFlowAction,
            final Boolean propertyByEmployee, final HashMap<String, String> meesevaParams) {
        return saveVacancyRemission(vacancyRemission, approvalPosition, approvalComent, additionalRule, workFlowAction,
                propertyByEmployee);

    }
    
    public void buildSMS(VacancyRemission vacancyRemission, String workFlowAction) {
        for (PropertyOwnerInfo ownerInfo : vacancyRemission.getBasicProperty().getPropertyOwnerInfo()) {
            if (StringUtils.isNotBlank(ownerInfo.getOwner().getMobileNumber())) {
                buildSms(vacancyRemission, ownerInfo.getOwner(), workFlowAction);
            }
        }
    }

    private void buildSms(VacancyRemission vacancyRemission, User user, String workFlowAction) {
        final String assessmentNo = vacancyRemission.getBasicProperty().getUpicNo();
        final String mobileNumber = user.getMobileNumber();
        final String applicantName = user.getName();
        String smsMsg = "";
        if (workFlowAction.equals(WFLOW_ACTION_STEP_FORWARD)) {
        	//to be enabled once acknowledgement feature is developed
            /*smsMsg = messageSource.getMessage("vacancyremission.ack.sms",
                    new String[] { applicantName, assessmentNo }, null);*/
        } else if (workFlowAction.equals(WFLOW_ACTION_STEP_REJECT)) {
            smsMsg = ptisMessageSource.getMessage("vacancyremission.rejection.sms", new String[] { applicantName, assessmentNo,
                    ApplicationThreadLocals.getMunicipalityName() }, null);
        } else if (workFlowAction.equals(WFLOW_ACTION_STEP_APPROVE)) {
            smsMsg = ptisMessageSource.getMessage("vacancyremission.approval.sms", new String[] { applicantName, assessmentNo,
                    ApplicationThreadLocals.getMunicipalityName() },null);
        }

        if (StringUtils.isNotBlank(mobileNumber)) {
            messagingService.sendSMS(mobileNumber, smsMsg);
        }

    }
}
