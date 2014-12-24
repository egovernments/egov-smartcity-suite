/**
 * 
 */
package org.egov.model.cheque;

import java.io.Serializable;

import org.egov.lib.rjbac.dept.DepartmentImpl;

/**
 * @author manoranjan
 *
 */
public class ChequeDeptMapping implements Serializable {


	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private AccountCheques accountCheque;
	
	private DepartmentImpl allotedTo;

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

	public DepartmentImpl getAllotedTo() {
		return allotedTo;
	}

	public void setAllotedTo(DepartmentImpl allotedTo) {
		this.allotedTo = allotedTo;
	}
	
	

}
