$(document).ready(function(){
	
	var fileformats = ['jpg', 'jpeg', 'gif', 'png',  '3g2', '3gp', '3gp2', '3gpp', 'avi', 'divx', 'flv', 'mov', 'mp4', 'mpeg4', 'mpg4', 'mpeg', 'mpg', 'm4v', 'wmv' ];
	var myCenter;
	
	var fileinputid = ['file1','file2','file3'];//assigning file id
	var filefilled = {};//image fullfilled array
	var removedarray = [];
	var fileid;
	
	$('#triggerFile').click(function(){
		console.log(removedarray.length);
		if(removedarray.length == 0 || removedarray.length == 3)
		{
			var keys=Object.keys(filefilled);
			fileid = fileinputid[keys.length];
			console.log("File ID normal:"+fileid);
			}else{
			fileid = removedarray[0];
			console.log("File ID removal:"+fileid);
		}
	    
		
		$('#'+fileid).trigger("click");
	});
	
	$('.remove-img').click(function(){
		console.log("Removal");
		delete filefilled[$(this).attr('data-file-id')];
		if ($.inArray($(this).attr('data-file-id'), removedarray) !== -1)//check removed file id already exists, if exists leave as such or push it
		{
			
			}else{
			removedarray.push($(this).attr('data-file-id'));
			removedarray.sort();
			console.log("sorted removed array"+removedarray);
		}
		
		console.log("File filled array:"+JSON.stringify(filefilled));
		$('#'+$(this).attr('data-file-id')).val('');
		$('#triggerFile').removeAttr('disabled');
		if($(this).attr('data-file-id') == 'file1')
		{
			$('#file1block, .preview-cross1, #preview1').hide();
			$('#preview1').removeAttr("src");
			$('#filename1').html('');
		}else if($(this).attr('data-file-id') == 'file2')
		{
			$('#file2block, .preview-cross2, #preview2').hide();
			$('#preview2').removeAttr("src");
			$('#filename2').html('');
		}else if($(this).attr('data-file-id') == 'file3')
		{
			$('#file3block, .preview-cross3, #preview3').hide();
			$('#preview3').removeAttr("src");
			$('#filename3').html('');
		}
		
	});
	
	$('#file1, #file2, #file3').on('change.bs.fileinput',function(e)
	{
		/*validation for file upload*/
		myfile= $( this ).val();
		var ext = myfile.split('.').pop();
		if($.inArray(ext, fileformats) > -1){
			//do something    
			}else{
			alert(ext+" file format is not allowed");
			return;
		}
		
		//alert('ext'+ext);
		
		if(e.target.files.length>0)
		{
			filefilled[$(this).attr('id')]=this.files[0].name;
			console.log("File filled array:"+JSON.stringify(filefilled));
			readURL(this, this.files[0].name);
			var index = removedarray.indexOf(fileid);
			if (index > -1) {
				removedarray.splice(index, 1);
			}
			if(fileid == 'file1')
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
							$('#location').typeahead('val', data.results[0].formatted_address);
							myCenter=new google.maps.LatLng(formatted_lat, formatted_long);
							setmarkerfromimage();
						}
					});
					//console.log(EXIF.pretty(this));
				});
			}
			
			if(Object.keys(filefilled).length == 3)
			{
				$('#triggerFile').attr('disabled','disabled');
			}
		}
	});
	
	function readURL(input, filename) {
		//console.log("Key:"+fileid);
		//console.log("Key value is:"+filefilled[fileid]);
		filename = ((filename.length > 15) ? filename.substring(0,13)+".." : filename);
		if (input.files && input.files[0]) {
			var reader = new FileReader();
			reader.onload = function(e) {
				if(fileid == 'file1')
				{
					$('#file1block, .preview-cross1, #preview1').show();
					$('.preview-cross1').attr('data-file-id',fileid);
					$('#preview1').attr('src', e.target.result).width(100);
					$('#filename1').html(filename);
				}else if(fileid == 'file2')
				{
					$('#file2block, .preview-cross2, #preview2').show();
					$('.preview-cross2').attr('data-file-id',fileid);
					$('#preview2').attr('src', e.target.result).width(100);
					$('#filename2').html(filename);
				}else if(fileid == 'file3')
				{
					$('#file3block, .preview-cross3, #preview3').show();
					$('.preview-cross3').attr('data-file-id',fileid);
					$('#preview3').attr('src', e.target.result).width(100);
					$('#filename3').html(filename);
				}
			}
			
			reader.readAsDataURL(input.files[0]);
		}
	}
	
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
	
	
	var map, geocoder, geolocate, marker, mapProp;        
	myCenter=new google.maps.LatLng(13.081604, 80.275183);
	
	function initialize() {
		
		marker=new google.maps.Marker();
		
		mapprop();
		
		geocoder = new google.maps.Geocoder();
		map=new google.maps.Map(document.getElementById("normal"),mapProp);
		
		var GeoMarker = new GeolocationMarker(map);
		
		marker.setMap(map);
		
		if (navigator.geolocation) {
			navigator.geolocation.getCurrentPosition(function (position) {
				
				var userLatLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
				//alert(userLatLng);
				
				marker = new google.maps.Marker({
					position: userLatLng,
					draggable:true,
					map: map
				});
				
				map.setCenter(userLatLng);
				
				dragendmarker();
				
				}, function error(err) {
				console.log('error: ' + err.message);        
			});
		}
		
	};
	
	function setmarkerfromimage()
	{
		marker.setMap(null);

		marker = new google.maps.Marker({
			position: myCenter,
			draggable:true,
			map: map
		});
		
		mapprop();
		
		geocoder = new google.maps.Geocoder();
		
		marker.setMap(map);
		
		dragendmarker();
		
	}
	
	function mapprop(){
	
		mapProp = {
			center:myCenter,
			mapTypeControl: true,
			zoom:12,
			mapTypeId:google.maps.MapTypeId.ROADMAP
		};
		
	}
	
	function dragendmarker(){
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
	
});
