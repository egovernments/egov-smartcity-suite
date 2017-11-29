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

jQuery(document).ready(function($){
	var rowLength = $('#new-row-table tr').length;
	var toVolume = $('#toVolume').val();
	for(var i = 0 ; i<=(rowLength-2); i++){
		if(toVolume!="" && toVolume!=undefined) {
			$('#ratesDetail\\['+(i)+'\\]\\.recursive').attr('disabled', true);
			$('#ratesDetail\\['+(i)+'\\]\\.recursiveFactor').attr('disabled', true);
			$('#ratesDetail\\['+(i)+'\\]\\.recursiveAmount').attr('disabled', true);
		}
	}
		
	var resultDataTable;
	jQuery("#createSearch").click(function(e){
		if($("form").valid()){
			var url = '/wtms/masters/metered-rate-create/'+ $('#slabName').val();
			$('#meteredRatesMasterform').attr('method', 'get');
			$('#meteredRatesMasterform').attr('action', url);
			document.forms["meteredRatesMasterform"].submit();
		}
		
	});
	
	$('#saveButton').on('click', function(){
		var rowLength = $('#new-row-table tr').length;

		for(var i = 0 ; i<=(rowLength-2); i++){
			var rateAmt = $('#ratesDetail\\['+(i)+'\\]\\.rateAmount').val();
			var flatAmt = $('#ratesDetail\\['+(i)+'\\]\\.flatAmount').val();
			var recursive = $('#ratesDetail\\['+(i)+'\\]\\.recursive').val();
			var recursiveFactor = $('#ratesDetail\\['+(i)+'\\]\\.recursiveFactor').val();
			var recursiveAmt = $('#ratesDetail\\['+(i)+'\\]\\.recursiveAmount').val();
			var fromDate = $('#ratesDetail\\['+(i)+'\\]\\.fromDate').val();
			var toDate = $('#ratesDetail\\['+(i)+'\\]\\.toDate').val();
			if((rateAmt === undefined && flatAmt===undefined) || (rateAmt==="" && flatAmt==="") 
					||(IsNumeric(rateAmt) && IsNumeric(flatAmt))){
				bootbox.alert("Enter either Rate Amount or Flat Amount");
				return false;
			}
			
			if(fromDate==="" || fromDate===undefined){
				bootbox.alert("From Date is Mandatory");
				return false;
			}
			else if(rowLength>=2){
				
				var fromDateNewRow = $('#ratesDetail\\['+(i)+'\\]\\.fromDate').val();
				var fromDatePrevRow = $('#ratesDetail\\['+(i-1)+'\\]\\.fromDate').val();
				var toDatePrevRow = $('#ratesDetail\\['+(i-1)+'\\]\\.toDate').val();
				
				var result = compareDate (fromDate, toDate);
				if(result == -1){
					bootbox.alert("To Date "+toDate+" can not be less than From Date "+fromDate);
					return false;
				}
				
				if(recursive==="true" && (recursiveFactor==="" && recursiveAmt==="") ){
					bootbox.alert("Please Enter Recursive Factor and Recursive Amount for the row with from date "+fromDate);
					return false;
				}
				else if (recursive==="true" && recursiveFactor===""){
					bootbox.alert("Please enter recursive factor for the row with from date "+fromDate);
					return false;
				}
				else if(recursive==="true" && recursiveAmt===""){
					bootbox.alert("Please enter recursive amount for the row with from date "+fromDate);
					return false;
				}	
				else if ((recursive==="false" && recursiveAmt!="") || (recursive==="false" && recursiveFactor!="")){
					bootbox.alert("Please check the recursive checkbox for the row with from date "+fromDate);
					return false;
				}
				if(fromDateNewRow!=undefined && fromDateNewRow!="" && fromDatePrevRow!=undefined){
					var dateResultVal = compareDate (fromDatePrevRow, fromDateNewRow);
					if(dateResultVal === -1){
						bootbox.alert("From Date "+fromDateNewRow+" can not be less than "+fromDatePrevRow);
						return false;
					}
					else if (dateResultVal === 0){
						bootbox.alert("From Date "+fromDatePrevRow+" already exists. Please select next date");
						return false;
					}
				}
				if(toDatePrevRow==="" && fromDateNewRow!=undefined){
					var toDate1=fromDateNewRow.split("/").reverse().join("/");
					var toDate2 = new Date(toDate1);
					toDate2.setDate(toDate2.getDate()-1);
					var newToDate = (toDate2.getDate())+"/"+(toDate2.getMonth()+1)+"/"+toDate2.getFullYear();
					if(compareDate(fromDatePrevRow, fromDateNewRow) == 0){
						bootbox.alert("Date "+fromDatePrevRow+" already present. Please choose different date")
					}
					if(newToDate!=undefined)
						$('#ratesDetail\\['+(rowLength-3)+'\\]\\.toDate').val(newToDate);
				}
				else if(toDatePrevRow!=undefined && fromDateNewRow!=undefined){
					var toDate3 = toDatePrevRow.split("/").reverse().join("/");
					var toDate4 = new Date(toDate3);
					toDate4.setDate(toDate4.getDate()+1);
					var prevToDate = toDate4.getDate()+"/"+(toDate4.getMonth()+1)+"/"+toDate4.getFullYear();
					var validResult = compareDate(prevToDate, fromDateNewRow);
					if(validResult === -1){
						bootbox.alert("From date "+fromDateNewRow+" should not be less than  "+prevToDate);
						return false;
					}
					if(validResult === 1){
						bootbox.alert("There is date gap between dates "+toDatePrevRow+" and "+fromDateNewRow);
						return false;
					}
				}
			}

		}

		return true;

	});
	
	var rowLength = $('#new-row-table tr').length;
		$('#ratesDetail\\['+(rowLength-2)+'\\]\\.toDate').attr('disabled', false);
	
});


jQuery('#addnewid').on('click',function(e){
	window.open('/wtms/masters/metered-rate-create', '_self');
});

function checkAmountExists(){
	var rateAmount = $('#rateAmount').val();
	var flatAmount = $('#flatAmount').val();
	if((rateAmount=="" && flatAmount=="") || (rateAmount!="" && flatAmount!="")){
		bootbox.alert("Enter Either Rate Amount or Flat Amount");
		return false;
	}
	return true;
}


function addNewRowValue(obj){
	var toVolume = $('#toVolume').val();	
	var rowLength = $('#new-row-table tr').length;
	
	for(var i = 0 ; i<=(rowLength-2); i++){
		var rateAmt = $('#ratesDetail\\['+(i)+'\\]\\.rateAmount').val();
		var flatAmt = $('#ratesDetail\\['+(i)+'\\]\\.flatAmount').val();
		var recursive = $('#ratesDetail\\['+(i)+'\\]\\.recursive').val();
		var recursiveFactor = $('#ratesDetail\\['+(i)+'\\]\\.recursiveFactor').val();
		var recursiveAmt = $('#ratesDetail\\['+(i)+'\\]\\.recursiveAmount').val();
		var fromDate = $('#ratesDetail\\['+(i)+'\\]\\.fromDate').val();
		var toDate = $('#ratesDetail\\['+(i)+'\\]\\.toDate').val();
		
		if((rateAmt === undefined && flatAmt===undefined) || (rateAmt==="" && flatAmt==="") 
				||(IsNumeric(rateAmt) && IsNumeric(flatAmt))){
			bootbox.alert("Enter either Rate Amount or Flat Amount");
			return false;
		}
		
		if(fromDate==="" || fromDate===undefined){
			bootbox.alert("From Date is Mandatory");
			return false;
		}
		else if(rowLength>2){
			
			var fromDateNewRow = $('#ratesDetail\\['+(i)+'\\]\\.fromDate').val();
			var fromDatePrevRow = $('#ratesDetail\\['+(i-1)+'\\]\\.fromDate').val();
			var toDatePrevRow = $('#ratesDetail\\['+(i-1)+'\\]\\.toDate').val();
			
			var result = compareDate (fromDate, toDate);
			if(result == -1){
				bootbox.alert("To Date "+toDate+" can not be less than From Date "+fromDate);
				return false;
			}
			
			if(recursive==="true" && (recursiveFactor==="" && recursiveAmt==="") ){
				bootbox.alert("Please Enter Recursive Amount and Recursive Factor for the row with from date "+fromDate);
				return false;
			}
			else if (recursive==="true" && recursiveFactor===""){
				bootbox.alert("Please enter recursive factor for the row with from date "+fromDate);
				return false;
			}
			else if(recursive==="true" && recursiveAmt===""){
				bootbox.alert("Please enter recursive amount for the row with from date "+fromDate);
				return false;
			}	
			else if (recursive==="false" && recursiveAmt!=""){
				bootbox.alert("Please check the recursive checkbox for the row with from date "+fromDate);
				return false;
			}
			else if (recursive==="false" && recursiveFactor!=""){
				bootbox.alert("Please check the recursive checkbox for the row with from date "+fromDate);
				return false;
			}
	
			var rowLen = $('#new-row-table tr').length;
			if(fromDateNewRow!=undefined && fromDateNewRow!="" && fromDatePrevRow!=undefined){
				var dateResultVal = compareDate (fromDatePrevRow, fromDateNewRow);
				if(dateResultVal === -1){
					bootbox.alert("From Date "+fromDateNewRow+" can not be less than "+fromDatePrevRow);
					return false;
				}
				else if (dateResultVal === 0){
					bootbox.alert("From Date "+fromDatePrevRow+" already exists. Please select next date");
					return false;
				}
			}
			if(toDatePrevRow==="" && fromDateNewRow!=undefined){
				var toDate1=fromDateNewRow.split("/").reverse().join("/");
				var toDate2 = new Date(toDate1);
				toDate2.setDate(toDate2.getDate()-1);
				var newToDate = (toDate2.getDate())+"/"+(toDate2.getMonth()+1)+"/"+toDate2.getFullYear();
				if(compareDate(fromDatePrevRow, fromDateNewRow) == 0){
					bootbox.alert("Date "+fromDatePrevRow+" already present. Please choose different date")
				}
				if(newToDate!=undefined)
					$('#ratesDetail\\['+(rowLength-3)+'\\]\\.toDate').val(newToDate);
			}
			else if(toDatePrevRow!=undefined && fromDateNewRow!=undefined){
				var toDate3 = toDatePrevRow.split("/").reverse().join("/");
				var toDate4 = new Date(toDate3);
				toDate4.setDate(toDate4.getDate()+1);
				var prevToDate = toDate4.getDate()+"/"+(toDate4.getMonth()+1)+"/"+toDate4.getFullYear();
				var validResult = compareDate(prevToDate, fromDateNewRow);
				if(validResult == -1){
					bootbox.alert("From date "+fromDateNewRow+" should not be less than  "+prevToDate);
					return false;
				}
				if(validResult == 1){
					bootbox.alert("There is date gap between dates "+toDatePrevRow+" and "+fromDateNewRow);
					return false;
				}
			}
		}

	}
	
		
		if (document.getElementById('meteredRateRow') != null) {
			// get Next Row Index to Generate
			var nextIdx = 0;
			nextIdx = $("#new\\-row\\-table tbody tr").length;
			
			// Generate all textboxes Id and name with new index
			$("#meteredRateRow").clone().find("input, errors").each(
					function() {

						if ($(this).data('server')) {
							$(this).removeAttr('data-server');
						}
						
							$(this).attr(
									{
										'name': function(_, name) {
									    	  if(!(jQuery(this).attr('name')===undefined))
									    		  return name.replace(/\[.\]/g, '['+ nextIdx +']'); 
									      },
									      'id': function(_, id) {
									    	  if(!(jQuery(this).attr('id')===undefined))
									    		  return id.replace(/\[.\]/g, '['+ nextIdx +']'); 
									      },
									      'class' : function(_, name) {
												if(!(jQuery(this).attr('class')===undefined))
													return name.replace(/\[.\]/g, '['+ nextIdx +']'); 
											},
										'data-idx' : function(_,dataIdx)
										{
											return nextIdx;
										}
									});

							// if element is static attribute hold values for
							// next row, otherwise it will be reset
							if (!$(this).data('static')) {
								$(this).val('');
								// set default selection for dropdown
								if ($(this).is("select")) {
									$(this).prop('selectedIndex', 0);
								}
							}
							
							$(this).removeAttr('disabled');
							$(this).removeAttr('readonly');
							$(this).prop('checked', false);

					}).end().appendTo("#new\\-row\\-table tbody");
			$("#new\\-row\\-table").find('tr td:last').append($('<button type="button" id="deleteRow" class="btn btn-primary delete-button" onclick="removeRow(this);">Delete Row</button>'));
			
			if($(this).attr('checked')){
				$('#ratesDetail\\['+nextIdx+'\\]\\.recursive').val(true);
				$(this).data('checked',true);
			}
			else{
				$('#ratesDetail\\['+nextIdx+'\\]\\.recursive').val(false);
			}
			
			if(fromDate!="" && toDate==""){
				var date=new Date();
				var currentDate = date.getDate() + "/" + (date.getMonth()+1) +"/"+ date.getFullYear();
				nextDate = (date.getDate()+1) + "/" + (date.getMonth()+1) +"/"+ date.getFullYear();
				

				var result = compareDate (fromDate, currentDate);
				if(result!=-1){
					$('#ratesDetail\\['+(i-1)+'\\]\\.toDate').val(currentDate);
					$('#ratesDetail\\['+i+'\\]\\.fromDate').val(nextDate);
				}
			
			}
			else if(fromDate!="" && toDate!="" && toDate!=undefined){
					var dateArray = toDate.split("/").reverse().join("/");
					var newToDate = new Date(dateArray);
					newToDate.setDate(newToDate.getDate()+1);
					var nextToDate = newToDate.getDate()+"/"+(newToDate.getMonth()+1)+"/"+newToDate.getFullYear();
					$('#ratesDetail\\['+i+'\\]\\.fromDate').val(nextToDate);
					
			}
		
			if(toVolume!="" && toVolume!=undefined) {
				$('#ratesDetail\\['+(i)+'\\]\\.recursive').attr('disabled', true);
				$('#ratesDetail\\['+(i)+'\\]\\.recursiveFactor').attr('disabled', true);
				$('#ratesDetail\\['+(i)+'\\]\\.recursiveAmount').attr('disabled', true);
			}
						
			//reinitialise datepicker fields
			jQuery(".datepicker").datepicker({
				format : 'dd/mm/yyyy',
				autoclose : true
			});
		}
	
}



function removeRow(obj) {
    var rIndex = getRow(obj).rowIndex;
	var tbl=document.getElementById('new-row-table');	
	var rowcount=$("#new-row-table tbody tr").length;

    if(rowcount<=1) {
		//bootbox.alert("This row can not be deleted");
    	$('.row-section').attr('class','row-section display-hide');
    	$('#addRow').show();
    	return false;
	} else {
		tbl.deleteRow(rIndex);
		//starting index for table fields
		var idx= 0;
		//regenerate index existing inputs in table row
		jQuery("#new-row-table tbody tr").each(function() {
		
				jQuery(this).find("input, select, textarea, errors, span, input:hidden").each(function() {
					jQuery(this).attr({
					      'name': function(_, name) {
					    	  if(!(jQuery(this).attr('name')===undefined))
					    		  return name.replace(/\[.\]/g, '['+ idx +']'); 
					      },
					      'id': function(_, id) {
					    	  if(!(jQuery(this).attr('id')===undefined))
					    		  return id.replace(/\[.\]/g, '['+ idx +']'); 
					      },
					      'class' : function(_, name) {
								if(!(jQuery(this).attr('class')===undefined))
									return name.replace(/\[.\]/g, '['+ idx +']'); 
							},
						  'data-idx' : function(_,dataIdx)
						  {
							  return idx;
						  }
					   });
			    });
				
				idx++;
		});
		
		var rowLen = $('#new-row-table tr').length;
		var lastRowToDate = $('#ratesDetail\\['+(rowLen-2)+'\\]\\.toDate').val();
		if(!$('#ratesDetail\\['+(rowLen-2)+'\\]\\.toDate').prop("disabled")){
			$('#ratesDetail\\['+(rowLen-2)+'\\]\\.toDate').val("");
		}
		return true;
	}	
}

function getRow(obj) {
	if(!obj)return null;
	tag = obj.nodeName.toUpperCase();
	while(tag != 'BODY'){
		if (tag == 'TR') return obj;
		obj=obj.parentNode ;
		tag = obj.nodeName.toUpperCase();
	}
	return null;
}



function IsNumeric(sText)
{
	var ValidChars = "0123456789.";
	var IsNumber=true;
	var Char;
	if(sText.length==0){
		return false;
	}
	for (i = 0; i < sText.length && IsNumber == true; i++) 
	{ 
	    Char = sText.charAt(i); 
	    if (ValidChars.indexOf(Char) == -1) 
	    {
	       IsNumber = false;
	    }
	}
	return IsNumber;
}

function changeRecursive(obj){
	if(obj.dataset.idx==undefined)
		obj.dataset.idx=0;
	if($(obj).is(':checked'))
		$('#ratesDetail\\['+(obj.dataset.idx)+'\\]\\.recursive').val(true);
	else
		$('#ratesDetail\\['+(obj.dataset.idx)+'\\]\\.recursive').val(false);
}


$('#search').on('click', function(){
	if($("form").valid()){
		var slabName = $('#slabName').val();
			var url = '/wtms/masters/metered-rate-search/'+ slabName;
			$('#meteredRatesSearchform').attr('method', 'get');
			$('#meteredRatesSearchform').attr('action', url);
			document.forms["meteredRatesSearchform"].submit();
	}
});

