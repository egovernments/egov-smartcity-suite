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
	
	$('#addConnection').click(function() {
		var url = '/wtms/application/addconnection/'+ $('#consumerCode').val();
		$('#waterConnectionSuccess').attr('method', 'get');
		$('#waterConnectionSuccess').attr('action', url);
		$('#waterConnectionSuccess').attr('mode', 'search');
		window.location = url;
	});
	
	$('#changeConnection').click(function() {
		var url = '/wtms/application/changeOfUse/'+$('#consumerCode').val();
		$('#waterConnectionSuccess').attr('method', 'get');
		$('#waterConnectionSuccess').attr('action', url);
		$('#waterConnectionSuccess').attr('mode', 'search');
		window.location = url;
	});
	
	$('#closureConnection').click(function() {
		var url = '/wtms/application/close/'+$('#consumerCode').val();
		$('#waterConnectionSuccess').attr('method', 'get');
		$('#waterConnectionSuccess').attr('action', url);
		$('#waterConnectionSuccess').attr('mode', 'search');
		window.location = url;
	});
	
	$('#dcbscreen-view').click(function() {
		var url = '/wtms/viewDcb/consumerCodeWis/'+$('#consumerCode').val();
		$('#waterConnectionSuccess').attr('method', 'get');
		$('#waterConnectionSuccess').attr('action', url);
		$('#waterConnectionSuccess').attr('mode', 'search');
		window.location = url;
	});
	
	$('#meter-entry').click(function() {
		var url = '/wtms/application/meterentry/'+$('#consumerCode').val();
		$('#waterConnectionSuccess').attr('method', 'get');
		$('#waterConnectionSuccess').attr('action', url);
		$('#waterConnectionSuccess').attr('mode', 'search');
		window.location = url;
	});
		
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
	
	$('#re-connection').click(function() {
		var url = '/wtms/application/reconnection/'+$('#consumerCode').val();
		$('#waterConnectionSuccess').attr('method', 'get');
		$('#waterConnectionSuccess').attr('action', url);
		$('#waterConnectionSuccess').attr('mode', 'search');
		window.location = url;
	});
	$('#viewRecOonnAck').click(function() {
		var url = '/wtms/application/ReconnacknowlgementNotice/view/'+ $('#applicationCode').val();
		$('#waterConnectionSuccess').attr('method', 'get');
		$('#waterConnectionSuccess').attr('action', url);
		$('#waterConnectionSuccess').attr('mode', 'search');
		window.location = url;
	});
	$('#viewClosureAck').click(function() {
		var url = '/wtms/application/acknowlgementNotice/view/'+ $('#applicationCode').val();
		$('#waterConnectionSuccess').attr('method', 'get');
		$('#waterConnectionSuccess').attr('action', url);
		$('#waterConnectionSuccess').attr('mode', 'search');
		window.location = url;
	});
	
});

$('#payBtn').click(function() {
	var url = '/wtms/application/generatebill/'+ $('#applicationCode').val()+"?applicationTypeCode="+$('#applicationTypeCode').val();
	$('#waterConnectionSuccess').attr('method', 'post');
	$('#waterConnectionSuccess').attr('action', url);
})

$('#viewdcb').click(function() {
	var url = '/wtms/viewDcb/consumerCodeWis/'+$('#applicationCode').val();
	$('#waterConnectionSuccess').attr('method', 'get');
	$('#waterConnectionSuccess').attr('action', url);
});