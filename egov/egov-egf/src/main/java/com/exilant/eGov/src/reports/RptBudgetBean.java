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
package com.exilant.eGov.src.reports;

import org.apache.log4j.Logger;

public class RptBudgetBean
{  
	private static final Logger LOGGER = Logger.getLogger(RptBudgetBean.class);
	private String slno;
	private String code;
	private String particulars;
	private String actPrevYr;
	private String budgetCurYr;
	private String actUptoDec;
	private String revisedBudgetCurYr;
	private String budgetNextYr;
    private String previousYear;
    private String currentYear;
    private String nextYear;
	
	public int finYear;
	public int accType;


	/**
	 *
	 */
	public RptBudgetBean() {

		// TODO Auto-generated constructor stub
	}

	/**
	 * @return Returns the slno.
	 */
	public  String getSlno() {
		return slno;
	}
	/**
	 * @param slno The slno to set.
	 */
	public  void setSlno(String slno) {
		this.slno = slno;
	}
	
	/**
	 * @return Returns the particulars.
	 */
	public  String getParticulars() {
		return particulars;
	}
	/**
	 * @param billDate The particulars to set.
	 */
	public  void setParticulars(String particulars) {
		this.particulars = particulars;
	}
	/**
	 * @return Returns the code.
	 */
	public  String getCode() {
		return code;
	}
	/**
	 * @param code The code to set.
	 */
	public   void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return Returns the actPrevYr.
	 */
	public  String getActPrevYr() {
		return actPrevYr;
	}
	/**
	 * @param actPrevYr The actPrevYr to set.
	 */
	public  void setActPrevYr(String actPrevYr) {
		this.actPrevYr = actPrevYr;
	}
	/**
	 * @return Returns the budgetCurYr.
	 */
	public String getBudgetCurYr() {
		return budgetCurYr;
	}
	/**
	 * @param budgetCurYr The budgetCurYr to set.
	 */
	public void setBudgetCurYr(String budgetCurYr) {
		this.budgetCurYr = budgetCurYr;
	}
	/**
	 * @return Returns the actUptoDec.
	 */
	public String getActUptoDec() {
		return actUptoDec;
	}
	/**
	 * @param actUptoDec The actUptoDec to set.
	 */
	public void setActUptoDec(String actUptoDec) {
		this.actUptoDec = actUptoDec;
	}
	/**
	 * @return Returns the revisedBudgetCurYr.
	 */
	public String getRevisedBudgetCurYr() {
		return revisedBudgetCurYr;
	}
	/**
	 * @param revisedBudgetCurYr The revisedBudgetCurYr to set.
	 */
	public void setRevisedBudgetCurYr(String revisedBudgetCurYr) {
		this.revisedBudgetCurYr = revisedBudgetCurYr;
	}
	/**
	 * @return Returns the budgetNextYr.
	 */
	public String getBudgetNextYr() {
		return budgetNextYr;
	}
	/**
	 * @param BudgetNextYr The BudgetNextYr to set.
	 */
	public void setBudgetNextYr(String budgetNextYr) {
		this.budgetNextYr = budgetNextYr;
	}
	
	/**
	 * @return Returns the finYear.
	 */
	public int getFinYear() {
		return finYear;
	}
	/**
	 * @param finYear The finYear to set.
	 */
	public void setFinYear(int finYear) {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("inside set finYear");
		this.finYear = finYear;
	}
	/**
	 * @return Returns the accType.
	 */
	public int getAccType() {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("inside set accType");
				return accType;
			}
	/**
	 * @param accType The accType to set.
	 */
	public void setAccType(int accType) {
	this.accType = accType;
	}
    /**
     * @return Returns the previousYear.
     */
    public String getPreviousYear() {
        if(LOGGER.isDebugEnabled())     LOGGER.debug("inside set previousYear");
                return previousYear;
            }
    /**
     * @param previousYear The previousYear to set.
     */
    public void setPreviousYear(String previousYear) {
    this.previousYear = previousYear;
    }
    /**
     * @return Returns the currentYear.
     */
    public String getCurrentYear() {
        if(LOGGER.isDebugEnabled())     LOGGER.debug("inside set currentYear");
                return currentYear;
            }
    /**
     * @param currentYear The currentYear to set.
     */
    public void setCurrentYear(String currentYear) {
    this.currentYear = currentYear;
    }
    /**
     * @return Returns the nextYear.
     */
    public String getNextYear() {
        if(LOGGER.isDebugEnabled())     LOGGER.debug("inside set nextYear");
                return nextYear;
            }
    /**
     * @param nextYear The nextYear to set.
     */
    public void setNextYear(String nextYear) {
    this.nextYear = nextYear;
    }
}
