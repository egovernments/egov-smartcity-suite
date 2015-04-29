package org.egov.works.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.service.CommonsService;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.model.budget.BudgetGroup;
import org.springframework.beans.factory.annotation.Autowired;

public class WorkProgressAbstractReportService {
	private static final Logger logger = Logger.getLogger(WorkProgressAbstractReportService.class); 
	@Autowired
        private CommonsService commonsService;	
	private BudgetDetailsDAO budgetDetailsDAO;
		
	public void setBudgetHeadsFromString(String budgetHeadsStr,List<String> budgetHeads,List<Long> budgetHeadIds)
	{
		List<BudgetGroup> budgetHeadList = new ArrayList<BudgetGroup>();
		List<CChartOfAccounts> coaList = new ArrayList<CChartOfAccounts>();
		if(StringUtils.isNotBlank(budgetHeadsStr))
		{
			String[] budgetHeadsFromString = budgetHeadsStr.split(",");
			for(int i=0;i<budgetHeadsFromString.length;i++)
			{
				// Split and obtain only the glcode
				coaList.addAll(commonsService.getListOfDetailCode(budgetHeadsFromString[i].split("-")[0]));
			}
			budgetHeadList.addAll( budgetDetailsDAO.getBudgetHeadForGlcodeList(coaList));
			List<Long> budgetHeadIdsLong= new ArrayList<Long>();
			List<String> budgetHeadIdStr= new ArrayList<String>();
			if(budgetHeadList!=null && budgetHeadList.size()>0)
			{
				for(BudgetGroup bdgtGrp:budgetHeadList)
				{
					budgetHeadIdStr.add(bdgtGrp.getId().toString());
					budgetHeadIdsLong.add(bdgtGrp.getId());
				}
			}
			budgetHeads.addAll(budgetHeadIdStr);
			budgetHeadIds.addAll(budgetHeadIdsLong);
		}
	}
	
	public void setBudgetHeadsFromIdString(String budgetHeadsStr,List<String> budgetHeads,List<Long> budgetHeadIds)
	{		
		if(StringUtils.isNotBlank(budgetHeadsStr))
		{
			String[] budgetHeadIdsFromString = budgetHeadsStr.split(",");
			for(int i=0;i<budgetHeadIdsFromString.length;i++)
			{
				budgetHeads.add(budgetHeadIdsFromString[i]);
				budgetHeadIds.add(Long.parseLong(budgetHeadIdsFromString[i]));
			}
		}
	}
	
	public void setDepositCodesFromString(String depositCodesStr,List<Long> depositCodeIds)
	{
		List<CChartOfAccounts> coaList = new ArrayList<CChartOfAccounts>();
		if(StringUtils.isNotBlank(depositCodesStr))
		{
			String[] depositCodesFromStr = depositCodesStr.split(",");
			for(int i=0;i<depositCodesFromStr.length;i++)
				coaList.addAll(commonsService.getListOfDetailCode(depositCodesFromStr[i].split("-")[0]));
			List<Long> depositCodeIdsLong= new ArrayList<Long>();
			if(coaList!=null && coaList.size()>0)
			{
				for(CChartOfAccounts coa:coaList)
					depositCodeIdsLong.add(coa.getId());
			}
			depositCodeIds.addAll(depositCodeIdsLong);
		}
	}
	
	public void setDepositCodesFromIdString(String depositCodesStr,List<Long> depositCodeIds)
	{
		List<CChartOfAccounts> coaList = new ArrayList<CChartOfAccounts>();
		if(StringUtils.isNotBlank(depositCodesStr))
		{
			String[] depositCodesFromStr = depositCodesStr.split(",");
			for(int i=0;i<depositCodesFromStr.length;i++)
			{
				depositCodeIds.add(Long.parseLong(depositCodesFromStr[i]));
			}
		}
	}
	
	/**
         * Converting given amount to show in Crores with no of decimal points to be rounded off
         * @param amount
         * @param decimalPoints
         * @return 
         */
        public BigDecimal getRoundedOfAmount(Object amount, int decimalPoints) {
                int dividingFactor = 10000000; // 1 Crore
                if(amount!=null) {
                        BigDecimal divisor = new BigDecimal(dividingFactor);
                        BigDecimal amountBD  =  (BigDecimal) amount;
                        BigDecimal result = amountBD.divide(divisor);
                        return result.setScale(decimalPoints, RoundingMode.HALF_UP);
                }
                else
                        return null;
        }
        
	public CommonsService getCommonsService() {
		return commonsService;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public void setBudgetDetailsDAO(BudgetDetailsDAO budgetDetailsDAO) {
		this.budgetDetailsDAO = budgetDetailsDAO;
	}


}
