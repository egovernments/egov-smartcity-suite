package org.egov.payroll.services.payslipApprove;

import java.math.BigDecimal;
import java.util.List;


import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.EgwStatus;
import org.egov.lib.rjbac.dept.Department;
import org.egov.payroll.dao.DeductionsDAO;
import org.egov.payroll.dao.EmpPayrollDAO;
import org.egov.payroll.dao.PayrollDAOFactory;
import org.egov.payroll.model.EmpPayroll;

public class PayslipApproveServiceImpl implements PayslipApproveService{

	
	
	
	public List getAllPayslip()throws Exception{
		try{
			EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
			return empPayrollDAO.findAll();
		}catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction();
            throw new EGOVRuntimeException(e.getMessage(),e);
        }
	}	
	
	public List getAllPayslipByStatus(EgwStatus status)throws Exception{
		try{
			EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
			return empPayrollDAO.getAllPayslipByStatus(status);
		}catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction();
            throw new EGOVRuntimeException(e.getMessage(),e);
        }
	}
	
	public List getAllOtherDeductionChartOfAccount()throws Exception{
		try{
			DeductionsDAO deductionsDAO = PayrollDAOFactory.getDAOFactory().getDeductionsDAO();
			return deductionsDAO.getAllOtherDeductionChartOfAccount();
		}catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction();
            throw new EGOVRuntimeException(e.getMessage(),e);
        }
	}
	
	public List getPayslipsOfYearMonthStatus(String year, BigDecimal month, Department department, EgwStatus status)throws Exception{
		try{
			EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
			return empPayrollDAO.getPayslipsOfYearMonthStatus(year, month, department, status);
		}catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction();
            throw new EGOVRuntimeException(e.getMessage(),e);
        }
	}
	
	public EmpPayroll getPayslipById(Integer id)throws Exception{
		try{
			EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
			return (EmpPayroll)empPayrollDAO.findById(id, false);
		}catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction();
            throw new EGOVRuntimeException(e.getMessage(),e);
        }
	}
	
	public List getPayslipsOfYearMonthStatusInternal(String year, BigDecimal month, EgwStatus status)throws Exception{
		try{
			EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
			return empPayrollDAO.getPayslipsOfYearMonthStatusInternal(year,month,status);
		}catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction();
            throw new EGOVRuntimeException(e.getMessage(),e);
        }
	}
}
