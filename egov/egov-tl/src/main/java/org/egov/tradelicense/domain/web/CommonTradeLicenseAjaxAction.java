package org.egov.tradelicense.domain.web;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletRedirectResult;
import org.apache.struts2.dispatcher.StreamResult;
import org.egov.EGOVRuntimeException;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.tradelicense.utils.LicenseUtils;
import org.egov.web.actions.BaseFormAction;

import com.opensymphony.xwork2.Action;

@Result(
		name = Action.SUCCESS, type = ServletRedirectResult.class, value = "CommonTradeLicenseAjaxAction.action")
@Results({ @Result(
		name = "AJAX_RESULT", type = StreamResult.class, value = "returnStream", params = { "contentType", "text/plain" }) })
@ParentPackage("egov")
public class CommonTradeLicenseAjaxAction extends BaseFormAction {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(CommonTradeLicenseAjaxAction.class);
	protected LicenseUtils licenseUtils;
	private BoundaryDAO boundaryDAO;
	private int zoneId;
	private List<Boundary> divisionList = new LinkedList<Boundary>();
		
	/**
	 * Populate wards.
	 * 
	 * @return the string   
	 */
	public String populateDivisions() {
		try {
			Boundary boundary = boundaryDAO.getBoundary(this.zoneId);
			String cityName = this.licenseUtils.getAllCity().get(0).getName();
			if (!boundary.getName().equals(cityName)) {
				this.divisionList = boundaryDAO.getChildBoundaries(String.valueOf(this.zoneId));
			}
		} catch (final Exception e) {
			     LOGGER.error("populateDivisions() - Error while loading divisions ." + e.getMessage());
			     this.addFieldError("divisions", "Unable to load division information");
			      throw new EGOVRuntimeException("Unable to load division information", e);
		       }
		          return "ward";
	    }
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
     * Convenience method to get the response
     *
     * @return current response
     */

	public HttpServletResponse getServletResponse() {
        return ServletActionContext.getResponse();
	}
	public LicenseUtils getLicenseUtils() {
		return licenseUtils;
	}
	public void setLicenseUtils(LicenseUtils licenseUtils) {
		this.licenseUtils = licenseUtils;
	}
	public BoundaryDAO getBoundaryDAO() {
		return boundaryDAO;
	}
	public void setBoundaryDAO(BoundaryDAO boundaryDAO) {
		this.boundaryDAO = boundaryDAO;
	}
	public int getZoneId() {
		return zoneId;
	}
	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}
	public List<Boundary> getDivisionList() {
		return divisionList;
	}
	public void setDivisionList(List<Boundary> divisionList) {
		this.divisionList = divisionList;
	}

}
