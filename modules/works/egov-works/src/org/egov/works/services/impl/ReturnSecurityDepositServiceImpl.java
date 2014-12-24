package org.egov.works.services.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.script.ScriptContext;

import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.commons.service.CommonsService;
import org.egov.egf.bills.model.Cbill;
import org.egov.egf.commons.EgovCommon;
import org.egov.exceptions.EGOVException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.utils.SequenceGenerator;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregister;
import org.egov.model.bills.EgBillregistermis;
import org.egov.works.models.estimate.FinancialDetail;
import org.egov.works.models.securityDeposit.ReturnSecurityDeposit;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.services.ReturnSecurityDepositService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;
import org.hibernate.internal.SessionImpl;

public class ReturnSecurityDepositServiceImpl extends BaseServiceImpl<ReturnSecurityDeposit,Long> implements ReturnSecurityDepositService{
	private WorksService worksService;
	private static final String EXPENSE_CODE_PURPOSE ="Contigency Bill Code";
	private static final String WORKS_SECURITY_DEPOSIT_PURPOSE ="Security Deposit";
	private static final String ACCOUNTDETAIL_TYPE_CONTRACTOR = "contractor";
		
	private EgovCommon egovCommon;
	private CommonsService commonsService;
	private SequenceGenerator sequenceGenerator;
	private ScriptService scriptExecutionService;
	public ReturnSecurityDepositServiceImpl(
			PersistenceService<ReturnSecurityDeposit, Long> persistenceService) {
		super(persistenceService);
		// TODO Auto-generated constructor stub
	}

	/**
	 * This method will get the total security deposit amount deducted from all approved bills for that workorder and glcodeId
	 * @param wo
	 * @param glcodeId
	 * @return
	 */
	public double getTotalSDAmountDeducted(WorkOrder workorder , Long glcodeId,boolean withRefund){
		double totalSDAmountDeducted=0;
		try{
		List<EgBillregister> egBillregisterList = genericService
				.findAllBy(
						"select distinct mbBills.egBillregister from MBHeader mbh left join mbh.mbBills mbBills where mbh.workOrder.id=? and mbBills.egBillregister.billstatus='APPROVED' ",
						workorder.getId());
		for(EgBillregister bill:egBillregisterList){
			for(EgBilldetails billDetails:bill.getEgBilldetailes()){
				if(glcodeId==billDetails.getGlcodeid().longValue()){
					totalSDAmountDeducted=totalSDAmountDeducted+billDetails.getCreditamount().doubleValue();
				}	
			}
		}
		if(withRefund)
			totalSDAmountDeducted=totalSDAmountDeducted-workorder.getSdRefunded();
		}catch(Exception e){
			
		}
		return totalSDAmountDeducted;
	}
	
	public EgBillregister createExpenseBill(ReturnSecurityDeposit returnSecurityDeposit) throws Exception{
		Cbill egBillRegister=new Cbill();
		egBillRegister.setExpendituretype(WorksConstants.EXPENDITURETYPE_CONTINGENT);
		egBillRegister.setBilltype(worksService.getWorksConfigValue(WorksConstants.BILL_TYPE_FINALBILL));
				
		Connection con = ((SessionImpl)HibernateUtil.getCurrentSession()).connection();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
		String txndate = commonsService.getCurrentDate(con);		
		GregorianCalendar gregorianCalender = new GregorianCalendar();
		gregorianCalender.setGregorianChange(formatter.parse(txndate));	
		egBillRegister.setBilldate(formatter.parse(txndate));
		
		CFinancialYear financialYear = commonsService.getFinancialYearByDate(egBillRegister.getBilldate());
		
		if(financialYear == null) {
			throw new ValidationException(Arrays.asList(new ValidationError("no.financial.year","No Financial Year for bill date"+formatter.format(egBillRegister.getBilldate()))));
		}
		
		String year=financialYear.getFinYearRange();
		
		egBillRegister.setBillstatus("APPROVED");
		
		egBillRegister.setCreatedBy(returnSecurityDeposit.getCreatedBy());
		egBillRegister.setCreatedDate(formatter.parse(txndate));
		egBillRegister.setModifiedBy(returnSecurityDeposit.getCreatedBy());
		egBillRegister.setModifiedDate(formatter.parse(txndate));
		
		
		EgwStatus status = commonsService.getStatusByModuleAndCode("CBILL", "APPROVED");			 
		egBillRegister.setStatus(status);
		egBillRegister.setBillamount(BigDecimal.valueOf(returnSecurityDeposit.getReturnSecurityDepositAmount()));	
		egBillRegister.setPassedamount(BigDecimal.valueOf(returnSecurityDeposit.getReturnSecurityDepositAmount()));
		egBillRegister.setWorkorderdate(returnSecurityDeposit.getWorkOrder().getWorkOrderDate());
		egBillRegister.setWorkordernumber(returnSecurityDeposit.getWorkOrder().getWorkOrderNumber());
		
		EgBillregistermis egBillregisterMis=new EgBillregistermis();
		egBillregisterMis.setEgBillregister(egBillRegister);			
		egBillregisterMis.setMonth(new BigDecimal(gregorianCalender.get(Calendar.MONTH)));	
		egBillregisterMis.setEgDepartment((DepartmentImpl)returnSecurityDeposit.getWorkOrder().getWorkOrderEstimates().get(0).getEstimate().getExecutingDepartment());
		egBillregisterMis.setPayto(returnSecurityDeposit.getWorkOrder().getContractor().getName());
		egBillregisterMis.setFieldid((BoundaryImpl)returnSecurityDeposit.getWorkOrder().getWorkOrderEstimates().get(0).getEstimate().getWard());
		List<FinancialDetail> fdList=returnSecurityDeposit.getWorkOrder().getWorkOrderEstimates().get(0).getEstimate().getFinancialDetails();
		if(fdList!=null && !fdList.isEmpty()){
			if(fdList.get(0).getFund()!=null)
				egBillregisterMis.setFund(fdList.get(0).getFund());
			if(fdList.get(0).getFunctionary()!=null)
				egBillregisterMis.setFunctionaryid(fdList.get(0).getFunctionary());
			if(fdList.get(0).getScheme()!=null)
				egBillregisterMis.setScheme(fdList.get(0).getScheme());
			if(fdList.get(0).getSubScheme()!=null)
				egBillregisterMis.setSubScheme(fdList.get(0).getSubScheme());
			if(!fdList.get(0).getFinancingSources().isEmpty())
				egBillregisterMis.setFundsource(fdList.get(0).getFinancingSources().get(0).getFundSource());
		}
		egBillregisterMis.setFinancialyear(financialYear);
		egBillregisterMis.setEgBillregister(egBillRegister); 
		egBillRegister.setEgBillregistermis(egBillregisterMis);
		
		Script billNumberScript = (Script)genericService.findAllByNamedQuery(Script.BY_NAME, "egf.bill.number.generator").get(0);
		ScriptContext scriptContext = ScriptService.createContext("sequenceGenerator",sequenceGenerator,"sItem",egBillRegister,"year",year);
		String billNumber = (String)scriptExecutionService.executeScript(billNumberScript.getName(), scriptContext);
		if(billNumber==null)  
		{
			throw new ValidationException(Arrays.asList(new ValidationError("unable.to.generate.bill.number","No Financial Year for bill date"+formatter.format(egBillRegister.getBilldate()))));
		}
		egBillRegister.setBillnumber(billNumber);
		
		EgBilldetails egBilldetails = new EgBilldetails();
		egBilldetails.setDebitamount(BigDecimal.valueOf(returnSecurityDeposit.getReturnSecurityDepositAmount()));
		egBilldetails.setCreditamount(BigDecimal.ZERO);
		egBilldetails.setGlcodeid(BigDecimal.valueOf(returnSecurityDeposit.getGlcode().getId()));
		Accountdetailtype adt = null;
		List<Accountdetailtype> detailCode = commonsService.getAccountdetailtypeListByGLCode(returnSecurityDeposit.getGlcode().getGlcode());			
		if (detailCode!=null && !detailCode.isEmpty()) {			
			adt = commonsService.getAccountDetailTypeIdByName(returnSecurityDeposit.getGlcode().getGlcode(), ACCOUNTDETAIL_TYPE_CONTRACTOR);
			if(adt!=null){
				EgBillPayeedetails egBillPaydetail = new EgBillPayeedetails();				
				egBillPaydetail.setAccountDetailKeyId(returnSecurityDeposit.getContractor().getId().intValue());
				egBillPaydetail.setAccountDetailTypeId(adt.getId());
			    egBillPaydetail.setDebitAmount(BigDecimal.valueOf(returnSecurityDeposit.getReturnSecurityDepositAmount()));
				egBillPaydetail.setCreditAmount(BigDecimal.ZERO);
				egBillPaydetail.setEgBilldetailsId(egBilldetails);	
				egBilldetails.getEgBillPaydetailes().add(egBillPaydetail);
			}
			else{
				List<ValidationError> errors=new ArrayList<ValidationError>();
				errors.add(new ValidationError("returnSD.validate_glcode_for_subledger","The account code "+returnSecurityDeposit.getGlcode().getGlcode()+" does not have the required sub ledger details")); 
				throw new ValidationException(errors);
			}			
		}
		
		egBilldetails.setEgBillregister(egBillRegister);
		egBillRegister.addEgBilldetailes(egBilldetails);
		
		egBilldetails = new EgBilldetails();
		egBilldetails.setDebitamount(BigDecimal.ZERO);
		egBilldetails.setCreditamount(BigDecimal.valueOf(returnSecurityDeposit.getReturnSecurityDepositAmount()));
		egBilldetails.setGlcodeid(BigDecimal.valueOf(commonsService.getAccountCodeByPurpose(egovCommon.getAccountCodePurposeByName(EXPENSE_CODE_PURPOSE).getId().intValue()).get(0).getId()));					
		egBilldetails.setEgBillregister(egBillRegister);		
		egBillRegister.addEgBilldetailes(egBilldetails);
		return egBillRegister;
	}
	
	
	
	/**
	 * This method will return the list of all chartofaccounts linked with collection of security deposit amount
	 * @return
	 */
	public List<CChartOfAccounts> getSDCOAList() throws NumberFormatException, EGOVException{
		return commonsService.getAccountCodeByPurpose(egovCommon.getAccountCodePurposeByName(WORKS_SECURITY_DEPOSIT_PURPOSE).getId().intValue());
	}

	/*public String getConfigValueForSecurityDeposit(){
		List<AppConfigValues> configList = worksService.getAppConfigValue("EGF", WORKS_SECURITY_DEPOSIT_CODE);
		if(!configList.isEmpty())
			return configList.get(0).getValue(); 
		return null;
	}*/
	
	public void setCommonsService(CommonsService commonsService) { 
		this.commonsService = commonsService;
	}

	public String getFinalBillTypeConfigValue() {		
		return worksService.getWorksConfigValue("FinalBillType");
	}
	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}

	public void setScriptExecutionService(ScriptService scriptExecutionService) {
		this.scriptExecutionService = scriptExecutionService;
	}

	public SequenceGenerator getSequenceGenerator() {
		return sequenceGenerator;
	}

	public ScriptService getScriptExecutionService() {
		return scriptExecutionService;
	}

	public void setEgovCommon(EgovCommon egovCommon) {
		this.egovCommon = egovCommon;
	}
	
}