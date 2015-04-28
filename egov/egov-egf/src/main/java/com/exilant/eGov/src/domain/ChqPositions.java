/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
 * 
 *     The updated version of eGov suite of products as by eGovernments Foundation 
 *     is available at http://www.egovernments.org
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or 
 *     http://www.gnu.org/licenses/gpl.html .
 * 
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 * 
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
/*
   * ChqPositions.java Created on July 17, 2007
   *
   * Copyright 2005 eGovernments Foundation. All rights reserved.
   * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.exilant.eGov.src.domain;

/**
 * 
 * @author Mani
 * This Class represents one Complete Cheque Printing Format For Specific Bank 
 * These Values are used to position the data like date ,Amount in the Jsp Page
 *
 */

public class ChqPositions {
	public ChqPosition rs;
	public ChqPosition amount1;//FirstLine
	public ChqPosition amount2;//SecondLine
	public ChqPosition name1;//FirstLine
	public ChqPosition name2;//SecondLine	
	public ChqPosition date;
	public ChqPosition sign;
	/**
	 * @return the amount1
	 * 
	 */
	public ChqPosition getAmount1() {
		return amount1;
	}
	/**
	 * @param amount1 the amount1 to set
	 */
	public void setAmount1(ChqPosition amount1) {
		this.amount1 = amount1;
	}
	/**
	 * @return the amount2
	 */
	public ChqPosition getAmount2() {
		return amount2;
	}
	/**
	 * @param amount2 the amount2 to set
	 */
	public void setAmount2(ChqPosition amount2) {
		this.amount2 = amount2;
	}
	/**
	 * @return the name1
	 */
	public ChqPosition getName1() {
		return name1;
	}
	/**
	 * @param name1 the name1 to set
	 */
	public void setName1(ChqPosition name1) {
		this.name1 = name1;
	}
	/**
	 * @return the name2
	 */
	public ChqPosition getName2() {
		return name2;
	}
	/**
	 * @param name2 the name2 to set
	 */
	public void setName2(ChqPosition name2) {
		this.name2 = name2;
	}


	/**
	 * @return the date
	 */
	public ChqPosition getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(ChqPosition date) {
		this.date = date;
	}


	/**
	 * @return the rs
	 */
	public ChqPosition getRs() {
		return rs;
	}
	/**
	 * @param rs the rs to set
	 */
	public void setRs(ChqPosition rs) {
		this.rs = rs;
	}
	/**
	 * @return the sign
	 */
	public ChqPosition getSign() {
		return sign;
	}
	/**
	 * @param sign the sign to set
	 */
	public void setSign(ChqPosition sign) {
		this.sign = sign;
	}







}

