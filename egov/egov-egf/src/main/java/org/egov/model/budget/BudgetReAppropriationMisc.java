package org.egov.model.budget;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.egov.infra.workflow.entity.StateAware;
import org.egov.utils.Constants;

public class BudgetReAppropriationMisc extends StateAware{
	Long id;
	String sequenceNumber;
	String remarks;
	Date reAppropriationDate;
	Set<BudgetReAppropriation> budgetReAppropriations = new HashSet<BudgetReAppropriation>();
	
	public Set<BudgetReAppropriation> getBudgetReAppropriations() {
		return budgetReAppropriations;
	}
	public void setBudgetReAppropriations(Set<BudgetReAppropriation> budgetReAppropriations) {
		this.budgetReAppropriations = budgetReAppropriations;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Date getReAppropriationDate() {
		return reAppropriationDate;
	}
	public void setReAppropriationDate(Date reAppropriationDate) {
		this.reAppropriationDate = reAppropriationDate;
	}
	@Override
	public String getStateDetails() {
		return sequenceNumber==null?"":sequenceNumber;
	}
	
	public List<BudgetReAppropriation> getNonApprovedReAppropriations(){
		List<BudgetReAppropriation> reAppList = new ArrayList<BudgetReAppropriation>();
		budgetReAppropriations = budgetReAppropriations == null? new HashSet<BudgetReAppropriation>():budgetReAppropriations;
		for (BudgetReAppropriation entry : budgetReAppropriations) {
			if(!Constants.END.equalsIgnoreCase(entry.getState().getValue()) || !"APPROVED".equalsIgnoreCase(entry.getState().getValue()))
				reAppList.add(entry);
		}
		return reAppList;
	}
	
	public BudgetReAppropriation getBudgetReAppropriationWithId(Long id){
		for (BudgetReAppropriation reAppropriation : budgetReAppropriations) {
			if(id.equals(reAppropriation.getId()))
				return reAppropriation;
		}
		return null;
	}
}
