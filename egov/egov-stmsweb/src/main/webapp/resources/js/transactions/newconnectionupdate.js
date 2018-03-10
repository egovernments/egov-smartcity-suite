/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces, 
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any 
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines, 
 *            please contact contact@egovernments.org
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
 *
 */
$(document).ready(function()
{
	 var appStatus =$('#statuscode').val();
	 if(appStatus=='ESTIMATIONNOTICEGENERATED'){
			$("#Forward").hide();
	 }  
	 
	var mode =$('#mode').val();
	if(mode == 'editOnReject'){
		loadPropertyDetails();
		$("#propertyIdentifier").prop("readonly", true);
	}
	var showApprovalDtls = $('#showApprovalDtls').val(); 
	if(showApprovalDtls == 'no' || appStatus=='ESTIMATIONNOTICEGENERATED'){ 
		$(".show-row").hide(); 
		$('#approverDetailHeading').hide();
		$('#approvalDepartment').removeAttr('required');
		$('#approvalDesignation').removeAttr('required');
		$('#approvalPosition').removeAttr('required');
	} else {
		$(".show-row").show(); 
		$('#approverDetailHeading').show();
		$('#approvalDepartment').attr('required', 'required');
		$('#approvalDesignation').attr('required', 'required');
		$('#approvalPosition').attr('required', 'required');
	}
	
	$("form").submit(function() {
		if($('form').valid())	{
			$('.loader-class').modal('show', {backdrop: 'static'});
		}
		else 
			$('.loader-class').modal('hide');
	});
		
	$(".btnWorkflow").click(function() { 
		 var action = document.getElementById("workFlowAction").value;
		 var status=$('#statuscode').val();
		 
		 if(action != 'Reject' && (status=='INSPECTIONFEEPAID' || status=='CREATED')){
			 if(!validateInspectionDetailsOnSubmit()){
					return false;
				}
				
				if(!validateEstimationDetailsOnSubmit()){
					return false;
				}
		 }
		 
		 if(action=='Approve'){
			 	$('#approvalDepartment').removeAttr('required');
				$('#approvalDesignation').removeAttr('required');
				$('#approvalPosition').removeAttr('required');			 
		 }
		 
		 if(action == 'Reject' && (status=='INITIALAPPROVED' || status=='INSPECTIONFEEPAID' || status=='CREATED')) { 
			 $('#Reject').attr('formnovalidate','true');
			 bootbox.confirm("Do you really want to Reject the application?", function(result){
				if(result){
					var approvalComent=$('#approvalComent').val();
					  if(approvalComent == "") {
						  bootbox.alert("Please enter rejection comments!");
							$('#approvalComent').focus();
							return true; 
					  }
					  else {
						  validateWorkFlowApprover(action);
						  document.forms[0].submit();
					  }
				}
				
			 });
			 return false;
		}  

		 if(action == 'Cancel') { 
			 $('#Cancel').attr('formnovalidate','true');
			 bootbox.confirm("Do you really want to Cancel the application?", function(result){
					if(result){
						var approvalComent=$('#approvalComent').val();
						  if(approvalComent == "") {
							  bootbox.alert("Please enter cancellation comments!");
							  return true; 
						  }
						  else {
							  validateWorkFlowApprover(action);
							  document.forms[0].submit();
						  }
					}
					
				 });
				 return false;
		}  
		 
		 if((action == 'Generate Estimation Notice')) {
			 document.forms[0].submit();	
		 }
		 else if(status=='WORKORDERGENERATED') {
			 if(!validateTapExecutionDate())
				 return false;
		 }
		 
		validateWorkFlowApprover(action); 
		if($('form').valid())
			document.forms[0].submit();	
		return;
	});	
		
	$('#executionDate').blur(function (){
		validateTapExecutionDate();
	});
	
	function validateDateRange(fromDate, toDate) {
		if (fromDate != "" && toDate != "") {
			var stsplit = fromDate.split("/");
			var ensplit = toDate.split("/");
			
			var startDate = Date.parse(stsplit[1] + "/" + stsplit[0] + "/" + stsplit[2]);
			var endDate = Date.parse(ensplit[1] + "/" + ensplit[0] + "/" + ensplit[2]);
			
	        // Check the date range, 86400000 is the number of milliseconds in one day
	        var difference = (endDate - startDate) / (86400000 * 7);
	        if (difference < 0) {
				return false;
				} 
	        else {
				return true;
			}	
	    }
	    return true;		
	}
	
	function validateTapExecutionDate() {
		var applicationDate = $('#appDate').val();	 
		var executionDate = $('#executionDate').val();
		
		if(applicationDate !='' && executionDate != '') {
			if(validateDateRange(applicationDate, executionDate)) {
				validateWorkFlowApprover(action);
		    	document.forms[0].submit();	
			}
			else{
				bootbox.alert("The Execution Date can not be less than the Date of Application.");
				$('#executionDate').val('');
				return false;	
			}
		}
		return true;
	}
	
	$('.slide-history-menu').click(function(){
		$(this).parent().find('.history-slide').slideToggle();
		if($(this).parent().find('#toggle-his-icon').hasClass('fa fa-angle-down'))
		{
			$(this).parent().find('#toggle-his-icon').removeClass('fa fa-angle-down').addClass('fa fa-angle-up');
			//$('#see-more-link').hide();
			}else{
			$(this).parent().find('#toggle-his-icon').removeClass('fa fa-angle-up').addClass('fa fa-angle-down');
			//$('#see-more-link').show();
		}
	});
	
});

function loadPropertyDetails() {
	propertyID=$('#propertyIdentifier').val()
	
	if(propertyID != '') {
		$.ajax({
			url: "/ptis/rest/property/"+propertyID,      
			type: "GET",
			dataType: "json",
			success: function (response) { 
						$('#propertyIdentifierError').html('');
						applicantName = '';
						for(i=0; i<response.ownerNames.length; i++) {
							if(applicantName == '')
								applicantName = response.ownerNames[i].ownerName;
							else 							
								applicantName = applicantName+ ', '+response.ownerNames[i].ownerName;
						}
						$("#applicantname").val(applicantName);
						$("#nooffloors").val(response.propertyDetails.noOfFloors);
						if(response.ownerNames[0].mobileNumber != '')
							$("#mobileNumber").val(response.ownerNames[0].mobileNumber);
						if(response.ownerNames[0].emailId != '')
							$("#email").val(response.ownerNames[0].emailId);
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
						$("#aadhaar").val(response.ownerNames[0].aadhaarNumber);
						$("#locality").val(response.boundaryDetails.localityName);
						$("#zonewardblock").val(boundaryData);
						$("#propertytax").val(response.propertyDetails.currentTax);
			}, 
			error: function (response) {
				console.log("failed"); 
			}
		});
	}		
}

$('#noOfClosetsResidential').blur(function(){
	var propertyType = $('#propertyType').val()
	var noOfClosetsResidential = $('#noOfClosetsResidential').val()
	
	if(noOfClosetsResidential==="0" ){
		bootbox.alert("Please enter number of closets value more than 0");
	}
	else if(noOfClosetsResidential != ''){
		validateClosets(propertyType , noOfClosetsResidential,true);
	} else{
		bootbox.alert("Please enter number of closets for property type Residential");
	}
});

$('#noOfClosetsNonResidential').blur(function(){
	var propertyType = $('#propertyType').val()
	var noOfClosetsNonResidential = $('#noOfClosetsNonResidential').val()
	if(noOfClosetsNonResidential==="0" ){
		bootbox.alert("Please enter number of closets value more than 0");
	}
	else if(noOfClosetsNonResidential != ''){
		validateClosets(propertyType , noOfClosetsNonResidential,false);
	} else{
		bootbox.alert("Please enter number of closets for property type Non Residential");
	}
	
});


function validateClosets(propertyType,noOfClosets,flag)
{
		$.ajax({
			url: "/stms/ajaxconnection/check-closets-exists",      
			type: "GET",
			data: {
				propertyType : propertyType , 
				noOfClosets : noOfClosets,
				flag : flag
			},
			dataType: "json",
			success: function (response) { 
				if(response != '') {
					bootbox.alert(response);
						resetClosetDetails();
				}
			}, 
			error: function () {
				
				bootbox.alert("Oops Something Wrong!!!!!");
			}
		});
}

function resetClosetDetails() {
	$('#noOfClosetsResidential').val('');
	$('#noOfClosetsNonResidential').val('');
}