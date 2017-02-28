/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
	
	$('#connectionType').change(function(){
			if($('#connectionType').val()==='METERED')
			{
				
				$('.showfields').show();
				$('#waterSupplyType').attr('required', 'required');
				$("#buildingName").attr('required','required');
			}
			else
			{
				
				$('.showfields').hide();
				$('#waterSupplyType').removeAttr('required');
				$('#buildingName').removeAttr('required');
				$("#waterSupplyType").val('');
				$("#buildingName").val('');
			}
	});
	
	if($('#noJAORSAMessage').val()!='')
		bootbox.alert($('#noJAORSAMessage').val());

	var mode =$('#mode').val();
	var validateIfPTDueExists=$('#validateIfPTDueExists').val();
	var currentloggedInUser=$('#currentUser').val();
	if((currentloggedInUser=='true' && mode=='changeOfUse' && validateIfPTDueExists=='') ||(currentloggedInUser=='true' && validateIfPTDueExists=='false'))
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
	$('#propertyType').change(function(){
		 //loadPropertyCategories();
		 loadPropertyUsageTypes();
		 loadPropertyPipeTypes();
});
	function loadPropertyPipeTypes(){
	
      $.ajax({
			url: "/wtms/ajax-PipeSizesByPropertyType",     
			type: "GET",
			data: {
				propertyType: $('#propertyType').val()  
			},
			dataType: "json",
			success: function (response) {
			    console.log("success"+response);
				$('#pipeSize').empty();
				$('#pipeSize').append($("<option value=''>Select from below</option>"));
				$.each(response, function(index, value) {
				$('#pipeSize').append($('<option>').text(value.code).attr('value', value.id))
				});
			}, 
			error: function (response) {
				console.log("failed");
			}
		});
		
	}
	function loadPropertyUsageTypes(){
		$.ajax({
			url: "/wtms/ajax-UsageTypeByPropertyType",     
			type: "GET",
			data: {
				propertyType: $('#propertyType').val()  
			},
			dataType: "json",
			success: function (response) {
				console.log("success"+response);
				$('#usageType').empty();
				$('#usageType').append($("<option value=''>Select from below</option>"));
				$.each(response, function(index, value) {
					$('#usageType').append($('<option>').text(value.name).attr('value', value.id))
				});
			}, 
			error: function (response) {
				console.log("failed");
			}
		});
	}
	
	function loadPropertyCategories(){
		
	$.ajax({
		url: "/wtms/ajax-CategoryTypeByPropertyType",     
		type: "GET",
		data: {
			propertyType: $('#propertyType').val() ,
			connectionType: $('#typeOfConnection').val()
		},
		dataType: "json",
		success: function (response) {
			console.log("success"+response);
			$('#connectionCategorie').empty();
			$('#connectionCategorie').append($("<option value=''>Select from below</option>"));
			$.each(response, function(index, value) {
				$('#connectionCategorie').append($('<option>').text(value.name).attr('value', value.id))
			});
			
		}, 
		error: function (response) {
			console.log("failed");
		}
	});
	}
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
						$("#propertytaxdue").html(response.propertyDetails.taxDue);
						
						   
						if(parseInt($("#propertytaxdue").html()) > 0){
							$("#propertytaxdue").addClass("error-msg");
						}
										
				}, 
				error: function (response) {
					console.log("failed");
				}
			});
		}		
	}
	
	$("#waterSourceDropdown").prop("disabled", true);
	$("#connectionCategorie").prop("disabled", true);
	
	
	var connectionType = $('#connectionType').val();
	var usageType = $('#usageType').val();
	var category = $('#category').val();
	var propertyType = $('#propertyType').val();
	var pipeSize = $('#pipeSize').val();
	
	$(".workAction").click(function (){
		if(($('#connectionType').val()==connectionType) && ($('#usageType').val()==usageType) 
				&&($('#propertyType').val()==propertyType)
				&& ($('#pipeSize').val()==pipeSize)) {
			bootbox.alert("Please modify at least one mandatory field");
			return false;
		}
	});
	
	$('#connectionType,#usageType,#propertyType,#pipeSize').change(function (){
		$(".btn-primary").show();
	});
	
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
				//$('#approvalPosition').append($('<option>').text(value.name).attr('value', value.id));
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
