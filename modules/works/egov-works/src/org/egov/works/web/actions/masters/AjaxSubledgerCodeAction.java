package org.egov.works.web.actions.masters;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.web.actions.BaseFormAction;

public class AjaxSubledgerCodeAction extends BaseFormAction{
	private static final Logger LOGGER = Logger.getLogger(AjaxSubledgerCodeAction.class);
	private List<Boundary> wardList = new LinkedList<Boundary>();
	private Long zoneId;	    // Set by Ajax call
	public static final String WARDS = "wards";
	/**
	 * Populate the ward list by  zone
	 */
	public String populateWard(){
		try{	
			wardList = new BoundaryDAO().getChildBoundaries(String.valueOf(zoneId));
		}catch(Exception e){
			LOGGER.error("Error while loading warda - wards." + e.getMessage());
			addFieldError("location", getText("slCode.wardLoad.failure")); 
			throw new EGOVRuntimeException("Unable to load ward information",e);
		}
		return WARDS;
	}
	public List<Boundary> getwardList() {
		return wardList;
	}
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}

}
