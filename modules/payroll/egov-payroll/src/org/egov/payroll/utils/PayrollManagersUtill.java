package org.egov.payroll.utils;

import org.egov.billsaccounting.services.BillsAccountingService;
import org.egov.billsaccounting.services.BillsAccountingServiceImpl;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.VoucherDetail;
import org.egov.commons.service.CommonsServiceImpl;
import org.egov.dao.recoveries.EgDeductionDetailsHibernateDAO;
import org.egov.dao.recoveries.RecoveryDAOFactory;
import org.egov.dao.recoveries.TdsHibernateDAO;
import org.egov.dao.voucher.VoucherHibernateDAO;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.services.SessionFactory;
import org.egov.lib.admbndry.ejb.api.BoundaryService;
import org.egov.lib.admbndry.ejb.server.BoundaryServiceImpl;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.egov.lib.rjbac.dept.ejb.server.DepartmentServiceImpl;
import org.egov.lib.rjbac.role.ejb.api.RoleService;
import org.egov.lib.rjbac.role.ejb.server.RoleServiceImpl;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.egov.lib.rjbac.user.ejb.server.UserServiceImpl;
import org.egov.lib.rrbac.services.RbacService;
import org.egov.lib.rrbac.services.RbacServiceImpl;
import org.egov.masters.services.MastersService;
import org.egov.masters.services.MastersServiceImpl;
import org.egov.payroll.services.advance.AdvanceServiceImpl;
import org.egov.payroll.services.exception.ExceptionService;
import org.egov.payroll.services.exception.ExceptionServiceImpl;
import org.egov.payroll.services.payhead.PayheadServiceImpl;
import org.egov.payroll.services.payslip.PayRollService;
import org.egov.payroll.services.payslip.PayRollServiceImpl;
import org.egov.payroll.services.payslipApprove.PayslipApproveServiceImpl;
import org.egov.payroll.services.providentfund.PFServiceImpl;
import org.egov.pims.commons.service.EisCommonsService;
import org.egov.pims.commons.service.EisCommonsServiceImpl;
import org.egov.pims.empLeave.service.EmpLeaveServiceImpl;
import org.egov.pims.service.EmployeeServiceImpl;
import org.egov.services.recoveries.RecoveryService;
import org.egov.services.voucher.VoucherService;

public class PayrollManagersUtill {

	public static PayheadServiceImpl getPayheadService() {
		return new PayheadServiceImpl();
	}

	public static CommonsServiceImpl getCommonsService() {
		return new CommonsServiceImpl();
	}

	public static UserService getUserService() {
		return new UserServiceImpl();
	}

	public static PayslipApproveServiceImpl getPayslipApproveService() {
		return new PayslipApproveServiceImpl();
	}

	public static PayRollService getPayRollService() {
		return new PayRollServiceImpl();
	}

	public static EmployeeServiceImpl getEmployeeService() {
		return new EmployeeServiceImpl();
	}

	public static AdvanceServiceImpl getAdvanceService() {
		return new AdvanceServiceImpl();
	}

	public static ExceptionService getExceptionService() {
		return new ExceptionServiceImpl();
	}

	public static EmpLeaveServiceImpl getEmpLeaveService() {
		return new EmpLeaveServiceImpl();
	}
	
	/*
	 * public static PensionManager getPensionManager() { PensionManager
	 * pensionMngr = new PensionManagerBean(); try { ScriptService scriptService
	 * = new ScriptService(2,5,10,30); scriptService.setSessionFactory(new
	 * SessionFactory()); pensionMngr.setScriptService(scriptService);
	 * GratuityWFService gratuityWFService = new GratuityWFService();
	 * gratuityWFService.setType(PensionDetails.class);
	 * gratuityWFService.setPayrollExternalInterface(new PayrollExternalImpl());
	 * PersistenceService<T, Serializable> SimpleWorkflowService
	 * simpleGratuityWFService = new SimpleWorkflowService();
	 * gratuityWFService.setSimpleGratuityWFService(simpleGratuityWFService); }
	 * catch (Exception e) { throw new
	 * EGOVRuntimeException("Unable to get PensionManager Home", e); } return
	 * pensionMngr; }
	 */

	public static EisCommonsService getEisCommonsService() {
		return new EisCommonsServiceImpl();
	}

	public static DepartmentService getDepartmentService() {
		return new DepartmentServiceImpl();
	}

	public static PFServiceImpl getPfService() {
		return new PFServiceImpl();
	}

	public static MastersService getMastersService() {
		MastersServiceImpl mastersService = new MastersServiceImpl();
		mastersService.setCommonsService(getCommonsService());
		return mastersService;
	}

	public static BillsAccountingService getBillsAccountingService() {
		return new BillsAccountingServiceImpl();
	}

	public static RbacService getRbacService() {
		return new RbacServiceImpl();
	}

	public static BoundaryService getBoundaryService() {
		return new BoundaryServiceImpl();
	}

	public static RoleService getRoleService() {
		return new RoleServiceImpl();
	}
	

	public static PayrollExternalInterface getPayrollExterInterface() {
		PayrollExternalImpl payrollExternalImpl = new PayrollExternalImpl();
		/*
		 * RecoveryService recoveryService = new RecoveryService();
		 * recoveryService.setType(Recovery.class);
		 * recoveryService.setSessionFactory(new SessionFactory());
		 * recoveryService.setEgDeductionDetHibernateDao(new
		 * EgDeductionDetailsHibernateDAO
		 * (EgDeductionDetails.class,recoveryService.getSession()));
		 * recoveryService.setTdsHibernateDAO(new
		 * TdsHibernateDAO(Recovery.class,recoveryService.getSession()));
		 */
		PersistenceService persistenceService = new PersistenceService();
		persistenceService.setSessionFactory(new SessionFactory());
		ScriptService scriptService = new ScriptService(2, 5, 10, 30);
		scriptService.setSessionFactory(new SessionFactory());

		payrollExternalImpl.setPersistenceService(persistenceService);
		payrollExternalImpl.setScriptExecutionService(scriptService);
		// payrollExternalImpl.setVoucherService(getVoucherService());
		payrollExternalImpl.setRecoveryService(getRecoveryService());
		return payrollExternalImpl;
	}

	public static RecoveryService getRecoveryService() {
		EgDeductionDetailsHibernateDAO egDeductionDetHibernateDao = RecoveryDAOFactory
				.getDAOFactory().getEgDeductionDetailsDAO();
		TdsHibernateDAO tdsHibernateDAO = RecoveryDAOFactory.getDAOFactory()
				.getTdsDAO();
		RecoveryService recoveryService = new RecoveryService();
		recoveryService.setSessionFactory(new SessionFactory());
		recoveryService
				.setEgDeductionDetHibernateDao(egDeductionDetHibernateDao);
		recoveryService.setTdsHibernateDAO(tdsHibernateDAO);
		return recoveryService;
	}

	public static VoucherService getVoucherService() {
		try {
			PersistenceService persistenceService = new PersistenceService();
			persistenceService.setSessionFactory(new SessionFactory());
			VoucherService voucherService = new VoucherService();
			voucherService.setPersistenceService(persistenceService);
			voucherService.setSessionFactory(new SessionFactory());
			voucherService.setType(CVoucherHeader.class);
			VoucherHibernateDAO voucherHibDAO = new VoucherHibernateDAO();
			voucherHibDAO.setSessionFactory(new SessionFactory());
			voucherHibDAO.setGenericDao(new GenericHibernateDaoFactory());
			PersistenceService<VoucherDetail, Integer> vdPersitSer = new PersistenceService<VoucherDetail, Integer>();
			vdPersitSer.setSessionFactory(new SessionFactory());
			vdPersitSer.setType(VoucherDetail.class);
			voucherHibDAO.setVdPersitSer(vdPersitSer);
			voucherService.setVoucherHibDAO(voucherHibDAO);
			return voucherService;
		} catch (Exception e) {
			throw new EGOVRuntimeException("Not able to create voucherService",
					e);
		}

	}

}
