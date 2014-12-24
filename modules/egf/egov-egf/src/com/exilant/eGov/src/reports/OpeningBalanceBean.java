
/*
 * Created on June 20, 2006
 * @author Tilak
 */
package com.exilant.eGov.src.reports;
import java.util.*;
public class OpeningBalanceBean
{

	String fund;
	String accCode;
	String accName;
	String debit;
	String credit;
	String description;

	public String getFund() {
		return fund;
	}
	public void setFund(String fund) {
		this.fund = fund;
	}
	public void setAccCode(String accCode) {
		this.accCode = accCode;
	}
	public String getAccCode() {
		return accCode;
	}
	public void setAccName(String accName) {
			this.accName = accName;
	}
	public String getAccName() {
		return accName;
	}
	public String getDebit() {
			return debit;
	}
	public void setDebit(String dr) {
			this.debit = dr;
	}
	public String getCredit() {
				return credit;
	}
	public void setCredit(String cr) {
				this.credit = cr;
	}


	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}



