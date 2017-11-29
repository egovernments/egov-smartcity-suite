/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.infra.gis.service;

import org.apache.commons.lang.StringUtils;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.gis.model.GeoKmlInfo;
import org.egov.infra.gis.model.GeoLocation;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class GeoLocationService {
	

	private static final Logger	LOGGER	= LoggerFactory.getLogger(GeoLocationService.class);
	
	/**
	 * 
	 * @param geoLocation - contains the latitude and longitude , and the info window text to be show on the click event of the marker.  
	 * @return - the html code to display the static text in the marker info window.
	 */
	public static String getMarkerDesc(GeoLocation geoLocation){
		
		StringBuffer markerDesc = new StringBuffer(1000);
		markerDesc.append(null!=geoLocation.getInfo1()?"<tr><td><b>"+geoLocation.getInfo1().substring(0,geoLocation.getInfo1().indexOf("="))+"</b></td><td>"+geoLocation.getInfo1().substring(geoLocation.getInfo1().indexOf("=")+1)+"</td></tr>":"")
				  .append(null!=geoLocation.getInfo2()?"<tr><td><b>"+geoLocation.getInfo2().substring(0,geoLocation.getInfo2().indexOf("="))+"</b></td><td>"+geoLocation.getInfo2().substring(geoLocation.getInfo2().indexOf("=")+1)+"</td></tr>":"")
				  .append(null!=geoLocation.getInfo3()?"<tr><td><b>"+geoLocation.getInfo3().substring(0,geoLocation.getInfo3().indexOf("="))+"</b></td><td>"+geoLocation.getInfo3().substring(geoLocation.getInfo3().indexOf("=")+1)+"</td></tr>":"")
				  .append(null!=geoLocation.getInfo1()?"<tr><td><b>"+geoLocation.getInfo4().substring(0,geoLocation.getInfo4().indexOf("="))+"</b></td><td>"+geoLocation.getInfo4().substring(geoLocation.getInfo4().indexOf("=")+1)+"</td></tr>":"");
		return markerDesc.toString();
		
	}
	/**
	 * 
	 * @param geoLoc - the marker option object , which is going to be passed to the marker constructor 
	 * @return - the marker option javascript object.
	 */
	
	public static String getMarkerOption(GeoLocation geoLoc){
		StringBuffer markerOption = new StringBuffer(1000);
		markerOption.append("{")
		.append("position: new google.maps.LatLng('").append(geoLoc.getGeoLatLong().getLatitude()).append("','")
		.append(geoLoc.getGeoLatLong().getLongitude()).append("'), map: map");
		Map<String, Object>  markerOptData = geoLoc.getMarkerOptionData() ;
		if(null!=markerOptData && markerOptData.size()>0){
			for (Map.Entry<String, Object> entry :  markerOptData.entrySet()) {
				String value = entry.getValue().toString();
			   if(entry.getKey().equalsIgnoreCase(GeoLocationConstants.MARKEROPTION_ICON)){
				   value = "http://www.google.com/mapfiles/ms/icons/"+value+"-dot.png"; 
			   }
			   markerOption.append(",").append(entry.getKey()).append(":'").append(value).append("'"); 
			}
		}
		
		markerOption.append("};");  
		return markerOption.toString();
	}
	/**
	 * puts the kml data model into the map and returns the random key which is used to fetch the exact kmldatamodel from the cache.
	 * @param kmlDataMap - Kml data model map to store into the jboss cache, for the purpose generating the KML file by passing the kml file
	 * and the data model map to the freemarker.
	 * @return
	 */
	private static String  putKmlDataToCache(GeoKmlInfo geoKmlInfo){
		
		Map<String, Object> cacheDataModelMap= new HashMap<String, Object> ();
		String kmlDataModelKey = UUID.randomUUID().toString().substring(0, 10); 
		cacheDataModelMap.put(kmlDataModelKey, geoKmlInfo);
		/*try {
			//TODO CACHE
			//che.put(GeoLocationConstants.KML_DATA_JBOSS_CACHE_NODE,cacheDataModelMap);
		} catch (CacheException e) {
			LOGGER.error(e.getMessage());
		}*/
		return kmlDataModelKey;
		
	}
	/**
	 * 
	 * @param kmlDataModelKey - 
	 * @return 
	 */
	public static GeoKmlInfo getKmlDataFromCache(String kmlDataModelKey){
		GeoKmlInfo geoKmlInfo = null;
		//TODO CACHE
		/*try {
			geoKmlInfo =  (GeoKmlInfo) cache.get(GeoLocationConstants.KML_DATA_JBOSS_CACHE_NODE,kmlDataModelKey);
		} catch (CacheException e) {
			LOGGER.error(e.getMessage());
		}*/
		if(null == geoKmlInfo){
			LOGGER.error("Could not able to retrive kml data  from cache for the key "+kmlDataModelKey);
			throw new ApplicationRuntimeException("Could not able to retrive kml data  from cache for the key "+kmlDataModelKey);
		}
		return geoKmlInfo;  
	}
	
	/**
	 * 
	 * @param wardWiseData Map<String, Integer>- Map having the key as the ward number and value as the no of complaints/properties/assets
	 * 	in that ward. e.g [<Ward Number>,<no Of Complaints/assets/properties> ]
	 * 
	 * @param colorCodes Map<Integer,String> - Map having colour codes , the key as the the colour priority and value as the colour 
	 * code in RGB format. e.g - key = 1 , means the associated colour to represent the ward having  no of complaints/assets/properties
	 * that falls in highest range , key = 2 means associated colour to represent ward having  no of complaints/assets/properties
	 * that falls in the 2nd highest range and so on.
	 * example :  (1, "FF0000");
	 *			  (2, "8968CD");
	 *		      (3, "FFA500");
	 *		      (4, "4169E1");
	 *		      (5, "008B00");
	 */
	
	public static void setKmlDataToCacheAndRequest(Map<String, BigDecimal>  wardWiseData,Map<Integer, String> colorCodes ,String kmlPath, HttpServletRequest request)
	{
		
		LOGGER.debug("GeoLocationService | setKmlDataToCacheAndRequest | Start");
		
		int totalNoOfColors = colorCodes.size();
		BigDecimal wardDataMinAmount = Collections.min(wardWiseData.values()).setScale(0, BigDecimal.ROUND_HALF_UP); // to hold the minimum amount in all the wards.
		BigDecimal wardDataMaxAmount = Collections.max(wardWiseData.values()).setScale(0, BigDecimal.ROUND_HALF_UP); // to hold the maximum amount in all the wards.
		if((wardDataMaxAmount.subtract(wardDataMinAmount)).compareTo(BigDecimal.valueOf(totalNoOfColors))==-1){
			throw new ValidationException(Arrays.asList(new ValidationError("colorrange","no of colors supplied is more than the range of data " +
					"in the wards")));
		}
	
		BigDecimal rangeSize = getRangeSize(wardDataMinAmount,wardDataMaxAmount,totalNoOfColors);
		
		GeoKmlInfo geoKmlInfo = new GeoKmlInfo();
		Map<String, String> wardWiseKmlColorStyle = new HashMap<String, String>();
		
		 for ( Map.Entry<String, BigDecimal> entry : wardWiseData.entrySet()) {
				wardWiseKmlColorStyle.put("style"+entry.getKey(), getStyleColorName(entry.getValue(),wardDataMinAmount,totalNoOfColors,rangeSize));
		 }
		

		geoKmlInfo.setWardWiseColor(wardWiseKmlColorStyle);
		geoKmlInfo.setColorCodes(convertToKmlColor(colorCodes));
		
		request.setAttribute(GeoLocationConstants.KML_DATA_MODEL_JBOSS_CACHE_KEY_NAME , putKmlDataToCache(geoKmlInfo));
		request.setAttribute(GeoLocationConstants.COLOR_CODE_AND_RANGE_MAP_NAME , getColorRange(wardDataMinAmount,wardDataMaxAmount,
																									rangeSize,colorCodes));
		if(null != kmlPath && StringUtils.isNotEmpty(kmlPath)){
			request.setAttribute(GeoLocationConstants.KML_URL_PATH_REQ_ATTR_NAME , kmlPath);
		}
												
		
		LOGGER.debug("GeoLocationService | setKmlDataToCacheAndRequest | End");
		
	}
	
	private static Map<String, String> convertToKmlColor(Map<Integer, String> colorMap){
		
		 Map<String, String> kmlColorConvertedMap = new HashMap<String, String>();
		
		 for ( Map.Entry<Integer, String> entry : colorMap.entrySet()) {
			String color = entry.getValue();
			color ="FF"+color.substring(4,6)+color.substring(2,4)+color.substring(0,2); // FF appended for the opacity level.
			kmlColorConvertedMap.put(GeoLocationConstants.KML_STYLE_COLOR+entry.getKey(), color);
		 }
		 
		 return kmlColorConvertedMap;
	}
	/**
	 * 
	 * @param TotalNos e.g = 50
	 * @param colorMap - the different colours to be shown in the kml.
	 * @return = [0-10,11-20,21-30,31-40,41-50]
	 */
	private static Map<String, String> getColorRange( BigDecimal wardDataMinAmount,BigDecimal wardDataMaxAmount,BigDecimal rangeSize,
																				Map<Integer, String> colorCodes){
		int totalNoOfColors = colorCodes.size();
		
		Map<String, String> colorRangeMap  = new LinkedHashMap<String, String>(); // map to hold the colour code and the range .
		
		BigDecimal rangeStartVal = wardDataMinAmount;
		BigDecimal rangeEndVal = wardDataMinAmount;
		for (int i = 0; i <totalNoOfColors; i++) {
			
			if(totalNoOfColors != i+1){
				rangeEndVal = (rangeStartVal.add(rangeSize)).subtract(BigDecimal.ONE);
			}else{
				rangeEndVal = wardDataMaxAmount;
			}
				
			String colorRange = rangeStartVal +" - "+rangeEndVal;
			colorRangeMap.put(colorCodes.get((totalNoOfColors-i)), colorRange);
			BigDecimal nextRangeStartVal = rangeEndVal.add(BigDecimal.ONE);
			rangeStartVal = nextRangeStartVal;
		
		}
		return colorRangeMap;
	}
	private static String getStyleColorName(BigDecimal wardWiseNos,BigDecimal wardDataMinAmount,Integer totalNoOfColors,BigDecimal rangeSize){
		
		return "#color"+(BigDecimal.valueOf(totalNoOfColors).subtract((wardWiseNos.subtract(wardDataMinAmount).subtract(BigDecimal.ONE)).divide(rangeSize,0,BigDecimal.ROUND_DOWN))); 
		
	}
	private static BigDecimal getRangeSize( BigDecimal wardDataMinAmount,BigDecimal wardDataMaxAmount,int totalNoOfColors){
	 
		BigDecimal rangeSize = (wardDataMaxAmount.subtract(wardDataMinAmount)).divide(BigDecimal.valueOf(totalNoOfColors),BigDecimal.ROUND_HALF_UP);
		return rangeSize;
	}
	
}
