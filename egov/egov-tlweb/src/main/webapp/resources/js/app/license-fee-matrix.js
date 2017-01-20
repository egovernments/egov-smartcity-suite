/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *  
 *  Copyright (C) 2017  eGovernments Foundation
 *  
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *  
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *  
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *  
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *  
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *  
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

$(document).ready(function()
{

$('#unitOfMeasurement').attr("disabled", true); 
	
$('#feeType').click(function(){
	if($('#subCategory').val()==""){
		bootbox.alert("Please Choose Sub Category");
		return false;
	}
});

$('#unitOfMeasurement').click(function(){
	if($('#feeType').val()==""){
		bootbox.alert("Please Choose Fee Type");
		return false;
	}
});
$('#feeType').change(function(){
	$.ajax({
		url: "/tl/feeType/uom-by-subcategory?feeTypeId="+$('#feeType').val()+"&subCategoryId="+$('#subCategory').val()+"",    
		type: "GET",
		dataType: "json",
		success: function (response) {
			$('#unitOfMeasurement').empty();
			$('#unitOfMeasurement').append($("<option value=''>Select</option>"));
			$.each(response, function(index, value) {
			     $('#unitOfMeasurement').append("<option value="+value.uom.id+">"+value.uom.name+"</option>");
			     $('#rateType').val(value.rateType);
			});
			$("#unitOfMeasurement").prop("selectedIndex",1);
		}, 
		error: function () {
		} 
	});
});
$('#subCategory').change(function(){
	$('#unitOfMeasurement').empty();
	$('#unitOfMeasurement').append($("<option value=''>Select</option>"));
	$('#rateType').val("");
	$.ajax({
		url: "/tl/feeType/feetype-by-subcategory",    
		type: "GET",
		data: {
			subCategoryId : $('#subCategory').val()   
		},
		dataType: "json",
		success: function (response) {
			var feeType = $('#feeType')
			feeType.find("option:gt(0)").remove();
			$.each(response, function(index, value) {
				feeType.append($('<option>').text(value.feeType.name).attr('value', value.feeType.id));
			});
			
		}, 
		
	})
});
$('#subCategory').select2({
	placeholder: "Select",
	width:'100%'
});
$('#licenseCategory').change(function(){
	var results = [];
	$('#feeType').empty();
	$('#feeType').append($("<option value=''>Select</option>"));
	$('#unitOfMeasurement').empty();
	$('#unitOfMeasurement').append($("<option value=''>Select</option>"));
	$('#rateType').val("");
	$.ajax({
		url: "/tl/licensesubcategory/subcategories-by-category",    
		type: "GET",
		data: {
			categoryId : $('#licenseCategory').val()   
		},
		dataType: "json",
		 success: function(data) {
	            $.each(data, function(i) {
	                var obj = {};
	                obj['id'] = data[i]['id']
	                obj['text'] = data[i]['name'];
	                results.push(obj);
	            });
	            $("#subCategory").empty();
	            $("#subCategory").append("<option value=''>Select</option>");
	            $("#subCategory").select2({
	                placeholder: "Select",
	                width:'100%',
	                data: results
	            });
	        },
	        error: function() {
	        	bootbox.alert('something went wrong on server');
	        } 
	})
});

$( "#save" ).click(function() {
	
	if($('#feematrix-new').valid()){
		var natureOfBusinessDisabled=$('#natureOfBusiness').is(':disabled');
		var licenseAppTypeDisabled=$('#licenseAppType').is(':disabled');
		var unitOfMeasurementDisabled=$('#unitOfMeasurement').is(':disabled');
		$('#natureOfBusiness').removeAttr("disabled");
		$('#licenseAppType').removeAttr("disabled");
		$('#unitOfMeasurement').removeAttr("disabled");
		var fd=$('#feematrix-new').serialize();
		
		  $.ajax({
				url: "/tl/feematrix/create",
				type: "POST",
				data: fd,
			    beforeSend: function() {
			    	$("#save").attr('disabled',true);
			    },
				//dataType: "text",
				success: function (response) {
					 $('#resultdiv').html(response);
					 if(natureOfBusinessDisabled)
						 $('#natureOfBusiness').attr("disabled", true); 
					 if(licenseAppTypeDisabled)
						 $('#licenseAppType').attr("disabled", true); 
					 if(unitOfMeasurementDisabled)
						 $('#unitOfMeasurement').attr("disabled", true); 
					 bootbox.alert("Details saved Successfully");
				}, 
				error: function () {
					if(natureOfBusinessDisabled)
						$('#natureOfBusiness').attr("disabled", true); 
					if(licenseAppTypeDisabled)
						$('#licenseAppType').attr("disabled", true);
					if(unitOfMeasurementDisabled)
						 $('#unitOfMeasurement').attr("disabled", true); 
					bootbox.alert("Failed to Save Details");
				},
			    complete: function() {
			  	  $("#save").removeAttr('disabled');
			    }
			});
		}
 });
$( "#search" ).click(function() {
	$('#resultdiv').empty();
	var valid = $('#feematrix-new').validate().form();
	if(!valid)
		{
		bootbox.alert("Please fill mandatory fields");
		return false;
		}
	  var param="uniqueNo=";
	  param=param+$('#natureOfBusiness').val()+"-";
	  param=param+$('#licenseAppType').val()+"-";
	  param=param+$('#licenseCategory').val()+"-";
	  param=param+$('#subCategory').val()+"-";
	  param=param+$('#feeType').val()+"-";
	  param=param+$('#unitOfMeasurement').val()+"-";
	  param=param+$('#financialYear').val(); 
	   $.ajax({
			url: "/tl/feematrix/search?"+param,
			type: "GET",
			//dataType: "json",
			success: function (response) {
				 $('#resultdiv').html(response);
			}, 
			error: function () {
			}
		});
   });
$("#add-row").click(
		function(event) {
			var rowCount = $('#result tbody tr').length;
			var valid = true;
			//validate all rows before adding new row
			$('#result tbody tr').each(function(index,value){
				$('#result tbody tr:eq('+index+') td input[type="text"]').each(function(i,v){
					if(!$.trim($(v).val())){
						valid = false;
						bootbox.alert("Enter all values for existing rows!",function(){
							$(v).focus();
						});
						return false;
					}
				});
			});
			if(valid){
				//Create new row
				var newRow = $('#result tbody tr:first').clone();
				newRow.find("input").each(function(){
	    	        $(this).attr({
	    	        	'name': function(_, name) { return name.replace(/\[.\]/g, '['+ rowCount +']'); }
	    	        });
	    	    });
				$('#result tbody').append(newRow);
				var prev_tovalue = $('#result tbody tr:eq('+(rowCount-1)+')').find('input.tovalue').val();
				$('#result tbody tr:last').find('input').val('');
				$('#result tbody tr:last').find('input.fromvalue').val(prev_tovalue);
				patternvalidation();
			}
		});


$('#result tbody').on('click','tr td .delete-row',function(e){
	var id = $(this).closest('tr').find('td:eq(0) .detailId').val();
	//console.log(id)
	var idx = $(this).closest('tr').index();
	if(idx == 0){
		bootbox.alert('Cannot delete first row!');
	}else if((idx < ($('#result tbody tr').length - 1))){
		bootbox.alert('Try to delete from last row!');
	}else{
		bootbox.confirm("This will delete the row permanently. Press OK to Continue. ",function(result) {
			if(result){
				if(!id){
					$('#result tbody tr:last').remove();
				}else{
					$.ajax({
						url: '../feematrix/deleterow?feeMatrixDetailId='+id,
						type : "GET",
						success : function(response) {
							$('#result tbody tr:last').remove();
						},
						error : function(response) {
							bootbox.alert("Unable to delete this row.");
						}
					});
				}
			}
		});
	}
 });
});