package org.egov.works.models.rateContract;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import org.egov.infstr.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.Money;

public class RateContractDetail extends BaseModel {
	
	private RateContract rateContract;
	
	private IndentDetail indentDetail;
	
	private Money rcRate;
	
	@Transient
	private String sorNumber;

	public RateContract getRateContract() {
		return rateContract;
	}

	public void setRateContract(RateContract rateContract) {
		this.rateContract = rateContract;
	}

	public IndentDetail getIndentDetail() {
		return indentDetail;
	}

	public void setIndentDetail(IndentDetail indentDetail) {
		this.indentDetail = indentDetail;
	}

	public Money getRcRate() {
		return rcRate;
	}

	public void setRcRate(Money rcRate) {
		this.rcRate = rcRate;
	}
	
	public String getSorNumber() {
		return sorNumber;
	}

	public void setSorNumber(String sorNumber) {
		this.sorNumber = sorNumber;
	}

	public List<ValidationError> validate() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>(); 
		return validationErrors;		
	}
}
