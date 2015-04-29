/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
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
