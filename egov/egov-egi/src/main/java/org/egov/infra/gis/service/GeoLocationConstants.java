/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.infra.gis.service;

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
	public static final String MARKEROPTION_ICON_PURPLE = "purple";
	public static final String MARKEROPTION_ICON_PINK = "pink";
	
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
