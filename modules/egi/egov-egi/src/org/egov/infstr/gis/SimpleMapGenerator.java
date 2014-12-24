/*
 * @(#)SimpleMapGenerator.java 3.0, 17 Jun, 2013 2:41:36 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.gis;

import java.awt.Dimension;
import java.io.File;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.commons.service.CommonsService;
import org.egov.commons.service.CommonsServiceImpl;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.gis.mapreporting.GeoLayer;
import org.egov.gis.mapreporting.GeoMapImpl;
import org.egov.gis.mapreporting.ShapeFileReader;
import org.egov.gis.mapreporting.data.DataItem;
import org.egov.gis.mapreporting.data.DataItemImpl;
import org.egov.gis.mapreporting.data.LayerDataset;
import org.egov.gis.mapreporting.data.LayerDatasetImpl;
import org.egov.gis.mapreporting.rendering.ImageWritter;
import org.egov.gis.mapreporting.rendering.RenderingTarget;
import org.egov.gis.mapreporting.style.LayerShading;
import org.egov.gis.mapreporting.style.MonoShader;
import org.egov.infstr.utils.EGovConfig;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryType;

public class SimpleMapGenerator extends MapGeneratorImpl {
	java.util.Map imgMap;
	java.util.Map unitMap = null;
	java.util.List bndrySet = null;
	ShapeFileReader shpReader;
	String bndryTypeName = null;
	LayerShading lyrShading;
	private static final Logger logger = LoggerFactory.getLogger(SimpleMapGenerator.class);
	private static SimpleMapGenerator instance = new SimpleMapGenerator();

	public SimpleMapGenerator() {
		super();
	}

	public static SimpleMapGenerator getInstance() {
		return instance;
	}

	@Override
	public void setPath(final short hlevel, final Integer bndryID) {

		this.shapePath = this.appCtx + EGovConfig.getProperty("SHAPE_PATH", "", "PT") + hlevel + "/" + bndryID + ".shp";
		this.file = new File(this.shapePath);
		if (!this.file.exists()) {
			logger.info(">>>>>File does not Exist");
		} else {
			this.imagePath = this.appCtx + EGovConfig.getProperty("SIMMAP_IMG_PATH", "", "PT") + hlevel + "/" + bndryID + ".png";
			this.legendPath = this.appCtx + EGovConfig.getProperty("SIMMAP_IMG_PATH", "", "PT") + hlevel + "/legend" + bndryID + ".png";
		}

	}

	@Override
	public void generateMap(final Boundary bndry, final BoundaryType bndryType, final int topLevelBoundaryID) {
		try {

			final short hlevel = bndryType.getHeirarchy();
			setPath(hlevel, bndry.getId());
			if (this.file.exists()) {
				createMap(bndry.getId());
			}
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Exception occured -----> " + e.getMessage());
		}
	}

	public void createMap(final Integer bndryID) {
		try {
			this.shpReader = new ShapeFileReader();
			this.shpReader.setPath(this.shapePath);

			final GeoLayer layer = this.shpReader.createLayer();
			layer.setKeyField(EGovConfig.getProperty("ID_STRING", "", "PT"));
			layer.setLabelField(EGovConfig.getProperty("NAME_STRING", "", "PT"));

			this.lyrShading = new MonoShader();
			this.lyrShading.setColorSchema(new String[] { "#DEF3BD" });

			layer.getStyleManager().setShader(this.lyrShading);
			layer.setActive(true);
			this.map = new GeoMapImpl(bndryID);
			this.map.addLayer(layer);

			// Dimension size = new Dimension(450,400);
			// int width = 450;
			// int height = 400;
			final CommonsService commonsService = new CommonsServiceImpl();

			final Map widthMap = commonsService.getWidth(bndryID);
			final Map heightMap = commonsService.getHeight(bndryID);

			final int width = ((Integer) widthMap.get(bndryID)).intValue();
			final int height = ((Integer) heightMap.get(bndryID)).intValue();

			final Dimension size = new Dimension(width, height);
			final RenderingTarget imageTarget = new ImageWritter(this.imagePath, this.legendPath, size);
			this.map.setRenderTarget(imageTarget);
			this.map.draw();
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Exception occured -----> " + e.getMessage());
		}
	}

	@Override
	public LayerDataset buildDataset() {
		final LayerDataset lyrDataset = new LayerDatasetImpl();
		DataItem dItem = null;

		for (int i = 0; i < this.ids.length; i++) {
			dItem = new DataItemImpl();
			dItem.setKey(new Integer(this.ids[i]));
			dItem.setValue(new Integer(this.vals[i]));
			lyrDataset.add(dItem);
		}
		return lyrDataset;
	}

}
