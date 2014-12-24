/*
 * PaymentAdviceForm.java Created on Mar 20, 2008
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.payment.client;

import org.apache.struts.action.ActionForm;

/**
 * TODO Brief Description of the purpose of the class/interface
 * 
 * @author Sathish P
 * @version 1.00 
 */

public class PaymentAdviceForm extends ActionForm
{
	
	public String pymntVhNo;
	public String pymntVhDateFrom;
	public String pymntVhDateTo;
	public String chequeNo;
	public Boolean select;
	public String[] pymntVhId;
	public String[] chqNo;
	public String[] chqDate;
	public String[] partyName;
	public String[] accountNo;
	public String[] amount;
	public String[] isCancel;
	public String  pymntType;
	public String departmentId;
	public String functionaryId;
	
	public String getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	public String getFunctionaryId() {
		return functionaryId;
	}
	public void setFunctionaryId(String functionaryId) {
		this.functionaryId = functionaryId;
	}
	/**
	 * @return the select
	 */
	public Boolean getSelect()
	{
		return select;
	}
	/**
	 * @param select the select to set
	 */
	public void setSelect(Boolean select)
	{
		this.select = select;
	}
	/**
	 * @return the chequeNo
	 */
	public String getChequeNo()
	{
		return chequeNo;
	}
	/**
	 * @param chequeNo the chequeNo to set
	 */
	public void setChequeNo(String chequeNo)
	{
		this.chequeNo = chequeNo;
	}
	
	
	public String getPymntVhDateFrom() {
		return pymntVhDateFrom;
	}
	public void setPymntVhDateFrom(String pymntVhDateFrom) {
		this.pymntVhDateFrom = pymntVhDateFrom;
	}
	public String getPymntVhDateTo() {
		return pymntVhDateTo;
	}
	public void setPymntVhDateTo(String pymntVhDateTo) {
		this.pymntVhDateTo = pymntVhDateTo;
	}
	/**
	 * @return the pymntVhNo
	 */
	public String getPymntVhNo()
	{
		return pymntVhNo;
	}
	/**
	 * @param pymntVhNo the pymntVhNo to set
	 */
	public void setPymntVhNo(String pymntVhNo)
	{
		this.pymntVhNo = pymntVhNo;
	}
	/**
	 * @return the pymntVhId
	 */
	public String[] getPymntVhId()
	{
		return pymntVhId;
	}
	/**
	 * @param pymntVhId the pymntVhId to set
	 */
	public void setPymntVhId(String[] pymntVhId)
	{
		this.pymntVhId = pymntVhId;
	}
	/**
	 * @return the accountNo
	 */
	public String[] getAccountNo()
	{
		return accountNo;
	}
	/**
	 * @param accountNo the accountNo to set
	 */
	public void setAccountNo(String[] accountNo)
	{
		this.accountNo = accountNo;
	}
	/**
	 * @return the amount
	 */
	public String[] getAmount()
	{
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(String[] amount)
	{
		this.amount = amount;
	}
	/**
	 * @return the chqNo
	 */
	public String[] getChqNo()
	{
		return chqNo;
	}
	/**
	 * @param chqNo the chqNo to set
	 */
	public void setChqNo(String[] chqNo)
	{
		this.chqNo = chqNo;
	}
	/**
	 * @return the partyName
	 */
	public String[] getPartyName()
	{
		return partyName;
	}
	/**
	 * @param partyName the partyName to set
	 */
	public void setPartyName(String[] partyName)
	{
		this.partyName = partyName;
	}
	/**
	 * @return the chqDate
	 */
	public String[] getChqDate()
	{
		return chqDate;
	}
	/**
	 * @param chqDate the chqDate to set
	 */
	public void setChqDate(String[] chqDate)
	{
		this.chqDate = chqDate;
	}
	/**
	 * @return the isCancel
	 */
	public String[] getIsCancel()
	{
		return isCancel;
	}
	/**
	 * @param isCancel the isCancel to set
	 */
	public void setIsCancel(String[] isCancel)
	{
		this.isCancel = isCancel;
	}
	public String getPymntType() {
		return pymntType;
	}
	public void setPymntType(String pymntType) {
		this.pymntType = pymntType;
	}
}

