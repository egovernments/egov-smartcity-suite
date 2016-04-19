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
				bootbox.alert(ext+" file format is not allowed");
			return;
		}
		
		//bootbox.alert('ext'+ext);
		
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
					var imagelat = EXIF.getTag(this, "GPSLatitude"),
					imagelongt = EXIF.getTag(this, "GPSLongitude");
					var formatted_lat = format_lat_long(imagelat.toString());
					var formatted_long = format_lat_long(imagelongt.toString());
					$.ajax({
						type: "POST",
						url: 'https://maps.googleapis.com/maps/api/geocode/json?latlng='+formatted_lat+','+formatted_long+'&sensor=true',
						dataType: 'json',
						success : function(data){
							$('#location').typeahead('val', data.results[0].formatted_address);
							myCenter=new google.maps.LatLng(formatted_lat, formatted_long);
							$('#lat').val(formatted_lat);
							$('#lng').val(formatted_long);
							lat = formatted_lat;
							lng = formatted_long;
							//setmarkerfromimage();
						    	map.setCenter(myCenter);
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
        var lat, lng, address;
	myCenter=new google.maps.LatLng(13.081604, 80.275183);
	
	function initialize() {
		
		//marker=new google.maps.Marker();
		
		//mapprop();
		var mapOptions = {
			zoom: 10,
			mapTypeControl: false,
			navigationControl: false,
		};
		
		geocoder = new google.maps.Geocoder();
		map=new google.maps.Map(document.getElementById("normal"),mapOptions);
		
		var GeoMarker = new GeolocationMarker(map);
		$('<div/>').addClass('centerMarker').appendTo(map.getDiv());
		//marker.setMap(map);
		
		if (navigator.geolocation) {
			navigator.geolocation.getCurrentPosition(function (position) {

				var userLatLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
				lat = position.coords.latitude;
				lng = position.coords.longitude;
				
				getAddress(lat, lng);

				map.setCenter(userLatLng);
				
				}, function error(err) {
					//console.log('error: ' + err.message); 
					citylat = $('#getcitylat').val();
					citylng = $('#getcitylng').val();
					if(!citylat || !citylng){
						citylat = 20.5937;
						citylng = 78.9629;
						setlatlong(citylat, citylng);
					}else{
						setlatlong(citylat, citylng);
					}
					
				
			});
		}

		google.maps.event.addListener(map, 'center_changed', function() {
			var location = map.getCenter();
			//console.log(location.lat()+"<=======>"+location.lng());
			getAddress(location.lat(), location.lng());
		});
		
	};
	
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
		console.log(location.lat()+"<=======>"+location.lng());
		lat = location.lat();
		lng = location.lng();
		$.ajax({
			type: "POST",
			url: 'https://maps.googleapis.com/maps/api/geocode/json?latlng='+lat+','+lng+'&sensor=true',
			dataType: 'json',
			success : function(data){
				 address = data.results[0].formatted_address;
				 $('#location').typeahead('val', address);
				 $('#latlngaddress').val(address);
			}
		});	
		$('#lat').val(lat);
		$('#lng').val(lng);
	});

	$('#modal-6').on('hidden.bs.modal', function () {
	    var userLatLng = new google.maps.LatLng(lat, lng);
	    map.setCenter(userLatLng);
	});
	
});