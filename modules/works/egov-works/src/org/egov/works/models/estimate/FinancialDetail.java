package org.egov.works.models.estimate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.infstr.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.egov.model.budget.BudgetGroup;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;

public class FinancialDetail extends BaseModel {
	
	private Fund fund;
	
	private CFunction function;
	private Functionary functionary;
	private Scheme scheme;
	private SubScheme subScheme;
	private BudgetGroup budgetGroup;
	private AbstractEstimate abstractEstimate;
	private CChartOfAccounts coa;
	
	@Valid
	private List<FinancingSource> financingSources = new LinkedList<FinancingSource>();
	
	public FinancialDetail(){
	}
	
	public FinancialDetail(AbstractEstimate estimate,Fund fund,BudgetGroup budgetGroup){
		this.abstractEstimate = estimate;
		this.fund = fund;
		this.budgetGroup = budgetGroup;
	}
	
	//for testing
	public FinancialDetail(Fund fund,CFunction function,Functionary functionary){
		this.function = function;
		this.fund = fund;
		this.functionary = functionary;
	}
	
	public AbstractEstimate getAbstractEstimate() {
		return abstractEstimate;
	}
	public void setAbstractEstimate(AbstractEstimate estimate) {
		this.abstractEstimate = estimate;
	}
	
	public BudgetGroup getBudgetGroup() {
		return budgetGroup;
	}
	
	public void setBudgetGroup(BudgetGroup budgetGroup) {
		this.budgetGroup = budgetGroup;
	}
	
	@NotNull(message="financial.fund.null")
	public Fund getFund() {
		return fund;
	}
	
	public void setFund(Fund fund) {
		this.fund = fund;
	}
	public CFunction getFunction() {
		return function;
	}
	public void setFunction(CFunction function) {
		this.function = function;
	}
	public Functionary getFunctionary() {
		return functionary;
	}
	public void setFunctionary(Functionary functionary) {
		this.functionary = functionary;
	}
	public Scheme getScheme() {
		return scheme;
	}
	public void setScheme(Scheme scheme) {
		this.scheme = scheme;
	}
	public SubScheme getSubScheme() {
		return subScheme;
	}
	public void setSubScheme(SubScheme subScheme) {
		this.subScheme = subScheme;
	}

	public List<FinancingSource> getFinancingSources() {
		return financingSources;
	}

	public void setFinancingSources(List<FinancingSource> financingSources) {
		this.financingSources = financingSources;
	}
	
	public void addFinancingSource(FinancingSource financingSource){
		this.financingSources.add(financingSource);
	}
	
	public List<ValidationError> validate(){
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		
		double total = 0;
		boolean finSourceError=false;
		
		if(fund==null){
			validationErrors.add(new ValidationError("fund_null","financial.fund.null"));
		}
		
		if(financingSources==null || financingSources.isEmpty()){
			validationErrors.add(new ValidationError("financingsource_null","financingsource.null"));
		}
		
		int errorCnt = validationErrors.size();
		
		if(financingSources!=null){
			for(FinancingSource financingSource : financingSources) {
				if(!finSourceError)
					validationErrors.addAll(financingSource.validate());
				
				//if one financial source row has invalid values, same check need not be done
				//for the remaining objects, and duplicate error messages can be avoided
				if(!finSourceError && errorCnt < validationErrors.size())
					finSourceError=true;
				
				total+=financingSource.getPercentage();
			}
		}

		if((financingSources!=null && !financingSources.isEmpty()) && total != 100){
			validationErrors.add(new ValidationError(
					"percentageequalto100","financingsource.percentage.percentageequalto100"));
		}
		
		return validationErrors;
	}
	
	/**
	 * This method is invoked from the script to generate the budget appropriation number
	 * 
	 * @return an instance of <code>FinancingSource</code> having the maximum of the 
	 * financial sources selected
	 */
	public FinancingSource getMaxFinancingSource(){
		double max = 0.0;
		FinancingSource maxFinSource = null;
		for(FinancingSource finSource : financingSources){
			if(finSource.getPercentage() > max){
				max = finSource.getPercentage();
				maxFinSource = finSource; 
			}
		}
		
		return maxFinSource;
	}

	public CChartOfAccounts getCoa() {
		return coa;
	}

	public void setCoa(CChartOfAccounts coa) {
		this.coa = coa;
	}
}
