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
	
	var map, geocoder, geolocate, marker, mapProp;    
        var lat, lng, address;
	myCenter=new google.maps.LatLng(13.081604, 80.275183);
	
	function initialize() {
		
		//marker=new google.maps.Marker();
		
		//mapprop();
		var latLng,mapOptions = {
			zoom: 10,
			timeout: 500, 
			mapTypeControl: false,
			navigationControl: false,
		};
		
		var userLocationFound = function(position){
		    latLng = {
		        lat: position.coords.latitude,
		        lng: position.coords.longitude
		    };
		    console.log("User confirmed! Location found: " + latLng.lat + ", " + latLng.lng);
		    
		    //Set current locaion to map
		    var userLatLng = new google.maps.LatLng(latLng.lat, latLng.lng);
			lat = latLng.lat;
			lng = latLng.lng;
			
			getAddress(lat, lng);

			map.setCenter(userLatLng);
			
			mapcenterchangeevent();
			
		}
		
		var userLocationNotFound = function(){
			
			citylat = $('#getcitylat').val();
			citylng = $('#getcitylng').val();
			
		    //Assign static point to map
			if(!citylat || !citylng){
				citylat = 20.5937;
				citylng = 78.9629;
			    //console.log("Fallback set with no city setup: ", citylat+'<-->'+citylng);
			}else{
			    //console.log("Fallback set with city setup: ", citylat+'<-->'+citylng);
			}
			
			latLng = {
		        lat: citylat, // fallback lat 
		        lng: citylng  // fallback lng
		    };
			setlatlong(citylat, citylng);
			mapcenterchangeevent();
			
		}

		
		geocoder = new google.maps.Geocoder();
		map=new google.maps.Map(document.getElementById("normal"),mapOptions);
		
		var GeoMarker = new GeolocationMarker(map);
		$('<div/>').addClass('centerMarker').appendTo(map.getDiv());
		
		navigator.geolocation.getCurrentPosition(userLocationFound, userLocationNotFound, mapOptions);

		setTimeout(function () {
		    if(!latLng){
		        console.log("No confirmation from user, using fallback");
		        userLocationNotFound();
		    }else{
		        console.log("Location was set");
		    }
		}, mapOptions.timeout + 500); // Wait extra second
		
		searchBar(map);  
		
	};
	
	function searchBar(map) {
		
		var input = /** @type {!HTMLInputElement} */(
			      document.getElementById('pac-input'));

			  var autocomplete = new google.maps.places.Autocomplete(input);
			  autocomplete.bindTo('bounds', map);

			  autocomplete.addListener('place_changed', function() {
			    var place = autocomplete.getPlace();
			    if (!place.geometry) {
			      window.alert("Autocomplete's returned place contains no geometry");
			      return;
			    }

			    // If the place has a geometry, then present it on a map.
			    if (place.geometry.viewport) {
			      map.fitBounds(place.geometry.viewport);
			    } else {
			      map.setCenter(place.geometry.location);
			      map.setZoom(17);  // Why 17? Because it looks good.
			    }

			    var address = '';
			    if (place.address_components) {
			      address = [
			        (place.address_components[0] && place.address_components[0].short_name || ''),
			        (place.address_components[1] && place.address_components[1].short_name || ''),
			        (place.address_components[2] && place.address_components[2].short_name || '')
			      ].join(' ');
			    }

			  });

    };
	
	function mapcenterchangeevent(){
		google.maps.event.addListener(map, 'center_changed', function() {
			var location = map.getCenter();
			//console.log(location.lat()+"<=======>"+location.lng());
			getAddress(location.lat(), location.lng());
		});
	}

	function setlatlong(citylat , citylng){
		var userLatLng = new google.maps.LatLng(citylat, citylng);
		lat = citylat;
		lng = citylng;
		
		getAddress(lat, lng);

		map.setCenter(userLatLng);
	}
	
	function getAddress(lat, lng){
		$.ajax({
			type: "POST",
			url: 'https://maps.googleapis.com/maps/api/geocode/json?latlng='+lat+','+lng+'&sensor=true',
			dataType: 'json',
			success : function(data){
				 address = data.results[0].formatted_address;
			}
		});	
	}
	
	google.maps.event.addDomListener(window, 'load', initialize);
	
	google.maps.event.addDomListener(window, "resize", resizingMap());
	
	$('#modal-6').on('show.bs.modal', function() {
		//Must wait until the render of the modal appear, thats why we use the resizeMap and NOT resizingMap!! ;-)
		
		//complaint registration map
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
	
	$('.btn-save-location').click(function(){
		var location = map.getCenter();
		lat = location.lat();
		lng = location.lng();
		$("#latlonDiv").show();
		$.ajax({
			type: "POST",
			url: 'https://maps.googleapis.com/maps/api/geocode/json?latlng='+lat+','+lng+'&sensor=true',
			dataType: 'json',
			success : function(data){
				 address = data.results[0].formatted_address;
				 $('#location').val(address);
			}
		});	
		console.log(lat+'<-->'+lng);
		$('#latitude').val(lat);
		$('#longitude').val(lng);
	});

	$('#modal-6').on('hidden.bs.modal', function () {
		$('#pac-input').val('');
	    var userLatLng = new google.maps.LatLng(lat, lng);
	    map.setCenter(userLatLng);
	});
	
	$('.clear').click(function(){
		$('#location').val('');
		$('#latitude').val('');
		$('#longitude').val('');
		$("#latlonDiv").hide();
	});
	
});