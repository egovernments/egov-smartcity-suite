package org.egov.ptis.domain.service.writeOff;

import static java.lang.Boolean.FALSE;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_WRITE_OFF;
import static org.egov.ptis.constants.PropertyTaxConstants.COMMISSIONER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.COMMISSIONER_DESIGNATIONS;
import static org.egov.ptis.constants.PropertyTaxConstants.COUNCIL_ERROR;
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
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.PropertyMutationMasterHibDAO;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.enums.TransactionType;
import org.egov.ptis.domain.entity.property.Document;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.entity.property.WriteOff;
import org.egov.ptis.domain.repository.master.structureclassification.StructureClassificationRepository;
import org.egov.ptis.domain.repository.writeOff.WriteOffReasonRepository;
import org.egov.ptis.domain.repository.writeOff.WriteOffRepository;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.domain.service.voucher.DemandVoucherService;
import org.egov.ptis.master.service.PropertyUsageService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class WriteOffService extends GenericWorkFlowController {

    private static final Logger LOGGER = Logger.getLogger(WriteOffService.class);
    private static final String CURRENT_STATE = "currentState";
    private static final String STATE_TYPE = "stateType";
    private static final String PROPERTY = "property";
    private static final String CREATED = "Created";
    private static final String ERROR_MSG = "errorMsg";
    private static final String ERROR = "error";
    Property property = null;

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
    private WriteOffReasonRepository writeOffReasonRepository;
    @Autowired
    private DemandVoucherService demandVoucherService;

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
        boolean hasActiveSwg = checkActiveSewage(sewConnDetails);
        for (Installment inst : installmentList)
            instString = instString.append(inst.getDescription() + ",");
        model.addAttribute("installments", installmentList);
        model.addAttribute("fromInstallments", installmentList);
        model.addAttribute("toInstallments", installmentList);
        model.addAttribute("instString", instString);
        model.addAttribute("wcDetails", wcDetails);
        model.addAttribute("sewConnDetails", sewConnDetails);
        model.addAttribute("hasActiveSwg", hasActiveSwg);
    }

    public void addModelAttributesupdate(final Model model, WriteOff writeOff) {
        List<HashMap<String, Object>> historyMap;
        User loggedInUser = securityUtils.getCurrentUser();
        model.addAttribute("type", propertyMutationMasterHibDAO
                .getAllPropertyMutationMastersByType(WRITEOFF_CODE));
        model.addAttribute("reasonsList",
                writeOffReasonRepository.findByTypeOrderByIdAsc(writeOff.getWriteOffType().getMutationDesc()));
        model.addAttribute(PROPERTY, writeOff.getProperty());
        model.addAttribute(CURRENT_STATE, writeOff.getCurrentState().getValue());
        model.addAttribute(STATE_TYPE, writeOff.getClass().getSimpleName());
        model.addAttribute(STATE_AWARE_ID, writeOff.getId());
        model.addAttribute("writeOff", writeOff);
        model.addAttribute(TRANSACTION_TYPE, APPLICATION_TYPE_WRITE_OFF);
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
            if (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workFlowAction))
                pos = writeOff.getCurrentState().getOwnerPosition();
            else if (null != approvalPosition && approvalPosition != -1 && !approvalPosition.equals(Long.valueOf(0)))
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
                propertyService.copyCollection(writeOff.getBasicProperty().getActiveProperty(),
                        writeOff.getBasicProperty().getActiveProperty());
                propertyService.copyCollection(writeOff.getBasicProperty().getActiveProperty(), writeOff.getProperty());
                if (writeOff.getWriteOffType().getCode().equals("FULL WRITEOFF"))
                    demandVoucherService.createDemandVoucher(
                            writeOff.getBasicProperty().getActiveProperty(), null,
                            propertyTaxCommonUtils.prepareApplicationDetailsForDemandVoucher(
                                    NATURE_WRITE_OFF,
                                    PropertyTaxConstants.ZERO_DEMAND));
                else
                    demandVoucherService.createDemandVoucher(writeOff.getProperty(),
                            writeOff.getBasicProperty().getActiveProperty(),
                            propertyTaxCommonUtils.prepareApplicationDetailsForDemandVoucher(
                                    NATURE_WRITE_OFF,
                                    PropertyTaxConstants.NO_ACTION));
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

    public void updateProperty(WriteOff writeOff) {
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

    }

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

    public void updateDemandDetails(WriteOff writeOff) {

        updateDemandDetailVariation(writeOff);
        Set<EgDemandDetails> demandDetails = propertyService.getCurrrentDemand(writeOff.getProperty()).getEgDemandDetails();
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

    public void updateDemandDetailVariation(WriteOff writeOff) {
        List<Installment> install = new ArrayList<>();
        Set<EgDemandDetails> demandDetails = propertyService.getCurrrentDemand(writeOff.getProperty()).getEgDemandDetails();
        for (final EgDemandDetails demandDetail : demandDetails)
            for (final DemandDetail dmdDetailBean : writeOff.getDemandDetailBeanList()) {
                dmdDetailBean.setInstallment(installmentDao.findById(dmdDetailBean.getInstallment().getId(), false));
                if (dmdDetailBean.getRevisedAmount() != null && dmdDetailBean.getRevisedAmount().compareTo(BigDecimal.ZERO) > 0
                        && dmdDetailBean.getInstallment()
                                .equals(demandDetail.getEgDemandReason().getEgInstallmentMaster())
                        && demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster()
                                .equalsIgnoreCase(dmdDetailBean.getReasonMaster())) {
                    install.add(dmdDetailBean.getInstallment());
                    if (!demandDetail.getDemandDetailVariation().isEmpty())
                        for (DemandDetailVariation dmdVar : demandDetail.getDemandDetailVariation())
                            dmdVar.setDramount(dmdDetailBean.getRevisedAmount());
                    else {
                        DemandDetailVariation dmdVar = persistDemandDetailVariation(demandDetail,
                                dmdDetailBean.getRevisedAmount(),
                                WRITE_OFF);
                        Set<DemandDetailVariation> variationSet = new HashSet<>();
                        variationSet.add(dmdVar);
                        demandDetail.setDemandDetailVariation(variationSet);
                    }
                    demandDetail.setModifiedDate(new Date());
                    break;
                }
                if ((dmdDetailBean.getRevisedAmount() == null || dmdDetailBean.getRevisedAmount().compareTo(BigDecimal.ZERO) < 1)
                        && !demandDetail.getDemandDetailVariation().isEmpty())
                    for (DemandDetailVariation dmdVar : demandDetail.getDemandDetailVariation())
                        dmdVar.setDramount(BigDecimal.ZERO);
            }
        if (writeOff.getWriteOffType().getMutationDesc().equalsIgnoreCase(FULL_WRITEOFF)) {
            install.sort(Comparator.comparing(Installment::getId));
            writeOff.setFromInstallment(install.get(0).getDescription());
            DemandDetail maxInstallment = writeOff.getDemandDetailBeanList().get(writeOff.getDemandDetailBeanList().size() - 1);
            writeOff.setToInstallment(maxInstallment.getInstallment().getDescription());
        }
    }

    public DemandDetailVariation persistDemandDetailVariation(EgDemandDetails dmdDetails, BigDecimal revisedAmount,
            String code) {
        DemandDetailVariation demandDetailVariation = new DemandDetailVariation();
        demandDetailVariation.setDemandDetail(dmdDetails);
        demandDetailVariation.setDemandreasonMaster(demandGenericDAO.getDemandReasonMasterByCode(code, getModuleByName()));
        if (revisedAmount != null && revisedAmount.compareTo(BigDecimal.ZERO) > 0)
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
            final DemandDetail dmdDtl = setDemandDetailBean(installment, reasonMaster, demandDetail.getAmount(),
                    getTotalRevisedAmount(demandDetail), demandDetail.getAmtCollected());
            demandDetailList.add(dmdDtl);
        }
        return demandDetailList;
    }

    private DemandDetail setDemandDetailBean(final Installment installment, final String reasonMaster,
            final BigDecimal amount, final BigDecimal revisedAmount, final BigDecimal amountCollected) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Entered into setDemandDetailBean");
            LOGGER.debug("setDemandDetailBean - installment=" + installment + ", reasonMaster=" + reasonMaster
                    + ", amount=" + amount + ", amountCollected=" + amountCollected);
        }

        final DemandDetail demandDetail = new DemandDetail();
        demandDetail.setInstallment(installment);
        demandDetail.setReasonMaster(reasonMaster);
        demandDetail.setActualAmount(amount);
        demandDetail.setRevisedAmount(revisedAmount);
        demandDetail.setActualCollection(amountCollected);
        demandDetail.setIsCollectionEditable(true);
        return demandDetail;
    }

    protected Module getModuleByName() {
        return moduleDao.getModuleByName(PTMODULENAME);
    }

    private BigDecimal getTotalDemand(Set<EgDemandDetails> dmndDetails) {
        BigDecimal totalDmd = BigDecimal.ZERO;
        for (EgDemandDetails newDemandDetails : dmndDetails)
            totalDmd = totalDmd.add(newDemandDetails.getAmount());
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
            for (final EgDemandDetails oldDemandDetail : oldDmndDetails)
                if (oldDemandDetail.getEgDemandReason().getEgInstallmentMaster()
                        .equals(demandDetail.getEgDemandReason().getEgInstallmentMaster())
                        && oldDemandDetail.getEgDemandReason().getEgDemandReasonMaster()
                                .equals(demandDetail.getEgDemandReason().getEgDemandReasonMaster())) {
                    final Installment installment = demandDetail.getEgDemandReason().getEgInstallmentMaster();
                    final String reasonMaster = demandDetail.getEgDemandReason().getEgDemandReasonMaster()
                            .getReasonMaster();
                    BigDecimal revisedAmount = BigDecimal.ZERO;
                    for (DemandDetailVariation demandDetailVariation : demandDetail.getDemandDetailVariation())
                        if (demandDetailVariation.getDemandDetail().getId().equals(demandDetail.getId())
                                && demandDetailVariation.getDramount().compareTo(BigDecimal.ZERO) >= 0) {
                            revisedAmount = demandDetailVariation.getDramount().setScale(0, BigDecimal.ROUND_DOWN);
                            break;
                        }
                    final DemandDetail dmdDtl = createDemandDetailBean(installment, reasonMaster,
                            oldDemandDetail.getAmount().setScale(0, BigDecimal.ROUND_DOWN), revisedAmount,
                            oldDemandDetail.getAmtCollected().setScale(0, BigDecimal.ROUND_DOWN));
                    demandDetailList.add(i, dmdDtl);

                    break;
                }
            i++;
        }
        return demandDetailList;
    }

    private DemandDetail createDemandDetailBean(final Installment installment, final String reasonMaster,
            final BigDecimal amount, final BigDecimal revisedAmount, final BigDecimal amountCollected) {
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
        demandDetail.setIsCollectionEditable(true);
        return demandDetail;
    }

    public List<DemandDetail> getDemandDetails(WriteOff writeOff) {
        Set<EgDemandDetails> newDemandDetails = ptDemandDAO.getNonHistoryCurrDmdForProperty(writeOff.getProperty())
                .getEgDemandDetails();
        Set<EgDemandDetails> oldDemandDetails = ptDemandDAO
                .getNonHistoryCurrDmdForProperty(writeOff.getBasicProperty().getProperty()).getEgDemandDetails();
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

    public boolean checkActiveSewage(List<Map<String, Object>> sewConnDetails) {
        boolean connStatus = true;
        for (Map<String, Object> error : sewConnDetails)
            for (Object errors : error.values())
                if (errors.toString().contains("does not exist")) {
                    connStatus = false;
                    break;
                }
        return connStatus;

    }

    public boolean checkValidCouncil(List<Map<String, String>> councilDetails) {
        boolean status = true;
        for (Map<String, String> errormessage : councilDetails)
            for (String error : errormessage.values())
                if (COUNCIL_ERROR.equalsIgnoreCase(error)) {
                    status = false;
                    break;
                }
        return status;

    }

    public Map<String, String> displayValidation(final WriteOff writeOff, final HttpServletRequest request) {
        Map<String, String> errorMessages = new HashMap<>();
        if (writeOff.getPropertyDeactivateFlag() && writeOff.getWriteOffType().getCode().equals("FULL WRITEOFF"))
            errorMessages = validateWCSewconnection(writeOff, request);
        if (errorMessages.isEmpty())
            errorMessages = validateCouncil(writeOff, request);
        if (errorMessages.isEmpty())
            errorMessages = validateDemand(writeOff.getDemandDetailBeanList(), writeOff);
        return errorMessages;
    }

    public BindingResult validate(final WriteOff writeOff, final BindingResult errors) {
        int i = 0;
        if (!StringUtils.isNotBlank(writeOff.getWriteOffType().getCode()))
            errors.rejectValue("writeOffType.mutationDesc", "writeoff.type");
        if (!StringUtils.isNotBlank(writeOff.getWriteOffReasons().getCode()))
            errors.rejectValue("writeOffReasons.name", "writeoff.reason");
        if (!StringUtils.isNotBlank(writeOff.getResolutionNo()))
            errors.rejectValue("resolutionNo", "writeoff.resolution.no");
        if (!StringUtils.isNotBlank(writeOff.getResolutionType()))
            errors.rejectValue("resolutionType", "writeoff.resolution.type");
        if (StringUtils.isNotBlank(writeOff.getWriteOffType().getCode())
                && !writeOff.getWriteOffType().getCode().equalsIgnoreCase("FULL WRITEOFF")) {
            if (!StringUtils.isNotBlank(writeOff.getFromInstallment()))
                errors.rejectValue("fromInstallment", "writeoff.frominstallment");
            if (!StringUtils.isNotBlank(writeOff.getToInstallment()))
                errors.rejectValue("toInstallment", "writeoff.toinstallment");
        }
        if (writeOff.getDemandDetailBeanList() != null)
            for (final DemandDetail demanddetail : writeOff.getDemandDetailBeanList()) {
                demanddetail.setInstallment(installmentDao.findById(demanddetail.getInstallment().getId(), false));
                if (demanddetail.getRevisedAmount() != null && demanddetail.getActualAmount() != null
                        && demanddetail.getRevisedAmount().compareTo(demanddetail.getActualAmount()) > 0)
                    errors.rejectValue("demandDetailBeanList[" + i + "].revisedAmount",
                            "writeoff.revised.amount.is.greater.than");
                i++;
            }
        return errors;
    }

    public Map<String, String> validateWCSewconnection(WriteOff writeOff, final HttpServletRequest request) {
        Map<String, String> errorMessages = new HashMap<>();
        List<Map<String, Object>> activeWCDetails = propertyService.getWCDetails(writeOff.getBasicProperty().getUpicNo(),
                request);
        boolean hasActiveWC = propertyTaxCommonUtils.checkActiveWC(activeWCDetails);
        if (hasActiveWC) {
            errorMessages.put(ERROR, "writeoff.wc.swg.error");
            return errorMessages;
        }
        boolean hasActiveSwg = checkActiveSewage(propertyTaxCommonUtils
                .getSewConnDetails(writeOff.getBasicProperty().getUpicNo(), request));
        if (hasActiveSwg) {
            errorMessages.put(ERROR, "writeoff.wc.swg.error");
            return errorMessages;
        }
        return errorMessages;
    }

    public Map<String, String> validateCouncil(WriteOff writeOff, final HttpServletRequest request) {
        Map<String, String> errorMessages = new HashMap<>();
        boolean hasActiveCouncil = checkValidCouncil(
                propertyTaxCommonUtils.getCouncilDetails(writeOff.getResolutionNo(), writeOff.getResolutionType(), request));
        if (!hasActiveCouncil)
            errorMessages.put(ERROR, "writeoff.council.error");
        return errorMessages;
    }

    public String displayErrors(WriteOff writeOff, Model model, Map<String, String> errorMessages,
            HttpServletRequest request) {
        User loggedInUser = securityUtils.getCurrentUser();
        model.addAttribute(ERROR_MSG, errorMessages);
        List<Installment> installmentList = propertyTaxUtil.getInstallments(writeOff.getBasicProperty().getActiveProperty());
        writeOff.setDemandDetailBeanList(demandDisplay(writeOff));
        model.addAttribute("dmndDetails", writeOff.getDemandDetailBeanList());
        model.addAttribute("type", propertyMutationMasterHibDAO
                .getAllPropertyMutationMastersByType(WRITEOFF_CODE));
        model.addAttribute(PROPERTY, writeOff.getProperty());
        model.addAttribute("reasonsList",
                writeOffReasonRepository.findByTypeOrderByIdAsc(writeOff.getWriteOffType().getMutationDesc()));
        model.addAttribute(CURRENT_STATE, CREATED);
        model.addAttribute(STATE_TYPE, writeOff.getClass().getSimpleName());
        model.addAttribute(LOGGED_IN_USER, propertyService.isEmployee(loggedInUser));
        addModelAttributes(model, writeOff.getBasicProperty().getUpicNo(), request, installmentList);
        model.addAttribute("fromInstallment", writeOff.getFromInstallment());
        model.addAttribute("toInstallment", writeOff.getToInstallment());
        prepareWorkflow(model, writeOff, new WorkflowContainer());
        if (!writeOff.getWriteoffDocumentsProxy().isEmpty()) {
            List<Document> documentList = writeOff.getWriteoffDocumentsProxy();
            writeOff.getWriteoffDocumentsProxy().clear();
            writeOff.setWriteoffDocumentsProxy(documentList);
            model.addAttribute("attachedDocuments", writeOff.getWriteoffDocumentsProxy());
        }
        model.addAttribute("writeOff", writeOff);
        return WO_FORM;
    }

    public List<DemandDetail> demandDisplay(WriteOff writeOff) {
        List<DemandDetail> demandDetailBeanList = new ArrayList<>();
        for (final DemandDetail demandDetailBean : writeOff.getDemandDetailBeanList()) {
            demandDetailBean.setInstallment(installmentDao.findById(demandDetailBean.getInstallment().getId(), false));
            final DemandDetail dmdDtl = createDemandDetailBean(demandDetailBean.getInstallment(),
                    demandDetailBean.getReasonMaster(),
                    demandDetailBean.getActualAmount().setScale(0, BigDecimal.ROUND_DOWN),
                    demandDetailBean.getRevisedAmount() != null
                            ? demandDetailBean.getRevisedAmount().setScale(0, BigDecimal.ROUND_DOWN) : BigDecimal.ZERO,
                    demandDetailBean.getActualCollection().setScale(0, BigDecimal.ROUND_DOWN));
            demandDetailBeanList.add(dmdDtl);
        }
        return demandDetailBeanList;
    }

    public Map<String, String> validateDemand(List<DemandDetail> demandDetailBeanList, final WriteOff writeoff) {

        HashMap<String, String> errors = new HashMap<>();

        for (final DemandDetail demandDetail : demandDetailBeanList) {
            demandDetail.setInstallment(installmentDao.findById(demandDetail.getInstallment().getId(), false));
            if (demandDetail.getActualCollection() != null && demandDetail.getRevisedAmount() != null && demandDetail
                    .getActualCollection().add(demandDetail.getRevisedAmount()).compareTo(demandDetail.getActualAmount()) > 0)
                errors.put(ERROR, "writeoff.revised.amount.is.greater.than.sum");
            if (demandDetail.getInstallment().getDescription().equalsIgnoreCase(writeoff.getFromInstallment())
                    && demandDetail.getRevisedAmount() == null)
                errors.put(ERROR, "writeoff.revised.amount.is.less.than.zero");
            if (demandDetail.getRevisedAmount() != null && demandDetail.getRevisedAmount().compareTo(BigDecimal.ZERO) < 0)
                errors.put(ERROR, "writeoff.revised.amount.is.less.than.zero");
        }
        return errors;
    }

    public void processAndStoreApplicationDocuments(final WriteOff writeoff) {
        for (final Document applicationDocument : writeoff.getWriteoffDocumentsProxy())
            if (!writeoff.getWriteoffDocumentsProxy().isEmpty() && !writeoff.getDocuments().isEmpty())
                replaceDoc(writeoff, applicationDocument);
            else {
                writeoff.getDocuments().clear();
                writeoff.getDocuments().addAll(writeoff.getWriteoffDocumentsProxy());
                for (final Document document : writeoff.getDocuments())
                    if (document.getFile() != null) {
                        document.setType(getDocType(document.getType().getName()));
                        document.setFiles(propertyService.addToFileStore(document.getFile()));
                    }
            }
    }

    private void replaceDoc(final WriteOff writeoff, final Document applicationDocument) {
        for (MultipartFile mp : applicationDocument.getFile())
            if (mp.getSize() != 0)
                for (Document document : writeoff.getDocuments())
                    if (document.getType().getName().equals(applicationDocument.getType().getName()))
                        document.setFiles(propertyService.addToFileStore(applicationDocument.getFile()));
    }

    public BigDecimal getTotalRevisedAmount(final EgDemandDetails demandDetails) {
        BigDecimal revisedAmount = BigDecimal.ZERO;
        if (!demandDetails.getDemandDetailVariation().isEmpty())
            for (final DemandDetailVariation demandDetailVariation : demandDetails.getDemandDetailVariation())
                revisedAmount = revisedAmount.add(demandDetailVariation.getDramount().setScale(0, BigDecimal.ROUND_DOWN));
        return revisedAmount;
    }

}