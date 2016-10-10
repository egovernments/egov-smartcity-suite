package org.egov.ptis.bean.dashboard;

import java.util.List;

public class TaxPayerResponseDetails {
	
	private List<TaxPayerDetails> producers;
	private List<TaxPayerDetails> achievers;
	public List<TaxPayerDetails> getProducers() {
		return producers;
	}
	public void setProducers(List<TaxPayerDetails> producers) {
		this.producers = producers;
	}
	public List<TaxPayerDetails> getAchievers() {
		return achievers;
	}
	public void setAchievers(List<TaxPayerDetails> achievers) {
		this.achievers = achievers;
	}
}
