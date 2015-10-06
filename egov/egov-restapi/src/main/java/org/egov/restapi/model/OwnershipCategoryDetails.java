package org.egov.restapi.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class OwnershipCategoryDetails implements Serializable {

	private String ownershipCategoryCode;

	public String getOwnershipCategoryCode() {
		return ownershipCategoryCode;
	}

	public void setOwnershipCategoryCode(String ownershipCategoryCode) {
		this.ownershipCategoryCode = ownershipCategoryCode;
	}

	@Override
	public String toString() {
		return "OwnershipCategoryDetails [ownershipCategoryCode=" + ownershipCategoryCode + "]";
	}

}
