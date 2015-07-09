package org.egov.bpa.utils;

import org.egov.bpa.constants.BpaConstants;

public enum ApplicationMode {
	
	GENERAL(BpaConstants.GENERAL),
	GREEENCHANNEL(BpaConstants.GREENCHANNEL);
	
	final  String code;
	ApplicationMode(String code) {
		this.code = code;
	}

	ApplicationMode() {
		this.code= this.name();
	}
	
	public String getCode() {
		return code;
	}

}
