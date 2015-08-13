package org.egov.ptis.domain.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MasterCodeNamePairDetails implements Serializable {

	private String code;
	private String name;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "MasterCodeNamePairDetails [code=" + code + ", name=" + name + "]";
	}
}
