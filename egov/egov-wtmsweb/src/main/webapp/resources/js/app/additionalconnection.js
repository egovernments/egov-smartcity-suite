$(document).ready(function(){
	loadPropertyDetails();
	
	if($('#validationMessage').val()!='')
		alert($('#validationMessage').val());
	
	function loadPropertyDetails() {
		propertyID=$('#propertyIdentifier').html()
		if(propertyID != '') {
			$.ajax({
				url: "/ptis/rest/property/"+propertyID,      
				type: "GET",
				dataType: "json",
				success: function (response) { 
					console.log("success"+response);
						applicantName = '';
						for(i=0; i<response.ownerNames.length; i++) {
							if(applicantName == '')
								applicantName = response.ownerNames[i].ownerName;
							else 							
								applicantName = applicantName+ ', '+response.ownerNames[i].ownerName;
						}
						$("#applicantname").html(applicantName);
						$("#nooffloors").html(response.propertyDetails.noOfFloors);
						if(response.ownerNames[0].mobileNumber != '')
							$("#mobileNumber").html(response.ownerNames[0].mobileNumber);
						$("#propertyaddress").html(response.propertyAddress);
						boundaryData = '';
						if(response.boundaryDetails.zoneName != null && response.boundaryDetails.zoneName != '')
							boundaryData = response.boundaryDetails.zoneName;
						if(response.boundaryDetails.wardName != null && response.boundaryDetails.wardName != '') {
							if(boundaryData == '')
								boundaryData = response.boundaryDetails.wardName;
							else
								boundaryData = boundaryData + " / " + response.boundaryDetails.wardName;
						}
						if(response.boundaryDetails.blockName != null && response.boundaryDetails.blockName != '') {
							if(boundaryData == '')
								boundaryData = response.boundaryDetails.blockName;
							else
								boundaryData = boundaryData + " / " +response.boundaryDetails.blockName; 
						}
						$("#aadhaar").html(response.ownerNames[0].aadhaarNumber);
						$("#locality").html(response.boundaryDetails.localityName);
						$("#zonewardblock").html(boundaryData);
						$("#propertytaxdue").html(response.propertyDetails.currentTax);
										
				}, 
				error: function (response) {
					console.log("failed");
				}
			});
		}		
	}
	
	$("#propertytaxdue").addClass("error-msg");
	
});
