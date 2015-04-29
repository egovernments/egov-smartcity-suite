package org.egov.works.models.tender;

import java.util.Date;
import java.util.List;

import org.egov.works.models.estimate.Activity;

public class EstimateLineItemsForTR {

	public List<TenderResponseQuotes> getTenderResponseQuotes() {
		return tenderResponseQuotes;
	}
	public void setTenderResponseQuotes(
			List<TenderResponseQuotes> tenderResponseQuotes) {
		this.tenderResponseQuotes = tenderResponseQuotes;
	}
	private Integer  srlNo;
	private String code;
	private double quantity;
	private String description;
	private String summary;
	private double rate;
	private String uom;
	private double conversionFactor;
	private double amt;
	private Date estimateDate;
	private double marketRate;
	private Activity activity;
	private double negotiatedRate;
	private List<TenderResponseQuotes> tenderResponseQuotes;
	
	public Integer getSrlNo() {
		return srlNo;
	}
	public void setSrlNo(Integer srlNo) {
		this.srlNo = srlNo;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	public double getConversionFactor() {
		return conversionFactor;
	}
	public void setConversionFactor(double conversionFactor) {
		this.conversionFactor = conversionFactor;
	}
	public double getAmt() {
		return amt;
	}
	public void setAmt(double amt) {
		this.amt = amt;
	}
	public Date getEstimateDate() {
		return estimateDate;
	}
	public void setEstimateDate(Date estimateDate) {
		this.estimateDate = estimateDate;
	}
	public double getMarketRate() {
		return marketRate;
	}
	public void setMarketRate(double marketRate) {
		this.marketRate = marketRate;
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
	
} 