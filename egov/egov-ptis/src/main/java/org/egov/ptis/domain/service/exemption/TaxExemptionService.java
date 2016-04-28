package org.egov.ptis.domain.service.exemption;

import static java.lang.Boolean.FALSE;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_EDUCATIONAL_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_GENERAL_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_LIBRARY_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_UNAUTHORIZED_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_VACANT_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.EXEMPTION;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_TAX_EXEMPTION;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_CANCELLED;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_APPROVE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_ASSISTANT_APPROVAL_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_REJECTED;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_FORWARD;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.egov.commons.Installment;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.messaging.MessagingService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.ApplicationNumberGenerator;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.pims.commons.Position;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.PropertyTypeMasterDAO;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Floor;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.TaxExeptionReason;
import org.egov.ptis.domain.service.property.PropertyPersistenceService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.elasticsearch.common.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class TaxExemptionService extends PersistenceService<PropertyImpl, Long> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaxExemptionService.class);

    @Autowired
    private PropertyTypeMasterDAO propertyTypeMasterDAO;

    @Autowired
    private PropertyService propService;

    @Autowired
    private PropertyPersistenceService propertyPerService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private SimpleWorkflowService<PropertyImpl> propertyWorkflowService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

    PropertyImpl propertyModel = new PropertyImpl();

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationNumberGenerator applicationNumberGenerator;

    @Autowired
    private PtDemandDao ptDemandDAO;

    @Autowired
    private ResourceBundleMessageSource messageSource;

    @Autowired
    private MessagingService messagingService;

    @Transactional
    public BasicProperty saveProperty(final Property newProperty, final Property oldProperty, final Character status,
            final String approvalComment, final String workFlowAction, final Long approvalPosition,
            final String taxExemptedReason, final Boolean propertyByEmployee, final String additionalRule) {

        Date propCompletionDate = null;
        final BasicProperty basicProperty = oldProperty.getBasicProperty();
        final PropertyDetail propertyDetail = oldProperty.getPropertyDetail();
        propCompletionDate = propertyDetail.getDateOfCompletion();
        propertyModel = (PropertyImpl) newProperty;
        propertyModel.setStatus(status);
        if (propertyModel.getApplicationNo() == null)
            propertyModel.setApplicationNo(applicationNumberGenerator.generate());

        if (!propertyModel.getPropertyDetail().getPropertyTypeMaster().getCode()
                .equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            propCompletionDate = propService.getLowestDtOfCompFloorWise(propertyDetail.getFloorDetails());
        else
            propCompletionDate = propertyDetail.getDateOfCompletion();
        for (final Floor floor : propertyModel.getPropertyDetail().getFloorDetails()) {
            propertyPerService.applyAuditing(floor);
            floor.setPropertyDetail(propertyModel.getPropertyDetail());
        }
        if (StringUtils.isNotBlank(taxExemptedReason) && !taxExemptedReason.equals("-1")) {
            final TaxExeptionReason taxExemptionReason = (TaxExeptionReason) propertyPerService.find(
                    "From TaxExeptionReason where id = ?", Long.valueOf(taxExemptedReason));
            propertyModel.setTaxExemptedReason(taxExemptionReason);
            propertyModel.setIsExemptedFromTax(Boolean.TRUE);
        } else {
            propertyModel.setTaxExemptedReason(null);
            propertyModel.setIsExemptedFromTax(Boolean.FALSE);
        }

        basicProperty.setUnderWorkflow(Boolean.TRUE);
        propertyModel.setEffectiveDate(propCompletionDate);
        for (Ptdemand ptdemand : propertyModel.getPtDemandSet()) {
            propertyPerService.applyAuditing(ptdemand.getDmdCalculations());
        }
        propertyModel.setBasicProperty(basicProperty);
        basicProperty.addProperty(propertyModel);
        transitionWorkFlow(propertyModel, approvalComment, workFlowAction, approvalPosition, additionalRule,
                propertyByEmployee);
        return propertyPerService.update(basicProperty);

    }

    @Transactional
    public void updateProperty(final Property newProperty, final String comments, final String workFlowAction,
            final Long approverPosition, final Boolean propertyByEmployee, final String additionalRule) {
        transitionWorkFlow((PropertyImpl) newProperty, comments, workFlowAction, approverPosition, additionalRule,
                propertyByEmployee);
        propertyPerService.update(newProperty.getBasicProperty());
    }

    private void transitionWorkFlow(final PropertyImpl property, final String approvarComments,
            final String workFlowAction, Long approverPosition, final String additionalRule,
            final Boolean propertyByEmployee) {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("WorkFlow Transition For Demolition Started  ...");
        final User user = securityUtils.getCurrentUser();
        final DateTime currentDate = new DateTime();
        final Assignment userAssignment = assignmentService.getPrimaryAssignmentForUser(user.getId());
        Position pos = null;
        String currentState = "";
        Assignment wfInitiator = null;

        if (!propertyByEmployee) {
            currentState = "Created";
            final Assignment assignment = propService.getUserPositionByZone(property.getBasicProperty());
            if (null != assignment)
                approverPosition = assignment.getPosition().getId();
        } else
            currentState = null;
        if (null != property.getId()
                && (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_REJECT) || workFlowAction
                        .equalsIgnoreCase(WFLOW_ACTION_STEP_APPROVE)))
            wfInitiator = propService.getWorkflowInitiator(property);

        if (WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction)) {
            if (wfInitiator.equals(userAssignment)) {
                property.transition(true).end().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvarComments).withDateInfo(currentDate.toDate());
                property.setStatus(STATUS_CANCELLED);
                property.getBasicProperty().setUnderWorkflow(FALSE);
            } else {
                final String stateValue = property.getCurrentState().getValue().split(":")[0] + ":" + WF_STATE_REJECTED;
                property.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvarComments).withStateValue(stateValue).withDateInfo(currentDate.toDate())
                        .withOwner(wfInitiator.getPosition()).withNextAction(WF_STATE_ASSISTANT_APPROVAL_PENDING);
                buildSMS(property, workFlowAction);
            }

        } else {
            if (null != approverPosition && approverPosition != -1 && !approverPosition.equals(Long.valueOf(0)))
                pos = positionMasterService.getPositionById(approverPosition);
            else if (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workFlowAction))
                pos = wfInitiator.getPosition();
            WorkFlowMatrix wfmatrix = null;
            if (null == property.getState()) {
                wfmatrix = propertyWorkflowService.getWfMatrix(property.getStateType(), null, null, additionalRule,
                        currentState, null);
                property.transition().start().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvarComments).withStateValue(wfmatrix.getNextState())
                        .withDateInfo(new Date()).withOwner(pos).withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(NATURE_TAX_EXEMPTION);
                //to be enabled once acknowledgement feature is developed
                //buildSMS(property, workFlowAction);
            } else {
                wfmatrix = propertyWorkflowService.getWfMatrix(property.getStateType(), null, null, additionalRule,
                        property.getCurrentState().getValue(), null);

                if (wfmatrix != null)
                    if (wfmatrix.getNextAction().equalsIgnoreCase("END"))
                        property.transition().end().withSenderName(user.getUsername() + "::" + user.getName())
                                .withComments(approvarComments).withDateInfo(currentDate.toDate());
                    else
                        property.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                                .withComments(approvarComments).withStateValue(wfmatrix.getNextState())
                                .withDateInfo(currentDate.toDate()).withOwner(pos)
                                .withNextAction(wfmatrix.getNextAction());
                
                	if(workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_APPROVE))
                		buildSMS(property, workFlowAction);
            }
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(" WorkFlow Transition Completed for Demolition ...");
    }

        Property property = null;
        public void addModelAttributes(final Model model, final BasicProperty basicProperty) {
        if (null != basicProperty.getWFProperty())
            property = basicProperty.getWFProperty();
        else
            property = basicProperty.getActiveProperty();
        final Ptdemand ptDemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(property);
        if (ptDemand != null && ptDemand.getDmdCalculations() != null && ptDemand.getDmdCalculations().getAlv() != null)
            model.addAttribute("ARV", ptDemand.getDmdCalculations().getAlv());
        else
            model.addAttribute("ARV", BigDecimal.ZERO);
        model.addAttribute("propertyByEmployee", propService.isEmployee(securityUtils.getCurrentUser()));
        if (!property.getIsExemptedFromTax()) {
            final Map<String, BigDecimal> demandCollMap = null;
            model.addAttribute("currTax", demandCollMap.get(CURR_DMD_STR));
            model.addAttribute("eduCess", demandCollMap.get(DEMANDRSN_STR_EDUCATIONAL_CESS));
            model.addAttribute("currTaxDue", demandCollMap.get(CURR_DMD_STR).subtract(demandCollMap.get(CURR_COLL_STR)));
            model.addAttribute("libraryCess", demandCollMap.get(DEMANDRSN_STR_LIBRARY_CESS));
            model.addAttribute("totalArrDue", demandCollMap.get(ARR_DMD_STR).subtract(demandCollMap.get(ARR_COLL_STR)));
            BigDecimal propertyTax = BigDecimal.ZERO;
            if (null != demandCollMap.get(DEMANDRSN_STR_GENERAL_TAX))
                propertyTax = demandCollMap.get(DEMANDRSN_STR_GENERAL_TAX);
            else
                propertyTax = demandCollMap.get(DEMANDRSN_STR_VACANT_TAX);
            final BigDecimal totalTax = propertyTax
                    .add(demandCollMap.get(DEMANDRSN_STR_LIBRARY_CESS) == null ? BigDecimal.ZERO : demandCollMap.get(DEMANDRSN_STR_LIBRARY_CESS))
                    .add(demandCollMap.get(DEMANDRSN_STR_EDUCATIONAL_CESS) == null ? BigDecimal.ZERO : demandCollMap.get(DEMANDRSN_STR_EDUCATIONAL_CESS));
            model.addAttribute("propertyTax", propertyTax);
            if (demandCollMap.get(DEMANDRSN_STR_UNAUTHORIZED_PENALTY)!=null) {
                model.addAttribute("unauthorisedPenalty", demandCollMap.get(DEMANDRSN_STR_UNAUTHORIZED_PENALTY));
                model.addAttribute("totalTax", totalTax.add(demandCollMap.get(DEMANDRSN_STR_UNAUTHORIZED_PENALTY)));
                model.addAttribute("showUnauthorisedPenalty", "yes");
            } else {
                model.addAttribute("totalTax", totalTax);
                model.addAttribute("showUnauthorisedPenalty", "no");
            }
        }
    }

    public Boolean isPropertyByEmployee(final Property property) {
        return propService.isEmployee(property.getCreatedBy());
    }

    public BasicProperty saveProperty(final Property newProperty, final Property oldProperty, final Character status,
            final String approvalComment, final String workFlowAction, final Long approvalPosition,
            final String taxExemptedReason, final Boolean propertyByEmployee, final String additionalRule,
            final HashMap<String, String> meesevaParams) {
        return saveProperty(newProperty, oldProperty, status, approvalComment, workFlowAction, approvalPosition,
                taxExemptedReason, propertyByEmployee, EXEMPTION);

    }

    public void buildSMS(Property property, String workFlowAction) {
        final User user = property.getBasicProperty().getPrimaryOwner();
        final String assessmentNo = property.getBasicProperty().getUpicNo();
        final String mobileNumber = user.getMobileNumber();
        final String applicantName = user.getName();
        String smsMsg = "";
        if (workFlowAction.equals(WFLOW_ACTION_STEP_FORWARD)) {
        	//to be enabled once acknowledgement feature is developed
        	/*smsMsg = messageSource.getMessage("msg.initiateexemption.sms",
                    new String[] { applicantName, assessmentNo }, null);*/
        } else if (workFlowAction.equals(WFLOW_ACTION_STEP_REJECT)) {
            smsMsg = messageSource.getMessage("msg.rejectexemption.sms", new String[] { applicantName, assessmentNo,
                    EgovThreadLocals.getMunicipalityName() }, null);
        } else if (workFlowAction.equals(WFLOW_ACTION_STEP_APPROVE)) {
            Installment installment = propertyTaxUtil.getInstallmentListByStartDate(new Date()).get(0);
            Date effectiveDate = DateUtils.addDays(installment.getToDate(), 1);
            smsMsg = messageSource.getMessage("msg.approveexemption.sms", new String[] { applicantName, assessmentNo,
                    new SimpleDateFormat("dd/MM/yyyy").format(effectiveDate), EgovThreadLocals.getMunicipalityName() },
                    null);
        }

        if (StringUtils.isNotBlank(mobileNumber)) {
            messagingService.sendSMS(mobileNumber, smsMsg);
        }

    }

}