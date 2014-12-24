/*
 * advanceDisburseByChequeForm.java Created on Oct 25, 2007
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*package org.egov.payroll.client.advance;

import org.apache.struts.action.ActionForm;
*/
/**
 * TODO Brief Description of the purpose of the class/interface
 * 
 * @author Sathish
 * @version 1.00
 */

/*public class AdvanceDisbursementByChequeForm extends ActionForm
{
	private String[] id;
	private String[] glCode;
	private String employee;
	private String employeeName;
	private String[] advanceType;
	private String[] sanctionNo;
	private String[] sanctionedBy;
	private String[] sanctionAmount;	
	private String[] pay;
	private String totalAmt;
	private String voucherNo;
	private String voucherDate;
	private String paidBy;
	private String bank;
	private String bankAccount;
	private String balance;
	private String chequeNo;
	private String chequeDate;
	private String voucherNoPrefix;
	private Boolean isChqSurrendered=false;
	private String newChequeNo;
	private String newChequeDate;
	private String fundId;
	private String fundName;
	private String vhId;
	
	*//**
	 * @return the fundName
	 *//*
	public String getFundName()
	{
		return fundName;
	}
	*//**
	 * @param fundName the fundName to set
	 *//*
	public void setFundName(String fundName)
	{
		this.fundName = fundName;
	}
	*//**
	 * @return the bankAccount
	 *//*
	public String getBankAccount()
	{
		return bankAccount;
	}
	*//**
	 * @param bankAccount the bankAccount to set
	 *//*
	public void setBankAccount(String bankAccount)
	{
		this.bankAccount = bankAccount;
	}
	*//**
	 * @return the advanceType
	 *//*
	public String[] getAdvanceType()
	{
		return advanceType;
	}
	*//**
	 * @param advanceType the advanceType to set
	 *//*
	public void setAdvanceType(String[] advanceType)
	{
		this.advanceType = advanceType;
	}
	*//**
	 * @return the balance
	 *//*
	public String getBalance()
	{
		return balance;
	}
	*//**
	 * @param balance the balance to set
	 *//*
	public void setBalance(String balance)
	{
		this.balance = balance;
	}
	*//**
	 * @return the bank
	 *//*
	public String getBank()
	{
		return bank;
	}
	*//**
	 * @param bank the bank to set
	 *//*
	public void setBank(String bank)
	{
		this.bank = bank;
	}
	*//**
	 * @return the chequeDate
	 *//*
	public String getChequeDate()
	{
		return chequeDate;
	}
	*//**
	 * @param chequeDate the chequeDate to set
	 *//*
	public void setChequeDate(String chequeDate)
	{
		this.chequeDate = chequeDate;
	}
	*//**
	 * @return the chequeNo
	 *//*
	public String getChequeNo()
	{
		return chequeNo;
	}
	*//**
	 * @param chequeNo the chequeNo to set
	 *//*
	public void setChequeNo(String chequeNo)
	{
		this.chequeNo = chequeNo;
	}
	*//**
	 * @return the employee
	 *//*
	public String getEmployee()
	{
		return employee;
	}
	*//**
	 * @param employee the employee to set
	 *//*
	public void setEmployee(String employee)
	{
		this.employee= employee;
	}
	*//**
	 * @return the employeeName
	 *//*
	public String getEmployeeName()
	{
		return employeeName;
	}
	*//**
	 * @param employeeName the employeeName to set
	 *//*
	public void setEmployeeName(String employeeName)
	{
		this.employeeName = employeeName;
	}
	*//**
	 * @return the paidBy
	 *//*
	public String getPaidBy()
	{
		return paidBy;
	}
	*//**
	 * @param paidBy the paidBy to set
	 *//*
	public void setPaidBy(String paidBy)
	{
		this.paidBy = paidBy;
	}
	
	*//**
	 * @return the pay
	 *//*
	public String[] getPay()
	{
		return pay;
	}
	*//**
	 * @param pay the pay to set
	 *//*
	public void setPay(String[] pay)
	{
		this.pay = pay;
	}
	*//**
	 * @return the sanctionAmount
	 *//*
	public String[] getSanctionAmount()
	{
		return sanctionAmount;
	}
	*//**
	 * @param sanctionAmount the sanctionAmount to set
	 *//*
	public void setSanctionAmount(String[] sanctionAmount)
	{
		this.sanctionAmount = sanctionAmount;
	}
	*//**
	 * @return the sanctionedBy
	 *//*
	public String[] getSanctionedBy()
	{
		return sanctionedBy;
	}
	*//**
	 * @param sanctionedBy the sanctionedBy to set
	 *//*
	public void setSanctionedBy(String[] sanctionedBy)
	{
		this.sanctionedBy = sanctionedBy;
	}
	*//**
	 * @return the sanctionNo
	 *//*
	public String[] getSanctionNo()
	{
		return sanctionNo;
	}
	*//**
	 * @param sanctionNo the sanctionNo to set
	 *//*
	public void setSanctionNo(String[] sanctionNo)
	{
		this.sanctionNo = sanctionNo;
	}
	*//**
	 * @return the totalAmt
	 *//*
	public String getTotalAmt()
	{
		return totalAmt;
	}
	*//**
	 * @param total the total to set
	 *//*
	public void setTotalAmt(String totalAmt)
	{
		this.totalAmt = totalAmt;
	}
	*//**
	 * @return the voucherDate
	 *//*
	public String getVoucherDate()
	{
		return voucherDate;
	}
	*//**
	 * @param voucherDate the voucherDate to set
	 *//*
	public void setVoucherDate(String voucherDate)
	{
		this.voucherDate = voucherDate;
	}
	*//**
	 * @return the voucherNo
	 *//*
	public String getVoucherNo()
	{
		return voucherNo;
	}
	*//**
	 * @param voucherNo the voucherNo to set
	 *//*
	public void setVoucherNo(String voucherNo)
	{
		this.voucherNo = voucherNo;
	}
	*//**
	 * @return the voucherNoPrefix
	 *//*
	public String getVoucherNoPrefix()
	{
		return voucherNoPrefix;
	}
	*//**
	 * @param voucherNoPrefix the voucherNoPrefix to set
	 *//*
	public void setVoucherNoPrefix(String voucherNoPrefix)
	{
		this.voucherNoPrefix = voucherNoPrefix;
	}
	*//**
	 * @return the isChqSurrendered
	 *//*
	public Boolean getIsChqSurrendered()
	{
		return isChqSurrendered;
	}
	*//**
	 * @param isChqSurrendered the isChqSurrendered to set
	 *//*
	public void setIsChqSurrendered(Boolean isChqSurrendered)
	{
		this.isChqSurrendered = isChqSurrendered;
	}
	*//**
	 * @return the newChequeDate
	 *//*
	public String getNewChequeDate()
	{
		return newChequeDate;
	}
	*//**
	 * @param newChequeDate the newChequeDate to set
	 *//*
	public void setNewChequeDate(String newChequeDate)
	{
		this.newChequeDate = newChequeDate;
	}
	*//**
	 * @return the newChequeNo
	 *//*
	public String getNewChequeNo()
	{
		return newChequeNo;
	}
	*//**
	 * @param newChequeNo the newChequeNo to set
	 *//*
	public void setNewChequeNo(String newChequeNo)
	{
		this.newChequeNo = newChequeNo;
	}
	*//**
	 * @return the id
	 *//*
	public String[] getId()
	{
		return id;
	}
	*//**
	 * @param id the id to set
	 *//*
	public void setId(String[] id)
	{
		this.id = id;
	}
	*//**
	 * @return the fundId
	 *//*
	public String getFundId()
	{
		return fundId;
	}
	*//**
	 * @param fundId the fundId to set
	 *//*
	public void setFundId(String fundId)
	{
		this.fundId = fundId;
	}
	*//**
	 * @return the glCode
	 *//*
	public String[] getGlCode()
	{
		return glCode;
	}
	*//**
	 * @param glCode the glCode to set
	 *//*
	public void setGlCode(String[] glCode)
	{
		this.glCode = glCode;
	}
	*//**
	 * @return the vhId
	 *//*
	public String getVhId()
	{
		return vhId;
	}
	*//**
	 * @param vhId the vhId to set
	 *//*
	public void setVhId(String vhId)
	{
		this.vhId = vhId;
	}
}
*/
