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
<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=false">
</script>

<style type="text/css">
    html {
        height: 100%
    }

    body {
        height: 100%;
        margin: 0;
        padding: 0
    }

    #map_canvas {
        height: 80%;
        width: 80%
    }
</style>


<script type="text/javascript">
    var google_map;
    var marker = new google.maps.Marker({});
    google.maps.event.addDomListener(window, 'load', initializeMap);
    function initializeMap() {
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
