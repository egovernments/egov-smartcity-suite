/*
 * @(#)MapGenerator.java 3.0, 17 Jun, 2013 2:35:43 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.gis;

import java.util.Map;

import org.egov.gis.mapreporting.data.LayerDataset;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryType;

public interface MapGenerator {

	public void setPath(short hlevel, Integer bndryID);

	public Map convertToBoundaryNumber(Map idMap);

	public void generateMap(Boundary bndry, BoundaryType bndryType, int topLevelBoundaryID);

	public void createMap();

	public LayerDataset buildDataset();

	public java.util.Map getMap();

}
