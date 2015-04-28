package org.egov.bpa.utils;

import org.egov.bpa.constants.BpaConstants;

public enum ApprovalType {
	
	RECOMMENDED(BpaConstants.RECOMMENDED),
	NOTRECOMMENDED(BpaConstants.NOTRECOMMENDED);
	final String code;
	ApprovalType(String code) {
		this.code = code;
	}

	ApprovalType() {
		this.code= this.name();
	}
	
	public String getCode() {
		return code;
	}

}
