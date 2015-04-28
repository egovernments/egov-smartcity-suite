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
 * Created on March 20, 2006
 * @author Prabhu
 */

package com.exilant.eGov.src.reports;

public class MonthlyExpRptBean
{
	private String monthName;
	private String yearName;
	private String zoneName;


// for displaying  in the report (Using display tag)

	private String deptCode;
	private String accCode;
	private String nomenName;
	private String budEstimate;
	private String expPrev;
	private String expCurr;
	private String payPrev;
	private String payCurr;
	private String progExp;
	private String liaAccured;


	/**
	 *
	 */
	public MonthlyExpRptBean() {

		this.monthName = "";
		this.yearName = "";
		this.zoneName = "";

		this.deptCode = "";
		this.accCode = "";
		this.nomenName = "";
		this.budEstimate = "";
		this.expPrev = "";
		this.expCurr = "";
		this.payPrev = "";
		this.payCurr = "";
		this.progExp = "";
		this.liaAccured = "";  
	}

	/**
	 * @return Returns the month.
	 */
	public String getMonthName() {
		return monthName;
	}
	/**
	 * @param month The month to set.
	 */
	public void setMonthName(String monthName) {
		this.monthName = monthName;
	}
	/**
	 * @return Returns the year.
	 */
	public String getYearName() {
		return yearName;
	}
	/**
	 * @param year. The year to set.
	 */
	public void setYearName(String yearName) {
		this.yearName = yearName;
	}
	/**
	 * @return Returns the zone.
	 */
	public String getZoneName() {
		return zoneName;
	}
	/**
	 * @param zone. The zone to set.
	 */
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

// Getter and Setter methods for displaying the report

	/**
	 * @return Returns the dept code.
	 */
	public String getDeptCode() {
		return deptCode;
	}
	
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	
	/**
	 * @return Returns the acc code.
	 */
	public String getAccCode() {
		return accCode;
	}
	/**
	 * @param acccode. The acc code to set.
	 */
	public void setAccCode(String accCode) {
		this.accCode = accCode;
	}
	
	/**
	 * @return Returns the nomenclature name.
	 */
	public String getNomenName() {
		return nomenName;
	}
	/**
	 * @param nomenname The nomenclature to set.
	 */
	public void setNomenName(String nomenName) {
		this.nomenName = nomenName;
	}
	/**
	 * @return Returns the Budget Estimate.
	 */
	public String getBudEstimate() {
		return budEstimate;
	}
	/**
	 * @param budestimate. The Budget Estimate to set.
	 */
	public void setBudEstimate(String budEstimate) {
		this.budEstimate = budEstimate;
	}
	
	/**
	 * @return Returns the Expenditure upto the previous month of entered date.
	 */
	public String getExpPrev() {
		return expPrev;
	}
	/**
	 * @param expprev The Expenditure upto the previous month to set.
	 */
	public void setExpPrev(String expPrev) {
		this.expPrev = expPrev;
	}
	/**
	 * @return Returns the Current month Expenditure.
	 */
	public String getExpCurr() {
		return expCurr;
	}
	/**
	 * @param expCurr The Expenditure during the current month to set.
	 */
	public void setExpCurr(String expCurr) {
		this.expCurr = expCurr;
	}
	/**
	 * @return Returns the payment upto the previous month.
	 */
	public String getPayPrev() {
		return payPrev;
	}
	/**
	 * @param payPrev The payment upto the previous month to set.
	 */
	public void setPayPrev(String payPrev) {
		this.payPrev = payPrev;
	}
	/**
	 * @return Returns the Current month expenditure.
	 */
	public String getPayCurr() {
		return payCurr;
	}
	/**
	 * @param paycurr. The payment during the current month to set.
	 */
	public void setPayCurr(String payCurr) {
		this.payCurr = payCurr;
	}
	/**
	 * @return Returns the progressive expenditure.
	 */
	public String getProgExp() {
		return progExp;
	}
	/**
	 * @param progexp. The progressive expenditure to set. It is the sum of expPrev + expCurr
	 */
	public void setProgExp(String progExp) {
		this.progExp = progExp;
	}
	/**
	 * @return Returns the Liablities Accured.
	 */
	public String getLiaAccured() {
		return liaAccured;
	}
	/**
	 * @param liaAccured. The Liablities Accured to set. It is calculated as 
	 */
	public void setLiaAccured(String liaAccured) {
		this.liaAccured = liaAccured;
	}




}



