package org.egov.works.models.tender;

import java.util.ArrayList;
import java.util.List;

import org.egov.infstr.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.egov.works.models.rateContract.IndentDetail;


public class TenderFileIndentDetail extends BaseModel{
		
	private TenderFileDetail tenderFileDetail;
	
	private IndentDetail indentDetail;
		
	public TenderFileDetail getTenderFileDetail() {
		return tenderFileDetail;
	}

	public void setTenderFileDetail(TenderFileDetail tenderFileDetail) {
		this.tenderFileDetail = tenderFileDetail;
	}

	public IndentDetail getIndentDetail() {
		return indentDetail;
	}

	public void setIndentDetail(IndentDetail indentDetail) {
		this.indentDetail = indentDetail;
	}

	public List<ValidationError> validate() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>(); 
		return validationErrors;		
	}

}
