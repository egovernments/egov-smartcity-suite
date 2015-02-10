$(document).ready(function(){
	
	var myCenter;
	
	$('#file1, #file2, #file3').on('change.bs.fileinput',function(e)
	{
		if(e.target.files.length>0)
		{
			var filename = this.files[0].name;
			readURL(this, filename);
			if($(this).attr('id') == 'file1')
			{
				EXIF.getData(e.target.files[0], function() {
					var lat = EXIF.getTag(this, "GPSLatitude"),
					longt = EXIF.getTag(this, "GPSLongitude");
					var formatted_lat = format_lat_long(lat.toString());
					var formatted_long = format_lat_long(longt.toString());
					$.ajax({
						type: "POST",
						url: 'http://maps.googleapis.com/maps/api/geocode/json?latlng='+formatted_lat+','+formatted_long+'&sensor=true',
						dataType: 'json',
						success : function(data){
							//$('#clocation').typeahead('setQuery', data.results[0].formatted_address); 
							$('#clocation').val(data.results[0].formatted_address); 
							myCenter=new google.maps.LatLng(formatted_lat, formatted_long);
							initialize();
						}
					});
					
					//console.log(EXIF.pretty(this));
				});
				
				$('#file2block').show();
				}else{
				$('#file3block').show();
			}
			
		}
		else
		{
			//alert('Cancel detected!');
		}
	});
	
	function readURL(input, filename) {
		filename = ((filename.length > 15) ? filename.substring(0,13)+".." : filename);
		if (input.files && input.files[0]) {
			var reader = new FileReader();
			
			reader.onload = function (e) {
				if($(input).attr('id') == 'file1')
				{
					$('#preview1, #triggerFile2, .preview-cross1').removeClass('display-hide');
					$('#preview1').attr('src', e.target.result).width(100);
					$('#filename1').html(filename);
					$('#file1block').show();
					$('#triggerFile1').addClass('display-hide');
				}else if($(input).attr('id') == 'file2')
				{
					$('#preview2, #triggerFile3, .preview-cross2').removeClass('display-hide');
					$('#preview2').attr('src', e.target.result).width(100);
					$('#filename2').html(filename);
					$('#file2block').show();
					$('#triggerFile2').addClass('display-hide');
				}else if($(input).attr('id') == 'file3')
				{
					$('#preview3, .preview-cross3').removeClass('display-hide');
					$('#preview3').attr('src', e.target.result).width(100);
					$('#filename3').html(filename);
					$('#file3block').show();
					$('#triggerFile3').attr('disabled','disabled');
				}
				
			};
			
			reader.readAsDataURL(input.files[0]);
		}
	}
	
	$('.remove-img').click(function(){
		var lclass = $(this).attr('class');
		if(lclass == "remove-img preview-cross1")
		{
			$('#file1').val("");
			$('#file1block').hide();
			$('#triggerFile1').removeClass('display-hide');
			$('#preview1').removeAttr("src");
			$('#triggerFile2, #triggerFile3').addClass('display-hide');
		}else if(lclass == "remove-img preview-cross2")
		{
			$('#file2').val("");
			$('#file2block').hide();
			$('#triggerFile2').removeClass('display-hide');
			$('#preview2').removeAttr("src");
			$('#triggerFile1, #triggerFile3').addClass('display-hide');
		}else if(lclass == "remove-img preview-cross3")
		{
			$('#file3').val("");
			$('#file3block').hide();/*hide whole block, need to clear img src*/
			$('#triggerFile3').removeAttr('disabled');
			$('#triggerFile3').removeClass('display-hide');
			$('#preview3').removeAttr("src");
			$('#triggerFile1, #triggerFile2').addClass('display-hide');
		}
		
		//console.log("file 1:"+$('#file1').val());
		
	});
	
	function format_lat_long(latorlong)
	{
		var loc_arry = latorlong.split(",");
		var degree= parseFloat(loc_arry[0]);
		var minutes= parseFloat(loc_arry[1]);
		var seconds= parseFloat(loc_arry[2]);
		
		//formula is degree+((minutes*60)+seconds/3600)
		var formatted = degree+((minutes*60)+seconds)/3600;
		
		return formatted;
	}
	
	
	var map, geocoder, geolocate, marker;        
	myCenter=new google.maps.LatLng(13.081604, 80.275183);
	
	
	function initialize() {
		
		marker=new google.maps.Marker({
			draggable:true,
			title:"Drag me!",
			position:myCenter
		});
		
		var mapProp = {
			center:myCenter,
			zoom: 12,
			mapTypeId:google.maps.MapTypeId.ROADMAP
		};
		
		geocoder = new google.maps.Geocoder();
		map=new google.maps.Map(document.getElementById("normal"),mapProp);
		
		/*navigator.geolocation.getCurrentPosition(function(position) {
			myCenter = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
			
			marker=new google.maps.Marker({
			draggable:true,
			title:"Drag me!",
			position:myCenter
			});
			
			map.setCenter(myCenter);
			marker.setMap(map);
			console.log("geo lat long inside"+myCenter);
		});*/
		
		marker.setMap(map);
		
		google.maps.event.addListener(marker, 'click', function() {
			
			infowindow.setContent(contentString);
			infowindow.open(map, marker);
			
		});
		
		google.maps.event.addListener(marker, "dragend", function (e) {
			var lat, lng, address;
			geocoder.geocode({ 'latLng': marker.getPosition() }, function (results, status) {
				if (status == google.maps.GeocoderStatus.OK) {
					lat = marker.getPosition().lat();
					lng = marker.getPosition().lng();
					address = results[0].formatted_address;
					$('#location').val(address);
					$('#lat').val(lat);
					$('#lng').val(lng);
				}
			});
		});
		
		marker=new google.maps.Marker({
			draggable:true,
			title:"Drag me!",
			position:myCenter
		});
		
		console.log("my center"+myCenter);
		
		var mapProp = {
			center:myCenter,
			zoom: 12,
			mapTypeId:google.maps.MapTypeId.ROADMAP
		};
		
		geocoder = new google.maps.Geocoder();
		map=new google.maps.Map(document.getElementById("normal"),mapProp);
		console.log("geo lat long before map set"+myCenter);
		
		/*navigator.geolocation.getCurrentPosition(function(position) {
			myCenter = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
			
			marker=new google.maps.Marker({
			draggable:true,
			title:"Drag me!",
			position:myCenter
			});
			
			map.setCenter(myCenter);
			marker.setMap(map);
			console.log("geo lat long inside"+myCenter);
		});*/
		
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
					$('#clocation').val(address);
					//$('#clocation').typeahead('setQuery', address); 
				}
			});
		});
		
		marker=new google.maps.Marker({
			draggable:true,
			title:"Drag me!",
			position:myCenter
		});
		
		console.log("my center"+myCenter);
		
		var mapProp = {
			center:myCenter,
			zoom: 12,
			mapTypeId:google.maps.MapTypeId.ROADMAP
		};
		
		geocoder = new google.maps.Geocoder();
		map=new google.maps.Map(document.getElementById("normal"),mapProp);
		console.log("geo lat long before map set"+myCenter);
		
		/*navigator.geolocation.getCurrentPosition(function(position) {
			myCenter = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
			
			marker=new google.maps.Marker({
			draggable:true,
			title:"Drag me!",
			position:myCenter
			});
			
			map.setCenter(myCenter);
			marker.setMap(map);
			console.log("geo lat long inside"+myCenter);
		});*/
		
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
					$('#clocation').val(address);
					$('#lat').val(lat);
					$('#lon').val(lng);
					//$('#clocation').typeahead('setQuery', address); 
				}
			});
		});
		
		marker=new google.maps.Marker({
			draggable:true,
			title:"Drag me!",
			position:myCenter
		});
		
		console.log("my center"+myCenter);
		
		var mapProp = {
			center:myCenter,
			zoom: 12,
			mapTypeId:google.maps.MapTypeId.ROADMAP
		};
		
		geocoder = new google.maps.Geocoder();
		map=new google.maps.Map(document.getElementById("normal"),mapProp);
		console.log("geo lat long before map set"+myCenter);
		
		/*navigator.geolocation.getCurrentPosition(function(position) {
			myCenter = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
			
			marker=new google.maps.Marker({
			draggable:true,
			title:"Drag me!",
			position:myCenter
			});
			
			map.setCenter(myCenter);
			marker.setMap(map);
			console.log("geo lat long inside"+myCenter);
		});*/
		
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
					console.log("drag end!!!"+lat+"---->"+lng);
					address = results[0].formatted_address;
					$('#clocation').val(address);
					//$('#clocation').typeahead('setQuery', address); 
				}
			});
		});
		
	};
	
	google.maps.event.addDomListener(window, 'load', initialize);
	
	google.maps.event.addDomListener(window, "resize", resizingMap());
	
	$('#modal-6').on('show.bs.modal', function() {
		//Must wait until the render of the modal appear, thats why we use the resizeMap and NOT resizingMap!! ;-)
		
		//complaint registration map
		resizeMap();
	});
	
	var lat,longe ;
	
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
	
	
});
