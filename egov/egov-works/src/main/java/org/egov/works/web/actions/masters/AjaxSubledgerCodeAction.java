package org.egov.works.web.actions.masters;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.estimate.ProjectCode;
import org.springframework.beans.factory.annotation.Autowired;

public class AjaxSubledgerCodeAction extends BaseFormAction{
	private static final Logger LOGGER = Logger.getLogger(AjaxSubledgerCodeAction.class);
	private List<Boundary> wardList = new LinkedList<Boundary>();
	private Long zoneId;	    // Set by Ajax call
	public static final String WARDS = "wards";
	private String query = "wards";
	private List<ProjectCode> projectCodeList=new LinkedList<ProjectCode>();
	@Autowired
	private BoundaryDAO boundaryDAO;
	/**
	 * Populate the ward list by  zone
	 */
	public String populateWard(){
		try{	
			wardList = boundaryDAO.getChildBoundaries(String.valueOf(zoneId));
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
	
	private void populateProjectCodeList(){
		String strquery="";
		ArrayList<Object> params=new ArrayList<Object>();
		strquery="from ProjectCode pc where upper(pc.code) like '%'||?||'%'"+" and pc.egwStatus.code=? and pc.id in (select mbh.workOrderEstimate.estimate.projectCode.id from MBHeader mbh left outer join mbh.egBillregister egbr where egbr.status.code=? and egbr.billtype=? and mbh.workOrderEstimate.estimate.depositCode is null )";
		params.add(query.toUpperCase());
		params.add("CREATED");
		params.add("APPROVED");
		params.add("Final Bill");
		projectCodeList=getPersistenceService().findAllBy(strquery,params.toArray());
	}

	public String searchProjectCode(){
		populateProjectCodeList();
		return "projectCodeSearchResults";
	}
	
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}
	public void setQuery(String query) {
		this.query = query;
	}

	public List<ProjectCode> getProjectCodeList() {
		return projectCodeList;
	}
}
