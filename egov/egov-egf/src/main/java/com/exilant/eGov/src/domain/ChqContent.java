/*
   * ChqContent.java Created on July 17, 2007
   *
   * Copyright 2005 eGovernments Foundation. All rights reserved.
   * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.exilant.eGov.src.domain;

/**
 * 
 * @author Mani
 * Thsi Class holds the data to be printed on cheque. 
 * Datas will be taken From ClientSide
 *  
 *
 */
public class ChqContent {
	public String rs="";
	public static String amount1="";
	public String amount2="";
	public String name1="";
	public String name2="";	
	public String date="";
	public String sign="";
	/**
	 * @return the amount1
	 */
	public String getAmount1() {
		return amount1;
	}
	/**
	 * @param amount1 the amount1 to set
	 */
	public void setAmount1(String amount1) {
		this.amount1 = amount1;
	}
	/**
	 * @return the amount2
	 */
	public String getAmount2() {
		return amount2;
	}
	/**
	 * @param amount2 the amount2 to set
	 */
	public void setAmount2(String amount2) {
		this.amount2 = amount2;
	}
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}
	/**
	 * @return the name1
	 */
	public String getName1() {
		return name1;
	}
	/**
	 * @param name1 the name1 to set
	 */
	public void setName1(String name1) {
		this.name1 = name1;
	}
	/**
	 * @return the name2
	 */
	public String getName2() {
		return name2;
	}
	/**
	 * @param name2 the name2 to set
	 */
	public void setName2(String name2) {
		this.name2 = name2;
	}
	/**
	 * @return the rs
	 */
	public String getRs() {
		return rs;
	}
	/**
	 * @param rs the rs to set
	 */
	public void setRs(String rs) {
		this.rs = rs;
	}
	/**
	 * @return the sign
	 */
	public String getSign() {
		return sign;
	}
	/**
	 * @param sign the sign to set
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}





}
