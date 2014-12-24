package org.egov.payroll.workflow.payslip;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.EgwSatuschange;
import org.egov.commons.EgwStatus;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.services.EISServeable;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.StringUtils;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.lib.rjbac.user.User;
import org.egov.masters.model.BillNumberMaster;
import org.egov.payroll.model.EmpPayroll;
import org.egov.payroll.services.advance.AdvanceService;
import org.egov.payroll.services.payslip.IPayslipProcess;
import org.egov.payroll.services.payslip.PayslipProcessImpl;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.payroll.utils.PayrollManagersUtill;
import org.egov.pims.commons.Position;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * @author surya,DivyaShree
 */
public class PayslipService extends PersistenceService<EmpPayroll, Long>
{
	private static final Logger LOGGER = Logger.getLogger(PayslipService.class);
	private PayrollExternalInterface payrollExternalInterface;
	private AdvanceService advanceService;
	private EISServeable eisService;
	private PersistenceService<BillNumberMaster, Long> billNumberMasterService;
	
	public EISServeable getEisService() {
		return eisService;
	}
	public void setEisService(EISServeable eisService) {
		this.eisService = eisService;
	}
	public void setPayrollExternalInterface(PayrollExternalInterface payrollExternalInterface) {
		this.payrollExternalInterface = payrollExternalInterface;
	}	
	public void setAdvanceService(AdvanceService advanceService) {
		this.advanceService = advanceService;
	}	
	
	
	public PersistenceService<BillNumberMaster, Long> getBillNumberMasterService() {
		return billNumberMasterService;
	}
	public void setBillNumberMasterService(
			PersistenceService<BillNumberMaster, Long> billNumberMasterService) {
		this.billNumberMasterService = billNumberMasterService;
	}
	public Position getPositionForEmployee(PersonalInformation emp)throws EGOVRuntimeException{
		Position pos;
		try {
			pos = payrollExternalInterface.getPositionforEmp(emp.getIdPersonalInformation());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException("Error While Getting postion for the Employee"+emp.getEmployeeFirstName());
		}
		return pos;
	}
	
	public void updatePayslipStatus(EmpPayroll payslip){
		EgwStatus statusObj = payslip.getStatus();
		
		String userId = EGOVThreadLocals.getUserId();
		User user = payrollExternalInterface.getUserByID(Integer.parseInt(userId));
		EgwSatuschange statusChanges = new EgwSatuschange();
		statusChanges.setCreatedby(user.getId());
		statusChanges.setFromstatus(payslip.getStatus().getId());
		statusChanges.setTostatus(statusObj.getId());
		statusChanges.setModuleid(1);
		statusChanges.setModuletype(statusObj.getModuletype());
		payrollExternalInterface.createEgwSatuschange(statusChanges);
		payslip.setStatus(statusObj);
		String PayslipCancelled = GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payslip","PayslipCancelledStatus",new Date()).getValue();
		if(PayslipCancelled.equals(statusObj.getCode())){
			advanceService.cancelAdvanceEmiIfPayslipReject(payslip);
		}
		//return payslip;
	}
	
	/**
	 * Getting superior position
	 * @param position
	 * @param objType
	 * @return position
	 */
	public Position getSuperiorPosition(Position position,String objType){
		Position posTemp = null;
		posTemp =payrollExternalInterface.getSuperiorPositionByObjType(position, objType);
		return posTemp;
	}
	
	/**
	 * Getting Inferior position
	 * @param position
	 * @param objType
	 * @return position
	 */
	public Position getInferiorPosition(Position position,String objType){
		return payrollExternalInterface.getInferiorPositionByObjType(position, objType);
	}
	
	/**
	 * Deciding the payslip generation workflow whether manual or auto
	 * @return 'manual' for manual workflow and 'auto' for automatic workflow
	 */
	public String typeOfPayslipWF(){
		String payslipWfType = GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payroll","PayslipWorkflow",new Date()).getValue();
		return payslipWfType;
	}
	
   /**
    * Api to return the last numOfPreviousMonth payslip.For Instance if numOfPreviousMonth= -5 then will return
    * payslip from past 5 month along with current payslip
    * 
    * @param employee
    * @param fromDate
    * @param numOfPreviousMonth
    * @return List of payslip based on numOfPreviousMonth
    */
	 public List<EmpPayroll> getPreviousMonthPayslip(PersonalInformation employee,Date fromDate,int numOfPreviousMonth)
	 {
		 IPayslipProcess payslipImpl = new PayslipProcessImpl();
		 List<EmpPayroll> empPayrollList = new LinkedList<EmpPayroll>();
		 Date EmpfromDate = fromDate;
		 Date PrevMonthDate =payslipImpl.getDateBySubOrAddForGivenMonth(EmpfromDate,numOfPreviousMonth);
		 if(PrevMonthDate!=null)
		 {
			 LOGGER.info("from date="+EmpfromDate+"previous Six Month date="+PrevMonthDate);
			// EgwStatus statusObj = payrollExternalInterface.getStatusByModuleAndDescription(PayrollConstants.PAYSLIP_MODULE, status); 
			 Criteria criteria=getSession().createCriteria(EmpPayroll.class,"emp")
			 .add(Restrictions.eq("emp.employee", employee))
			 .add(Restrictions.le("emp.fromDate", fromDate))
			 .add(Restrictions.ge("emp.toDate", PrevMonthDate))
			 .add(Restrictions.eq("emp.status", payrollExternalInterface.getStatusByModuleAndDescription(PayrollConstants.PAYSLIP_MODULE, PayrollConstants.AUDIT_APPROVED)))
			 .setMaxResults(6);
			 empPayrollList = (List<EmpPayroll>)criteria.list();
			 
		 }
		 return empPayrollList;
	 }
	 
	 public Position getApproverPositionByBillId(Integer billId)
	 {
		BillNumberMaster billNumberMaster=  getBillNumberMasterService().findByNamedQuery("BILLNUMBER_BY_ID",
				 billId);
		
		return  billNumberMaster.getPosition();
	 }
	 
	 /**
	  *  To set the department drop down to default the department of logged in user in all the screens with workflow
	  */
	 public Integer getLoggedUserDepartmentId() 
	 {
		 Integer approverDeptId=null;
			PersonalInformation loggedEmp=payrollExternalInterface.getEmpForUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
			Assignment loggedEmpAssignment = null;
			try 
			{
				loggedEmpAssignment = payrollExternalInterface.getLatestAssignmentForEmployeeByToDate(loggedEmp.getId(), DateUtils.today());
			} catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(null != loggedEmpAssignment)
			{
				if(null!=loggedEmpAssignment.getDeptId())
					approverDeptId= loggedEmpAssignment.getDeptId().getId();  
			}
			return approverDeptId;
		} 
	
}
