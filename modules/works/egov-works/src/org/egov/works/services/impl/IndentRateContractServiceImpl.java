package org.egov.works.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.egov.commons.CFinancialYear;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.models.rateContract.Indent;
import org.egov.works.models.rateContract.IndentRateContractNumberGenerator;
import org.egov.works.services.IndentRateContractService;

public class IndentRateContractServiceImpl extends BaseServiceImpl<Indent,Long> implements IndentRateContractService {

	private IndentRateContractNumberGenerator indentRateContractNumberGenerator;
	private BudgetDetailsDAO budgetDetailsDAO;
	public IndentRateContractServiceImpl(PersistenceService<Indent, Long> persistenceService) {
		super(persistenceService);
	}
	
	public void setIndentRateContractNumber(Indent entity,CFinancialYear finYear) {
		if(entity.getIndentNumber() == null) {
			entity.setIndentNumber(indentRateContractNumberGenerator.getIndentRateContractNumber(entity, finYear));
		}
	}

	public void setIndentRateContractNumberGenerator(
			IndentRateContractNumberGenerator indentRateContractNumberGenerator) {
		this.indentRateContractNumberGenerator = indentRateContractNumberGenerator;
	}
	
	public String getBudgetAppropriationNumber(Indent entity){
		CFinancialYear finYear = getCurrentFinancialYear(entity.getIndentDate());
		return indentRateContractNumberGenerator.getBudgetApprNo(entity, finYear);
	}

	public void setBudgetDetailsDAO(BudgetDetailsDAO budgetDetailsDAO) {
		this.budgetDetailsDAO = budgetDetailsDAO;
	}
	
	public boolean checkForBudgetaryAppropriation(Indent indent) 
	throws ValidationException{
		CFinancialYear finYear = getCurrentFinancialYear(indent.getIndentDate());
		List<Long> budgetheadid=new ArrayList<Long>();
		budgetheadid.add(indent.getBudgetGroup().getId());
		
		boolean flag=false;
		flag= budgetDetailsDAO.consumeEncumbranceBudget(
			finYear.getId(), Integer.valueOf(11), 
			indent.getIndentNumber(), 
			indent.getDepartment().getId(), 
			(indent.getFunction()==null? null:indent.getFunction().getId()),null,null,null,
			(indent.getBoundary()==null? null:indent.getBoundary().getId()),
			(indent.getBudgetGroup()==null? null:budgetheadid), 
			(indent.getFund()==null? null:indent.getFund().getId()), 
			indent.getIndentAmount().getValue(),
			indent.getBudgetApprNo()==null? null:indent.getBudgetApprNo());
		
		return flag;
	}

	public BigDecimal getBudgetAvailable(Indent indent) throws ValidationException{
		   BigDecimal budgetAvailable=BigDecimal.ZERO;
		   List<Long> budgetheadid=new ArrayList<Long>();
			CFinancialYear finYear = getCurrentFinancialYear(indent.getIndentDate());
			budgetheadid.add(indent.getBudgetGroup().getId());
			budgetAvailable=budgetDetailsDAO.getPlanningBudgetAvailable(
					   	 finYear.getId(), 
						 indent.getDepartment().getId(),
						 (indent.getFunction()==null? null:indent.getFunction().getId()), null,null,null,null,
						 (indent.getBudgetGroup()==null? null:budgetheadid),
						 (indent.getFund()==null? null:indent.getFund().getId()));
			return budgetAvailable;
	 }

}