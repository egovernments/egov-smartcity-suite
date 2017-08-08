package org.egov.ptis.bean;

public class BulkBoundaryUpdate {

	private String propertyId;
	private String zone;
	private String locality;
	private String ward;
	private String electionWard;
	private String block;

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getWard() {
		return ward;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

	public String getElectionWard() {
		return electionWard;
	}

	public void setElectionWard(String electionWard) {
		this.electionWard = electionWard;
	}

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

}
