package org.egov.ptis.bean;

import org.egov.infra.web.support.search.DataTableSearchRequest;

public class BulkBoundaryRequest extends DataTableSearchRequest {

	private String assessmentNumber;
	private String doorNumber;
	private String zone;
	private String locality;
	private String ward;
	private String electionWard;
	private String block;
	private Boolean active = Boolean.TRUE;

	public String getAssessmentNumber() {
		return assessmentNumber;
	}

	public void setAssessmentNumber(String assessmentNumber) {
		this.assessmentNumber = assessmentNumber;
	}

	public String getDoorNumber() {
		return doorNumber;
	}

	public void setDoorNumber(String doorNumber) {
		this.doorNumber = doorNumber;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

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

}
