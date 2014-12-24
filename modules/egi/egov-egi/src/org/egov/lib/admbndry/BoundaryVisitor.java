/*
 * @(#)BoundaryVisitor.java 3.0, 16 Jun, 2013 3:29:19 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.admbndry;

import java.util.List;

public interface BoundaryVisitor {
	/**
	 * visit & collect the information about boundary
	 */
	void visit(VisitableBoundary vBoundary);

	/**
	 * whether to visit a particulary boundary
	 */
	boolean visitNeeded(Boundary boundary);

	/**
	 * returns the Path
	 */
	Path getPath();

	/**
	 * returns the result of visit as List
	 */
	List getResult();
}
