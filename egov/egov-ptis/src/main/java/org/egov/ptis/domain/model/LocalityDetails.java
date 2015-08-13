package org.egov.ptis.domain.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LocalityDetails implements Serializable {
	private String zoneName;
	private String wardName;
	private String blockName;

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public String getBlockName() {
		return blockName;
	}

	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}

	@Override
	public String toString() {
		return "LocalityDetails [zoneName=" + zoneName + ", wardName=" + wardName + ", blockName=" + blockName + "]";
	}
}
