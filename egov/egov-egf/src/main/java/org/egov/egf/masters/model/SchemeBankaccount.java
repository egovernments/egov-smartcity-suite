/**
 * 
 */
package org.egov.egf.masters.model;

import org.apache.log4j.Logger;
import org.egov.commons.Bankaccount;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.infstr.models.BaseModel;

/**
 * @author mani
 */
public class SchemeBankaccount extends BaseModel {
	private static final long	serialVersionUID	= -8256178953783440029L;
	final static Logger			LOGGER				= Logger.getLogger(LoanGrantReceiptDetail.class);
	private Scheme				scheme;
	private SubScheme			subScheme;
	private Bankaccount			bankAccount;
	
	public Scheme getScheme() {
		return scheme;
	}
	
	public void setScheme(Scheme scheme) {
		this.scheme = scheme;
	}
	
	public SubScheme getSubScheme() {
		return subScheme;
	}
	
	public void setSubScheme(SubScheme subScheme) {
		this.subScheme = subScheme;
	}
	
	public Bankaccount getBankAccount() {
		return bankAccount;
	}
	
	public void setBankAccount(Bankaccount bankAccount) {
		this.bankAccount = bankAccount;
	}
	
}
