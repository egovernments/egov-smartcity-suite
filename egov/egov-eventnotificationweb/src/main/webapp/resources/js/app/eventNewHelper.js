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
		
	$('#ispaid').on('change', function(event) { 
		//alert(($(this).is(':checked')));
	    if ($(this).is(':checked')) {
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
		
		if ($('form').valid()) {
			if($("#mode").val()=='create'){
				var start = $('#startDate').val();
				var end = $('#endDate').val();
				var stsplit = start.split("/");
				var ensplit = end.split("/");
				
				start = stsplit[1] + "/" + stsplit[0] + "/" + stsplit[2];
				end = ensplit[1] + "/" + ensplit[0] + "/" + ensplit[2];
				if(!validateStartDateAndEndDate(start,end))
				{
					return false;
				}
				var startHH = $('#startHH').val();
				var startMM = $('#startMM').val();
				var endHH = $('#endHH').val();
				var endMM = $('#endMM').val();
				if(validateEqualStartDateAndEndDate(start,end) && startHH == endHH &&  startMM == endMM)
				{
					bootbox.alert("Invalid date time range. Start Date, Start Time cannot be equal to End Date, End Time!");
					$('#endHH').val('');
					$('#endMM').val('');
					return false;
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
	
});

function validateStartDateAndEndDate(start, end) {
    var startDate = Date.parse(start);
    var endDate = Date.parse(end);
    if (startDate > endDate) {
    	bootbox.alert("Invalid date range. Start Date cannot be after End Date!");
		$('#endDate').val('');
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