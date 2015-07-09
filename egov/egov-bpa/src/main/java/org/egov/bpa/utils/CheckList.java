package org.egov.bpa.utils;

import org.egov.bpa.constants.BpaConstants;

public enum CheckList {
	
	DOCUMENTATION(BpaConstants.DOCUMENTATION),
	LP(BpaConstants.LP),
	UNCONSIDERED(BpaConstants.UNCONSIDERED);
	
	final  String code;

	CheckList(String code) {
		this.code = code;
	}

	CheckList() {
		this.code= this.name();
	}
	
	public String getCode() {
		return code;
	}
	

}
