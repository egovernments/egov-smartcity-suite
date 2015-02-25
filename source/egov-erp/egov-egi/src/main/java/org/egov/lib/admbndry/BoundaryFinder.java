/*
 * @(#)BoundaryFinder.java 3.0, 16 Jun, 2013 3:14:14 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.admbndry;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.HibernateUtil;

public class BoundaryFinder implements BoundaryVisitor {

	private static final Logger LOGGER = LoggerFactory.getLogger(BoundaryVisitor.class);

	private final List resultList = new ArrayList();
	private BoundaryType target = null;
	private Boundary boundary = null;
	private boolean middleBoundariesRequired = false;
	private final Path path = new VisitorPath();

	/**
	 * visits the given VisitableBoundary and gather information
	 */
	@Override
	public void visit(final VisitableBoundary vBoundary) {

		this.boundary = vBoundary.getBoundary();

		try {

			if (this.middleBoundariesRequired) {
				addToResult(this.boundary);
				return;
			}

			if (this.target != null) {
				if (this.boundary.getBoundaryType().equals(this.target)) {
					addToResult(this.boundary);
				}
				return;
			}

			if (this.boundary.isLeaf()) {
				addToResult(this.boundary);
				return;
			}

		} catch (final RuntimeException e) {
			LOGGER.error("Error occurred in visit", e);
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Error occurred in visit", e);
		}
	}

	/**
	 * adds the collected information to the resultList
	 */
	private void addToResult(final Boundary boundary) {
		this.resultList.add(boundary);
	}

	/**
	 * set targetBoundaryType, which this class should look for
	 * @param type
	 */
	public void setTargetBoundaryType(final BoundaryType type) {
		setTargetBoundaryType(type, false);
	}

	/**
	 * same as above, but can specify whether result should include the intermediate boundaries also
	 * @param type
	 * @param needIntermediates
	 */
	public void setTargetBoundaryType(final BoundaryType type, final boolean needIntermediates) {
		this.target = type;
		this.middleBoundariesRequired = needIntermediates;
		if (this.target != null) {
			this.path.buildPathFromChild(this.target);
		}
	}

	/**
	 * whether to visit the given boundary
	 */
	@Override
	public boolean visitNeeded(final Boundary boundary) {
		return this.path.isTraversable(boundary);
	}

	/**
	 * returns the resultList
	 */
	@Override
	public List getResult() {
		return this.resultList;
	}

	/**
	 * returns the path this class contain
	 */
	@Override
	public Path getPath() {
		return this.path;
	}

}
