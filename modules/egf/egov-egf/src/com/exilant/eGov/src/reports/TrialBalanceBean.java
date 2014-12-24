
/*
 * Created on April 24, 2006
 * @author Tilak
 */
package com.exilant.eGov.src.reports;
public class TrialBalanceBean
{

	String accCode;
	String accName;
	String debit;
	String credit;    
    String fundId;
    String openingBal;
    String closingBal;
    String serialNo;
    
    public void setFundId(String fundId) {
        this.fundId = fundId;
    }
    public String getFundId() {
            return fundId;
    }
    
    public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getSerialNo() {
		return serialNo;
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
public String getOpeningBal() {
	return openingBal;
}
public void setOpeningBal(String openingBal) {
	this.openingBal = openingBal;
}
public String getClosingBal() {
	return closingBal;
}
public void setClosingBal(String closingBal) {
	this.closingBal = closingBal;
}

}



