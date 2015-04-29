package org.egov.works.models.masters;

import org.egov.infstr.models.BaseModel;

public class ScheduleOfRateType extends BaseModel{
	private String name;
	private String description;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
