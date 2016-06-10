package org.egov.lcms.masters.entity;

import org.egov.infstr.models.BaseModel;

public class CaseStage extends BaseModel {
	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 1L;

	private String stage;

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

}
