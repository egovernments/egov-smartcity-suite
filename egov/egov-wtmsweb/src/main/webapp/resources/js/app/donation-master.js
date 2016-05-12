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
	
	$('#donationMasterTbl').dataTable({
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 hidden col-xs-12'i><'col-md-3 hidden col-xs-6'l><'col-md-3 hidden col-xs-6 text-right'p>>",
		"autoWidth": false,
		"destroy":true,
		/* Disable initial sort */
        "aaSorting": [],
		"oLanguage": {
			"sInfo": ""
		},
		"columnDefs": [ {
			"targets": 8,
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
							bootbox.alert("Minimum PipeSize  should not be greater than the maximum PipeSize");
							return false;
						}else{
								  if($('#effectiveDate').val() !=undefined)
							     donationheadercombination();
						}
					}
			  }
		  		});
	  
	  $('#listid').click(function() {
			window.open("/wtms/masters/donationMaster/list", "_self");
		 });
	  
	  $('#addnewid').click(function() {
		  window.open("/wtms/masters/donationMaster/", "_self");
			
	  });
	  
	  $('#resetid').click(function() {
		  document.forms[0].reset();
		  window.open("/wtms/masters/donationMaster/", "_self");
			
	  });


     });


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
	$.ajax({
        url: '/wtms/ajax-donationheadercombination',
        type: "GET",
        data: {
        	propertyType: $('#propertyType').val(),
        	categoryType: $('#connectionCategorie').val(),
        	usageType: $('#usageType').val(),
        	maxPipeSize :$('#pipeSize').val(),
        	minPipeSize :$('#minpipeSize').val()
        },
        dataType : 'json',
        success: function (response) {
			console.log("success"+response);
			if(response > 0){
				var res = overwritedonation(response)
				if(res==false)
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
	window.open("/wtms/masters/donationMaster/"+donationHeader, "_self");
	
	
}

function addNew()
{
	window.open("/wtms/masters/donationMaster/", "_self");
}
	