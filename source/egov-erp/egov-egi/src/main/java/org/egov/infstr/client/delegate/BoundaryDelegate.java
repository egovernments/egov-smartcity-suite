/*
 * @(#)BoundaryDelegate.java 3.0, 18 Jun, 2013 1:17:28 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.delegate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.egov.exceptions.DuplicateElementException;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.adminBoundry.BoundryForm;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.admbndry.CityWebsite;
import org.egov.lib.admbndry.CityWebsiteImpl;
import org.egov.lib.admbndry.HeirarchyType;
import org.egov.lib.admbndry.ejb.api.BoundaryService;
import org.egov.lib.admbndry.ejb.api.BoundaryTypeService;
import org.egov.lib.admbndry.ejb.api.CityWebsiteService;
import org.egov.lib.admbndry.ejb.server.BoundaryServiceImpl;
import org.egov.lib.admbndry.ejb.server.BoundaryTypeServiceImpl;
import org.egov.lib.admbndry.ejb.server.CityWebsiteServiceImpl;

final public class BoundaryDelegate {

	private static final BoundaryDelegate BOUNDARY_DELEGATE = new BoundaryDelegate();
	private final CityWebsiteService cityWebsiteService = new CityWebsiteServiceImpl();
	private final BoundaryService boundaryService = new BoundaryServiceImpl();
	private final BoundaryTypeService boundaryTypeService = new BoundaryTypeServiceImpl(null);

	/**
	 * make the constructor private because this is a singleton note that session is cached during constuction saving overhead if subsequent service invocations
	 */
	private BoundaryDelegate() {
	}

	/**
	 * Closure to get the Instance of this Class
	 * @return {@link BoundaryDelegate}
	 */
	public static BoundaryDelegate getInstance() {
		return BOUNDARY_DELEGATE;
	}

	/**
	 * This method creates a Boundary.
	 * @param Boundary object
	 * @exception DuplicateElementException
	 * @exception EGOVException
	 */
	public Boundary createBoundary(final Boundary boundry, final String operation, final HeirarchyType heirarchyType) throws DuplicateElementException, EGOVException {

		validateBoundry(boundry);
		Boundary chB = null;
		Boundary pb = null;

		pb = this.boundaryService.getBoundary(boundry.getParentBoundaryNum(), Short.parseShort(String.valueOf(boundry.getBndryTypeHeirarchyLevel())), heirarchyType, boundry.getTopLevelBoundaryID());
		final BoundaryType pbt = this.boundaryTypeService.getBoundaryType(Short.parseShort(String.valueOf(boundry.getBndryTypeHeirarchyLevel())), heirarchyType);
		if (!operation.equals("create")) {
			chB = new BoundaryImpl(pbt);
			chB.setName(pb.getName());
			chB.setBoundaryNum(pb.getBoundaryNum());
			chB.setFromDate(pb.getFromDate());
			chB.setIsHistory('Y');
			chB.setBndryId(pb.getId());
			chB.setToDate(pb.getToDate());
			chB.setParent(pb.getParent());
			chB.setBndryNameLocal(pb.getBndryNameLocal());
			pb.setName(boundry.getName());
			pb.setBndryNameLocal(boundry.getBndryNameLocal());
			pb.setBoundaryNum(boundry.getBoundaryNum());
			pb.setFromDate(boundry.getFromDate());
			pb.setToDate(boundry.getToDate());
			return this.boundaryService.createBoundary(chB);

		} else {
			Iterator itr = null;
			if (pbt != null && pbt.getChildBoundaryTypes() != null && !pbt.getChildBoundaryTypes().isEmpty()) {
				itr = pbt.getChildBoundaryTypes().iterator();
			}

			if (itr != null && itr.hasNext() && pb != null) {
				chB = new BoundaryImpl((BoundaryType) pbt.getChildBoundaryTypes().iterator().next());
			} else if (pbt != null) {
				chB = new BoundaryImpl(pbt);
			}

			if (chB != null) {
				chB.setParent(pb);
				chB.setName(boundry.getName());
				chB.setBoundaryNum(boundry.getBoundaryNum());
				setMaterializedPath(chB);
				chB.setFromDate(boundry.getFromDate());
				chB.setIsHistory('N');
				if (boundry.getBndryNameLocal() != null) {
					chB.setBndryNameLocal(boundry.getBndryNameLocal());
				}

				if (boundry.getToDate() != null) {
					chB.setToDate(boundry.getToDate());
				}

				return this.boundaryService.createBoundary(chB);
			}
		}

		return null;

	}

	/**
	 * This method Updates already existing Boundary.
	 * @param Boundary object
	 * @exception DuplicateElementException
	 * @exception EGOVException
	 */

	public void updateBoundary(final Boundary boundry) throws EGOVRuntimeException {

		final Boundary bndry = this.boundaryService.getBoundary(boundry.getId());
		bndry.setName(boundry.getName());
		bndry.setBoundaryNum(boundry.getBoundaryNum());
		setMaterializedPath(bndry);
		rebuildMaterializedPath(bndry);
		bndry.setFromDate(boundry.getFromDate());
		if (boundry.getToDate() != null) {
			bndry.setToDate(boundry.getToDate());
		}
		bndry.setBndryNameLocal(boundry.getBndryNameLocal());
		bndry.setUpdatedTime(new Date());
		this.boundaryService.updateBoundary(bndry);

	}

	/**
	 * This method Deletes an already existing Boundary. In the deletion process all the child boundries are also deleted.
	 * @param Boundary object
	 * @exception EGOVRuntimeException
	 * @exception EGOVException
	 */
	public void deleteBoundary(final Boundary boundry, final HeirarchyType heirarchyType) throws EGOVRuntimeException {

		validateBoundry(boundry);

		Boundary bndry, parentBoundry = null;

		bndry = this.boundaryService.getBoundary(boundry.getBoundaryNum(), Short.parseShort(String.valueOf(boundry.getBndryTypeHeirarchyLevel())), heirarchyType, boundry.getTopLevelBoundaryID());
		parentBoundry = bndry.getParent();
		if (parentBoundry != null) {
			parentBoundry.deleteChild(bndry);
		}
		bndry.setParent(null);
		this.boundaryService.removeBoundary(bndry);

	}

	/**
	 * Used to deactivate the boundary for the given BoundaryId
	 * @param boundaryId
	 * @return Boundary
	 * @throws EGOVRuntimeException,CreateException
	 */
	public Boundary deactivateBoundary(final int boundaryId) throws EGOVRuntimeException, Exception {
		final Boundary boundary = this.boundaryService.getBoundary(boundaryId);
		boundary.setIsHistory('Y');
		final Date currentDate = new Date();
		boundary.setUpdatedTime(currentDate);
		boundary.setToDate(currentDate);
		recursiveUpdate(boundary);
		this.boundaryService.updateBoundary(boundary);
		return boundary;
	}

	/**
	 * This is Recursive method to change all the Child Boundary
	 * @param Boundary
	 * @return Boundary throws {@link Exception}
	 */
	private Boundary recursiveUpdate(final Boundary bndry) throws Exception {
		if (bndry.isLeaf()) {
			return bndry;
		}
		final Set<Boundary> childBndrySet = bndry.getChildren();
		for (final Boundary boundary : childBndrySet) {
			boundary.setIsHistory(bndry.getIsHistory());
			boundary.setToDate(bndry.getToDate());
			recursiveUpdate(boundary);
		}

		return bndry;
	}

	/**
	 * Create a City Website for the given Top Boundary
	 * @param boundaryForm
	 * @param boundary
	 * @throws Exception
	 **/
	public void createCityWebsite(final BoundryForm boundaryForm, final Object boundary) throws Exception {
		if (boundaryForm.getCityBaseURL() != null) {
			Boundary bndary = null;
			if (boundary instanceof Integer) {
				bndary = this.boundaryService.getBoundaryById(((Integer) boundary));
			} else if (boundary instanceof Boundary) {
				bndary = (Boundary) boundary;
			}

			for (int i = 0; i < boundaryForm.getCityBaseURL().length; i++) {
				if (boundaryForm.getCityBaseURL()[i] != null && !boundaryForm.getCityBaseURL()[i].trim().equals("")) {
					final CityWebsite cityWebsite = new CityWebsiteImpl();
					cityWebsite.setCityBaseURL(boundaryForm.getCityBaseURL()[i]);
					cityWebsite.setLogo(boundaryForm.getCityLogo());
					cityWebsite.setBoundaryId(bndary);
					cityWebsite.setCityName(boundaryForm.getName());
					if (boundaryForm.getCityNameLocal() != null && !boundaryForm.getCityNameLocal().trim().equals("")) {
						cityWebsite.setCityNameLocal(boundaryForm.getCityNameLocal());
					}
					cityWebsite.setIsActive(boundaryForm.getIsActive());
					this.cityWebsiteService.create(cityWebsite);
				}
			}
		}
	}

	/**
	 * Remove the City Website for the given Top Boundary Id
	 * @param boundaryId
	 * @throws Exception
	 **/
	public void removeCityWebsite(final Integer boundaryId) throws Exception {
		final List cityList = this.cityWebsiteService.getCityWebsite(Integer.valueOf(boundaryId));
		if (cityList.size() != 0) {
			for (final Iterator itr = cityList.iterator(); itr.hasNext();) {
				final CityWebsite cityWebsite = (CityWebsite) itr.next();
				this.cityWebsiteService.remove(cityWebsite);
			}
		}
	}

	/**
	 * This method validates the Boundary Object Before database update
	 * @param boundary
	 * @throws EGOVRuntimeException
	 */
	private void validateBoundry(final Boundary boundary) throws EGOVRuntimeException {
		if (boundary != null) {
			if (boundary.getName() == null || boundary.getName().trim().equals("")) {
				throw new EGOVRuntimeException("Boundry Name is Invalid Please Check !!");
			}
			if (boundary.getBndryTypeHeirarchyLevel() == null || boundary.getBndryTypeHeirarchyLevel().intValue() == 0) {
				throw new EGOVRuntimeException("Internal Server Error: Boundry Heirarchy Level is Invalid Please Check !!");
			}
			if (boundary.getTopLevelBoundaryID() == null || boundary.getTopLevelBoundaryID().intValue() < 0) {
				throw new EGOVRuntimeException("Internal Server Error: Top Level BoundryID is Invalid Please Check !!");
			}
		} else {
			throw new EGOVRuntimeException("Internal Server Error!!");
		}

	}

	private Boundary rebuildMaterializedPath(final Boundary bndry) {
		if (bndry.isLeaf()) {
			return bndry;
		}
		final Set<Boundary> childBndrySet = bndry.getChildren();
		for (final Boundary boundary : childBndrySet) {
			setMaterializedPath(boundary);
			rebuildMaterializedPath(boundary);
		}

		return bndry;
	}

	private void setMaterializedPath(final Boundary boundary) {
		if (boundary.getParent() != null) {
			boundary.setMaterializedPath(boundary.getParent().getMaterializedPath() + "." + boundary.getBoundaryNum());
		} else {
			boundary.setMaterializedPath(String.valueOf(boundary.getBoundaryNum()));
		}
	}

}
