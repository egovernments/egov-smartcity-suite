package org.egov.payroll.utils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.script.ScriptContext;

import org.apache.log4j.Logger;
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
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.auditing.model.AuditEntity;
import org.egov.infstr.auditing.model.AuditEvent;
import org.egov.infstr.auditing.model.AuditModule;
import org.egov.infstr.auditing.service.AuditEventService;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rrbac.model.Action;
import org.egov.model.bills.EgBillregister;
import org.egov.model.recoveries.EgDeductionDetails;
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

public class PayrollExternalImpl implements PayrollExternalInterface {
	private static final Logger	LOGGER	= Logger.getLogger(PayrollExternalImpl.class);	
	private RecoveryService recoveryService;
	private PersistenceService persistenceService;
	private VoucherService voucherService;
	private ScriptService scriptExecutionService;
	private AuditEventService auditEventService;	
	
	public void setScriptExecutionService(ScriptService scriptExecutionService) {
		this.scriptExecutionService = scriptExecutionService;
	}
	public void setVoucherService(VoucherService voucherService) {
		this.voucherService = voucherService;
	}
	public RecoveryService getRecoveryService(){		
		return recoveryService; 
	}
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	public void setRecoveryService(RecoveryService recoveryService) {
		this.recoveryService = recoveryService;
	}
	//commons
	public List<CFinancialYear> getAllActiveFinancialYearList(){
		return PayrollManagersUtill.getCommonsService().getAllActiveFinancialYearList();
		
	}
	public String getCurrYearFiscalId(){
		return PayrollManagersUtill.getCommonsService().getCurrYearFiscalId();
	}
	public CFinancialYear findFinancialYearById(Long id){
		return PayrollManagersUtill.getCommonsService().findFinancialYearById(id);
	}
	 public Bank getBankById(Integer id){
		 return PayrollManagersUtill.getCommonsService().getBankById(id);
	 }
	 public Bankbranch getBankbranchById(Integer id)
	 {
		 return PayrollManagersUtill.getCommonsService().getBankbranchById(id);
	 }
	 public EgwStatus getStatusByModuleAndDescription(String moduleType,String description)
	 {
		 return PayrollManagersUtill.getCommonsService().getStatusByModuleAndCode(moduleType, description);
		 
	 }
	 public  ObjectType getObjectTypeByType(String type)
	 {
		 return PayrollManagersUtill.getCommonsService().getObjectTypeByType(type); 
	 }
	 public  ObjectHistory createObjectHistory(ObjectHistory objhistory)
	 {
		 return PayrollManagersUtill.getCommonsService().createObjectHistory(objhistory);
	 }
	 public EgwStatus findEgwStatusById(Integer id)
	 {
		 return PayrollManagersUtill.getCommonsService().findEgwStatusById(id); 
	 }
	 
	 public void createEgwSatuschange(EgwSatuschange egwSatuschange){
		 PayrollManagersUtill.getCommonsService().createEgwSatuschange(egwSatuschange);
	 }
	 public List<Recovery> getAllTdsByPartyType(String partyType){
		return recoveryService.getAllTdsByPartyType(partyType);	 
	 }
	 public CChartOfAccounts getCChartOfAccountsByGlCode(String glCode){
		 return PayrollManagersUtill.getCommonsService().getCChartOfAccountsByGlCode(glCode); 
	 }
	 public  Recovery getTdsById(Integer tdsId){
		 return recoveryService.getTdsById(tdsId.longValue());
	 }
	 public  CChartOfAccounts getCChartOfAccountsById(Long chartOfAccountsId)
	 {
		 return PayrollManagersUtill.getCommonsService().getCChartOfAccountsById(chartOfAccountsId);		 
	 }
	 public List<org.egov.model.recoveries.EgDeductionDetails> findByTds(Recovery tds){
		 return recoveryService.findByTds(tds); 
	 }
	 public List<CFinancialYear> getAllActivePostingFinancialYear(){
		 return PayrollManagersUtill.getCommonsService().getAllActivePostingFinancialYear();
	 }
	 public String getCurrentDate(Connection connection)throws Exception{
		 return PayrollManagersUtill.getCommonsService().getCurrentDate(connection); 
	 }
	 public String getTxnNumber(String txnType,String vDate,Connection con) throws Exception{
		 return PayrollManagersUtill.getCommonsService().getTxnNumber(txnType, vDate, con);
	 }
	 public CChartOfAccounts findCodeByPurposeId(Long purposeId) throws Exception{
		 return PayrollManagersUtill.getCommonsService().getCChartOfAccountsById(purposeId); 
	 }
	/* public int getDetailTypeIdByName(String glCode,Connection connection,String name) throws Exception{
		 return PayrollManagersUtill.getCommonsService().getDetailTypeIdByName(glCode, name);
	 }*/
	 public Accountdetailtype getAccountDetailTypeIdByName(String glCode,String name) throws Exception{
		 return PayrollManagersUtill.getCommonsService().getAccountDetailTypeIdByName(glCode,name);
	 }
	 public  CVoucherHeader findVoucherHeaderById(Long id) throws Exception{
		 return PayrollManagersUtill.getCommonsService().findVoucherHeaderById(id);
	 }
	 public List<EgDeductionDetails> getEgDeductionDetailsFilterBy(Recovery tds, BigDecimal amount, String date, EgwTypeOfWork egwTypeOfWork, EgwTypeOfWork egwSubTypeOfWork){
		 return recoveryService.getEgDeductionDetailsFilterBy(tds, amount, date, egwTypeOfWork, egwSubTypeOfWork);
	 }
	/* public int getDetailTypeId(String glCode,Connection connection) throws Exception{
		 return PayrollManagersUtill.getCommonsService().getDetailTypeId(glCode, connection);
	 }*/
	 public void createEgActiondetails(EgActiondetails egActiondetails){
		  PayrollManagersUtill.getCommonsService().createEgActiondetails(egActiondetails);
	 }
	 public List<CFinancialYear> getAllFinancialYearList(){
		return PayrollManagersUtill.getCommonsService().getAllFinancialYearList();
	 }
	 public List<EgwStatus> getStatusByModule(String moduleType){
		 return PayrollManagersUtill.getCommonsService().getStatusByModule(moduleType);
	 }
	 //department
	 public Department getDepartment(Integer deptId){
		 return PayrollManagersUtill.getDepartmentService().getDepartment(deptId);
	 }
	 public List getAllDepartments(){
		 return PayrollManagersUtill.getDepartmentService().getAllDepartments();
	 }
	//exception
	 public boolean isEmpHasExceptionForFullMonth(Integer empid,GregorianCalendar fromdate,GregorianCalendar todate) throws Exception{
		 return PayrollManagersUtill.getExceptionService().isEmpHasExceptionForFullMonth(empid, fromdate, todate);
	 }
	 //masters
	 public Accountdetailtype getAccountdetailtypeByName(String name){
		 return PayrollManagersUtill.getMastersService().getAccountdetailtypeByName(name);
	 }
	//billsa/cMgr
	 public long createVoucherFromBill(int billId) throws EGOVRuntimeException{
		 return PayrollManagersUtill.getBillsAccountingService().createVoucherFromBill(billId);
	 }
	 //usermanager
	  public User getUserByUserName(String userName)
	 {
		  return PayrollManagersUtill.getUserService().getUserByUserName(userName);
	 }
	  public User getUserByID(Integer id){
		  return PayrollManagersUtill.getUserService().getUserByID(id); 
	  }
	 //eis related apis
	 public  PersonalInformation getEmloyeeById(Integer employeeId){
		 return PayrollManagersUtill.getEmployeeService().getEmloyeeById(employeeId);
	 }
	 public  PersonalInformation createEmloyee(PersonalInformation egpimsPersonalInformation)
	 {
		 return PayrollManagersUtill.getEmployeeService().createEmloyee(egpimsPersonalInformation);
	 }
	 public  GenericMaster getGenericMaster(Integer masterId,String masterName)
	 {
		 return PayrollManagersUtill.getEmployeeService().getGenericMaster(masterId, masterName);
	 }
	 public  void updateBankDet(BankDet bankDet)
	 {
		 PayrollManagersUtill.getEmployeeService().updateBankDet(bankDet);
	 }
	 public  void addBankDets(PersonalInformation personalInformation,BankDet egpimsBankDet)
	 {
		 PayrollManagersUtill.getEmployeeService().addBankDets(personalInformation,egpimsBankDet);
	 }
	 public  Map getMapForList(List list){
		 return PayrollManagersUtill.getEmployeeService().getMapForList(list);
	 }
	 public  Assignment getAssignmentByEmpAndDate(Date date,Integer empId){
		 return PayrollManagersUtill.getEmployeeService().getAssignmentByEmpAndDate(date,empId);
	 }
	 public  PersonalInformation getEmpForUserId(Integer userId){
		 return PayrollManagersUtill.getEmployeeService().getEmpForUserId(userId); 
	 }
	 public  Assignment getLatestAssignmentForEmployee(Integer empId){
		 return PayrollManagersUtill.getEmployeeService().getLatestAssignmentForEmployee(empId);
	 }
	 public  List getListOfEmpforDept(Integer deptId){
		 return PayrollManagersUtill.getEmployeeService().getListOfEmpforDept(deptId);
	 }
	 public  Position getPositionByUserId(Integer userId){
		 return PayrollManagersUtill.getEisCommonsService().getPositionByUserId(userId);
	 }
	 public Assignment getLatestAssignmentForEmployeeByToDate(Integer empId,Date todate) throws Exception{
		 return PayrollManagersUtill.getEmployeeService().getLatestAssignmentForEmployeeByToDate(empId, todate);
	 }
	 public  List searchEmployeeByGrouping(LinkedList<String> groupingByOrder)throws Exception{
		 return PayrollManagersUtill.getEmployeeService().searchEmployeeByGrouping(groupingByOrder); 
	 }
	 public  Position getSuperiorPositionByObjType(Position position,String obType){
		 return PayrollManagersUtill.getEisCommonsService().getSuperiorPositionByObjType(position, obType); 
	 }
	 public  Position getPositionforEmp(Integer empId){
		 return PayrollManagersUtill.getEmployeeService().getPositionforEmp(empId); 
	 }
	 public  Position getInferiorPositionByObjType(Position position,String obType){
		 return PayrollManagersUtill.getEisCommonsService().getInferiorPositionByObjType(position, obType);
	 }
	 public  User getSupUserforPositionandObjectType(Position pos,ObjectType objType){
		 return PayrollManagersUtill.getEisCommonsService().getSupUserforPositionandObjectType(pos, objType);
	 }
	 public List getListOfPersonalInformationByEmpIdsList(List empIdsList){
		 return PayrollManagersUtill.getEmployeeService().getListOfPersonalInformationByEmpIdsList(empIdsList);
	 }
	 //leave
	 public   EmployeeAttendenceReport getEmployeeAttendenceReportBetweenTwoDates(Date fromDate, Date toDate,PersonalInformation personalInformation){
		 return PayrollManagersUtill.getEmpLeaveService().getEmployeeAttendenceReportBetweenTwoDates(fromDate,toDate,personalInformation);
	 }
	 public List<LeaveApplication> getEncashmentLeaveApplicationByStatus(String statusName)throws Exception
	 {
		 return PayrollManagersUtill.getEmpLeaveService().getEncashmentLeaveApplicationByStatus(statusName);
	 }
	 public  TypeOfLeaveMaster getTypeOfLeaveMasterByName(String name){
		 return PayrollManagersUtill.getEmpLeaveService().getTypeOfLeaveMasterByName(name);
	 }
	 public Float getAvailableLeavs(Integer empId,Integer type,Date givenDate){
		 return PayrollManagersUtill.getEmpLeaveService().getAvailableLeavs(empId, type, givenDate);
	 }
	 public boolean checkLeave(Integer empId){
		 return PayrollManagersUtill.getEmpLeaveService().checkLeave(empId);
	 }
	 //rbac
	 public Action getActionById(Integer id){
		return PayrollManagersUtill.getRbacService().getActionById(id);
	 }
	 public Action getActionByURL(String url){
		 return PayrollManagersUtill.getRbacService().getActionByURL(url, url); 
	 }
	 //boundary
	 public String getBoundaryNameForID(Integer id){
		 return PayrollManagersUtill.getBoundaryService().getBoundaryNameForID(id);
	 }
     
	 public String getCurrentDate()throws Exception{
		 return PayrollManagersUtill.getCommonsService().getCurrentDate(null); 
	 }

	 public Boolean isBudgetCheckSucceedForBill(EgBillregister billRegister){		 
		Integer budgetCheck = 0; 
	 	List<AppConfigValues> list = GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF","budgetCheckRequired");
		if("Y".equalsIgnoreCase(((AppConfigValues)list.get(0)).getValue())){
			ScriptContext scriptContext = ScriptService.createContext("voucherService",voucherService,"bill",billRegister);
			budgetCheck = (Integer)scriptExecutionService.executeScript("egf.bill.budgetcheck", scriptContext);
			
			if(budgetCheck==1){
				return true;
			}
			else{
				throw new ValidationException(Arrays.asList(new ValidationError("Budget check failed","Budget Check failed")));
			}			
		}
		else{
			return true;
		}		
	 }
	 
	 public List<Accountdetailtype> getAccountdetailtypeListByGLCode(final String glCode) throws EGOVException{
		 return PayrollManagersUtill.getCommonsService().getAccountdetailtypeListByGLCode(glCode);
	 }
	 public void doAuditing(AuditEntity auditEntity, String action, SalaryCodes salaryCode) {	
			final String details1 = new StringBuffer("[ Name : ").
									append(salaryCode.getHead()).append(", Calculation Type : ").append(salaryCode.getCalType()).
									append(", Account code : ").append(salaryCode.getChartofaccounts()!=null?(salaryCode.getChartofaccounts().getGlcode()):(salaryCode.getTdsId()!=null && salaryCode.getTdsId().getChartofaccounts() != null)?salaryCode.getTdsId().getChartofaccounts().getGlcode():"").append(" ]").toString();
			final String details2 = new StringBuffer("[ Is Recurring : ").
					append(salaryCode.getIsRecurring()).append(", Recovery Code : ").append(salaryCode.getTdsId() != null?salaryCode.getTdsId().getRecoveryName():"").append(" ]").toString();	

			final AuditEvent auditEvent = new AuditEvent(AuditModule.PAYROLL, auditEntity, action, salaryCode.getHead(), details1);
			auditEvent.setDetails2(details2);
			auditEvent.setPkId(Long.valueOf(salaryCode.getId()));
			this.auditEventService.createAuditEvent(auditEvent, SalaryCodes.class);
		}

		public AuditEventService getAuditEventService() {
			return auditEventService;
		}

		public void setAuditEventService(AuditEventService auditEventService) {
			this.auditEventService = auditEventService;
		}

	 
}
