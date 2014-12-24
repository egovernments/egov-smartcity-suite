package org.egov.works.services;

import java.math.BigDecimal;

import org.egov.commons.CFinancialYear;
import org.egov.works.models.rateContract.Indent;
import org.egov.infstr.ValidationException;

public interface IndentRateContractService extends BaseService<Indent,Long>{
	
	public void setIndentRateContractNumber(Indent entity,CFinancialYear finYear);
	/**
	 * 
	 * @param indent
	 * @return true if budget consumed otherwise false
	 * @throws ValidationException
	 */
	
	public boolean checkForBudgetaryAppropriation(Indent indent) 
	throws ValidationException;
	
	/**
	 * 
	 * @param indent
	 * @return budget available 
	 * @throws ValidationException
	 */
	
	public BigDecimal getBudgetAvailable(Indent indent) 
	throws ValidationException;
	/**
	 * This method invokes the script service to generate the budget appropriation number.
	 * This method is invoked from the work flow rules.
	 * 
	 * @param entity an instance of <code>Indent</code>
	 * 
	 * @return a <code>String<code> containing the generated the budget 
	 * appropriation number.
	 */

	public String getBudgetAppropriationNumber(Indent entity);
}
