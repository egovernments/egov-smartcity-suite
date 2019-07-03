package org.egov.ptis.domain.service.writeOff;

import static java.lang.Boolean.FALSE;
import static org.egov.ptis.constants.PropertyTaxConstants.*;
import java.text.*;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.*;
import org.egov.infra.admin.master.entity.*;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.notification.service.NotificationService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.ApplicationNumberGenerator;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Position;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.enums.TransactionType;
import org.egov.ptis.domain.entity.property.*;
import org.egov.ptis.domain.repository.writeOff.WriteOffRepository;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
@Transactional(readOnly = true)
public class WriteOffService {

	@Autowired
	private WriteOffRepository writeOffRepo;
	@Autowired
	private PropertyTaxUtil propertyTaxUtil;
	@Autowired
	@Qualifier("parentMessageSource")
	private MessageSource ptisMessageSource;
	@Autowired
	private NotificationService notificationService;
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
	@Qualifier("workflowService")
	private SimpleWorkflowService<PropertyImpl> propertyWorkflowService;
	@Autowired
	private ApplicationNumberGenerator applicationNo;
	private Logger logger = Logger.getLogger(getClass());

	@PersistenceContext
	EntityManager entityManager;
	private List<Installment> fromInstallments;

	private Session getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}

	public void addModelAttributes(final Model model, final BasicProperty basicProperty,
			final HttpServletRequest request) {

		Property property = null;
		if (null != basicProperty.getProperty())
			property = basicProperty.getActiveProperty();
		final DateFormat dateFormat = new SimpleDateFormat(PropertyTaxConstants.DATE_FORMAT_DDMMYYY);
		try {
			fromInstallments = propertyTaxUtil
					.getInstallmentListByStartDateToCurrFinYearDesc(dateFormat.parse("01/04/1963"));
		} catch (final ParseException e) {
			throw new ApplicationRuntimeException("Error while getting all installments from start date", e);
		}
		model.addAttribute("fromInstallments", fromInstallments);
		model.addAttribute("toInstallments", fromInstallments);

		List<Map<String, Object>> wcDetails = propertyService.getWCDetails(basicProperty.getUpicNo(), request);
		model.addAttribute("wcDetails", wcDetails);

	}

	@Transactional
	public WriteOff saveWriteOff(WriteOff writeOff, Long approvalPosition, final String approvalComent,
			final String additionalRule, final String workFlowAction, final Boolean propertyByEmployee) {
		final User user = securityUtils.getCurrentUser();
		final DateTime currentDate = new DateTime();
		Position pos = null;
		Assignment wfInitiator = null;
		String currentState;
		Assignment assignment;
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
		}
		if (SOURCE_ONLINE.equalsIgnoreCase(writeOff.getSource()) && ApplicationThreadLocals.getUserId() == null)
			ApplicationThreadLocals.setUserId(securityUtils.getCurrentUser().getId());
		if (propertyService.isCitizenPortalUser(user) || !propertyByEmployee
				|| ANONYMOUS_USER.equalsIgnoreCase(user.getName())) {
			currentState = "Created";
			assignment = propertyService.getUserPositionByZone(writeOff.getBasicProperty(), false);
			wfInitiator = assignment;
			if (null != assignment)
				approvalPosition = assignment.getPosition().getId();
		} else {
			currentState = "Created";
			if (null != approvalPosition && approvalPosition != 0) {
				assignment = assignmentService.getAssignmentsForPosition(approvalPosition, new Date()).get(0);
				assignment.getEmployee().getName().concat("~").concat(assignment.getPosition().getName());
				approverDesignation = assignment.getDesignation().getName();
			}
		}
		if (writeOff.getState() != null)
			loggedInUserDesignation = getLoggedInUserDesignation(writeOff.getCurrentState().getOwnerPosition().getId(),
					securityUtils.getCurrentUser());
		if (WFLOW_ACTION_STEP_FORWARD.equalsIgnoreCase(workFlowAction)
				&& (writeOff.getId() == null || REVENUE_OFFICER_DESGN.contains(loggedInUserDesig))
				&& (COMMISSIONER_DESIGNATIONS.contains(approverDesignation))) {

			final String designation = approverDesignation.split(" ")[0];
			nextAction = getWorkflowNextAction(designation);
		}

		if (writeOff.getId() != null && writeOff.getState() != null)
			wfInitiator = propertyService.getWorkflowInitiator(writeOff.getBasicProperty().getActiveProperty());
		else if (wfInitiator == null)
			wfInitiator = propertyTaxCommonUtils.getWorkflowInitiatorAsRO(user.getId());

		if (WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction)) {
			if (wfInitiator.getPosition().equals(writeOff.getState().getOwnerPosition())) {
				writeOff.transition().end().withSenderName(user.getUsername() + "::" + user.getName())
						.withComments(approvalComent).withDateInfo(currentDate.toDate()).withNextAction(null)
						.withOwner(writeOff.getState().getOwnerPosition());
				writeOff.setStatus(STATUS_CANCELLED);
				writeOff.getBasicProperty().setUnderWorkflow(FALSE);
			} else {
				final Assignment assignmentOnreject = getUserAssignmentOnReject(loggedInUserDesignation, writeOff);
				if (assignmentOnreject != null) {
					nextAction = "Revenue Officer Approval Pending";
					wfInitiator = assignmentOnreject;
				} else
					nextAction = WF_STATE_REVENUE_OFFICER_APPROVAL_PENDING;
				final String stateValue = writeOff.getCurrentState().getValue().split(":")[0] + ":" + WF_STATE_REJECTED;
				writeOff.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
						.withComments(approvalComent).withStateValue(stateValue).withDateInfo(currentDate.toDate())
						.withOwner(wfInitiator.getPosition()).withNextAction(nextAction);
				buildSMS(writeOff, workFlowAction);
			}

		} else {
			if (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workFlowAction))
				pos = writeOff.getCurrentState().getOwnerPosition();
			else if (null != approvalPosition && approvalPosition != -1 && !approvalPosition.equals(Long.valueOf(0)))
				pos = positionMasterService.getPositionById(approvalPosition);
			WorkFlowMatrix wfmatrix;
			if (null == writeOff.getState()) {
				wfmatrix = propertyWorkflowService.getWfMatrix(writeOff.getStateType(), null, null, additionalRule,
						currentState, null);
				writeOff.transition().start().withSenderName(user.getUsername() + "::" + user.getName())
						.withComments(approvalComent).withStateValue(wfmatrix.getNextState()).withDateInfo(new Date())
						.withOwner(pos)
						.withNextAction(wfmatrix.getNextAction() == null
								? getNextAction(writeOff.getProperty(), nextAction, workFlowAction)
								: wfmatrix.getNextAction())
						.withNatureOfTask(NATURE_WRITE_OFF)
						.withInitiator(wfInitiator != null ? wfInitiator.getPosition() : null)
						.withSLA(propertyService.getSlaValue(APPLICATION_TYPE_WRITE_OFF));
			} else if (writeOff.getCurrentState().getNextAction().equalsIgnoreCase("END"))
				writeOff.transition().end().withSenderName(user.getUsername() + "::" + user.getName())
						.withComments(approvalComent).withDateInfo(currentDate.toDate()).withNextAction(null)
						.withOwner(writeOff.getCurrentState().getOwnerPosition());
			else {

				wfmatrix = propertyWorkflowService.getWfMatrix(writeOff.getStateType(), null, null, additionalRule,
						writeOff.getCurrentState().getValue(), writeOff.getCurrentState().getNextAction(), null,
						loggedInUserDesignation);
				writeOff.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
						.withComments(approvalComent).withStateValue(wfmatrix.getNextState())
						.withDateInfo(currentDate.toDate()).withOwner(pos)
						.withNextAction(StringUtils.isNotBlank(nextAction) ? nextAction : wfmatrix.getNextAction());

				if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_APPROVE))
					buildSMS(writeOff, workFlowAction);
			}
		}
		return writeOffRepo.save(writeOff);
	}

	private String getNextAction(final PropertyImpl property, final String approverDesignation, String workFlowAction) {
		String nextAction = "";
		if (WFLOW_ACTION_STEP_FORWARD.equalsIgnoreCase(workFlowAction)
				&& COMMISSIONER_DESIGNATIONS.contains(approverDesignation))
			if (property.getCurrentState().getNextAction().equalsIgnoreCase(WF_STATE_DIGITAL_SIGNATURE_PENDING))
				nextAction = WF_STATE_DIGITAL_SIGNATURE_PENDING;
			else {
				final String designation = approverDesignation.split(" ")[0];
				if (designation.equalsIgnoreCase(COMMISSIONER_DESGN))
					nextAction = WF_STATE_COMMISSIONER_APPROVAL_PENDING;
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

	public Assignment getUserAssignmentOnReject(final String loggedInUserDesignation, final WriteOff writeOff) {
		Assignment assignmentOnreject = null;
		if (loggedInUserDesignation.equalsIgnoreCase(REVENUE_OFFICER_DESGN)
				|| loggedInUserDesignation.equalsIgnoreCase(ASSISTANT_COMMISSIONER_DESIGN)
				|| loggedInUserDesignation.equalsIgnoreCase(ADDITIONAL_COMMISSIONER_DESIGN)
				|| loggedInUserDesignation.equalsIgnoreCase(DEPUTY_COMMISSIONER_DESIGN)
				|| loggedInUserDesignation.equalsIgnoreCase(COMMISSIONER_DESGN)
				|| loggedInUserDesignation.equalsIgnoreCase(ZONAL_COMMISSIONER_DESIGN))
			assignmentOnreject = propertyService.getUserOnRejection(writeOff);

		return assignmentOnreject;

	}

	public void buildSMS(final WriteOff writeOff, final String workFlowAction) {
		for (final PropertyOwnerInfo ownerInfo : writeOff.getBasicProperty().getPropertyOwnerInfo())
			if (StringUtils.isNotBlank(ownerInfo.getOwner().getMobileNumber()))
				buildSms(writeOff, ownerInfo.getOwner(), workFlowAction);
	}

	private void buildSms(final WriteOff writeOff, final User user, final String workFlowAction) {
		final String assessmentNo = writeOff.getBasicProperty().getUpicNo();
		final String mobileNumber = user.getMobileNumber();
		final String applicantName = user.getName();
		String smsMsg = "";
		if (workFlowAction.equals(WFLOW_ACTION_STEP_FORWARD)) {
			// to be enabled once acknowledgement feature is developed
			/*
			 * smsMsg = messageSource.getMessage("msg.initiateexemption.sms",
			 * new String[] { applicantName, assessmentNo }, null);
			 */
		} else if (workFlowAction.equals(WFLOW_ACTION_STEP_REJECT))
			smsMsg = ptisMessageSource.getMessage("msg.rejectexemption.sms",
					new String[] { applicantName, assessmentNo, ApplicationThreadLocals.getMunicipalityName() }, null);
		else if (workFlowAction.equals(WFLOW_ACTION_STEP_APPROVE)) {
			final Installment installment = propertyTaxUtil.getInstallmentListByStartDate(new Date()).get(0);
			final Date effectiveDate = org.apache.commons.lang3.time.DateUtils.addDays(installment.getToDate(), 1);
			smsMsg = ptisMessageSource.getMessage("msg.approveexemption.sms",
					new String[] { applicantName, assessmentNo,
							new SimpleDateFormat("dd/MM/yyyy").format(effectiveDate),
							ApplicationThreadLocals.getMunicipalityName() },
					null);
		}

		if (StringUtils.isNotBlank(mobileNumber))
			notificationService.sendSMS(mobileNumber, smsMsg);

	}

	public List<Installment> getInstallmentsByModuleBetweenFromDateAndToDate(final Module module, final Date fromDate,
			final Date toDate) {
		final Query qry = getCurrentSession().createQuery(
				"from Installment I where I.module=:module and I.toDate >=:fromDate and I.fromDate<=:tillDate");
		qry.setEntity("module", module);
		qry.setDate("fromDate", fromDate);
		qry.setDate("tillDate", toDate);
		return qry.list();
	}

	public List<DocumentType> getDocuments(final TransactionType transactionType) {
		return propertyService.getDocumentTypesForTransactionType(transactionType);
	}

}