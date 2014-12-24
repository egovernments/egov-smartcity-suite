<%@ page import= "java.util.*"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="org.egov.infstr.services.GeoLocationConstants, org.egov.infstr.utils.AppConfigTagUtil"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=false">
</script>
<style type="text/css">
  html { height: 100% }
  body { height: 100%; margin: 0; padding: 0 }
  #map_canvas { height: 80%;width:80% }
</style>
 
<script type="text/javascript">


   var latlng = new google.maps.LatLng('<%= AppConfigTagUtil.getAppConfigValue("CITY_LATITUDE","egi",this.getServletContext()) %>'
, '<%= AppConfigTagUtil.getAppConfigValue("CITY_LONGITUDE","egi",this.getServletContext()) %>');
    var myOptions = {
      zoom: 8,
      center: latlng,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    //create map object 
    var map = new google.maps.Map(document.getElementById("map_canvas"),myOptions);
    
      <%

   				  String url = request.getRequestURL().toString();
					String uri = request.getRequestURI().toString(); String serverPath = url;
					if(url != null && url.indexOf(uri) != -1){
						serverPath = url.substring(0, url.indexOf(uri));
					}
			
     %>
      var wardlayerUrl;
     <% if(null != request.getAttribute(GeoLocationConstants.KML_DATA_MODEL_JBOSS_CACHE_KEY_NAME)){%>
      wardlayerUrl = '<%=serverPath%>'+'/egi/common/geo/geoLocation!getKMLStream.action?'+
     					'kmlDataModelKey='+'<%=request.getAttribute(GeoLocationConstants.KML_DATA_MODEL_JBOSS_CACHE_KEY_NAME).toString()%>';
    <%} %>
    
     <% if(null != request.getAttribute(GeoLocationConstants.KML_URL_PATH_REQ_ATTR_NAME)){ %>
    		
    		wardlayerUrl = wardlayerUrl+'&kmlUrlPath='+'<%=request.getAttribute(GeoLocationConstants.KML_URL_PATH_REQ_ATTR_NAME).toString()%>';
    <%} %>
   
    
     
	//var wardlayerUrl = "http://118.102.160.137/pgr/img/2_green_nmc_wards.kml";
	
    var wardLayer = new google.maps.KmlLayer(wardlayerUrl);
	wardLayer.setMap(map);
	 // Create the legend and display on the map
  	var legendDiv = document.createElement('DIV');
  	var legend = new Legend(legendDiv, map);
  	legendDiv.index = 1;
  	map.controls[google.maps.ControlPosition.RIGHT_BOTTOM].push(legendDiv);
  
  


function Legend(controlDiv, map) {
  // Set CSS styles for the DIV containing the control
  // Setting padding to 5 px will offset the control
  // from the edge of the map
  controlDiv.style.padding = '5px';

  // Set CSS for the control border
  var controlUI = document.createElement('DIV');
  controlUI.style.backgroundColor = 'white';
  controlUI.style.borderStyle = 'solid';
  controlUI.style.borderWidth = '1px';
  controlUI.title = 'Legend';
  controlDiv.appendChild(controlUI);

  controlUI.appendChild(colorDiv());
}


function colorDiv(){

var colors=new Array(); 

colors.push('#FF0000');
colors.push('#8968CD');
colors.push('#FFA500');
colors.push('#4169E1');
colors.push('#008B00');

var table = document.createElement('table');	
<% if(null != request.getAttribute(GeoLocationConstants.COLOR_CODE_AND_RANGE_MAP_NAME)){

Map<String, String> colorRangeMap = (Map<String, String>)request.getAttribute(GeoLocationConstants.COLOR_CODE_AND_RANGE_MAP_NAME);
 for ( Map.Entry<String, String> entry : colorRangeMap.entrySet()) { %>
 
 		var tr  = document.createElement('tr');
		var td1  = document.createElement('td');
		
		var dvClr = document.createElement('div');
		dvClr.style.width = "50px";
		dvClr.style.height = "10px";
		dvClr.style.border = "1px";
		dvClr.style.solid = "#000";
		dvClr.style.backgroundColor='#'+'<%= entry.getKey()%>';
	
		var dvText = document.createElement('div');
		dvText.innerHTML = '<%= entry.getValue()%>';
		dvText.align = 'center';
		td1.appendChild(dvClr);
		var td2  = document.createElement('td');
		
		td2.appendChild(dvText);
		tr.appendChild(td1);
		tr.appendChild(td2);
		table.appendChild(tr);
 	
<%} }%>

    document.body.appendChild(table);
    return table;
}
</script>

