package org.egov.works.models.estimate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.commons.Fundsource;
import org.egov.infstr.ValidationError;
import org.egov.infstr.models.BaseModel;
import javax.validation.constraints.Min;

/**
 * This class represents the entity EGW_FINANCINGSOURCE
 * 
 * @author Divya
 * @version 1.0, July 2, 2009
 */
public class FinancingSource extends BaseModel {
	
	public FinancingSource(){
	}

	private Long id;
	
	@Min(value=0,message="financingsource.percentage.not.negative")
	private double percentage;
	
	private Fundsource fundSource;
	private FinancialDetail financialDetail;
	
	private Date lastModifiedDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public Fundsource getFundSource() {
		return fundSource;
	}

	public void setFundSource(Fundsource fundSource) {
		this.fundSource = fundSource;
	}

	public FinancialDetail getFinancialDetail() {
		return financialDetail;
	}

	public void setFinancialDetail(FinancialDetail financialDetail) {
		this.financialDetail = financialDetail;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
	public List<ValidationError> validate(){
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		
		if(fundSource==null || fundSource.getCode()==null) {
			validationErrors.add(new ValidationError("invalidpercentage","financingsource.fundsource.null"));
		}
			
		if(percentage <= 0.0 || percentage > 100){
			validationErrors.add(new ValidationError("invalidpercentage","financingsource.invalid.percentage"));
		}
		return validationErrors;
	}
}
