package org.egov.wtms.elasticSearch.entity;

import java.math.BigDecimal;

import org.egov.infra.search.elastic.Indexable;
import org.egov.search.domain.Searchable;

public class ConsumerSearch implements Indexable {

	@Searchable(name="zone", group = Searchable.Group.CLAUSES)
	private String zone;
	@Searchable(name="ward", group = Searchable.Group.CLAUSES)
	private String ward;
	@Searchable(name="consumercode", group = Searchable.Group.CLAUSES)
	private String consumerCode;
	@Searchable(name="propertyid", group = Searchable.Group.CLAUSES)
	private String propertyId;
	@Searchable(name="bpaid", group = Searchable.Group.CLAUSES)
	private String bpaId;
	@Searchable(name="mobilenumber", group = Searchable.Group.CLAUSES)
	private String mobileNumber;
	@Searchable(name="consumername", group = Searchable.Group.SEARCHABLE)
	private String consumerName;
	@Searchable(name="locality", group = Searchable.Group.SEARCHABLE)
	private String locality;
	@Searchable(name="usage", group = Searchable.Group.CLAUSES)
	private String usageType;
	@Searchable(name="totaldue", group = Searchable.Group.CLAUSES)
	private BigDecimal totalDue;

	@Override
	public String getIndexId() {
		return this.consumerCode;
	}
	
	public void setZone(String zone) {
		this.zone = zone;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}
	
	public void setConsumerCode(String consumerCode) {
		this.consumerCode = consumerCode;
	}
	
	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}
	
	public void setBpaId(String bpaId) {
		this.bpaId = bpaId;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public void setConsumerName(String consumerName) {
		this.consumerName = consumerName;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public void setUsageType(String usageType) {
		this.usageType = usageType;
	}

	public void setTotalDue(BigDecimal totalDue) {
		this.totalDue = totalDue;
	}

}
