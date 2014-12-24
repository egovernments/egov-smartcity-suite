package org.egov.dcb.bean;

import java.util.List;

public class DCBDisplayInfo {
	
	private List<String> reasonCategoryCodes;
	private List<String> reasonMasterCodes;

	public List<String> getReasonMasterCodes() {
		return reasonMasterCodes;
	}

	public void setReasonMasterCodes(List<String> reasonMasterCodes) {
		this.reasonMasterCodes = reasonMasterCodes;
	}

	public List<String> getReasonCategoryCodes() {
		return reasonCategoryCodes;
	}

	public void setReasonCategoryCodes(List<String> reasonCategoryCodes) {
		this.reasonCategoryCodes = reasonCategoryCodes;
	}
 
	public String toString()
	{
	    return "-getReasonMasterCodes->"+getReasonMasterCodes()+"--getReasonCategoryCodes-->"+getReasonCategoryCodes();
	}
	
	

}
