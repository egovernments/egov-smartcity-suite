package org.egov.works.models.tender;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.egov.infstr.models.BaseModel;
import org.egov.works.models.estimate.Activity;

public class TenderResponseActivity extends BaseModel{
	

	private TenderResponse tenderResponse;
		
	private Activity activity;
	
	//@Required(message="tenderResponseActivity.negotiatedRate.not.null")
	//@GreaterThan(value=0,message="tenderResponseActivity.negotiatedRate.non.negative")
	private double negotiatedRate;
	
	//@Required(message="tenderResponseActivity.negotiatedQuantity.not.null")  
	//@GreaterThan(value=0,message="tenderResponseActivity.negotiatedQuantity.non.negative")
	private double negotiatedQuantity;

	private String schCode;
	private double assignedQty;
	
	private double estimatedQty;
	
	@Valid
	private List<TenderResponseQuotes> tenderResponseQuotes = new LinkedList<TenderResponseQuotes>();

	
	public TenderResponse getTenderResponse() {
		return tenderResponse;
	}

	public void setTenderResponse(TenderResponse tenderResponse) {
		this.tenderResponse = tenderResponse;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public double getNegotiatedRate() {
		return negotiatedRate;
	}

	public void setNegotiatedRate(double negotiatedRate) {
		this.negotiatedRate = negotiatedRate;
	}

	public double getNegotiatedQuantity() {
		return negotiatedQuantity;
	}

	public void setNegotiatedQuantity(double negotiatedQuantity) {
		this.negotiatedQuantity = negotiatedQuantity;
	}

	public String getSchCode() {
		return schCode;
	}

	public void setSchCode(String schCode) {
		this.schCode = schCode;
	}
	
	public List<TenderResponseQuotes> getTenderResponseQuotes() {
		return tenderResponseQuotes;
	}
	
	public Collection<TenderResponseQuotes> getTenderResponseQuotesList(){
		return CollectionUtils.select(tenderResponseQuotes, new Predicate(){
			public boolean evaluate(Object tenderReponseQuote) {
				return ((TenderResponseQuotes)tenderReponseQuote)!=null;
			}});
		
	}

	public void setTenderResponseQuotes(
			List<TenderResponseQuotes> tenderResponseQuotes) {
		this.tenderResponseQuotes = tenderResponseQuotes;
	}
	
	public void addTenderResponseQuotes(TenderResponseQuotes tenderResponseQuotes) {
		this.tenderResponseQuotes.add(tenderResponseQuotes);
	}

	public double getAssignedQty() {
		return assignedQty;
	}

	public void setAssignedQty(double assignedQty) {
		this.assignedQty = assignedQty;
	}
	

	public double getEstimatedQty() {
			return estimatedQty;
	}

	public void setEstimatedQty(double estimatedQty) {
			this.estimatedQty = estimatedQty;
	}
	
}
