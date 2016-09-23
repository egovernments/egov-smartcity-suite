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
						$("#nooffloors").html(response.propertyDetails.noOfFloors);
						if(response.ownerNames[0].mobileNumber != '')
							$("#mobileNumber").html(response.ownerNames[0].mobileNumber);
						$("#email").html(response.ownerNames[0].emailId);
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
	
	var mode = $("#mode").val();
	if(mode=='inbox') {
		$("#propertytaxdue").addClass("error-msg");
	}
	
	/*$('#addConnection').click(function() {
		var url = '/wtms/application/addconnection/'+ $('#consumerCode').val();
		$('#waterConnectionSuccess').attr('method', 'get');
		$('#waterConnectionSuccess').attr('action', url);
		$('#waterConnectionSuccess').attr('mode', 'search');
		window.location = url;
	});*/
	
	/*$('#changeConnection').click(function() {
		var url = '/wtms/application/changeOfUse/'+$('#consumerCode').val();
		$('#waterConnectionSuccess').attr('method', 'get');
		$('#waterConnectionSuccess').attr('action', url);
		$('#waterConnectionSuccess').attr('mode', 'search');
		window.location = url;
	});*/
	
	/*$('#closureConnection').click(function() {
		var url = '/wtms/application/close/'+$('#consumerCode').val();
		$('#waterConnectionSuccess').attr('method', 'get');
		$('#waterConnectionSuccess').attr('action', url);
		$('#waterConnectionSuccess').attr('mode', 'search');
		window.location = url;
	});*/
	
	$('#viewEstimationNotice').click(function() {
		var url = '/wtms/application/estimationNotice/view/'+ $('#applicationCode').val();
		$('#waterConnectionSuccess').attr('method', 'get');
		$('#waterConnectionSuccess').attr('action', url);
		$('#waterConnectionSuccess').attr('mode', 'search');
		window.open(url,'window','scrollbars=yes,resizable=yes,height=700,width=800,status=yes');
	});
	
	$('#viewWorkOrder').click(function() {
		var url = '/wtms/application/workorder/view/'+ $('#applicationCode').val();
		$('#waterConnectionSuccess').attr('method', 'get');
		$('#waterConnectionSuccess').attr('action', url);
		$('#waterConnectionSuccess').attr('mode', 'search');
		window.location(url);
	});
	
	/*$('#re-connection').click(function() {
		var url = '/wtms/application/reconnection/'+$('#consumerCode').val();
		$('#waterConnectionSuccess').attr('method', 'get');
		$('#waterConnectionSuccess').attr('action', url);
		$('#waterConnectionSuccess').attr('mode', 'search');
		window.location = url;
	});*/
});

$('#payBtn').click(function() {
	var url = '/wtms/application/generatebill/'+ $('#applicationCode').val()+"?applicationTypeCode="+$('#applicationTypeCode').val();
	$('#waterConnectionSuccess').attr('method', 'post');
	$('#waterConnectionSuccess').attr('action', url);
})