$(document).ready(function(){
	$('#propertyIdentifier').blur(function(){
		loadPropertyDetails();
	});
	
	loadPropertyDetails();
	
	function loadPropertyDetails() {
		propertyID=$('#propertyIdentifier').val()
		if(propertyID != '') {
			$.ajax({
				url: "/ptis/rest/property/"+propertyID,      
				type: "GET",
				dataType: "json",
				success: function (response) { 
					console.log("success"+response);
					if(response.errorDetails.errorCode != null && response.errorDetails.errorCode != '') {
						$('#propertyIdentifier').val('');
						$('#applicantname').val('');
						$('#mobileNumber').val('');
						$('#propertyaddress').val('');
						$('#zonewardblock').val('');
						$('#propertytaxdue').val('0.00');
						alert(response.errorDetails.errorMessage);
					}
					else {	
						applicantName = '';
						for(i=0; i<response.ownerNames.length; i++) {
							if(applicantName == '')
								applicantName = response.ownerNames[i].ownerName;
							else 							
								applicantName = applicantName+ ', '+response.ownerNames[i].ownerName;
						}
						$("#applicantname").val(applicantName);
						if(response.ownerNames[0].mobileNumber != '')
							$("#mobileNumber").val(response.ownerNames[0].mobileNumber);
						$("#propertyaddress").val(response.propertyAddress);
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
						$("#zonewardblock").val(boundaryData);
						$("#propertytaxdue").val(response.propertyDetails.taxDue);
					}					
				}, 
				error: function (response) {
					console.log("failed");
				}
			});
		}		
	}
});
