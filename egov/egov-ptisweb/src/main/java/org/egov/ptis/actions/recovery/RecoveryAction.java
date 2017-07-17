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
/**
 * 
 */
package org.egov.ptis.actions.recovery;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.CFinancialYear;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgDemandReason;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Position;
import org.egov.ptis.client.model.PropertyBillInfo;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.recovery.IntimationNotice;
import org.egov.ptis.domain.entity.recovery.Recovery;
import org.egov.ptis.domain.entity.recovery.Warrant;
import org.egov.ptis.domain.entity.recovery.WarrantFee;
import org.egov.ptis.notice.PtNotice;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_COURT_FEE;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_NOTICE_FEE;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_WARRANT_FEE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_APPROVE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_FORWARD;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_SAVE;

/**
 * @author manoranjan
 * 
 */
@ParentPackage("egov")
@Results({ @Result(name = "invalidUser", location = "workflow", params = { "namespace", "/workflow", "method",
		"inboxItemViewErrorUserInvalid" }) })
public class RecoveryAction extends BaseRecoveryAction {

	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(RecoveryAction.class);
	private Recovery recovery = new Recovery();
	@Autowired
	@Qualifier("ptRecoveryPeristenceService")
	private PersistenceService<Recovery, Long> recoveryService;
	protected WorkflowService<Recovery> recoveryWorkflowService;
	private static String MESSAGE = "message";
	private static String WARRANTAPPLICATIONVIEW = "warrantApplicationView";
	private static String WARRANTAPPLICATIONNEW = "warrantApplicationNew";
	private static String NOTICE156NEW = "notice156New";
	private static String NOTICE156VIEW = "notice156View";
	private static String NOTICE159NEW = "notice159New";
	private static String NOTICE159VIEW = "notice159View";
	private static String PRINT = "print";

	private UserService userService;

	@PersistenceContext
	private EntityManager entityManager;

	public RecoveryAction() {

		addRelatedEntity("basicProperty", BasicPropertyImpl.class);
		addRelatedEntity("bill", EgBill.class);
		addRelatedEntity("intimationNotice", IntimationNotice.class);
		addRelatedEntity("warrant", Warrant.class);
	}

	@Override
	public StateAware getModel() {
		return recovery;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void prepare() {
		// to merge the new values from jsp with existing
		if (recovery.getId() != null) {
			recovery = recoveryService.findById(recovery.getId(), false);
		}
		super.prepare();
		setUserInfo();
	}

	public String newform() {
		String returnStr = MESSAGE;
		recovery.setBasicProperty(getPropertyView(parameters.get("propertyId")[0]));
		Map<String, String> wfMap = recovery.getBasicProperty().getPropertyWfStatus();
		BigDecimal totalArrDue = BigDecimal.valueOf((Double.valueOf(viewMap.get("totalArrDue").toString())));
		StringBuffer consumerId = new StringBuffer();
		consumerId.append(recovery.getBasicProperty().getUpicNo()).append("(Zone:")
				.append(recovery.getBasicProperty().getPropertyID().getZone().getBoundaryNum()).append(" Ward:")
				.append(recovery.getBasicProperty().getPropertyID().getWard().getBoundaryNum()).append(")");

		EgBill bill = getBil(consumerId.toString());

		if (wfMap.get(PropertyTaxConstants.WFSTATUS).equalsIgnoreCase(Boolean.TRUE.toString())
				&& StringUtils.isNotEmpty(wfMap.get(PropertyTaxConstants.WFOWNER))) {
			addActionMessage(getText("property.state.recovery"));
		} else if (wfMap.get(PropertyTaxConstants.WFSTATUS).equalsIgnoreCase(Boolean.TRUE.toString())
				&& StringUtils.isEmpty(wfMap.get(PropertyTaxConstants.WFOWNER))) {
			addActionMessage(getText("property.state.in.recovery"));
		} else if (null == bill) {
			addActionMessage(getText("bill.not.generated"));
		} else if (totalArrDue.compareTo(BigDecimal.ZERO) == 0) {
			addActionMessage(getText("tax.arrears.not"));
		} else {
			setupWorkflowDetails();
			recovery.setBill(bill);
			returnStr = NEW;
		}

		return returnStr;
	}

	@ValidationErrorPage(value = "new")
	public String startRecovery() {
		LOGGER.debug("RecoveryAction | startRecovery | Start" + recovery);
		setupWorkflowDetails();
		validateStartRecovery(recovery);
		recovery.getBasicProperty().setStatus(getPropStatusByStatusCode("NOTICE155ISSUED"));
		recovery.setStatus(getEgwStatusForModuleAndCode(PropertyTaxConstants.RECOVERY_MODULE,
				PropertyTaxConstants.RECOVERY_NOTICE155CREATED));
		IntimationNotice intimationNotice = recovery.getIntimationNotice();
		intimationNotice.setRecovery(recovery);
		recoveryService.persist(recovery);
		// Position position =
		// eisCommonsManager.getPositionByUserId(Integer.valueOf(ApplicationThreadLocals.getUserId()));
		Position position = null;
		recovery.transition().start().withOwner(position);
		updateWfstate("Notice 155");
		addActionMessage(getText("notice155.success"));
		LOGGER.debug("RecoveryAction | startRecovery | end" + recovery);
		return MESSAGE;
	}

	public String view() {
		if (!authenticateInboxItemRqst(recovery.getState())) {
			return "invalidUser";
		}
		getPropertyView(recovery.getBasicProperty().getUpicNo());
		setupWorkflowDetails();
		if (recovery.getStatus().getCode().equalsIgnoreCase(PropertyTaxConstants.RECOVERY_NOTICE155GENERATED)) {
			return WARRANTAPPLICATIONNEW;
		} else if (recovery.getStatus().getCode().equalsIgnoreCase(PropertyTaxConstants.RECOVERY_WARRANTPREPARED)) {
			return WARRANTAPPLICATIONVIEW;
		} else if (recovery.getStatus().getCode().equalsIgnoreCase(PropertyTaxConstants.RECOVERY_WARRANTAPPROVED)) {
			return NOTICE156NEW;
		} else if (recovery.getStatus().getCode().equalsIgnoreCase(PropertyTaxConstants.RECOVERY_WARRANTNOTICECREATED)) {
			return NOTICE156VIEW;
		} else if (recovery.getStatus().getCode().equalsIgnoreCase(PropertyTaxConstants.RECOVERY_WARRANTNOTICEISSUED)) {
			return NOTICE159NEW;
		} else if (recovery.getStatus().getCode().equalsIgnoreCase(PropertyTaxConstants.RECOVERY_CEASENOTICECREATED)) {
			return NOTICE159VIEW;
		}
		return "view";
	}

	@ValidationErrorPage(value = "view")
	public String updateWf() {
		updateWfstate(recovery.getStatus().getDescription());
		return MESSAGE;
	}

	@ValidationErrorPage(value = "view")
	public String generateNotice155() {
		LOGGER.debug("RecoveryAction | generateNotice155 | start" + recovery.getIntimationNotice());
		String noticeNo = propertyTaxNumberGenerator.generateRecoveryNotice(PropertyTaxConstants.NOTICE155);
		recovery.setStatus(getEgwStatusForModuleAndCode(PropertyTaxConstants.RECOVERY_MODULE,
				PropertyTaxConstants.RECOVERY_NOTICE155GENERATED));
		updateWfstate("Notice 155 Generated");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		CFinancialYear currentFinancialYear = propertyTaxUtil.getFinancialYearforDate(getCurrentDate());
		String currFinYear = currentFinancialYear.getFinYearRange();
		paramMap.put("paasoon", currFinYear);
		paramMap.put("currentDate", DDMMYYYYFORMATS.format(getCurrentDate()));
		paramMap.put("noticeNo", noticeNo);
		PropertyTaxUtil propertyTaxUtil = new PropertyTaxUtil();
		Map<String, Map<String, BigDecimal>> reasonwiseDues = propertyTaxUtil.getDemandDues(recovery.getBasicProperty()
				.getUpicNo());
		PropertyBillInfo propertyBillInfo = new PropertyBillInfo(reasonwiseDues, recovery.getBasicProperty(), null);
		ReportRequest reportRequest = new ReportRequest("Notice-155", propertyBillInfo, paramMap);
		reportRequest.setPrintDialogOnOpenReport(true);
		ReportOutput reportOutput = reportService.createReport(reportRequest);
		reportId = addingReportToSession(reportOutput);
		if (reportOutput != null && reportOutput.getReportOutputData() != null) {
			InputStream Notice155PDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
			PtNotice ptNotice = noticeService.saveNotice(null,noticeNo, PropertyTaxConstants.NOTICE155,
					recovery.getBasicProperty(), Notice155PDF);
			recovery.getIntimationNotice().setNotice(ptNotice);
		}
		LOGGER.debug("RecoveryAction | generateNotice155 | end" + recovery.getIntimationNotice());
		return PRINT;

	}

	@ValidationErrorPage(value = "warrantApplicationNew")
	public String warrantApplication() {
		LOGGER.debug("RecoveryAction | warrantApplication | Start");
		entityManager.unwrap(Session.class).setFlushMode(FlushMode.MANUAL);
		setupWorkflowDetails();
		List<WarrantFee> warrantFess = new LinkedList<WarrantFee>();
		for (WarrantFee warrantFee : recovery.getWarrant().getWarrantFees()) {
			EgDemandReason demandReason = (EgDemandReason) persistenceService.find(" from EgDemandReason where id="
					+ warrantFee.getDemandReason().getId());
			warrantFee.setDemandReason(demandReason);
			warrantFess.add(warrantFee);
			LOGGER.debug("RecoveryAction | warrantApplication | Warrant Fee" + warrantFee);
		}
		recovery.getWarrant().getWarrantFees().clear();
		recovery.getWarrant().getWarrantFees().addAll(warrantFess);
		recovery.getBasicProperty().setStatus(getPropStatusByStatusCode(PropertyTaxConstants.RECOVERY_WARRANTPREPARED));
		recovery.setStatus(getEgwStatusForModuleAndCode(PropertyTaxConstants.RECOVERY_MODULE,
				PropertyTaxConstants.RECOVERY_WARRANTPREPARED));
		updateWfstate("Warrant Application");
		LOGGER.debug("RecoveryAction | warrantApplication | end" + recovery.getWarrant());
		entityManager.unwrap(Session.class).flush();
		addActionMessage(getText("warrantApp.success"));

		return MESSAGE;
	}

	@ValidationErrorPage(value = "warrantApplicationView")
	public String generateWarrantApplicaton() {
		String noticeNo = propertyTaxNumberGenerator.generateRecoveryNotice(PropertyTaxConstants.WARRANT_APPLICATION);
		recovery.getBasicProperty().setStatus(getPropStatusByStatusCode(PropertyTaxConstants.RECOVERY_WARRANTAPPROVED));
		recovery.setStatus(getEgwStatusForModuleAndCode(PropertyTaxConstants.RECOVERY_MODULE,
				PropertyTaxConstants.RECOVERY_WARRANTAPPROVED));

		updateWfstate(PropertyTaxConstants.WARRANT_APPLICATION);
		BigDecimal courtFee = BigDecimal.ZERO;
		BigDecimal noticeFee = BigDecimal.ZERO;
		BigDecimal warrantFee = BigDecimal.ZERO;
		for (WarrantFee fee : recovery.getWarrant().getWarrantFees()) {
			if (fee.getDemandReason().getEgDemandReasonMaster().getCode().equalsIgnoreCase(DEMANDRSN_CODE_WARRANT_FEE)) {
				warrantFee = fee.getAmount();
			} else if (fee.getDemandReason().getEgDemandReasonMaster().getCode()
					.equalsIgnoreCase(DEMANDRSN_CODE_COURT_FEE)) {
				courtFee = fee.getAmount();
			} else if (fee.getDemandReason().getEgDemandReasonMaster().getCode()
					.equalsIgnoreCase(DEMANDRSN_CODE_NOTICE_FEE)) {
				noticeFee = fee.getAmount();
			}
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		CFinancialYear currentFinancialYear = propertyTaxUtil.getFinancialYearforDate(getCurrentDate());
		String currFinYear = currentFinancialYear.getFinYearRange();
		paramMap.put("paasoon", currFinYear);
		paramMap.put("noticeDate", new Date());
		paramMap.put("billNo", recovery.getBill().getBillNo());
		paramMap.put("warrantFee", warrantFee.toString());
		paramMap.put("courtFee", courtFee.toString());
		paramMap.put("noticeFee", noticeFee.toString());
		paramMap.put("zoneNum", recovery.getBasicProperty().getPropertyID().getZone().getBoundaryNum().toString());
		paramMap.put("noticeNo", noticeNo);
		PropertyTaxUtil propertyTaxUtil = new PropertyTaxUtil();
		Map<String, Map<String, BigDecimal>> reasonwiseDues = propertyTaxUtil.getDemandDues(recovery.getBasicProperty()
				.getUpicNo());
		PropertyBillInfo propertyBillInfo = new PropertyBillInfo(reasonwiseDues, recovery.getBasicProperty(), null);
		BigDecimal totalRecoverAmt = (propertyBillInfo.getGrandTotal().add(courtFee.add(warrantFee))).setScale(2);
		paramMap.put("totalAmt", totalRecoverAmt.toString());
		ReportRequest reportRequest = new ReportRequest(PropertyTaxConstants.WARRANT_APPLICATION, propertyBillInfo,
				paramMap);
		reportRequest.setPrintDialogOnOpenReport(true);
		ReportOutput reportOutput = reportService.createReport(reportRequest);
		reportId = addingReportToSession(reportOutput);
		if (reportOutput != null && reportOutput.getReportOutputData() != null) {
			InputStream warrantApplPDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
			PtNotice ptNotice = noticeService.saveNotice(null,noticeNo, PropertyTaxConstants.WARRANT_APPLICATION,
					recovery.getBasicProperty(), warrantApplPDF);
			recovery.getWarrant().setNotice(ptNotice);
		}
		return PRINT;
	}

	@ValidationErrorPage(value = "notice156New")
	public String warrantNotice() {
		LOGGER.debug("RecoveryAction | warrantNotice | Start" + recovery.getWarrantNotice());
		setupWorkflowDetails();
		validateWarrantNotice(recovery);
		recovery.getBasicProperty().setStatus(
				getPropStatusByStatusCode(PropertyTaxConstants.RECOVERY_WARRANTNOTICECREATED));
		recovery.setStatus(getEgwStatusForModuleAndCode(PropertyTaxConstants.RECOVERY_MODULE,
				PropertyTaxConstants.RECOVERY_WARRANTNOTICECREATED));
		updateWfstate("Notice 156");
		addActionMessage(getText("notice156.success"));
		LOGGER.debug("RecoveryAction | warrantNotice | end" + recovery.getWarrantNotice());
		return MESSAGE;
	}

	public String generateWarrantNotice() {
		LOGGER.debug("RecoveryAction | generateWarrantNotice | Start" + recovery.getWarrantNotice());
		String noticeNo = propertyTaxNumberGenerator.generateRecoveryNotice(PropertyTaxConstants.NOTICE156);
		recovery.getBasicProperty().setStatus(
				getPropStatusByStatusCode(PropertyTaxConstants.RECOVERY_WARRANTNOTICEISSUED));
		recovery.setStatus(getEgwStatusForModuleAndCode(PropertyTaxConstants.RECOVERY_MODULE,
				PropertyTaxConstants.RECOVERY_WARRANTNOTICEISSUED));
		updateWfstate("Notic 156 Generated");
		updateDemand(recovery);
		Map<String, Object> paramMap = getNotice156Param(recovery);
		PropertyTaxUtil propertyTaxUtil = new PropertyTaxUtil();
		Map<String, Map<String, BigDecimal>> reasonwiseDues = propertyTaxUtil.getDemandDues(recovery.getBasicProperty()
				.getUpicNo());
		PropertyBillInfo propertyBillInfo = new PropertyBillInfo(reasonwiseDues, recovery.getBasicProperty(), null);
		String adress = recovery.getBasicProperty().getAddress().toString();
		BigDecimal totalRecoverAmt = propertyBillInfo.getGrandTotal()
				.add(BigDecimal.valueOf(Double.valueOf(paramMap.get("totalWarrantFees").toString()))).setScale(2);
		paramMap.put("adress", adress);
		paramMap.put("zoneNum", recovery.getBasicProperty().getPropertyID().getZone().getBoundaryNum().toString());
		paramMap.put("totalRecoverAmt", totalRecoverAmt.toString());
		ReportRequest reportRequest = new ReportRequest("Warrent", propertyBillInfo, paramMap);
		reportRequest.setPrintDialogOnOpenReport(true);
		ReportOutput reportOutput = reportService.createReport(reportRequest);
		reportId = addingReportToSession(reportOutput);
		if (reportOutput != null && reportOutput.getReportOutputData() != null) {
			InputStream Notice156PDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
			PtNotice ptNotice = noticeService.saveNotice(null,noticeNo, PropertyTaxConstants.NOTICE156,
					recovery.getBasicProperty(), Notice156PDF);
			recovery.getWarrantNotice().setNotice(ptNotice);
		}
		LOGGER.debug("RecoveryAction | generateWarrantNotice | End" + recovery.getWarrantNotice());
		return PRINT;
	}

	@ValidationErrorPage(value = "notice159New")
	public String ceaseNotice() {
		LOGGER.debug("RecoveryAction | ceaseNotice | Start" + recovery.getCeaseNotice());
		setupWorkflowDetails();
		validateCeaseNotice(recovery);
		recovery.getBasicProperty().setStatus(
				getPropStatusByStatusCode(PropertyTaxConstants.RECOVERY_CEASENOTICECREATED));
		recovery.setStatus(getEgwStatusForModuleAndCode(PropertyTaxConstants.RECOVERY_MODULE,
				PropertyTaxConstants.RECOVERY_CEASENOTICECREATED));
		updateWfstate("Notice 159");
		addActionMessage(getText("notice159.success"));
		LOGGER.debug("RecoveryAction | ceaseNotice | end" + recovery.getCeaseNotice());
		return MESSAGE;
	}

	@ValidationErrorPage(value = "notice159View")
	public String generateCeaseNotice() {
		LOGGER.debug("RecoveryAction | generateCeaseNotice | Start" + recovery.getCeaseNotice());
		String noticeNo = propertyTaxNumberGenerator.generateRecoveryNotice(PropertyTaxConstants.NOTICE159);
		recovery.getBasicProperty().setStatus(
				getPropStatusByStatusCode(PropertyTaxConstants.RECOVERY_CEASENOTICEISSUED));
		recovery.setStatus(getEgwStatusForModuleAndCode(PropertyTaxConstants.RECOVERY_MODULE,
				PropertyTaxConstants.RECOVERY_CEASENOTICEISSUED));
		// FIX ME
		// Position position =
		// eisCommonsManager.getPositionByUserId(Integer.valueOf(ApplicationThreadLocals.getUserId()));
		Position position = null;
		recovery.transition().progress().withNextAction("END").withStateValue("END").withOwner(position)
				.withComments(workflowBean.getComments());

		Map<String, Object> paramMap = getNotice159Param(recovery);
		PropertyTaxUtil propertyTaxUtil = new PropertyTaxUtil();
		Map<String, Map<String, BigDecimal>> reasonwiseDues = propertyTaxUtil.getDemandDues(recovery.getBasicProperty()
				.getUpicNo());
		PropertyBillInfo propertyBillInfo = new PropertyBillInfo(reasonwiseDues, recovery.getBasicProperty(), null);
		BigDecimal totalRecoverAmt = propertyBillInfo.getGrandTotal()
				.add(BigDecimal.valueOf(Double.valueOf(paramMap.get("totalWarrantFees").toString()))).setScale(2);
		paramMap.put("totalRecoverAmt", totalRecoverAmt.toString());
		paramMap.put("executionDate", DDMMYYYYFORMATS.format(recovery.getCeaseNotice().getExecutionDate()));
		paramMap.put("currentDate", DDMMYYYYFORMATS.format(new Date()));
		paramMap.put("north", recovery.getBasicProperty().getPropertyID().getNorthBoundary());
		paramMap.put("south", recovery.getBasicProperty().getPropertyID().getSouthBoundary());
		paramMap.put("zoneNum", recovery.getBasicProperty().getPropertyID().getZone().getBoundaryNum().toString());
		paramMap.put("east", recovery.getBasicProperty().getPropertyID().getEastBoundary());
		paramMap.put("west", recovery.getBasicProperty().getPropertyID().getWestBoundary());
		ReportRequest reportRequest = new ReportRequest("Notice-159", propertyBillInfo, paramMap);
		reportRequest.setPrintDialogOnOpenReport(true);
		ReportOutput reportOutput = reportService.createReport(reportRequest);
		reportId = addingReportToSession(reportOutput);
		if (reportOutput != null && reportOutput.getReportOutputData() != null) {
			InputStream Notice159PDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
			PtNotice ptNotice = noticeService.saveNotice(null,noticeNo, PropertyTaxConstants.NOTICE159,
					recovery.getBasicProperty(), Notice159PDF);
			recovery.getCeaseNotice().setNotice(ptNotice);
		}
		LOGGER.debug("RecoveryAction | generateCeaseNotice | End" + recovery.getCeaseNotice());
		return PRINT;
	}

	public String viewDetails() {
		getPropertyView(recovery.getBasicProperty().getUpicNo());
		return "viewDetails";
	}

	private void updateWfstate(String value) {
		LOGGER.debug("RecoveryAction | updateStateAndStatus | Start");

		if (WFLOW_ACTION_STEP_SAVE.equalsIgnoreCase(workflowBean.getActionName())) {
			// FIX ME
			// Position position =
			// eisCommonsManager.getPositionByUserId(Integer.valueOf(ApplicationThreadLocals.getUserId()));
			Position position = null;
			recovery.transition().progress().withNextAction("Saved : " + value).withOwner(position)
					.withComments(workflowBean.getComments());
			addActionMessage(getText("file.save"));

		} else if (WFLOW_ACTION_STEP_FORWARD.equalsIgnoreCase(workflowBean.getActionName())) {
			// FIX ME
			// Position position =
			// eisCommonsManager.getPositionByUserId(workflowBean.getApproverUserId());
			Position position = null;
			User approverUser = userService.getUserById(workflowBean.getApproverUserId().longValue());
			recovery.transition().progress().withNextAction("Forwarded : " + value)
					.withStateValue("Forward/Approve").withOwner(position).withComments(workflowBean.getComments());
			addActionMessage(getText("recovery.forward", new String[] { approverUser.getUsername() }));

		} else if (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workflowBean.getActionName())) {
			// Position position =
			// eisCommonsManager.getPositionByUserId(recovery.getCreatedBy().getId());
			Position position = null;
			User approverUser = userService.getUserById(recovery.getCreatedBy().getId());

			recovery.transition().progress().withNextAction("Approved : " + value)
					.withStateValue(getNextState(recovery.getStatus().getCode())).withOwner(position)
					.withComments(workflowBean.getComments());
			addActionMessage(getText("recovery.approve", new String[] { approverUser.getUsername() }));

		} else {
			// FIX ME
			// Position position =
			// eisCommonsManager.getPositionByUserId(workflowBean.getApproverUserId());
			Position position = null;
			User approverUser = userService.getUserById(workflowBean.getApproverUserId().longValue());

			recovery.transition().start().withNextAction(value).withStateValue(recovery.getStatus().getCode())
					.withOwner(position).withComments(workflowBean.getComments());
			addActionMessage(getText("recovery.approve", new String[] { approverUser.getUsername() }));

		}

		LOGGER.debug("RecoveryAction | updateStateAndStatus | End");
	}

	public Recovery getRecovery() {
		return recovery;
	}

	public void setRecovery(Recovery recovery) {
		this.recovery = recovery;
	}

	public void setRecoveryService(PersistenceService<Recovery, Long> recoveryService) {
		this.recoveryService = recoveryService;
	}

	public void setRecoveryWorkflowService(WorkflowService<Recovery> recoveryWorkflowService) {
		this.recoveryWorkflowService = recoveryWorkflowService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
