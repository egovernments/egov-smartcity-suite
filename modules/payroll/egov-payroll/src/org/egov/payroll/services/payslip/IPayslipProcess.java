package org.egov.payroll.services.payslip;

import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.egov.infstr.workflow.Action;
import org.egov.payroll.model.BatchFailureDetails;
import org.egov.payroll.model.EmpPayroll;
import org.egov.payroll.model.PayStructure;
import org.egov.pims.commons.Position;

public interface IPayslipProcess {
	
	public List<BatchFailureDetails> generateBatchPayslips(GregorianCalendar fromDate,GregorianCalendar toDate,Integer deptid,String username,Integer functionaryId, boolean persist, Position approverPos, Integer billNumberId) throws Exception;
	public List<BatchFailureDetails> generateBatchPayslips(GregorianCalendar fromDate,GregorianCalendar toDate,Integer deptid,String username,Integer functionaryId, boolean persist, Position approverPos,Long functionId, Integer billNumberId) throws Exception;
	
	public PayStructure insertPaystructureHistory(EmpPayroll payslip,PayStructure paystructure,BigDecimal incrementedBasic, Boolean persists) throws Exception;
	
	/**
	  * Apply incement on payscale
	  */
	 public PayStructure applyPayscaleIncrement(EmpPayroll currPay,PayStructure payStructure,boolean persists) throws Exception;
	 
	 public void createWorkFlow(EmpPayroll payslip)throws Exception;
	 
	 public Date getDateBySubOrAddForGivenMonth(Date givenDate,int numOfPreviousMonth);
	 
	 public List<Action> getValidActions(EmpPayroll payslip);
	 public void fireWorkFlow(EmpPayroll monthlyPayslip,String workFlowAction,String approverComments);
}
