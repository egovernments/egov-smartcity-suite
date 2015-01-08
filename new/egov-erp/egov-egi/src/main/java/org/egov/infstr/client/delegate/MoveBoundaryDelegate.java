/*
 * @(#)MoveBoundaryDelegate.java 3.0, 18 Jun, 2013 1:24:42 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.delegate;

import java.util.Date;
import java.util.List;

import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;

public class MoveBoundaryDelegate {

	private BoundaryDAO boundaryDAO; // Data Access Object for Boundary

	/**
	 * Used to get all the Child Boundary from a given Parent Boundary ID
	 * @param parentBoundaryId
	 * @return {@link List<Boundary>}
	 * @throws Exception
	 **/
	public List<Boundary> getChildBoundaries(final String parentBoundaryId) throws Exception {
		return this.boundaryDAO.getChildBoundaries(parentBoundaryId);
	}

	/**
	 * Used to get all the Parent Boundary from a given Child Boundary ID
	 * @param childBoundaryId
	 * @return {@link List<Boundary>}
	 * @throws Exception
	 **/
	public List<Boundary> getAllParentBoundaries(final String childBoundaryId) throws Exception {
		return this.boundaryDAO.getAllParentBoundaries(childBoundaryId);
	}

	/**
	 * Used to move a Child Boundary from its Parent to another Parent Boundary
	 * @param childBoundaryId
	 * @param parentBoundaryId
	 * @return {@link Boundary}
	 * @throws Exception
	 **/
	public Boundary moveBoundary(final String childBoundaryId, final String parentBoundaryId) throws Exception {
		final Boundary boundary = this.boundaryDAO.getBoundary(Integer.parseInt(childBoundaryId));
		final Boundary parentBoundary = this.boundaryDAO.getBoundary(Integer.parseInt(parentBoundaryId));
		boundary.setParent(parentBoundary);
		boundary.setUpdatedTime(new Date());
		this.boundaryDAO.updateBoundary(boundary);
		return boundary;
	}

	/**
	 * BoundaryDAO to set
	 * @param boundaryDAO
	 **/
	public void setBoundaryDAO(final BoundaryDAO boundaryDAO) {
		this.boundaryDAO = boundaryDAO;
	}
}
