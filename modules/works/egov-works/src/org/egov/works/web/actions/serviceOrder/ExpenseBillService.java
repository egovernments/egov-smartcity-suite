/**
 * 
 */
package org.egov.works.web.actions.serviceOrder;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.script.ScriptContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CChartOfAccountDetail;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.EgwStatus;
import org.egov.commons.service.CommonsService;
import org.egov.egf.bills.model.Cbill;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.SequenceGenerator;
import org.egov.infstr.workflow.Action;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregister;
import org.egov.model.voucher.VoucherDetails;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.service.EisCommonsService;
import org.egov.pims.service.EmployeeService;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.FinancialConstants;
import org.egov.utils.VoucherHelper;

/**
 * @author manoranjan
 *
 */
public class ExpenseBillService {
	private static final Logger LOGGER = Logger.getLogger(MeasurementBookAction.class);
	protected final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	protected GenericHibernateDaoFactory genericDao;
	protected CommonsService commonsService;
	protected PersistenceService persistenceService;
	protected ScriptService scriptExecutionService;
	protected SequenceGenerator sequenceGenerator;
	protected VoucherService voucherService;
	protected EmployeeService employeeService;
	protected EisCommonsService	eisCommonsService;
	@SuppressWarnings("unchecked")
	public Cbill createExpenseBill(List<VoucherDetails> billDetailslist,List<VoucherDetails> subLedgerlist,Cbill cbill){
		
		LOGGER.debug("ExpenseBillService | createExpenseBill | Start");
		validateData(billDetailslist, subLedgerlist);
		cbill = addBillInfo(cbill);
		createBillDetails(billDetailslist,subLedgerlist,cbill);
		//checkBudgetandGenerateNumber(cbill);
		persistenceService.setType(Cbill.class);
		persistenceService.persist(cbill);       
		
		LOGGER.debug("ExpenseBillService | createExpenseBill | END");
		return cbill;
		
	}
	
	private Cbill addBillInfo(Cbill cbill){
		
		if(isBillNumberGenerationAuto()){
			cbill.setBillnumber(getNextBillNumber(cbill));
		}else if(null == cbill.getBillnumber() || StringUtils.isEmpty(cbill.getBillnumber()) ){
			
			throw new ValidationException(Arrays.asList(new ValidationError("error","Bill number is required")));
		}
		cbill.setBillstatus(FinancialConstants.CONTINGENCYBILL_CREATED_STATUS);
		String statusQury="from EgwStatus where upper(moduletype)=upper('"+FinancialConstants.CONTINGENCYBILL_FIN+"') and  upper(description)=upper('"+FinancialConstants.CONTINGENCYBILL_CREATED_STATUS+"')";
		EgwStatus egwStatus=(EgwStatus)persistenceService.find(statusQury);
		cbill.setStatus(egwStatus);
		cbill.setBilltype("Final Bill");
		cbill.setExpendituretype("Expense");
		return cbill;
	}
	
	private  boolean isBillNumberGenerationAuto()
	{
		LOGGER.debug("ExpenseBillService | isBillNumberGenerationAuto | Start");
		List<AppConfigValues> configValuesByModuleAndKey = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF","Bill_Number_Geneartion_Auto");
		return configValuesByModuleAndKey.get(0).getValue().equalsIgnoreCase("y")?true:false;
	}
	
	private String getNextBillNumber(Cbill bill) {
		LOGGER.debug("ExpenseBillService | getNextBillNumber | Start");
		String billNumber=null;
		CFinancialYear financialYear =  commonsService.getFinancialYearByDate(bill.getBilldate());  
		if(financialYear==null)
		{
			throw new ValidationException(Arrays.asList(new ValidationError("no.financial.year","No Financial Year for bill date"+sdf.format(bill.getBilldate()))));
		}
		String year=financialYear.getFinYearRange();
		Script billNumberScript=(Script)persistenceService.findAllByNamedQuery(Script.BY_NAME, "egf.bill.number.generator").get(0);
		ScriptContext scriptContext = ScriptService.createContext("sequenceGenerator",sequenceGenerator,"sItem",bill,"year",year);
		billNumber =(String) scriptExecutionService.executeScript(billNumberScript.getName(), scriptContext);
		if(billNumber==null)  
		{
			throw new ValidationException(Arrays.asList(new ValidationError("error","No Financial Year for bill date"+sdf.format(bill.getBilldate()))));
		}
		LOGGER.debug("ExpenseBillService | getNextBillNumber | End");
		
		return billNumber;
	}
	private Cbill createBillDetails(List<VoucherDetails> billDetailslist,List<VoucherDetails> subLedgerlist,Cbill cbill) {
		EgBilldetails billdetails;
		EgBillPayeedetails payeedetails;
		Set<EgBillPayeedetails> payeedetailsSet=null;
		Set<EgBilldetails> billdetailsSet=new HashSet<EgBilldetails>();
      
		BigDecimal billamount = BigDecimal.ZERO;
		for(VoucherDetails ledgerDetails:billDetailslist )
		{
			billdetails=new EgBilldetails();
			billdetails.setGlcodeid(BigDecimal.valueOf(ledgerDetails.getGlcodeIdDetail()));
			if(ledgerDetails.getFunctionIdDetail()!=null)
			{
				billdetails.setFunctionid(BigDecimal.valueOf(ledgerDetails.getFunctionIdDetail()));
			}
			if(ledgerDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) == 1){
				billdetails.setDebitamount(ledgerDetails.getDebitAmountDetail());
			}else{
				billdetails.setCreditamount(ledgerDetails.getCreditAmountDetail());
			}
			billamount = billamount.add(ledgerDetails.getDebitAmountDetail());
			payeedetailsSet=new HashSet<EgBillPayeedetails>();
			for(VoucherDetails subledgerDetails:subLedgerlist )
			{
				if(ledgerDetails.getGlcodeDetail().equals(subledgerDetails.getSubledgerCode()))
				{
					payeedetails=new EgBillPayeedetails();
					if(null != ledgerDetails.getDebitAmountDetail() && ledgerDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) ==1)
					payeedetails.setDebitAmount(subledgerDetails.getAmount());
					
					payeedetails.setAccountDetailKeyId(subledgerDetails.getDetailKeyId());
					payeedetails.setAccountDetailTypeId(subledgerDetails.getDetailType().getId());
					payeedetails.setEgBilldetailsId(billdetails);
					payeedetailsSet.add(payeedetails);
				}
			}
			if(payeedetailsSet.size() != 0){
				billdetails.setEgBillPaydetailes(payeedetailsSet);
			}
				
			billdetails.setEgBillregister(cbill);
			
			billdetailsSet.add(billdetails);
		}
		
		cbill.setEgBilldetailes(billdetailsSet);
		cbill.setBillamount(billamount);
		cbill.setPassedamount(billamount);
		return cbill;
	}
	private Cbill checkBudgetandGenerateNumber(Cbill bill) {
		
		ScriptContext scriptContext = ScriptService.createContext("voucherService",voucherService,"bill",bill);
		scriptExecutionService.executeScript( "egf.bill.budgetcheck", scriptContext);
		return bill;   
	}
	

	public  List<Action> getValidActions(String purpose , EgBillregister cbill) {
		List<Action> validButtons = new ArrayList<Action>();
		Script validScript = (Script)persistenceService.findAllByNamedQuery(Script.BY_NAME,"cbill.validation").get(0);
		List<String> list =  (List<String>) scriptExecutionService.executeScript(validScript,ScriptService.createContext("employeeServiceBean", employeeService,"userId",Integer.valueOf(EGOVThreadLocals.getUserId().trim()),
				"date",new Date(),"purpose",purpose,"wfitem",cbill));
		for(Object s:list) 
		{
			if("invalid".equals(s))
				break;
			Action action = (Action) persistenceService.find(" from org.egov.infstr.workflow.Action where type='EgBillregister' and name=?", s.toString());
			validButtons.add(action);
		}
		return validButtons;
	}
	
	protected void  validateData(final List<VoucherDetails> billDetailslist, final List<VoucherDetails> subLedgerList){
		BigDecimal totalDrAmt = BigDecimal.ZERO;
		BigDecimal totalCrAmt = BigDecimal.ZERO;
		int index=0;
		boolean isValFailed = false;
		Map<String,List<String>> accCodeFuncMap = new HashMap<String,List<String>> ();
		
		for (VoucherDetails voucherDetails : billDetailslist) {
			index = index+1;
			totalDrAmt = totalDrAmt.add(voucherDetails.getDebitAmountDetail());
			totalCrAmt = totalCrAmt.add(voucherDetails.getCreditAmountDetail());
			
			if(voucherDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO)==0 && 
					voucherDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO) == 0 && voucherDetails.getGlcodeDetail().trim().length()!=0){
				
				throw new ValidationException(Arrays.asList(new ValidationError("journalvoucher.accdetail.amountZero",
						 "Enter debit/credit amount for the account code :"+voucherDetails.getGlcodeDetail())));
			}else if (voucherDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) >0 && 
					voucherDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO)>0) {
				throw new ValidationException(Arrays.asList(new ValidationError("journalvoucher.accdetail.amount",
						 "Account code is missing for credit/debit supplied field in account grid row : "+voucherDetails.getGlcodeDetail())));
			}else if (( voucherDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) >0 
					||voucherDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO) >0)
					&& voucherDetails.getGlcodeDetail().trim().length()==0) {
				throw new ValidationException(Arrays.asList(new ValidationError("journalvoucher.accdetail.accmissing",
						 "Account code is missing for credit/debit supplied field in account grid row : "+index)));
			}
			else {
				String functionId =  voucherDetails.getFunctionIdDetail()!=null?voucherDetails.getFunctionIdDetail().toString():"0";
				List<String> existingFuncs = accCodeFuncMap.get(voucherDetails.getGlcodeDetail());
				if(null == existingFuncs){
					List<String> list = new ArrayList<String>();
					list.add(functionId);
					accCodeFuncMap.put(voucherDetails.getGlcodeDetail(), list); 
				}else{
					if(functionId.equals("0") || existingFuncs.contains("0")){
						throw new ValidationException(Arrays.asList(new ValidationError("jv.multiplecodes.funcMissing",
								 "Function is missing for the repeated account code,check account code : "+voucherDetails.getGlcodeDetail())));
					}else if(existingFuncs.contains(functionId)){
						throw new ValidationException(Arrays.asList(new ValidationError("jv.multiplecodes.duplicateFunc",
						 "Same account code should not be used multiple times with the same function,check account code : "+voucherDetails.getGlcodeDetail())));
					}else{
						existingFuncs.add(functionId);
						accCodeFuncMap.put(voucherDetails.getGlcodeDetail(),existingFuncs); 
					}
					
				}
			}
			
		
		}
		 if (totalDrAmt.compareTo(totalCrAmt) != 0 && !isValFailed) {
			 throw new ValidationException(Arrays.asList(new ValidationError("journalvoucher.accdetail.drcrmatch",
					 "Total Credit and Total Debit amount must be same ")));
		}
		
			 validateSubledgerDetails( billDetailslist,subLedgerList);
	}
	/*
	 * description -  validate the total subledger data.
	 */
	protected void validateSubledgerDetails(List<VoucherDetails> billDetailslist,List<VoucherDetails> subLedgerlist){
		
		Map<String,Object> accountDetailMap ;
		List<Map<String,Object>> subLegAccMap=  new ArrayList<Map<String,Object>>(); // this list will contain  the details about the account code those are detail codes.
		List<String> repeatedglCodes = VoucherHelper.getRepeatedGlcodes(billDetailslist);
		for (VoucherDetails voucherDetails : billDetailslist) {
			CChartOfAccountDetail  chartOfAccountDetail = (CChartOfAccountDetail) persistenceService.find(" from CChartOfAccountDetail" +
					" where glCodeId=(select id from CChartOfAccounts where glcode=?)", voucherDetails.getGlcodeDetail());
			if(null != chartOfAccountDetail){
				accountDetailMap = new HashMap<String,Object>();
				if(repeatedglCodes.contains(voucherDetails.getGlcodeIdDetail().toString())){
					accountDetailMap.put("glcodeId-funcId", voucherDetails.getGlcodeIdDetail()+"-"+voucherDetails.getFunctionIdDetail());
				}else{
					accountDetailMap.put("glcodeId-funcId", voucherDetails.getGlcodeIdDetail()+"-"+"0");
				}
				accountDetailMap.put("glcode", voucherDetails.getGlcodeDetail());
				if(voucherDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO)==0){
					accountDetailMap.put("amount", voucherDetails.getCreditAmountDetail());
				}else if (voucherDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO)==0) {
					accountDetailMap.put("amount", voucherDetails.getDebitAmountDetail());
				}
					subLegAccMap.add(accountDetailMap);
			}
		}
		
		Map<String,BigDecimal> subledAmtmap = new HashMap<String,BigDecimal> ();
		Map<String, String> subLedgerMap = new HashMap<String, String> ();
		for (VoucherDetails voucherDetails : subLedgerlist) {
			
			if(voucherDetails.getGlcode().getId() != 0){
				String function = repeatedglCodes.contains(voucherDetails.getGlcode().getId().toString())?voucherDetails.getFunctionDetail():"0";
				//above is used to check if this account code is used multiple times in the account grid or not, if it used multiple times
				// then take the function into consideration while calculating the total sl amount , else ignore the function by paasing function value=0
				if(null !=subledAmtmap.get(voucherDetails.getGlcode().getId()+"-"+function)){
					BigDecimal debitTotalAmount =subledAmtmap.get(voucherDetails.getGlcode().getId()+"-"+function)
												.add(voucherDetails.getAmount());
					subledAmtmap.put(voucherDetails.getGlcode().getId()+"-"+function,debitTotalAmount);
				}else {
					subledAmtmap.put(voucherDetails.getGlcode().getId()+"-"+function
							,voucherDetails.getAmount());
				}
				StringBuffer subledgerDetailRow = new StringBuffer();
				if(voucherDetails.getDetailType().getId() ==0 || null == voucherDetails.getDetailKeyId()){
					throw new ValidationException(Arrays.asList(new ValidationError("journalvoucher.subledger.entrymissing","Subledger detail entry is missing for account code: "+voucherDetails.getSubledgerCode())));
				}else
				{
					subledgerDetailRow.append( voucherDetails.getGlcode().getId().toString()).
					append(voucherDetails.getDetailType().getId().toString()).
					append(voucherDetails.getDetailKeyId().toString()).
					append(voucherDetails.getFunctionDetail());
				}
				if(null == subLedgerMap.get(subledgerDetailRow.toString())){
					subLedgerMap.put(subledgerDetailRow.toString(),subledgerDetailRow.toString());
				}// to check for  the same subledger should not allow for same gl code
				else{ 
					throw new ValidationException(Arrays.asList(new ValidationError("journalvoucher.samesubledger.repeated","Same subledger should not allow for same account code")));
				}
				
			}
		}
		if(subLegAccMap.size() > 0){
			for (Map<String,Object> map : subLegAccMap) {
				 String glcodeIdAndFuncId = map.get("glcodeId-funcId").toString();
				if(null == subledAmtmap.get(glcodeIdAndFuncId) ){
					String functionId = glcodeIdAndFuncId.split("-")[1];
					if(functionId.equalsIgnoreCase("0")){
						throw new ValidationException(Arrays.asList(new ValidationError("journalvoucher.subledger.entrymissing",
								"Subledger detail entry is missing for account code:"+map.get("glcode").toString())));
						
					}else{
						CFunction function = (CFunction)persistenceService.find("from CFunction where id=?",Long.valueOf(functionId));
						throw new ValidationException(Arrays.asList(new ValidationError("journalvoucher.subledger.entrymissingFunc",
								"Subledger detail entry is missing for account code:"+map.get("glcode").toString()+" and function : "+function.getName())));
					}
				}else if(((BigDecimal) subledAmtmap.get(glcodeIdAndFuncId)).compareTo(new BigDecimal(map.get("amount").toString()))!=0){
					String functionId = glcodeIdAndFuncId.split("-")[1];
					if(functionId.equalsIgnoreCase("0")){
						throw new ValidationException(Arrays.asList(new ValidationError("journalvoucher.subledger.amtnotmatchinng",
								"Total subledger amount is not matching for account code :"+map.get("glcode").toString())));
						
					}else{
						CFunction function = (CFunction)persistenceService.find("from CFunction where id=?",Long.valueOf(functionId));
						throw new ValidationException(Arrays.asList(new ValidationError("journalvoucher.subledger.amtnotmatchinngFunc",
								"Total subledger amount is not matching for account code :"+map.get("glcode").toString() + " and function :"+ function.getName())));
					}
				}
			}
		}
		
	}
	
	public List getBillSubTypes() {
		return persistenceService.findAllBy("from EgBillSubType where expenditureType=? order by name",FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT );
	}
	public Position getPosition()throws EGOVRuntimeException
	{
		return  eisCommonsService.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
	}
	public void setGenericDao(GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public void setScriptExecutionService(ScriptService scriptExecutionService) {
		this.scriptExecutionService = scriptExecutionService;
	}

	public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}

	public void setVoucherService(VoucherService voucherService) {
		this.voucherService = voucherService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public void setEisCommonsService(EisCommonsService eisCommonsService) {
		this.eisCommonsService = eisCommonsService;
	}

	

}
