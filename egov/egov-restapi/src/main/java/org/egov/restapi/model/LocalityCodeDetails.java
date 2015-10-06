package org.egov.restapi.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LocalityCodeDetails implements Serializable {
	private String localityCode;

	public String getLocalityCode() {
		return localityCode;
	}

	public void setLocalityCode(String localityCode) {
		this.localityCode = localityCode;
	}

	@Override
	public String toString() {
		return "LocalityCodeDetails [localityCode=" + localityCode + "]";
	}

}
