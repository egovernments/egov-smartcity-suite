
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

$(document).ready(function()
{

$('#unitOfMeasurement').attr("disabled", true); 
	
$('#feeType').click(function(event){
	if($('#subCategory').val()==""){
		bootbox.alert("Please Choose Sub Category");
		return false;
	}
});

$('#unitOfMeasurement').click(function(event){
	if($('#feeType').val()==""){
		bootbox.alert("Please Choose Fee Type");
		return false;
	}
});


$('#subCategory').change(function(){ 
	console.log("came onchange of "+$('#subCategory').val());
	$('#unitOfMeasurement').empty();
	$('#unitOfMeasurement').append($("<option value=''>Select</option>"));
	$('#rateType').val("");
	$.ajax({
		url: "/tl/domain/commonAjax-ajaxPopulateFeeType.action?subCategoryId="+$('#subCategory').val()+"",
		type: "GET",
		dataType: "json",
		success: function (response) {
			console.log("success"+JSON.stringify(response) );
			$('#feeType').empty();
			
			$('#feeType').append($("<option value=''>Select</option>"));
			$.each(response.Result, function(index, value) {
				
			     $('#feeType').append("<option value="+value.Value+">"+value.Text+"</option>");
			});
			console.log("completed"+response);
			
		}, 
		error: function (response) {
			console.log("failed");
		}
	});
});

$('#feeType').change(function(){
	console.log("came onchange of "+$('#feeType').val());
	var subCategoryValue=$('#subCategory').val();
	$.ajax({
		url: "/tl/domain/commonAjax-ajaxPopulateUom.action?feeTypeId="+$('#feeType').val()+"&subCategoryId="+$('#subCategory').val()+"",
		type: "GET",
		dataType: "json",
		success: function (response) {
			console.log("success"+JSON.stringify(response) );
			$('#unitOfMeasurement').empty();
			$('#unitOfMeasurement').append($("<option value=''>Select</option>"));
			$.each(response.Result, function(index, value) {
			     $('#unitOfMeasurement').append("<option value="+value.uomId+">"+value.uomName+"</option>");
			     $('#rateType').val(value.rateType);
			});
			console.log("completed"+response);
			$("#unitOfMeasurement").prop("selectedIndex",1);
		}, 
		error: function (response) {
			console.log("failed"+response);
		} 
	});
});
	
$('#licenseCategory').change(function(){
		console.log("came onchange of "+$('#licenseCategory').val());
		$('#feeType').empty();
		$('#feeType').append($("<option value=''>Select</option>"));
		$('#unitOfMeasurement').empty();
		$('#unitOfMeasurement').append($("<option value=''>Select</option>"));
		$('#rateType').val("");
		$.ajax({
			url: "/tl/domain/commonAjax-ajaxPopulateSubCategory.action?categoryId="+$('#licenseCategory').val()+"",
			type: "GET",
			dataType: "json",
			success: function (response) {
				console.log("success"+JSON.stringify(response) );
				$('#subCategory').empty();
				
				$('#subCategory').append($("<option value=''>Select</option>"));
				$.each(response.Result, function(index, value) {
					
				     $('#subCategory').append("<option value="+value.Value+">"+value.Text+"</option>");
				});
				console.log("completed"+response);
				
			}, 
			error: function (response) {
				console.log("failed");
			}
		});
	});

});

/*$( "#add-row" ).click(function( event ) {
	bootbox.alert( "add-row event called." );
	  $(this).closest("tr").remove(); // remove row
	    return false;
});*/

$( "#add-row" ).click(function( event ) {
	var rowCount = $('#result tr').length;
	if(!checkforNonEmptyPrevRow())      
		return false;
	var prevUOMFromVal=getPrevUOMFromData();
	var content= $('#resultrow0').html();
	resultContent=   content.replace(/0/g,rowCount-1);   
	$(resultContent).find("input").val("");
	$('#result > tbody:last').append("<tr>"+resultContent+"</tr>"); 
	$('#result tr:last').find("input").val("");   
	intiUOMFromData(prevUOMFromVal);  
	patternvalidation(); 
});    

$( "#save" ).click(function( event ) {
if(!validateDetailsBeforeSubmit()){
	return false;
} 
	
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
				console.log("success"+response );
				 $('#resultdiv').html(response);
				 if(natureOfBusinessDisabled)
					 $('#natureOfBusiness').attr("disabled", true); 
				 if(licenseAppTypeDisabled)
					 $('#licenseAppType').attr("disabled", true); 
				 if(unitOfMeasurementDisabled)
					 $('#unitOfMeasurement').attr("disabled", true); 
				 bootbox.alert("Details saved Successfully");
			}, 
			error: function (response) {
				console.log("failed");
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

});
