package org.egov.works.models.masters;

import java.math.BigDecimal;


public class ContractorQuote {
   
	private Contractor contractor;
	private BigDecimal percentage;
	
	public Contractor getContractor() {
		return contractor;
	}
	public void setContractor(Contractor contractor) {
		this.contractor = contractor;
	}
	public BigDecimal getPercentage() {
		return percentage;
	}
	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}
	
}
