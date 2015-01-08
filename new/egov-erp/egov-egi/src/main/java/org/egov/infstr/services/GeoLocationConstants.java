/*
 * @(#)GeoLocationConstants.java 3.0, 17 Jun, 2013 3:05:20 PM Copyright 2013 eGovernments Foundation. All rights reserved. eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.services;

import java.util.HashMap;
import java.util.Map;

public interface GeoLocationConstants {
	
	public static final String GEOLOCATIONLIST_ATTRIBUTE = "geoLocationList";
	public static final String KML_FILENAME_ATTRIBUTE = "kmlfilename";
	
	public static final String MARKEROPTION_ANIMATION ="animation";
	public static final String MARKEROPTION_CLICKABLE ="clickable";
	public static final String MARKEROPTION_CURSOR ="cursor";
	public static final String MARKEROPTION_DRAGGABLE ="draggable";
	public static final String MARKEROPTION_FLAT ="flat";
	public static final String MARKEROPTION_ICON ="icon";
	public static final String MARKEROPTION_MAP ="map";
	public static final String MARKEROPTION_OPTIMIZED ="optimized";
	public static final String MARKEROPTION_RAISEONDRAG ="raiseOnDrag";
	public static final String MARKEROPTION_SHADOW ="shadow";
	public static final String MARKEROPTION_TITLE ="title";
	public static final String MARKEROPTION_VISIBLE ="visible";
	public static final String MARKEROPTION_ZINDEX ="zIndex";

	
	
	public static final String MARKEROPTION_ICON_RED = "red" ;
	public static final String MARKEROPTION_ICON_ORANGE = "orange" ;
	public static final String MARKEROPTION_ICON_YELLOW = "yellow" ;
	public static final String MARKEROPTION_ICON_BLUE = "blue" ;
	public static final String MARKEROPTION_ICON_GREEN = "green" ;
	public static final String MARKEROPTION_ICON_BLACK = "black";
	
	public static final String PGR_GOOGLE_MAP_URL_REDIRECT_PATH = "/pgr/staff/greComplaintHistory.jsp?ComplaintNumber=";
	
	public static final String PGR_GOOGLE_MAP_URL_DISPLAY = "/pgr/citizen/xmlRequest!getGeoDisplayUrl.action?ComplaintNumber=";
	
	public static final String BASE_KML_CLASS_PATH_FILE_NAME = "base.kml";
	
	public static final String KML_URL_PATH_REQ_ATTR_NAME = "kmlurlpath";
	
	public static final String KML_FREEMARKER_PROPERRIES_FILENAME = "wardcolor.properties"; 
	
	public static final String KML_DATA_MODEL_SESSION_NAME = "kmldatamodel"; 
	
	public static final String KML_DATA_MODEL_JBOSS_CACHE_KEY_NAME = "kmlDataModelKey"; 
	
	public static final String KML_DATA_JBOSS_CACHE_NODE ="GMAP/"+"kmlMapData";
	
	public static final String KML_STYLE_COLOR = "color";
	
	
	public static final String COLOR_CODE_AND_RANGE_MAP_NAME = "colorCodeAndRange";
	
	/**
	 * 
	 */
	
	public static final Map<Integer, String> COLORCODES = new HashMap<Integer, String>() {
		{
			put(1, "FF0000");
			put(2, "8968CD");
			put(3, "FFA500");
			put(4, "4169E1");
			put(5, "008B00");
		}
	};


}
