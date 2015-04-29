package org.egov.works.services.impl;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.Fund;
import org.egov.commons.service.CommonsService;
import org.egov.egf.commons.EgovCommon;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.BudgetFolioDetail;
import org.egov.works.models.estimate.DepositWorksUsage;
import org.egov.works.models.estimate.FinancialDetail;
import org.egov.works.services.ContractorBillService;
import org.egov.works.services.DepositWorksUsageService;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * This class will expose all Contractor Bill related operations.
 * @author Sathish P
 *
 */
public class DepositWorksUsageServiceImpl extends BaseServiceImpl<DepositWorksUsage, Long>
										implements DepositWorksUsageService{
	private static final Logger LOGGER = Logger.getLogger(DepositWorksUsageServiceImpl.class);
	private EgovCommon egovCommon;
	private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.US);
	@Autowired
        private CommonsService commonsService;
	public static final String dateFormat="dd-MMM-yyyy";
	private ContractorBillService contractorBillService;
	
	public DepositWorksUsageServiceImpl(
			PersistenceService<DepositWorksUsage, Long> persistenceService) {
		super(persistenceService);
		// TODO Auto-generated constructor stub
	}
	public BigDecimal getTotalDepositWorksAmount(Fund fund,CChartOfAccounts coa,Accountdetailtype accountdetailtype,Long depositCode,Date appropriationDate){
		
		return egovCommon.getDepositAmountForDepositCode(appropriationDate, coa.getGlcode(), fund.getCode(), accountdetailtype.getId(), depositCode.intValue());
	}
	
	public Map<String,List>  getDepositFolioDetails(AbstractEstimate abstractEstimate,Fund fund,CChartOfAccounts coa,Accountdetailtype accountdetailtype,Long depositCode,Date appropriationDate){
		List<BudgetFolioDetail> approvedBudgetFolioResultList=new ArrayList<BudgetFolioDetail>();
		List<Object> paramList = new ArrayList<Object>();
		 Object[] params;
		 paramList.add(appropriationDate);
		 paramList.add(fund.getId());
		 paramList.add(depositCode);
		 paramList.add(coa.getId());
		 params 			= new Object[paramList.size()];
		 params 			= paramList.toArray(params);
		 
		 //Getting deposit works usage list across the financial year
		 List<DepositWorksUsage> depositWorksUsageList=(List<DepositWorksUsage>) persistenceService.findAllBy("from DepositWorksUsage dwu where trunc(dwu.appropriationDate)<=trunc(?) and  dwu.depositCode.fund.id=?  and  dwu.depositCode.id=?  and  dwu.coa.id=?   order by dwu.id asc",params);
		 
		 //List<DepositWorksUsage> depositWorksUsageList=(List<DepositWorksUsage>) persistenceService.findAllBy("from DepositWorksUsage dwu where  dwu.financialYearId=? and to_date(to_char(dwu.appropriationDate,'DD-Mon-YYYY'),'DD-Mon-YYYY') <=to_date(?,'DD-Mon-YYYY') and dwu.abstractEstimate.id in (select fd.abstractEstimate.id from FinancialDetail fd where fd.fund.id=? and fd.abstractEstimate.depositCode.id=? and fd.coa.id=?) order by dwu.id asc",params);

		 if(depositWorksUsageList != null && !depositWorksUsageList.isEmpty()){
			return addApprovedEstimateResultList(approvedBudgetFolioResultList,depositWorksUsageList,appropriationDate);
		}
		return new HashMap<String,List>();
	}
	
	public Map<String,List> addApprovedEstimateResultList(List<BudgetFolioDetail> depositFolioResultList, List<DepositWorksUsage> depositWorksUsageList,Date appropriationDate){
		int srlNo=1;
		Double cumulativeTotal = 0.00D;
		BigDecimal totalDeposit  = BigDecimal.ZERO;
		double cumulativeExpensesIncurred=0.0;
		Map<String,List>  budgetFolioMap=new HashMap<String, List>();
		for(DepositWorksUsage depositWorksUsage : depositWorksUsageList){
			BudgetFolioDetail budgetFolioDetail=new BudgetFolioDetail();
			budgetFolioDetail.setSrlNo(srlNo++);
			
			if(depositWorksUsage.getAbstractEstimate() != null){	
				 budgetFolioDetail.setEstimateNo(depositWorksUsage.getAbstractEstimate().getEstimateNumber());
				 budgetFolioDetail.setNameOfWork(depositWorksUsage.getAbstractEstimate().getName());
				 budgetFolioDetail.setEstimateDate(sdf.format(depositWorksUsage.getAbstractEstimate().getEstimateDate()));
				 if(!isAppropriationRejected(depositWorksUsage.getAppropriationNumber())){
					 budgetFolioDetail.setExpensesIncurred(contractorBillService.getTotalActualExpenseForProject(depositWorksUsage.getAbstractEstimate(),appropriationDate));
					 budgetFolioDetail.setCumulativeExpensesIncurred(cumulativeExpensesIncurred);
					 budgetFolioDetail.setActualBalanceAvailable(depositWorksUsage.getTotalDepositAmount().doubleValue()-cumulativeExpensesIncurred);
					 cumulativeExpensesIncurred=cumulativeExpensesIncurred+budgetFolioDetail.getExpensesIncurred().doubleValue();
				 }
				 else{ 
					 budgetFolioDetail.setExpensesIncurred(0.0);
					 budgetFolioDetail.setCumulativeExpensesIncurred(cumulativeExpensesIncurred);
					 budgetFolioDetail.setActualBalanceAvailable(depositWorksUsage.getTotalDepositAmount().doubleValue()-cumulativeExpensesIncurred);
					 cumulativeExpensesIncurred=cumulativeExpensesIncurred+budgetFolioDetail.getExpensesIncurred().doubleValue();
				 }
			} 
			
			budgetFolioDetail.setBudgetApprNo(depositWorksUsage.getAppropriationNumber());
			budgetFolioDetail.setCumulativeTotal(cumulativeTotal);
			budgetFolioDetail.setAppDate(sdf.format(depositWorksUsage.getAppropriationDate()));
			//Note: commented because re appropriation type is not required for deposit works
			//budgetFolioDetail.setAppType(getAppropriationType(depositWorksUsage.getId()));
		    if(depositWorksUsage.getReleasedAmount().compareTo(BigDecimal.ZERO)>0){
				cumulativeTotal = cumulativeTotal - depositWorksUsage.getReleasedAmount().doubleValue(); 
				budgetFolioDetail.setWorkValue(depositWorksUsage.getAbstractEstimate().getTotalAmount().getValue()*(-1));//to display released amount as negative
				budgetFolioDetail.setAppropriatedValue((BigDecimal.ZERO.subtract(depositWorksUsage.getReleasedAmount())).doubleValue());
		    }
			else{
				cumulativeTotal = cumulativeTotal + depositWorksUsage.getConsumedAmount().doubleValue();
				budgetFolioDetail.setWorkValue(depositWorksUsage.getAbstractEstimate().getTotalAmount().getValue());
				budgetFolioDetail.setAppropriatedValue((depositWorksUsage.getConsumedAmount()).doubleValue());
			}
			totalDeposit=depositWorksUsage.getTotalDepositAmount();
			
			budgetFolioDetail.setBalanceAvailable(totalDeposit);
			
			depositFolioResultList.add(budgetFolioDetail);
		}
		List calculatedValuesList = new ArrayList();
		calculatedValuesList.add(cumulativeTotal);
		budgetFolioMap.put("depositFolioList", depositFolioResultList);
		budgetFolioMap.put("calculatedValues",calculatedValuesList);
		budgetFolioMap.put("totalCumulativeExpensesIncurred",Arrays.asList(cumulativeExpensesIncurred));
		return budgetFolioMap;
	 }
	
//Note:commented because appropriation type is not required for depositworks
/*	public String getAppropriationType(Long depositWorkUsageId){
		String appType="Regular";
		if(genericService!=null){
			List allReadyAppropriatedBudgetUsageList=genericService.findAllBy("from AbstractEstimateAppropriation where abstractEstimate.id=(select max(abstractEstimate.id) from AbstractEstimateAppropriation where depositWorksUsage.id=?) and depositWorksUsage.id<?",depositWorkUsageId,depositWorkUsageId);
			if(allReadyAppropriatedBudgetUsageList.size()!=0){
				appType="Re-Appropriation";
			}
		}
		return appType;
	}
*/
	public BigDecimal getTotalUtilizedAmountForDepositWorks(FinancialDetail financialDetail) {
		return (BigDecimal)genericService.findByNamedQuery("getDepositWorksUsageAmount", financialDetail.getAbstractEstimate().getDepositCode().getId(), financialDetail.getFund().getId(),financialDetail.getCoa().getId());
	}

	public BigDecimal getTotalUtilizedAmountForDepositWorks(FinancialDetail financialDetail,Date appDate) {
		BigDecimal totalUtilizedAmount=BigDecimal.ZERO;
		//NOTE: utilizedAmountForRunningProject holds sum of all appropriation amount for estimates which are not closed 
		BigDecimal utilizedAmountForRunningProject= (BigDecimal) genericService.find("select sum(dwu.consumedAmount-dwu.releasedAmount) from DepositWorksUsage dwu where dwu.createdDate<=? and EXISTS (select 'true' from FinancialDetail fd where fd.abstractEstimate.id=dwu.abstractEstimate.id and fd.fund.id=? and fd.abstractEstimate.depositCode.id=? and fd.coa.id=?) " +
				"and (dwu.abstractEstimate.projectCode.id is null or dwu.abstractEstimate.projectCode.id not in (select proj.id from ProjectCode proj where proj.egwStatus.code='CLOSED'))", appDate,financialDetail.getFund().getId(),financialDetail.getAbstractEstimate().getDepositCode().getId(),financialDetail.getCoa().getId());
		if(utilizedAmountForRunningProject==null){
			utilizedAmountForRunningProject=BigDecimal.ZERO;
		}
		LOGGER.debug("Total Utilized amount for deposit works (Running projects) >>>>Depositcodeid="+financialDetail.getAbstractEstimate().getDepositCode().getId()+"|| till date="+appDate+"||utilizedAmount="+utilizedAmountForRunningProject);
		//NOTE: utilizedAmountForClosedProject holds sum of all appropriation amount for estimates which are closed 
		Double utilizedAmountForClosedProject= (Double) genericService.find("select sum(fd.abstractEstimate.projectCode.projectValue) from FinancialDetail fd where trunc(fd.abstractEstimate.projectCode.completionDate)<=trunc(?) and fd.fund.id=? and fd.abstractEstimate.depositCode.id=? and fd.coa.id=? and fd.abstractEstimate.projectCode.egwStatus.code='CLOSED'", appDate,financialDetail.getFund().getId(),financialDetail.getAbstractEstimate().getDepositCode().getId(),financialDetail.getCoa().getId());
		if(utilizedAmountForClosedProject==null){
			utilizedAmountForClosedProject=0.0;
		}
		totalUtilizedAmount=utilizedAmountForRunningProject.add(new BigDecimal(utilizedAmountForClosedProject.doubleValue()));
		LOGGER.debug("Total Utilized amount for deposit works (Closed projects) >>>>Depositcodeid="+financialDetail.getAbstractEstimate().getDepositCode().getId()+"|| till date="+appDate+"||utilizedAmount="+utilizedAmountForClosedProject);
		LOGGER.debug("Total Utilized amount for deposit works (including Closed and running projects) >>>>Depositcodeid="+financialDetail.getAbstractEstimate().getDepositCode().getId()+"|| till date="+appDate+"||totalutilizedAmount="+totalUtilizedAmount);
		return totalUtilizedAmount;
	}

	public void setEgovCommon(EgovCommon egovCommon) {
		this.egovCommon = egovCommon;
	}
	
	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}
	
	public DepositWorksUsage getDepositWorksUsage(AbstractEstimate estimate,String appropriationNumber){
		DepositWorksUsage depositWorksUsage=(DepositWorksUsage) persistenceService.find("from DepositWorksUsage dwu where dwu.abstractEstimate=? and dwu.appropriationNumber=?", estimate,appropriationNumber);
	    return depositWorksUsage;
	}
	public void setContractorBillService(ContractorBillService contractorBillService) {
		this.contractorBillService = contractorBillService;
	}

	private boolean isAppropriationRejected(String apprNumber){
		
		if(apprNumber==null){
			throw new EGOVRuntimeException("Invalid parameter passed to isAppropriationRejected() ||apprNumber="+apprNumber);
		}
		String [] str=apprNumber.split("/");
		if(str.length>0 && "BC".equalsIgnoreCase(str[0])){
			return true;
		}
		
		String rejectedApprNumber="BC/"+apprNumber;

		DepositWorksUsage depositWorksUsage=(DepositWorksUsage) persistenceService.find("from DepositWorksUsage dwu where dwu.appropriationNumber=?", rejectedApprNumber);
		if(depositWorksUsage!=null){
			return true;
		}
		
		return false;
	}
}
