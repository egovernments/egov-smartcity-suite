/*
 * @(#)VisitableBoundary.java 3.0, 16 Jun, 2013 3:48:25 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.admbndry;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VisitableBoundary {

	private static final Logger LOGGER = LoggerFactory.getLogger(VisitableBoundary.class);
	private Boundary bndry = null;
	private VisitableBoundary sBnd = null;

	/**
	 * construct a visitable boundary by passing a boundary
	 */
	public VisitableBoundary(final Boundary boundary) {
		this.bndry = boundary;
	}

	/**
	 * accepts boundary visitors to query the class allow the visitor to visit in the following way - Depth First and breadth later for clarity see the numbers/chars in the tree - the 1 / \ 2 8 / | | \ 3 5 9 B / / \ | | \ 4 6 7 A C D
	 */
	public void accept(final BoundaryVisitor visitor) {
		if (!visitor.visitNeeded(this.getBoundary())) {
			return;
		}

		visitor.visit(this);

		final Iterator iter = this.bndry.getChildren().iterator();

		try {
			while (iter.hasNext()) {
				this.sBnd = new VisitableBoundary((Boundary) iter.next());
				this.sBnd.accept(visitor);
			}
		} catch (final ClassCastException cce) {
			LOGGER.warn("Error occurred while coverting the child boundaries into Boundary");

		} catch (final NullPointerException nule) {
			LOGGER.warn("Children of the currentBoundary seems to be null");
		}
	}

	/**
	 * returns the boundary contained
	 */
	public Boundary getBoundary() {
		return this.bndry;
	}
}
