package org.egov.works.models.tender;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;

import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infstr.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.egov.works.utils.DateConversionUtil;
import org.egov.works.utils.WorksConstants;
import org.hibernate.validator.constraints.Length;

public class TenderHeader extends BaseModel{
	
	@Required(message="tenderHeader.tenderNo.null")
	@Length(max=50,message="tenderHeader.tenderNo.length")
	@OptionalPattern(regex=WorksConstants.alphaNumericwithspecialchar,message="tenderHeader.tenderNo.alphaNumeric")
	private String tenderNo;
	 
	@Required(message="tenderHeader.tenderDate.null")
	@DateFormat(message="invalid.fieldvalue.tenderDate") 
	private Date tenderDate;
	
	@Valid
	private List<TenderEstimate> tenderEstimates = new LinkedList<TenderEstimate>();

	public String getTenderNo() {
		return tenderNo;
	}

	public void setTenderNo(String tenderNo) {
		this.tenderNo = tenderNo;
	}

	public Date getTenderDate() {
		return tenderDate;
	}

	public void setTenderDate(Date tenderDate) {
		this.tenderDate = tenderDate;
	}

	public List<TenderEstimate> getTenderEstimates() {
		return tenderEstimates;
	}

	public void setTenderEstimates(List<TenderEstimate> tenderEstimates) {
		this.tenderEstimates = tenderEstimates;
	}
	
	public void addTenderEstimate(TenderEstimate tenderEstimate) {
		this.tenderEstimates.add(tenderEstimate);
	}
	public List<ValidationError> validate()	{
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		for (TenderEstimate tenderEstimate : tenderEstimates) {
			if(tenderDate!=null){
				if(tenderEstimate.getAbstractEstimate()!=null && DateConversionUtil.isBeforeByDate(tenderDate,tenderEstimate.getAbstractEstimate().getEstimateDate())){
					validationErrors.add(new ValidationError("tenderDate", "tenderEstimate.tenderDate.cannot_greaterthan_estimateDate"));
				}
				else if(tenderEstimate.getWorksPackage()!=null && DateConversionUtil.isBeforeByDate(tenderDate,tenderEstimate.getWorksPackage().getPackageDate())){
					validationErrors.add(new ValidationError("tenderDate", "tenderEstimate.tenderDate.cannot_greaterthan_packageDate"));
				}
			}
		}
		return validationErrors;
	}
}
