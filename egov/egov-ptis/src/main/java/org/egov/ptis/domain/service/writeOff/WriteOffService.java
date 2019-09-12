package org.egov.ptis.domain.service.writeOff;

import static java.lang.Boolean.FALSE;
import static org.egov.ptis.constants.PropertyTaxConstants.ACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_WRITE_OFF;
import static org.egov.ptis.constants.PropertyTaxConstants.COMMISSIONER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.COMMISSIONER_DESIGNATIONS;
import static org.egov.ptis.constants.PropertyTaxConstants.ENDORSEMENT_NOTICE;
import static org.egov.ptis.constants.PropertyTaxConstants.FULL_WRITEOFF;
import static org.egov.ptis.constants.PropertyTaxConstants.HISTORY_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.LOGGED_IN_USER;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_WRITE_OFF;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_OFFICER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.STATE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATE_AWARE_ID;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_CANCELLED;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISHISTORY;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_WORKFLOW;
import static org.egov.ptis.constants.PropertyTaxConstants.TRANSACTION_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_APPROVE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_FORWARD;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_COMMISSIONER_APPROVAL_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_DIGITAL_SIGNATURE_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_REJECTED;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_REVENUE_OFFICER_APPROVAL_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.WO_FORM;
import static org.egov.ptis.constants.PropertyTaxConstants.WRITEOFF_CODE;
import static org.egov.ptis.constants.PropertyTaxConstants.WRITE_OFF;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.T;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentHibDao;
import org.egov.demand.dao.DemandGenericDao;
import org.egov.demand.model.DemandDetailVariation;
import org.egov.demand.model.EgDemandDetails;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.ApplicationNumberGenerator;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Position;
import org.egov.ptis.bean.demand.DemandDetail;
import org.egov.ptis.client.bill.PTBillServiceImpl;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.PropertyMutationMasterHibDAO;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.enums.TransactionType;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.entity.property.WriteOff;
import org.egov.ptis.domain.entity.property.WriteOffReasons;
import org.egov.ptis.domain.repository.master.structureclassification.StructureClassificationRepository;
import org.egov.ptis.domain.repository.writeOff.WriteOffRepository;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.master.service.PropertyUsageService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Service
@Transactional(readOnly = true)
public class WriteOffService extends GenericWorkFlowController {

    @Autowired
    private WriteOffRepository writeOffRepo;
    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource ptisMessageSource;
    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private PositionMasterService positionMasterService;
    @Autowired
    private PropertyMutationMasterHibDAO propertyMutationMasterHibDAO;
    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<PropertyImpl> propertyWorkflowService;
    @Autowired
    private ApplicationNumberGenerator applicationNo;
    @Autowired
    private PropertyService propService;
    @Autowired
    private PtDemandDao ptDemandDAO;
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    private PersistenceService<T, Serializable> persistenceService;
    PropertyMutationMaster propertyMutationMaster;
    @Autowired
    StructureClassificationRepository structureDAO;
    @Autowired
    PropertyUsageService propertyUsageService;
    @Autowired
    private InstallmentHibDao<?, ?> installmentDao;
    @Autowired
    private ModuleService moduleDao;
    @Autowired
    private DemandGenericDao demandGenericDAO;
    @Autowired
    private PropertyTaxUtil propertyTaxUtil;
    @Autowired
    private PTBillServiceImpl ptBillServiceImpl;

    Property property = null;
    private static final Logger LOGGER = Logger.getLogger(WriteOffService.class);
    private static final String CURRENT_STATE = "currentState";
    private static final String STATE_TYPE = "stateType";
    private static final String PROPERTY = "property";
    private static final String CREATED = "Created";
    private static final String ERROR_MSG ="errorMsg";
    

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }
    
    public List<DocumentType> getDocuments(final TransactionType transactionType) {
        return propService.getDocumentTypesForTransactionType(transactionType);
    }

    public WriteOff getWriteOffById(final Long id) {
        return writeOffRepo.findOne(id);
    }

    public void addModelAttributes(final Model model, String assessmentNo, final HttpServletRequest request,
            List<Installment> installmentList) {
        StringBuilder instString = new StringBuilder();
        List<Map<String, Object>> sewConnDetails = propertyTaxCommonUtils
                .getSewConnDetails(assessmentNo, request);
        List<Map<String, Object>> wcDetails = propertyService.getWCDetails(assessmentNo, request);
        for (Installment inst : installmentList)
            instString = instString.append(inst.getDescription() + ",");
        model.addAttribute("installments", installmentList);
        model.addAttribute("fromInstallments", installmentList);
        model.addAttribute("toInstallments", installmentList);
        model.addAttribute("instString", instString);
        model.addAttribute("wcDetails", wcDetails);
        model.addAttribute("sewConnDetails", sewConnDetails);
    }

    public void addModelAttributesupdate(final Model model, WriteOff writeOff) {
        List<HashMap<String, Object>> historyMap;
        User loggedInUser = securityUtils.getCurrentUser();
        model.addAttribute("type", propertyMutationMasterHibDAO
                .getAllPropertyMutationMastersByType(WRITEOFF_CODE));
        model.addAttribute("reasonsList", getAllwriteoffMastersByType(writeOff.getWriteOffType().getMutationDesc()));
        model.addAttribute(PROPERTY, writeOff.getProperty());
        model.addAttribute(CURRENT_STATE, writeOff.getCurrentState().getValue());
        model.addAttribute(STATE_TYPE, writeOff.getClass().getSimpleName());
        model.addAttribute(STATE_AWARE_ID, writeOff.getId());
        model.addAttribute(ENDORSEMENT_NOTICE, new ArrayList<>());
        model.addAttribute("writeOff", writeOff);
        model.addAttribute(TRANSACTION_TYPE,APPLICATION_TYPE_WRITE_OFF);
        model.addAttribute(LOGGED_IN_USER, propertyService.isEmployee(loggedInUser));
        if (writeOff.getId() != null && writeOff.getState() != null) {
            historyMap = propertyService.populateHistory(writeOff);
            model.addAttribute(HISTORY_MAP, historyMap);
            model.addAttribute(STATE, writeOff.getState());
        }

    }

    @Transactional
    public WriteOff saveWriteOff(WriteOff writeOff, Long approvalPosition, final String approvalComent,
            final String additionalRule, final String workFlowAction) {
        final User user = securityUtils.getCurrentUser();
        final DateTime currentDate = new DateTime();
        Position pos = null;
        Assignment wfInitiator = null;
        String currentState;
        Assignment assignment = null;
        String approverDesignation = "";
        String nextAction = null;
        String loggedInUserDesignation = "";
        String loggedInUserDesig = "";
        List<Assignment> loggedInUserAssign;

        if (writeOff.getState() != null) {
            loggedInUserAssign = assignmentService.getAssignmentByPositionAndUserAsOnDate(
                    writeOff.getCurrentState().getOwnerPosition().getId(), user.getId(), new Date());
            loggedInUserDesig = !loggedInUserAssign.isEmpty() ? loggedInUserAssign.get(0).getDesignation().getName()
                    : "";
        } else {
            assignment = propertyTaxCommonUtils.getWorkflowInitiatorAsRO(user.getId());
            wfInitiator = assignment;
            loggedInUserDesig = assignment != null ? assignment.getDesignation().getName() : "";
        }

        if (loggedInUserDesig.contains(REVENUE_OFFICER_DESGN)
                && !workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_REJECT)) {
            currentState = "Created";
            assignment = assignmentService.getAssignmentsForPosition(approvalPosition, new Date()).get(0);
            approverDesignation = assignment.getDesignation().getName();
            if (null != assignment)
                approvalPosition = assignment.getPosition().getId();
        } else {
            currentState = null;
            if (null != approvalPosition && approvalPosition != 0) {
                assignment = assignmentService.getAssignmentsForPosition(approvalPosition, new Date()).get(0);
                assignment.getEmployee().getName().concat("~").concat(assignment.getPosition().getName());
                approverDesignation = assignment.getDesignation().getName();
            }
        }

        if (writeOff.getState() != null)
            loggedInUserDesignation = getLoggedInUserDesignation(writeOff.getCurrentState().getOwnerPosition().getId(),
                    securityUtils.getCurrentUser());
        if (WFLOW_ACTION_STEP_FORWARD.equalsIgnoreCase(workFlowAction)) {

            final String designation = approverDesignation.split(" ")[0];
            nextAction = getWorkflowNextAction(designation);
        }
        if (writeOff.getId() != null && writeOff.getState() != null)
            wfInitiator = assignmentService
                    .getAssignmentsForPosition(writeOff.getState().getInitiatorPosition().getId()).get(0);
        else if (wfInitiator == null)
            wfInitiator = propertyTaxCommonUtils.getWorkflowInitiatorAsRO(user.getId());

        WorkFlowMatrix wfmatrix = null;
        if (WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction)) {
            if (wfInitiator.getPosition().equals(writeOff.getState().getOwnerPosition())) {
                writeOff.transition().end().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withDateInfo(currentDate.toDate()).withNextAction(null)
                        .withOwner(writeOff.getState().getOwnerPosition());
                writeOff.getProperty().setStatus(STATUS_CANCELLED);
                writeOff.getBasicProperty().setUnderWorkflow(FALSE);
            } else {
                final Assignment assignmentOnreject = assignmentService
                        .getAssignmentsForPosition(writeOff.getState().getInitiatorPosition().getId()).get(0);
                if (assignmentOnreject != null) {
                    nextAction = "Revenue Officer Approval Pending";
                    wfInitiator = assignmentOnreject;
                } else
                    nextAction = WF_STATE_REVENUE_OFFICER_APPROVAL_PENDING;
                final String stateValue = WF_STATE_REJECTED;
                writeOff.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(stateValue).withDateInfo(currentDate.toDate())
                        .withOwner(wfInitiator.getPosition()).withNextAction(nextAction);
            }

        }

        else {
            if (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workFlowAction)) {
                pos = writeOff.getCurrentState().getOwnerPosition();
            } else if (null != approvalPosition && approvalPosition != -1 && !approvalPosition.equals(Long.valueOf(0)))
                pos = positionMasterService.getPositionById(approvalPosition);
            if (null == writeOff.getState()) {
                wfmatrix = propertyWorkflowService.getWfMatrix(writeOff.getStateType(), null, null, additionalRule,
                        currentState, null);
                writeOff.transition().start().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState()).withDateInfo(new Date())
                        .withOwner(pos).withNextAction(nextAction).withNatureOfTask(NATURE_WRITE_OFF)
                        .withInitiator(wfInitiator != null ? wfInitiator.getPosition() : null)
                        .withSLA(propertyService.getSlaValue(APPLICATION_TYPE_WRITE_OFF));

            }

            else if (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workFlowAction)) {
                wfmatrix = propertyWorkflowService.getWfMatrix(writeOff.getStateType(), null, null, additionalRule,
                        writeOff.getCurrentState().getValue(), writeOff.getCurrentState().getNextAction(), null,
                        loggedInUserDesignation);
                writeOff.getProperty().setStatus(STATUS_ISACTIVE);
                writeOff.getBasicProperty().getActiveProperty().setStatus(STATUS_ISHISTORY);
                propertyService.copyCollection(writeOff.getBasicProperty().getActiveProperty(), writeOff.getProperty());
                writeOff.getBasicProperty().addProperty(writeOff.getProperty());
                nextAction = WF_STATE_DIGITAL_SIGNATURE_PENDING;
                writeOff.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState()).withDateInfo(new Date())
                        .withOwner(pos)
                        .withNextAction(nextAction != null ? nextAction : wfmatrix.getNextAction())
                        .withNatureOfTask(NATURE_WRITE_OFF)
                        .withInitiator(wfInitiator != null ? wfInitiator.getPosition() : null)
                        .withSLA(propertyService.getSlaValue(APPLICATION_TYPE_WRITE_OFF));

            } else if (WFLOW_ACTION_STEP_FORWARD.equalsIgnoreCase(workFlowAction)) {
                wfmatrix = propertyWorkflowService.getWfMatrix(writeOff.getStateType(), null, null, additionalRule,
                        writeOff.getCurrentState().getValue(), writeOff.getCurrentState().getNextAction(), null,
                        loggedInUserDesignation);
                writeOff.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState()).withDateInfo(new Date())
                        .withOwner(pos)
                        .withNextAction(nextAction).withNatureOfTask(NATURE_WRITE_OFF)
                        .withInitiator(wfInitiator != null ? wfInitiator.getPosition() : null)
                        .withSLA(propertyService.getSlaValue(APPLICATION_TYPE_WRITE_OFF));

            } else {
                wfmatrix = propertyWorkflowService.getWfMatrix(writeOff.getStateType(), null, null, additionalRule,
                        writeOff.getCurrentState().getValue(), writeOff.getCurrentState().getNextAction(), null,
                        loggedInUserDesignation);
                writeOff.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate())
                        .withOwner(pos)
                        .withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction(StringUtils.isNotBlank(nextAction)
                                ? getNextAction(approverDesignation, workFlowAction) : wfmatrix.getNextAction());

            }
        }
        return writeOffRepo.save(writeOff);
    }

    private String getNextAction(final String approverDesignation, String workFlowAction) {
        String nextAction = "";
        if (WFLOW_ACTION_STEP_FORWARD.equalsIgnoreCase(workFlowAction)
                && COMMISSIONER_DESIGNATIONS.contains(approverDesignation)) {

            final String designation = approverDesignation.split(" ")[0];
            if (designation.equalsIgnoreCase(COMMISSIONER_DESGN))
                nextAction = WF_STATE_COMMISSIONER_APPROVAL_PENDING;
            else if (REVENUE_OFFICER_DESGN.equalsIgnoreCase(approverDesignation))
                nextAction = WF_STATE_REVENUE_OFFICER_APPROVAL_PENDING;
            else
                nextAction = new StringBuilder().append(designation).append(" ")
                        .append(WF_STATE_COMMISSIONER_APPROVAL_PENDING).toString();
        }
        return nextAction;
    }

    public String getLoggedInUserDesignation(final Long posId, final User user) {
        final List<Assignment> loggedInUserAssign = assignmentService.getAssignmentByPositionAndUserAsOnDate(posId,
                user.getId(), new Date());
        return !loggedInUserAssign.isEmpty() ? loggedInUserAssign.get(0).getDesignation().getName() : null;
    }

    private String getWorkflowNextAction(final String designation) {
        String nextAction;
        if (designation.equalsIgnoreCase(COMMISSIONER_DESGN))
            nextAction = WF_STATE_COMMISSIONER_APPROVAL_PENDING;
        else
            nextAction = new StringBuilder().append(designation).append(" ")
                    .append(WF_STATE_COMMISSIONER_APPROVAL_PENDING).toString();
        return nextAction;
    }

    @SuppressWarnings("unchecked")
    public List<WriteOffReasons> getAllwriteoffMastersByType(String type) {
        Query qry = getCurrentSession()
                .createQuery("from WriteOffReasons WO where WO.type = :type order by WO.orderId");
        qry.setString("type", type);
        return qry.list();
    }

    @SuppressWarnings("unchecked")
    public List<WriteOffReasons> getAllwriteoffMastersByName(Long id) {
        Query qry = getCurrentSession().createQuery("from WriteOffReasons WO where WO.id = :id");
        qry.setLong("id", id);
         return qry.list();
    }

    @SuppressWarnings("deprecation")
    public WriteOff saveProperty(WriteOff writeOff) {
        final Character status = STATUS_WORKFLOW;
        PropertyImpl newProperty;
        newProperty = writeOff.getProperty();
        newProperty.setPropertyModifyReason(WRITE_OFF);
        writeOff.getBasicProperty().setUnderWorkflow(Boolean.TRUE);
        newProperty = propertyService.createProperty(newProperty, null, writeOff.getWriteOffType().getCode(),
                newProperty.getPropertyDetail().getPropertyTypeMaster().getId().toString(), null, null, status, null,
                null, null, null, null, null, null, null, null, null, Boolean.FALSE);
        newProperty.setEffectiveDate(new Date());
        writeOff.setProperty(newProperty);
        writeOff.setApplicationNumber(applicationNo.generate());
        return writeOff;

    }

    @SuppressWarnings("deprecation")
    public void setPtDemandSet(WriteOff writeOff) {
        Ptdemand ptdemand;
        List<Ptdemand> currPtdemand = getCurrPtDemand(writeOff);

        if (currPtdemand != null) {
            ptdemand = (Ptdemand) currPtdemand.get(0).clone();
            ptdemand.setEgptProperty(writeOff.getProperty());
            ptdemand.getDmdCalculations().setCreatedDate(new Date());
            persistenceService.applyAuditing(ptdemand.getDmdCalculations());
            writeOff.getProperty().getPtDemandSet().clear();
            writeOff.getProperty().getPtDemandSet().add(ptdemand);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Ptdemand> getCurrPtDemand(WriteOff writeOff) {
        final List<Ptdemand> currPtdemand;
        final javax.persistence.Query qry = entityManager.createNamedQuery("QUERY_CURRENT_PTDEMAND");
        qry.setParameter("basicProperty", writeOff.getProperty().getBasicProperty());
        qry.setParameter("installment", propertyTaxCommonUtils.getCurrentInstallment());
        currPtdemand = qry.getResultList();
        return currPtdemand;
    }

    public DocumentType getDocType(String docname) {
        return writeOffRepo.findDocumentTypeByName(docname);

    }

    public WriteOff getLatestSpecialNoticeGeneratedWriteOff(final String upicNo) {
        WriteOff writeOff = null;
        final List<WriteOff> approvedRemissionList = writeOffRepo.findAllSpecialNoticesGeneratedForUpicNo(upicNo);
        if (!approvedRemissionList.isEmpty())
            writeOff = approvedRemissionList.get(0);
        return writeOff;
    }

    @SuppressWarnings("deprecation")
    public void updateDemandDetails(WriteOff writeOff) {

        Set<EgDemandDetails> demandDetails = propertyService.getCurrrentDemand(writeOff.getProperty())
                .getEgDemandDetails();
        DemandDetailVariation dmdVar = null;
        List<Installment> install = new ArrayList<>();
        for (final EgDemandDetails dmdDetails : demandDetails) {
            for (final DemandDetail dmdDetailBean : writeOff.getDemandDetailBeanList()) {
                Boolean isUpdateAmount = Boolean.FALSE;
                dmdDetailBean.setInstallment(installmentDao.findById(dmdDetailBean.getInstallment().getId(), false));
                if (dmdDetails.getEgDemandReason().getEgInstallmentMaster().getId()
                        .equals(dmdDetailBean.getInstallment().getId())) {
                    if (dmdDetailBean.getRevisedAmount() != null) {
                        if (dmdDetailBean.getRevisedAmount() != null
                                && dmdDetailBean.getRevisedAmount().compareTo(BigDecimal.ZERO) > 0
                                && dmdDetails.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster()
                                        .equalsIgnoreCase(dmdDetailBean.getReasonMaster())) {
                            install.add(dmdDetailBean.getInstallment());
                            isUpdateAmount = true;
                        }

                    }
                }
                if (isUpdateAmount) {
                    BigDecimal balance = dmdDetailBean.getActualAmount().subtract(
                            dmdDetailBean.getActualCollection() != null ? dmdDetailBean.getActualCollection() : BigDecimal.ZERO);
                    if (writeOff.getWriteOffType().getMutationDesc().equalsIgnoreCase(FULL_WRITEOFF)) {
                        dmdVar = persistDemandDetailVariation(dmdDetails, balance, WRITE_OFF);
                    } else {
                        dmdVar = persistDemandDetailVariation(dmdDetails, dmdDetailBean.getRevisedAmount(), WRITE_OFF);
                    }
                    Set<DemandDetailVariation> variationSet = new HashSet<>();
                    variationSet.add(dmdVar);
                    dmdDetails.setDemandDetailVariation(variationSet);
                }
                if (isUpdateAmount) {
                    dmdDetails.setModifiedDate(new Date());
                    break;
                }

            }
        }
        if (writeOff.getWriteOffType().getMutationDesc().equalsIgnoreCase(FULL_WRITEOFF)) {
            install.sort(Comparator.comparing(Installment::getId));
            writeOff.setFromInstallment(install.get(0).getDescription());
            DemandDetail maxInstallment = writeOff.getDemandDetailBeanList().get(writeOff.getDemandDetailBeanList().size() - 1);
            writeOff.setToInstallment(maxInstallment.getInstallment().getDescription());

        }
        if (writeOff.getState() != null) {
            for (Ptdemand ptdemand : writeOff.getProperty().getPtDemandSet()) {

                ptdemand.setEgDemandDetails(demandDetails);
            }
        } else {
            List<Ptdemand> currPtdemand = getCurrPtDemand(writeOff);
            if (currPtdemand != null) {
                final Ptdemand ptdemand = (Ptdemand) currPtdemand.get(0).clone();
                ptdemand.setBaseDemand(getTotalDemand(demandDetails));
                ptdemand.setEgDemandDetails(demandDetails);
                ptdemand.setEgptProperty(writeOff.getProperty());
                ptdemand.getDmdCalculations().setCreatedDate(new Date());
                persistenceService.applyAuditing(ptdemand.getDmdCalculations());
                writeOff.getProperty().getPtDemandSet().clear();
                writeOff.getProperty().getPtDemandSet().add(ptdemand);
            }
        }

    }

    public DemandDetailVariation persistDemandDetailVariation(EgDemandDetails dmdDetails, BigDecimal revisedAmount,
            String code) {
        DemandDetailVariation demandDetailVariation = new DemandDetailVariation();
        demandDetailVariation.setDemandDetail(dmdDetails);
        demandDetailVariation.setDemandreasonMaster(demandGenericDAO.getDemandReasonMasterByCode(code, module()));
        if(revisedAmount!=null && revisedAmount.compareTo(BigDecimal.ZERO)>0)
            demandDetailVariation.setDramount(revisedAmount);
        else
            demandDetailVariation.setDramount(BigDecimal.ZERO);
        return demandDetailVariation;
        
    }

    public List<EgDemandDetails> sortDemandDetails(List<EgDemandDetails> demandDetails) {
        Collections.sort(demandDetails,
                new ComparatorImplementation().thenComparing((Comparator<EgDemandDetails>) (dmdDtl1, dmdDtl2) -> dmdDtl1
                        .getEgDemandReason().getEgDemandReasonMaster().getOrderId().compareTo(
                                dmdDtl2.getEgDemandReason().getEgDemandReasonMaster().getOrderId())));
        return demandDetails;
    }

    public List<DemandDetail> setDemandBeanList(List<EgDemandDetails> demandDetails) {

        List<DemandDetail> demandDetailList = new ArrayList<>();

        for (final EgDemandDetails demandDetail : demandDetails) {
            final Installment installment = demandDetail.getEgDemandReason().getEgInstallmentMaster();
            final String reasonMaster = demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster();
            final DemandDetail dmdDtl = createDemandDetailBean(installment, reasonMaster, demandDetail.getAmount(),
                    demandDetail.getAmtCollected());
            demandDetailList.add(dmdDtl);
        }
        return demandDetailList;
    }

    private DemandDetail createDemandDetailBean(final Installment installment, final String reasonMaster,
            final BigDecimal amount, final BigDecimal amountCollected) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Entered into createDemandDetailBean");
            LOGGER.debug("createDemandDetailBean - installment=" + installment + ", reasonMaster=" + reasonMaster
                    + ", amount=" + amount + ", amountCollected=" + amountCollected);
        }

        final DemandDetail demandDetail = new DemandDetail();
        demandDetail.setInstallment(installment);
        demandDetail.setReasonMaster(reasonMaster);
        demandDetail.setActualAmount(amount);
        demandDetail.setActualCollection(amountCollected);
        demandDetail.setIsCollectionEditable(true);
        return demandDetail;
    }

    protected Module module() {
        return moduleDao.getModuleByName(PTMODULENAME);
    }

    private BigDecimal getTotalDemand(Set<EgDemandDetails> dmndDetails) {
        BigDecimal totalDmd = BigDecimal.ZERO;
        for (EgDemandDetails newDemandDetails : dmndDetails) {
            totalDmd = totalDmd.add(newDemandDetails.getAmount());
        }
        return totalDmd;
    }

    public void addDemandDetails(WriteOff writeOff) {

        List<DemandDetail> demandDetailList = getDemandDetails(writeOff);
        writeOff.setDemandDetailBeanList(demandDetailList);

    }

    private List<DemandDetail> setDemandBeanLists(List<EgDemandDetails> newDmndDetails,
            List<EgDemandDetails> oldDmndDetails) {

        List<DemandDetail> demandDetailList = new ArrayList<>();

        int i = 0;
        for (final EgDemandDetails demandDetail : newDmndDetails) {
            for (final EgDemandDetails oldDemandDetail : oldDmndDetails) {
                if (oldDemandDetail.getEgDemandReason().getEgInstallmentMaster()
                        .equals(demandDetail.getEgDemandReason().getEgInstallmentMaster())
                        && oldDemandDetail.getEgDemandReason().getEgDemandReasonMaster()
                                .equals(demandDetail.getEgDemandReason().getEgDemandReasonMaster())) {
                    final Installment installment = demandDetail.getEgDemandReason().getEgInstallmentMaster();
                    final String reasonMaster = demandDetail.getEgDemandReason().getEgDemandReasonMaster()
                            .getReasonMaster();
                    BigDecimal revisedAmount = BigDecimal.ZERO;
                    for (DemandDetailVariation demandDetailVariation : demandDetail.getDemandDetailVariation()) {
                        if (demandDetailVariation.getDemandDetail().getId().equals(demandDetail.getId())
                                && demandDetailVariation.getDramount().compareTo(BigDecimal.ZERO) >= 0) {
                            revisedAmount = demandDetailVariation.getDramount();
                            break;
                        }
                    }

                    final BigDecimal revisedCollection = demandDetail.getAmtCollected();
                    final DemandDetail dmdDtl = createDemandDetailBean(installment, reasonMaster,
                            oldDemandDetail.getAmount(), revisedAmount, oldDemandDetail.getAmtCollected(),
                            revisedCollection);
                    demandDetailList.add(i, dmdDtl);

                    break;
                }
            }
            i++;
        }
        return demandDetailList;
    }

    private DemandDetail createDemandDetailBean(final Installment installment, final String reasonMaster,
            final BigDecimal amount, final BigDecimal revisedAmount, final BigDecimal amountCollected,
            final BigDecimal revisedCollection) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Entered into createDemandDetailBean");
            LOGGER.debug("createDemandDetailBean - installment=" + installment + ", reasonMaster=" + reasonMaster
                    + ", amount=" + amount + ", amountCollected=" + amountCollected);
        }

        final DemandDetail demandDetail = new DemandDetail();
        demandDetail.setInstallment(installment);
        demandDetail.setReasonMaster(reasonMaster);
        demandDetail.setActualAmount(amount);
        demandDetail.setRevisedAmount(revisedAmount);
        demandDetail.setActualCollection(amountCollected);
        demandDetail.setRevisedCollection(revisedCollection);
        demandDetail.setIsCollectionEditable(true);
        return demandDetail;
    }
   
    public List<DemandDetail> getDemandDetails(WriteOff writeOff) {
        Set<EgDemandDetails> newDemandDetails = (ptDemandDAO.getNonHistoryCurrDmdForProperty(writeOff.getProperty()))
                .getEgDemandDetails();
        Set<EgDemandDetails> oldDemandDetails = (ptDemandDAO
                .getNonHistoryCurrDmdForProperty(writeOff.getBasicProperty().getProperty())).getEgDemandDetails();
        List<EgDemandDetails> newDmndDetails = new ArrayList<>(newDemandDetails);
        List<EgDemandDetails> oldDmndDetails = new ArrayList<>(oldDemandDetails);

        if (!newDmndDetails.isEmpty())
            newDmndDetails = sortDemandDetails(newDmndDetails);

        if (!oldDmndDetails.isEmpty())
            oldDmndDetails = sortDemandDetails(oldDmndDetails);

        return setDemandBeanLists(newDmndDetails, oldDmndDetails);
    }

    private final class ComparatorImplementation implements Comparator<EgDemandDetails> {
        @Override
        public int compare(EgDemandDetails dmdDtl1, EgDemandDetails dmdDtl2) {
            return dmdDtl1.getEgDemandReason().getEgInstallmentMaster()
                    .compareTo(dmdDtl2.getEgDemandReason().getEgInstallmentMaster());
        }
    }

    public boolean checkActiveWC(List<Map<String, Object>> wcDetails) {
        boolean connStatus = false;
        for (Map<String, Object> status : wcDetails) {
            for (Object state : status.values()) {
                if (ACTIVE.equalsIgnoreCase(state.toString())) {
                    connStatus = true;
                }
            }
        }
        return connStatus;

    }

    public boolean checkActiveSewage(List<Map<String, Object>> sewConnDetails) {
        boolean connStatus = false;
        for (Map<String, Object> error : sewConnDetails) {
            for (Object errors : error.values()) {
                if (errors.toString().isEmpty()) {
                    connStatus = true;
                }
            }
        }
        return connStatus;

    }
    
    public boolean checkValidCouncil(List<Map<String, String>> councilDetails) {
        boolean status = false;
        for (Map<String, String> errormessage : councilDetails) {
            for (String errors : errormessage.values()) {
                if (errors.toString().isEmpty()) {
                    status = true;
                }
            }
        }
        return status;

    }
    
    public Map<String, String> displayValidation(final WriteOff writeOff, final HttpServletRequest request, Model model) {
        Map<String, String> errorMessages = new HashMap<>();
        if (writeOff.getPropertyDeactivateFlag() && writeOff.getWriteOffType().getCode().equals("FULL WRITEOFF")) {
            errorMessages = validateWCSewconnection(writeOff, request);
        }
        if (errorMessages.isEmpty() && writeOff.getState() == null) {
            List<WriteOff> councilresult = writeOffRepo.findCouncilTypeAndNo(writeOff.getResolutionType(),
                    writeOff.getResolutionNo(),
                    writeOff.getBasicProperty().getUpicNo());
            if (councilresult != null && !councilresult.isEmpty())
                errorMessages.put("resolutionError", "writeoff.resolution.error");
        }
        return errorMessages;
    }

    public BindingResult validate(final WriteOff writeOff, final BindingResult errors) {
        if (StringUtils.isBlank(writeOff.getWriteOffType().getCode()))
            errors.rejectValue("writeOffType.mutationDesc", "writeoff.type");
        if (StringUtils.isBlank(writeOff.getWriteOffReasons().getName()))
            errors.rejectValue("writeOffReasons.name", "writeoff.reason");
        if (StringUtils.isBlank(writeOff.getResolutionNo()) || null == writeOff.getResolutionNo())
            errors.rejectValue("resolutionNo", "writeoff.resolution.no");
        if (StringUtils.isBlank(writeOff.getResolutionType()) || null == writeOff.getResolutionType())
            errors.rejectValue("resolutionType", "writeoff.resolution.type");
        return errors;
    }
    
    public Map<String, String> validateWCSewconnection(WriteOff writeOff, final HttpServletRequest request) {
        boolean hasActiveWC;
        boolean hasActiveSwg;
        boolean hasActiveCouncil;
        Map<String, String> errorMessages = new HashMap<>();
        List<Map<String, Object>> activeWCDetails = propertyService.getWCDetails(writeOff.getBasicProperty().getUpicNo(),
                request);
        hasActiveWC = checkActiveWC(activeWCDetails);
        if (hasActiveWC) {
            errorMessages.put("active.error", "writeoff.wc.error");
            return errorMessages;
        }
        hasActiveSwg = checkActiveSewage(propertyTaxCommonUtils
                .getSewConnDetails(writeOff.getBasicProperty().getUpicNo(), request));
        if (hasActiveSwg) {
            errorMessages.put("active.error", "writeoff.Swg.error");
            return errorMessages;
        }
        hasActiveCouncil = checkValidCouncil(
                propertyTaxCommonUtils.getCouncilDeatils(writeOff.getResolutionNo(), writeOff.getResolutionType(), request));
        if (hasActiveCouncil) {
            errorMessages.put("active.error", "writeoff.council.error");
            return errorMessages;
        }
        return errorMessages;
    }

    public String displayErrors(WriteOff writeOff, Model model, Map<String, String> errorMessages,
            HttpServletRequest request) {
        User loggedInUser = securityUtils.getCurrentUser();
        model.addAttribute(ERROR_MSG, errorMessages);
        model.addAttribute("writeOff", writeOff);
        List<Installment> installmentList = propertyTaxUtil.getInstallments(writeOff.getBasicProperty().getActiveProperty());
        Set<EgDemandDetails> demandDetails = (ptDemandDAO
                .getNonHistoryCurrDmdForProperty(writeOff.getBasicProperty().getProperty()))
                        .getEgDemandDetails();
        List<EgDemandDetails> dmndDetails = new ArrayList<>(demandDetails);
        if (!dmndDetails.isEmpty())
            dmndDetails = sortDemandDetails(dmndDetails);
        List<DemandDetail> demandDetailBeanList = setDemandBeanList(dmndDetails);
        writeOff.setDemandDetailBeanList(demandDetailBeanList);
        model.addAttribute("dmndDetails", writeOff.getDemandDetailBeanList());
        model.addAttribute("type", propertyMutationMasterHibDAO
                .getAllPropertyMutationMastersByType(WRITEOFF_CODE));
        model.addAttribute(PROPERTY, writeOff.getProperty());
        model.addAttribute("reasonsList", getAllwriteoffMastersByType(writeOff.getWriteOffType().getMutationDesc()));
        model.addAttribute(CURRENT_STATE, CREATED);
        model.addAttribute(ENDORSEMENT_NOTICE, new ArrayList<>());
        model.addAttribute(STATE_TYPE, writeOff.getClass().getSimpleName());
        model.addAttribute(LOGGED_IN_USER, propertyService.isEmployee(loggedInUser));
        addModelAttributes(model, writeOff.getBasicProperty().getUpicNo(), request, installmentList);
        model.addAttribute("fromInstallment", writeOff.getFromInstallment());
        model.addAttribute("toInstallment", writeOff.getToInstallment());
        prepareWorkflow(model, writeOff, new WorkflowContainer());

        return WO_FORM;
    }

}