package org.egov.infstr.models;


public class ECSType  {
	private Long id;
	private String type;
	private String isActive;

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	public String toString() {
		StringBuffer itBuffer = new StringBuffer();
		itBuffer.append("[id=" + id).append(",type=" + type).append(
				",isActive=" + isActive).append("]");
		return itBuffer.toString();
	}

}
