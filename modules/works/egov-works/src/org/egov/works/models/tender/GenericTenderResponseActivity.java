package org.egov.works.models.tender;

import org.egov.tender.model.GenericTenderResponse;
import org.egov.tender.model.TenderResponseLine;
import org.egov.works.models.estimate.Activity;

public class GenericTenderResponseActivity {
	
	private GenericTenderResponse genericTenderResponse;
	private Activity activity;
	private TenderResponseLine tenderResponseLine;
	private double negotiatedRate;
	private double negotiatedQuantity;
	private String schCode;
	private double assignedQty;
	private double estimatedQty;
	
	public GenericTenderResponse getGenericTenderResponse() {
		return genericTenderResponse;
	}
	public void setGenericTenderResponse(GenericTenderResponse genericTenderResponse) {
		this.genericTenderResponse = genericTenderResponse;
	}
	public Activity getActivity() {
		return activity;
	}
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	public TenderResponseLine getTenderResponseLine() {
		return tenderResponseLine;
	}
	public void setTenderResponseLine(TenderResponseLine tenderResponseLine) {
		this.tenderResponseLine = tenderResponseLine;
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
