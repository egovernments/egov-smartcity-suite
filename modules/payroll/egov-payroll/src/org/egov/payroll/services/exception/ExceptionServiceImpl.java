package org.egov.payroll.services.exception;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


import org.egov.exceptions.DuplicateElementException;
import org.egov.commons.EgwStatus;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.lib.rjbac.dept.Department;
import org.egov.payroll.dao.ExceptionDAO;
import org.egov.payroll.dao.ExceptionMstrDAO;
import org.egov.payroll.dao.PayrollDAOFactory;
import org.egov.payroll.model.EmpException;
import org.egov.payroll.model.ExceptionMstr;
import org.egov.payroll.utils.PayrollExternalImpl;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.payroll.utils.PayrollManagersUtill;
import org.egov.pims.model.PersonalInformation;

public class ExceptionServiceImpl implements ExceptionService {

	

	public List getAllDistinctTypeExceptionMstr(){
			ExceptionMstrDAO exceptionMstrDAO = PayrollDAOFactory.getDAOFactory().getExceptionMstrDAO();
			return exceptionMstrDAO.getAllDistinctTypeExceptionMstr();
	}
	public List<ExceptionMstr> getAllExceptionMstr(){
			ExceptionMstrDAO exceptionMstrDAO = PayrollDAOFactory.getDAOFactory().getExceptionMstrDAO();
			return exceptionMstrDAO.findAll();
	}
	public ExceptionMstr getExceptionMstrById(Integer id){
			ExceptionMstrDAO exceptionMstrDAO = PayrollDAOFactory.getDAOFactory().getExceptionMstrDAO();
			return (ExceptionMstr)exceptionMstrDAO.findById(id, false);
	}

	public EmpException createException(EmpException empException){
			ExceptionDAO exceptionDAO = PayrollDAOFactory.getDAOFactory().getExceptionDAO();
			EgovMasterDataCaching.getInstance().removeFromCache("pay-empExceptions");
			return (EmpException)exceptionDAO.create(empException);
	}

	public EmpException getExceptionById(Long exceptionId){
			ExceptionDAO exceptionDAO = PayrollDAOFactory.getDAOFactory().getExceptionDAO();
			return (EmpException)exceptionDAO.findById(exceptionId, false);
	}

	public List<EmpException> getAllExceptions(){
			List<EmpException> e = new ArrayList<EmpException>();
			e = EgovMasterDataCaching.getInstance().get("pay-empExceptions");
			return e;
	}
	public List<EmpException> getAllExceptionByFinYearIdMonth(Long finYearId,BigDecimal month){
			ExceptionDAO exceptionDAO = PayrollDAOFactory.getDAOFactory().getExceptionDAO();
			return exceptionDAO.getAllExceptionByFinYearIdAndMonth(finYearId, month);
	}
	public List<EmpException> getAllExceptionByEmpIdFinYearIdMonth(Integer empId,Long finYearId,BigDecimal month){
			ExceptionDAO exceptionDAO = PayrollDAOFactory.getDAOFactory().getExceptionDAO();
			return exceptionDAO.getAllExceptionByEmpIdFinYearMonth(empId, finYearId, month);
	}

	public List<EmpException> getAllExceptionByDeptForFinYear(Department dept,Long finYearId,BigDecimal month){
			PayrollExternalInterface payrollExternalInterface=PayrollManagersUtill.getPayrollExterInterface();
			List<EmpException> exceptionsFordept = new ArrayList<EmpException>();
			List<PersonalInformation> employees = payrollExternalInterface.getListOfEmpforDept(dept.getId());
			List<EmpException> exceptions = getAllExceptionByFinYearIdMonth(finYearId, month);
			for(EmpException empException : exceptions){
				for(PersonalInformation employe : employees)
					if(empException.getEmployee() == employe)
						exceptionsFordept.add(empException);
			}
			return exceptionsFordept;
	}

	public List<EmpException> getAllExceptionByDeptAndStatusForFinYear(Department dept,EgwStatus status,Long finYearId,BigDecimal month){
			List<EmpException> exceptionsForDeptAndStatus = new ArrayList<EmpException>();
			List<EmpException> exceptions = getAllExceptionByDeptForFinYear(dept, finYearId, month);
			for(EmpException empException : exceptions){
				if(empException.getStatus() == status)
					exceptionsForDeptAndStatus.add(empException);
			}
			return exceptionsForDeptAndStatus;
	}

	public Boolean checkExistingEmpExceptionForMonthYear(EmpException empException){
			List<EmpException> exceptions = getAllExceptions();
			for(EmpException exception : exceptions){
				if(exception.getEmployee().equals(empException.getEmployee()) &&
										exception.getFinancialyear().equals(empException.getFinancialyear()) &&
										exception.getMonth().equals(empException.getMonth()) )
					return true;
			}
			return false;

	}

	public EmpException checkAndCreateEmpException(EmpException empException)throws DuplicateElementException{
			List<EmpException> exceptions = getAllExceptions();
			for(EmpException exception : exceptions){
				if(exception.getEmployee().equals(empException.getEmployee()) &&
												exception.getFinancialyear().equals(empException.getFinancialyear()) &&
												exception.getMonth().equals(empException.getMonth()) &&
												("Created".equals(exception.getStatus().getDescription()) ||
												 "Approved".equals(exception.getStatus().getDescription())))
					throw new DuplicateElementException("Employee exception for this month and year already created");
			}
			ExceptionDAO exceptionDAO = PayrollDAOFactory.getDAOFactory().getExceptionDAO();
			EgovMasterDataCaching.getInstance().removeFromCache("pay-empExceptions");
			return (EmpException)exceptionDAO.create(empException);
	}
	 public boolean isEmpHasExceptionForFullMonth(Integer empid,GregorianCalendar fromdate,GregorianCalendar todate)
	 {
		  	ExceptionDAO exceptionDAO = PayrollDAOFactory.getDAOFactory().getExceptionDAO();
			return (Boolean) exceptionDAO.isEmpHasExceptionForFullMonth(empid,fromdate,todate);
	  }
	 public List<EmpException> getActiveExceptionsForEmp(Integer empid,Date fromdate,Date todate)
	 {
			 ExceptionDAO exceptionDAO = PayrollDAOFactory.getDAOFactory().getExceptionDAO();
			 return (List) exceptionDAO.getActiveExceptionsForEmp(empid,fromdate,todate);
	 }
	 public List<EmpException> getAllExceptionsForUserByFinYrAndMonth(Integer userid,Integer finyrid,Integer month) throws Exception
	 {
			 ExceptionDAO exceptionDAO = PayrollDAOFactory.getDAOFactory().getExceptionDAO();
			 return (List) exceptionDAO.getAllExceptionsForUserByFinYrAndMonth(userid,finyrid,month);
	 }
	 public List<EmpException> getAllExceptionsForUserByFinYrAndMonthAndStatus(Integer userid,Integer finyrid,Integer month,EgwStatus status) throws Exception
	 {
			 ExceptionDAO exceptionDAO = PayrollDAOFactory.getDAOFactory().getExceptionDAO();
			 return (List) exceptionDAO.getAllExceptionsForUserByFinYrAndMonthAndStatus(userid,finyrid,month,status);
	 }
	 
	 public List<EmpException> getAllEmpException(Long finYearId, BigDecimal month, String status){
		 ExceptionDAO exceptionDAO = PayrollDAOFactory.getDAOFactory().getExceptionDAO();
		 return exceptionDAO.getAllEmpException(finYearId, month, status);
	 }
}
