$(document).ready(function(){
	loadPropertyDetails();
	
	var mode =$('#mode').val();
	var currentloggedInUser=$('#currentUser').val();
	if(currentloggedInUser=='true' && mode=='')
		{
		$(".show-row").hide(); 
		/*$('#approvalComent').hide();
		$('#approvalComent').hide();*/
		$('#approvalDepartment').removeAttr('required');
		$('#approvalDesignation').removeAttr('required');
		$('#approvalPosition').removeAttr('required');
		}
	
	
	changeLabel();
	function changeLabel() {
		if ($('#usageType :selected').text().localeCompare("Lodges") == 0) {
			$('#persons').hide();
			$('#rooms').show();
			$('#personsdiv').hide();
			$('#roomsdiv').show();
		}
		else {
			$('#persons').show();
			$('#rooms').hide();
			$('#personsdiv').show();
			$('#roomsdiv').hide();
		}
	}
	
	$('#usageType').change(function () {
		changeLabel();
	});
	
	$('#cardHolderDiv').hide();
	$('#bplCardHolderName').removeAttr('required');
	
	if($('#validationMessage').val()!='')
		alert($('#validationMessage').val());
	
	$('#connectionCategorie').change(function(){
		if ($('#connectionCategorie :selected').text().localeCompare("BPL") == 0) {  
			$("#cardHolderDiv").show();
	    	$("#bplCardHolderName").attr('required', 'required');
	    	$("#bplCardHolderName").val();
		}
		else  {
			$("#cardHolderDiv").hide();
	    	$("#bplCardHolderName").removeAttr('required');
		}
	});
	
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
