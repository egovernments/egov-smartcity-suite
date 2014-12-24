package org.egov.payroll.utils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bank;
import org.egov.commons.Bankbranch;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgActiondetails;
import org.egov.commons.EgwSatuschange;
import org.egov.commons.EgwStatus;
import org.egov.commons.EgwTypeOfWork;
import org.egov.commons.ObjectHistory;
import org.egov.commons.ObjectType;
import org.egov.commons.dao.ChartOfAccountsDAO;
import org.egov.commons.dao.CommonsDaoFactory;
import org.egov.infstr.auditing.model.AuditEntity;
import org.egov.infstr.auditing.service.AuditEventService;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rrbac.model.Action;
import org.egov.model.bills.EgBillregister;
import org.egov.model.recoveries.Recovery;
import org.egov.payroll.model.SalaryCodes;
import org.egov.pims.commons.Position;
import org.egov.pims.empLeave.model.EmployeeAttendenceReport;
import org.egov.pims.empLeave.model.LeaveApplication;
import org.egov.pims.empLeave.model.TypeOfLeaveMaster;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.BankDet;
import org.egov.pims.model.GenericMaster;
import org.egov.pims.model.PersonalInformation;
import org.egov.services.recoveries.RecoveryService;
import org.egov.services.voucher.VoucherService;
/**
 * this interface is the central location for the apis used from other dependent projects EGF,EIS
 * @author suhasini
 *
 */
public interface PayrollExternalInterface {
	//commons
	public List<CFinancialYear> getAllActiveFinancialYearList();
	public String getCurrYearFiscalId();
	public CFinancialYear findFinancialYearById(Long id);
	 public Bank getBankById(Integer id);
	 public Bankbranch getBankbranchById(Integer id);
	 public EgwStatus getStatusByModuleAndDescription(String moduleType,String description);
	 public  ObjectType getObjectTypeByType(String type);
	 public  ObjectHistory createObjectHistory(ObjectHistory objhistory);
	 public EgwStatus findEgwStatusById(Integer id);
	 public void createEgwSatuschange(EgwSatuschange egwSatuschange);
	 public List<Recovery> getAllTdsByPartyType(String partyType);
	 public CChartOfAccounts getCChartOfAccountsByGlCode(String glCode);
	 public  Recovery getTdsById(Integer tdsId);
	 public  CChartOfAccounts getCChartOfAccountsById(Long chartOfAccountsId);
	 public List<org.egov.model.recoveries.EgDeductionDetails> findByTds(Recovery tds);
	 public List<CFinancialYear> getAllActivePostingFinancialYear();
	 public String getCurrentDate()throws Exception;
	 public String getTxnNumber(String txnType,String vDate,Connection con) throws Exception;
	 public CChartOfAccounts findCodeByPurposeId(Long purposeId) throws Exception;
	 //public int getDetailTypeIdByName(String glCode,Connection connection,String name) throws Exception;
	 public Accountdetailtype getAccountDetailTypeIdByName(String glCode,String name)  throws Exception ;
	 public  CVoucherHeader findVoucherHeaderById(Long id) throws Exception;
	 public List<org.egov.model.recoveries.EgDeductionDetails> getEgDeductionDetailsFilterBy(Recovery tds, BigDecimal amount, String date, EgwTypeOfWork egwTypeOfWork, EgwTypeOfWork egwSubTypeOfWork);
	 //public int getDetailTypeId(String glCode,Connection connection) throws Exception;
	 public void createEgActiondetails(EgActiondetails egActiondetails);
	 public List<CFinancialYear> getAllFinancialYearList();
	 public List<EgwStatus> getStatusByModule(String moduleType);
	 //usermanager
	 public User getUserByUserName(String userName);
	 public User getUserByID(Integer id);
	 //department
	 public Department getDepartment(Integer deptId);
	 public List getAllDepartments();
	 //exception
	 public boolean isEmpHasExceptionForFullMonth(Integer empid,GregorianCalendar fromdate,GregorianCalendar todate) throws Exception;
	 //masters
	 public Accountdetailtype getAccountdetailtypeByName(String name);
	 //billsa/cMgr
	 public long createVoucherFromBill(int billId) throws EGOVRuntimeException;	
	 //eis related aPIs
	 public  PersonalInformation getEmloyeeById(Integer employeeId);
	 public  PersonalInformation createEmloyee(PersonalInformation egpimsPersonalInformation);
	 public  GenericMaster getGenericMaster(Integer masterId,String masterName);
	 public  void updateBankDet(BankDet bankDet);
	 public  void addBankDets(PersonalInformation personalInformation,BankDet egpimsBankDet);
	 public  Map getMapForList(List list);
	 public  Assignment getAssignmentByEmpAndDate(Date date,Integer empId);
	 public  PersonalInformation getEmpForUserId(Integer userId);
	 public  Assignment getLatestAssignmentForEmployee(Integer empId);
	 public  List getListOfEmpforDept(Integer deptId);
	 public  Position getPositionByUserId(Integer userId);
	 public Assignment getLatestAssignmentForEmployeeByToDate(Integer empId,Date todate) throws Exception;
	 public  List searchEmployeeByGrouping(LinkedList<String> groupingByOrder)throws Exception;
	 public  Position getSuperiorPositionByObjType(Position position,String obType);
	 public  Position getPositionforEmp(Integer empId);
	 public  Position getInferiorPositionByObjType(Position position,String obType);
	 public  User getSupUserforPositionandObjectType(Position pos,ObjectType objType);
	 public List getListOfPersonalInformationByEmpIdsList(List empIdsList);	
	 //leave
	 public   EmployeeAttendenceReport getEmployeeAttendenceReportBetweenTwoDates(Date fromDate, Date toDate,PersonalInformation personalInformation);
	 public List<LeaveApplication> getEncashmentLeaveApplicationByStatus(String statusName)throws Exception;
	 public  TypeOfLeaveMaster getTypeOfLeaveMasterByName(String name);
	 public Float getAvailableLeavs(Integer empId,Integer type,Date givenDate);
	 public boolean checkLeave(Integer empId);
	 //rbac
	 public Action getActionById(Integer id);
	 public Action getActionByURL(String url); 
	 //boundary
	 public String getBoundaryNameForID(Integer id);
	 /**
	  * Checking budget availability before saving bill
	  */
	 public Boolean isBudgetCheckSucceedForBill(EgBillregister billRegister);
	 
	 
	 public void setScriptExecutionService(ScriptService scriptExecutionService);
	 public void setVoucherService(VoucherService voucherService);
	 public RecoveryService getRecoveryService();
	 public void setPersistenceService(PersistenceService persistenceService);
	 public void setRecoveryService(RecoveryService recoveryService);
	 public List<Accountdetailtype> getAccountdetailtypeListByGLCode(final String glCode)throws EGOVException;
	 public void doAuditing(AuditEntity auditEntity, String action, SalaryCodes salaryCode);
	 public void setAuditEventService(AuditEventService auditEventService);
	
}
