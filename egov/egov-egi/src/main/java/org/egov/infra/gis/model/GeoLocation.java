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

package org.egov.infra.gis.model;

import java.util.Map;

public class GeoLocation {

	public static final String INFO5SEPERATOR="~";

	private GeoLatLong geoLatLong;

	private String info1;

	private String info2;

	private String info3;

	private String info4;
	// contains key=values seperated by INFO5SEPERATOR[ tilde(~)]
	//eg: "Contractor Name=Ramaswamy,Address=123 street,Total Amount=250000" 
	private String info5;
	private String urlRedirect;

	private String urlDisplay;

	// Meta data information for the marker option
	private Map<String, Object> markerOptionData;

	private Boolean useNiceInfoWindow=false;
    private String styleClass;
	
	public String getStyleClass() {
		return styleClass;
	}
	public void setStyleClass(String style) {
		this.styleClass = style;
	}
	public String getInfo5() {
		return info5;
	}
	/**
	 * use appendToInfor5(String strToAppend) method to avoid errors
	 * @param info5
	 */
	@Deprecated 
	public void setInfo5(String info5) {
		this.info5 = info5;
	}

	
	public void setUseNiceInfoWindow(Boolean useNiceInfoWindow) {
		this.useNiceInfoWindow = useNiceInfoWindow;
	}

	public GeoLatLong getGeoLatLong() {
		return geoLatLong;
	}

	public String getInfo1() {
		return info1;
	}

	public String getInfo2() {
		return info2;
	}

	public String getInfo3() {
		return info3;
	}

	public String getInfo4() {
		return info4;
	}

	public String getUrlRedirect() {
		return urlRedirect;
	}

	public String getUrlDisplay() {
		return urlDisplay;
	}

	public void setGeoLatLong(GeoLatLong geoLatLong) {
		this.geoLatLong = geoLatLong;
	}

	public void setInfo1(String info1) {
		this.info1 = info1;
	}

	public void setInfo2(String info2) {
		this.info2 = info2;
	}

	public void setInfo3(String info3) {
		this.info3 = info3;
	}

	public void setInfo4(String info4) {
		this.info4 = info4;
	}

	public void setUrlRedirect(String urlRedirect) {
		this.urlRedirect = urlRedirect;
	}

	public void setUrlDisplay(String urlDisplay) {
		this.urlDisplay = urlDisplay;
	}

	public Map<String, Object> getMarkerOptionData() {
		return markerOptionData;
	}

	public void setMarkerOptionData(Map<String, Object> markerOptionData) {
		this.markerOptionData = markerOptionData;
	}
	//use atleast a char on right side of = 
	public void appendToInfo5(String strToAppend)
	{
		if(null==this.info5||this.info5.isEmpty())
		{
			this.info5=strToAppend;
		}else
		{
			this.info5=this.info5+INFO5SEPERATOR+strToAppend;
		}
	}
	

	public Boolean getUseNiceInfoWindow() {
		
		return useNiceInfoWindow;
	}	

}
