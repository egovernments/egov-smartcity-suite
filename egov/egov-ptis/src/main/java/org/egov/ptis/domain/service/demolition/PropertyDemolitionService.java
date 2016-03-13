package org.egov.ptis.domain.service.demolition;

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
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_DEMOLITION;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_FULL_DEMOLITION;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_CANCELLED;
import static org.egov.ptis.constants.PropertyTaxConstants.VACANTLAND_PROPERTY_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_APPROVE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_FORWARD;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_SIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_ASSISTANT_APPROVAL_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_REJECTED;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.messaging.MessagingService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.workflow.WorkFlowMatrix;
import org.egov.pims.commons.Position;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.PropertyTypeMasterDAO;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.VacantProperty;
import org.egov.ptis.domain.service.property.PropertyPersistenceService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.exceptions.TaxCalculatorExeption;
import org.hibernate.FlushMode;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public class PropertyDemolitionService extends PersistenceService<PropertyImpl, Long> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyDemolitionService.class);

    @Autowired
    private PropertyTypeMasterDAO propertyTypeMasterDAO;

    @Autowired
    private PropertyService propService;

    @Autowired
    private PropertyPersistenceService propertyPerService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private SimpleWorkflowService<PropertyImpl> propertyWorkflowService;

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

    @Autowired
    private PtDemandDao ptDemandDAO;
    
    @Autowired
    private ResourceBundleMessageSource messageSource;

    @Autowired
    private MessagingService messagingService;
    
    @Autowired
    private InstallmentDao installmentDao;
    
    @Autowired
    private ModuleService moduleDao;

    @Transactional
    public void saveProperty(Property oldProperty, Property newProperty, Character status, String comments,
            String workFlowAction, Long approverPosition, String additionalRule) throws TaxCalculatorExeption {
        Date propCompletionDate = null;
        PropertyImpl propertyModel;
        BasicProperty basicProperty = oldProperty.getBasicProperty();
        PropertyTypeMaster propTypeMstr = propertyTypeMasterDAO.getPropertyTypeMasterByCode(OWNERSHIP_TYPE_VAC_LAND);
        propertyModel = (PropertyImpl) newProperty;
        newProperty.getPropertyDetail().setPropertyTypeMaster(propTypeMstr);
        newProperty.getBasicProperty().setPropOccupationDate(newProperty.getPropertyDetail().getDateOfCompletion());
        propCompletionDate = newProperty.getPropertyDetail().getDateOfCompletion();
        String areaOfPlot = String.valueOf(propertyModel.getPropertyDetail().getSitalArea().getArea());
        propertyModel = propService.createProperty(propertyModel, areaOfPlot, PROPERTY_MODIFY_REASON_FULL_DEMOLITION,
                propertyModel.getPropertyDetail().getPropertyTypeMaster().getId().toString(), null, null, status, null,
                null, null, null, null, null, null);
        propertyModel.setBasicProperty(basicProperty);
        propertyModel.setEffectiveDate(propCompletionDate);
        if (!propertyModel.getPropertyDetail().getPropertyTypeMaster().getCode().equals(OWNERSHIP_TYPE_VAC_LAND)) {
            propService.changePropertyDetail(propertyModel, new VacantProperty(), 0);
        }
        propertyModel.getPropertyDetail().setCategoryType(VACANTLAND_PROPERTY_CATEGORY);
        basicProperty.setUnderWorkflow(Boolean.TRUE);
        propertyModel.setBasicProperty(basicProperty);
        basicProperty.addProperty(propertyModel);
        getSession().setFlushMode(FlushMode.MANUAL);
        transitionWorkFlow(propertyModel, comments, workFlowAction, approverPosition, additionalRule);
        Installment currInstall = PropertyTaxUtil.getCurrentInstallment();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currInstall.getToDate());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Property modProperty = propService.createDemand(propertyModel, calendar.getTime());
        // Property modProperty = propService.modifyDemand(propertyModel,
        // (PropertyImpl) oldProperty);
        Ptdemand currPtDmd = null;
        for (final Ptdemand demand : oldProperty.getPtDemandSet())
            if (demand.getIsHistory().equalsIgnoreCase("N"))
                if (demand.getEgInstallmentMaster().equals(currInstall)) {
                    currPtDmd = demand;
                    break;
                }
        Ptdemand clonedDemand = (Ptdemand) currPtDmd.clone();
        clonedDemand.setEgptProperty((PropertyImpl) modProperty);
        modProperty.addPtDemand(clonedDemand);
        basicProperty.addProperty(modProperty);
        for (Ptdemand ptDemand : modProperty.getPtDemandSet()) {
            propertyPerService.applyAuditing(ptDemand.getDmdCalculations());
        }
        propertyPerService.update(basicProperty);
        getSession().flush();
    }

    public void updateProperty(Property newProperty, String comments, String workFlowAction, Long approverPosition,
            String additionalRule) {
        transitionWorkFlow((PropertyImpl) newProperty, comments, workFlowAction, approverPosition, additionalRule);
        propertyPerService.update(newProperty.getBasicProperty());
        getSession().flush();
    }

    private void transitionWorkFlow(PropertyImpl property, String approvarComments, String workFlowAction,
            Long approverPosition, String additionalRule) {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("WorkFlow Transition For Demolition Started  ...");
        final User user = securityUtils.getCurrentUser();
        final DateTime currentDate = new DateTime();
        final Assignment userAssignment = assignmentService.getPrimaryAssignmentForUser(user.getId());
        Position pos = null;

        if (WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction)) {
        	Assignment wfInitiator = propService.getWorkflowInitiator(property);
        	if (wfInitiator.equals(userAssignment)) {
        		property.transition(true).end().withSenderName(user.getUsername() + "::" + user.getName())
                .withComments(approvarComments).withDateInfo(currentDate.toDate());
        		property.setStatus(STATUS_CANCELLED);
        		property.getBasicProperty().setUnderWorkflow(FALSE);
        	}else{
        		final String stateValue = property.getCurrentState().getValue().split(":")[0] + ":" + WF_STATE_REJECTED;
                property.transition(true)
                        .withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvarComments)
                        .withStateValue(stateValue)
                        .withDateInfo(currentDate.toDate())
                        .withOwner(
                                assignmentService.getPrimaryAssignmentForUser(property.getCreatedBy().getId())
                                        .getPosition()).withNextAction(WF_STATE_ASSISTANT_APPROVAL_PENDING);
                buildSMS(property, workFlowAction);
        	}
        } else {
            if (null != approverPosition && approverPosition != -1 && !approverPosition.equals(Long.valueOf(0)))
                pos = positionMasterService.getPositionById(approverPosition);
            else if (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workFlowAction))
                pos = positionMasterService.getPositionByUserId(securityUtils.getCurrentUser().getId());
            else if (WFLOW_ACTION_STEP_SIGN.equalsIgnoreCase(workFlowAction))
                pos = assignmentService.getPrimaryAssignmentForUser(property.getCreatedBy().getId()).getPosition();
            WorkFlowMatrix wfmatrix = null;
            if (null == property.getState()) {
                wfmatrix = propertyWorkflowService.getWfMatrix(property.getStateType(), null, null, additionalRule,
                        null, null);
                property.transition().start().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvarComments).withStateValue(wfmatrix.getNextState())
                        .withDateInfo(new Date()).withOwner(pos).withNextAction(wfmatrix.getNextAction()).withNatureOfTask(NATURE_DEMOLITION);
                //to be enabled once acknowledgement feature is developed
                //buildSMS(property, workFlowAction);
            } else {
                wfmatrix = propertyWorkflowService.getWfMatrix(property.getStateType(), null, null, additionalRule,
                        property.getCurrentState().getValue(), null);

                if (wfmatrix != null) {
                    if (wfmatrix.getNextAction().equalsIgnoreCase("END")) {
                        property.transition().end().withSenderName(user.getUsername() + "::" + user.getName())
                                .withComments(approvarComments).withDateInfo(currentDate.toDate());
                    } else {
                        property.transition(false).withSenderName(user.getUsername() + "::" + user.getName())
                                .withComments(approvarComments).withStateValue(wfmatrix.getNextState())
                                .withDateInfo(currentDate.toDate()).withOwner(pos)
                                .withNextAction(wfmatrix.getNextAction());
                    }
                    if(workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_APPROVE))
                		buildSMS(property, workFlowAction);
                }
            }
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(" WorkFlow Transition Completed for Demolition ...");
    }

    public void validateProperty(Property property, final BindingResult errors, final HttpServletRequest request) {
        PropertyDetail propertyDetail = property.getPropertyDetail();
        if (StringUtils.isBlank(propertyDetail.getPattaNumber()))
            errors.rejectValue("propertyDetail.pattaNumber", "pattaNumber.required");
        if (StringUtils.isBlank(propertyDetail.getSurveyNumber()))
            errors.rejectValue("propertyDetail.surveyNumber", "surveyNumber.required");
        if (null == propertyDetail.getSitalArea().getArea())
            errors.rejectValue("propertyDetail.sitalArea.area", "vacantLandArea.required");
        if (null == propertyDetail.getMarketValue())
            errors.rejectValue("propertyDetail.marketValue", "marketValue.required");
        if (null == propertyDetail.getCurrentCapitalValue())
            errors.rejectValue("propertyDetail.currentCapitalValue", "currCapitalValue.required");
        if (StringUtils.isBlank(property.getBasicProperty().getPropertyID().getNorthBoundary()))
            errors.rejectValue("basicProperty.propertyID.northBoundary", "northBoundary.required");
        if (StringUtils.isBlank(property.getBasicProperty().getPropertyID().getEastBoundary()))
            errors.rejectValue("basicProperty.propertyID.eastBoundary", "eastBoundary.required");
        if (StringUtils.isBlank(property.getBasicProperty().getPropertyID().getWestBoundary()))
            errors.rejectValue("basicProperty.propertyID.westBoundary", "westBoundary.required");
        if (StringUtils.isBlank(property.getBasicProperty().getPropertyID().getSouthBoundary()))
            errors.rejectValue("basicProperty.propertyID.southBoundary", "southBoundary.required");
        if (StringUtils.isBlank(property.getDemolitionReason()))
            errors.rejectValue("demolitionReason", "demolitionReason.required");

    }

    public void addModelAttributes(Model model, BasicProperty basicProperty) {
        Property property = null;
        if (null != basicProperty.getProperty())
            property = basicProperty.getProperty();
        else
            property = basicProperty.getActiveProperty();
        Ptdemand ptDemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(property);
        if (ptDemand != null && ptDemand.getDmdCalculations() != null && ptDemand.getDmdCalculations().getAlv() != null)
            model.addAttribute("ARV", ptDemand.getDmdCalculations().getAlv());
        else
            model.addAttribute("ARV", BigDecimal.ZERO);
        if (!basicProperty.getActiveProperty().getIsExemptedFromTax()) {
            final Map<String, BigDecimal> demandCollMap = propertyTaxUtil.prepareDemandDetForView(property,
                    PropertyTaxUtil.getCurrentInstallment());
            model.addAttribute("currTax", demandCollMap.get(CURR_DMD_STR));
            model.addAttribute("eduCess", (demandCollMap.get(DEMANDRSN_STR_EDUCATIONAL_CESS) == null ? BigDecimal.ZERO : demandCollMap.get(DEMANDRSN_STR_EDUCATIONAL_CESS)));
            model.addAttribute("currTaxDue", demandCollMap.get(CURR_DMD_STR).subtract(demandCollMap.get(CURR_COLL_STR)));
            model.addAttribute("libraryCess", (demandCollMap.get(DEMANDRSN_STR_LIBRARY_CESS) == null ? BigDecimal.ZERO : demandCollMap.get(DEMANDRSN_STR_LIBRARY_CESS)));
            model.addAttribute("totalArrDue", demandCollMap.get(ARR_DMD_STR).subtract(demandCollMap.get(ARR_COLL_STR)));
            BigDecimal propertyTax = BigDecimal.ZERO;
            if (null != demandCollMap.get(DEMANDRSN_STR_GENERAL_TAX))
                propertyTax = demandCollMap.get(DEMANDRSN_STR_GENERAL_TAX);
            else
                propertyTax = demandCollMap.get(DEMANDRSN_STR_VACANT_TAX);
            BigDecimal totalTax = propertyTax.add(
                    demandCollMap.get(DEMANDRSN_STR_LIBRARY_CESS) == null ? BigDecimal.ZERO : demandCollMap
                            .get(DEMANDRSN_STR_LIBRARY_CESS)).add(
                    demandCollMap.get(DEMANDRSN_STR_EDUCATIONAL_CESS) == null ? BigDecimal.ZERO : demandCollMap
                            .get(DEMANDRSN_STR_EDUCATIONAL_CESS));
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
    
    public void buildSMS(Property property, String workFlowAction) {
        final User user = property.getBasicProperty().getPrimaryOwner();
        final String assessmentNo = property.getBasicProperty().getUpicNo();
        final String mobileNumber = user.getMobileNumber();
        final String applicantName = user.getName();
        String smsMsg = "";
        if (workFlowAction.equals(WFLOW_ACTION_STEP_FORWARD)) {
        	//to be enabled once acknowledgement feature is developed
            /*smsMsg = messageSource.getMessage("demolition.ack.sms",
                    new String[] { applicantName, assessmentNo }, null);*/
        } else if (workFlowAction.equals(WFLOW_ACTION_STEP_REJECT)) {
            smsMsg = messageSource.getMessage("demolition.rejection.sms", new String[] { applicantName, assessmentNo,
                    EgovThreadLocals.getMunicipalityName() }, null);
        } else if (workFlowAction.equals(WFLOW_ACTION_STEP_APPROVE)) {
        	Installment installment = propertyTaxUtil.getInstallmentListByStartDate(new Date()).get(0);
            Date effectiveDate = DateUtils.addDays(installment.getToDate(), 1);
            Module module = moduleDao.getModuleByName(PTMODULENAME);
            Installment nextInstallment = installmentDao.getInsatllmentByModuleForGivenDate(module, effectiveDate);
            final Map<String, BigDecimal> demandCollMap = propertyTaxUtil.prepareDemandDetForView(property,nextInstallment);
            BigDecimal totalTax = demandCollMap.get(DEMANDRSN_STR_VACANT_TAX)
                    .add(demandCollMap.get(DEMANDRSN_STR_LIBRARY_CESS) == null ? BigDecimal.ZERO
                            : demandCollMap.get(DEMANDRSN_STR_LIBRARY_CESS));
            smsMsg = messageSource.getMessage("demolition.approval.sms", new String[] { applicantName, assessmentNo,
            		totalTax.toString(),new SimpleDateFormat("dd/MM/yyyy").format(effectiveDate),EgovThreadLocals.getMunicipalityName() },
                    null);
        }

        if (StringUtils.isNotBlank(mobileNumber)) {
            messagingService.sendSMS(mobileNumber, smsMsg);
        }

    }

}
