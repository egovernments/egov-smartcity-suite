
/*
 * Created on April 27, 2006
 * @author Tilak
 */
package com.exilant.eGov.src.reports;

import java.math.BigDecimal;

public class TransferBalance
{

	String code;
	Double balance;
	String fundId;
	public void setFundId(String fundId) {
		this.fundId = fundId;
	}
	public String getFundId() {
			return fundId;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCode() {
				return code;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public Double getBalance() {
		return balance;
	}



}



