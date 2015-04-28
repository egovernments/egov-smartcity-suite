package org.egov.bpa.utils;

import org.egov.bpa.constants.BpaConstants;

public enum ServiceType {
	
	    NEWBUILDINGONVACANTPLOTCODE(BpaConstants.NEWBUILDINGONVACANTPLOTCODE),
	    APPLICATIONFORDEMOLITIONCODE(BpaConstants.APPLICATIONFORDEMOLITIONCODE),
	    DEMOLITIONRECONSTRUCTIONCODE(BpaConstants.DEMOLITIONRECONSTRUCTIONCODE),
	    SUBDIVISIONOFLANDCODE(BpaConstants.SUBDIVISIONOFLANDCODE),
	    LAYOUTAPPPROVALCODE(BpaConstants.LAYOUTAPPPROVALCODE),
	    ADDITIONALCONSTRUCTIONCODE(BpaConstants.ADDITIONALCONSTRUCTIONCODE),
	    RECLASSIFICATIONCODE(BpaConstants.RECLASSIFICATIONCODE),
	    CMDACODE(BpaConstants.CMDACODE);	
	
		final  String code;
		ServiceType(String code) {
			this.code = code;
		}

		ServiceType() {
			this.code= this.name();
		}
		
		public String getCode() {
			return code;
		}

}
