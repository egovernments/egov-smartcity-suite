package org.egov.ptis.domain.model;

public enum DrainageEnum {
	Yes("true"), No("false");
	String flag;
	DrainageEnum(String flag) {
		this.flag = flag;
	}
	public String getCode() {
		return flag;
	}
}
