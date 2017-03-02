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
	$('#save-button').hide();
	  $('#buttonid').click(function() {
		  loadPropertyDetails();
		  loadConnectionDetails();
		  $('#save-button').show();
		  ///
	  });

	  
	  $('#search-span').click(function() {
		  loadActivePropertyDetails();
		  loadActiveConnectionDetails();
		  $('#save-button').show();
	  });
	  
	  
	  $('#buttonsave').click(function() {
		  if ($( "#linkedAssessmentform" ).valid())
		  {
		  document.forms[0].submit();
		  }
	  });
	  
});


function loadPropertyDetails() {
	consumerCode=$('#consumerCode').val();
	if(consumerCode != '') {
		propertyID=getPropertyId(consumerCode);
	}
	propertyID=$('#assessmentNo').val();


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
						$("#ptassessmentNo").val(response.propertyID);
						$("#ownerName").val(applicantName);
						$("#address").val(response.propertyAddress);
						if (response.status==true)
						   $("#status").val("ACTIVE");
						else
							$("#status").val("INACTIVE");
						
						$('#assessmentdetailsid').removeClass('hide');
						$('#activeassessmentdetailsid').removeClass('hide');
						
			}, 
			error: function (response) {
				console.log("failed");
			}
		});
	}		
}

function loadActivePropertyDetails() {
	propertyID=$('#activePropertyIdentifier').val()
	
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
						$("#activePropertyIdentifier").val(response.propertyID);
						$("#actOwnerName").val(applicantName);
						$("#actAddress").val(response.propertyAddress);
						if (response.status==true)
							   $("#actstatus").val("ACTIVE");
							else
								$("#actstatus").val("INACTIVE");
						$('#activeassessmentdetailsid').removeClass('hide');
						
			}, 
			error: function (response) {
				console.log("failed");
			}
		});
	}		
}

function getPropertyId(consumerno)
{

	var propertyId = "";
	consumerno = $('#consumerNo').val();
	    $.ajax({
	        url: '/wtms/ajax-getPropertyIdByConsumerCode',
	        type: "GET",
	        async: false,
	        data: {
	        	consumerCode: consumerno,
	        },
	        dataType : 'json',
	        success: function (response) {
				console.log("success"+response);
				propertyId = response;
			},error: function (response) {
				console.log("failed");
			}
	    });
	    return propertyId;
	}




function loadActiveConnectionDetails()
{
	propertyID=$('#activePropertyIdentifier').val()
	if(propertyID != '') {

		$.ajax({
			url: "/wtms/rest/watertax/connectiondetails/byptno/"+propertyID,      
			type: "GET",
			dataType: "json",
			success: function (response) { 
				loadActiveConnectionDetailsTbl(response);
				$('#activeconnectiondetailsid').removeClass('hide');
			}, 
			error: function (response) {
				console.log("failed");
			}
		});
		
	}
	}


function loadConnectionDetails()
{
	
	consumerCode=$('#consumerCode').val();

	
	if(consumerCode != '') {
		propertyID=getPropertyId(consumerCode);

	}
	propertyID=$('#assessmentNo').val();
	if(propertyID != '') {
		$.ajax({
			url: "/wtms/rest/watertax/connectiondetails/byptno/"+propertyID,      
			type: "GET",
			dataType: "json",
			success: function (response) { 
				loadWaterSourceTbl(response);
				$('#connectiondetailsid').removeClass('hide');
				$('#activeconnectiondetailsid').removeClass('hide');
			}, 
			error: function (response) {
				console.log("failed");
			}
		});
		
	}
	}


function loadWaterSourceTbl(waterConnections)
{
	$('#waterSourceTbl tbody').empty();
	$.each(waterConnections,function(i,waterConnection){
		primaryconn = '';
		if (waterConnection.isPrimaryConnection == true)
			primaryconn = "Yes";
		else
			primaryconn = "No";
		var $tr = $('<tr>').append(
	            $('<td>').text(waterConnection.consumerCode),
	            $('<td>').text(primaryconn),
	            $('<td>').text(waterConnection.totalTaxDue),
	            $('<td>').text(waterConnection.connectionStatus)
	    );
		$('#waterSourceTbl tbody').append($tr);
	});
}

function loadActiveConnectionDetailsTbl(waterConnections)
{

	$('#activeConnDetailsTbl tbody').empty();
	$.each(waterConnections,function(i,waterConnection){
		primaryconn = '';
		console.log(waterConnection);
		if (waterConnection.isPrimaryConnection == true)
			primaryconn = "Yes";
		else
			primaryconn = "No";
		var $tr = $('<tr>').append(
	            $('<td>').text(waterConnection.consumerCode),
	            $('<td>').text(primaryconn),
	            $('<td>').text(waterConnection.totalTaxDue),
	            $('<td>').text(waterConnection.connectionStatus)
	    );
		$('#activeConnDetailsTbl tbody').append($tr);
	});
}


