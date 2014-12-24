<%@ page import= "java.util.*"%>
<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=false">
</script>

<style type="text/css">
  html { height: 100% }
  body { height: 100%; margin: 0; padding: 0 }
  #map_canvas { height: 80%;width:80% }
</style>
 

<script type="text/javascript">
var google_map;
var marker = new google.maps.Marker({});  
google.maps.event.addDomListener(window, 'load', initializeMap);
function initializeMap()
{
   var lat = <%=  request.getParameter("latitude") %>;
   var lng = <%=  request.getParameter("longitude") %>;
   google_map = new google.maps.Map(document.getElementById("map_canvas"),{
   zoom: 7,
   center: new google.maps.LatLng(lat, lng),
   mapTypeId: google.maps.MapTypeId.ROADMAP
    });
   
   if(<%=request.getParameter("replaceMarker") %>){
    google.maps.event.addListener(google_map, 'click', function(event) {
       setMarkerData(event.latLng);
       setLatLongHiddenValue(marker.getPosition().lat(),marker.getPosition().lng());
     });
   }else{
   	 setMarkerData(new google.maps.LatLng(lat,lng));
   	 
   }
}
function setMarkerData(location)
{
    marker.setPosition(location);
    marker.setMap(google_map);
}


function setLatLongHiddenValue(lat,lng){

	 document.getElementById('latitude').value = lat;
  	 document.getElementById('longitude').value =lng;
}

</script>