
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
$('#licenseCategory').change(function(){
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
		success: function (response) {
			var subCategory = $('#subCategory')
			subCategory.find("option:gt(0)").remove();
			$.each(response, function(index, value) {
				subCategory.append($('<option>').text(value.name).attr('value', value.id));
			});
			
		}, 
	
	})
});
$( "#add-row" ).click(function() {
	var rowCount = $('#result tr').length;
	if(!checkforNonEmptyPrevRow())      
		return false;
	var prevUOMFromVal=getPrevUOMFromData();
	var content= $('#resultrow0').html();
	var resultContent=   content.replace(/0/g,rowCount-1);   
	$(resultContent).find("input").val("");
	$('#result > tbody:last').append("<tr>"+resultContent+"</tr>"); 
	$('#result tr:last').find("input").val("");   
	intiUOMFromData(prevUOMFromVal);  
	patternvalidation(); 
});    

$( "#save" ).click(function() {
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
});
function checkValue(obj){
	var rowobj=getRow(obj);
	var tbl = document.getElementById('result');
	var uomToval=getControlInBranch(tbl.rows[rowobj.rowIndex],'uomTo').value;
	var uomFromval=getControlInBranch(tbl.rows[rowobj.rowIndex],'uomFrom').value;
	if(uomFromval!='' && uomToval!='' && (JSON.parse(uomFromval)>=JSON.parse(uomToval))){
		bootbox.alert("\"UOM To\" should be greater than \"UOM From\".");
		getControlInBranch(tbl.rows[rowobj.rowIndex],'uomTo').value="";
		return false;
	  }
  	var lastRow = (tbl.rows.length)-1;
    var curRow=rowobj.rowIndex; 
    if(curRow!=lastRow){
		var uomFromVal1=getControlInBranch(tbl.rows[rowobj.rowIndex+1],'uomFrom').value;
		if(uomToval!=uomFromVal1)
			getControlInBranch(tbl.rows[rowobj.rowIndex+1],'uomFrom').value=uomToval; 
    }
}

function checkforNonEmptyPrevRow(){
	var tbl=document.getElementById("result");
    var lastRow = (tbl.rows.length)-1;
    var uomFromval=getControlInBranch(tbl.rows[lastRow],'uomFrom').value;
    var uomToval=getControlInBranch(tbl.rows[lastRow],'uomTo').value;
    var amountVal=getControlInBranch(tbl.rows[lastRow],'amount').value;
    if(uomFromval=='' || uomToval=='' || amountVal==''){
    	bootbox.alert("Enter all values for existing rows before adding.");
		return false;       
    } 
    return true;
}  

function getPrevUOMFromData(){
	var tbl=document.getElementById("result");
    var lastRow = (tbl.rows.length)-1;
    return getControlInBranch(tbl.rows[lastRow],'uomTo').value;
}

function intiUOMFromData(obj){
	var tbl=document.getElementById("result");
    var lastRow = (tbl.rows.length)-1;
    getControlInBranch(tbl.rows[lastRow],'uomFrom').value=obj;
} 

function deleteThisRow(obj){
	var tbl=document.getElementById("result");
    var lastRow = (tbl.rows.length)-1;
    var curRow=getRow(obj).rowIndex; 
    if(curRow == 1)	{
    	bootbox.alert('Cannot delete first row');
  	     return false;
    } else if(curRow != lastRow){
    	bootbox.alert('Cannot delete in between. Delete from last.');
 	    return false;
    } else	{
        if(getControlInBranch(tbl.rows[lastRow],'detailId').value==''){
	  	  	tbl.deleteRow(curRow);
			return true;
	    } 
        else if(getControlInBranch(tbl.rows[lastRow],'detailId').value!='')
        {
	    bootbox.confirm("This will delete the row permanently. Press OK to Continue. ",
			function(r) {
				if (r) {
					$.ajax({
						url: "/tl/domain/commonAjax-deleteFee.action?feeMatrixDetailId="+getControlInBranch(tbl.rows[lastRow],'detailId').value+"",
						type: "GET",
						dataType: "json",
						success: function () {
							tbl.deleteRow(curRow);
						}, 
						error: function () {
							bootbox.alert("Unable to delete this row.");
							}
					});
			   }
		   });
  	   }
    }
}

function validateDetailsBeforeSubmit(){
	var tbl=document.getElementById("result");
    var tabLength = (tbl.rows.length)-1;
    var uomFromval,uomToval,amt;
    for(var i=1;i<=tabLength;i++){
    	uomFromval=getControlInBranch(tbl.rows[i],'uomFrom').value;
    	uomToval=getControlInBranch(tbl.rows[i],'uomTo').value;
    	amt=getControlInBranch(tbl.rows[i],'amount').value;
    	if(uomFromval=="" || uomToval=="" ||  amt == "" && i!=tabLength){
    		bootbox.alert("\"UOM To\" or \"UOM From\" or \"Amount\" cannot be null for the row "+(i)+".");
    		return false;
    	}
    	if(uomFromval!='' && uomToval!='' && (JSON.parse(uomFromval)>=JSON.parse(uomToval))){
    		bootbox.alert("\"UOM To\" should be greater than \"UOM From\" for row "+(i)+".");
    		getControlInBranch(tbl.rows[i],'uomTo').value="";
    		getControlInBranch(tbl.rows[i],'uomTo').focus();
    		return false;
    	}  
    }
    return true;
}
