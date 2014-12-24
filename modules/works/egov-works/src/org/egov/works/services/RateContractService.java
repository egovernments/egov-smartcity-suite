package org.egov.works.services;

import org.egov.commons.CFinancialYear;
import org.egov.tender.model.TenderResponseLine;
import org.egov.works.models.rateContract.Indent;
import org.egov.works.models.rateContract.IndentDetail;
import org.egov.works.models.rateContract.RateContract;
import org.egov.works.models.rateContract.RateContractDetail;

public interface RateContractService extends BaseService<RateContract, Long>{
	/**
	 * Method to Generate Rate Contract Number 
	 * @param entity
	 * @param finYear
	 * @param deptId
	 * @param zone
	 */
	public void setRateContractNumber(RateContract entity,CFinancialYear finYear);
	
	/**
	 * Method to het IndentDetail for Accepted Tenders
	 * @param rcDetail
	 * @param indent
	 * @return
	 */
	public IndentDetail getActivityFromRateContractDetailAndIndent(RateContractDetail rcDetail, Indent indent);

}
