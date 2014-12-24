package org.egov.works.models.workorder;

public class AbstractEstimateForWp {
	private Integer  srlNo;
	private String aeWorkNameForWp;
	private double negotiatedAmtForWp;
	public Integer getSrlNo() {
		return srlNo;
	}
	public void setSrlNo(Integer srlNo) {
		this.srlNo = srlNo;
	}
	public String getAeWorkNameForWp() {
		return aeWorkNameForWp;
	}
	public void setAeWorkNameForWp(String aeWorkNameForWp) {
		this.aeWorkNameForWp = aeWorkNameForWp;
	}
	public double getNegotiatedAmtForWp() {
		return negotiatedAmtForWp;
	}
	public void setNegotiatedAmtForWp(double negotiatedAmtForWp) {
		this.negotiatedAmtForWp = negotiatedAmtForWp;
	}

}
