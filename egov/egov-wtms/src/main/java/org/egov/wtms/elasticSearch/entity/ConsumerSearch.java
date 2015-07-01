package org.egov.wtms.elasticSearch.entity;

import java.math.BigDecimal;

import org.egov.infra.search.elastic.Indexable;
import org.egov.search.domain.Searchable;

public class ConsumerSearch implements Indexable {

	@Searchable(name="consumercode", group = Searchable.Group.CLAUSES)
	private String consumerCode;
	@Searchable(name="mobilenumber", group = Searchable.Group.CLAUSES)
	private String mobileNumber;
	@Searchable(name="consumername", group = Searchable.Group.SEARCHABLE)
	private String consumerName;
	@Searchable(name="locality", group = Searchable.Group.SEARCHABLE)
	private String locality;
	@Searchable(name="usage", group = Searchable.Group.COMMON)
	private String usageType;
	@Searchable(name="totaldue", group = Searchable.Group.COMMON)
	private BigDecimal totalDue;

	@Override
	public String getIndexId() {
		return this.consumerCode;
	}

	public void setConsumerCode(String consumerCode) {
		this.consumerCode = consumerCode;
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
