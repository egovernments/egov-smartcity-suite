/**
 * 
 */
package org.egov.model.cheque;

import java.io.Serializable;

import org.egov.infra.admin.master.entity.Department;

/**
 * @author manoranjan
 *
 */
public class ChequeDeptMapping implements Serializable {


	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private AccountCheques accountCheque;
	
	private Department allotedTo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AccountCheques getAccountCheque() {
		return accountCheque;
	}

	public void setAccountCheque(AccountCheques accountCheque) {
		this.accountCheque = accountCheque;
	}

	public Department getAllotedTo() {
		return allotedTo;
	}

	public void setAllotedTo(Department allotedTo) {
		this.allotedTo = allotedTo;
	}
	
	

}
