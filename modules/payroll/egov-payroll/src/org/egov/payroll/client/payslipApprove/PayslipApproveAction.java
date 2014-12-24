package org.egov.payroll.client.payslipApprove;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.commons.EgwStatus;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.ejb.server.DepartmentServiceImpl;
import org.egov.lib.rjbac.user.User;
import org.egov.payroll.model.EmpPayroll;
import org.egov.payroll.model.SalaryCodes;
import org.egov.payroll.services.payhead.PayheadService;
import org.egov.payroll.services.payslipApprove.PayslipApproveService;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.payroll.utils.PayrollManagersUtill;

public class PayslipApproveAction extends Action{
	private static final Logger logger = Logger.getLogger(PayslipApproveAction.class);
	private static final String payslipCreatedStatus="Created";//EGovConfig.getProperty("payroll_egov_config.xml","CREATED_STATUS","", "PAYSLIP");
	private static final String payslipModule = "PaySlip";//EGovConfig.getProperty("payroll_egov_config.xml", "PAYSLIP", "", "MODULE");
	PayrollExternalInterface payrollExternalInterface = PayrollManagersUtill.getPayrollExterInterface();
	
	public ActionForward execute(ActionMapping actionMapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
//		Getting all managers			
			PayslipApproveService payslipApproveManager = PayrollManagersUtill.getPayslipApproveService();
			PayheadService payheadService = PayrollManagersUtill.getPayheadService();
			HttpSession session = request.getSession();
			PayslipApproveForm payslipApproveForm = (PayslipApproveForm)form;			
			String forward = payslipApproveForm.getAction();
			logger.info("ACTION"+forward);
			String year = (String)session.getAttribute("year");
			String monthInput = (String)session.getAttribute("month");	
			String deptId = (String)session.getAttribute("deptId");
			String userName =(String) session.getAttribute("com.egov.user.LoginUserName");			
			
			List earningPayheads = payheadService.getAllSalaryCodesByType("E");			
			Collections.sort(earningPayheads);			
			List deductionPayheads = payheadService.getAllSalaryCodesByType("D");			
			Collections.sort(deductionPayheads,SalaryCodes.CategoryComparator);
			
			
			User user = payrollExternalInterface.getUserByUserName(userName); 
			logger.info("USER:---"+user.getFirstName());			
			//Department dept = user.getDepartment();	
			//String depatName = dept.getDeptName();
			//payslipApproveForm.setDeptName("dasdfadsdgg");
			EgwStatus status = payrollExternalInterface.getStatusByModuleAndDescription(payslipModule,payslipCreatedStatus);
			BigDecimal month = BigDecimal.ZERO;
			DepartmentServiceImpl dmb = new DepartmentServiceImpl();
			//DepartmentManager dm = GetRjbacManagers.getDepartmentManager();
			//batchgenobj.setDepartment((DepartmentImpl)dmb.getDepartment(new Integer(deptid)));
			Department deptMent =(Department)dmb.getDepartment(new Integer(deptId));
			logger.info("dept----------------------"+deptMent.getDeptName());
			payslipApproveForm.setDeptName(deptMent.getDeptName());
			if(monthInput != null)
				 {
				month = BigDecimal.valueOf(Integer.valueOf(monthInput));
				 }
			List empPayrolls = payslipApproveManager.getPayslipsOfYearMonthStatus(year, month,deptMent, status);			
			
		/// SORTING EMPPAYROLL		\\\\\\\\\\\\\				
			if("empCode".equals(payslipApproveForm.getSortedBy()))				
				{
				Collections.sort(empPayrolls);			
				}
			if("empName".equals(payslipApproveForm.getSortedBy()))
				{
				Collections.sort(empPayrolls, EmpPayroll.EmpNameComparator);
				}
			if("empDesig".equals(payslipApproveForm.getSortedBy()))
				{
				Collections.sort(empPayrolls, EmpPayroll.DesignationComparator);		
				}
		
		///	GETTING TOTAL NET & GROSS ROW 	\\\\\\\\\\\ 			
			BigDecimal grossTotal = BigDecimal.ZERO;
			BigDecimal netTotal = BigDecimal.ZERO;
			BigDecimal deductionTotalAllPayslip = BigDecimal.ZERO;
			for(Iterator iter = empPayrolls.iterator(); iter.hasNext();){
				EmpPayroll empPayroll = (EmpPayroll)iter.next();
				grossTotal = grossTotal.add(empPayroll.getGrossPay());
				netTotal = netTotal.add(empPayroll.getNetPay());
				deductionTotalAllPayslip = deductionTotalAllPayslip.add(empPayroll.getTotalDeductions());
			}			
			payslipApproveForm.setGrossTotal(grossTotal);
			payslipApproveForm.setNetTotal(netTotal);	
			payslipApproveForm.setDeductionTotal(deductionTotalAllPayslip);
			payslipApproveForm.setEmpPayrolls(empPayrolls);	
			
			if(empPayrolls.isEmpty())
				{
				return actionMapping.findForward("noPayslip");				
				}
			if("submit".equals(forward)){
				return actionMapping.findForward("afterApprove");
			}
		return actionMapping.findForward("regularPayslip");
	}

}
