<%@ page import= "java.util.*"%>
<%@page import="org.egov.infstr.services.GeoLocationConstants"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=false">
</script>
<SCRIPT type="text/javascript" src="/egi/javascript/googleMap/spider.js" ></SCRIPT>
<style type="text/css">
  html { height: 100% }
  body { height: 100%; margin: 0; padding: 0 }
  #map_canvas { height: 80%;width:80% }
</style>
 
<%@page import="org.egov.infstr.models.GeoLocation,org.egov.infstr.services.GeoLocationService,java.lang.reflect.Field,
org.egov.infstr.services.GeoLocationConstants" %>
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