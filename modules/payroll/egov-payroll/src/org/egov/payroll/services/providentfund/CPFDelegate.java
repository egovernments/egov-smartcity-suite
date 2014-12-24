package org.egov.payroll.services.providentfund;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.Accountdetailkey;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.payroll.dao.EmpPayrollDAO;
import org.egov.payroll.dao.PayrollDAOFactory;
import org.egov.payroll.model.Deductions;
import org.egov.payroll.model.Earnings;
import org.egov.payroll.model.EmpPayroll;
import org.egov.payroll.model.providentfund.PFHeader;
import org.egov.payroll.model.providentfund.PFTriggerDetail;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.pims.model.EmployeeView;


/**
 * @author Jagadeesan
 *
 */
public class CPFDelegate
{
	private static final Logger LOGGER = Logger.getLogger(CPFDelegate.class);
	private static final String FUNCTIONSTR ="function";
	private PersistenceService<PFTriggerDetail, Long> cpfTriggerDtlService;
	private PersistenceService persistenceService;
	private PayrollExternalInterface payrollExternalInterface;
	private boolean isTestMode = false;
	
	public boolean isTestMode() {
		return isTestMode;
	}

	public void setTestMode(boolean isTestMode) {
		this.isTestMode = isTestMode;
	}

	public void setPayrollExternalInterface(PayrollExternalInterface payrollExternalInterface) {
		this.payrollExternalInterface = payrollExternalInterface;
	}

	public void CPFCalculation(int userId,Date currentDate,String cpfHeaderId,String cpfMonth,String cpfFinancialYearId,List<String> mandatoryFields) throws ServletException,Exception
	{
		LOGGER.info("=======================CPF calculation method calling at="+currentDate);

		String detail[] = null;
		try
		{
			if(!isTestMode){
				HibernateUtil.beginTransaction();
			}
			EGOVThreadLocals.setUserId(String.valueOf(userId));
			EmployeeView employeeView = null;
			EmployeeView tmpEmployeeView =null;
			LinkedList<String> groupingByOrderList=new LinkedList<String>();
			
			PFHeader pfHeader = getCPFHeader(cpfHeaderId,cpfMonth,cpfFinancialYearId);
			
			String tdsId=pfHeader.getTds().getId().toString();
			// to get all the active employees
			
			if(mandatoryFields.contains("fund")){
				groupingByOrderList.add("FundCode");
			}
			if(mandatoryFields.contains("department")){
				groupingByOrderList.add("DeptCode");
			}
			if(mandatoryFields.contains(FUNCTIONSTR)){
				groupingByOrderList.add("FunctionCode");
			}
			
			List empList = payrollExternalInterface.searchEmployeeByGrouping(groupingByOrderList);
			
			String cpfAccountCode[] = getPFAccountCodes("cpfaccount");
			String cpfExpAccountCode[] = getPFAccountCodes("cpfexpaccount");

			//To get status obj of emppayroll for payslip approved.
			EgwStatus status = (EgwStatus) payrollExternalInterface.getStatusByModuleAndDescription(
					PayrollConstants.PAYSLIP_MODULE, GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payslip","PayslipAuditApprovedStatus",new Date()).getValue());
			
			HashMap tmpEmpMap = null;
			String detailKey="",detailTypeId="",fundCode="",functionCode="",deptCode="",empId=""; 
			LOGGER.info("Employee list size="+empList.size());
			int noOfJV=0;
			int noOfEmp=0;
			String groupByCode="";
			String tempGroupByCode="";
			BigDecimal empCPFAmt=BigDecimal.ZERO;
			BigDecimal totFundwiseCPFAmt=BigDecimal.ZERO;
			List tempFundwiseList=new ArrayList();
			
			for(int i=0;i<=empList.size();i++)
			{
				if(i<empList.size())
				{
					employeeView = (EmployeeView) empList.get(i);
					
					//Try to skip the employee if below details not there.
					if(employeeView.getAssignment().getFundId()==null || employeeView.getAssignment().getFunctionId()==null || employeeView.getDeptId()==null )
					{
						LOGGER.error("CPF calculation not considered due to Fund/Function/Department not defined for employeeCode"+employeeView.getEmployeeCode());
						continue;
					}
					
					fundCode =""+employeeView.getAssignment().getFundId().getCode();
					functionCode=""+employeeView.getAssignment().getFunctionId().getCode();
					deptCode =""+employeeView.getDeptId().getDeptCode();
					groupByCode="";
					if(mandatoryFields.contains("fund")){
						groupByCode += ""+fundCode;
					}
					if(mandatoryFields.contains("department")){
						groupByCode += ""+functionCode;
					}
					if(mandatoryFields.contains(FUNCTIONSTR)){
						groupByCode += ""+deptCode;
					}
					
					LOGGER.info("groupByCode===="+groupByCode);
					empId = ""+employeeView.getId();
					
					if(!"".equals(tempGroupByCode) && !tempGroupByCode.equals(groupByCode))
					{
						LOGGER.info(" when difference there, totFundwiseCPFAmt== "+totFundwiseCPFAmt);
						if(totFundwiseCPFAmt.compareTo(BigDecimal.ZERO)!=0)
						{
							LOGGER.info(" when difference there,  empId="+empId+"==fundCode=="+fundCode+"==functionCode=="+functionCode+"==deptCode=="+deptCode+"==totFundwiseCPFAmt=="+totFundwiseCPFAmt);
							setParamValuesForCreateVoucher(userId, totFundwiseCPFAmt,cpfExpAccountCode[0],cpfAccountCode[0],tdsId,tempFundwiseList,currentDate,tmpEmployeeView,mandatoryFields);
							noOfJV++;
						}
						tempFundwiseList=new ArrayList();
						totFundwiseCPFAmt = BigDecimal.ZERO;
					}
					tmpEmployeeView = employeeView;
					LOGGER.info("empId="+empId+"==fundCode=="+fundCode+"==functionCode=="+functionCode+"==deptCode=="+deptCode);

					//Get the emp Detail Key and detail type id.
					detail = getDetailKeyForEmp(Integer.parseInt(empId));
					detailKey = detail[0];
					detailTypeId = detail[1];
					// if detailkey is null for the employee, skip it
					LOGGER.info("##################### detailKey="+detailKey);
					if(detailKey == null){
						continue;
					}
					
					//Get the empCPF Amt using python script
					empCPFAmt = getEmpCPFAmt(Integer.parseInt(empId),pfHeader,status.getId().toString());
					LOGGER.info("##################### empCPFAmt="+empCPFAmt);

					if(empCPFAmt.compareTo(BigDecimal.ZERO)==0){
						continue;
					}
					totFundwiseCPFAmt = totFundwiseCPFAmt.add(empCPFAmt);
					
					noOfEmp++;
					LOGGER.info("##################### totFundwiseCPFAmt="+totFundwiseCPFAmt+",empCPFAmt="+empCPFAmt+",detailKey="+detailKey+",groupcode="+""+fundCode+""+functionCode+""+deptCode);
					tmpEmpMap = new HashMap();
					tmpEmpMap.put(VoucherConstant.DETAILKEYID, detailKey);
					tmpEmpMap.put(VoucherConstant.DETAILTYPEID, detailTypeId);
					tmpEmpMap.put(PayrollConstants.DETAIL_AMOUNT, empCPFAmt);
					tempFundwiseList.add(tmpEmpMap);
					
					tempGroupByCode = groupByCode;
				}
				else //to execute after last employee record.
				{
					LOGGER.info(" At last reocrd, totFundwiseCPFAmt== "+totFundwiseCPFAmt);
					if(totFundwiseCPFAmt.compareTo(BigDecimal.ZERO)!=0)
					{
						LOGGER.info(" At last record,  empId="+empId+"==fundCode=="+fundCode+"==functionCode=="+functionCode+"==deptCode=="+deptCode+"==totFundwiseCPFAmt=="+totFundwiseCPFAmt);
						setParamValuesForCreateVoucher(userId, totFundwiseCPFAmt,cpfExpAccountCode[0],cpfAccountCode[0],tdsId,tempFundwiseList,currentDate,tmpEmployeeView,mandatoryFields);
						noOfJV++;
					}
				}
			}
			LOGGER.info("##################### noOfJV="+noOfJV+",noOfEmp="+noOfEmp);
			
			//To add the record in pftriggerdetail if once JV passed successfully
			PFTriggerDetail cpfTriggerDetail = new PFTriggerDetail();
			
			if(noOfJV>0){  //Atleast one JV need to create.
				LOGGER.info("cpfTriggerDearRange()======================");
				cpfTriggerDetail.setPfType("CPF");
				cpfTriggerDetail.setMonth(pfHeader.getMonth());
				cpfTriggerDetail.setFinancialYear(pfHeader.getFinancialYear());
				cpfTriggerDetail.setCreatedBy(payrollExternalInterface.getUserByID(Integer.valueOf(userId)));
				cpfTriggerDetail.setCreatedDate(new Date());
				cpfTriggerDtlService.create(cpfTriggerDetail);
				LOGGER.info("cpfTriggerDetail id ===="+cpfTriggerDetail.getId()+"===month=="+cpfTriggerDetail.getMonth() +"===Year=="+cpfTriggerDetail.getFinancialYear().getFinYearRange());
			}
			LOGGER.info("before committing the transaction in CPF  calculation");
			if(!isTestMode){
				HibernateUtil.commitTransaction();
			}
			LOGGER.info("After committing the transaction in CPF calculation");

		}catch(EGOVRuntimeException ex)
	    {
	        LOGGER.error("EGOVRuntimeException Encountered!!!"+ ex.getMessage());
	        HibernateUtil.rollbackTransaction();
	        throw ex;
	    }
		catch(Exception e)
		{
			LOGGER.error("Exception in CPF calculation="+e.getMessage());
			HibernateUtil.rollbackTransaction();	
		}
		finally
		{
			if(!isTestMode){
				HibernateUtil.closeSession();
			}
		}
	}
	
	public void setParamValuesForCreateVoucher(int userid,BigDecimal totalAmt,String pfExpenseAccount,String pfPayableAccount,String tdsId,List tempList,Date currentDate,EmployeeView employeeView,List<String> mandatoryFields) throws Exception 
	{
		Connection con = null;
		
		try{
			
			//Setting voucher header detials
			HashMap<String,Object> headerdetails = new HashMap<String,Object>();
			headerdetails.put(VoucherConstant.VOUCHERNAME,"CPF");
			headerdetails.put(VoucherConstant.VOUCHERTYPE,"Journal Voucher");
			headerdetails.put(VoucherConstant.DESCRIPTION,"");
			headerdetails.put(VoucherConstant.VOUCHERDATE,currentDate);
			String fundcode=employeeView.getAssignment().getFundId().getCode();
			String functioncode=employeeView.getAssignment().getFunctionId().getCode();
			String deptcode = employeeView.getAssignment().getDeptId().getDeptCode();
			/*if(fundcode!=null){
				headerdetails.put(VoucherConstant.FUNDCODE,fundcode);
			}
			if(deptcode!=null){
				headerdetails.put(VoucherConstant.DEPARTMENTCODE,deptcode);
			}*/
			
			if(mandatoryFields.contains("fund")){
				headerdetails.put(VoucherConstant.FUNDCODE,fundcode);
			}
			if(mandatoryFields.contains("department")){
				headerdetails.put(VoucherConstant.DEPARTMENTCODE,deptcode);
			}
			
			
			Module egModule= (Module)GenericDaoFactory.getDAOFactory().getModuleDao().getModuleByName("Provident Fund");
			if(egModule!=null){
				headerdetails.put(VoucherConstant.MODULEID,egModule.getId().toString());
			}
			
			//Setting account details
			List<HashMap<String,Object>> accountcodedetails = new ArrayList<HashMap<String,Object>>();
			HashMap<String,Object> accountMap = new HashMap<String,Object>();
			accountMap.put(VoucherConstant.GLCODE, pfExpenseAccount);
			accountMap.put(VoucherConstant.DEBITAMOUNT, totalAmt);
			accountMap.put(VoucherConstant.CREDITAMOUNT, 0);
			/*if(functioncode!=null){
				accountMap.put(VoucherConstant.FUNCTIONCODE,""+functioncode);
			}*/
			if(mandatoryFields.contains(FUNCTIONSTR)){
				accountMap.put(VoucherConstant.FUNCTIONCODE,""+functioncode);
			}
			accountcodedetails.add(accountMap);
			
			
			accountMap = new HashMap<String,Object>();
			accountMap.put(VoucherConstant.GLCODE, pfPayableAccount);
			accountMap.put(VoucherConstant.DEBITAMOUNT, 0);
			accountMap.put(VoucherConstant.CREDITAMOUNT, totalAmt);
			/*if(functioncode!=null){
				accountMap.put(VoucherConstant.FUNCTIONCODE,""+functioncode);
			}*/
			if(mandatoryFields.contains(FUNCTIONSTR)){
				accountMap.put(VoucherConstant.FUNCTIONCODE,""+functioncode);
			}
			accountcodedetails.add(accountMap);
			
			
			//Setting subledger details
			List<HashMap<String,Object>> subledgerdetails = new ArrayList<HashMap<String,Object>>();
			
			if(!payrollExternalInterface.getAccountdetailtypeListByGLCode(pfExpenseAccount).isEmpty())
			{
				HashMap<String,Object> empMap = null;
				for(int i=0;i<tempList.size();i++)
				{
					empMap = new HashMap<String,Object>();
					empMap = (HashMap) tempList.get(i);
					empMap.put(VoucherConstant.GLCODE, pfExpenseAccount);
					empMap.put(VoucherConstant.DETAILKEYID, empMap.get(VoucherConstant.DETAILKEYID).toString());
					empMap.put(VoucherConstant.DETAILTYPEID, empMap.get(VoucherConstant.DETAILTYPEID).toString());
					empMap.put(VoucherConstant.DEBITAMOUNT, empMap.get(PayrollConstants.DETAIL_AMOUNT).toString());
					subledgerdetails.add(empMap);
				}
			}
			
			if(!payrollExternalInterface.getAccountdetailtypeListByGLCode(pfPayableAccount).isEmpty())
			{
				HashMap<String,Object> empMap = null;
				for(int i=0;i<tempList.size();i++)
				{
					empMap = new HashMap<String,Object>();
					empMap = (HashMap) tempList.get(i);
					empMap.put(VoucherConstant.GLCODE, pfPayableAccount);
					empMap.put(VoucherConstant.TDSID, tdsId);
					empMap.put(VoucherConstant.DETAILKEYID, empMap.get(VoucherConstant.DETAILKEYID).toString());
					empMap.put(VoucherConstant.DETAILTYPEID, empMap.get(VoucherConstant.DETAILTYPEID).toString());
					empMap.put(VoucherConstant.CREDITAMOUNT, empMap.get(PayrollConstants.DETAIL_AMOUNT).toString());
					subledgerdetails.add(empMap);
				}
			}

			CreateVoucher cv = new CreateVoucher();
			CVoucherHeader vh = cv.createVoucher(headerdetails, accountcodedetails, subledgerdetails);
			LOGGER.info("voucherId=="+vh.getId()+"==vouhcer no=="+vh.getVoucherNumber());
		}
		catch(EGOVRuntimeException egovE)
		{
			LOGGER.error("EGOVRuntimeException = "+egovE.getMessage());
			throw egovE;
		}
		catch(Exception e)
		{
			LOGGER.error("Exception = "+e.getMessage());
		}
		finally{
			try{
				con.close();
			}catch(Exception e){LOGGER.error("Exception = "+e.getMessage());}
		}
	}
	
	/**
	 * To get the employee detail key
	 * @param empId
	 * @return
	 */
	public String[] getDetailKeyForEmp(Integer empId) throws Exception
	{
		String emp[] = new String[2];
		try
		{
			String query = " select adk from Accountdetailkey adk " +
						   " where  adk.accountdetailtype=(select accDtlType.id from Accountdetailtype accDtlType where accDtlType.name=? ) " +
						   " and adk.detailkey =? ";
			Accountdetailkey accDtlKey = (Accountdetailkey)persistenceService.find(query,"Employee",empId);
			
			if(accDtlKey!=null){
				emp[0] = accDtlKey.getDetailkey().toString();
				emp[1] = accDtlKey.getAccountdetailtype().getId().toString();
			}
		}
		catch(Exception e)
		{
			LOGGER.error("Exception while getting the DetailKeyForEmp="+e.getMessage());
			throw e;
		}
		return emp;
	}
	
	/**
	 * To get the glcode 
	 * @param id-> glcode id
	 * @param whichId --> which account is going to match
	 * @return glcode from chartofaccounts
	 */
	public String[] getPFAccountCodes(String whichId) throws Exception
	{
		String glcode[]=new String[3];
		try
		{
			String query ="";

			if(whichId.equalsIgnoreCase("cpfaccount")){
				query = " select coa from CChartOfAccounts coa, Recovery td,PFHeader pfh " +
						" where  coa.isActiveForPosting=? and coa.id = td.chartofaccounts.id  and td.id = pfh.tds.id and pfh.pfType=?  ";
			}
			else if(whichId.equalsIgnoreCase("cpfexpaccount")){
				query = " select coa from CChartOfAccounts coa, PFHeader pfh " +
						" where  coa.isActiveForPosting=? and coa.id = pfh.pfIntExpAccount.id  and pfh.pfType=?  ";
			}
			
			CChartOfAccounts chartOfAccount =(CChartOfAccounts) persistenceService.find(query,Long.valueOf(1),"CPF");
			if(chartOfAccount!=null){
				glcode[0] = chartOfAccount.getGlcode().toString();
				glcode[1] = chartOfAccount.getName().toString();
				glcode[2] = chartOfAccount.getId().toString();
			}
		}
		catch(Exception e)
		{
			LOGGER.error("Exception while getting the CPFAccountcodes="+e.getMessage());
			throw e;
		}
		return glcode;
	}
	
	/**
	 * To get the interest rate for the particular date
	 * @param fromDate
	 * @param pfAccountId
	 * @return
	 */
	public BigDecimal getEmpCPFAmt(Integer empId,PFHeader pfHeader,String empPayrollApprovedStatus) throws Exception
	{
		BigDecimal empCPFAmt =BigDecimal.ZERO;

		String query = "from EmpPayroll empPayroll where empPayroll.employee.idPersonalInformation="+empId+" and empPayroll.month="+pfHeader.getMonth()+" and empPayroll.financialyear.id = "+pfHeader.getFinancialYear().getId()+" and empPayroll.status.id="+Integer.parseInt(empPayrollApprovedStatus) ;
		List<EmpPayroll> empPayrollList = (List)persistenceService.findAllBy(query);
		if(empPayrollList!=null && !empPayrollList.isEmpty()){
			LOGGER.info("EMP  payroll with status approved for emp=="+empId);
			empCPFAmt = computeRuleBasedCPFAmt(empPayrollList,pfHeader);
		}
		return empCPFAmt;
	}
	
	/**
	 * @param cpfHeaderId
	 * @param cpfMonth
	 * @param cpfFinancialYearId
	 * @return
	 */
	public PFHeader getCPFHeader(String cpfHeaderId,String cpfMonth,String cpfFinancialYearId)
	{
		PFHeader pfHeader =(PFHeader)persistenceService.find("from PFHeader where id =?",Long.valueOf(cpfHeaderId));
		pfHeader.setMonth(Integer.valueOf(cpfMonth));
		pfHeader.setFinancialYear((CFinancialYear)persistenceService.find("from CFinancialYear where id=?",Long.valueOf(cpfFinancialYearId)));
		return pfHeader;
	}
	
	/**
	 * @param empPayroll
	 * @param pfHeader
	 * @return
	 * @throws Exception
	 */
	public static BigDecimal computeRuleBasedCPFAmt(List<EmpPayroll> empPayrollList,PFHeader pfHeader) throws Exception{
		
		BigDecimal empCPFAmt=BigDecimal.ZERO;

		if(pfHeader.getRuleScript()!=null && !pfHeader.getRuleScript().getName().equals(""))
		{
			EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
			
			String actionName = pfHeader.getRuleScript().getName();
			String scriptName = "Payroll.CPFHeader."+actionName;
			Script script =empPayrollDAO.getScript(scriptName);

			if(script ==null){
				throw new EGOVRuntimeException("Script not found for "+scriptName);
			}else{
				try{
					//empCPFAmt =new BigDecimal( script.eval(Script.createContext("empPayrollList",empPayrollList,"cpfDelegate",new CPFDelegate())).toString());
					//TODO - commenting above line for time being and need to fix Script.createContext
			    }catch (ValidationException e) {
			    	String errorMsg = "";
			    	for(ValidationError vr : e.getErrors()){
			    		errorMsg += vr.getMessage(); 				  
			    	}
			    	LOGGER.error(errorMsg);
			    }
			}
		}
		LOGGER.info("CPFAmt calculated by rulebased for emp=="+empCPFAmt +"===emppayrollList size"+empPayrollList.size());
		return empCPFAmt;
	}
	
	/**
	 * getting earning object from payslip by earnig
	 * @throws EGOVException 
	 */
	public Earnings getEarningByNameFromPayslip(EmpPayroll payslip, String payheadname) throws EGOVException{
		for(Earnings earning : payslip.getEarningses()){
			if(payheadname.equals(earning.getSalaryCodes().getHead())){
				return earning; 
			}
		}		
		return null;
	}
	
	/**
	 * getting deductions object from payslip by deduction for CPF
	 * @throws EGOVException 
	 */
	public Deductions getDeductionByNameFromPayslip(EmpPayroll payslip, String payheadname) throws EGOVException{
		for(Deductions deduction : payslip.getDeductionses()){
			if(payheadname.equals(deduction.getSalaryCodes().getHead())){
				return deduction; 
			}
		}		
		return null;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	

	public PersistenceService<PFTriggerDetail, Long> getCpfTriggerDtlService() {
		return cpfTriggerDtlService;
	}

	public void setCpfTriggerDtlService(
			PersistenceService<PFTriggerDetail, Long> cpfTriggerDtlService) {
		this.cpfTriggerDtlService = cpfTriggerDtlService;
	}

	
}
