package com.exilant.eGov.src.reports;

public class DepositeRegisterReportBean {
	
	public String sno;
	public String voucherDate;
	public String PayeeName;
	public String mode;
	public String reciptnumber;
	public String amount;
	public String voucherNumber;
	public String income;
	public String year;
	public String bdeposite;
	public String chequeAmount;
	
	
	public DepositeRegisterReportBean()
	{
		this.sno="";
		this.reciptnumber="";
		
	    this.voucherNumber="";
	    this.PayeeName="";
	    this.voucherDate="";
	    
	    this. income="";
	    this.year="";
	    this.amount="";
	    this.bdeposite="";
	   this.chequeAmount="";
	    	
	}


	public String getPayeeName() {
		return PayeeName;
	}


	public void setPayeeName(String payeeName) {
		PayeeName = payeeName;
	}


	public String getReciptnumber() {
		return reciptnumber;
	}


	public void setReciptnumber(String reciptnumber) {
		this.reciptnumber = reciptnumber;
	}


	public String getSno() {
		return sno;
	}


	public void setSno(String sno) {
		this.sno = sno;
	}


	

	public String getVoucherDate() {
		return voucherDate;
	}


	public void setVoucherDate(String voucherDate) {
		this.voucherDate = voucherDate;
	}


	public String getVoucherNumber() {
		return voucherNumber;
	}


	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}


	public String getMode() {
		return mode;
	}


	public void setMode(String mode) {
		this.mode = mode;
	}


	public String getAmount() {
		return amount;
	}


	public void setAmount(String amount) {
		this.amount = amount;
	}


	public String getBdeposite() {
		return bdeposite;
	}


	public void setBdeposite(String bdeposite) {
		this.bdeposite = bdeposite;
	}


	public String getIncome() {
		return income;
	}


	public void setIncome(String income) {
		this.income = income;
	}


	


	public String getChequeAmount() {
		return chequeAmount;
	}


	public void setChequeAmount(String chequeAmount) {
		this.chequeAmount = chequeAmount;
	}


	public String getYear() {
		return year;
	}


	public void setYear(String year) {
		this.year = year;
	}
}