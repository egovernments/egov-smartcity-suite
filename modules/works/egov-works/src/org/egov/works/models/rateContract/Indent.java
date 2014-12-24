package org.egov.works.models.rateContract;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.egov.commons.CFunction;
import org.egov.commons.ContractorGrade;
import org.egov.commons.EgwStatus;
import org.egov.commons.Fund;
import org.egov.commons.Period;
import org.egov.infstr.ValidationError;
import org.egov.infstr.models.Money;
import org.egov.infstr.models.validator.CheckDateFormat;
import org.egov.infstr.models.validator.Required;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.rjbac.dept.Department;
import org.egov.model.budget.BudgetGroup;
import org.egov.pims.model.PersonalInformation;
import org.egov.tender.TenderableGroupType;
import org.egov.tender.interfaces.Tenderable;
import org.egov.tender.interfaces.TenderableGroup;
import org.egov.works.models.workflow.WorkFlow;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;

public class Indent  extends WorkFlow implements TenderableGroup{
	
	public enum IndentStatus{
		CREATED,CHECKED,APPROVED,REJECTED,CANCELLED,RESUBMITTED
	}
	public enum Actions{
		SUBMIT_FOR_APPROVAL,APPROVE,REJECT,CANCEL;

		public String toString() {
			return this.name().toLowerCase();
		}
	}
	private String indentNumber;
	
	@Required(message="rateContract.date.null")
	@CheckDateFormat(message="invalid.fieldvalue.indentDate") 
	private Date indentDate;
	
	@Required(message = "rateContract.type.null")
	private String indentType;
	
	private Period validity;
	
	private String remarks;
		
	private Department department;
		
	private Boundary boundary;
	
	private Fund fund;
	
	private CFunction function;
	
	private BudgetGroup budgetGroup;
	
	private ContractorGrade contractorGrade;
	
	private Money indentAmount;
	
	private String budgetApprNo;
	
	private EgwStatus egwStatus;
	
	@NotNull(message="rateContract.preparedBy.is.null") 
	private PersonalInformation preparedBy; 

	private Long documentNumber;
		
	private boolean isBudgetCheckRequired;
		
	@Valid
	private List<IndentDetail> indentDetails = new LinkedList<IndentDetail>();
	
	private List<String> indentActions = new ArrayList<String>();
		
	public String getIndentNumber() {
		return indentNumber;
	}

	public void setIndentNumber(String indentNumber) {
		this.indentNumber = indentNumber;
	}
	
	public Date getIndentDate() {
		return indentDate;
	}

	public void setIndentDate(Date indentDate) {
		this.indentDate = indentDate;
	}

	
	public String getIndentType() {
		return indentType;
	}

	public void setIndentType(String indentType) {
		this.indentType = indentType;
	}

	@Required(message = "rateContract.validity.null")
	public Period getValidity() {
		return validity;
	}

	public void setValidity(Period validity) {
		this.validity = validity;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Required(message = "rateContract.dept.null")
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	@Required(message = "rateContract.zone.null")
	public Boundary getBoundary() {
		return boundary;
	}

	public void setBoundary(Boundary boundary) {
		this.boundary = boundary;
	}

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

	
	public BudgetGroup getBudgetGroup() {
		return budgetGroup;
	}

	public void setBudgetGroup(BudgetGroup budgetGroup) {
		this.budgetGroup = budgetGroup;
	}

	@Required(message = "rateContract.contractorGrade.null")
	public ContractorGrade getContractorGrade() {
		return contractorGrade;
	}

	public void setContractorGrade(ContractorGrade contractorGrade) {
		this.contractorGrade = contractorGrade;
	}

	public Money getIndentAmount() {
		return indentAmount;
	}

	public void setIndentAmount(Money indentAmount) {
		this.indentAmount = indentAmount;
	}

	public String getBudgetApprNo() {
		return budgetApprNo;
	}

	public void setBudgetApprNo(String budgetApprNo) {
		this.budgetApprNo = budgetApprNo;
	}

	public EgwStatus getEgwStatus() {
		return egwStatus;
	}

	public void setEgwStatus(EgwStatus egwStatus) {
		this.egwStatus = egwStatus;
	}
	
	public List<IndentDetail> getIndentDetails() {
		return indentDetails;
	}

	public void setIndentDetails(List<IndentDetail> indentDetails) {
		this.indentDetails = indentDetails;
	}

	@Override
	public String getStateDetails() {
		return "Indent Rate Contract : " + getIndentNumber();
	}
	
	public void addIndentDetails(IndentDetail detail) {
		this.indentDetails.add(detail);
	}
	
	public Collection<IndentDetail> getSorIndentDetails() {
		return CollectionUtils.select(indentDetails, new Predicate(){
			public boolean evaluate(Object indentDetail) {
				return ((IndentDetail)indentDetail).getScheduleOfRate()!=null;
			}});
	}
	
	public Collection<IndentDetail> getNonSorIndentDetails() {
		return CollectionUtils.select(indentDetails, new Predicate(){
			public boolean evaluate(Object indentDetail) {
				return ((IndentDetail)indentDetail).getNonSor()!=null;
			}});
	}
		
	public List<ValidationError> validate()	{
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		validationErrors.addAll(validateDetails());
		if(indentType == "Amount")
		{
			if(fund==null){
				validationErrors.add(new ValidationError("fund_null","rateContract.fund.null"));
			}
			if(function==null){
				validationErrors.add(new ValidationError("function_null","rateContract.function.null"));
			}
		
			if(budgetGroup==null){
				validationErrors.add(new ValidationError("validity_null","rateContract.budgetGroup.null"));
			}
			if(validity==null){
				validationErrors.add(new ValidationError("validity_null","rateContract.validity.null"));
			}
		
			if(contractorGrade == null){
				validationErrors.add(new ValidationError("contractorGrade_null","rateContract.contractorGrade.null"));
			}
			if(indentAmount == null){
				validationErrors.add(new ValidationError("amount_null","rateContract.amount.null"));
			}
		}
		
		return validationErrors;
	}
	
	public List<ValidationError> validateDetails() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		for(IndentDetail detail: indentDetails) {
			validationErrors.addAll(detail.validate());
		}
		return validationErrors;
	}
	
	@Override
	public String getNumber(){
		return this.indentNumber;
	}
	
	@Override
	public String getDescription() {
		return getRemarks();
	}
	
	@Override
	public BigDecimal getEstimatedCost(){
		return BigDecimal.ZERO;
	}
	
	@Override
	public TenderableGroupType getTenderableGroupType(){
		return TenderableGroupType.WORKS_RC_INDENT;
	}
	
	@Override
	public Set<Tenderable> getEntities(){
		Set<Tenderable> tenderableFile= new TreeSet<Tenderable>(new IndentDetailComparator());
		for(IndentDetail indentDetail:indentDetails) {
			tenderableFile.add(indentDetail);
			
		}
		return tenderableFile;
	}

	public PersonalInformation getPreparedBy() {
		return preparedBy;
	}

	public void setPreparedBy(PersonalInformation preparedBy) {
		this.preparedBy = preparedBy;
	}
	
	public Long getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(Long documentNumber) {
		this.documentNumber = documentNumber;
	}

	public List<String> getIndentActions() {
		return indentActions;
	}

	public void setIndentActions(List<String> indentActions) {
		this.indentActions = indentActions;
	}
   
	public boolean getIsBudgetCheckRequired() {
		return isBudgetCheckRequired;
	}

	public void setIsBudgetCheckRequired(boolean isBudgetCheckRequired) {
		this.isBudgetCheckRequired = isBudgetCheckRequired;
	}
   
}

