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
$(document).ready(function(){
	
	loadPropertyDetails();
	mode=$('#mode').val()
	if(mode == 'create')
		$('#propertyIdentifier').val($('#ptAssessmentNo').val());
	$('#propertyIdentifier').blur(function(){
		validateSewerageConnection();
	});
	
	$('#noOfClosetsResidential').blur(function(){
		var propertyType = $('#propertyType').val()
		var noOfClosetsResidential = $('#noOfClosetsResidential').val()
		if(noOfClosetsResidential==="0" ){
			bootbox.alert("Please enter number of closets value more than 0");
		}
		else if(noOfClosetsResidential != ''){
			validateClosets(propertyType , noOfClosetsResidential,true);
		} else{
			bootbox.alert("Please Enter Closets For Property Type Residential");
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
			bootbox.alert("Please Enter Closets For Property Type Non Residential");
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
				error: function (response) {
					
					bootbox.alert("Oops Something wrong!!");
				}
			});
	}
	
	function resetClosetDetails() {
		$('#noOfClosetsResidential').val('');
		$('#noOfClosetsNonResidential').val('');
	}
	
	function validateSewerageConnection() {
		var propertyID=$('#propertyIdentifier').val();
		if(propertyID != '') {
			$.ajax({
				url: "/stms/ajaxconnection/check-connection-exists",      
				type: "GET",
				data: {
					propertyID : propertyID  
				},
				dataType: "json",
				success: function (response) { 
					if(response != '') {
							resetPropertyDetails();
							bootbox.alert(response);
					}
					else {
						loadPropertyDetails();
					}
				}, 
				error: function (response) {
					resetPropertyDetails();
					bootbox.alert("connection validation failed");
				}
			});
		} else {
			resetPropertyDetails();
			}
	}
});


function loadPropertyDetails() {
	propertyID=$('#propertyIdentifier').val()
	allowIfPTDueExists = $('#allowIfPTDueExists').val() 
	var errorMessage=""; 
	var subErrorMessage="";
	if(propertyID != '') {
		$.ajax({
			url: "/ptis/rest/property/"+propertyID,      
			type: "GET",
			dataType: "json",
			success: function (response) { 
				var waterTaxDue = getWaterTaxDue(propertyID);
				if(response.errorDetails.errorCode != null && response.errorDetails.errorCode != '') {
					if($('#legacy'))
					{
				    resetPropertyDetails();
					}
					bootbox.alert(response.errorDetails.errorMessage);
				}
				else {
					if($('#applicationCode').val()== 'NEWSEWERAGECONNECTION')
						subErrorMessage=" create new sewerage connection.";
					else
						subErrorMessage=" apply for change in closets.";
					if(allowIfPTDueExists=='false' &&response.propertyDetails.taxDue > 0) {
						errorMessage = "For entered Property tax Assessment number "+propertyID+" demand is due Rs."+ response.propertyDetails.taxDue+"/-. Please clear demand and"+subErrorMessage;
					}
					if(typeof waterTaxDue != 'undefined'  && waterTaxDue['WATERTAXDUE'] > 0 ) {
						errorMessage += "For entered Property tax Assessment number "+propertyID+" linked water tap connection demand with Consumer code:"+waterTaxDue['CONSUMERCODE'][0]+" is due Rs."+ waterTaxDue['WATERTAXDUE']+"/- . Please clear demand and"+subErrorMessage;
					}
					if((allowIfPTDueExists=='false' && response.propertyDetails.taxDue > 0) || typeof waterTaxDue != 'undefined' && waterTaxDue['WATERTAXDUE'] > 0) {
						resetPropertyDetails();	
						bootbox.alert(errorMessage);
					}
					else {					
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
						if(typeof waterTaxDue != 'undefined' && waterTaxDue['PROPERTYID']!="")
							$('#watercharges').val(waterTaxDue['CURRENTWATERCHARGE']);
						else
							$('#watercharges').val('N/A');
					}
				}					
			}, 
			error: function (response) {
				console.log("failed");
			}
		});
	}		
}

function resetPropertyDetails() {
	$('#propertyIdentifier').val('');
	$('#applicantname').val('');
	$('#mobileNumber').val('');
	$('#email').val('');
	$('#aadhaar').val('');	
	$('#propertyaddress').val('');
	$('#locality').val('');
	$('#zonewardblock').val('');
	$('#nooffloors').val(''); 
	$('#propertytax').val('0.00');
	$('#watercharges').val('0.00');

}

function getWaterTaxDue(propertyID) {
	var result;
	if(propertyID != "") {
		$.ajax({
			url: "/stms/ajaxconnection/check-watertax-due",
				type: "GET",
				'async':false,
				cache: true,
				data: {
					assessmentNo : propertyID
				},
				dataType: "json",
		}).done(function(value) {
				result = value; 
		});
		return result;
	}
}

