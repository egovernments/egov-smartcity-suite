$(document).ready(function(){
	
	var myCenter;
	var map, geocoder, geolocate, marker;  
	$('#complaint-locate').on('show.bs.modal', function() {
		//Must wait until the render of the modal appear, thats why we use the resizeMap and NOT resizingMap!! ;-)
		
		//complaint view(update) map
		$('#show_address_in_map').html($('#address_locate').html());
		$.ajax({
			type: "POST",
			url: 'https://maps.googleapis.com/maps/api/geocode/json?address='+$('#address_locate').html(),
			dataType: 'json',
			success : function(data){
				//console.log(JSON.stringify(data.results[0].geometry.location));
				lat = JSON.stringify(data.results[0].geometry.location.lat);
				longe = JSON.stringify(data.results[0].geometry.location.lng);
				
				myCenter=new google.maps.LatLng(lat, longe);
				initialize();
			}
		});
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
			draggable:true,
			title:"Drag me!",
			position:myCenter
		});
		
		var mapProp = {
			center:myCenter,
			mapTypeControl: true,
			zoom:7,
			mapTypeId:google.maps.MapTypeId.ROADMAP
		};
		
		geocoder = new google.maps.Geocoder();
		map=new google.maps.Map(document.getElementById("normal"),mapProp);
		
		/* var GeoMarker = new GeolocationMarker(map); */
		
		marker.setMap(map);
		
		google.maps.event.addListener(marker, 'click', function() {
			
			infowindow.setContent(contentString);
			infowindow.open(map, marker);
			
		});
		
		google.maps.event.addListener(marker, "dragend", function (e) {
			var lat, lng, address;
			geocoder.geocode({ 'latLng': marker.getPosition() }, function (results, status) {
				if (status == google.maps.GeocoderStatus.OK) {
					console.log("drag end!!!");
					lat = marker.getPosition().lat();
					lng = marker.getPosition().lng();
					address = results[0].formatted_address;
					$('#location').typeahead('val', address);
					$('#lat').val(lat);
					$('#lng').val(lng);
				}
			});
		});
		
	};
	
	google.maps.event.addDomListener(window, 'load', initialize);
	
	google.maps.event.addDomListener(window, "resize", resizingMap());
	
	
});