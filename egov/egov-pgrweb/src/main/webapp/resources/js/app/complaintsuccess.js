/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */

$(document).ready(function(){
	
	var myCenter;
	var map, geocoder, geolocate, marker;
	if (lat != '0.0') {
		$.ajax({
	        type: "POST",
	        url: 'https://maps.googleapis.com/maps/api/geocode/json?key='+googleapikey+'&latlng='+lat+','+lng,
	        dataType: 'json',
	        success : function(data){
	            $('#address_locate').html(JSON.stringify(data.results[0].formatted_address))  
	        }
		});
	}
	$('#complaint-locate').on('show.bs.modal', function() {
		//Must wait until the render of the modal appear, thats why we use the resizeMap and NOT resizingMap!! ;-)
		
		//complaint view(update) map
		$('#show_address_in_map').html($('#address_locate').html());
		myCenter=new google.maps.LatLng(lat, lng);
		initialize();
		resizeMap();
	});
	
	function resizeMap() {
		if(typeof map =="undefined") return;
		setTimeout( function(){resizingMap();} , 400);
	}
	
	function resizingMap() {
		if(typeof map =="undefined") return;
		var center = map.getCenter();
		google.maps.event.trigger(map, "resize");
		map.setCenter(center); 
	}
	
	function initialize() {
		
		marker=new google.maps.Marker({
			position:myCenter
		});
		
		var mapProp = {
			center:myCenter,
			mapTypeControl: true,
			zoom:15,
			mapTypeId:google.maps.MapTypeId.ROADMAP
		};
		
		geocoder = new google.maps.Geocoder();
		map=new google.maps.Map(document.getElementById("normal"),mapProp);
		
		marker.setMap(map);
		
	};
	
	google.maps.event.addDomListener(window, 'load', initialize);
	
	google.maps.event.addDomListener(window, "resize", resizingMap());
	
	
});
