package org.egov.payroll.client.payslipApprove;

import java.util.ArrayList;
import java.util.Date;
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
import org.egov.commons.EgwSatuschange;
import org.egov.commons.EgwStatus;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.lib.rjbac.user.User;
import org.egov.model.bills.EgBillregister;
import org.egov.payroll.model.EmpPayroll;
import org.egov.payroll.services.payslipApprove.SalaryPaybill;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.payroll.utils.PayrollExternalInterface;


public class AfterPayslipApproveAction extends Action{
	
	private static final Logger logger = Logger.getLogger(AfterPayslipApproveAction.class);	
	private PayrollExternalInterface payrollExternalInterface;
	private SalaryPaybill salaryPaybill;
	
	
	public void setSalaryPaybill(SalaryPaybill salaryPaybill) {
		this.salaryPaybill = salaryPaybill;
	}

	public void setPayrollExternalInterface(PayrollExternalInterface payrollExternalInterface) {
		this.payrollExternalInterface = payrollExternalInterface;
	}

	public ActionForward execute(ActionMapping actionMapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		
		HttpSession session = request.getSession();
		String userName =(String) session.getAttribute("com.egov.user.LoginUserName");
		User user = payrollExternalInterface.getUserByUserName(userName);
		PayslipApproveForm payslipApproveForm = (PayslipApproveForm)form;	
		logger.info("LOGGER-----------");
		logger.info("After Aapprove:----------");
		logger.info(payslipApproveForm.getGrossTotal());		
		String approvePayslips[] = payslipApproveForm.getApprovedPayslips();
		String rejectPayslips[] = payslipApproveForm.getRejectedPayslips();
		String rejectComments[] = payslipApproveForm.getRejectComments();
		List empPayrolls = payslipApproveForm.getEmpPayrolls();
		List approvablePayslips = new ArrayList();
		List<EmpPayroll> rejectablePayslips = new ArrayList<EmpPayroll>();	
		//Getting from config table instead of config xml file
		//EgwStatus status = commonManager.getStatusByModuleAndDescription(PayrollConstants.PAYSLIP_MODULE,PayrollConstants.PAYSLIP_DEPT_APPROVED_STATUS);
		/*EgwStatus status = (EgwStatus) commonManager.getStatusByModuleAndDescription(
				PayrollConstants.PAYSLIP_MODULE,  EGovConfig.getProperty("payroll_egov_config.xml","APPROVED_STATUS","",EGOVThreadLocals.getDomainName()+".PaySlip"));*/
		EgwStatus status = (EgwStatus) payrollExternalInterface.getStatusByModuleAndDescription(
				PayrollConstants.PAYSLIP_MODULE, GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payslip","PayslipAuditApprovedStatus",new Date()).getValue());
		//EgwStatus status = commonManager.findEgwStatusById(new Integer(50));
		for(Iterator iter = empPayrolls.iterator(); iter.hasNext();){
			EmpPayroll empPayroll = (EmpPayroll)iter.next();			
			if(approvePayslips!=null){			
				for(int i = 0;i<approvePayslips.length;i++){
					if(approvePayslips[i].equals(Long.toString(empPayroll.getId()))){																	
						EgwSatuschange statusChanges = new EgwSatuschange();
						statusChanges.setCreatedby(user.getId());
						statusChanges.setFromstatus(empPayroll.getStatus().getId());
						statusChanges.setTostatus(status.getId());
						statusChanges.setModuleid(1);
						statusChanges.setModuletype(status.getModuletype());
						payrollExternalInterface.createEgwSatuschange(statusChanges);						
						empPayroll.setStatus(status);
						approvablePayslips.add(empPayroll);	
					}				
				}				
			}			
		}
		
		if(rejectPayslips != null){
			for(int i = 0; i<rejectPayslips.length; i++){
				for(Iterator iter = empPayrolls.iterator(); iter.hasNext();){
					EmpPayroll empPayroll = (EmpPayroll)iter.next();	
					if(rejectPayslips[i].equals(Long.toString(empPayroll.getId()))){
						empPayroll.setRejectComment(rejectComments[i]);
						rejectablePayslips.add(empPayroll);
					}
				}
			}		
		}	
		List<EgBillregister> billRegisters = salaryPaybill.createSalaryBill(approvablePayslips,user);
		//POPULATING REJECTED PAYSLIP COMMENTS
		salaryPaybill.populateRejectedPayslips(rejectablePayslips, user);
		
		session.setAttribute("billRegisters", billRegisters);
		return actionMapping.findForward("success");
	}

}
