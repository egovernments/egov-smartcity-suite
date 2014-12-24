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
