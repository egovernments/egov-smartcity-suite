/*
 * Created on Jan 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.transactions;

/**
 * @author vijaykumar.b
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PreDefinedAccCodes {
	
	private String bankCharge="247100";

	public void setBankChargeCode(String aBankCharge){
   	bankCharge=aBankCharge;
   }
   public String getBankChargeCode(){
   	return bankCharge;
   }
}
