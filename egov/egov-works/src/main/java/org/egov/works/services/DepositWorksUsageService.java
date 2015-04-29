package org.egov.works.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.Fund;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.DepositWorksUsage;
import org.egov.works.models.estimate.FinancialDetail;




/**
 * This class will have all business logic related to DepositWorksUsage.
 *
 */
public interface DepositWorksUsageService extends BaseService<DepositWorksUsage,Long> {

	public Map<String,List>  getDepositFolioDetails(AbstractEstimate abstractEstimate,Fund fund,CChartOfAccounts coa,Accountdetailtype accountdetailtype,Long depositCode,Date appropriationDate);
	public BigDecimal getTotalDepositWorksAmount(Fund fund,CChartOfAccounts coa,Accountdetailtype accountdetailtype,Long depositCode,Date appropriationDate); 
	public BigDecimal getTotalUtilizedAmountForDepositWorks(FinancialDetail financialDetail);
	/*
	 * This API returns utilized amount upto appDate accross financial year
	 * @param financailDeatil
	 * @param appDate
	 */
	public BigDecimal getTotalUtilizedAmountForDepositWorks(FinancialDetail financialDetail,Date appDate);
    public DepositWorksUsage getDepositWorksUsage(AbstractEstimate estimate,String appropriationNumber);
}