package org.egov.tradelicense.domain.entity;

import org.egov.infstr.models.BaseModel;

public class FeeType extends BaseModel {
	private static final long serialVersionUID = 1L;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("FeeType={");
		str.append("name=").append(name == null ? "null" : name.toString());
		str.append("}");
		return str.toString();
	}
}
