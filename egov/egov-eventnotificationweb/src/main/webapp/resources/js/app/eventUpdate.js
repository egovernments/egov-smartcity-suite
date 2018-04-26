var _URL = window.URL || window.webkitURL;
$(document).ready(function(){
	
	var locationOption = {
			zoom: 11,
			mapTypeId : google.maps.MapTypeId.TERRAIN, 
			streetViewControl: true,
			center: new google.maps.LatLng(citylat, citylng),
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
	
	google.maps.event.addListener(map, "click", function(event) {
		//alert( 'Lat: ' + event.latLng.lat() + ' and Longitude is: ' + event.latLng.lng() );
		$("#eventlocation").val(event.latLng.lat() + ':' + event.latLng.lng());
		   
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