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
 * Created on Aug 27, 2008
 * @author Iliyaraja S
 */
package com.exilant.eGov.src.reports;

public class SummaryStatementBean
{
//	for displaying  in the report (Using display tag)
	
	public String headOfAccount;
	public String workOrderNo;
	public String nameOfProject;
	public String valueOfWorkAmount;
	public String expenditureAmount;
	public String expenditureBillAdmittedAmount;
	public String totalExpenditure="";
	public String  amountOfContractUnexecute;
	public String projectCompleted;
	public String worksPassedAmount;
	
	
	public String getAmountOfContractUnexecute()
	{
		return amountOfContractUnexecute;
	}
	
	
	public void setAmountOfContractUnexecute(String amountOfContractUnexecute)
	{
		this.amountOfContractUnexecute = amountOfContractUnexecute;
	}
	
	
	public String getExpenditureAmount()
	{
		return expenditureAmount;
	}
	
	
	public void setExpenditureAmount(String expenditureAmount)
	{
		this.expenditureAmount = expenditureAmount;
	}
	
	
	public String getExpenditureBillAdmittedAmount()
	{
		return expenditureBillAdmittedAmount;
	}
	
	
	public void setExpenditureBillAdmittedAmount(String expenditureBillAdmittedAmount)
	{
		this.expenditureBillAdmittedAmount = expenditureBillAdmittedAmount;
	}
	
	
	public String getHeadOfAccount()
	{
		return headOfAccount;
	}
	
	
	public void setHeadOfAccount(String headOfAccount)
	{
		this.headOfAccount = headOfAccount;
	}
	
	
	public String getNameOfProject()
	{
		return nameOfProject;
	}
	
	
	public void setNameOfProject(String nameOfProject)
	{
		this.nameOfProject = nameOfProject;
	}
	
	
	public String getProjectCompleted()
	{
		return projectCompleted;
	}
	
	
	public void setProjectCompleted(String projectCompleted)
	{
		this.projectCompleted = projectCompleted;
	}
	
	
	public String getTotalExpenditure()
	{
		return totalExpenditure;
	}
	
	
	public void setTotalExpenditure(String totalExpenditure)
	{
		this.totalExpenditure = totalExpenditure;
	}
	
	
	public String getValueOfWorkAmount()
	{
		return valueOfWorkAmount;
	}
	
	
	public void setValueOfWorkAmount(String valueOfWorkAmount)
	{
		this.valueOfWorkAmount = valueOfWorkAmount;
	}
	
	
	public String getWorkOrderNo()
	{
		return workOrderNo;
	}
	
	
	public void setWorkOrderNo(String workOrderNo)
	{
		this.workOrderNo = workOrderNo;
	}
	
	
	public String getWorksPassedAmount() {
		return worksPassedAmount;
	}
	
	
	public void setWorksPassedAmount(String worksPassedAmount) {
		this.worksPassedAmount = worksPassedAmount;
	}
}
