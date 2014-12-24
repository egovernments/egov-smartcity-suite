package org.egov.payroll.client.advance;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.services.PersistenceService;
import org.egov.lib.rjbac.dept.Department;
import org.egov.payroll.model.SalaryCodes;
import org.egov.payroll.services.advance.AdvanceService;
import org.egov.payroll.utils.PayrollManagersUtill;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class BeforeAdvanceAction extends Action {
	private static final Logger LOGGER = Logger
			.getLogger(BeforeAdvanceAction.class);
	private PersistenceService actionService;
	private EmployeeService employeeService;

	public ActionForward execute(ActionMapping actionMapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String target = "";
		ActionMessages messages = new ActionMessages();
		try {
			AdvanceForm salaryadvanceForm = (AdvanceForm) form;
			if (request.getAttribute("ess") != null
					|| (request.getParameter("ess") != null)) {
				PersonalInformation employee = employeeService
						.getEmpForUserId(Integer.valueOf(EGOVThreadLocals
								.getUserId()));
				if (employee == null)
					return actionMapping.findForward("essError");
				// The current assignment is required to generate ARF number
				if (employeeService.getLatestAssignmentForEmployee(employee
						.getId()) == null) {
					ActionMessage message = new ActionMessage("errors.message",
							"You need to be currently assigned to a post to create advance");
					messages.add(ActionMessages.GLOBAL_MESSAGE, message);
					saveMessages(request, messages);
					return actionMapping.findForward("essError");
				}
				salaryadvanceForm.setEmployeeCode(employee.getCode());
				salaryadvanceForm.setEmployeeCodeId(String.valueOf(employee
						.getId()));
				salaryadvanceForm.setEmployeeName(employee.getName());
				salaryadvanceForm.setEss("1");

			}
			AdvanceService salaryAdvanceService = PayrollManagersUtill
					.getAdvanceService();
			HttpSession session = request.getSession();
			String userName = (String) session
					.getAttribute("com.egov.user.LoginUserName");
			List<SalaryCodes> salarycodes = salaryAdvanceService
					.getSalaryCodesByCategoryNames("Deduction-Advance",
							"Deduction-BankLoan");
			for (SalaryCodes sal : salarycodes) {
				LOGGER.info(sal.getHead());
				if (sal.getTdsId() != null && sal.getTdsId().getBank() != null) {
					LOGGER.info(sal.getTdsId().getBank().getName());
				}
			}
			List<Department> departmentList = actionService
					.findAllBy("from DepartmentImpl order by deptName");
			List<DesignationMaster> designationList = actionService
					.findAllBy("from DesignationMaster order by designationName");
			request.getSession().setAttribute("departmentList", departmentList);
			request.getSession().setAttribute("designationList",
					designationList);
			request.setAttribute("salarycodes", salarycodes);
			session.setAttribute("userName", userName);
			target = "success";
		} catch (EGOVRuntimeException ex) {
			LOGGER.error("EGOVRuntimeException Encountered!!!"
					+ ex.getMessage());
			target = "error";
		} catch (Exception e) {
			LOGGER.error("Error while getting data>>>>>" + e.getMessage());
			target = "error";
		}

		return actionMapping.findForward(target);

	}

	public PersistenceService getActionService() {
		return actionService;
	}

	public void setActionService(PersistenceService actionService) {
		this.actionService = actionService;
	}

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
}
