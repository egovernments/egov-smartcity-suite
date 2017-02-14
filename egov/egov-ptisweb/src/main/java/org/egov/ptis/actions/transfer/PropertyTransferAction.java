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
package org.egov.ptis.actions.transfer;

import static org.egov.ptis.constants.PropertyTaxConstants.ADDITIONAL_COMMISSIONER_DESIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.ADDTIONAL_RULE_FULL_TRANSFER;
import static org.egov.ptis.constants.PropertyTaxConstants.ADDTIONAL_RULE_REGISTERED_TRANSFER;
import static org.egov.ptis.constants.PropertyTaxConstants.APPCONFIG_CLIENT_SPECIFIC_DMD_BILL;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ASSISTANT_COMMISSIONER_DESIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.BILL_COLLECTOR_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.COMMISSIONER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_BAL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_FIRSTHALF_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_SECONDHALF_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.DEPUTY_COMMISSIONER_DESIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.GUARDIAN_RELATION;
import static org.egov.ptis.constants.PropertyTaxConstants.JUNIOR_ASSISTANT;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_FULL_TRANSFER;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_PARTIAL_TRANSFER;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_REGISTERED_TRANSFER;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_MUTATION_CERTIFICATE;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_INSPECTOR_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_OFFICER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.SENIOR_ASSISTANT;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_REJECTED;
import static org.egov.ptis.constants.PropertyTaxConstants.TARGET_WORKFLOW_ERROR;
import static org.egov.ptis.constants.PropertyTaxConstants.UD_REVENUE_INSPECTOR_APPROVAL_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_NEW;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_APPROVE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_FORWARD;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_SIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_ASSISTANT_APPROVAL_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_ASSISTANT_APPROVED;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_CLOSED;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_COMMISSIONER_APPROVAL_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_COMMISSIONER_APPROVED;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_DIGITAL_SIGNATURE_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_REGISTRATION_COMPLETED;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_REJECTED;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_REVENUE_OFFICER_APPROVED;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_UD_REVENUE_INSPECTOR_APPROVAL_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONAL_COMMISSIONER_DESIGN;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.web.actions.workflow.GenericWorkFlowAction;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.messaging.MessagingService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.web.utils.WebUtils;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Position;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Document;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.entity.property.PropertyMutationTransferee;
import org.egov.ptis.domain.service.notice.NoticeService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.domain.service.transfer.PropertyTransferService;
import org.egov.ptis.notice.PtNotice;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;

@Results({
        @Result(name = BaseFormAction.NEW, location = "transfer/transferProperty-new.jsp"),
        @Result(name = BaseFormAction.EDIT, location = "transfer/transferProperty-edit.jsp"),
        @Result(name = BaseFormAction.VIEW, location = "transfer/transferProperty-view.jsp"),
        @Result(name = PropertyTransferAction.REDIRECT, location = "transfer/transferProperty-redirect.jsp"),
        @Result(name = TARGET_WORKFLOW_ERROR, location = "workflow/workflow-error.jsp"),
        @Result(name = PropertyTransferAction.ACK, location = "transfer/transferProperty-ack.jsp"),
        @Result(name = PropertyTransferAction.REJECT_ON_TAXDUE, location = "transfer/transferProperty-balance.jsp"),
        @Result(name = PropertyTransferAction.PRINTACK, location = "transfer/transferProperty-printAck.jsp"),
        @Result(name = PropertyTransferAction.PRINTNOTICE, location = "transfer/transferProperty-printNotice.jsp"),
        @Result(name = PropertyTransferAction.SEARCH, location = "transfer/transferProperty-search.jsp"),
        @Result(name = PropertyTransferAction.ERROR, location = "common/meeseva-errorPage.jsp"),
        @Result(name = PropertyTransferAction.MEESEVA_RESULT_ACK, location = "common/meesevaAck.jsp"),
        @Result(name = PropertyTransferAction.COLLECT_FEE, location = "collection/collectPropertyTax-view.jsp"),
        @Result(name = PropertyTransferAction.REDIRECT_SUCCESS, location = PropertyTransferAction.REDIRECT_SUCCESS, type = "redirectAction", params = {
                "assessmentNo", "${assessmentNo}", "mutationId", "${mutationId}" }),
        @Result(name = PropertyTransferAction.COMMON_FORM, location = "search/searchProperty-commonForm.jsp"),
        @Result(name = PropertyTransferAction.DIGITAL_SIGNATURE_REDIRECTION, location = "transfer/transferProperty-digitalSignatureRedirection.jsp") })
@Namespace("/property/transfer")
public class PropertyTransferAction extends GenericWorkFlowAction {
    protected static final String COMMON_FORM = "commonForm";
    protected static final String REDIRECT = "redirect";
    protected static final String DIGITAL_SIGNATURE_REDIRECTION = "digitalSignatureRedirection";

    private static final String PROPERTY_TRANSFER = "property transfer";
    private static final long serialVersionUID = 1L;
    public static final String ACK = "ack";
    public static final String ERROR = "error";
    public static final String SEARCH = "search";
    public static final String REJECT_ON_TAXDUE = "balance";
    public static final String PRINTACK = "printAck";
    public static final String PRINTNOTICE = "printNotice";
    public static final String REDIRECT_SUCCESS = "redirect-success";
    public static final String COLLECT_FEE = "collect-fee";
    public static final String MEESEVA_RESULT_ACK = "meesevaAck";
    private static final String PROPERTY_MODIFY_REJECT_FAILURE = "property.modify.reject.failure";

    // Form Binding Model
    private PropertyMutation propertyMutation = new PropertyMutation();

    // Dependent Services
    @Autowired
    @Qualifier("transferOwnerService")
    private PropertyTransferService transferOwnerService;

    @Autowired
    protected AssignmentService assignmentService;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<PropertyMutation> transferWorkflowService;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;

    @Autowired
    private ReportViewerUtil reportViewerUtil;

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

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
    private List<Hashtable<String, Object>> historyMap = new ArrayList<Hashtable<String, Object>>();
    private String actionType;
    private boolean digitalSignEnabled;
    private boolean mutationFeePaid = Boolean.FALSE;
    private boolean receiptCanceled = Boolean.FALSE;
    private boolean allowEditDocument = Boolean.FALSE;

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
        if (basicproperty.getProperty().getStatus().equals(PropertyTaxConstants.STATUS_DEMAND_INACTIVE)) {
            addActionError(getText("error.msg.demandInactive"));
            return COMMON_FORM;
        }
        if (basicproperty.isUnderWorkflow()) {
            final List<String> msgParams = new ArrayList<String>();
            msgParams.add("Transfer of Ownership");
            wfErrorMsg = getText("wf.pending.msg", msgParams);
            return TARGET_WORKFLOW_ERROR;
        } else {
            final PropertyTaxUtil propertyTaxUtil = new PropertyTaxUtil();
            propertyTaxUtil.setPersistenceService(persistenceService);
            final boolean hasChildPropertyUnderWorkflow = propertyTaxUtil.checkForParentUsedInBifurcation(basicproperty
                    .getUpicNo());
            if (hasChildPropertyUnderWorkflow) {
                wfErrorMsg = getText("error.msg.child.underworkflow");
                return TARGET_WORKFLOW_ERROR;
            }
            final Map<String, BigDecimal> propertyTaxDetails = propertyService
                    .getCurrentPropertyTaxDetails(basicproperty.getActiveProperty());
            final Map<String, BigDecimal> currentTaxAndDue = propertyService.getCurrentTaxAndBalance(propertyTaxDetails,
                    new Date());
            currentPropertyTax = currentTaxAndDue.get(CURR_DMD_STR);
            propertyOwner = basicproperty.getFullOwnerName();
            houseNo = basicproperty.getAddress().getHouseNoBldgApt();
            currentPropertyTaxDue = currentTaxAndDue.get(CURR_BAL_STR);
            arrearPropertyTaxDue = propertyTaxDetails.get(ARR_DMD_STR).subtract(propertyTaxDetails.get(ARR_COLL_STR));
            currentWaterTaxDue = propertyService.getWaterTaxDues(assessmentNo);
            if (currentWaterTaxDue.add(currentPropertyTaxDue).add(arrearPropertyTaxDue).longValue() > 0) {
                setTaxDueErrorMsg(getText("taxdues.error.msg", new String[] { PROPERTY_TRANSFER }));
                return REJECT_ON_TAXDUE;
            }
            if (basicproperty.getActiveProperty().getPropertyDetail().isStructure()) {
                addActionError(getText("error.superstruc.prop.notallowed"));
                return COMMON_FORM;
            }

            else {
                loggedUserIsMeesevaUser = propertyService.isMeesevaUser(transferOwnerService.getLoggedInUser());
                if (loggedUserIsMeesevaUser)
                    if (getMeesevaApplicationNumber() == null) {
                        addActionMessage(getText("MEESEVA.005"));
                        return ERROR;
                    } else
                        propertyMutation.setMeesevaApplicationNumber(getMeesevaApplicationNumber());
                return NEW;
            }
        }
    }

    @ValidationErrorPage(value = NEW)
    @Action(value = "/save")
    public String save() {
        transitionWorkFlow(propertyMutation);

        loggedUserIsMeesevaUser = propertyService.isMeesevaUser(transferOwnerService.getLoggedInUser());
        if (!loggedUserIsMeesevaUser)
            transferOwnerService.initiatePropertyTransfer(basicproperty, propertyMutation);
        else {
            final HashMap<String, String> meesevaParams = new HashMap<String, String>();
            meesevaParams.put("APPLICATIONNUMBER", propertyMutation.getMeesevaApplicationNumber());
            propertyMutation.setSource(PropertyTaxConstants.SOURCEOFDATA_MEESEWA);
            propertyMutation.setApplicationNo(propertyMutation.getMeesevaApplicationNumber());
            transferOwnerService.initiatePropertyTransfer(basicproperty, propertyMutation, meesevaParams);
        }

        buildSMS(propertyMutation);
        buildEmail(propertyMutation);
        setAckMessage("Transfer of ownership data saved successfully in the system and forwarded to : ");
        setAssessmentNoMessage(" with assessment number : ");

        if (!loggedUserIsMeesevaUser)
            return ACK;
        else
            return MEESEVA_RESULT_ACK;

    }

    @SkipValidation
    @Action(value = "/view")
    public String view() {
        final String currState = propertyMutation.getState().getValue();
        final String nextAction = propertyMutation.getState().getNextAction();
        propertyMutation.getTransfereeInfosProxy().addAll(propertyMutation.getTransfereeInfos());
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
        else if (StringUtils.isNotBlank(applicationNo))
            propertyMutation = transferOwnerService.getPropertyMutationByApplicationNo(applicationNo);
        else {
            addActionError(getText("mandatory.assessmentno.applicationno"));
            target = SEARCH;
        }
        if (propertyMutation == null || propertyMutation.getId() == null) {
            addActionError(getText("mutation.notexists"));
            target = SEARCH;
        } else if (propertyMutation != null && propertyMutation.getReceiptDate() != null
                && !propertyTaxCommonUtils.isReceiptCanceled(propertyMutation.getReceiptNum())) {
            final SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            addActionError(getText("mutationpayment.done",
                    new String[] { df.format(propertyMutation.getReceiptDate()) }));
            target = SEARCH;
        } else if (propertyMutation != null && propertyMutation.getMutationFee() == null) {
            addActionError(getText("mutationfee.notexists"));
            target = SEARCH;
        } else {
            collectXML = transferOwnerService.generateReceipt(propertyMutation);
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
            transferOwnerService.updatePropertyTransfer(basicproperty, propertyMutation);
        } else {
            transitionWorkFlow(propertyMutation);
            transferOwnerService.viewPropertyTransfer(basicproperty, propertyMutation);
        }
        buildSMS(propertyMutation);
        buildEmail(propertyMutation);
        setAssessmentNoMessage(" with assessment number : ");
        return ACK;
    }

    @SkipValidation
    @Action(value = "/reject")
    public String reject() {
        if (isRejectionNotAllowed()) {
            addActionError(getText("error.mutation.reject.notallowed"));
            if (propertyMutation.getType().equalsIgnoreCase(ADDTIONAL_RULE_FULL_TRANSFER))
                return VIEW;
            else
                return EDIT;
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
        if (isRoOrCommissioner(loggedInUserDesignation)) {
            final Assignment assignmentOnreject = propertyService.getUserOnRejection(propertyMutation);
            wfInitiator = assignmentOnreject;
        } else if (BILL_COLLECTOR_DESGN.equalsIgnoreCase(loggedInUserDesignation)
                || REVENUE_INSPECTOR_DESGN.equalsIgnoreCase(loggedInUserDesignation))
            wfInitiator = transferOwnerService.getWorkflowInitiator(propertyMutation);

        if (wfInitiator != null || JUNIOR_ASSISTANT.equalsIgnoreCase(loggedInUserDesignation)) {
            transitionWorkFlow(propertyMutation);
            transferOwnerService.viewPropertyTransfer(basicproperty, propertyMutation);
            buildSMS(propertyMutation);
            buildEmail(propertyMutation);
            approverName = "";
            List<Assignment> assignment;
            if (WF_STATE_CLOSED.equals(propertyMutation.getState().getValue())) {
                final List<StateHistory> history = propertyMutation.getStateHistory();
                Collections.reverse(history);
                assignment = assignmentService.getAssignmentByPositionAndUserAsOnDate(history.get(0).getOwnerPosition().getId(),
                        transferOwnerService.getLoggedInUser().getId(), new Date());
                mutationInitiatedBy = transferOwnerService.getLoggedInUser().getName().concat("~")
                        .concat(assignment.get(0).getPosition().getName());
                setAckMessage("Transfer of ownership data rejected successfuly By ");
            } else
                setAckMessage("Transfer of ownership data rejected successfuly and forwarded to : ");
            setAssessmentNoMessage(" with assessment number : ");
        } else
            setAckMessage(getText(PROPERTY_MODIFY_REJECT_FAILURE));
        return ACK;
    }

    private boolean isRejectionNotAllowed() {
        return ("Rejected".equals(propertyMutation.getState().getValue())
                || propertyMutation.getType().equalsIgnoreCase(ADDTIONAL_RULE_FULL_TRANSFER))
                && propertyMutation.getReceiptNum() != null && !receiptCanceled;
    }

    private boolean isRoOrCommissioner(final String loggedInUserDesignation) {
        boolean isany;
        if (!REVENUE_OFFICER_DESGN.equalsIgnoreCase(loggedInUserDesignation))
            isany = isCommissioner(loggedInUserDesignation);
        else
            isany = true;
        return isany;
    }

    private boolean isCommissioner(final String loggedInUserDesignation) {
        boolean isanyone;
        if (!ASSISTANT_COMMISSIONER_DESIGN.equalsIgnoreCase(loggedInUserDesignation)
                || !ADDITIONAL_COMMISSIONER_DESIGN.equalsIgnoreCase(loggedInUserDesignation))
            isanyone = isDeputyOrAbove(loggedInUserDesignation);
        else
            isanyone = true;
        return isanyone;
    }

    private boolean isDeputyOrAbove(final String loggedInUserDesignation) {
        boolean isanyone = false;
        if (DEPUTY_COMMISSIONER_DESIGN.equalsIgnoreCase(loggedInUserDesignation)
                || COMMISSIONER_DESGN.equalsIgnoreCase(loggedInUserDesignation)
                || ZONAL_COMMISSIONER_DESIGN.equalsIgnoreCase(loggedInUserDesignation))
            isanyone = true;
        return isanyone;
    }

    @ValidationErrorPage(value = EDIT)
    @Action(value = "/approve")
    public String approve() {
        transitionWorkFlow(propertyMutation);
        transferOwnerService.approvePropertyTransfer(basicproperty, propertyMutation);
        transferOwnerService.viewPropertyTransfer(basicproperty, propertyMutation);
        approverName = "";
        final List<StateHistory> history = propertyMutation.getStateHistory();
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
        setAckMessage("Transfer of ownership is created successfully in the system and forwarded to : ");
        setAssessmentNoMessage(" for Digital Signature for the property : ");
        return ACK;
    }

    @SkipValidation
    @Action(value = "/printAck")
    public String printAck() {
        final HttpServletRequest request = ServletActionContext.getRequest();

        final String url = WebUtils.extractRequestDomainURL(request, false);
        final String cityLogo = url.concat(PropertyTaxConstants.IMAGE_CONTEXT_PATH).concat(
                (String) request.getSession().getAttribute("citylogo"));
        final String cityName = request.getSession().getAttribute("citymunicipalityname").toString();
        reportId = reportViewerUtil.addReportToTempCache(
                transferOwnerService.generateAcknowledgement(basicproperty, propertyMutation, cityName, cityLogo));
        return PRINTACK;
    }

    @SkipValidation
    @Action(value = "/printNotice")
    public String printNotice() {
        setUlbCode(ApplicationThreadLocals.getCityCode());
        final HttpServletRequest request = ServletActionContext.getRequest();
        final String url = WebUtils.extractRequestDomainURL(request, false);
        final String cityLogo = url.concat(PropertyTaxConstants.IMAGE_CONTEXT_PATH).concat(
                (String) request.getSession().getAttribute("citylogo"));
        final String cityName = request.getSession().getAttribute("citymunicipalityname").toString();

        final String cityGrade = request.getSession().getAttribute("cityGrade") != null ? request.getSession()
                .getAttribute("cityGrade").toString() : null;
        Boolean isCorporation;
        if (cityGrade != null && cityGrade != ""
                && cityGrade.equalsIgnoreCase(PropertyTaxConstants.CITY_GRADE_CORPORATION))
            isCorporation = true;
        else
            isCorporation = false;

        final ReportOutput reportOutput = transferOwnerService.generateTransferNotice(basicproperty, propertyMutation,
                cityName, cityLogo, actionType, isCorporation);
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
        super.prepare();
        final Long userId = securityUtils.getCurrentUser().getId();
        userDesignationList = propertyTaxCommonUtils.getAllDesignationsForUser(userId);
        propertyByEmployee = propertyService.isEmployee(transferOwnerService.getLoggedInUser());
        final String actionInvoked = ActionContext.getContext().getActionInvocation().getProxy().getMethod();
        if (!(actionInvoked.equals("search") || actionInvoked.equals("collectFee"))) {
            if (StringUtils.isNotBlank(assessmentNo) && mutationId == null)
                basicproperty = transferOwnerService.getBasicPropertyByUpicNo(assessmentNo);

            if (mutationId != null) {
                propertyMutation = (PropertyMutation) persistenceService.find("From PropertyMutation where id = ? ",
                        mutationId);
                basicproperty = propertyMutation.getBasicProperty();
                historyMap = propertyService.populateHistory(propertyMutation);
            }
            final Map<String, BigDecimal> propertyTaxDetails = propertyService
                    .getCurrentPropertyTaxDetails(basicproperty.getActiveProperty());

            final Map<String, BigDecimal> currentTaxAndDue = propertyService.getCurrentTaxAndBalance(propertyTaxDetails,
                    new Date());
            currentPropertyTax = currentTaxAndDue.get(CURR_DMD_STR);
            currentPropertyTaxFirstHalf = propertyTaxDetails.get(CURR_FIRSTHALF_DMD_STR);
            currentPropertyTaxSecondHalf = propertyTaxDetails.get(CURR_SECONDHALF_DMD_STR);
            documentTypes = transferOwnerService.getPropertyTransferDocumentTypes();
            addDropdownData("MutationReason", transferOwnerService.getPropertyTransferReasons());
            setGuardianRelationMap(GUARDIAN_RELATION);
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
    }

    @Override
    public void validate() {
        if (PropertyTaxConstants.MUTATION_TYPE_REGISTERED_TRANSFER.equalsIgnoreCase(propertyMutation.getType())) {
            if (propertyMutation.getMutationReason() == null || propertyMutation.getMutationReason().getId() == -1)
                addActionError(getText("mandatory.trRsnId"));
            else if (propertyMutation.getMutationReason().getMutationName()
                    .equals(PropertyTaxConstants.MUTATIONRS_SALES_DEED)
                    && StringUtils.isBlank(propertyMutation.getSaleDetail()))
                addActionError(getText("mandatory.saleDtl"));
            if (propertyMutation.getDeedDate() == null)
                addActionError("Registration Document Date should not be empty");
            if (StringUtils.isBlank(propertyMutation.getDeedNo()))
                addActionError("Registration Document Number should not be empty");
        }
        if (propertyMutation.getPartyValue() == null || propertyMutation.getPartyValue().equals(""))
            addActionError(getText("mandatory.party.value"));
        if (propertyMutation.getDepartmentValue() == null || propertyMutation.getDepartmentValue().equals(""))
            addActionError(getText("mandatory.department.value"));
        boolean anyDocIsMandatory = false;
        for (final DocumentType docTypes : documentTypes)
            if (docTypes.isMandatory()) {
                anyDocIsMandatory = true;
                break;
            }

        if (anyDocIsMandatory)
            if (propertyMutation.getDocuments().isEmpty())
                addActionError("Please attach the mandatory documents.");
            else
                for (final Document document : propertyMutation.getDocuments())
                    if (document.isEnclosed() && document.getFiles().isEmpty())
                        addActionError(document.getType()
                                + " document marked as enclosed, please add the relavent documents.");
        // To set proxy list at approval stage
        if (propertyMutation.getState() != null && propertyMutation.getState().getValue() != null
                && (propertyMutation.getState().getValue().equalsIgnoreCase(WF_STATE_REVENUE_OFFICER_APPROVED)
                        || propertyMutation.getState().getValue().equalsIgnoreCase(WF_STATE_REGISTRATION_COMPLETED)
                        || propertyMutation.getState().getNextAction()
                                .equalsIgnoreCase(PropertyTaxConstants.WF_STATE_COMMISSIONER_APPROVAL_PENDING)))
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

    public void transitionWorkFlow(final PropertyMutation propertyMutation) {
        final DateTime currentDate = new DateTime();
        final User user = transferOwnerService.getLoggedInUser();
        Position pos;
        Assignment wfInitiator = null;
        String nextAction = "";
        Assignment assignment;

        if (!propertyByEmployee) {
            currentState = "Created";
            if (propertyService.isCscOperator(user))
                assignment = propertyService.getMappedAssignmentForCscOperator(basicproperty);
            else
                assignment = propertyService.getUserPositionByZone(basicproperty, false);
            approverPositionId = assignment.getPosition().getId();
            approverName = assignment.getEmployee().getName().concat("~").concat(assignment.getPosition().getName());
            wfInitiator = assignment;
            approverDesignation = assignment.getDesignation().getName();
        } else {
            if (null != approverPositionId && approverPositionId != -1) {
                assignment = assignmentService.getAssignmentsForPosition(approverPositionId, new Date())
                        .get(0);
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
                && (approverDesignation.equalsIgnoreCase(ASSISTANT_COMMISSIONER_DESIGN) ||
                        approverDesignation.equalsIgnoreCase(DEPUTY_COMMISSIONER_DESIGN)
                        || approverDesignation.equalsIgnoreCase(ADDITIONAL_COMMISSIONER_DESIGN)
                        || approverDesignation.equalsIgnoreCase(ZONAL_COMMISSIONER_DESIGN) ||
                        approverDesignation.equalsIgnoreCase(COMMISSIONER_DESGN)))
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
        else if (wfInitiator == null)
            wfInitiator = propertyTaxCommonUtils.getWorkflowInitiatorAssignment(user.getId());

        if (WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction)) {
            if (wfInitiator.getPosition().equals(propertyMutation.getState().getOwnerPosition())
                    || propertyMutation.getType().equalsIgnoreCase(ADDTIONAL_RULE_FULL_TRANSFER)) {
                propertyMutation.transition(true).end().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approverComments).withDateInfo(currentDate.toDate());
                propertyMutation.getBasicProperty().setUnderWorkflow(Boolean.FALSE);
            } else {
                if (loggedInUserDesignation.equalsIgnoreCase(REVENUE_OFFICER_DESGN)
                        || loggedInUserDesignation.equalsIgnoreCase(ASSISTANT_COMMISSIONER_DESIGN) ||
                        loggedInUserDesignation.equalsIgnoreCase(ADDITIONAL_COMMISSIONER_DESIGN)
                        || loggedInUserDesignation.equalsIgnoreCase(DEPUTY_COMMISSIONER_DESIGN) ||
                        loggedInUserDesignation.equalsIgnoreCase(COMMISSIONER_DESGN) ||
                        loggedInUserDesignation.equalsIgnoreCase(ZONAL_COMMISSIONER_DESIGN)) {
                    nextAction = UD_REVENUE_INSPECTOR_APPROVAL_PENDING;
                    final Assignment assignmentOnreject = propertyService.getUserOnRejection(propertyMutation);
                    wfInitiator = assignmentOnreject;
                    setMutationInitiatedBy(assignmentOnreject.getEmployee().getName().concat("~")
                            .concat(assignmentOnreject.getPosition().getName()));
                } else if (loggedInUserDesignation.equalsIgnoreCase(BILL_COLLECTOR_DESGN)
                        || loggedInUserDesignation.equalsIgnoreCase(REVENUE_INSPECTOR_DESGN)) {
                    nextAction = WF_STATE_ASSISTANT_APPROVAL_PENDING;
                    setMutationInitiatedBy(wfInitiator.getEmployee().getName().concat("~")
                            .concat(wfInitiator.getPosition().getName()));
                }
                final String stateValue = WF_STATE_REJECTED;
                propertyMutation.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approverComments).withStateValue(stateValue).withDateInfo(currentDate.toDate())
                        .withOwner(wfInitiator != null ? wfInitiator.getPosition() : null)
                        .withNextAction(nextAction);
            }
        } else {
            if (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workFlowAction))
                pos = propertyMutation.getCurrentState().getOwnerPosition();
            else if (null != approverPositionId && approverPositionId != -1)
                pos = (Position) persistenceService.find("from Position where id=?", approverPositionId);
            else
                pos = wfInitiator.getPosition();
            if (null == propertyMutation.getState()) {
                final WorkFlowMatrix wfmatrix = transferWorkflowService.getWfMatrix(propertyMutation.getStateType(),
                        null, null, getAdditionalRule(), currentState, null);
                propertyMutation.transition().start().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approverComments).withStateValue(wfmatrix.getNextState())
                        .withDateInfo(currentDate.toDate()).withOwner(pos).withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(getNatureOfTask())
                        .withInitiator(wfInitiator != null ? wfInitiator.getPosition() : null);
            } else if (propertyMutation.getCurrentState().getNextAction().equalsIgnoreCase("END"))
                propertyMutation.transition(true).end().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approverComments).withDateInfo(currentDate.toDate());
            else {
                final WorkFlowMatrix wfmatrix = transferWorkflowService.getWfMatrix(propertyMutation.getStateType(), null, null,
                        getAdditionalRule(), propertyMutation.getCurrentState().getValue(),
                        propertyMutation.getCurrentState().getNextAction(), null, loggedInUserDesignation);
                propertyMutation.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
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

    }

    public void buildSMS(final PropertyMutation propertyMutation) {
        final List<String> argsForTransferor = new ArrayList<String>();
        final List<String> argsForTransferee = new ArrayList<String>();
        String smsMsgForTransferor = "";
        String smsMsgForTransferee = "";
        if (null != propertyMutation && null != propertyMutation.getState()) {
            final State mutationState = propertyMutation.getState();
            if (mutationState.getValue().equals(WF_STATE_ASSISTANT_APPROVED)) {
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
                smsMsgForTransferor = getText("msg.createtransferproperty.sms", argsForTransferor);
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
                messagingService.sendSMS(transferor.getMobileNumber(), smsMsgForTransferor);
        for (final PropertyMutationTransferee transferee : propertyMutation.getTransfereeInfos())
            if (StringUtils.isNotBlank(transferee.getTransferee().getMobileNumber())
                    && StringUtils.isNotBlank(smsMsgForTransferee))
                messagingService.sendSMS(transferee.getTransferee().getMobileNumber(), smsMsgForTransferee);
    }

    public void buildEmail(final PropertyMutation propertyMutation) {
        String emailBodyTransferor = "";
        String emailBodyTransferee = "";
        String subject = "";
        final List<String> argsForTransferor = new ArrayList<String>();
        final List<String> argsForTransferee = new ArrayList<String>();
        if (null != propertyMutation && null != propertyMutation.getState()) {
            final State mutationState = propertyMutation.getState();
            if (mutationState.getValue().equals(WF_STATE_ASSISTANT_APPROVED)) {
                subject = getText("subject.createtransferproperty", new String[] { propertyMutation.getBasicProperty()
                        .getUpicNo() });
                argsForTransferor.add(propertyMutation.getFullTranferorName());
                argsForTransferor.add(propertyMutation.getBasicProperty().getUpicNo());
                argsForTransferor.add(transferOwnerService.getCityName());
                emailBodyTransferor = getText("body.createtransferproperty", argsForTransferor);
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
                messagingService.sendEmail(transferor.getEmailId(), subject, emailBodyTransferor);
        for (final PropertyMutationTransferee transferee : propertyMutation.getTransfereeInfos())
            if (StringUtils.isNotBlank(transferee.getTransferee().getEmailId()) && StringUtils.isNotBlank(subject)
                    && StringUtils.isNotBlank(emailBodyTransferee))
                messagingService.sendEmail(transferee.getTransferee().getEmailId(), subject, emailBodyTransferee);
    }

    private String getNatureOfTask() {
        final String nature = ADDTIONAL_RULE_REGISTERED_TRANSFER.equals(getAdditionalRule())
                ? NATURE_REGISTERED_TRANSFER
                : ADDTIONAL_RULE_FULL_TRANSFER.equals(getAdditionalRule())
                        ? NATURE_FULL_TRANSFER
                        : PropertyTaxConstants.ADDTIONAL_RULE_PARTIAL_TRANSFER
                                .equals(getAdditionalRule())
                                        ? NATURE_PARTIAL_TRANSFER
                                        : "PropertyMutation";
        return nature;
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

    public List<Hashtable<String, Object>> getHistoryMap() {
        return historyMap;
    }

    public void setHistoryMap(final List<Hashtable<String, Object>> historyMap) {
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
}