/*
 * @(#)RegisterComplaintAction.java 3.0, 23 Jul, 2013 3:29:44 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.web.actions.complaint;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.pgr.constant.PGRConstants;
import org.egov.pgr.domain.entities.CompReceivingModes;
import org.egov.pgr.domain.entities.ComplaintDetails;
import org.egov.pgr.domain.entities.ComplaintGroup;
import org.egov.pgr.domain.entities.ComplaintReceivingCenter;
import org.egov.pgr.domain.entities.ComplaintTypes;
import org.egov.web.annotation.ValidationErrorPage;

@ParentPackage("egov")
@Results({ @Result(name = "view", type = "redirect", location = "complaint!view.action", params = { "model.id", "${model.id}" }) })
public class RegisterComplaintAction extends BaseComplaintAction {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(RegisterComplaintAction.class);
	private List<CompReceivingModes> modeList;
	private List<ComplaintReceivingCenter> receivingCenterList;
	private Integer zone;
	private Integer ward;
	private Integer area;
	private Integer street;
	private Map<String, List<ComplaintTypes>> compGroupTypeMap;
	private Long defaultMode;
	protected WorkflowService<ComplaintDetails> complaintWorkflowService;

	@Override
	public void prepare() {
		super.prepare();
		LOGGER.debug("RegisterComplaintAction | prepare | Start");
		this.modeList = this.compReceivingModesService.getAllCompReceivingModes();

		for (final CompReceivingModes mode : this.modeList) {
			if (mode.getCompMode().equalsIgnoreCase("INTERNET")) {
				this.defaultMode = mode.getId();
				break;
			}
		}

		this.receivingCenterList = this.compReceivingCenterService.getAllComplaintReceivingCenter();
		addDropdownData("receivingCenter", this.receivingCenterList);
		addDropdownData("zoneList", this.pgrCommonUtils.getAllZoneOfHTypeAdmin());
		addDropdownData("wardList", Collections.EMPTY_LIST);
		addDropdownData("areaList", Collections.EMPTY_LIST);
		addDropdownData("streetList", Collections.EMPTY_LIST);
		addDropdownData("top5Complaints", this.complaintTypeService.getTop5Complaints());
		getComplaintTypesByGroup();
		this.boundaryFields.add("zone");
		this.boundaryFields.add("ward");
		this.boundaryFields.add("area");
		this.boundaryFields.add("street");

		if (null != this.parameters.get("complaintType")) {
			final String[] complaintTypes = this.parameters.get("complaintType");
			for (int i = 0; i < complaintTypes.length; i++) {
				if (!complaintTypes[i].equalsIgnoreCase("-1")) {
					this.complaintDetails.setComplaintType(this.complaintTypeService.findById(Long.valueOf(complaintTypes[i]), false));
				}
			}
		}
	}

	@Override
	public Object getModel() {

		return super.getModel();
	}

	@SkipValidation
	public String newform() {

		return "new";
	}

	@ValidationErrorPage(value = "new")
	public String saveAndView() {
		LOGGER.debug("RegisterComplaintAction | saveAndView | start" + this.complaintDetails);
		loadPreviousData();
		create();
		return "view";
	}

	private void create() {

		final Integer boundary = this.street != null && this.street != -1 ? this.street : this.area != null && this.area != -1 ? this.area : this.ward != null && this.ward != -1 ? this.ward : this.zone != null && this.zone != -1 ? this.zone : null;
		if (null != boundary) {
			this.complaintDetails.setLocBndry(this.boundaryService.getBoundary(boundary.intValue()));
			this.complaintDetails.setBoundary(getBoundry(boundary));
		}
		this.complaintDetails = this.complaintService.createNewComplaint(this.complaintDetails);
		this.complaintDetails = this.redressalService.createNewRedressal(this.complaintDetails);

		this.complaintDetails = this.complaintService.persist(this.complaintDetails);

		if (null != this.complaintDetails.getGeoLocationDetails().getGeoLatLong().getLatitude() && null != this.complaintDetails.getGeoLocationDetails().getGeoLatLong().getLongitude()) {

			this.complaintDetails.getGeoLocationDetails().setUrlDisplay(PGRConstants.PGR_GOOGLE_MAP_URL_DISPLAY + this.complaintDetails.getId());
			this.complaintDetails.getGeoLocationDetails().setUrlRedirect(PGRConstants.PGR_GOOGLE_MAP_URL_REDIRECT_PATH + this.complaintDetails.getId());
			this.complaintService.update(this.complaintDetails);
		}

		this.complaintWorkflowService.start(this.complaintDetails, this.complaintDetails.getRedressal().getPosition());
		this.complaintDetails.changeState(this.complaintDetails.getRedressal().getComplaintStatus().getName(), "", this.complaintDetails.getRedressal().getPosition(), this.complaintDetails.getTitle());
	}

	private void getComplaintTypesByGroup() {
		this.compGroupTypeMap = new HashMap<String, List<ComplaintTypes>>();
		final List<ComplaintGroup> complaintGroupList = this.complaintGroupService.findAll();
		for (final ComplaintGroup complaintGroup : complaintGroupList) {
			this.compGroupTypeMap.put(complaintGroup.getComplaintGroupName(), this.complaintTypeService.findAllByNamedQuery("getAllCompTypeByGroup", complaintGroup.getId()));
		}
	}

	protected void loadPreviousData() {
		LOGGER.debug("Loading ajax data");
		try {
			final AppConfigValues appConfigValue = this.genericHibDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(PGRConstants.MODULE_NAME, "LOCATIONHTYPE").get(0);
			if (null != this.zone && this.zone != -1) {
				addDropdownData("wardList", this.pgrCommonUtils.populateWard(this.zone));
			}
			if (null != this.ward && this.ward != -1) {
				addDropdownData("streetList", this.pgrCommonUtils.populateStreets(appConfigValue.getValue(), this.ward));
				addDropdownData("areaList", this.pgrCommonUtils.populateStreets(appConfigValue.getValue(), this.ward));

			}
		} catch (final RuntimeException e) {
			LOGGER.warn(" Unable to load boundary" + e.getMessage());
		}

	}

	public List<CompReceivingModes> getModeList() {
		return this.modeList;
	}

	public Integer getZone() {
		return this.zone;
	}

	public void setZone(final Integer zone) {
		this.zone = zone;
	}

	public Integer getWard() {
		return this.ward;
	}

	public void setWard(final Integer ward) {
		this.ward = ward;
	}

	public Integer getArea() {
		return this.area;
	}

	public void setArea(final Integer area) {
		this.area = area;
	}

	public Integer getStreet() {
		return this.street;
	}

	public void setStreet(final Integer street) {
		this.street = street;
	}

	/**
	 * @return the compGroupTypeMap
	 */
	public Map<String, List<ComplaintTypes>> getCompGroupTypeMap() {
		return this.compGroupTypeMap;
	}

	/**
	 * @param compGroupTypeMap the compGroupTypeMap to set
	 */
	public void setCompGroupTypeMap(final Map<String, List<ComplaintTypes>> compGroupTypeMap) {
		this.compGroupTypeMap = compGroupTypeMap;
	}

	/**
	 * @return the defaultMode
	 */
	public Long getDefaultMode() {
		return this.defaultMode;
	}

	/**
	 * @param defaultMode the defaultMode to set
	 */
	public void setDefaultMode(final Long defaultMode) {
		this.defaultMode = defaultMode;
	}

	/**
	 * @param complaintWorkflowService the complaintWorkflowService to set
	 */
	public void setComplaintWorkflowService(final WorkflowService<ComplaintDetails> complaintWorkflowService) {
		this.complaintWorkflowService = complaintWorkflowService;
	}

}
