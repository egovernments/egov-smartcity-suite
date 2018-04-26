var _URL = window.URL || window.webkitURL;
$(document).ready(function(){
	
	var locationOption = {
			zoom: 11,
			mapTypeId : google.maps.MapTypeId.TERRAIN, 
			streetViewControl: true,
			center: new google.maps.LatLng(loclatlang[0],loclatlang[1]),
			panControl: true,
			zoomControl: true,
			//disableDefaultUI: true,
	        scrollwheel: true,
	        draggable: true,
	        //styles: mapStyle,
			mapTypeControlOptions: {
				style: google.maps.MapTypeControlStyle.DROPDOWN_MENU
			},
			zoomControlOptions: {
				style: google.maps.ZoomControlStyle.SMALL
			}
	 };
	
	var map = new google.maps.Map(document.getElementById("normal"), locationOption);
	
	var latLang = ($("#eventlocation").val()).split(":");
	
	var marker = new google.maps.Marker({
        position: new google.maps.LatLng(latLang[0],latLang[1]),
		color:"0000aa",
        map: map,
        animation: google.maps.Animation.DROP,
        //icon: marker_image,
        //title: data.zone

      });
	
	var input = $("#pac-input");
	var searchBox = new google.maps.places.SearchBox(input);
	map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);
	
	// Bias the SearchBox results towards current map's viewport.
    map.addListener('bounds_changed', function() {
      searchBox.setBounds(map.getBounds());
    });

    var markers = [];
    // Listen for the event fired when the user selects a prediction and retrieve
    // more details for that place.
    searchBox.addListener('places_changed', function() {
        var places = searchBox.getPlaces();

        if (places.length == 0) {
          return;
        }

        // Clear out the old markers.
        markers.forEach(function(marker) {
          marker.setMap(null);
        });
        markers = [];

        // For each place, get the icon, name and location.
        var bounds = new google.maps.LatLngBounds();
        places.forEach(function(place) {
          if (!place.geometry) {
            console.log("Returned place contains no geometry");
            return;
          }
          var icon = {
            url: place.icon,
            size: new google.maps.Size(71, 71),
            origin: new google.maps.Point(0, 0),
            anchor: new google.maps.Point(17, 34),
            scaledSize: new google.maps.Size(25, 25)
          };

          // Create a marker for each place.
          markers.push(new google.maps.Marker({
            map: map,
            icon: icon,
            title: place.name,
            position: place.geometry.location
          }));

          if (place.geometry.viewport) {
            // Only geocodes have viewport.
            bounds.union(place.geometry.viewport);
          } else {
            bounds.extend(place.geometry.location);
          }
        });
        map.fitBounds(bounds);
      });
	
    google.maps.event.addListener(map, "click", function(event) {
		//Get the location that the user clicked.
        var clickedLocation = event.latLng;
        console.log("clickedLocation=="+clickedLocation);
        //If the marker hasn't been added.
        if(marker === false){
            //Create the marker.
            marker = new google.maps.Marker({
                position: clickedLocation,
                map: map,
                draggable: true //make it draggable
            });
            //Listen for drag events!
            google.maps.event.addListener(marker, 'dragend', function(event){
                markerLocation();
            });
        } else{
            //Marker has already been added, so just change its location.
            marker.setPosition(clickedLocation);
        }
        //Get the marker's location.
        markerLocation();
		   
	});
			
	if($("#ispaidHid").val() === "true"){
		$("#ispaid")[0].checked = true;
		$("#costLabel").show();
    	$("#costDiv").show();
    	$("#urllabel").show();
    	$("#urldiv").show();
	}
	
	$("#ispaid").on("change", function(event) { 
		//alert(($(this).is(':checked')));
	    if ($(this).is(':checked')) {
	        //$(this).trigger("change");
	        $("#costLabel").show();
	    	$("#costDiv").show();
	    	$("#urllabel").show();
	    	$("#urldiv").show();
	    }else{
	    	$("#costLabel").hide();
	    	$("#costDiv").hide();
	    	$("#urllabel").hide();
	    	$("#urldiv").hide();
	    }
	    event.preventDefault();
	});
	
	$(".btn-primary").click(function(event){
		
		if($("#mode").val()==="update"){
			var start = $("#startDt").val();
			var end = $("#endDt").val();
			var stsplit = start.split("/");
			var ensplit = end.split("/");
			
			start = stsplit[1] + "/" + stsplit[0] + "/" + stsplit[2];
			end = ensplit[1] + "/" + ensplit[0] + "/" + ensplit[2];
			if(!validateStartDateAndEndDate(start,end))
			{
				return false;
			}
			var startHH = $("#startHH").val();
			var startMM = $("#startMM").val();
			var endHH = $("#endHH").val();
			var endMM = $("#endMM").val();
			if(validateEqualStartDateAndEndDate(start,end) && startHH === endHH &&  startMM === endMM)
			{
				bootbox.alert("Invalid date time range. Start Date, Start Time cannot be equal to End Date, End Time!");
				$("#endHH").val("");
				$("#endMM").val("");
				return false;
			}
		}
		
		document.getElementById("updateEventform").submit();
		return true;
		//event.preventDefault();
	});
	
	$("input[type=file]").change(function () {
        var val = $(this).val().toLowerCase(),
            regex = new RegExp("(.*?)\.(jpg|jpeg|bmp|gif|png)$");

        if (!(regex.test(val))) {
            $(this).val("");
            bootbox.alert("Sorry, " + $(this).val() + " is invalid, allowed extensions are: jpg, jpeg, bmp, gif or png");
        }
        var img = new Image();
	    img.src = _URL.createObjectURL($(this)[0].files[0]);
	    img.onload = function(){
	    	if(this.width<100||this.height<100) {
	    		$(this).val("");
	        	bootbox.alert("Your image is too small, it must be equal to or more than 100x100");
	        }
	    }
    });
	
});

function validateStartDateAndEndDate(start, end) {
    var startDate = Date.parse(start);
    var endDate = Date.parse(end);
    if (startDate > endDate) {
    	bootbox.alert("Invalid date range. Start Date cannot be after End Date!");
		$("#endDate").val("");
		return false;
		} else {
		return true;
	}
    return true;
}

function validateEqualStartDateAndEndDate(start, end) {
    var startDate = Date.parse(start);
    var endDate = Date.parse(end);
    if (startDate === endDate) {
		return true;
		} else {
		return false;
	}
    return true;
}

function checkcreateform(){
	var val = $("#file").val().toLowerCase(),
    regex = new RegExp("(.*?)\.(jpg|jpeg|bmp|gif|png)$");
	if (!(regex.test(val))) {
		$("#file").val("");
	    bootbox.alert("Sorry, " + $(this).val() + " is invalid, allowed extensions are: jpg, jpeg, bmp, gif or png");
	    return false;
	}
	var img = new Image();
	img.src = URL.createObjectURL($("#file")[0].files[0]);
	img.onload = function(){
    if(this.width<100||this.height<100) {
    	$("#file").val("");
    	bootbox.alert("Your image is too small, it must be equal to or more than 100x100");
    	return false;
    }
    
    return true;
	}
}

//values to our textfields so that we can save the location.
function markerLocation(){
  //Get location.
  var currentLocation = marker.getPosition();
  //Add lat and lng values to a field that we can save.
  console.log(currentLocation.lat());
  console.log(currentLocation.lng());
  $("#lat").val(currentLocation.lat());
  $("#lng").val(currentLocation.lng());
  $("#eventlocation").val(currentLocation.lat() + ':' + currentLocation.lng());
}