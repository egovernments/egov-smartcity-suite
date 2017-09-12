/*
 * eGov SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2017>  eGovernments Foundation
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
	loadPropertyDetails();
	
	if($("#connectionType").val()=="METERED"){
		$(".showfields").show();
	}
	
	if($('#noJAORSAMessage').val()!='')
		bootbox.alert($('#noJAORSAMessage').val());

	var mode =$('#mode').val();
	var validateIfPTDueExists=$('#validateIfPTDueExists').val();
	var currentloggedInUser=$('#currentUser').val();
    var citizenPortal=$('#citizenPortalUser').val();
    var isAnonymousUser = $('#isAnonymousUser').val();
	if((currentloggedInUser=='true' && mode=='addconnection' && validateIfPTDueExists=='') ||(currentloggedInUser=='true' && validateIfPTDueExists=='false')
	|| (citizenPortal=='true' && mode=='addconnection' && validateIfPTDueExists=='') ||(citizenPortal=='true' && validateIfPTDueExists=='false')
	|| (isAnonymousUser=='true' && mode=='addconnection' && validateIfPTDueExists=='') ||(isAnonymousUser=='true' && validateIfPTDueExists=='false'))
		{
		$(".show-row").hide(); 
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
		bootbox.alert($('#validationMessage').val());
	
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
						if(response.ownerNames[0].emailId != '')
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
						$("#propertytaxdue").html(response.propertyDetails.taxDue);
										
				}, 
				error: function (response) {
					console.log("failed");
				}
			});
		}		
	}
	
	$("#propertytaxdue").addClass("error-msg");
	
	deptapp=$('#approvalDepartment').val() ;
	if(deptapp.length != 0){
	$.ajax({
		url: "/eis/ajaxWorkFlow-getDesignationsByObjectType",     
		type: "GET",
		data: {
			approvalDepartment : $('#approvalDepartment').val(),
			departmentRule : $('#approvalDepartment').find("option:selected").text(),
			type : $('#stateType').val(),
			currentState : $('#currentState').val(),
			amountRule : $('#amountRule').val(),
			additionalRule : $('#additionalRule').val(),
			pendingAction : $('#pendingActions').val()
		},
		dataType: "json",
		success: function (response) {
			console.log("success"+response);
			$('#approvalDesignation').empty();
			
			$.each(response, function(index, value) {
				$('#approvalDesignation').append($('<option>').text(value.name).attr('value', value.id));
			});
			
		}, 
		error: function (response) {
			bootbox.alert('json fail');
			console.log("failed");
		}
	});
	if($('#approvalPosOnValidate').val().length != 0){
	$.ajax({
		url: "/wtms/ajaxconnection/assignmentByPositionId",     
		type: "GET",
		data: {
			approvalPositionId : $('#approvalPosOnValidate').val(),
			
		},
		dataType: "json",
		success: function (response) {
			console.log("success"+response);
			$('#approvalPosition').empty();
			$.each(response, function(index, value) {
				$('#approvalPosition').append($('<option>').text(value.userName+'/'+value.positionName).attr('value', value.positionId));  
			});
			
		}, 
		error: function (response) {
			console.log("failed");
		}
	});
	}
	}
	
});
