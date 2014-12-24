package org.egov.works.services.impl;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.Fund;
import org.egov.egf.commons.EgovCommon;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.BudgetFolioDetail;
import org.egov.works.models.estimate.DepositWorksUsage;
import org.egov.works.models.estimate.FinancialDetail;
import org.egov.works.services.DepositWorksUsageService;
import org.egov.works.utils.WorksConstants;


/**
 * This class will expose all Contractor Bill related operations.
 * @author Sathish P
 *
 */
public class DepositWorksUsageServiceImpl extends BaseServiceImpl<DepositWorksUsage, Long>
										implements DepositWorksUsageService{
	
	private EgovCommon egovCommon;
	private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.US);
	
	public DepositWorksUsageServiceImpl(
			PersistenceService<DepositWorksUsage, Long> persistenceService) {
		super(persistenceService);
		// TODO Auto-generated constructor stub
	}
	public BigDecimal getTotalDepositWorksAmount(Fund fund,CChartOfAccounts coa,Accountdetailtype accountdetailtype,Long depositCode,Date appropriationDate){
		
		return egovCommon.getCreditBalanceforDate(appropriationDate, coa.getGlcode(), fund.getCode(), accountdetailtype.getId(), depositCode.intValue());
	}
	
	public Map<String,List>  getDepositFolioDetails(AbstractEstimate abstractEstimate,Fund fund,CChartOfAccounts coa,Accountdetailtype accountdetailtype,Long depositCode,Date appropriationDate){
		List<BudgetFolioDetail> approvedBudgetFolioResultList=new ArrayList<BudgetFolioDetail>();
		List<Object> paramList = new ArrayList<Object>();
		 Object[] params;
		 paramList.add(depositCode);
		 paramList.add(fund.getId());
		 paramList.add(coa.getId());
		 paramList.add(appropriationDate);
		 params 			= new Object[paramList.size()];
		 params 			= paramList.toArray(params);
		 List<DepositWorksUsage> depositWorksUsageList=(List<DepositWorksUsage>)persistenceService.findAllByNamedQuery(WorksConstants.QUERY_GETDEPOSITWORKSUSAGELISTFORDEPOSITFOLIO, params);
			
		if(depositWorksUsageList != null && !depositWorksUsageList.isEmpty()){
			return addApprovedEstimateResultList(approvedBudgetFolioResultList,depositWorksUsageList);
		}
		return new HashMap<String,List>();
	}
	
	public Map<String,List> addApprovedEstimateResultList(List<BudgetFolioDetail> depositFolioResultList, List<DepositWorksUsage> depositWorksUsageList){
		int srlNo=1;
		Double cumulativeTotal = 0.00D;
		BigDecimal totalDeposit  = BigDecimal.ZERO;
		Map<String,List>  budgetFolioMap=new HashMap<String, List>();
		for(DepositWorksUsage depositWorksUsage : depositWorksUsageList){
			BudgetFolioDetail budgetFolioDetail=new BudgetFolioDetail();
			budgetFolioDetail.setSrlNo(srlNo++);
			
			if(depositWorksUsage.getAbstractEstimate() != null){	
				 budgetFolioDetail.setEstimateNo(depositWorksUsage.getAbstractEstimate().getEstimateNumber());
				 budgetFolioDetail.setNameOfWork(depositWorksUsage.getAbstractEstimate().getName());
				 budgetFolioDetail.setEstimateDate(sdf.format(depositWorksUsage.getAbstractEstimate().getEstimateDate()));	
			} 
			
			budgetFolioDetail.setBudgetApprNo(depositWorksUsage.getAppropriationNumber());
			budgetFolioDetail.setCumulativeTotal(cumulativeTotal);
		    if(depositWorksUsage.getReleasedAmount().compareTo(BigDecimal.ZERO)>0){
				cumulativeTotal = cumulativeTotal - depositWorksUsage.getReleasedAmount().doubleValue(); 
				budgetFolioDetail.setWorkValue(depositWorksUsage.getAbstractEstimate().getTotalAmount().getValue()*(-1));//to display released amount as negative
			}
			else{
				cumulativeTotal = cumulativeTotal + depositWorksUsage.getConsumedAmount().doubleValue();
				budgetFolioDetail.setWorkValue(depositWorksUsage.getAbstractEstimate().getTotalAmount().getValue());
			}
			totalDeposit=depositWorksUsage.getTotalDepositAmount();
			
			budgetFolioDetail.setBalanceAvailable(totalDeposit);
			
			depositFolioResultList.add(budgetFolioDetail);
		}
		List calculatedValuesList = new ArrayList();
		calculatedValuesList.add(cumulativeTotal);
		budgetFolioMap.put("depositFolioList", depositFolioResultList);
		budgetFolioMap.put("calculatedValues",calculatedValuesList);
		return budgetFolioMap;
	 }
	
	public BigDecimal getTotalUtilizedAmountForDepositWorks(FinancialDetail financialDetail) {
		return (BigDecimal)genericService.findByNamedQuery("getDepositWorksUsageAmount", financialDetail.getAbstractEstimate().getDepositCode().getId(), financialDetail.getFund().getId(),financialDetail.getCoa().getId());
	}

	public void setEgovCommon(EgovCommon egovCommon) {
		this.egovCommon = egovCommon;
	}
	
	public DepositWorksUsage getDepositWorksUsage(AbstractEstimate estimate,String appropriationNumber){
		DepositWorksUsage depositWorksUsage=(DepositWorksUsage) persistenceService.find("from DepositWorksUsage dwu where dwu.abstractEstimate=? and dwu.appropriationNumber=?", estimate,appropriationNumber);
	    return depositWorksUsage;
	}

}
