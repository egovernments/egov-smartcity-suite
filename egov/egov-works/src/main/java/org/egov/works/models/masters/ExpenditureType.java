package org.egov.works.models.masters;

public class ExpenditureType{
	private String value;
	private ExpenditureType() {}
	public ExpenditureType(String value) {
		this.value=value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
