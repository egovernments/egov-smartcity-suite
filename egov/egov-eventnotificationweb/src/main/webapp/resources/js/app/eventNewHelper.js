var _validFileExtensions = [".jpg", ".jpeg", ".bmp", ".gif", ".png"]; 
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
		
	$("#ispaid").on("change", function(event) { 
		//alert(($(this).is(':checked')));
	    if ($(this).is(":checked")) {
	        //$(this).trigger("change");
	        $("#costLabel").show();
	    	$("#costDiv").show();
	    }else{
	    	$("#costLabel").hide();
	    	$("#costDiv").hide();
	    }
	    event.preventDefault();
	});
	
	$(".btn-primary").click(function(event){
		
		if ($("form").valid()) {
			if($("#mode").val()==="create"){
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
				
				var uploadImage = $("#file").val();
			    var img = new Image();
			    img.src = uploadImage;
			    img.onload = function(){
			        if(this.width<100||this.height<100) {
			        	bootbox.alert("Your image is too small, it must be equal or more less than 100x100");
			        }
			    }
			}
			document.getElementById("createEventform").submit(); 
		} else {
			event.preventDefault();
		}
		
		
		//document.getElementById("createEventform").submit(); 
		//document.forms[0].submit;
		return true;
		//event.preventDefault();
	});
	
	google.maps.event.addListener(map, "click", function(event) {
		//alert( 'Lat: ' + event.latLng.lat() + ' and Longitude is: ' + event.latLng.lng() );
		$("#eventlocation").val(event.latLng.lat() + ':' + event.latLng.lng());
		   
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
    if (startDate == endDate) {
		return true;
		} else {
		return false;
	}
    return true;
}

function ValidateSingleInput(oInput) {
    if (oInput.type === "file") {
        var sFileName = oInput.value;
         if (sFileName.length > 0) {
            var blnValid = false;
            for (var j = 0; j < _validFileExtensions.length; j++) {
                var sCurExtension = _validFileExtensions[j];
                if (sFileName.substr(sFileName.length - sCurExtension.length, sCurExtension.length).toLowerCase() === sCurExtension.toLowerCase()) {
                    blnValid = true;
                    break;
                }
            }
             
            if (!blnValid) {
                alert("Sorry, " + sFileName + " is invalid, allowed extensions are: " + _validFileExtensions.join(", "));
                oInput.value = "";
                return false;
            }
        }
    }
    return true;
}