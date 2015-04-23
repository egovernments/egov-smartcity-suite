/*
 * Created on April 24, 2006
 * @author Tilak
 */
package com.exilant.eGov.src.reports;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrialBalanceBean {

	String accCode;
	String accName;
	String debit;
	String credit;
	String fundId;
	String openingBal;
	String closingBal;
	String serialNo;

	Map<String,String> fundWiseMap;
	String fromAmountMap;
	BigDecimal closingBalance;
	BigDecimal creditOPB;
	BigDecimal debitOPB;
	BigDecimal tillDateCreditOPB;
	BigDecimal tillDateDebitOPB;
	BigDecimal debitAmount;
	BigDecimal creditAmount;
	BigDecimal amount;
	String amount1;
	String amount2;
	String amount3;
	String amount4;
	String amount5;
	String amount6;
	String amount7;
	String amount8;
	String amount9;
	String amount10;
	
	public String getAmount1() {
		return amount1;
	}

	public String getAmount2() {
		return amount2;
	}

	public String getAmount3() {
		return amount3;
	}

	public String getAmount4() {
		return amount4;
	}

	public String getAmount5() {
		return amount5;
	}

	public String getAmount6() {
		return amount6;
	}

	public String getAmount7() {
		return amount7;
	}

	public String getAmount8() {
		return amount8;
	}

	public String getAmount9() {
		return amount9;
	}

	public String getAmount10() {
		return amount10;
	}

	public void setAmount1(String amount1) {
		this.amount1 = amount1;
	}

	public void setAmount2(String amount2) {
		this.amount2 = amount2;
	}

	public void setAmount3(String amount3) {
		this.amount3 = amount3;
	}

	public void setAmount4(String amount4) {
		this.amount4 = amount4;
	}

	public void setAmount5(String amount5) {
		this.amount5 = amount5;
	}

	public void setAmount6(String amount6) {
		this.amount6 = amount6;
	}

	public void setAmount7(String amount7) {
		this.amount7 = amount7;
	}

	public void setAmount8(String amount8) {
		this.amount8 = amount8;
	}

	public void setAmount9(String amount9) {
		this.amount9 = amount9;
	}

	public void setAmount10(String amount10) {
		this.amount10 = amount10;
	}

	List<BigDecimal> amoutList = new ArrayList<BigDecimal>();

	public List<BigDecimal> getAmoutList() {
		return amoutList;
	}

	public void setAmoutList(List<BigDecimal> amoutList) {
		this.amoutList = amoutList;
	}



	public Map<String, String> getFundWiseMap() {
		return fundWiseMap;
	}

	public void setFundWiseMap(Map<String, String> fundWiseMap) {
		this.fundWiseMap = fundWiseMap;
	}

	// Below are used for Enhancement
	BigDecimal openingBalance;

	public BigDecimal getOpeningBalance() {
		return openingBalance;
	}

	public void setOpeningBalance(BigDecimal openingBalance) {
		this.openingBalance = openingBalance;
	}

	public BigDecimal getClosingBalance() {
		return closingBalance;
	}

	public void setClosingBalance(BigDecimal closingBalance) {
		this.closingBalance = closingBalance;
	}

	

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getCreditOPB() {
		return creditOPB;
	}

	public void setCreditOPB(BigDecimal creditOPB) {
		this.creditOPB = creditOPB;
	}

	public BigDecimal getDebitOPB() {
		return debitOPB;
	}

	public void setDebitOPB(BigDecimal debitOPB) {
		this.debitOPB = debitOPB;
	}

	public BigDecimal getTillDateCreditOPB() {
		return tillDateCreditOPB;
	}

	public void setTillDateCreditOPB(BigDecimal tillDateCreditOPB) {
		this.tillDateCreditOPB = tillDateCreditOPB;
	}

	public BigDecimal getTillDateDebitOPB() {
		return tillDateDebitOPB;
	}

	public void setTillDateDebitOPB(BigDecimal tillDateDebitOPB) {
		this.tillDateDebitOPB = tillDateDebitOPB;
	}

	public BigDecimal getDebitAmount() {
		return debitAmount;
	}

	public void setDebitAmount(BigDecimal debitAmount) {
		this.debitAmount = debitAmount;
	}

	public BigDecimal getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(BigDecimal creditAmount) {
		this.creditAmount = creditAmount;
	}

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

	public void addToAmountMap(String fundWiseKey, String amount2) {
		if (fundWiseMap == null) {
			fundWiseMap = new HashMap<String, String>();
		}
		fundWiseMap.put(fundWiseKey, amount2);

	}

	public String getFromAmountMap(String fundWiseKey) {
		if (fundWiseMap == null)
			return "0.0";
		else if (fundWiseMap.get(fundWiseKey) != null)
			return (String) fundWiseMap.get(fundWiseKey);
		else
			return "0.0";
	}

	@Override
	public String toString() {
		StringBuffer str = new StringBuffer(1024);
		str.append(accCode).append(":").append(accName).append(":").append(amount).append(":").append(creditAmount)
				.append(":").append(debitAmount).append(":").append(fundWiseMap);

		return str.toString();
	}

}
