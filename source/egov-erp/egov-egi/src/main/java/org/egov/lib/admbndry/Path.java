/*
 * @(#)Path.java 3.0, 16 Jun, 2013 3:47:31 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.admbndry;

public interface Path {
	/**
	 * whether path exist on not
	 */
	boolean pathExists();

	/**
	 * build a Path based on source and target boundary types
	 * @param superBoundaryType
	 * @param childBoundaryType
	 */
	void buildPath(BoundaryType source, BoundaryType target);

	void buildPathFromChild(BoundaryType child);

	/**
	 * checks whether the passed boundary is anywhere inside the path of traversal
	 */
	boolean isTraversable(Boundary boundary);
}
