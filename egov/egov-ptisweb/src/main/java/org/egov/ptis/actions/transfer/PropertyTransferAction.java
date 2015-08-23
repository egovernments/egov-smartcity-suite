/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.ptis.actions.transfer;

import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.GUARDIAN_RELATION;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_CLERK_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_INSPECTOR_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.TARGET_WORKFLOW_ERROR;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_READY_FOR_PAYMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_APPROVE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_COMMISSIONER_APPROVED;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_REJECTED;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_REVENUE_CLERK_APPROVAL_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_REVENUE_CLERK_APPROVED;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.messaging.MessagingService;
import org.egov.infra.reporting.engine.ReportConstants;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.web.utils.WebUtils;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.workflow.WorkFlowMatrix;
import org.egov.pims.commons.Position;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Document;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.domain.service.transfer.PropertyTransferService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;

@Results({
        @Result(name = BaseFormAction.NEW, location = "transfer/transferProperty-new.jsp"),
        @Result(name = BaseFormAction.EDIT, location = "transfer/transferProperty-edit.jsp"),
        @Result(name = BaseFormAction.VIEW, location = "transfer/transferProperty-view.jsp"),
        @Result(name = TARGET_WORKFLOW_ERROR, location = "workflow/workflow-error.jsp"),
        @Result(name = PropertyTransferAction.ACK, location = "transfer/transferProperty-ack.jsp"),
        @Result(name = PropertyTransferAction.REJECT_ON_TAXDUE, location = "transfer/transferProperty-balance.jsp"),
        @Result(name = PropertyTransferAction.PRINTACK, location = "transfer/transferProperty-printAck.jsp"),
        @Result(name = PropertyTransferAction.PRINTNOTICE, location = "transfer/transferProperty-printNotice.jsp"),
        @Result(name = PropertyTransferAction.SEARCH, location = "transfer/transferProperty-search.jsp"),
        @Result(name = PropertyTransferAction.COLLECT_FEE, location = "collection/collectPropertyTax-view.jsp"),
        @Result(name = PropertyTransferAction.REDIRECT_SUCCESS, location = PropertyTransferAction.REDIRECT_SUCCESS, type = "redirectAction", params = {
                "assessmentNo", "${assessmentNo}", "mutationId", "${mutationId}" }) })
@Namespace("/property/transfer")
public class PropertyTransferAction extends GenericWorkFlowAction {

    private static final String PROPERTY_TRANSFER = "property transfer";
    private static final long serialVersionUID = 1L;
    public static final String ACK = "ack";
    public static final String SEARCH = "search";
    public static final String REJECT_ON_TAXDUE = "balance";
    public static final String PRINTACK = "printAck";
    public static final String PRINTNOTICE = "printNotice";
    public static final String REDIRECT_SUCCESS = "redirect-success";
    public static final String COLLECT_FEE = "collect-fee";

    // Form Binding Model
    private PropertyMutation propertyMutation = new PropertyMutation();

    // Dependent Services
    @Autowired
    @Qualifier("transferOwnerService")
    private PropertyTransferService transferOwnerService;

    @Autowired
    protected AssignmentService assignmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private SimpleWorkflowService<PropertyMutation> transferWorkflowService;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private MessagingService messagingService;

    // Model and View data
    private Long mutationId;
    private String assessmentNo;
    private String wfErrorMsg;
    private BigDecimal currentPropertyTax;
    private BigDecimal currentPropertyTaxDue;
    private BigDecimal currentWaterTaxDue;
    private BigDecimal arrearPropertyTaxDue;
    private List<DocumentType> documentTypes = new ArrayList<>();
    private BasicProperty basicproperty; // Do not change variable name, struts2
    // crazy.
    private Integer reportId = -1;
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
    private String userDesignation;

    private Map<String, String> guardianRelationMap;

    public PropertyTransferAction() {
        addRelatedEntity("mutationReason", PropertyMutationMaster.class);
    }

    @SkipValidation
    @Action(value = "/new")
    public String showNewTransferForm() {
        if (basicproperty.isUnderWorkflow()) {
			List<String> msgParams = new ArrayList<String>();
			msgParams.add("Transfer of Ownership");
            wfErrorMsg = getText("wf.pending.msg", msgParams);
            return TARGET_WORKFLOW_ERROR;
        } else {
            currentWaterTaxDue = propertyService.getWaterTaxDues(assessmentNo);
            if (currentWaterTaxDue.add(currentPropertyTaxDue).add(arrearPropertyTaxDue).longValue() > 0) {
                setTaxDueErrorMsg(getText("taxdues.error.msg", new String[] { PROPERTY_TRANSFER }));
                return REJECT_ON_TAXDUE;
            } else
                return NEW;
        }
    }

    @ValidationErrorPage(value = NEW)
    @Action(value = "/save")
    public String save() {
        transitionWorkFlow(propertyMutation);
        transferOwnerService.initiatePropertyTransfer(basicproperty, propertyMutation);
        buildSMS(propertyMutation);
        buildEmail(propertyMutation);
        propertyService.updateIndexes(propertyMutation, APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP);
        setAckMessage("Transfer of ownership data saved successfully in the system and forwarded to : ");
        setAssessmentNoMessage(" with assessment number : ");
        return ACK;
    }

    @SkipValidation
    @Action(value = "/view")
    public String view() {
        final String currState = propertyMutation.getState().getValue();
        final String userDesignation = transferOwnerService.getLoggedInUserDesignation();
        if (currState.endsWith(WF_STATE_REJECTED) || REVENUE_INSPECTOR_DESGN.equalsIgnoreCase(userDesignation)) {
            mode = EDIT;
            return EDIT;
        } else {
            mode = VIEW;
            return VIEW;
        }

    }

    @SkipValidation
    @Action(value = "/search")
    public String search() {
        return SEARCH;
    }

    @SkipValidation
    @Action(value = "/collect-fee")
    public String collectFee() {
        if (StringUtils.isNotBlank(assessmentNo))
            propertyMutation = transferOwnerService.getCurrentPropertyMutationByAssessmentNo(assessmentNo);
        else if (StringUtils.isNotBlank(applicationNo))
            propertyMutation = transferOwnerService.getPropertyMutationByApplicationNo(applicationNo);
        if (null == propertyMutation) {
            addActionError("There is no property transfer exist for the given Assessment No / Application No");
            return SEARCH;
        } else if (null != propertyMutation && null != propertyMutation.getReceiptDate()) {
            final SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            addActionError("Payment has been made for property On Date : "
                    + df.format(propertyMutation.getReceiptDate()));
            return SEARCH;
        } else
            collectXML = transferOwnerService.generateReceipt(propertyMutation);
        return COLLECT_FEE;
    }

    @SkipValidation
    @Action(value = "/forward")
    public String forward() {
        if (mode.equals(EDIT)) {
            validate();
            if (hasErrors()) {
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
        propertyService.updateIndexes(propertyMutation, APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP);
        setAssessmentNoMessage(" with assessment number : ");
        return ACK;
    }

    @SkipValidation
    @Action(value = "/reject")
    public String reject() {
        transitionWorkFlow(propertyMutation);
        transferOwnerService.viewPropertyTransfer(basicproperty, propertyMutation);
        buildSMS(propertyMutation);
        buildEmail(propertyMutation);
        propertyService.updateIndexes(propertyMutation, APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP);
        if (propertyService.isEmployee(propertyMutation.getCreatedBy()))
            mutationInitiatedBy = propertyMutation.getCreatedBy().getName();
        else
            mutationInitiatedBy = assignmentService
            .getPrimaryAssignmentForPositon(
                            propertyMutation.getStateHistory().get(0).getOwnerPosition().getId()).getEmployee()
                    .getUsername();
        if (propertyMutation.getState().getValue().equals("Closed")) {
            mutationInitiatedBy = transferOwnerService.getLoggedInUser().getUsername();
            setAckMessage("Transfer of ownership data rejected successfuly By ");
        } else
            setAckMessage("Transfer of ownership data rejected successfuly and forwarded to initiator : ");
        setAssessmentNoMessage(" with assessment number : ");
        return ACK;
    }

    @ValidationErrorPage(value = EDIT)
    @Action(value = "/approve")
    public String approve() {
        transitionWorkFlow(propertyMutation);
        transferOwnerService.approvePropertyTransfer(basicproperty, propertyMutation);
        approverName = "";
        if (propertyService.isEmployee(propertyMutation.getCreatedBy()))
            mutationInitiatedBy = propertyMutation.getCreatedBy().getName();
        else
            mutationInitiatedBy = assignmentService
            .getPrimaryAssignmentForPositon(
                            propertyMutation.getStateHistory().get(0).getOwnerPosition().getId()).getEmployee()
                    .getUsername();
        buildSMS(propertyMutation);
        buildEmail(propertyMutation);
        propertyService.updateIndexes(propertyMutation, APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP);
        setAckMessage("Transfer of ownership is created successfully in the system and forwarded to : ");
        setAssessmentNoMessage(" for notice generation for the property : ");
        return ACK;
    }

    @SkipValidation
    @Action(value = "/printAck")
    public String printAck() {
        final HttpServletRequest request = ServletActionContext.getRequest();
        final String cityLogo = WebUtils.extractRequestDomainURL(request, false)
                .concat(PropertyTaxConstants.IMAGES_BASE_PATH)
                .concat(request.getSession().getAttribute("citylogo").toString());
        final String cityName = request.getSession().getAttribute("cityname").toString();
        getSession().remove(ReportConstants.ATTRIB_EGOV_REPORT_OUTPUT_MAP);
        reportId = ReportViewerUtil.addReportToSession(
                transferOwnerService.generateAcknowledgement(basicproperty, propertyMutation, cityName, cityLogo),
                getSession());
        return PRINTACK;
    }

    @SkipValidation
    @Action(value = "/printNotice")
    public String printNotice() {
        reportId = ReportViewerUtil.addReportToSession(
                transferOwnerService.generateTransferNotice(basicproperty, propertyMutation), getSession());
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
        userDesignation = transferOwnerService.getUserDesigantion().getName();
        propertyByEmployee = propertyService.isEmployee(transferOwnerService.getLoggedInUser());
        final String actionInvoked = ActionContext.getContext().getActionInvocation().getProxy().getMethod();
        if (!(actionInvoked.equals("search") || actionInvoked.equals("collectFee"))) {
            if (StringUtils.isNotBlank(assessmentNo) && mutationId == null)
                basicproperty = transferOwnerService.getBasicPropertyByUpicNo(assessmentNo);

            if (mutationId != null) {
                propertyMutation = transferOwnerService.find("From PropertyMutation where id = ? ", mutationId);
                basicproperty = propertyMutation.getBasicProperty();
            }

            final Map<String, BigDecimal> propertyTaxDetails = propertyService
                    .getCurrentPropertyTaxDetails(basicproperty.getActiveProperty());
            currentPropertyTax = propertyTaxDetails.get(CURR_DMD_STR);
            currentPropertyTaxDue = propertyTaxDetails.get(CURR_DMD_STR)
                    .subtract(propertyTaxDetails.get(CURR_COLL_STR));
            arrearPropertyTaxDue = propertyTaxDetails.get(ARR_DMD_STR).subtract(propertyTaxDetails.get(ARR_COLL_STR));
            documentTypes = transferOwnerService.getPropertyTransferDocumentTypes();
            addDropdownData("MutationReason", transferOwnerService.getPropertyTransferReasons());
            setGuardianRelationMap(GUARDIAN_RELATION);
        }
    }

    @Override
    public void validate() {
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

        if (propertyMutation.getTransfereeInfos().isEmpty())
            addActionError("Transfree info is mandatory, add atleast one transferee info.");
        else
            for (final User propOwnerInfo : propertyMutation.getTransfereeInfos()) {
                if (StringUtils.isBlank(propOwnerInfo.getName()))
                    addActionError(getText("mandatory.ownerName"));
                if (StringUtils.isBlank(propOwnerInfo.getMobileNumber()))
                    addActionError(getText("mandatory.mobilenumber"));
            }

        if (getMutationId() != null && !userDesignation.equalsIgnoreCase(REVENUE_CLERK_DESGN) ) {
            if (propertyMutation.getMutationFee() == null)
                addActionError(getText("mandatory.mutationFee"));
            else if (propertyMutation.getMutationFee().compareTo(BigDecimal.ZERO) < 1)
                addActionError(getText("madatory.mutFeePos"));
            if (propertyMutation.getMarketValue() == null)
                addActionError("Market Value is mandatory");
            else if (propertyMutation.getMarketValue().compareTo(BigDecimal.ZERO) < 1)
                addActionError("Please enter a valid Market Value");
        }

        super.validate();
    }

    public void transitionWorkFlow(final PropertyMutation propertyMutation) {
        final DateTime currentDate = new DateTime();
        final User user = transferOwnerService.getLoggedInUser();
        final Assignment userAssignment = assignmentService.getPrimaryAssignmentForUser(user.getId());
        Position pos = null;
        Assignment wfInitiator = null;

        if (!propertyByEmployee) {
            currentState = "Created";
            final Assignment assignment = propertyService.getUserPositionByZone(basicproperty);
            approverPositionId = assignment.getPosition().getId();
            approverName = assignment.getEmployee().getUsername();
        } else
            currentState = null;

        if (null != propertyMutation.getId())
            if (propertyService.isEmployee(propertyMutation.getCreatedBy()))
                wfInitiator = assignmentService.getPrimaryAssignmentForUser(propertyMutation.getCreatedBy().getId());
            else if (!propertyMutation.getStateHistory().isEmpty())
                wfInitiator = assignmentService.getPrimaryAssignmentForPositon(propertyMutation.getStateHistory()
                        .get(0).getOwnerPosition().getId());
            else
                wfInitiator = assignmentService.getPrimaryAssignmentForPositon(propertyMutation.getState()
                        .getOwnerPosition().getId());

        if (WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction)) {
            if (wfInitiator.equals(userAssignment))
                propertyMutation.transition(true).end().withSenderName(user.getName()).withComments(approverComments)
                .withDateInfo(currentDate.toDate());
            else {
                final String stateValue = WF_STATE_REJECTED;
                propertyMutation.transition(true).withSenderName(user.getName()).withComments(approverComments)
                .withStateValue(stateValue).withDateInfo(currentDate.toDate())
                .withOwner(wfInitiator.getPosition()).withNextAction(WF_STATE_REVENUE_CLERK_APPROVAL_PENDING);
            }

        } else {
            if (null != approverPositionId && approverPositionId != -1)
                pos = (Position) persistenceService.find("from Position where id=?", approverPositionId);
            else if (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workFlowAction))
                pos = wfInitiator.getPosition();
            if (null == propertyMutation.getState()) {
                final WorkFlowMatrix wfmatrix = transferWorkflowService.getWfMatrix(propertyMutation.getStateType(),
                        null, null, getAdditionalRule(), currentState, null);
                propertyMutation.transition().start().withSenderName(user.getName()).withComments(approverComments)
                .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction());
            } else if (propertyMutation.getCurrentState().getNextAction().equalsIgnoreCase("END"))
                propertyMutation.transition(true).end().withSenderName(user.getName()).withComments(approverComments)
                .withDateInfo(currentDate.toDate());
            else {
                final WorkFlowMatrix wfmatrix = transferWorkflowService.getWfMatrix(propertyMutation.getStateType(),
                        null, null, getAdditionalRule(), propertyMutation.getCurrentState().getValue(), null);
                propertyMutation.transition(true).withSenderName(user.getName()).withComments(approverComments)
                .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction());
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
        String transferorMobileNumber = propertyMutation.getPrimaryTransferor().getMobileNumber();
        final String transfereeMobileNumber = propertyMutation.getPrimaryTransferee().getMobileNumber();
        final List<String> argsForTransferor = new ArrayList<String>();
        final List<String> argsForTransferee = new ArrayList<String>();
        String smsMsgForTransferor = "";
        String smsMsgForTransferee = "";
        if (null != propertyMutation && null != propertyMutation.getState()) {
            final State mutationState = propertyMutation.getState();
            if (mutationState.getValue().equals(WF_STATE_REVENUE_CLERK_APPROVED)) {
                argsForTransferor.add(propertyMutation.getFullTranferorName());
                argsForTransferor.add(propertyMutation.getBasicProperty().getUpicNo());
                argsForTransferor.add(transferOwnerService.getCityName());
                argsForTransferee.add(propertyMutation.getFullTranfereeName());
                argsForTransferee.add(propertyMutation.getBasicProperty().getUpicNo());
                argsForTransferee.add(transferOwnerService.getCityName());
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
            } else if (mutationState.getNextAction().equals(WFLOW_ACTION_READY_FOR_PAYMENT)) {
                argsForTransferee.add(propertyMutation.getFullTranfereeName());
                argsForTransferee.add(propertyMutation.getMarketValue().toString());
                argsForTransferee.add(propertyMutation.getMutationFee().toString());
                transferorMobileNumber = "";
                smsMsgForTransferee = getText("msg.paymenttransferproperty.sms", argsForTransferee);
            } else if (mutationState.getValue().equals(WF_STATE_COMMISSIONER_APPROVED)) {
                argsForTransferor.add(propertyMutation.getFullTranferorName());
                argsForTransferor.add(propertyMutation.getFullTranferorName());
                argsForTransferor.add(propertyMutation.getFullTranfereeName());
                argsForTransferor.add(propertyMutation.getBasicProperty().getUpicNo());
                argsForTransferor.add(transferOwnerService.getCityName());
                smsMsgForTransferor = getText("msg.approvetransferproperty.sms", argsForTransferor);
                argsForTransferee.add(propertyMutation.getFullTranfereeName());
                argsForTransferee.add(propertyMutation.getFullTranferorName());
                argsForTransferee.add(propertyMutation.getFullTranfereeName());
                argsForTransferee.add(propertyMutation.getBasicProperty().getUpicNo());
                argsForTransferee.add(transferOwnerService.getCityName());
                smsMsgForTransferee = getText("msg.approvetransferproperty.sms", argsForTransferee);
            }
        }
        messagingService.sendSMS(transferorMobileNumber, smsMsgForTransferor);
        messagingService.sendSMS(transfereeMobileNumber, smsMsgForTransferee);
    }

    public void buildEmail(final PropertyMutation propertyMutation) {
        String transferorEmailId = propertyMutation.getPrimaryTransferor().getEmailId();
        final String transfereeEmailId = propertyMutation.getPrimaryTransferee().getEmailId();
        String emailBodyTransferor = "";
        String emailBodyTransferee = "";
        String subject = "";
        final List<String> argsForTransferor = new ArrayList<String>();
        final List<String> argsForTransferee = new ArrayList<String>();
        if (null != propertyMutation && null != propertyMutation.getState()) {
            final State mutationState = propertyMutation.getState();
            if (mutationState.getValue().equals(WF_STATE_REVENUE_CLERK_APPROVED)) {
                subject = getText("subject.createtransferproperty", propertyMutation.getBasicProperty().getUpicNo());
                argsForTransferor.add(propertyMutation.getFullTranferorName());
                argsForTransferor.add(propertyMutation.getBasicProperty().getUpicNo());
                argsForTransferor.add(transferOwnerService.getCityName());
                emailBodyTransferor = getText("body.createtransferproperty", argsForTransferor);
                argsForTransferee.add(propertyMutation.getFullTranfereeName());
                argsForTransferee.add(propertyMutation.getBasicProperty().getUpicNo());
                argsForTransferee.add(transferOwnerService.getCityName());
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
            } else if (mutationState.getNextAction().equals(WFLOW_ACTION_READY_FOR_PAYMENT)) {
                subject = getText("subject.paymenttransferproperty");
                argsForTransferee.add(propertyMutation.getFullTranfereeName());
                argsForTransferee.add(propertyMutation.getMarketValue().toString());
                argsForTransferee.add(propertyMutation.getMutationFee().toString());
                argsForTransferee.add(transferOwnerService.getCityName());
                transferorEmailId = "";
                emailBodyTransferee = getText("body.paymenttransferproperty", argsForTransferee);
            } else if (mutationState.getValue().equals(WF_STATE_COMMISSIONER_APPROVED)) {
                subject = getText("subject.approvetransferproperty");
                argsForTransferor.add(propertyMutation.getFullTranferorName());
                argsForTransferor.add(propertyMutation.getFullTranferorName());
                argsForTransferor.add(propertyMutation.getFullTranfereeName());
                argsForTransferor.add(propertyMutation.getBasicProperty().getUpicNo());
                argsForTransferor.add(transferOwnerService.getCityName());
                argsForTransferee.add(propertyMutation.getFullTranfereeName());
                argsForTransferee.add(propertyMutation.getFullTranferorName());
                argsForTransferee.add(propertyMutation.getFullTranfereeName());
                argsForTransferee.add(propertyMutation.getBasicProperty().getUpicNo());
                argsForTransferee.add(transferOwnerService.getCityName());
                emailBodyTransferor = getText("body.approvetransferproperty", argsForTransferor);
                emailBodyTransferee = getText("body.approvetransferproperty", argsForTransferee);
            }
        }
        messagingService.sendEmail(transferorEmailId, subject, emailBodyTransferor);
        messagingService.sendEmail(transfereeEmailId, subject, emailBodyTransferee);
    }

    public BigDecimal getCurrentPropertyTax() {
        return currentPropertyTax;
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

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(final Integer reportId) {
        this.reportId = reportId;
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

    public String getUserDesignation() {
        return userDesignation;
    }

    public void setUserDesignation(String userDesignation) {
        this.userDesignation = userDesignation;
    }
    
}
