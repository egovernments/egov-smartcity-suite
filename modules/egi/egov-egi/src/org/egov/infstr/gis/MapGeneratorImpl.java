/*
 * @(#)MapGeneratorImpl.java 3.0, 17 Jun, 2013 2:38:07 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.gis;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.gis.mapreporting.GeoMap;
import org.egov.gis.mapreporting.data.DataItem;
import org.egov.gis.mapreporting.data.DataItemImpl;
import org.egov.gis.mapreporting.data.LayerDataset;
import org.egov.gis.mapreporting.data.LayerDatasetImpl;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.admbndry.ejb.server.BoundaryServiceImpl;

public abstract class MapGeneratorImpl implements MapGenerator {
	protected int[] ids;
	protected int[] vals;
	private static Map BoundaryID_numMap = new HashMap();
	// ServiceLocator serviceloc;
	protected String jboss_home = System.getProperty("JBOSS_HOME");
	protected String gispathString = EGovConfig.getProperty("GIS_PATH_STRING", "", "PT");
	protected String appCtx = this.jboss_home + this.gispathString;
	protected String shapePath;
	protected String imagePath;
	protected String legendPath;
	protected String roadPath;
	protected String riverPath;
	protected String assemblyPath;
	protected String blockPath;
	protected String villagePath;
	protected GeoMap map = null;
	protected File file = null;

	@Override
	public void setPath(final short hlevel, final Integer bndryID) {
	};

	/**
	 * This Method convertToBoundaryNumber() converts the BoundaryId to BoundaryNumber The map passed is a HashMap with key as BoundaryId
	 * @param idMap
	 * @return HashMap
	 */
	@Override
	public Map convertToBoundaryNumber(final Map idMap) {

		Map retMap = null;
		try {
			if (idMap == null) {
				return null;
			}
			retMap = new HashMap();
			final Set keySet = idMap.keySet();

			for (final Iterator iter = keySet.iterator(); iter.hasNext();) {
				final Integer element = (Integer) iter.next();
				if (BoundaryID_numMap.containsKey(element)) {
					retMap.put(BoundaryID_numMap.get(element), idMap.get(element));
				} else {
					final Boundary boundary = new BoundaryServiceImpl().getBoundary(element);
					BoundaryID_numMap.put(element, boundary.getBoundaryNum());
					retMap.put(BoundaryID_numMap.get(element), idMap.get(element));
				}

			}
		} catch (final Exception e) {
			e.printStackTrace();
			HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception occured -----> " + e.getMessage());
		}

		return retMap;
	}

	@Override
	public void generateMap(final Boundary bndry, final BoundaryType bndryType, final int topLevelBoundaryID) {
	}

	@Override
	public void createMap() {
	};

	/**
	 * This method buildDataset() is used to add the labels on top of the map. We can format the labels using setLabel()
	 */
	@Override
	public LayerDataset buildDataset() {
		final LayerDataset lyrDataset = new LayerDatasetImpl();
		DataItem dItem = null;

		for (int i = 0; i < this.ids.length; i++) {
			dItem = new DataItemImpl();
			dItem.setKey(new Integer(this.ids[i]));
			dItem.setValue(new Integer(this.vals[i]));
			dItem.setLabel("(" + dItem.getValue().toString() + ")");
			lyrDataset.add(dItem);
		}
		return lyrDataset;
	}

	@Override
	public java.util.Map getMap() {
		return null;
	}

}
