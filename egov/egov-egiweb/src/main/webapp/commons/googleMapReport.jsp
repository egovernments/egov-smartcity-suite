<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>
<%@ page import= "org.egov.infra.gis.model.GeoLocation"%>
<%@page import="org.egov.infra.gis.service.GeoLocationConstants"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=false">
</script>
<SCRIPT type="text/javascript" src="/egi/javascript/googleMap/spider.js" ></SCRIPT>
<style type="text/css">
  html { height: 100% }
  body { height: 100%; margin: 0; padding: 0 }
  #map_canvas { height: 80%;width:80% }
</style>
 
<%@page import="org.egov.infra.gis.service.GeoLocationService,java.util.List" %>
<script type="text/javascript"><!--
var allmarkers = []; 
var marker;
var infowindow = new google.maps.InfoWindow();

  //set map options
    var myOptions = {
      zoom: 7,
      center: new google.maps.LatLng(0, 0),
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    //create map object 
    var map = new google.maps.Map(document.getElementById("map_canvas"),myOptions);
    // create bounds to set the center of the map
    var bounds = new google.maps.LatLngBounds();
     <% if(null!= request.getAttribute(GeoLocationConstants.KML_FILENAME_ATTRIBUTE)){%>
         var wardlayerUrl = 'http://118.102.160.138/mapguide/nmc/kmlexample/' +'<%=request.getAttribute(GeoLocationConstants.KML_FILENAME_ATTRIBUTE).toString()%>'+'.kml';
    	 var wardLayer = new google.maps.KmlLayer(wardlayerUrl);
    	 wardLayer.setMap(map);
    <% }%>
initialize();
  function initialize() {
 <%
    List<GeoLocation> geoLocationList = (List<GeoLocation>)request.getAttribute(GeoLocationConstants.GEOLOCATIONLIST_ATTRIBUTE);
   
  %>
   var markerDescDisplay;

   <% for (GeoLocation geoLoc : geoLocationList) {
   		StringBuffer markerDesc = new StringBuffer("<table width=\"245\" border=\"1\"  cellpadding=\"2\" cellspacing=\"0\" bordercolor=\"#CCCCCC\">");
   		markerDesc.append(GeoLocationService.getMarkerDesc(geoLoc));%>
		var  markerInfoDesc = '<%=markerDesc.toString()%>';
		
		
    <% if(null!=geoLoc.getUrlDisplay()){ %>
			
			markerInfoDesc = markerInfoDesc + getDisplayUrlInfo('<%=geoLoc.getUrlDisplay()%>');
	<%}%>
	<% if(null!=geoLoc.getUrlRedirect()){%>
		markerInfoDesc = markerInfoDesc + setRedirectUrl('<%=geoLoc.getUrlRedirect()%>');
	<% } %>
	 markerInfoDesc = markerInfoDesc + '<tr><td></td><td></td></tr></table>';
     	 var markerOption = <%=GeoLocationService.getMarkerOption(geoLoc)%>;
    	 marker = new google.maps.Marker(markerOption); 
         marker.desc = markerInfoDesc;
        // setting latlong value to the bounds to fix the center of the map
        bounds.extend(new google.maps.LatLng(<%=geoLoc.getGeoLatLong().getLatitude().toString()%>, <%=geoLoc.getGeoLatLong().getLongitude().toString() %>));
        map.fitBounds(bounds);
	google.maps.event.addListener(marker, 'click', function () {
	infowindow.setContent(this.desc);
	infowindow.open(map, this);
	});
	allmarkers.push(marker);
  <% } %>
   


  }// end of function

function setRedirectUrl(url){
	return  '<tr><td><b>View Details</b></td><td>'+'<a href="javascript:setUrl('+"'"+url+"'"+');">'+'Click </a>'+'</td></tr>'; 
}
function setUrl(link)
{
	win1 =window.open(link,"Streetidwindow",'scrollbars=yes,resizable=yes,height=600,width=775,status=yes');
}
function getDisplayUrlInfo(displayUrl) {
	var request = initiateRequest();
	request.open("GET", displayUrl, false);
	request.send(null);
	if (request.status == 200) {
		return request.responseText;
	}
}
function showMarker(index){

   google.maps.event.trigger(allmarkers[index], "click");
  
}
--></script>
