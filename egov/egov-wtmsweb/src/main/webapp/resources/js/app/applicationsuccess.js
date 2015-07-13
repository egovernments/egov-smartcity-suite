$(document).ready(function(){
	loadPropertyDetails();
	
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
						$("#zonewardblock").html(boundaryData);
						$("#propertytaxdue").html(response.propertyDetails.taxDue);
										
				}, 
				error: function (response) {
					console.log("failed");
				}
			});
		}		
	}
	
	var mode = $("#mode").val();
	if(mode=='inbox') {
		$("#propertytaxdue").addClass("error-msg");
	}
});

$('#payBtn').click(function() {
	var url = '/wtms/application/generatebill/'+ $('#consumerCode').val();
	$('#waterConnectionSuccess').attr('method', 'post');
	$('#waterConnectionSuccess').attr('action', url);
})