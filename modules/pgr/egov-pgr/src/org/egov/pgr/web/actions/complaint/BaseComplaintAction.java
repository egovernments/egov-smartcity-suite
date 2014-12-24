/*
 * @(#)BaseComplaintAction.java 3.0, 23 Jul, 2013 3:29:43 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.web.actions.complaint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.exceptions.NoSuchObjectException;
import org.egov.exceptions.TooManyValuesException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.GeoLocation;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.admbndry.HeirarchyType;
import org.egov.lib.admbndry.ejb.api.BoundaryService;
import org.egov.lib.admbndry.ejb.api.BoundaryTypeService;
import org.egov.lib.admbndry.ejb.api.HeirarchyTypeService;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.user.User;
import org.egov.pgr.constant.PGRConstants;
import org.egov.pgr.domain.entities.CompReceivingModes;
import org.egov.pgr.domain.entities.ComplaintDetails;
import org.egov.pgr.domain.entities.ComplaintReceivingCenter;
import org.egov.pgr.domain.entities.ComplaintStatus;
import org.egov.pgr.domain.entities.ComplaintTypes;
import org.egov.pgr.domain.entities.Priority;
import org.egov.pgr.domain.entities.RedressalDetails;
import org.egov.pgr.domain.services.CompReceivingCenterService;
import org.egov.pgr.domain.services.CompReceivingModesService;
import org.egov.pgr.domain.services.ComplaintDetailService;
import org.egov.pgr.domain.services.ComplaintGroupService;
import org.egov.pgr.domain.services.ComplaintStatusService;
import org.egov.pgr.domain.services.ComplaintTypeService;
import org.egov.pgr.domain.services.RedressalDetailsService;
import org.egov.pgr.utils.PgrCommonUtils;
import org.egov.pims.commons.service.EisCommonsService;
import org.egov.web.actions.BaseFormAction;

public class BaseComplaintAction extends BaseFormAction {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(BaseComplaintAction.class);
	protected ComplaintDetails complaintDetails = new ComplaintDetails();
	protected CompReceivingCenterService compReceivingCenterService;
	protected ComplaintTypeService complaintTypeService;
	protected RedressalDetailsService redressalService;
	protected ComplaintDetailService complaintService;
	protected CompReceivingModesService compReceivingModesService;
	protected ComplaintGroupService complaintGroupService;
	protected List<String> boundaryFields = new ArrayList<String>();
	protected BoundaryService boundaryService;
	protected GenericHibernateDaoFactory genericHibDao;
	protected ComplaintStatusService complaintStatusService;
	protected HeirarchyTypeService heirarchyTypeService;
	protected BoundaryTypeService boundaryTypeService;
	protected EisCommonsService eisCommonsService;
	protected PgrCommonUtils pgrCommonUtils;

	public BaseComplaintAction() {

		this.complaintDetails.setRedressal(new RedressalDetails());
		addRelatedEntity("receivingCenter", ComplaintReceivingCenter.class);
		addRelatedEntity("complaintType", ComplaintTypes.class);
		addRelatedEntity("compReceivingModes", CompReceivingModes.class);
		addRelatedEntity("geoLocationDetails", GeoLocation.class);
		addRelatedEntity("redressal.complaintStatus", ComplaintStatus.class);
		addRelatedEntity("department", DepartmentImpl.class);
		addRelatedEntity("redressal.redressalOfficer", User.class);
		addRelatedEntity("priority", Priority.class);
	}

	@Override
	public Object getModel() {
		return this.complaintDetails;
	}

	protected void throwValidationException(final String key, final String value) {
		LOGGER.error(value);
		throw new ValidationException(Arrays.asList(new ValidationError(key, value)));
	}

	public Boolean shouldShowBoundatyField(final String boundarTypeName) {

		return this.boundaryFields.contains(boundarTypeName);
	}

	protected Boundary getBoundry(final int boundaryId) {

		final Boundary boundary = this.boundaryService.getBoundary(boundaryId);

		final AppConfigValues appConfigValue = this.genericHibDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(PGRConstants.MODULE_NAME, "HTYPENAME").get(0);
		if (PGRConstants.HIERARCHY_TYPE_ADMINISTRATION.equals(appConfigValue.getValue())) {
			if (null != boundary) {
				try {
					final HeirarchyType hType = this.heirarchyTypeService.getHierarchyTypeByName(appConfigValue.getValue());
					final BoundaryType bType = this.boundaryTypeService.getBoundaryType("Ward", hType);
					final Set<Boundary> adBndrySet = this.boundaryService.getCrossHeirarchyChildren(boundary, bType);
					if (null != adBndrySet && adBndrySet.size() == 1) {
						for (final Boundary boundary2 : adBndrySet) {
							if (null != boundary2) {
								return boundary2;
							}

						}
					}
				} catch (final TooManyValuesException e) {
					LOGGER.error(e.getMessage());
				} catch (final NoSuchObjectException e) {
					LOGGER.error(e.getMessage());
				}

			}
		}

		return boundary;

	}

	public void setCompReceivingCenterService(final CompReceivingCenterService compReceivingCenterService) {
		this.compReceivingCenterService = compReceivingCenterService;
	}

	public void setComplaintTypeService(final ComplaintTypeService complaintTypeService) {
		this.complaintTypeService = complaintTypeService;
	}

	public void setRedressalService(final RedressalDetailsService redressalService) {
		this.redressalService = redressalService;
	}

	public void setComplaintService(final ComplaintDetailService complaintService) {
		this.complaintService = complaintService;
	}

	public void setCompReceivingModesService(final CompReceivingModesService compReceivingModesService) {
		this.compReceivingModesService = compReceivingModesService;
	}

	public List<String> getBoundaryFields() {
		return this.boundaryFields;
	}

	public void setBoundaryFields(final List<String> boundaryFields) {
		this.boundaryFields = boundaryFields;
	}

	/**
	 * @param complaintGroupService the complaintGroupService to set
	 */
	public void setComplaintGroupService(final ComplaintGroupService complaintGroupService) {
		this.complaintGroupService = complaintGroupService;
	}

	/**
	 * @param boundaryService the boundaryService to set
	 */
	public void setBoundaryService(final BoundaryService boundaryService) {
		this.boundaryService = boundaryService;
	}

	/**
	 * @param genericHibDao the genericHibDao to set
	 */
	public void setGenericHibDao(final GenericHibernateDaoFactory genericHibDao) {
		this.genericHibDao = genericHibDao;
	}

	public void setComplaintStatusService(final ComplaintStatusService complaintStatusService) {
		this.complaintStatusService = complaintStatusService;
	}

	/**
	 * @param heirarchyTypeService the heirarchyTypeService to set
	 */
	public void setHeirarchyTypeService(final HeirarchyTypeService heirarchyTypeService) {
		this.heirarchyTypeService = heirarchyTypeService;
	}

	/**
	 * @param boundaryTypeService the boundaryTypeService to set
	 */
	public void setBoundaryTypeService(final BoundaryTypeService boundaryTypeService) {
		this.boundaryTypeService = boundaryTypeService;
	}

	/**
	 * @param eisCommonsService the eisCommonsService to set
	 */
	public void setEisCommonsService(final EisCommonsService eisCommonsService) {
		this.eisCommonsService = eisCommonsService;
	}

	public void setComplaintDetails(final ComplaintDetails complaintDetails) {
		this.complaintDetails = complaintDetails;
	}

	public void setPgrCommonUtils(final PgrCommonUtils pgrCommonUtils) {
		this.pgrCommonUtils = pgrCommonUtils;
	}

}
