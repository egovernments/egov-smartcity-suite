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
	
	$('#donationMasterTbl').dataTable({
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 hidden col-xs-12'i><'col-md-3 hidden col-xs-6'l><'col-md-3 hidden col-xs-6 text-right'p>>",
		"autoWidth": false,
		"destroy":true,
		/* Disable initial sort */
		"paging":false,
        "aaSorting": [],
		"oLanguage": {
			"sInfo": ""
		},
		"columnDefs": [ {
			"targets": 9,
			"orderable": false
		} ]
	});
	 $('#propertyType').change(function(){
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
	  });
	  
	  $('#propertyType').change(function(){
		  $.ajax({
				url: "/wtms/ajax-PipeSizesByPropertyType",     
				type: "GET",
				data: {
					propertyType: $('#propertyType').val()  
				},
				dataType: "json",
				success: function (response) {
				    console.log("success"+response);
					$('#minpipeSize').empty();
					$('#minpipeSize').append($("<option value=''>Select from below</option>"));
					$.each(response, function(index, value) {
					$('#minpipeSize').append($('<option>').text(value.code).attr('value', value.id))
					});
				}, 
				error: function (response) {
					console.log("failed");
				}
			});
		  
	  });
	  
	  $('#statusdiv').hide();
		
		var activeDiv = $('#reqAttr').val();
		
		if (activeDiv =='false')
			{
			$('#statusdiv').hide();
		     $('#addnewid').hide();
		     $('#resetid').show();
			}
		else
			{
			$('#resetid').hide();
			$('#statusdiv').show();
			 $('#addnewid').show();
			}
	  
	  $('#buttonid').click(function() {
		  if ($( "#donationDetailsform" ).valid())
			  {
			  if($('#effectiveDate').val() != '' && $('#toDate').val() != ''){
					var start = $('#effectiveDate').val();
					var end = $('#toDate').val();
					var stsplit = start.split("/");
						var ensplit = end.split("/");
						
						start = stsplit[1] + "/" + stsplit[0] + "/" + stsplit[2];
						end = ensplit[1] + "/" + ensplit[0] + "/" + ensplit[2];
						if(!validRange(start,end))
						{
						return false;
						}
				}
			  
			  var val = parseFloat($('#donationAmount').val());
			  if (isNaN(val) || (val === 0)  )
			  {
				  bootbox.alert("Please Enter Donation amount");
			      return false;
			     
			  }
				   var minimum = getMinimumPipeSizeInInch();
					var maximum = getMaximumPipeSizeInInch();
					if( (minimum > 0) && (maximum  > 0) ){
						if (minimum > maximum){
							bootbox.alert("Minimum PipeSize should not be greater than the maximum PipeSize");
							return false;
						}
						else if(minimum == maximum) {
							bootbox.alert("Minimum PipeSize should not be same as maximum PipeSize");
							return false;
					    }
						else{
								  if($('#effectiveDate').val() !=undefined)
							     donationheadercombination();
						}
					}
			  }
		  		});
	  
	  $("#resetid").click(function(){
			$("#donationDetailsform")[0].reset();
			window.open("/wtms/masters/donationMaster/", "_self");
			});

     });

function validRange(start, end) {
    var startDate = Date.parse(start);
    var endDate = Date.parse(end);
	
    // Check the date range, 86400000 is the number of milliseconds in one day
    var difference = (endDate - startDate) / (86400000 * 7);
    if (difference < 0) {
    	bootbox.alert("From date should not be greater than the To Date.");
		$('#end_date').val('');
		return false;
		} else {
		return true;
	}
    return true;
}

function getMinimumPipeSizeInInch() {
	var minPipeSize = "";
	$.ajax({
        url: '/wtms/ajax-minimumpipesizeininch',
        type: "GET",
        async: false,
        data: {
        	minPipeSize :$('#minpipeSize').val()
        },
        dataType : 'json',
        success: function (response) {
			console.log("success"+response);
			minPipeSize = response;
		},error: function (response) {
			console.log("failed");
		}
    });
	return minPipeSize;
}
function getMaximumPipeSizeInInch()
{
	var maxPipeSize = "";
	$.ajax({
        url: '/wtms/ajax-maximumpipesizeininch',
        type: "GET",
        async: false,
        data: {
        	maxPipeSize :$('#pipeSize').val()
        },
        dataType : 'json',
        success: function (response) {
			console.log("success"+response);
			maxPipeSize = response;
		},error: function (response) {
			console.log("failed");
		}
    });
	return maxPipeSize;
	}
function donationheadercombination()
{
	var activeDiv = $('#reqAttr').val();
	if (activeDiv =='false')
		{
		var activeid = true;
		}
	else{
		var activeid = ( $("#activeid").is(':checked') ) ? true : false;
	    }
	$.ajax({
        url: '/wtms/ajax-donationheadercombination',
        type: "GET",
        data: {
        	propertyType: $('#propertyType').val(),
        	categoryType: $('#connectionCategorie').val(),
        	usageType: $('#usageType').val(),
        	maxPipeSize :$('#pipeSize').val(),
        	minPipeSize :$('#minpipeSize').val(),
        	fromDate : $('#effectiveDate').val(),
        	toDate : $('#toDate').val(),
        	activeid : activeid
        },
        dataType : 'json',
        success: function (response) {
			console.log("success"+response);
			if(response){
				response=JSON.parse(response);
				bootbox.alert("For the Selected Combination, record already present with the H.S.C Pipesize range : "+response.maxPipeSize+" and "+response.minPipeSize +" and date range "+response.fromDate+ "and "+response.toDate);
				return false;
    			}
			else{
				 document.forms[0].submit();
				 return true;
			}
		},error: function (response) {
			console.log("failed");
		}
    });
	
}

function overwritedonation(res)
{
	document.forms[0].submit();
	
}

function edit(donationHeader)
{
	window.open("/wtms/masters/donationMaster/edit/"+donationHeader, "_self");
	
	
}

$('#addnewid').click(function() {
	window.open("/wtms/masters/donationMaster/", "_self");
});