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
package org.egov.ptis.actions.transfer;

import com.opensymphony.xwork2.ActionContext;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.entity.Source;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.eis.web.actions.workflow.GenericWorkFlowAction;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.integration.service.ThirdPartyService;
import org.egov.infra.notification.service.NotificationService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Position;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Document;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.entity.property.PropertyMutationTransferee;
import org.egov.ptis.domain.service.notice.NoticeService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.domain.service.property.PropertyThirdPartyService;
import org.egov.ptis.domain.service.reassign.ReassignService;
import org.egov.ptis.domain.service.transfer.PropertyTransferService;
import org.egov.ptis.event.MutationEventPublisher;
import org.egov.ptis.notice.PtNotice;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import static org.egov.ptis.constants.PropertyTaxConstants.*;

@Results({
        @Result(name = BaseFormAction.NEW, location = "transfer/transferProperty-new.jsp"),
        @Result(name = BaseFormAction.EDIT, location = "transfer/transferProperty-edit.jsp"),
        @Result(name = BaseFormAction.VIEW, location = "transfer/transferProperty-view.jsp"),
        @Result(name = PropertyTransferAction.REDIRECT, location = "transfer/transferProperty-redirect.jsp"),
        @Result(name = TARGET_WORKFLOW_ERROR, location = "workflow/workflow-error.jsp"),
        @Result(name = PropertyTransferAction.ACK, location = "transfer/transferProperty-ack.jsp"),
        @Result(name = PropertyTransferAction.ACK_FOR_REGISTRATION, location = "transfer/transferProperty-ackForRegistration.jsp"),
        @Result(name = PropertyTransferAction.REJECT_ON_TAXDUE, location = "transfer/transferProperty-balance.jsp"),
        @Result(name = PropertyTransferAction.PRINTACK, location = "transfer/transferProperty-printAck.jsp"),
        @Result(name = PropertyTransferAction.PRINTNOTICE, location = "transfer/transferProperty-printNotice.jsp"),
        @Result(name = PropertyTransferAction.SEARCH, location = "transfer/transferProperty-search.jsp"),
        @Result(name = PropertyTransferAction.ERROR, location = "common/meeseva-errorPage.jsp"),
        @Result(name = PropertyTransferAction.MEESEVA_RESULT_ACK, location = "common/meesevaAck.jsp"),
        @Result(name = PropertyTransferAction.COLLECT_FEE, location = "collection/collectPropertyTax-view.jsp"),
        @Result(name = PropertyTransferAction.COLLECT_ONLINE_FEE, location = "citizen/collection/collection-collectTax.jsp"),
        @Result(name = PropertyTransferAction.REDIRECT_SUCCESS, location = PropertyTransferAction.REDIRECT_SUCCESS, type = "redirectAction", params = {
                "assessmentNo", "${assessmentNo}", "mutationId", "${mutationId}" }),
        @Result(name = PropertyTransferAction.COMMON_FORM, location = "search/searchProperty-commonForm.jsp"),
        @Result(name = PropertyTransferAction.DIGITAL_SIGNATURE_REDIRECTION, location = "transfer/transferProperty-digitalSignatureRedirection.jsp") })
@Namespace("/property/transfer")
public class PropertyTransferAction extends GenericWorkFlowAction {
    private static final String WITH_ASSESSMENT_NUMBER = " with assessment number : ";
    public static final String ACK_FOR_REGISTRATION = "ackForRegistration";
    public static final String ACK = "ack";
    public static final String ERROR = "error";
    public static final String SEARCH = "search";
    public static final String REJECT_ON_TAXDUE = "balance";
    public static final String PRINTACK = "printAck";
    public static final String PRINTNOTICE = "printNotice";
    public static final String REDIRECT_SUCCESS = "redirect-success";
    public static final String COLLECT_FEE = "collect-fee";
    public static final String MEESEVA_RESULT_ACK = "meesevaAck";
    public static final String COLLECT_ONLINE_FEE = "onlineCollection";
    protected static final String COMMON_FORM = "commonForm";
    protected static final String REDIRECT = "redirect";
    protected static final String DIGITAL_SIGNATURE_REDIRECTION = "digitalSignatureRedirection";
    private static final String PROPERTY_TRANSFER = "property transfer";
    private static final long serialVersionUID = 1L;
    private static final String PROPERTY_MODIFY_REJECT_FAILURE = "property.modify.reject.failure";
    public static final String TAXDUE = "taxdue";
    public static final String STRUCTURED = "structured";
    private static final String POSITION_EXPIRED = "position.expired";

    @Autowired
    protected transient AssignmentService assignmentService;
    // Form Binding Model
    private transient PropertyMutation propertyMutation = new PropertyMutation();
    // Dependent Services
    @Autowired
    @Qualifier("transferOwnerService")
    private transient PropertyTransferService transferOwnerService;
    @Autowired
    @Qualifier("workflowService")
    private transient SimpleWorkflowService<PropertyMutation> transferWorkflowService;

    @Autowired
    private transient PropertyService propertyService;

    @Autowired
    private transient NotificationService notificationService;

    @Autowired
    private transient SecurityUtils securityUtils;

    @Autowired
    private transient NoticeService noticeService;

    @Autowired
    private transient PropertyTaxCommonUtils propertyTaxCommonUtils;

    @Autowired
    private transient ReportViewerUtil reportViewerUtil;

    @Autowired
    private transient PropertyTaxUtil propertyTaxUtil;

    @Autowired
    private transient ReassignService reassignmentservice;

    @Autowired
    private transient MutationEventPublisher mutationEventPublisher;

    @Autowired
    private transient CityService cityService;

    @Autowired
    private transient PositionMasterService positionMasterService;

    @PersistenceContext
    private transient EntityManager entityManager;
    
    @Autowired
    private transient PropertyThirdPartyService propertyThirdPartyService;
    
    @Autowired
    private ThirdPartyService thirdPartyService;

    // Model and View data
    private Long mutationId;
    private String assessmentNo;
    private String wfErrorMsg;
    private BigDecimal currentPropertyTax;
    private String propertyOwner;
    private String houseNo;
    private BigDecimal currentPropertyTaxFirstHalf;
    private BigDecimal currentPropertyTaxSecondHalf;
    private BigDecimal currentPropertyTaxDue;
    private BigDecimal currentWaterTaxDue;
    private BigDecimal arrearPropertyTaxDue;
    private List<DocumentType> documentTypes = new ArrayList<>();
    private List<DocumentType> successionDocs = new ArrayList<>();
    private BasicProperty basicproperty; // Do not change variable name, struts2
    // crazy.
    private String reportId;
    private Long transfereeId;
    private double marketValue;
    private String transferReason;
    private String collectXML;
    private String applicationNo;
    private String ackMessage;
    private String mode;
    private String mutationInitiatedBy;
    private String assessmentNoMessage;
    private String taxDueErrorMsg;
    private Boolean propertyByEmployee = Boolean.TRUE;
    private String userDesignationList;
    private Boolean loggedUserIsMeesevaUser = Boolean.FALSE;
    private String meesevaApplicationNumber;
    private String meesevaServiceCode;
    private String applicationType;
    private String fileStoreIds;
    private String ulbCode;
    private boolean enableApproverDetails = Boolean.FALSE;
    private boolean initiatorIsActive = true;
    private Map<String, String> guardianRelationMap;
    private transient List<HashMap<String, Object>> historyMap = new ArrayList<>();
    private String actionType;
    private boolean digitalSignEnabled;
    private boolean mutationFeePaid = Boolean.FALSE;
    private boolean receiptCanceled = Boolean.FALSE;
    private boolean allowEditDocument = Boolean.FALSE;
    private String applicationSource;
    private Boolean citizenPortalUser = Boolean.FALSE;
    private Boolean showAckBtn = Boolean.FALSE;
    private Boolean isReassignEnabled = Boolean.FALSE;
    private Long stateAwareId;
    private String transactionType;
    private Boolean showPayBtn = Boolean.FALSE;
    private String ownersName;
    private transient List<PtNotice> endorsementNotices;
    private Boolean endorsementRequired = Boolean.FALSE;
    private String assessmentNumber;
    private String applicationNumber;
    private String oldTransferReason;
    private List<Document> successionDocuments = new ArrayList<>();
    private List<Document> otherDocuments = new ArrayList<>();
    private String taxDueOrStruc;
    private String transactionId;
    protected transient boolean wsPortalRequest;

    public PropertyTransferAction() {
        addRelatedEntity("mutationReason", PropertyMutationMaster.class);
    }

    @SkipValidation
    @Action(value = "/redirect")
    public String redirect() {
        return REDIRECT;
    }

    @SkipValidation
    @Action(value = "/new")
    public String showNewTransferForm() {
    	final HttpServletRequest request = ServletActionContext.getRequest();
    	if (request.getParameter(WARDSECRETARY_WSPORTAL_REQUEST) != null)
            wsPortalRequest = Boolean.valueOf(request.getParameter(WARDSECRETARY_WSPORTAL_REQUEST));
    	if (!thirdPartyService.isValidWardSecretaryRequest(wsPortalRequest)) {
            addActionMessage(getText("WS.002"));
            return ERROR;
        }
        if (basicproperty.getProperty().getStatus().equals(STATUS_DEMAND_INACTIVE)) {
            addActionError(getText("error.msg.demandInactive"));
            return COMMON_FORM;
        }
        if (basicproperty.isUnderWorkflow()) {
            setFormProperties();
            if (TAXDUE.equals(taxDueOrStruc))
                return REJECT_ON_TAXDUE;
            if (STRUCTURED.equals(taxDueOrStruc))
                return COMMON_FORM;
            if (isInAddAltDemolitionFlow() && propertyService.isLatestPropertyMutationClosed(basicproperty.getUpicNo())) {
                if (isEligibleLoggedUser()) {
                    loggedUserIsMeesevaUser = propertyService.isMeesevaUser(transferOwnerService.getLoggedInUser());
                    if (loggedUserIsMeesevaUser) {
                        if (getMeesevaApplicationNumber() == null) {
                            addActionMessage(getText("MEESEVA.005"));
                            return ERROR;
                        } else
                            propertyMutation.setMeesevaApplicationNumber(getMeesevaApplicationNumber());
                    } else if (!thirdPartyService.isWardSecretaryRequest(wsPortalRequest) && (request.getParameter(WARDSECRETARY_TRANSACTIONID_CODE) == null
                            || request.getParameter("applicationSource") == null)) {
                        addActionMessage(getText("WS.001"));
                        return ERROR;
                    }
                } else {
                    addActionError(getText("initiator.noteligible"));
                    return COMMON_FORM;
                }
                return NEW;
            }
            final List<String> msgParams = new ArrayList<>();
            msgParams.add("Transfer of Ownership");
            wfErrorMsg = getText("wf.pending.msg", msgParams);
            return TARGET_WORKFLOW_ERROR;
        } else {
            final PropertyTaxUtil propertyTaxUtil = new PropertyTaxUtil();
            propertyTaxUtil.setPersistenceService(persistenceService);
            final boolean hasChildPropertyUnderWorkflow = propertyTaxUtil
                    .checkForParentUsedInBifurcation(basicproperty.getUpicNo());
            if (hasChildPropertyUnderWorkflow) {
                wfErrorMsg = getText("error.msg.child.underworkflow");
                return TARGET_WORKFLOW_ERROR;
            }
            if (isUnderWtmsWF()) {
                wfErrorMsg = getText("msg.under.wtms.wf.ttp");
                return TARGET_WORKFLOW_ERROR;
            }

            setFormProperties();
            if (TAXDUE.equals(taxDueOrStruc))
                return REJECT_ON_TAXDUE;
            if (STRUCTURED.equals(taxDueOrStruc))
                return COMMON_FORM;

            if (!ADDITIONAL_RULE_FULL_TRANSFER.equalsIgnoreCase(propertyMutation.getType())) {
                if (isEligibleLoggedUser()) {
                    loggedUserIsMeesevaUser = propertyService.isMeesevaUser(transferOwnerService.getLoggedInUser());
                    if (loggedUserIsMeesevaUser)
                        if (getMeesevaApplicationNumber() == null) {
                            addActionMessage(getText("MEESEVA.005"));
                            return ERROR;
                        } else
                            propertyMutation.setMeesevaApplicationNumber(getMeesevaApplicationNumber());
                    return NEW;
                } else {
                    addActionError(getText("initiator.noteligible"));
                    return COMMON_FORM;
                }
            } else
                return NEW;
        }
    }

    public boolean isInAddAltDemolitionFlow() {
        boolean isAddAltDem;
        if (basicproperty.getWFProperty() != null) {
            isAddAltDem = Arrays.asList(PROPERTY_MODIFY_REASON_ADD_OR_ALTER, DEMOLITION)
                    .contains(basicproperty.getWFProperty().getPropertyModifyReason());
        } else {
            isAddAltDem = Arrays.asList(PROPERTY_MODIFY_REASON_ADD_OR_ALTER, DEMOLITION)
                    .contains(basicproperty.getProperty().getPropertyModifyReason());
        }
        return isAddAltDem;
    }

    public boolean isEligibleLoggedUser() {
        return StringUtils.isBlank(applicationSource)
                && propertyService.isEmployee(transferOwnerService.getLoggedInUser())
                && !propertyTaxCommonUtils.isEligibleInitiator(transferOwnerService.getLoggedInUser().getId())
                && !propertyService.isCitizenPortalUser(transferOwnerService.getLoggedInUser()) ? false : true;
    }

    public void setFormProperties() {
        final Map<String, BigDecimal> propertyTaxDetails = propertyService
                .getCurrentPropertyTaxDetailsIncludingPenalty(basicproperty.getActiveProperty());
        final Map<String, BigDecimal> currentTaxAndDue = propertyService
                .getCurrentTaxAndBalanceIncludingPenalty(propertyTaxDetails, new Date());
        currentPropertyTax = currentTaxAndDue.get(CURR_DMD_STR);
        propertyOwner = basicproperty.getFullOwnerName();
        houseNo = basicproperty.getAddress().getHouseNoBldgApt();
        currentPropertyTaxDue = currentTaxAndDue.get(CURR_BAL_STR);
        arrearPropertyTaxDue = propertyTaxDetails.get(ARR_DMD_STR).subtract(propertyTaxDetails.get(ARR_COLL_STR));
        currentWaterTaxDue = getWaterTaxDues();
        checkForMandatoryDocuments();
        if (currentWaterTaxDue.add(currentPropertyTaxDue).add(arrearPropertyTaxDue).longValue() > 0) {
            setTaxDueErrorMsg(getText("taxdues.error.msg", new String[] { PROPERTY_TRANSFER }));
            taxDueOrStruc = TAXDUE;
        }
        if (basicproperty.getActiveProperty() != null && basicproperty.getActiveProperty().getPropertyDetail().isStructure()) {
            addActionError(getText("error.superstruc.prop.notallowed"));
            taxDueOrStruc = STRUCTURED;
        }
    }

    @ValidationErrorPage(value = NEW)
    @Action(value = "/save")
    public String save() {
        if (propertyTaxCommonUtils.isUnderMutationWorkflow(basicproperty)) {
            final List<String> msgParams = new ArrayList<>();
            msgParams.add("Transfer of Ownership");
            wfErrorMsg = getText("wf.pending.msg", msgParams);
            return TARGET_WORKFLOW_ERROR;
        }
        if (!thirdPartyService.isWardSecretaryRequest(wsPortalRequest) && (transactionId == null || applicationSource == null)) {
            addActionError("TransactionId and source is required.");
            return NEW;
        }
        transitionWorkFlow(propertyMutation);
        propertyMutation.setSource(propertyTaxCommonUtils.setSourceOfProperty(securityUtils.getCurrentUser(),
                ANONYMOUS_USER.equalsIgnoreCase(securityUtils.getCurrentUser().getName())));
        if (getNatureOfTask().equalsIgnoreCase(ADDITIONAL_RULE_FULL_TRANSFER)) {
            propertyMutation.setMutationReason(transferOwnerService.getPropertyTransferReasonsByCode(MUTATION_REASON_CODE_SALE));
        }
        loggedUserIsMeesevaUser = propertyService.isMeesevaUser(transferOwnerService.getLoggedInUser());
        if (!loggedUserIsMeesevaUser) {
            final Position position = propertyMutation.getState().getOwnerPosition() == null
                    ? propertyMutation.getState().getPreviousOwner() : propertyMutation.getState().getOwnerPosition();
            if (position != null && assignmentService.getAssignmentsForPosition(position.getId(), new Date()).isEmpty()) {
                addActionError(getText(POSITION_EXPIRED));
                return NEW;
            }
            transferOwnerService.initiatePropertyTransfer(basicproperty, propertyMutation,ServletActionContext.getRequest(),transactionId);
        } else {
            final HashMap<String, String> meesevaParams = new HashMap<>();
            meesevaParams.put("APPLICATIONNUMBER", propertyMutation.getMeesevaApplicationNumber());
            propertyMutation.setApplicationNo(propertyMutation.getMeesevaApplicationNumber());
            transferOwnerService.initiatePropertyTransfer(basicproperty, propertyMutation, meesevaParams);
        }

        buildSMS(propertyMutation);
        buildEmail(propertyMutation);
        mutationEventPublisher.publishEvent(propertyMutation, false);
        setAckMessage("Transfer of ownership data saved successfully in the system and forwarded to : ");
        setAssessmentNoMessage(WITH_ASSESSMENT_NUMBER);

        if (ADDITIONAL_RULE_FULL_TRANSFER.equalsIgnoreCase(propertyMutation.getType())) {
            propertyOwner = basicproperty.getFullOwnerName();
            return ACK_FOR_REGISTRATION;
        } else if (!loggedUserIsMeesevaUser)
            return ACK;
        else
            return MEESEVA_RESULT_ACK;
    }

    @SkipValidation
    @Action(value = "/redirectForPayment")
    public String redirectForPayment() {
        assessmentNo = basicproperty.getUpicNo();
        propertyOwner = basicproperty.getFullOwnerName();
        return ACK_FOR_REGISTRATION;
    }

    @SkipValidation
    @Action(value = "/view")
    public String view() {
        final String currState = propertyMutation.getState().getValue();
        final String nextAction = propertyMutation.getState().getNextAction();
        propertyMutation.getTransfereeInfosProxy().addAll(propertyMutation.getTransfereeInfos());
        isReassignEnabled = reassignmentservice.isReassignEnabled();
        stateAwareId = propertyMutation.getId();
        transactionType = APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP;
        if (!propertyMutation.getDocuments().isEmpty())
            propertyMutation.setDocumentsProxy(propertyMutation.getDocuments());
        if (propertyMutation.getMutationReason() != null
                && propertyMutation.getMutationReason().getMutationName().equals(MUTATIONRS_SUCCESSION))
            setSuccessionDocuments(propertyMutation.getDocumentsProxy());
        else
            setOtherDocuments(propertyMutation.getDocumentsProxy());
        if (propertyMutation.getState() != null) {
            ownersName = propertyMutation.getBasicProperty().getFullOwnerName();
            applicationNumber = propertyMutation.getApplicationNo();
            endorsementNotices = propertyTaxCommonUtils.getEndorsementNotices(applicationNumber);
            endorsementRequired = propertyTaxCommonUtils.getEndorsementGenerate(securityUtils.getCurrentUser().getId(),
                    propertyMutation.getCurrentState());
            assessmentNumber = propertyMutation.getBasicProperty().getUpicNo();
        }
        if (currState.endsWith(WF_STATE_REJECTED)
                || nextAction != null && nextAction.equalsIgnoreCase(WF_STATE_UD_REVENUE_INSPECTOR_APPROVAL_PENDING)
                || currState.equals(WFLOW_ACTION_NEW)) {
            setAllowEditDocument(Boolean.TRUE);
            mode = EDIT;
        } else
            mode = VIEW;
        return mode;
    }

    @SkipValidation
    @Action(value = "/search")
    public String search() {
        return SEARCH;
    }

    @SkipValidation
    @Action(value = "/collect-fee")
    public String collectFee() {
        String target = "";
        if (StringUtils.isNotBlank(assessmentNo))
            propertyMutation = transferOwnerService.getCurrentPropertyMutationByAssessmentNo(assessmentNo);
        else if (StringUtils.isNotBlank(applicationNo)) {
            propertyMutation = transferOwnerService.getPropertyMutationByApplicationNo(applicationNo);
            if (propertyMutation != null && propertyMutation.getState().getValue().equals(WF_STATE_CLOSED)) {
                addActionError(getText("error.cancelled.applicationno"));
                target = SEARCH;
            }
        } else {
            addActionError(getText("mandatory.assessmentno.applicationno"));
            target = SEARCH;
        }
        if (propertyMutation == null || propertyMutation.getId() == null) {
            addActionError(getText("mutation.notexists"));
            target = SEARCH;
        } else if (propertyMutation.getReceiptDate() != null
                && !propertyTaxCommonUtils.isReceiptCanceled(propertyMutation.getReceiptNum())) {
            final SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            addActionError(getText("mutationpayment.done",
                    new String[] { df.format(propertyMutation.getReceiptDate()) }));
            target = SEARCH;
        } else if (propertyMutation.getMutationFee() == null) {
            addActionError(getText("mutationfee.notexists"));
            target = SEARCH;
        } else {
            if (ANONYMOUS_USER.equalsIgnoreCase(securityUtils.getCurrentUser().getName()) || citizenPortalUser) {
                target = COLLECT_ONLINE_FEE;
            }
            collectXML = transferOwnerService.generateReceipt(propertyMutation);
            if (StringUtils.isBlank(target))
                target = COLLECT_FEE;
        }
        return target;
    }

    @SkipValidation
    @Action(value = "/forward")
    public String forward() {
        if (mode.equals(EDIT)) {
            validate();
            if (hasErrors()) {
                setAllowEditDocument(Boolean.TRUE);
                mode = EDIT;
                return EDIT;
            }
            transitionWorkFlow(propertyMutation);
            transferOwnerService.updatePropertyTransfer(basicproperty, propertyMutation, oldTransferReason);
        } else {
            transitionWorkFlow(propertyMutation);
            transferOwnerService.viewPropertyTransfer(basicproperty, propertyMutation);
        }
        buildSMS(propertyMutation);
        buildEmail(propertyMutation);
        setAssessmentNoMessage(WITH_ASSESSMENT_NUMBER);
        return ACK;
    }

    @SkipValidation
    @Action(value = "/reject")
    public String reject() {
        if (isRejectionNotAllowed()) {
            addActionError(getText("error.mutation.reject.notallowed"));
            if (propertyMutation.getType().equalsIgnoreCase(ADDITIONAL_RULE_FULL_TRANSFER))
                return VIEW;
            else
                return EDIT;
        }
        if (transferOwnerService.getWorkflowInitiator(propertyMutation) == null) {
            addActionError(getText("reject.error.initiator.inactive", Arrays.asList(ASSISTANT_DESGN)));
            return VIEW;
        }

        if (mutationId != null) {
            final javax.persistence.Query qry = entityManager.createNamedQuery("BY_MUTATION_ID");
            qry.setParameter("id", mutationId);
            propertyMutation = (PropertyMutation) qry.getSingleResult();
            persistenceService.getSession().refresh(propertyMutation);
        }

        Assignment wfInitiator = null;
        String loggedInUserDesignation = "";
        List<Assignment> loggedInUserAssign;
        final User user = transferOwnerService.getLoggedInUser();
        if (propertyMutation.getState() != null) {
            loggedInUserAssign = assignmentService.getAssignmentByPositionAndUserAsOnDate(
                    propertyMutation.getCurrentState().getOwnerPosition().getId(), user.getId(), new Date());
            loggedInUserDesignation = !loggedInUserAssign.isEmpty() ? loggedInUserAssign.get(0).getDesignation().getName() : null;
        }
        if (propertyTaxCommonUtils.isRoOrCommissioner(loggedInUserDesignation)) {
            final Assignment assignmentOnreject = propertyService.getUserOnRejection(propertyMutation);
            wfInitiator = assignmentOnreject;
        } else if (BILL_COLLECTOR_DESGN.equalsIgnoreCase(loggedInUserDesignation)
                || TAX_COLLECTOR_DESGN.equalsIgnoreCase(loggedInUserDesignation)
                || REVENUE_INSPECTOR_DESGN.equalsIgnoreCase(loggedInUserDesignation))
            wfInitiator = transferOwnerService.getWorkflowInitiator(propertyMutation);
        if (propertyMutation.getType().equals(ADDITIONAL_RULE_FULL_TRANSFER)
                || (wfInitiator != null || JUNIOR_ASSISTANT.equalsIgnoreCase(loggedInUserDesignation)
                        || SENIOR_ASSISTANT.equalsIgnoreCase(loggedInUserDesignation))) {
            transitionWorkFlow(propertyMutation);
            transferOwnerService.viewPropertyTransfer(basicproperty, propertyMutation);
            
            if(Source.WARDSECRETARY.toString().equalsIgnoreCase(propertyMutation.getSource())){
                
                String remarks = "Closed".equalsIgnoreCase(propertyMutation.getState().getValue()) ? "Property Transfer Cancelled"
                        : "Property Transfer Rejected";
                propertyThirdPartyService.publishUpdateEvent(propertyMutation.getApplicationNo(), WFLOW_ACTION_STEP_REJECT, remarks);
            }
            buildEmail(propertyMutation);
            approverName = "";
            List<Assignment> assignment;
            if (WF_STATE_CLOSED.equals(propertyMutation.getState().getValue())) {
                mutationEventPublisher.publishEvent(propertyMutation, true);
                final List<StateHistory<Position>> history = propertyMutation.getStateHistory();
                Collections.reverse(history);
                assignment = assignmentService.getAssignmentByPositionAndUserAsOnDate(history.get(0).getOwnerPosition().getId(),
                        transferOwnerService.getLoggedInUser().getId(), new Date());
                mutationInitiatedBy = transferOwnerService.getLoggedInUser().getName().concat("~")
                        .concat(assignment.get(0).getPosition().getName());
                setAckMessage("Transfer of ownership data rejected successfuly By ");
            } else
                setAckMessage("Transfer of ownership data rejected successfuly and forwarded to : ");
            setAssessmentNoMessage(WITH_ASSESSMENT_NUMBER);
        } else
            setAckMessage(getText(PROPERTY_MODIFY_REJECT_FAILURE));
        return ACK;
    }

    private boolean isRejectionNotAllowed() {
        return ("Rejected".equals(propertyMutation.getState().getValue())
                || propertyMutation.getType().equalsIgnoreCase(ADDITIONAL_RULE_FULL_TRANSFER))
                && propertyMutation.getReceiptNum() != null && !receiptCanceled;
    }

    @ValidationErrorPage(value = EDIT)
    @Action(value = "/approve")
    public String approve() {
        transitionWorkFlow(propertyMutation);
        transferOwnerService.approvePropertyTransfer(basicproperty, propertyMutation);
        transferOwnerService.viewPropertyTransfer(basicproperty, propertyMutation);
        approverName = "";
        final List<StateHistory<Position>> history = propertyMutation.getStateHistory();
        Collections.reverse(history);
        final List<Assignment> assignment = assignmentService.getAssignmentByPositionAndUserAsOnDate(
                history.get(0).getOwnerPosition().getId(), securityUtils.getCurrentUser().getId(),
                new Date());
        mutationInitiatedBy = !assignment.isEmpty()
                ? assignment.get(0).getEmployee().getName().concat("~").concat(assignment.get(0).getPosition().getName()) : "";
        final String clientSpecificDmdBill = propertyTaxCommonUtils
                .getAppConfigValue(APPCONFIG_CLIENT_SPECIFIC_DMD_BILL, PTMODULENAME);
        if ("Y".equalsIgnoreCase(clientSpecificDmdBill))
            propertyTaxCommonUtils.makeExistingDemandBillInactive(basicproperty.getUpicNo());
        else
            propertyTaxUtil.makeTheEgBillAsHistory(basicproperty);
        buildSMS(propertyMutation);
        buildEmail(propertyMutation);
        mutationEventPublisher.publishEvent(propertyMutation, false);
        if (Source.WARDSECRETARY.toString().equalsIgnoreCase(propertyMutation.getSource())) {
            propertyThirdPartyService.publishUpdateEvent(propertyMutation.getApplicationNo(), WFLOW_ACTION_STEP_REJECT,
                    "Property Transfer Approved");
        }
        setAckMessage("Transfer of ownership is created successfully in the system and forwarded to : ");
        setAssessmentNoMessage(" for Digital Signature for the property : ");
        return ACK;
    }

    @SkipValidation
    @Action(value = "/printAck")
    public String printAck() {
        if (ANONYMOUS_USER.equalsIgnoreCase(securityUtils.getCurrentUser().getName())
                && ApplicationThreadLocals.getUserId() == null) {
            ApplicationThreadLocals.setUserId(securityUtils.getCurrentUser().getId());
            setApplicationSource(SOURCE_ONLINE.toLowerCase());
        }
        reportId = reportViewerUtil.addReportToTempCache(
                transferOwnerService.generateAcknowledgement(basicproperty, propertyMutation));
        return PRINTACK;
    }

    @SkipValidation
    @Action(value = "/printNotice")
    public String printNotice() {
        setUlbCode(ApplicationThreadLocals.getCityCode());
        final String cityGrade = cityService.getCityGrade();
        Boolean isCorporation;
        if (cityGrade != null && cityGrade != ""
                && cityGrade.equalsIgnoreCase(CITY_GRADE_CORPORATION))
            isCorporation = true;
        else
            isCorporation = false;

        final ReportOutput reportOutput = transferOwnerService.generateTransferNotice(basicproperty, propertyMutation, actionType,
                isCorporation);
        if (!WFLOW_ACTION_STEP_SIGN.equalsIgnoreCase(actionType))
            reportId = reportViewerUtil.addReportToTempCache(reportOutput);
        else {
            final PtNotice notice = noticeService.getNoticeByNoticeTypeAndApplicationNumber(NOTICE_TYPE_MUTATION_CERTIFICATE,
                    propertyMutation.getApplicationNo());
            setFileStoreIds(notice.getFileStore().getFileStoreId());
            return DIGITAL_SIGNATURE_REDIRECTION;
        }
        return PRINTNOTICE;
    }

    @SkipValidation
    @Action(value = "/delete-transferee")
    public void deleteTransferee() throws IOException {
        if (transfereeId != null) {
            transferOwnerService.deleteTransferee(propertyMutation, transfereeId);
            ServletActionContext.getResponse().getWriter().write("true");
        } else
            ServletActionContext.getResponse().getWriter().write("false");
    }

    @SkipValidation
    @Action(value = "/calculate-mutationfee")
    public void calculateMutationFee() throws IOException {
        if (marketValue > 0)
            ServletActionContext
                    .getResponse()
                    .getWriter()
                    .write(String.valueOf(transferOwnerService.calculateMutationFee(marketValue, transferReason,
                            propertyMutation)));
        else
            ServletActionContext.getResponse().getWriter().write("0");
    }

    @SkipValidation
    @Action(value = "/redirect-success")
    public String redirectSuccess() {
        getAckMessage();
        return ACK;
    }

    @Override
    public void prepare() {
        final Long userId = securityUtils.getCurrentUser().getId();
        userDesignationList = propertyTaxCommonUtils.getAllDesignationsForUser(userId);
        propertyByEmployee = propertyService.isEmployee(transferOwnerService.getLoggedInUser());
        citizenPortalUser = propertyService.isCitizenPortalUser(securityUtils.getCurrentUser());
        final String actionInvoked = ActionContext.getContext().getActionInvocation().getProxy().getMethod();
        if (!(actionInvoked.equals(SEARCH) || actionInvoked.equals("collectFee"))) {
            if (StringUtils.isNotBlank(assessmentNo) && mutationId == null)
                basicproperty = transferOwnerService.getBasicPropertyByUpicNo(assessmentNo);
            if (mutationId != null) {
                final javax.persistence.Query qry = entityManager.createNamedQuery("BY_MUTATION_ID");
                qry.setParameter("id", mutationId);
                propertyMutation = (PropertyMutation) qry.getSingleResult();
                basicproperty = propertyMutation.getBasicProperty();
                historyMap = propertyService.populateHistory(propertyMutation);
            }
            super.prepare();
            final Map<String, BigDecimal> propertyTaxDetails = propertyService
                    .getCurrentPropertyTaxDetails(basicproperty.getActiveProperty());

            final Map<String, BigDecimal> currentTaxAndDue = propertyService.getCurrentTaxAndBalance(propertyTaxDetails,
                    new Date());
            currentPropertyTax = currentTaxAndDue.get(CURR_DMD_STR);
            currentPropertyTaxFirstHalf = propertyTaxDetails.get(CURR_FIRSTHALF_DMD_STR);
            currentPropertyTaxSecondHalf = propertyTaxDetails.get(CURR_SECONDHALF_DMD_STR);
            documentTypes = transferOwnerService.getPropertyTransferDocumentTypes();
            successionDocs = transferOwnerService.getSuccessionDouments();
            addDropdownData("MutationReason", transferOwnerService.getPropertyTransferReasons());
            if (propertyMutation.getReceiptNum() != null) {
                final boolean isCanceled = propertyTaxCommonUtils.isReceiptCanceled(propertyMutation.getReceiptNum());
                setReceiptCanceled(isCanceled);
                if (isCanceled)
                    setMutationFeePaid(Boolean.FALSE);
                else
                    setMutationFeePaid(Boolean.TRUE);
            }
        }
        digitalSignEnabled = propertyTaxCommonUtils.isDigitalSignatureEnabled();
        if (SecurityUtils.userAnonymouslyAuthenticated() || citizenPortalUser) {
            showPayBtn = Boolean.TRUE;
        }
    }

    private void checkForMandatoryDocuments() {
        if (!MUTATION_TYPE_REGISTERED_TRANSFER.equalsIgnoreCase(propertyMutation.getType())) {
            for (DocumentType docType : documentTypes)
                docType.setMandatory(false);
        }
    }

    @Override
    public void validate() {
        if (MUTATION_TYPE_REGISTERED_TRANSFER.equalsIgnoreCase(propertyMutation.getType())) {
            if (propertyMutation.getMutationReason() == null || propertyMutation.getMutationReason().getId() == -1)
                addActionError(getText("mandatory.trRsnId"));
            else if (MUTATIONRS_DECREE_BY_CIVIL_COURT
                    .equals(propertyMutation.getMutationReason().getMutationName())) {
                validateDecreeDetails();
            }
            if (!Arrays.asList(MUTATIONRS_DECREE_BY_CIVIL_COURT, MUTATIONRS_UNREG_WILL)
                    .contains(propertyMutation.getMutationReason().getMutationName())) {
                if (propertyMutation.getDeedDate() == null)
                    addActionError("Registration Document Date should not be empty");
                if (StringUtils.isBlank(propertyMutation.getDeedNo()))
                    addActionError("Registration Document Number should not be empty");
            }
        }
        if (propertyMutation.getPartyValue() == null)
            addActionError(getText("mandatory.party.value"));
        if (propertyMutation.getDepartmentValue() == null)
            addActionError(getText("mandatory.department.value"));
        if (propertyMutation.getState() != null && propertyMutation.getState().getValue() != null
                && (propertyMutation.getState().getValue().equalsIgnoreCase(WF_STATE_REVENUE_OFFICER_APPROVED)
                        || propertyMutation.getState().getValue().equalsIgnoreCase(WF_STATE_REGISTRATION_COMPLETED)
                        || propertyMutation.getState().getNextAction().toLowerCase()
                                .endsWith(WF_STATE_COMMISSIONER_APPROVAL_PENDING.toLowerCase())))
            propertyMutation.getTransfereeInfosProxy().addAll(propertyMutation.getTransfereeInfos());

        if (propertyMutation.getTransfereeInfosProxy().isEmpty())
            addActionError("Transfree info is mandatory, add atleast one transferee info.");
        else {
            for (final PropertyMutationTransferee propOwnerInfo : propertyMutation.getTransfereeInfosProxy()) {
                if (StringUtils.isBlank(propOwnerInfo.getTransferee().getName()))
                    addActionError(getText("mandatory.ownerName"));
                if (StringUtils.isBlank(propOwnerInfo.getTransferee().getMobileNumber()))
                    addActionError(getText("mandatory.mobilenumber"));
                if (propOwnerInfo.getTransferee().getGender() == null)
                    addActionError(getText("mandatory.gender"));
                if (StringUtils.isBlank(propOwnerInfo.getTransferee().getGuardianRelation()))
                    addActionError(getText("mandatory.guardianrelation"));
                if (StringUtils.isBlank(propOwnerInfo.getTransferee().getGuardian()))
                    addActionError(getText("mandatory.guardian"));
            }

            final int count = propertyMutation.getTransfereeInfosProxy().size();
            for (int i = 0; i < count; i++) {
                final PropertyMutationTransferee owner = propertyMutation.getTransfereeInfosProxy().get(i);
                for (int j = i + 1; j <= count - 1; j++) {
                    final PropertyMutationTransferee owner1 = propertyMutation.getTransfereeInfosProxy().get(j);
                    if (owner.getTransferee().getMobileNumber()
                            .equalsIgnoreCase(owner1.getTransferee().getMobileNumber())
                            && owner.getTransferee().getName().equalsIgnoreCase(owner1.getTransferee().getName()))
                        addActionError(getText("error.transferee.duplicateMobileNo", "", owner.getTransferee()
                                .getMobileNumber().concat(",").concat(owner.getTransferee().getName())));
                }
            }
        }

        if ((loggedUserIsMeesevaUser || !propertyByEmployee) && null != basicproperty) {
            final Assignment assignment = propertyService.isCscOperator(securityUtils.getCurrentUser())
                    ? propertyService.getAssignmentByDeptDesigElecWard(basicproperty)
                    : null;
            if (assignment == null && propertyService.getUserPositionByZone(basicproperty, false) == null)
                addActionError(getText("notexists.position"));
        }
       super.validate();
    }

    private void validateDecreeDetails() {
        if (StringUtils.isBlank(propertyMutation.getDecreeNumber()))
            addActionError(getText("mandatory.decreeNum"));
        if (propertyMutation.getDecreeDate() == null)
            addActionError(getText("mandatory.decreeDate"));
        if (StringUtils.isBlank(propertyMutation.getCourtName()))
            addActionError(getText("mandatory.courtname"));
    }

    public void transitionWorkFlow(final PropertyMutation propertyMutation) {
        final DateTime currentDate = new DateTime();
        final User user = transferOwnerService.getLoggedInUser();
        Position pos;
        Assignment wfInitiator = null;
        String nextAction = "";
        Assignment assignment;

		if (citizenPortalUser || !propertyByEmployee
				|| ANONYMOUS_USER.equalsIgnoreCase(transferOwnerService.getLoggedInUser().getName())) {
			currentState = getCurrentStateForThirdPartyWF(propertyMutation);
			assignment = transferOwnerService.getAssignmentForThirdPartyByMutationType(propertyMutation, basicproperty,
					user,wsPortalRequest);
			if (assignment == null && propertyMutation.getType().equalsIgnoreCase(ADDITIONAL_RULE_FULL_TRANSFER)){
				 checkForMandatoryDocuments();
			throw new ValidationException(
					Arrays.asList(new ValidationError(POSITION_EXPIRED,
							POSITION_EXPIRED)));
			}else {
				approverPositionId = assignment.getPosition().getId();
				approverName = assignment.getEmployee().getName().concat("~")
						.concat(assignment.getPosition().getName());
				wfInitiator = setInitiatorForThirdPartyByMutationType(propertyMutation, assignment);
				approverDesignation = assignment.getDesignation().getName();
			}
		} else {
			if (null != approverPositionId && approverPositionId != -1) {
				assignment = assignmentService.getAssignmentsForPosition(approverPositionId, new Date()).get(0);
				approverDesignation = assignment.getDesignation().getName();
			}
			currentState = null;
		}

        List<Assignment> loggedInUserAssign;
        String loggedInUserDesignation = "";
        if (propertyMutation.getState() != null) {
            loggedInUserAssign = assignmentService.getAssignmentByPositionAndUserAsOnDate(
                    propertyMutation.getCurrentState().getOwnerPosition().getId(), user.getId(), new Date());
            loggedInUserDesignation = !loggedInUserAssign.isEmpty() ? loggedInUserAssign.get(0).getDesignation().getName() : null;
        }

        if (loggedInUserDesignation.equals(JUNIOR_ASSISTANT) || loggedInUserDesignation.equals(SENIOR_ASSISTANT))
            loggedInUserDesignation = null;

        if (WFLOW_ACTION_STEP_FORWARD.equalsIgnoreCase(workFlowAction)
                && getNatureOfTask().equalsIgnoreCase(ADDTIONAL_RULE_REGISTERED_TRANSFER)
                && COMMISSIONER_DESIGNATIONS.contains(approverDesignation))
            if (propertyMutation.getCurrentState().getNextAction().equalsIgnoreCase(WF_STATE_DIGITAL_SIGNATURE_PENDING))
                nextAction = WF_STATE_DIGITAL_SIGNATURE_PENDING;
            else {
                final String designation = approverDesignation.split(" ")[0];
                if (designation.equalsIgnoreCase(COMMISSIONER_DESGN))
                    nextAction = WF_STATE_COMMISSIONER_APPROVAL_PENDING;
                else
                    nextAction = new StringBuilder().append(designation).append(" ")
                            .append(WF_STATE_COMMISSIONER_APPROVAL_PENDING)
                            .toString();
            }

        if (propertyMutation.getId() != null)
            wfInitiator = transferOwnerService.getWorkflowInitiator(propertyMutation);
        if (WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction)) {
            if (wfInitiator.getPosition().equals(propertyMutation.getState().getOwnerPosition())
                    || propertyMutation.getType().equalsIgnoreCase(ADDITIONAL_RULE_FULL_TRANSFER)) {
                propertyMutation.transition().end().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approverComments).withDateInfo(currentDate.toDate()).withNextAction(null)
                        .withOwner(propertyMutation.getCurrentState().getOwnerPosition());
                propertyMutation.getBasicProperty().setUnderWorkflow(Boolean.FALSE);
            } else {
                if (loggedInUserDesignation.equalsIgnoreCase(REVENUE_OFFICER_DESGN)
                        || COMMISSIONER_DESIGNATIONS.contains(loggedInUserDesignation)) {
                    nextAction = UD_REVENUE_INSPECTOR_APPROVAL_PENDING;
                    final Assignment assignmentOnreject = propertyService.getUserOnRejection(propertyMutation);
                    wfInitiator = assignmentOnreject;
                    setMutationInitiatedBy(assignmentOnreject.getEmployee().getName().concat("~")
                            .concat(assignmentOnreject.getPosition().getName()));
                } else if (BILL_COLLECTOR_DESGN.equalsIgnoreCase(loggedInUserDesignation)
                        || TAX_COLLECTOR_DESGN.equalsIgnoreCase(loggedInUserDesignation)
                        || REVENUE_INSPECTOR_DESGN.equalsIgnoreCase(loggedInUserDesignation)) {
                    nextAction = WF_STATE_ASSISTANT_APPROVAL_PENDING;
                    setMutationInitiatedBy(wfInitiator.getEmployee().getName().concat("~")
                            .concat(wfInitiator.getPosition().getName()));
                }
                final String stateValue = WF_STATE_REJECTED;
                propertyMutation.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approverComments).withStateValue(stateValue).withDateInfo(currentDate.toDate())
                        .withOwner(wfInitiator != null ? wfInitiator.getPosition() : null)
                        .withNextAction(nextAction);
            }
        } 
        else if(WFLOW_ACTION_STEP_REJECT_TO_CANCEL.equalsIgnoreCase(workFlowAction))
            wFRejectToCancel(propertyMutation);
        else {
            if (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workFlowAction))
                pos = propertyMutation.getCurrentState().getOwnerPosition();
            else if (null != approverPositionId && approverPositionId != -1)
                pos = positionMasterService.getPositionById(approverPositionId);
            else
                pos = wfInitiator.getPosition();
            if (null == propertyMutation.getState()) {
                final WorkFlowMatrix wfmatrix = transferWorkflowService.getWfMatrix(propertyMutation.getStateType(),
                        null, null, getAdditionalRule(), currentState, null);
                propertyMutation.transition().start().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approverComments).withStateValue(wfmatrix.getNextState())
                        .withDateInfo(currentDate.toDate()).withOwner(pos).withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(getNatureOfTask())
                        .withInitiator(wfInitiator != null ? wfInitiator.getPosition() : null)
                        .withSLA(propertyService.getSlaValue(propertyMutation.getType()
                                .equalsIgnoreCase(ADDTIONAL_RULE_REGISTERED_TRANSFER)
                                        ? NATURE_REGISTERED_TRANSFER : NATURE_FULL_TRANSFER));
            } else if (propertyMutation.getCurrentState().getNextAction().equalsIgnoreCase("END"))
                propertyMutation.transition().end().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approverComments).withDateInfo(currentDate.toDate()).withNextAction(null)
                        .withOwner(propertyMutation.getCurrentState().getOwnerPosition());
            else {
                final WorkFlowMatrix wfmatrix = transferWorkflowService.getWfMatrix(propertyMutation.getStateType(), null, null,
                        getAdditionalRule(), propertyMutation.getCurrentState().getValue(),
                        propertyMutation.getCurrentState().getNextAction(), null, loggedInUserDesignation);
                propertyMutation.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approverComments).withStateValue(wfmatrix.getNextState())
                        .withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction(StringUtils.isNotBlank(nextAction) ? nextAction : wfmatrix.getNextAction());

            }
        }
        if (approverName != null && !approverName.isEmpty() && !approverName.equalsIgnoreCase("----Choose----")) {
            final String approvalmesg = " Succesfully Forwarded to : ";
            ackMessage = ackMessage == null ? approvalmesg : ackMessage + approvalmesg;
        } else if (workFlowAction != null && workFlowAction.equalsIgnoreCase("cancel")) {
            final String approvalmesg = " Succesfully Cancelled.";
            ackMessage = ackMessage == null ? approvalmesg : ackMessage + approvalmesg;
        }
        checkToDisplayAckButton();
    }

    public void buildSMS(final PropertyMutation propertyMutation) {
        final List<String> argsForTransferor = new ArrayList<>();
        final List<String> argsForTransferee = new ArrayList<>();
        String smsMsgForTransferor = "";
        String smsMsgForTransferee = "";
        if (null != propertyMutation && null != propertyMutation.getState()) {
            final State mutationState = propertyMutation.getState();
            if (mutationState.getValue().equalsIgnoreCase(WF_STATE_ASSISTANT_APPROVED)) {
                argsForTransferor.add(propertyMutation.getFullTranferorName());
                argsForTransferor.add(propertyMutation.getBasicProperty().getUpicNo());
                argsForTransferee.add(propertyMutation.getFullTranfereeName());
                argsForTransferee.add(propertyMutation.getBasicProperty().getUpicNo());
                if (propertyMutation.getPartyValue().compareTo(propertyMutation.getDepartmentValue()) > 0)
                    argsForTransferee.add(propertyMutation.getPartyValue().toString());
                else
                    argsForTransferee.add(propertyMutation.getDepartmentValue().toString());
                argsForTransferee
                        .add(propertyMutation.getMutationFee() != null ? propertyMutation.getMutationFee().toString() : "N/A");
                smsMsgForTransferor = getText("msg.createtransferpropertytransferor.sms", argsForTransferor);
                smsMsgForTransferee = getText("msg.createtransferproperty.sms", argsForTransferee);
            } else if (mutationState.getValue().equals(WF_STATE_REJECTED)) {
                argsForTransferor.add(propertyMutation.getFullTranferorName());
                argsForTransferor.add(propertyMutation.getBasicProperty().getUpicNo());
                argsForTransferor.add(transferOwnerService.getCityName());
                argsForTransferee.add(propertyMutation.getFullTranfereeName());
                argsForTransferee.add(propertyMutation.getBasicProperty().getUpicNo());
                argsForTransferee.add(transferOwnerService.getCityName());
                smsMsgForTransferor = getText("msg.rejecttransferproperty.sms", argsForTransferor);
                smsMsgForTransferee = getText("msg.rejecttransferproperty.sms", argsForTransferee);
            } else if (mutationState.getValue().equals(WF_STATE_COMMISSIONER_APPROVED)) {
                argsForTransferor.add(propertyMutation.getFullTranferorName());
                argsForTransferor.add(propertyMutation.getFullTranfereeName());
                argsForTransferor.add(propertyMutation.getBasicProperty().getUpicNo());
                argsForTransferor.add(transferOwnerService.getCityName());
                smsMsgForTransferor = getText("msg.approvetransferproperty.sms", argsForTransferor);
                argsForTransferee.add(propertyMutation.getFullTranferorName());
                argsForTransferee.add(propertyMutation.getFullTranfereeName());
                argsForTransferee.add(propertyMutation.getBasicProperty().getUpicNo());
                argsForTransferee.add(transferOwnerService.getCityName());
                smsMsgForTransferee = getText("msg.approvetransferproperty.sms", argsForTransferee);
            }
        }
        for (final User transferor : propertyMutation.getTransferorInfos())
            if (StringUtils.isNotBlank(transferor.getMobileNumber()) && StringUtils.isNotBlank(smsMsgForTransferor))
                notificationService.sendSMS(transferor.getMobileNumber(), smsMsgForTransferor);
        for (final PropertyMutationTransferee transferee : propertyMutation.getTransfereeInfos())
            if (StringUtils.isNotBlank(transferee.getTransferee().getMobileNumber())
                    && StringUtils.isNotBlank(smsMsgForTransferee))
                notificationService.sendSMS(transferee.getTransferee().getMobileNumber(), smsMsgForTransferee);
    }

    public void buildEmail(final PropertyMutation propertyMutation) {
        String emailBodyTransferor = "";
        String emailBodyTransferee = "";
        String subject = "";
        final List<String> argsForTransferor = new ArrayList<>();
        final List<String> argsForTransferee = new ArrayList<>();
        if (null != propertyMutation && null != propertyMutation.getState()) {
            final State mutationState = propertyMutation.getState();
            if (mutationState.getValue().equalsIgnoreCase(WF_STATE_ASSISTANT_APPROVED)) {
                subject = getText("subject.createtransferproperty", new String[] { propertyMutation.getBasicProperty()
                        .getUpicNo() });
                argsForTransferor.add(propertyMutation.getFullTranferorName());
                argsForTransferor.add(propertyMutation.getBasicProperty().getUpicNo());
                argsForTransferor.add(transferOwnerService.getCityName());
                emailBodyTransferor = getText("body.createtransferpropertytransferor", argsForTransferor);
                argsForTransferee.add(propertyMutation.getFullTranfereeName());
                argsForTransferee.add(propertyMutation.getBasicProperty().getUpicNo());
                argsForTransferee.add(transferOwnerService.getCityName());
                if (propertyMutation.getPartyValue().compareTo(propertyMutation.getDepartmentValue()) > 0)
                    argsForTransferee.add(propertyMutation.getPartyValue().toString());
                else
                    argsForTransferee.add(propertyMutation.getDepartmentValue().toString());
                argsForTransferee
                        .add(propertyMutation.getMutationFee() != null ? propertyMutation.getMutationFee().toString() : "N/A");
                emailBodyTransferee = getText("body.createtransferproperty", argsForTransferee);
            } else if (mutationState.getValue().equals(WF_STATE_REJECTED)) {
                subject = getText("subject.rejecttransferproperty");
                argsForTransferor.add(propertyMutation.getFullTranferorName());
                argsForTransferor.add(propertyMutation.getBasicProperty().getUpicNo());
                argsForTransferor.add(transferOwnerService.getCityName());
                argsForTransferee.add(propertyMutation.getFullTranfereeName());
                argsForTransferee.add(propertyMutation.getBasicProperty().getUpicNo());
                argsForTransferee.add(transferOwnerService.getCityName());
                emailBodyTransferor = getText("body.rejecttransferproperty", argsForTransferor);
                emailBodyTransferee = getText("body.rejecttransferproperty", argsForTransferee);
            } else if (mutationState.getValue().equals(WF_STATE_COMMISSIONER_APPROVED)) {
                subject = getText("subject.approvetransferproperty");
                argsForTransferor.add(propertyMutation.getFullTranferorName());
                argsForTransferor.add(propertyMutation.getFullTranferorName());
                argsForTransferor.add(propertyMutation.getFullTranfereeName());
                argsForTransferor.add(propertyMutation.getBasicProperty().getUpicNo());
                argsForTransferor.add(transferOwnerService.getUserDesigantion().getName());
                argsForTransferor.add(transferOwnerService.getCityName());

                argsForTransferee.add(propertyMutation.getFullTranfereeName());
                argsForTransferee.add(propertyMutation.getFullTranferorName());
                argsForTransferee.add(propertyMutation.getFullTranfereeName());
                argsForTransferee.add(propertyMutation.getBasicProperty().getUpicNo());
                argsForTransferee.add(transferOwnerService.getUserDesigantion().getName());
                argsForTransferee.add(transferOwnerService.getCityName());
                emailBodyTransferor = getText("body.approvetransferproperty", argsForTransferor);
                emailBodyTransferee = getText("body.approvetransferproperty", argsForTransferee);
            }
        }
        for (final User transferor : propertyMutation.getTransferorInfos())
            if (StringUtils.isNotBlank(transferor.getEmailId()) && StringUtils.isNotBlank(subject)
                    && StringUtils.isNotBlank(emailBodyTransferor))
                notificationService.sendEmail(transferor.getEmailId(), subject, emailBodyTransferor);
        for (final PropertyMutationTransferee transferee : propertyMutation.getTransfereeInfos())
            if (StringUtils.isNotBlank(transferee.getTransferee().getEmailId()) && StringUtils.isNotBlank(subject)
                    && StringUtils.isNotBlank(emailBodyTransferee))
                notificationService.sendEmail(transferee.getTransferee().getEmailId(), subject, emailBodyTransferee);
    }
    
    @SkipValidation
    @Action(value = "/rejecttocancel")
    public String rejectToCancel() {
        if (mutationId != null) {
            final javax.persistence.Query qry = entityManager.createNamedQuery("BY_MUTATION_ID");
            qry.setParameter("id", mutationId);
            propertyMutation = (PropertyMutation) qry.getSingleResult();
            persistenceService.getSession().refresh(propertyMutation);
        }
        transitionWorkFlow(propertyMutation);
        transferOwnerService.viewPropertyTransfer(basicproperty, propertyMutation);
        if (Source.WARDSECRETARY.toString().equalsIgnoreCase(propertyMutation.getSource())) {
            propertyThirdPartyService.publishUpdateEvent(propertyMutation.getApplicationNo(), WFLOW_ACTION_STEP_REJECT_TO_CANCEL,
                    "Property Transfer Rejected to Cancel");
        }
        setAckMessage(
                "Transfer of ownership rejected successfuly and forwarded to : " + securityUtils.getCurrentUser().getName());
        setAssessmentNoMessage(" with Assessment Number: ");
        return ACK;
    }
    
    private String getNatureOfTask() {
        return ADDTIONAL_RULE_REGISTERED_TRANSFER.equals(getAdditionalRule())
                ? NATURE_REGISTERED_TRANSFER
                : ADDITIONAL_RULE_FULL_TRANSFER.equals(getAdditionalRule())
                        ? NATURE_FULL_TRANSFER
                        : ADDTIONAL_RULE_PARTIAL_TRANSFER
                                .equals(getAdditionalRule())
                                        ? NATURE_PARTIAL_TRANSFER
                                        : "PropertyMutation";
    }

    private String getCurrentStateForThirdPartyWF(PropertyMutation propertyMutation) {

        return propertyMutation.getType().equals(ADDITIONAL_RULE_FULL_TRANSFER) ? "NEW" : "Created";
    }

    private Assignment setInitiatorForThirdPartyByMutationType(PropertyMutation propertyMutation, Assignment assignment) {
        return propertyMutation.getType().equals(ADDITIONAL_RULE_FULL_TRANSFER) ? null : assignment;
    }

    private BigDecimal getWaterTaxDues() {
        BigDecimal waterTaxDues = BigDecimal.ZERO;
        Map<String, Object> waterTaxDetails = propertyService.getWaterTaxDues(assessmentNo);
        if (waterTaxDetails.get("currentInstDemand") != null)
            waterTaxDues = BigDecimal.valueOf((double) waterTaxDetails.get("currentInstDemand"));
        return waterTaxDues;
    }

    private Boolean isUnderWtmsWF() {
        return propertyService.getWaterTaxDues(assessmentNo).get(UNDER_WTMS_WF) == null
                ? Boolean.FALSE
                : (Boolean) propertyService.getWaterTaxDues(assessmentNo)
                        .get(UNDER_WTMS_WF);
    }
    
    private void wFRejectToCancel(final PropertyMutation propertyMutation) {
        final DateTime currentDate = new DateTime();
        final User user = securityUtils.getCurrentUser();
        String nextAction = "";
        nextAction = WF_STATE_DIGITAL_SIGNATURE_PENDING;
        propertyMutation.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                .withComments(approverComments).withStateValue(WF_STATE_REJECTED_TO_CANCEL).withDateInfo(currentDate.toDate())
                .withOwner(propertyMutation.getCurrentState().getOwnerPosition()).withNextAction(nextAction);
    }
    
    public BigDecimal getCurrentPropertyTax() {
        return currentPropertyTax;
    }

    public BigDecimal getCurrentPropertyTaxFirstHalf() {
        return currentPropertyTaxFirstHalf;
    }

    public BigDecimal getCurrentPropertyTaxSecondHalf() {
        return currentPropertyTaxSecondHalf;
    }

    public BigDecimal getCurrentPropertyTaxDue() {
        return currentPropertyTaxDue;
    }

    public BigDecimal getCurrentWaterTaxDue() {
        return currentWaterTaxDue;
    }

    @Override
    public StateAware getModel() {
        return propertyMutation;
    }

    public String getWfErrorMsg() {
        return wfErrorMsg;
    }

    public String getAssessmentNo() {
        return assessmentNo;
    }

    public void setAssessmentNo(final String assessmentNo) {
        this.assessmentNo = assessmentNo;
    }

    public BasicProperty getBasicproperty() {
        return basicproperty;
    }

    public List<DocumentType> getDocumentTypes() {
        return documentTypes;
    }

    public Long getMutationId() {
        return mutationId;
    }

    public void setMutationId(final Long mutationId) {
        this.mutationId = mutationId;
    }

    public BigDecimal getArrearPropertyTaxDue() {
        return arrearPropertyTaxDue;
    }

    public String getReportId() {
        return reportId;
    }

    public void setTransfereeId(final Long transfereeId) {
        this.transfereeId = transfereeId;
    }

    public void setMarketValue(final double marketValue) {
        this.marketValue = marketValue;
    }

    public void setTransferReason(final String transferReason) {
        this.transferReason = transferReason;
    }

    public String getCollectXML() throws UnsupportedEncodingException {
        return URLEncoder.encode(collectXML, "utf-8");
    }

    public void setApplicationNo(final String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public String getAckMessage() {
        return ackMessage;
    }

    public void setAckMessage(final String ackMessage) {
        this.ackMessage = ackMessage;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public String getMutationInitiatedBy() {
        return mutationInitiatedBy;
    }

    public void setMutationInitiatedBy(final String mutationInitiatedBy) {
        this.mutationInitiatedBy = mutationInitiatedBy;
    }

    public String getAssessmentNoMessage() {
        return assessmentNoMessage;
    }

    public void setAssessmentNoMessage(final String assessmentNoMessage) {
        this.assessmentNoMessage = assessmentNoMessage;
    }

    public String getTaxDueErrorMsg() {
        return taxDueErrorMsg;
    }

    public void setTaxDueErrorMsg(final String taxDueErrorMsg) {
        this.taxDueErrorMsg = taxDueErrorMsg;
    }

    public Map<String, String> getGuardianRelationMap() {
        return guardianRelationMap;
    }

    public void setGuardianRelationMap(final Map<String, String> guardianRelationMap) {
        this.guardianRelationMap = guardianRelationMap;
    }

    public Boolean getPropertyByEmployee() {
        return propertyByEmployee;
    }

    public void setPropertyByEmployee(final Boolean propertyByEmployee) {
        this.propertyByEmployee = propertyByEmployee;
    }

    public String getUserDesignationList() {
        return userDesignationList;
    }

    public void setUserDesignationList(final String userDesignationList) {
        this.userDesignationList = userDesignationList;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(final String actionType) {
        this.actionType = actionType;
    }

    public String getMeesevaApplicationNumber() {
        return meesevaApplicationNumber;
    }

    public void setMeesevaApplicationNumber(final String meesevaApplicationNumber) {
        this.meesevaApplicationNumber = meesevaApplicationNumber;
    }

    public String getMeesevaServiceCode() {
        return meesevaServiceCode;
    }

    public void setMeesevaServiceCode(final String meesevaServiceCode) {
        this.meesevaServiceCode = meesevaServiceCode;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(final String applicationType) {
        this.applicationType = applicationType;
    }

    public String getFileStoreIds() {
        return fileStoreIds;
    }

    public void setFileStoreIds(final String fileStoreIds) {
        this.fileStoreIds = fileStoreIds;
    }

    public String getUlbCode() {
        return ulbCode;
    }

    public void setUlbCode(final String ulbCode) {
        this.ulbCode = ulbCode;
    }

    public List<HashMap<String, Object>> getHistoryMap() {
        return historyMap;
    }

    public void setHistoryMap(final List<HashMap<String, Object>> historyMap) {
        this.historyMap = historyMap;
    }

    public boolean isEnableApproverDetails() {
        return enableApproverDetails;
    }

    public void setEnableApproverDetails(final boolean enableApproverDetails) {
        this.enableApproverDetails = enableApproverDetails;
    }

    public boolean isDigitalSignEnabled() {
        return digitalSignEnabled;
    }

    public void setDigitalSignEnabled(final boolean digitalSignEnabled) {
        this.digitalSignEnabled = digitalSignEnabled;
    }

    @Override
    public String getAdditionalRule() {
        return propertyMutation.getType();
    }

    public boolean isMutationFeePaid() {
        return mutationFeePaid;
    }

    public void setMutationFeePaid(final boolean mutationFeePaid) {
        this.mutationFeePaid = mutationFeePaid;
    }

    public boolean getReceiptCanceled() {
        return receiptCanceled;
    }

    public void setReceiptCanceled(final boolean receiptCanceled) {
        this.receiptCanceled = receiptCanceled;
    }

    public boolean isInitiatorIsActive() {
        return initiatorIsActive;
    }

    public void setInitiatorIsActive(final boolean initiatorIsActive) {
        this.initiatorIsActive = initiatorIsActive;
    }

    @Override
    public String getPendingActions() {
        return propertyMutation != null ? propertyMutation.getCurrentState().getNextAction() : null;
    }

    @Override
    public String getCurrentDesignation() {
        return propertyMutation != null && !(propertyMutation.getCurrentState().getValue().endsWith(STATUS_REJECTED) ||
                propertyMutation.getCurrentState().getValue().equals(WFLOW_ACTION_NEW))
                        ? propertyService.getDesignationForPositionAndUser(
                                propertyMutation.getCurrentState().getOwnerPosition().getId(),
                                securityUtils.getCurrentUser().getId())
                        : null;
    }

    private void checkToDisplayAckButton() {
        if (getModel().getId() == null)
            showAckBtn = Boolean.TRUE;
    }

    public boolean isAllowEditDocument() {
        return allowEditDocument;
    }

    public void setAllowEditDocument(final boolean allowEditDocument) {
        this.allowEditDocument = allowEditDocument;
    }

    public String getPropertyOwner() {
        return propertyOwner;
    }

    public void setPropertyOwner(final String propertyOwner) {
        this.propertyOwner = propertyOwner;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(final String houseNo) {
        this.houseNo = houseNo;
    }

    public String getApplicationSource() {
        return applicationSource;
    }

    public void setApplicationSource(String applicationSource) {
        this.applicationSource = applicationSource;
    }

    public Boolean getCitizenPortalUser() {
        return citizenPortalUser;
    }

    public void setCitizenPortalUser(Boolean citizenPortalUser) {
        this.citizenPortalUser = citizenPortalUser;
    }

    public Boolean getShowAckBtn() {
        return showAckBtn;
    }

    public void setShowAckBtn(final Boolean showAckBtn) {
        this.showAckBtn = showAckBtn;
    }

    public Boolean getIsReassignEnabled() {
        return isReassignEnabled;
    }

    public void setIsReassignEnabled(Boolean isReassignEnabled) {
        this.isReassignEnabled = isReassignEnabled;
    }

    public Long getStateAwareId() {
        return stateAwareId;
    }

    public void setStateAwareId(Long stateAwareId) {
        this.stateAwareId = stateAwareId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Boolean getShowPayBtn() {
        return showPayBtn;
    }

    public void setShowPayBtn(final Boolean showPayBtn) {
        this.showPayBtn = showPayBtn;
    }

    public List<PtNotice> getEndorsementNotices() {
        return endorsementNotices;
    }

    public void setEndorsementNotices(List<PtNotice> endorsementNotices) {
        this.endorsementNotices = endorsementNotices;
    }

    public Boolean getEndorsementRequired() {
        return endorsementRequired;
    }

    public void setEndorsementRequired(Boolean endorsementRequired) {
        this.endorsementRequired = endorsementRequired;
    }

    public String getAssessmentNumber() {
        return assessmentNumber;
    }

    public void setAssessmentNumber(String assessmentNumber) {
        this.assessmentNumber = assessmentNumber;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getOwnersName() {
        return ownersName;
    }

    public void setOwnersName(String ownersName) {
        this.ownersName = ownersName;
    }

    public List<DocumentType> getSuccessionDocs() {
        return successionDocs;
    }

    public void setSuccessionDocs(List<DocumentType> successionDocs) {
        this.successionDocs = successionDocs;
    }

    public String getOldTransferReason() {
        return oldTransferReason;
    }

    public void setOldTransferReason(String oldTransferReason) {
        this.oldTransferReason = oldTransferReason;
    }

    public List<Document> getSuccessionDocuments() {
        return successionDocuments;
    }

    public void setSuccessionDocuments(List<Document> successionDocuments) {
        this.successionDocuments = successionDocuments;
    }

    public List<Document> getOtherDocuments() {
        return otherDocuments;
    }

    public void setOtherDocuments(List<Document> otherDocuments) {
        this.otherDocuments = otherDocuments;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public boolean isWsPortalRequest() {
        return wsPortalRequest;
    }

    public void setWsPortalRequest(boolean wsPortalRequest) {
        this.wsPortalRequest = wsPortalRequest;
    }

}