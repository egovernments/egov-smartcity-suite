package org.egov.tradelicense.domain.web;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletRedirectResult;
import org.apache.struts2.dispatcher.StreamResult;
import org.egov.EGOVRuntimeException;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.service.EisManager;
import org.egov.tradelicense.utils.LicenseUtils;
import org.egov.web.actions.BaseFormAction;

import com.opensymphony.xwork2.Action;

@Result(
		name = Action.SUCCESS, type = ServletRedirectResult.class, value = "CommonAjaxAction.action")
@Results({ @Result(
		name = "AJAX_RESULT", type = StreamResult.class, value = "returnStream", params = { "contentType", "text/plain" }) })
@ParentPackage("egov")
public class CommonAjaxAction extends BaseFormAction {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(CommonAjaxAction.class);
	public static final String LOCATIONS = "locations";
	public static final String STREETS = "streets";
	public static final String DIVISIONS = "divisions";
	public static final String AREAS = "areas";
	private static final String AREA = "area";
	private static final String LOCATION = "location";

	// these are Set by Ajax call
	private int divisionId;
	private int areaId;
	private int locationId;
	private int zoneId;
	private List<Boundary> locationList = new LinkedList<Boundary>();
	private List<Boundary> areaList = new LinkedList<Boundary>();
	private List<Boundary> streetList = new LinkedList<Boundary>();
	private List<Boundary> divisionList = new LinkedList<Boundary>();
	private String returnStream = "";
	private EisManager eisManager;
	private List<DesignationMaster> designationList;
	private Integer departmentId;
	private Integer designationId;
	private List<UserImpl> allActiveUsersByGivenDesg;
	protected LicenseUtils licenseUtils;
	private BoundaryDAO boundaryDAO;
	

	public InputStream getReturnStream() {
		final ByteArrayInputStream is = new ByteArrayInputStream(this.returnStream.getBytes());
		return is;
	}

	public Object getModel() {
		return null;
	}

	public String populateLocations() {
		try {
			this.locationList = boundaryDAO.getChildBoundaries(String.valueOf(this.areaId));
			final StringBuilder result = new StringBuilder();
			for (final Boundary boundary : this.locationList) {
				result.append("Text:").append(boundary.getName()).append("Value:").append(boundary.getId()).append("\n");
			}
			this.returnStream = result.toString();
		} catch (final Exception e) {
			LOGGER.error("populateLocations() - Error while loading locations." + e.getMessage());
			this.addFieldError(CommonAjaxAction.LOCATION, "Unable to load location information");
			throw new EGOVRuntimeException("Unable to load location information", e);
		}
		return "AJAX_RESULT";
	}

	/**
	 * Populate streets.
	 * 
	 * @return the string
	 */
	public String populateStreets() {
		try {
			this.streetList = boundaryDAO.getChildBoundaries(String.valueOf(this.locationId));
		} catch (final Exception e) {
			LOGGER.error("populateStreets() - Error while loading streets.", e);
			this.addFieldError(CommonAjaxAction.LOCATION, "Unable to load street information");
			throw new EGOVRuntimeException("Unable to load street information", e);
		}
		return CommonAjaxAction.STREETS;
	}

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
			     this.addFieldError(CommonAjaxAction.LOCATION, "Unable to load division information");
			      throw new EGOVRuntimeException("Unable to load division information", e);
		       }
		          return "ward";
	    }

	public String ajaxPopulateDesignationsByDept() {
		try {

			this.designationList = this.eisManager.getAllDesignationByDept(this.departmentId);
		} catch (final Exception e) {
			LOGGER.error("populateDesignationsByDept() - Error while loading divisions ." + e.getMessage());
			this.addFieldError(CommonAjaxAction.LOCATION, "Unable to load Designation information");
			throw new EGOVRuntimeException("Unable to load Designation information", e);
		}
		return "designation";
	}

	@SuppressWarnings("unchecked")
	public String ajaxPopulateUsersByDesignation() {
		try {

			this.allActiveUsersByGivenDesg = this.eisManager.getAllActiveUsersByGivenDesg(this.designationId);
		} catch (final Exception e) {
			LOGGER.error("populateUsersByDept() - Error while loading divisions ." + e.getMessage());
			this.addFieldError(CommonAjaxAction.LOCATION, "Unable to load User information");
			throw new EGOVRuntimeException("Unable to load User information", e);
		}
		return "users";
	}

	public List<UserImpl> getAllActiveUsersByGivenDesg() {
		return this.allActiveUsersByGivenDesg;
	}

	public void setAllActiveUsersByGivenDesg(final List<UserImpl> allActiveUsersByGivenDesg) {
		this.allActiveUsersByGivenDesg = allActiveUsersByGivenDesg;
	}

	public int getDivisionId() {
		return this.divisionId;
	}

	public void setDivisionId(final int divisionId) {
		this.divisionId = divisionId;
	}

	public int getAreaId() {
		return this.areaId;
	}

	public void setAreaId(final int areaId) {
		this.areaId = areaId;
	}

	public int getLocationId() {
		return this.locationId;
	}

	public void setLocationId(final int locationId) {
		this.locationId = locationId;
	}

	public int getZoneId() {
		return this.zoneId;
	}

	public void setZoneId(final int zoneId) {
		this.zoneId = zoneId;
	}

	public List<Boundary> getLocationList() {
		return this.locationList;
	}

	public void setLocationList(final List<Boundary> locationList) {
		this.locationList = locationList;
	}

	public List<DesignationMaster> getDesignationList() {
		return this.designationList;
	}

	public void setDesignationList(final List<DesignationMaster> designationList) {
		this.designationList = designationList;
	}

	public Integer getDepartmentId() {
		return this.departmentId;
	}

	public void setDepartmentId(final Integer departmentId) {
		this.departmentId = departmentId;
	}

	public List<Boundary> getAreaList() {
		return this.areaList;
	}

	public void setAreaList(final List<Boundary> areaList) {
		this.areaList = areaList;
	}

	public List<Boundary> getStreetList() {
		return this.streetList;
	}

	public void setStreetList(final List<Boundary> streetList) {
		this.streetList = streetList;
	}

	public List<Boundary> getDivisionList() {
		return this.divisionList;
	}

	public void setDivisionList(final List<Boundary> divisionList) {
		this.divisionList = divisionList;
	}

	public static String getAREA() {
		return CommonAjaxAction.AREA;
	}

	public void setEisManager(final EisManager eisManager) {
		this.eisManager = eisManager;
	}

	public Integer getDesignationId() {
		return this.designationId;
	}

	public void setDesignationId(final Integer designationId) {
		this.designationId = designationId;
	}

	public void setBoundaryDAO(BoundaryDAO boundaryDAO) {
		this.boundaryDAO = boundaryDAO;
	}
	
	public void setLicenseUtils(final LicenseUtils licenseUtils) {
		this.licenseUtils = licenseUtils;
	}

}
