package org.egov.payroll.client.advance;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.services.ScriptService;
import org.egov.lib.rjbac.user.User;
import org.egov.payroll.model.Advance;
import org.egov.payroll.model.AdvanceSchedule;
import org.egov.payroll.model.SalaryCodes;
import org.egov.payroll.services.advance.AdvanceService;
import org.egov.payroll.services.payhead.PayheadService;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.payroll.utils.PayrollManagersUtill;
import org.egov.payroll.workflow.advance.AdvanceWorkflowService;
import org.egov.payroll.workflow.advance.SalaryARFWorkflowService;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;

public class AfterAdvanceAction extends Action {

	private static final Logger LOGGER = Logger
			.getLogger(AfterAdvanceAction.class);
	private PayrollExternalInterface payrollExternalInterface;
	private AdvanceWorkflowService advanceWorkflowService;
	private SalaryARFWorkflowService salaryARFWorkflowService;
	private ScriptService scriptService;

	public ActionForward execute(ActionMapping actionMapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String target = "";
		ActionMessages messages = new ActionMessages();
		try {
			HttpSession session = request.getSession();
			String userName = (String) session
					.getAttribute("com.egov.user.LoginUserName");
			User user = payrollExternalInterface.getUserByUserName(userName);
			AdvanceForm salaryadvanceForm = (AdvanceForm) form;
			target = salaryadvanceForm.getAction();
			AdvanceService salaryAdvanceService = PayrollManagersUtill
					.getAdvanceService();
			Advance salaryadvance = new Advance();
			populateAdvance(salaryadvance, salaryadvanceForm, user);

			// To set the approver for manual workflow
			String approverEmpAssgnId = salaryadvanceForm
					.getApproverEmpAssignmentId();
			if (approverEmpAssgnId != null) {
				Assignment approverAssignment = PayrollManagersUtill
						.getEmployeeService().getAssignmentById(
								Integer.parseInt(approverEmpAssgnId));
				salaryadvance.setApproverPos(approverAssignment.getPosition());
			}

			if (PayrollConstants.Deduction_BankLoan.equals(salaryadvance
					.getSalaryCodes().getCategoryMaster().getName())) {
				if (salaryadvance.getSalaryCodes().getTdsId() != null
						&& salaryadvance.getSalaryCodes().getTdsId().getBank() != null) {
					salaryAdvanceService.createSaladvanceForBankLoan(
							salaryadvance, scriptService);
					getAdvanceWorkflowService().createWorkFlow(salaryadvance);
					// To load the obj.Without this throwing no session
					// exception.
					LOGGER.info(salaryadvance.getSalaryCodes().getTdsId()
							.getBank().getName());
				}
			} else {
				salaryAdvanceService.createSaladvance(salaryadvance,
						scriptService);

				// To call a create workflow
				getSalaryARFWorkflowService().createWorkFlow(salaryadvance);
			}

			session.setAttribute("salaryadvance", salaryadvance);
		} catch (EGOVRuntimeException ex) {
			LOGGER.error("EGOVRuntimeException Encountered!!!"
					+ ex.getMessage());
			ActionMessage message = new ActionMessage("errors.message",
					ex.getMessage());
			messages.add(ActionMessages.GLOBAL_MESSAGE, message);
			saveMessages(request, messages);
			throw new EGOVRuntimeException(ex.getMessage(), ex);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while getting data>>>>>" + e.getMessage());
			ActionMessage message = new ActionMessage("errors.message",
					e.getMessage());
			messages.add(ActionMessages.GLOBAL_MESSAGE, message);
			saveMessages(request, messages);
			throw new EGOVRuntimeException(e.getMessage(), e);
		}
		return actionMapping.findForward(target);
	}

	protected Advance populateAdvance(Advance salaryadvance,
			AdvanceForm salaryadvanceForm, User user) throws Exception {

		try {
			EmployeeService eisManager = PayrollManagersUtill
					.getEmployeeService();
			PayheadService payheadManager = PayrollManagersUtill
					.getPayheadService();

			// SimpleDateFormat formatter = new
			// SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
			PersonalInformation employee = eisManager
					.getEmloyeeById(new Integer(salaryadvanceForm
							.getEmployeeCodeId()));
			salaryadvance.setEmployee(employee);
			SalaryCodes salarycode = payheadManager
					.getSalaryCodeByHead(salaryadvanceForm.getSalarycode());
			salaryadvance.setSalaryCodes(salarycode);
			salaryadvance.setAdvanceAmt(new BigDecimal(salaryadvanceForm
					.getAdvAmount()));
			salaryadvance.setIsLegacyAdvance("N");
			salaryadvance.setRequestedAmt(new BigDecimal(salaryadvanceForm
					.getAdvAmount()));
			if (PayrollConstants.SAL_ADV_TYPE_INTEREST.equals(salaryadvanceForm
					.getAdvanceType())) {
				salaryadvance.setInterestPct(new BigDecimal(salaryadvanceForm
						.getInterestPct()));
				salaryadvance.setInterestType(salaryadvanceForm
						.getInterestType());
				salaryadvance.setInterestAmt(new BigDecimal(salaryadvanceForm
						.getInterestAmount()));
			}
			salaryadvance.setNumOfInst(new BigDecimal(salaryadvanceForm
					.getNumberOfInstallments()));
			salaryadvance.setInstAmt(new BigDecimal(salaryadvanceForm
					.getMonthlyPayment()));

			BigDecimal totalAmt = new BigDecimal(salaryadvanceForm.getTotal());
			salaryadvance.setPendingAmt(totalAmt);
			salaryadvance.setPaymentType(salaryadvanceForm.getPaymentMethod());
			salaryadvance.setAdvanceType(salaryadvanceForm.getAdvanceType());
			salaryadvance.setMaintainSchedule(salaryadvanceForm
					.getMaintainSchedule());
			if (salaryadvanceForm.getPendingPrevAmt().equalsIgnoreCase(""))
				salaryadvance.setPreviousPendingAmt(BigDecimal.ZERO);
			else
				salaryadvance.setPreviousPendingAmt(new BigDecimal(
						salaryadvanceForm.getPendingPrevAmt()));
			if (salaryadvanceForm.getMaintainSchedule() != null
					&& salaryadvanceForm.getMaintainSchedule().equals("Y"))// if
																			// maintain
																			// schedule
																			// is
																			// Y
																			// ,
																			// then
																			// populate
																			// advance
																			// schedule
																			// rows.
			{
				Set<AdvanceSchedule> advanceScheduleSet = new LinkedHashSet<AdvanceSchedule>(
						0);
				int noOfInstallments = salaryadvanceForm.getInstallmentNo().length;
				String installmentNo[] = salaryadvanceForm.getInstallmentNo();
				String principalAmt[] = salaryadvanceForm
						.getPrincipalInstAmount();
				String interestAmt[] = salaryadvanceForm
						.getInterestInstAmount();

				for (int i = 0; i < noOfInstallments; i++) {
					AdvanceSchedule advScheduleObj = new AdvanceSchedule();

					advScheduleObj.setAdvance(salaryadvance);
					advScheduleObj.setInstallmentNo(Integer
							.parseInt(installmentNo[i]));
					advScheduleObj.setPrincipalAmt(new BigDecimal(
							principalAmt[i]));
					advScheduleObj
							.setInterestAmt(new BigDecimal(interestAmt[i]));
					advanceScheduleSet.add(advScheduleObj);
				}
				salaryadvance.setAdvanceSchedules(advanceScheduleSet);
			}

			return salaryadvance;

		} catch (Exception e) {
			LOGGER.error(e);
		}
		return salaryadvance;
	}

	public AdvanceWorkflowService getAdvanceWorkflowService() {
		return advanceWorkflowService;
	}

	public void setAdvanceWorkflowService(
			AdvanceWorkflowService advanceWorkflowService) {
		this.advanceWorkflowService = advanceWorkflowService;
	}

	public void setPayrollExternalInterface(
			PayrollExternalInterface payrollExternalInterface) {
		this.payrollExternalInterface = payrollExternalInterface;
	}

	public void setSalaryARFWorkflowService(
			SalaryARFWorkflowService salaryARFWorkflowService) {
		this.salaryARFWorkflowService = salaryARFWorkflowService;
	}

	public SalaryARFWorkflowService getSalaryARFWorkflowService() {
		return salaryARFWorkflowService;
	}

	public void setScriptService(ScriptService scriptService) {
		this.scriptService = scriptService;
	}

}
