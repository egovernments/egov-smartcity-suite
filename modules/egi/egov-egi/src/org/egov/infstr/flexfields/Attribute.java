package org.egov.infstr.flexfields;

public class Attribute {

	int id;
	int applDomainId;
	int attributeTypeId;
	String attributeTypeValue;
	int domainTxnId;

	public int getDomainTxnId() {
		return this.domainTxnId;
	}

	public void setDomainTxnId(final int domainTxnId) {
		this.domainTxnId = domainTxnId;
	}

	public int getAttributeTypeId() {
		return this.attributeTypeId;
	}

	public void setAttributeTypeId(final int attributeTypeId) {
		this.attributeTypeId = attributeTypeId;
	}

	public String getAttributeTypeValue() {
		return this.attributeTypeValue;
	}

	public void setAttributeTypeValue(final String attributeTypeValue) {
		this.attributeTypeValue = attributeTypeValue;
	}

	public int getApplDomainId() {
		return this.applDomainId;
	}

	public void setApplDomainId(final int domainId) {
		this.applDomainId = domainId;
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

}
